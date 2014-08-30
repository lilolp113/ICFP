package com.icfp.frame.ria.request;

import javax.servlet.http.HttpServletRequest;

import com.icfp.frame.exception.JsonResolvingException;
import com.icfp.frame.ria.util.DataCenterUtil;


/**
 * 请求信息 2011-08-25
 * 
 * @author wangxing
 * 
 */
public class RequestEnvelope {
	
	public RequestEnvelope() {
		
	}
	
	public RequestEnvelope(HttpServletRequest request) throws JsonResolvingException {
		this.setBody(DataCenterUtil.wrapped(request).getBody());
	}
	
	public RequestEnvelope(Object jsonstr) throws JsonResolvingException {
		if(null != jsonstr){
			this.setBody(DataCenterUtil.wrapped(jsonstr).getBody());
		}
	}
	
	private RequestEnvelopeHeader header = new RequestEnvelopeHeader();

	private RequestEnvelopeBody body = new RequestEnvelopeBody();

	public RequestEnvelopeHeader getHeader() {
		return this.header;
	}

	public void setHeader(RequestEnvelopeHeader header) {
		this.header = header;
	}

	public RequestEnvelopeBody getBody() {
		return this.body;
	}

	public void setBody(RequestEnvelopeBody body) {
		this.body = body;
	}
}