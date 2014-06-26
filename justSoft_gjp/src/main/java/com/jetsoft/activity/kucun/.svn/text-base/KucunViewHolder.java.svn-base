package com.jetsoft.activity.kucun;

import java.util.HashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.baobiao.DetailViewHolder;

public class KucunViewHolder extends DetailViewHolder {
	
	View convertView;
	
	TextView date;
	TextView sName;
	TextView eName;

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.kucun_list_item, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView==null){
			return;
		}
		date = (TextView) convertView.findViewById(R.id.date);
		sName = (TextView) convertView.findViewById(R.id.sName);
		eName = (TextView) convertView.findViewById(R.id.eName);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		date.setText(itemMap.get("date"));
		sName.setText(itemMap.get("sName"));
		eName.setText(itemMap.get("eName"));
	}

}
