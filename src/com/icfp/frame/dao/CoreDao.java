package com.icfp.frame.dao;

import com.icfp.frame.entity.ZA02;

public interface CoreDao extends BaseDao{

	/**
	 * 根据业务编号获取对象信息列表
	 * @param paramString
	 * @return
	 */
	public ZA02 findByBizId(String paramString);
	
}
