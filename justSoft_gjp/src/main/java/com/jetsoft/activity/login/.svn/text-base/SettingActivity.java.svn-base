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
package com.jetsoft.activity.login;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.LeftMenu;
import com.jetsoft.R;
import com.jetsoft.utils.StaticValues;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 设置服务器的地址
 * 
 *
 * @version 1.0
 * @author Administrator
 */
public class SettingActivity extends SherlockActivity{
	
	private EditText ip_address,port_address;
	
	private SharedPreferences preferences;
	
	/**
	 * IP地址的正则表示式
	 */
	public String address_matcher = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
	/**
	 * 域名解析的正则表达式
	 */
	public String domain_matcher = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z0-9]+$";

    boolean from_menu = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);

        from_menu = getIntent().getBooleanExtra(LeftMenu.FROM_MENU,false);

        getSupportActionBar().setTitle("服务器设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		ip_address = (EditText) findViewById(R.id.ip);
		port_address = (EditText) findViewById(R.id.port);
//
//		nv_return = (Button) findViewById(R.id.nv_return);
//		nv_save = (Button) findViewById(R.id.nv_save);
//		nv_save.setOnClickListener(this);
//		nv_return.setOnClickListener(this);
		
		preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		ip_address.setText(preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server)));
		port_address.setText(preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port)));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Save")
                .setIcon(android.R.drawable.ic_menu_save)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }else if(item.getTitle().equals("Save")){
            Editor editor = preferences.edit();
            String address = ip_address.getText().toString();
            boolean same = false;
            if(address.matches(address_matcher)||address.matches(domain_matcher)){
                if(address.equals(preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server)))
                        && preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port)).equals(port_address.getText().toString())){
                    same = true;
                }
                editor.putString(StaticValues.SERVER_FLAG, address);
                editor.putString(StaticValues.PORT_FLAG, port_address.getText().toString());
                editor.commit();
            }else{
                Toast.makeText(this, "输入的地址格式错误，请检查后重新输入！", Toast.LENGTH_SHORT).show();
                ip_address.setText(preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server)));
                return true;
            }
            Intent intent = new Intent();
            if(from_menu){
                intent.putExtra("same",same);
                setResult(LeftMenu.MENULEFT_RESULT_SERVER,intent);
            }else{
                setResult(RESULT_OK, intent);
            }
            this.finish();
        }
        return true;
    }
}
