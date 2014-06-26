package com.jetsoft.activity.baobiao;

import java.util.HashMap;
import com.jetsoft.R;
import com.jetsoft.application.MyApplication;
import com.jetsoft.utils.StaticValues;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class JYLCViewHolder extends ViewHolder {
	
   private static final  int item_layout_id = R.layout.baobiao_jylc_title;
	
	public TextView date;
	public TextView nUMBER;
	public TextView bilName;
	public TextView num;
	public TextView amount;
	public TextView summary;
	public TextView eFullName;
	public TextView uFullName;
	public TextView dFullName;
	public TextView memo;
	
	private Activity activity; 
	
	@Override
	public void findViewWithAttribate(Context context) {
		if(contentView==null){
			contentView = LayoutInflater.from(context).inflate(item_layout_id, null);
		}
		activity = (Activity) context;
		date = (TextView) contentView.findViewById(R.id.date);
		nUMBER = (TextView) contentView.findViewById(R.id.nUMBER);
		bilName = (TextView) contentView.findViewById(R.id.bilName);
		num = (TextView) contentView.findViewById(R.id.num);
		amount = (TextView) contentView.findViewById(R.id.amount);
		summary = (TextView) contentView.findViewById(R.id.summary);
		eFullName = (TextView) contentView.findViewById(R.id.eFullName);
		uFullName = (TextView) contentView.findViewById(R.id.uFullName);
		dFullName = (TextView) contentView.findViewById(R.id.dFullName);
		memo = (TextView) contentView.findViewById(R.id.memo);
		rightArrow = contentView.findViewById(R.id.right_arrow);
	}
	
	@Override
	public View getContentView(Context context) {
		contentView = LayoutInflater.from(context).inflate(item_layout_id, null);
		return contentView;
	}

	@Override
	public void setValue(HashMap<String, String> entity) {
		date.setText(entity.get("date"));
		nUMBER.setText(entity.get("nUMBER").trim());
		bilName.setText(entity.get("bilName"));
		num.setText(entity.get("num"));
		summary.setText(entity.get("summary").trim());
		eFullName.setText(entity.get("eFullName"));
		uFullName.setText(entity.get("uFullName"));
		dFullName.setText(entity.get("dFullName"));
		memo.setText(entity.get("memo").trim());
		
		String amount_value = entity.get("amount");
		MyApplication application = (MyApplication) activity.getApplication();
		try{
			amount_value = ""+StaticValues.format(Float.parseFloat(amount_value), application.getAmountDigit());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String bilType = entity.get("bilType");
		if(application.getRightCost()==1
				|| bilType.equals(StaticValues.SALE_IN_BILTYPE)
				|| bilType.equals(StaticValues.SALE_BILTYPE) 
				|| bilType.equals(StaticValues.SALE_RE_BILTYPE)){
			amount.setText(amount_value);
		}else{
			amount.setText("*");
		}
	}
	
}
