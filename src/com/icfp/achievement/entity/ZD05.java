package com.icfp.achievement.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 类ZD05.java的实现描述： 勋章规则表
 *
 * @author       王兴 wangx@cs-cs.com.cn
 * @version      1.0
 * Date			 2013-6-19
 * @see          
 * History： 
 *		<author>   <time>	<version>   <desc>
 *
 */
public class ZD05 implements Serializable {
	
	/**
	 * ID
	 */
	private String ZDE001;

	/**
	 * 角色ID
	 */
	private String SAA001;

	/**
	 * 勋章类型 1：合计，2：排序，3：首次
	 */
	private String ZDE002;

	/**
	 * 是否需要升级
	 */
	private String ZDE003;

	/**
	 * 勋章名称
	 */
	private String ZDE004;

	/**
	 * 勋章名称
	 */
	private int ZDE005;

	/**
	 * 勋章描述
	 */
	private String ZDE006;

	/**
	 * 勋章来源表名
	 */
	private String ZDE007;

	/**
	 * 勋章来源字段名
	 */
	private String ZDE008;

	/**
	 * 时间精度，结合勋章类型进行年月日精度确认（预留）0：无，1：年，2：月，3：日
	 */
	private String ZDE009;

	/**
	 * 等级数值下线
	 */
	private int ZDE010;

	/**
	 * 勋章获得后提示语
	 */
	private String ZDE011;

	/**
	 * 勋章组ID
	 */
	private String ZDE012;

	/**
	 * 勋章组名称
	 */
	private String ZDE013;

	/**
	 * 添加人
	 */
	private String ZDE014;

	/**
	 * 添加时间
	 */
	private Date ZDE015;
	
	/**
	 * 勋章组描述
	 */
	private String ZDE016;
	
	/**
	 * 勋章图片文件名
	 */
	private String ZDE017;

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
	 * @return the zDE002
	 */
	public String getZDE002() {
		return ZDE002;
	}

	/**
	 * @param zDE002 the zDE002 to set
	 */
	public void setZDE002(String zDE002) {
		ZDE002 = zDE002;
	}

	/**
	 * @return the zDE003
	 */
	public String getZDE003() {
		return ZDE003;
	}

	/**
	 * @param zDE003 the zDE003 to set
	 */
	public void setZDE003(String zDE003) {
		ZDE003 = zDE003;
	}

	/**
	 * @return the zDE004
	 */
	public String getZDE004() {
		return ZDE004;
	}

	/**
	 * @param zDE004 the zDE004 to set
	 */
	public void setZDE004(String zDE004) {
		ZDE004 = zDE004;
	}

	/**
	 * @return the zDE005
	 */
	public int getZDE005() {
		return ZDE005;
	}

	/**
	 * @param zDE005 the zDE005 to set
	 */
	public void setZDE005(int zDE005) {
		ZDE005 = zDE005;
	}

	/**
	 * @return the zDE006
	 */
	public String getZDE006() {
		return ZDE006;
	}

	/**
	 * @param zDE006 the zDE006 to set
	 */
	public void setZDE006(String zDE006) {
		ZDE006 = zDE006;
	}

	/**
	 * @return the zDE007
	 */
	public String getZDE007() {
		return ZDE007;
	}

	/**
	 * @param zDE007 the zDE007 to set
	 */
	public void setZDE007(String zDE007) {
		ZDE007 = zDE007;
	}

	/**
	 * @return the zDE008
	 */
	public String getZDE008() {
		return ZDE008;
	}

	/**
	 * @param zDE008 the zDE008 to set
	 */
	public void setZDE008(String zDE008) {
		ZDE008 = zDE008;
	}

	/**
	 * @return the zDE009
	 */
	public String getZDE009() {
		return ZDE009;
	}

	/**
	 * @param zDE009 the zDE009 to set
	 */
	public void setZDE009(String zDE009) {
		ZDE009 = zDE009;
	}

	/**
	 * @return the zDE010
	 */
	public int getZDE010() {
		return ZDE010;
	}

	/**
	 * @param zDE010 the zDE010 to set
	 */
	public void setZDE010(int zDE010) {
		ZDE010 = zDE010;
	}

	/**
	 * @return the zDE011
	 */
	public String getZDE011() {
		return ZDE011;
	}

	/**
	 * @param zDE011 the zDE011 to set
	 */
	public void setZDE011(String zDE011) {
		ZDE011 = zDE011;
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

	/**
	 * @return the zDE013
	 */
	public String getZDE013() {
		return ZDE013;
	}

	/**
	 * @param zDE013 the zDE013 to set
	 */
	public void setZDE013(String zDE013) {
		ZDE013 = zDE013;
	}

	/**
	 * @return the zDE014
	 */
	public String getZDE014() {
		return ZDE014;
	}

	/**
	 * @param zDE014 the zDE014 to set
	 */
	public void setZDE014(String zDE014) {
		ZDE014 = zDE014;
	}

	/**
	 * @return the zDE015
	 */
	public Date getZDE015() {
		return ZDE015;
	}

	/**
	 * @param zDE015 the zDE015 to set
	 */
	public void setZDE015(Date zDE015) {
		ZDE015 = zDE015;
	}

	/**
	 * @return the zDE016
	 */
	public String getZDE016() {
		return ZDE016;
	}

	/**
	 * @param zDE016 the zDE016 to set
	 */
	public void setZDE016(String zDE016) {
		ZDE016 = zDE016;
	}

	/**
	 * @return the zDE017
	 */
	public String getZDE017() {
		return ZDE017;
	}

	/**
	 * @param zDE017 the zDE017 to set
	 */
	public void setZDE017(String zDE017) {
		ZDE017 = zDE017;
	}
	
}
