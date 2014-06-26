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
package com.jetsoft.utils;

import java.text.DecimalFormat;

import android.content.Context;

public class StaticValues {
	
	public static final String CONFIG = "config.xml";
	
	public static final String SERVER_FLAG = "server";
	
	public static final String PORT_FLAG = "port";
	
	/**
	 * 判断是否是数字，包括"012"这样的验证
	 * @param value
	 * @return
	 */
	public static boolean isNum(String value){
		return value.matches(ONLYNUMBIC);
	}
	
	private static String ONLYNUMBIC = "[0-9.]+$";
	
//	public static String ORDER_IN_BILTYPE = "1";
//	public static String ORDER_BILTYPE = "2";
//	public static String ORDER_RE_BILTYPE = "3";
//	public static String SALE_IN_BILTYPE = "11";
//	public static String SALE_BILTYPE = "12";
//	public static String SALE_RE_BILTYPE = "13";
//	public static String BIANJIA_BILTYPE = "22";
//	public static String SK_BILTYPE = "14";
//	public static String FK_BILTYPE = "4";
	
	public static String ORDER_IN_BILTYPE = "7";
	public static String ORDER_BILTYPE = "34";
	public static String ORDER_RE_BILTYPE = "6";
	public static String SALE_IN_BILTYPE = "8";
	public static String SALE_BILTYPE = "11";
	public static String SALE_RE_BILTYPE = "45";
	public static String BIANJIA_BILTYPE = "21";
	public static String TONGJIA_BILTYPE = "17";
	public static String SK_BILTYPE = "4";
	public static String FK_BILTYPE = "66";
	
	/**
	 * 保存的默认仓库标签名
	 */
	public static final String DEFAULT_CK = "ck";
	/**
	 * 保存的默认单位标签名
	 */
	public static final String DEFAULT_DW = "dw";
	/**
	 * 打印机地址
	 */
	public static final String PRINTER_IP = "printer_ip";
	/**
	 * 打印机port
	 */
	public static final String PRINTER_PORT = "printer_port";
	/**
	 * 打印机port
	 */
	public static final String PRINTER_ACTIVE = "printer_flag";
	
	public static String format(float src,int num){
		String s_format = "############.";
		if(num==0){
			return (int)src+"";
		}else{
			num = 2;
		}
		for(int i=0;i<num;i++){
			s_format += "#";
		}
		DecimalFormat df = new DecimalFormat(s_format);
		return df.format(src);
	}
	
	public static String format(String src,int num){
		float f = 0;
		try{
			//如果不是float类型直接返回
			if(src.indexOf(".")==-1 && src.indexOf("E")==-1 && src.indexOf("e")==-1){
				return src;
			}
			f = Float.parseFloat(src);
			return format(f,2)+"";
		}catch(Exception e){
			return src;
		}
	}
	
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
}
