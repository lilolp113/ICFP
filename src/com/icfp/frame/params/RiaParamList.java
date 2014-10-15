package com.icfp.frame.params;

/**请求数据中的参数
 * @author wangxing
 *2011-09-09
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class RiaParamList {
	
	/**
	 * 请求编号
	 */
	public static String REQUESTID = "requestId";
	
	/**
	 * 业务编号
	 */
	public static String BIZID = "bizId";
	
	/**
	 * METHOD
	 */
	public static String METHOD = "method";
	
	//统一数据交互格式参数 Begin  -->
	public static String REQUEST_HEAD = "head";
	
	public static String HEAD_CODE = "code";
	
	public static String HEAD_MESSAGE = "message";
	
	public static String MESSAGE_TITLE = "title";
	
	public static String MESSAGE_DETAIL = "detail";
	
	public static String REQUEST_BODY = "body";
	
	public static String BODY_PARAMETERS = "parameters";
	
	public static String BODY_DATASTORES = "dataStores";
	//统一数据交互格式参数 End    <--
	
	/**
	 * 成功标志位
	 */
	public static int CODE_SUCCESS = 0;
	
	/**
	 * 错误标志位
	 */
	public static int CODE_FAIL = 100;
	
	/**
	 * 系统标志位
	 */
	public static int CODE_SYS_FAIL = -1;
	
	/**
	 * 许可证过期标志位
	 */
	public static int CODE_EXPIRED = -100;
	
	/**
	 * Session已销毁标志位
	 */
	public static int CODE_SESSION_DESTROYED = 200;
	
	/**
	 * VPD Key标志
	 */
	public static String VPD = "VPD";
	
}
