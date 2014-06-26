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
package com.jetsoft.activity;

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
import com.jetsoft.activity.draft.DraftListActivity;
import com.jetsoft.adapter.DJPagerAdapter;
import com.jetsoft.adapter.ProSelectAdapter;
import com.jetsoft.entity.BuMenEntity;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.JingShouRenEntity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;
/**
 * 进货订单
 * 
 *
 * @version 1.0
 * @author Administrator
 */
public class TabHostActivity extends BaseActivity implements OnClickListener{

	//每行layout
	private RelativeLayout ldrq_layout,wldw_action,jsr_action,bm_action,ck_action,dhrq_action,fkzh_action;
	
	public TextView dhrq_date,ldrq_date;
	
	public EditText bm_edit,ck_edit,jsr_edit,dw_edit,zh_edit,zy_edit,fkje_edit;
	
	public EditText ldbh_edit;
	
	private ProSelectAdapter psa;
	
	private ListView selectProList;
	
	private ImageView dw_add,jsr_add,bm_add,ck_add,zh_add;
	
	private TextView pro_num_tv,pro_count_b_tv,pro_count_a_tv;
	
	float pro_num;
	
	float pro_count_a,pro_count_b;
	
	private SaveTablesForm form = new SaveTablesForm();

    ViewPager pager;
    boolean fromDraft;

    TableEntity te = null;

