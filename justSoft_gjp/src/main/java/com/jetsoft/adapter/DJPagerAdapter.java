package com.jetsoft.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jetsoft.activity.BaseActivity;

/**
 * Created by funever on 2014/6/6.
 */
public class DJPagerAdapter extends PagerAdapter{

    int[] pager;

    BaseActivity activity;

    public DJPagerAdapter(BaseActivity activity,int[] pager){
        this.activity = activity;
        this.pager = pager;
    }
    @Override
    public int getCount() {
        return pager.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view =  LayoutInflater.from(activity).inflate(pager[position],null);
        container.addView(view);
        activity.initView(position,view);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "基本信息";
        }else{
            return "添加商品";
        }
    }
}
