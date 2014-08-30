package com.icfp.frame.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.icfp.frame.ria.request.RequestEnvelope;

public interface BaseManager {
	
	/**
	 * 获取服务器系统时间
	 * @return date
	 */
	public Date getSysDate();
	
	/**
	 * 获取String型主键
	 * @return 主键
	 */
	public String getStringPK();
	
	/**
	 * 获取BigDecimal型主键
	 * @return 主键
	 */
	public BigDecimal getBigDecimalPK();
	
	/**
	 * 调用存储过程
	 * @param hibernateTemplate
	 * @return List 数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List callProcedure_Object(List procList);
	
	/**
	 * 得到区域编号
	 * @param loginUser
	 * @return 区域编码
	 */
	public String getAreaCoding(RequestEnvelope rep);
	
	/**
	 * 获取登录用户信息
	 * @param rep
	 * @return Object登录用户对象
	 */
	public Object getLoginUser(RequestEnvelope rep);
	
	/**
	 * 获取登录用户名称
	 * @param rep
	 * @return Object登录用户对象名称
	 */
	public String getLoginUserName(RequestEnvelope rep);
	
	/**
	 * 获取登录用户编号
	 * @param rep
	 * @return Object登录用户对象编号
	 */
	public String getLoginUserID(RequestEnvelope rep);
	
	/**
	 * 获取登录用户所属角色编号
	 * @param rep
	 * @return 登录用户所属角色编号
	 */
	public String getLoginUserRoleID(RequestEnvelope rep);
	
	public String cul(String curl);


	/**获取系统时间 精确到秒 
	 * 2012-11-27
	 * @return date
	 */
	public Date getSysDateTime(); 
	
	
	/**
	 * 获取序列的下一个值
	 * @param Seq_name 序列名
	 * @param length 长度
	 * @return
	 */
	public String getSeq_next(String Seq_name,int length);
	
	/**
	 * 获取当前序列值
	 * @param Seq_name
	 * @param length
	 * @return
	 */
	public String getSeq_curr(String Seq_name,int length);
	
	/**
	 * 设置序列初始值 并返回下一个序列值
	 * @param Seq_name
	 * @param length
	 * @return
	 */
	public String setSeq_currval(String Seq_name,int value,int length);
	
	
}
