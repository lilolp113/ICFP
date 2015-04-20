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
import com.icfp.core.entity.SA05;

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
	
	public Date getSysDate() {
		return basedao.getSysDate();
	}
	
	public String getStringPK() {
		return basedao.getStringPK();
	}
	
	public BigDecimal getBigDecimalPK() {
		return basedao.getBigDecimalPK();
	}
	
	@SuppressWarnings("unchecked")
	public List callProcedure_Object(List procList){
		return basedao.callProcedure_Object(procList);
	}
	
	public String getAreaCoding(RequestEnvelope rep)
	{
		return basedao.getAreaCoding(rep);
	}
	
	public Object getLoginUser(RequestEnvelope rep)
	{
		Object object=rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		return object;
	}
	
	public String getLoginUserName(RequestEnvelope rep)
	{
		SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		return sa05.getSAC004();
	}
	public String getLoginUserID(RequestEnvelope rep)
	{
		SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		return sa05.getSAC001();
	}
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

	public Date getSysDateTime() {
		return basedao.getSysDateTime();
	}
	
	public Object getQuerySysLoginUser(RequestEnvelope rep)
	{
		Object obj =rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SEARCH_NAME);
		return obj;
	}
	
	public String getQuerySysLoginUID(RequestEnvelope rep)
	{
		SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SEARCH_NAME);
		return sa05.getSAC001();
	}
	
	public String getQuerySysLoginUName(RequestEnvelope rep)
	{
		SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SEARCH_NAME);
		return sa05.getSAC004();
	}
	
	public String getQuerySysLoginRID(RequestEnvelope rep)
	{
		SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SEARCH_NAME);
		return sa05.getSAA001();
	}
}
