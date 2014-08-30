package com.icfp.achievement.entity;

import java.io.Serializable;

/**
 * 类ZD02.java的实现描述： 积分参数表
 *
 * @author       王兴 wangx@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-24
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class ZD02 implements Serializable {
	
	/**
	 * 交互编号
	 */
	private String ZAA001;
	
	/**
	 * 奖励积分
	 */
	private int ZDB002;
	
	/**
	 * 每日上限
	 */
	private int ZDB003;
	
	/**
	 * 勋章组ID
	 */
	private String ZDE012;
	
	public ZD02() {
		super();
	}

	/**
	 * @return the zAA001
	 */
	public String getZAA001() {
		return ZAA001;
	}

	/**
	 * @param zAA001 the zAA001 to set
	 */
	public void setZAA001(String zAA001) {
		ZAA001 = zAA001;
	}

	/**
	 * @return the zDB002
	 */
	public int getZDB002() {
		return ZDB002;
	}

	/**
	 * @param zDB002 the zDB002 to set
	 */
	public void setZDB002(int zDB002) {
		ZDB002 = zDB002;
	}

	/**
	 * @return the zDB003
	 */
	public int getZDB003() {
		return ZDB003;
	}

	/**
	 * @param zDB003 the zDB003 to set
	 */
	public void setZDB003(int zDB003) {
		ZDB003 = zDB003;
	}

	/**
	 * @return the zDE012
	 */
	public String getZDE012() {
		return ZDE012;
	}

	/**
	 * @param zDE012 the zDE012 to set
	 */
	public void setZDE012(String zDE012) {
		ZDE012 = zDE012;
	}
	
	
	
}
