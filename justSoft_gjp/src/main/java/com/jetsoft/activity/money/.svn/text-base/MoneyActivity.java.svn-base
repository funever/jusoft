/**
 * 创建日期 2012-11-27
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
package com.jetsoft.activity.money;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.R;
import com.jetsoft.activity.BaseActivity;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.activity.draft.DraftListActivity;
import com.jetsoft.entity.BuMenEntity;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.JingShouRenEntity;
import com.jetsoft.entity.SaveTablesForm;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.entity.ZhangHuEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class MoneyActivity extends BaseActivity implements OnClickListener,OnEditorActionListener{
	
	//每行layout
	private RelativeLayout ldrq_layout,wldw_action,jsr_action,bm_action,zh_action;
	
	public TextView ldrq_date;
	
	public EditText bm_edit,jsr_edit,dw_edit,zh_edit,zy_edit,money_edit;
	
	private ImageView dw_add,jsr_add,bm_add,zh_add;
	
	public static final String MONEY_SKD = "money_skd";
	public static final String MONEY_FKD = "money_fkd";
	
	private String money_type;

	private TextView zh_title,dw_title,je_title;
	
//	private ListView danjuList;
	
	private SaveTablesForm form = new SaveTablesForm();
	
	private ZhangHuEntity zh_entity = new ZhangHuEntity();
	
	private int isClient = 0;

    MenuItem item;

    boolean fromDraft;

    TableEntity te;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.money_skd_in_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		/**
		 * 如果是从草稿单过来的，处理草稿单内容
		 */
        fromDraft = getIntent().getBooleanExtra(DraftListActivity.FROM_DRAFT, false);

		if(fromDraft){
			te = (TableEntity) getIntent().getSerializableExtra(DraftListActivity.DRAFT_TABLE);
			String pro_json = getIntent().getStringExtra(DraftListActivity.DRAFT_PRODUCTS);
			getDraftProduct(pro_json,te);
			nBilType = getIntent().getStringExtra(DraftListActivity.DRAFT_BILTYPE);
		}
		money_type = getIntent().getStringExtra("money_type");
		
        zh_title = (TextView) findViewById(R.id.zh_title);
        dw_title = (TextView) findViewById(R.id.dw_title);
        je_title = (TextView) findViewById(R.id.je_title);
        //清除账户监听类的标示
        int key = 0;
        if(money_type.equals(MONEY_FKD)){
        	zh_title.setText("付款账户:");
            getSupportActionBar().setTitle("付款单");
        	dw_title.setText("收款单位");
        	je_title.setText("付款金额");
        	nBilType = StaticValues.FK_BILTYPE; 
        	key = BaseOnTextChangeListener.FKZH_KEY;
        	isClient = 0;
        }else{
        	zh_title.setText("收款账户:");
            getSupportActionBar().setTitle("收款单");
        	nBilType = StaticValues.SK_BILTYPE;
        	key = BaseOnTextChangeListener.SKZH_KEY;
        	isClient = 1;
        }
        
        djbh_edit = (EditText) findViewById(R.id.ldbh_edit);
        if(!fromDraft){
        	getDanJuBianHao(djbh_edit, nBilType);
        }
        
        bm_edit = (EditText) findViewById(R.id.bm_edit);
        bm_edit.setOnEditorActionListener(this);
        bm_edit.addTextChangedListener(new BaseOnTextChangeListener(bm_edit, BaseOnTextChangeListener.BM_KEY));
        dw_entity = new DanWeiEntity();
        dw_edit = (EditText) findViewById(R.id.wldw_edit);
        dw_edit.setOnEditorActionListener(this);
        dw_edit.addTextChangedListener(new BaseOnTextChangeListener(dw_edit, BaseOnTextChangeListener.DW_KEY));
        jsr_edit = (EditText) findViewById(R.id.jsr_edit);
        jsr_edit.setOnEditorActionListener(this);
        jsr_edit.addTextChangedListener(new BaseOnTextChangeListener(jsr_edit, BaseOnTextChangeListener.JSR_KEY));
        //设置经手人的默认值
        jsr_edit.setText(jsr_entity.getFullName());
        zh_edit = (EditText) findViewById(R.id.zh);
        zh_edit.setOnEditorActionListener(this);
        zh_edit.addTextChangedListener(new BaseOnTextChangeListener(zh_edit, key));
        zy_edit = (EditText) findViewById(R.id.zy_edit);
        money_edit = (EditText) findViewById(R.id.money_edit);
        
        bm_edit.setOnFocusChangeListener(this);
        dw_edit.setOnFocusChangeListener(this);
        jsr_edit.setOnFocusChangeListener(this);
        
        if(!isEdit){
        	bm_edit.setEnabled(false);
        	dw_edit.setEnabled(false);
        	jsr_edit.setEnabled(false);
        	zh_edit.setEnabled(false);
        	zy_edit.setEnabled(false);
        	djbh_edit.setEnabled(false);
        	if(!orderCheck){
                item.setVisible(false);
        	}
        }
        
