package com.management.biz.menu.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.icfp.frame.biz.impl.BaseManagerImpl;
import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.params.SysParamsList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;
import com.management.biz.menu.MenuBiz;
import com.management.entity.SA05;
import com.management.entity.SA09;

/**
 * 类MenuBizImpl.java的实现描述：菜单管理实现 
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-21
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class MenuBizImpl extends BaseManagerImpl implements MenuBiz  {
	
	private CoreDao coreDao;

	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}
	
	/**
	 * 显示菜单列表模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#showListFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.setModeFreeMaker("management/menu/SA09List.html");
		return renp;
	}
	
	/**
	 * 显示新增菜单模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#showAddFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showAddFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.setModeFreeMaker("management/menu/AddSA09.html");
		return renp;
	}
	
	/**
	 * 显示修改菜单模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#showEdtFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showEdtFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.setModeFreeMaker("management/menu/EdtSA09.html");
		return renp;
	}
	
	/**
	 * 查询菜单列表信息
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#querySA09(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA09(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try {
			DataStore SA09ds = (DataStore)rep.getBody().getParameter("SA09list");
			String SAE001 = (String)rep.getBody().getParameter("SAE001");
			if(SAE001 == null || SAE001 == ""){
				SA09ds.setHQL(" from SA09 a where a.SAE004='0'");
			}else{
				SA09ds.setHQL(" from SA09 a where a.SAE004 = '"+SAE001+"'");
			}
			renp.getBody().addParameter("SA09list",this.coreDao.query(rep, SA09ds));
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("获取菜单信息列表！");
		} catch (Exception e) {
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，获取菜单信息列表失败！");
		}
		return renp;
	}
	
	/**
	 * 查询菜单ByID
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#querySA09ByID(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA09ByID(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			String SAE001 = (String)rep.getBody().getParameter("SAE001");
			if(SAE001!=null && !"".equals(SAE001)){
				DataStore ds = new DataStore();
				ds.setDataType("com.management.entity.SA09");
				ds.setSQL("SELECT a.*,b.SAE002 AS SAE999 FROM sa09 a LEFT JOIN sa09 b ON a.SAE004 = b.SAE001 WHERE a.SAE001 = '"+SAE001+"'",true);
				renp.getBody().addParameter("ds", this.coreDao.query(rep, ds));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，查询菜单错误!");
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("");
		}
		return renp;
	}
	
	
	/**
	 * 查询菜单树
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#querySA09Tree(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA09Tree(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try {
			DataStore SA09ds = (DataStore)rep.getBody().getParameter("SA09tree");
			SA09ds.setHQL(" from SA09");
			renp.getBody().addParameter("SA09tree",this.coreDao.query(rep,SA09ds));
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("成功获取菜单树！");
		} catch (Exception e) {
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，获取菜单树失败！");
		}
		return renp;
	}
	
	/**
	 * 根据当前用户查询菜单（暂时未使用）
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#querySA09ByUser(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA09ByUser(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try {
			DataStore SA09ds = (DataStore)rep.getBody().getParameter("menulist");
			SA05 sa05 = (SA05)rep.getBody().getHttpSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME);
			SA09ds.setHQL("select a from SA09 a,SA10 b where a.SAE001 = b.id.SAE001 and b.id.SAF001='"+ sa05.getSAA001() +"'");
			renp.getBody().addParameter("menulist",this.coreDao.query(rep, SA09ds));
			renp.getBody().addParameter("cstaff",super.getLoginUserName(rep));
			renp.getBody().addParameter("cstaffid",super.getLoginUserID(rep));
			renp.getBody().addParameter("cstaffroleid",super.getLoginUserRoleID(rep));
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("成功获取菜单菜单列表！");
		} catch (Exception e) {
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，获取菜单菜单列表失败！");
		}
		return renp;
	}
	
	/**
	 * 新增菜单
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#executeAddSA09(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeAddSA09(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			ds = this.getSA09DataStore(rep, ds, "insert");
			ds.setAutoPK(false);
			this.coreDao.insert(rep,ds);
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("成功添加菜单!");
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，添加菜单失败!");
		}
		return renp;
	}
	
	/**
	 * 修改菜单
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#executeEdtSA09(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeEdtSA09(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			ds = this.getSA09DataStore(rep, ds, "update");
			this.coreDao.update(ds);
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("成功修改菜单!");
			
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，修改菜单失败!");
		}
		return renp;
	}
	
	/**
	 * 删除菜单
	 * @param rep
	 * @return
	 * @see com.management.biz.menu.MenuBiz#executeDelSA09(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeDelSA09(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			this.coreDao.realDel(ds);
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("成功删除菜单!");
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，删除菜单失败!");
		}
		return renp;
	}
	
	private DataStore getSA09DataStore(RequestEnvelope requestEnvelope,DataStore datastore,String type){
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
				SA09 sa09 = (SA09)lists.get(i);
				if("insert".equals(type)){
					sa09.setSAE001(super.getSeq_next("SEQ_SA09_SAE001",10));
					sa09.setZZD001("0");
					sa09.setZZD002(new BigDecimal("0"));
					sa09.setZZB001(super.getLoginUserName(requestEnvelope));
					sa09.setZZB002(super.getSysDateTime());
				}
				if("update".equals(type)){
					sa09.setZZB001(super.getLoginUserName(requestEnvelope));
					sa09.setZZB002(super.getSysDateTime());
				}
				if("delete".equals(type)){
					sa09.setZZB001(super.getLoginUserName(requestEnvelope));
					sa09.setZZB002(super.getSysDateTime());
				}
				relists.add(sa09);
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
	
}
