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
	
	public String getDetailMsg() {
		return detailMsg;
	}
	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}
	public long getErrCode() {
		return errCode;
	}
	public void setErrCode(long errCode) {
		this.errCode = errCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getAppCode() {
		return appCode;
	}
	public void setAppCode(int appCode) {
		this.appCode = appCode;
	}


}