package com.jetsoft.activity.baobiao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jetsoft.R;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.activity.SourceActivity;
import com.jetsoft.activity.warning.ViewHolderAdatper;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.ProductEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.Utils;

public class BaoBiaoItemActivity extends SourceActivity implements OnClickListener{
	
	/**
	 * 利润表报表功能
	 */
	public static final int TYPE_PROFIT = 1;
	/**
	 * 最近月销售功能
	 */
	public static final int TYPE_MONTH_IN_SALE = 2;

	ListView entityList;
	PullToRefreshListView refreshList;
	/**
	 * 请求参数map
	 */
	HashMap<String, String> params = new HashMap<String, String>();

	String action;
	
	/**
	 * 返回数据list
	 */
	List<HashMap<String, String>> dataList = new LinkedList<HashMap<String, String>>();

	int page = 0;
	
	/**
	 * 通用adapter
	 */
	ViewHolderAdatper adatper;

	LinearLayout baobiao_profit_title,baobiao_monthsale_title;
	
	int type;
	
	int viewHolderType ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baobiao_item_list_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		baobiao_profit_title = (LinearLayout) findViewById(R.id.baobiao_profit_title);
		baobiao_monthsale_title = (LinearLayout) findViewById(R.id.baobiao_monthsale_title);
		type = getIntent().getIntExtra("type", TYPE_PROFIT);
		
		refreshList = (PullToRefreshListView) findViewById(R.id.entity_list);
		refreshList.setMode(Mode.DISABLED);
		refreshList.setScrollingWhileRefreshingEnabled(false);
		entityList = refreshList.getRefreshableView();

