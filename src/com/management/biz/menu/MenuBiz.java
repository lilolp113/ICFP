package com.management.biz.menu;

import com.icfp.frame.biz.BaseManager;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;

/**
 * 类MenuBiz.java的实现描述： 菜单管理接口
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-21
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public interface MenuBiz extends BaseManager{
	
	/**
	 * 显示菜单列表模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope rep);
	
	/**
	 * 显示新增菜单模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showAddFtl(RequestEnvelope rep);
	
	/**
	 * 显示修改菜单模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showEdtFtl(RequestEnvelope rep);
	
	/**
	 * 查询菜单列表信息
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA09(RequestEnvelope rep);
	
	/**
	 * 查询菜单ByID
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA09ByID(RequestEnvelope rep);
	
	/**
	 * 查询菜单树
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA09Tree(RequestEnvelope rep);
	
	/**
	 * 根据当前用户查询菜单（暂时未使用）
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA09ByUser(RequestEnvelope rep);
	
	/**
	 * 新增菜单
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeAddSA09(RequestEnvelope rep);
	
	/**
	 * 修改菜单
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeEdtSA09(RequestEnvelope rep);
	
	/**
	 * 删除菜单
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeDelSA09(RequestEnvelope rep);
	
}
