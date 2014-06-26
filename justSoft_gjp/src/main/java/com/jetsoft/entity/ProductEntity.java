package com.jetsoft.entity;

import java.io.Serializable;

import com.jetsoft.utils.Utils;

public class ProductEntity extends BaseEntity implements Serializable,Cloneable{
	
	/**
	 * {
	 * "_py":"sp1",
	 * "area":"",
	 * "coseKind":"",
	 * "fullName":"商品1",
	 * "id":"00001",
	 * "parID":"00000",
	 * "personCode":"1",
	 * "sonNum":0,
	 * "standard":"",
	 * "type":"",
	 * "uRate0":"1.0000000000",
	 * "uRate1":"12.0000000000",
	 * "uRate2":"0E-10",
	 * "unitID":"-1",
	 * "unitName0":"g",
	 * "unitName1":"gg",
	 * "unitName2":"",
	 * "unitNameList":"1gg=12g"
	 * }
	 */
	
	private String _py;
	private String area;
	private String coseKind;
	private String fullName;
	private String id = "";
	private String parID;
	private String personCode;
	private String sonNum;
	private String standard;
	private String type;
	private String uRate0;
	private String uRate1;
	private String unitID;
	private String unitName0;
	private String unitName1;
	private String unitName2;
	private String unitNameList;
	
	/*
	 * 扩展字段
	 */
	private double num = 1;
	
	private String memo;
	
	private String date = Utils.getFormatDate(0);
	
	private float yanhuo;
	
	private float numUnit1 = 1;
	
	private int dNumUnit1;
	
	private float weiyan;
	
	private boolean orderPro;
	
	private String bilCode;
	
	private String largess = "0";
	/**
	 * 税率
	 */
	private float tax;
	
	/*
	 * 二次请求价格数据字段
	 */
	private String unitCode;
	private String unit1;
	private String uRateBil;
	private String barCode;
	private String priceRec = "0";
	private String disRec = "0";
	private String promoID;
	
	/*
	 * 上页下页请求的返回的属性
	 * 
	 * "amount":"200.0000000000"
		"bID":"0000100001",
		"bilCode":"65",
		"bilID":"36",
		"cosePrice":"0E-10",
		"costAmount":"0E-10",
		"costKind":"0",
		"dNumUnit1":"",
		"date":"2013-07-04",
		"disCount":"1.0000000000",
		"disCountPrice":"10.0000000000",
		"eID":"00001",
		"gFullName":"商品1",
		"gID":"00001",
		"num":20,
		"period":"1",
		"price":"10.0000000000",
		"priceUnit":"10.0000000000",
		"sID":"00001",
		"tax":"0E-10",
		"taxPrice":"10.0000000000",
		"uID":"00001",
		"uRate":"",
		"uRate0":"1.0000000000",
		"uRate1":"12.0000000000",
		"uRate2":"0E-10",
		"uRateBil":"1.0000000000",
		"unit":"0",
		"unit1":"g",
		"unitCode":"",
		"unitName0":"g",
		"unitName1":"gg",
		"unitName2":"",
		"unitNameList":"1gg=12g"
	 */
	private String amount;
	private String bID;
	private String bilID;
	private String cosePrice;
	private String costAmount;
	private String costKind;
	private String disCount;
	private String disCountPrice;
	private String eID;
	private String gFullName;
	private String gID;
	private String period;
	private String price;
	private String priceUnit;
	private String sID;
	private String taxPrice;
	private String uID;
	private String uRate;
	private String uRate2;
	private String unit;
	
	/**
	 * 外贸单选择商品返回的属性
	 * {"area":"",
		"barCode":"",
		"fullName":"商品3",
		"lowSalePrice":"0E-10",
		"num":0,
		"price1":"0E-10",
		"price2":"0E-10",
		"price3":"0E-10",
		"recSalePrice":"0E-10",
		"retailprice":"0E-10",
		"standard":"",
		"type":"",
		"uRate":"",
		"unit1":"",
		"unit2":"",
		"voidNum":0}
	 */
	
	private String lowSalePrice;
	private String price1;
	private String price2;
	private String price3;
	private String price4;
	private String price5;
	private String recSalePrice;
	private String retailprice;
	private String unit2;
	private String voidNum;
	
	private boolean isCopy = false;
	
	private ProductEntity cloneProduct;
	
