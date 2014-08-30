package com.icfp.achievement.entity;

import java.io.Serializable;

/**
 * 类ZD01.java的实现描述： 等级参数表
 *
 * @author       王兴 wangx@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-24
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class ZD01 implements Serializable {
	
	/**
	 * ID 
	 */
	private int ZDA001;
	
	/**
	 * 角色ID
	 */
	private String SAA001;
	
	/**
	 * 等级名称
	 */
	private String ZDA002;

	/**
	 * 等级
	 */
	private int ZDA003;
	
	/**
	 * 描述
	 */
	private String ZDA004;
	
	/**
	 * 等级积分上限
	 */
	private int ZDA005;
	
	/**
	 * @return the zDA001
	 */
	public int getZDA001() {
		return ZDA001;
	}

	public ZD01() {
		super();
	}

	/**
	 * @param zDA001 the zDA001 to set
	 */
	public void setZDA001(int zDA001) {
		ZDA001 = zDA001;
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
	 * @return the zDA002
	 */
	public String getZDA002() {
		return ZDA002;
	}

	/**
	 * @param zDA002 the zDA002 to set
	 */
	public void setZDA002(String zDA002) {
		ZDA002 = zDA002;
	}

	/**
	 * @return the zDA003
	 */
	public int getZDA003() {
		return ZDA003;
	}

	/**
	 * @param zDA003 the zDA003 to set
	 */
	public void setZDA003(int zDA003) {
		ZDA003 = zDA003;
	}

	/**
	 * @return the zDA004
	 */
	public String getZDA004() {
		return ZDA004;
	}

	/**
	 * @param zDA004 the zDA004 to set
	 */
	public void setZDA004(String zDA004) {
		ZDA004 = zDA004;
	}

	/**
	 * @return the zDA005
	 */
	public int getZDA005() {
		return ZDA005;
	}

	/**
	 * @param zDA005 the zDA005 to set
	 */
	public void setZDA005(int zDA005) {
		ZDA005 = zDA005;
	}
	
}
