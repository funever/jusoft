/**
 * 创建日期 2012-12-20
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
package com.jetsoft.io;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import com.jetsoft.R;
import com.jetsoft.activity.login.ZTActivity.ExitThread;
import com.jetsoft.application.MyApplication;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CommunicationServer implements Runnable{
	
	private String url;
	
	private Handler handler;
	
	private ExcuteThread actionThread;
	
	private List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
	
	public HttpClient client;
	
	private Context ctx;
	/**
	 * 没有登录返回的
	 */
	private static final String NOLOGIN = "[{\"error\":\"noLogin\"}]";
	
	public boolean zip = Utils.ZIP;
	
	public CommunicationServer(Context ctx,HttpClient client,String url,HashMap<String, String> paramMap,Handler handler,ExcuteThread actionThread){
		this.handler = handler;
		this.actionThread = actionThread;
		this.url = url;
		this.client = client;
		this.ctx = ctx;
		for(Entry<String, String> entry : paramMap.entrySet()){
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		//添加imei
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		params.add(new BasicNameValuePair("imei",tm.getDeviceId()));
	}
	
	@Override
	public void run() {
		//读取http获取json数据
		try {
			//对参数进行编码设置
			System.out.println(url);
			System.out.println(params);
			HttpPost get = new HttpPost(url);
			HttpEntity requestEntity = new UrlEncodedFormEntity(params,"gbk");
			get.setEntity(requestEntity);
			StringBuffer sb = Utils.excute(client, get,false,zip);
			if(sb.toString().equalsIgnoreCase(NOLOGIN)){
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ctx, "连接已经超时，重新连接!", Toast.LENGTH_SHORT).show();
					}
				});
				reLogin();
				return;
			}
			actionThread.setJsonString(sb.toString());
			handler.post(actionThread);
		}catch(NullPointerException e1){
			e1.printStackTrace();
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ctx, "服务器返回数据错误！", Toast.LENGTH_SHORT).show();
					Utils.dismissProgressDialog();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ctx, "服务器长时间未响应！", Toast.LENGTH_SHORT).show();
					Utils.dismissProgressDialog();
				}
			});
		}finally{
			/**
			 * 如果是退出请求，则回调一定要执行
			 */
			if(actionThread instanceof ExitThread){
				handler.post(actionThread);
			}
		}
	}
	/**
	 * 重新登录
	 */
	public void reLogin(){
		try {
			MyApplication application = (MyApplication) ((Activity)ctx).getApplication();
			
			SharedPreferences preferences = ctx.getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
			String server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, ctx.getString(R.string.server))+":"
				+ preferences.getString(StaticValues.PORT_FLAG, ctx.getString(R.string.port));
			String actionUrl = ctx.getString(R.string.zt_action);
			
			String url = server+actionUrl;
            List<BasicNameValuePair> reloginParam = new LinkedList<BasicNameValuePair>();
            reloginParam.add(new BasicNameValuePair("type","2"));
            reloginParam.add(new BasicNameValuePair("dataBase",application.getZtEntity().getDbName()));
			System.out.println(url);
            System.out.println(reloginParam);
            HttpEntity requestEntity = new UrlEncodedFormEntity(reloginParam,"gbk");
            HttpPost get = new HttpPost(url);
            get.setEntity(requestEntity);
			StringBuffer sb = Utils.excute(client, get,false,zip);
			JSONArray ja = new JSONArray(sb.toString());
			//当有数据时再次请求
			if(ja.length()>0){
				request();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 重新请求session失效前的url
	 */
	public void request(){
		//读取http获取json数据
		try {
			//对参数进行编码设置
			System.out.println(url);
			System.out.println(params);
			HttpPost get = new HttpPost(url);
			HttpEntity requestEntity = new UrlEncodedFormEntity(params,"gbk");
			get.setEntity(requestEntity);
			StringBuffer sb = Utils.excute(client, get,false,zip);
			System.out.println(sb.toString());
			actionThread.setJsonString(sb.toString());
			handler.post(actionThread);
		} catch (Exception e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ctx, "服务器长时间未响应！", Toast.LENGTH_SHORT).show();
					Utils.dismissProgressDialog();
				}
			});
			e.printStackTrace();
		}finally{
			/**
			 * 如果是退出请求，则回调一定要执行
			 */
			if(actionThread instanceof ExitThread){
				handler.post(actionThread);
			}
		}
	}
}
