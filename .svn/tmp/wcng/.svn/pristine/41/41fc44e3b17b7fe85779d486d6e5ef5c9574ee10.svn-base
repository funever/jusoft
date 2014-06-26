package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import com.jetsoft.R;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class XJDetailViewHolder extends DetailViewHolder {
	
	View convertView;

	public TextView dj_type;
	public TextView dj_no;
	public TextView dj_date;
	public TextView dj_zybh;
	public TextView dj_zyqm;
	public TextView dj_kmbh;
	public TextView dj_kmqm;
	public TextView dj_zjje;
	public TextView dj_jsje;
	public TextView dj_ye;
	public TextView dj_zy;
	
	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.detail_xjyh_item, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView == null){
			return;
		}
		dj_type = (TextView) convertView.findViewById(R.id.dj_type);
		dj_no = (TextView) convertView.findViewById(R.id.dj_no);
		dj_date = (TextView) convertView.findViewById(R.id.dj_date);
		dj_zybh = (TextView) convertView.findViewById(R.id.dj_zybh);
		dj_zyqm = (TextView) convertView.findViewById(R.id.dj_zyqm);
		dj_kmbh = (TextView) convertView.findViewById(R.id.dj_kmbh);
		dj_kmqm = (TextView) convertView.findViewById(R.id.dj_kmqm);
		dj_zjje = (TextView) convertView.findViewById(R.id.dj_zjje);
		dj_jsje = (TextView) convertView.findViewById(R.id.dj_jsje);
		dj_ye = (TextView) convertView.findViewById(R.id.dj_ye);
		dj_zy = (TextView) convertView.findViewById(R.id.dj_zy);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		dj_type.setText(Utils.getFormatType(itemMap.get("bilType")));
		dj_no.setText(itemMap.get("number"));
		dj_date.setText(itemMap.get("date"));
		dj_zybh.setText(itemMap.get("eCode"));
		dj_zyqm.setText(itemMap.get("eName"));
		dj_kmbh.setText(itemMap.get("bCode"));
		dj_kmqm.setText(itemMap.get("bName"));
		dj_zjje.setText(StaticValues.format(itemMap.get("amountAdd"), 2));
		dj_jsje.setText(StaticValues.format(itemMap.get("amountDec"),2));
		dj_ye.setText(StaticValues.format(itemMap.get("amountYe"),2));
		dj_zy.setText(itemMap.get("summary"));
		
	}
}
