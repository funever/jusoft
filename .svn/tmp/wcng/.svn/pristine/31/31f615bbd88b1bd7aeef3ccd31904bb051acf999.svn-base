/**
 * 创建日期 2012-12-25
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
package com.jetsoft.application;

import java.util.Timer;

import org.apache.http.client.HttpClient;

import cn.jpush.android.api.JPushInterface;

import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.JingShouRenEntity;
import com.jetsoft.entity.UserEntity;
import com.jetsoft.entity.ZTEntity;
import com.jetsoft.utils.ExceptionHandler;

import android.app.Application;

public class MyApplication extends Application{
	
	private ZTEntity ztEntity;
	
	private String period;
	 /**
	  * 数量保留小数位
	  */
	 int numDigit;
	 
	 /**
	  * 金额保留小数位
	  */
	 int amountDigit;
	 
	 /**
	  * 单价保留小数位
	  */
	 int priceDigit;
	 
	 /**
	  * 是否有成本权限
	  */
	 int rightCost;
	 
	 int BilPrice;
	 
	 int setGoodsPrice;
	 
	 boolean printStyle;
	 
	private HttpClient client;
	
	private UserEntity userEntity;
	
	private JingShouRenEntity jsr;
	
	private CangKuEntity shck;
	
	private CangKuEntity fhck;
	
	public ExceptionHandler crashHandler;
	
	public Timer timer;
	
	public  static boolean sending = false;

	public ZTEntity getZtEntity() {
		return ztEntity;
	}

	public void setZtEntity(ZTEntity ztEntity) {
		this.ztEntity = ztEntity;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public  HttpClient getClient() {
//		System.out.println("client:"+client.toString());
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
	
	public int getNumDigit() {
		return numDigit;
	}

	public void setNumDigit(int numDigit) {
		this.numDigit = numDigit;
	}

	public int getAmountDigit() {
		return amountDigit;
	}

	public void setAmountDigit(int amountDigit) {
		this.amountDigit = amountDigit;
	}

	public int getPriceDigit() {
		return priceDigit;
	}

	public void setPriceDigit(int priceDigit) {
		this.priceDigit = priceDigit;
	}

	public int getRightCost() {
		return rightCost;
	}

	public void setRightCost(int rightCost) {
		this.rightCost = rightCost;
	}
	
	public int getBilPrice() {
		return BilPrice;
	}

	public void setBilPrice(int bilPrice) {
		BilPrice = bilPrice;
	}
	
	public JingShouRenEntity getJsr() {
		return jsr;
	}

	public void setJsr(JingShouRenEntity jsr) {
		this.jsr = jsr;
	}

	public CangKuEntity getShck() {
		return shck;
	}

	public void setShck(CangKuEntity shck) {
		this.shck = shck;
	}

	public CangKuEntity getFhck() {
		return fhck;
	}

	public void setFhck(CangKuEntity fhck) {
		this.fhck = fhck;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		 crashHandler = ExceptionHandler.getInstance(this);  
		 //注册crashHandler   
		 crashHandler.init(getApplicationContext());  
		 //发送以前没发送的报告(可选)   
		 crashHandler.sendPreviousReportsToServer();  
		 JPushInterface.setDebugMode(true);
		 JPushInterface.init(this);
	}

	public ExceptionHandler getCrashHandler() {
		return crashHandler;
	}

	public void setCrashHandler(ExceptionHandler crashHandler) {
		this.crashHandler = crashHandler;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public int getSetGoodsPrice() {
		return setGoodsPrice;
	}

	public void setSetGoodsPrice(int setGoodsPrice) {
		this.setGoodsPrice = setGoodsPrice;
	}

	public boolean isPrintStyle() {
		return printStyle;
	}

	public void setPrintStyle(boolean printStyle) {
		this.printStyle = printStyle;
	}
}
