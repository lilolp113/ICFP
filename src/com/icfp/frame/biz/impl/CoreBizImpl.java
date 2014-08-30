package com.icfp.frame.biz.impl;

import java.util.List;

import com.icfp.frame.biz.CoreBiz;
import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.entity.ZA02;

public class CoreBizImpl extends BaseManagerImpl implements CoreBiz {

	private CoreDao coreDao;
	
	
	@SuppressWarnings("unchecked")
	public ZA02 findByBizId(String paramString) {
		String sql = "select * from za02 z where z.zaa001 = '" + paramString + "'";
		List list = (List)coreDao.executeQueryByClass(sql, ZA02.class);
		ZA02 za02 = null;; 
		if(list != null && list.size() != 0 && !list.isEmpty() && list.get(0)!= null){
			za02 = (ZA02)list.get(0);
		}
		return za02;
	}
	
	public List<?> queryAA02() {
		String hql="from AA02 where ZZD001='0' Order By AAA001";
		return coreDao.find(hql, null);
	}
	
	public List<?> querySA03() {
		String hql="from SA03 where ZZD001='0' Order By ZZB002";
		return coreDao.find(hql, null);
	}
	
	
	public List<?> querySA09(String hql) {
		//String hql="from SA09 where ZZD001='0' Order By ZZB002";
		return coreDao.find(hql, null);
	}

	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}
	
}