	public String get_py() {
		return _py;
	}
	public void set_py(String _py) {
		this._py = _py;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCoseKind() {
		return coseKind;
	}
	public void setCoseKind(String coseKind) {
		this.coseKind = coseKind;
	}
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
	public String getPersonCode() {
		return personCode;
	}
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	public String getSonNum() {
		return sonNum;
	}
	public void setSonNum(String sonNum) {
		this.sonNum = sonNum;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getUnitID() {
		return unitID;
	}
	public void setUnitID(String unitID) {
		this.unitID = unitID;
	}
	public String getUnitName0() {
		return unitName0;
	}
	public void setUnitName0(String unitName0) {
		this.unitName0 = unitName0;
	}
	public String getUnitName1() {
		return unitName1;
	}
	public void setUnitName1(String unitName1) {
		this.unitName1 = unitName1;
	}
	public String getUnitName2() {
		return unitName2;
	}
	public void setUnitName2(String unitName2) {
		this.unitName2 = unitName2;
	}
	public String getUnitNameList() {
		return unitNameList;
	}
	public void setUnitNameList(String unitNameList) {
		this.unitNameList = unitNameList;
	}
	public double getNum() {
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnit1() {
		return unit1;
	}
	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}
	public String getuRateBil() {
		return uRateBil;
	}
	public void setuRateBil(String uRateBil) {
		this.uRateBil = uRateBil;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getPriceRec() {
		return priceRec;
	}
	public void setPriceRec(String priceRec) {
		this.priceRec = priceRec;
	}
	public String getDisRec() {
		return disRec;
	}
	public void setDisRec(String disRec) {
		this.disRec = disRec;
	}
	public String getPromoID() {
		return promoID;
	}
	public void setPromoID(String promoID) {
		this.promoID = promoID;
	}
	public float getYanhuo() {
		return yanhuo;
	}
	public void setYanhuo(float yanhuo) {
		this.yanhuo = yanhuo;
	}
	public float getNumUnit1() {
		return numUnit1;
	}
	public void setNumUnit1(float numUnit1) {
		this.numUnit1 = numUnit1;
	}
	public int getdNumUnit1() {
		return dNumUnit1;
	}
	public void setdNumUnit1(int dNumUnit1) {
		this.dNumUnit1 = dNumUnit1;
	}
	public float getWeiyan() {
		return weiyan;
	}
	public void setWeiyan(float weiyan) {
		this.weiyan = weiyan;
	}
	public boolean isOrderPro() {
		return orderPro;
	}
	public void setOrderPro(boolean orderPro) {
		this.orderPro = orderPro;
	}
	public String getBilCode() {
		return bilCode;
	}
	public void setBilCode(String bilCode) {
		this.bilCode = bilCode;
	}
	public String getLargess() {
		if(largess == null || "".equals(largess)){
			return "0";
		}
		return largess;
	}
	public void setLargess(String largess) {
		if(largess == null || "".equals(largess)){
			this.largess = "0";
		}
		this.largess = largess;
	}
	public float getTax() {
		return tax;
	}
	public void setTax(float tax) {
		this.tax = tax;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getbID() {
		return bID;
	}
	public void setbID(String bID) {
		this.bID = bID;
	}
	public String getBilID() {
		return bilID;
	}
	public void setBilID(String bilID) {
		this.bilID = bilID;
	}
	public String getCosePrice() {
		return cosePrice;
	}
	public void setCosePrice(String cosePrice) {
		this.cosePrice = cosePrice;
	}
	public String getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(String costAmount) {
		this.costAmount = costAmount;
	}
	public String getCostKind() {
		return costKind;
	}
	public void setCostKind(String costKind) {
		this.costKind = costKind;
	}
	public String getDisCount() {
		return disCount;
	}
	public void setDisCount(String disCount) {
		this.disCount = disCount;
	}
	public String getDisCountPrice() {
		return disCountPrice;
	}
	public void setDisCountPrice(String disCountPrice) {
		this.disCountPrice = disCountPrice;
	}
	public String geteID() {
		return eID;
	}
	public void seteID(String eID) {
		this.eID = eID;
	}
	public String getgFullName() {
		return gFullName;
	}
	public void setgFullName(String gFullName) {
		this.gFullName = gFullName;
	}
	public String getgID() {
		return gID;
	}
	public void setgID(String gID) {
		this.gID = gID;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getsID() {
		return sID;
	}
	public void setsID(String sID) {
		this.sID = sID;
	}
	public String getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice;
	}
	public String getuID() {
		return uID;
	}
	public void setuID(String uID) {
		this.uID = uID;
	}
	public String getuRate() {
		return uRate;
	}
	public void setuRate(String uRate) {
		this.uRate = uRate;
	}
	public String getuRate2() {
		return uRate2;
	}
	public void setuRate2(String uRate2) {
		this.uRate2 = uRate2;
	}
	public String getLowSalePrice() {
		return lowSalePrice;
	}
	public void setLowSalePrice(String lowSalePrice) {
		this.lowSalePrice = lowSalePrice;
	}
	public String getPrice1() {
		return price1;
	}
	public void setPrice1(String price1) {
		this.price1 = price1;
	}
	public String getPrice2() {
		return price2;
	}
	public void setPrice2(String price2) {
		this.price2 = price2;
	}
	public String getPrice3() {
		return price3;
	}
	public void setPrice3(String price3) {
		this.price3 = price3;
	}
	public String getRecSalePrice() {
		return recSalePrice;
	}
	public void setRecSalePrice(String recSalePrice) {
		this.recSalePrice = recSalePrice;
	}
	public String getRetailprice() {
		return retailprice;
	}
	public void setRetailprice(String retailprice) {
		this.retailprice = retailprice;
	}
	public String getUnit2() {
		return unit2;
	}
	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}
	public String getVoidNum() {
		return voidNum;
	}
	public void setVoidNum(String voidNum) {
		this.voidNum = voidNum;
	}
	public String getPrice4() {
		return price4;
	}
	public void setPrice4(String price4) {
		this.price4 = price4;
	}
	public String getPrice5() {
		return price5;
	}
	public void setPrice5(String price5) {
		this.price5 = price5;
	}
	public boolean isCopy() {
		return isCopy;
	}
	public void setCopy(boolean isCopy) {
		this.isCopy = isCopy;
	}
	
	public ProductEntity clone(){ 
		ProductEntity pe = null;
		try {
			pe = (ProductEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return pe;
	}
	public ProductEntity getCloneProduct() {
		return cloneProduct;
	}
	public void setCloneProduct(ProductEntity cloneProduct) {
		this.cloneProduct = cloneProduct;
	} 
}
