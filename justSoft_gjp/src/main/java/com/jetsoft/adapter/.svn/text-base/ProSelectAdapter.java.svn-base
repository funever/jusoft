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
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.*;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;

public class ProSelectAdapter extends BaseAdapter{
	
	private Context ctx;
	
	public List<ProductEntity> proList;
	
	public EditText searchText;
	
	public ImageView search;
	
	public String searchString;
	
	MyApplication application;
	
	String server ;
	
	HashMap<String,String> paramMap = new HashMap<String,String>();
	
	public ProSelectAdapter(Context ctx,List<ProductEntity> proList){
		this.ctx = ctx;
		this.proList = proList;
		this.application = (MyApplication) ((BaseActivity)ctx).getApplication();
	}

    public ProSelectAdapter(Context ctx,List<ProductEntity> proList,View searchView){
        this.ctx = ctx;
        this.proList = proList;
        this.application = (MyApplication) ((BaseActivity)ctx).getApplication();

        search = (ImageView) searchView.findViewById(R.id.pro_search);
        searchText = (EditText) searchView.findViewById(R.id.pro_search_text);
        search.setOnClickListener(new MyOnClickListener(searchText));
        ImageView pro_add = (ImageView) searchView
                .findViewById(R.id.pro_add);
        pro_add.setOnClickListener(new MyOnClickListener(searchText));
    }

	@Override
	public int getCount() {
		return proList.size();
	}

