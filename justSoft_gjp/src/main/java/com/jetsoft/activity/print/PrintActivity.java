package com.jetsoft.activity.print;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.actionbarsherlock.app.SherlockActivity;
import com.funever.bluetoochscanner.BluetoothChatService;
import com.jetsoft.CompanyInfoActivity;
import com.jetsoft.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PrintActivity extends SherlockActivity{

	private BluetoothChatService mBTService = null;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	private ListView deviceList;// 设备列表
	private BtAdapter btAdapter = null;// 新搜索列表
	private Set<BluetoothDevice> devices;
	public Handler handler = new Handler();
	private LinearLayout layoutscan;
	
	private BlueHandler mhandler;
	private List<BluetoothDevice> btList = new LinkedList<BluetoothDevice>();
	ProgressDialog dialog;

	String data;
	
	SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StyledIndicators);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qunsuo_main);

        getSupportActionBar().setTitle("选择打印机");

		preferences = getSharedPreferences(CompanyInfoActivity.COM_CONFIG,Activity.MODE_PRIVATE);
		
		deviceList = (ListView) findViewById(R.id.lv_device);
		layoutscan = (LinearLayout) findViewById(R.id.layoutscan);
		
		data = getIntent().getStringExtra("data");
		data = data + getString(R.string.print_company_info, preferences.getString("cp_name", ""),preferences.getString("cp_tel", ""),preferences.getString("cp_address", ""));
		System.out.println("data:"+data);
		mhandler = new BlueHandler();
		mBTService = new BluetoothChatService(this, mhandler);// 创建对象的时候必须有一个是Handler类型
		btAdapter = new BtAdapter();

		layoutscan.setVisibility(View.VISIBLE);
		btList.clear();
		devices = mBTService.mAdapter.getBondedDevices();
		if (devices.size() > 0) {
			for (BluetoothDevice device : devices) {
				btList.add(device);
			}
		}
		deviceList.setAdapter(btAdapter);
		/**
		 * 开始扫描
		 */
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBTService.mAdapter.startDiscovery();
			}
		});
		/**
		 * 20s后结束扫描
		 */
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(mBTService!=null){
					mBTService.mAdapter.cancelDiscovery();
				}
			}
		}, 20*1000);

		deviceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mBTService == null){
					return;
				}
				if(mBTService.mAdapter.isDiscovering()){
					mBTService.mAdapter.cancelDiscovery();
				}
				showProgressDialog("连接蓝牙打印机……");
//				mBTService.DisConnected();
				if(mBTService.getState() == BluetoothChatService.STATE_CONNECTED){
					print();
				}else{
					mBTService.connect(btList.get(position));
				}
			}
		});
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		registerReceiver(mReceiver, filter);
	}
	
	public byte[] convert(String content){
		try {
			String formatString = content.replaceAll("\\[ESC\\]", new String(new byte[]{0x1B}))
					.replaceAll("\\[LF\\]", new String(new byte[]{0x0A},"GBK"))
					.replaceAll("\\[NUL\\]", new String(new byte[]{0x00},"GBK"))
					.replaceAll("\\[GS\\]", new String(new byte[]{0x1D},"GBK"))
					.replaceAll("\\[HT\\]", new String(new byte[]{0x09},"GBK"));
			return formatString.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	class BtAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return btList.size();
		}

		@Override
		public Object getItem(int position) {
			return btList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if(convertView==null){
				convertView = LayoutInflater.from(PrintActivity.this).inflate(R.layout.qunsuo_device_name, null);
				vh = new ViewHolder();
				vh.name = (TextView) convertView.findViewById(R.id.bt_name);
				vh.mac = (TextView) convertView.findViewById(R.id.bt_address);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			vh.name.setText(btList.get(position).getName());
			vh.mac.setText(btList.get(position).getAddress());
			return convertView;
		}
		
		class ViewHolder{
			TextView name;
			TextView mac;
		}
	}
	
	/**
	 * 
	 * @author funever
	 *
	 */
	class BlueHandler extends Handler{
		
		public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluetoothChatService.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                	print();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    break;
                }
                break;
            case BluetoothChatService.MESSAGE_WRITE:
                break;
            case BluetoothChatService.MESSAGE_READ:
				break;
            case BluetoothChatService.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                String mConnectedDeviceName = msg.getData().getString(BluetoothChatService.DEVICE_NAME);
                Toast.makeText(PrintActivity.this, "成功连接\""
                               + mConnectedDeviceName+"\"", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothChatService.MESSAGE_TOAST:
            	String message = msg.getData().getString(BluetoothChatService.TOAST);
                Toast.makeText(PrintActivity.this,"不能连接到蓝牙打印机！" ,
                               Toast.LENGTH_SHORT).show();
                if(message.equals("Unable to connect device")){
                	dismissProgressDialog();
                }
                break;
            }
        }
	}
	
	/**
	 * 发送打印数据
	 */
	public void print(){
		try{
			if(data == null){
				Toast.makeText(this, "打印数据错误!", 3000).show();
			}else{
				byte[] bt = new byte[3];
				bt[0] = 27;
				bt[1] = 56;
				bt[2] = 2;// 1,2//设置字体大小
				showProgressDialog("正在发送打印数据……");
				mBTService.write(bt);
				mBTService.write(convert(data));
				Toast.makeText(this, "打印完成!", 2000).show();
				onBackPressed();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dismissProgressDialog();
		}
	}
	
	@Override
	protected void onDestroy() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (mBTService != null) {
			mBTService.stop();
			mBTService = null;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.finish();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private void showProgressDialog(String message){
		if(dialog!=null && dialog.isShowing()){
			setProgressDialogMsg(message);
			return;
		}
		dialog = new ProgressDialog(this);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.show();
	}
	
	private void dismissProgressDialog(){
		if(dialog!=null && dialog.isShowing()){
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private  void setProgressDialogMsg(String msg){
		if(dialog!=null && dialog.isShowing()){
			dialog.setMessage(msg);
		}
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 找到设备
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				btList.add(device);
				btAdapter.notifyDataSetChanged();
			}
		}
	};
}
