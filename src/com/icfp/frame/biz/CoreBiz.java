package com.icfp.frame.biz;

import java.util.List;

import com.icfp.frame.entity.ZA02;

public interface CoreBiz extends BaseManager{

	public abstract ZA02 findByBizId(String paramString);
	
	public List<?> queryAA02();
	
	public List<?> querySA03();
	
	public List<?> querySA09(String hql);
	
	
}
