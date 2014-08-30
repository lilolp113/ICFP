package com.icfp.frame.datastore.util;

import java.math.BigDecimal;
import net.sf.json.JSONNull;
import net.sf.json.processors.DefaultValueProcessor;

public class DefaultJsonValueProcessor implements DefaultValueProcessor{

	public Object getDefaultValue(Class type) {
		if(type == null){
			return "";
		}else if(String.class.isAssignableFrom(type)){
			return "";
		}else if(BigDecimal.class.isAssignableFrom(type)){
			return "";
		}else{
			return JSONNull.getInstance();  
		}
	}

}
