package com.jetsoft.activity.warning;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.utils.StaticValues;

public class WarnUpDownViewHolder extends DetailViewHolder {
	
	View convertView;
	
	TextView personCode;
	TextView fullName;
	TextView warnUP;
	TextView num;
	TextView range;
	
	public static final int TYPE_UP = 1;
	public static final int TYPE_DOWN = 2;
	
	int type ;
	
	public WarnUpDownViewHolder(int type){
		this.type = type;
	}

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.warn_up_down_list_item, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView==null){
			return;
		}
		personCode = (TextView) convertView.findViewById(R.id.personCode);
		fullName = (TextView) convertView.findViewById(R.id.fullName);
		warnUP = (TextView) convertView.findViewById(R.id.warnUP);
		num = (TextView) convertView.findViewById(R.id.num);
		range = (TextView) convertView.findViewById(R.id.range_num);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		personCode.setText(itemMap.get("personCode"));
		fullName.setText(itemMap.get("fullName"));
		String tag = "warnUP";
		if(type == TYPE_UP){
			tag = "warnUP";
		}else if(type == TYPE_DOWN){
			tag = "warnDown";
		}
		warnUP.setText(StaticValues.format(itemMap.get(tag), 2));
		num.setText(StaticValues.format(itemMap.get("num"),2));
		float f = Float.parseFloat(itemMap.get(tag))-Float.parseFloat(itemMap.get("num"));
		range.setText(StaticValues.format(Math.abs(f),2));
	}

}
