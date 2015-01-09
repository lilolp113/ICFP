package com.icfp.frame.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.icfp.frame.biz.CoreBiz;
import com.icfp.frame.entity.ZA02;
import com.icfp.frame.lisence.License;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.params.SysParamsList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.io.IOException;

public class CoreAction extends BaseAction {

	private static final long serialVersionUID = 3908189360358122822L;

	private static Logger logger = Logger.getLogger(CoreAction.class);

	private CoreBiz coreBiz;
	
	private Object[] json;
	
	private String ftlpath;
	
	private List<Object> ftlLists = new ArrayList<Object>();
	
	public Object getJson() {
		if(null!=json && json.length>0){
			Object jjson = json[0];
			return jjson;
		}else{
			return null;
		}
	}

	public void setJson(Object[] json) {
		this.json = json;
	}

	public String getFtlpath() {
		return ftlpath;
	}

	public void setFtlpath(String ftlpath) {
		this.ftlpath = ftlpath;
	}
	
	public List<Object> getFtlLists() {
		return ftlLists;
	}

	public void setFtlLists(List<Object> ftlLists) {
		this.ftlLists = ftlLists;
	}

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		logger.info("CoreAction 执行开始  >>> >>> >>>");
		ResponseEnvelope responseEnvelope = new ResponseEnvelope();
		String mode = "0";
		if(License.FLAG) {
			logger.error("CoreAction 许可证过期，请联系供应商更新！");
			responseEnvelope.getHeader().setAppCode(RiaParamList.CODE_EXPIRED);
		}else {
			logger.info("CoreAction 许可证加载正常");
			RequestEnvelope rqev = new RequestEnvelope(this.getJson());
			rqev.getBody().setHttpSession(request.getSession());
			rqev.getBody().setCookie(request.getCookies());
			Iterator tempIterator = request.getParameterMap().entrySet().iterator();
			boolean blSepParam = false;
			String requestId = "";
			String bizId = "";
			String method = "";
			while(tempIterator.hasNext()){
				blSepParam = false;
				Entry tempEntry= (Entry)tempIterator.next();
			    String ParameterName = (String)tempEntry.getKey();
			    if (ParameterName.equals(RiaParamList.REQUESTID)){
			    	requestId = request.getParameter(RiaParamList.REQUESTID);
			    	blSepParam=true;
			    }
			    if (ParameterName.equals(RiaParamList.BIZID)) {
			    	bizId = request.getParameter(RiaParamList.BIZID);
			    	blSepParam=true;
			    }
			    if (ParameterName.equals(RiaParamList.METHOD)){
			    	method = request.getParameter(RiaParamList.METHOD);
			    	blSepParam=true;
			    }
			    if (ParameterName.equals("mode")){
			    	mode = request.getParameter("mode");
			    	blSepParam=true;
			    }
			    if(!blSepParam){
			    	logger.info("交互参数中出现特殊参数[" + ParameterName + "=" + request.getParameter(ParameterName) + "]");
			    }
			    rqev.getBody().addParameter(ParameterName, request.getParameter(ParameterName));
			}
			if(!"REQ_S_001_01".equals(requestId) && !"REQ_Q_001_01".equals(requestId)){
				if (request.getSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME) == null) {
					this.setFtlpath("WEB-INF/template/frame/error/ChaoShi.html");
					return FREEMARKER;
				}
			}
			logger.info("Request参数：交互编号[" + requestId + "]，业务编号[" + bizId + "]，Method[" + method + "]");
			if(requestId == null || requestId.trim().equals("")) {
				logger.error("交互编号为空");
				responseEnvelope.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
			}else {
				responseEnvelope = invoke(requestId, request, rqev);
			}
		}
		if(responseEnvelope == null) {
			logger.error("返回值ResponseEnvelope为空");
			responseEnvelope.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
		}
		if("0".equals(mode) && "0".equals(responseEnvelope.getMode())){
			logger.info("CoreAction 交互模式 :正常模式   >>> >>> >>> \n");
			String rspsStr = responseEnvelope.toResponseString();
			logger.debug("Response输出数据：" + rspsStr);
			logger.info("Response输出数据：" + rspsStr);
			try {
				HttpServletResponse response = super.getResponse();
				response.getWriter().println(rspsStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("CoreAction 执行结束！ <<< <<< <<< \n");
			return null;
		}else if("1".equals(mode) && "1".equals(responseEnvelope.getMode())){
			logger.info("CoreAction 交互模式 :模版模式   >>> >>> >>> \n");
			this.setFtlpath(responseEnvelope.getUrlPath());
			String APP_PATH = request.getContextPath();
			responseEnvelope.getBody().addParameter("APP_PATH", APP_PATH);
			List<Object> ll = new ArrayList<Object>();
			ll.add(responseEnvelope.getBody().getAllParameters());
			this.setFtlLists(ll);
			logger.info("CoreAction 执行结束！ <<< <<< <<< \n");
			return FREEMARKER;
		}else{
			return ERROR;
		}
	}
	
	@SuppressWarnings("unchecked")
	private ResponseEnvelope invoke(String bizId, HttpServletRequest request,RequestEnvelope rqev) {
		ResponseEnvelope responseEnvelope = null;
		logger.info("读取交互编号[" + bizId + "] 信息 ... ");
		ZA02 za02 =(ZA02)coreBiz.findByBizId(bizId);
		if (za02 == null) {
			logger.error("交互编号[" + bizId + "]不存在！");
		} else {
			String bizName = za02.getZAA004(); // userService
			String method = za02.getZAA005(); // add
			logger.info("读取交互编号[" + bizId + "] 信息成功！ 交互类：[" + bizName + "]，交互方法：[" + method + "]");
			ServletContext servletContext = super.getServletContext(request);
			logger.info("读取Spring配置文件 ... ");
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			if (wac == null) {
				logger.info("读取Spring配置文件失败！");
			} else {
				logger.info("读取Spring配置文件成功！");
				Object biz = wac.getBean(bizName);
				if (biz == null) {
					logger.info("Spring注入交互类[" + bizName + "]失败！");
				} else {
					logger.info("Spring注入交互类[" + bizName + "]成功");
					String className = biz.getClass().getName(); // com.pysf.sys.biz.impl.UserServiceImpl// 代理类
					try {
						Class c = Class.forName(className);
						Class[] parameterTypes = { RequestEnvelope.class };
						Object arglist[] = { rqev };
						Method m = c.getMethod(method, parameterTypes);
						logger.info("业务方法[" + method + "]执行开始  >>> >>> ");
						responseEnvelope = (ResponseEnvelope) m.invoke(biz,arglist);
						logger.info("业务方法[" + method + "]执行结束  <<< <<< ");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return responseEnvelope;
	}

	public CoreBiz getCoreBiz() {
		return coreBiz;
	}

	public void setCoreBiz(CoreBiz coreBiz) {
		this.coreBiz = coreBiz;
	}
	
}
