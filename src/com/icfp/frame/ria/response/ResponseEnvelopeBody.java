package com.icfp.frame.ria.response;

import java.util.HashMap;

/**返回数据信息
 * 2011-08-25
 * @author wangxing
 *
 */
public class ResponseEnvelopeBody {
	
	private HashMap<String,Object> body = new HashMap<String,Object>();

	/**
	 * 添加参数
	 * @param paramName 参数名称 
	 * @param paramValue 参数值
	 */
	public void addParameter(String paramName, Object paramValue) {
		if (this.body.containsKey(paramName)) {
			removeParameter(paramName);
		}
		this.body.put(paramName, paramValue);
	}

	/**
	 * 移除参数
	 * @param paramName 参数名
	 */
	public void removeParameter(String paramName) {
		this.body.remove(paramName);
	}

	/**
	 * 移除所有参数
	 */
	public void removeAllParameters() {
		this.body.clear();
	}

	/**
	 * 获取参数值
	 * @param paramName 参数名
	 * @return
	 */
	public Object getParameter(String paramName) {
		if (this.body.containsKey(paramName)) {
			return this.body.get(paramName);
		}

		return null;
	}

	public HashMap<String,Object> getAllParameters() {
		return this.body;
	}

}
