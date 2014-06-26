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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.LeftMenu;
import com.jetsoft.MenuNewActivity;
import com.jetsoft.R;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.UserEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 登录界面
 * 
 *
 * @version 1.0
 * @author Administrator
 */
public class UserLoginActivity extends SherlockActivity implements OnClickListener{
	
	private Button user;
	private EditText username;
	
	private EditText password;
	
	private TextView date;
	
	private Button login_button;
	
	private List<UserEntity> userList = new LinkedList<UserEntity>();
	
	private Handler handler = new Handler();
	
	private MyApplication application;
	
	private RelativeLayout login_date;
	
	private String server;
	
	private String actionUrl;
	
	private UserAdapter userAdapter;
	
	private UserEntity selectUser = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
        super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);

        getSupportActionBar().setTitle("用户登录");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		user = (Button) findViewById(R.id.user);
		user.setOnClickListener(this);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		date = (TextView) findViewById(R.id.login_date_text);
		date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		login_button = (Button) findViewById(R.id.login_action);
		login_button.setOnClickListener(this);
		login_date = (RelativeLayout) findViewById(R.id.login_date);
		login_date.setOnClickListener(this);
		application = (MyApplication) getApplication();
		
		userAdapter = new UserAdapter();
		/**
		 * 获取登录url
		 */
		SharedPreferences preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
			+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
		actionUrl = getString(R.string.zt_action);
		
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("type", "2");
		if(application.getZtEntity()!=null){
			param.put("dataBase", application.getZtEntity().getDbName());
		}else{
			return;
		}
		Utils.showProgressDialog("正在加载操作员，请稍候……", this);
		CommunicationServer cs = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new GetUser());
		Thread t = new Thread(cs);
		t.start();
	}
	/**
	 * 获取用户列表
	 * @version 1.0
	 * @author Administrator
	 */
	class GetUser extends ExcuteThread{
		@Override
		public void run() {
			try {
				JSONArray ja = new JSONArray(getJsonString());
				for(int i=0;i<ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					UserEntity userEntity = new UserEntity();
					userEntity.setEfullName(jo.getString("efullName"));
					userEntity.seteUserCode(jo.getString("eUserCode"));
					userEntity.setId(jo.getString("id"));
					userEntity.setIsManager(jo.getString("isManager"));
					userList.add(userEntity);
				}
				userAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				Toast.makeText(UserLoginActivity.this, "获取用户列表错误", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	/**
	 * 记录当前会计期间
	 * 
	 * [
	 * {"bilPrice":0,"sName":"period","sValue":1},
	 * {"bilPrice":0,"sName":"NumDigit","sValue":4},
	 * {"bilPrice":0,"sName":"PriceDigit","sValue":8},
	 * {"bilPrice":0,"sName":"AmountDigit","sValue":2},
	 * {"bilPrice":0,"sName":"RightCost","sValue":1},
	 * {"bilPrice":0,"sName":"BilPrice","sValue":1},
	 * {"bilPrice":0,"sName":"SetGoodsPrice","sValue":1}]
	 * @version 1.0
	 * @author Administrator
	 */
	class GetPeriod extends ExcuteThread{
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				for(int i=0;i<ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					String sName = jo.getString("sName");
					String sValue = jo.getString("sValue");
					if(sName.equals("period")){
						application.setPeriod(sValue);
					}else if(sName.equals("NumDigit")){
						application.setNumDigit(Integer.parseInt(sValue));
					}else if(sName.equals("AmountDigit")){
						application.setAmountDigit(Integer.parseInt(sValue));
					}else if(sName.equals("PriceDigit")){
						application.setPriceDigit(Integer.parseInt(sValue));
					}else if(sName.equals("RightCost")){
						application.setRightCost(Integer.parseInt(sValue));
					}else if(sName.equals("BilPrice")){
						application.setBilPrice(Integer.parseInt(sValue));
					}else if(sName.equals("SetGoodsPrice")){
						application.setSetGoodsPrice(Integer.parseInt(sValue));
					}
				}
                boolean fromMenu = getIntent().getBooleanExtra(LeftMenu.FROM_MENU,false);
                if(fromMenu){
                    setResult(LeftMenu.MENULEFT_RESULT_USER);
                }else{
                    //流程完成。跳转
                    Intent intent = new Intent();
                    intent.setClass(UserLoginActivity.this, MenuNewActivity.class);
                    startActivity(intent);
                }
                UserLoginActivity.this.finish();

			} catch (JSONException e) {
				Toast.makeText(UserLoginActivity.this, "记录当前会计期间错误,请检查服务器状态", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	/**
	 * 
	 * 
	 * 登录操作
	 * @version 1.0
	 * @author Administrator
	 */
	class LoginAction extends ExcuteThread{
		@Override
		public void run() {
			try {
				JSONArray ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				if (selectUser == null) {
					selectUser = new UserEntity();
				}
				selectUser.setId(jo.getString("eID"));
				selectUser.setEfullName(jo.getString("fullName"));
				selectUser.seteUserCode(jo.getString("userCode"));
				// 保存登录用户
				application.setUserEntity(selectUser);
				Toast.makeText(UserLoginActivity.this, "验证用户成功",
						Toast.LENGTH_SHORT).show();
				Utils.setProgressDialogMsg("正在请求当前会计期间……");

				// 去获取 记录当前会计期间
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("type", "4");
				param.put("operator", selectUser.getId());

				CommunicationServer loginCS = new CommunicationServer(
						UserLoginActivity.this, application.getClient(), server
								+ actionUrl, param, handler, new GetPeriod());
				Thread t = new Thread(loginCS);
				t.start();
			} catch (Exception e) {
				Toast.makeText(UserLoginActivity.this, "登录失败,请检查用户名密码!",
						Toast.LENGTH_SHORT).show();
				Utils.dismissProgressDialog();
			}
		}
	}
	/**
	 * 选择用户spinner的适配器
	 * 
	 *
	 * @version 1.0
	 * @author Administrator
	 */
	class UserAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(userList.size() == 0){
				return 1;
			}
			return userList.size();
		}

		@Override
		public UserEntity getItem(int position) {
			if(userList.size() == 0){
				return null;
			}
			return userList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(userList.size() == 0){
				view = LayoutInflater.from(UserLoginActivity.this).inflate(android.R.layout.simple_spinner_item, null);
				((TextView)view.findViewById(android.R.id.text1)).setText("没有用户");
			}else{
				view = LayoutInflater.from(UserLoginActivity.this).inflate(android.R.layout.simple_spinner_dropdown_item, null);
				((TextView)view.findViewById(android.R.id.text1)).setText(userList.get(position).getEfullName());
			}
			view.setMinimumHeight(StaticValues.dip2px(UserLoginActivity.this, 40));
			return view;
		}
		
	}
	@Override
	public void onClick(View v) {
		if(v == login_date){
			showRiQi(date);
		}else if(v == login_button){
			Utils.showProgressDialog("登录中……",this);
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("type", "3");
			if(selectUser!=null){
				param.put("fullName", selectUser.getEfullName());
				param.put("operator", selectUser.getId());
				param.put("password", password.getText().toString());
				selectUser.setPassword(password.getText().toString());
			}else{
				if(username.getText().toString().equals("")){
					Toast.makeText(this, "用户名不能为空，请输入用户名！", Toast.LENGTH_SHORT).show();
					Utils.dismissProgressDialog();
					return;
				}
				param.put("fullName", username.getText().toString());
				param.put("operator","");
			}
			/**
			 * 请求登录
			 */
			CommunicationServer loginCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new LoginAction());
			Thread t = new Thread(loginCS);
			t.start();
		}else if(v == user){
			AlertDialog.Builder dialog = new Builder(this);
			dialog.setTitle("选择用户");
			dialog.setAdapter(userAdapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(userList.size()>0){
						selectUser = userList.get(which);
						username.setText(selectUser.getEfullName());
					}
				}
			});
			dialog.show();
		}
	}
	
	/**
	 * 显示选择日期的dialog
	 */
	public void showRiQi(final TextView tv){
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
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}