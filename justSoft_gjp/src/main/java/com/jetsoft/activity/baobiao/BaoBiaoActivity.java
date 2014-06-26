/**
 * 创建日期 2012-12-25
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
package com.jetsoft.activity.baobiao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.R;
import com.jetsoft.activity.BaseActivity;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.entity.BuMenEntity;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.entity.JingShouRenEntity;
import com.jetsoft.entity.ProductEntity;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.view.CustomSinnper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BaoBiaoActivity extends BaseActivity implements OnClickListener{
    //往来单位应收应付
    public static final int WLDW = 1;
    //现金银行
    public static final int XJYH = 2;
    //3.	进货汇总表
    public static final int JHHZ = 3;
    //4.	销售汇总表
    public static final int XSHZ = 4;
    //5.	库存状况表
    public static final int KCZK = 5;
    //6.	经营历程
    public static final int JYLC = 6;
    //7.	商品查询
    public static final int SPCX = 7;

    private EditText dw_edit,ck_edit,jsr_edit,bm_edit,pro_edit;

    private ImageView dw_action,ck_action,jsr_action,bm_action,pro_action;

    private RelativeLayout start_date,end_date;

    private TextView start_date_text,end_date_text;

    private Button search,nv_return;
    /**
     * 是否显示0行
     */
    private CheckBox showZero;

    private int flag;

//	private String dw_id,pr_id,ck_id,bm_id,jsr_id;

    private String szParGid = "";

    private ProductEntity pro_entity = new ProductEntity();

    private CustomSinnper sinnper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
        super.onCreate(savedInstanceState);

        flag = getIntent().getIntExtra("type", 1);
        switch (flag) {
            case WLDW:
                setContentView(R.layout.baobiao_search_wldw);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("单位应收应付");
                showZero = (CheckBox) findViewById(R.id.show_zero);
                break;
            case XJYH:
                setContentView(R.layout.search_list);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("现金银行");
                break;
            case JHHZ:
                setContentView(R.layout.baobiao_search_jhhz);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("商品进货汇总表");
                showZero = (CheckBox) findViewById(R.id.show_zero);
                break;
            case XSHZ:
                setContentView(R.layout.baobiao_search_xshz);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("商品销售汇总表");
                showZero = (CheckBox) findViewById(R.id.show_zero);
                break;
            case KCZK:
                setContentView(R.layout.baobiao_search_kczt);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("库存状况表");
                showZero = (CheckBox) findViewById(R.id.show_zero);
                break;
            case JYLC:
                setContentView(R.layout.baobiao_search_jylc);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("经营历程");
                sinnper = (CustomSinnper) findViewById(R.id.dj_type);
                String item[] = new String[]{"全部","销售单","销售退货","销售订单","付款单","收款单","进货单","进货退货单","进货订单","变价调拨单","同价调拨单"};
                List<String> valueList = new LinkedList<String>();
                valueList.add("");
                valueList.add(StaticValues.SALE_BILTYPE);
                valueList.add(StaticValues.SALE_RE_BILTYPE);
                valueList.add(StaticValues.SALE_IN_BILTYPE);
                valueList.add(StaticValues.FK_BILTYPE);
                valueList.add(StaticValues.SK_BILTYPE);
                valueList.add(StaticValues.ORDER_BILTYPE);
                valueList.add(StaticValues.ORDER_RE_BILTYPE);
                valueList.add(StaticValues.ORDER_IN_BILTYPE);
                valueList.add(StaticValues.BIANJIA_BILTYPE);
                valueList.add(StaticValues.TONGJIA_BILTYPE);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        R.layout.spinner_item, item);
                sinnper.setAdapter(adapter);
                sinnper.setValueList(valueList);
                break;
            case SPCX:
                setContentView(R.layout.baobiao_search_spcx);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("商品查询");
                break;
            default:
                break;
        }

        dw_edit = (EditText) findViewById(R.id.dw_edit);
        ck_edit = (EditText) findViewById(R.id.ck_edit);
        jsr_edit = (EditText) findViewById(R.id.jsr_edit);
        //设置经手人的默认值
