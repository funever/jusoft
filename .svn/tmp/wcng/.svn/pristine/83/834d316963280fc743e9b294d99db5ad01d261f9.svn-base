package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import com.jetsoft.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class XJYHViewHolder extends ViewHolder {
	
   private static final  int item_layout_id = R.layout.baobiao_xj_title;
	
	public TextView xj_bh;
	public TextView xj_km;
	public TextView xj_je;
	public TextView xj_ljje;
	
	@Override
	public void findViewWithAttribate(Context context) {
		if(contentView==null){
			contentView = LayoutInflater.from(context).inflate(item_layout_id, null);
		}
		xj_bh = (TextView) contentView.findViewById(R.id.xj_bh);
		xj_km = (TextView) contentView.findViewById(R.id.xj_km);
		xj_je = (TextView) contentView.findViewById(R.id.xj_je);
		xj_ljje = (TextView) contentView.findViewById(R.id.xj_ljje);
		rightArrow = contentView.findViewById(R.id.right_arrow);
	}
	
	@Override
	public View getContentView(Context context) {
		contentView = LayoutInflater.from(context).inflate(item_layout_id, null);
		return contentView;
	}

	@Override
	public void setValue(HashMap<String, String> entity) {
		xj_bh.setText(entity.get("personCode"));
		xj_km.setText(entity.get("fullName").trim());
		xj_je.setText(entity.get("totalNow"));
		xj_ljje.setText(entity.get("totalAll"));
	}
	
}
