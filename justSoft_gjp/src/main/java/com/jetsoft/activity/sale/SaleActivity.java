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
package com.jetsoft.activity.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.R;
import com.jetsoft.activity.BaseActivity;
import com.jetsoft.activity.ChooseProductAcitivity;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.activity.TiaoOrderActivity;
import com.jetsoft.activity.draft.DraftListActivity;
import com.jetsoft.adapter.DJPagerAdapter;
import com.jetsoft.adapter.ProSelectAdapter;
import com.jetsoft.entity.BuMenEntity;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.JingShouRenEntity;
import com.jetsoft.entity.OrderEntity;
import com.jetsoft.entity.ProductEntity;
import com.jetsoft.entity.SaveTablesForm;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.entity.ZhangHuEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import com.viewpagerindicator.TabPageIndicator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class SaleActivity extends BaseActivity implements OnClickListener,OnEditorActionListener{
	
	private TextView nv_title;
	//每行layout
	private RelativeLayout ldrq_layout,wldw_action,jsr_action,bm_action,ck_action,dhrq_action,zh_action;
	
	public TextView dhrq_date,ldrq_date;
	
	public EditText bm_edit,ck_edit,jsr_edit,dw_edit,zh_edit,zy_edit,je_edit,youhui_je_edit,dj_edit;
	
	public EditText ldbh_edit;
	
	private ProSelectAdapter psa;
	
	private ListView selectProList;
	
	private ImageView dw_add,jsr_add,bm_add,ck_add,zh_add;
	
	private TextView pro_num_tv,pro_count_b_tv,pro_count_a_tv;
	
	private SaveTablesForm form = new SaveTablesForm();
	
	/**
	 * 销售订单
	 */
	public static final String SALE_ORDER = "sale_order";
	/**
	 * 销售单
	 */
	public static final String SALE_NOTE = "sale_note";
	/**
	 * 销售退货单
	 */
	public static final String SALE_RETURN = "sale_return";
	
	private String sale_type = SALE_ORDER;
	
	private OrderEntity orderEntity;
	
	boolean fromDraft = false;

    ViewPager pager;

    TableEntity te = null;

    MenuItem item,tiao_item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_in);
		nBilType = StaticValues.SALE_IN_BILTYPE;
		
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
		
		sale_type = getIntent().getStringExtra("sale_type");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View proListLayout = null;
        int[] layouts = null;
        if(sale_type.equals(SALE_ORDER)){
        	getSupportActionBar().setTitle("销售订单");
        	nBilType = StaticValues.SALE_IN_BILTYPE;
            layouts = new int[]{R.layout.sale_order_in_layout,R.layout.sale_order_in_product_list};
        }else if(sale_type.equals(SALE_NOTE)){
        	nBilType = StaticValues.SALE_BILTYPE;
            getSupportActionBar().setTitle("销售单");
            layouts = new int[]{R.layout.sale_note_in_layout,R.layout.sale_note_in_product_list};
        }else if(sale_type.equals(SALE_RETURN)){
        	nBilType = StaticValues.SALE_RE_BILTYPE;
            getSupportActionBar().setTitle("销售退货");
            layouts = new int[]{R.layout.sale_return_in_layout,R.layout.sale_return_in_product_list};
        }
        
        pager = (ViewPager) findViewById(R.id.dj_pager);
        DJPagerAdapter pagerAdapter = new DJPagerAdapter(this,layouts);
        pager.setAdapter(pagerAdapter);

        TabPageIndicator tabPageIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        tabPageIndicator.setViewPager(pager);
        /**
		 * 注册到蓝牙扫描填值类
		 */
		sv.registActivity(this);
		
		LastNextClickListener lncl = new LastNextClickListener(nBilType);
		last = (Button) findViewById(R.id.last_page);
		next = (Button) findViewById(R.id.next_page);
		last.setOnClickListener(lncl);
		next.setOnClickListener(lncl);
		

	}

    /**
	 * 点击添加商品时跳转页面
	 * @version 1.0
	 * @author Administrator
	 */
	class ProductInfoListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
            //添加商品

            if (!isEdit) {
                return;
            }
            Intent intent = new Intent();
            intent.setClass(SaleActivity.this, ChooseProductAcitivity.class);
            intent.putExtra("entity", selectProducts.get(arg2));
            intent.putExtra("index", arg2);
            intent.putExtra("nBilType", nBilType);
            intent.putExtra("szSid", ck_entity.getId());
            intent.putExtra("szUid", dw_entity.getId());
            startActivityForResult(intent, EntityListActivity.EDIT_PRODUCT);
            return;
        }
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setHibernation(false);
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == EntityListActivity.PR_RE_CODE){
			Serializable entity = data.getSerializableExtra("entity");
			if(entity instanceof ProductEntity){
				selectProducts.add((ProductEntity) entity);
			}else if(entity instanceof ArrayList){
				Serializable order = data.getSerializableExtra("orderEntity");
				if(order!=null && order instanceof OrderEntity){
					orderEntity = (OrderEntity) order;
					dw_entity.setId(orderEntity.getUid());
					dw_entity.setFullName(orderEntity.getuFullName());
					dw_entity.setPersonCode(orderEntity.getUPersonCode());
					dw_edit.setText(dw_entity.getFullName());
					
					jsr_entity.setId(orderEntity.geteID());
					jsr_entity.setFullName(orderEntity.geteFullName());
					jsr_edit.setText(jsr_entity.getFullName());
					
					ck_entity.setId(orderEntity.getSid());
					ck_entity.setFullName(orderEntity.getsFullName());
					ck_entity.setPersonCode(orderEntity.getSPersonCode());
					ck_edit.setText(ck_entity.getFullName());
					selectProducts.clear();
					selectProducts.addAll((Collection<? extends ProductEntity>) entity);
					pager.setCurrentItem(1);
				}else{
					selectProducts.addAll((Collection<? extends ProductEntity>) entity);
				}
			}
			psa.notifyDataSetChanged();
			updateProNum();
		}else if(requestCode == EntityListActivity.DW_RE_CODE){
			dw_entity = (DanWeiEntity) data.getSerializableExtra("entity");
			dw_edit.setText(dw_entity.getFullName());
		}else if(requestCode == EntityListActivity.JSR_RE_CODE){
			 jsr_entity = (JingShouRenEntity) data.getSerializableExtra("entity");
			jsr_edit.setText(jsr_entity.getFullName());
		}else if(requestCode == EntityListActivity.BM_RE_CODE){
			 bm_entity = (BuMenEntity) data.getSerializableExtra("entity");
			bm_edit.setText(bm_entity.getFullName());
		}else if(requestCode == EntityListActivity.CK_RE_CODE){
			ck_entity = (CangKuEntity) data.getSerializableExtra("entity");
			ck_edit.setText(ck_entity.getFullName());
		}else if(requestCode == EntityListActivity.ZH_RE_CODE){
			fkzh_entity = (ZhangHuEntity) data.getSerializableExtra("entity");
			zh_edit.setText(fkzh_entity.getFullName());
			mulitZh.clear();
		}else if(requestCode == EntityListActivity.EDIT_PRODUCT){
			int index = data.getIntExtra("index", 0);
			//商品删除
			if(resultCode == EntityListActivity.DELETE_PRODUCT){
				selectProducts.remove(index);
			}else if(resultCode == EntityListActivity.COPY_PRODUCT){
				if(index+1>=selectProducts.size()){
					selectProducts.addLast( (ProductEntity)data.getSerializableExtra("entity"));
				}else{
					selectProducts.add( index+1,(ProductEntity)data.getSerializableExtra("entity"));
				}
			}else{
				//商品编辑
				selectProducts.set(index, (ProductEntity)data.getSerializableExtra("entity"));
			}
			psa.notifyDataSetChanged();
			updateProNum();
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
		}else if(v == ck_action){
			//跳转仓库选择界面
			selectEntity(EntityListActivity.CK_RE_CODE,ck_edit.getText().toString());
		}else if(v == dhrq_action){
			showLuDanRiQi(dhrq_date);
		}else if(v == zh_action){
			selectEntity(EntityListActivity.ZH_RE_CODE,zh_edit.getText().toString());
		}else if(v == save_button){

		}else if(v == returnButton){
			returnMenu();
		}else if(v == je_more){
			showJeLayout(fkzh_entity,je_edit,zh_edit);
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
		//如果是销售订单或者是销售单
		//将账户设置为收款账户
		if(requestCode == EntityListActivity.ZH_RE_CODE && (sale_type.equals(SALE_ORDER) || sale_type.equals(SALE_NOTE))){
			intent.putExtra("nBiltype", StaticValues.SK_BILTYPE);
		}
		//供应商，客户标示。进货订单，进货单，进货退货单传0，销售订单，销售单，销售退货单传1，其他单据传2
		if(requestCode == EntityListActivity.DW_RE_CODE){
			intent.putExtra("isClient", "1");
		}
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void onBackPressed() {
		//后退键屏蔽
		if(selectProducts.size()>0 && isEdit){
            returnMenu();
		}else{
			super.onBackPressed();
		}
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
	 * 更新商品的信息
	 */
	public void updateProNum(){
		/**
		 * 数量
		 * 计算折扣价格
		 * 总金额
		 */
		int pro_num = 0;
		float pro_count_b = 0;
		float pro_count_a = 0;
		
		float tax_total=0;
		try{
			for(ProductEntity entity : selectProducts){
				float price_des = Float.parseFloat(StaticValues.format(entity.getPriceRec(), application.getNumDigit()))*entity.getNumUnit1()*Float.parseFloat(StaticValues.format(entity.getDisRec(), application.getNumDigit()));
				float tax = entity.getTax();
				tax = (tax>0&&tax<1)?tax:(tax/100);
				float tax_price = price_des*tax;
				pro_num += entity.getNumUnit1();
				pro_count_b += Float.parseFloat(StaticValues.format(entity.getPriceRec(), application.getNumDigit()))*entity.getNumUnit1();
				pro_count_a += price_des;
				tax_total += (price_des+tax_price);
			}
			form.setTax_total(tax_total+"");
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(this, "商品价格数据错误！", Toast.LENGTH_SHORT).show();
		}
		amount = pro_count_a;
		
		if(pro_num_tv!=null){
			pro_num_tv.setText(StaticValues.format(pro_num, application.getNumDigit()));
		}
		
		if(pro_count_a_tv!=null){
			pro_count_a_tv.setText(StaticValues.format(pro_count_a, application.getNumDigit()));
		}
		
		if(pro_count_b_tv!=null){
			pro_count_b_tv.setText(StaticValues.format(pro_count_b, application.getNumDigit()));
		}
	}
	
	/**
	 * 提交表单
	 */
	public void submit() throws Exception{
		super.submit();
		
		 if(sale_type.equals(SALE_RETURN)
//				 ||sale_type.equals(SALE_ORDER)
				 ){
			application.setShck(ck_entity);
			form.setsID("");
			form.setsID2(ck_entity.getId());
		 }else{
			application.setFhck(ck_entity);
			form.setsID(ck_entity.getId());
			form.setsID2("");
		 }
		
		String action = getString(R.string.order_sale_action);
		
		params.clear();
		//构建表单数据
		form.setDate(ldrq_date.getText().toString());
		form.setNumber(ldbh_edit.getText().toString());
		form.setBilType(nBilType);
		form.setSaveDate(Utils.getFormatDate(0,"yyyy-MM-dd HH:mm:ss.SSS"));
		
		String summary = zy_edit.getText().toString();
//		if(summary == null || "".equals(summary)){
//			summary = getString(R.string.default_summary);
//		}else{
//			summary = summary +getString(R.string.default_summary);
//		}
		form.setSummary(getString(R.string.default_summary)+summary);
		if(dj_edit!=null){
			form.setMemo(dj_edit.getText().toString());
		}else{
			form.setMemo(getString(R.string.default_summary));
		}
		form.setuID(dw_entity.getId());
		form.seteID(jsr_entity.getId());
		form.setPeriod(application.getPeriod());
		form.setInputID(application.getUserEntity().getId());
		form.setDraft("2");
		form.setdID(bm_entity.getId());
		form.setbID("0000100001");
		params.put("countID", formatZhangHu(fkzh_entity));
		params.put("amountInput", formatCount(je_edit.getText().toString()));
		
		Field[] fields = form.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			fields[i].setAccessible(true);
			params.put(fields[i].getName(), (String)fields[i].get(form));
		}
//		if(sale_type.equals(SALE_NOTE) && orderEntity!=null){
			params.put("sourceBilid",orderEntity!=null? orderEntity.getBilID():bilid+"");
			params.put("sourceBilcode", StaticValues.SALE_IN_BILTYPE);
//		}
		
		if(sale_type.equals(SALE_NOTE) || sale_type.equals(SALE_RETURN)){
			params.put("disCountAmount", youhui_je_edit.getText().toString());
		}
		
		params.put("operatorName", application.getUserEntity().getEfullName());
		params.put("szOperCode", application.getUserEntity().geteUserCode());
		params.put("bilid", bilid+"");
		if(dhrq_date!=null){
			params.put("toDate", dhrq_date.getText().toString());
		}
		
		for(int i=0;i<selectProducts.size();i++){
			ProductEntity product = selectProducts.get(i);
			params.put("products["+i+"]._py", product.get_py());
			params.put("products["+i+"].area", product.getArea());
			params.put("products["+i+"].coseKind", product.getCoseKind());
			params.put("products["+i+"].fullName", product.getFullName());
			params.put("products["+i+"].id", product.getId());
			params.put("products["+i+"].parID", product.getParID());
			params.put("products["+i+"].personCode", product.getPersonCode());
			params.put("products["+i+"].sonNum", product.getSonNum());
			params.put("products["+i+"].standard", product.getStandard());
			params.put("products["+i+"].type", product.getType());
			params.put("products["+i+"].uniltCode", product.getUnitCode());
			params.put("products["+i+"].unitl", "0");
			params.put("products["+i+"].urate", product.getuRate0());
			params.put("products["+i+"].num", product.getNum()+"");
			params.put("products["+i+"].price", product.getPriceRec()+"");
			params.put("products["+i+"].zheKou", product.getDisRec()+"");
			params.put("products["+i+"].largess", product.getLargess());
			params.put("products["+i+"].tax", product.getTax()+"");
			params.put("products["+i+"].numUnit1", product.getNumUnit1()+"");
			params.put("products["+i+"].date", product.getDate());
			params.put("products["+i+"].memo", product.getMemo());
			params.put("products["+i+"].kind", "1");
			params.put("products["+i+"].uRateBil", product.getuRateBil());
			params.put("products["+i+"].promoID", product.getPromoID());
//			if(product.isOrderPro()){
				params.put("products["+i+"].sourceBilcode", product.getBilCode());
				params.put("products["+i+"].sourceBilid", product.getBilID());
//			}
			params.put("products["+i+"].unitCode", product.getUnitCode());
		}
		
		Utils.showProgressDialog("正在提交表单，请稍候……",this);
		Intent intent = new Intent();
		intent.setClass(SaleActivity.this, SaleActivity.class);
		intent.putExtra("sale_type", sale_type);
		CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action,params, handler, new SubmitExcute(intent));
		Thread t = new Thread(cs);
		t.start();
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
		if(ck_entity.getFullName() == null){
			Toast.makeText(this, "请选择仓库！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(sale_type.equals(SALE_ORDER)){
			//验证商品
			for(ProductEntity entity : selectProducts){
				if(entity.getNumUnit1() == 0){
					Toast.makeText(this, entity.getFullName()+"的数量不能为0!", Toast.LENGTH_SHORT).show();
					return false;
				}
				if(Float.parseFloat(entity.getPriceRec()) == 0 && entity.getLargess().equals("0")){
					Toast.makeText(this, entity.getFullName()+"的单价不能为0!", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		if(sale_type.equals(SALE_NOTE) || sale_type.equals(SALE_RETURN)){
			try{
				if(!je_edit.getText().toString().equals("") && Float.parseFloat(je_edit.getText().toString())!=0){
					if(fkzh_entity.getId()==null && mulitZh.size()==0){
						Toast.makeText(this, "请选择付款账户！", Toast.LENGTH_SHORT).show();
						return false;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				Toast.makeText(this, "金额数据错误！", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void setScanValue(String value) {
		//当在第一个页面时扫描无反应
		if(pager.getCurrentItem() == 0){
			return;
		}
		setHibernation(true);
		psa.searchString = value;
		psa.notifyDataSetChanged();
		psa.performSearch(value);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		//bm_edit,ck_edit,jsr_edit,dw_edit,zh_edit
		//得到焦点时不处理
		if(hasFocus){
			return;
		}
		try{
			//bm_edit,ck_edit,jsr_edit,dw_edit,zh_edit
			if(v == bm_edit){
				bm_edit.setText(bm_entity.getFullName());
			}else if(v == ck_edit){
				ck_edit.setText(ck_entity.getFullName());
			}else if(v == jsr_edit){
				jsr_edit.setText(jsr_entity.getFullName());
			}else if(v == dw_edit){
				dw_edit.setText(dw_entity.getFullName());
			}else if(v == zh_edit){
				zh_edit.setText(fkzh_entity.getFullName());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean canScan() {
		if(pager.getCurrentItem() == 1){
			return true;
		}
		return false;
	}

	@Override
	public void disposeLastNextData(TableEntity te) {
		bilid = Integer.parseInt(te.getBilID());
		ldrq_date.setText(te.getDate());
		
		jsr_entity.setFullName(te.geteFullName());
		jsr_entity.setId(te.geteID());
		jsr_edit.setText(te.geteFullName());
		
//		dhrq_date.setText(te.getGatheringDate());
		
		zy_edit.setText(te.getSummary());
		ldbh_edit.setText(te.getNumber());
		
		ck_entity.setFullName(te.getsFullName());
		ck_entity.setId(te.getsID());
		
		fhck_entity.setFullName(te.getsFullName2());
		fhck_entity.setId(te.getsID2());
		
		ck_edit.setText(te.getsFullName());
		
		dw_entity.setFullName(te.getuFullName());
		dw_entity.setId(te.getuID());
		dw_edit.setText(te.getuFullName());
		
		psa.notifyDataSetChanged();
//		je_edit.setText(je);
		if(youhui_je_edit!=null){
			youhui_je_edit.setText(te.getYouhui());
		}
		if(dhrq_date!=null){
			dhrq_date.setText(te.getToDate());
		}
		if(dj_edit!=null){
			dj_edit.setText(te.getMemo());
		}
		getAllCount(je_edit);
		getZhName(zh_edit);
		updateProNum();
	}
	
	@Override
	public String getNumber() {
		return ldbh_edit.getText().toString();
	}
	
	@Override
	public String getPrintValue() {
		if(sale_type.equals(SALE_ORDER)){
			//销售订单
			return getString(R.string.sale_in_print_value, 
					"销售订单",
					ldrq_date.getText().toString(),
					ldbh_edit.getText().toString(),
					jsr_entity.getFullName(),
					dw_entity.getFullName(),
					ck_entity.getFullName(),
					zy_edit.getText().toString(),
					dhrq_date.getText().toString(),
					getPrintProducts(),
					pro_num_tv.getText().toString(),
					pro_count_b_tv.getText().toString(),
					pro_count_a_tv.getText().toString()
					);
		}else if(sale_type.equals(SALE_NOTE)){
			//销售单
			return getString(R.string.sale_print_value,
					"销售单",
					ldrq_date.getText().toString(),
					ldbh_edit.getText().toString(),
					jsr_entity.getFullName(),
					dw_entity.getFullName(),
					ck_entity.getFullName(),
					zy_edit.getText().toString(),
					dhrq_date.getText().toString(),
					getPrintProducts(),
					pro_num_tv.getText().toString(),
					pro_count_a_tv.getText().toString(),
					youhui_je_edit.getText().toString(),
					je_edit.getText().toString(),
					zh_edit.getText().toString()
					);
		}else if(sale_type.equals(SALE_RETURN)){
			return getString(R.string.sale_re_print_value,
					ldrq_date.getText().toString(),
					ldbh_edit.getText().toString(),
					jsr_entity.getFullName(),
					dw_entity.getFullName(),
					ck_entity.getFullName(),
					zy_edit.getText().toString(),
					getPrintProducts(),
					pro_num_tv.getText().toString(),
					pro_count_a_tv.getText().toString(),
					youhui_je_edit.getText().toString(),
					je_edit.getText().toString(),
					zh_edit.getText().toString()
					);
		}
		return null;
	}
    private static final int MENU_TIAO = 10001;
    private static final int MENU_SAVE = 10002;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(sale_type.equals(SALE_NOTE)){
            tiao_item = menu.add(0,MENU_TIAO,0,"调订单");
            tiao_item.setIcon(R.drawable.ic_menu_find_holo_light);
            tiao_item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        item = menu.add(0,MENU_SAVE,1,"保存");
        item.setIcon(android.R.drawable.ic_menu_save);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

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
        if(item.getItemId() == MENU_TIAO){
            if(!bhShow && !fromDraft){
                Toast.makeText(this, "正在获取编号，请稍后！", Toast.LENGTH_SHORT).show();
                return true;
            }
            if(fromDraft){
                Toast.makeText(this,"该单据仅供浏览，不能提交",2000).show();
                return true;
            }
            Intent intent = new Intent();
            intent.setClass(this, TiaoOrderActivity.class);
            intent.putExtra("products", selectProducts);
            intent.putExtra("nBiltype", StaticValues.SALE_IN_BILTYPE);
            startActivityForResult(intent, EntityListActivity.PR_RE_CODE);
        }else if(item.getItemId() == MENU_SAVE){
            if(orderCheck){
                checkOrder();
                return true;
            }
            if(fromDraft){
                Toast.makeText(this,"该单据仅供浏览，不能提交",2000).show();
                return true;
            }
            try {
                if(selectProducts.size()==0){
                    Toast.makeText(this, "没有选择任何商品，请添加商品。", Toast.LENGTH_SHORT).show();
                    pager.setCurrentItem(1);
                    return true;
                }
                if(!validate()){
                    return true;
                }
//				submit();
                validateNumber();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView(int postion,View parentView){
        if(postion == 0){
            ldbh_edit = (EditText) findViewById(R.id.ldbh_edit);
            if(!fromDraft){
                getDanJuBianHao(ldbh_edit, nBilType);
            }

            ck_edit = (EditText) findViewById(R.id.ck_edit);
            ck_edit.setOnEditorActionListener(this);
            if(sale_type.equals(SALE_NOTE)||sale_type.equals(SALE_ORDER)){
                ck_edit.setText(fhck_entity.getFullName());
                ck_edit.addTextChangedListener(new BaseOnTextChangeListener(ck_edit,BaseOnTextChangeListener.FHCK_KEY));
            }else{
                ck_edit.setText(ck_entity.getFullName());
                ck_edit.addTextChangedListener(new BaseOnTextChangeListener(ck_edit,BaseOnTextChangeListener.CK_KEY));
            }
            bm_edit = (EditText) findViewById(R.id.bm_edit);
            bm_edit.setOnEditorActionListener(this);
            bm_edit.addTextChangedListener(new BaseOnTextChangeListener(bm_edit,BaseOnTextChangeListener.BM_KEY));
            dw_edit = (EditText) findViewById(R.id.wldw_edit);
            dw_edit.setText(dw_entity.getFullName());
            dw_edit.addTextChangedListener(new BaseOnTextChangeListener(dw_edit,BaseOnTextChangeListener.DW_KEY));
            dw_edit.setOnEditorActionListener(this);
            jsr_edit = (EditText) findViewById(R.id.jsr_edit);
            jsr_edit.addTextChangedListener(new BaseOnTextChangeListener(jsr_edit,BaseOnTextChangeListener.JSR_KEY));
            jsr_edit.setOnEditorActionListener(this);
            //设置经手人的默认值
            jsr_edit.setText(jsr_entity.getFullName());

            dj_edit = (EditText) findViewById(R.id.dj_edit);
            bm_edit.setOnFocusChangeListener(this);
            dw_edit.setOnFocusChangeListener(this);
            jsr_edit.setOnFocusChangeListener(this);
            ck_edit.setOnFocusChangeListener(this);

            /**
             * 录单日期处理
             */
            ldrq_date = (TextView) findViewById(R.id.ldrq_edit);
            ldrq_date.setText(DateFormat.format("yyyy-MM-dd", new Date()));
            /**
             * 到货日期处理
             */
            if(sale_type.equals(SALE_ORDER) || sale_type.equals(SALE_NOTE)){
                dhrq_date = (TextView) findViewById(R.id.dhrq_edit);
                dhrq_date.setText(DateFormat.format("yyyy-MM-dd", new Date()));
                //选择到货日期
                dhrq_action = (RelativeLayout) findViewById(R.id.dhrq_action);
                dhrq_action.setOnClickListener(this);
            }
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
            //仓库
            ck_action = (RelativeLayout) findViewById(R.id.ck_action);
            ck_action.setOnClickListener(this);

            dw_add = (ImageView) findViewById(R.id.dw_add);
            dw_add.setOnClickListener(this);
            jsr_add = (ImageView) findViewById(R.id.jsr_add);
            jsr_add.setOnClickListener(this);
            ck_add = (ImageView) findViewById(R.id.ck_add);
            ck_add.setOnClickListener(this);
            bm_add = (ImageView) findViewById(R.id.bm_add);
            bm_add.setOnClickListener(this);
            //单据打开时当前状态光标默认在往来单位字段上
            dw_edit.requestFocus();
        }else{
            //已经选择的商品列表添加适配器
            selectProList = (ListView) findViewById(R.id.prolist);
            psa = new ProSelectAdapter(this,selectProducts,findViewById(R.id.select_view));
            selectProList.setAdapter(psa);
            selectProList.setOnItemClickListener(new ProductInfoListener());

            zh_edit = (EditText) findViewById(R.id.zh);
            zh_edit.addTextChangedListener(new BaseOnTextChangeListener(zh_edit,BaseOnTextChangeListener.FKZH_KEY));
            zh_edit.setOnEditorActionListener(this);
            zy_edit = (EditText) findViewById(R.id.zy_edit);
            je_edit = (EditText) findViewById(R.id.je);
            if(sale_type.equals(SALE_NOTE)||sale_type.equals(SALE_RETURN)){
                youhui_je_edit = (EditText) findViewById(R.id.youhui_je);
                if(!isEdit){
                    youhui_je_edit.setEnabled(false);
                }
            }
            zh_edit.setOnFocusChangeListener(this);

            //选择付款账户
            zh_action = (RelativeLayout) findViewById(R.id.fkzh_action);
            zh_action.setOnClickListener(this);
            zh_add = (ImageView) findViewById(R.id.zh_add);
            zh_add.setOnClickListener(this);

            pro_num_tv = (TextView) findViewById(R.id.sum);
            pro_count_a_tv = (TextView) findViewById(R.id.count_a);
            pro_count_b_tv = (TextView) findViewById(R.id.count_b);

            je_more = (TextView) findViewById(R.id.je_more);
            if(je_more!=null){
                je_more.setOnClickListener(this);
            }

            if(fromDraft){
                disposeLastNextData(te);
            }
            if(!isEdit){
                bm_edit.setEnabled(false);
                dw_edit.setEnabled(false);
                jsr_edit.setEnabled(false);
                zh_edit.setEnabled(false);
                ck_edit.setEnabled(false);
                zy_edit.setEnabled(false);
                je_edit.setEnabled(false);
                ldbh_edit.setEnabled(false);

                if(dj_edit!=null){
                    dj_edit.setEnabled(false);
                }
            }
        }
    }
}