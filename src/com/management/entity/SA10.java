package com.management.entity;

import java.math.BigDecimal;
import java.util.Date;

public class SA10 implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private SA10ID id;

	private String ZZB001;

	private Date ZZB002;

	private BigDecimal ZZD002;

	private BigDecimal ZZE001;

	private BigDecimal ZZE002;

	public SA10ID getId() {
		return id;
	}

	public void setId(SA10ID id) {
		this.id = id;
	}

	public String getZZB001() {
		return ZZB001;
	}

	public void setZZB001(String zzb001) {
		ZZB001 = zzb001;
	}

	public Date getZZB002() {
		return ZZB002;
	}

	public void setZZB002(Date zzb002) {
		ZZB002 = zzb002;
	}

	public BigDecimal getZZD002() {
		return ZZD002;
	}

	public void setZZD002(BigDecimal zzd002) {
		ZZD002 = zzd002;
	}

	public BigDecimal getZZE001() {
		return ZZE001;
	}

	public void setZZE001(BigDecimal zze001) {
		ZZE001 = zze001;
	}

	public BigDecimal getZZE002() {
		return ZZE002;
	}

	public void setZZE002(BigDecimal zze002) {
		ZZE002 = zze002;
	}

}