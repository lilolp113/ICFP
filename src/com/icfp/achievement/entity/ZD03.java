package com.icfp.achievement.entity;

import java.io.Serializable;

/**
 * 类ZD03.java的实现描述： 用户积分成就类
 *
 * @author       王兴 wangx@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-23
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class ZD03 implements Serializable {
	/**
	 * 用户编号
	 */
	private String SAC001;
	
	/**
	 * 角色编号
	 */
	private String SAA001;
	
	/**
	 * 积分
	 */
	private int ZDA005;
	
	/**
	 * 等级
	 */
	private int ZDA003;
	
	/**
	 * 预留
	 */
	private int ZDD001;

	/**
	 * @return the sAC001
	 */
	public String getSAC001() {
		return SAC001;
	}
	
	public ZD03() {
		super();
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
	 * @return the zDD001
	 */
	public int getZDD001() {
		return ZDD001;
	}

	/**
	 * @param zDD001 the zDD001 to set
	 */
	public void setZDD001(int zDD001) {
		ZDD001 = zDD001;
	}
	
	
	
}
