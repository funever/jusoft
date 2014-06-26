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

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.LeftMenu;
import com.jetsoft.R;
import com.jetsoft.application.MyApplication;
import com.jetsoft.application.SetValue;
import com.jetsoft.entity.DogEntity;
import com.jetsoft.entity.ZTEntity;
import com.jetsoft.io.ActiveThread;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 选择帐套界面
 * 
 *
 * @version 1.0
 * @author Administrator
 */
public class ZTActivity extends SherlockActivity implements OnClickListener,OnItemClickListener{
	
	ListView ztList;
	
	LinearLayout zt_input;
	
	private TextView setting,scanner;

	private List<ZTEntity> ztEntityList = new LinkedList<ZTEntity>();
	
	private ZTAdapter ztAdapter;
	
	private Handler handler = new Handler();
	
	public static final int SETTING_RE_CODE = 1;
	
	public static final int REG_DOG_CODE = 2;
	/**
	 * 服务器地址
	 */
	private String server;
	
	/**
	 * url中的参数
	 * 
	 */
	private HashMap<String, String> param ;
	/**
	 * 请求的url
	 */
	private String actionUrl;
	
	MyApplication application;
	
	private SetValue sv = SetValue.getIntance();
	
	DogEntity de = new DogEntity();
	
	SharedPreferences preferences;
	
	int loginStyle = 1; 
	
	private Button search;
	
	private EditText zt_name;
	
//	View loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_zt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("选择帐套");

//		loading = findViewById(R.id.loading);
//		loading.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
		application = (MyApplication) getApplication();
		preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		
		ztList = (ListView) findViewById(R.id.ztlist);
		zt_input = (LinearLayout) findViewById(R.id.zt_input);
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);
		zt_name = (EditText) findViewById(R.id.zt_name);
		
		setting = (TextView) findViewById(R.id.setting);
		setting .getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
		setting.getPaint().setAntiAlias(true);
		setting.setOnClickListener(this);
		
		scanner = (TextView) findViewById(R.id.scanner);
		scanner .getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
		scanner.getPaint().setAntiAlias(true);
		scanner.setOnClickListener(this);
		
		ztAdapter = new ZTAdapter();
		ztList.setAdapter(ztAdapter);
		ztList.setOnItemClickListener(this);
		
		param = new HashMap<String, String>();
		param.put("type", "5");
//		param.put("type", "1");
		
		Utils.showProgressDialog("正在获取加密狗信息，请稍候……", this);
		actionUrl = getString(R.string.zt_action);
		//获取服务器
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
		+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
		HttpParams params = new BasicHttpParams();
		SchemeRegistry schReg = new SchemeRegistry();  
		   schReg.register(new Scheme("http", PlainSocketFactory  
		     .getSocketFactory(), 80)); 
		ThreadSafeClientConnManager tm = new ThreadSafeClientConnManager(params,schReg);
		HttpConnectionParams.setConnectionTimeout(params, 10*1000);
		HttpConnectionParams.setSoTimeout(params,60*1000);  
		HttpClient client = new DefaultHttpClient(tm,params);
		
		application.setClient(client);
		CommunicationServer cs = new CommunicationServer(this,client,server+actionUrl, param, handler, new GetDog());
		Thread t = new Thread(cs);
		t.start();
		
		//扫描器初始化
