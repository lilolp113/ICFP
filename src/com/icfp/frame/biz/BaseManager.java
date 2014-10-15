package com.icfp.frame.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.icfp.frame.ria.request.RequestEnvelope;

public interface BaseManager {
	
	public Date getSysDate();
	
	public String getStringPK();
	
	public BigDecimal getBigDecimalPK();
	
	@SuppressWarnings("unchecked")
	public List callProcedure_Object(List procList);
	
	public String getAreaCoding(RequestEnvelope rep);
	
	public Object getLoginUser(RequestEnvelope rep);
	
}
