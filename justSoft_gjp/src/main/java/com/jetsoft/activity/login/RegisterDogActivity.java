/**
 * 创建日期 2013-4-18
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.R;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.DogEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterDogActivity extends SherlockActivity implements OnClickListener{
	
	private EditText comp_name,reg_no,pc_dog_no;
	
	private TextView dog_no,reg_date;
	
	private TextView online_count;
	
	private DogEntity de;
	
	private Button bt_reg;
	
	public String actionUrl,server;
	
	private RelativeLayout reg_action;

    int status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_dog);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("加密狗注册");
		SharedPreferences preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		
		actionUrl = getString(R.string.dog_action);
		//获取服务器
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG,getString(R.string.server))+":"
		+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));

		
		comp_name = (EditText) findViewById(R.id.comp_name);
		dog_no = (TextView) findViewById(R.id.dog_no);
		reg_no = (EditText) findViewById(R.id.reg_no);
		pc_dog_no = (EditText) findViewById(R.id.pc_dog_no);
		online_count = (TextView) findViewById(R.id.online_count);
		
		de = (DogEntity) getIntent().getSerializableExtra("dogEntity");
		dog_no.setText(de.getDogNum());
		online_count.setText(de.getUserCount());
		
		bt_reg = (Button) findViewById(R.id.submit);
		bt_reg.setOnClickListener(this);
		
		reg_action = (RelativeLayout) findViewById(R.id.reg_action);
		reg_date = (TextView) findViewById(R.id.reg_date);
		reg_action.setOnClickListener(this);

        status =  Integer.parseInt(de.getStatus());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(status>0){
            MenuItem saveItem = menu.add(0,10001,0,"添加");
            saveItem.setIcon(android.R.drawable.ic_menu_add);
            saveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT|MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 10001){
            //试用
            Intent intent = new Intent();
            intent.putExtra("result", REG_TEST);
            setResult(RESULT_OK, intent);
            this.finish();
        }else if(item.getItemId() == android.R.id.home){
            //取消
            Intent intent = new Intent();
            intent.putExtra("result", REG_CANCEL);
            setResult(RESULT_OK, intent);
            this.finish();
            return  false;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(View v) {
		if(v == bt_reg){
			//companyName=dddd&dogNo=81100763&regNum=11111
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("companyName", comp_name.getText().toString());
			param.put("dogNo", pc_dog_no.getText().toString());
			param.put("regNum", reg_no.getText().toString());
			param.put("reg_date", reg_date.getText().toString());
			
			MyApplication application = (MyApplication) getApplication();
			
			CommunicationServer cs = new CommunicationServer(this,application.getClient(),server+actionUrl, param, new Handler(), new GetResult());
			Thread t = new Thread(cs);
			t.start();
		}else if(v == reg_action){
			showDate(reg_date);
		}
	}
	
	class GetResult extends ExcuteThread{
		
		@Override
		public void run() {
			try {
				JSONArray ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				//[{"status":"-1"}]
				String result = jo.getString("status");
				Intent intent = new Intent();
				if(result.equals("0")){
					//成功
					intent.putExtra("result", REG_SUC);
					setResult(RESULT_OK, intent);
					RegisterDogActivity.this.finish();
				}else{
					if(result.equals("-1")||result.equals("-2")){
						intent.putExtra("result", REG_FAIL);
						setResult(RESULT_OK, intent);
						RegisterDogActivity.this.finish();
					}else{
						Toast.makeText(RegisterDogActivity.this, "注册加密狗失败，请重试!", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				Toast.makeText(RegisterDogActivity.this, "注册加密狗失败，请重试!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * 显示选择录单日期的dialog
	 */
	public void showDate(final TextView tv){
		final Calendar calendar = Calendar.getInstance();
		DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				calendar.set(Calendar.YEAR, year); 
				calendar.set(Calendar.MONTH, monthOfYear); 
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
				tv.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}
		},  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}
	
	public static final int REG_SUC = 1;
	public static final int REG_FAIL = 2;
	public static final int REG_CANCEL = 3;
	public static final int REG_TEST = 4;
	
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
