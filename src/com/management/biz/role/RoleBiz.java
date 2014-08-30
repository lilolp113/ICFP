package com.management.biz.role;

import com.icfp.frame.biz.BaseManager;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;

/**
 * 类RoleBiz.java的实现描述： 角色管理接口
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-21
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public interface RoleBiz  extends BaseManager{
	
	/**
	 * 显示角色列表模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope rep);
	
	/**
	 * 显示新增角色模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showAddFtl(RequestEnvelope rep);
	
	/**
	 * 显示修改角色模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showEdtFtl(RequestEnvelope rep);
	
	/**
	 * 查询角色列表信息
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA03(RequestEnvelope rep);
	
	/**
	 * 查询角色ByID
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA03ByID(RequestEnvelope rep);
	
	/**
	 * 新增角色
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeAddSA03(RequestEnvelope rep);
	
	/**
	 * 修改角色
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeEdtSA03(RequestEnvelope rep);
	
	/**
	 * 删除角色
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeDelSA03(RequestEnvelope rep);

}
