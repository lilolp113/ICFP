package com.icfp.frame.ria.response;

import com.icfp.frame.ria.util.DataCenterUtil;


/**
 * 返回信息 2011-08-25
 * 
 * 
 * 
 */
public class ResponseEnvelope {
	
	private ResponseEnvelopeHeader header = new ResponseEnvelopeHeader();

	private ResponseEnvelopeBody body = new ResponseEnvelopeBody();
	
	private String mode = "0"; //交互模式
	
	private String urlPath;

	public ResponseEnvelopeHeader getHeader() {
		return this.header;
	}

	public void setHeader(ResponseEnvelopeHeader header) {
		this.header = header;
	}

	public ResponseEnvelopeBody getBody() {
		return this.body;
	}

	public void setBody(ResponseEnvelopeBody body) {
		this.body = body;
	}
	
	public String toResponseString() {
		return DataCenterUtil.toResponseString(this);
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public void setModeFreeMaker(String urlPath){
		this.setMode("1");
		this.setUrlPath(urlPath);
	}
	
}
