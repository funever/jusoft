package com.jetsoft.activity.warning;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.Utils;
import com.jetsoft.view.CustomSinnper;

public class OrderWarningActivity extends SourceActivity implements
		OnClickListener,OnItemClickListener{

	ListView entityList;
	PullToRefreshListView refreshList;

	HashMap<String, String> params = new HashMap<String, String>();

	String action;

	List<HashMap<String, String>> dataList = new LinkedList<HashMap<String, String>>();

	int page = 1;

	ViewHolderAdatper adatper;
	
	LinearLayout sale_item,order_item;
	
	private String cMode;
	
	EditText search_dw;
	
	View dwAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderwarn_list_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("订单报警报表");
		
		cMode = getIntent().getStringExtra("cMode");

		sale_item = (LinearLayout) findViewById(R.id.sale_item);
		order_item = (LinearLayout) findViewById(R.id.order_item);
		order_item.setVisibility(View.VISIBLE);
		
		refreshList = (PullToRefreshListView) findViewById(R.id.entity_list);
		refreshList.setMode(Mode.PULL_FROM_END);
		refreshList.setScrollingWhileRefreshingEnabled(false);
		entityList = refreshList.getRefreshableView();
		entityList.setOnItemClickListener(this);

		refreshList.getLoadingLayoutProxy().setReleaseLabel("上拉加载下一页");
		refreshList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});
		
		search_dw = (EditText) findViewById(R.id.search_dw);
		dwAction = findViewById(R.id.dw_search);
		dwAction.setOnClickListener(this);

		adatper = new ViewHolderAdatper(dataList, this,
				DetailViewHolder.ORDERWARN_TYPE_ORDER);
		entityList.setAdapter(adatper);

		params.put("szOperatorID", application.getUserEntity().getId());
		params.put("cpage", page++ + "");
		params.put("szDate", Utils.getFormatDate(0));
		
		action = getString(R.string.getWarnOrder);
		
//		showSearch();
		search(search_dw.getText().toString());
	}

	@Override
	public void onClick(View v) {
		if(v == dwAction){
			selectEntity(EntityListActivity.DW_RE_CODE,search_dw.getText().toString());
		}else if(v == search){
			dialog.dismiss();
		}
	}

	class GetContent extends ExcuteThread {

		@Override
		public void run() {
			Utils.dismissProgressDialog();
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				if (ja.length() == 0) {
					Toast.makeText(OrderWarningActivity.this, "没有更多的数据了!",
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

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... arg0) {
			params.put("cpage", (page++) + "");
			CommunicationServer sc = new CommunicationServer(
					OrderWarningActivity.this, application.getClient(), server
							+ action, params, handler, new GetContent());
			Thread t = new Thread(sc);
			t.start();
			return null;
		}
	}
	
	private EditText dw_search;
	private CustomSinnper order_type;
	private Button search;
	private View searchDw;
	private DanWeiEntity dwEntity;
	private Dialog dialog;
	
	private void showSearch(){
		if(dialog==null){
			dialog = new Dialog(this, R.style.dialog);
			dialog.setContentView(R.layout.order_warn_search);
			
			order_type = (CustomSinnper) dialog.findViewById(R.id.order_type);
			
			String[] item = new String[]{"进货订单","销售订单"};
			List<String> valueList = new LinkedList<String>();
			valueList.add("B");
			valueList.add("S");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.spinner_item, item);
			order_type.setAdapter(adapter);
			order_type.setValueList(valueList);
			
			dw_search = (EditText) dialog.findViewById(R.id.search_dw);
			search = (Button) dialog.findViewById(R.id.submit);
			searchDw = dialog.findViewById(R.id.dw_search);
			searchDw.setOnClickListener(this);
			search.setOnClickListener(this);
		}
		dialog.show();
	}
	
	private void search(String dw){
		params.put("cMode", cMode);
		params.put("szUID", dw);
		page = 1;
		dataList.clear();
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
			dataList.clear();
			search(dwEntity.getId());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		HashMap<String, String> data = dataList.get(arg2-1);
		nBilType = data.get("bilType");
		bilID = data.get("bilID");
		getDraftContent();
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}