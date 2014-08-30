package com.icfp.frame.params;

/**DAO参数列表
 * @author wangxing 
 * 2011-09-09
 */
public class DaoParamList {
	
	/**
	 * 系统字段：经办人
	 */
	public static String OPERATOR = "ZZB001";
	
	/**
	 * 系统字段：经办时间
	 */
	public static String OPERATE_DATE = "ZZB002";
	
	/**
	 * 系统字段：区域编号
	 */
	public static String AREACODING = "ZZC001";
	
	/**
	 * 系统字段：有效标志
	 */
	public static String VALID_FLAG = "ZZD001";
	
	/**
	 * 系统字段：并发字段
	 */
	public static String CONCURRENCY = "ZZD002";
	
	/**
	 *  系统字段：删除标志
	 */
	public static String DELETE_FLAG = "ZZD003";

	/**
	 * 得到并发字段方法名
	 *//*
	public static String GET_CONCURRENCY = "getZZD002";*/

	/**
	 * 设置并发字段方法名 
	 *//*
	public static String SET_CONCURRENCY = "setZZD002";*/
	
	/**
	 * 设置删除标志字段方法名
	 */
//	public static String SET_DELETEFLAG = "setZZD003";
	
	/**
	 * 得到区域编号字段方法名
	 */
//	public static String GET_AREACODING = "getZZC001";

	/**
	 * 普通主键类型
	 */
	public static String[] PK_COMMONTYPE = { "short", "integer", "long", "string", "int" };

	/**
	 * 得到联合主键方法名（由Hibernate自动生成）
	 */
	public static String GET_COMPOSITEID = "getID";

	
	/**
	 * 设置操作时间方法名
	 */
	//public static String SET_DATE = "setZZB002";
	
	/**
	 * 数据库包名
	 */
	public static String PKG_NAME = "pkg_comm";
	
	/**
	 * 调用存储过程获得当前时间
	 */
	public static String PRC_DATE = PKG_NAME + "." + "prc_currenttime";
	
	/**
	 * 调用存储过程获得String型主键
	 */
	public static String PRC_STRING_PK = PKG_NAME + "." + "prc_string_pk";
	
	
	/**
	 * 调用存储过程获得机构编号
	 */
	public static String PRC_STRING_JIGOUBM = PKG_NAME + "." + "prc_string_jigou_bm";
	
	/**
	 * 调用存储过程获得BigDecimal型主键
	 */
	public static String PRC_BIGDECIMAL_PK = PKG_NAME + "." + "prc_big_decimal_pk";
	
	/**
	 * 书数库包名+表名
	 */
	public static String PRC_PK_TABLENAME = PKG_NAME + "." + "tablename";
	
	/**
	 * 查询操作开始时间前缀
	 */
	public static String BEGIN_DATE_FLAG = "b";

	/**
	 * 查询操作结束时间前缀
	 */
	public static String END_DATE_FLAG = "e";
	
	/**
	 * 查询操作默认开始时间
	 */
	public static String QUERY_BEGIN_DATE = "1000-01-01";
	
	/**
	 * 查询操作默认结束时间
	 */
	public static String QUERY_END_DATE = "9999-01-01";
	
	/**
	 * 不需要分页的pageSize值
	 */
	public static int PAGING = -1;
	
	//添加时刷新
	public static int INSERT_SIZE=10;
	
	public static int UPDATE_SIZE=10;
	
	public static int DELETE_SIZE=10;
	

}
