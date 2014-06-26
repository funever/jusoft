package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import android.content.Context;
import android.view.View;

public abstract class ViewHolder {
	
	protected View contentView;
	
	public View rightArrow;
	
	public abstract void findViewWithAttribate(Context context);
	
	public abstract View getContentView(Context context);
	
	public abstract void setValue(HashMap<String, String> entity);
	
}