		refreshList.getLoadingLayoutProxy().setReleaseLabel("上拉加载下一页");
		refreshList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});
		
		if(type == TYPE_PROFIT){
			viewHolderType = DetailViewHolder.PROFIT_TYPE;
		}else{
			viewHolderType = DetailViewHolder.MONTH_IN_SALE_TYPE;
		}

		adatper = new ViewHolderAdatper(dataList, this,
				viewHolderType);
		entityList.setAdapter(adatper);
		
		if(type == TYPE_PROFIT ){
			baobiao_profit_title.setVisibility(View.VISIBLE);
			getSupportActionBar().setTitle("利润表");
			viewHolderType = DetailViewHolder.PROFIT_TYPE;
			action = getString(R.string.getProfitInfo);
			
			params.put("cMode", "Z");
			params.put("szBID", "");
			params.put("szDateBegin", "");
			params.put("szDateEnd", "");
			params.put("szPeriod", application.getPeriod());
			params.put("cpage", page++ + "");
			params.put("bIsZero", "0");
			params.put("szOperator", application.getUserEntity().getId());
		}else if(type == TYPE_MONTH_IN_SALE){
			baobiao_monthsale_title.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("最近月销售");
			viewHolderType = DetailViewHolder.MONTH_IN_SALE_TYPE;
			action = getString(R.string.getGoodsSaleFenbu);
			
			params.put("cMode", "Z");
			params.put("szDate", Utils.getFormatDate(0));
            String uid = getIntent().getStringExtra("szUID");
            if(uid!=null && !"".equals(uid)){
                params.put("szUID", uid);
            }
			params.put("szGID", getIntent().getStringExtra("szGID"));
			params.put("szOperator", application.getUserEntity().getId());
			params.put("cpage", page++ + "");
			new GetDataTask().execute();
			return;
		}
		/**
		 * 一进来页面就显示查询条件的dialog
		 */
		showSearch();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(type == TYPE_PROFIT){
            MenuItem searchItem = menu.add(0,10001,0,"筛选");
            searchItem.setIcon(android.R.drawable.ic_menu_search);
            searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 10001){
            if(dialog!=null && dialog.isShowing()){
                return false;
            }
            showSearch();
        }else if(item.getItemId() == android.R.id.home){
            this.finish();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
	public void onClick(View v) {
		if(v == searchDw){
			selectEntity(EntityListActivity.DW_RE_CODE,dw_search.getText().toString());
		}else if(v == searchPro){
			selectEntity(EntityListActivity.PR_RE_CODE,pro_search.getText().toString());
		}
		else if(v == search){
			search();
			dialog.dismiss();
		}else if(v == startDate_acttion){
			showChooseDate(startDate);
		}else if(v == endDate_action){
			showChooseDate(endDate);
		}
	}
	
	/**
	 * 获取返回数据
	 * @author funever
	 *
	 */
	class GetContent extends ExcuteThread {
		@Override
		public void run() {
			Utils.dismissProgressDialog();
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				if (ja.length() == 0) {
					Toast.makeText(BaoBiaoItemActivity.this, "没有更多的数据了!",
							Toast.LENGTH_SHORT).show();
					refreshList.onRefreshComplete();
					refreshList.setMode(Mode.DISABLED);
					return;
				}
				for (int i = 0; i < ja.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject jo = ja.getJSONObject(i);
					Iterator<String> keys = jo.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						map.put(key, jo.getString(key));
					}
					dataList.add(map);
				}
				adatper.notifyDataSetChanged();
				refreshList.onRefreshComplete();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 下拉刷新请求
	 * @author funever
	 *
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... arg0) {
			params.put("cpage", (page++) + "");
			CommunicationServer sc = new CommunicationServer(
					BaoBiaoItemActivity.this, application.getClient(), server
							+ action, params, handler, new GetContent());
			Thread t = new Thread(sc);
			t.start();
			return null;
		}
	}
	
	private Button search;
	private Dialog dialog;
	
	private TextView startDate,endDate;
	private RelativeLayout startDate_acttion,endDate_action;
	private CheckBox showZero;
	
	private void showProfitDialog(){
		if(dialog == null){
			dialog = new Dialog(this, R.style.dialog);
			dialog.setContentView(R.layout.profit_search_dialog);
			
			startDate = (TextView) dialog.findViewById(R.id.start_date_text);
			Calendar c = Calendar.getInstance();
			startDate.setText(Utils.getFormatDate(-c.get(Calendar.DAY_OF_MONTH)));
			endDate = (TextView) dialog.findViewById(R.id.end_date_text);		
			endDate.setText(Utils.getFormatDate(0));
			
			startDate_acttion = (RelativeLayout) dialog.findViewById(R.id.start_date);
			endDate_action = (RelativeLayout) dialog.findViewById(R.id.end_date);
			startDate_acttion.setOnClickListener(this);
			endDate_action.setOnClickListener(this);
			
			showZero = (CheckBox) dialog.findViewById(R.id.show_zero);
			
			search = (Button)dialog. findViewById(R.id.submit);
			search.setOnClickListener(this);
		}
		dialog.show();
	}
	
	private EditText dw_search;
	private View searchDw;
	private DanWeiEntity dwEntity;
	private EditText pro_search;
	private View searchPro;
	private ProductEntity productEntity;
	
	private void showMonthSaleDialog(){
		if(dialog == null){
			dialog = new Dialog(this, R.style.dialog);
			dialog.setContentView(R.layout.month_sale_search);
			
			dw_search = (EditText)dialog. findViewById(R.id.search_dw);
			searchDw = dialog.findViewById(R.id.dw_search);
			
			pro_search = (EditText) dialog.findViewById(R.id.search_pro);
			searchPro = dialog.findViewById(R.id.pro_search);
			
			searchDw.setOnClickListener(this);
			searchPro.setOnClickListener(this);
			
			search = (Button) dialog.findViewById(R.id.submit);
			search.setOnClickListener(this);
		}
		dialog.show();
	}
	
	
	private void showSearch(){
		if(type == TYPE_PROFIT){
			showProfitDialog();
		}else if(type == TYPE_MONTH_IN_SALE){
			showMonthSaleDialog();
		}
	}
	
	private void search(){
		if(type == TYPE_PROFIT){
			params.put("szDateBegin", startDate.getText().toString());
			params.put("szDateEnd", endDate.getText().toString());
			params.put("bIsZero", showZero.isChecked()?"1":"0");
		}else if(type == TYPE_MONTH_IN_SALE){
			params.put("szUID", dwEntity!=null?dwEntity.getId():dw_search.getText().toString());
			params.put("szGID", productEntity!=null?productEntity.getId():pro_search.getText().toString());
		}
		page = 1;
		dataList.clear();
		adatper.notifyDataSetChanged();
		new GetDataTask().execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode == RESULT_CANCELED){
				return;
		}
		if(requestCode == EntityListActivity.DW_RE_CODE){
			dwEntity = (DanWeiEntity) data.getSerializableExtra("entity");
			if(dw_search!=null){
				dw_search.setText(dwEntity.getFullName());
			}
		}else if(requestCode == EntityListActivity.PR_RE_CODE){
			Object o = data.getSerializableExtra("entity");
			if(o instanceof ProductEntity){
				productEntity = (ProductEntity) o;
			}else if(o instanceof ArrayList){
				productEntity =(ProductEntity) ((ArrayList)o).get(0);
			}
			if(productEntity!=null){
				pro_search.setText(productEntity.getFullName());
			}
		}
	}


}