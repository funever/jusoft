package com.jetsoft.activity.draft;

/**
 * 单据实体
 * {"bilID":"36",
 * "bilType":"34",
 * "dFullName":"",
 * "date":"2013-07-04",
 * "eFullName":"职员1",
 * "memo":"",
 * "number":"JH-2013-07-04-014",
 * "uFullName":"往来1"},
 * @author funever_win8
 *
 */
public class DJEntity{

	private String bilID;
	private String bilType;
	private String dFullName;
	private String date;
	private String eFullName;
	private String memo;
	private String uFullName;
	private String number;
	
	public String getBilID() {
		return bilID;
	}
	public void setBilID(String bilID) {
		this.bilID = bilID;
	}
	public String getBilType() {
		return bilType;
	}
	public void setBilType(String bilType) {
		this.bilType = bilType;
	}
	public String getdFullName() {
		return dFullName;
	}
	public void setdFullName(String dFullName) {
		this.dFullName = dFullName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String geteFullName() {
		return eFullName;
	}
	public void seteFullName(String eFullName) {
		this.eFullName = eFullName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getuFullName() {
		return uFullName;
	}
	public void setuFullName(String uFullName) {
		this.uFullName = uFullName;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}