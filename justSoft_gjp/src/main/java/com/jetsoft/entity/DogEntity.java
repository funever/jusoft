/**
 * 创建日期 2013-4-18
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

public class DogEntity implements Serializable{
	
	public String dogNum;
	public String status;
	public String userCount;
	public String getDogNum() {
		return dogNum;
	}
	public void setDogNum(String dogNum) {
		this.dogNum = dogNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserCount() {
		return userCount;
	}
	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}
}
