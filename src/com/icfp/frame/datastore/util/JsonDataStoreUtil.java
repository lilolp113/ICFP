package com.icfp.frame.datastore.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.exception.ApplicationRuntimeException;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;

/**Json数据与DataStore转换类 
 * 
 * 
 */
public class JsonDataStoreUtil {
	
	/**
	 * 将Json字符串转换为DataStore
	 * 
	 * @param jsonDataStoreStr
	 * @return dataStore
	 */
	public static DataStore jsonStrToDataStore(String jsonDataStoreStr) {
		DataStore dataStore = new DataStore();
		JSONObject jsonObj = JSONObject.fromObject(jsonDataStoreStr);
		String bizId = jsonObj.getString(DataStoreParamList.BIZID);
		dataStore.setBizId(bizId);
		String dataType = jsonObj.getString(DataStoreParamList.DATATYPE);
		dataStore.setDataType(dataType);
		int totalPage = jsonObj.getInt(DataStoreParamList.TOTALPAGE);
		dataStore.setTotalPage(totalPage);
		int pageSize = jsonObj.getInt(DataStoreParamList.PAGESIZE);
		dataStore.setPageSize(pageSize);
		int pageNo = jsonObj.getInt(DataStoreParamList.PAGENO);
		dataStore.setPageNo(pageNo);
		int rowCount = jsonObj.getInt(DataStoreParamList.ROWCOUNT);
		dataStore.setRowCount(rowCount);
		String ordering = jsonObj.getString(DataStoreParamList.ORDERING);
		dataStore.setOrdering(ordering);
		String sql = jsonObj.getString(DataStoreParamList.CONDITION);
		dataStore.setSQL(sql);
		String psObj = jsonObj.getString(DataStoreParamList.PARAMS);
		Object[] params = (Object[]) null;
		if ((psObj != null) && (!psObj.trim().equals(""))) {
			JSONArray paramsJsonArray = JSONArray.fromObject(psObj);
			params = jsonArrayToParams(paramsJsonArray);
		}
		dataStore.setParams(params);
		// 检查条件语句占位符和条件参数是否数量匹配
		if ((sql != null) && (sql.length() > 0) && (params != null) && (params.length > 0)) {
			int markCount = sql.concat(" ").split("\\" + DataStoreParamList.MARK).length - 1;
			int paramsCount = params.length;
			if (markCount != paramsCount) {
				throw new ApplicationRuntimeException("条件语句占位符个数[" + markCount + "]和条件参数个数[" + paramsCount + "]不匹配");
			}
		}
		// 解析RowSet
		String rsObj = jsonObj.getString(DataStoreParamList.ROWSET);
		Object[] rowSet = (Object[]) null;
		if ((rsObj != null) && (!rsObj.trim().equals(""))) {
			JSONArray rowSetJsonArray = JSONArray.fromObject(rsObj);
			rowSet = jsonArrayToRowSet(rowSetJsonArray, dataType);
		}
		dataStore.setRowSet(rowSet);
		return dataStore;
	}

	/**
	 * 将JSONArray解析为Params，目前采用二维数组实现 Object[][] Params =
	 * {{{Integer},{2}},{String, 'week'},{}....};
	 * 
	 * @param jsonArray
	 * @return Params
	 */
	private static Object[] jsonArrayToParams(JSONArray jsonArray) {
		int size = jsonArray.size();
		Object[] params = new Object[size];
		if (size == 0)
			return params;
		for (int i = 0; i < size; i++) {
			JSONObject jO = jsonArray.getJSONObject(i);
			String[] param = new String[2];
			String paramType = jO.getString(DataStoreParamList.PARAMTYPE).toLowerCase();
			if ((!paramType.equals(DataStoreParamList.PARAMTYPE_INTEGER)) && (!paramType.equals(DataStoreParamList.PARAMTYPE_STRING)) && (!paramType.equals(DataStoreParamList.PARAMTYPE_DATE))) {
				new ApplicationRuntimeException("查询条件中参数类型[" + paramType + "]不能识别,"+ "只能识别[" + DataStoreParamList.PARAMTYPE_INTEGER+ "],[" + DataStoreParamList.PARAMTYPE_STRING + "],["+ DataStoreParamList.PARAMTYPE_DATE + "]");
			}
			param[0] = paramType;
			String paramValue = jO.getString(DataStoreParamList.PARAMVALUE);
			param[1] = paramValue;
			params[i] = param;
		}
		return params;
	}

