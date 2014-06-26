/**
 * 创建日期 2013-2-20
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
package com.jetsoft.activity.kucun;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jetsoft.R;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.entity.PanDianProduct;
import com.jetsoft.utils.StaticValues;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 用于修改商品的盘点数量的页面
 * 
 *
 * @version 1.0
 * @author Administrator
 */
public class KuCunProductEditActivity extends SherlockActivity implements OnClickListener{
	
	PanDianProduct product;
	
	TextView pro_name,pro_kucunnum,pro_price,pro_amount,pro_guige,pro_xinghao,pro_changdi;
	
	EditText pro_pandiannum;
	
	Button pro_delete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pandian_product_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("编辑商品数量");
		
		product = (PanDianProduct) getIntent().getSerializableExtra("product");
//		entity = (ProductEntity) getIntent().getSerializableExtra("entity");
		
		pro_name = (TextView) findViewById(R.id.pro_name);
		pro_kucunnum = (TextView) findViewById(R.id.pro_kucunnum);
		pro_price = (TextView) findViewById(R.id.pro_price);
		pro_amount = (TextView) findViewById(R.id.pro_amount);
		pro_guige = (TextView) findViewById(R.id.pro_guige);
		pro_xinghao = (TextView) findViewById(R.id.pro_xinghao);
		pro_changdi = (TextView) findViewById(R.id.pro_changdi);

		pro_pandiannum = (EditText) findViewById(R.id.pro_pandiannum);
		
		pro_name.setText(product.getFullName());
		pro_kucunnum.setText(product.getNum()+"");
		pro_price.setText(product.getPrice());
		pro_amount.setText(product.getAmount());
		pro_guige.setText(product.getStandard());
		pro_xinghao.setText(product.getType());
		pro_changdi.setText(product.getArea());
		
		pro_pandiannum.setText(product.getPanDianNum()+"");
		pro_pandiannum.setOnClickListener(this);
		
		pro_delete = (Button) findViewById(R.id.pro_delete);
		
		pro_delete.setOnClickListener(this);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem saveItem = menu.add(0,10001,0,"确定");
        saveItem.setIcon(android.R.drawable.ic_menu_save);
        saveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT|MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 10001){
            product.setPanDianNum(Integer.parseInt(pro_pandiannum.getText().toString()));
            Intent intent = new Intent();
            intent.putExtra("product", product);
            setResult(RESULT_OK, intent);
            this.finish();
        }else if(item.getItemId() == android.R.id.home){
            this.finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	public void onClick(View v) {
		 if(v == pro_delete){
			Intent intent = new Intent();
			intent.putExtra("product", product);
			setResult(EntityListActivity.DELETE_PRODUCT, intent);
			this.finish();
		}else if(v == pro_pandiannum){
			editNum();
		}
	}
	
	/**
	 * 修改数量
	 * 计算数量=基本数量* URate0+ URate1*辅助数量1+ URate2*辅助数量2
	 * 单据界面的商品数量=计算数量/URateBil
	 */
	public void editNum(){
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.waimao_edit_num_price);
		final EditText pro_count = (EditText) dialog.findViewById(R.id.pro_num);
		final EditText pro_unit1 = (EditText) dialog.findViewById(R.id.pro_unit1);
		final EditText pro_unit2 = (EditText) dialog.findViewById(R.id.pro_unit2);
//		EditText pro_price = (EditText) dialog.findViewById(R.id.pro_price);
		dialog.findViewById(R.id.price_layout).setVisibility(View.GONE);
		dialog.findViewById(R.id.ok).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				float num = 0;
				try{
					num = Float.parseFloat(pro_count.getText().toString());
				}catch(Exception e){
					num = 0;
				}
				float unit1 = 0;
				try{
					unit1 = Float.parseFloat(pro_unit1.getText().toString());
				}catch(Exception e){
					unit1 = 0;
				}
				float unit2 = 0;
				try{
					unit2 = Float.parseFloat(pro_unit2.getText().toString());
				}catch(Exception e){
					unit2 = 0;
				}
				float count = num*Float.parseFloat(product.getuRate0())+unit1*Float.parseFloat(product.getuRate1())+unit2*Float.parseFloat(product.getuRate2());
				float uRateBil  = 1;
				try{
					uRateBil = Float.parseFloat(product.getuRateBil());
				}catch(Exception e){
					uRateBil = 1;
				}
				pro_pandiannum.setText(StaticValues.format(count/uRateBil,4));
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.cancel).setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.recent_sale).setVisibility(View.GONE);
		dialog.show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
