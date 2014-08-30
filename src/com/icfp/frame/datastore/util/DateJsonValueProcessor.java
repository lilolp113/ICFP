package com.icfp.frame.datastore.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**JSON格式化时间工具
 * @author wangxing 2011-09-09
 *
 */
public class DateJsonValueProcessor implements JsonValueProcessor {

	private String format = "yyyy-MM-dd HH:mm:ss";
	private String format2 = "yyyy-MM-dd";

	private SimpleDateFormat sdf = new SimpleDateFormat(format);
	private SimpleDateFormat sdf2 = new SimpleDateFormat(format2);

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return this.process(value);
	}

	public Object processObjectValue(String key, Object value,JsonConfig jsonConfig) {
		return this.process(value);
	}

	private Object process(Object value) {
		if (value == null) {
			return "";
		}else if (value instanceof Date) {
			String dataStr="";
			if(value.toString().length()>10)
			{
				dataStr=sdf.format((Date) value);
			}else{
				dataStr =  sdf2.format((Date) value);
			}
			if(dataStr.endsWith("00:00:00")) {
				dataStr =  sdf2.format((Date) value);
			}
			return dataStr;
		}else if(value instanceof Timestamp){
			Date date = new Date(((Timestamp) value).getTime());
			String dataStr = sdf.format(date);
			if(dataStr.endsWith("00:00:00")) {
				dataStr =  sdf2.format(date);
			}
			return dataStr;
		}else if(value instanceof BigDecimal){
			BigDecimal bidDValue = (BigDecimal) value;
			return bidDValue;
		}else if(value instanceof String){
			String StringValue = (String)value;
			if("".equals(StringValue) || "null".equals(StringValue) || "NULL".equals(StringValue)){
				return "";
			}else{
				return StringValue;
			}
		}else{
			return value.toString();
		}
	}

}
