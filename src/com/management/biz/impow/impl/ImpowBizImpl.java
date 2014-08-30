package com.management.biz.impow.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.icfp.frame.biz.impl.BaseManagerImpl;
import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;
import com.management.biz.impow.ImpowBiz;
import com.management.entity.SA03;
import com.management.entity.SA09;
import com.management.entity.SA10;
import com.management.entity.SA10ID;

/**
 * 类ImpowBizImpl.java的实现描述： 角色授权实现
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-21
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class ImpowBizImpl extends BaseManagerImpl implements ImpowBiz{
	
	private CoreDao coreDao;
	
	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}
	
	/**
	 * 显示授权模板页
	 * @param rep
	 * @return
	 * @see com.management.biz.impow.ImpowBiz#showListFtl(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		renp.setModeFreeMaker("management/impow/SA10List.html");
		return renp;
	}
	
	/**
	 * 查询角色信息
	 * @param rep
	 * @return
	 * @see com.management.biz.impow.ImpowBiz#querySA03(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA03(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try{
			DataStore ds = (DataStore)rep.getBody().getParameter("SA03list");
			StringBuffer sql = new StringBuffer("from SA03 a where 1=1 ");
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
	 * 查询菜单信息
	 * @param rep
	 * @return
	 * @see com.management.biz.impow.ImpowBiz#querySA09(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA09(RequestEnvelope rep){
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
	 * 查询授权信息
	 * @param rep
	 * @return
	 * @see com.management.biz.impow.ImpowBiz#querySA10(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope querySA10(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try {
			String SAB001 = (String)rep.getBody().getParameter("SAB001");
			if(SAB001!=null && !"".equals(SAB001)){
				DataStore ds = new DataStore();
				ds.setPageSize(-1);
				ds.setDataType("com.management.entity.SA09");
				ds.setSQL("SELECT a.* FROM sa09 a,sa10 b WHERE a.SAE001 = b.SAE001 AND b.SAF001 = '"+SAB001+"'");
				renp.getBody().addParameter("SA09menu",this.coreDao.query(rep,ds));
				renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
				renp.getHeader().setDetailMsg("成功获取菜单树！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，获取菜单树失败！");
		}
		return renp;
	}
	
	/**
	 * 保存授权
	 * @param rep
	 * @return
	 * @see com.management.biz.impow.ImpowBiz#executeAddSA10(com.icfp.frame.ria.request.RequestEnvelope)
	 */
	public ResponseEnvelope executeAddSA10(RequestEnvelope rep){
		ResponseEnvelope renp = new ResponseEnvelope();
		try {
			String roleid = (String)rep.getBody().getParameter("roleid");
			String menus = (String)rep.getBody().getParameter("menus");
			if(roleid!=null && !"".equals(roleid)){
				if(menus!=null && !"".equals(menus)){
					DataStore ds = this.getSA10DataStore(rep, roleid, menus);
					this.coreDao.executeSql("delete from sa10 where SAF001 = '"+roleid+"'");
					this.coreDao.insert(rep, ds);
					this.CreateJavaScript(rep);
					renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
					renp.getHeader().setDetailMsg("成功保存授权！");
				}else{
					this.coreDao.executeSql("delete from sa10 where SAF001 = '"+roleid+"'");
					renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
					renp.getHeader().setDetailMsg("成功保存授权！");
				}
			}else{
				renp.getHeader().setAppCode(RiaParamList.CODE_SYS_FAIL);
				renp.getHeader().setDetailMsg("对不起，保存授权失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("对不起，保存授权失败！");
		}
		return renp;
	}
	
	/**
	 * 新增授权 辅助方法
	 * @param requestEnvelope
	 * @param roleid
	 * @param menus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataStore getSA10DataStore(RequestEnvelope requestEnvelope,String roleid,String menus){
		DataStore ds = new DataStore();
		ds.setAutoPK(false);
		ds.setDataType("com.management.entity.SA10");
		List lists = new ArrayList();
		String[] menuids = menus.split(",");
		int size = menuids.length;
		for(int i=0;i<size;i++){
			SA10 sa10 = new SA10();
			SA10ID sa10id = new SA10ID();
			sa10id.setSAE001(menuids[i]);
			sa10id.setSAF001(roleid);
			sa10id.setSAF002("1");
			sa10.setId(sa10id);
			sa10.setZZB001(super.getLoginUserName(requestEnvelope));
			sa10.setZZB002(super.getSysDateTime());
			sa10.setZZD002(new BigDecimal("0"));
			lists.add(sa10);
		}
		ds.setInsertRowSet(lists);
		return ds;
	}
	
	
	/**
	 * 生成菜单JS
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private void CreateJavaScript(RequestEnvelope requestEnvelope) throws IOException{
		ServletContext servletcontext = requestEnvelope.getBody().getServletContext();
		String path = servletcontext.getRealPath("/")+"/icfp/ui/js/";
		String hql="from SA03 where ZZD001='0' Order By ZZB002";
		List SA03lists = coreDao.find(hql, null);
		int sa03size = SA03lists.size();
		if(SA03lists!=null && sa03size>0){
			File file = new File(path+"menulist.js");
			OutputStream out = new FileOutputStream(file,false);
			String code = "var __MenuList = [];";
			byte bcode[] = code.getBytes("utf-8");
			out.write(bcode);
			for(int i=0;i<sa03size;i++){
				StringBuffer role = new StringBuffer("");
				SA03 sa03 = (SA03)SA03lists.get(i);
				role.append("__MenuList.push({'roleid':'"+sa03.getSAB001()+"'");
				String sa09hql = "select a from SA09 a,SA10 b where a.SAE001 = b.id.SAE001 and b.id.SAF001='"+ sa03.getSAB001() +"' ORDER BY a.SAE008";
				List<?> SA09lists =  coreDao.find(sa09hql, null);
				int sa09size = SA09lists.size();
				if(SA09lists!=null && sa09size>0){
					role.append(",menus:[");
					for(int j=0;j<sa09size;j++){
						SA09 sa09 = (SA09)SA09lists.get(j);
						role.append("{");
						role.append("'SAE001':'"+sa09.getSAE001()+"',");
						role.append("'SAE004':'"+sa09.getSAE004()+"',");
						role.append("'SAE002':'"+sa09.getSAE002()+"',");
						role.append("'SAE007':'"+sa09.getSAE007()+"',");
						role.append("'SAE008':'"+sa09.getSAE008()+"',");
						role.append("'SAE009':'"+sa09.getSAE009()+"'");
						if(j==(sa09size-1)){
							role.append("}");
						}else{
							role.append("},");
						}
					}
					role.append("]");
				}
				role.append("});");
				if(role!=null && !"".equals(role.toString())){
					byte ecode[] = (role.toString()).getBytes("utf-8");
					out.write(ecode);
				}
			}
			out.close();
		}
	}
	
}
