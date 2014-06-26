package com.jetsoft.activity.baobiao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DetailAdapte extends BaseAdapter {
	
	private List<HashMap<String,String>> dataList = new LinkedList<HashMap<String,String>>();
	private int type;
	private Context ctx;
	
	public DetailAdapte(int type,List<HashMap<String,String>> dataList,Context ctx){
		this.dataList = dataList;
		this.type = type;
		this.ctx = ctx;
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
}
