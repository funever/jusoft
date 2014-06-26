/**
 * 创建日期 2013-2-4
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
package com.jetsoft.activity;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jetsoft.R;
import com.jetsoft.entity.AddCompanyFrom;
import com.jetsoft.entity.AreaEntity;
import com.jetsoft.entity.PriceEntity;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 添加往来单位的activity
 * 
 *
 * @version 1.0
 * @author Administrator
 */
public class AddWLDWActivity extends BaseActivity implements OnClickListener{
	
	private EditText add_wldw_no,add_wldw_name,add_wldw_address,add_wldw_tel,add_wldw_mobile,add_wldw_memo,add_wldw_area,add_wldw_price;
	
	private Button submit,ret;
	/**
	 * 提交的单位实体
	 */
	private AddCompanyFrom acf = new AddCompanyFrom();
	/**
	 * 提交参数map
	 */
	public HashMap<String,String> params = new HashMap<String,String>();
	
	public String action;
	/**
	 * 从哪个界面传过来的
	 */
	//public String isClient = "2";
	
	public ImageView area,price;
	
	private AreaEntity ae;
	
	private PriceEntity pe;
	
	private String parId = "00000";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_wldw);
		
		action = getString(R.string.add_wldw_action);
		
		parId = getIntent().getStringExtra("parId");
		
		//isClient = getIntent().getStringExtra("isClient");
		
		add_wldw_no = (EditText) findViewById(R.id.add_wldw_no);
		add_wldw_name = (EditText) findViewById(R.id.add_wldw_name);
		add_wldw_address = (EditText) findViewById(R.id.add_wldw_address);
		add_wldw_tel = (EditText) findViewById(R.id.add_wldw_tel);
		add_wldw_mobile = (EditText) findViewById(R.id.add_wldw_mobile);
		add_wldw_memo = (EditText) findViewById(R.id.add_wldw_memo);
		add_wldw_price =  (EditText) findViewById(R.id.add_wldw_price);
		add_wldw_area =  (EditText) findViewById(R.id.add_wldw_area);
		
		submit = (Button) findViewById(R.id.add_wldw_submit);
		submit.setOnClickListener(this);
		
		ret =  (Button) findViewById(R.id.nv_return);
		ret.setOnClickListener(this);
		
		area = (ImageView) findViewById(R.id.area_action);
		area.setOnClickListener(this);
		
		price = (ImageView) findViewById(R.id.price_action);
		price.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == submit){
//			if(ae==null){
//				Toast.makeText(this, "请选择地区!", Toast.LENGTH_LONG).show();
//				return;
//			}
//			if(pe==null){
//				Toast.makeText(this, "请选择价格!", Toast.LENGTH_LONG).show();
//				return;
//			}
			acf.setAddress(add_wldw_address.getText().toString());
			acf.setPersonCode(add_wldw_no.getText().toString());
			acf.setFullName(add_wldw_name.getText().toString());
			acf.setTelephone(add_wldw_tel.getText().toString());
			acf.setSzMoPhone(add_wldw_mobile.getText().toString());
			acf.setMemo(add_wldw_memo.getText().toString());
			acf.setIsClient("2");
			acf.setaID(ae==null?"":ae.getId());
			acf.setSzPRTypeID(pe==null?"":pe.getId());
			acf.setParID(parId);
			
			Field[] fields = acf.getClass().getDeclaredFields();
			for(int i=0;i<fields.length;i++){
				fields[i].setAccessible(true);
				try {
					params.put(fields[i].getName(), (String)fields[i].get(acf));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			if(acf.getFullName()==null||"".equals(acf.getFullName())){
				Toast.makeText(AddWLDWActivity.this, "添加失败,单位名称不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(acf.getPersonCode()==null||"".equals(acf.getPersonCode())){
				Toast.makeText(AddWLDWActivity.this, "添加失败,单位编号不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Utils.showProgressDialog("正在添加往来单位，请稍候……",this);
			CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action,params, handler, new SubmitExcute());
			Thread t = new Thread(cs);
			t.start();
		}else if(v == ret){
			this.finish();
		}else if(v == area){
			selectEntity(EntityListActivity.AREA_RE_CODE,add_wldw_area.getText().toString());
		}else if(v == price){
			selectEntity(EntityListActivity.PRICE_RE_CODE,add_wldw_price.getText().toString());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == EntityListActivity.AREA_RE_CODE){
			ae = (AreaEntity) data.getSerializableExtra("entity");
			add_wldw_area.setText(ae.getFullName());
		}else if(requestCode == EntityListActivity.PRICE_RE_CODE){
			pe =  (PriceEntity) data.getSerializableExtra("entity");
			add_wldw_price.setText(pe.getFullName());
		}
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
	
	/**
	 * 提交后的返回处理
	 * @version 1.0
	 * @author Administrator
	 */
	class SubmitExcute extends ExcuteThread{
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				//02-04 15:20:51.827: I/System.out(7782): [{"responseCode":0}]
				String result = jo.getString("responseCode");
				if(result.equalsIgnoreCase("0")){
					Toast.makeText(AddWLDWActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
				}else if(result.equalsIgnoreCase("-4")){
					Toast.makeText(AddWLDWActivity.this, "该单位已存在并且停用！", Toast.LENGTH_SHORT).show();
				}else if(result.equalsIgnoreCase("-9")){
					Toast.makeText(AddWLDWActivity.this, "该记录的编号与其它记录相同，与您的配置不符!", Toast.LENGTH_SHORT).show();
				}else if(result.equalsIgnoreCase("-104")){
					Toast.makeText(AddWLDWActivity.this, "系统行不允许修改!", Toast.LENGTH_SHORT).show();
				}else if(result.equalsIgnoreCase("-103")){
					Toast.makeText(AddWLDWActivity.this, "父ID号不存在！", Toast.LENGTH_SHORT).show();
				}else if(result.equalsIgnoreCase("-102")){
					Toast.makeText(AddWLDWActivity.this, "父单位的期初应收或期初应付款、期初预收款或初预付款有数据，不能再分类！", Toast.LENGTH_SHORT).show();
				}else if(result.equalsIgnoreCase("-101")){
					Toast.makeText(AddWLDWActivity.this, "数据库操作失败，请稍后重试！", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}

	@Override
	public void updateProNum() {
		//不处理
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
		
	}
	
	@Override
	public boolean canScan() {
		return false;
	}

	@Override
	public void disposeLastNextData(TableEntity te) {
		
	}
	
	@Override
	public String getNumber() {
		return "";
	}
}
