package com.icfp.frame.dao.impl;

import static org.hibernate.EntityMode.POJO;

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
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.icfp.frame.dao.BaseDao;
import com.icfp.frame.dao.base.Condition;
import com.icfp.frame.dao.base.Finder;
import com.icfp.frame.dao.base.Nullable;
import com.icfp.frame.dao.base.OrderBy;
import com.icfp.frame.dao.base.Updater;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.datastore.util.DataStoreParamList;
import com.icfp.frame.params.DaoParamList;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.params.SysParamsList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.util.MyBeanUtils;
import com.icfp.frame.util.ProcUtil;
import com.icfp.core.entity.SA05;
import com.icfp.core.entity.SA07;

import org.hibernate.criterion.Order;

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
	 * @param autoPK
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void insert(RequestEnvelope requestEnvelope,DataStore dataStore,boolean autoPK)
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
		
		Date date=this.getSysDate();   //获取当前系统时间
		Class[] parameterTypes2 = { Date.class };
		Object[] arglist2 = { date };
		logger.info("经办人[" + operator + "], 经办时间["
				+ new SimpleDateFormat("yyyy-MM-dd").format(date) + "]");
		
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
				m1 = dataClass.getMethod("set" + DaoParamList.OPERATOR,parameterTypes);
			if (Arrays.asList(props).contains(DaoParamList.OPERATE_DATE))
				m2 = dataClass.getMethod("set" + DaoParamList.OPERATE_DATE,parameterTypes2);
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
			if (autoPK) {// 需要生成主键
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
							+ "]，BYCFP v1.0版本不支持此类主键生成，请自行主键赋值！");
					return;
				}
				logger.info("该添加操作，系统生成主键[" + autoPKString + "]");
				this.getHibernateTemplate().getSessionFactory().getClassMetadata(dataClass).setIdentifier(obj, autoPKString,EntityMode.POJO); // 设置主键
			}
			
			try {
				if (m1 != null)
					m1.invoke(obj, arglist);
				if (m2 != null)
					m2.invoke(obj, arglist2);
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
			
			if (i % DaoParamList.INSERT_SIZE == 0) {
				this.getHibernateTemplate().flush();
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
			this.deleteOrUpdate(getHibernateTemplate(), object,dataStore.getStatus(i),false);
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
			this.deleteOrUpdate(getHibernateTemplate(), object,dataStore.getStatus(i),true);
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
			this.deleteOrUpdate(getHibernateTemplate(), object, dataStore.getStatus(i),true);
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
		String tableName = object.getClass().getSimpleName();     //得到表名
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

		Object[] props = hibernateTemplate.getSessionFactory()
				.getClassMetadata(dataClass).getPropertyNames();

		if (!Arrays.asList(props).contains(DaoParamList.CONCURRENCY)) {
			logger.error("该对象无并发标志字段[" + DaoParamList.CONCURRENCY + "]");
		    return false;
		}

		Method getc = null;
		try {
			getc = dataClass.getMethod("get" + DaoParamList.CONCURRENCY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object a = null;
		try {
			a = getc.invoke(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (a == null) {
			logger.error("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "="
					+ vid + "]并发字段为空");
			return false;
		}
		BigDecimal concurrency = new BigDecimal(a.toString());
		logger.info("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "=" + vid
				+ "]并发字段：" + DaoParamList.CONCURRENCY + " = "
				+ concurrency.toString());
		BigDecimal dbConcurrency = null;
		if (Arrays.asList(DaoParamList.PK_COMMONTYPE).contains(idType)) {// 普通主键情况
				if (idType != null && idType.equals("string"))
					vid = "'" + vid + "'";
				String hql = "select t." + DaoParamList.CONCURRENCY + " from "
						+ tableName + " t where t." + idKey + "=" + vid;
				logger.info("查询数据库并发字段hql语句  >>");
				List list =hibernateTemplate.find(hql);
				if (list == null || list.size() == 0) {
					logger.error("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "="
							+ vid + "]数据库中无并发字段，操作失败！");
					return false;
				}
				Object concObj = list.get(0);
				if (concObj == null) {
					logger.error("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "="
							+ vid + "]数据库中并发字段为空，操作失败！");
					return false;
				}
				dbConcurrency = new BigDecimal(concObj.toString());
				logger.info("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "=" + vid
						+ "]数据库并发字段：" + DaoParamList.CONCURRENCY + " = "
						+ dbConcurrency.toString());
		}else {// 联合主键情况
			Method getConcurrency = null;
			try {
				getConcurrency = dataClass.getMethod("get"
						+ DaoParamList.CONCURRENCY);// 方法名为getZZD002()
			} catch (Exception e) {
				e.printStackTrace();
			}
			Object dbComCon = null;
			try {
				dbComCon = getConcurrency.invoke(po);// 得到对象的并发字段
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (dbComCon == null) {
				logger.error("待修改对象: 类型[" + dataType + "], 主键[" + idKey + "="
						+ vid + "]修改操作中数据状态标志[" + status + "]无法识别");
				return false;
			}
			dbConcurrency = new BigDecimal(dbComCon.toString());//
		}
		if (dbConcurrency.equals(concurrency)) {
			logger.info("待删/改对象: 类型[" + dataType + "], 主键[" + idKey + "=" + vid
					+ "]并发正常，修改前未被修改");
			
			if (status.equals(DataStoreParamList.STATUS_DELETE)&& sign) {
				
				hibernateTemplate.delete(object);
				hibernateTemplate.getSessionFactory().getCurrentSession().flush();
				hibernateTemplate.flush();
				return true;
			}else if(!sign){
				if (!Arrays.asList(props)
						.contains(DaoParamList.DELETE_FLAG)) {
					logger.error("该对象无删除标志字段[" + DaoParamList.DELETE_FLAG
							+ "]");
					return false;
				}
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
				Class[] parameterTypes = { BigDecimal.class }; // setZZD002(BigDecimal// l)
				BigDecimal plus = concurrency.add(new BigDecimal(1));// 并发字段加1

				Object arglist[] = { plus };
				Method m = null;
				try {
					m = dataClass.getMethod("set" + DaoParamList.CONCURRENCY,parameterTypes);// 设置并发字段setZZD002(BigDecimal l)
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				}
				try {
					m.invoke(object, arglist);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			hibernateTemplate.merge(object);
			//hibernateTemplate.getSessionFactory().getCurrentSession().flush();
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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List findAll(final DataStore dataStore,final RequestEnvelope requestEnvelope,final HibernateTemplate hibernateTemplate)
	{
		String areaCoding = getAreaCoding(requestEnvelope); // 区域编号
		String bizId="";     // 交互编号
		bizId = (String) requestEnvelope.getBody().getParameter(RiaParamList.REQUESTID);
		final Class<?> dataClass = this.getClass(dataStore.getDataType());   //实例化对象
		String tablename = dataClass.getSimpleName();
		// 是否启用VPD
		if(dataStore.isVpd())
		{
			logger.info("此次查询操作<启用VPD机制>");
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
		          int rowCount = BaseDaoImpl.this.countQueryResult(finder,dataStore);
		          dataStore.setRowCount(rowCount);
		          query = session.createSQLQuery(finder.getOrigHql());
		          query = finder.matchParams(query,dataStore);
			        if (dataStore.getPageSize() != -1) {
			          query.setFirstResult(dataStore.getFirstResult());
			          query.setMaxResults(dataStore.getPageSize());
			        }
			        dataList=query.setResultTransformer(Transformers.aliasToBean(dataClass)).list();
		        }
		        if ((dataStore.getHQL() != null) && (!"".equals(dataStore.getHQL())))
		        {
		          finder = Finder.create(dataStore);
		          int rowCount = BaseDaoImpl.this.countQueryResult(finder,dataStore);
		          dataStore.setRowCount(rowCount);
		          query = session.createQuery(finder.getOrigHql());
		          query = finder.matchParams(query,dataStore);
			        if (dataStore.getPageSize() != -1) {
			          query.setFirstResult(dataStore.getFirstResult());
			          query.setMaxResults(dataStore.getPageSize());
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
		        query = finder.matchParams(query,dataStore);
		        object=query.uniqueResult();
	    	}
	    	if(dataStore.getSQL()!=null && !"".equals(dataStore.getSQL()))
	    	{
	    		finder = Finder.create(dataStore);
	    		query = session.createSQLQuery(finder.getOrigHql());
		        query = finder.matchParams(query,dataStore);
		        object=query.setResultTransformer(Transformers.aliasToBean(dataClass)).uniqueResult();
	    	}
	    	session.flush();
	        return object;
	      }
	    });
	  }
	
	public Object save(Object entity) {
		Assert.notNull(entity);
		this.getHibernateTemplate().save(entity);
		this.getHibernateTemplate().flush();
		return entity;
	}

	public Object update(Object entity) {
		getHibernateTemplate().update(entity);
		getHibernateTemplate().flush();
		return entity;
	}

	public Object saveOrUpdate(Object entity) {
		Assert.notNull(entity);
		getHibernateTemplate().saveOrUpdate(entity);
		getHibernateTemplate().flush();
		return entity;
	}

	public Object merge(Object entity) {
		Object object=getHibernateTemplate().merge(entity);
		this.refresh(object);
		getHibernateTemplate().flush();
		return object;
	}

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

	public Object deleteById(Serializable id) {
		Assert.notNull(id);
		Object entity = load(id);
		getHibernateTemplate().delete(entity);
		getHibernateTemplate().flush();
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public void deleteAll(List list)
	{
		if(list!=null && list.size()>0)
		{
			getHibernateTemplate().deleteAll(list);
			getHibernateTemplate().flush();
		}
	}

	public Object load(Serializable id) {
		Assert.notNull(id);
		return (Object)getHibernateTemplate().load(getPersistentClass(),id);
	}

	public Object get(Serializable id) {
		Assert.notNull(id);
		return (Object)getHibernateTemplate().get(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	public List findAll() {
		return findByCriteria();
	}
	

	/**
	 * 按HQL查询对象列表.
	 * 
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
	 * 按属性查找对象列表.
	 * param property 属性名称
	 * param value 属性值
	 */
	@SuppressWarnings("unchecked")
	public List findByProperty(String property, Object value) {
		Assert.hasText(property);
		return createCriteria(Restrictions.eq(property, value)).list();
	}

	/**
	 * 按属性查找唯一对象.
	 * param proerty 属性名称
	 * param value 属性值
	 */
	public Object findUniqueByProperty(String property, Object value) {
		Assert.hasText(property);
		Assert.notNull(value);
		return (Object) createCriteria(Restrictions.eq(property, value))
				.uniqueResult();
	}
	
	/**
	 * 按属性查找对象数量.
	 * param proerty 属性名称
	 * param value 属性值
	 */
	public int countByProperty(String property, Object value) {
		Assert.hasText(property);
		Assert.notNull(value);
		return ((Number) (createCriteria(Restrictions.eq(property, value))
				.setProjection(Projections.rowCount()).uniqueResult()))
				.intValue();
	}

	

	@SuppressWarnings("unchecked")
	public List findByEgList(Object eg, boolean anyWhere, Condition[] conds,
			String... exclude) {
		Criteria crit = getCritByEg(eg, anyWhere, conds, exclude);
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List findByEgList(Object eg, boolean anyWhere, Condition[] conds,
			int firstResult, int maxResult, String... exclude) {
		Criteria crit = getCritByEg(eg, anyWhere, conds, exclude);
		crit.setFirstResult(firstResult);
		crit.setMaxResults(maxResult);
		return crit.list();
	}


	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	protected Query createQuery(String queryString, Object... values) {
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
	 * 按Criterion查询对象列表.
	 * 
	 * @param criterion
	 *            数量可变的Criterion.
	 */
	@SuppressWarnings("unchecked")
	protected List findByCriteria(Criterion... criterion) {
		return createCriteria(criterion).list();
	}

	@SuppressWarnings("unchecked")
	protected DataStore findByCriteria(Criteria crit,DataStore dataStore, Projection projection, Order... orders) {
		int totalCount = ((Number) crit.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
		DataStore store=dataStore;
		if (totalCount < 1) {
			return store;
		}
		crit.setProjection(projection);
		if (projection == null) {
			crit.setResultTransformer(Criteria.ROOT_ENTITY);
		}
		if (orders != null) {
			for (Order order : orders) {
				crit.addOrder(order);
			}
		}
		crit.setFirstResult(store.getFirstResult());
		crit.setMaxResults(store.getPageSize());
		store.setQueryRowSet(crit.list());
		return store;
	}

	/**
	 * 通过count查询获得本次查询所能获得的对象总数.
	 * 
	 * @param finder
	 * @return
	 */
	protected int countQueryResult(final Finder finder,final DataStore dataStore) {
		return (Integer)getHibernateTemplate().execute(new HibernateCallback()
	    {
	      public Object doInHibernate(Session session) throws HibernateException, SQLException {
	        Query query = null;
	        session.clear();
	        if ((dataStore.getSQL() != null) && (!"".equals(dataStore.getSQL())))
	        {
	          query = session.createSQLQuery(finder.getRowCountHql());
	        } else if ((dataStore.getHQL() != null) && (!"".equals(dataStore.getHQL())))
	        {
	          query = session.createQuery(finder.getRowCountHql());
	        }
	        finder.matchParams(query, dataStore);

	        return Integer.valueOf(Integer.parseInt(query.uniqueResult().toString()));
	      }
	    });
	}

	/**
	 * 通过count查询获得本次查询所能获得的对象总数.
	 * 
	 * @return page对象中的totalCount属性将赋值.
	 */
	@SuppressWarnings("unchecked")
	protected int countQueryResult(Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;
		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();
		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) MyBeanUtils.getFieldValue(impl,
					"orderEntries");
			MyBeanUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			log.error("不可能抛出的异常:{}", e.getMessage());
		}
		// 执行Count查询
		int totalCount = (Integer) c.setProjection(Projections.rowCount())
				.uniqueResult();
		if (totalCount < 1) {
			return 0;
		}
		// 将之前的Projection和OrderBy条件重新设回去
		c.setProjection(projection);
		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			MyBeanUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			log.error("不可能抛出的异常:{}", e.getMessage());
		}
		return totalCount;
	}

	@SuppressWarnings("deprecation")
	protected Criteria getCritByEg(Object bean, boolean anyWhere, Condition[] conds,
			String... exclude) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(bean);
		example.setPropertySelector(NOT_BLANK);
		if (anyWhere) {
			example.enableLike(MatchMode.ANYWHERE);
			example.ignoreCase();
		}
		for (String p : exclude) {
			example.excludeProperty(p);
		}
		crit.add(example);
		// 处理排序和is null字段
		if (conds != null) {
			for (Condition o : conds) {
				if (o instanceof OrderBy) {
					OrderBy order = (OrderBy) o;
					crit.addOrder(order.getOrder());
				} else if (o instanceof Nullable) {
					Nullable isNull = (Nullable) o;
					if (isNull.isNull()) {
						crit.add(Restrictions.isNull(isNull.getField()));
					} else {
						crit.add(Restrictions.isNotNull(isNull.getField()));
					}
				} else {
					// never
				}
			}
		}
		// 处理many to one查询
		ClassMetadata cm = getCmd(bean.getClass());
		String[] fieldNames = cm.getPropertyNames();
		for (String field : fieldNames) {
			Object o = cm.getPropertyValue(bean, field, POJO);
			if (o == null) {
				continue;
			}
			ClassMetadata subCm = getCmd(o.getClass());
			if (subCm == null) {
				continue;
			}
			Serializable id = subCm.getIdentifier(o, POJO);
			if (id != null) {
				Serializable idName = subCm.getIdentifierPropertyName();
				crit.add(Restrictions.eq(field + "." + idName, id));
			} else {
				crit.createCriteria(field).add(Example.create(o));
			}
		}
		return crit;
	}

	public void refresh(Object entity) {
		getSession().refresh(entity);
	}

	public Object updateDefault(Object entity) {
		return updateByUpdater(Updater.create(entity));
	}

	@SuppressWarnings("deprecation")
	public Object updateByUpdater(Updater updater) {
		ClassMetadata cm = getCmd(updater.getBean().getClass());
		if (cm == null) {
			throw new RuntimeException("所更新的对象没有映射或不是实体对象");
		}
		Object bean = updater.getBean();
		Object po = getHibernateTemplate().load(bean.getClass(),
				cm.getIdentifier(bean, POJO));
		updaterCopyToPersistentObject(updater, po);
		return po;
	}

	/**
	 * 将更新对象拷贝至实体对象，并处理many-to-one的更新。
	 * 
	 * @param updater
	 * @param po
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void updaterCopyToPersistentObject(Updater updater, Object po) {
		Map map = MyBeanUtils.describe(updater.getBean());
		Set<Map.Entry<String, Object>> set = map.entrySet();
		for (Map.Entry<String, Object> entry : set) {
			String name = entry.getKey();
			Object value = entry.getValue();
			if (!updater.isUpdate(name, value)) {
				continue;
			}
			if (value != null) {
				Class valueClass = value.getClass();
				ClassMetadata cm = getCmd(valueClass);
				if (cm != null) {
					Serializable vid = cm.getIdentifier(value, POJO);
					// 如果更新的many to one的对象的id为空，则将many to one设置为null。
					if (vid != null) {
						value = getHibernateTemplate().load(valueClass, vid);
					} else {
						value = null;
					}
				}
			}
			try {
				PropertyUtils.setProperty(po, name, value);
			} catch (Exception e) {
				// never
				log.warn("更新对象时，拷贝属性异常", e);
			}
		}
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	protected Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	private Class persistentClass;

	@SuppressWarnings("unchecked")
	public Class getPersistentClass() {
		return persistentClass;
	}

	public Object createNewEntiey() {
		try {
			return getPersistentClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("不能创建实体对象："
					+ getPersistentClass().getName());
		}
	}

	@SuppressWarnings("unchecked")
	private ClassMetadata getCmd(Class clazz) {
		return (ClassMetadata)this.getHibernateTemplate().getSessionFactory().getClassMetadata(clazz);
	}

	public static final NotBlankPropertySelector NOT_BLANK = new NotBlankPropertySelector();

	/**
	 * 不为空的EXAMPLE属性选择方式
	 * 
	 * @author liufang
	 * 
	 */
	static final class NotBlankPropertySelector implements PropertySelector {
		private static final long serialVersionUID = 1L;

		public boolean include(Object object, String property, Type type) {
			return object != null
					&& !(object instanceof String && StringUtils
							.isBlank((String) object));
		}
	}
	
	/**
	 * 执行sql语句进行相关增删改操作
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
	 * 执行sql语句进行查询，返回对象实体
	 * @param sql
	 * @return
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
	 * @param sql
	 * @param obj
	 * @return
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
	 * 得到系统时间
	 * @param hibernateTemplate
	 * @param procList
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public Date getSysDate() {
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
	 * 生成String类型主键
	 * 
	 * @param hibernateTemplate
	 * @return stringPK
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
	 * 
	 * @param hibernateTemplate
	 * @return bigDecimalPK
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
	
	public String getOperator(RequestEnvelope requestEnvelope) {
		Object loginUser = requestEnvelope.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		String operator = "";
		if (loginUser instanceof SA05) {
			operator = ((SA05) loginUser).getSAC004();
		}
		/*if (loginUser instanceof SA07) {
			operator = ((SA07) loginUser).getSAD004();
		}*/
		return operator;
	}
	
	/**
	 * 调用存储过程
	 * 
	 * @param hibernateTemplate
	 * @return date
	 */
	@SuppressWarnings("unchecked")
	public List callProcedure_Object(List procList) {
		return ProcUtil.createProcedure_Object(this.getHibernateTemplate(), procList);
	}
	
	/**
	 * 得到区域编号
	 * 
	 * @param loginUser
	 * @return
	 */
	public String getAreaCoding(RequestEnvelope requestEnvelope) {
		Object loginUser = requestEnvelope.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
		String areaCoding = ""; // 区域编号
		Method m = null;
		try {
			if (loginUser instanceof SA05)
				m = SA05.class.getMethod("get" + DaoParamList.AREACODING);
			else if (loginUser instanceof SA07)
				m = SA07.class.getMethod("get" + DaoParamList.AREACODING);
			areaCoding = (String) m.invoke(loginUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return areaCoding;
	}


	public Date getSysDateTime() {
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
}
