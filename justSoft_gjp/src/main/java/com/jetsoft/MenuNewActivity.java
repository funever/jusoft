package com.jetsoft;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import me.imid.view.SwitchButton;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.funever.bluetoochscanner.BluetoochUtils;
import com.jetsoft.activity.EntityListActivity;
import com.jetsoft.activity.TabHostActivity;
import com.jetsoft.activity.baobiao.BaoBiaoActivity;
import com.jetsoft.activity.baobiao.BaoBiaoItemActivity;
import com.jetsoft.activity.baobiao.BaoBiaoResultActivity;
import com.jetsoft.activity.baobiao.OrderCheckActivity;
import com.jetsoft.activity.draft.DraftActivity;
import com.jetsoft.activity.kucun.KuCunActivity;
import com.jetsoft.activity.kucun.PandianListActivity;
import com.jetsoft.activity.money.MoneyActivity;
import com.jetsoft.activity.order.OrderActivity;
import com.jetsoft.activity.returnorder.ReturnOrderActivity;
import com.jetsoft.activity.sale.SaleActivity;
import com.jetsoft.activity.sale.WaiMaoActivity;
import com.jetsoft.activity.warning.OrderWarningActivity;
import com.jetsoft.activity.warning.OutTimeActivity;
import com.jetsoft.activity.warning.WarnUpDownActivity;
import com.jetsoft.application.MyApplication;
import com.jetsoft.application.SetValue;
import com.jetsoft.entity.CangKuEntity;
import com.jetsoft.entity.DanWeiEntity;
import com.jetsoft.io.CommunicationServer;
import com.jetsoft.io.ExcuteThread;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.viewpagerindicator.TabPageIndicator;

import net.simonvt.menudrawer.MenuDrawer;

/**
 * 新样式的菜单列表
 * @author funever_win8
 *
 */
public class MenuNewActivity extends SherlockActivity implements OnClickListener{

    ViewPager viewPager;

	PagerTabStrip pagerTabStrip;

//	List<View> menuList;
//	
//	List<String> titleList;
	
	List<Group> groups;
	
	private RelativeLayout baobiao_outtime,baobiao_order_warning,baobiao_sale_warning,kc_warn_up,kc_warn_down;
	
	/**
	 * 进货管理的菜单
	 */
	private RelativeLayout order_in,order_re,order_jinhuodan;
	/**
	 * 销售管理的菜单
	 */
	private RelativeLayout sale_waimao,sale_waimao_order,sale_order,sale_note,sale_return;
	/**
	 * 库存管理的菜单
	 */
	private RelativeLayout kuncun_tiaojian,kuncun_tongjian,kucun_pandian;
	/**
	 * 钱流广利的菜单
	 */
	private RelativeLayout money_skd,money_fkd;
	/**
	 * 报表的菜单
	 */
	private RelativeLayout baobiao_wldw,baobiao_xjyh,baobiao_jhhz,baobiao_xshz,baobiao_kczk,baobiao_jylc,baobiao_spcx,order_check,baobiao_profit,baobiao_month_sale;
	
	private RelativeLayout draft_order,draft_sale,draft_kc,draft_money,draft_baobiao,menu_set;
	
	private TextView zt_name;
	
	private MyApplication application;
	
	private String server;
	
	/**
	 * 验证权限地址
	 */
	private String actionUrl;
	
	private Handler handler = new Handler();
	
	/**
	 * 设置默认仓库和默认单位的
	 */
	private EditText ck_edit,dw_edit;
	
	private ImageView ck_action,dw_action;
	
	private Button ck_dw_submit;
	
	private RelativeLayout scanner,painter;
	
	private ImageView clear_ck,clear_dw;
	
	private CangKuEntity ck_entity;
	
	private DanWeiEntity dw_entity;
	
	SharedPreferences preferences;
	
	public BluetoochUtils bu;
	
	TextView show_ip_port;
	/**
	 * ViewPager adapter
	 */
	MyPageAdapter adapter;
	
	MenuController mc;
    /**
     * 侧菜单
     */
    public MenuDrawer menuDrawer;

