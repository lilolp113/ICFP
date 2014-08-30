package com.icfp.frame.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.icfp.frame.dao.BaseDao;
import com.icfp.frame.dao.base.Finder;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.datastore.util.DataStoreParamList;
import com.icfp.frame.params.DaoParamList;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.params.SysParamsList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.util.ProcUtil;
import com.management.entity.SA05;

/**
 * DAO基类。
 * 
 * 提供hql分页查询，example分页查询，拷贝更新等功能。
 * 
 * @author liufei
 * 
 * @param <T>
 */
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 添加信息
	 * @param requestEnvelope
	 * @param dataStore
	 * @param autoPK 是否自动添加主键
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void insert(RequestEnvelope requestEnvelope,DataStore dataStore)
	{
		if (dataStore == null) {
			logger.error("添加方法中DataStore为空");
			return;
		}
		List<Object> insertRowSet = dataStore.getInsertRowSet();
		if (insertRowSet == null || insertRowSet.size() == 0) {
			logger.error("待添加数据为空，类型[" + dataStore.getDataType() + "]");
			return;
		}
		int size = insertRowSet.size();
		logger.info("待添加数据: 类型[" + dataStore.getDataType() + "], 个数[" + size
				+ "]");
		Class dataClass=this.getClass(dataStore.getDataType());   //实例化对象
		
		String operator = getOperator(requestEnvelope);// 得到经办人
		Class[] parameterTypes = { String.class };
		Object[] arglist = { operator };
		
		Date date=this.getSysDateTime();   //获取当前系统时间
		Class[] parameterTypes2 = { Date.class };
		Object[] arglist2 = { date };
		logger.info("经办人[" + operator + "], 经办时间["
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "]");
		
		// 设置并发字段反射参数
		Class[] parameterTypes3 = { BigDecimal.class }; // setZZD002(BigDecimal)，setZZD003(BigDecimal)
		Object[] arglist3 = { new BigDecimal(0) };
		
		Method m1 = null; // 设置经办人
		Method m2 = null; // 设置经办时间
		Method m3 = null; // 设置并发标志
		Method m4 = null; // 设置删除标志
		Method m5 = null; // 设置有效标志
		
		Object[] props = this.getHibernateTemplate().getSessionFactory()
		.getClassMetadata(dataClass).getPropertyNames();
		
		try {
			if (Arrays.asList(props).contains(DaoParamList.OPERATOR))
				//m1 = dataClass.getMethod("set" + DaoParamList.OPERATOR,parameterTypes);
			if (Arrays.asList(props).contains(DaoParamList.OPERATE_DATE))
				//m2 = dataClass.getMethod("set" + DaoParamList.OPERATE_DATE,parameterTypes2);
			if (Arrays.asList(props).contains(DaoParamList.CONCURRENCY))
				m3 = dataClass.getMethod("set" + DaoParamList.CONCURRENCY,parameterTypes3);
			if (Arrays.asList(props).contains(DaoParamList.DELETE_FLAG))
				m4 = dataClass.getMethod("set" + DaoParamList.DELETE_FLAG,parameterTypes);
			if (Arrays.asList(props).contains(DaoParamList.VALID_FLAG))
				m5 = dataClass.getMethod("set" + DaoParamList.VALID_FLAG,parameterTypes);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		for(int i=0;i<size;i++)
		{
			Object obj=insertRowSet.get(i);
			if (dataStore.isAutoPK()) {// 需要生成主键
				Serializable autoPKString = null;
				String idType = this.getHibernateTemplate().getSessionFactory()
						.getClassMetadata(dataClass).getIdentifierType()
						.getName(); // 对象主键类型
				if (idType.equals("string") || idType.equals("big_decimal")) { // 普通主键
					if (idType.equals("string")) { // 普通string主键
						autoPKString = getStringPK();
					} else { // 普通big_decimal主键
						autoPKString = getBigDecimalPK();
					}
				} else { // 联合主键
					logger.error("改对象对应数据库表采用联合主键，类型为[" + idType
							+ "]，ICFP v1.0版本不支持此类主键生成，请自行主键赋值！");
					return;
				}
				logger.info("该添加操作，系统生成主键[" + autoPKString + "]");
				this.getHibernateTemplate().getSessionFactory().getClassMetadata(dataClass).setIdentifier(obj, autoPKString,EntityMode.POJO); // 设置主键
			}
			
			try {
				if (m1 != null)
					//m1.invoke(obj, arglist);
				if (m2 != null)
					//m2.invoke(obj, arglist2);
				if (m3 != null)
					m3.invoke(obj, arglist3);
				if (m4 != null)
					m4.invoke(obj, "0");
				if (m5 != null)
					m5.invoke(obj, "1");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.getHibernateTemplate().save(obj);
			
			this.getHibernateTemplate().flush();
			
			if (i % DaoParamList.INSERT_SIZE == 0) {
				this.getHibernateTemplate().clear();
			}
		}
	}
	
	
	/**
	 * 删除信息、假象删除
	 * @param dataStore
	 */
	public void delete(DataStore dataStore)
	{
		if (dataStore == null) {
			logger.error("删除方法中DataStore为空");
			return;
		}
		List<Object> delRowSet = dataStore.getDeleteRowSet();
		if (delRowSet == null || delRowSet.size() == 0) {
			logger.error("待删除数据为空，类型[" + dataStore.getDataType() + "]");
			return;
		}
		int size = delRowSet.size();
		logger.info("待删除数据: 类型[" + dataStore.getDataType() + "], 个数[" + size
				+ "]");
		for(int i=0;i<size;i++)
		{
			Object object=delRowSet.get(i);
			this.deleteOrUpdate(getHibernateTemplate(), object,DataStoreParamList.STATUS_UPDATE,false);
			if (i % DaoParamList.INSERT_SIZE == 0) {
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
			}
		}
	}
	
	/**
	 * 删除数据，真实删除
	 * @param dataStore
	 */
	public void realDel(DataStore dataStore)
	{
		if (dataStore == null) {
			logger.error("删除方法中DataStore为空");
			return;
		}
		List<Object> delRowSet = dataStore.getDeleteRowSet();
		if (delRowSet == null || delRowSet.size() == 0) {
			logger.error("待删除数据为空，类型[" + dataStore.getDataType() + "]");
			return;
		}
		int size = delRowSet.size();
		logger.info("待删除数据: 类型[" + dataStore.getDataType() + "], 个数[" + size
				+ "]");
		for(int i=0;i<size;i++)
		{
			Object object=delRowSet.get(i);
			this.deleteOrUpdate(getHibernateTemplate(), object,DataStoreParamList.STATUS_DELETE,true);
			if (i % DaoParamList.INSERT_SIZE == 0) {
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
			}
		}
	}
	
	/**
	 * 修改信息
	 * @param dataStore
	 */
	public void update(DataStore dataStore)
	{
		if (dataStore == null) {
			logger.error("修改方法中DataStore为空");
			return;
		}
		List<Object> updateRowSet = dataStore.getUpdateRowSet();
		if (updateRowSet == null || updateRowSet.size() == 0) {
			logger.error("待修改数据为空，类型[" + dataStore.getDataType() + "]");
			return;
		}
		int size = updateRowSet.size();
		logger.info("待修改数据: 类型[" + dataStore.getDataType() + "], 个数[" + size
				+ "]");
		
		for(int i=0;i<size;i++)
		{
			Object object=updateRowSet.get(i);
			this.deleteOrUpdate(getHibernateTemplate(), object, DataStoreParamList.STATUS_UPDATE,true);
			if (i % DaoParamList.INSERT_SIZE == 0) {
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
			}
		}
	}
	
	/**
	 * 修改操作，不进行并发控制
	 */
	public void updateWithNoCur(DataStore dataStore)
	{
		if (dataStore == null) {
			logger.error("修改方法中DataStore为空");
			return;
		}
		List<Object> updateRowSet = dataStore.getUpdateRowSet();
		if (updateRowSet == null || updateRowSet.size() == 0) {
			logger.error("待修改数据为空，类型[" + dataStore.getDataType() + "]");
			return;
		}
		int size = updateRowSet.size();
		logger.info("待修改数据: 类型[" + dataStore.getDataType() + "], 个数[" + size
				+ "]");
		
		for(int i=0;i<size;i++)
		{
			Object object=updateRowSet.get(i);
			this.merge(object);
			if (i % DaoParamList.INSERT_SIZE == 0) {
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
			}
		}
	}
	
	/**
	 * 修改信息
	 * @param hibernateTemplate
	 * @param object
	 * @param status
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	private boolean deleteOrUpdate(HibernateTemplate hibernateTemplate,Object object,String status,boolean sign)
	{
		String dataType = object.getClass().getName();      
//		String tableName = object.getClass().getSimpleName();     //得到表名
		Class dataClass=this.getClass(dataType);   //实例化对象
		String idKey = hibernateTemplate.getSessionFactory().getClassMetadata(
				dataClass).getIdentifierPropertyName();// 对象主键名
		Serializable vid = hibernateTemplate.getSessionFactory().getClassMetadata(dataClass).getIdentifier(object,EntityMode.POJO);// 对象主键值
		String idType = hibernateTemplate.getSessionFactory().getClassMetadata(
				dataClass).getIdentifierType().getName(); // 对象主键类型
		logger.info("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "=" + vid
				+ "], 主键类型[" + idType + "]");
		Object po = hibernateTemplate.get(object.getClass(), vid);
		if (po == null) {
			logger.error("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "="
					+ vid + "]已被删除，操作失败！");
			return false;
		}
		getHibernateTemplate().evict(po);     //移除session中相同标示对象,使其托管
		if (status.equals(DataStoreParamList.STATUS_DELETE)&& sign) {
				
			hibernateTemplate.delete(object);
			hibernateTemplate.flush();
			return true;
		}else if(!sign){
			Class[] parameterTypes1 = { String.class };
			Method m1 = null;
			try {
				m1 = dataClass.getMethod("set"
							+ DaoParamList.DELETE_FLAG, parameterTypes1);
				m1.invoke(object, "1");// 设置删除标志为1
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (status.equals(DataStoreParamList.STATUS_UPDATE)) {
			hibernateTemplate.merge(object);
			hibernateTemplate.flush();
			return true;
		}else{
			logger.error("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "="
					+ vid + "]已修改，操作失败！");
			return false;
		}
	}
	

	/**
	 * 获取分页数据信息返回DataStore
	 * @param dataStore
	 * @return store
	 */
	@SuppressWarnings("unchecked")
	public DataStore query(RequestEnvelope requestEnvelope,DataStore dataStore) {
		DataStore store=dataStore;
		if(store==null)
		{
			logger.error("查询方法中DataStore为空");
			return null;
		}
		store.setQueryRowSet(this.findAll(store, requestEnvelope,this.getHibernateTemplate()));
		return store;
	}

	/**
	 * 分页获取信息列表
	 * @param dataStore
	 * @return List 数据信息列表
	 */
	@SuppressWarnings("unchecked")
	protected List findAll(final DataStore dataStore,final RequestEnvelope requestEnvelope,final HibernateTemplate hibernateTemplate)
	{
		String bizId="";     // 交互编号
		bizId = (String) requestEnvelope.getBody().getParameter(RiaParamList.REQUESTID);
		final Class<?> dataClass = this.getClass(dataStore.getDataType());   //实例化对象
		String tablename=null;
		if(dataClass!=null && !"".equals(dataClass))
		{
			tablename = dataClass.getSimpleName();
		}
		// 是否启用VPD
		if(dataStore.isVpd())
		{
			logger.info("此次查询操作<启用VPD机制>");
			String areaCoding = getAreaCoding(requestEnvelope); // 区域编号
			boolean flag=ProcUtil.addContext(hibernateTemplate, areaCoding, bizId,tablename);
			if(flag)
			{
				logger.info("此次查询操作<启用VPD机制成功!>");
			}else{
				logger.info("此次查询操作<启用VPD机制失败!>");
			}
		}
		hibernateTemplate.clear();
		return hibernateTemplate.executeFind(new HibernateCallback()
	    {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
		        Query query = null;
		        Finder finder = null;
		        List dataList=null;
		        session.clear();
		        if ((dataStore.getSQL() != null) && (!"".equals(dataStore.getSQL())))
		        {
		          finder = Finder.create(dataStore);
		          if(dataStore.getPageSize() != -1)
		          {
		        	  int rowCount = BaseDaoImpl.this.countQueryResult(finder,dataStore);
			          dataStore.setRowCount(rowCount);
			          query = session.createSQLQuery(finder.getOrigHql());
			          if(dataStore.isMatchParams())
			          {
			        	  query = finder.matchParams(query,dataStore);
			          }
			          query.setFirstResult(dataStore.getFirstResult());
			          query.setMaxResults(dataStore.getPageSize());
		          }else{
			          query = session.createSQLQuery(finder.getOrigHql());
			          if(dataStore.isMatchParams())
			          {
			        	  query = finder.matchParams(query,dataStore);
			          }
		          }
		          if(dataStore.isManyTables()){
		        	  dataList=query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		          }else{
			        dataList=query.setResultTransformer(Transformers.aliasToBean(dataClass)).list();
		          }
		        }
		        if ((dataStore.getHQL() != null) && (!"".equals(dataStore.getHQL())))
		        {
		          finder = Finder.create(dataStore);
		          if(dataStore.getPageSize() != -1)
		          {
		        	  int rowCount = BaseDaoImpl.this.countQueryResult(finder,dataStore);
			          dataStore.setRowCount(rowCount);
			          query = session.createQuery(finder.getOrigHql());
			          if(dataStore.isMatchParams())
			          {
			        	  query = finder.matchParams(query,dataStore);
			          }
			          query.setFirstResult(dataStore.getFirstResult());
			          query.setMaxResults(dataStore.getPageSize());
		          }else{
		        	  query = session.createQuery(finder.getOrigHql());
			          if(dataStore.isMatchParams())
			          {
			        	  query = finder.matchParams(query,dataStore);
			          }
		          }
			      dataList=query.list();
		        }
		        session.flush();
		        return dataList;
			}
	    });
	}
	
	/**
	 * 获取数据单个对象，返回dataStore
	 * @param dataStore
	 * @return store
	 */
	public DataStore get(DataStore dataStore) {
		DataStore store=dataStore;
		if(store==null)
		{
			logger.error("查询方法中DataStore为空");
			return null;
		}
		store.setRowSet(findObj(dataStore));
		return store;
	}

	/**
	 * 按HSQL查询唯一对象.
	 * @return 对象实例
	 */
	public Object findObj(final DataStore dataStore)
	{
		final Class<?> dataClass = this.getClass(dataStore.getDataType());   //实例化对象
		getHibernateTemplate().clear();
	    return getHibernateTemplate().execute(new HibernateCallback()
	    {
	      public Object doInHibernate(Session session) throws HibernateException, SQLException {
	    	Query query=null;
	    	Finder finder = null;
	    	Object object=null;
	    	session.clear();
	    	if(dataStore.getHQL()!=null && !"".equals(dataStore.getHQL()))
	    	{
	    		finder = Finder.create(dataStore);
	    		query = session.createQuery(finder.getOrigHql());
	    		if(dataStore.isMatchParams())
	    		{
	    			query = finder.matchParams(query,dataStore);
	    		}
		        object=query.uniqueResult();
	    	}
	    	if(dataStore.getSQL()!=null && !"".equals(dataStore.getSQL()))
	    	{
	    		finder = Finder.create(dataStore);
	    		query = session.createSQLQuery(finder.getOrigHql());
	    		if(dataStore.isMatchParams())
	    		{
	    			query = finder.matchParams(query,dataStore);
	    		}
		        object=query.setResultTransformer(Transformers.aliasToBean(dataClass)).uniqueResult();
	    	}
	    	session.flush();
	        return object;
	      }
	    });
	  }
	
	/**
	 * 保存对象
	 * @param entity 保存对象实例
	 * @return 
	 */
	public Object save(Object entity) {
		Assert.notNull(entity);
		this.getHibernateTemplate().save(entity);
		this.getHibernateTemplate().flush();
		return entity;
	}

	/**
	 * 修改对象
	 * @param entity 对象实例
	 * @return Object 修改后对象实例
	 */
	public Object update(Object entity) {
		getHibernateTemplate().update(entity);
		getHibernateTemplate().flush();
		return entity;
	}

	/**
	 * 保存或更新对象拷贝
	 * @param entity
	 * @return 已更新的持久化对象
	 */
	public Object merge(Object entity) {
		Object object=getHibernateTemplate().merge(entity);
		this.refresh(object);
		getHibernateTemplate().flush();
		return object;
	}
	
	/**
	 * 根据ID删除记录
	 * 
	 * @param id
	 *            记录ID
	 */
	public void deleteById(Serializable id) {
		Assert.notNull(id);
		Object entity = load(id);
		getHibernateTemplate().delete(entity);
		getHibernateTemplate().flush();
	}
	
	/**
	 * 批量删除对象
	 */
	@SuppressWarnings("unchecked")
	public void deleteAll(List list)
	{
		if(list!=null && list.size()>0)
		{
			getHibernateTemplate().deleteAll(list);
			getHibernateTemplate().flush();
		}
	}

	/**
	 * 通过ID查找对象,不锁定对象
	 * @param id
	 *            记录的ID
	 * @return 实体对象
	 */
	public Object load(Serializable id) {
		Assert.notNull(id);
		return (Object)getHibernateTemplate().load(getPersistentClass(),id);
	}


	/**
	 * 按HQL查询对象列表.
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */
	@SuppressWarnings("unchecked")
	public List find(final String hql,final Object... values) {
		return getHibernateTemplate().executeFind(new HibernateCallback()
	    {
	      public Object doInHibernate(Session session) throws HibernateException, SQLException {
	        Query query = session.createQuery(hql);
	        if (values != null) {
	          for (int i = 0; i <values.length; i++) {
	            query.setParameter(i,values[i]);
	          }
	        }
	        return query.list();
	      }
	    });
	}

	/**
	 * 按HQL查询唯一对象.
	 * @param hql 查询语句
	 * @param values 参数
	 * @return Object 实体对象
	 */
	public Object findUnique(final String hql,final Object[] values)
	{
	    return getHibernateTemplate().execute(new HibernateCallback()
	    {
	      public Object doInHibernate(Session session) throws HibernateException, SQLException {
	        Query query = session.createQuery(hql);
	        if (values != null) {
	          for (int i = 0; i <values.length; i++) {
	            query.setParameter(i,values[i]);
	          }
	        }
	        return query.uniqueResult();
	      }
	    });
	  }

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	@SuppressWarnings("unused")
	private Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 执行存储于dataStore中的增删改操作
	 */
	public void executeDataStore(final DataStore dataStore) {
		if(dataStore.getSQL()!=null && !"".equals(dataStore.getSQL()))
		{
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException,
						SQLException {
					return session.createSQLQuery(dataStore.getSQL()).executeUpdate();
				}
			});
		}
	}
	
	/**
	 * 通过count查询获得本次查询所能获得的对象总数.
	 * @param finder
	 * @param dataStore
	 * @return 对象总数
	 */
	protected int countQueryResult(final Finder finder,final DataStore dataStore) {
		return (Integer)getHibernateTemplate().execute(new HibernateCallback()
	    {
	      public Object doInHibernate(Session session) throws HibernateException, SQLException {
	        Query query = null;
	        session.clear();
	        if ((dataStore.getSQL() != null) && (!"".equals(dataStore.getSQL())))
	        {
	          query = session.createSQLQuery(finder.getRowCountSql());
	        } else if ((dataStore.getHQL() != null) && (!"".equals(dataStore.getHQL())))
	        {
	          query = session.createQuery(finder.getRowCountHql());
	        }
	        if(dataStore.isMatchParams())
	        {
	        	finder.matchParams(query, dataStore);
	        }

	        return Integer.valueOf(Integer.parseInt(query.uniqueResult().toString()));
	      }
	    });
	}

	/**
	 * 对象更新
	 */
	private void refresh(Object entity) {
		getSession().refresh(entity);
	}


	@SuppressWarnings("unchecked")
	private Class persistentClass;

	@SuppressWarnings("unchecked")
	public Class getPersistentClass() {
		return persistentClass;
	}

	/**
	 * 新建对象实例
	 */
	public Object createNewEntiey() {
		try {
			return getPersistentClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("不能创建实体对象："
					+ getPersistentClass().getName());
		}
	}

	/**
	 * 获取对象元数据
	 * @param clazz
	 * @return 对象元数据
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private ClassMetadata getCmd(Class clazz) {
		return (ClassMetadata)this.getHibernateTemplate().getSessionFactory().getClassMetadata(clazz);
	}
	
	/**
	 * 执行sql语句进行相关增删改操作
	 * @param sql 操作语句
	 * @return true/false
	 */
	public boolean executeSql(final String sql) {
		int row=0;
		boolean flag=false;
		try{
			row=(Integer)getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException,
				SQLException {
					Query query=session.createSQLQuery(sql);
					int row = query.executeUpdate();
					session.flush();
					return row;
				}
			});
			logger.info("本次执行的sql语句为："+sql+",操作影响条数为："+row);
			flag=true;
		}catch (Exception e) {
			flag=false;
		}
		return flag;
	}
	
	/**
	 * 执行sql语句获取相应列表
	 * @param sql 查询语句
	 * @return List 数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List executeQuery(final String sql) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				session.clear();
				return session.createSQLQuery(sql).list();
			}
		});
	}
	
	
	/**
	 * 执行sql语句获取相应列表 返回结果 键值对
	 * @param sql需执行的sql语句
	 * @return List 数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List executeQueryMap(final String sql) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				session.clear();
				SQLQuery query=(SQLQuery)session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	
	/**
	 * 执行sql语句获取相应列表并进行分页， 返回结果 键值对
	 * @param dataStore 查询信息载体
	 * @return List 分页后数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List executeQueryMapByPage(final DataStore dataStore) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				session.clear();
				Query query=(Query)session.createSQLQuery(dataStore.getSQL()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				Finder finder = Finder.create(dataStore);
		        query = session.createSQLQuery(finder.getOrigHql());
		        //如果需进行参数自动匹配，则执行参数匹配方法进行匹配
		        if(dataStore.isMatchParams())
		        {
		        	query = finder.matchParams(query,dataStore);
		        }
		        //如果需要分页，则进行分页处理
		        if (dataStore.getPageSize() != -1) {
		        	int rowCount = BaseDaoImpl.this.countQueryResult(finder,dataStore);
			        dataStore.setRowCount(rowCount);
		            query.setFirstResult(dataStore.getFirstResult());
		            query.setMaxResults(dataStore.getPageSize());
		       }
		       return query.list();
			}
		});
	}
	
	
	
	/**
	 * 执行sql语句进行查询，返回对象实体
	 * @param sql 查询语句
	 * @return Object 对象实体
	 */
	@SuppressWarnings("unchecked")
	public Object executeUniqueByClass(final String sql,final Class obj)
	{
		HibernateCallback hibernatecallback = new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException, SQLException {
				Object queryObj=null;
				session.clear();
				SQLQuery query = session.createSQLQuery(sql);
				List list =query.setResultTransformer(Transformers.aliasToBean(obj)).list();
				session.flush();
		        if(list!=null && list.size()!=0)
		        {
		        	queryObj=list.get(0);
		        }
				return queryObj;
			}
		};
		return (Object) this.getHibernateTemplate().execute(hibernatecallback);
	}
	
	/**
	 * 通过sql语句获取信息列表并封装至相应对象
	 * @param sql 查询语句
	 * @param obj 对象实例
	 * @return List对象信息列表
	 */
	@SuppressWarnings("unchecked")
	public List executeQueryByClass(final String sql,final Class obj) {
		HibernateCallback hibernatecallback = new HibernateCallback() {
			public List doInHibernate(Session session)throws HibernateException, SQLException {
				session.clear();
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.setResultTransformer(Transformers.aliasToBean(obj)).list();
				session.flush();
				return list;
			}
		};
		return (List<?>) this.getHibernateTemplate().execute(hibernatecallback);
	}

	/**
	 * 对象实例化
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class getClass(String type) {
		if (type == null || "".equals(type)) {
			logger.error("DataStore中dataType对象类型为空！");
			return null;
		}
		Class dataClass = null;
		try {
			dataClass = Class.forName(type);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dataClass;
	}
	
	/**
	 * 通过存储过程得到系统时间
	 * @param hibernateTemplate
	 * @param procList
	 * @return Date
	 */
	@SuppressWarnings("unchecked")
	public Date getSysDateByProc() {
		List prcDateList = new ArrayList();
		prcDateList.add(DaoParamList.PRC_DATE);
		Map<String, String> outParam = new HashMap<String, String>();
		outParam.put("name", "currenttime ");
		outParam.put("type", "output");
		outParam.put("dataType", "String");
		prcDateList.add(outParam);
		String currenttime = (String) (ProcUtil.createProcedure_Object(
				this.getHibernateTemplate(), prcDateList).get(0));
		if (currenttime == null || currenttime.equals("")) {
			logger.error("存储过程获取当前时间异常");
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(currenttime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 通过数据库内置时间函数得到系统时间
	 * @param hibernateTemplate
	 * @param procList
	 * @return Date
	 */
	@SuppressWarnings("unchecked")
	public Date getSysDate()
	{
		String sql="select curdate()";
		List list=this.executeQuery(sql);
		//String str=(String)list.get(0);
		return (Date)list.get(0);
	}
	
	/**
	 * 生成String类型主键
	 * @param hibernateTemplate
	 * @return stringPK 主键
	 */
	@SuppressWarnings("unchecked")
	public String getStringPK() {
		List prcList = new ArrayList();
		prcList.add(DaoParamList.PRC_STRING_PK);
		Map<String, String> outParam = new HashMap<String, String>();
		outParam.put("name", "stringpk");
		outParam.put("type", "output");
		outParam.put("dataType", "String");
		prcList.add(outParam);
		String stringPK = (String) (ProcUtil.createProcedure_Object(this.getHibernateTemplate(), prcList).get(0));
		if (stringPK == null || stringPK.equals("")) {
			logger.error("存储过程生成主键异常");
			return null;
		}
		return stringPK;
	}
	
	/**
	 * 生成BigDecimal类型主键
	 * @param hibernateTemplate
	 * @return bigDecimalPK 主键
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getBigDecimalPK() {
		List prcList = new ArrayList();
		prcList.add(DaoParamList.PRC_BIGDECIMAL_PK);
		Map<String, String> outParam = new HashMap<String, String>();
		outParam.put("name", "big_decimalpk");
		outParam.put("type", "output");
		outParam.put("dataType", "DECIMAL");
		prcList.add(outParam);
		BigDecimal bigDecimalPK = (BigDecimal) (ProcUtil.createProcedure_Object(this.getHibernateTemplate(), prcList).get(0));
		if (bigDecimalPK == null || bigDecimalPK.equals("")) {
			logger.error("存储过程生成主键异常");
			return null;
		}
		return bigDecimalPK;
	}
	
	/**
	 * 获取用户全名
	 * @param requestEnvelope
	 * @return 用户全名
	 */
	public String getOperator(RequestEnvelope requestEnvelope) {
		Object loginUser = requestEnvelope.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		String operator = "";
		if (loginUser instanceof SA05) {
			operator = ((SA05) loginUser).getSAC004();
		}
		return operator;
	}
	
	/**
	 * 调用存储过程
	 * @param hibernateTemplate
	 * @return List 数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List callProcedure_Object(List procList) {
		return ProcUtil.createProcedure_Object(this.getHibernateTemplate(), procList);
	}
	
	/**
	 * 得到区域编号
	 * @param loginUser
	 * @return 区域编码
	 */
	public String getAreaCoding(RequestEnvelope requestEnvelope) {
		Object loginUser = requestEnvelope.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		String areaCoding = ""; // 区域编号
		Method m = null;
		try {
			m = SA05.class.getMethod("get" + DaoParamList.AREACODING);
			areaCoding = (String) m.invoke(loginUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return areaCoding;
	}

	/**
	 * 通过存储过程获取服务器系统时间
	 * @return 当前服务器系统时间
	 */
	@SuppressWarnings("unchecked")
	public Date getSysDateTimeByProc() {
		List prcDateList = new ArrayList();
		prcDateList.add(DaoParamList.PRC_DATE);
		Map<String, String> outParam = new HashMap<String, String>();
		outParam.put("name", "currenttime ");
		outParam.put("type", "output");
		outParam.put("dataType", "String");
		prcDateList.add(outParam);
		String currenttime = (String) (ProcUtil.createProcedure_Object(
				this.getHibernateTemplate(), prcDateList).get(0));
		if (currenttime == null || currenttime.equals("")) {
			logger.error("存储过程获取当前时间异常");
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(currenttime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return date;
	}
	
	/**通过数据库内置时间函数获取系统时间 精确到秒 
	 * 2012-11-27
	 * @return date
	 */
	@SuppressWarnings("unchecked")
	public Date getSysDateTime()
	{
		String sql="select now()";
		List list=this.executeQuery(sql);
		//String str=(String)list.get(0);
		Date date = (Date) list.get(0);
		
		return date;
	}
	
	/**
	 * 获取序列的下一个值
	 * @param Seq_name 序列名
	 * @param length 长度
	 * @return
	 */
	public int getSeq_next(String Seq_name){
		String sql = "SELECT fun_seq_nextval('"+Seq_name+"')";
		List lists = this.executeQuery(sql);
		if(lists!=null && lists.size()>0 && lists.get(0)!=null){
			int val =  Integer.parseInt(lists.get(0).toString());
			return val;
		}else{
			return -1;
		}
	}
	
	/**
	 * 获取当前序列值
	 * @param Seq_name
	 * @param length
	 * @return
	 */
	public int getSeq_curr(String Seq_name){
		String sql = "SELECT fun_seq_currval('"+Seq_name+"')";
		List lists = this.executeQuery(sql);
		if(lists!=null && lists.size()>0 && lists.get(0)!=null){
			int val =  Integer.parseInt(lists.get(0).toString());
			return val;
		}else{
			return -1;
		}
	}
	
	/**
	 * 设置序列初始值 并返回下一个序列值
	 * @param Seq_name
	 * @param length
	 * @return
	 */
	public int setSeq_currval(String Seq_name,int value){
		String sql = "SELECT fun_seq_setval('"+Seq_name+"',"+value+")";
		List lists = this.executeQuery(sql);
		if(lists!=null && lists.size()>0 && lists.get(0)!=null){
			int val =  Integer.parseInt(lists.get(0).toString());
			return val;
		}else{
			return -1;
		}
	}


}
