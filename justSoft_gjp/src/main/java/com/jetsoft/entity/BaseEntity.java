/**
 * 创建日期 2012-12-28
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

public class BaseEntity extends Entity {
	
	private String fullName;
	
	private String id = "00000";
	
	private String parID = "0";
	
	private String sonNum;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParID() {
		return parID;
	}

	public void setParID(String parID) {
		this.parID = parID;
	}

	public String getSonNum() {
		return sonNum;
	}

	public void setSonNum(String sonNum) {
		this.sonNum = sonNum;
	}
}
