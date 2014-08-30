package com.icfp.frame.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AA02 implements java.io.Serializable {
	
	private static final long serialVersionUID = -5924680965078392119L;

	private AA02Id id;
	
	private String AAA003;
	
	private String AAA004;
	
	private Date AAA005;
	
	private Date AAA006;
	
	private String ZZA001;
	
	private String ZZB001;
	
	private Date ZZB002;
	
	private BigDecimal ZZE001;
	
	private BigDecimal ZZE002;

	public AA02() {
		
	}

	public AA02Id getId() {
		return id;
	}

	public void setId(AA02Id id) {
		this.id = id;
	}

	public String getAAA003() {
		return AAA003;
	}

	public void setAAA003(String aAA003) {
		AAA003 = aAA003;
	}

	public String getAAA004() {
		return AAA004;
	}

	public void setAAA004(String aAA004) {
		AAA004 = aAA004;
	}

	public Date getAAA005() {
		return AAA005;
	}

	public void setAAA005(Date aAA005) {
		AAA005 = aAA005;
	}

	public Date getAAA006() {
		return AAA006;
	}

	public void setAAA006(Date aAA006) {
		AAA006 = aAA006;
	}

	public String getZZA001() {
		return ZZA001;
	}

	public void setZZA001(String zZA001) {
		ZZA001 = zZA001;
	}

	public String getZZB001() {
		return ZZB001;
	}

	public void setZZB001(String zZB001) {
		ZZB001 = zZB001;
	}

	public Date getZZB002() {
		return ZZB002;
	}

	public void setZZB002(Date zZB002) {
		ZZB002 = zZB002;
	}

	public BigDecimal getZZE001() {
		return ZZE001;
	}

	public void setZZE001(BigDecimal zZE001) {
		ZZE001 = zZE001;
	}

	public BigDecimal getZZE002() {
		return ZZE002;
	}

	public void setZZE002(BigDecimal zZE002) {
		ZZE002 = zZE002;
	}

}