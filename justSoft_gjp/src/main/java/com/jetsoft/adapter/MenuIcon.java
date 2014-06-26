/**
 * 创建日期 2012-11-26
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
package com.jetsoft.adapter;

public class MenuIcon {
	
	private String title;
	
	private int icon_res;
	
	private Class activity;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon_res() {
		return icon_res;
	}

	public void setIcon_res(int iconRes) {
		icon_res = iconRes;
	}

	public Class getActivity() {
		return activity;
	}

	public void setActivity(Class activity) {
		this.activity = activity;
	}
}
