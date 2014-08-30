package com.icfp.frame.dao;

import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.ria.request.RequestEnvelope;

public interface CoreDao extends BaseDao{

	/**
	 * 获取所要查询对象信息
	 * @param revp
	 * @param dataStore
	 * @return
	 */
	public DataStore queryObject(RequestEnvelope revp,DataStore dataStore);
	
	/**
	 * 添加对象信息
	 * @param revp
	 * @param dataStore
	 */
	public void executeAddObject(RequestEnvelope revp,DataStore dataStore);
	
	/**
	 * 修改对象信息
	 * @param dataStore
	 */
	public void executeEdtObject(DataStore dataStore);
	
	/**
	 * 删除对象信息
	 * @param dataStore
	 */
	public void executeDelObject(DataStore dataStore);
	
	/**
	 * 真实性删除对象信息
	 * @param dataStore
	 */
	public void executeRealDelObj(DataStore dataStore);
	
}
