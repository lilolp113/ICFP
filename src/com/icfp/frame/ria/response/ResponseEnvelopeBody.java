package com.icfp.frame.ria.response;

import java.util.HashMap;

/**返回数据信息
 * 2011-08-25
 * @author wangxing
 *
 */
public class ResponseEnvelopeBody {
	private HashMap body = new HashMap();

	@SuppressWarnings("unchecked")
	public void addParameter(String paramName, Object paramValue) {
		if (this.body.containsKey(paramName)) {
			removeParameter(paramName);
		}
		this.body.put(paramName, paramValue);
	}

	public void removeParameter(String paramName) {
		this.body.remove(paramName);
	}

	public void removeAllParameters() {
		this.body.clear();
	}

	public Object getParameter(String paramName) {
		if (this.body.containsKey(paramName)) {
			return this.body.get(paramName);
		}

		return null;
	}

	public HashMap getAllParameters() {
		return this.body;
	}

}
