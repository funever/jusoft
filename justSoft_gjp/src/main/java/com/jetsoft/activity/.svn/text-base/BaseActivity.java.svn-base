/**
 * 创建日期 2012-12-27
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import net.tsz.afinal.FinalActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.funever.bluetoochscanner.BluetoochUtils;
import com.funever.bluetoochscanner.ScannerResult;
import com.jetsoft.R;
import com.jetsoft.activity.baobiao.OrderCheckActivity;
import com.jetsoft.activity.baobiao.SubmitOrderCheckActivity;
import com.jetsoft.activity.kucun.KuCunActivity;
import com.jetsoft.activity.order.OrderActivity;
import com.jetsoft.activity.print.PrintActivity;
import com.jetsoft.activity.returnorder.ReturnOrderActivity;
import com.jetsoft.application.MyApplication;
import com.jetsoft.application.SetValue;
import com.jetsoft.entity.BuMenEntity;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.JingShouRenEntity;
import com.jetsoft.entity.ProductEntity;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.entity.UserEntity;
import com.jetsoft.entity.ZhangHuEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.CountList;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.zxing.CaptureActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends FinalActivity implements OnFocusChangeListener,ScannerResult{
	
	public MyApplication application;

	public String server;
	
	public HashMap<String,String> params = new HashMap<String,String>();
	
	public SharedPreferences preferences;
	
	public Handler handler = new Handler();
	
	public DanWeiEntity dw_entity = new DanWeiEntity();
	
	public JingShouRenEntity jsr_entity = new JingShouRenEntity();
	
	public BuMenEntity bm_entity = new BuMenEntity();
	
	public CangKuEntity ck_entity = new CangKuEntity();
	
	public CangKuEntity fhck_entity = new CangKuEntity();
	
	public ZhangHuEntity fkzh_entity = new ZhangHuEntity();
	
	public ZhangHuEntity skzh_entity = new ZhangHuEntity();
	
	//返回按钮
	public Button returnButton;
	
	public Button save_button;
	 /**
	  * 商品选择后总的金额
	  */
	public float amount;
	
	public SetValue sv = SetValue.getIntance();
	
	public String nBilType;
	
	public boolean bhShow = false;
	
	public MediaPlayer mediaPlayer;
	
	public CountList selectProducts = new CountList();
	
	public Button last,next;
	
	public int bilid = 0;
	
	public String bilNumber;
	
	public EditText djbh_edit;
	
	/**
	 * 金额
	 */
	public String je;
	/**
	 * 单据是否能编辑
	 */
	public boolean isEdit = true;
	/**
	 * 是否是审核单据
	 */
	public boolean orderCheck = false;
	/**
	 * 审核单据信息
	 */
	public HashMap<String,String> orderInfo;
	
	public TextView je_more;
	/**
	 * 多账户数据
	 */
	public HashMap<ZhangHuEntity, Float> mulitZh = new HashMap<ZhangHuEntity, Float>(); 
	
	BluetoochUtils bu;
	
	boolean print_flag;
	
	/**
	 * 扫描是否休眠，不接受扫描值
	 */
	public boolean hiberation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.StyledIndicators);
		application = (MyApplication) getApplication();
		preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
		+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
		
		UserEntity user = ((MyApplication)getApplication()).getUserEntity();
		if(application.getJsr()!=null){
			jsr_entity = application.getJsr();
		}else{
			if(user!=null && !user.getId().equals("00000")){
				jsr_entity.setId(user.getId());
				jsr_entity.setFullName(user.getEfullName());
			}
		}
		
		isEdit = getIntent().getBooleanExtra("isEdit",true);
		orderCheck = getIntent().getBooleanExtra(OrderCheckActivity.ORDER_CHECK,false);
		orderInfo = (HashMap<String, String>) getIntent().getSerializableExtra(OrderCheckActivity.ORDER_INFO);
		
		/**
		 * 获取默认设定的单位
		 */
		String dw_string = preferences.getString(StaticValues.DEFAULT_DW, null);
		if(dw_string!=null){
			try{
				JSONObject dw_json = new JSONObject(dw_string);
				Utils.setObjectValue(dw_entity, dw_json);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		/**
		 * 获取默认设置的仓库
		 */
		String ck_string = preferences.getString(StaticValues.DEFAULT_CK, null);
		if(ck_string!=null){
			try{
				JSONObject ckJsonObject = new JSONObject(ck_string);
				Utils.setObjectValue(ck_entity, ckJsonObject);
				Utils.setObjectValue(fhck_entity, ckJsonObject);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
//		if(application.getShck()!=null){
//			ck_entity = application.getShck();
//		}
//		
//		if(application.getFhck()!=null){
//			fhck_entity = application.getFhck();
//		}
		
		bu = BluetoochUtils.getInstance(this, handler, this);
		bu.register(this, handler, this);
		
		print_flag = application.isPrintStyle();
		
	}
	
	public void submit() throws Exception{
		/**
		 * 保存经手人和仓库
		 */
//		if(jsr_entity!=null&&!"".equals(jsr_entity.getFullName())){
//			application.setJsr(jsr_entity);
//		}
//		if(ck_entity!=null&&!"".equals(ck_entity.getFullName())){
//			application.setShck(ck_entity);
//		}
//		if(fhck_entity!=null && !"".equals(fhck_entity.getFullName())){
//			application.setFhck(fhck_entity);
//		}
	}
	
	public void validateNumber(){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cMode", "Z");
		params.put("operator", application.getUserEntity().getId());
		params.put("nBilID", bilid+"");
		params.put("nBileType", nBilType);
		params.put("nBilNumber",getNumber());
		String action = getString(R.string.checkbil_action);
		
		Utils.showProgressDialog("正在检查单据编号", this);
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action, params, handler, new CheckBil());
		Thread t = new Thread(cs);
		t.start();
	}
	
	/**
	 * 检查单据编号
	 * @author funever_win8
	 *
	 */
	public class CheckBil extends ExcuteThread{
		
		@Override
		public void run() {
			Utils.dismissProgressDialog();
			try {
				JSONArray ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				int result = jo.getInt("result");
				if(result==0){
					try{
						submit();
					}catch(Exception e){
						e.printStackTrace();
						Toast.makeText(BaseActivity.this, "提交数据异常！", Toast.LENGTH_SHORT).show();
					}
				}else if(result == -1){
					AlertDialog.Builder builder = new Builder(BaseActivity.this);
					builder.setTitle("提示");
					builder.setMessage("已存在相同单据编号,是否刷新单据编号");
					builder.setPositiveButton("刷新", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getDanJuBianHao(djbh_edit, nBilType);
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
				}
				else if(result == -2){
					Toast.makeText(BaseActivity.this, "单据已经是执行状态，不能执行修改操作", Toast.LENGTH_SHORT).show();
				}
			} catch(Exception e){
				e.printStackTrace();
				Toast.makeText(BaseActivity.this, "检查单据编号错误！", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * 显示选择录单日期的dialog
	 */
	public void showChooseDate(final TextView tv){
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
	 * 页面加载时获取初始单据编号
	 */
	public void getDanJuBianHao(EditText djbh_edit,String nBiltype){
		this.djbh_edit = djbh_edit;
		String action = getString(R.string.dxbh_action);
		params.put("cMode", "Z");
		params.put("nBiltype", nBiltype);
		params.put("date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		try{
			params.put("operatorName", application.getUserEntity().getEfullName());
			params.put("szOperCode", application.getUserEntity().geteUserCode());
		}catch(Exception e){
			Toast.makeText(this, "用户为空，获取不到单据编号.", Toast.LENGTH_SHORT).show();
			return;
		}
		Utils.showProgressDialog("正在获取单据编号", this);
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action, params, handler, new GetDJBH(djbh_edit));
		Thread t = new Thread(cs);
		t.start();
	}
	/**
	 * 请求单据编号
	 * @version 1.0
	 * @author Administrator
	 */
	class GetDJBH extends ExcuteThread{
		
		private EditText djbh_edit;
		
		public GetDJBH(EditText djbh_edit){
			this.djbh_edit = djbh_edit;
		}
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				bilNumber = jo.getString("bilNumber");
				djbh_edit.setText(bilNumber);
				bhShow = true;
			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	/**
	 * 提交成功的返回处理
	 * @param
	 */
	public void submitResult(final Intent intent){
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.submit_result_dialog);
		Button add = (Button) dialog.findViewById(R.id.add);
		Button print = (Button) dialog.findViewById(R.id.print);
//		print.setVisibility(View.GONE);
		Button cancel = (Button) dialog.findViewById(R.id.cancel);
		add.setOnClickListener(
				new OnClickListener() {
					public void onClick(View view) {
						dialog.dismiss();
						startActivity(intent);
						BaseActivity.this.finish();
					}
				});
		cancel.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						dialog.dismiss();
						BaseActivity.this.finish();
					}
				});
//		boolean print_flag = preferences.getBoolean(StaticValues.PRINTER_ACTIVE, false);
//		if(print_flag){
		print.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					btPrint();
				}
			});
//		}
		dialog.setCancelable(false);
		dialog.show();
	}
	
	/**
	 * 打印
	 */
	public void print(){
		String printServer = "http://"+preferences.getString(StaticValues.PRINTER_IP, "")+":"
				+ preferences.getString(StaticValues.PRINTER_PORT, "8088");
		String printAction = getString(R.string.print_action);
		Utils.showProgressDialog("正在打印中……", this);
		HashMap<String, String> param = new HashMap<String, String>();
		//cMode=Z&nBiltype=34&szOperator=00001&nBilid=2
		param.put("cMode", "Z");
		param.put("nBiltype", nBilType);
		param.put("szOperator", application.getUserEntity().getId());
		param.put("nBilid", bilid+"");
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), printServer+printAction, param, handler, new PrintResult());
		cs.zip = false;
		Thread t = new Thread(cs);
		t.start();
	}
	
	/**
	 * 蓝牙打印机
	 */
	public void btPrint(){
		String printValue = getPrintValue();
		if(printValue == null){
			Toast.makeText(this, "该单据不支持打印!", 2000).show();
			return;
		}
		BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!mAdapter.isEnabled()){
			Toast.makeText(this, "请先打开蓝牙后再打印！", 2000).show();
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(intent);
		}else{
			Intent intent = new Intent();
			intent.putExtra("data", printValue);
			intent.setClass(this, PrintActivity.class);
			startActivity(intent);
		}
	}
	
	public String getPrintValue(){
		return null;
	}
	
	/**
	 * 打印结果的返回
	 * @author funever
	 */
	class PrintResult extends ExcuteThread{
		@Override
		public void run() {
			try{
				String result = getJsonString().trim();
				if(result!=null && result.equals(getString(R.string.print_result))){
					Toast.makeText(BaseActivity.this, "打印成功!", Toast.LENGTH_LONG).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				Utils.dismissProgressDialog();
//				BaseActivity.this.finish();
			}
		}
	}
	/**
	 * 返回菜单
	 */
	public void returnMenu(){
		if(selectProducts.size()==0){
			BaseActivity.this.finish();
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setMessage("确定返回吗，选择的数据将不被保存！");
		builder.setTitle("提示");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("确定返回",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						BaseActivity.this.finish();
					}
				});
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		builder.setCancelable(false);
		builder.show();
	}
	
	protected void onPause() {
		SetValue.getIntance().onPause();
    	super.onPause();
    	MobclickAgent.onPause(this);
    	setHibernation(true);
	}   
	
    public synchronized void onResume() {
    	SetValue.getIntance().onResume();
        super.onResume();
        MobclickAgent.onResume(this);
        setHibernation(false);
    }
    
    public void playMedia(int seconds){
    	mediaPlayer = MediaPlayer.create(this, R.raw.beep);
    	mediaPlayer.start();
    	handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.stop();
			}
		}, seconds);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (mediaPlayer != null) {  
    		mediaPlayer.release();  
    		mediaPlayer = null;  
        } 
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_CAMERA || keyCode== KeyEvent.KEYCODE_VOLUME_DOWN){
    		if(!canScan()){
    			return super.onKeyDown(keyCode, event);
    		}
    		Intent intent = new Intent();
    		intent.setClass(this, CaptureActivity.class);
    		startActivityForResult(intent, EntityListActivity.CAMREA_RE);
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == RESULT_CANCELED){
    		if(requestCode == SubmitOrderCheckActivity.RECODE){
    			this.finish();
    		}
    		return;
    	}
    	if(requestCode == EntityListActivity.CAMREA_RE){
    		String value = data.getStringExtra("scanvalue");
//    		playMedia(10);
    		sv.setScannerValue(value);
    		return;
    	}
    }
    
    /**
     * 上一张下一张的button的专用监听
     * @author funever_win8
     *
     */
    public class LastNextClickListener implements OnClickListener{
    	
    	String biltype;
    	
    	public LastNextClickListener(String biltype){
    		this.biltype = biltype;
    	}
    	
		@Override
		public void onClick(View v) {
			if(!isEdit){
				Toast.makeText(BaseActivity.this, "该单据不能获取上下张单据，仅供浏览！", Toast.LENGTH_SHORT).show();
				return;
			}
			findLastNextPage((Button)v,biltype);
		}
    }
    
    boolean showNextFlag = false;
    /**
     * 寻找上一个下一个单据
     * @param action
     * @param biltype
     */
	public void findLastNextPage(Button action,String biltype) {
		params.clear();
		final String action_url = getString(R.string.draft_last_next);
		
		params.put("cMode", action==last?"L":"N");
		params.put("bilType", biltype);
		params.put("bilID", bilid+"");
		params.put("operator", application.getUserEntity().getId());
		
		if(!showNextFlag && selectProducts.size()!=0){
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
			.setMessage("确定？选择的数据将不被保存！");
			builder.setTitle("提示");
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Utils.showProgressDialog("正在获取单据信息,请稍候……",BaseActivity.this);
					CommunicationServer cs = new CommunicationServer(BaseActivity.this, application.getClient(), server+action_url,params, handler, new GetLastNext());
					Thread t = new Thread(cs);
					t.start();
					showNextFlag = true;
				}
			});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.setCancelable(false);
			builder.show();
		}else{
			Utils.showProgressDialog("正在获取单据信息,请稍候……",BaseActivity.this);
			CommunicationServer cs = new CommunicationServer(BaseActivity.this, application.getClient(), server+action_url,params, handler, new GetLastNext());
			Thread t = new Thread(cs);
			t.start();
		}
	}
	/**
	 * 获取到上张或者是下张单据后执行的线程
	 * @author funever_win8
	 *
	 */
	class GetLastNext extends ExcuteThread{
		@Override
		public void run() {
			if(getJsonString().equals("[]")){
				Toast.makeText(BaseActivity.this, "没有可用的单据！", Toast.LENGTH_SHORT).show();
				Utils.dismissProgressDialog();
				return;
			}else{
				//填充值或者是下一步的请求商品操作
//				Toast.makeText(BaseActivity.this, getJsonString(), Toast.LENGTH_SHORT).show();
				try {
					JSONArray ja = new JSONArray(getJsonString());
					JSONObject jo = ja.getJSONObject(0);
					bilid = jo.getInt("bilID");
					TableEntity te = Utils.parseJSONObjectToObject(TableEntity.class, jo);
					bilNumber = te.getNumber();
					String action_url = getString(R.string.draft_last_next);
					params.put("cMode", "G");
					params.put("bilID", bilid+"");
					
					CommunicationServer cs = new CommunicationServer(BaseActivity.this, application.getClient(), server+action_url,params, handler, new GetLastNextProduct(te));
					Thread t = new Thread(cs);
					t.start();
				}  catch (Exception e) {
					Utils.dismissProgressDialog();
				}
			}
		}
	}
	
	/**
	 * 获取上张下张数据的商品基本信息
	 * @author funever_win8
	 *
	 */
	class GetLastNextProduct extends ExcuteThread{
		
		TableEntity te;
		
		public GetLastNextProduct(TableEntity te){
			this.te = te;
		}
		@Override
		public void run() {
			if(te!=null){
				try {
					mulitZh.clear();
					selectProducts.clear();
					JSONArray ja = new JSONArray(getJsonString());
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						int accountType = jo.getInt("accountType");
						if(accountType==0){
							ProductEntity pe = new ProductEntity();
							Utils.setObjectValue(pe, jo);
							pe.setFullName(pe.getgFullName());
							pe.setId(pe.getgID());
							pe.setPriceRec(pe.getPriceUnit());
							pe.setDisRec(pe.getDisCount());
							pe.setNumUnit1(pe.getNumUnit1());
							pe.setLargess(jo.getString("largess"));
							pe.setNum(pe.getNum()*Float.parseFloat(pe.getuRateBil()));
							ProductEntity product = pe.clone();
							product.setCopy(true);
							pe.setCloneProduct(product);
							selectProducts.add(pe,true);
						}else if(accountType == 1){
							ZhangHuEntity zh = new ZhangHuEntity();
							//银行账户信息
//							fkzh_entity.setId(jo.getString("bID"));
//							fkzh_entity.setFullName(jo.getString("gFullName"));
//							skzh_entity = fkzh_entity;
							
							zh.setId(jo.getString("bID"));
							zh.setFullName(jo.getString("gFullName"));
							je = StaticValues.format(jo.getString("amount"), 2);
							mulitZh.put(zh, Float.parseFloat(je));
						}else if(accountType == 2){
							//优惠信息
							String youhui = StaticValues.format(jo.getString("amount"), 2);
							te.setYouhui(youhui);
						}
					}
                    disposeLastNextData(te);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    Utils.dismissProgressDialog();
                }
			}
		}
	}
	
	public void getDraftProduct(String json,TableEntity te){
		mulitZh.clear();
		try{
			selectProducts.clear();
			JSONArray ja = new JSONArray(json);
			for(int i=0;i<ja.length();i++){
				JSONObject jo = ja.getJSONObject(i);
				int accountType = jo.getInt("accountType");
				if(accountType==0){
					ProductEntity pe = new ProductEntity();
					Utils.setObjectValue(pe, jo);
					pe.setFullName(pe.getgFullName());
					pe.setId(pe.getgID());
					pe.setPriceRec(pe.getPriceUnit());
					pe.setDisRec(pe.getDisCount());
					pe.setNumUnit1(pe.getNumUnit1());
					pe.setLargess(jo.getString("largess"));
					pe.setNum(pe.getNum()*Float.parseFloat(pe.getuRateBil()));
					selectProducts.add(pe,true);
				}else if(accountType == 1){
					ZhangHuEntity zh = new ZhangHuEntity();
					//银行账户信息
//					fkzh_entity.setId(jo.getString("bID"));
//					fkzh_entity.setFullName(jo.getString("gFullName"));
//					skzh_entity = fkzh_entity;
					zh.setId(jo.getString("bID"));
					zh.setFullName(jo.getString("gFullName"));
					je = StaticValues.format(jo.getString("amount"), 2);
					mulitZh.put(zh, Float.parseFloat(je));
				}else if(accountType == 2){
					//优惠信息，现在不处理
					String youhui = StaticValues.format(jo.getString("amount"), 2);
					te.setYouhui(youhui);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(this, "草稿商品列表信息错误!", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 更新商品的信息
	 */
	public abstract void updateProNum();
	
	/**
	 * 验证提交表单
	 */
	
	public abstract boolean validate();
	/**
	 * 设置扫描的值
	 * @param value
	 */
	public abstract void setScanValue(String value);
	
	public abstract boolean canScan();
	/**
	 * 处理获取到上张下张单据信息后的处理
	 * @param te 单据的数据
	 */
	public abstract void disposeLastNextData(TableEntity te);
	
	/**
	 * 获取单据编号，可能是编辑后的
	 */
	public abstract String getNumber();
	
	/**
	 * edittext 回退到空时的处理
	 * @author funever_win8
	 *
	 */
	public class BaseOnTextChangeListener implements TextWatcher{
		
		public static final int CK_KEY = 1; 
		public static final int DW_KEY = 2;
		public static final int JSR_KEY = 3;
		public static final int FHCK_KEY = 4;
		public static final int BM_KEY = 5;
		public static final int FKZH_KEY = 6;
		public static final int SKZH_KEY = 7;
		
		private EditText et;
		
		private int key;
		
		public BaseOnTextChangeListener(EditText et,int key){
			this.et = et;
			this.key = key;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(!isEdit){
				return;
			}
			if(et.getText().toString().equals("") && before>0){
				String show = "";
				switch(key){
					case CK_KEY:
						if(!"".equals(ck_entity.getFullName())){
							show = "仓库已清空";
						}
						ck_entity = new CangKuEntity();
						break;
					case DW_KEY:
						if(!"".equals(dw_entity.getFullName())){
							show = "单位已清空";
						}
						dw_entity = new DanWeiEntity();
						break;
					case JSR_KEY:
						if(!"".equals(dw_entity.getFullName())){
							show = "经手人已清空";
						}
						jsr_entity = new JingShouRenEntity();
						break;
					case FHCK_KEY:
						if(!"".equals(fhck_entity.getFullName())){
							show = "发货仓库已清空";
						}
						fhck_entity = new CangKuEntity();
						break;
					case BM_KEY:
						if(!"".equals(bm_entity.getFullName())){
							show = "部门已清空";
						}
						bm_entity = new BuMenEntity();
						break;
					case FKZH_KEY:
						if(!"".equals(fkzh_entity.getFullName())){
							show = "付款账户已清空";
						}
						fkzh_entity = new ZhangHuEntity();
						break;
					case SKZH_KEY:
						if(!"".equals(skzh_entity.getFullName())){
							show = "收款账户已清空";
						}
						skzh_entity = new ZhangHuEntity();
						break;
					default:
						break;
				}
//				if(!show.equals("")){
//					Toast.makeText(BaseActivity.this, show, Toast.LENGTH_SHORT).show();
//				}
			}
		}
	}
	
	public void showJeLayout(ZhangHuEntity zhEntity,EditText jeText,EditText zh_Text){
		String action = getString(R.string.fkzh_action);
		HashMap<String, String> 	paramMap = new HashMap<String, String>();
		paramMap.put("cMode", "1");
		paramMap.put("szParid", "00000");
		paramMap.put("operator", "00000");
		paramMap.put("cKind", "Z");
		paramMap.put("cpage", 1+"");
		paramMap.put("nBiltype", nBilType);
		
		Utils.showProgressDialog("正在获取账户列表……", this);
		CommunicationServer cs = new CommunicationServer(this,application.getClient(),server+action,paramMap,handler, new GetZHList(zhEntity,jeText,zh_Text));
		Thread t = new Thread(cs);
		t.start();
	}
	
	class GetZHList extends ExcuteThread{
		
		ZhangHuEntity zhEntity;
		EditText jeText;
		EditText zh_Text;
		
		public GetZHList(ZhangHuEntity zhEntity,EditText jeText,EditText zh_Text){
			this.zh_Text = zh_Text;
			this.zhEntity = zhEntity;
			this.jeText = jeText;
		}
		
		@Override
		public void run() {
			try {
				JSONArray ja = new JSONArray(getJsonString());
				List<ZhangHuEntity> zhList = new LinkedList<ZhangHuEntity>();
				for(int i=0;i<ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					ZhangHuEntity zh = new ZhangHuEntity();
					Utils.setObjectValue(zh, jo);
					zhList.add(zh);
				}
				showMultipleCash(zhEntity,jeText,zh_Text,zhList);
			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	public void showMultipleCash(final ZhangHuEntity zhEntity,final EditText jeText,final EditText zh_Text,List<ZhangHuEntity> zhList){
		final Dialog dialog = new Dialog(this, R.style.dialog);
		LinearLayout mainLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.je_layout, null);
		LinearLayout add_layout = (LinearLayout) mainLayout.findViewById(R.id.add_layout);
		final HashMap<EditText,ZhangHuEntity> edMap = new HashMap<EditText,ZhangHuEntity>();
		for(int i=0;i<zhList.size();i++){
			RelativeLayout row = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.je_row,null);
			TextView title = (TextView) row.findViewById(R.id.zh_name);
			title.setText(zhList.get(i).getFullName()+":");
			EditText edit = (EditText) row.findViewById(R.id.edit);
			for(Entry<ZhangHuEntity, Float> entry : mulitZh.entrySet()){
				ZhangHuEntity zhangHuEntity = entry.getKey();
				if(zhangHuEntity.getId().equals(zhList.get(i).getId())){
					edit.setText(entry.getValue()+"");
					break;
				}
			}
			edMap.put(edit, zhList.get(i));
			add_layout.addView(row);
		}
		Button save = (Button) mainLayout.findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = 0;
				List<ZhangHuEntity> hasValue = new LinkedList<ZhangHuEntity>();
				mulitZh.clear();
				for(EditText et : edMap.keySet()){
					if(!et.getText().toString().equals("")){
						count += Float.parseFloat(et.getText().toString());
						hasValue.add(edMap.get(et));
						mulitZh.put(edMap.get(et), Float.parseFloat(et.getText().toString()));
					}
				}
				if(hasValue.size()>0){
					if(hasValue.size() == 1){
						zhEntity.setId(hasValue.get(0).getId());
						zhEntity.setFullName(hasValue.get(0).getFullName());
						zh_Text.setText(hasValue.get(0).getFullName());
					}else{
						zh_Text.setText("现金银行多账户");
						jeText.setEnabled(false);
					}
				}
				jeText.setText(count+"");
				dialog.dismiss();
			}
		});
		dialog.setContentView(mainLayout);
		dialog.show();
	}
	
	/**
	 * 多账户格式化
	 * @return
	 */
	public String formatZhangHu(ZhangHuEntity zh){
		String re = "";
		if(mulitZh.size()>0){
			for(ZhangHuEntity zhEntity : mulitZh.keySet()){
				if(re.equals("")){
					re = zhEntity.getId();
				}else{
					re += (";"+zhEntity.getId());
				}
			}
		}else if(zh!=null){
			return zh.getId();
		}
		return re;
	}
	
	public String formatCount(String inputCount){
		String re = "";
		if(mulitZh.size()>1){
			for(float count : mulitZh.values()){
				if(re.equals("")){
					re = count+"";
				}else{
					re += (";"+count+"");
				}
			}
		}else{
			return inputCount;
		}
		return re;
	}
	
	/**
	 * 返回价格合计
	 * @return
	 */
	public void getAllCount(EditText jeText){
		float re = 0;
		for(float f : mulitZh.values()){
			re += f;
		}
		jeText.setText(re+"");
		if(mulitZh.size()>1){
			jeText.setEnabled(false);
		}
	}
	
	public void getZhName(EditText zh_Text){
		if(mulitZh.size()>1){
			zh_Text.setText("现金银行多账户");
		}else if(mulitZh.size()==1){
			Object[] ob = mulitZh.keySet().toArray();
			ZhangHuEntity zh = (ZhangHuEntity) ob[0];
			zh_Text.setText(zh.getFullName());
		}else{
			zh_Text.setText("");
		}
	}

	@Override
	public void disposeScanValue(String re) {
		if(hiberation){
			Toast.makeText(this, "扫描数据正在处理中，请处理完毕再进行扫描", Toast.LENGTH_SHORT).show();
			return;
		}
		if(re.startsWith("*")&&re.endsWith("*")){
			re = re.substring(1, re.length()-1);
		}
		setScanValue(re);
	}
	
	@Override
	protected void onStop() {
		Utils.showing = true;
		super.onStop();
	}
	
	/**
	 * 提交后的返回处理
	 * @version 1.0
	 * @author Administrator
	 */
	public class SubmitExcute extends ExcuteThread{
		
		Intent intent;
		
		public SubmitExcute(Intent intent){
			this.intent = intent;
		}
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				String result = jo.getString("result");
				bilid = jo.getInt("bilID");
				if(result.equalsIgnoreCase("success")){
					if(print_flag){
						print();
					}
					submitResult(intent);
				}else{
					Toast.makeText(BaseActivity.this, "服务器数据处理异常!", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(BaseActivity.this, "服务器数据处理异常!", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	public void checkOrder(){
		Intent intent = new Intent();
		intent.setClass(this, SubmitOrderCheckActivity.class);
		intent.putExtra(OrderCheckActivity.ORDER_INFO, orderInfo);
		startActivityForResult(intent, SubmitOrderCheckActivity.RECODE);
	}
	
	/**
	 * 获取打印商品列表
	 * @return
	 */
	public String getPrintProducts(){
		String products = "";
		for(ProductEntity pe : selectProducts){
			float je = 0;
			try {
				je = Float.parseFloat(pe.getPriceRec())
						* pe.getNumUnit1();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String je_str = "";
			String danjia_str = "";
			if ((this instanceof TabHostActivity || this instanceof OrderActivity
					|| this instanceof ReturnOrderActivity
					|| this instanceof KuCunActivity) && application.getRightCost() == 0) {
				je_str="*";
				danjia_str="*";
			}else{
				je_str = StaticValues.format(je,
						application.getPriceDigit());
				danjia_str = StaticValues.format(
						Float.parseFloat(pe.getPriceRec()),
						application.getPriceDigit());
			}
			products += getString(R.string.print_product_info, pe.getFullName(), pe.getNum(), danjia_str,je_str);
		}
		return products;
	}
	
	@Override
	public boolean isHibernation() {
		return hiberation;
	}

	@Override
	public void setHibernation(boolean hibernation) {
		this.hiberation = hibernation;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(selectProducts!=null && selectProducts.size()>0 && isEdit){
                returnMenu();
            }else{
                super.onBackPressed();
            }
        }
        return true;
    }

    /**
     * 初始化界面
     */
    public void initView(int postion,View view){}
}
