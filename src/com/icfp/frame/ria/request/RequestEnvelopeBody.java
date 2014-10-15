package com.icfp.frame.ria.request;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.icfp.frame.params.SysParamsList;

/**
 * 请求信息体 2011-08-25
 * 
 * @author wangxing
 * 
 */
public class RequestEnvelopeBody {
	private HashMap body = new HashMap();

	@SuppressWarnings("unchecked")
	public void addParameter(String paramName, Object paramValue) {
		if (this.body.containsKey(paramName)) {
			removeParameter(paramName);
		}
		this.body.put(paramName, paramValue);
	}

	public void removeParameter(String paramName) {
		this.body.remove(paramName);
	}

	public void removeAllParameters() {
		this.body.clear();
	}

	public Object getParameter(String paramName) {
		if (this.body.containsKey(paramName)) {
			return this.body.get(paramName);
		}
		return null;
	}

	public HashMap getAllParameters() {
		return this.body;
	}
	
	@SuppressWarnings("unchecked")
	public void setHttpSession(HttpSession session) {
		this.body.put(SysParamsList.HTTPSESSION_KEY, session);
	}
	
	public HttpSession getHttpSession() {
		return (HttpSession)this.getParameter(SysParamsList.HTTPSESSION_KEY);
	}
	
}