	/**
	 * 将JSONArray解析为RowSet，目前采用二维数组实现 Object[][] rowSet =
	 * {{{status},{user}},{status, user},{}....};
	 * 
	 * @param jsonArray
	 * @param dataType
	 * @return RowSet
	 */
	@SuppressWarnings("unchecked")
	private static Object[] jsonArrayToRowSet(JSONArray jsonArray,String dataType) {
		int size = jsonArray.size();
		Object[] rowSet = new Object[size];
		if (size == 0){
			return rowSet;
		}
		Class dataClass = null;
		try {
			dataClass = Class.forName(dataType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < size; i++) {
			JSONObject jO = jsonArray.getJSONObject(i);
			Object[] row = new Object[2];
			row[0] = jO.getString(DataStoreParamList.STATUS);
			JSONObject cellObj = jO.getJSONObject(DataStoreParamList.CELL);
			String[] dateFormats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));
			Set set = cellObj.keySet();
			int s = set.size();
			Iterator iter = set.iterator();
			List removeKeys = new ArrayList();
			for (int j = 0; j < s; j++) {
				Object key = iter.next();
				String type = "";
				try {
					if(dataClass.getDeclaredField((String) key)!=null){
						type = dataClass.getDeclaredField((String) key).getType().getSimpleName();
					}
				} catch (Exception e) {
					//e.printStackTrace();
					continue;
				}
				if (type.equals("Date")) {
					Object value = cellObj.get(key);
					if ((value == null) || (value.toString().trim().equals(""))) {
						removeKeys.add(key);
					}
				}
			}
			for (int k = 0; k < removeKeys.size(); k++) {
				cellObj.remove(removeKeys.get(k));
			}

			//row[1] = JSONObject.toBean(cellObj,dataClass);
			//2012-6-15 李雷修改
			row[1] = getEntity(cellObj,dataClass);
			if (row[1] == null){
				throw new ApplicationRuntimeException("rowSet[" + i + "]中["+ DataStoreParamList.CELL + "]解析错误");
			}
			rowSet[i] = row;
		}
		return rowSet;
	}

	/**
	 * 将DataStore转换为JOSNObject
	 * 
	 * @param dataStore
	 * @return JSONObject
	 */
	public static JSONObject dataStoreToJSONObject(DataStore dataStore) {
		String s = dataStoreToJsonStr(dataStore);
		return JSONObject.fromObject(s);
	}

	/**
	 * 将DataStore转换为JOSNObject字符串
	 * 
	 * @param dataStore
	 * @return JSONObject字符串
	 */
	public static String dataStoreToJsonStr(DataStore dataStore) {
		StringBuffer buf = new StringBuffer();

		buf.append("{").append(DataStoreParamList.BIZID).append(":\"").append(
				dataStore.getBizId()).append("\", ").append(
				DataStoreParamList.DATATYPE).append(":\"").append(
				dataStore.getDataType()).append("\", ").append(
				DataStoreParamList.TOTALPAGE).append(":").append(
				dataStore.getTotalPage()).append(", ").append(
				DataStoreParamList.PAGESIZE).append(":").append(
				dataStore.getPageSize()).append(", ").append(
				DataStoreParamList.PAGENO).append(":").append(
				dataStore.getPageNo()).append(", ").append(
				DataStoreParamList.ROWCOUNT).append(":").append(
				dataStore.getRowCount()).append(", ").append(
				DataStoreParamList.ORDERING).append(":\"").append(
				dataStore.getOrdering()).append("\", ").append(
				DataStoreParamList.CONDITION).append(":\"").append(
				dataStore.getSQL()).append("\", ").append(
				DataStoreParamList.PARAMS).append(":[").append(
				paramsToJsonStr(dataStore.getParams())).append("], ").append(
				DataStoreParamList.ROWSET).append(":[").append(
				rowSetToJsonStr(dataStore.getRowSet())).append("]");
		buf.append("}");

		return buf.toString();
	}
	
	public static JSONObject dataStoreToJsonObj(DataStore dataStore){
		JSONObject ds = new JSONObject();
		ds.put(DataStoreParamList.BIZID,dataStore.getBizId());
		ds.put(DataStoreParamList.DATATYPE,dataStore.getDataType());
		ds.put(DataStoreParamList.TOTALPAGE,dataStore.getTotalPage());
		ds.put(DataStoreParamList.PAGESIZE,dataStore.getPageSize());
		ds.put(DataStoreParamList.PAGENO,dataStore.getPageNo());
		ds.put(DataStoreParamList.ROWCOUNT,dataStore.getRowCount());
		ds.put(DataStoreParamList.ORDERING,dataStore.getOrdering());
		ds.put(DataStoreParamList.CONDITION,dataStore.getSQL());
		ds.put(DataStoreParamList.PARAMS,paramsToJsonObj(dataStore.getParams()));
		ds.put(DataStoreParamList.ROWSET,rowSetToJsonObj(dataStore.getRowSet()));
		return ds;
	}
	
	
	private static JSONObject paramsToJsonObj(Object[] params) {
		JSONObject jparams = new JSONObject();
		if ((params == null) || (params.length == 0))
			return jparams;
		for (int i = 0; i < params.length; i++) {
			String type = ((String[]) params[i])[0];
			String value = ((String[]) params[i])[1];
			jparams.put(DataStoreParamList.PARAMTYPE, type);
			jparams.put(DataStoreParamList.PARAMVALUE, value);
		}
		return jparams;
	}

	private static String paramsToJsonStr(Object[] params) {
		if ((params == null) || (params.length == 0))
			return "";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			String type = ((String[]) params[i])[0];
			String value = ((String[]) params[i])[1];
			buf.append("{").append(DataStoreParamList.PARAMTYPE).append(":\"")
					.append(type).append("\", ").append(
							DataStoreParamList.PARAMVALUE).append(":\"")
					.append(value).append("\"}, ");
		}
		String s = buf.toString();
		return s.substring(0, s.length() - 2);
	}

	private static List<Object> rowSetToJsonObj(Object[] rowSet) {
		List<Object> rowset  = new ArrayList<Object>();
		if ((rowSet == null) || (rowSet.length == 0))
			return rowset;
		for (int i = 0; i < rowSet.length; i++) {
			Object[] object = (Object[]) rowSet[i];
			if ((object == null) || (object.length == 0)) {
				return rowset;
			}
			Object status = String.valueOf(object[0]);
			Object obj = object[1];
			if (obj == null) {
				return rowset;
			}
			//过滤集合属性begin
			JsonConfig config = new JsonConfig();
			config.setJsonPropertyFilter(new PropertyFilter() {
				public boolean apply(Object source, String name, Object value) {
					if (value != null) {
						String typeName = value.getClass().getName();
						if (typeName.equals("org.hibernate.collection.PersistentSet")) {
							return true;
						}
					}
					return false;
				}
			});
			JsonValueProcessor jsonProcessor = new DateJsonValueProcessor();
			config.registerJsonValueProcessor(Timestamp.class,jsonProcessor);
			config.registerJsonValueProcessor(Date.class,jsonProcessor);
			//过滤集合属性end
			//2012-12-03 李雷加
			if(obj instanceof Map<?,?>){
				JSONObject jsonObj = JSONObject.fromObject((Map) obj,config);
				JSONObject row = new JSONObject();
				row.put(DataStoreParamList.STATUS,status);
				row.put(DataStoreParamList.CELL,jsonObj);
				rowset.add(row);
			}else{
				JSONObject jsonObj = JSONObject.fromObject(obj,config);
				JSONObject row = new JSONObject();
				row.put(DataStoreParamList.STATUS,status);
				row.put(DataStoreParamList.CELL,jsonObj);
				rowset.add(row);
			}
		}
		return rowset;
	}
	
	private static String rowSetToJsonStr(Object[] rowSet) {
		if ((rowSet == null) || (rowSet.length == 0))
			return "";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < rowSet.length; i++) {
			Object[] object = (Object[]) rowSet[i];
			if ((object == null) || (object.length == 0)) {
				return "";
			}
			Object status = String.valueOf(object[0]);
			Object obj = object[1];
			if (obj == null) {
				return "";
			}
			//过滤集合属性begin
			JsonConfig config = new JsonConfig();
			config.setJsonPropertyFilter(new PropertyFilter() {
				public boolean apply(Object source, String name, Object value) {
					if (value != null) {
						String typeName = value.getClass().getName();
						if (typeName
								.equals("org.hibernate.collection.PersistentSet")) {
							return true;
						}
					}
					return false;
				}
			});
			JsonValueProcessor jsonProcessor = new DateJsonValueProcessor();
			config.registerJsonValueProcessor(Date.class, jsonProcessor);
			//过滤集合属性end
			JSONObject jsonObj = JSONObject.fromObject(obj, config);
			String objStr = cellToString(jsonObj);
			buf.append("{").append(DataStoreParamList.STATUS).append(":\"")
					.append(status).append("\", ").append(
							DataStoreParamList.CELL).append(":{")
					.append(objStr).append("}}, ");
		}
		String s = buf.toString();
		return s.substring(0, s.length() - 2);
	}

	@SuppressWarnings("unchecked")
	private static String cellToString(JSONObject jsonObj) {
		StringBuffer buf = new StringBuffer();
		Iterator iter = jsonObj.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry it = (Map.Entry) (Map.Entry) iter.next();

			buf.append("\"");
			String property = (String) it.getKey();
			buf.append(property).append("\":\"");
			String value = String.valueOf(it.getValue());
			buf.append(value).append("\", ");
		}

		String s = buf.toString();
		return s.substring(0, s.length() - 2);
	}
	
	private static Object getEntity(Map<String,Object> mm,Class<?> zclass){
		Object obj = null;
		try{
			obj = zclass.newInstance();
			Method[] methods = zclass.getMethods();
			for(Method method : methods){
				String methodname = method.getName();
				if(methodname.startsWith("set")){
					Type[] methodParams = method.getParameterTypes();
					String attributeName = method.getName().substring(3, method.getName().length());  
					if(methodParams.length == 1){//如果set方法的参数个数是
						Object defValue = null;
						boolean defLock = false;
						Object  oValues = (Object)(mm.get(attributeName.toLowerCase())!=null? mm.get(attributeName.toLowerCase()) : mm.get(attributeName.toUpperCase()));
						try {
							if(methodParams[0].equals(Integer.TYPE)){
								defValue = -1;//失败默认值
								method.invoke(obj, Integer.parseInt(oValues.toString()));
							}else if(methodParams[0].equals(String.class)){//如果set方法的参数类型是String类型
								defValue = "";//失败默认值 
								method.invoke(obj,oValues.toString());
							}else if(methodParams[0].equals(Long.TYPE)){
								defValue = -1L;//失败默认值  
								method.invoke(obj, new Long(oValues.toString()));
							}else if(methodParams[0].equals(Date.class)){
								defValue = new Date();
								
								SimpleDateFormat sdf;
								if(oValues.toString().toString().length()>10){
									sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								}else{
									sdf = new SimpleDateFormat("yyyy-MM-dd");
								}
								Date date = sdf.parse(oValues.toString() );
								method.invoke(obj,date);
							}else if(methodParams[0].equals(BigDecimal.class)){
								defValue = 0;
								method.invoke(obj, new BigDecimal(oValues.toString()));
							}else if(methodParams[0].equals(Timestamp.class)){
								defValue = new Date();
								SimpleDateFormat sdf;
								if(oValues.toString().toString().length()>10){
									sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								}else{
									sdf = new SimpleDateFormat("yyyy-MM-dd");
								}
								Date date = sdf.parse(oValues.toString());
								method.invoke(obj,new Timestamp(date.getTime()));
							}
						} catch (Exception e) {
							if(!defLock){  
								try{  
									method.invoke(obj,defValue);//尝试使用默认值   
								}catch(Exception s){ 
									
								}  
							} 
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
}
