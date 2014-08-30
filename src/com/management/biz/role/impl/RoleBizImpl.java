package com.management.biz.role.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.icfp.frame.biz.impl.BaseManagerImpl;
import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;
import com.management.biz.role.RoleBiz;
import com.management.entity.SA03;
import com.management.entity.SA04;

/**
 * 类RoleBizImpl.java的实现描述： 角色管理实现
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-21
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class RoleBizImpl  extends BaseManagerImpl implements RoleBiz{
	
	private CoreDao coreDao;
	
	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}
	
	/**
	 * 显示角色列表模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.role.RoleBiz#showListFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.setModeFreeMaker("management/role/SA03List.html");
		return renp;
	}
	
	/**
	 * 显示新增角色模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.role.RoleBiz#showAddFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showAddFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.setModeFreeMaker("management/role/AddSA03.html");
		return renp;
	}
	
	/**
	 * 显示修改角色模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.role.RoleBiz#showEdtFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showEdtFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.setModeFreeMaker("management/role/EdtSA03.html");
		return renp;
	}
	
	/**
	 * 查询角色列表信息
	 * @param rep
	 * @return
	 * @see com.management.biz.role.RoleBiz#querySA03(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA03(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("SA03list");
			StringBuffer sql = new StringBuffer("from SA03 a where 1=1 ");
			String SAB002 = (String) rep.getBody().getParameter("SAB002");
			if(SAB002!=null && !"".equals(SAB002)){
				sql.append(" and a.SAB002 like '%" + SAB002 + "%' ");
			}
			String SAB003 = (String) rep.getBody().getParameter("SAB003");
			if(SAB003!=null && !"".equals(SAB003)){
				sql.append(" and a.SAB003 like '%" + SAB003 + "%' ");
			}
			ds.setHQL(sql.toString());
			renp.getBody().addParameter("SA03list", this.coreDao.query(rep,ds));
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("");
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("");
		}
		return renp;
	}
	
	/**
	 * 查询角色ByID
	 * @param rep
	 * @return
	 * @see com.management.biz.role.RoleBiz#querySA03ByID(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA03ByID(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			String SAB001 = (String)rep.getBody().getParameter("SAB001");
			if(SAB001!=null && !"".equals(SAB001)){
				DataStore ds = new DataStore();
				ds.setDataType("com.management.entity.SA03");
				ds.setSQL("select * from sa03 where SAB001 = '"+SAB001+"'");
				renp.getBody().addParameter("ds", this.coreDao.query(rep, ds));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，查询角色错误!");
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("");
		}
		return renp;
	}
	
	/**
	 * 新增角色
	 * @param rep
	 * @return
	 * @see com.management.biz.role.RoleBiz#executeAddSA03(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeAddSA03(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			ds = this.getSA03DataStore(rep, ds, "insert");
			ds.setAutoPK(false);
			if(this.checkRolename(rep, ds,"insert")){
				this.coreDao.insert(rep,ds);
				this.coreDao.insert(rep, this.getSA04DataStore(rep, ds, "insert"));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
				renp.getHeader().setDetailMsg("成功添加角色!");
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，该角色名已经存在！");
				return renp;
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，添加角色失败!");
		}
		return renp;
	}
	
	/**
	 * 修改角色
	 * @param rep
	 * @return
	 * @see com.management.biz.role.RoleBiz#executeEdtSA03(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeEdtSA03(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			ds = this.getSA03DataStore(rep, ds, "update");
			if(this.checkRolename(rep, ds,"update")){
				this.coreDao.update(ds);
				this.coreDao.insert(rep, this.getSA04DataStore(rep, ds, "update"));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
				renp.getHeader().setDetailMsg("成功修改角色!");
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，该角色名已经存在！");
				return renp;
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，修改角色失败!");
		}
		return renp;
	}
	
	/**
	 * 删除角色
	 * @param rep
	 * @return
	 * @see com.management.biz.role.RoleBiz#executeDelSA03(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeDelSA03(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			if(this.checkDelRole(rep, ds)){
				this.coreDao.realDel(ds);
				this.coreDao.insert(rep, this.getSA04DataStore(rep, ds, "delete"));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
				renp.getHeader().setDetailMsg("成功删除角色!");
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，不能删除该角色因为还有用户为该角色！");
				return renp;
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，删除角色失败!");
		}
		return renp;
	}
	
	
	/**
	 * 校验角色名
	 * @param rep
	 * @param datastore
	 * @return
	 */
	private boolean checkRolename(RequestEnvelope rep,DataStore datastore,String type){
		boolean result = true;
		List lists = new ArrayList();
		if("insert".equals(type)){
			lists = datastore.getInsertRowSet();
		}
		if("update".equals(type)){
			lists = datastore.getUpdateRowSet();
		}
		if(lists != null && lists.size() != 0 && !lists.isEmpty() && lists.get(0)!= null){
			SA03 sa03 = (SA03)lists.get(0);
			DataStore ds = new DataStore();
			ds.setDataType("com.management.entity.SA03");
			ds.setHQL(" from SA03 a where a.SAB002 = '"+ sa03.getSAB002() +"'");
			this.coreDao.query(rep,ds);
			List<Object> relists = ds.getQueryRowSet();
			if(relists != null && relists.size() != 0 && !relists.isEmpty() && relists.get(0)!= null){
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * 校验是否可以删除角色
	 * @param rep
	 * @param datastore
	 * @return
	 */
	private boolean checkDelRole(RequestEnvelope rep,DataStore datastore){
		boolean result = true;
		List<Object> lists = datastore.getDeleteRowSet();
		if(lists != null && lists.size() != 0 && !lists.isEmpty() && lists.get(0)!= null){
			SA03 sa03 = (SA03)lists.get(0);
			DataStore ds = new DataStore();
			ds.setDataType("com.management.entity.SA05");
			ds.setHQL(" from SA05 a where a.SAA001 = '"+ sa03.getSAB001() +"'");
			this.coreDao.query(rep,ds);
			List<Object> relists = ds.getQueryRowSet();
			if(relists != null && relists.size() != 0 && !relists.isEmpty() && relists.get(0)!= null){
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * 角色增删改辅助方法
	 * @param requestEnvelope
	 * @param datastore
	 * @param type
	 * @return
	 */
	private DataStore getSA03DataStore(RequestEnvelope requestEnvelope,DataStore datastore,String type){
		DataStore ds = new DataStore();
		ds.setDataType(datastore.getDataType());
		ds.setBizId(datastore.getBizId());
		ds.setAutoPK(false);
		List lists = new ArrayList();
		List relists = new ArrayList();
		if("insert".equals(type)){
			lists = datastore.getInsertRowSet();
		}
		if("update".equals(type)){
			lists = datastore.getUpdateRowSet();
		}
		if("delete".equals(type)){
			lists = datastore.getDeleteRowSet();
		}
		if(lists!=null && lists.size()>0 && lists.get(0)!=null){
			int size = lists.size();
			for(int i=0;i<size;i++){
				SA03 sa03 = (SA03)lists.get(i);
				if("insert".equals(type)){
					sa03.setSAB001(super.getSeq_next("SEQ_SA03_SAB001",10));
					sa03.setSAB004("0");
					sa03.setZZD001("0");
					sa03.setZZD002(new BigDecimal("0"));
				}
				if("update".equals(type)){
					sa03.setZZB001(super.getLoginUserName(requestEnvelope));
					sa03.setZZB002(super.getSysDateTime());
				}
				if("delete".equals(type)){
					sa03.setZZB001(super.getLoginUserName(requestEnvelope));
					sa03.setZZB002(super.getSysDateTime());
				}
				relists.add(sa03);
			}
			if("insert".equals(type)){
				ds.setInsertRowSet(relists);
			}
			if("update".equals(type)){
				ds.setUpdateRowSet(relists);
			}
			if("delete".equals(type)){
				ds.setDeleteRowSet(relists);
			}
		}
		return ds;
	}
	
	/**
	 * 角色增删改变更
	 * @param requestEnvelope
	 * @param datastore
	 * @param type
	 * @return
	 */
	private DataStore getSA04DataStore(RequestEnvelope requestEnvelope,DataStore datastore,String type){
		DataStore ds = new DataStore();
		String tag = "";
		ds.setDataType("com.management.entity.SA04");
		ds.setAutoPK(false);
		List<Object> lists = new ArrayList<Object>();
		List<Object> relists = new ArrayList<Object>();
		if("insert".equals(type)){
			lists = datastore.getInsertRowSet();
			tag = "1";
		}else if("update".equals(type)){
			lists = datastore.getUpdateRowSet();
			tag = "2";
		}else if("delete".equals(type)){
			lists = datastore.getDeleteRowSet();
			tag = "3";
		}
		if(lists != null && lists.size() != 0 && !lists.isEmpty() && lists.get(0)!= null){
			for(int i=0;i<lists.size();i++){
				SA03 sa03 = (SA03)lists.get(i);
				SA04 sa04 = new SA04();
				sa04.setZZF001(super.getSeq_next("SEQ_biangeng",10));
				sa04.setSAB001(sa03.getSAB001());
				sa04.setZZF002(super.getSysDateTime());
				sa04.setZZF003(super.getLoginUserName(requestEnvelope));
				sa04.setZZF004(tag);
				sa04.setZZB001(super.getLoginUserName(requestEnvelope));
				sa04.setZZB002(super.getSysDateTime());
				relists.add(sa04);
			}
		}
		ds.setInsertRowSet(relists);
		return ds;
	}
	
}
