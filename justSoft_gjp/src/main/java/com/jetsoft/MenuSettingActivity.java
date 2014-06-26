package com.jetsoft;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import me.imid.view.SwitchButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class MenuSettingActivity extends SherlockActivity{

    ExpandableListView list;

    MenuAdapter menuAdapter;

    SharedPreferences preferences;

    List<Group> groups ;

    Editor editor;

    HashMap<String, Boolean> checked = new HashMap<String, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_setting);

        getSupportActionBar().setTitle("菜单设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(MenuController.MENU_CONFIG,Activity.MODE_PRIVATE);
        editor = preferences.edit();
        String config = preferences.getString(MenuController.MENU_CONFIG, "");
        if(config.equals("")){
            init();
        }

        groups = getGroups();

        list = (ExpandableListView) findViewById(R.id.menus);
        menuAdapter = new MenuAdapter();
        list.setAdapter(menuAdapter);
        list.requestFocusFromTouch();
    }

    /**
     * 初始化配置
     */
    public void init(){
        String[] menus = getResources().getStringArray(R.array.menu_titles);
        String[][] children = new String[6][];
        children[0] = getResources().getStringArray(R.array.warn_menu);
        children[1] = getResources().getStringArray(R.array.order_menu);
        children[2] = getResources().getStringArray(R.array.sale_menu);
        children[3] = getResources().getStringArray(R.array.kucun_menu);
        children[4] = getResources().getStringArray(R.array.money_menu);
        children[5] = getResources().getStringArray(R.array.baobiao_menu);
        try{
            JSONArray ja = new JSONArray();
            for(int i=0;i<menus.length;i++){
                JSONObject jo = new JSONObject();
                jo.put("title", menus[i]);
                jo.put("show", true);
                checked.put(menus[i], true);
                JSONArray itemList = new JSONArray();
                String[] items = children[i];
                for(int j=0;j<items.length;j++){
                    JSONObject menu = new JSONObject();
                    menu.put("title", items[j]);
                    menu.put("show", true);
                    checked.put(items[j], true);
                    itemList.put(menu);
                }
                jo.put("items", itemList);
                ja.put(jo);
            }
            editor.putString(MenuController.MENU_CONFIG, ja.toString());
            editor.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 保存
     */
    public void save(){
        try{
            JSONArray ja = new JSONArray();
            for(int i=0;i<groups.size();i++){
                Group group = groups.get(i);
                JSONObject jo = new JSONObject();
                jo.put("title", group.title);
                jo.put("show", group.show);
                List<Menu> mList = group.getmList();
                JSONArray itemList = new JSONArray();
                for(int j=0;j<mList.size();j++){
                    JSONObject menuObject = new JSONObject();
                    Menu menu = mList.get(j);
                    menuObject.put("title", menu.title);
                    menuObject.put("show", menu.show);
                    itemList.put(menuObject);
                }
                jo.put("items", itemList);
                ja.put(jo);
            }
            editor.putString(MenuController.MENU_CONFIG, ja.toString());
            editor.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取配置
     * @return
     */
    public List<Group> getGroups(){
        List<Group> groups = new LinkedList<Group>();
        String config = preferences.getString("menu_config", "");
        if(config.equals("")){
            return groups;
        }
        try {
            JSONArray ja = new JSONArray(config);
            for(int i=0;i<ja.length();i++){
                JSONObject groupObject = ja.getJSONObject(i);
                Group group = new Group();
                group.setTitle(groupObject.getString("title"));
                group.setShow(groupObject.getBoolean("show"));
                if(groupObject.getBoolean("show")){
                    checked.put(groupObject.getString("title"),true);
                }
                LinkedList<Menu> mList = new LinkedList<Menu>();
                JSONArray items = groupObject.getJSONArray("items");
                for(int j=0;j<items.length();j++){
                    JSONObject menuObject = items.getJSONObject(j);
                    Menu m = new Menu();
                    m.setTitle(menuObject.getString("title"));
                    m.setShow(menuObject.getBoolean("show"));
                    if(menuObject.getBoolean("show")){
                        checked.put(menuObject.getString("title"),true);
                    }
                    mList.add(m);
                }
                group.setmList(mList);
                groups.add(group);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groups;
    }

    class MenuAdapter extends BaseExpandableListAdapter{

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groups.get(groupPosition).getmList().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final Menu menu = groups.get(groupPosition).getmList().get(childPosition);
            ViewHolder vh = null;
            if(convertView==null){
                convertView = LayoutInflater.from(MenuSettingActivity.this).inflate(R.layout.menu_setting_child_item, null);
                vh = new ViewHolder();
                vh.title = (TextView) convertView.findViewById(R.id.group_title);
                vh.check = (SwitchButton) convertView.findViewById(R.id.check);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            vh.title.setText(menu.getTitle());
            final String title = vh.title.getText().toString();
            vh.check.setOnCheckedChangeListener(null);
            if(checked.containsKey(menu.getTitle())){
                vh.check.setChecked(true);
            }else{
                vh.check.setChecked(false);
            }
            vh.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        checked.put(title,true);
                    }else{
                        checked.remove(title);
                    }
                    menu.setShow(isChecked);
                }
            });
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groups.get(groupPosition).getmList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            final Group group = groups.get(groupPosition);
            ViewHolder vh = null;
            if(convertView==null){
                convertView = LayoutInflater.from(MenuSettingActivity.this).inflate(R.layout.menu_setting_group_item, null);
                vh = new ViewHolder();
                vh.title = (TextView) convertView.findViewById(R.id.group_title);
                vh.check = (SwitchButton) convertView.findViewById(R.id.check);
                vh.check.setTag(groupPosition+"");
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            vh.title.setText(group.getTitle());
            vh.check.setOnCheckedChangeListener(null);
            if(checked.containsKey(group.getTitle())){
                vh.check.setChecked(true);
            }else{
                vh.check.setChecked(false);
            }
            final String title = vh.title.getText().toString();
//			vh.check.setOnCheckedChangeListener(new GroupCheckListener(groupPosition));
            vh.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        checked.put(title, true);
                    }else{
                        checked.remove(title.toString());
                    }
                    group.setShow(isChecked);
                }
            });
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded) {
                        list.collapseGroup(groupPosition);
                    } else {
                        list.expandGroup(groupPosition);
                    }
                }
            });
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class ViewHolder{

        TextView title;
        SwitchButton check;

    }

    @Override
    public void onBackPressed() {
        save();
        Intent intent = new Intent();
        intent.setClass(this, MenuNewActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            save();
            Intent intent = new Intent();
            intent.setClass(this, MenuNewActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
