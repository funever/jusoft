package com.jetsoft.io;

import java.util.TimerTask;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import com.jetsoft.R;
import com.jetsoft.utils.StaticValues;
import com.jetsoft.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

/**
 * 心跳检测线程
 * @author funever_win8
 *
 */
public class ActiveThread extends TimerTask{
	
	public HttpClient client;
	
	public Context ctx;
	
	private String deviceId="";
	
	public ActiveThread(HttpClient client,Context ctx,boolean... same){
		if(same!=null && same.length>0){
			if(same[0]){
				this.client = client;
			}else{
				this.client = new DefaultHttpClient();
			}
		}else{
			this.client = new DefaultHttpClient();
		}
		this.ctx = ctx;
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = tm.getDeviceId();
	}
	
	@Override
	public void run() {
		/**
		 * 获取服务器地址和验证权限的url
		 */
		SharedPreferences preferences = ctx.getSharedPreferences(StaticValues.CONFIG, Activity.MODE_PRIVATE);
		String server = "http://"+preferences.getString(StaticValues.SERVER_FLAG, ctx.getString(R.string.server))+":"
		+ preferences.getString(StaticValues.PORT_FLAG, ctx.getString(R.string.port));
		String actionUrl = ctx.getString(R.string.alive_action);
		
		HttpPost get = new HttpPost(server+actionUrl+"?imei="+deviceId);
		try {
			Utils.excute(client, get,true,Utils.ZIP);
			System.out.println(Utils.getFormatDate(0, "yyyy-MM-dd HH:mm:ss")+":send active package");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
