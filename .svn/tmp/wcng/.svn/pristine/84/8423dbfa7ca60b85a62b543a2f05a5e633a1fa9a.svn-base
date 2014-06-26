/**
 * 创建日期 2013-3-12
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
package com.jetsoft.receiver;

import com.jetsoft.activity.login.ZTActivity;
import com.jetsoft.application.SetValue;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScannerReceiver extends BroadcastReceiver {
	
	private static ScannerReceiver sr;
	
	private ScannerReceiver(){
		
	}
	
	public static ScannerReceiver getInstance(){
		if(sr == null){
			sr = new ScannerReceiver();
		}
		return sr;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ZTActivity.ACTION_BROADCASTRECEIVER.equals(action)) 
		{
			String scanStr = intent.getStringExtra("smartshell_data");
			Toast.makeText(context, scanStr, Toast.LENGTH_SHORT).show();
//				 mReceiver.setText(scanStr);
			scanStr = scanStr.replaceAll("barcode:", "");
			SetValue.getIntance().setScannerValue(scanStr);
		}
	}
}
