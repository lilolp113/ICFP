package com.icfp.frame.datastore;

import java.util.ArrayList;
import java.util.List;

import com.icfp.frame.datastore.util.DataStoreParamList;
import com.icfp.frame.datastore.util.JsonDataStoreUtil;

import net.sf.json.JSONObject;

/**数据仓库 
 * @author wangxing
 * 2011-08-25
 */
public class DataStore {
	/** 请求编号 */
	private String bizId = "";
	/** 数据类型 */
	private String dataType = "";
	/** 总页数 */
	private int totalPage = 1;
	/** 页面显示数 */
	private int pageSize = 10;
	/** 当前页数 */
	private int pageNo = 1;
	/** 总记录数 */
	private int rowCount = 0;
	/** 排序语句 */
	private String ordering = "";
	/** 分组语句*/
	private String groupBy = "";
	/** sql查询条件 */
	private String sql = "";
	/** hql查询条件 */
	private String hql = "";
	/**vpd开关*/
	private boolean vpd=false;
	/**
	 * 查询参数,目前采用二维数组实现 Object[][] Params = {{{Integer},{2}},{String,
	 * 'week'},{}....};
	 */
	private Object[] params;
	/**
	 * 数据集,目前采用二维数组实现 Object[][] rowSet = {{{status},{user}},{status,
	 * user},{}....};
	 */
	private Object[] rowSet;
	/** 查询结果数据集 */
	private List<Object> queryRowSet = new ArrayList<Object>();
	/** 待添加数据集 */
	private List<Object> insertRowSet = new ArrayList<Object>();
	/** 待删除数据集 */
	private List<Object> deleteRowSet = new ArrayList<Object>();
	/** 待更新数据集 */
	private List<Object> updateRowSet = new ArrayList<Object>();
	
	
	/*2012-12-17李雷*/
	/**多表查询 返回map**/
	private boolean ManyTables = false;
	
	//是否进行参数匹配
	private boolean matchParams =true;
	
	//是否自动生成主键
	private boolean autoPK=true;
	
	/**
	 * 构造方法
	 */
	public DataStore() {
		super();
	}
	
	/**构造校验DataStore
	 * @param dataType 对象类型
	 * @param SQL 校验语句
	 */
	public DataStore(String dataType, String SQL) {
		this.setDataType(dataType);
		this.setPageSize(1);
		this.setSQL(SQL);
	}

	/**
	 * DataStore转换为JSONObject
	 * 
	 * @return JSONObject
	 */
	public JSONObject toJSONObject() {
		return JsonDataStoreUtil.dataStoreToJSONObject(this);
	}

	/**
	 * DataStore转换为JSON字符串
	 * 
	 * @return String
	 */
	public String toJsonStr() {
		return JsonDataStoreUtil.dataStoreToJsonStr(this);
	}
	
	public JSONObject toJsonObj() {
		return JsonDataStoreUtil.dataStoreToJsonObj(this);
	}

	/**
	 * 得到索引记录的状态
	 * 
	 * @param index
	 *            RowSet下标值
	 * @return 索引记录的状态（insert, delete, update）
	 */
	public String getStatus(int index) {
		return (String)((Object[]) rowSet[index])[0];
	}

	/**
	 * 得到索引记录的对象
	 * 
	 * @param index
	 *            RowSet下标值
	 * @return 索引记录的对象
	 */
	public Object getCell(int index) {
		return ((Object[]) rowSet[index])[1];
	}

	/**
	 * 数据归类
	 */
	public void separateRowSet() {
		if (rowSet != null && rowSet.length > 0) {
			for (int i = 0; i < rowSet.length; i++) {
				String status = (String) ((Object[]) rowSet[i])[0];
				Object obj = ((Object[]) rowSet[i])[1];
				if (status != null && !"".equals(status.trim()) && obj != null) {
					if (status.equals(DataStoreParamList.STATUS_INSERT))
						insertRowSet.add(obj);
					else if (status.equals(DataStoreParamList.STATUS_DELETE))
						deleteRowSet.add(obj);
					else if (status.equals(DataStoreParamList.STATUS_UPDATE))
						updateRowSet.add(obj);
					else 
						queryRowSet.add(obj);
				}else {
					queryRowSet.add(obj);
				}
			}
		}
	}

	/**
	 * 得到查询条件和排序条件的sql语句
	 * 
	 * @return whereAndOrdSQL
	 */
	public String getWhereAndOrderSQL() {
		return getSQL() + getOrderingSQL();
	}

	/**
	 * 得到排序条件的sql语句
	 * 
	 * @return " order by id "
	 */
	private String getOrderingSQL() {
		return " order by " + getOrdering();
	}

	/** 请求编号 */
	public String getBizId() {
		return bizId;
	}

	/** 请求编号 */
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	/** 数据类型 */
	public String getDataType() {
		return dataType;
	}

	/** 数据类型 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/** 总页数 */
	public int getTotalPage() {
		if (pageSize <= 0)
			return 1;
		int count = this.getRowCount() / this.pageSize;
		if (count == 0)
			return 1;
		return this.getRowCount() % this.pageSize > 0 ? count + 1 : count;
	}

	/** 总页数 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/** 页面显示数 */
	public int getPageSize() {
		return pageSize;
	}

	/** 页面显示数 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/** 当前页数 */
	public int getPageNo() {
		return pageNo;
	}

