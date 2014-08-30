package com.icfp.frame.util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.icfp.frame.params.SysParamsList;

/**
 * 调用存储过程工具类
 * @author liufei
 *
 */
public class ProcUtil {

	protected static ProcUtil instance;
	
	public ProcUtil(){}
	
	public static ProcUtil getInstance()
	{
		if(instance==null)
		{
			instance=new ProcUtil();
		}
		return instance;
	}
	
	/**
	 * 动态调用存储过程返回个体数据
	 * procList.get(0) //存储过程名称
	 * procList.get(1)//存储过程参数
	 * map.put("name","")//参数名称;
	 * map.put("value","")//参数值
	 * map.put("type""input/output/outtable")//input标示输入参数/output标示输出参数/outtable标示列表输出
	 * map.put("dataType","")//传入穿出的参数类型
	 * @param template
	 * @param procList   参数列表
	 * @return list//返回结果列表，列表中所保存数据为map形式
	 */
	
	@SuppressWarnings("unchecked")
	public static List createProcedure_Object(HibernateTemplate template,final List<?> procList)
	{
		List all=(List)template.execute(new HibernateCallback(){

			@SuppressWarnings("deprecation")
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				int no=0;
				
				//生成存储过程调用字符串
				String callStr = "{call ";
				callStr += procList.get(0).toString() + "(";
				for (int i = 1; i < procList.size(); i++) {
						callStr += "?,";
				}
				if (procList.size() > 1) {
					callStr = callStr.substring(0, callStr.length() - 1);
				}
				callStr += ")}";
				
				CallableStatement cstmt=session.connection().prepareCall(callStr);
				
				//循环参数列表并设置参数
				for(int j=1;j<procList.size();j++)
				{
					 Map<String, String> map = (Map<String, String>)procList.get(j);  
					 //说明此函数是传入函数 
					 if(map.get("type").equals("input")){   
						 if(map.get("dataType").toUpperCase().equals("STRING")){   
							 cstmt.setString(j, map.get("value"));   
						 }else if(map.get("dataType").toUpperCase().equals("INT")){   
							 try {   
								 cstmt.setInt(j, Integer.parseInt(map.get("value").toString()));    
							 } catch (Exception e) {   
								 throw new SQLException(map.get("value") + " INT 数据类型转换错误");   
							 }   
						 }else if(map.get("dataType").toUpperCase().equals("DECIMAL")){   
							 try {   
								 cstmt.setDouble(j, Double.parseDouble(map.get("value").toString()));   
							 } catch (Exception e) {   
								 throw new SQLException(map.get("value") + " DECIMAL 数据类型转换错误");   
							 }   
						 }else if(map.get("dataType").toUpperCase().equals("DATE")){   
							 try{   
								 DateFormat formatter1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
								 cstmt.setDate(j, new java.sql.Date(formatter1.parse(map.get("value").toString()).getTime()));   
							 }catch(Exception e1){   
								 try{   
									 DateFormat formatter2= new SimpleDateFormat("yyyy-MM-dd");   
									 cstmt.setDate(j, new java.sql.Date(formatter2.parse(map.get("value").toString()).getTime()));   
								 }catch(Exception e2){   
									 throw new SQLException(map.get("value") + " DATE 数据类型转换错误");   
								 }   
							 }   
						 }   
					 }else if(map.get("type").equals("output")){  
						 //说明此函数是返回函数
						 if(map.get("dataType").toUpperCase().equals("STRING")){   
							 cstmt.registerOutParameter(j, Types.VARCHAR);   
						 }else if(map.get("dataType").toUpperCase().equals("INT")){   
							 cstmt.registerOutParameter(j, Types.INTEGER);   
						 }else if(map.get("dataType").toUpperCase().equals("DECIMAL")){   
							 cstmt.registerOutParameter(j, Types.DECIMAL);   
						 }else if(map.get("dataType").toUpperCase().equals("DATE")){   
							 cstmt.registerOutParameter(j, Types.DATE);   
						 }   
						 no+=1;
					 }else if(map.get("type").equals("outtable"))
					 {
						 cstmt.registerOutParameter(j,oracle.jdbc.OracleTypes.CURSOR);
					 }
				}
				
				cstmt.execute();
				
				List lists=new ArrayList();
				
				for(int i=1;i<=no;i++)
				{
					Object obj=cstmt.getObject(i);
					lists.add(obj);
				}
				
				ProcUtil.closeAll(session, cstmt, null);
				
				return lists;
			}
			
		});
		
		return all;
	}
	
	
	/**添加数据库环境 
	 * @param template
	 * @param param1 区域编号
	 * @param param2 交互编号
	 * @return 
	 * 
	 */
	public static boolean addContext(HibernateTemplate template,final String AreaCode,final String BizID,final String TableName)
	{
		boolean flag=(Boolean)template.execute(new HibernateCallback(){

			@SuppressWarnings("deprecation")
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// 生成存储过程调用字符串
				String callStr = "{call " + SysParamsList.SYS_DB_USERNAME + ".pkg_vpd.prc_vpdAddContext(?,?,?)}";
				
				CallableStatement cstmt=session.connection().prepareCall(callStr);

				cstmt.setString(1, AreaCode);
				cstmt.setString(2, BizID);
				cstmt.setString(3, TableName);
				int no=cstmt.executeUpdate();      //执行操作
				
				boolean flag=false;
				
				if(no>0)
				{
					flag=true;
				}
				
				ProcUtil.closeAll(session, cstmt,null);    //关闭连接
				
				return flag;
			}
			
		});
		return flag;
		
	}
	
	/**
	 * 关闭所有连接
	 * @param session
	 * @param cstmt
	 * @param rs
	 */
	@SuppressWarnings("deprecation")
	public static void closeAll(Session session,CallableStatement cstmt,ResultSet rs)
	{
		try {
			if(session.connection()!=null || !session.connection().isClosed())
			{
				session.connection().close();
			}
			if(cstmt!=null)
			{
				cstmt.close();
			}
			if(rs!=null)
			{
				rs.close();
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
