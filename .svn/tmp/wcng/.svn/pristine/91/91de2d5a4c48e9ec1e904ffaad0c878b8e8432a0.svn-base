package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import com.jetsoft.R;
import com.jetsoft.utils.StaticValues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ProfitDetailViewHolder extends DetailViewHolder {
	
	View convertView;
	
	TextView aCode;
	TextView aName;
	TextView amountN;
	TextView amountM;
	TextView amountY;

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.baobiao_profit_title, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView==null){
			return;
		}
		aCode = (TextView) convertView.findViewById(R.id.aCode);
		aName = (TextView) convertView.findViewById(R.id.aName);
		amountN = (TextView) convertView.findViewById(R.id.amountN);
		amountM = (TextView) convertView.findViewById(R.id.amountM);
		amountY = (TextView) convertView.findViewById(R.id.amountY);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		aCode.setText(itemMap.get("aCode"));
		aName.setText(itemMap.get("aName"));
		amountN.setText(StaticValues.format(itemMap.get("amountN"), 2));
		amountM.setText(StaticValues.format(itemMap.get("amountM"), 2));
		amountY.setText(StaticValues.format(itemMap.get("amountY"),2));
	}

}
