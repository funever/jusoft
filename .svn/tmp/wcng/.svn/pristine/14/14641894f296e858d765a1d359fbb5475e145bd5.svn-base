package com.jetsoft.activity.warning;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.jetsoft.activity.baobiao.DetailViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ViewHolderAdatper extends BaseAdapter {
	
	private List<HashMap<String,String>> dataList = new LinkedList<HashMap<String,String>>();
	private Context ctx;
	int type;
	
	public ViewHolderAdatper(List<HashMap<String,String>> dataList,Context ctx,int type){
		this.dataList = dataList;
		this.ctx = ctx;
		this.type = type;
	}
	
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
		DetailViewHolder dvh = null;
		if(convertView == null){
			dvh = DetailViewHolder.getViewHolder(type);
			convertView = dvh.getConvertView(ctx);
			dvh.findViewbyId();
			convertView.setTag(dvh);
		}else{
			dvh = (DetailViewHolder) convertView.getTag();
		}
		dvh.setValue(dataList.get(position));
		return convertView;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
