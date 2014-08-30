package com.management.biz.user.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.icfp.frame.biz.impl.BaseManagerImpl;
import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;
import com.icfp.frame.util.Md5PwdEncoder;
import com.management.biz.user.UserBiz;
import com.management.entity.SA03;
import com.management.entity.SA05;
import com.management.entity.SA06;

/**
 * 类UserBizImpl.java的实现描述： 用户管理实现
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-21
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class UserBizImpl extends BaseManagerImpl implements UserBiz {

	private CoreDao coreDao;
	
	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}

	/**
	 * 显示用户列表模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.user.UserBiz#showListFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		List sa03lists = new ArrayList();
		sa03lists = this.coreDao.executeQueryByClass("SELECT * FROM sa03", SA03.class);
		if(sa03lists!=null && sa03lists.size()>0 && sa03lists.get(0)!=null){
			renp.getBody().addParameter("SA03List", sa03lists);
		}
		renp.setModeFreeMaker("management/user/SA05List.html");
		return renp;
	}
	
	/**
	 * 显示新增用户模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.user.UserBiz#showAddFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showAddFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		List sa03lists = new ArrayList();
		sa03lists = this.coreDao.executeQueryByClass("SELECT * FROM sa03", SA03.class);
		if(sa03lists!=null && sa03lists.size()>0 && sa03lists.get(0)!=null){
			renp.getBody().addParameter("SA03List", sa03lists);
		}
		renp.setModeFreeMaker("management/user/AddSA05.html");
		return renp;
	}
	
	/**
	 * 显示修改用户模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.user.UserBiz#showEdtFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showEdtFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		List sa03lists = new ArrayList();
		sa03lists = this.coreDao.executeQueryByClass("SELECT * FROM sa03", SA03.class);
		if(sa03lists!=null && sa03lists.size()>0 && sa03lists.get(0)!=null){
			renp.getBody().addParameter("SA03List", sa03lists);
		}
		renp.setModeFreeMaker("management/user/EdtSA05.html");
		return renp;
	}
	
	/**
	 * 查询用户列表信息
	 * @param rep
	 * @return
	 * @see com.management.biz.user.UserBiz#querySA05(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA05(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("SA05list");
			StringBuffer sql = new StringBuffer("SELECT a.*,b.SAB002 FROM sa05 a,sa03 b WHERE a.SAA001 = b.SAB001 ");
			String SAC004 = (String) rep.getBody().getParameter("SAC004");
			if(SAC004!=null && !"".equals(SAC004)){
				sql.append(" and a.SAC004 like '%" + SAC004 + "%' ");
			}
			String SAA001 = (String) rep.getBody().getParameter("SAA001");
			if(SAA001!=null && !"".equals(SAA001)){
				sql.append(" and a.SAA001 = '" + SAA001 + "' ");
			}
			ds.setSQL(sql.toString(),true);
			renp.getBody().addParameter("SA05list", this.coreDao.query(rep,ds));
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
	 * 查询用户ByID
	 * @param rep
	 * @return
	 * @see com.management.biz.user.UserBiz#querySA05ByID(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA05ByID(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			String SAC001 = (String)rep.getBody().getParameter("SAC001");
			if(SAC001!=null && !"".equals(SAC001)){
				DataStore ds = new DataStore();
				ds.setDataType("com.management.entity.SA05");
				ds.setSQL("select * from sa05 where SAC001 = '"+SAC001+"'");
				renp.getBody().addParameter("ds", this.coreDao.query(rep, ds));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，查询用户错误!");
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("");
		}
		return renp;
	}
	
	/**
	 * 新增用户
	 * @param rep
	 * @return
	 * @see com.management.biz.user.UserBiz#executeAddSA05(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeAddSA05(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			ds = this.getSA05DataStore(rep, ds, "insert");
			ds.setAutoPK(false);
			if(this.checkUserLoginName(rep, ds, "insert")){
				this.coreDao.insert(rep,ds);
				this.coreDao.insert(rep, this.getSA06DataStore(rep, ds, "insert"));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
				renp.getHeader().setDetailMsg("成功添加用户!");
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，该用户登录名已经存在！");
				return renp;
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，添加用户失败!");
		}
		return renp;
	}
	
	/**
	 * 修改用户
	 * @param rep
	 * @return
	 * @see com.management.biz.user.UserBiz#executeEdtSA05(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeEdtSA05(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			ds = this.getSA05DataStore(rep, ds, "update");
			if(this.checkUserLoginName(rep, ds,"update")){
				this.coreDao.update(ds);
				this.coreDao.insert(rep, this.getSA06DataStore(rep, ds, "update"));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
				renp.getHeader().setDetailMsg("成功修改用户!");
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，该用户登录名已经存在！");
				return renp;
			}
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，修改用户失败!");
		}
		return renp;
	}
	
	/**
	 * 删除用户
	 * @param rep
	 * @return
	 * @see com.management.biz.user.UserBiz#executeDelSA05(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeDelSA05(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("ds");
			this.coreDao.realDel(ds);
			this.coreDao.insert(rep, this.getSA06DataStore(rep, ds, "delete"));
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("成功删除用户!");
		}catch(Exception e){
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，删除用户失败!");
		}
		return renp;
	}
	
	private DataStore getSA05DataStore(RequestEnvelope rep,DataStore datastore,String type){
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
		Md5PwdEncoder m = new Md5PwdEncoder();
		if(lists!=null && lists.size()>0 && lists.get(0)!=null){
			int size = lists.size();
			for(int i=0;i<size;i++){
				SA05 sa05 = (SA05)lists.get(i);
				if("insert".equals(type)){
					sa05.setSAC001(super.getSeq_next("SEQ_SA05_SAC001",10));
					sa05.setZZD002(new BigDecimal("0"));
					sa05.setZZD003("0");
					sa05.setSAC003(m.encodePassword("123456"));
				}
				sa05.setZZC001("000000");
				sa05.setZZB001(super.getLoginUserName(rep));
				sa05.setZZB002(super.getSysDateTime());
				relists.add(sa05);
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
	 * 校验用户名
	 * @param rep
	 * @param datastore
	 * @param type
	 * @return
	 */
	private boolean checkUserLoginName(RequestEnvelope rep,DataStore datastore,String type){
		boolean result = true;
		List lists = new ArrayList();
		if("insert".equals(type)){
			lists = datastore.getInsertRowSet();
		}
		if("update".equals(type)){
			lists = datastore.getUpdateRowSet();
		}
		if(lists != null && lists.size() != 0 && !lists.isEmpty() && lists.get(0)!= null){
			SA05 sa05 = (SA05)lists.get(0);
			DataStore ds = new DataStore();
			ds.setDataType("com.management.entity.SA05");
			ds.setHQL(" from SA05 a where a.SAC002 = '"+ sa05.getSAC002() +"'");
			this.coreDao.query(rep, ds);
			List<Object> relists = ds.getQueryRowSet();
			if(relists != null && relists.size() != 0 && !relists.isEmpty() && relists.get(0)!= null){
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * 用户增删改变更辅助
	 * @param rep
	 * @param datastore
	 * @param type
	 * @return
	 */
	private DataStore getSA06DataStore(RequestEnvelope rep,DataStore datastore,String type){
		DataStore ds = new DataStore();
		String tag = "";
		ds.setDataType("com.management.entity.SA06");
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
				SA05 sa05 = (SA05)lists.get(i);
				SA06 sa06 = new SA06();
				sa06.setZZF001(super.getSeq_next("SEQ_biangeng",10));
				sa06.setSAC001(sa05.getSAC001());
				sa06.setZZF002(super.getSysDateTime());
				sa06.setZZF003(super.getLoginUserName(rep));
				sa06.setZZF005(tag);
				sa06.setZZC001(sa05.getZZC001());
				sa06.setZZB001(super.getLoginUserName(rep));
				sa06.setZZB002(super.getSysDateTime());
				relists.add(sa06);
			}
		}
		ds.setInsertRowSet(relists);
		return ds;
	}

	
}
