package com.icfp.achievement.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 类ZD04.java的实现描述： 当日所获积分统计
 *
 * @author       王兴 wangx@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-5-23
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class ZD04 implements Serializable {
	/**
	 * ID 
	 */
	private String ZDD001;
	
	/**
	 * 用户编号
	 */
	private String SAC001;
	
	/**
	 * 交互编号
	 */
	private String ZAA001;
	
	/**
	 * 当日获得积分总数
	 */
	private int ZDD002;
	
	/**
	 * 日期
	 */
	private Date ZDD003;
	
	/**
	 * 是否达到上限（0：否，1：是）
	 */
	private String ZDD004;

	public ZD04() {
		super();
	}

	/**
	 * @return the zDD001
	 */
	public String getZDD001() {
		return ZDD001;
	}

	/**
	 * @param zDD001 the zDD001 to set
	 */
	public void setZDD001(String zDD001) {
		ZDD001 = zDD001;
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
	 * @return the zDD002
	 */
	public int getZDD002() {
		return ZDD002;
	}

	/**
	 * @param zDD002 the zDD002 to set
	 */
	public void setZDD002(int zDD002) {
		ZDD002 = zDD002;
	}

	/**
	 * @return the zDD003
	 */
	public Date getZDD003() {
		return ZDD003;
	}

	/**
	 * @param zDD003 the zDD003 to set
	 */
	public void setZDD003(Date zDD003) {
		ZDD003 = zDD003;
	}

	/**
	 * @return the zDD004
	 */
	public String getZDD004() {
		return ZDD004;
	}

	/**
	 * @param zDD004 the zDD004 to set
	 */
	public void setZDD004(String zDD004) {
		ZDD004 = zDD004;
	}
	
	

}
