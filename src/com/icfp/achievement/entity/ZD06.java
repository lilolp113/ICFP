package com.icfp.achievement.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 类ZD06.java的实现描述： 用户已获勋章表
 *
 * @author       王兴 wangx@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-6-19
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class ZD06 implements Serializable {
	
	/**
	 * ID
	 */
	private String ZDF001;
	
	/**
	 * 获得时间
	 */
	private Date ZDF002;
	
	/**
	 * 勋章数值
	 */
	private int ZDF003;
	
	/**
	 * 预留
	 */
	private String ZDF004;
	
	/**
	 * 预留
	 */
	private String ZDF005;

	/**
	 * 用户ID
	 */
	private String SAC001;
	
	/**
	 * 角色ID
	 */
	private String SAA001;

	/**
	 * 勋章ID
	 */
	private String ZDE001;

	/**
	 * @return the zDF001
	 */
	public String getZDF001() {
		return ZDF001;
	}

	/**
	 * @param zDF001 the zDF001 to set
	 */
	public void setZDF001(String zDF001) {
		ZDF001 = zDF001;
	}

	/**
	 * @return the zDF002
	 */
	public Date getZDF002() {
		return ZDF002;
	}

	/**
	 * @param zDF002 the zDF002 to set
	 */
	public void setZDF002(Date zDF002) {
		ZDF002 = zDF002;
	}

	/**
	 * @return the zDF003
	 */
	public int getZDF003() {
		return ZDF003;
	}

	/**
	 * @param zDF003 the zDF003 to set
	 */
	public void setZDF003(int zDF003) {
		ZDF003 = zDF003;
	}

	/**
	 * @return the zDF004
	 */
	public String getZDF004() {
		return ZDF004;
	}

	/**
	 * @param zDF004 the zDF004 to set
	 */
	public void setZDF004(String zDF004) {
		ZDF004 = zDF004;
	}

	/**
	 * @return the zDF005
	 */
	public String getZDF005() {
		return ZDF005;
	}

	/**
	 * @param zDF005 the zDF005 to set
	 */
	public void setZDF005(String zDF005) {
		ZDF005 = zDF005;
	}

	/**
	 * @return the sAC001
	 */
	public String getSAC001() {
		return SAC001;
	}

	/**
	 * @param sAC001 the sAC001 to set
	 */
	public void setSAC001(String sAC001) {
		SAC001 = sAC001;
	}

	/**
	 * @return the sAA001
	 */
	public String getSAA001() {
		return SAA001;
	}

	/**
	 * @param sAA001 the sAA001 to set
	 */
	public void setSAA001(String sAA001) {
		SAA001 = sAA001;
	}

	/**
	 * @return the zDE001
	 */
	public String getZDE001() {
		return ZDE001;
	}

	/**
	 * @param zDE001 the zDE001 to set
	 */
	public void setZDE001(String zDE001) {
		ZDE001 = zDE001;
	}

}
