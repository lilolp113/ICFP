package com.icfp.frame.ria.request;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.icfp.frame.params.SysParamsList;

/**
 * 请求信息体 2011-08-25
 * 
 * @author wangxing
 * 
 */
public class RequestEnvelopeBody {
	
	private HashMap<String,Object> body = new HashMap<String,Object>();

	/**
	 * 添加参数
	 * @param paramName 参数名称 
	 * @param paramValue 参数值
	 */
	public void addParameter(String paramName, Object paramValue) {
		if (this.body.containsKey(paramName)) {
			removeParameter(paramName);
		}
		this.body.put(paramName, paramValue);
	}

	/**
	 * 移除参数
	 * @param paramName 参数名
	 */
	public void removeParameter(String paramName) {
		this.body.remove(paramName);
	}

	/**
	 * 移除所有参数
	 */
	public void removeAllParameters() {
		this.body.clear();
	}

	/**
	 * 获取参数值
	 * @param paramName 参数名
	 * @return
	 */
	public Object getParameter(String paramName) {
		if (this.body.containsKey(paramName)) {
			return this.body.get(paramName);
		}
		return null;
	}

	public HashMap<String,Object> getAllParameters() {
		return this.body;
	}
	
	/**
	 * 建立session
	 * @param session
	 */
	public void setHttpSession(HttpSession session) {
		this.body.put(SysParamsList.HTTPSESSION_KEY, session);
	}
	
	/**
	 * 获取session
	 * @return
	 */
	public HttpSession getHttpSession() {
		return (HttpSession)this.getParameter(SysParamsList.HTTPSESSION_KEY);
	}
	
	/**
	 * 建立cookie
	 * @param cookie
	 */
	public void setCookie(Cookie[] cookie) {
		this.body.put(SysParamsList.COOKIE_KEY, cookie);
	}
	
	/**
	 * 获取cookie
	 * @return
	 */
	public Cookie[] getCookie() {
		return (Cookie[])this.getParameter(SysParamsList.COOKIE_KEY);
	}
	
	/**
	 * 建立ServletContext
	 * @param session
	 */
	public void setServletContext(ServletContext servletcontext) {
		this.body.put(SysParamsList.SERVLETCONTEXT_KEY, servletcontext);
	}
	
	/**
	 * 获取ServletContext
	 * @return
	 */
	public ServletContext getServletContext() {
		return (ServletContext)this.getParameter(SysParamsList.SERVLETCONTEXT_KEY);
	}
	
}
