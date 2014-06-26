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
package com.jetsoft.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {
	
	private String eUserCode;
	
	private String efullName;
	
	private String id;
	
	private String isManager;
	
	private String password;

	public String geteUserCode() {
		return eUserCode;
	}

	public void seteUserCode(String eUserCode) {
		this.eUserCode = eUserCode;
	}

	public String getEfullName() {
		return efullName;
	}

	public void setEfullName(String efullName) {
		this.efullName = efullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