	@Override
	public Object getItem(int position) {
		return proList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.product_title,null);
            vh.prolist_spqm = ((TextView) convertView
                    .findViewById(R.id.prolist_spqm));
            vh.pro_danwei = ((TextView) convertView
                    .findViewById(R.id.pro_danwei));
            vh.pro_shuliang = ((TextView) convertView
                    .findViewById(R.id.pro_shuliang));
            vh.guige = ((TextView) convertView.findViewById(R.id.guige));
            vh.xinghao = ((TextView) convertView.findViewById(R.id.xinghao));
            vh.pro_je = ((TextView) convertView.findViewById(R.id.pro_je));
            vh.pro_fzsl = ((TextView) convertView.findViewById(R.id.pro_fzsl));
            vh.zp = (TextView) convertView.findViewById(R.id.zp);
            vh.pro_danjia = (TextView) convertView
                    .findViewById(R.id.pro_danjia);
            vh.rightArrow = (ImageView) convertView.findViewById(R.id.right_arrow);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
		ProductEntity product = proList.get(position);
        vh.rightArrow.setVisibility(View.VISIBLE);
		vh.setValue(product);
		return convertView;
	}
	
	class ViewHolder{
		TextView prolist_spqm;
		TextView pro_danwei;
		TextView pro_shuliang;
		TextView guige;
		TextView xinghao;
		TextView pro_je;
		TextView pro_fzsl;
        TextView zp;
		TextView pro_danjia;
        ImageView rightArrow;
		
		public void setValue(ProductEntity product){
			prolist_spqm.setText(product.getFullName());
			pro_danwei.setText(product.getUnit1());
			pro_shuliang.setText(product.getNumUnit1() + "");
			guige.setText(product.getStandard() == null ? "" : product
					.getStandard() + "");
			xinghao.setText(product.getType() == null ? "" : product.getType()
					+ "");
			MyApplication application = (MyApplication) ((BaseActivity) ctx)
					.getApplication();
			float je = 0;
			try {
				je = Float.parseFloat(product.getPriceRec())
						* product.getNumUnit1();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 辅助数量
			int ur0 = 0;
			int ur1 = 0;
			int ur2 = 0;
			try {
				ur0 = (int) Float.parseFloat(StaticValues.format(
						product.getuRate0(), 2));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				ur1 = (int) Float.parseFloat(StaticValues.format(
						product.getuRate1(), 2));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				ur2 = (int) Float.parseFloat(StaticValues.format(
						product.getuRate2(), 2));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String fzsl_str = getNumUnitFz2((float) product.getNum(),
					product.getUnitName0(), product.getUnitName1(),
					product.getUnitName2(), ur0, ur1, ur2);
			pro_fzsl.setText(fzsl_str);
			
			if (!product.getLargess().equals("0")
					&& !(ctx instanceof KuCunActivity)) {
                zp.setText("是");
				je = 0;
			}else{
                zp.setText("否");
            }
			if (application.getRightCost() == 1) {
				// 有权限时显示数字
				String sss = StaticValues.format(product.getPriceRec(),
						application.getPriceDigit());
				pro_danjia.setText(Float.parseFloat(sss) + "");
				pro_je.setText(StaticValues.format(je,
						application.getPriceDigit()));
			} else {
				// 没有权限，为一下几个订单样式时显示星号
				if (ctx instanceof TabHostActivity || ctx instanceof OrderActivity
						|| ctx instanceof ReturnOrderActivity
						|| ctx instanceof KuCunActivity) {
					pro_danjia.setText("*");
					pro_je.setText("*");
				} else {
					pro_danjia.setText(StaticValues.format(
							Float.parseFloat(product.getPriceRec()),
							application.getPriceDigit()));
					pro_je.setText(StaticValues.format(je,
							application.getPriceDigit()));
				}
			}
		}
	}
	
	public class MyOnClickListener implements OnClickListener{
		
		EditText searchET;
		
		public MyOnClickListener(EditText searchET){
			this.searchET = searchET;
		}

		@Override
		public void onClick(View v) {
			BaseActivity base = (BaseActivity) ctx;
			if(!base.isEdit){
				Toast.makeText(ctx, "该单据不能编辑，仅供浏览！", Toast.LENGTH_SHORT).show();
				return;
			}
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
		paramMap.put("cpage", "1");
		paramMap.put("szParid", "00000");
		paramMap.put("szSid", ((BaseActivity)ctx).ck_entity.getId());
		try{
			paramMap.put("szOperator", application.getUserEntity().getId());
		}catch(Exception e){
			Toast.makeText(ctx, "用户为空！", Toast.LENGTH_SHORT).show();
		}
		paramMap.put("szStr", searchString);
		paramMap.put("nBilType", ((BaseActivity)ctx).nBilType);
		String szUid = "";
		if(!(ctx instanceof KuCunActivity)){
			try{
				szUid = ((BaseActivity)ctx).dw_entity.getId();
				//调拨单没有选择单位
				if(szUid==null || "".equals(szUid)){
					Toast.makeText(ctx, "没有选择单位！", Toast.LENGTH_SHORT).show();
					return;
				}
			}catch(Exception e){
				Toast.makeText(ctx, "没有选择单位！", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		paramMap.put("szUid", szUid);
		SharedPreferences preferences = ctx.getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		//获取服务器地址
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, ctx.getString(R.string.server))+":"
				+ preferences.getString(StaticValues.PORT_FLAG, ctx.getString(R.string.port));
		
		Utils.showProgressDialog("正在加载中,请稍候……",ctx);
		
		CommunicationServer sc = new CommunicationServer(ctx,application.getClient(),server+action,paramMap,new Handler(), new EntityParseThread(searchString));
		Thread t = new Thread(sc);
		t.start();
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
		intent.putExtra("szSid", ctx instanceof KuCunActivity ? ((BaseActivity)ctx).fhck_entity.getId() : ((BaseActivity)ctx).ck_entity.getId());
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
						Utils.setProgressDialogMsg("正在获取价格数据!");
						CommunicationServer sc = new CommunicationServer(ctx,application.getClient(),server+action,paramMap,new Handler(), new GetProductPrice(pe));
						Thread t = new Thread(sc);
						t.start();
						return;
					}else{
						search(searchString);
					}
				}else{
					search(searchString);
				}
			} catch (JSONException e) {
				Toast.makeText(ctx, "服务器数据异常!", Toast.LENGTH_SHORT).show();
				BaseActivity activity = (BaseActivity) ctx;
				activity.setHibernation(false);
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	/**
	 * 获取辅助数量
	 * @param dNum 基本单位数量，即对应软件中QTY数据
	 * @param szUnitName0 基本单位名称，查询商品存储过程d_AD_Goods返回，字段UnitName0
	 * @param szUnitName1 辅助单位一名称，查询商品存储过程d_AD_Goods返回，字段UnitName1
	 * @param szUnitName2 辅助单位二名称，查询商品存储过程d_AD_Goods返回，字段UnitName2
	 * @param dURate0 基本单位单位关系，查询商品存储过程d_AD_Goods返回，字段URate0
	 * @param URate1 辅助单位一单位关系，查询商品存储过程d_AD_Goods返回，字段URate1
	 * @param URate2 辅助单位二单位关系，查询商品存储过程d_AD_Goods返回，字段URate2
	 * @return 为换算后的辅助数量
	 */
	public static String getNumUnitFz2(float dNum,
											   String szUnitName0,
											   String szUnitName1, 
											   String szUnitName2,
											   float dURate0,
											   float URate1,
											   float URate2){
		String result = "";
		int dNumUnit1,dNumUnit2;
		float dSmallNum;
		String szStr = "";
		dNumUnit1 = 0;
		dNumUnit2 = 0;
		dSmallNum = dNum;
		if(dSmallNum == 0){
			result = szStr; 
		}else{
			if(URate2 > 0 && !szUnitName2.equals("")){
				dNumUnit2 = (int) (dSmallNum/URate2);
				dSmallNum  = dSmallNum - dNumUnit2 * URate2;
			}
			if(URate1 > 0 && !szUnitName1.equals("")){
				dNumUnit1 = (int) (dSmallNum/URate1);
		        dSmallNum = dSmallNum - dNumUnit1 * URate1;
			}
		}
		if(dNumUnit2>0){
			szStr = dNumUnit2+szUnitName2;
		} 
		if(dNumUnit1>0){
			szStr += (dNumUnit1+szUnitName1);
		}
		if(dSmallNum>0){
			szStr += (StaticValues.format(dSmallNum, 2)+szUnitName0);
		}
		result = szStr;
		return result;
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
				proList.add(pe);
				notifyDataSetChanged();
				if(ctx instanceof BaseActivity){
					((BaseActivity)ctx).updateProNum();
				}
				return;
			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				Utils.dismissProgressDialog();
				BaseActivity activity = (BaseActivity) ctx;
				activity.setHibernation(false);
			}
		}
	}
}
