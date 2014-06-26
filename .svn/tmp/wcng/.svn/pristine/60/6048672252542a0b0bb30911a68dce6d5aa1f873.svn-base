package com.jetsoft.activity.baobiao;

import java.util.HashMap;

import com.jetsoft.activity.dynic.KeMuViewHolder;
import com.jetsoft.activity.dynic.ProductViewHolder;
import com.jetsoft.activity.kucun.KucunViewHolder;
import com.jetsoft.activity.warning.OrderWarnViewHolder;
import com.jetsoft.activity.warning.SaleWarnViewHolder;
import com.jetsoft.activity.warning.OutTimeViewHolder;
import com.jetsoft.activity.warning.WarnUpDownViewHolder;

import android.content.Context;
import android.view.View;

public abstract class DetailViewHolder {
	
	public static final int OUTTIME_TYPE = 101;
	public static final int ORDERWARN_TYPE_ORDER = 102;
	public static final int ORDERWARN_TYPE_SALE = 103;
	public static final int ORDERCHECK_TYPE = 104;
	public static final int PROFIT_TYPE = 105;
	public static final int MONTH_IN_SALE_TYPE = 106;
	public static final int ORDERWARN_TYPE_UP = 107;
	public static final int KM_TYPE = 108;
	public static final int KUCUN_LIST_TYPE = 109;
	public static final int PRODUCT_TYPE = 110;
	public static final int ORDERWARN_TYPE_DOWN = 111;
	
	public static DetailViewHolder getViewHolder(int type){
		DetailViewHolder dh = null;
		if(type == BaoBiaoActivity.XJYH){
		    dh = new XJDetailViewHolder();
		}else if(type == BaoBiaoActivity.WLDW){
			dh = new WLDetailViewHolder();
		}else if(type == OUTTIME_TYPE){
			dh = new OutTimeViewHolder();
		}else if(type == ORDERWARN_TYPE_SALE){
			dh = new SaleWarnViewHolder();
		}else if(type == ORDERWARN_TYPE_ORDER){
			dh = new OrderWarnViewHolder();
		}else if(type == PROFIT_TYPE){
			dh = new ProfitDetailViewHolder();
		}else if(type == MONTH_IN_SALE_TYPE){
			dh = new MonthSaleDetailViewHolder();
		}else if(type == ORDERCHECK_TYPE){
			dh = new OrderCheckViewHolder();
		}else if(type == ORDERWARN_TYPE_UP){
			dh = new WarnUpDownViewHolder(WarnUpDownViewHolder.TYPE_UP);
		}else if(type == ORDERWARN_TYPE_DOWN){
			dh = new WarnUpDownViewHolder(WarnUpDownViewHolder.TYPE_DOWN);
		}else if(type == KM_TYPE){
			dh = new KeMuViewHolder();
		}else if(type == KUCUN_LIST_TYPE){
			dh = new  KucunViewHolder();
		}else if(type == PRODUCT_TYPE){
			dh = new ProductViewHolder();
		}
		return  dh;
	}
	
	public abstract View getConvertView(Context context);
	
	public abstract void findViewbyId();
	
	public abstract void setValue(HashMap<String, String> itemMap);
	
}
