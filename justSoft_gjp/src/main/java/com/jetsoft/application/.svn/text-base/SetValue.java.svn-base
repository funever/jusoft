/**
 * 创建日期 2013-3-12
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

import com.jetsoft.activity.BaseActivity;
import com.jetsoft.activity.login.ZTActivity;
/**
 * 扫描器获取到值得中间类
 * 单例
 * 在每个具体的activity需要重新调用registActivity进行注册
 * @version 1.0
 * @author Administrator
 */
public class SetValue {
	
	private static SetValue sv;
	
	private BaseActivity baseActivity;
	
	private ZTActivity ztActivity;
	
	private SetValue(){
		
	}
	
	public static SetValue getIntance(){
		if(sv == null){
			sv = new SetValue();
		}
		return sv;
	}
	/**
	 * 对activity进行注册，此activity的editext才能设置扫描的值
	 * @param baseActivity
	 */
	public void registActivity(BaseActivity baseActivity){
		this.baseActivity = baseActivity;
	}
	
	/**
	 * 触发填值，在ZTActivity中调用
	 * @param value
	 */
	public void setScannerValue(String value){
		if(baseActivity == null){
			return;
		}
		baseActivity.setScanValue(value);
	}
	/**
	 * 关闭蓝牙扫描器服务
	 */
	public void unregisterReceiver(){
		if(ztActivity==null){
			return;
		}
//		ztActivity.unregisterReceiver(ztActivity.myReceiver);
	}
	
	public void onPause(){
		if(ztActivity==null){
			return;
		}
//		CommonUtils.pausebtservice(ztActivity);
	}
	
	public void onResume(){
		if(ztActivity==null){
			return;
		}
//		 CommonUtils.resumebtservice(ztActivity);
	}

	public ZTActivity getZtActivity() {
		return ztActivity;
	}

	public void setZtActivity(ZTActivity ztActivity) {
		this.ztActivity = ztActivity;
	}
}
