package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import com.jetsoft.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class WLViewHolder extends ViewHolder {
	
	private static final  int item_layout_id = R.layout.baobiao_wl_title;
	
	TextView personCode;
	TextView fullName;
	TextView ySamount;
	TextView yFamount;
	TextView otherYS;
	TextView otherYF;

	@Override
	public void findViewWithAttribate(Context context) {
		if(contentView==null){
			contentView = LayoutInflater.from(context).inflate(item_layout_id, null);
		}
		personCode = (TextView) contentView.findViewById(R.id.personCode);
		fullName = (TextView) contentView.findViewById(R.id.fullName);
		ySamount = (TextView) contentView.findViewById(R.id.ySamount);
		yFamount = (TextView) contentView.findViewById(R.id.yFamount);
		otherYS = (TextView) contentView.findViewById(R.id.otherYS);
		otherYF = (TextView) contentView.findViewById(R.id.otherYF);
		rightArrow = contentView.findViewById(R.id.right_arrow);
	}

	@Override
	public View getContentView(Context context) {
		contentView = LayoutInflater.from(context).inflate(item_layout_id, null);
		return contentView;
	}

	@Override
	public void setValue(HashMap<String, String> entity) {
		personCode.setText(entity.get("personCode"));
		fullName.setText(entity.get("fullName"));
		ySamount.setText(entity.get("ySamount"));
		yFamount.setText(entity.get("yFamount"));
		otherYS.setText(entity.get("otherYS"));
		otherYF.setText(entity.get("otherYF"));
	}

}
