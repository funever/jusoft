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
package com.jetsoft.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jetsoft.R;
import com.jetsoft.adapter.EntityAdapter;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.AreaEntity;
import com.jetsoft.entity.BaseEntity;
import com.jetsoft.entity.BuMenEntity;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.JingShouRenEntity;
import com.jetsoft.entity.OrderEntity;
import com.jetsoft.entity.PriceEntity;
import com.jetsoft.entity.ProductEntity;
import com.jetsoft.entity.UserEntity;
import com.jetsoft.entity.ZhangHuEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class EntityListActivity extends SherlockActivity implements OnItemClickListener{
	
	PullToRefreshListView refreshList;
	
	ListView entity_List;
	/**
	 * 查询到的所有的商品
	 */
	private List<BaseEntity> entitys;
	/**
	 * 返回码
	 * 
	 * 
	 */
	public static final int PR_RE_CODE = 1;
	public static final int DW_RE_CODE = 2;
	public static final int JSR_RE_CODE = 3;
	public static final int CK_RE_CODE = 4;
	public static final int BM_RE_CODE = 5;
	public static final int ZH_RE_CODE = 6;
	public static final int EDIT_PRODUCT = 7;
	public static final int DELETE_PRODUCT = 8;
	public static final int WAIMAO_ADD_PRODUCT = 9;
	public static final int OR_RE_CODE = 10;
	public static final int CAMREA_RE = 11;
	public static final int AREA_RE_CODE = 12;
	public static final int PRICE_RE_CODE = 13;
	public static final int COPY_PRODUCT = 14;
	
	int type;
	
	public String server;
	
	public Handler handler = new Handler();
	
	public EntityAdapter ea;
	/**
	 * 请求的url中的参数
	 */
	public HashMap<String,String> paramMap = new HashMap<String,String>();
		
	/**
	 * 请求的action路径
	 */
	public String action;
	
	MyApplication application;
	
	//public BaseEntity lastEntity = null;
	
	public LinkedList<BaseEntity> lastEntitys = new LinkedList<BaseEntity>();
	
//	private TextView list_progressing_title;
	/**
	 * 是否是盘点
	 */
	private boolean pro_pandian = false;
	/**
	 * 供应商，客户标示。进货订单，进货单，进货退货单传0，销售订单，销售单，销售退货单传1，其他单据传2
	 */
	private String isClient = "2";
	
	private String defautTitle = "";
	
	boolean replace_pro = false;
	
	int page = 1;
	
	boolean havePage = true;

    MenuItem add_dw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
				+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
		action = getString(R.string.entity_action);
		application = (MyApplication) getApplication();
		initParam();
		//初始化url的参数map
		setContentView(R.layout.search_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		pro_pandian = getIntent().getBooleanExtra("pandian", false);
		replace_pro = getIntent().getBooleanExtra("replace", false);
//		list_progressing_title = (TextView) findViewById(R.id.list_progressing_title);
		/**
		 * 查看是什么类型的列表
		 */
		type = getIntent().getIntExtra("type", DW_RE_CODE);
		String search = getIntent().getStringExtra("search");
		
		refreshList = (PullToRefreshListView) findViewById(R.id.entity_list);
		entity_List = refreshList.getRefreshableView();
		refreshList.setScrollingWhileRefreshingEnabled(false);
		
		entitys = new LinkedList<BaseEntity>();
		
		ea = new EntityAdapter(this, entitys);
		ea.check_no_show = pro_pandian;
		entity_List.setAdapter(ea);
		
		entity_List.setOnItemClickListener(this);
		//添加一个根的
		lastEntitys.add(new BaseEntity());
		
		/**
		 * 获取数据
		 */
		switch (type) {
		case DW_RE_CODE:
			paramMap.put("type", "1");
			defautTitle = "选择来往单位";
			isClient = getIntent().getStringExtra("isClient");
			if(isClient == null || "".equals(isClient)){
				isClient = "2";
			}
			paramMap.put("isClient", isClient);
			break;
		case JSR_RE_CODE:
			paramMap.put("type", "2");
			defautTitle = "选择经手人";
			break;
		case CK_RE_CODE:
			paramMap.put("type", "4");
			defautTitle = "选择仓库";
			break;
		case BM_RE_CODE:
			paramMap.put("type", "3");
			defautTitle = "选择部门";
			break;
		case ZH_RE_CODE:
			//http://192.168.0.21:8080/AndroidWEB/bursary.do?cMode=1&szParid=00000&operator=00000&szStr=&cKind=Z&nBiltype=4
			action = getString(R.string.fkzh_action);
			paramMap.clear();
			//构建请求付款账户的参数
			paramMap.put("cMode", "1");
			paramMap.put("szParid", "00000");
			paramMap.put("operator", "00000");
			paramMap.put("cKind", "Z");
			String nBiltype = getIntent().getStringExtra("nBiltype");
			if(nBiltype!=null && StaticValues.SK_BILTYPE.equals(nBiltype)){
				paramMap.put("nBiltype", nBiltype);
				defautTitle = "选择收款账户";
			}else{
				paramMap.put("nBiltype", StaticValues.FK_BILTYPE);
				defautTitle = "选择付款账户";
			}
			paramMap.put("szStr", "");
			break;
		case PR_RE_CODE:
			paramMap.clear();
			//cMode=Z&szParid=00000&szSid=&szOperator=00000&szStr=
			paramMap.put("cMode", "Z");
			paramMap.put("szParid", "00000");
			paramMap.put("szSid", getIntent().getStringExtra("szSid"));
			UserEntity entity = application.getUserEntity();
			String userId = "";
			if(entity!=null){
				userId = entity.getId();
			}
			paramMap.put("szOperator", userId);
			if(replace_pro){
				action = getString(R.string.product_replace_action);
				paramMap.put("szGid", getIntent().getStringExtra("szGid"));
			}else{
				action = getString(R.string.product_action);
				paramMap.put("szStr", "");
			}
			
			String nBilType = getIntent().getStringExtra("nBilType");
			if(nBilType==null || "".endsWith(nBilType)){
				nBilType = "0";
			}
			paramMap.put("nBilType", nBilType);
			String szUid = getIntent().getStringExtra("szUid");
			if(szUid != null){
				paramMap.put("szUid", szUid);
			}
			paramMap.put("szStr", "");
			
			defautTitle = "选择商品";
			break;
		case OR_RE_CODE:
			action = getString(R.string.order_action);
			paramMap.clear();
			
			paramMap.put("cMode", "T");
			paramMap.put("nBiltype", getIntent().getStringExtra("nBiltype"));
			paramMap.put("szUId", "");
			paramMap.put("szNumber", "");
			paramMap.put("szDateBegin", "2000-01-01");
			paramMap.put("szDateEnd", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			paramMap.put("nBilid", "0");
			paramMap.put("szOperator", application.getUserEntity().getId());
			break;
		case AREA_RE_CODE:
			defautTitle = "选择地区";
			action = getString(R.string.selectarea_action);
			paramMap.clear();
			paramMap.put("cMode", "Z");
			paramMap.put("szParID", "00000");
			paramMap.put("szOperator", application.getUserEntity().getId());
			break;
		case PRICE_RE_CODE:
			defautTitle = "选择价格";
			action = getString(R.string.selectBPrice_action);
			paramMap.clear();
			paramMap.put("cMode", "Z");
			paramMap.put("szOperator", application.getUserEntity().getId());
			havePage = false;
			break;
		default:
			break;
		}
		if(havePage){
			paramMap.put("cpage", (page++)+"");
			refreshList.setMode(Mode.PULL_FROM_END);
			refreshList.getLoadingLayoutProxy().setReleaseLabel("上拉加载下一页");
			refreshList.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					new GetDataTask().execute();
				}
			});
		}else{
			refreshList.setMode(Mode.DISABLED);
		}
		if(search != null){
			paramMap.put("szStr", search);
		}
		Utils.showProgressDialog("正在加载数据……", this);
		CommunicationServer sc = new CommunicationServer(this,application.getClient(),server+action,paramMap,handler, new EntityParseThread(false));
		Thread t = new Thread(sc);
		t.start();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

    private  static final int ADD_DW_ID = 10001;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        add_dw = menu.add(0,ADD_DW_ID,0,"添加");
        add_dw.setIcon(android.R.drawable.ic_menu_add);
        add_dw.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        add_dw.setVisible(false);

        switch (type){
            case DW_RE_CODE:
                add_dw.setVisible(true);
                break;
            case PR_RE_CODE:
                add_dw.setTitle("选择");
                if(!pro_pandian){
                    add_dw.setVisible(true);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            //如果父列表有数据
            if(lastEntitys.size()>1){
                String parentId = lastEntitys.removeLast().getParID();
                ea.checkedMap.clear();
                ea.checkShow.clear();
                //设置参数后重新获取
                paramMap.put("szParid", parentId);
                page = 1;
                paramMap.put("cpage", page+"");
                refreshList.setMode(Mode.PULL_FROM_END);
                Utils.showProgressDialog("正在获取数据，请稍候……",this);
                /**
                 * 重新请求数据
                 */
                CommunicationServer sc = new CommunicationServer(this,application.getClient(),server+action,paramMap,handler, new EntityParseThread(false));
                Thread t = new Thread(sc);
                t.start();
            }else{
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                this.finish();
                return true;
            }
        }else if(item.getItemId() ==ADD_DW_ID){
            if(type == DW_RE_CODE){
                //新增往来单位
                Intent intent = new Intent();
//				intent.putExtra("isClient", isClient);
                intent.putExtra("parId", entitys.get(0).getParID());
                intent.setClass(this, AddWLDWActivity.class);
                startActivity(intent);
            }else if(type == PR_RE_CODE){
                Utils.showProgressDialog("正在请求商品价格数据……", this);
                paramMap.put("szDate", Utils.getFormatDate(0));
                ArrayList<BaseEntity> selectEntitys = new ArrayList<BaseEntity>();
                int index=0;
                for(Entry<Integer, Boolean> entry : ea.checkedMap.entrySet()){
                    if(entry.getValue()){
                        selectEntitys.add(entitys.get(entry.getKey()));
                        paramMap.put("bilPriceList["+index+"].szGid", entitys.get(entry.getKey()).getId());
                        paramMap.put("bilPriceList["+index+"].unitID", ((ProductEntity)entitys.get(entry.getKey())).getUnitID());
                        index++;
                    }
                }
                /**
                 * @cMode：直接传参数“Z”
                 @szGid：商品ID，选完商品后传商品GID
                 @szSid：仓库ID，开单没选仓库就传空
                 @szUid：往来单位ID，开单没选往来单位就传空
                 @nBiltype：单据类型。
                 @nUnitID：单位标示，选商品存储过程返回字段UnitID的值。
                 @szDate：当前单据的制单日期。
                 @szOperator：当前操作员ID。
                 @szGid：商品ID，选完商品后传商品GID？
                 */
                action = getString(R.string.product_price);
                CommunicationServer sc = new CommunicationServer(this,application.getClient(),server+action,paramMap,new Handler(), new GetProductPrice(selectEntitys));
                Thread t = new Thread(sc);
                t.start();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			//反射获取entity的sonNum属性
			BaseEntity entity = entitys.get(arg2-1);
			/**
			 * 获取属性值，判断是否还有子列表
			 */
			String num = entity.getSonNum();
			int num_int = 0;
			try{
				num_int = Integer.parseInt(num);
			}catch(Exception e){
				e.printStackTrace();
			}
			//有子项
			if(num_int>0 && !(entity instanceof PriceEntity)){
				page = 1;
				refreshList.setMode(Mode.PULL_FROM_END);
				ea.checkedMap.clear();
				ea.checkShow.clear();
				//设置参数后重新获取
				paramMap.put("szParid", entity.getId());
				paramMap.put("cpage", (page++)+"");
				Utils.showProgressDialog("正在获取数据，请稍候……",this);
				/**
				 * 重新请求数据
				 */
				CommunicationServer sc = new CommunicationServer(this,application.getClient(),server+action,paramMap,handler, new EntityParseThread(false));
				Thread t = new Thread(sc);
				t.start();
				if(!lastEntitys.getLast().getParID().equalsIgnoreCase(entity.getParID())){
					lastEntitys.addLast(entity);
				}else{
					lastEntitys.removeLast();
					lastEntitys.addLast(entity);
				}
			}else{
				//选取成功跳转
				if(!(entity instanceof ProductEntity)){
					Intent intent = new Intent();
					intent.putExtra("entity", entity);
					setResult(RESULT_OK, intent);
					this.finish();
					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
					return;
				}
				Utils.showProgressDialog("正在请求商品价格数据……", this);
				ProductEntity productEntity = (ProductEntity) entity;
				paramMap.put("szDate", Utils.getFormatDate(0));
				paramMap.put("bilPriceList[0].szGid", productEntity.getId());
				paramMap.put("bilPriceList[0].unitID", productEntity.getUnitID());
				/**
				 * @cMode：直接传参数“Z”
					@szGid：商品ID，选完商品后传商品GID
					@szSid：仓库ID，开单没选仓库就传空
					@szUid：往来单位ID，开单没选往来单位就传空
					@nBiltype：单据类型。
					@nUnitID：单位标示，选商品存储过程返回字段UnitID的值。
					@szDate：当前单据的制单日期。
					@szOperator：当前操作员ID。
					@szGid：商品ID，选完商品后传商品GID？
				 */
				action = getString(R.string.product_price);
				ArrayList<BaseEntity> arrayList = new ArrayList<BaseEntity>();
				arrayList.add(entity);
				CommunicationServer sc = new CommunicationServer(this,application.getClient(),server+action,paramMap,new Handler(), new GetProductPrice(arrayList));
				Thread t = new Thread(sc);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
	}

	class GetProductPrice extends ExcuteThread{
		
		ArrayList<BaseEntity> selectEntitys;
		
		public GetProductPrice(ArrayList<BaseEntity> selectEntitys){
			this.selectEntitys = selectEntitys;
		}
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				if(ja.length()!=selectEntitys.size()){
					Toast.makeText(EntityListActivity.this, "返回商品价格信息数据错误！", Toast.LENGTH_SHORT).show();
					Utils.dismissProgressDialog();
					return;
				}
				for(int i=0;i<selectEntitys.size();i++){
					ProductEntity pe = (ProductEntity) selectEntitys.get(i);
					JSONObject jo = ja.getJSONObject(i);
					if(jo!=null){
						/**
						 * 不更新unitNameList的数据
						 */
						Utils.setObjectValue(pe, jo);
					}
					pe.setNum(pe.getNum()*Float.parseFloat(pe.getuRateBil()));
					/**
					 * 克隆
					 */
					ProductEntity cloneProduct = pe.clone();
					cloneProduct.setCopy(true);
					pe.setCloneProduct(cloneProduct);
				}
				Intent intent = new Intent();
				intent.putExtra("entity", selectEntitys);
				setResult(RESULT_OK, intent);
				EntityListActivity.this.finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
 	
	
	
	class EntityParseThread extends ExcuteThread{
		
		private boolean isRefresh;
		
		public EntityParseThread(boolean isRefresh){
			this.isRefresh = isRefresh;
		}

		public void run(){
			try {
				JSONArray ja = new JSONArray(getJsonString());
				if(!isRefresh){
					if(lastEntitys.size()>1){
						//设置标题
						String nameValue = lastEntitys.getLast().getFullName();
						getSupportActionBar().setTitle(nameValue);
					}else{
                        getSupportActionBar().setTitle(defautTitle);
					}
					entitys.clear();
				}else{
					if(ja.length()==0){
						Toast.makeText(EntityListActivity.this, "没有更多的数据了!", Toast.LENGTH_SHORT).show();
						refreshList.onRefreshComplete();
						refreshList.setMode(Mode.DISABLED);
						return;
					}
				}
				switch (type) {
				case DW_RE_CODE:
					/*{
					 * "_py":"WL1",
					 * "fullName":"往来1",
					 * "id":"00001",
					 * "parID":"00000",
					 * "personCode":"1",
					 * "sonNum":0
					 * }
					*/
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						DanWeiEntity dwe = new DanWeiEntity();
						dwe.set_py(jo.getString("_py"));
						dwe.setFullName(jo.getString("fullName"));
						dwe.setPersonCode(jo.getString("personCode"));
						dwe.setId(jo.getString("id"));
						dwe.setSonNum(jo.getString("sonNum"));
						dwe.setParID(jo.getString("parID"));
						entitys.add(dwe);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				case CK_RE_CODE:
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						CangKuEntity dwe = new CangKuEntity();
						dwe.set_py(jo.getString("_py"));
						dwe.setFullName(jo.getString("fullName"));
						dwe.setPersonCode(jo.getString("personCode"));
						dwe.setId(jo.getString("id"));
						dwe.setSonNum(jo.getString("sonNum"));
						dwe.setParID(jo.getString("parID"));
						entitys.add(dwe);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				case BM_RE_CODE:
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						BuMenEntity dwe = new BuMenEntity();
						dwe.set_py(jo.getString("_py"));
						dwe.setFullName(jo.getString("fullName"));
						dwe.setPersonCode(jo.getString("personCode"));
						dwe.setId(jo.getString("id"));
						dwe.setSonNum(jo.getString("sonNum"));
						dwe.setParID(jo.getString("parID"));
						entitys.add(dwe);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				case JSR_RE_CODE:
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						JingShouRenEntity dwe = new JingShouRenEntity();
						dwe.set_py(jo.getString("_py"));
						dwe.setFullName(jo.getString("fullName"));
						dwe.setPersonCode(jo.getString("personCode"));
						dwe.setId(jo.getString("id"));
						dwe.setSonNum(jo.getString("sonNum"));
						dwe.setParID(jo.getString("parID"));
						entitys.add(dwe);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				case ZH_RE_CODE:
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						ZhangHuEntity dwe = new ZhangHuEntity();
						dwe.setFullName(jo.getString("fullName"));
						dwe.setPersonCode(jo.getString("personCode"));
						dwe.setId(jo.getString("id"));
						dwe.setSonNum(jo.getString("sonNum"));
						dwe.setParId(jo.getString("parId"));
						entitys.add(dwe);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				case PR_RE_CODE:
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						ProductEntity pe = Utils.parseJSONObjectToObject(ProductEntity.class, jo);
						entitys.add(pe);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				case OR_RE_CODE:
                    getSupportActionBar().setTitle("订单列表");
					for(int i=0;i<ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						OrderEntity oe = new OrderEntity();
						oe.setDate(jo.getString("date"));
						oe.setNumber(jo.getString("number"));
						oe.setuFullName(jo.getString("uFullName"));
						entitys.add(oe);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				case AREA_RE_CODE:
                    getSupportActionBar().setTitle("地区列表");
					for(int i=0;i<ja.length();i++){
						AreaEntity ae = new AreaEntity();
						JSONObject jo = ja.getJSONObject(i);
						Utils.setObjectValue(ae, jo);
						entitys.add(ae);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				case PRICE_RE_CODE:
                    getSupportActionBar().setTitle("价格列表");
					for(int i=0;i<ja.length();i++){
						PriceEntity pe = new PriceEntity();
						JSONObject jo = ja.getJSONObject(i);
						Utils.setObjectValue(pe, jo);
						entitys.add(pe);
					}
					ea.notifyDataSetChanged();
					if(isRefresh){
						refreshList.onRefreshComplete();
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				Toast.makeText(EntityListActivity.this, "数据加载异常", Toast.LENGTH_LONG).show();
				EntityListActivity.this.finish();
			} finally{
				if(!isRefresh){
					Utils.dismissProgressDialog();
				}
			}
		}
	}
	
	public void initParam(){
		paramMap.clear();
		paramMap.put("type", "1");
		paramMap.put("operator", "00000");
		paramMap.put("operatorId", application.getUserEntity().getId());
		paramMap.put("szParid", "00000");
		paramMap.put("cKind", "Z");
		paramMap.put("szStr", "");
	}
	
	@Override
	public void onBackPressed() {
            //如果父列表有数据
            if(lastEntitys.size()>1){
                String parentId = lastEntitys.removeLast().getParID();
                ea.checkedMap.clear();
                ea.checkShow.clear();
                //设置参数后重新获取
                paramMap.put("szParid", parentId);
                page = 1;
                paramMap.put("cpage", page+"");
                refreshList.setMode(Mode.PULL_FROM_END);
                Utils.showProgressDialog("正在获取数据，请稍候……",this);
                /**
                 * 重新请求数据
                 */
                CommunicationServer sc = new CommunicationServer(this,application.getClient(),server+action,paramMap,handler, new EntityParseThread(false));
                Thread t = new Thread(sc);
                t.start();
            }else{
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
	}
	
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
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... arg0) {
			paramMap.put("cpage", (page++)+"");
			CommunicationServer sc = new CommunicationServer(EntityListActivity.this,application.getClient(),server+action,paramMap,handler, new EntityParseThread(true));
			Thread t = new Thread(sc);
			t.start();
			return null;
		}
	}
}
