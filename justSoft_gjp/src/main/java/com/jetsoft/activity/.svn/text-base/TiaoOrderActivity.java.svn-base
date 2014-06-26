/**
 * 创建日期 2013-4-10
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jetsoft.R;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.OrderEntity;
import com.jetsoft.entity.ProductEntity;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class TiaoOrderActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	
	Button search;
	
	ViewFlipper vf;
	
	PullToRefreshListView refreshList;
	
	ListView orderlist;
	
	List<OrderEntity> oeList = new LinkedList<OrderEntity>();
	
	OrderAdapter oAdapter;
	
	ProductAdapter pa;
	
	ListView proList;
	
	ArrayList<ProductEntity> peList = new ArrayList<ProductEntity>();
	
	String nBiltype;
	
	EditText dw_edit,or_edit,pr_edit;
	
	private ImageView dw_add,or_add;
	
	TextView sDate,eDate;
	
	RelativeLayout sdate_action,edate_action;
	
	private OrderEntity selectOrder;
	
	ArrayList<ProductEntity> selectPro = new ArrayList<ProductEntity>();
	
	int page=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tiao_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("调订单");
		
		dw_entity = new DanWeiEntity();
		
		nBiltype = getIntent().getStringExtra("nBiltype");
		selectPro = (ArrayList<ProductEntity>) getIntent().getSerializableExtra("products");
		vf = (ViewFlipper) findViewById(R.id.vf);
		vf.setDisplayedChild(0);
		
		dw_edit = (EditText) findViewById(R.id.wldw_edit);
		dw_add = (ImageView) findViewById(R.id.dw_add);
		dw_add.setOnClickListener(this);
		or_edit = (EditText) findViewById(R.id.order_edit);
		or_add = (ImageView) findViewById(R.id.or_add);
//		or_add.setOnClickListener(this);
		
		pr_edit = (EditText) findViewById(R.id.pr_edit);
		
		sDate = (TextView) findViewById(R.id.sdate_edit);
		eDate = (TextView) findViewById(R.id.edate_edit);
		sDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		eDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		sdate_action = (RelativeLayout) findViewById(R.id.sdate_action);
		edate_action = (RelativeLayout) findViewById(R.id.edate_action);
		sdate_action.setOnClickListener(this);
		edate_action.setOnClickListener(this);
		
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);
		
		refreshList = (PullToRefreshListView) findViewById(R.id.orderlist);
		orderlist = refreshList.getRefreshableView();
		
		refreshList.setMode(Mode.PULL_FROM_END);
		refreshList.getLoadingLayoutProxy().setReleaseLabel("上拉加载下一页");
		refreshList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});
		
		oAdapter = new OrderAdapter();
		orderlist.setAdapter(oAdapter);
		orderlist.setOnItemClickListener(this);
		
		proList = (ListView) findViewById(R.id.prolist);
		pa = new ProductAdapter();
		proList.setAdapter(pa);
		proList.setOnItemClickListener(this);
		
		/**
		 * 注册到蓝牙扫描填值类
		 */
		sv.registActivity(this);
	}
    private static final int MENU_SAVE = 10002;
    MenuItem saveItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        saveItem = menu.add(0,MENU_SAVE,0,"确定");
        saveItem.setIcon(android.R.drawable.ic_menu_save);
        saveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(vf.getDisplayedChild()==0){
                this.finish();
            }else if(vf.getDisplayedChild()>0){
                vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                vf.setDisplayedChild(vf.getDisplayedChild()-1);
                return false;
            }
        }else if(item.getItemId() ==MENU_SAVE ){
            if(vf.getDisplayedChild()==0 || vf.getDisplayedChild()==1){
                Toast.makeText(this,"请选择单据！",2000).show();
                return false;
            }
            Intent intent = new Intent();
            ArrayList<ProductEntity> selelct = new ArrayList<ProductEntity>();
            for(int i=0;i<peList.size();i++){
                ProductEntity productEntity = peList.get(i);
                if(productEntity.getYanhuo()>0){
                    selelct.add(productEntity);
                }
            }
            intent.putExtra("entity", selelct);
            intent.putExtra("orderEntity", selectOrder);
            setResult(RESULT_OK, intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	public void onFocusChange(View v, boolean hasFocus) {
	}

	@Override
	public void updateProNum() {
	}

	@Override
	public boolean validate() {
		return false;
	}

	@Override
	public void setScanValue(String value) {
		setHibernation(true);
		String action = getString(R.string.product_action);
		/**
		 * 请求的url中的参数
		 */
		HashMap<String,String> paramMap = new HashMap<String,String>();
		
		paramMap.clear();
		//cMode=Z&szParid=00000&szSid=&szOperator=00000&szStr=
		paramMap.put("cMode", "Z");
		paramMap.put("szParid", "00000");
		paramMap.put("szSid", "");
		paramMap.put("szOperator", application.getUserEntity().getId());
		paramMap.put("szStr", value);
		paramMap.put("nBilType", "0");
		paramMap.put("szUid", "");
		
		//获取服务器地址
		CommunicationServer sc = new CommunicationServer(this,application.getClient(),server+action,paramMap,new Handler(), new EntityParseThread());
		Thread t = new Thread(sc);
		t.start();
	}
	
	/**
	 * 扫描后商品处理
	 * 
	 *
	 * @version 1.0
	 * @author Administrator
	 */
	public class EntityParseThread extends ExcuteThread {

		public void run() {
			try {
				if(getJsonString().equals("[]")){
					Toast.makeText(TiaoOrderActivity.this, "没有商品数据!", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray ja = new JSONArray(getJsonString());
				// 如果搜索的结果只有一个
				if (ja.length() == 1) {
					JSONObject jo = ja.getJSONObject(0);
					String pro_name = jo.getString("fullName");
					String id = jo.getString("id");
					boolean flag = false;
					for(ProductEntity pe : peList){
						if(pe.getId().equals(id)){
							if(pe.getYanhuo()<pe.getdNumUnit1()){
								pe.setYanhuo(pe.getYanhuo()+1);
								pe.setWeiyan(pe.getdNumUnit1()-pe.getYanhuo());
							}else{
								Toast.makeText(TiaoOrderActivity.this, pe.getFullName()+"的验货数不能超过订货数", Toast.LENGTH_SHORT).show();
								playMedia(1000);
								return;
							}
							flag = true;
							break;
						}
					}
					if(flag){
//						pr_edit.setText(pro_name);
						pa.notifyDataSetChanged();
					}else{
						Toast.makeText(TiaoOrderActivity.this, "扫描的商品不在选取的订单列表中", Toast.LENGTH_SHORT).show();
						playMedia(10);
					}
				}else{
					Toast.makeText(TiaoOrderActivity.this, "该条码扫描有多条商品数据！", Toast.LENGTH_SHORT).show();
					playMedia(10);
				}
			} catch (Exception e) {
				Toast.makeText(TiaoOrderActivity.this, "扫描商品数据错误", Toast.LENGTH_SHORT).show();
			}finally{
				setHibernation(false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v == search){
			String action = getString(R.string.order_action);
			
			params.put("cMode", "T");
			params.put("nBiltype", nBiltype+"");
			if(dw_entity==null || dw_entity.getFullName()==null || dw_entity.getFullName().equals("")){
				params.put("szUID", "");
			}else{
				params.put("szUID", dw_entity.getId());
			}
			if(selectOrder==null){
				params.put("szNumber", "");
			}else{
				params.put("szNumber", selectOrder.getNumber());
			}
			page = 1;
			params.put("szDateBegin", sDate.getText().toString());
			params.put("szDateEnd", eDate.getText().toString());
			params.put("nBilID", "0");
			params.put("cpage", page+++"");
			params.put("szOperator", application.getUserEntity().getId());
			oeList.clear();
			Utils.showProgressDialog("正在加载订单……",this);
			CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action,params, handler, new SubmitExcute(SubmitExcute.ORDER));
			Thread t = new Thread(cs);
			t.start();
		}else if(v == dw_add){
			selectEntity(EntityListActivity.DW_RE_CODE,dw_edit.getText().toString());
		}else if(v == sdate_action){
			showLuDanRiQi(sDate);
		}else if(v == edate_action){
			showLuDanRiQi(eDate);
		}else if(v == or_add){
			selectEntity(EntityListActivity.OR_RE_CODE,or_edit.getText().toString());
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == EntityListActivity.DW_RE_CODE){
			dw_entity = (DanWeiEntity) data.getSerializableExtra("entity");
			dw_edit.setText(dw_entity.getFullName());
		}else if(requestCode == EntityListActivity.OR_RE_CODE){
			selectOrder = (OrderEntity) data.getSerializableExtra("entity");
			System.out.println("selectOrder.number="+selectOrder.getNumber()+","+selectOrder.getFullName());
			or_edit.setText(selectOrder.getNumber());
		}
	}
	
	/**
	 * 跳转到选择多列项的页面
	 */
	public void selectEntity(int requestCode,String searchString){
		Intent intent = new Intent();
		intent.setClass(this, EntityListActivity.class);
		//供应商，客户标示。进货订单，进货单，进货退货单传0，销售订单，销售单，销售退货单传1，其他单据传2
		if(requestCode == EntityListActivity.DW_RE_CODE){
			if(nBiltype.equals(StaticValues.ORDER_IN_BILTYPE)){
				intent.putExtra("isClient", "0");
			}else if(nBiltype.equals(StaticValues.SALE_IN_BILTYPE)){
				intent.putExtra("isClient", "1");
			}
		}
		if(requestCode == EntityListActivity.OR_RE_CODE){
			intent.putExtra("nBiltype", ""+nBiltype);
		}
		intent.putExtra("type", requestCode);
		intent.putExtra("search", searchString);
		startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 显示选择录单日期的dialog
	 */
	public void showLuDanRiQi(final TextView tv){
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
	
	class SubmitExcute extends ExcuteThread{
		
		public static final int ORDER = 1;
		
		public static final int PRODUCT = 2;
		
		private int type;
		
		public SubmitExcute(int type){
			this.type = type;
		}
		 
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				if(ja.length()==0){
					Toast.makeText(TiaoOrderActivity.this, "没有更多的数据了!", Toast.LENGTH_SHORT).show();
					refreshList.onRefreshComplete();
					refreshList.setMode(Mode.DISABLED);
					return;
				}
				if(type == ORDER){
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						OrderEntity oe = new OrderEntity();
						oe.setDate(jo.getString("date"));
						oe.setNumber(jo.getString("number"));
						oe.setuFullName(jo.getString("uFullName"));
						oe.setBilID(jo.getString("bilID"));
						oe.setUid(jo.getString("uID"));
						oe.setUPersonCode(jo.getString("uPersonCode"));
						oe.setSid(jo.getString("sID"));
						oe.setSPersonCode(jo.getString("sPersonCode"));
						oe.setsFullName(jo.getString("sFullName"));
						oe.seteFullName(jo.getString("eFullName"));
						oe.seteID(jo.getString("eID"));
						oeList.add(oe);
					}
					vf.setInAnimation(AnimationUtils.loadAnimation(TiaoOrderActivity.this, R.anim.push_left_in));
					vf.setOutAnimation(AnimationUtils.loadAnimation(TiaoOrderActivity.this, R.anim.push_left_out));
					vf.setDisplayedChild(1);
					oAdapter.notifyDataSetChanged();
				}else if(type == PRODUCT){
						/*
						"amount":"10.000000"
						,"bID":"0000100001"
						,"bilCode":"156"
						,"bilID":"96"
						,"cosePrice":"0E-8"
						,"costAmount":"0.000000"
						,"costKind":"0"
						,"dNumUnit1":"1.0000000000000000000"
						,"date":"2013-04-10"
						,"disCount":"0.700000"
						,"disCountPrice":"7.00000000"
						,"eID":"00001"
						,"gFullName":"商品1"
						,"gID":"00001"
						,"num":1
						,"period":"2"
						,"price":"10.00000000"
						,"priceUnit":"10.000000"
						,"sID":"00001"
						,"tax":"0.000000"
						,"taxPrice":"7.00000000"
						,"uID":"00004"
						,"uRate":"1.00000000"
						,"unit":"0"
						*/
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						ProductEntity pe = new ProductEntity();
						Utils.setObjectValue(pe, jo);
						pe.setdNumUnit1((int)Float.parseFloat(jo.getString("dNumUnit1")));
						pe.setPriceRec(jo.getString("priceUnit"));
						pe.setDisRec(jo.getString("disCount"));
						pe.setTax(Float.parseFloat(jo.getString("tax")));
						pe.setId(jo.getString("gID"));
						pe.setYanhuo(0);
						pe.setOrderPro(true);
						pe.setPromoID("0");
						pe.setFullName(pe.getgFullName());
						pe.setMemo(selectOrder!=null?selectOrder.getNumber():"");
						for(ProductEntity pro : selectPro){
							if(pro.getId().equals(pe.getId())){
								pe.setYanhuo((int) pro.getNum());
								continue;
							}
						}
						if(pe.getYanhuo()>pe.getdNumUnit1()){
							//验货数多余了订单数
							Toast.makeText(TiaoOrderActivity.this, "解析订单商品时数据错误！", Toast.LENGTH_SHORT).show();
							continue;
						}
						pe.setWeiyan(pe.getdNumUnit1()-pe.getYanhuo());
						peList.add(pe);
					}
					vf.setDisplayedChild(2);
					pa.notifyDataSetChanged();
				}
			}catch(Exception e){
				e.printStackTrace();
				Toast.makeText(TiaoOrderActivity.this, "解析订单商品时数据错误！", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	class OrderAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return oeList.size();
		}
		@Override
		public Object getItem(int position) {
			return oeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(TiaoOrderActivity.this).inflate(R.layout.order_list_row, null);
            TextView order_id = (TextView) view.findViewById(R.id.order_id);
            TextView order_dw = (TextView) view.findViewById(R.id.order_dw);
            TextView order_date = (TextView) view.findViewById(R.id.order_date);

            order_id.setText(oeList.get(position).getNumber());
            order_dw.setText(oeList.get(position).getuFullName());
            order_date.setText(oeList.get(position).getDate());
            return view;
        }

    }

	@Override
	public void onItemClick(AdapterView<?> listView, View arg1, int arg2, long arg3) {
		if(listView == orderlist){
			OrderEntity orderEntity = oeList.get(arg2-1);
			selectOrder = orderEntity;
			String action = getString(R.string.order_action);
			peList.clear();
			
			params.put("cMode", "D");
			params.put("nBiltype", ""+nBiltype);
			params.put("szUID", orderEntity.getUid());
			params.put("szNumber", oeList.get(arg2-1).getNumber());
			params.put("szDateBegin", sDate.getText().toString());
			params.put("szDateEnd", eDate.getText().toString());
			params.put("nBilID", oeList.get(arg2-1).getBilID());
			params.put("szOperator", application.getUserEntity().getId());
			
			Utils.showProgressDialog("正在加载订单商品……",this);
			CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action,params, handler, new SubmitExcute(SubmitExcute.PRODUCT));
			Thread t = new Thread(cs);
			t.start();
		}else if(listView == proList){
			if(arg2<1){
				return;
			}
			editProductNum(peList.get(arg2-1));
		}
	}
	
	class ProductAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return peList.size()+1;
		}

		@Override
		public Object getItem(int position) {
			return peList.get(position-1);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(TiaoOrderActivity.this).inflate(R.layout.tiao_pro_list_row, null);
			if(position>0){
				((TextView)view.findViewById(R.id.pro_name)).setText(peList.get(position-1).getgFullName());
//				((TextView)view.findViewById(R.id.pro_amount)).setText(peList.get(position-1).getAmount()+"");
				//订货数
				((TextView)view.findViewById(R.id.pro_num2)).setText(peList.get(position-1).getdNumUnit1()+"");
				//验货数
				((TextView)view.findViewById(R.id.pro_num3)).setText(peList.get(position-1).getYanhuo()+"");
				//未验数
				((TextView)view.findViewById(R.id.pro_num4)).setText(peList.get(position-1).getWeiyan()+"");
			}
			return view;
		}
		
	}
	
	@Override
	public void onBackPressed() {
        if(vf.getDisplayedChild()==0){
            this.finish();
        }else if(vf.getDisplayedChild()>0){
            vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
            vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
            vf.setDisplayedChild(vf.getDisplayedChild()-1);
        }
	}
	
	public void editProductNum(final ProductEntity pe){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(pe.getFullName());
		View view = getLayoutInflater().inflate(R.layout.product_yanhuo, null);
		builder.setView(view);
		final EditText pro_num = (EditText) view.findViewById(R.id.pro_num);
		pro_num.setText("0");
		EditText pro_weiyan = (EditText) view.findViewById(R.id.pro_weiyan);
		pro_weiyan.setText(pe.getWeiyan()+"");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				float yanhuo = 0;
				try{
					yanhuo = Float.parseFloat(pro_num.getText().toString());
				}catch(Exception e){
					yanhuo = 0;
				}
				if(yanhuo>pe.getdNumUnit1()){
					Toast.makeText(TiaoOrderActivity.this, pe.getFullName()+"的验货数不能超过订货数", Toast.LENGTH_SHORT).show();
					pro_num.setText(pe.getdNumUnit1()+"");
					playMedia(1000);
					return;
				}
				pe.setYanhuo(yanhuo);
				int urate = 1;
				try{
					urate = Integer.parseInt(pe.getuRate0());
				}catch(Exception e){
					urate = 1;
				}
				pe.setNum(yanhuo*urate);
				pe.setNumUnit1(yanhuo);
				pe.setWeiyan(pe.getdNumUnit1()-pe.getYanhuo());
				pa.notifyDataSetChanged();
				dialog.dismiss();
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
	
	@Override
	public boolean canScan() {
		return true;
	}

	@Override
	public void disposeLastNextData(TableEntity te) {
		
	}
	
	@Override
	public String getNumber() {
		return "";
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... arg0) {
			String action = getString(R.string.order_action);
			params.put("cpage", (page++)+"");
			CommunicationServer cs = new CommunicationServer(TiaoOrderActivity.this, application.getClient(), server+action,params, handler, new SubmitExcute(SubmitExcute.ORDER));
			Thread t = new Thread(cs);
			t.start();
			return null;
		}
	}
}
