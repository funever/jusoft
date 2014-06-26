/**
 * 创建日期 2013-2-20
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
package com.jetsoft.activity.kucun;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import com.jetsoft.R;
import com.jetsoft.activity.BaseActivity;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;


public class KuCunPanDianActivity extends BaseActivity implements OnClickListener,OnEditorActionListener{
		
	private EditText ck_edit;
	
	private RelativeLayout ck_action,ldrq_layout;
	
	private ImageView ck_add;
	
	private TextView pandian_date_text;
	
	private Button submit;
	
	private String ck_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.kucun_pandian);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("库存盘点");
		
        ck_entity = new CangKuEntity();
		ck_edit = (EditText) findViewById(R.id.ck_edit);
		//仓库
        ck_action = (RelativeLayout) findViewById(R.id.ck_action);
        ck_action.setOnClickListener(this);
        ck_add = (ImageView) findViewById(R.id.ck_add);
        ck_add.setOnClickListener(this);
        
        ldrq_layout = (RelativeLayout) findViewById(R.id.pandian_date);
        ldrq_layout.setOnClickListener(this);
        pandian_date_text = (TextView) findViewById(R.id.pandian_date_text);
        pandian_date_text.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == EntityListActivity.CK_RE_CODE){
			ck_entity = (CangKuEntity) data.getSerializableExtra("entity");
			ck_edit.setText(ck_entity.getFullName());
			ck_id = ck_entity.getId();
		}
	}

	@Override
	public void updateProNum() {
		
	}

	@Override
	public void onClick(View v) {
		if(v instanceof ImageView){
			RelativeLayout re = (RelativeLayout) v.getParent();
			re.performClick();
			return;
		}else if(v == ck_action){
			//跳转仓库选择界面
			selectEntity(EntityListActivity.CK_RE_CODE,ck_edit.getText().toString());
		}else if(v == ldrq_layout){
			//点击弹出选择录单日期
			showDate(pandian_date_text);
		}else if(v == submit){
			if(ck_id == null || "".equals(ck_id)){
				Toast.makeText(this, "请选择仓库！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			//http://192.168.0.21:6002/AndroidWEB/saveCheck.do?mID=0&fullName=ddd&sID=00001&date=2013-02-20&eID=00002&modiDate=&modiEID=&memo=&tag=0&cKind=A&newMID=0
			SharedPreferences preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
			server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
				+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
			String actionUrl = getString(R.string.pandian_mid_action);
			
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("mID", "0");
			param.put("fullName", application.getUserEntity().getEfullName()+"安卓盘点数据"+pandian_date_text.getText().toString());
			param.put("sID", ck_id);
			param.put("date", pandian_date_text.getText().toString());
			param.put("eID", application.getUserEntity().getId());
			param.put("modiDate", "");
			param.put("modiEID", "");
			param.put("memo", "");
			param.put("tag", "");
			param.put("cKind", "A");
			param.put("newMID", "0");
			
			Utils.showProgressDialog("请求数据中，请稍候！", this);
			CommunicationServer ccs = new CommunicationServer(this, application.getClient(), server+actionUrl, param, handler, new SaveCheckThread());
			Thread t = new Thread(ccs);
			t.start();
		}
	}
	
	/**
	 * 显示选择日期的dialog
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
	
	
	
	/**
	 * 跳转到选择多列项的页面
	 */
	public void selectEntity(int requestCode,String searchString){
		Intent intent = new Intent();
		intent.setClass(this, EntityListActivity.class);
		intent.putExtra("type", requestCode);
		intent.putExtra("search", searchString);
		startActivityForResult(intent, requestCode);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	/**
	 * 获取mid后的返回处理
	 * 
	 *
	 * @version 1.0
	 * @author Administrator
	 */
	class SaveCheckThread extends ExcuteThread{
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				String mid = jo.getString("mID");
				
				//获取mid
				Intent intent = new Intent();
				intent.setClass(KuCunPanDianActivity.this, KuCunProductActivity.class);
				intent.putExtra("ck_id", ck_id);
				intent.putExtra("date", pandian_date_text.getText());
				intent.putExtra("mid", mid);
				startActivity(intent);
				KuCunPanDianActivity.this.finish();
			}catch (Exception e) {
				Toast.makeText(KuCunPanDianActivity.this, "获取仓库数据错误!", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}


	@Override
	public boolean validate() {
		return false;
	}
	
	@Override
	public void setScanValue(String value) {
	}


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean canScan() {
		return false;
	}


	@Override
	public void disposeLastNextData(TableEntity te) {}
	
	@Override
	public String getNumber() {
		return "";
	}
}
