package com.icfp.frame.dao.impl;

import java.util.List;

import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.entity.ZA02;

/**
 * 核心DAO
 * @author liufei
 *
 * @param <T>
 */
public class CoreDaoImpl extends BaseDaoImpl implements CoreDao {

	/**
	 * 根据业务编号获取对应信息
	 */
	@SuppressWarnings("unchecked")
	public ZA02 findByBizId(String paramString)
	{
		String sql = "select * from za02 z where z.zaa001 = '" + paramString + "'";
		List list = (List) super.executeSqlByClass(sql, ZA02.class);
		ZA02 za02 = null;; 
		if(list != null && list.size() != 0 && !list.isEmpty() && list.get(0)!= null){
			za02 = (ZA02)list.get(0);
		}
		return za02;
	}
	
}