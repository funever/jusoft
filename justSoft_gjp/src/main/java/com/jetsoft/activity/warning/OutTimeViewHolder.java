package com.jetsoft.activity.warning;

import java.util.HashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.utils.StaticValues;

public class OutTimeViewHolder extends DetailViewHolder{
	
	View convertView;
	
	private TextView number;
	private TextView date;
	private TextView amountSF;
	private TextView amountYS;
	private TextView toDate;
	private TextView uCode;
	private TextView uName;
	private TextView taxAmount;

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.outtime_list_item, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView==null){
			return;
		}
		number = (TextView) convertView.findViewById(R.id.number);
		date = (TextView) convertView.findViewById(R.id.date);
		amountSF = (TextView) convertView.findViewById(R.id.amountSF);
		amountYS = (TextView) convertView.findViewById(R.id.amountYS);
		toDate = (TextView) convertView.findViewById(R.id.toDate);
		uCode = (TextView) convertView.findViewById(R.id.uCode);
		uName = (TextView) convertView.findViewById(R.id.uName);
		taxAmount = (TextView) convertView.findViewById(R.id.taxAmount);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		number.setText(itemMap.get("number"));
		date.setText(itemMap.get("date"));
		amountSF.setText(StaticValues.format(itemMap.get("amountSF"), 2));
		amountYS.setText(StaticValues.format(itemMap.get("amountYS"),2));
		toDate.setText(itemMap.get("toDate"));
		uCode.setText(itemMap.get("uCode"));
		uName.setText(itemMap.get("uName"));
		taxAmount.setText(StaticValues.format(itemMap.get("taxAmount"),2));
	}

}
