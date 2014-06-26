package com.jetsoft.activity.warning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jetsoft.R;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.activity.SourceActivity;
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.Utils;

public class OutTimeActivity extends SourceActivity implements OnClickListener,OnItemClickListener{
	
	ListView entityList;
	PullToRefreshListView refreshList;
	
	HashMap<String, String> params = new HashMap<String, String>();
	
	String action;
	
	List<HashMap<String,String>> dataList = new  LinkedList<HashMap<String,String>>();
	
	int page = 1;
	
	ViewHolderAdatper holderAdatper;
	
	EditText search_dw;
	
	View searchDwAction;
	
	DanWeiEntity dw_entity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outtime_list_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("超期应收款报表");

		search_dw = (EditText) findViewById(R.id.search_dw);
		searchDwAction = findViewById(R.id.search);
		searchDwAction.setOnClickListener(this);
		
		refreshList = (PullToRefreshListView) findViewById(R.id.entity_list);
		entityList = refreshList.getRefreshableView();
		refreshList.setMode(Mode.PULL_FROM_END);
		refreshList.setScrollingWhileRefreshingEnabled(false);
		refreshList.getLoadingLayoutProxy().setReleaseLabel("上拉加载下一页");
		refreshList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});
		entityList.setOnItemClickListener(this);
		
		holderAdatper = new ViewHolderAdatper(dataList,this,DetailViewHolder.OUTTIME_TYPE);
		entityList.setAdapter(holderAdatper);
		
		params.put("cMode", "Z");
		params.put("szUID", "");
		params.put("szOperatorID", application.getUserEntity().getId());
		params.put("cpage", page+++"");
		params.put("szDate", Utils.getFormatDate(0));
		
		action = getString(R.string.getWarnARTotal);
		
//		Utils.setProgressDialogMsg("正在请求数据...");
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action, params, handler, new GetContent());
		Thread t = new Thread(cs);
		t.start();
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    class GetContent extends ExcuteThread{
		
		@Override
		public void run() {
//			Utils.dismissProgressDialog();
			refreshList.onRefreshComplete();
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				if(ja.length()==0){
					Toast.makeText(OutTimeActivity.this, "没有更多的数据了!", Toast.LENGTH_SHORT).show();
					refreshList.setMode(Mode.DISABLED);
					return;
				}
				for(int i=0;i<ja.length();i++){
					HashMap<String,String> map = new HashMap<String, String>();
					JSONObject jo = ja.getJSONObject(i);
					Iterator<String> keys = jo.keys();
					while(keys.hasNext()){
						String key = keys.next();
						map.put(key, jo.getString(key));
					}
					dataList.add(map);
				}
				holderAdatper.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... arg0) {
			params.put("cpage", (page++)+"");
			CommunicationServer sc = new CommunicationServer(OutTimeActivity.this,application.getClient(),server+action,params,handler, new GetContent());
			Thread t = new Thread(sc);
			t.start();
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		if(v == searchDwAction){
			selectEntity(EntityListActivity.DW_RE_CODE,search_dw.getText().toString());
		}
	}
	
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode == RESULT_CANCELED){
				return;
		}
		if(requestCode == EntityListActivity.DW_RE_CODE){
			dw_entity = (DanWeiEntity) data.getSerializableExtra("entity");
			search_dw.setText(dw_entity.getFullName());
			dataList.clear();
			page = 1;
			params.put("szUID", dw_entity.getId());
			refreshList.setMode(Mode.PULL_FROM_END);
			new GetDataTask().execute();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		HashMap<String, String> data = dataList.get(arg2-1);
		nBilType = data.get("bilType");
		bilID = data.get("bilID");
		getDraftContent();
	}
	
}
