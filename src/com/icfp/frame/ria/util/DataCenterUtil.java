package com.icfp.frame.ria.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.datastore.util.DataStoreParamList;
import com.icfp.frame.datastore.util.JsonDataStoreUtil;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;


/**数据中心工具
 * @author wangxing 2011-09-09
 */
public class DataCenterUtil {
	
	private static Logger logger = Logger.getLogger(DataCenterUtil.class);

	@SuppressWarnings("unchecked")
	public static RequestEnvelope wrapped(HttpServletRequest request){
		logger.info("开始封装请求信息   >>> >>>");
		RequestEnvelope rqev = new RequestEnvelope();
		try{
			String jsonString = getFromRequest(request);
			JSONObject jsonObj = JSONObject.fromObject(jsonString);
			JSONObject paramsObj = jsonObj.getJSONObject(RiaParamList.REQUEST_BODY).getJSONObject(RiaParamList.BODY_PARAMETERS);
			Iterator items = paramsObj.entrySet().iterator();
			Object originValue = null;
			Object value = null;
			while (items.hasNext()) {
				Map.Entry entry = (Map.Entry) items.next();
				originValue = entry.getValue();
				value = null;
				if ((originValue instanceof JSONObject)) {
					if (!((JSONObject) originValue).isNullObject())
						value = ((JSONObject) originValue).toString();
				} else if ((originValue instanceof JSONArray))
					value = ((JSONArray) originValue).toArray();
				else {
					value = originValue;
				}
				rqev.getBody().addParameter((String) entry.getKey(), value);
			}
			
			JSONObject dataStoresObj = jsonObj.getJSONObject(
					RiaParamList.REQUEST_BODY).getJSONObject(
					RiaParamList.BODY_DATASTORES);
			items = dataStoresObj.entrySet().iterator();

			while (items.hasNext()) {
				Map.Entry entry = (Map.Entry) items.next();
				String dataStoreID = (String) entry.getKey();
				Object storeStr = entry.getValue();
				DataStore store = JsonDataStoreUtil.jsonStrToDataStore(storeStr.toString());
				store.separateRowSet(); //进行数据分类操作
				rqev.getBody().addParameter(dataStoreID, store);
			}
			logger.info("请求详细信息： ");
			log4Console(jsonString);
			logger.info("封装请求信息成功 ！<<< <<< ");
		}catch (Exception e) {
			//e.printStackTrace();
		}
		return rqev;

	}
	
	
	@SuppressWarnings("unchecked")
	public static RequestEnvelope wrapped(Object jsonstr){
		logger.info("开始封装请求信息   >>> >>>");
		RequestEnvelope rqev = new RequestEnvelope();
		try {
			JSONObject jsonObj = JSONObject.fromObject(jsonstr);
			JSONObject paramsObj = jsonObj.getJSONObject(RiaParamList.REQUEST_BODY).getJSONObject(RiaParamList.BODY_PARAMETERS);
			Iterator items = paramsObj.entrySet().iterator();
			Object originValue = null;
			Object value = null;
			while (items.hasNext()) {
				Map.Entry entry = (Map.Entry) items.next();
				originValue = entry.getValue();
				value = null;
				if ((originValue instanceof JSONObject)) {
					if (!((JSONObject) originValue).isNullObject())
						value = ((JSONObject) originValue).toString();
				} else if ((originValue instanceof JSONArray))
					value = ((JSONArray) originValue).toArray();
				else {
					value = originValue;
				}
				rqev.getBody().addParameter((String) entry.getKey(), value);
			}
			
			JSONObject dataStoresObj = jsonObj.getJSONObject(
					RiaParamList.REQUEST_BODY).getJSONObject(
					RiaParamList.BODY_DATASTORES);
			items = dataStoresObj.entrySet().iterator();

			while (items.hasNext()) {
				Map.Entry entry = (Map.Entry) items.next();
				String dataStoreID = (String) entry.getKey();
				Object storeStr = entry.getValue();
				DataStore store = JsonDataStoreUtil.jsonStrToDataStore(storeStr.toString());
				store.separateRowSet(); //进行数据分类操作
				rqev.getBody().addParameter(dataStoreID, store);
			}
			logger.info("请求详细信息： ");
			log4Console(jsonstr.toString());
			logger.info("封装请求信息成功 ！<<< <<< ");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return rqev;

	}

	private static String getFromRequest(HttpServletRequest request) {
		String jsonRequest = "";
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = request.getInputStream();
			reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			int c;
			while ((c = reader.read()) != -1) {
				sb.append((char) c);
			}
			jsonRequest = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Requst请求数据：" + jsonRequest);
		return jsonRequest;
	}

	public static String toResponseString(ResponseEnvelope responseEnvelope) {
		logger.info("开始封装响应信息   >>> >>>");
		JSONObject jsono = new JSONObject();
		JSONObject head = new JSONObject();
		JSONObject body = new JSONObject();
		JSONObject message = new JSONObject();
		try{
			message.put(RiaParamList.MESSAGE_TITLE,responseEnvelope.getHeader().getMsg());
			message.put(RiaParamList.MESSAGE_DETAIL,responseEnvelope.getHeader().getDetailMsg());
			head.put(RiaParamList.HEAD_CODE, responseEnvelope.getHeader().getAppCode());
			head.put(RiaParamList.HEAD_MESSAGE,message);
			jsono.put(RiaParamList.REQUEST_HEAD, head);
			body = getDataStoreBody(responseEnvelope.getBody().getAllParameters());
			jsono.put(RiaParamList.REQUEST_BODY, body);
			/*StringBuffer dataCenter = new StringBuffer();
			dataCenter.append("{")
					  .append(RiaParamList.REQUEST_HEAD + ":{")
					  .append(RiaParamList.HEAD_CODE + ":")
					  .append(responseEnvelope.getHeader().getAppCode())
					  .append("," + RiaParamList.HEAD_MESSAGE + ":{"+ RiaParamList.MESSAGE_TITLE + ":\"")
					  .append(responseEnvelope.getHeader().getMsg())
					  .append("\",")
					  .append(RiaParamList.MESSAGE_DETAIL + ":\"")
					  .append(responseEnvelope.getHeader().getDetailMsg())
					  .append("\"}},");
			
			String paramsString = getParamsString(responseEnvelope.getBody().getAllParameters());
			dataCenter.append(paramsString)
					  .append("}");
			String respString = dataCenter.toString();*/
			logger.info("响应详细信息： ");
			//log4Console(jsono.toString());
			logger.info("封装响应信息成功 ！<<< <<< ");
		}catch (Exception e) {
		}
		return jsono.toString();
	}
	
	private static JSONObject getDataStoreBody(HashMap<String,Object> allParameters){
		JSONObject body = new JSONObject();
		JSONObject bodypams = new JSONObject();
		JSONObject bodydatastore = new JSONObject();
		HashMap<String, Object> params = new HashMap<String, Object>();
		HashMap<String, DataStore> dataStores = new HashMap<String, DataStore>();
		Iterator<?> tempIterator = allParameters.entrySet().iterator();
		while(tempIterator.hasNext()){
			Entry<?,?> tempEntry= (Entry<?,?>)tempIterator.next();
		    String parameterName = (String)tempEntry.getKey();
		    Object obj = tempEntry.getValue();
		    if(obj instanceof DataStore) {
		    	dataStores.put(parameterName, (DataStore)obj);
		    }else {
		    	params.put(parameterName, obj);
		    	
		    }
		}
		Iterator<?> paramsIterator = params.entrySet().iterator();
		while(paramsIterator.hasNext()){
			Entry<?,?> tempEntry= (Entry<?,?>)paramsIterator.next();
		    Object paraID = (String)tempEntry.getKey();
		    if(paraID == null) 
		    	paraID = "";
		    Object parameter = tempEntry.getValue();
		    if(parameter == null)
		    	parameter = "";
		    bodypams.put(paraID.toString(),parameter.toString());
		}
		body.put(RiaParamList.BODY_PARAMETERS, bodypams);
		Iterator<?> dataStoreIterator = dataStores.entrySet().iterator();
		while(dataStoreIterator.hasNext()){
			Entry<?,?> tempEntry= (Entry<?,?>)dataStoreIterator.next();
		    String dsID = (String)tempEntry.getKey();
		    DataStore dataStore = (DataStore)tempEntry.getValue();
		    bodydatastore.put(dsID, dataStore.toJsonObj());
		}
		body.put(RiaParamList.BODY_DATASTORES,bodydatastore);
		return body;
	}
	

	private static String getParamsString(HashMap allParameters) {
		StringBuffer bodyBuf = new StringBuffer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		HashMap<String, DataStore> dataStores = new HashMap<String, DataStore>();
		Iterator tempIterator = allParameters.entrySet().iterator();
		while(tempIterator.hasNext()){
			Entry tempEntry= (Entry)tempIterator.next();
		    String parameterName = (String)tempEntry.getKey();
		    Object obj = tempEntry.getValue();
		    if(obj instanceof DataStore) {
		    	dataStores.put(parameterName, (DataStore)obj);
		    }else {
		    	params.put(parameterName, obj);
		    	
		    }
		}
		
		bodyBuf.append(RiaParamList.REQUEST_BODY + ":")
				.append("{" + RiaParamList.BODY_PARAMETERS + ":{");
		
		Iterator paramsIterator = params.entrySet().iterator();
		while(paramsIterator.hasNext()){
			Entry tempEntry= (Entry)paramsIterator.next();
		    Object paraID = (String)tempEntry.getKey();
		    if(paraID == null) 
		    	paraID = "";
		    Object parameter = tempEntry.getValue();
		    if(parameter == null)
		    	parameter = "";
		    bodyBuf.append("\"" + paraID.toString() + "\":\"")
		    		.append(parameter.toString())
		    		.append("\",");
		}
		if(params != null && params.size() != 0)
		bodyBuf = new StringBuffer(bodyBuf.toString().substring(0, bodyBuf.toString().length() - 1));
		bodyBuf.append("},").append(RiaParamList.BODY_DATASTORES + ":{");
		
		Iterator dataStoreIterator = dataStores.entrySet().iterator();
		while(dataStoreIterator.hasNext()){
			Entry tempEntry= (Entry)dataStoreIterator.next();
		    String dsID = (String)tempEntry.getKey();
		    DataStore dataStore = (DataStore)tempEntry.getValue();
		    bodyBuf.append("\"" + dsID + "\":")
		    		.append(dataStore.toJsonStr())
		    		.append(",");
		}
		if(dataStores != null && dataStores.size() != 0)
		bodyBuf = new StringBuffer(bodyBuf.toString().substring(0, bodyBuf.toString().length() - 1));
		bodyBuf.append("}}");
		return bodyBuf.toString();
	}
	
	private static void log4Console(String riaString) {
		JSONObject jsonObj = JSONObject.fromObject(riaString);
		logger.info(RiaParamList.REQUEST_HEAD + " >> " + jsonObj.getString(RiaParamList.REQUEST_HEAD));
		logger.info(RiaParamList.REQUEST_BODY + " >> ");
		logger.info("	" + RiaParamList.BODY_PARAMETERS + " >> ");
		JSONObject paramsObj = jsonObj.getJSONObject(RiaParamList.REQUEST_BODY)
				.getJSONObject(RiaParamList.BODY_PARAMETERS);
		
		Iterator items = paramsObj.entrySet().iterator();
		while (items.hasNext()) {
			Map.Entry entry = (Map.Entry) items.next();
			logger.info("			" + entry.getKey() + " : " + entry.getValue());
		}
		
		logger.info("	" + RiaParamList.BODY_DATASTORES + " >> ");
		JSONObject dataStoresObj = jsonObj.getJSONObject(RiaParamList.REQUEST_BODY)
										  .getJSONObject(RiaParamList.BODY_DATASTORES);
		items = dataStoresObj.entrySet().iterator();
		while (items.hasNext()) {
			Map.Entry entry = (Map.Entry) items.next();
			String dataStoreID = (String) entry.getKey();
			JSONObject dsObj = dataStoresObj.getJSONObject(dataStoreID);
			logger.info("			" + dataStoreID + " > ");
			logger.info("			  " + DataStoreParamList.BIZID + " : " + dsObj.getString(DataStoreParamList.BIZID));
			logger.info("		  	  " + DataStoreParamList.DATATYPE + " : " + dsObj.getString(DataStoreParamList.DATATYPE));
			logger.info("		  	  " + DataStoreParamList.TOTALPAGE + " : " + dsObj.getString(DataStoreParamList.TOTALPAGE));
			logger.info("		 	  " + DataStoreParamList.PAGESIZE + " : " + dsObj.getString(DataStoreParamList.PAGESIZE));
			logger.info("		 	  " + DataStoreParamList.PAGENO + " : " + dsObj.getString(DataStoreParamList.PAGENO));
			logger.info("		  	  " + DataStoreParamList.ROWCOUNT + " : " + dsObj.getString(DataStoreParamList.ROWCOUNT));
			logger.info("		  	  " + DataStoreParamList.ORDERING + " : " + dsObj.getString(DataStoreParamList.ORDERING));
			
			JSONArray params = dsObj.getJSONArray(DataStoreParamList.PARAMS);
			StringBuffer bf = new StringBuffer();
			for(int i=0; i<params.size(); i++) {
				JSONObject param = params.getJSONObject(i);
				bf.append("[" + param.getString(DataStoreParamList.PARAMTYPE) + ", " + param.getString(DataStoreParamList.PARAMVALUE) + "] ");
			}
			logger.info("		  	  " + DataStoreParamList.PARAMS + " : " + bf.toString());
			
			logger.info("		  	  " + DataStoreParamList.ROWSET + " > ");
			JSONArray rowsets = dsObj.getJSONArray(DataStoreParamList.ROWSET);
			for(int i=1; i<=rowsets.size(); i++) {
				JSONObject row = rowsets.getJSONObject(i - 1);
				logger.info("		" + i + " > " + row.getString(DataStoreParamList.STATUS) + " : " + 
						row.getString(DataStoreParamList.CELL));
			}
		}
		
	}
}