//        CommonUtils.smartshellinit(this);
//		//注册接收条码数据的receiver
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ACTION_BROADCASTRECEIVER);
//        registerReceiver(myReceiver, filter); 
//        
        sv.setZtActivity(this);
        
        /**
		 * 友盟自动更新
		 */
		UmengUpdateAgent.update(this);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("服务器设置").setIcon(android.R.drawable.ic_menu_preferences)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        if(item.getTitle().equals("服务器设置")){
            Intent intent = new Intent();
            intent.setClass(this, SettingActivity.class);
            startActivityForResult(intent, SETTING_RE_CODE);
        }
        return true;
    }

    class ZTAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return ztEntityList.size()+1;
		}

		@Override
		public Object getItem(int position) {
			return ztEntityList.get(position-1);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(position == 0){
				view = LayoutInflater.from(ZTActivity.this).inflate(R.layout.zt_list_row1, null);
			}else{
				view = LayoutInflater.from(ZTActivity.this).inflate(R.layout.zt_list_row, null);
//				((TextView)view.findViewById(R.id.zt_id)).setText(ztEntityList.get(position-1).getId());
				((TextView)view.findViewById(R.id.zt_name)).setText(ztEntityList.get(position-1).getFullName());
//				((TextView)view.findViewById(R.id.zt_data)).setText(ztEntityList.get(position-1).getDbName());
			}
			return view;
		}
	}
	
	class GetDog extends ExcuteThread{
		
		@Override
		public void run() {
			try {
				JSONArray ja = new JSONArray(getJsonString());
				//[{"dogNum":"81100763","status":"0","userCount":"1"}]
				JSONObject jo = ja.getJSONObject(0);
				de.setDogNum(jo.getString("dogNum"));
				de.setStatus(jo.getString("status"));
				de.setUserCount(jo.getString("userCount"));
				loginStyle = jo.getInt("loginStyle");
				int printStyle = jo.getInt("printStyle");
				if(printStyle == 0){
					application.setPrintStyle(false);
				}else{
					application.setPrintStyle(true);
				}
				
				/**
				 * 帐套选择方式
				 */
				if(loginStyle == 1){
					ztList.setVisibility(View.INVISIBLE);
					zt_input.setVisibility(View.VISIBLE);
					zt_name.requestFocus();
					return;
				}else{
					zt_input.setVisibility(View.INVISIBLE);
					ztList.setVisibility(View.VISIBLE);
				}
				
				if(de.getStatus().equals("0")){
					
					param.clear();
					//获取帐套
					param.put("type", "1");
					
					CommunicationServer cs = new CommunicationServer(ZTActivity.this,application.getClient(),server+actionUrl, param, handler, new GetZTThread());
					Thread t = new Thread(cs);
					t.start();
				}else{
					//大于0，弹出注册,带试用button
					//小于0，弹出注册  =-1 | =-2 退出
					if(de.getStatus().equals("-1")){
						Toast.makeText(ZTActivity.this, "没有找到加密狗！", Toast.LENGTH_SHORT).show();
//						exit();
						return;
					}else if(de.getStatus().equals("-2")){
						Toast.makeText(ZTActivity.this, "用户数大于加密狗允许的用户数！", Toast.LENGTH_SHORT).show();
//						exit();
						return;
					}
					Intent intent = new Intent();
					intent.setClass(ZTActivity.this, RegisterDogActivity.class);
					intent.putExtra("dogEntity", de);
					startActivityForResult(intent, REG_DOG_CODE);
				}
				/**
				 * 添加心跳计时器
				 */
				Timer timer = new Timer();
				timer.schedule(new ActiveThread(application.getClient(),ZTActivity.this,true), 20*1000, 30*1000);
				application.setTimer(timer);
			} catch (Exception e) {
				Toast.makeText(ZTActivity.this, "获取加密狗信息错误!", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	class GetZTThread extends ExcuteThread{
		
		@Override
		public void run() {
			try {
				JSONArray ja = new JSONArray(getJsonString());
				for(int i=0;i<ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					ZTEntity ztEntity = new ZTEntity();
					ztEntity.setId(jo.getString("id"));
					ztEntity.setFullName(jo.getString("fullName"));
					ztEntity.setDbName(jo.getString("dbName"));
					ztEntityList.add(ztEntity);
				}
				ztAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				Toast.makeText(ZTActivity.this, "没有获取到数据库帐套!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v == setting){
			Intent intent = new Intent();
			intent.setClass(this, SettingActivity.class);
			startActivityForResult(intent, SETTING_RE_CODE);
		}else if(v == scanner){
			//选择扫描器
//			Intent serverIntent = new Intent(this, DeviceListActivity.class);
//            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
		}else if(v == search){
			String databaseName = zt_name.getText().toString();
			if(databaseName==null || "".equals(databaseName)){
				Toast.makeText(this, "请输入帐套名称", Toast.LENGTH_SHORT).show();
				return;
			}
			ZTEntity ztEntity = new ZTEntity();
			ztEntity.setDbName(databaseName);
			application.setZtEntity(ztEntity);

            boolean fromMenu = getIntent().getBooleanExtra(LeftMenu.FROM_MENU,false);
            if(fromMenu){
                setResult(LeftMenu.MENULEFT_RESULT_ZT);
                this.finish();
            }else{
                Intent intent = new Intent();
                intent.setClass(this, UserLoginActivity.class);
                startActivity(intent);
            }
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == SETTING_RE_CODE){
			/**
			 * 重置帐套列表
			 */
			ztEntityList.clear();
			
			param.clear();
			server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
					+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
			param.put("type", "5");
			//重新获取服务器地址
			CommunicationServer cs = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new GetDog());
			Thread t = new Thread(cs);
			t.start();
			return;
		}else if(requestCode == REG_DOG_CODE){
			int result = data.getIntExtra("result", 0);
			if(result == RegisterDogActivity.REG_SUC||result == RegisterDogActivity.REG_TEST){
				param.clear();
				param.put("type", "1");
				
				if(result == RegisterDogActivity.REG_TEST){
					Toast.makeText(this, "试用中，正在获取帐套!", Toast.LENGTH_SHORT).show();
				}
				
				CommunicationServer cs = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new GetZTThread());
				Thread t = new Thread(cs);
				t.start();
				return;
			}else if(result == RegisterDogActivity.REG_CANCEL){
				return;
			}else if(result == RegisterDogActivity.REG_FAIL){
				//注册失败，强制退出
				exit();
			}
			return;
		}
		//蓝牙扫描器接收部分
		switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
//            if (resultCode == Activity.RESULT_OK) {           	
//            	CommonUtils.connectbtservice(this,data);
//            }
            break;
        case REQUEST_ENABLE_BT:
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "BT  enabled");
            } else 
            {
                Log.d(TAG, "BT not enabled");
            }
        } 
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg2 == 0){
			return;
		}
		ZTEntity selectZt = ztEntityList.get(arg2-1);
		application.setZtEntity(selectZt);

        boolean fromMenu = getIntent().getBooleanExtra(LeftMenu.FROM_MENU,false);
        if(fromMenu){
            setResult(LeftMenu.MENULEFT_RESULT_ZT);
            this.finish();
        }else{
            Intent intent = new Intent();
            intent.setClass(this, UserLoginActivity.class);
            startActivity(intent);
        }
		//this.finish();
	}
	
	private static final String TAG = "smartshell";
	public static final String ACTION_BROADCASTRECEIVER ="action.broadcast.smartshell.data";	
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 11;
    private static final int REQUEST_ENABLE_BT = 31;
    
  //收到数据的处理函数
    public final BroadcastReceiver myReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			String action = intent.getAction();
						
			if (ACTION_BROADCASTRECEIVER.equals(action)) 
			{
				String scanStr = intent.getStringExtra("smartshell_data");
//				Toast.makeText(context, scanStr, Toast.LENGTH_SHORT).show();
//				 mReceiver.setText(scanStr);
				scanStr = scanStr.replaceAll("barcode:", "");
				sv.setScannerValue(scanStr);
			}
		}
	}; 
	
	public void onStart() {
        super.onStart();
    }
     
	protected void onPause() {
//		CommonUtils.pausebtservice(this);
    	super.onPause();
    	MobclickAgent.onPause(this);
	
	}   
    public synchronized void onResume() {
//        CommonUtils.resumebtservice(this);
        super.onResume();
        MobclickAgent.onResume(this);
    }
    
    @Override
    public void onBackPressed() {
//    	exit();
//    	String url = server+getString(R.string.exit_action);
//    	HashMap<String, String> paramMap = new HashMap<String, String>();
//    	Utils.showProgressDialog("正在退出处理！", this);
//    	CommunicationServer cs = new CommunicationServer(this, application.getClient(), url, paramMap, handler, new ExitThread());
//    	Thread t = new Thread(cs);
//    	t.start();
        this.finish();
    }
    
    /**
     * 
     * @author funever
     *
     */
    public class ExitThread extends ExcuteThread{
    	@Override
    	public void run() {
    		try{
    			Toast.makeText(ZTActivity.this, "已成功退出！", Toast.LENGTH_SHORT).show();
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			Utils.dismissProgressDialog();
    			android.os.Process.killProcess(android.os.Process
        				.myPid());
        		System.exit(10);
    		}
    	}
    }
    /**
     * 退出
     */
    public void exit(){
    	try{
    		new Thread(new Runnable() {
				@Override
				public void run() {
					exitRequest();
				}
			}).start();
//    	Intent startMain = new Intent(Intent.ACTION_MAIN);
//		startMain.addCategory(Intent.CATEGORY_HOME);
//		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(startMain);
    		android.os.Process.killProcess(android.os.Process
    				.myPid());
    		System.exit(10);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    //退出时发送
    public void exitRequest(){
    	//http://192.168.0.21:6003/AndroidWEB/exit.do
    	String url = server+getString(R.string.exit_action);
    	HttpPost get = new HttpPost(url);
		try {
			Utils.excute(application.getClient(), get,false,Utils.ZIP);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
