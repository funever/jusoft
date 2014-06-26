/**
 * 创建日期 2012-12-17
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
package com.jetsoft.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.entity.*;

public class DanJuAdapter extends BaseAdapter {
	
	private Context ctx;
	
	private List<ProductEntity> proList;
	
	public DanJuAdapter(Context ctx,List<ProductEntity> proList){
		this.ctx = ctx;
		this.proList = proList;
	}

	@Override
	public int getCount() {
		return proList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return proList.get(position+1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if(position==0){
			view = LayoutInflater.from(ctx).inflate(R.layout.product_list_row, null);
			ImageView search = (ImageView) view.findViewById(R.id.pro_search);
			final EditText searchText = (EditText) view.findViewById(R.id.pro_search_text);
			search.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(ctx, EntityListActivity.class);
					intent.putExtra("type", EntityListActivity.PR_RE_CODE);
					intent.putExtra("search", searchText.getText().toString());
					((Activity)ctx).startActivityForResult(intent, EntityListActivity.PR_RE_CODE);
				}
			});
		}else{
			view = LayoutInflater.from(ctx).inflate(R.layout.product_list_row1, null);
			((TextView)view.findViewById(R.id.prolist_spqm)).setText(proList.get(position-1).getFullName());
			((TextView)view.findViewById(R.id.pro_danwei)).setText(proList.get(position-1).getUnit1());
			((TextView)view.findViewById(R.id.pro_shuliang)).setText(proList.get(position-1).getNum()+"");
			((TextView)view.findViewById(R.id.pro_danjia)).setText(proList.get(position-1).getPriceRec()+"");
		}
		return view;
	}

}
