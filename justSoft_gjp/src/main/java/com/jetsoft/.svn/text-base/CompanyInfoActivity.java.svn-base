package com.jetsoft;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.view.*;

public class CompanyInfoActivity extends FinalActivity{

    @ViewInject(id=R.id.cp_name)
    private EditText cp_name;
    @ViewInject(id=R.id.cp_tel)
    private EditText cp_tel;
    @ViewInject(id=R.id.cp_address)
    private EditText cp_address;

    SharedPreferences preferences;
    Editor editor;

    MenuItem item;

    public static final String COM_CONFIG = "company_info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_info);

        getSupportActionBar().setTitle("自定义字段");
        getSupportActionBar().setSubtitle("在打印单据的末尾中打印该设置的信息。");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(CompanyInfoActivity.COM_CONFIG,Activity.MODE_PRIVATE);
        editor = preferences.edit();

        cp_name.setText(preferences.getString("cp_name", ""));
        cp_tel.setText(preferences.getString("cp_tel", ""));
        cp_address.setText(preferences.getString("cp_address", ""));
    }

    private static final int MENU_SAVE = 10002;
    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        item = menu.add(0,MENU_SAVE,0,"编辑");
        item.setIcon(android.R.drawable.ic_menu_save);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }else if(item.getItemId() == MENU_SAVE){
            if(item.getTitle().equals("编辑")){
                item.setTitle("保存");
                cp_name.setEnabled(true);
                cp_tel.setEnabled(true);
                cp_address.setEnabled(true);

                cp_name.requestFocus();

                return true;
            }else if(item.getTitle().equals("保存")){
                item.setTitle("编辑");
                editor.putString("cp_name", cp_name.getText().toString());
                editor.putString("cp_tel", cp_tel.getText().toString());
                editor.putString("cp_address", cp_address.getText().toString());
                editor.commit();
                cp_name.setEnabled(false);
                cp_tel.setEnabled(false);
                cp_address.setEnabled(false);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
