package com.icfp.frame.exception;

/**Json解析异常
 * @author wangxing
 *2011-08-25
 */
public class JsonResolvingException extends Exception {
	private static final long serialVersionUID = -6762818560018021544L;

	public JsonResolvingException(String msg) {
		super(msg);
		//super.printStackTrace();
	}
}
