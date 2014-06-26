/**
 * 创建日期 2013-1-4
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
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.jetsoft.R;
import com.jetsoft.activity.TabHostActivity;
import com.jetsoft.activity.draft.DraftListActivity;
import com.jetsoft.activity.kucun.KuCunActivity;
import com.jetsoft.activity.money.MoneyActivity;
import com.jetsoft.activity.order.OrderActivity;
import com.jetsoft.activity.returnorder.ReturnOrderActivity;
import com.jetsoft.activity.sale.SaleActivity;
import com.jetsoft.activity.sale.WaiMaoActivity;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.KeyValues;
import com.jetsoft.entity.TableEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BaoBiaoInfoActivity extends SherlockActivity{

    ListView info_list;

    List<String> values = new LinkedList<String>();

    Button open_jylc;

    int type;

    Class activityClass;

    MyApplication application;

    public String server;

    public Handler handler = new Handler();

    private SharedPreferences preferences;

    HashMap<String, String> params = new HashMap<String, String>();

    String selectProducts = "";

    String sale_type = null;

    String money_type = null;

    String cMode = "";

    String nBilType;

    String bilID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", 0);
        application = (MyApplication) getApplication();
        preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
        server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
                + preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));

        nBilType = getIntent().getStringExtra("nBilType");
        bilID = getIntent().getStringExtra("bilID");

        if(type == BaoBiaoActivity.JYLC){
            getDraftContent();
            return;
        }

        setContentView(R.layout.baobiao_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("详细信息");
        info_list = (ListView) findViewById(R.id.infoList);
        open_jylc = (Button) findViewById(R.id.open);

        open_jylc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDraftContent();
            }
        });

        List<KeyValues> info = (List<KeyValues>) getIntent().getSerializableExtra("info");
        try {
            Iterator<KeyValues> it = info.iterator();
            while(it.hasNext()){
                KeyValues key = it.next();
                values.add(key.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        info_list.setAdapter(new InfoAdapter());
    }

    class InfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(BaoBiaoInfoActivity.this);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            tv.setPadding(15, 0, 0, 0);
            tv.setText(values.get(position));
            tv.setTextColor(Color.BLACK);
            tv.setMinHeight(60);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            return tv;
        }

    }

    /**
     * 获取草稿内容
     */
    public void getDraftContent(){

        activityClass = getDraftActivity(nBilType);

        HashMap<String, String> params = new HashMap<String, String>();
        String action_url = getString(R.string.draft_load);
        params.put("cMode", "T");
        params.put("nBilType",nBilType);
        params.put("nBilID", bilID);
        params.put("szOperator", application.getUserEntity().getId());

        Utils.showProgressDialog("正在获取单据信息,请稍候……", this);
        CommunicationServer cs = new CommunicationServer(this, application.getClient(), server+action_url,params, handler, new GetDraftContent());
        Thread t = new Thread(cs);
        t.start();
    }

    /**
     * 获取单据参数
     * @author funever_win8
     *
     */
    class GetDraftContent extends ExcuteThread{
        @Override
        public void run() {
            if(getJsonString().equals("[]")){
                Toast.makeText(BaoBiaoInfoActivity.this, "没有可用的单据！", Toast.LENGTH_SHORT).show();
                Utils.dismissProgressDialog();
                return;
            }else{
                //填充值或者是下一步的请求商品操作
//				Toast.makeText(BaseActivity.this, getJsonString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray ja = new JSONArray(getJsonString());
                    JSONObject jo = ja.getJSONObject(0);
                    TableEntity te = Utils.parseJSONObjectToObject(TableEntity.class, jo);

                    String action_url = getString(R.string.draft_load);
                    params.put("cMode", "D");
                    params.put("nBilType",nBilType);
                    params.put("nBilID", bilID);
                    params.put("szOperator", application.getUserEntity().getId());

                    CommunicationServer cs = new CommunicationServer(BaoBiaoInfoActivity.this, application.getClient(), server+action_url,params, handler, new GetDraftProduct(te));
                    Thread t = new Thread(cs);
                    t.start();
                }  catch (Exception e) {
                    e.printStackTrace();
                    Utils.dismissProgressDialog();
                }
            }
        }
    }
    /**
     * 获取单据的商品
     * @author funever_win8
     *
     */
    class GetDraftProduct extends ExcuteThread{

        TableEntity te;

        public GetDraftProduct(TableEntity te){
            this.te = te;
        }
        @Override
        public void run() {
            if(te!=null){
                try {
                    selectProducts = getJsonString();
                    //跳转到各自的单据页面
                    toPage(te);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    Utils.dismissProgressDialog();
                }
            }
        }
    }
    /**
     * 获取到所有的参数后跳转到对应的单据页面
     * @param te
     */
    public void toPage(TableEntity te){
        if(activityClass == null){
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, activityClass);
        intent.putExtra(DraftListActivity.DRAFT_TABLE, te);
        intent.putExtra(DraftListActivity.DRAFT_PRODUCTS, selectProducts);
        intent.putExtra(DraftListActivity.FROM_DRAFT, true);
        intent.putExtra(DraftListActivity.DRAFT_BILTYPE, nBilType);
        intent.putExtra("nBilType", nBilType);
        intent.putExtra("isEdit", false);
        if(sale_type!=null){
            intent.putExtra("sale_type", sale_type);
        }
        if(money_type!=null){
            intent.putExtra("money_type", money_type);
        }
        if(nBilType.equals(StaticValues.TONGJIA_BILTYPE)){
            intent.putExtra("type", KuCunActivity.KUCUN_TONGJIA);
        }
        startActivity(intent);
        this.finish();
    }

    /**
     * 获取单据class
     * @param nBilType
     * @return 单据页面activity的class
     */
    private Class getDraftActivity(String nBilType){
        if(nBilType.equals(StaticValues.ORDER_IN_BILTYPE)){
            cMode = "B";
            return TabHostActivity.class;
        }else if(nBilType.equals(StaticValues.ORDER_BILTYPE)){
            cMode = "B";
            return OrderActivity.class;
        }else if(nBilType.equals(StaticValues.ORDER_RE_BILTYPE)){
            cMode = "B";
            return ReturnOrderActivity.class;
        }else if(nBilType.equals(StaticValues.SALE_IN_BILTYPE)){
            sale_type = SaleActivity.SALE_ORDER;
            cMode = "S";
            return WaiMaoActivity.class;
        }else if(nBilType.equals(StaticValues.SALE_BILTYPE)){
            cMode = "S";
            sale_type = SaleActivity.SALE_NOTE;
            return SaleActivity.class;
        }else if(nBilType.equals(StaticValues.SALE_RE_BILTYPE)){
            cMode = "S";
            sale_type = SaleActivity.SALE_RETURN;
            return SaleActivity.class;
        }else if(nBilType.equals(StaticValues.BIANJIA_BILTYPE) || nBilType.equals(StaticValues.TONGJIA_BILTYPE)){
            cMode = "O";
            return KuCunActivity.class;
        }else if(nBilType.equals(StaticValues.SK_BILTYPE)){
            money_type = MoneyActivity.MONEY_SKD;
            cMode = "A";
            return MoneyActivity.class;
        }else if(nBilType.equals(StaticValues.FK_BILTYPE)){
            money_type = MoneyActivity.MONEY_FKD;
            cMode = "A";
            return MoneyActivity.class;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}