//		UserEntity user = ((MyApplication)getApplication()).getUserEntity();
//		if(jsr_edit != null){
//			jsr_edit.setText(user.getEfullName());
//		}
//        jsr_id = user.getId();

        bm_edit = (EditText) findViewById(R.id.bm_edit);
        if(bm_edit!=null){
            bm_edit.setOnFocusChangeListener(this);
            bm_edit.addTextChangedListener(new BaseOnTextChangeListener(bm_edit, BaseOnTextChangeListener.BM_KEY));
        }
        if(dw_edit!=null){
            dw_entity = new DanWeiEntity();
            dw_edit.setOnFocusChangeListener(this);
            dw_edit.addTextChangedListener(new BaseOnTextChangeListener(dw_edit, BaseOnTextChangeListener.DW_KEY));
        }
        if(jsr_edit!=null){
            jsr_entity = new JingShouRenEntity();
            jsr_edit.setOnFocusChangeListener(this);
            jsr_edit.addTextChangedListener(new BaseOnTextChangeListener(jsr_edit, BaseOnTextChangeListener.JSR_KEY));
        }
        if(ck_edit!=null){
            ck_entity = new CangKuEntity();
            ck_edit.setOnFocusChangeListener(this);
            ck_edit.addTextChangedListener(new BaseOnTextChangeListener(ck_edit, BaseOnTextChangeListener.CK_KEY));
        }

        pro_edit = (EditText) findViewById(R.id.pro_edit);
        if(pro_edit!=null){
            pro_edit.setOnFocusChangeListener(this);
            pro_edit.addTextChangedListener(new MyTextTextWatcher(pro_edit));
        }

        start_date_text = (TextView) findViewById(R.id.start_date_text);
        if(start_date_text!=null){
            start_date_text.setText(DateFormat.format("yyyy-MM-dd", new Date()));
        }

        end_date_text = (TextView) findViewById(R.id.end_date_text);
        if(end_date_text!=null){
            end_date_text.setText(DateFormat.format("yyyy-MM-dd", new Date()));
        }

        dw_action = (ImageView) findViewById(R.id.dw_action);
        setViewClickListener(dw_action);
        ck_action = (ImageView) findViewById(R.id.ck_action);
        setViewClickListener(ck_action);
        jsr_action = (ImageView) findViewById(R.id.jsr_action);
        setViewClickListener(jsr_action);
        bm_action = (ImageView) findViewById(R.id.bm_action);
        setViewClickListener(bm_action);
        pro_action = (ImageView) findViewById(R.id.pro_action);
        setViewClickListener(pro_action);

        start_date = (RelativeLayout) findViewById(R.id.start_date);
        setViewClickListener(start_date);
        end_date = (RelativeLayout) findViewById(R.id.end_date);
        setViewClickListener(end_date);

        search = (Button) findViewById(R.id.search);
        setViewClickListener(search);

        nv_return = (Button) findViewById(R.id.nv_return);
        setViewClickListener(nv_return);

        if(dw_edit!=null){
            //单据打开时当前状态光标默认在往来单位字段上
            dw_edit.requestFocus();
        }

        /**
         * 注册到蓝牙扫描填值类
         */
        sv.registActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == nv_return){
            this.finish();
            return;
        }else if(v == dw_action){
            //选择单位
            selectEntity(EntityListActivity.DW_RE_CODE,dw_edit.getText().toString());
        }else if(v == ck_action){
            //选择仓库
            selectEntity(EntityListActivity.CK_RE_CODE,ck_edit.getText().toString());
        }else if(v == jsr_action){
            //跳转到经手人选择界面
            selectEntity(EntityListActivity.JSR_RE_CODE,jsr_edit.getText().toString());
        }else if(v == bm_action){
            //跳转部门选择界面
            selectEntity(EntityListActivity.BM_RE_CODE,bm_edit.getText().toString());
        }else if(v == pro_action){
            //跳转到选择商品界面
            selectEntity(EntityListActivity.PR_RE_CODE,pro_edit.getText().toString());
        }else if(v == start_date){
            //选择开始时间
            showChooseDate(start_date_text);
        }else if(v == end_date){
            //选择结束时间
            showChooseDate(end_date_text);
        }else if(v == search){
            Intent intent = new Intent();
            intent.setClass(this, BaoBiaoResultActivity.class);
            if("".equals(dw_entity.getId())){
                if(dw_edit!=null && !dw_edit.getText().toString().equals("")){
                    Toast.makeText(this, "请点击搜索选择一个具体的单位！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if("".equals(jsr_entity.getId())){
                if(jsr_edit!=null && !jsr_edit.getText().toString().equals("")){
                    Toast.makeText(this, "请点击搜索选择一个具体的经手人！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if("".equals(bm_entity.getId())){
                if(bm_edit!=null && !bm_edit.getText().toString().equals("")){
                    Toast.makeText(this, "请点击搜索选择一个具体的部门！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if("".equals(ck_entity.getId())){
                if(ck_edit!=null && !ck_edit.getText().toString().equals("")){
                    Toast.makeText(this, "请点击搜索选择一个具体的仓库！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(pro_entity!=null && "".equals(pro_entity.getId())){
                if(pro_edit!=null && !pro_edit.getText().toString().equals("")){
                    Toast.makeText(this, "请点击搜索选择一个具体的商品！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //进行查询
            switch (flag) {
                case WLDW:
                    intent.putExtra("dw", dw_entity.getId());
                    intent.putExtra("bIsZero", showZero.isChecked()?"1":"0");
                    break;
                case JHHZ:
                    intent.putExtra("dw", dw_entity.getId());
                    intent.putExtra("zy", jsr_entity.getId());
                    intent.putExtra("bm", bm_entity.getId());
                    intent.putExtra("ck", ck_entity.getId());
                    intent.putExtra("sd", start_date_text.getText().toString());
                    intent.putExtra("ed", end_date_text.getText().toString());
                    intent.putExtra("bIsZero", showZero.isChecked()?"1":"0");
                    break;
                case XSHZ:
                    intent.putExtra("dw", dw_entity.getId());
                    intent.putExtra("zy", jsr_entity.getId());
                    intent.putExtra("bm", bm_entity.getId());
                    intent.putExtra("ck", ck_entity.getId());
                    intent.putExtra("sd", start_date_text.getText().toString());
                    intent.putExtra("ed", end_date_text.getText().toString());
                    intent.putExtra("bIsZero", showZero.isChecked()?"1":"0");
                    break;
                case KCZK:
                    intent.putExtra("pr", pro_entity.getId());
                    intent.putExtra("ck", ck_entity.getId()==null?"":ck_entity.getId());
                    intent.putExtra("szParGid", szParGid);
                    intent.putExtra("bIsZero", showZero.isChecked()?"1":"0");
                    break;
                case JYLC:
                    intent.putExtra("dw", dw_entity.getId());
                    intent.putExtra("zy", jsr_entity.getId());
                    intent.putExtra("bm", bm_entity.getId());
                    intent.putExtra("ck", ck_entity.getId());
                    intent.putExtra("pr", pro_entity.getId());
                    intent.putExtra("dj_type", sinnper.getValue());
                    intent.putExtra("sd", start_date_text.getText().toString());
                    intent.putExtra("ed", end_date_text.getText().toString());
                    break;
                case SPCX:
                    if(pro_entity.getId().equals("")){
                        Toast.makeText(this, "没有选择商品!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    intent.putExtra("pr", pro_entity.getId());
                    intent.putExtra("product", pro_entity);
                    break;
                default:
                    return;
            }
            intent.putExtra("type", flag);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            return;
        }
        if(requestCode == EntityListActivity.PR_RE_CODE){
            Object o = data.getSerializableExtra("entity");
            if(o instanceof ProductEntity){
                pro_entity = (ProductEntity) o;
            }else if(o instanceof ArrayList){
                pro_entity =(ProductEntity) ((ArrayList)o).get(0);
            }
            pro_edit.setText(pro_entity.getFullName());
            try{
                if(Integer.parseInt(pro_entity.getSonNum())==0){
                    szParGid = pro_entity.getParID();
                    if(szParGid.equals("00000")){
                        szParGid = "";
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                szParGid = "";
            }
            if(flag == SPCX){
                Intent intent = new Intent();
                intent.setClass(this, BaoBiaoResultActivity.class);
                intent.putExtra("pr", pro_entity.getPersonCode());
                intent.putExtra("type", flag);
                intent.putExtra("product", pro_entity);
                startActivity(intent);
            }
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
        }
    }

    /**
     * 设置view的点击事件
     * @param clickView
     */
    public void setViewClickListener(View clickView){
        if(clickView==null){
            return;
        }
        clickView.setOnClickListener(this);
    }

    /**
     * 跳转到选择多列项的页面
     */
    public void selectEntity(int requestCode,String searchString){
        Intent intent = new Intent();
        intent.setClass(this, EntityListActivity.class);
        intent.putExtra("type", requestCode);
        intent.putExtra("search", searchString);
        intent.putExtra("pandian", true);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 显示选择录单日期的dialog
     */
    public void showChooseDate(final TextView tv){
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

    @Override
    public void updateProNum() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public void setScanValue(String value) {
        if(pro_edit != null){
            selectEntity(EntityListActivity.PR_RE_CODE,value);
        }
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
            }else if(v == pro_edit){
                pro_edit.setText(pro_entity.getFullName());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 监听界面上几个edittext上的文字变化
     *
     *
     * @version 1.0
     * @author Administrator
     */
    class MyTextTextWatcher implements TextWatcher{

        public EditText foucsText;

        public MyTextTextWatcher(EditText foucsText){
            this.foucsText = foucsText;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            try{
                if(foucsText == pro_edit && pro_edit.getText().toString().equals("")){
                    pro_entity = new ProductEntity();
                    Toast.makeText(BaoBiaoActivity.this, "商品已清空！", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public boolean canScan() {
        return true;
    }

    @Override
    public void disposeLastNextData(TableEntity te) {
    }

    @Override
    public String getNumber() {
        return "";
    }
}