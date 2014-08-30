package com.management.biz.impow;

import com.icfp.frame.biz.BaseManager;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;

/**
 * 类ImpowBiz.java的实现描述： 角色授权接口
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-21
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public interface ImpowBiz  extends BaseManager{
	
	/**
	 * 显示授权模板页
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope rep);
	
	/**
	 * 查询角色信息
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA03(RequestEnvelope rep);
	
	/**
	 * 查询菜单信息
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA09(RequestEnvelope rep);
	
	/**
	 * 查询授权信息
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope querySA10(RequestEnvelope rep);
	
	/**
	 * 保存授权
	 * @param rep
	 * @return
	 */
	public ResponseEnvelope executeAddSA10(RequestEnvelope rep);
	
}