    MenuItem item;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_in);

        getSupportActionBar().setTitle("进货订单");
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
		nBilType = StaticValues.ORDER_IN_BILTYPE;

        pager =  (ViewPager) findViewById(R.id.dj_pager);
        int[] pagers = new int[]{R.layout.order_in_layout,R.layout.product_list};
        DJPagerAdapter pagerAdapter = new DJPagerAdapter(this,pagers);
        pager.setAdapter(pagerAdapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        /**
         * 上一张下一张的button的处理
         */
        LastNextClickListener lncl = new LastNextClickListener(StaticValues.ORDER_IN_BILTYPE);
        last = (Button) findViewById(R.id.last_page);
        next = (Button) findViewById(R.id.next_page);
        last.setOnClickListener(lncl);
        next.setOnClickListener(lncl);

        /**
		 * 注册到蓝牙扫描填值类
		 */
		sv.registActivity(this);
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
            intent.setClass(TabHostActivity.this, ChooseProductAcitivity.class);
            intent.putExtra("entity", selectProducts.get(arg2));
            intent.putExtra("index", arg2);
            intent.putExtra("RightCost", application.getRightCost());
            intent.putExtra("nBilType", nBilType);
            intent.putExtra("szSid", ck_entity.getId());
            intent.putExtra("szUid", dw_entity.getId());
            startActivityForResult(intent, EntityListActivity.EDIT_PRODUCT);
            return;
        }
    }
	
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
				selectProducts.addAll((Collection<? extends ProductEntity>) entity);
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
			bm_entity= (BuMenEntity) data.getSerializableExtra("entity");
			bm_edit.setText(bm_entity.getFullName());
		}else if(requestCode == EntityListActivity.CK_RE_CODE){
			ck_entity = (CangKuEntity) data.getSerializableExtra("entity");
			ck_edit.setText(ck_entity.getFullName());
		}else if(requestCode == EntityListActivity.ZH_RE_CODE){
			fkzh_entity = (ZhangHuEntity) data.getSerializableExtra("entity");
			zh_edit.setText(fkzh_entity.getFullName());
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
	/**
	 * 更新商品的信息
	 */
	public void updateProNum(){
		/**
		 * 数量
		 * 计算折扣价格
		 * 总金额
		 */
		pro_num = 0;
		pro_count_b = 0;
		pro_count_a = 0;
		
		float tax_total=0;
		try{
			for(ProductEntity entity : selectProducts){
				float price_des = 0;
					price_des = Float.parseFloat(StaticValues.format(entity.getPriceRec(), application.getNumDigit()))*entity.getNumUnit1()*Float.parseFloat(StaticValues.format(entity.getDisRec(), application.getNumDigit()));
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
			if(application.getRightCost()==1){
				pro_count_a_tv.setText(StaticValues.format(pro_count_a, application.getNumDigit()));
			}else{
				pro_count_a_tv.setText("*");
			}
		}
		
		if(pro_count_b_tv!=null){
			if(application.getRightCost()==1){
				pro_count_b_tv.setText(StaticValues.format(pro_count_b, application.getNumDigit()));
			}else{
				pro_count_b_tv.setText("*");
			}
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
		}else if(v == fkzh_action){
			selectEntity(EntityListActivity.ZH_RE_CODE,zh_edit.getText().toString());
		}else if(v == returnButton){
			returnMenu();
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
		//供应商，客户标示。进货订单，进货单，进货退货单传0，销售订单，销售单，销售退货单传1，其他单据传2
		if(requestCode == EntityListActivity.DW_RE_CODE){
			intent.putExtra("isClient", "0");
		}
		intent.putExtra("type", requestCode);
		intent.putExtra("search", searchString);
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void onBackPressed() {
		if(selectProducts.size()>0 && isEdit){
			returnMenu();
		}else{
			super.onBackPressed();
		}
	}

	/**
	 * 提交表单
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void submit() throws Exception{
		super.submit();
		String action = getString(R.string.order_sale_action);
		
		params.clear();
		//构建表单数据,35个属性
		form.setDate(ldrq_date.getText().toString());
		form.setNumber(ldbh_edit.getText().toString());
		form.setBilType(nBilType);
		form.setSaveDate(Utils.getFormatDate(0,"yyyy-MM-dd HH:mm:ss.SSS"));
		
		String summary = zy_edit.getText().toString();
//		if(summary == null || "".equals(summary)){
//			summary = getString(R.string.default_summary);
//		}else{
//			summary = summary+getString(R.string.default_summary);
//		}
		form.setSummary(getString(R.string.default_summary)+summary);
		form.setMemo(getString(R.string.default_summary));
		form.setuID(dw_entity.getId());
		form.seteID(jsr_entity.getId());
		form.setsID(ck_entity.getId());
		form.setsID2("");
		form.setPeriod(application.getPeriod());
		form.setInputID(application.getUserEntity().getId());
		form.setDraft("2");
		form.setdID(bm_entity.getId());
		form.setbID("0000100001");
		
//		params.put("amount", amount+"");
//		params.put("countID", fkzh_entity.getId());
//		params.put("amountInput", fkje_edit.getText().toString());
		
		Field[] fields = form.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			fields[i].setAccessible(true);
			params.put(fields[i].getName(), (String)fields[i].get(form));
		}
		
		params.put("operatorName", application.getUserEntity().getEfullName());
		params.put("szOperCode", application.getUserEntity().geteUserCode());
		params.put("bilid", bilid+"");
		params.put("toDate", dhrq_date.getText().toString());
		
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
			params.put("products["+i+"].date", Utils.getFormatDate(0));
			params.put("products["+i+"].memo", product.getMemo());
			params.put("products["+i+"].kind", "1");
			params.put("products["+i+"].uRateBil", product.getuRateBil());
			params.put("products["+i+"].promoID", product.getPromoID());
			params.put("products["+i+"].unitCode", product.getUnitCode());
		}
		
		Utils.showProgressDialog("正在提交表单，请稍候……",this);
		Intent intent = new Intent();
		intent.setClass(this, TabHostActivity.class);
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
		if(ck_entity.getFullName() == null  || ck_entity.getFullName().equals("")){
			Toast.makeText(this, "请选择仓库！", Toast.LENGTH_SHORT).show();
			return false;
		}
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
		
		dhrq_date.setText(te.getGatheringDate());
		
		zy_edit.setText(te.getSummary());
		ldbh_edit.setText(te.getNumber());
		
		ck_entity.setFullName(te.getsFullName());
		ck_entity.setId(te.getsID());
		ck_edit.setText(te.getsFullName());
		
		fhck_entity.setFullName(te.getsFullName2());
		fhck_entity.setId(te.getsID2());
		
		dw_entity.setFullName(te.getuFullName());
		dw_entity.setId(te.getuID());
		dw_edit.setText(te.getuFullName());
		
		dhrq_date.setText(te.getToDate());
		
		psa.notifyDataSetChanged();
		updateProNum();
	}
	
	@Override
	public String getNumber() {
		return ldbh_edit.getText().toString();
	}
	
	@Override
	public String getPrintValue() {
		return getString(R.string.order_in_print_value, 
				ldrq_date.getText().toString(),
				ldbh_edit.getText().toString(),
				jsr_entity.getFullName(),
				dw_entity.getFullName(),
				ck_entity.getFullName(),
				zy_edit.getText().toString(),
				getPrintProducts(),
				pro_count_a_tv.getText().toString(),
				pro_count_b_tv.getText().toString()
				);
	}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        item = menu.add("保存");
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
        if(item.getTitle().equals("保存")) {
            if (orderCheck) {
                checkOrder();
                return true;
            }
            if(fromDraft){
                Toast.makeText(this,"该单据仅供浏览，不能提交",2000).show();
                return true;
            }
            try {
                if (selectProducts.size() == 0) {
                    Toast.makeText(this, "没有选择任何商品，请添加商品。", Toast.LENGTH_SHORT).show();
                    pager.setCurrentItem(1);
                    return true;
                }
                //验证没有通过
                if (!validate()) {
                    return true;
                }
                validateNumber();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView(int postion,View parentView){
        if(postion == 0){
            ldbh_edit = (EditText) parentView.findViewById(R.id.ldbh_edit);
            if(!fromDraft){
                getDanJuBianHao(ldbh_edit,nBilType);
            }
            ck_edit = (EditText) parentView.findViewById(R.id.shck_edit);
            ck_edit.setText(ck_entity.getFullName());
            ck_edit.addTextChangedListener(new BaseOnTextChangeListener(ck_edit,BaseOnTextChangeListener.CK_KEY));
            bm_edit = (EditText) parentView.findViewById(R.id.bm_edit);
            bm_edit.addTextChangedListener(new BaseOnTextChangeListener(bm_edit,BaseOnTextChangeListener.BM_KEY));
            dw_edit = (EditText) parentView.findViewById(R.id.wldw_edit);
            dw_edit.addTextChangedListener(new BaseOnTextChangeListener(dw_edit,BaseOnTextChangeListener.DW_KEY));
            dw_edit.setText(dw_entity.getFullName());
            dw_edit.requestFocus();
            jsr_edit = (EditText) parentView.findViewById(R.id.jsr_edit);
            jsr_edit.addTextChangedListener(new BaseOnTextChangeListener(jsr_edit,BaseOnTextChangeListener.JSR_KEY));

            bm_edit.setOnFocusChangeListener(this);
            dw_edit.setOnFocusChangeListener(this);
            jsr_edit.setOnFocusChangeListener(this);
            ck_edit.setOnFocusChangeListener(this);


            //设置经手人的默认值
            jsr_edit.setText(jsr_entity.getFullName());

            zy_edit = (EditText) parentView.findViewById(R.id.zy_edit);

            /**
             * 录单日期处理
             */
            ldrq_date = (TextView) parentView.findViewById(R.id.ldrq_edit);
            ldrq_date.setText(DateFormat.format("yyyy-MM-dd", new Date()));
            /**
             * 到货日期处理
             */
            dhrq_date = (TextView) parentView.findViewById(R.id.dhrq_edit);
            dhrq_date.setText(DateFormat.format("yyyy-MM-dd", new Date()));

            ldrq_layout = (RelativeLayout) parentView.findViewById(R.id.ldrq_action);
            ldrq_layout.setOnClickListener(this);

            //选择往来单位
            wldw_action = (RelativeLayout) parentView.findViewById(R.id.wldw_action);
            wldw_action.setOnClickListener(this);
            //选择经手人
            jsr_action = (RelativeLayout) parentView.findViewById(R.id.jsr_action);
            jsr_action.setOnClickListener(this);
            //选择部门
            bm_action = (RelativeLayout) parentView.findViewById(R.id.bm_action);
            bm_action.setOnClickListener(this);
            //选择到货日期
            dhrq_action = (RelativeLayout) parentView.findViewById(R.id.dhrq_action);
            dhrq_action.setOnClickListener(this);

            //仓库
            ck_action = (RelativeLayout) parentView.findViewById(R.id.ck_action);
            ck_action.setOnClickListener(this);

            dw_add = (ImageView) parentView.findViewById(R.id.dw_add);
            dw_add.setOnClickListener(this);
            jsr_add = (ImageView) parentView.findViewById(R.id.jsr_add);
            jsr_add.setOnClickListener(this);
            ck_add = (ImageView) parentView.findViewById(R.id.ck_add);
            ck_add.setOnClickListener(this);

            bm_add = (ImageView) parentView.findViewById(R.id.bm_add);
            bm_add.setOnClickListener(this);
        }else{
            //已经选择的商品列表
            selectProList = (ListView)parentView.findViewById(R.id.prolist);
            psa = new ProSelectAdapter(this,selectProducts,parentView.findViewById(R.id.select_view));
            selectProList.setAdapter(psa);
            selectProList.setOnItemClickListener(new ProductInfoListener());

            fkje_edit = (EditText) parentView.findViewById(R.id.je);

            pro_num_tv = (TextView) parentView.findViewById(R.id.sum);
            pro_count_a_tv = (TextView) parentView.findViewById(R.id.count_a);
            pro_count_b_tv = (TextView) parentView.findViewById(R.id.count_b);

            zh_edit = (EditText) parentView.findViewById(R.id.fkzh);
            zh_edit.addTextChangedListener(new BaseOnTextChangeListener(zh_edit,BaseOnTextChangeListener.FKZH_KEY));
            //选择付款账户
            fkzh_action = (RelativeLayout) parentView.findViewById(R.id.fkzh_action);
            fkzh_action.setOnClickListener(this);

            zh_add = (ImageView) parentView.findViewById(R.id.zh_add);
            zh_add.setOnClickListener(this);

            zh_edit.setOnFocusChangeListener(this);

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
                fkje_edit.setEnabled(false);
                ldbh_edit.setEnabled(false);
            }
        }
    }
}