package com.jetsoft.activity.dynic;

import java.util.HashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.utils.StaticValues;

public class KeMuViewHolder extends DetailViewHolder {
	
	TextView kmbh;
	TextView kmqm;
	TextView amount;
	
	View convertView;

	@Override
	public void setValue(HashMap<String, String> entity) {
		kmbh.setText(entity.get("gPersonCode"));
		kmqm.setText(entity.get("gFullName"));
		amount.setText(StaticValues.format(entity.get("amount"), 2));
	}

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.dynic_kemu_title, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		kmbh = (TextView) convertView.findViewById(R.id.kmbh);
		kmqm = (TextView) convertView.findViewById(R.id.kmqm);
		amount = (TextView) convertView.findViewById(R.id.amount);
	}

}
