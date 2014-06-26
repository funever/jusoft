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
package com.jetsoft.adapter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.jetsoft.R;
import com.jetsoft.entity.*;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class EntityAdapter extends BaseAdapter implements OnClickListener{

	private Context ctx;

	private List<BaseEntity> entityList;
	/**
	 * 保存是否选中
	 */
	public HashMap<Integer,Boolean> checkedMap = new HashMap<Integer,Boolean>();
	
	public List<Integer> checkShow = new LinkedList<Integer>();
	
	public boolean check_no_show;

	public EntityAdapter(Context ctx, List<BaseEntity> entityList) {
		this.ctx = ctx;
		this.entityList = entityList;
	}

	@Override
	public int getCount() {
		return entityList.size();
	}

	@Override
	public Object getItem(int position) {
		return entityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseEntity entity = entityList.get(position);
		ViewHolder holder = null; 
		if(convertView==null){
			holder = new ViewHolder();
			if (entity instanceof ProductEntity) {
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.pro_search_list_item, null);
				holder.pro_name = ((TextView) convertView.findViewById(R.id.pro_name));
				holder.pro_id = ((TextView) convertView.findViewById(R.id.pro_id));
				holder.pro_changdi = ((TextView) convertView.findViewById(R.id.pro_changdi));
				holder.check = (CheckBox) convertView.findViewById(R.id.pro_check);
			}else if (entity instanceof DanWeiEntity) {
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.danwei_search_list_item, null);
				holder.dw_name = ((TextView) convertView.findViewById(R.id.dw_name));
				holder.dw_id = ((TextView) convertView.findViewById(R.id.dw_id));
			}else if(entity instanceof JingShouRenEntity){
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.jsr_search_list_item, null);
				holder.jsr_name = ((TextView) convertView.findViewById(R.id.jsr_name));
				holder.jsr_id = ((TextView) convertView.findViewById(R.id.jsr_id));
			}else if(entity instanceof CangKuEntity){
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.ck_search_list_item, null);
				holder.ck_name = ((TextView) convertView.findViewById(R.id.ck_name));
				holder.ck_id = ((TextView) convertView.findViewById(R.id.ck_id));
			}else if(entity instanceof ZhangHuEntity){
				//部门
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.zh_search_list_item, null);
				holder.zh_name = ((TextView) convertView.findViewById(R.id.zh_name));
				holder.zh_id = ((TextView) convertView.findViewById(R.id.zh_id));
			}else if(entity instanceof BuMenEntity){
				//部门
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.bm_search_list_item, null);
				holder.bm_name = ((TextView) convertView.findViewById(R.id.bm_name));
				holder.bm_id = ((TextView) convertView.findViewById(R.id.bm_id));
			}else if(entity instanceof OrderEntity){
				convertView = LayoutInflater.from(ctx).inflate(R.layout.order_list_row, null);
				convertView.setBackgroundColor(Color.parseColor("#dddddd"));
				holder.order_id = (TextView) convertView.findViewById(R.id.order_id);
				holder.order_dw = (TextView) convertView.findViewById(R.id.order_dw);
				holder.order_date = (TextView) convertView.findViewById(R.id.order_date);
			}else if(entity instanceof AreaEntity){
				//部门
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.zh_search_list_item, null);
				holder.zh_name = ((TextView) convertView.findViewById(R.id.zh_name));
				holder.zh_id = ((TextView) convertView.findViewById(R.id.zh_id));
			}else if(entity instanceof PriceEntity){
				//部门
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.zh_search_list_item, null);
				holder.zh_name = ((TextView) convertView.findViewById(R.id.zh_name));
				holder.zh_id = ((TextView) convertView.findViewById(R.id.zh_id));
			}
			holder.right_arrow = (ImageView) convertView.findViewById(R.id.right_arrow);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (entity instanceof ProductEntity) {
			if(entity.getSonNum().equalsIgnoreCase("0")){
				checkShow.add(position);
			}
			holder.check.setTag(position);
			holder.pro_name.setText(((ProductEntity) entity).getFullName());
			holder.pro_id.setText(((ProductEntity) entity).getPersonCode());
			holder.pro_changdi.setText(((ProductEntity) entity).getStandard());
			if(check_no_show){
				holder.check.setVisibility(View.GONE);
			}else{
				if(checkShow.contains(position)){
					holder.check.setVisibility(View.VISIBLE);
					if(!checkedMap.containsKey(position)){
						holder.check.setChecked(false);
					}else{
						holder.check.setChecked(true);
					}
				}else{
					holder.check.setVisibility(View.INVISIBLE);
				}
			}
			//不能使用OnChangeListener事件监听，因为在listView中会有可能销毁，自动刷新出来后就自动触发了此事件
			holder.check.setOnClickListener(this);
		} else if (entity instanceof DanWeiEntity) {
			// 选择单位的list item构造
			holder.dw_name.setText(((DanWeiEntity) entity).getFullName());
			holder.dw_id.setText(((DanWeiEntity) entity).getPersonCode());
		} else if(entity instanceof JingShouRenEntity){
			//经手人
			holder.jsr_name.setText(((JingShouRenEntity) entity).getFullName());
			holder.jsr_id.setText(((JingShouRenEntity) entity).getPersonCode());
		} else if(entity instanceof CangKuEntity){
			//仓库
			holder.ck_name.setText(((CangKuEntity) entity).getFullName());
			holder.ck_id.setText(((CangKuEntity) entity).getPersonCode());
		}else if(entity instanceof BuMenEntity){
			//部门
			holder.bm_name.setText(((BuMenEntity) entity).getFullName());
			holder.bm_id.setText(((BuMenEntity) entity).getPersonCode());
		}else if(entity instanceof ZhangHuEntity){
			//部门
			holder.zh_name.setText(((ZhangHuEntity) entity).getFullName());
			holder.zh_id.setText(((ZhangHuEntity) entity).getPersonCode());
		}else if(entity instanceof OrderEntity){
			convertView.setBackgroundColor(Color.parseColor("#dddddd"));
			holder.order_id.setText(((OrderEntity)entity).getNumber());
			holder.order_dw.setText(((OrderEntity)entity).getuFullName());
			holder.order_date.setText(((OrderEntity)entity).getDate());
		}else if(entity instanceof AreaEntity){
			holder.zh_name.setText(((AreaEntity) entity).getFullName());
			holder.zh_id.setText(((AreaEntity) entity).getPersonCode());
		}else if(entity instanceof PriceEntity){
			PriceEntity pe = (PriceEntity) entity;
			holder.zh_name.setText(pe.getFullName());
			holder.zh_id.setText(pe.getId());
			holder.right_arrow.setVisibility(View.INVISIBLE);
		}
		try{
			int num = 0;
			String n = entity.getSonNum();
			num = Integer.parseInt(n);
			if(num==0){
				holder.right_arrow.setVisibility(View.INVISIBLE);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		if(v instanceof CheckBox){
			int postion = (Integer)v.getTag();
			if(((CheckBox)v).isChecked()){
				checkedMap.put(postion, true);
			}else{
				checkedMap.remove(postion);
			}
		}
	}
	
	class ViewHolder{
		//ProductEntity
		public TextView pro_name;
		public TextView pro_id;
		public TextView pro_changdi;
		public CheckBox check;
		//DanWeiEntity
		public TextView dw_name;
		public TextView dw_id;
		//JingShouRenEntity
		public TextView jsr_name;
		public TextView jsr_id;
		//CangKuEntity
		public TextView ck_name;
		public TextView ck_id;
		//BuMenEntity
		public TextView bm_name;
		public TextView bm_id;
		//OrderEntity
		public TextView order_id;
		public TextView order_dw;
		public TextView order_date;
		//AreaEntity&PriceEntity&ZhangHuEntity
		public TextView zh_name;
		public TextView zh_id;
		
		public ImageView right_arrow;
	}

}
