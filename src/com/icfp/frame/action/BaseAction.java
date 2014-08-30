package com.icfp.frame.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	
	private static final long serialVersionUID = 3557314514387386284L;
	
	public static final String FREEMARKER = "freemarker";
	
	public HttpServletRequest request;
	
	public HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public ServletContext getServletContext(HttpServletRequest request) {
		return request.getSession().getServletContext();
	}

}
