package com.icfp.frame.ria.response;

import com.icfp.frame.params.RiaParamList;

/**返回错误信息
 * 2011-08-25
 * 
 *
 */
public class ResponseEnvelopeHeader {
	
	private int appCode = RiaParamList.CODE_SYS_FAIL;
	private long errCode;
	private String msg = "";
	private String detailMsg = "";
	
    /**
     * 获取响应详细信息
     * @return 详细信息
     */
	public String getDetailMsg() {
		return detailMsg;
	}
	
	/**
	 * 设置响应详细信息
	 * @param detailMsg 详细信息
	 */
	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}
	
    /**
     * 获取错误标示
     * @return 错误标示
     */
	public long getErrCode() {
		return errCode;
	}
	
	/**
	 * 设置错误标示
	 * @param errCode 错误标示
	 */
	public void setErrCode(long errCode) {
		this.errCode = errCode;
	}
	
	/**
	 * 获取响应信息
	 * @return 响应信息
	 */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * 设置响应信息
	 * @param msg 响应信息
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 获取操作标示
	 * @return 操作标示
	 */
	public int getAppCode() {
		return appCode;
	}
	
	/**
	 * 设置操作标示
	 * @param appCode 操作标示
	 */
	public void setAppCode(int appCode) {
		this.appCode = appCode;
	}


}