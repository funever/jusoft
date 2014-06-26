/**
 * 创建日期 2013-2-20
 * 创建用户 Administrator
 * 变更情况:
 * 文档位置 $Archive$
 * 最后变更 $Author$
 * 变更日期 $Date$
 * 当前版本 $Revision$
 * 
 * Copyright (c) 2004 Sino-Japanese Engineering Corp, Inc. All Rights Reserved.
 * 
 */
package com.jetsoft.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.jetsoft.utils.StaticValues;

public class PanDianProduct implements Serializable{
	
	private int Num;
	
	private String Price;
	
	private String Amount;
	
	private String PersonCode;
	
	private String FullName;
	
	private String Standard;
	
	private String Type;
	
	private String Area;
	
	private String id;
	
	private int panDianNum;
	
	private String uRate0;
	private String uRate1 ;
	private String uRate2;
	
	private String uRateBil;

	public int getPanDianNum() {
		return panDianNum;
	}

	public void setPanDianNum(int panDianNum) {
		this.panDianNum = panDianNum;
	}

	public int getNum() {
		return Num;
	}

	public void setNum(int num) {
		Num = num;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		try{
			BigDecimal bd = new BigDecimal(price);
			Price = bd.toPlainString();
			Price = StaticValues.format(Price, 2);
		}catch (Exception e) {
			Price = price;
		}
	}

	public String getAmount() {
		return StaticValues.format(Amount, 2);
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getPersonCode() {
		return PersonCode;
	}

	public void setPersonCode(String personCode) {
		PersonCode = personCode;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public String getStandard() {
		return Standard;
	}

	public void setStandard(String standard) {
		Standard = standard;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getuRate0() {
		return uRate0;
	}

	public void setuRate0(String uRate0) {
		this.uRate0 = uRate0;
	}

	public String getuRate1() {
		return uRate1;
	}

	public void setuRate1(String uRate1) {
		this.uRate1 = uRate1;
	}

	public String getuRate2() {
		return uRate2;
	}

	public void setuRate2(String uRate2) {
		this.uRate2 = uRate2;
	}

	public String getuRateBil() {
		return uRateBil;
	}

	public void setuRateBil(String uRateBil) {
		this.uRateBil = uRateBil;
	}
	
}
