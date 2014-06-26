package com.jetsoft.activity.draft;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.BaseActivity;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.JingShouRenEntity;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import com.jetsoft.view.CustomSinnper;

public class DraftActivity extends BaseActivity {
	
	@ViewInject(id=R.id.dw_edit)
	private EditText dw_edit;
	@ViewInject(id=R.id.jsr_edit)
	private EditText jsr_edit;
	@ViewInject(id=R.id.ck_edit)
	private EditText ck_edit;
	@ViewInject(id=R.id.dj_no)
	private EditText dj_no;
	@ViewInject(id=R.id.dw_action,click="click")
	private ImageView dw_action;
	@ViewInject(id=R.id.jsr_action,click="click")
	private ImageView jsr_action;
	@ViewInject(id=R.id.ck_action,click="click")
	private ImageView ck_action;
	@ViewInject(id=R.id.start_date,click="click")
	private RelativeLayout start_date;
	@ViewInject(id=R.id.end_date,click="click")
	private RelativeLayout end_date;
	@ViewInject(id=R.id.start_date_text)
	private TextView start_date_text;
	@ViewInject(id=R.id.end_date_text)
	private TextView end_date_text;
	@ViewInject(id=R.id.search,click="click")
	private Button search;
	private String draft_type;
	
	public static final String DRAFTTYPE = "draft_type";
	public static final String DRAFT_TYPE_SALE = "sale";
	public static final String DRAFT_TYPE_KC = "kc";
	public static final String DRAFT_TYPE_MONEY = "money";
	public static final String DRAFT_TYPE_ORDER = "order";
	public static final String DRAFT_TYPE_BAOBIAO = "baobiao";
	
	private CustomSinnper draftTypeSinnper;
	private String[] item;
	private List<String> valueList;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	String cMode = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.draft_search);
//		ck_edit.setText(ck_entity.getFullName());
//		dw_edit.setText(dw_entity.getFullName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("草稿查询");
		
		dw_edit.addTextChangedListener(new BaseOnTextChangeListener(dw_edit, BaseOnTextChangeListener.DW_KEY));
		jsr_edit.addTextChangedListener(new BaseOnTextChangeListener(jsr_edit, BaseOnTextChangeListener.JSR_KEY));
		ck_edit.addTextChangedListener(new BaseOnTextChangeListener(ck_edit, BaseOnTextChangeListener.CK_KEY));
		
		ck_entity = new CangKuEntity();
		dw_entity = new DanWeiEntity();
		jsr_entity = new JingShouRenEntity();
		draft_type = getIntent().getStringExtra(DRAFTTYPE);
		draftTypeSinnper = (CustomSinnper) findViewById(R.id.draft_type);
		valueList = new LinkedList<String>();
		if(draft_type.equals(DRAFT_TYPE_SALE)){
			item = new String[]{"全部","外贸销售单","销售单","销售退货"};
			valueList.add("");
			valueList.add(StaticValues.SALE_IN_BILTYPE);
			valueList.add(StaticValues.SALE_BILTYPE);
			valueList.add(StaticValues.SALE_RE_BILTYPE);
			cMode = "S";
		}else if(draft_type.equals(DRAFT_TYPE_KC)){
			item = new String[]{"全部","变价调拨单","同价调拨单"};
			valueList.add("");
			valueList.add(StaticValues.BIANJIA_BILTYPE);
			valueList.add(StaticValues.TONGJIA_BILTYPE);
			cMode = "O";
		}else if(draft_type.equals(DRAFT_TYPE_MONEY)){
			item = new String[]{"全部","付款单","收款单"};
			valueList.add("");
			valueList.add(StaticValues.FK_BILTYPE);
			valueList.add(StaticValues.SK_BILTYPE);
			cMode = "A";
			findViewById(R.id.ck_line).setVisibility(View.GONE);
			findViewById(R.id.ck_layout).setVisibility(View.GONE);
		}else if(draft_type.endsWith(DRAFT_TYPE_ORDER)){
			item = new String[]{"全部","进货单","进货退货单"};
			valueList.add("");
			valueList.add(StaticValues.ORDER_BILTYPE);
			valueList.add(StaticValues.ORDER_RE_BILTYPE);
			cMode = "B";
		}else if(draft_type.endsWith(DRAFT_TYPE_BAOBIAO)){
			item = new String[]{"全部"};
			valueList.add("");
			cMode = "Z";
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, item);
		draftTypeSinnper.setAdapter(adapter);
		draftTypeSinnper.setValueList(valueList);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		
		start_date_text.setText(sdf.format(calendar.getTime()));
		end_date_text.setText(sdf.format(new Date()));
	}
	
	public void click(View v){
		if(v == dw_action){
			selectEntity(EntityListActivity.DW_RE_CODE,dw_edit.getText().toString());
		}else if(v == jsr_action){
			selectEntity(EntityListActivity.JSR_RE_CODE,jsr_edit.getText().toString());
		}else if(v == ck_action){
			selectEntity(EntityListActivity.CK_RE_CODE,ck_edit.getText().toString());
		}else if(v == start_date){
			showChooseDate(start_date_text);
		}else if(v == end_date){
			showChooseDate(end_date_text);
		}else if(v == search){
			//提交
			submit();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try{
			if(requestCode == EntityListActivity.DW_RE_CODE){
				dw_entity = (DanWeiEntity) data.getSerializableExtra("entity");
				dw_edit.setText(dw_entity.getFullName());
			}else if(requestCode == EntityListActivity.JSR_RE_CODE){
				jsr_entity = (JingShouRenEntity) data.getSerializableExtra("entity");
				jsr_edit.setText(jsr_entity.getFullName());
			}else if(requestCode == EntityListActivity.CK_RE_CODE){
				ck_entity = (CangKuEntity) data.getSerializableExtra("entity");
				ck_edit.setText(ck_entity.getFullName());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 跳转到选择多列项的页面
	 */
	public void selectEntity(int requestCode,String searchString){
		Intent intent = new Intent();
		intent.setClass(this, EntityListActivity.class);
		intent.putExtra("type", requestCode);
		intent.putExtra("search", searchString);
		startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 提交
	 */
	public void submit(){
		String action = getString(R.string.draft_action);
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("cMode", cMode);
		params.put("szUid", dw_entity.getId());
		params.put("szEid", jsr_entity.getId());
		params.put("szGid", "");
		params.put("szDid", "");
		params.put("szSid", ck_entity.getId());
		params.put("bilType",draftTypeSinnper.getValue());
		params.put("szbillNumber", dj_no.getText().toString());
		params.put("szSummary", "");
		params.put("szBeginDate", start_date_text.getText().toString());
		params.put("szEndDate", end_date_text.getText().toString());
		params.put("operator", application.getUserEntity().getId());
		
//		Utils.showProgressDialog("正在提交表单，请稍候……",this);
//		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action,params, handler, new SubmitExcute());
//		Thread t = new Thread(cs);
//		t.start();
		
		Intent intent = new Intent();
		intent.setClass(DraftActivity.this, DraftListActivity.class);
		intent.putExtra(DraftListActivity.DJENTITY_INTENT, params);
		DraftActivity.this.startActivity(intent);
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

	}

	@Override
	public boolean canScan() {
		return false;
	}

	@Override
	public void disposeLastNextData(TableEntity te) {
	}
	
	@Override
	public String getNumber() {
		return "";
	}
}