//        zh_edit.setOnFocusChangeListener(this);
        
        /**
         * 录单日期处理
         */
        ldrq_date = (TextView) findViewById(R.id.ldrq_edit);
        ldrq_date.setText(DateFormat.format("yyyy-MM-dd", new Date()));
        
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        
        //添加每行点击的事件
        ldrq_layout = (RelativeLayout) findViewById(R.id.ldrq_action);
        ldrq_layout.setOnClickListener(this);
        
        //选择往来单位
        wldw_action = (RelativeLayout) findViewById(R.id.wldw_action);
        wldw_action.setOnClickListener(this);
        //选择经手人
        jsr_action = (RelativeLayout) findViewById(R.id.jsr_action);
        jsr_action.setOnClickListener(this);
        //选择部门
        bm_action = (RelativeLayout) findViewById(R.id.bm_action);
        bm_action.setOnClickListener(this);
        //选择付款账户
        zh_action = (RelativeLayout) findViewById(R.id.zh_action);
        zh_action.setOnClickListener(this);
        
        dw_add = (ImageView) findViewById(R.id.dw_add);
        dw_add.setOnClickListener(this);
        jsr_add = (ImageView) findViewById(R.id.jsr_add);
        jsr_add.setOnClickListener(this);
        zh_add = (ImageView) findViewById(R.id.zh_add);
        zh_add.setOnClickListener(this);
        bm_add = (ImageView) findViewById(R.id.bm_add);
        bm_add.setOnClickListener(this);
        
      //单据打开时当前状态光标默认在往来单位字段上
        dw_edit.requestFocus();
        
        if(fromDraft){
        	disposeLastNextData(te);
        }
        je_more = (TextView) findViewById(R.id.je_more);
	    if(je_more!=null){
	    	je_more.setOnClickListener(this);
	    }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
			return;
		}
	    if(requestCode == EntityListActivity.DW_RE_CODE){
			 dw_entity = (DanWeiEntity) data.getSerializableExtra("entity");
			dw_edit.setText(dw_entity.getFullName());
		}else if(requestCode == EntityListActivity.JSR_RE_CODE){
			jsr_entity = (JingShouRenEntity) data.getSerializableExtra("entity");
			jsr_edit.setText(jsr_entity.getFullName());
		}else if(requestCode == EntityListActivity.BM_RE_CODE){
			bm_entity = (BuMenEntity) data.getSerializableExtra("entity");
			bm_edit.setText(bm_entity.getFullName());
		}else if(requestCode == EntityListActivity.ZH_RE_CODE){
			zh_entity = (ZhangHuEntity) data.getSerializableExtra("entity");
			zh_edit.setText(zh_entity.getFullName());
			mulitZh.clear();
		}
	}

	@Override
	public void onClick(View v) {
		if(!isEdit && v!=save_button){
			Toast.makeText(this, "该单据不能编辑，仅供浏览！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(v instanceof ImageView){
			RelativeLayout re = (RelativeLayout) v.getParent();
			re.performClick();
			return;
		}
		if(v == ldrq_layout){
			//点击弹出选择录单日期
			showLuDanRiQi(ldrq_date);
		}else if(v == wldw_action){
			//跳转到选择单位界面
			selectEntity(EntityListActivity.DW_RE_CODE,dw_edit.getText().toString());
		}else if(v == jsr_action){
			//跳转到经手人选择界面
			selectEntity(EntityListActivity.JSR_RE_CODE,jsr_edit.getText().toString());
		}else if(v == bm_action){
			//跳转部门选择界面
			selectEntity(EntityListActivity.BM_RE_CODE,bm_edit.getText().toString());
		}else if(v == zh_action){
			selectEntity(EntityListActivity.ZH_RE_CODE,zh_edit.getText().toString());
		}else if(v == returnButton){
			returnMenu();
		}else if(v == save_button){
			if(orderCheck){
				checkOrder();
				return;
			}
			//提交表单
			try {
				if(!validate()){
					return;
				}
//				submit();
				validateNumber();
			} catch (Exception e) {
				Toast.makeText(this, "添加失败！", Toast.LENGTH_SHORT).show();
			} 
		}else if(v == je_more){
			showJeLayout(zh_entity,money_edit,zh_edit);
		}
		
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
	
	/**
	 * 跳转到选择多列项的页面
	 */
	public void selectEntity(int requestCode,String searchString){
		Intent intent = new Intent();
		intent.setClass(this, EntityListActivity.class);
		intent.putExtra("type", requestCode);
		intent.putExtra("search", searchString);
		if(requestCode == EntityListActivity.DW_RE_CODE){
			intent.putExtra("isClient", isClient);
		}
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == 0 || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH){
			RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
			relativeLayout.performClick();
			return true;
		}
		return false;
	}
	/**
	 * 提交表单
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void submit() throws IllegalArgumentException, IllegalAccessException{
		String action = getString(R.string.order_sale_action);
		
		params.clear();
		//构建表单数据
		form.setDate(ldrq_date.getText().toString());
		form.setNumber(djbh_edit.getText().toString());
		form.setBilType(nBilType);
		form.setSaveDate(Utils.getFormatDate(0,"yyyy-MM-dd HH:mm:ss.SSS"));
		
		String summary = zy_edit.getText().toString();
		form.setSummary(getString(R.string.default_summary)+summary);
		form.setMemo(getString(R.string.default_summary));
		form.setuID(dw_entity.getId());
		form.seteID(jsr_entity.getId());
		form.setsID(ck_entity.getId());
		form.setsID2(fhck_entity.getId());
		form.setPeriod(application.getPeriod());
		form.setInputID(application.getUserEntity().getId());
		form.setDraft("2");
		form.setdID(bm_entity.getId());
		form.setbID("0000100001");
		form.setTax_total(money_edit.getText().toString());
		
		Field[] fields = form.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			fields[i].setAccessible(true);
			params.put(fields[i].getName(), (String)fields[i].get(form));
		}
		
		params.put("operatorName", application.getUserEntity().getEfullName());
		params.put("szOperCode", application.getUserEntity().geteUserCode());
		String inputCount = money_edit.getText().toString().equals("")?"0":money_edit.getText().toString();
		params.put("countID", formatZhangHu(zh_entity));
		params.put("amount", formatCount(inputCount));
		params.put("amountInput", formatCount(inputCount));
		
		params.put("bilid", bilid+"");
		params.put("bilid", bilid+"");
		
		Utils.showProgressDialog("正在提交表单，请稍候……",this);
		Intent intent = new Intent();
		intent.setClass(MoneyActivity.this, MoneyActivity.class);
		intent.putExtra("money_type", money_type);
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action,params, handler, new SubmitExcute(intent));
		Thread t = new Thread(cs);
		t.start();
	}
	
	@Override
	public void updateProNum() {
		//不处理
	}

	@Override
	public boolean validate() {
		if(dw_entity.getFullName() == null){
			Toast.makeText(this, "请选择往来单位！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(jsr_entity.getFullName() == null){
			Toast.makeText(this, "请选择操作经手人！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(zh_entity.getFullName() == null && mulitZh.size()==0){
			Toast.makeText(this, "请选择账户！", Toast.LENGTH_SHORT).show();
			return false;
		}
		//验证商品
		return true;
	}
	
	@Override
	public void setScanValue(String value) {
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		//bm_edit,jsr_edit,dw_edit,zh_edit
		//得到焦点时不处理
		if(hasFocus){
			return;
		}
		try{
			//bm_edit,ck_edit,jsr_edit,dw_edit,zh_edit
			if(v == bm_edit){
				bm_edit.setText(bm_entity.getFullName());
			}else if(v == jsr_edit){
				jsr_edit.setText(jsr_entity.getFullName());
			}else if(v == dw_edit){
				dw_edit.setText(dw_entity.getFullName());
			}else if(v == zh_edit){
				zh_edit.setText(zh_entity.getFullName());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean canScan() {
		return false;
	}

	@Override
	public void disposeLastNextData(TableEntity te) {
		bilid = Integer.parseInt(te.getBilID());
		ldrq_date.setText(te.getDate());
		
		jsr_entity.setFullName(te.geteFullName());
		jsr_entity.setId(te.geteID());
		jsr_edit.setText(te.geteFullName());
		
		zy_edit.setText(te.getSummary());
		djbh_edit.setText(te.getNumber());
		
		ck_entity.setFullName(te.getsFullName());
		ck_entity.setId(te.getsID());
		
		fhck_entity.setFullName(te.getsFullName2());
		fhck_entity.setId(te.getsID2());
		
		dw_entity.setFullName(te.getuFullName());
		dw_entity.setId(te.getuID());
		dw_edit.setText(te.getuFullName());
		
		getAllCount(money_edit);
		getZhName(zh_edit);
	}
	
	@Override
	public String getNumber() {
		return djbh_edit.getText().toString();
	}
	
	@Override
	public String getPrintValue() {
		String title = money_type.equals(MONEY_FKD)?"付款":"收款";
		return getString(R.string.money_print_value,
				getSupportActionBar().getTitle(),
				ldrq_date.getText().toString(),
				djbh_edit.getText().toString(),
				title,dw_entity.getFullName(),
				jsr_entity.getFullName(),
				title,money_edit.getText().toString().equals("")?"0":money_edit.getText().toString(),
				title,zh_edit.getText().toString(),
				zy_edit.getText().toString()
				);
	}

    private static final int MENU_SAVE = 10002;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        item = menu.add(0,MENU_SAVE,0,"保存");
        item.setIcon(android.R.drawable.ic_menu_save);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        if(orderCheck){
            item.setTitle("审核");
        }else{
            if(!isEdit){
                item.setVisible(false);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == MENU_SAVE){
            if(orderCheck){
                checkOrder();
                return true;
            }
            if(fromDraft){
                Toast.makeText(this,"该单据仅供浏览，不能提交",2000).show();
                return true;
            }
            //提交表单
            try {
                if(!validate()){
                    return true;
                }
//				submit();
                validateNumber();
            } catch (Exception e) {
                Toast.makeText(this, "添加失败！", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
