package com.jetsoft;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Group {
	
	String title;
	
	boolean show;
	
	List<Menu> mList;
	
	int layoutId;
	
	View view;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public List<Menu> getmList() {
		return mList;
	}

	public void setmList(List<Menu> mList) {
		this.mList = mList;
	}

	public int getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(int layoutId,Context ctx) {
		this.layoutId = layoutId;
		setView(ctx);
	}
	
	private void setView(Context ctx){
		LayoutInflater inflater = LayoutInflater.from(ctx);
		view = inflater.inflate(layoutId, null);
	}
}
