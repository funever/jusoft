/**
 * 创建日期 2012-12-26
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jetsoft.R;
import com.jetsoft.activity.BaseActivity;
import com.jetsoft.activity.product.ProImgGridActivity;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.KeyValues;
import com.jetsoft.entity.ProductEntity;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 报表查询结果
 *
 * @version 1.0
 * @author Administrator
 */
public class BaoBiaoResultActivity extends BaseActivity implements OnClickListener,OnItemClickListener{

    private int type;
    /**
     * 请求url的参数
     */
    private HashMap<String, String> param = new HashMap<String, String>();
    /**
     * 返回数据的属性列表
     */
    private LinkedHashMap<String,String> data_attr_name = new LinkedHashMap<String, String>();
    /**
     * 返回数据的属性值列表
     */
    private List<LinkedHashMap<String,String>> dataList = new LinkedList<LinkedHashMap<String,String>>();
    /**
     * 保存经营历程的每行的biltype和bilid
     */
    private List<HashMap<String, String>> jylc_bil_Map = new LinkedList<HashMap<String,String>>();

    private MyApplication application;

    private ListView entityList;

    private String server;

    private String action;

    private SharedPreferences preferences;

    private Handler handler = new Handler();

    private ResultAdapter resultAdapter;

    private TextView list_progressing_title;

    private boolean haveSum = false;

    private LinearLayout pro_info;

    private Button img,price,pro_beizhu,pro_code;

    ProductEntity product;
    /**
     * 是否获取到价格参数
     */
    boolean hasPrice = false;

    PullToRefreshListView refreshList;

