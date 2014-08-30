package com.icfp.frame.entity;

import java.util.Date;

/**日志信息
 * @author wangxing
 *2011-10-21
 */
public class SA99 implements java.io.Serializable {

	private static final long serialVersionUID = 7126401908395497613L;

	private String saz001;

	private String saz002;

	private String saz003;
	
	private String saz004;

	private String saz005;
	
	private String saz006;
	
	private Date saz007;
	
	public SA99() {
		
	}

	public SA99(String saz001, String saz002, String saz003, String saz004, String saz005, String saz006, Date saz007) {
		super();
		this.saz001 = saz001;
		this.saz002 = saz002;
		this.saz003 = saz003;
		this.saz004 = saz004;
		this.saz005 = saz005;
		this.saz006 = saz006;
		this.saz007 = saz007;
	}

	public String getSaz001() {
		return saz001;
	}

	public void setSaz001(String saz001) {
		this.saz001 = saz001;
	}

	public String getSaz002() {
		return saz002;
	}

	public void setSaz002(String saz002) {
		this.saz002 = saz002;
	}

	public String getSaz003() {
		return saz003;
	}

	public void setSaz003(String saz003) {
		this.saz003 = saz003;
	}

	public String getSaz004() {
		return saz004;
	}

	public void setSaz004(String saz004) {
		this.saz004 = saz004;
	}

	public String getSaz005() {
		return saz005;
	}

	public void setSaz005(String saz005) {
		this.saz005 = saz005;
	}

	public String getSaz006() {
		return saz006;
	}

	public void setSaz006(String saz006) {
		this.saz006 = saz006;
	}

	public Date getSaz007() {
		return saz007;
	}

	public void setSaz007(Date saz007) {
		this.saz007 = saz007;
	}

	
}