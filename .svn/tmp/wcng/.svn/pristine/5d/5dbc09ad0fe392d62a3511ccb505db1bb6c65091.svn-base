package com.jetsoft.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;
import com.jetsoft.application.MyApplication;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.DatePicker;
import android.widget.TextView;

public class Utils {
	
	public static final boolean ZIP = true;
	
	private static ProgressDialog dialog;
	
	private static Handler handler = new Handler();
	
	/**
	 * 将Object类的所有属性JSon化，如果属性中有不是基本类型不能转化的抛出异常
	 * @param o 要转化的对象
	 * @return 转化后的JSONObject
	 * @throws Exception
	 */
	public static JSONObject parseObjectToJSONObject(Object o) throws Exception{
		JSONObject jo = new JSONObject();
		//只能获取自己的属性
		Field[] fields = o.getClass().getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			jo.put(f.getName(), f.get(o));
		}
		//父类的属性
		Field[] superFields = o.getClass().getSuperclass().getDeclaredFields();
		for(Field f : superFields){
			f.setAccessible(true);
			//以子类属性为主，父类有这个属性，子类覆盖
			if(!jo.has(f.getName())){
				jo.put(f.getName(), f.get(o));
			}
		}
		return jo;
	}
	/**
	 * 将JSOn对象根据传递的class类型转化生成一个class对象
	 * @param clazz 类型
	 * @param jo json对象
	 * @return class的生成对象
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws Exception
	 */
	public static <T> T parseJSONObjectToObject(Class<? extends T> clazz,JSONObject jo) throws Exception{
		T  entity = (T) clazz.newInstance();
		//只能获取自己的属性
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			if(!jo.has(f.getName())){
				continue;
			}
			try{
				Object value = getValue(f.getType(), jo, f.getName());
				if(value!=null){
					f.set(entity, value);
				}
			}catch(Exception e){
				continue;
			}
		}
		//父类的属性
		Field[] superFields = clazz.getSuperclass().getDeclaredFields();
		for(Field f : superFields){
			f.setAccessible(true);
			if(!jo.has(f.getName())){
				continue;
			}
			try{
				Object value = getValue(f.getType(), jo, f.getName());
				if(value!=null){
					f.set(entity, value);
				}
			}catch(Exception e){
				continue;
			}
		}
		return entity;
	}
	
	/**
	 * 将JSOn对象根据传递的class类型转化生成一个class对象
	 * @param clazz 类型
	 * @param jo json对象
	 * @return class的生成对象
	 * @throws Exception
	 */
	public static void setJSONObjectToObject(Object object,JSONObject jo) throws Exception{
		Class clazz = object.getClass();
		//只能获取自己的属性
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			Object value = getValue(f.getType(), jo, f.getName());
			if(value!=null){
				f.set(object, value);
			}
		}
		//父类的属性
		Field[] superFields = clazz.getSuperclass().getDeclaredFields();
		for(Field f : superFields){
			f.setAccessible(true);
			if(!jo.has(f.getName())){
				continue;
			}
			Object value = getValue(f.getType(), jo, f.getName());
			if(value!=null){
				f.set(object, value);
			}
		}
	}
	
	/**
	 * 设置json的值到object
	 * @param object
	 * @param jo
	 * @param noUpdateParam 不更新的属性列表
	 */
	public static void setObjectValue(Object object,JSONObject jo,String... noUpdateParam){
		List<String> paramsList = new LinkedList<String>();
		if(noUpdateParam!=null){
			paramsList = Arrays.asList(noUpdateParam);
		}
		Iterator it = jo.keys();
		while(it.hasNext()){
			String key = (String) it.next();
			if(paramsList.contains(key)){
				continue;
			}
			Field f = null;
			try {
				 f = object.getClass().getDeclaredField(key);
			} catch (Exception e) {
				 try {
					f = object.getClass().getSuperclass().getDeclaredField(key);
				}  catch (Exception e1) {
					continue;
				}
			}
			if(f!=null){
				f.setAccessible(true);
			}
			Object value;
			try {
				value = getValue(f.getType(), jo, f.getName());
			} catch (Exception e) {
				continue;
			}
			if(value!=null && f!=null){
				try {
					f.set(object, value);
				} catch (Exception e) {
					continue;
				}
			}
		}
	}
	
	/**
	 * 动态根据类型读取json里面的数据
	 * @param clazz 数据类型，如String,int,boolean
	 * @param jo json对象
	 * @param name 属性名
	 * @return 返回在json对象中name的值
	 * @throws Exception
	 */
	private static Object getValue(Class clazz,JSONObject jo,String name) throws Exception{
		if(clazz == String.class){
			return jo.getString(name);
		}else if(clazz == int.class){
			return jo.getInt(name);
		}else if(clazz == boolean.class){
			return jo.getBoolean(name);
		}else if(clazz == double.class){
			return jo.getDouble(name);
		}else if(clazz == long.class){
			return jo.getLong(name);
		}else if(clazz == float.class){
			try{
				return Float.parseFloat(jo.getString(name));
			}catch(Exception e){
				return 0;
			}
		}
		return null;
	}
	
	/**
	 * 显示加载progress
	 * @param message
	 */
	public static void showProgressDialog(String message,final Context ctx){
		dialog = new ProgressDialog(ctx);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.show();
		showing = true;
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(showing){
					dismissProgressDialog();
				}
			}
		}, 30*1000);
	}
	
	/**
	 * 关闭加载progress
	 */
	public static void dismissProgressDialog(){
		if(dialog!=null && dialog.isShowing()){
			dialog.dismiss();
			dialog = null;
		}
		showing = false;
	}
	
	public static void setProgressDialogMsg(String msg){
		if(dialog!=null && dialog.isShowing()){
			dialog.setMessage(msg);
		}
	}
	
	/**
	 * 返回日期
	 * @param day
	 * @return
	 */
	public static String getFormatDate(int day,String... format){
		String pattern = "yyyy-MM-dd";
		if(format!=null && format.length>0){
			pattern = format[0];
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		if(day!=0){
			calendar.add(Calendar.DAY_OF_YEAR, day);
		}
		return sdf.format(calendar.getTime());
	}
	
	public static boolean showing = false;
	
	/**
	 * 
	 * @param client
	 * @param post
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static synchronized StringBuffer excute(HttpClient client, HttpPost post,boolean active,boolean zip) throws IOException {
		if(MyApplication.sending && active){
			return null;
		}
		if(client==null){
			return null;
		}
		if(!active){
			MyApplication.sending = true;
		}
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		try{
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			if(zip){
				sb = uncompressToString(entity.getContent(), "gbk");
			}else{
				bufferedReader = new BufferedReader(
						new InputStreamReader(entity.getContent(),"gbk"));
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
			}
			if(sb!=null){
				System.out.println(sb.toString());
			}
			return sb;
		}catch(ClientProtocolException e){
			throw e;
		}finally{
			MyApplication.sending = false;
			if(bufferedReader!=null){
				bufferedReader.close();
			}
		}
	}
	
	 /* 
     * 字节数组解压缩后返回字符串 
     */  
    public static StringBuffer uncompressToString(InputStream in, String encoding) {  
    	StringBuffer sb = new StringBuffer();
    	GZIPInputStream gunzip = null;
    	BufferedReader bufferedReader = null;
        try {
        	gunzip = new GZIPInputStream(in);
        	bufferedReader = new BufferedReader(new InputStreamReader(gunzip,encoding));
            String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			return sb;
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{
        	if(bufferedReader!=null){
        		try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(gunzip!=null){
        		try {
					gunzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return null;  
    } 
    
    /**
	 * 格式化订单类型名称
	 * @param nBilType
	 * @return
	 */
	public static String getFormatType(String nBilType){
		if(nBilType.equals(StaticValues.ORDER_IN_BILTYPE)){
			return "进货订单";
		}else if(nBilType.equals(StaticValues.ORDER_BILTYPE)){
			return "进货单";
		}else if(nBilType.equals(StaticValues.ORDER_RE_BILTYPE)){
			return "进货退货单";
		}else if(nBilType.equals(StaticValues.SALE_IN_BILTYPE)){
			return "销售订单";
		}else if(nBilType.equals(StaticValues.SALE_BILTYPE)){
			return "销售单";
		}else if(nBilType.equals(StaticValues.SALE_RE_BILTYPE)){
			return "销售退货单";
		}else if(nBilType.equals(StaticValues.BIANJIA_BILTYPE)){
			return "变价调拨单";
		}else if(nBilType.equals(StaticValues.SK_BILTYPE)){
			return "收款单";
		}else if(nBilType.equals(StaticValues.FK_BILTYPE)){
			return "付款单";
		}
		return nBilType;
	}
	
	public static void showChooseDate(Context context,final TextView tv){
		Calendar calendar = Calendar.getInstance();
		Date d = new Date();
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(tv.getText().toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(d);
		DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, year); 
				cal.set(Calendar.MONTH, monthOfYear); 
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
				tv.setText(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
			}
		},  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}
}
