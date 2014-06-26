package com.funever.bluetoochscanner;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoochUtils {

    private Dialog bt_list_dialog;

    private Activity activity;

    private Handler handler;

    private BluetoothAdapter mAdapter;

    BTListAdapter baAdapter;

    List<BTDevice> deviceList = new LinkedList<BTDevice>();

    LinearLayout progress_layout;

    private static BluetoochUtils utils = null;

    public ScannerResult sr;

    private BluetoothChatService mChatService = null;

    private String mConnectedDeviceName = null;

    private BluetoochUtils(Activity activity,Handler handler,ScannerResult sr){
        this.activity = activity;
        this.handler = handler;
        this.sr = sr;
        mChatService = new BluetoothChatService(activity, mHandler);
        if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
            mChatService.start();
        }
    }

    public static BluetoochUtils getInstance(Activity activity,Handler handler,ScannerResult sr){
        if(utils == null){
            utils = new BluetoochUtils(activity, handler,sr);
        }
        return utils;
    }

    /**
     * 重新注册
     * @param activity
     * @param handler
     * @param sr
     */
    public void register(Activity activity,Handler handler,ScannerResult sr){
        this.activity = activity;
        this.handler = handler;
        this.sr = sr;
        mChatService = new BluetoothChatService(activity, mHandler);
        if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
            mChatService.start();
        }
    }

    public void showList(){
        deviceList.clear();
        bt_list_dialog = new Dialog(activity, R.style.dialog);
        bt_list_dialog.setContentView(R.layout.bt_list);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        for(BluetoothDevice bd : mAdapter.getBondedDevices()){
            BTDevice btd = new BTDevice();
            btd.bond = true;
            btd.bDevice = bd;
            btd.macAddress = bd.getAddress();
            btd.name = bd.getName();
            deviceList.add(btd);
        }
        baAdapter = new BTListAdapter();
        ListView	bt_list = (ListView)bt_list_dialog.findViewById(R.id.bt_list);
        bt_list.setAdapter(baAdapter);
        bt_list.setOnItemClickListener(new ListClickListener());

        progress_layout = (LinearLayout) bt_list_dialog.findViewById(R.id.progress_layout);
        bt_list_dialog.show();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        activity.registerReceiver(mReceiver, filter);
        scan();
        bt_list_dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                bt_list_dialog.dismiss();
                activity.unregisterReceiver(mReceiver);
            }
        });
    }

    public void unregisterReceiver(){
        activity.unregisterReceiver(mReceiver);
    }

    class ListClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            DialogClickListener dcl = new DialogClickListener(arg2);
            String items[] = new String[] { "连接"};
            Builder dialog = new AlertDialog.Builder(activity)
                    .setTitle("操作").setItems(items, dcl)
                    .setNegativeButton("取消", dcl);
            dialog.show();
        }

    }

    class DialogClickListener implements DialogInterface.OnClickListener {

        int deviceIndex;

        public DialogClickListener(int deviceIndex) {
            this.deviceIndex = deviceIndex;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
                // 连接
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        mChatService.connect(deviceList.get(deviceIndex).bDevice);
                    }
                };
                Thread t = new Thread(r);
                t.start();
                return;
            } else if (which == 1) {
                // 配对
                BluetoothDevice device = deviceList.get(deviceIndex)
                        .getbDevice();
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    try {
                        Method createBondMethod = BluetoothDevice.class
                                .getMethod("createBond");
                        Log.d("BlueToothTestActivity", "开始配对");
                        createBondMethod.invoke(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity,
                                "配对失败，请到系统设置进行尝试配对!", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (which == 2) {
                // 取消配对
                BluetoothDevice device = deviceList.get(deviceIndex)
                        .getbDevice();
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    try {
                        Method createBondMethod = BluetoothDevice.class
                                .getMethod("removeBond");
                        Log.d("BlueToothTestActivity", "取消配对");
                        createBondMethod.invoke(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity,
                                "配对失败，请到系统设置进行尝试配对!", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.dismiss();
            }
        }
    }

    public void scan() {
        baAdapter.notifyDataSetChanged();
        startScan();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cancelScan();
            }
        }, 20 * 1000);
    }

    public void startScan() {
        if (!mAdapter.isEnabled()) {
            Intent enabler = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivity(enabler);
        }
        mAdapter.startDiscovery();
    }


    public void cancelScan() {
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        progress_layout.setVisibility(View.GONE);
    }

    public void close(){
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    /**
     * 蓝牙列表适配器
     *
     * @version 1.0
     * @author Administrator
     */
    class BTListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return deviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(activity).inflate(
                    R.layout.bt_list_row, null);
            BTDevice device = deviceList.get(position);
            ((TextView) view.findViewById(R.id.bt_name)).setText(device
                    .getName());
            ((TextView) view.findViewById(R.id.bt_address)).setText(device
                    .getMacAddress());
            if (device.isBond()) {
                ((TextView) view.findViewById(R.id.bond)).setText("已配对");
            } else {
                ((TextView) view.findViewById(R.id.bond)).setText("未配对");
            }
            return view;
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
                BTDevice bd = new BTDevice();
                bd.setName(device.getName());
                bd.setMacAddress(device.getAddress());
                bd.setbDevice(device);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    bd.setBond(true);
                } else {
                    bd.setBond(false);
                }
                deviceList.add(bd);
                baAdapter.notifyDataSetChanged();
            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                for(BTDevice d : deviceList){
                    if(d.getMacAddress().equals(device.getAddress())){
                        d.setBond(device.getBondState()==BluetoothDevice.BOND_BONDED?true:false);
                        baAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    };

    /**
     * 蓝牙bean
     *
     *
     * @version 1.0
     * @author Administrator
     */
    class BTDevice {

        String name;

        String macAddress;

        boolean bond;

        BluetoothDevice bDevice;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public boolean isBond() {
            return bond;
        }

        public void setBond(boolean bond) {
            this.bond = bond;
        }

        public BluetoothDevice getbDevice() {
            return bDevice;
        }

        public void setbDevice(BluetoothDevice bDevice) {
            this.bDevice = bDevice;
        }
    }

//	public static BluetoothSocket socket;
    /**
     * 蓝牙服务的通用UUID
     */
    public static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    public StringBuffer sb = new StringBuffer();

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothChatService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
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
                    String value =  (String) msg.obj;
                    handler.post(new SetValue(value));
                    break;
                case BluetoothChatService.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothChatService.DEVICE_NAME);
                    Toast.makeText(activity, "成功连接\""
                            + mConnectedDeviceName+"\"", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatService.MESSAGE_TOAST:
                    Toast.makeText(activity, msg.getData().getString(BluetoothChatService.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    class SetValue implements Runnable{

        String value;

        public SetValue(String value){
            this.value = value;
        }
        @Override
        public void run() {
            if(sr!=null){
                sr.disposeScanValue(value);
            }
        }
    }
}
