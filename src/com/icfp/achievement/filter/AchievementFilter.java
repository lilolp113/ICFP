package com.icfp.achievement.filter;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.icfp.achievement.AchievementParams;
import com.icfp.achievement.entity.ZD01;
import com.icfp.achievement.entity.ZD02;
import com.icfp.achievement.entity.ZD03;
import com.icfp.achievement.entity.ZD04;
import com.icfp.achievement.entity.ZD05;
import com.icfp.achievement.entity.ZD06;
import com.icfp.frame.biz.impl.BaseManagerImpl;
import com.icfp.frame.params.RiaParamList;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;
import com.management.entity.SA05;

/**
 * 类AchievementAction.java的实现描述： 成就系统处理
 * 
 * @author 王兴 wangx@cs-cs.com.cn
 * @version 1.0 Date 2013-5-23
 * @see History： <author> <time> <version> <desc>
 * 
 */
public class AchievementFilter extends BaseManagerImpl implements MethodInterceptor {

	private static Logger logger = Logger.getLogger(AchievementFilter.class);

	private HibernateTemplate hibernateTemplate;

	/**
	 * @return the hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * @param hibernateTemplate
	 *            the hibernateTemplate to set
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object rval = invocation.proceed();
		ResponseEnvelope response = (ResponseEnvelope)rval;
		
		Method method = invocation.getMethod();
		Object[] args = invocation.getArguments();
		
		if (args == null || args.length != 1
				|| !(args[0] instanceof RequestEnvelope)) {
			logger.warn("成就系统记录方法参数有且只有一个，其类型为RequestEnvelope的方法，当前方法操作["
							+ method.getName() + "]未作成就处理！");
		}else if (method.getName().startsWith("execute")) {
			
			final RequestEnvelope reqs = (RequestEnvelope) args[0];
			final String requestId = (String) reqs.getBody().getParameter(
					RiaParamList.REQUESTID); // 交互编号
			
			ZD02 zd02 = (ZD02) hibernateTemplate.get(ZD02.class, requestId);
			
			//有配置成就
			if(zd02!=null) {
				response = processScore(response, reqs, zd02);
				response = processMedal(response, reqs, zd02);
			}
		}

		//如果没有成就参数则附上初值
		if(!response.getBody().getAllParameters().containsKey(AchievementParams.SCORE)) {
			response.getBody().addParameter(AchievementParams.SCORE, 0);
		}
		if(!response.getBody().getAllParameters().containsKey(AchievementParams.UPGRADE)) {
			response.getBody().addParameter(AchievementParams.UPGRADE, 0);
		}
		if(!response.getBody().getAllParameters().containsKey(AchievementParams.MEDAL)) {
			response.getBody().addParameter(AchievementParams.MEDAL, 0);
		}

		return response;
	}
	
	/**
	 * 处理成就积分
	 * @param response
	 * @param reqs
	 * @param zd02
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ResponseEnvelope processScore(ResponseEnvelope response, RequestEnvelope reqs, ZD02 zd02) {
		int score = 0;
		int upgrade = 0;
		
		final int zdb002 = zd02.getZDB002();//奖励积分
		final int zdb003 = zd02.getZDB003();//每日上线
		if(needScore(reqs, zdb002, zdb003)) {//未到上限，可以加分
			SA05 sa05 = (SA05) super.getLoginUser(reqs);
			
			ZD03 zd03 = (ZD03) hibernateTemplate.get(ZD03.class, sa05.getSAC001());
			if (zd03 == null) {
				zd03 = new ZD03();
				zd03.setSAC001(sa05.getSAC001());
				zd03.setSAA001(sa05.getSAA001());
				zd03.setZDA005(zdb002);
				zd03.setZDA003(1);
				
				hibernateTemplate.save(zd03);
				
			}else {
				zd03.setZDA005(zd03.getZDA005() + zdb002);
				
				List list = hibernateTemplate.find("from ZD01 where SAA001 = '" 
						+ sa05.getSAA001() + "' and ZDA003 = " 
						+ zd03.getZDA003() + "");
				if(list!=null && list.get(0)!=null) {
					
					ZD01 zd01 = (ZD01) list.get(0);
					//升级
					if(zd01.getZDA005()!=0 && zd03.getZDA005()>=zd01.getZDA005()) {
						zd03.setZDA003(zd03.getZDA003() + 1);
						upgrade = zd03.getZDA003();
					}
					
					hibernateTemplate.update(zd03);
				}
			}
			score = zdb002;
		}
		response.getBody().addParameter(AchievementParams.SCORE, score);
		response.getBody().addParameter(AchievementParams.UPGRADE, upgrade);
		
		return response;
	}
	
	/**
	 * 处理成就勋章
	 * @param response
	 * @param reqs
	 * @param zd02
	 * @return
	 */
	private ResponseEnvelope processMedal(ResponseEnvelope response, RequestEnvelope reqs, ZD02 zd02) {
		String medal = "";
		String zde012s = zd02.getZDE012();//勋章组ID
		
		StringTokenizer token = new StringTokenizer(zde012s, ",");
		while(token.hasMoreTokens()) {
			final String zde012 = token.nextToken();
			System.out.println(zde012);
			if(zde012!=null && !zde012.equals("")) {
				final SA05 sa05 = (SA05) super.getLoginUser(reqs);
				//final String sac001 = sa05.getSAC001();//用户ID
				final String saa001 = sa05.getSAA001();//角色ID
				List<ZD05> zd05s = getMedalGroup(saa001, zde012);
				if(zd05s!=null && zd05s.size()>0) {
					ZD05 zd05 = zd05s.get(0);
					if(zd05!=null) {
						String zde002 = zd05.getZDE002();//勋章类型
						if("1".equals(zde002)) {//合计 
							medal = processSum(zd05, sa05);
						}else if("2".equals(zde002)) {//排序
							
						}else if("3".equals(zde002)) {//首次 
							
						}else {
							
						}
					}
				}
			}
			
		}
		
		response.getBody().addParameter(AchievementParams.MEDAL, medal);
		
		return response;
	}
	

