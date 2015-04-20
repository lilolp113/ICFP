package com.icfp.frame.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.icfp.frame.dao.base.Condition;
import com.icfp.frame.dao.base.Updater;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.ria.request.RequestEnvelope;

/**
 * 数据交互基本操作类
 * @author liufei
 *
 * @param <T>
 */
public interface BaseDao {
	
	/**
	 * 获取分页数据信息返回DataStore
	 * @param dataStore
	 * @return store
	 */
	public DataStore query(RequestEnvelope requestEnvelope,DataStore dataStore);
	
	
	/**
	 * 添加信息
	 * @param requestEnvelope
	 * @param dataStore
	 * @param autoPK
	 */
	public void insert(RequestEnvelope requestEnvelope,DataStore dataStore,boolean autoPK);
	
	/**
	 * 删除信息/假象删除
	 * @param dataStore
	 */
	public void delete(DataStore dataStore);
	
	/**
	 * 真实性删除
	 * @param dataStore
	 */
	public void realDel(DataStore dataStore);
	
	/**
	 * 修改信息,并发控制
	 * @param dataStore
	 */
	public void update(DataStore dataStore);
	
	/**
	 * 修改信息，不进行并发控制
	 * @param dataStore
	 */
	public void updateWithNoCur(DataStore dataStore);
	

	public Object get(Serializable id);

	/**
	 * 通过ID查找对象,不锁定对象
	 * 
	 * @param id
	 *            记录的ID
	 * @return 实体对象
	 */
	public Object load(Serializable id);

	/**
	 * 通过实例对象查找对象列表，分页
	 * 
	 * @param eg
	 *            实例对象
	 * @param anyWhere
	 *            是否模糊查询，默认false。
	 * @param conds
	 *            排序和is null的字段。分别为OrderBy和String。
	 * @param exclude
	 *            需要排除的属性
	 * @return 对象列表
	 */
	public List<Object> findByEgList(Object eg, boolean anyWhere, Condition[] conds,
			int firstResult, int maxResult, String... exclude);


	/**
	 * 按属性查找对象列表.
	 */
	public List<Object> findByProperty(String property, Object value);

	/**
	 * 按属性查找唯一对象.
	 */
	public Object findUniqueByProperty(String property, Object value);

	/**
	 * 按属性查找对象的数量
	 * 
	 * @param property
	 * @param value
	 * @return
	 */
	public int countByProperty(String property, Object value);
	
	/**
	 * 根据Updater更新对象
	 * 
	 * @param updater
	 * @return 持久化对象
	 */
	public Object updateByUpdater(Updater updater);

	public Object updateDefault(Object entity);

	/**
	 * 保存对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	public Object save(Object entity);

	/**
	 * 更新对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	public Object update(Object entity);

	/**
	 * 保存或更新对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	public Object saveOrUpdate(Object entity);

	/**
	 * 保存或更新对象拷贝
	 * 
	 * @param entity
	 * @return 已更新的持久化对象
	 */
	public Object merge(Object entity);

	/**
	 * 删除对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void executeDataStore(DataStore dataStore);

	/**
	 * 根据ID删除记录
	 * 
	 * @param id
	 *            记录ID
	 */
	public Object deleteById(Serializable id);
	
	/**
	 * 批量删除
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void deleteAll(List list);

	/**
	 * 刷新对象
	 * 
	 * @param entity
	 */
	public void refresh(Object entity);

	/**
	 * 获得实体Class
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class getPersistentClass();

	/**
	 * 创建实体类的对象
	 * 
	 * @return
	 */
	public Object createNewEntiey();
	
	/**
	 * 执行sql语句进行相关增删改操作
	 */
	public boolean executeSql(final String sql);
	
	/**
	 * 执行sql语句进行查询操作
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List executeQuery(final String sql);
	
	/**
	 * 执行sql语句进行查询，返回对象实体
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object executeUniqueByClass(final String sql,final Class obj);
	
	/**
	 * 通过实例对象查找对象列表
	 */
	@SuppressWarnings("unchecked")
	public List executeQueryByClass(final String sql,final Class obj);
	
	/**
	 * 执行sql语句获取相应列表 返回结果 键值对
	 */
	public List executeQueryMap(final String sql);
	
	public Date getSysDate();
	
	public String getStringPK();
	
	public BigDecimal getBigDecimalPK();
	
	@SuppressWarnings("unchecked")
	public List callProcedure_Object(List procList);
	
	public String getAreaCoding(RequestEnvelope requestEnvelope);
	
	/**获取系统时间 精确到秒 
	 * 2012-11-27
	 * @return date
	 */
	public Date getSysDateTime(); 
	
	public List executeQueryMapByPage(final DataStore dataStore);
}
