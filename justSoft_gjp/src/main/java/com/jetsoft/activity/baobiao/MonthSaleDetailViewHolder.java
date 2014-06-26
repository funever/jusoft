package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import com.jetsoft.R;
import com.jetsoft.utils.StaticValues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MonthSaleDetailViewHolder extends DetailViewHolder {
	
	View convertView;
	
	TextView date;
	TextView num;

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.baobiao_monthsale_title, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView==null){
			return;
		}
		date = (TextView) convertView.findViewById(R.id.date);
		num = (TextView) convertView.findViewById(R.id.num);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		date.setText(itemMap.get("date"));
		num.setText(StaticValues.format(itemMap.get("num"), 2));
	}

}
