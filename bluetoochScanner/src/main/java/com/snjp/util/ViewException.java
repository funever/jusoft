package com.snjp.util;


public class ViewException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String strMsg = null;
	public ViewException(String strExce) {
		strMsg = strExce;
	}
	
	public void printStackTrace() {
		if(strMsg!=null)
			System.err.println(strMsg);
		super.printStackTrace();
	}
}
