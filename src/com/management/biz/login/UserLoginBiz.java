package com.management.biz.login;

import com.icfp.frame.biz.BaseManager;
import com.icfp.frame.ria.request.RequestEnvelope;
import com.icfp.frame.ria.response.ResponseEnvelope;

/**
 * 类UserLoginBiz.java的实现描述： 系统登录接口
 *
 * @author       李雷 lilei@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-17
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public interface UserLoginBiz extends BaseManager {
	
	/**
	 * 显示登陆页
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope ShowLoginFtl(RequestEnvelope revp);
	
	/**
	 * 显示武刚测试登录页
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope ShowTestLoginFtl(RequestEnvelope revp);
	
	
	/**
	 * 显示修改密码模板页
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope showChangePWFtl(RequestEnvelope revp);
	
	/**
	 * 修改密码
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope ChangePassWord(RequestEnvelope revp);
	
	
	/**
	 * 校验账户
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope checkLogin(RequestEnvelope revp);
	
	/**
	 * 显示主页面
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope ShowMainFtl(RequestEnvelope revp);
	
	/**
	 * 显示主操作界面
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope ShowRightFtl(RequestEnvelope revp);
	
	/**
	 * 登出系统
	 * @param revp
	 * @return
	 */
	public ResponseEnvelope logout(RequestEnvelope revp);
	
}
