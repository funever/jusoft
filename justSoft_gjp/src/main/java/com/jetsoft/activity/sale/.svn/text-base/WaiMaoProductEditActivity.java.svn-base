/**
 * 创建日期 2013-4-1
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
package com.jetsoft.activity.sale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.R;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.activity.baobiao.BaoBiaoItemActivity;
import com.jetsoft.activity.product.ProImgGridActivity;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.BaseEntity;
import com.jetsoft.entity.ProductEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WaiMaoProductEditActivity extends SherlockActivity implements OnClickListener{
	/**
	 * 选择商品
	 */
	private RelativeLayout choose_pro;

	private TextView pro_name,pro_standard,pro_type,pro_area,pro_unit1,pro_unit2,pro_urate,pro_num,pro_voidnum;
	
	/**
	 * 选中的产品
	 */
	private ProductEntity entity;
	
	int index;
	
	private String szSID = "00000";
	
	HashMap<String,String> paramMap = new HashMap<String,String>();
	
	String nBilType = "";
	
	MyApplication application;
	
	String server;
	
	String szUid = "";
	
	Button pro_img,pro_replace,pro_kucun,pro_price,pro_beizhu;
	
	Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waimao_choose_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("选择商品");

		application = (MyApplication) getApplication();
		entity = (ProductEntity) getIntent().getSerializableExtra("entity");
		index = getIntent().getIntExtra("index", 0);
		szSID = getIntent().getStringExtra("szSID");
		nBilType = getIntent().getStringExtra("nBiltype");
		szUid = getIntent().getStringExtra("szUid");
		
		choose_pro = (RelativeLayout) findViewById(R.id.choose_pro);
		choose_pro.setOnClickListener(this);

		pro_name = (TextView) findViewById(R.id.pro_name);
		pro_standard = (TextView) findViewById(R.id.pro_standard);
		pro_type = (TextView) findViewById(R.id.pro_type);
		pro_area = (TextView) findViewById(R.id.pro_area);
		pro_unit1 = (TextView) findViewById(R.id.pro_unit1);
		pro_unit2 = (TextView) findViewById(R.id.pro_unit2);
		pro_num = (TextView) findViewById(R.id.pro_num);
		pro_voidnum = (TextView) findViewById(R.id.pro_voidnum);
		pro_urate = (TextView) findViewById(R.id.pro_urate);
		
		SharedPreferences preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
				+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
		String action = getString(R.string.product_void_stock);
		
		MyApplication application = (MyApplication) getApplication();
		paramMap.put("cMode", "Z");
		paramMap.put("szGID", entity.getId());
		paramMap.put("szSID", szSID);
		paramMap.put("szOperator", application.getUserEntity()!=null?application.getUserEntity().getId():"");
		
		Utils.showProgressDialog("正在获取商品信息，请稍候……", this);
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action, paramMap, new Handler(), new SearchProductInfo());
		Thread t = new Thread(cs);
		t.start();
		
		pro_img = (Button) findViewById(R.id.pro_img);
		pro_img.setOnClickListener(this);
		pro_replace = (Button) findViewById(R.id.pro_replace);
		pro_replace.setOnClickListener(this);
		pro_kucun = (Button) findViewById(R.id.pro_kucun);
		pro_kucun.setOnClickListener(this);
		pro_price = (Button) findViewById(R.id.pro_price);
		pro_price.setOnClickListener(this);
		pro_beizhu = (Button) findViewById(R.id.pro_beizhu);
		pro_beizhu.setOnClickListener(this);
		
		if(nBilType.equals(StaticValues.ORDER_IN_BILTYPE) 
				|| nBilType.equals(StaticValues.ORDER_BILTYPE)
				|| nBilType.equals(StaticValues.ORDER_RE_BILTYPE)
				|| nBilType.equals(StaticValues.BIANJIA_BILTYPE)){
			pro_price.setVisibility(View.GONE);
			if(nBilType.equals(StaticValues.BIANJIA_BILTYPE)){
				pro_replace.setVisibility(View.GONE);
			}
		}
		//进入时的动画
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem saveItem = menu.add(0,10001,0,"确定");
        saveItem.setIcon(android.R.drawable.ic_menu_add);
        saveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT|MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 10001){
            //这里要去价格跟踪

            paramMap.put("szSid", szSID);
            paramMap.put("szUid", szUid);
            paramMap.put("nBilType", nBilType);
            paramMap.put("szDate", Utils.getFormatDate(0));
            paramMap.put("bilPriceList[0].szGid", entity.getId());
            paramMap.put("bilPriceList[0].unitID", entity.getUnitID());

            String action = getString(R.string.product_price);
            Utils.showProgressDialog("正在加载商品价格信息……", this);
            CommunicationServer sc = new CommunicationServer(this,application.getClient(),server+action,paramMap,new Handler(), new GetProductPrice(entity));
            Thread t = new Thread(sc);
            t.start();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
		 if(v == choose_pro){
			//暂时不处理
		}else if(v == pro_img){
			//查看商品图片
			Intent intent = new Intent();
			intent.setClass(this, ProImgGridActivity.class);		
			intent.putExtra("szGid", entity.getId());
			startActivity(intent);
		}else if(v == pro_replace){
			search();
		}else if(v == pro_kucun){
			kucunAction();
		}else if(v == pro_price){
			priceAction();
		}else if(v == pro_beizhu){
			showProductBeizhu();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == EntityListActivity.PR_RE_CODE){
			ArrayList<BaseEntity> re = (ArrayList<BaseEntity>) data.getSerializableExtra("entity");
			entity = (ProductEntity) re.get(0);
			showProductInfoDialog(entity);
		}
	}
	
	/**
	 * 返回替代商品时，重新请求商品信息，并刷新页面
	 * @param entity
	 */
	public void showProductInfo(ProductEntity entity){
		
		String action = getString(R.string.product_void_stock);
		MyApplication application = (MyApplication) getApplication();
		paramMap.put("cMode", "Z");
		paramMap.put("szGID", entity.getId());
		paramMap.put("szSID", szSID);
		paramMap.put("szOperator", application.getUserEntity().getId());
		
		Utils.showProgressDialog("正在获取商品信息，请稍候……", this);
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action, paramMap, new Handler(), new SearchProductInfo());
		Thread t = new Thread(cs);
		t.start();
	}
	
	public void showProductBeizhu(){
		Dialog dialog = new Dialog(this, R.style.dialog);
		View view = LayoutInflater.from(this).inflate(R.layout.product_beizhu_layout, null);
		dialog.setContentView(view);
		dialog.getWindow().getAttributes().width = StaticValues.dip2px(this, 300);
		dialog.getWindow().getAttributes().height = StaticValues.dip2px(this, 350);
		EditText beizhu = (EditText) dialog.findViewById(R.id.pro_beizhu);
		beizhu.setText(entity.getMemo());
		dialog.show();
	}
	
	/**
	 * 显示替换的商品信息
	 * @param productEntity
	 */
	public void showProductInfoDialog(final ProductEntity productEntity){
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.replace_pro_info);
		
		TextView pro_name = (TextView) dialog.findViewById(R.id.pro_name);
		TextView pro_guige = (TextView) dialog.findViewById(R.id.pro_guige);
		TextView pro_type = (TextView) dialog.findViewById(R.id.pro_type);
		TextView pro_chandi = (TextView) dialog.findViewById(R.id.pro_chandi);
		TextView pro_urate = (TextView) dialog.findViewById(R.id.pro_urate);
		TextView pro_kucun = (TextView) dialog.findViewById(R.id.pro_kucun);
		TextView pro_void = (TextView) dialog.findViewById(R.id.pro_void);
		
		Button replace = (Button) dialog.findViewById(R.id.btn_replace);
		Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		
		replace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				entity = productEntity;
				showProductInfo(productEntity);
				dialog.dismiss();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		pro_name.setText(productEntity.getFullName());
		pro_guige.setText(productEntity.getStandard());
		pro_type.setText(productEntity.getType());
		pro_chandi.setText(productEntity.getArea());
		pro_urate.setText(productEntity.getuRate());
		pro_kucun.setText(productEntity.getNum()+"");
		pro_void.setText(productEntity.getVoidNum());
		
		dialog.show();
	}
	
	/**
	 * 选择替代商品
	 */
	public void search(){
		Intent intent = new Intent();
		intent.setClass(this, EntityListActivity.class);
		intent.putExtra("type", EntityListActivity.PR_RE_CODE);
		intent.putExtra("nBilType", nBilType);
		intent.putExtra("szSid", szSID);
		intent.putExtra("szUid", szUid);
		intent.putExtra("search", "");
		//单选
		intent.putExtra("pandian", true);
		intent.putExtra("replace", true);
		intent.putExtra("szGid", entity.getId());
		startActivityForResult(intent, EntityListActivity.PR_RE_CODE);
	}
	
	public void kucunAction(){
		String action = getString(R.string.product_kucun_action);
		paramMap.put("cMode", "Z");
		paramMap.put("szGid", entity.getId());
		paramMap.put("szOperator", application.getUserEntity().getId());
		
		Utils.showProgressDialog("正在请求库存数据……", this);
		CommunicationServer cs = new CommunicationServer(this,application.getClient(),server+action,paramMap,handler,new GetKuCun());
		Thread t = new Thread(cs);
		t.start();
	}
	
	/**
	 * 获取商品价格
	 */
	public void priceAction(){
		String action = getString(R.string.product_price_action);
		paramMap.put("cMode", "O");
		paramMap.put("szGid", entity.getId());
		paramMap.put("nPriceType", "0");
		paramMap.put("szOperator", application.getUserEntity().getId());
		
		Utils.showProgressDialog("正在请求 价格数据……", this);
		CommunicationServer cs = new CommunicationServer(this,application.getClient(),server+action,paramMap,handler,new GetProductPriceInfo());
		Thread t = new Thread(cs);
		t.start();
	}
	
	/**
	 * 获取商品价格信息
	 * @author funever_win8
	 *
	 */
	public class GetProductPrice extends ExcuteThread {
		
		ProductEntity pe;
		
		public GetProductPrice(ProductEntity pe){
			this.pe = pe;
		}
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				Utils.setObjectValue(pe, jo);
			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				Utils.dismissProgressDialog();
			}
			editNumPrice();
		}
	}
	
	Dialog dialog;
	Button ok;
	Button cancel;
	Button recent_sale;
	public void editNumPrice(){
		dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.waimao_edit_num_price);
		EditText pro_count = (EditText) dialog.findViewById(R.id.pro_num);
		EditText pro_unit1 = (EditText) dialog.findViewById(R.id.pro_unit1);
		EditText pro_unit2 = (EditText) dialog.findViewById(R.id.pro_unit2);
		EditText pro_price = (EditText) dialog.findViewById(R.id.pro_price);
		pro_price.setText(StaticValues.format(entity.getPriceRec(), application.getPriceDigit()));
		MyApplication application = (MyApplication) getApplication();
		if(application.getBilPrice()==0){
			pro_price.setEnabled(false);
		}
		ok =  (Button) dialog.findViewById(R.id.ok);
		cancel = (Button) dialog.findViewById(R.id.cancel);
		recent_sale = (Button) dialog.findViewById(R.id.recent_sale);
		ok.setOnClickListener(new DialogClickListener(pro_count,pro_price,pro_unit1,pro_unit2));
		cancel.setOnClickListener( new DialogClickListener(pro_count,pro_price,pro_unit1,pro_unit2));
		recent_sale.setOnClickListener( new DialogClickListener(pro_count,pro_price,pro_unit1,pro_unit2));
		dialog.show();
	}
	
	
	class DialogClickListener implements OnClickListener{
		
		EditText pro_count;
		
		EditText pro_price;
		
		EditText pro_unit1;
		EditText pro_unit2;
		
		public DialogClickListener(EditText pro_count,EditText pro_price,EditText pro_unit1,EditText pro_unit2){
			this.pro_count = pro_count;
			this.pro_price = pro_price;
			this.pro_unit1 = pro_unit1;
			this.pro_unit2 = pro_unit2;
		}
		
		@Override
		public void onClick(View v) {
			if(v==ok){
				//如果没有输入则都为0
				float num = 0;
				try{
					num = Float.parseFloat(pro_count.getText().toString());
				}catch(Exception e){
					num = 0;
				}
				float count = 0;
				try{
					count = Float.parseFloat(pro_price.getText().toString());
				}catch(Exception e){
					count = 0;
				}
				float unit1 = 0;
				try{
					unit1 = Float.parseFloat(pro_unit1.getText().toString());
				}catch(Exception e){
					unit1 = 0;
				}
				float unit2 = 0;
				try{
					unit2 = Float.parseFloat(pro_unit2.getText().toString());
				}catch(Exception e){
					unit2 = 0;
				}
//				float urate = 1;
//				try{
//					urate = Float.parseFloat(entity.getuRate0());
//				}catch(Exception e){
//					urate = 1;
//				}
				float co = num*Float.parseFloat(entity.getuRate0())+unit1*Float.parseFloat(entity.getuRate1())+unit2*Float.parseFloat(entity.getuRate2());
				co = Float.parseFloat(StaticValues.format(co, 4));
				//基本数量+辅助数量(辅助数量=输入的辅助数量*单位关系)
				entity.setNum(co);
				try{
					float uRateBil = Float.parseFloat(entity.getuRateBil());
					if(uRateBil==0){
						uRateBil = 1;
					}
					entity.setNumUnit1(Float.parseFloat(StaticValues.format(co/uRateBil,4)));
				}catch(Exception e){
					Toast.makeText(WaiMaoProductEditActivity.this, "商品单位关系错误，不能解析为数字！", Toast.LENGTH_LONG).show();
					return;
				}
				entity.setPriceRec(count+"");
				
				/**
				 * 商品添加描述
				 */
				Intent intent = new Intent();
				intent.putExtra("index", index);
				intent.putExtra("entity", entity);
				setResult(RESULT_OK, intent);
				dialog.dismiss();
				WaiMaoProductEditActivity.this.finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}else if(v == cancel){
				dialog.dismiss();
				WaiMaoProductEditActivity.this.finish();
			}else if(v == recent_sale){
				Intent intent = new Intent();
				intent.setClass(WaiMaoProductEditActivity.this, BaoBiaoItemActivity.class);
				intent.putExtra("type", BaoBiaoItemActivity.TYPE_MONTH_IN_SALE);
				intent.putExtra("szUID", szUid);
				intent.putExtra("szGID", entity.getId());
				startActivity(intent);
			}
		}
	}
	
	class SearchProductInfo extends ExcuteThread{
		
		@Override
		public void run() {
			//{"area":"产地","fullName":"商品1","num":200,"standard":"规格","type":"型号","uRate":"","unit1":"g","unit2":"gg","voidNum":472}
			try {
				JSONArray ja = new JSONArray(getJsonString());
				if(ja.length()>0){
					JSONObject jo = ja.getJSONObject(0);
					Utils.setObjectValue(entity, jo);
					pro_area.setText(entity.getArea());
					pro_name.setText(entity.getFullName());
					pro_num.setText(entity.getNum()+"");
					pro_standard.setText(entity.getStandard());
					pro_type.setText(entity.getType());
					pro_urate.setText(StaticValues.format(entity.getuRate(), 2));
					entity.setuRate0(StaticValues.format(entity.getuRate0(), 2));
					pro_unit1.setText(entity.getUnit1());
					pro_unit2.setText(entity.getUnit2());
					pro_voidnum.setText(entity.getVoidNum());
				}
			} catch (JSONException e) {
				Toast.makeText(WaiMaoProductEditActivity.this, "获取商品信息异常!", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	/**
	 * 获取库存
	 * @author funever_win8
	 *
	 */
	class GetKuCun extends ExcuteThread{
		
		@Override
		public void run() {
			try {
				JSONArray ja = new JSONArray(getJsonString());
				List<String> kuCunList = new LinkedList<String>();
				for(int i=0;i<ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					kuCunList.add(jo.getString("sName")+":"+StaticValues.format(jo.getString("num"), 4));
				}
				kuCunList.add("虚拟库存:"+pro_voidnum.getText().toString());
				if(kuCunList.size()>0){
					showProductInfo(kuCunList,"库存信息");
				}else{
					Toast.makeText(WaiMaoProductEditActivity.this, "没有获取\""+entity.getFullName()+"\"的库存信息!", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(WaiMaoProductEditActivity.this, "获取\""+entity.getFullName()+"\"的库存信息错误!", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	/**
	 * 显示库存列表
	 * @param kuCunList
	 */
	public void showProductInfo(List<String> kuCunList,String title){
		Dialog dialog = new Dialog(this, R.style.dialog);
		View view = LayoutInflater.from(this).inflate(R.layout.product_kucun_layout, null);
		dialog.setContentView(view);
		TextView tv = (TextView) dialog.findViewById(R.id.title);
		tv.setText(title);
		ListView lv = (ListView) view.findViewById(R.id.pro_kucun);
		SimpleTextAdapter sta = new SimpleTextAdapter(kuCunList);
		lv.setAdapter(sta);
		dialog.show();
	}
	
	public class SimpleTextAdapter extends BaseAdapter{
		
		private List<String> kuCunList;
		
		public SimpleTextAdapter(List<String> kuCunList){
			this.kuCunList = kuCunList;
		}

		@Override
		public int getCount() {
			return kuCunList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return kuCunList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = LayoutInflater.from(WaiMaoProductEditActivity.this).inflate(R.layout.list_text_item, null);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView value = (TextView) view.findViewById(R.id.value);
			String[] s  = kuCunList.get(arg0).split(":");
			title.setText(s[0]+":");
			value.setText(s[1]);
			return view;
		}
	}
	
	/**
	 * 获取商品的价格信息
	 * @author funever_win8
	 *
	 */
	class GetProductPriceInfo extends ExcuteThread{
		@Override
		public void run() {
			//[{"lowSalePrice":"0E-10","price1":"0E-10","price2":"0E-10","price3":"0E-10","price4":"0E-10","price5":"0E-10","recSalePrice":"0E-10","retailprice":"0E-10"}]
			try {
				JSONArray ja = new JSONArray(getJsonString());
				if(ja.length()>0){
					JSONObject jo = ja.getJSONObject(0);
					List<String> list = new LinkedList<String>();
					list.add("预设价格1:"+StaticValues.format(jo.getString("price1"), 4));
					list.add("预设价格2:"+StaticValues.format(jo.getString("price2"), 4));
					list.add("预设价格3:"+StaticValues.format(jo.getString("price3"), 4));
					list.add("预设价格4:"+StaticValues.format(jo.getString("price4"), 4));
					list.add("预设价格5:"+StaticValues.format(jo.getString("price5"), 4));
					list.add("零售价:"+StaticValues.format(jo.getString("retailprice"), 4));
					list.add("最低销售价:"+StaticValues.format(jo.getString("lowSalePrice"), 4));
					list.add("最近销售价:"+StaticValues.format(jo.getString("recSalePrice"), 4));
					showProductInfo(list,"价格信息");
				}
			} catch (Exception e) {
				Toast.makeText(WaiMaoProductEditActivity.this, "获取\""+entity.getFullName()+"\"的价格信息错误!", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
}
