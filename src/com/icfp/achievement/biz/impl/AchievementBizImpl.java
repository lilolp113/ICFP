package com.icfp.achievement.biz.impl;

import java.text.DecimalFormat;
import java.util.List;

import com.icfp.achievement.AchievementParams;
import com.icfp.achievement.biz.AchievementBiz;
import com.icfp.achievement.entity.ZD01;
import com.icfp.achievement.entity.ZD03;
import com.icfp.frame.biz.impl.BaseManagerImpl;
import com.icfp.frame.dao.CoreDao;
import com.icfp.frame.datastore.DataStore;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;

/**
 * 类AchievementBizImpl.java的实现描述： 成就系统实现类
 *
 * @author       王兴 wangx@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-6-20
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class AchievementBizImpl extends BaseManagerImpl implements
		AchievementBiz {
	private CoreDao coreDao;

	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEnvelope showListFtl(RequestEnvelope requestEnvelope) {
		ResponseEnvelope renp = new ResponseEnvelope();
		try {
			String saa001 = super.getLoginUserRoleID(requestEnvelope);//用户角色ID
			String sac001 = super.getLoginUserID(requestEnvelope);//用户ID
			
			//用户积分和等级
			String zd03Sql = "select * from zd03 where SAA001 = '" + saa001 + "' and sac001 = '" + sac001 + "'";
			List<ZD03> zd03s = (List<ZD03>)coreDao.executeQueryByClass(zd03Sql, ZD03.class);
			int score = 0;//积分
			int grade = 0;//等级
			int lower = 0;//下限
			int upper = 0;//上限
			
			if(zd03s!=null && zd03s.size()>0 && zd03s.get(0)!=null) {
				ZD03 zd03 = zd03s.get(0);
				score = zd03.getZDA005();
				grade = zd03.getZDA003();
				
				//等级上限
				String upperSql = "select * from zd01 where SAA001 = '" + saa001 + "' and ZDA003 = " + grade + "";
				List<ZD01> uppers = (List<ZD01>)coreDao.executeQueryByClass(upperSql, ZD01.class);
				if(uppers!=null && uppers.size()>0) {
					ZD01 zd01Upper = uppers.get(0);
					upper = zd01Upper.getZDA005();
				}
				
				//等级下限
				if(grade!=1) {
					int temp = grade - 1;
					String lowerSql = "select * from zd01 where SAA001 = '" + saa001 + "' and ZDA003 = " + temp + "";
					List<ZD01> lowers = (List<ZD01>)coreDao.executeQueryByClass(lowerSql, ZD01.class);
					if(lowers!=null && lowers.size()>0) {
						ZD01 zd01Lower = lowers.get(0);
						lower = zd01Lower.getZDA005();
					}
				}
			}
			
			String username = super.getLoginUserName(requestEnvelope);//用户名
			renp.getBody().addParameter(AchievementParams.USERNAME, username);//用户名
			renp.getBody().addParameter(AchievementParams.SCORE, score);//积分
			
			renp.getBody().addParameter(AchievementParams.GRADE, grade);//等级
			renp.getBody().addParameter(AchievementParams.UPPER, upper);//上限
			renp.getBody().addParameter(AchievementParams.LOWER, lower);//下限
			
			double d = (double)score/upper*310;
			long progress = (long)d;//等级所处进度
			progress = (progress>310) ? 310:progress;
			renp.getBody().addParameter(AchievementParams.PROGRESS, progress);//等级所处进度
			
			int distance = upper - score;
			renp.getBody().addParameter(AchievementParams.DISTANCE, distance);//距离下一个等级还有多少分
			
			//得到该用户角色下总人数和排名
			String orderzd03 = "select * from zd03 where SAA001 = '" + saa001 + "' order by ZDA005 desc";
			List<ZD03> zd03list = (List<ZD03>)coreDao.executeQueryByClass(orderzd03, ZD03.class);
			int countSameRole = 0;//人数
			int rankingSameRole = 0;//排名
 			if(zd03list!=null && zd03list.size()>0) {
 				countSameRole = zd03list.size();
				for(int i=0; i<zd03list.size(); i++) {
					if(sac001.equals(zd03list.get(i).getSAC001())) {
						rankingSameRole = i + 1;
						break;
					}
				}
			}
 			renp.getBody().addParameter(AchievementParams.COUNT_SAME_ROLE, countSameRole);//该角色总认识
 			renp.getBody().addParameter(AchievementParams.RANKING_SAME_ROLE, rankingSameRole);//该角色下排名
			
 			double percent = 0;//名次百分比
 			if(rankingSameRole!=0 && countSameRole!=0) {
 				percent = (double)(countSameRole-rankingSameRole+1)/countSameRole * 100;
 				DecimalFormat df = new DecimalFormat("0.00");
 				percent = Double.parseDouble(df.format(percent));
 			}
 			renp.getBody().addParameter(AchievementParams.PERCENT, percent);//名次百分比
			
		} catch (Exception e) {
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("");
		}
		renp.setModeFreeMaker("achievement/achievementList.html");
		return renp;
	}
	
	public ResponseEnvelope achievementList(RequestEnvelope requestEnvelope) {
		ResponseEnvelope renp = new ResponseEnvelope();
		try {
			String saa001 = super.getLoginUserRoleID(requestEnvelope);//用户角色ID
			String sac001 = super.getLoginUserID(requestEnvelope);//用户ID
			
			//角色积分等级列表
			DataStore zd01ds = new DataStore();
			String zd01Sql = "SELECT a.* FROM zd01 a WHERE a.SAA001 = '" + saa001 + "'";
			zd01ds.setSQL(zd01Sql, true);
			zd01ds.setPageSize(-1);
			renp.getBody().addParameter("zd01ds",
					this.coreDao.query(requestEnvelope, zd01ds));
			
			//最高级别的成就列表
			DataStore zd05ds = new DataStore();
			String zd05Sql = "SELECT a.* FROM " +
					"(SELECT * FROM zd05 where SAA001 = '" + saa001 + "' ORDER BY zde005 DESC) a " +
					"GROUP BY a.ZDE013";
			zd05ds.setSQL(zd05Sql, true);
			zd05ds.setPageSize(-1);
			renp.getBody().addParameter("zd05ds",
					this.coreDao.query(requestEnvelope, zd05ds));
			
			//所有成就列表
			DataStore zd05dsAll = new DataStore();
			String zd05dsAllSql = "SELECT * FROM zd05 where " +
					"SAA001 = '" + saa001 + "' order by zde012, zde005" ;
			zd05dsAll.setSQL(zd05dsAllSql, true);
			zd05dsAll.setPageSize(-1);
			renp.getBody().addParameter("zd05dsAll",
					this.coreDao.query(requestEnvelope, zd05dsAll));
			
			//我的成就列表
			DataStore zd06ds = new DataStore();
			String zd06Sql = "SELECT c.* FROM(" +
					"SELECT a.*,b.ZDE013,b.ZDE005 FROM zd06 a,zd05 b WHERE " +
					"a.ZDE001 = b.zde001 AND " +
					"a.SAC001 = '" + sac001 + "' " + 
					"ORDER BY b.zde005 DESC) c " +
					"GROUP BY c.zde013";
			zd06ds.setSQL(zd06Sql, true);
			zd06ds.setPageSize(-1);
			renp.getBody().addParameter("zd06ds",
					this.coreDao.query(requestEnvelope, zd06ds));
			
			renp.getHeader().setAppCode(RiaParamList.CODE_SUCCESS);
			renp.getHeader().setDetailMsg("");
		} catch (Exception e) {
			e.printStackTrace();
			renp.getHeader().setAppCode(RiaParamList.CODE_FAIL);
			renp.getHeader().setDetailMsg("");
		}
		return renp;
	}

}
