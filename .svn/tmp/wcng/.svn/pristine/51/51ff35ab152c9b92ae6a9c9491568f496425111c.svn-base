package com.jetsoft.activity.dynic;

import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.adapter.ProSelectAdapter;
import com.jetsoft.application.MyApplication;
import com.jetsoft.utils.StaticValues;

public class ProductViewHolder extends DetailViewHolder {
	
	TextView pro_name;
	TextView pro_danwei;
	TextView pro_shuliang;
	TextView pro_danjia;
	TextView pro_je;
	TextView pro_fzsl;
	TextView pro_guige;
	TextView pro_xinghao;
	TextView pro_zp;
	
	View convertView;
	
	private Activity activity;

	@Override
	public void setValue(HashMap<String, String> entity) {
		
		if (!entity.get("largess").equals("0")) {
			pro_zp.setText("是");
		}else{
			pro_zp.setText("否");
		}
		
		if(activity!=null){
			MyApplication application = (MyApplication) activity.getApplication();
			if(application.getRightCost() == 1){
				/**
				 * 有权限
				 */
				float je = 0;
				pro_danjia.setText(StaticValues.format(Float.parseFloat(entity.get("price")),application.getPriceDigit()));
				try {
					je = Float.parseFloat(entity.get("price"))* Float.parseFloat(entity.get("numUnit1"));
					if (!entity.get("largess").equals("0")) {
						je = 0;
					}
					pro_je.setText(StaticValues.format(je,application.getPriceDigit()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				pro_je.setText("*");
				pro_danjia.setText("*");
			}
		}
		
		pro_name.setText(entity.get("gFullName"));
		pro_danwei.setText(entity.get("unit1"));
		pro_shuliang.setText(StaticValues.format(entity.get("numUnit1"), 2));
		
		String fzsl_str = ProSelectAdapter.getNumUnitFz2(Float.parseFloat(entity.get("num")),
				entity.get("unitName0"), entity.get("unitName1"),
				entity.get("unitName2"), Float.parseFloat(entity.get("uRate0")), Float.parseFloat(entity.get("uRate1")), Float.parseFloat(entity.get("uRate2")));
		pro_fzsl.setText(fzsl_str);
		
		pro_guige.setText(entity.get("standard"));
		pro_xinghao.setText(entity.get("type"));
	}

	@Override
	public View getConvertView(Context context) {
		activity = (Activity) context;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.check_product_list_item, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		pro_name = (TextView) convertView.findViewById(R.id.pro_name);
		pro_danwei = (TextView) convertView.findViewById(R.id.pro_danwei);
		pro_shuliang = (TextView) convertView.findViewById(R.id.pro_shuliang);
		pro_danjia = (TextView) convertView.findViewById(R.id.pro_danjia);
		pro_je = (TextView) convertView.findViewById(R.id.pro_je);
		pro_fzsl = (TextView) convertView.findViewById(R.id.pro_fzsl);
		pro_guige = (TextView) convertView.findViewById(R.id.pro_guige);
		pro_xinghao = (TextView) convertView.findViewById(R.id.pro_xinghao);
		pro_zp = (TextView) convertView.findViewById(R.id.pro_zp);
	}

}
