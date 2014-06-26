package com.jetsoft.activity.warning;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.utils.Utils;

public class OrderWarnViewHolder extends DetailViewHolder {
	
	View convertView;
	
	TextView bilType;
	TextView number;
	TextView date;
	TextView summary;
	TextView memo;
	TextView toDate;
	TextView uName;
	TextView eName;
	TextView uCode;
	TextView eCode;

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.order_warn_list_item, null);
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
		toDate = (TextView) convertView.findViewById(R.id.toDate);
		uName = (TextView) convertView.findViewById(R.id.uName);
		eName = (TextView) convertView.findViewById(R.id.eName);
		uCode = (TextView) convertView.findViewById(R.id.uCode);
		eCode = (TextView) convertView.findViewById(R.id.eCode);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		bilType.setText(Utils.getFormatType(itemMap.get("bilType")));
		number.setText(itemMap.get("number"));
		date.setText(itemMap.get("date"));
		summary.setText(itemMap.get("summary"));
		memo.setText(itemMap.get("memo"));
		toDate.setText(itemMap.get("toDate"));
		uName.setText(itemMap.get("uName"));
		eName.setText(itemMap.get("eName"));
		uCode.setText(itemMap.get("uCode"));
		eCode.setText(itemMap.get("eCode"));
	}

}