    LeftMenu leftMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.menu_new);

        getSupportActionBar().setTitle("主菜单");

        menuDrawer = MenuDrawer.attach(this);
        menuDrawer.setContentView(R.layout.menu_new);
        menuDrawer.setDrawerIndicatorEnabled(true);
        menuDrawer.setSlideDrawable(R.drawable.abs__ic_ab_back_holo_light);
        leftMenu = new LeftMenu(this);
        leftMenu.initView();

		application = (MyApplication) getApplication();
		mc = new MenuController(this);
		initView();
		/**
		 * 获取服务器地址和验证权限的url
		 */
		preferences = getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, getString(R.string.server))+":"
		+ preferences.getString(StaticValues.PORT_FLAG, getString(R.string.port));
		actionUrl = getString(R.string.menu_action);
		
		try{
			zt_name  = (TextView) LayoutInflater.from(this).inflate(R.layout.zt_text, null);
			zt_name.setText(application.getZtEntity().getFullName());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		WindowManager manager = getWindowManager();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				-1,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.BOTTOM | Gravity.LEFT;
//		manager.addView(zt_name, params);
		
		bu = BluetoochUtils.getInstance(this, handler, null);
		
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int arg0, UpdateResponse arg1) {
				if(arg0 == 1){
					Toast.makeText(MenuNewActivity.this, "您的程序已经是最新版本!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateListener(null);
		UmengUpdateAgent.setDownloadListener(null);
		UmengUpdateAgent.setDialogListener(null);
	}
	
	/**
	 * 初始化界面
	 */
	public void initView(){
		viewPager = (ViewPager) findViewById(R.id.menu_pager);
		groups = mc.getGroups();
		adapter = new MyPageAdapter();
		viewPager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
	}
	
	/**
	 * ViewPager的页面适配器
	 * @author funever_win8
	 *
	 */
	class MyPageAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return groups.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

		 @Override
         public void destroyItem(ViewGroup container, int position,
                 Object object) {
			 container.removeView(groups.get(position).view);
         }

        @SuppressLint("WrongViewCast")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Group g = groups.get(position);
            container.addView(g.view);
            if (g.getLayoutId() == R.layout.menu_warning_menu) {
                baobiao_outtime = (RelativeLayout) findViewById(R.id.baobiao_outtime);
                baobiao_order_warning = (RelativeLayout) findViewById(R.id.baobiao_order_warning);
                baobiao_sale_warning = (RelativeLayout) findViewById(R.id.baobiao_sale_warning);
                baobiao_outtime.setOnClickListener(MenuNewActivity.this);
                baobiao_order_warning.setOnClickListener(MenuNewActivity.this);
                baobiao_sale_warning.setOnClickListener(MenuNewActivity.this);
                kc_warn_up = (RelativeLayout) findViewById(R.id.kc_warn_up);
                kc_warn_down = (RelativeLayout) findViewById(R.id.kc_warn_down);
                kc_warn_up.setOnClickListener(MenuNewActivity.this);
                kc_warn_down.setOnClickListener(MenuNewActivity.this);

                for (com.jetsoft.Menu m : g.getmList()) {
                    if (!m.isShow()) {
                        if (m.getTitle().equals("超期应收款报表")) {
                            baobiao_outtime.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("进货订单报警报表")) {
                            baobiao_order_warning.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("销售订单报警报表")) {
                            baobiao_sale_warning.setVisibility(View.GONE);
                        }else if(m.getTitle().equals("库存上限报警")){
                            kc_warn_up.setVisibility(View.GONE);
                        }else if(m.getTitle().equals("库存下限报警")){
                            kc_warn_down.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (g.getLayoutId() == R.layout.menu_order_menu) {
                // 加载进货类菜单
                order_in = (RelativeLayout) findViewById(R.id.order_in);
                order_re = (RelativeLayout) findViewById(R.id.order_re);
                order_jinhuodan = (RelativeLayout) findViewById(R.id.jinhuodan);
                order_in.setOnClickListener(MenuNewActivity.this);
                order_re.setOnClickListener(MenuNewActivity.this);
                order_jinhuodan.setOnClickListener(MenuNewActivity.this);

                draft_order = (RelativeLayout) findViewById(R.id.draft_order_action);
                draft_order.setOnClickListener(new DraftClickListener(
                        DraftActivity.DRAFT_TYPE_ORDER));

                for (com.jetsoft.Menu m : g.getmList()) {
                    if (!m.isShow()) {
                        if (m.getTitle().equals("进货订单")) {
                            order_in.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("进货退货单")) {
                            order_re.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("进货单")) {
                            order_jinhuodan.setVisibility(View.GONE);
                        }
                        if (m.getTitle().equals("进货草稿管理")) {
                            draft_order.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (g.getLayoutId() == R.layout.menu_sale_menu) {
                // 销售
                sale_waimao = (RelativeLayout) findViewById(R.id.sale_waimao);
                sale_waimao_order = (RelativeLayout) findViewById(R.id.sale_waimao_order);
                sale_order = (RelativeLayout) findViewById(R.id.sale_order);
                sale_note = (RelativeLayout) findViewById(R.id.sale_note);
                sale_return = (RelativeLayout) findViewById(R.id.sale_return);

                sale_waimao.setOnClickListener(MenuNewActivity.this);
                sale_waimao_order.setOnClickListener(MenuNewActivity.this);
                sale_order.setOnClickListener(MenuNewActivity.this);
                sale_note.setOnClickListener(MenuNewActivity.this);
                sale_return.setOnClickListener(MenuNewActivity.this);

                draft_sale = (RelativeLayout) findViewById(R.id.draft_sale_action);
                draft_sale.setOnClickListener(new DraftClickListener(
                        DraftActivity.DRAFT_TYPE_SALE));

                for (com.jetsoft.Menu m : g.getmList()) {
                    if (!m.isShow()) {
                        if (m.getTitle().equals("外贸订单")) {
                            sale_waimao.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("外贸销售单")) {
                            sale_waimao_order.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("销售订单")) {
                            sale_order.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("销售单")) {
                            sale_note.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("销售退货")) {
                            sale_return.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("销售草稿管理")) {
                            draft_sale.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (g.getLayoutId() == R.layout.menu_kucun_menu) {
                // 库存
                kuncun_tiaojian = (RelativeLayout) findViewById(R.id.bianjiatiao);
                kuncun_tongjian = (RelativeLayout) findViewById(R.id.tongjiatiao);
                kucun_pandian = (RelativeLayout) findViewById(R.id.kucunpandian);

                kuncun_tiaojian.setOnClickListener(MenuNewActivity.this);
                kuncun_tongjian.setOnClickListener(MenuNewActivity.this);
                kucun_pandian.setOnClickListener(MenuNewActivity.this);

                draft_kc = (RelativeLayout) findViewById(R.id.draft_action);
                draft_kc.setOnClickListener(new DraftClickListener(
                        DraftActivity.DRAFT_TYPE_KC));

                for (com.jetsoft.Menu m : g.getmList()) {
                    if (!m.isShow()) {
                        if (m.getTitle().equals("变价调拨单")) {
                            kuncun_tiaojian.setVisibility(View.GONE);
                        } else if(m.getTitle().equals("同价调拨单")) {
                            kuncun_tiaojian.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("库存盘点")) {
                            kucun_pandian.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("库存草稿管理")) {
                            draft_kc.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (g.getLayoutId() == R.layout.menu_money_menu) {
                // 钱流
                money_skd = (RelativeLayout) findViewById(R.id.money_skd);
                money_fkd = (RelativeLayout) findViewById(R.id.money_fkd);

                money_skd.setOnClickListener(MenuNewActivity.this);
                money_fkd.setOnClickListener(MenuNewActivity.this);

                draft_money = (RelativeLayout) findViewById(R.id.draft_money_action);
                draft_money.setOnClickListener(new DraftClickListener(
                        DraftActivity.DRAFT_TYPE_MONEY));

                for (com.jetsoft.Menu m : g.getmList()) {
                    if (!m.isShow()) {
                        if (m.getTitle().equals("收款单")) {
                            money_skd.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("付款单")) {
                            money_fkd.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("收付款草稿管理")) {
                            draft_money.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (g.getLayoutId() == R.layout.menu_pandian_menu) {
                // 报表
                baobiao_wldw = (RelativeLayout) findViewById(R.id.baobiao_wldw);
                baobiao_jhhz = (RelativeLayout) findViewById(R.id.baobiao_jhhz);
                baobiao_xshz = (RelativeLayout) findViewById(R.id.baobiao_xshz);
                baobiao_kczk = (RelativeLayout) findViewById(R.id.baobiao_kczk);
                baobiao_jylc = (RelativeLayout) findViewById(R.id.baobiao_jylc);
                baobiao_spcx = (RelativeLayout) findViewById(R.id.baobiao_spcx);
                baobiao_xjyh = (RelativeLayout) findViewById(R.id.baobiao_xjyh);
                order_check = (RelativeLayout) findViewById(R.id.order_check);
                baobiao_profit = (RelativeLayout) findViewById(R.id.baobiao_profit);
                baobiao_month_sale = (RelativeLayout) findViewById(R.id.baobiao_month_sale);

                baobiao_wldw.setOnClickListener(MenuNewActivity.this);
                baobiao_jhhz.setOnClickListener(MenuNewActivity.this);
                baobiao_xshz.setOnClickListener(MenuNewActivity.this);
                baobiao_kczk.setOnClickListener(MenuNewActivity.this);
                baobiao_jylc.setOnClickListener(MenuNewActivity.this);
                baobiao_spcx.setOnClickListener(MenuNewActivity.this);
                baobiao_xjyh.setOnClickListener(MenuNewActivity.this);
                order_check.setOnClickListener(MenuNewActivity.this);
                baobiao_profit.setOnClickListener(MenuNewActivity.this);
                baobiao_month_sale.setOnClickListener(MenuNewActivity.this);

                draft_baobiao = (RelativeLayout) findViewById(R.id.draft_baobiao_action);
                draft_baobiao.setOnClickListener(new DraftClickListener(
                        DraftActivity.DRAFT_TYPE_BAOBIAO));

                for (com.jetsoft.Menu m : g.getmList()) {
                    if (!m.isShow()) {
                        if (m.getTitle().equals("单位应收应付")) {
                            baobiao_wldw.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("现金银行")) {
                            baobiao_xjyh.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("商品进货汇总表")) {
                            baobiao_jhhz.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("商品销售汇总表")) {
                            baobiao_xshz.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("库存状况表")) {
                            baobiao_kczk.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("经营历程")) {
                            baobiao_jylc.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("商品查询")) {
                            baobiao_spcx.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("单据审核")) {
                            order_check.setVisibility(View.GONE);
                        } else if (m.getTitle().equals("利润表报表")) {
                            baobiao_profit.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (g.getLayoutId() == R.layout.setting_ck_dw) {
                // 设置默认仓库和默认单位的
                ck_edit = (EditText) findViewById(R.id.ck_edit);
                ck_action = (ImageView) findViewById(R.id.ck_add);

                dw_edit = (EditText) findViewById(R.id.wldw_edit);
                dw_action = (ImageView) findViewById(R.id.dw_add);
                ck_action.setOnClickListener(MenuNewActivity.this);
                dw_action.setOnClickListener(MenuNewActivity.this);

                ck_dw_submit = (Button) findViewById(R.id.submit);
                ck_dw_submit.setOnClickListener(MenuNewActivity.this);

                scanner = (RelativeLayout) findViewById(R.id.scanner);
                scanner.setOnClickListener(MenuNewActivity.this);

                painter = (RelativeLayout) findViewById(R.id.painter);
                painter.setOnClickListener(MenuNewActivity.this);

                clear_ck = (ImageView) findViewById(R.id.clear_ck);
                clear_dw = (ImageView) findViewById(R.id.clear_dw);
                clear_dw.setOnClickListener(MenuNewActivity.this);
                clear_ck.setOnClickListener(MenuNewActivity.this);

                menu_set = (RelativeLayout) findViewById(R.id.menu_set);
                menu_set.setOnClickListener(MenuNewActivity.this);

                /**
                 * 获取默认设定的单位
                 */
                String dw_string = preferences.getString(
                        StaticValues.DEFAULT_DW, null);
                if (dw_string != null) {
                    try {
                        JSONObject dw_json = new JSONObject(dw_string);
                        DanWeiEntity dw_entity = Utils.parseJSONObjectToObject(
                                DanWeiEntity.class, dw_json);
                        dw_edit.setText(dw_entity.getFullName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /**
                 * 获取默认设置的仓库
                 */
                String ck_string = preferences.getString(
                        StaticValues.DEFAULT_CK, null);
                if (ck_string != null) {
                    try {
                        JSONObject ckJsonObject = new JSONObject(ck_string);
                        CangKuEntity ck_entity = Utils.parseJSONObjectToObject(
                                CangKuEntity.class, ckJsonObject);
                        ck_edit.setText(ck_entity.getFullName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /**
                 * 设置打印机地址和端口的显示
                 */
                show_ip_port = (TextView) findViewById(R.id.show_ip_port);
                String ip = preferences.getString(StaticValues.PRINTER_IP, "");
                String port = preferences.getString(StaticValues.PRINTER_PORT,
                        "");
                if (!ip.equals("") && !port.equals("")) {
                    show_ip_port.setText(ip + ":" + port);
                }
                // boolean active =
                // preferences.getBoolean(StaticValues.PRINTER_ACTIVE, false);
                // if(!active){
                // show_ip_port.setTextColor(Color.GRAY);
                // }
                findViewById(R.id.company_info).setOnClickListener(MenuNewActivity.this);
            }
            return g.view;
        }

         @Override
         public int getItemPosition(Object object) {
             return super.getItemPosition(object);
         }

         @Override
         public CharSequence getPageTitle(int position) {
//             return titleList.get(position);//直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。
             return groups.get(position).getTitle();
         }
	}

	@Override
	public void onClick(View v) {
		HashMap<String, String> param = new HashMap<String, String>();
        if(application.getZtEntity()==null){
            Toast.makeText(this, "未登录!", Toast.LENGTH_SHORT).show();
            menuDrawer.toggleMenu();
            return;
        }
		try{
			param.put("eID", application.getUserEntity().getId());
		}catch(Exception e){
			Toast.makeText(this, "不能获取用户，请登录!", Toast.LENGTH_SHORT).show();
            menuDrawer.toggleMenu();
			return;
		}
		if(v == baobiao_outtime){
			param.put("caption", "超期应收款报表");
			//进入进货管理下的进货订单
			Intent intent = new Intent();
			intent.setClass(this, OutTimeActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_order_warning){
			param.put("caption", "进货订单报警报表");
			//进入进货管理下的进货订单
			Intent intent = new Intent();
			intent.setClass(this, OrderWarningActivity.class);
			intent.putExtra("cMode", "B");
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_sale_warning){
			param.put("caption", "销售订单报警报表");
			//进入进货管理下的进货订单
			Intent intent = new Intent();
			intent.setClass(this, OrderWarningActivity.class);
			intent.putExtra("cMode", "S");
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == kc_warn_up || v == kc_warn_down){
			String caption = "";
			String cMode = "";
			if(v == kc_warn_up){
				caption = "库存上限报警";
				cMode = "U";
			}else{
				caption = "库存下限报警";
				cMode = "D";
			}
			param.put("caption", caption);
			//进入进货管理下的进货订单
			Intent intent = new Intent();
			intent.setClass(this, WarnUpDownActivity.class);
			intent.putExtra("cMode", cMode);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == order_in){
			param.put("caption", "进货订单");
			//进入进货管理下的进货订单
			Intent intent = new Intent();
			intent.setClass(this, TabHostActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == order_re){
			//进货退货菜单
			Intent intent = new Intent();
			intent.setClass(this, ReturnOrderActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "进货退货单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == order_jinhuodan){
			//进货单
			Intent intent = new Intent();
			intent.setClass(this, OrderActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "进货单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == sale_waimao){
			//外贸订单
			Intent intent = new Intent();
			intent.setClass(this, WaiMaoActivity.class);
			intent.putExtra("nBilType", StaticValues.SALE_IN_BILTYPE);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "外贸订单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == sale_waimao_order){
			//外贸订单
			Intent intent = new Intent();
			intent.putExtra("nBilType", StaticValues.SALE_BILTYPE);
			intent.setClass(this, WaiMaoActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "外贸销售单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}
		else if(v == sale_order){
			//销售订单
			Intent intent = new Intent();
			intent.putExtra("sale_type", SaleActivity.SALE_ORDER);
			intent.setClass(this, SaleActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "销售订单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == sale_note){
			//销售单
			Intent intent = new Intent();
			intent.putExtra("sale_type", SaleActivity.SALE_NOTE);
			intent.setClass(this, SaleActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "销售单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == sale_return){
			//销售退货
			Intent intent = new Intent();
			intent.putExtra("sale_type", SaleActivity.SALE_RETURN);
			intent.setClass(this, SaleActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "销售退货单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == kuncun_tiaojian || v == kuncun_tongjian){
			//变价调拨单
			Intent intent = new Intent();
			intent.setClass(this, KuCunActivity.class);
			intent.putExtra("type", v == kuncun_tiaojian?KuCunActivity.KUCUN_BIANJIA:KuCunActivity.KUCUN_TONGJIA);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", v == kuncun_tiaojian?"变价调拨单":"同价调拨单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == kucun_pandian){
			//库存盘点
			Intent intent = new Intent();
			intent.setClass(this, PandianListActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "库存盘点");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}
		else if(v == money_skd){
			//收款单
			Intent intent = new Intent();
			intent.putExtra("money_type", MoneyActivity.MONEY_SKD);
			intent.setClass(this, MoneyActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "收款单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == money_fkd){
			//付款单
			Intent intent = new Intent();
			intent.putExtra("money_type", MoneyActivity.MONEY_FKD);
			intent.setClass(this, MoneyActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "付款单");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}
		else if(v == baobiao_wldw){
			Intent intent = new Intent();
			intent.putExtra("type", BaoBiaoActivity.WLDW);
			intent.setClass(this, BaoBiaoActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "单位应收应付");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_xjyh){
			Intent intent = new Intent();
			intent.putExtra("type", BaoBiaoActivity.XJYH);
			intent.setClass(this, BaoBiaoResultActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "现金银行");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_jylc){
			Intent intent = new Intent();
			intent.putExtra("type", BaoBiaoActivity.JYLC);
			intent.setClass(this, BaoBiaoActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "经营历程");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_jhhz){
			Intent intent = new Intent();
			intent.putExtra("type", BaoBiaoActivity.JHHZ);
			intent.setClass(this, BaoBiaoActivity.class);

			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "商品进货汇总表");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_xshz){
			Intent intent = new Intent();
			intent.putExtra("type", BaoBiaoActivity.XSHZ);
			intent.setClass(this, BaoBiaoActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "商品销售汇总表");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_kczk){
			Intent intent = new Intent();
			intent.putExtra("type", BaoBiaoActivity.KCZK);
			intent.setClass(this, BaoBiaoActivity.class);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "库存状况表");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_spcx){
			Intent intent = new Intent();
			intent.putExtra("type", BaoBiaoActivity.SPCX);
			intent.setClass(this, BaoBiaoActivity.class);
//			startActivity(intent);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "商品查询表");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == order_check){
			Intent intent = new Intent();
			intent.setClass(this, OrderCheckActivity.class);
//			startActivity(intent);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "单据审核");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}else if(v == baobiao_month_sale || v == baobiao_profit){
			int type = 0;
			if(v == baobiao_month_sale){
				type = BaoBiaoItemActivity.TYPE_MONTH_IN_SALE;
			}else{
				type = BaoBiaoItemActivity.TYPE_PROFIT;
			}
			Intent intent = new Intent();
			intent.setClass(this, BaoBiaoItemActivity.class);
			intent.putExtra("type", type);
			
			Utils.showProgressDialog("正在验证权限，请稍候……",this);
			param.put("caption", "单据审核");
			CommunicationServer rightCS = new CommunicationServer(this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
			Thread t = new Thread(rightCS);
			t.start();
		}
		else if(v == ck_action){
			selectEntity(EntityListActivity.CK_RE_CODE,ck_edit.getText().toString());
		}else if(v == dw_action){
			selectEntity(EntityListActivity.DW_RE_CODE,dw_edit.getText().toString());
		}else if(v == ck_dw_submit){
			//保存默认的仓库和单位设置
			Editor editor = preferences.edit();
			try {
				Toast toast = Toast.makeText(MenuNewActivity.this, "", Toast.LENGTH_SHORT);
				if(dw_entity!=null){
					String dw_name = dw_edit.getText().toString();
					editor.putString(StaticValues.DEFAULT_DW, dw_name.equals("")?"":Utils.parseObjectToJSONObject(dw_entity).toString());
					toast.setText("设置默认单位成功。");
					toast.show();
				}
				if(ck_entity!=null){
					String ck_name = ck_edit.getText().toString();
					editor.putString(StaticValues.DEFAULT_CK, ck_name.equals("")?"":Utils.parseObjectToJSONObject(ck_entity).toString());
					toast.setText("设置默认仓库成功。");
					toast.show();
				}
				editor.commit();
			} catch (Exception e) {
				Toast.makeText(MenuNewActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			}
		}else if(v == clear_ck||v == clear_dw){
			//清除设置的仓库和单位
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			String str = (v==clear_ck?"仓库":"单位");
			final String key = (v==clear_ck?StaticValues.DEFAULT_CK:StaticValues.DEFAULT_DW);
			builder.setMessage("确定要清除设置的默认"+str+"吗？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clear(key);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
				
			});
			builder.show();
		}else if(v == scanner){
			bu.showList();
		}else if(v == painter){
			showPainterSetting();
		}else if(v == menu_set){
			Intent intent = new Intent();
			intent.setClass(this, MenuSettingActivity.class);
			startActivity(intent);
			this.finish();
		}else if(v.getId() == R.id.company_info){
			Intent intent = new Intent();
			intent.setClass(this, CompanyInfoActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 清楚设置的默认单位，仓库
	 * @param key
	 */
	public void clear(String key){
		Editor editor = preferences.edit();
		editor.putString(key, "");
		if(key == StaticValues.DEFAULT_CK){
			ck_edit.setText("");
			ck_entity = null;
		}else{
			dw_edit.setText("");
			dw_entity = null;
		}
		editor.commit();
	}
	
	/**
	 * 草稿管理的专用监听
	 * @author funever_win8
	 *
	 */
	class DraftClickListener implements OnClickListener{
		
		String draft_type;
		
		public DraftClickListener(String draft_type){
			this.draft_type = draft_type;
		}
		
		@Override
		public void onClick(View v) {
			HashMap<String, String> param = new HashMap<String, String>();
			try{
				param.put("eID", application.getUserEntity().getId());
			}catch(Exception e){
				Toast.makeText(MenuNewActivity.this, "不能获取用户，请重新登录!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent();
			intent.putExtra(DraftActivity.DRAFTTYPE, draft_type);
			intent.setClass(MenuNewActivity.this, DraftActivity.class);
			startActivity(intent);
			
//			showProgressDialog("正在验证权限，请稍候……");
//			param.put("caption", "草稿管理");
//			CommunicationServer rightCS = new CommunicationServer(MenuNewActivity.this,application.getClient(),server+actionUrl, param, handler, new QuanXianExcute(intent));
//			Thread t = new Thread(rightCS);
//			t.start();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == EntityListActivity.DW_RE_CODE){
			dw_entity = (DanWeiEntity) data.getSerializableExtra("entity");
			dw_edit.setText(dw_entity.getFullName());
		}else if(requestCode == EntityListActivity.CK_RE_CODE){
			ck_entity = (CangKuEntity) data.getSerializableExtra("entity");
			ck_edit.setText(ck_entity.getFullName());
		}else if(requestCode == LeftMenu.MENULEFT_REQUEST){
            leftMenu.onActivityResult(requestCode,resultCode,data);
        }
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setMessage("确定退出吗？");
		builder.setTitle("退出");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						bu.close();
						Thread t = new Thread(new ExitRequest());
						t.start();
						//注销蓝牙扫描器监听
						SetValue.getIntance().unregisterReceiver();
						Intent startMain = new Intent(Intent.ACTION_MAIN);
						startMain.addCategory(Intent.CATEGORY_HOME);
						startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(startMain);
						android.os.Process.killProcess(android.os.Process
								.myPid());
						System.exit(10);
					}
				});
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		builder.setCancelable(false);
		builder.show();
	}
	
	/**
	 * 验证权限的线程
	 * @version 1.0
	 * @author Administrator
	 */
	class QuanXianExcute extends ExcuteThread{
		
		Intent intent;
		
		public QuanXianExcute(Intent intent){
			this.intent = intent;
		}
		
		@Override
		public void run() {
			JSONArray ja;
			try {
				ja = new JSONArray(getJsonString());
				JSONObject jo = ja.getJSONObject(0);
				String right = jo.getString("right");
				//验证通过
				if(right.equals("1")){
					startActivity(intent);
				}else{
					Toast.makeText(MenuNewActivity.this, "该用户没有此菜单的访问权限！", Toast.LENGTH_SHORT).show();
				}
			}catch (Exception e) {
				Toast.makeText(MenuNewActivity.this, "验证权限返回数据错误！", Toast.LENGTH_SHORT).show();
			}finally{
				Utils.dismissProgressDialog();
			}
		}
	}
	
	//退出时发送
    public void exitRequest(){
    	//http://192.168.0.21:6003/AndroidWEB/exit.do
    	String url = server+getString(R.string.exit_action);
    	HttpPost get = new HttpPost(url);
		try {
			Utils.excute(application.getClient(), get,false,Utils.ZIP);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    class ExitRequest implements Runnable{
		@Override
		public void run() {
			exitRequest();
		}
    }
    
    public void selectEntity(int requestCode,String searchString){
		Intent intent = new Intent();
		intent.setClass(this, EntityListActivity.class);
		//供应商，客户标示。进货订单，进货单，进货退货单传0，销售订单，销售单，销售退货单传1，其他单据传2
		if(requestCode == EntityListActivity.DW_RE_CODE){
			intent.putExtra("isClient", "2");
		}
		intent.putExtra("type", requestCode);
		intent.putExtra("search", searchString);
		startActivityForResult(intent, requestCode);
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
    	bu.register(this, handler, null);
    	MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	MobclickAgent.onPause(this);
    }
    
    /**
     * 设置打印机地址
     */
    public void showPainterSetting(){
    	Dialog dialog = new Dialog(this, R.style.dialog);
    	dialog.setContentView(R.layout.painter_add);
    	EditText address = (EditText) dialog.findViewById(R.id.paint_address);
    	EditText port = (EditText) dialog.findViewById(R.id.painter_port);
    	SwitchButton switchButton = (SwitchButton) dialog.findViewById(R.id.print_flag);
    	Button save = (Button) dialog.findViewById(R.id.save);
    	address.setText(preferences.getString(StaticValues.PRINTER_IP, ""));
    	port.setText(preferences.getString(StaticValues.PRINTER_PORT, ""));
    	switchButton.setChecked(preferences.getBoolean(StaticValues.PRINTER_ACTIVE, false));
    	save.setOnClickListener(new SavePrinterListener(dialog));
    	dialog.show();
    }
    /**
     * 保存打印机的监听
     * @author funever_win8
     *
     */
    class SavePrinterListener implements OnClickListener{
    	
    	Dialog dialog;
    	
    	public SavePrinterListener(Dialog dialog){
    		this.dialog = dialog;
    	}
		@Override
		public void onClick(View v) {
			Editor editor = preferences.edit();
			EditText address = (EditText) dialog.findViewById(R.id.paint_address);
	    	EditText port = (EditText) dialog.findViewById(R.id.painter_port);
//	    	SwitchButton switchButton = (SwitchButton) dialog.findViewById(R.id.print_flag);
			editor.putString(StaticValues.PRINTER_IP, address.getText().toString());
			editor.putString(StaticValues.PRINTER_PORT, port.getText().toString());
//			editor.putBoolean(StaticValues.PRINTER_ACTIVE, switchButton.isChecked());
			boolean result = editor.commit();
			if(result){
				Toast.makeText(MenuNewActivity.this , "保存打印机地址成功！", Toast.LENGTH_SHORT).show();
    			show_ip_port.setText(address.getText().toString()+":"+port.getText().toString());
//    			if(!switchButton.isChecked()){
//    				show_ip_port.setTextColor(Color.GRAY);
//    			}else{
//    				show_ip_port.setTextColor(Color.BLUE);
//    			}
				dialog.dismiss();
			}else{
				Toast.makeText(MenuNewActivity.this , "保存失败！", Toast.LENGTH_SHORT).show();
			}
		}
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        menu.add("检查更新");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	if(item.getTitle().equals("检查更新")){
    		 UmengUpdateAgent.forceUpdate(this);
            return true;
    	}
        if(item.getItemId() == android.R.id.home){
            menuDrawer.toggleMenu();
            return super.onOptionsItemSelected(item);
        }
    	return true;
    }
}
