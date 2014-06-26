package com.jetsoft.activity.kucun;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.activity.warning.ViewHolderAdatper;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.Utils;

public class PandianListActivity extends SourceActivity implements
		OnClickListener, OnItemClickListener,TextWatcher {

	HashMap<String, String> params = new HashMap<String, String>();

	ListView entityList;
	PullToRefreshListView refreshList;

	String action = "";

	List<HashMap<String, String>> dataList = new LinkedList<HashMap<String, String>>();

	ViewHolderAdatper holderAdatper;

	int page = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kucun_list_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("盘点单列表");

		action = getString(R.string.getCheckCount);

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

		holderAdatper = new ViewHolderAdatper(dataList, this,
				DetailViewHolder.KUCUN_LIST_TYPE);
		entityList.setAdapter(holderAdatper);

		params.put("cMode", "Z");
		params.put("szOperator", application.getUserEntity().getId());
		params.put("cpage", page++ + "");
		params.put("nStates", "0");
		
		selectDateDialog();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem saveItem = menu.add(0,10001,0,"添加");
        saveItem.setIcon(android.R.drawable.ic_menu_add);
        saveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT|MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 10001){
            showPandianDialog();
        }else if(item.getItemId() == android.R.id.home){
            this.finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(View v) {
		if(v == ck_add){
			selectEntity(EntityListActivity.CK_RE_CODE,ck_edit.getText().toString());
		}else if(v == submit){
			if(ck_entity == null){
				Toast.makeText(this, "请选择仓库！", 2000).show();
				return;
			}
			
			//http://192.168.0.21:6002/AndroidWEB/saveCheck.do?mID=0&fullName=ddd&sID=00001&date=2013-02-20&eID=00002&modiDate=&modiEID=&memo=&tag=0&cKind=A&newMID=0
			String actionUrl = getString(R.string.pandian_mid_action);
			
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("mID", "0");
			param.put("fullName", application.getUserEntity().getEfullName()+"安卓盘点数据"+pandian_date_text.getText().toString());
			param.put("sID", ck_entity.getId());
			param.put("date", pandian_date_text.getText().toString());
			param.put("eID", application.getUserEntity().getId());
			param.put("modiDate", "");
			param.put("modiEID", "");
			param.put("memo", "");
			param.put("tag", "");
			param.put("cKind", "A");
			param.put("newMID", "0");
			
			((Dialog)submit.getTag()).dismiss();
			
			Utils.showProgressDialog("请求数据中，请稍候！", this);
			CommunicationServer ccs = new CommunicationServer(this, application.getClient(), server+actionUrl, param, handler, new SaveCheckThread());
			Thread t = new Thread(ccs);
			t.start();
		}
	}

	class GetContent extends ExcuteThread {
		@Override
		public void run() {
			// Utils.dismissProgressDialog();
			refreshList.onRefreshComplete();
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				if (ja.length() == 0) {
					Toast.makeText(PandianListActivity.this, "没有更多的数据了!",
							Toast.LENGTH_SHORT).show();
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
				holderAdatper.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		HashMap<String,String> entity = dataList.get(arg2-1);
		String actionUrl = getString(R.string.loadCheckCount);
		
		HashMap<String,String> param = new HashMap<String, String>();
		param.put("cMode", "Z");
		param.put("nMID", entity.get("mID"));
		param.put("szOperator", application.getUserEntity().getId());
		
		Utils.showProgressDialog("请求数据中，请稍候！", this);
		CommunicationServer ccs = new CommunicationServer(this, application.getClient(), server+actionUrl, param, handler, new GetPanDianProduct(entity));
		Thread t = new Thread(ccs);
		t.start();
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... arg0) {
			params.put("cpage", (page++) + "");
			CommunicationServer sc = new CommunicationServer(
					PandianListActivity.this, application.getClient(), server
							+ action, params, handler, new GetContent());
			Thread t = new Thread(sc);
			t.start();
			return null;
		}
	}
	
	private void selectDateDialog(){
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.select_date);
		
		RelativeLayout startDate = (RelativeLayout) dialog.findViewById(R.id.start_date);
		final TextView startDateText = (TextView) dialog.findViewById(R.id.start_date_text);
		startDateText.setText(Utils.getFormatDate(-30));
		RelativeLayout endDate = (RelativeLayout) dialog.findViewById(R.id.end_date);
		final TextView endDateText = (TextView) dialog.findViewById(R.id.end_date_text);
		endDateText.setText(Utils.getFormatDate(0));
		Button submit = (Button) dialog.findViewById(R.id.submit);
		
		startDate.setOnClickListener(new SelectDateListener(startDateText));
		endDate.setOnClickListener(new SelectDateListener(endDateText));
		
		submit.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				if(startDateText.getText().toString().equals("") || endDateText.getText().toString().equals("")){
					Toast.makeText(PandianListActivity.this, "请选择日期!", Toast.LENGTH_SHORT).show();
					return;
				}
				params.put("szDateBegin",  startDateText.getText().toString());
				params.put("szDateEnd",  endDateText.getText().toString());
				page = 1;
				new GetDataTask().execute();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	CangKuEntity ck_entity;
	EditText ck_edit;
	ImageView ck_add;
	Button submit;
	TextView pandian_date_text;
	
	private void showPandianDialog(){
		Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.kucun_pandian_dialog);
		
		ck_edit = (EditText) dialog.findViewById(R.id.ck_edit);
		ck_edit.addTextChangedListener(this);
		//仓库
        ck_add = (ImageView)dialog. findViewById(R.id.ck_add);
        ck_add.setOnClickListener(this);
        
        RelativeLayout  ldrq_layout = (RelativeLayout) dialog.findViewById(R.id.pandian_date);
        pandian_date_text = (TextView) dialog.findViewById(R.id.pandian_date_text);
        pandian_date_text.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        ldrq_layout.setOnClickListener(new SelectDateListener(pandian_date_text));
        
        submit = (Button)dialog.findViewById(R.id.submit);
        submit.setTag(dialog);
        submit.setOnClickListener(this);
        
        dialog.show();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == EntityListActivity.CK_RE_CODE){
			ck_entity = (CangKuEntity) data.getSerializableExtra("entity");
			ck_edit.setText(ck_entity.getFullName());
		}else if(requestCode == PANDIAN){
			page = 1;
			dataList.clear();
			new GetDataTask().execute();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(ck_edit.getText().toString().equals("")){
			ck_entity = null;
		}
	}
	
	private static final int PANDIAN = 101;
	
	/**
	 * 获取mid后的返回处理
	 * 
	 *
	 * @version 1.0
	 * @author Administrator
	 */
	class SaveCheckThread extends ExcuteThread{
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				String mid = jo.getString("mID");
				
				//获取mid
				Intent intent = new Intent();
				intent.setClass(PandianListActivity.this, KuCunProductActivity.class);
				intent.putExtra("ck_id", ck_entity.getId());
				intent.putExtra("ck_name", ck_entity.getFullName());
				intent.putExtra("date", pandian_date_text.getText());
				intent.putExtra("mid", mid);
				startActivityForResult(intent, PANDIAN);
			}catch (Exception e) {
				Toast.makeText(PandianListActivity.this, "获取仓库数据错误!", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	class GetPanDianProduct extends ExcuteThread{
		
		HashMap<String, String> entity;
		
		public GetPanDianProduct(HashMap<String, String> entity){
			this.entity = entity;
		}
		
		@Override
		public void run() {
			Utils.dismissProgressDialog();
			String json = getJsonString();
			if(json!=null && !json.equals("")){
				Intent intent = new Intent();
				intent.setClass(PandianListActivity.this, KuCunProductActivity.class);
				intent.putExtra("ck_id", entity.get("sID"));
				intent.putExtra("date", entity.get("date"));
				intent.putExtra("mid", entity.get("mID"));
				intent.putExtra("ck_name", entity.get("sName"));
				intent.putExtra("product", json);
				startActivityForResult(intent, PANDIAN);
			}else{
				Toast.makeText(PandianListActivity.this, "获取盘点单商品数据错误！", 2000).show();
			}
		}
	}
}