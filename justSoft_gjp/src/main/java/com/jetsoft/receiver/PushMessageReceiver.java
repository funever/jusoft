package com.jetsoft.receiver;

import com.jetsoft.R;
import cn.jpush.android.api.JPushInterface;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PushMessageReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
        Log.d("PushMessageReceiver", "onReceive - " + intent.getAction());
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            return;
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            showMessage(context,message);
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
            //对通知打开不做处理
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            return;
        } else {
            Log.d("PushMessageReceiver", "Unhandled intent - " + intent.getAction());
        }
		return;
	}
	
	/**
	 * 显示通知
	 * @param context
	 * @param message
	 */
	public void showMessage(Context context,String message){
//		final Dialog dialog = new Dialog(context, R.style.dialog);
		LinearLayout mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.message_layout, null);
		TextView messageContent = (TextView) mainLayout.findViewById(R.id.message_content);
		messageContent.setText(message);
//		Button close = (Button) mainLayout.findViewById(R.id.close);
//		close.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				dialog.dismiss();
//			}
//		});
//		dialog.setContentView(mainLayout);
//		dialog.show();
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
		toast.setView(mainLayout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
