package com.management.biz.user;

import com.icfp.frame.biz.BaseManager;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;

/**
 * 类UserBiz.java的实现描述： 用户管理接口
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-21
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public interface UserBiz extends BaseManager{
	
	/**
	 * 显示用户列表模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope rep);
	
	/**
	 * 显示新增用户模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showAddFtl(RequestEnvelope rep);
	
	/**
	 * 显示修改用户模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showEdtFtl(RequestEnvelope rep);
	
	/**
	 * 查询用户列表信息
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA05(RequestEnvelope rep);
	
	/**
	 * 查询用户ByID
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA05ByID(RequestEnvelope rep);
	
	/**
	 * 新增用户
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeAddSA05(RequestEnvelope rep);
	
	/**
	 * 修改用户
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeEdtSA05(RequestEnvelope rep);
	
	/**
	 * 删除用户
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeDelSA05(RequestEnvelope rep);
	
}
