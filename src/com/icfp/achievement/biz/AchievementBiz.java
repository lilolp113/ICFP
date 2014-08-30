package com.icfp.achievement.biz;

import com.icfp.frame.biz.BaseManager;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;

/**
 * 类AchievementBiz.java的实现描述： 成就系统接口
 *
 * @author       王兴 wangx@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-6-20
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public interface AchievementBiz extends BaseManager {
	
	/**
	 * 成就页面
	 * @param requestEnvelope
	 * @return
	 */
	public ResponseEnvelope showListFtl(RequestEnvelope requestEnvelope);
	
	/**
	 * 成就列表
	 * @param requestEnvelope
	 * @return
	 */
	public ResponseEnvelope achievementList(RequestEnvelope requestEnvelope);

}
