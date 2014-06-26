/**
 * 创建日期 2012-12-17
 * 创建用户 Administrator
 * 变更情况:
 * 文档位置 $Archive$
 * 最后变更 $Author$
 * 变更日期 $Date$
 * 当前版本 $Revision$
 * 
 * Copyright (c) 2004 Sino-Japanese Engineering Corp, Inc. All Rights Reserved.
 * 
 */
package com.jetsoft.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NoScrollListView extends ListView 
{ 
	
    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    { 
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, 0) ); 
 
        // here I assume that height's being calculated for one-child only, seen it in ListView's source which is actually a bad idea 
        int childHeight = getMeasuredHeight() - (getListPaddingTop() + getListPaddingBottom() +  getVerticalFadingEdgeLength() * 2); 
 
        int fullHeight = getListPaddingTop() + getListPaddingBottom() + childHeight*(getCount())+getDividerHeight()*(getCount()-1); 
 
        setMeasuredDimension(getMeasuredWidth(), fullHeight); 
    } 
}