	/** 当前页数 */
	public void setPageNo(int pageNo) {
		// int totalPage = this.getTotalPage();
		if (pageNo > totalPage)
			pageNo = totalPage;
		if (pageNo <= 0)
			pageNo = 1;
		this.pageNo = pageNo;
	}

	/** 总记录数 */
	public int getRowCount() {
		return this.rowCount;
	}

	/** 总记录数 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
		int count = this.getRowCount() / this.pageSize;
		if (count <= 0) {
			count = 1;
			this.setPageNo(1);
		}else {
			int totalPage = this.getRowCount() % this.pageSize > 0 ? count + 1 : count;
			if(totalPage<this.getPageNo()){
				this.setPageNo(totalPage);
			}
			this.setTotalPage(totalPage);
		}
	}

	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	
	/** 排序语句 */
	public String getOrdering() {
		return ordering;
	}

	/** 排序语句 */
	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	/** 查询语句 */
	public String getSQL() {
		return sql;
	}

	/** 查询语句 */
	public void setSQL(String SQL) {
		this.sql = SQL;
	}
	
	/** 查询语句  返回map */
	public void setSQL(String SQL,boolean manytable) {
		this.sql = SQL;
		this.ManyTables = manytable;
	}
	
	/** 查询语句 */
	public String getHQL() {
		return hql;
	}

	/** 查询语句 */
	public void setHQL(String HQL) {
		this.hql = HQL;
	}

	/**
	 * 查询参数,目前采用二维数组实现 Object[][] Params = {{{Integer},{2}},{String,
	 * 'week'},{}....};
	 */
	public Object[] getParams() {
		return params;
	}

	/**
	 * 查询参数,目前采用二维数组实现 Object[][] Params = {{{Integer},{2}},{String,
	 * 'week'},{}....};
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}
	
	/**依索引得到参数类型
	 * @param index
	 * @return
	 */
	public Object getParamType(int index) {
		return ((Object[])params[index])[0];
	}
	
	/**依索引得到参数值
	 * @param index
	 * @return
	 */
	public Object getParamValue(int index) {
		return ((Object[])params[index])[1];
	}

	/**
	 * 数据集,包含数据状态和数据实体两部分，目前采用二维数组实现 Object[][] rowSet =
	 * {{{status},{user}},{status, user},{}....};
	 */
	public Object[] getRowSet() {
		return rowSet;
	}

	/**
	 * 数据集,目前采用二维数组实现 Object[][] rowSet = {{{status},{user}},{status,
	 * user},{}....};
	 */
	public void setRowSet(Object[] rowSet) {
		this.rowSet = rowSet;
		if(rowCount == 0) 
			this.setRowCount(rowSet.length);
		
	}

	/** 待添加数据集 */
	public List<Object> getInsertRowSet() {
		return insertRowSet;
	}

	/** 待添加数据集 */
	public void setInsertRowSet(List<Object> insertRowSet) {
		this.insertRowSet = insertRowSet;
	}

	/** 待删除数据集 */
	public List<Object> getDeleteRowSet() {
		return deleteRowSet;
	}

	/** 待删除数据集 */
	public void setDeleteRowSet(List<Object> deleteRowSet) {
		this.deleteRowSet = deleteRowSet;
	}

	/** 待更新数据集 */
	public List<Object> getUpdateRowSet() {
		return updateRowSet;
	}

	/** 待更新数据集 */
	public void setUpdateRowSet(List<Object> updateRowSet) {
		this.updateRowSet = updateRowSet;
	}

	/** 查询结果数据集 */
	public List<Object> getQueryRowSet() {
		return queryRowSet;
	}

	/** 查询结果数据集 */
	public void setQueryRowSet(List<Object> queryRowSet) {
		if(queryRowSet==null)
		{
			queryRowSet=new ArrayList<Object>();
		}
		this.queryRowSet = queryRowSet;
		int size = queryRowSet.size();
		Object[] rows = new Object[size];
		for (int i = 0; i < queryRowSet.size(); i++) {
			Object[] rs = new Object[2];
			rs[0] = " ";
			rs[1] = queryRowSet.get(i);
			rows[i] = rs;
		}
		this.setRowSet(rows);
	}
	
	/**结果集是单个对象
	 * @param object
	 */
	public void setRowSet(Object object) {
		Object[] rows = new Object[1];
		Object[] rs = new Object[2];
		rs[0] = " ";
		rs[1] = object;
		rows[0] = rs;
		this.setRowSet(rows);
	}
	
	/**
	 * 开启vpd
	 */
	public void openVPD()
	{
		this.setVpd(true);
	}
	
	/**
	 * 关闭vpd
	 * @return
	 */
	public void closeVPD()
	{
		this.setVpd(false);
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public boolean isVpd() {
		return vpd;
	}

	public void setVpd(boolean vpd) {
		this.vpd = vpd;
	}

	public boolean isManyTables() {
		return ManyTables;
	}

	public void setManyTables(boolean manyTables) {
		ManyTables = manyTables;
	}
	
	public boolean isMatchParams()
	{
		return matchParams;
	}
	
	public void setMatchParams(boolean matchParam)
	{
		matchParams=matchParam;
	}
	
	public boolean isAutoPK()
	{
		return autoPK;
	}
	
	public void setAutoPK(boolean autoPk)
	{
		autoPK=autoPk;
	}
}
