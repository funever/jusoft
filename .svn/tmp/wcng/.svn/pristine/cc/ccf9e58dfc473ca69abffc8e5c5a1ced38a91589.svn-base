package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import com.jetsoft.R;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class WLDetailViewHolder extends DetailViewHolder {
	
	View convertView;
	
	TextView bilType;
	TextView number;
	TextView date;
	TextView amountAdd;
	TextView amountDec;
	TextView amountYe;
	TextView summary;

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.detail_wldw_item, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView == null){
			return;
		}
		bilType = (TextView) convertView.findViewById(R.id.bilType);
		number = (TextView) convertView.findViewById(R.id.number);
		date = (TextView) convertView.findViewById(R.id.date);
		amountAdd = (TextView) convertView.findViewById(R.id.amountAdd);
		amountDec = (TextView) convertView.findViewById(R.id.amountDec);
		amountYe = (TextView) convertView.findViewById(R.id.amountYe);
		summary = (TextView) convertView.findViewById(R.id.summary);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		bilType.setText(Utils.getFormatType(itemMap.get("bilType")));
		number.setText(itemMap.get("number"));
		date.setText(itemMap.get("date"));
		amountAdd.setText(StaticValues.format(itemMap.get("amountAdd"), 2));
		amountDec.setText(StaticValues.format(itemMap.get("amountDec"),2));
		amountYe.setText(StaticValues.format(itemMap.get("amountYe"),2));
		summary.setText(itemMap.get("summary"));
	}

}
