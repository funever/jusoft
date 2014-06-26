/**
 * 创建日期 2013-5-7
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
package com.funever.bluetoochscanner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class BtListActivity extends Activity implements ScannerResult{
	
	Button show;
	
	BluetoochUtils utils;
	
	TextView value;
	
	Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);
		show = (Button) findViewById(R.id.show);
		utils = BluetoochUtils.getInstance(this, handler,this);
		show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				utils.showList();
			}
		});
		
		value = (TextView) findViewById(R.id.value);
	}
	@Override
	public void disposeScanValue(final String re) {
//		handler.post(new Runnable() {
//			@Override
//			public void run() {
				value.setText(re);
//			}
//		});
	}
	@Override
	public boolean isHibernation() {
		return false;
	}
	@Override
	public void setHibernation(boolean hibernation) {
		
	}
}
