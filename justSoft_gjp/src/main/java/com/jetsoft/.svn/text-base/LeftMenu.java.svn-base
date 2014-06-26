package com.jetsoft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jetsoft.activity.login.SettingActivity;
import com.jetsoft.activity.login.UserLoginActivity;
import com.jetsoft.activity.login.ZTActivity;
import com.jetsoft.application.MyApplication;
import com.jetsoft.entity.UserEntity;
import com.jetsoft.entity.ZTEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by funever on 2014/6/5.
 */
public class LeftMenu implements MenuAdapter.MenuListener,AdapterView.OnItemClickListener{
    private MenuNewActivity menuNewActivity;

    private MenuAdapter mAdapter;
    int mActivePosition=0;
    ListView mList;

    private static final int zt_id = 101;
    private static final int user_id = 102;
    private static final int scan_id = 103;
    private static final int print_id = 104;
    private static final int menu_id = 105;
    private static final int other_id = 106;
    private static final int server_id = 110;

    public static final int MENULEFT_REQUEST = 107;
    public static final int MENULEFT_RESULT_ZT = 108;
    public static final int MENULEFT_RESULT_USER = 109;
    public static final int MENULEFT_RESULT_SERVER = 111;
    public static final String FROM_MENU = "from_menu";

    Item zt_item,user_item;

    MyApplication application;

    public LeftMenu(MenuNewActivity menuNewActivity){
        this.menuNewActivity = menuNewActivity;
        application = (MyApplication) menuNewActivity.getApplication();
    }

    public void initView(){
        List<Object> items = new ArrayList<Object>();
        items.add(new Category("帐套"));
        ZTEntity ztEntity = application.getZtEntity();
        zt_item = new Item(ztEntity!=null?ztEntity.getFullName():"未选择帐套", 0);
        zt_item.setId(zt_id);
        items.add(zt_item);
        items.add(new Category("用户"));
        UserEntity userEntity = application.getUserEntity();
        user_item = new Item(userEntity!=null?userEntity.getEfullName():"未登录", 0);
        user_item.setId(user_id);
        items.add(user_item);
        items.add(new Category("其他设置"));
        Item server_Item = new Item("服务器设置",R.drawable.ic_menu_goto);
        server_Item.setId(server_id);
        items.add(server_Item);
        Item scan_Item = new Item("扫描器设置", R.drawable.ic_menu_paste_holo_dark);
        scan_Item.setId(scan_id);
        items.add(scan_Item);
        Item print_item = new Item("打印机设置", R.drawable.ic_menu_copy_holo_dark);
        print_item.setId(print_id);
        items.add(print_item);
        Item menu_item = new Item("菜单设置", R.drawable.ic_menu_attachment);
        menu_item.setId(menu_id);
        items.add(menu_item);
        Item other_item = new Item("自定义信息", R.drawable.ic_menu_edit);
        other_item.setId(other_id);
        items.add(other_item);

        mList = new ListView(menuNewActivity);

        mAdapter = new MenuAdapter(menuNewActivity, items);
        mAdapter.setListener(this);
        mAdapter.setActivePosition(mActivePosition);

        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
        menuNewActivity.menuDrawer.setMenuView(mList);
    }

    @Override
    public void onActiveViewChanged(View v) {
        menuNewActivity.menuDrawer.setActiveView(v, mActivePosition);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mActivePosition = i;
        menuNewActivity.menuDrawer.setActiveView(view, i);
        mAdapter.setActivePosition(i);
        onMenuItemClicked((Item) mAdapter.getItem(i));
    }

    private void onMenuItemClicked(Item item){
        switch (item.getId()){
            case zt_id:
                Intent intent3 = new Intent();
                intent3.putExtra(FROM_MENU,true);
                intent3.setClass(menuNewActivity, ZTActivity.class);
                menuNewActivity.startActivityForResult(intent3, MENULEFT_REQUEST);
                break;
            case  user_id:
                if(application.getZtEntity()==null){
                    zt_item.setColor(Color.parseColor("#ff0000"));
                    Toast.makeText(menuNewActivity,"请先选择帐套!",2000).show();
                    return;
                }
                Intent intent4 = new Intent();
                intent4.putExtra(FROM_MENU,true);
                intent4.setClass(menuNewActivity, UserLoginActivity.class);
                menuNewActivity.startActivityForResult(intent4,MENULEFT_REQUEST);
                break;
            case server_id:
                Intent intent5 = new Intent();
                intent5.putExtra(FROM_MENU,true);
                intent5.setClass(menuNewActivity, SettingActivity.class);
                menuNewActivity.startActivityForResult(intent5,MENULEFT_REQUEST);
                break;
            case scan_id:
                menuNewActivity.bu.showList();
                break;
            case print_id:
                menuNewActivity.showPainterSetting();
                break;
            case menu_id:
                Intent intent = new Intent();
                intent.setClass(menuNewActivity, MenuSettingActivity.class);
                menuNewActivity.startActivity(intent);
                menuNewActivity.finish();
                break;
            case other_id:
                Intent intent2 = new Intent();
                intent2.setClass(menuNewActivity, CompanyInfoActivity.class);
                menuNewActivity.startActivity(intent2);
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == MENULEFT_RESULT_ZT){
            zt_item.setmTitle(application.getZtEntity().getFullName());
            zt_item.setColor(Color.parseColor("#FFFFFF"));
            application.setUserEntity(null);
            user_item.setmTitle("未登录");
        }else if(resultCode == MENULEFT_RESULT_USER){
            user_item.setmTitle(application.getUserEntity().getEfullName());
            menuNewActivity.menuDrawer.closeMenu();
        }else if(resultCode == MENULEFT_RESULT_SERVER){
            boolean same = data.getBooleanExtra("same",false);
            if(same){
                Toast.makeText(menuNewActivity,"地址没有改变。",2000 ).show();
                return;
            }else{
                Toast.makeText(menuNewActivity,"设置成功，请重新登录!",2000).show();
                application.setUserEntity(null);
                application.setZtEntity(null);
                user_item.setmTitle("未登录");
                zt_item.setmTitle("请选择账套");
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
