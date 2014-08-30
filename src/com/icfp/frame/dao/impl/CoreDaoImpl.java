package com.icfp.frame.dao.impl;

import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.ria.request.RequestEnvelope;

/**
 * 核心DAO
 * @author liufei
 *
 */
public class CoreDaoImpl extends BaseDaoImpl implements CoreDao {
	
	/**
	 * 获取所要查询对象信息
	 * @param revp
	 * @param dataStore
	 * @return
	 */
	public DataStore queryObject(RequestEnvelope revp,DataStore dataStore)
	{
		return super.query(revp, dataStore);
	}
	
	/**
	 * 添加对象信息
	 * @param revp
	 * @param dataStore
	 */
	public void executeAddObject(RequestEnvelope revp,DataStore dataStore)
	{
		super.insert(revp, dataStore);
	}
	
	/**
	 * 修改对象信息
	 * @param dataStore
	 */
	public void executeEdtObject(DataStore dataStore)
	{
		super.update(dataStore);
	}
	
	/**
	 * 删除对象信息
	 * @param dataStore
	 */
	public void executeDelObject(DataStore dataStore)
	{
		super.delete(dataStore);
	}
	
	/**
	 * 真实性删除对象信息
	 * @param dataStore
	 */
	public void executeRealDelObj(DataStore dataStore)
	{
		super.realDel(dataStore);
	}
	
}