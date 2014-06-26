package com.jetsoft.entity;

/**
 *  "fullName":"零售价",
 *  "id":"0001"
 * @author funever_win8
 *
 */
public class PriceEntity extends BaseEntity{
	
	private String id;
	
	private String fullName;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
