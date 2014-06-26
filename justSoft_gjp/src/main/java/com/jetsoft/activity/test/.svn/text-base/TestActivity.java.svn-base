/**
 * 创建日期 2013-3-6
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
package com.jetsoft.activity.test;

import com.jetsoft.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class TestActivity extends Activity {
	
	public TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_in);
		
		// mTabHost定义在Activity的属性
        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup();

        LayoutInflater factory = null;
        factory = LayoutInflater.from(this);
        
        final TextView tabtext1 = (TextView) factory.inflate(R.layout.tab,null);
        tabtext1.setText("基本信息");

        // 添加商品
        final TextView tabtext2 = (TextView) factory.inflate(R.layout.tab,null);
        tabtext2.setText("商品");
        
        factory.inflate(R.layout.test_in_layout, mTabHost.getTabContentView());
        TabSpec tabl = mTabHost.newTabSpec("tab1");
        tabl.setIndicator(tabtext1);
        tabl.setContent(R.id.operate_in);
        mTabHost.addTab(tabl);
        
        factory.inflate(R.layout.product_list, mTabHost.getTabContentView());
		TabSpec tab2 = mTabHost.newTabSpec("tab2");
		tab2.setIndicator(tabtext2);
        tab2.setContent(R.id.add_product);
        mTabHost.addTab(tab2);
       
        mTabHost.setCurrentTab(1);
        
        tabtext1.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_style_select)); 
        // tab 选中改变时事件
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                public void onTabChanged(String arg0) {
                        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++){
                               mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tabname)
                                .setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_style));
                        }
                        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
                        .findViewById(R.id.tabname)
                        .setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_style_select));
                }
        });
	}
}
