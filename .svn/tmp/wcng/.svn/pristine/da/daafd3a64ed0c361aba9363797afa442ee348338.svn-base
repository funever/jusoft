/**
 * 创建日期 2012-12-17
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
package com.jetsoft.adapter;

import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jetsoft.R;
import com.jetsoft.activity.BaseActivity;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.activity.TabHostActivity;
import com.jetsoft.activity.kucun.KuCunActivity;
import com.jetsoft.activity.order.OrderActivity;
import com.jetsoft.activity.returnorder.ReturnOrderActivity;
import com.jetsoft.activity.sale.WaiMaoActivity;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.*;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;

public class WaiMaoProSelectAdapter extends BaseAdapter{
	
	private Context ctx;
	
	public List<ProductEntity> proList;
	
	public EditText searchText;
	
	public ImageView search;
	
	public String searchString;
	
	MyApplication application;
	
	String server ;
	
	HashMap<String,String> paramMap = new HashMap<String,String>();
	
	public WaiMaoProSelectAdapter(Context ctx,List<ProductEntity> proList){
		this.ctx = ctx;
		this.proList = proList;
		this.application = (MyApplication) ((BaseActivity)ctx).getApplication();
	}

	@Override
	public int getCount() {
		return proList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return proList.get(position+1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if(position==0){
			view = LayoutInflater.from(ctx).inflate(R.layout.product_list_row, null);
			search = (ImageView) view.findViewById(R.id.pro_search);
			ImageView pro_add = (ImageView) view.findViewById(R.id.pro_add);
			searchText = (EditText) view.findViewById(R.id.pro_search_text);
//			searchText.setText(searchString);
			search.setOnClickListener(new MyOnClickListener(searchText));
			pro_add.setOnClickListener(new MyOnClickListener(searchText));
		}else{
			view = LayoutInflater.from(ctx).inflate(R.layout.product_list_row1, null);
			((TextView)view.findViewById(R.id.prolist_spqm)).setText(proList.get(position-1).getFullName());
			((TextView)view.findViewById(R.id.pro_danwei)).setText(proList.get(position-1).getUnit1());
			((TextView)view.findViewById(R.id.pro_shuliang)).setText(proList.get(position-1).getNumUnit1()+"");
			((TextView)view.findViewById(R.id.guige)).setText(proList.get(position-1).getStandard()==null?"":proList.get(position-1).getStandard()+"");
			((TextView)view.findViewById(R.id.xinghao)).setText(proList.get(position-1).getType()==null?"":proList.get(position-1).getType()+"");
			TextView t_je = ((TextView)view.findViewById(R.id.pro_je));
			MyApplication application = (MyApplication) ((BaseActivity)ctx).getApplication();
			float je = Float.parseFloat(proList.get(position-1).getPriceRec())*proList.get(position-1).getNumUnit1();
			//辅助数量
			TextView fzsl_tv = ((TextView)view.findViewById(R.id.pro_fzsl));
			ProductEntity product = proList.get(position-1);
			int ur0 = 0;
			int ur1 = 0;
			int ur2 = 0;
			try{
				ur0 = (int) Float.parseFloat(StaticValues.format(product.getuRate0(), 2));
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				ur1 = (int) Float.parseFloat(StaticValues.format(product.getuRate1(),2));
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				ur2 = (int) Float.parseFloat(StaticValues.format(product.getuRate2(),2));
			}catch(Exception e){
				e.printStackTrace();
			}
			String fzsl_str = ProSelectAdapter.getNumUnitFz2((float)product.getNum(), product.getUnitName0(), product.getUnitName1(), product.getUnitName2(),ur0, ur1, ur2);
			fzsl_tv.setText(fzsl_str);
			
			if(!proList.get(position-1).getLargess().equals("0") && !(ctx instanceof KuCunActivity)){
				view.findViewById(R.id.zp).setVisibility(View.VISIBLE);
				TextView ch = (TextView) view.findViewById(R.id.check);
				ch.setText("是");
				je = 0;
			}
			if(application.getRightCost()==1){
				//有权限时显示数字
				((TextView)view.findViewById(R.id.pro_danjia)).setText(StaticValues.format(Float.parseFloat(proList.get(position-1).getPriceRec()), application.getPriceDigit()));
				t_je.setText(StaticValues.format(je,application.getPriceDigit()));
			}else{
				//没有权限，为一下几个订单样式时显示星号
				if(ctx instanceof TabHostActivity || ctx instanceof OrderActivity || ctx instanceof ReturnOrderActivity || ctx instanceof KuCunActivity){
					((TextView)view.findViewById(R.id.pro_danjia)).setText("*");
					t_je.setText("*");
				}else{
					((TextView)view.findViewById(R.id.pro_danjia)).setText(StaticValues.format(Float.parseFloat(proList.get(position-1).getPriceRec()), application.getPriceDigit()));
					t_je.setText(StaticValues.format(je,application.getPriceDigit()));
				}
			}
		}
		return view;
	}
	
	public class MyOnClickListener implements OnClickListener{
		
		EditText searchET;
		
		public MyOnClickListener(EditText searchET){
			this.searchET = searchET;
		}

		@Override
		public void onClick(View v) {
			performSearch(searchET.getText().toString());
		}
		
	}
	
	/**
	 * 启动查找
	 * @param searchString
	 */
	public void performSearch(String searchString){
		String action = ctx.getString(R.string.product_action);
		/**
		 * 请求的url中的参数
		 */
		paramMap.clear();
		//cMode=Z&szParid=00000&szSid=&szOperator=00000&szStr=
		paramMap.put("cMode", "Z");
		paramMap.put("szParid", "00000");
		paramMap.put("cpage", "1");
		paramMap.put("szSid", ((BaseActivity)ctx).ck_entity.getId());
		paramMap.put("szOperator", application.getUserEntity().getId());
		paramMap.put("szStr", searchString);
		paramMap.put("nBilType", ((BaseActivity)ctx).nBilType);
		String szUid = "";
		try{
			szUid = ((BaseActivity)ctx).dw_entity.getId();
			if(szUid==null || "".equals(szUid)){
				Toast.makeText(ctx, "没有选择单位！", Toast.LENGTH_SHORT).show();
				return;
			}
			paramMap.put("szUid", szUid);
		}catch(Exception e){
			Toast.makeText(ctx, "没有选择单位！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		SharedPreferences preferences = ctx.getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		//获取服务器地址
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, ctx.getString(R.string.server))+":"
				+ preferences.getString(StaticValues.PORT_FLAG, ctx.getString(R.string.port));
		
		if(ctx instanceof BaseActivity){
			BaseActivity activity = (BaseActivity) ctx;
			Utils.showProgressDialog("正在加载中,请稍候……",activity);
		}
		
		CommunicationServer sc = new CommunicationServer(ctx,application.getClient(),server+action,paramMap,new Handler(), new EntityParseThread(searchString));
		Thread t = new Thread(sc);
		t.start();
		searchText.setText("");
	}

	/**
	 * 前往搜索
	 */
	public void search(String searchString){
		Intent intent = new Intent();
		intent.setClass(ctx, EntityListActivity.class);
		intent.putExtra("type", EntityListActivity.PR_RE_CODE);
		intent.putExtra("search", searchString);
		intent.putExtra("nBilType", ((BaseActivity)ctx).nBilType);
		intent.putExtra("szUid", ((BaseActivity)ctx).dw_entity.getId());
		intent.putExtra("pandian", true);
		((Activity)ctx).startActivityForResult(intent, EntityListActivity.PR_RE_CODE);
	}
	
	/**
	 * 验证是否只有一个搜索内容
	 * @version 1.0
	 * @author Administrator
	 */
	public class EntityParseThread extends ExcuteThread {
		
		String searchString;
		
		public EntityParseThread(String searchString){
			this.searchString = searchString;
		}
		
		public void run() {
			try {
				JSONArray ja = new JSONArray(getJsonString());
				//如果搜索的结果只有一个
				if(ja.length()==1){
					JSONObject jo = ja.getJSONObject(0);
					String sonNum = jo.getString("sonNum");
					//并且没有子项，则直接添加
					if(sonNum.equalsIgnoreCase("0")){
						ProductEntity pe = new ProductEntity();
						Utils.setObjectValue(pe, jo);
//						pe.setUniltCode(jo.getString("uniltCode"));
//						pe.setUnitl(jo.getString("unitl"));
//						pe.setUrate(jo.getString("urate"));
//						pe.setZheKou(Float.parseFloat(jo.getString("disRec")));
//						pe.setPrice(Float.parseFloat(jo.getString("priceRec")));
//						WaiMaoActivity activity = (WaiMaoActivity) ctx;
//						activity.editProduct(pe);
						
						String action = ctx.getString(R.string.product_price);
						
//						String cMode;
//						String bilType;
//						String szDate;
//						String operator;
						
						paramMap.put("szDate", Utils.getFormatDate(0));
						paramMap.put("bilPriceList[0].szGid", pe.getId());
						paramMap.put("bilPriceList[0].unitID", pe.getUnitID());
						
						CommunicationServer sc = new CommunicationServer(ctx,application.getClient(),server+action,paramMap,new Handler(), new GetProductPrice(pe));
						Thread t = new Thread(sc);
						t.start();
					}else{
						search(searchString);
					}
				}else{
					search(searchString);
				}
			} catch (JSONException e) {
				Toast.makeText(ctx, "服务器数据异常!", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
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
				
				WaiMaoActivity activity = (WaiMaoActivity) ctx;
				activity.editProduct(pe);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
