package com.icfp.frame.biz.impl;

import com.icfp.frame.biz.CoreBiz;
import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.entity.ZA02;

public class CoreBizImpl extends BaseManagerImpl implements CoreBiz {

	private CoreDao coreDao;
	
	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}
	
	public ZA02 findByBizId(String paramString) {
		
		return coreDao.findByBizId(paramString);
	}

}
