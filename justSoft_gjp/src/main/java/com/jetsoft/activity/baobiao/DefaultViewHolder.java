package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import com.jetsoft.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DefaultViewHolder extends ViewHolder{
	
	private static final int  item_layout_id = R.layout.search_list_item;
	
	public TextView title1;
	public TextView title2;
	public TextView title3;
	public TextView title4;
	public TextView title5;
	
	public TextView value1;
	public TextView value2;
	public TextView value3;
	public TextView value4;
	public TextView value5;
	
	@Override
	public void findViewWithAttribate(Context context) {
		if(contentView==null){
			contentView = getContentView(context);
		}
		title1 = (TextView) contentView.findViewById(R.id.title1);
		title2 = (TextView) contentView.findViewById(R.id.title2);
		title3 = (TextView) contentView.findViewById(R.id.title3);
		title4 = (TextView) contentView.findViewById(R.id.title4);
		title5 = (TextView) contentView.findViewById(R.id.title5);
		
		value1 = (TextView) contentView.findViewById(R.id.value1);
		value2 = (TextView) contentView.findViewById(R.id.value2);
		value3 = (TextView) contentView.findViewById(R.id.value3);
		value4 = (TextView) contentView.findViewById(R.id.value4);
		value5 = (TextView) contentView.findViewById(R.id.value5);
		
		rightArrow = contentView.findViewById(R.id.right_arrow);
	}

	@Override
	public View getContentView(Context context) {
		contentView = LayoutInflater.from(context).inflate(item_layout_id, null);
		return contentView;
	}

	@Override
	public void setValue(HashMap<String, String> entity) {
		
	}
}
