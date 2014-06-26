/**
 * 创建日期 2012-3-1
 * 创建用户 funever
 * 变更情况:
 * 文档位置 $Archive$
 * 最后变更 $Author$
 * 变更日期 $Date$
 * 当前版本 $Revision$
 * 
 * Copyright (c) 2004 Sino-Japanese Engineering Corp, Inc. All Rights Reserved.
 * 
 */
package com.jetsoft.activity;

import java.util.Date;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * 当HywayBrowser发生异常时跳转的页面
 * 
 *
 * @version 1.0
 * @author funever
 */
public class ExceptionActivity extends SherlockActivity{
	
	private TextView exception_show;
	
	private String version;
	
	private String errorMsg;
	
	private StringBuffer msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exception_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("异常");

		exception_show = (TextView) findViewById(R.id.exception_msg);
		
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			version = "unknow";
		}
		/**
		 * 获取机器的一些信息
		 */
		Bundle bundle = getIntent().getExtras();
		msg = new StringBuffer();
		msg.append("Android Version:"+Build.VERSION.RELEASE+"\n");
		msg.append("Model Type:"+Build.MODEL+"\n");
		msg.append("Version:"+version+"\n");
		msg.append("Date:"+new Date().toString()+"\n");
		if(bundle!=null){
			 String error = bundle.getString("error");
			 if(error!=null || !"".equals(error)){
				 errorMsg = "Exception Info:"+error;
			 }else{
				 errorMsg = "Exception Info:unknown";
			 }
		}
		msg.append("Info:"+errorMsg);
		exception_show.setText(msg.toString());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem reportItem = menu.add(0,10001,0,"报告");
        reportItem.setIcon(android.R.drawable.ic_menu_edit);
        reportItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == android.R.id.home){
            //退出
           Intent startMain = new Intent(Intent.ACTION_MAIN);
           startMain.addCategory(Intent.CATEGORY_HOME);
           startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(startMain);
           System.exit(0);
           return false;
       }else if(item.getItemId() == 10001){
           String[] reciver = getResources().getStringArray(R.array.exception_recever);
           String[] mySbuject = new String[] {"Exception"};
//			String myCc = "cc";
           msg.append(errorMsg);
           String mybody = msg.toString();
           Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
           myIntent.setType("plain/text");
           myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
//			myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
           myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);
           myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
           startActivity(myIntent);
       }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
