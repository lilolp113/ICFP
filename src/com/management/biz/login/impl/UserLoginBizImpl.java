package com.management.biz.login.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.icfp.achievement.AchievementParams;
import com.icfp.achievement.entity.ZD03;
import com.icfp.frame.biz.impl.BaseManagerImpl;
import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;
import com.icfp.frame.util.Md5PwdEncoder;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.params.SysParamsList;
import com.management.biz.login.UserLoginBiz;
import com.management.entity.SA05;
import com.management.util.Lunar;

/**
 * 类UserLoginBizImpl.java的实现描述： 登录实现
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-17
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class UserLoginBizImpl extends BaseManagerImpl implements UserLoginBiz {

	private CoreDao coreDao;
	
	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}
	
	/**
	 * 显示登陆页
	 * @param revp
	 * @return
	 * @see com.management.biz.login.UserLoginBiz#ShowLoginFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope ShowLoginFtl(RequestEnvelope revp) {
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.getBody().addParameter("title","大鲸集团管理系统平台1.0");
		renp.setModeFreeMaker("management/login/login.html");
		return renp;
	}
	
	/**
	 * 显示武刚测试登录页
	 * @param revp
	 * @return
	 * @see com.management.biz.login.UserLoginBiz#ShowTestLoginFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope ShowTestLoginFtl(RequestEnvelope revp){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.getBody().addParameter("title","大鲸集团管理系统平台1.0");
		renp.setModeFreeMaker("management/login/wglogin.html");
		return renp;
	}
	
	/**
	 * 显示修改密码模板页
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope showChangePWFtl(RequestEnvelope revp){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.setModeFreeMaker("management/mainframe/changepassword.html");
		return renp;
	}
	
	/**
	 * 修改密码
	 * @param revp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResponseEnvelope ChangePassWord(RequestEnvelope revp){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			String key1 = (String)revp.getBody().getParameter("key1");
			String key3 = (String)revp.getBody().getParameter("key3");
			if(key1!=null && !"".equals(key1)){
				Md5PwdEncoder md5 =new Md5PwdEncoder();
				SA05 sa05 = (SA05)super.getLoginUser(revp);
				List lists = this.coreDao.executeQueryByClass("select * from sa05 where SAC001 = '" + sa05.getSAC001() + "' and SAC003 = '" + md5.encodePassword(key1) + "'",SA05.class);
				if(lists!=null && lists.size()>0 && lists.get(0)!=null){
					if(key3!=null && !"".equals(key3)){
						sa05.setSAC003(md5.encodePassword(key3));
						this.coreDao.update(sa05);
						renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
						renp.getHeader().setDetailMsg("成功修改密码，请重新登录！");
						renp.getBody().addParameter("tag", "1");
					}
				}else{
					renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
					renp.getBody().addParameter("tag", "0");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("修改密码失败！");
		}
		return renp;
	}
	
	/**
	 * 校验账户
	 * @param revp
	 * @return
	 * @see com.management.biz.login.UserLoginBiz#checkLogin(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	@SuppressWarnings("unchecked")
	public ResponseEnvelope checkLogin(RequestEnvelope revp) {
		ResponseEnvelope renp=new ResponseEnvelope();
		try {
			String rancode = (String)revp.getBody().getParameter("rancode");
			String vrancode = (String)revp.getBody().getHttpSession().getAttribute("RandStr");
			String username = (String)revp.getBody().getParameter("username");
			Md5PwdEncoder m = new Md5PwdEncoder();
			String password = m.encodePassword((String)revp.getBody().getParameter("password"));
			if(rancode.equals(vrancode)){
				//String sql="SELECT a.* FROM sa05 a LEFT JOIN ca01 b ON a.SAC006 = b.CAA001 WHERE (b.CAA006 = '01' OR b.CAA006 IS null) and a.SAC002='"+username+"' and a.SAC003='"+password+"'";
				String sql="SELECT a.* FROM sa05 a where a.SAC002='"+username+"' and a.SAC003='"+password+"'";
				List list=coreDao.executeQueryByClass(sql,SA05.class);
				if(list!=null && list.size()>0){
					SA05 sa05 = (SA05)list.get(0);
					revp.getBody().getHttpSession().setAttribute(SysParamsList.LOGIN_SESSION_NAME, sa05);
					revp.getBody().getHttpSession().setAttribute(SysParamsList.LOGIN_SESSION_USERTYPE, "0");
					renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
					renp.getBody().addParameter("loginflag", "2");
					renp.getBody().addParameter("userid",sa05.getSAC001());
				}else{
					renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
					renp.getBody().addParameter("loginflag", "0");
				}
			}else if(rancode!=null && "wugang".equals(rancode)){
				String sql="select * from SA05 u where u.SAC002 = '"+username+"' and u.SAC003 ='"+password+"'";
				List list = coreDao.executeQueryByClass(sql,SA05.class);
				if(list.size()>0){
					SA05 sa05 = (SA05)list.get(0);
					revp.getBody().getHttpSession().setAttribute(SysParamsList.LOGIN_SESSION_NAME, sa05);
					revp.getBody().getHttpSession().setAttribute(SysParamsList.LOGIN_SESSION_USERTYPE, "0");
					renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
					renp.getBody().addParameter("loginflag", "2");
					renp.getBody().addParameter("userid",sa05.getSAC001());
				}else{
					renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
					renp.getBody().addParameter("loginflag", "0");
				}
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
				renp.getBody().addParameter("loginflag", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("用户登录失败！");
		}
		return renp;
	}
	
	/**
	 * 显示主页面
	 * @param revp
	 * @return
	 * @see com.management.biz.login.UserLoginBiz#ShowMainFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope ShowMainFtl(RequestEnvelope revp) {
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.getBody().addParameter("title","大鲸集团管理系统平台1.0");
		String cid = (String)revp.getBody().getParameter("_cid");
		if(cid!=null && !"".equals(cid)){
			renp.getBody().addParameter("_cid",cid);
		}
		Date cdate = super.getSysDate();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat Chinadf = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar today = Calendar.getInstance();
		today.setTime(cdate);
		Lunar lunar = new Lunar(today);
		renp.getBody().addParameter("sysdatestr",Chinadf.format(cdate));
		renp.getBody().addParameter("nolistr",lunar);
		renp.getBody().addParameter("sysdate",df.format(cdate));
		renp.getBody().addParameter("roleid",super.getLoginUserRoleID(revp));
		renp.getBody().addParameter("username",super.getLoginUserName(revp));
		renp = loadAchievement(revp, renp);//加载成就系统数据
		renp.setModeFreeMaker("management/mainframe/main.html");
		return renp;
	}
	
	/**
	 * 加载成就系统数据
	 * @param revp 
	 * by wangxing @2013-06-21
	 */
	@SuppressWarnings("unchecked")
	private ResponseEnvelope loadAchievement(RequestEnvelope revp, ResponseEnvelope renp) {
		String sac001 = super.getLoginUserID(revp);
		String saa001 = super.getLoginUserRoleID(revp);
		
		int grade = 1;
		ZD03 zd03 = null;
		List zd03s = coreDao.find("from ZD03 where SAC001 = ?", sac001);
		if(zd03s!=null && zd03s.size()>0 && zd03s.get(0)!=null) {
			zd03 = (ZD03) zd03s.get(0);
			grade = zd03.getZDA003();
		}else {
			zd03 = new ZD03();
			zd03.setSAC001(sac001);
			zd03.setSAA001(saa001);
			zd03.setZDA005(0);
			zd03.setZDA003(1);
			coreDao.save(zd03);
		}

		renp.getBody().addParameter(AchievementParams.GRADE, grade);//等级	
		return renp;
	}
	
	/**
	 * 显示主操作界面
	 * @param revp
	 * @return
	 * @see com.management.biz.login.UserLoginBiz#ShowRightFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope ShowRightFtl(RequestEnvelope revp){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.getBody().addParameter("title","大鲸集团管理系统平台1.0");
		renp.setModeFreeMaker("management/mainframe/welcome.html");
		return renp;
	}


	/**
	 * 登出系统
	 * @param revp
	 * @return
	 * @see com.management.biz.login.UserLoginBiz#logout(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope logout(RequestEnvelope revp) {
		ResponseEnvelope renp = new ResponseEnvelope();
		revp.getBody().getHttpSession().setAttribute(SysParamsList.LOGIN_SESSION_NAME,null);
		revp.getBody().getHttpSession().setAttribute(SysParamsList.LOGIN_SESSION_USERTYPE,null);
		renp.getBody().addParameter("title","大鲸集团管理系统平台1.0");
		renp.setModeFreeMaker("management/login/login.html");
		return renp;
	}

}
