package com.snjp.activity;


import com.snjp.util.ActivityHelper;

import android.app.Activity;
import android.os.Bundle;

public class AutoIocAcitivity extends Activity {
	
	private ActivityHelper ah;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ah = new ActivityHelper(this);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ah.viewInject();
	}
}
