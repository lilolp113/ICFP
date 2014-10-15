package com.icfp.frame.biz;

import com.icfp.frame.entity.ZA02;

public interface CoreBiz extends BaseManager{

	public abstract ZA02 findByBizId(String paramString);
	
}
