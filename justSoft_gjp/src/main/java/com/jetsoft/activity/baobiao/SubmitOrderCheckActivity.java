package com.jetsoft.activity.baobiao;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.R;
import com.jetsoft.activity.SourceActivity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.Utils;

public class SubmitOrderCheckActivity extends SourceActivity{
	
	public static final int RECODE = 101;
	
	CheckBox agreeButton;
	
	HashMap<String, String> orderInfo;
	
	String action;
	
	HashMap<String, String> params = new HashMap<String, String>();
	
	EditText memo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_check);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("单据审核");
		
		memo = (EditText) findViewById(R.id.memo);
		
		orderInfo = (HashMap<String, String>) getIntent().getSerializableExtra(OrderCheckActivity.ORDER_INFO);
		
		String iCanDo = orderInfo.get("iCanDo");
		
		agreeButton = (CheckBox) findViewById(R.id.agree);
		agreeButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					buttonView.setText("同意");
				}else{
					buttonView.setText("不同意");
				}
			}
		});
		if(iCanDo.equals("1")){
			agreeButton.setChecked(false);
			agreeButton.setText("不同意");
		}else{
			agreeButton.setChecked(true);
			agreeButton.setText("同意");
		}
		
		action = getString(R.string.setBilAudit);
		
		params.put("cMode", "Z");
		params.put("nBilID", orderInfo.get("bilID"));
		params.put("nBilType", orderInfo.get("bilType"));
		params.put("nICanDo", orderInfo.get("iCanDo"));
		params.put("szOperatorID", application.getUserEntity().getId());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem saveItem = menu.add(0,10001,0,"确定");
        saveItem.setIcon(android.R.drawable.ic_menu_save);
        saveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 10001){
            params.put("szMemo", memo.getText().toString());
            params.put("nICanDo", agreeButton.isChecked()?"1":"0");
            Utils.showProgressDialog("正在审核中......", this);
            CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action,params, handler, new SubmitExcute());
            Thread t = new Thread(cs);
            t.start();
        }else if(item.getItemId() == android.R.id.home){
            setResult(RESULT_CANCELED);
        }
        return super.onOptionsItemSelected(item);
    }
	
	class SubmitExcute extends ExcuteThread{
		
		@Override
		public void run() {
			Toast t = Toast.makeText(SubmitOrderCheckActivity.this, "", Toast.LENGTH_LONG); 
			try {
				JSONArray ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				String result = jo.getString("result");
				if(result.equals("0")){
					t.setText("审核成功");
				}else if(result.equals("-1")){
					t.setText("单据已经删除");
				}else if(result.equals("-2")){
					t.setText("单据已经审核");
				}
			} catch (Exception e) {
				t.setText("数据错误");
			}finally{
				Utils.dismissProgressDialog();
				t.show();
				SubmitOrderCheckActivity.this.finish();
			}
		}
	}

}
