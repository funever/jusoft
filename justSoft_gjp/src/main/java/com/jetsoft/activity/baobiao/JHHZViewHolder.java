package com.jetsoft.activity.baobiao;

import java.util.HashMap;
import com.jetsoft.R;
import com.jetsoft.application.MyApplication;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class JHHZViewHolder extends ViewHolder{
	
	private static final  int item_layout_id = R.layout.baobiao_jhhz_title;
	
	TextView personCode;
	TextView fullName;
	TextView standard;
	TextView type;
	TextView area;
	TextView unitl;
	TextView num;
	TextView price;
	TextView amount;
	TextView fz_num;
	
	MyApplication application;

	@Override
	public void findViewWithAttribate(Context context) {
		if(contentView == null){
			getContentView(context);
		}
		personCode = (TextView) contentView.findViewById(R.id.personCode);
		fullName = (TextView) contentView.findViewById(R.id.fullName);
		standard = (TextView) contentView.findViewById(R.id.standard);
		type = (TextView) contentView.findViewById(R.id.type);
		area = (TextView) contentView.findViewById(R.id.area);
		unitl = (TextView) contentView.findViewById(R.id.unitl);
		num = (TextView) contentView.findViewById(R.id.num);
		price = (TextView) contentView.findViewById(R.id.price);
		amount = (TextView) contentView.findViewById(R.id.amount);
		fz_num = (TextView) contentView.findViewById(R.id.fz_num);
		
		rightArrow = contentView.findViewById(R.id.right_arrow);
	}

	@Override
	public View getContentView(Context context) {
		application = (MyApplication) ((Activity)context).getApplication();
		if(contentView == null){
			contentView = LayoutInflater.from(context).inflate(item_layout_id, null);
		}
		return contentView;
	}

	@Override
	public void setValue(HashMap<String, String> entity) {
		personCode.setText(entity.get("personCode"));
		fullName.setText(entity.get("fullName"));
		standard.setText(entity.get("standard"));
		type.setText(entity.get("type"));
		area.setText(entity.get("area"));
		unitl.setText(entity.get("unit1"));
		num.setText(entity.get("num"));
		price.setText("*");
		amount.setText("*");
		if(application!=null){
			if(application.getRightCost()==1){
				price.setText(entity.get("price"));
				amount.setText(entity.get("amount"));
			}
		}
		fz_num.setText(entity.get("unitNum"));
	}

}
