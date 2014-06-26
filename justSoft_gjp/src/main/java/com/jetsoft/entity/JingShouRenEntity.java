/**
 * 创建日期 2012-11-29
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
/**
 * "_py":"ZY5",
 * "fullName":"职员5",
 * "id":"00006",
 * "parID":"00000",
 * "personCode":"5",
 * "sonNum":1
 * 
 *
 * @version 1.0
 * @author Administrator
 */
public class JingShouRenEntity extends BaseEntity{
	
	private String _py;
	private String personCode;
	
	private String id = "";
	
	public String get_py() {
		return _py;
	}
	public void set_py(String py) {
		_py = py;
	}
	public String getPersonCode() {
		return personCode;
	}
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
