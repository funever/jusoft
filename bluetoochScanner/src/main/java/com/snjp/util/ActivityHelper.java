/**
 * 创建日期 2013-4-23
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
package com.snjp.util;

import java.lang.reflect.Field;
import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;

public class ActivityHelper {

    private Activity activity;

    public ActivityHelper(Activity activity){
        this.activity = activity;
    }

    public void viewInject(){
        //遍历所有属性
        Field[] fields = activity.getClass().getDeclaredFields();
        for(Field field :fields){
            field.setAccessible(true);
            try {
                //如果属性是View类型，则可以findViewById
                Object object = field.get(activity);
                //获取这个字段的注解
                ViewInject vi = field.getAnnotation(ViewInject.class);
                if(vi!=null){
                    String id = vi.id();
                    //如果id为空，则id为属性名
                    if(id==null || "".equals(id)){
                        id = field.getName();
                    }
                    if(object instanceof Preference){
                        PreferenceActivity pActivity = (PreferenceActivity) activity;
                        object = pActivity.findPreference(id);
                    }else{
                        object = activity.findViewById(activity.getResources().getIdentifier(id, "id", activity.getPackageName()));
                    }
                    //获取监听方法
                    String onClick = vi.onClick();
                    if(onClick==null || "".equals(onClick)){
                        continue;
                    }else{
                        setViewClickListener((View) object,onClick);
                    }
                    field.set(activity, object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setViewClickListener(View view,String clickMethod)
    {
        view.setOnClickListener(new EventListener(activity).click(clickMethod));
    }
}
