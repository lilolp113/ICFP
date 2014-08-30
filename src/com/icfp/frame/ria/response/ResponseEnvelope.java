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
	
	/**
	 * 封装响应信息
	 * @return 封装信息
	 */
	public String toResponseString() {
		return DataCenterUtil.toResponseString(this);
	}

	/**
	 * 获取响应跳转模式
	 * @return 响应模式
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * 设置响应跳转模式
	 * @param mode 模式
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	/**
	 * 获取响应路径
	 * @return 路径地址
	 */
	public String getUrlPath() {
		return urlPath;
	}

	/**
	 * 设置响应跳转路径
	 * @param urlPath 页面跳转路径
	 */
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	/**
	 * 设置freemarker模式跳转路径
	 * @param urlPath 页面路径
	 */
	public void setModeFreeMaker(String urlPath){
		this.setMode("1");
		this.setUrlPath(urlPath);
	}
	
}
