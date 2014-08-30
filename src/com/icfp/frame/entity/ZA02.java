package com.icfp.frame.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ZA02 implements java.io.Serializable{
	
	private static final long serialVersionUID = 1152257571364512577L;

	private String ZAA001;
	
	private String ZAA002;
	
	private String ZAA003;
	
	private String ZAA004;
	
	private String ZAA005;
	
	private String ZZB001;
	
	private Date ZZB002;
	
	private BigDecimal ZZE001;
	
	private BigDecimal ZZE002;
	
	public ZA02(){
		
	}

	public String getZAA001() {
		return this.ZAA001;
	}

	public void setZAA001(String zaa001) {
		this.ZAA001 = zaa001;
	}

	public String getZAA002() {
		return this.ZAA002;
	}

	public void setZAA002(String zaa002) {
		this.ZAA002 = zaa002;
	}

	public String getZAA003() {
		return this.ZAA003;
	}

	public void setZAA003(String zaa003) {
		this.ZAA003 = zaa003;
	}

	public String getZAA004() {
		return this.ZAA004;
	}

	public void setZAA004(String zaa004) {
		this.ZAA004 = zaa004;
	}

	public String getZAA005() {
		return this.ZAA005;
	}

	public void setZAA005(String zaa005) {
		this.ZAA005 = zaa005;
	}

	public String getZZB001() {
		return this.ZZB001;
	}

	public void setZZB001(String zzb001) {
		this.ZZB001 = zzb001;
	}

	public Date getZZB002() {
		return this.ZZB002;
	}

	public void setZZB002(Date zzb002) {
		this.ZZB002 = zzb002;
	}

	public BigDecimal getZZE001() {
		return this.ZZE001;
	}

	public void setZZE001(BigDecimal zze001) {
		this.ZZE001 = zze001;
	}

	public BigDecimal getZZE002() {
		return this.ZZE002;
	}

	public void setZZE002(BigDecimal zze002) {
		this.ZZE002 = zze002;
	}
}
