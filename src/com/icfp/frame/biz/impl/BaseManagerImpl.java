package com.icfp.frame.biz.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import com.icfp.frame.biz.BaseManager;
import com.icfp.frame.dao.BaseDao;
import com.icfp.frame.params.SysParamsList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.management.entity.SA05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseManagerImpl implements BaseManager {
	
	private BaseDao basedao;
	
	public BaseDao getBasedao() {
		return basedao;
	}

	public void setBasedao(BaseDao basedao) {
		this.basedao = basedao;
	}
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 获取服务器系统时间
	 * @return date
	 */
	public Date getSysDate() {
		return basedao.getSysDate();
	}
	
	/**
	 * 获取String型主键
	 * @return 主键
	 */
	public String getStringPK() {
		return basedao.getStringPK();
	}
	
	/**
	 * 获取BigDecimal型主键
	 * @return 主键
	 */
	public BigDecimal getBigDecimalPK() {
		return basedao.getBigDecimalPK();
	}
	
	/**
	 * 调用存储过程
	 * @param hibernateTemplate
	 * @return List 数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List callProcedure_Object(List procList){
		return basedao.callProcedure_Object(procList);
	}
	
	/**
	 * 得到区域编号
	 * @param loginUser
	 * @return 区域编码
	 */
	public String getAreaCoding(RequestEnvelope rep)
	{
		return basedao.getAreaCoding(rep);
	}
	
	/**
	 * 获取登录用户信息
	 * @param rep
	 * @return Object登录用户对象
	 */
	public Object getLoginUser(RequestEnvelope rep)
	{
		Object object=rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		return object;
	}
	
	/**
	 * 获取登录用户名称
	 * @param rep
	 * @return Object登录用户对象名称
	 */
	public String getLoginUserName(RequestEnvelope rep)
	{
		SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		return sa05.getSAC004();
	}
	
	/**
	 * 获取登录用户编号
	 * @param rep
	 * @return Object登录用户对象编号
	 */
	public String getLoginUserID(RequestEnvelope rep)
	{
		SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		return sa05.getSAC001();
	}
	
	/**
	 * 获取登录用户所属角色编号
	 * @param rep
	 * @return 登录用户所属角色编号
	 */
	public String getLoginUserRoleID(RequestEnvelope rep)
	{
		SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		return sa05.getSAA001();
	}

	public String cul(String curl) {
		String resultStr = "";
		if(curl != null && !"".equals(curl)){
			try {
				URL url = new URL(curl);
				URLConnection URLconnection = url.openConnection();
				HttpURLConnection httpConnection = (HttpURLConnection)URLconnection;
				int responseCode = httpConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream urlStream = httpConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
					String sCurrentLine = "";
					while ((sCurrentLine = bufferedReader.readLine()) != null) {
						resultStr += sCurrentLine;
					}
					return resultStr;
				}else{
					return resultStr;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			return resultStr;
		}
		return resultStr;
	}

	/**获取系统时间 精确到秒 
	 * 2012-11-27
	 * @return date
	 */
	public Date getSysDateTime() {
		return basedao.getSysDateTime();
	}
	
	/**
	 * 获取序列的下一个值
	 * @param Seq_name 序列名
	 * @param length 长度
	 * @return
	 * @see com.icfp.frame.biz.BaseManager#getSeq_next(java.lang.String, int)
	 */
	public String getSeq_next(String Seq_name,int length){
		if(length>0){
			int val = basedao.getSeq_next(Seq_name);
			if(val == -1){
				return "";
			}else{
				return String.format("%0"+length+"d",val);
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 获取当前序列值
	 * @param Seq_name
	 * @param length
	 * @return
	 * @see com.icfp.frame.biz.BaseManager#getSeq_curr(java.lang.String, int)
	 */
	public String getSeq_curr(String Seq_name,int length){
		if(length>0){
			int val = basedao.getSeq_curr(Seq_name);
			if(val == -1){
				return "";
			}else{
				return String.format("%0"+length+"d",val);
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 设置序列初始值 并返回下一个序列值
	 * @param Seq_name
	 * @param length
	 * @return
	 * @see com.icfp.frame.biz.BaseManager#setSeq_currval(java.lang.String, int, int)
	 */
	public String setSeq_currval(String Seq_name,int value,int length){
		if(length>0){
			int val = basedao.setSeq_currval(Seq_name, value);
			if(val == -1){
				return "";
			}else{
				return String.format("%0"+length+"d",val);
			}
		}else{
			return "";
		}
	}
	
	
	
}