	/**
	 * 是否需要增加积分
	 * @param reqs
	 * @param zdb002 奖励积分
	 * @param zdb003 每日上线 
	 * @return
	 */
	private boolean needScore(final RequestEnvelope reqs, final int zdb002, final int zdb003) {
		final String requestId = (String) reqs.getBody().getParameter(
				RiaParamList.REQUESTID); // 交互编号
		final SA05 sa05 = (SA05) super.getLoginUser(reqs);
		final String zdd001 = super.getSeq_next("SEQ_ZD04_ZDD001",10);
		
		return (Boolean)hibernateTemplate.execute(new HibernateCallback() {
			@SuppressWarnings("unchecked")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = null;
				query = session.createQuery("from ZD04 where SAC001 = ? and ZAA001 = ? and ZDD003 = ? ");
				query.setString(0, getLoginUserID(reqs));
				query.setString(1, requestId);
				query.setDate(2, getSysDate());
				
				List list = query.list();
				if(list!=null && list.size()>0) {
					ZD04 zd04 = (ZD04) list.get(0);
					if("1".equals(zd04.getZDD004())) {//到达上限，不用加分
						return false;
					}
					
					//加分累计
					zd04.setZDD002(zd04.getZDD002() + zdb002);
					if(zd04.getZDD002() >= zdb003) {
						zd04.setZDD002(zdb003);
						zd04.setZDD004("1");
					}
					
					hibernateTemplate.update(zd04);
					return true;
				}else {
					ZD04 zd04 = new ZD04();
					zd04.setZDD001(zdd001);
					zd04.setSAC001(sa05.getSAC001());
					zd04.setZAA001(requestId);
					zd04.setZDD002(zdb002);
					zd04.setZDD003(getSysDate());
					if(zdb002 < zdb003) {
						zd04.setZDD004("0");
					}else {
						zd04.setZDD004("1");//到达上限
					}
					
					hibernateTemplate.save(zd04);
					
					return true;
					
				}
				
			}
		});
	}
	
	/**
	 * 得到勋章组
	 * @param saa001 角色ID
	 * @param zde012 勋章组ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ZD05> getMedalGroup(final String saa001, final String zde012) {
		return (List<ZD05>)hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = null;
				query = session.createQuery("from ZD05 where SAA001 = ? and ZDE012 = ? ");
				query.setString(0, saa001);
				query.setString(1, zde012);
				
				return (List<ZD05>)query.list();
				
			}
		});
	}
	
	/**
	 * 处理合计勋章
	 * @param zd05
	 * @param sac001
	 * @return 勋章提示语
	 */
	private String processSum(final ZD05 zd05, final SA05 sa05) {
		return (String)hibernateTemplate.execute(new HibernateCallback() {
			@SuppressWarnings("unchecked")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String zde007 = zd05.getZDE007();//表名
				String zde008 = zd05.getZDE008();//字段名
				String ZDE011 = "";//获得勋章提示语
				
				//合计数值 sum(字段) SQL
				Query query = session.createSQLQuery("select sum(" + zde008 + ") from " +
						zde007 + " where ZZB001 = '" + sa05.getSAC004() + "'");
				
				List sums = query.list();
				if(sums!=null && sums.size()>0) {
					//得到合计数值
					double sum = Double.parseDouble(sums.get(0).toString());

					//按等级数值下限升序排列，查询出等级数值下限值小于合计值得勋章
					Query query2 = session.createQuery("from ZD05 where " +
							"ZDE012 = ? and ZDE010 <= ? order by ZDE010 asc");
					query2.setString(0, zd05.getZDE012());//勋章组ID
					query2.setDouble(1, sum);//勋章等级数值下限
					
					List<ZD05> zd05s = query2.list();//勋章ID集合
					if(zd05s!=null && zd05s.size()>0) {
						
						//依次循环，判断该用户是否已经获得此勋章
						for(int i=0; i<zd05s.size(); i++) {
							ZD05 zd05 = zd05s.get(i);
							if(zd05!=null) {
								String zde001 = zd05s.get(i).getZDE001();
								
								//判断该用户是否已经获得此勋章
								Query query3 = session.createQuery("from ZD06 where " +
											"SAA001 = ? and SAC001 = ? and ZDE001 = ? ");
								query3.setString(0, sa05.getSAA001());
								query3.setString(1, sa05.getSAC001());
								query3.setString(2, zde001);
								
								List<ZD06> zd06s = query3.list();//用户是否已经获得此勋章
								if(zd06s==null || zd06s.size()==0) {//到得新勋章
									saveZD06(sa05, zde001, (int)sum);
									ZDE011 = zd05.getZDE011();//依次循环后，返回最后获得的勋章提示语（最高级别）
									
								}
							}
						}
					}
				}
				
				return ZDE011;
				
			}
		});
	}
	
	private void saveZD06(SA05 sa05, String zde001, int zdf003) {
		ZD06 zd06 = new ZD06();
		zd06.setZDF001(super.getSeq_next("SEQ_ZD06_ZDF001",10));
		zd06.setZDF002(super.getSysDateTime());
		zd06.setZDF003(zdf003);
		zd06.setSAA001(sa05.getSAA001());
		zd06.setSAC001(sa05.getSAC001());
		zd06.setZDE001(zde001);
		
		hibernateTemplate.save(zd06);
		
	}

}