    int page = 1;
    /**
     * 返回上级的entity列表
     */
    LinkedList<HashMap<String, String>> lastEntitys = new  LinkedList<HashMap<String, String>>();
    /**
     * 经营历程选择的单据类型
     */
    String dj_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list_progressing_title = (TextView) findViewById(R.id.list_progressing_title);
        preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);

        server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
                + preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));

        application = (MyApplication) getApplication();

        refreshList = (PullToRefreshListView) findViewById(R.id.entity_list);
        entityList  = refreshList.getRefreshableView();
        refreshList.setMode(Mode.PULL_FROM_END);
        refreshList.setScrollingWhileRefreshingEnabled(false);

        resultAdapter = new ResultAdapter();
        entityList.setAdapter(resultAdapter);
        /**
         * 每行点击的监听
         */
        entityList.setOnItemClickListener(this);

        Intent intent = getIntent();
        /**
         * 获取请求类型
         */
        type = intent.getIntExtra("type", 0);
        switch (type) {
            case BaoBiaoActivity.JHHZ:
                getSupportActionBar().setTitle("商品进货汇总表");
                findViewById(R.id.baobiao_jhhz_title).setVisibility(View.VISIBLE);
                action = getString(R.string.jhhz_action);
                param.put("cMode", "Z");
                param.put("szGid", "00000");
                param.put("szUid", intent.getStringExtra("dw"));
                param.put("szEid", intent.getStringExtra("zy"));
                param.put("szDid", intent.getStringExtra("bm"));
                param.put("szSid", intent.getStringExtra("ck"));
                param.put("szBeginDate", intent.getStringExtra("sd"));
                param.put("szEndDate", intent.getStringExtra("ed"));
                param.put("operator", application.getUserEntity().getId());
                param.put("bIsZero", intent.getStringExtra("bIsZero"));

                data_attr_name.put("personCode", "商品编号");
                data_attr_name.put("fullName", "商品全名");
                data_attr_name.put("standard", "规格");
                data_attr_name.put("type", "型号");
                data_attr_name.put("area", "产地");
                data_attr_name.put("unitl", "基本单位");
                data_attr_name.put("num", "进货数量");
                data_attr_name.put("price", "进货单价");
                data_attr_name.put("amount", "进货金额");
                data_attr_name.put("rate", "进货比重");

                haveSum = true;
                //
                break;
            case BaoBiaoActivity.JYLC:
                getSupportActionBar().setTitle("经营历程");
                findViewById(R.id.baobiao_jylc_title).setVisibility(View.VISIBLE);
                action = getString(R.string.jylc_action);
                dj_type = intent.getStringExtra("dj_type");

                param.put("cMode", "Y");
                param.put("mode", "3");
                param.put("szUid", intent.getStringExtra("dw"));
                param.put("szEid", intent.getStringExtra("zy"));
                param.put("szDid", intent.getStringExtra("bm"));
                param.put("szSid", intent.getStringExtra("ck"));
                param.put("szGid", intent.getStringExtra("pr"));
                param.put("bilType", intent.getStringExtra("dj_type"));
                param.put("szSummary", "");
                param.put("szComment", "");
                param.put("szMemo", "");
                param.put("szBillNumber", "");
                param.put("szBeginDate", intent.getStringExtra("sd"));
                param.put("szEndDate", intent.getStringExtra("ed"));
                param.put("operator", application.getUserEntity().getId());

                data_attr_name.put("date", "日期");
                data_attr_name.put("nUMBER", "单据编号");
                data_attr_name.put("bilName", "单据类型");
                data_attr_name.put("num", "数量");
                data_attr_name.put("amount", "金额");
                data_attr_name.put("summary", "摘要");
                data_attr_name.put("eFullName", "经办人");
                data_attr_name.put("uFullName", "往来单位");
                data_attr_name.put("dFullName", "部门");
                data_attr_name.put("memo", "备注");

                break;
            case BaoBiaoActivity.KCZK:
                getSupportActionBar().setTitle("库存状况表");
                action = getString(R.string.kczk_action);
                findViewById(R.id.baobiao_kczk_title).setVisibility(View.VISIBLE);


                param.put("szSid", intent.getStringExtra("ck"));
                param.put("szGid", intent.getStringExtra("pr"));
                param.put("szParGid", intent.getStringExtra("szParGid"));
                param.put("priceMode", "0");
                param.put("isLieBiao", "1");
                param.put("uKind", "0");
                param.put("operator", application.getUserEntity().getId());
                param.put("bIsZero", intent.getStringExtra("bIsZero"));

                data_attr_name.put("personCode", "商品编号");
                data_attr_name.put("fullName", "商品全名");
                data_attr_name.put("standard", "规格");
                data_attr_name.put("type", "型号");
                data_attr_name.put("area", "产地");
                data_attr_name.put("unitl", "基本单位");
                data_attr_name.put("num", "数量");
                data_attr_name.put("price", "单价");
                data_attr_name.put("amount", "金额");

                haveSum = true;
                break;
            case BaoBiaoActivity.SPCX:
                getSupportActionBar().setTitle("商品查询");
                action = getString(R.string.spcx_action);

                param.put("cMode", "G");
                param.put("szGid", intent.getStringExtra("pr"));
                param.put("operator", application.getUserEntity().getId());

                data_attr_name.put("uFullname", "单位名称");
                //data_attr_name.put("uRate", "单位关系");
                data_attr_name.put("barCode", "条码");
                data_attr_name.put("price1", "预设售价一");
                data_attr_name.put("price2", "预设售价二");
                data_attr_name.put("price3", "预设售价三");
                data_attr_name.put("price4", "预设售价四");
                data_attr_name.put("price5", "预设售价五");
                data_attr_name.put("lowSalePrice", "最近售价");
                data_attr_name.put("retailPrice", "零售价");
                data_attr_name.put("file1", "商品图片");

                product = (ProductEntity) intent.getSerializableExtra("product");
                list_progressing_title.setVisibility(View.GONE);
                findViewById(R.id.list_layout).setVisibility(View.GONE);

                pro_info = (LinearLayout) findViewById(R.id.pro_info);
                pro_info.setVisibility(View.VISIBLE);

                LinearLayout img_price = (LinearLayout) findViewById(R.id.img_price);
                img_price.setVisibility(View.VISIBLE);

                img = (Button) findViewById(R.id.pro_img);
                img.setTag(product.getId());
                img.setOnClickListener(this);

                price = (Button) findViewById(R.id.pro_price);
                price.setTag(product.getId());
                price.setOnClickListener(this);

                pro_beizhu = (Button) findViewById(R.id.pro_beizhu);
                pro_beizhu.setOnClickListener(this);

                pro_code = (Button) findViewById(R.id.pro_code);
                pro_code.setTag(product.getId());
                pro_code.setOnClickListener(this);

                ((TextView)findViewById(R.id.pro_id)).setText(product.getPersonCode());
                ((TextView)findViewById(R.id.pro_name)).setText(product.getFullName());
                ((TextView)findViewById(R.id.pro_standard)).setText(product.getStandard());
                ((TextView)findViewById(R.id.pro_type)).setText(product.getType());
                ((TextView)findViewById(R.id.pro_area)).setText(product.getArea());
                ((TextView)findViewById(R.id.pro_dw)).setText(product.getNumUnit1()+"");
                ((TextView)findViewById(R.id.pro_dw2)).setText(product.getNumUnit1()+"");
                ((TextView)findViewById(R.id.pro_dw3)).setText(product.getuRate0());
                ((TextView)findViewById(R.id.pro_kc1)).setText("0");
                ((TextView)findViewById(R.id.pro_kc2)).setText("0");


                getProductKuCun(product.getId());

                /**
                 * 注册到蓝牙扫描填值类
                 */
                sv.registActivity(this);
                return;
            case BaoBiaoActivity.WLDW:
                getSupportActionBar().setTitle("单位应收应付");
                action = getString(R.string.wldw_action);
                findViewById(R.id.baobiao_wl_title).setVisibility(View.VISIBLE);
                String szid = intent.getStringExtra("dw");
                if(szid==null || "".equals(szid)){
                    szid = "00000";
                }
                param.put("szId", szid);
                param.put("szRw", "R");
                param.put("szRowExpand", "T");
                param.put("szUid", "");
                param.put("szAid", "");
                param.put("bIsZero", intent.getStringExtra("bIsZero"));
                try{
                    param.put("szOperator", application.getUserEntity().getId());
                }catch(Exception e){
                    Toast.makeText(this, "获取用户失败！", Toast.LENGTH_LONG).show();
                }

                data_attr_name.put("personCode", "往来编号");
                data_attr_name.put("fullName", "往来全名");
                data_attr_name.put("ySamount", "应收余额");
                data_attr_name.put("yFamount", "应付余额");
                data_attr_name.put("otherYS", "预收余额");
                data_attr_name.put("otherYF", "预付余额");

                haveSum = true;
                break;
            case BaoBiaoActivity.XJYH:
                //现金银行
                getSupportActionBar().setTitle("现金银行");
                action = getString(R.string.xjyh_action);

                findViewById(R.id.baobiao_xj_title).setVisibility(View.VISIBLE);

                param.put("cMode", "C");
                param.put("szBid", "");
                param.put("szOperator", application.getUserEntity().getId());
                param.put("szPeriod", application.getPeriod());

                data_attr_name.put("personCode", "科目编号");
                data_attr_name.put("fullName", "科目全名");
                data_attr_name.put("totalNow", "本月发生额");
                data_attr_name.put("totalAll", "累计金额");

                break;
            case BaoBiaoActivity.XSHZ:
                //销售汇总表
                getSupportActionBar().setTitle("销售汇总");
                action = getString(R.string.xshz_action);

                findViewById(R.id.baobiao_xshz_title).setVisibility(View.VISIBLE);

                param.put("cMode", "Z");
                param.put("szGid", "00000");
                param.put("szUid", intent.getStringExtra("dw"));
                param.put("szEid", intent.getStringExtra("zy"));
                param.put("szDid", intent.getStringExtra("bm"));
                param.put("szSid", intent.getStringExtra("ck"));
                param.put("szBeginDate", intent.getStringExtra("sd"));
                param.put("szEndDate", intent.getStringExtra("ed"));
                param.put("operator", application.getUserEntity().getId());
                param.put("bIsZero", intent.getStringExtra("bIsZero"));

                data_attr_name.put("personCode", "商品编号");
                data_attr_name.put("fullName", "商品全名");
                data_attr_name.put("standard", "规格");
                data_attr_name.put("type", "型号");
                data_attr_name.put("area", "产地");
                data_attr_name.put("unitl", "基本单位");
                data_attr_name.put("num", "销售数量");
                data_attr_name.put("price", "销售均价");
                data_attr_name.put("amount", "销售收入");
                data_attr_name.put("amountBefore", "折前金额");

                haveSum = true;
                break;
            default:
                break;
        }
        param.put("cpage", (page++)+"");
        refreshList.getLoadingLayoutProxy().setReleaseLabel("上拉加载下一页");
        refreshList.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
            }
        });

        Utils.showProgressDialog("数据加载中，请稍候……", this);
        CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action, param, handler, new GetResult(false));
        Thread t = new Thread(cs);
        t.start();
    }

    /**
     * 查询到结果，更新界面
     * @version 1.0
     * @author Administrator
     */
    class GetResult extends ExcuteThread{

        boolean isRefresh;

        public GetResult(boolean isRefresh){
            this.isRefresh = isRefresh;
        }
        @Override
        public void run() {
            try {
                JSONArray ja = new JSONArray(getJsonString());
                refreshList.onRefreshComplete();
                if(isRefresh && ja.length()==0){
                    Toast.makeText(BaoBiaoResultActivity.this, "没有更多的数据了!", Toast.LENGTH_SHORT).show();
                    refreshList.setMode(Mode.DISABLED);
                    return;
                }
                if(ja.length()==0){
                    list_progressing_title.setText("没有数据!");
                }else{
                    list_progressing_title.setVisibility(View.GONE);
                }
                for(int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    LinkedHashMap<String, String> entity = new LinkedHashMap<String, String>();
                    HashMap<String, String> biltypeMap = new HashMap<String, String>();
                    Iterator<String> it = jo.keys();
                    while(it.hasNext()){
                        String key = it.next();
                        if(jo.getString(key)!=null){
                            entity.put(key, StaticValues.format(jo.getString(key), 2));
                        }else{
                            entity.put(key, "");
                        }
                        if(type == BaoBiaoActivity.JYLC && (key.equals("bilType") || key.equals("bilID"))){
                            biltypeMap.put(key, jo.getString(key));
                        }
                    }
                    if(type == BaoBiaoActivity.JYLC){
                        //选择的是全部
                        if(dj_type.equals("")){
                            dataList.add(entity);
                            jylc_bil_Map.add(biltypeMap);
                        }else{
                            //有选择单据类型
                            if(dj_type.equals(biltypeMap.get("bilType"))){
                                dataList.add(entity);
                                jylc_bil_Map.add(biltypeMap);
                            }
                        }
                    }else{
                        dataList.add(entity);
                    }
                    /**
                     * 直接显示条目信息
                     */
                    LinkedList<KeyValues> values = new LinkedList<KeyValues>();
                    for (Entry<String, String> entry : data_attr_name.entrySet()) {
                        String key = entry.getKey();
                        if(entity.containsKey(key)){
                            KeyValues kv = new KeyValues();
                            kv.setKey(data_attr_name.get(key));
                            if((type == BaoBiaoActivity.KCZK || type==BaoBiaoActivity.JHHZ)
                                    && (key.equals("price") || key.equals("amount"))
                                    && application.getRightCost()==0){
                                kv.setValue("*");
                            }else{
                                kv.setValue(entity.get(key));
                            }
                            values.addLast(kv);
                        }
                    }
                    //如果是商品查询直接跳转，不更新list
                    if(type == BaoBiaoActivity.SPCX){
                        Intent intent = new Intent();
                        intent.setClass(BaoBiaoResultActivity.this, BaoBiaoInfoActivity.class);
                        /**
                         * 直接显示条目信息
                         */
                        intent.putExtra("info", values);
                        startActivity(intent);
                        return;
                    }else{
                        resultAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                list_progressing_title.setText("数据获取异常！");
            }finally{
                Utils.dismissProgressDialog();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if (type == BaoBiaoActivity.JHHZ || type == BaoBiaoActivity.XSHZ || type == BaoBiaoActivity.KCZK || type == BaoBiaoActivity.WLDW) {
                //如果父列表有数据
                if (lastEntitys.size() > 0) {
                    for (int i = lastEntitys.size() - 1; i >= 0; i--) {
                        HashMap<String, String> last = lastEntitys.remove(i);
                        if (last.containsKey("parID")) {
                            String parentId = last.get("parID");
                            /**
                             * 库存状况这里传的父id的参数名不一样
                             */
                            if (type == BaoBiaoActivity.KCZK) {
                                param.put("szParGid", parentId);
                                param.put("szGid", parentId);
                            } else if (type == BaoBiaoActivity.WLDW) {
                                param.put("szId", parentId);
                            } else {
                                param.put("szGid", parentId);
                            }
                            page = 1;
                            dataList.clear();
                            new GetDataTask().execute();
                            return false;
                        }
                    }
                }
            }
            this.finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void back() {
        if (type == BaoBiaoActivity.JHHZ || type == BaoBiaoActivity.XSHZ || type == BaoBiaoActivity.KCZK || type == BaoBiaoActivity.WLDW) {
            //如果父列表有数据
            if (lastEntitys.size() > 0) {
                for (int i = lastEntitys.size() - 1; i >= 0; i--) {
                    HashMap<String, String> last = lastEntitys.remove(i);
                    if (last.containsKey("parID")) {
                        String parentId = last.get("parID");
                        /**
                         * 库存状况这里传的父id的参数名不一样
                         */
                        if (type == BaoBiaoActivity.KCZK) {
                            param.put("szParGid", parentId);
                            param.put("szGid", parentId);
                        } else if (type == BaoBiaoActivity.WLDW) {
                            param.put("szId", parentId);
                        } else {
                            param.put("szGid", parentId);
                        }
                        page = 1;
                        dataList.clear();
                        new GetDataTask().execute();
                        return;
                    }
                }
            }
        }
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v == img){
            String id = (String) v.getTag();
            if(id == null){
                return;
            }
            showImg(id);
        }else if(v == price){
            String id = (String) v.getTag();
            if(id == null){
                return;
            }
            if(hasPrice){
                showPrice();
            }else{
                Toast.makeText(this, "没有获取到价格参数，请重新请求服务器！", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }else if(v == pro_beizhu){
            showProductBeizhu(product.getMemo());
        }else if(v == pro_code){
            String action = getString(R.string.getGoodsBarCode);
            String  id = (String) v.getTag();
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("cMode", "Z");
            param.put("nUnitCode", "0");
            param.put("szGID", id);
            param.put("szOperator", application.getUserEntity().getId());
            Utils.showProgressDialog("获取条码……", this);
            CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action, param, handler, new GetProductCode(id));
            Thread t = new Thread(cs);
            t.start();
        }
    }

    /**
     * 获取商品条码
     * @author funever
     *
     */
    class GetProductCode extends ExcuteThread{

        private String id;

        public GetProductCode(String id){
            this.id = id;
        }

        @Override
        public void run() {
            try {
                JSONArray ja = new JSONArray(getJsonString());
                JSONObject jo = ja.getJSONObject(0);
                final String unit1 = jo.getString("barCode00");
                final String unit2 =  jo.getString("barCode01");
                final String unit3 =  jo.getString("barCode02");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showProductCode(id,unit1, unit2, unit3);
                    }
                });
            } catch (Exception e) {
                Toast.makeText(BaoBiaoResultActivity.this, "获取条码数据错误!",Toast.LENGTH_LONG).show();
            }finally{
                Utils.dismissProgressDialog();
            }

        }
    }

    /**
     * 显示备注
     * @param memo
     */
    public void showProductBeizhu(String memo){
        Dialog dialog = new Dialog(this, R.style.dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.product_beizhu_layout, null);
        dialog.setContentView(view);
        dialog.getWindow().getAttributes().width = StaticValues.dip2px(this, 300);
        dialog.getWindow().getAttributes().height = StaticValues.dip2px(this, 350);
        EditText beizhu = (EditText) dialog.findViewById(R.id.pro_beizhu);
        beizhu.setText(memo);
        dialog.show();
    }


    EditText ed_unit = null;
    EditText ed_unit1 = null;
    EditText ed_unit2 = null;

    Button submit_code = null;
    /**
     * 显示条码
     * @param code_unit
     * @param code_unit1
     * @param code_unit2
     */
    public void showProductCode(final String id,String code_unit,String code_unit1,String code_unit2){
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.product_code_dialog);
        ed_unit = (EditText) dialog.findViewById(R.id.code_unit);
        ed_unit1  = (EditText) dialog. findViewById(R.id.code_unit1);
        ed_unit2 = (EditText)  dialog.findViewById(R.id.code_unit2);
        ed_unit.setText(code_unit);
        ed_unit1.setText(code_unit1);
        ed_unit2.setText(code_unit2);

        submit_code = (Button)  dialog.findViewById(R.id.ok);
        submit_code.setOnClickListener(new  OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = getString(R.string.setGoodsBarCode);
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("cMode", "Z");
                param.put("szGID", id);
                param.put("szBarCode0", ed_unit.getText().toString());
                param.put("szBarCode1", ed_unit1.getText().toString());
                param.put("szBarCode2", ed_unit2.getText().toString());
                param.put("szOperator", application.getUserEntity().getId());
                Utils.showProgressDialog("保存条码……", BaoBiaoResultActivity.this);
                CommunicationServer cs = new CommunicationServer(BaoBiaoResultActivity.this, application.getClient(), server+action, param, handler, new SaveCodeResult(dialog));
                Thread t = new Thread(cs);
                t.start();
            }
        });
        Button cancel =  (Button)  dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ed_unit = null;
                ed_unit1 = null;
                ed_unit2 = null;
                submit_code = null;
            }
        });
        /**
         * 摄像头扫描输入条码
         */
        dialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP){
                    return BaoBiaoResultActivity.this.onKeyUp(keyCode, event);
                }else{
                    return BaoBiaoResultActivity.this.onKeyDown(keyCode, event);
                }
            }
        });
        dialog.show();
    }

    class SaveCodeResult extends ExcuteThread{

        Dialog dialog;

        public SaveCodeResult(Dialog dialog){
            this.dialog = dialog;
        }

        @Override
        public void run() {
            try {
                Utils.dismissProgressDialog();
                JSONArray ja = new JSONArray(getJsonString());
                JSONObject jo = ja.getJSONObject(0);
                int result = jo.getInt("result");
                if(result==0){
                    Toast.makeText(BaoBiaoResultActivity.this, "保存成功.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }else{
                    Toast.makeText(BaoBiaoResultActivity.this, "保存失败.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(BaoBiaoResultActivity.this, "保存条码数据错误!",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    /**
     * 跳转到获取图片页面
     * @param id
     */
    public void showImg(String id){
        Intent intent = new Intent();
        intent.setClass(this, ProImgGridActivity.class);
        intent.putExtra("szGid", id);
        startActivity(intent);
    }

    private TextView price1,price2,price3,price4,price5,lowSalePrice,retailprice,recSalePrice;

    private TextView price1_edit,price2_edit,price3_edit,price4_edit,price5_edit,lowSalePrice_edit,recSalePrice_edit;

    private Button savePrice;

    /**
     * 编辑价格的弹出框
     */
    Dialog editDialog;
    /**
     * 跳转到显示价格界面
     */
    public void showPrice(){
        Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.product_price_layout);
        price1 = ((TextView)dialog.findViewById(R.id.price1));
        price2 = ((TextView)dialog.findViewById(R.id.price2));
        price3 = ((TextView)dialog.findViewById(R.id.price3));
        price4 = ((TextView)dialog.findViewById(R.id.price4));
        price5 = ((TextView)dialog.findViewById(R.id.price5));
        lowSalePrice = ((TextView)dialog.findViewById(R.id.lowSalePrice));
        recSalePrice = ((TextView)dialog.findViewById(R.id.recSalePrice));
        retailprice = ((TextView)dialog.findViewById(R.id.retailprice));

        price1_edit = (TextView) dialog.findViewById(R.id.price1_edit);
        price2_edit = (TextView) dialog.findViewById(R.id.price2_edit);
        price3_edit = (TextView) dialog.findViewById(R.id.price3_edit);
        price4_edit = (TextView) dialog.findViewById(R.id.price4_edit);
        price5_edit = (TextView) dialog.findViewById(R.id.price5_edit);
        lowSalePrice_edit = (TextView) dialog.findViewById(R.id.lowSalePrice_edit);
        recSalePrice_edit = (TextView) dialog.findViewById(R.id.recSalePrice_edit);

        //预设售价1传1，预设售价2传2，预设售价3传3，最低售价传19，零售价传15。
        price1_edit.setOnClickListener(new PriceEditClickListener(1));
        price2_edit.setOnClickListener(new PriceEditClickListener(2));
        price3_edit.setOnClickListener(new PriceEditClickListener(3));
        price4_edit.setOnClickListener(new PriceEditClickListener(4));
        price5_edit.setOnClickListener(new PriceEditClickListener(5));
        recSalePrice_edit.setOnClickListener(new PriceEditClickListener(15));
        lowSalePrice_edit.setOnClickListener(new PriceEditClickListener(19));

        price1.setText(StaticValues.format(product.getPrice1(),2));
        price2.setText(StaticValues.format(product.getPrice2(),2));
        price3.setText(StaticValues.format(product.getPrice3(), 2));
//		price4.setText(StaticValues.format(product.getPrice4(), 2));
//		price5.setText(StaticValues.format(product.getPrice5(), 2));
        lowSalePrice.setText(StaticValues.format(product.getLowSalePrice(), 2));
        recSalePrice.setText(StaticValues.format(product.getRecSalePrice(), 2));
        retailprice.setText(StaticValues.format(product.getRetailprice(), 2));
        dialog.show();
    }

    /**
     * 价格编辑的点击监听
     * @author funever_win8
     *
     */
    class PriceEditClickListener implements OnClickListener{

        int nPriceType;

        public PriceEditClickListener(int nPriceType){
            this.nPriceType = nPriceType;
        }

        @Override
        public void onClick(View v) {
            if(v == savePrice){
                param.clear();
                param.put("cMode", "Z");
                param.put("nPriceType", nPriceType+"");
                param.put("szGid", product.getId());
                param.put("dPrice0", et_price1.getText().toString());
                param.put("dPrice1", et_price2.getText().toString());
                param.put("dPrice2", et_price3.getText().toString());
                param.put("szOperator", application.getUserEntity().getId());
                String action = getString(R.string.product_pricesave_action);

                Utils.showProgressDialog("正在保存", BaoBiaoResultActivity.this);
                CommunicationServer cs = new CommunicationServer(BaoBiaoResultActivity.this, application.getClient(), server+action, param, handler, new SavePrice());
                Thread t = new Thread(cs);
                t.start();
            }else{
                if(application.getSetGoodsPrice()!=1){
                    Toast.makeText(BaoBiaoResultActivity.this, "没有权限对价格进行修改!", Toast.LENGTH_SHORT).show();
                    return;
                }
                action = getString(R.string.product_price_action);

                param.clear();
                param.put("cMode", "Z");
                param.put("szGid", product.getId());
                param.put("szOperator", application.getUserEntity().getId());
                param.put("nPriceType", nPriceType+"");

                Utils.showProgressDialog("正在请求数据", BaoBiaoResultActivity.this);
                CommunicationServer cs = new CommunicationServer(BaoBiaoResultActivity.this, application.getClient(), server+action, param, handler, new GetProductPrice(nPriceType));
                Thread t = new Thread(cs);
                t.start();
            }
        }
    }

    /**
     * 点击编辑价格时请求商品的价格参数
     * @author funever_win8
     *[{"price00":"10.000000000","price01":"100.000000000","price02":"0E-9"}]
     */
    class GetProductPrice extends ExcuteThread{

        int nPriceType;

        public GetProductPrice(int nPriceType){
            this.nPriceType = nPriceType;
        }

        @Override
        public void run() {
            Utils.dismissProgressDialog();
            JSONArray ja;
            try {
                ja = new JSONArray(getJsonString());
                JSONObject jo = ja.getJSONObject(0);
                editPrice(nPriceType,jo);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存价格
     * @author funever_win8
     */
    class SavePrice extends ExcuteThread{
        @Override
        public void run() {
            try {
                JSONArray ja = new JSONArray(getJsonString());
                JSONObject jo = ja.getJSONObject(0);
                int result = jo.getInt("result");
                if(result==0){
                    Toast.makeText(BaoBiaoResultActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    if(editDialog!=null && editDialog.isShowing()){
                        editDialog.dismiss();
                    }
                }else{
                    Toast.makeText(BaoBiaoResultActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(BaoBiaoResultActivity.this, "数据返回错误，保存失败！", Toast.LENGTH_SHORT).show();
            }finally{
                Utils.dismissProgressDialog();
            }
        }
    }

    private EditText et_price1,et_price2,et_price3;

    /**
     * 编辑价格
     */
    private void editPrice(int nPriceType,JSONObject jo){
        editDialog = new Dialog(this, R.style.dialog);
        editDialog.setContentView(R.layout.product_price_edit);

        et_price1 = (EditText) editDialog.findViewById(R.id.price1);
        et_price2 = (EditText) editDialog.findViewById(R.id.price2);
        et_price3 = (EditText) editDialog.findViewById(R.id.price3);

        try {
            et_price1.setText(StaticValues.format(jo.getString("price00"), 2));
            et_price2.setText(StaticValues.format(jo.getString("price01"), 2));
            et_price3.setText(StaticValues.format(jo.getString("price02"), 2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        savePrice = (Button) editDialog.findViewById(R.id.save);
        savePrice.setOnClickListener(new PriceEditClickListener(nPriceType));

        editDialog.show();
    }


    /**
     * 结果列表的适配器
     * @version 1.0
     * @author Administrator
     */
    class ResultAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HashMap<String, String> entity = dataList.get(position);
            ViewHolder viewHolder = null;
            if(convertView==null){
                viewHolder = ViewHolderFactory.newViewHolder(type);
                convertView = viewHolder.getContentView(BaoBiaoResultActivity.this);
                viewHolder.findViewWithAttribate(BaoBiaoResultActivity.this);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            /**
             * 如果含有子项，并且子项数大于0则显示旁边的小箭头
             */
            if(haveSum){
                String sonNum = entity.get("sonNum");
                try{
                    int num = Integer.parseInt(sonNum);
                    if(num>0){
                        viewHolder.rightArrow.setVisibility(View.VISIBLE);
                    }else{
                        viewHolder.rightArrow.setVisibility(View.INVISIBLE);
                    }
                }catch(Exception e){
                    viewHolder.rightArrow.setVisibility(View.VISIBLE);
                }
            }else{
                //不存在子项分类时，也显示旁边的箭头
                viewHolder.rightArrow.setVisibility(View.INVISIBLE);
            }
            viewHolder.setValue(entity);
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        try {
            Intent intent = new Intent();
            intent.setClass(this, BaoBiaoInfoActivity.class);
            if(type == BaoBiaoActivity.JYLC){
                HashMap<String, String> biltypeMap = jylc_bil_Map.get(arg2-1);
                intent.putExtra("type", type);
                if(biltypeMap.containsKey("bilType") && biltypeMap.containsKey("bilID")){
                    intent.putExtra("nBilType", biltypeMap.get("bilType"));
                    intent.putExtra("bilID", biltypeMap.get("bilID"));
                }else{
                    Toast.makeText(this, "没有获取到该单据的正确信息!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            LinkedHashMap<String, String> infoValue = dataList.get(arg2-1);

            /**
             * 当含有子项并且子项的数目大于0时，点击查询子项
             */
            if(haveSum){
                try{
                    String sonNum = infoValue.get("sonNum");
                    int num = Integer.parseInt(sonNum);
                    if(num>0){
                        dataList.clear();
                        page = 1;
                        refreshList.setMode(Mode.PULL_FROM_END);
                        resultAdapter.notifyDataSetChanged();
                        if(param.containsKey("szGid")){
                            param.put("szGid", infoValue.get("id"));
                        }
                        if(param.containsKey("szParGid")){
                            param.put("szParGid", infoValue.get("id"));
                        }
                        if(param.containsKey("szId")){
                            param.put("szId", infoValue.get("id"));
                        }
                        param.put("cpage", (page++)+"");
                        Utils.showProgressDialog("数据正在加载中,请稍候……",this);
                        CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action, param, handler, new GetResult(true));
                        Thread t = new Thread(cs);
                        t.start();
                        if(type == BaoBiaoActivity.JHHZ || type == BaoBiaoActivity.XSHZ || type == BaoBiaoActivity.KCZK || type == BaoBiaoActivity.WLDW){
                            if(lastEntitys.size()==0){
                                lastEntitys.addLast(infoValue);
                            }else{
                                HashMap<String, String> last = lastEntitys.getLast();
                                if(!last.containsKey("parID")){
                                    lastEntitys.addLast(infoValue);
                                }else{
                                    if(!last.get("parID").equalsIgnoreCase(infoValue.get("parID"))){
                                        lastEntitys.addLast(infoValue);
                                    }else{
                                        lastEntitys.removeLast();
                                        lastEntitys.addLast(infoValue);
                                    }
                                }
                            }
                        }
                        return;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    return;
                }
            }
            if(type == BaoBiaoActivity.XJYH){
                HashMap<String, String> paramMap = new HashMap<String, String>();
                /**
                 * nBilType（直接传‘Z’）
                 　　szBID（单据类型标记，直接传0）
                 　　szPeriod（会计科目ID（取现金银行报表上字段ID））
                 　　szDateBegin（当前会计期间）
                 　　szDateEnd（查询条件的开始日期）
                 　　szOperatorID（查询条件的结束日期）
                 　　cpage（当前页码）
                 */
                paramMap.put("nBilType", "Z");
                paramMap.put("szBID", infoValue.get("bID"));
                paramMap.put("szPeriod", application.getPeriod());
                paramMap.put("szDateBegin", "");
                paramMap.put("szDateEnd", "");
                paramMap.put("szOperatorID", application.getUserEntity().getId());
                paramMap.put("cpage", "1");
                selectDateDialog(paramMap, type);
                return;
            }else if(type == BaoBiaoActivity.WLDW){
                HashMap<String, String> paramMap = new HashMap<String, String>();
                /**
                 * cMode（直接传‘Z’）
                 nBilType（单据类型标记，直接传0）
                 szBID（会计科目ID，直接传空’’）
                 szUID（往来单位ID）
                 　　 			szPeriod（当前会计期间）
                 szDateBegin（查询条件的开始日期）
                 　　 			szDateEnd（查询条件的结束日期）
                 szOperatorID（当前操作员ID）
                 cpage（当前页面）
                 bIsZero(直接传0)
                 */
                paramMap.put("cMode", "Z");
                paramMap.put("nBilType", "0");
                paramMap.put("szBID", "");
                paramMap.put("szUID", infoValue.get("id"));
                paramMap.put("szPeriod",application.getPeriod());
                paramMap.put("szDateBegin", "");
                paramMap.put("szDateEnd", "");
                paramMap.put("szOperatorID", application.getUserEntity().getId());
                paramMap.put("cpage", "1");
                paramMap.put("bIsZero", "0");

                selectDateDialog(paramMap, type);
                return;
            }else if(type == BaoBiaoActivity.KCZK){
                return;
            }else if(type == BaoBiaoActivity.JHHZ || type == BaoBiaoActivity.XSHZ){
                return;
            }
            /**
             * 直接显示条目信息
             */
            LinkedList<KeyValues> values = new LinkedList<KeyValues>();
            for (Entry<String, String> entry : data_attr_name.entrySet()) {
                String key = entry.getKey();
                if(infoValue.containsKey(key)){
                    KeyValues kv = new KeyValues();
                    kv.setKey(data_attr_name.get(key));
                    if((type == BaoBiaoActivity.KCZK || type==BaoBiaoActivity.JHHZ)
                            && (key.equals("price") || key.equals("amount"))
                            && application.getRightCost()==0){
                        kv.setValue("*");
                    }else if(type==BaoBiaoActivity.JYLC && key.equals("amount") && application.getRightCost()==0){
                        kv.setValue("*");
                    }else{
                        kv.setValue(infoValue.get(key));
                    }
                    values.addLast(kv);
                }
            }
            intent.putExtra("info", values);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品查询时获取商品的库存信息
     * @param pro_id
     */
    public void getProductKuCun(String pro_id){
        String actionUrl = getString(R.string.product_void_stock);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("cMode", "Z");
        param.put("szGID", pro_id);
        param.put("szSID", "00000");
        param.put("szOperator", application.getUserEntity().getId());

        Utils.showProgressDialog("正在获取商品库存信息，请稍候", this);
        CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+actionUrl, param, new Handler(), new SearchProductInfo());
        Thread t = new Thread(cs);
        t.start();
    }

    class SearchProductInfo extends ExcuteThread{

        @Override
        public void run() {
            //{"area":"产地","fullName":"商品1","num":200,"standard":"规格","type":"型号","uRate":"","unit1":"g","unit2":"gg","voidNum":472}
            try {
                JSONArray ja = new JSONArray(getJsonString());
                if(ja.length()>0){
                    JSONObject jo = ja.getJSONObject(0);
                    Utils.setObjectValue(product, jo);
                    ((TextView)findViewById(R.id.pro_area)).setText(jo.getString("area"));
                    ((TextView)findViewById(R.id.pro_name)).setText(jo.getString("fullName"));
                    ((TextView)findViewById(R.id.pro_kc1)).setText(jo.getString("num"));
                    ((TextView)findViewById(R.id.pro_standard)).setText(jo.getString("standard"));
                    ((TextView)findViewById(R.id.pro_type)).setText(jo.getString("type"));
                    ((TextView)findViewById(R.id.pro_dw3)).setText(StaticValues.format(jo.getString("uRate"), 2));
                    ((TextView)findViewById(R.id.pro_dw)).setText(jo.getString("unit1"));
                    ((TextView)findViewById(R.id.pro_dw2)).setText(jo.getString("unit2"));
                    ((TextView)findViewById(R.id.pro_kc2)).setText(jo.getString("voidNum"));
                    ((TextView)findViewById(R.id.barCode)).setText(jo.getString("barCode"));
                    hasPrice = true;
                }
            } catch (JSONException e) {
                Toast.makeText(BaoBiaoResultActivity.this, "获取商品库存信息异常，请重新查询!", Toast.LENGTH_SHORT).show();
                BaoBiaoResultActivity.this.finish();
            }finally{
                Utils.dismissProgressDialog();
            }
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... arg0) {
            param.put("cpage", (page++)+"");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Utils.showProgressDialog("数据加载中,请稍候……", BaoBiaoResultActivity.this);
                }
            });
            CommunicationServer sc = new CommunicationServer(BaoBiaoResultActivity.this,application.getClient(),server+action,param,handler, new GetResult(true));
            Thread t = new Thread(sc);
            t.start();
            return null;
        }
    }

    private void selectDateDialog(final HashMap<String, String> param,final int type){
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
                    Toast.makeText(BaoBiaoResultActivity.this, "请选择日期!", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("szDateBegin",  startDateText.getText().toString());
                param.put("szDateEnd",  endDateText.getText().toString());
                Intent intent = new Intent();
                intent.setClass(BaoBiaoResultActivity.this, BaobiaoDetailActivity.class);
                intent.putExtra("param", param);
                intent.putExtra("type", type);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class SelectDateListener implements OnClickListener{

        TextView tv;

        public SelectDateListener(TextView tv){
            this.tv = tv;
        }

        @Override
        public void onClick(View v) {
            Utils.showChooseDate(BaoBiaoResultActivity.this, tv);
        }
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
        if(type != BaoBiaoActivity.SPCX){
            return;
        }
        if(ed_unit!=null &&ed_unit.isFocused()){
            ed_unit.setText(value);
            ed_unit.clearFocus();
            if(ed_unit1!=null){
                ed_unit1.requestFocus();
            }
            return;
        }else if(ed_unit1!=null && ed_unit1.isFocused()){
            ed_unit1.setText(value);
            ed_unit1.clearFocus();
            if(ed_unit2!=null){
                ed_unit2.requestFocus();
            }
            return;
        }else if(ed_unit2!=null && ed_unit2.isFocused()){
            ed_unit2.setText(value);
            submit_code.performClick();
        }
    }

    @Override
    public boolean canScan() {
        if(type == BaoBiaoActivity.SPCX){
            return true;
        }
        return false;
    }

    @Override
    public void disposeLastNextData(TableEntity te) {

    }

    @Override
    public String getNumber() {
        return null;
    }
}