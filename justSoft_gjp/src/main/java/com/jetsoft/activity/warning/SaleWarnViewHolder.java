package com.jetsoft.activity.warning;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jetsoft.R;
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;

public class SaleWarnViewHolder extends DetailViewHolder {
	
	View convertView;
	
	TextView bilType;
	TextView number;
	TextView date;
	TextView summary;
	TextView memo;
	TextView state;
	TextView uName;
	TextView eName;
	TextView inputName;
	TextView bilName;
	TextView amount;

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.sale_warn_list_item, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView==null){
			return;
		}
		bilType = (TextView) convertView.findViewById(R.id.bilType);
		number = (TextView) convertView.findViewById(R.id.number);
		date = (TextView) convertView.findViewById(R.id.date);
		summary = (TextView) convertView.findViewById(R.id.summary);
		memo = (TextView) convertView.findViewById(R.id.memo);
		state = (TextView) convertView.findViewById(R.id.state);
		uName = (TextView) convertView.findViewById(R.id.uName);
		eName = (TextView) convertView.findViewById(R.id.eName);
		inputName = (TextView) convertView.findViewById(R.id.inputName);
		bilName = (TextView) convertView.findViewById(R.id.bilName);
		amount = (TextView) convertView.findViewById(R.id.amount);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		bilType.setText(Utils.getFormatType(itemMap.get("bilType")));
		number.setText(itemMap.get("number"));
		date.setText(itemMap.get("date"));
		summary.setText(itemMap.get("summary"));
		memo.setText(itemMap.get("memo"));
		state.setText(itemMap.get("state"));
		uName.setText(itemMap.get("uName"));
		eName.setText(itemMap.get("eName"));
		inputName.setText(itemMap.get("inputName"));
		bilName.setText(itemMap.get("bilName"));
		amount.setText(StaticValues.format(itemMap.get("amount"), 2));
	}

}
