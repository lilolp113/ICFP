package com.icfp.frame.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
	public void insert(RequestEnvelope requestEnvelope,DataStore dataStore);
	
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

	/**
	 * 通过ID查找对象,不锁定对象
	 * @param id
	 *            记录的ID
	 * @return 实体对象
	 */
	public Object load(Serializable id);

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
	 * 保存或更新对象拷贝
	 * @param entity
	 * @return 已更新的持久化对象
	 */
	public Object merge(Object entity);


	/**
	 * 根据ID删除记录
	 * 
	 * @param id
	 *            记录ID
	 */
	public void deleteById(Serializable id);
	
	/**
	 * 批量删除
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void deleteAll(List list);

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
	 * 执行存储于dataStore中的增删改操作
	 */
	public void executeDataStore(final DataStore dataStore);
	
	/**
	 * 执行sql语句进行查询操作，获取信息列表
	 * @param sql 查询语句
	 * @return List 数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List executeQuery(final String sql);
	
	/**
	 * 执行sql语句进行查询，返回对象实体
	 * @param sql 查询语句
	 * @return Object 对象实体
	 */
	@SuppressWarnings("unchecked")
	public Object executeUniqueByClass(final String sql,final Class obj);
	
	/**
	 * 通过实例对象查找对象列表信息
	 * @param sql查询语句
	 * @param obj 对象实例
	 * @return 对象数据列表
	 */
	@SuppressWarnings("unchecked")
	public List executeQueryByClass(final String sql,final Class obj);
	
	/**
	 * 执行sql语句获取相应列表 返回结果 键值对
	 */
	@SuppressWarnings("unchecked")
	public List executeQueryMap(final String sql);
	
	/**
	 * 通过存储过程得到系统时间
	 * @param hibernateTemplate
	 * @param procList
	 * @return Date
	 */
	public Date getSysDateByProc();
	
	/**
	 * 通过数据库内置时间函数得到系统时间
	 * @param hibernateTemplate
	 * @param procList
	 * @return Date
	 */
	public Date getSysDate();
	
	/**
	 * 生成String类型主键
	 * @param hibernateTemplate
	 * @return stringPK 主键
	 */
	public String getStringPK();
	
	/**
	 * 生成BigDecimal类型主键
	 * @param hibernateTemplate
	 * @return bigDecimalPK 主键
	 */
	public BigDecimal getBigDecimalPK();
	
	/**
	 * 调用存储过程
	 * @param hibernateTemplate
	 * @return List 数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List callProcedure_Object(List procList);
	
	/**
	 * 得到区域编号
	 * @param loginUser
	 * @return 区域编码
	 */
	public String getAreaCoding(RequestEnvelope requestEnvelope);
	
	/**通过存储过程获取系统时间 精确到秒 
	 * 2012-11-27
	 * @return date
	 */
	public Date getSysDateTimeByProc(); 
	
	/**通过数据库内置时间函数获取系统时间 精确到秒 
	 * 2012-11-27
	 * @return date
	 */
	public Date getSysDateTime(); 
	
	/**
	 * 执行sql语句获取相应列表并进行分页， 返回结果 键值对
	 * @param dataStore 查询信息载体
	 * @return List 分页后数据信息列表
	 */
	@SuppressWarnings("unchecked")
	public List executeQueryMapByPage(final DataStore dataStore);
	
	/**
	 * 按HQL查询对象列表.
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */
	@SuppressWarnings("unchecked")
	public List find(final String hql,final Object... values);
	
	
	/**
	 * 获取序列的下一个值
	 * @param Seq_name 序列名
	 * @param length 长度
	 * @return
	 */
	public int getSeq_next(String Seq_name);
	
	/**
	 * 获取当前序列值
	 * @param Seq_name
	 * @param length
	 * @return
	 */
	public int getSeq_curr(String Seq_name);
	
	/**
	 * 设置序列初始值 并返回下一个序列值
	 * @param Seq_name
	 * @param length
	 * @return
	 */
	public int setSeq_currval(String Seq_name,int value);
	
}
