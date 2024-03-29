package com.icfp.frame.dao.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.datastore.util.DataStoreParamList;
import com.icfp.frame.params.DaoParamList;

/**
 * HQL语句分页查询
 * 
 * @author liufang
 * 
 */
public class Finder {
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	protected Finder() {
	}

	public Finder(DataStore dataStore) {
		if(dataStore.getSQL()!=null && !"".equals(dataStore.getSQL()))
		{
			hqlBuilder = new StringBuilder(dataStore.getSQL());
		}else if(dataStore.getHQL()!=null && !"".equals(dataStore.getHQL())){
			hqlBuilder = new StringBuilder(dataStore.getHQL());
		}
		if(dataStore.getOrdering()!=null && !"".equals(dataStore.getOrdering()))
		{
			hqlBuilder.append(" order by '").append(dataStore.getOrdering()).append("'");
		}
		
	}

	public static Finder create(DataStore dataStore) {
		Finder finder=new Finder(dataStore);
		return finder;
	}

	public Finder append(String hql) {
		hqlBuilder.append(hql);
		return this;
	}

	/**
	 * 获得原始hql语句
	 * 
	 * @return
	 */
	public String getOrigHql() {
		return hqlBuilder.toString();
	}

	/**
	 * 获得查询数据库记录数的hql语句。
	 * 
	 * @return
	 */
	public String getRowCountHql() {
		String hql = hqlBuilder.toString();

		int fromIndex = hql.toLowerCase().indexOf(FROM);
		String projectionHql = hql.substring(0, fromIndex);

		hql = hql.substring(fromIndex);
		String rowCountHql = hql.replace(HQL_FETCH, "");

		int index = rowCountHql.indexOf(ORDER_BY);
		if (index > 0) {
			rowCountHql = rowCountHql.substring(0, index);
		}
		return wrapProjection(projectionHql) + rowCountHql;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param param
	 * @param value
	 * @return
	 */
	public Finder setParam(String param, Object value) {
		return setParam(param, value, null);
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param param
	 * @param value
	 * @param type
	 * @return
	 */
	public Finder setParam(String param, Object value, Type type) {
		getParams().add(param);
		getValues().add(value);
		getTypes().add(type);
		return this;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param paramMap
	 * @return
	 */
	public Finder setParams(Map<String, Object> paramMap) {
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			setParam(entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param name
	 * @param vals
	 * @param type
	 * @return
	 */
	public Finder setParamList(String name, Collection<Object> vals, Type type) {
		getParamsList().add(name);
		getValuesList().add(vals);
		getTypesList().add(type);
		return this;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param name
	 * @param vals
	 * @return
	 */
	public Finder setParamList(String name, Collection<Object> vals) {
		return setParamList(name, vals, null);
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param name
	 * @param vals
	 * @param type
	 * @return
	 */
	public Finder setParamList(String name, Object[] vals, Type type) {
		getParamsArray().add(name);
		getValuesArray().add(vals);
		getTypesArray().add(type);
		return this;
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param name
	 * @param vals
	 * @return
	 */
	public Finder setParamList(String name, Object[] vals) {
		return setParamList(name, vals, null);
	}

	/**
	 * 参数匹配
	 * @param query
	 * @param dataStore
	 * @return
	 */
	public Query matchParams(Query query, DataStore dataStore) {
		Object[] params=dataStore.getParams();
		if(params==null)
		{
			return query;
		}
		for (int i = 0; i < params.length; i++) {
			String[] param = (String[]) params[i];
			String paramType = param[0].toLowerCase();
			String paramValue = param[1].toLowerCase();
			if (paramType.equals(DataStoreParamList.PARAMTYPE_INTEGER)) {
				query.setInteger(i, Integer.parseInt(paramValue));
			} else if (paramType.equals(DataStoreParamList.PARAMTYPE_DATE)) {
				if (paramValue.startsWith(DaoParamList.BEGIN_DATE_FLAG)
						|| paramValue.startsWith(DaoParamList.END_DATE_FLAG)) {
					if (paramValue.equals(DaoParamList.BEGIN_DATE_FLAG)) {
						paramValue = DaoParamList.QUERY_BEGIN_DATE;
					} else if (paramValue.equals(DaoParamList.END_DATE_FLAG)) {
						paramValue = DaoParamList.QUERY_END_DATE;
					} else {
						paramValue = paramValue.substring(1);
					}
				} else {
					log.error("查询参数中，开始日期必须以["
							+ DaoParamList.QUERY_BEGIN_DATE + "]为前缀，结束日期必须以["
							+ DaoParamList.QUERY_END_DATE + "]为前缀");
					return null;
				}

				Date date = null;
				try {
					if (paramValue.length() == 10) {
						date = new SimpleDateFormat("yyyy-MM-dd")
								.parse(paramValue);
					} else if (paramValue.length() == 19) {
						date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(paramValue);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				query.setDate(i, date);
			} else if (paramType.equals(DataStoreParamList.PARAMTYPE_STRING)) {
				query.setString(i, paramValue);
			}
		}
		return query;
	}
	
	/**
	 * 将finder中的参数设置到query中。
	 * 
	 * @param query
	 */
	public Query setParamsToQuery(Query query) {
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				if (types.get(i) == null) {
					query.setParameter(params.get(i), values.get(i));
				} else {
					query.setParameter(params.get(i), values.get(i), types
							.get(i));
				}
			}
		}
		if (paramsList != null) {
			for (int i = 0; i < paramsList.size(); i++) {
				if (typesList.get(i) == null) {
					query
							.setParameterList(paramsList.get(i), valuesList
									.get(i));
				} else {
					query.setParameterList(paramsList.get(i),
							valuesList.get(i), typesList.get(i));
				}
			}
		}
		if (paramsArray != null) {
			for (int i = 0; i < paramsArray.size(); i++) {
				if (typesArray.get(i) == null) {
					query.setParameterList(paramsArray.get(i), valuesArray
							.get(i));
				} else {
					query.setParameterList(paramsArray.get(i), valuesArray
							.get(i), typesArray.get(i));
				}
			}
		}
		return query;
	}

	public Query createQuery(Session s) {
		return setParamsToQuery(s.createQuery(getOrigHql()));
	}

	private String wrapProjection(String projection) {
		if (projection.indexOf("select") == -1) {
			return ROW_COUNT;
		} else {
			return projection.replace(projection, "select count(*) ");
		}
	}

	private List<String> getParams() {
		if (params == null) {
			params = new ArrayList<String>();
		}
		return params;
	}

	private List<Object> getValues() {
		if (values == null) {
			values = new ArrayList<Object>();
		}
		return values;
	}

	private List<Type> getTypes() {
		if (types == null) {
			types = new ArrayList<Type>();
		}
		return types;
	}

	private List<String> getParamsList() {
		if (paramsList == null) {
			paramsList = new ArrayList<String>();
		}
		return paramsList;
	}

	private List<Collection<Object>> getValuesList() {
		if (valuesList == null) {
			valuesList = new ArrayList<Collection<Object>>();
		}
		return valuesList;
	}

	private List<Type> getTypesList() {
		if (typesList == null) {
			typesList = new ArrayList<Type>();
		}
		return typesList;
	}

	private List<String> getParamsArray() {
		if (paramsArray == null) {
			paramsArray = new ArrayList<String>();
		}
		return paramsArray;
	}

	public Object[] getParam() {
		return param;
	}

	public void setParam(Object[] param) {
		this.param = param;
	}

	private List<Object[]> getValuesArray() {
		if (valuesArray == null) {
			valuesArray = new ArrayList<Object[]>();
		}
		return valuesArray;
	}

	private List<Type> getTypesArray() {
		if (typesArray == null) {
			typesArray = new ArrayList<Type>();
		}
		return typesArray;
	}

	private StringBuilder hqlBuilder;
	
	private Object[] param;

	private List<String> params;
	private List<Object> values;
	private List<Type> types;

	private List<String> paramsList;
	private List<Collection<Object>> valuesList;
	private List<Type> typesList;

	private List<String> paramsArray;
	private List<Object[]> valuesArray;
	private List<Type> typesArray;

	private int firstResult = 0;

	private int maxResults = 0;

	public static final String ROW_COUNT = "select count(*) ";
	public static final String FROM = "from";
	public static final String DISTINCT = "distinct";
	public static final String HQL_FETCH = "fetch";
	public static final String ORDER_BY = "order";

}