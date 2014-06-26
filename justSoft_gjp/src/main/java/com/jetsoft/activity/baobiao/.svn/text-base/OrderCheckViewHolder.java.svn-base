package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jetsoft.R;
import com.jetsoft.activity.baobiao.DetailViewHolder;
import com.jetsoft.utils.StaticValues;

/**
 * {"amount":"248.0000000000",
 * "bilID":"146",
 * "bilName":"进货单",
 * "bilType":"34",
 * "date":"2014-03-13",
 * "eName":"袁易",
 * "iCanDo":"1",
 * "inputName":"管理员",
 * "memo":"",
 * "number":"JH-2014-03-13-017",
 * "state":"待审核",
 * "summary":"从【成都果金贸易有限公司】购进【榄菊高级型黑蚊香超长装】等：袁易","uName":"成都果金贸易有限公司"}
 * @author funever
 *
 */
public class OrderCheckViewHolder extends DetailViewHolder {
	
	View convertView;
	
	TextView date;
	TextView number;
	TextView amount;
	TextView uName;
	TextView eName;
	TextView inputName;
	TextView summary;
	TextView memo;
	TextView state;
	TextView bilName;

	@Override
	public View getConvertView(Context context) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.order_check_list_item, null);
		}
		return convertView;
	}

	@Override
	public void findViewbyId() {
		if(convertView==null){
			return;
		}
		date = (TextView) convertView.findViewById(R.id.date);
		number = (TextView) convertView.findViewById(R.id.number);
		amount = (TextView) convertView.findViewById(R.id.amount);
		uName = (TextView) convertView.findViewById(R.id.uName);
		eName = (TextView) convertView.findViewById(R.id.eName);
		inputName = (TextView) convertView.findViewById(R.id.inputName);
		summary = (TextView) convertView.findViewById(R.id.summary);
		memo = (TextView) convertView.findViewById(R.id.memo);
		state = (TextView) convertView.findViewById(R.id.state);
		bilName = (TextView) convertView.findViewById(R.id.bilName);
	}

	@Override
	public void setValue(HashMap<String, String> itemMap) {
		date.setText(itemMap.get("date"));
		number.setText(itemMap.get("number"));
		amount.setText(StaticValues.format(itemMap.get("amount"), 2));
		uName.setText(itemMap.get("uName"));
		eName.setText(itemMap.get("eName"));
		inputName.setText(itemMap.get("inputName"));
		summary.setText(itemMap.get("summary"));
		memo.setText(itemMap.get("memo"));
		state.setText(itemMap.get("state"));
		bilName.setText(itemMap.get("bilName"));
	}

}
