package com.jetsoft.activity.draft;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jetsoft.R;
import com.jetsoft.activity.TabHostActivity;
import com.jetsoft.activity.kucun.KuCunActivity;
import com.jetsoft.activity.money.MoneyActivity;
import com.jetsoft.activity.order.OrderActivity;
import com.jetsoft.activity.returnorder.ReturnOrderActivity;
import com.jetsoft.activity.sale.SaleActivity;
import com.jetsoft.activity.sale.WaiMaoActivity;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.TableEntity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DraftListActivity extends FinalActivity implements OnItemClickListener{
	
	ListView dj_info_listView;
	@ViewInject(id=R.id.nv_return,click="click")
	Button nv_return;
	public static final String DJENTITY_INTENT = "djentity";
	
	PullToRefreshListView refreshList;
	
	List<DJEntity> deList = new LinkedList<DJEntity>();
	
	MyApplication application;
	
	public String server;
	
	private SharedPreferences preferences;
	
	private Handler handler = new Handler();
	
	HashMap<String, String> params = new HashMap<String, String>();
	
	String selectProducts = "";
	/**
	 * 点击的单据的类型
	 */
	String biltype = "";
	
	String sale_type = null;
	
	String money_type = null;
	
	int tiao_type = 0;
	
	String cMode = "";
	
	Class activityClass = null;
	
	int page = 1;
	
	HashMap<String, String> listParams = new HashMap<String, String>();
	
	DraftInfoAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draft_list_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("草稿列表");
		application = (MyApplication) getApplication();
		
		refreshList = (PullToRefreshListView) findViewById(R.id.draft_info);
		refreshList.setMode(Mode.PULL_FROM_END);
		refreshList.setScrollingWhileRefreshingEnabled(false);
		refreshList.getLoadingLayoutProxy().setReleaseLabel("上拉加载下一页");
		refreshList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});
		dj_info_listView = refreshList.getRefreshableView();
		dj_info_listView.setOnItemClickListener(this);
		
		preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
		+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
		
		adapter = new DraftInfoAdapter();
		dj_info_listView.setAdapter(adapter);
		
		listParams = (HashMap<String, String>) getIntent().getSerializableExtra(DJENTITY_INTENT);
		
		new GetDataTask().execute();
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... arg0) {
			listParams.put("cpage", (page++)+"");
			CommunicationServer sc = new CommunicationServer(DraftListActivity.this,application.getClient(),server+getString(R.string.draft_action),listParams,handler, new SubmitExcute());
			Thread t = new Thread(sc);
			t.start();
			return null;
		}
	}
	
	class SubmitExcute extends ExcuteThread{
		@Override
		public void run() {
			try{
				refreshList.onRefreshComplete();
				JSONArray ja = new JSONArray(getJsonString());
				if(ja.length()==0){
					Toast.makeText(DraftListActivity.this, "没有更多的数据了!", Toast.LENGTH_SHORT).show();
					refreshList.setMode(Mode.DISABLED);
					return;
				}
				for(int i=0;i<ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					DJEntity de = Utils.parseJSONObjectToObject(DJEntity.class, jo);
					deList.add(de);
				}
				adapter.notifyDataSetChanged();
			}catch(Exception e){
				Toast.makeText(DraftListActivity.this, "数据错误！", 2000).show();
			}
		}
	}
	
	/**
	 * 单据内容的适配器
	 * @author funever_win8
	 *
	 */
	class DraftInfoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return deList.size();
		}

		@Override
		public Object getItem(int position) {
			return deList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if(convertView == null){
				convertView = LayoutInflater.from(DraftListActivity.this).inflate(R.layout.draft_info_item, null);
				vh = new ViewHolder();
				vh.dj_no = (TextView) convertView.findViewById(R.id.dj_no);
				vh.dj_type = (TextView) convertView.findViewById(R.id.dj_type);
				vh.dj_dw = (TextView) convertView.findViewById(R.id.dj_dw);
				vh.dj_jsr = (TextView) convertView.findViewById(R.id.dj_jsr);
				vh.dj_date = (TextView) convertView.findViewById(R.id.dj_date);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			
			DJEntity de = deList.get(position);
			vh.dj_no.setText(de.getNumber());
			vh.dj_type.setText(getFormatType(de.getBilType()));
			vh.dj_dw.setText(de.getuFullName());
			vh.dj_jsr.setText(de.geteFullName());
			vh.dj_date.setText(de.getDate());
			return convertView;
		}
		
		class ViewHolder{
			TextView dj_no;
			TextView dj_type;
			TextView dj_dw;
			TextView dj_jsr;
			TextView dj_date;
		}
		/**
		 * 格式化订单类型名称
		 * @param nBilType
		 * @return
		 */
		private String getFormatType(String nBilType){
			if(nBilType.equals(StaticValues.ORDER_IN_BILTYPE)){
				return "进货订单";
			}else if(nBilType.equals(StaticValues.ORDER_BILTYPE)){
				return "进货单";
			}else if(nBilType.equals(StaticValues.ORDER_RE_BILTYPE)){
				return "进货退货单";
			}else if(nBilType.equals(StaticValues.SALE_IN_BILTYPE)){
				return "外贸销售单";
			}else if(nBilType.equals(StaticValues.SALE_BILTYPE)){
				return "销售单";
			}else if(nBilType.equals(StaticValues.SALE_RE_BILTYPE)){
				return "销售退货单";
			}else if(nBilType.equals(StaticValues.BIANJIA_BILTYPE)){
				return "变价调拨单";
			}else if(nBilType.equals(StaticValues.TONGJIA_BILTYPE)){
				return "同价调拨单";
			}else if(nBilType.equals(StaticValues.SK_BILTYPE)){
				return "收款单";
			}else if(nBilType.equals(StaticValues.FK_BILTYPE)){
				return "付款单";
			}
			return "";
		}
	}
	
	/**
	 * 获取单据class
	 * @param nBilType
	 * @return 单据页面activity的class
	 */
	private Class getDraftActivity(String nBilType){
		if(nBilType.equals(StaticValues.ORDER_IN_BILTYPE)){
			cMode = "B";
			return TabHostActivity.class;
		}else if(nBilType.equals(StaticValues.ORDER_BILTYPE)){
			cMode = "B";
			return OrderActivity.class;
		}else if(nBilType.equals(StaticValues.ORDER_RE_BILTYPE)){
			cMode = "B";
			return ReturnOrderActivity.class;
		}else if(nBilType.equals(StaticValues.SALE_IN_BILTYPE)){
			sale_type = SaleActivity.SALE_ORDER;
			cMode = "S";
			return WaiMaoActivity.class;
		}else if(nBilType.equals(StaticValues.SALE_BILTYPE)){
			cMode = "S";
			sale_type = SaleActivity.SALE_NOTE;
			return SaleActivity.class;
		}else if(nBilType.equals(StaticValues.SALE_RE_BILTYPE)){
			cMode = "S";
			sale_type = SaleActivity.SALE_RETURN;
			return SaleActivity.class;
		}else if(nBilType.equals(StaticValues.BIANJIA_BILTYPE)||nBilType.equals(StaticValues.TONGJIA_BILTYPE)){
			cMode = "O";
			tiao_type = nBilType.equals(StaticValues.BIANJIA_BILTYPE)?KuCunActivity.KUCUN_BIANJIA:KuCunActivity.KUCUN_TONGJIA;
			return KuCunActivity.class;
		}else if(nBilType.equals(StaticValues.SK_BILTYPE)){
			money_type = MoneyActivity.MONEY_SKD;
			cMode = "A";
			return MoneyActivity.class;
		}else if(nBilType.equals(StaticValues.FK_BILTYPE)){
			money_type = MoneyActivity.MONEY_FKD;
			cMode = "A";
			return MoneyActivity.class;
		}
		return null;
	}
	
	public void click(View v){
		if(v == nv_return){
			this.finish();
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		biltype = deList.get(arg2-1).getBilType();
		getDraftContent(deList.get(arg2-1));
	}
	
	/**
	 * 获取草稿内容
	 * @param de
	 */
	public void getDraftContent(DJEntity de){
		
		activityClass = getDraftActivity(biltype);
		
		HashMap<String, String> params = new HashMap<String, String>();
		String action_url = getString(R.string.draft_load);
		params.put("cMode", "T");
		params.put("nBilType", de.getBilType());
		params.put("nBilID", de.getBilID());
		params.put("szOperator", application.getUserEntity().getId());
		
		Utils.showProgressDialog("正在获取单据信息,请稍候……", this);
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action_url,params, handler, new GetDraftContent());
		Thread t = new Thread(cs);
		t.start();
	}
	
	/**
	 * 获取单据参数
	 * @author funever_win8
	 *
	 */
	class GetDraftContent extends ExcuteThread{
		@Override
		public void run() {
			if(getJsonString().equals("[]")){
				Toast.makeText(DraftListActivity.this, "没有可用的单据！", Toast.LENGTH_SHORT).show();
				Utils.dismissProgressDialog();
				return;
			}else{
				//填充值或者是下一步的请求商品操作
//				Toast.makeText(BaseActivity.this, getJsonString(), Toast.LENGTH_SHORT).show();
				try {
					JSONArray ja = new JSONArray(getJsonString());
					JSONObject jo = ja.getJSONObject(0);
					TableEntity te = Utils.parseJSONObjectToObject(TableEntity.class, jo);

					String action_url = getString(R.string.draft_last_next);
					params.put("cMode", "G");
					params.put("bilID", jo.getInt("bilID")+"");
					
					CommunicationServer cs = new CommunicationServer(DraftListActivity.this, application.getClient(), server+action_url,params, handler, new GetDraftProduct(te));
					Thread t = new Thread(cs);
					t.start();
				}  catch (Exception e) {
					e.printStackTrace();
					Utils.dismissProgressDialog();
				}
			}
		}
	}
	/**
	 * 获取单据的商品
	 * @author funever_win8
	 *
	 */
	class GetDraftProduct extends ExcuteThread{
		
		TableEntity te;
		
		public GetDraftProduct(TableEntity te){
			this.te = te;
		}
		@Override
		public void run() {
			if(te!=null){
				try {
					selectProducts = getJsonString();
					//跳转到各自的单据页面
					toPage(te);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					Utils.dismissProgressDialog();
				}
			}
		}
	}
	/**
	 * 获取到所有的参数后跳转到对应的单据页面
	 * @param te
	 */
	public void toPage(TableEntity te){
		if(activityClass == null){
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, activityClass);
		intent.putExtra(DRAFT_TABLE, te);
		intent.putExtra(DRAFT_PRODUCTS, selectProducts);
		intent.putExtra(FROM_DRAFT, true);
		intent.putExtra(DRAFT_BILTYPE, biltype);
		if(sale_type!=null){
			intent.putExtra("sale_type", sale_type);
		}
		if(money_type!=null){
			intent.putExtra("money_type", money_type);
		}
		if(tiao_type!=0){
			intent.putExtra("type", tiao_type);
		}
		startActivity(intent);
	}
	
	public static final String DRAFT_TABLE = "tableentity";
	public static final String DRAFT_PRODUCTS = "products";
	public static final String FROM_DRAFT = "fromdraft";
	public static final String DRAFT_BILTYPE = "draft_biltype";
	public static final String DRAFT_TITLE = "draft_title";
	
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
}
