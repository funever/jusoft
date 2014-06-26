package com.jetsoft.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TreeSet;

import com.jetsoft.R;
import com.jetsoft.activity.ExceptionActivity;
import com.jetsoft.application.MyApplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类
 * 来接管程序,并记录 发送错误报告.
 *
 */
public class ExceptionHandler implements UncaughtExceptionHandler {
	/** Debug Log tag*/
	public static final String TAG = "ExceptionHandler";
	/** 是否开启日志输出,在Debug状态下开启,
	 * 在Release状态下关闭以提示程序性能
	 * */
	public static final boolean DEBUG = true;
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static ExceptionHandler INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;
	
	/** 使用Properties来保存设备的信息和错误堆栈信息*/
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	/** 错误报告文件的扩展名 */
	private static final String CRASH_REPORTER_EXTENSION = ".exception";
	
	private static Application application;
	
	private static String version = "unknow";
	
	/** 保证只有一个ExceptionHandler实例 */
	private ExceptionHandler(Application application) {
		ExceptionHandler.application = application;
		try {
			version = application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			version = "unknow";
		}
	}
	/** 获取ExceptionHandler实例 ,单例模式*/
	public static ExceptionHandler getInstance(Application application) {
		if (INSTANCE == null) {
			INSTANCE = new ExceptionHandler(application);
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象,
	 * 获取系统默认的UncaughtException处理器,
	 * 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	public void uncaughtException(Thread thread, final Throwable ex) {
		MyApplication my = (MyApplication) application;
		Timer timer = my.getTimer();
		if(timer!=null){
			timer.cancel();
			my.setTimer(null);
		}
		if (!handleException(ex,thread) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}
		else {
			//Sleep一会后结束程序
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				Log.e(TAG, "Error : ", e);
//			}
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(0);
			Intent intent = new Intent();
			intent.setClass(mContext, ExceptionActivity.class);
			mContext.startActivity(intent);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息
	 * 发送错误报告等操作均在此完成.
	 * 开发者可以根据自己的情况来自定义异常处理逻辑
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex, Thread thread) {
		if (ex == null) {
			return true;
		}
		// 使用Toast来显示异常信息
		// new Thread() {
		// @Override
		// public void run() {
		// Looper.prepare();
		// Toast.makeText(mContext,
		// application.getResources().getString(R.string.app_error)+"\n"+ msg,
		// Toast.LENGTH_LONG)
		// .show();
		// Looper.loop();
		// }
		//
		// }.start();
		// 收集设备信息
		// collectCrashDeviceInfo(mContext);
		// 保存错误报告文件
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		saveCrashInfoToFile(ex,result);

		// Intent intent = new Intent();
		// intent.setClass(application, ExceptionActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// application.startActivity(intent);

		long threadId = thread.getId();
		if (threadId == 1) {
			// 此处示例跳转到汇报异常界面。
			Intent intent = new Intent(application, ExceptionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("error", result);
			application.startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());
		} else {
			// 此处示例发生异常后，重新启动应用
			Intent intent = new Intent(application, ExceptionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("error", result);
			intent.putExtra("by", "uehandler");
			application.startActivity(intent);
		}
		// 发送错误报告到服务器
		// sendCrashReportsToServer(mContext);
		return true;
	}

	/**
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}

	/**
	 * 把错误报告发送给服务器,包含新产生的和以前没发送的.
	 * 
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(ctx.getFilesDir(), fileName);
				postReport(cr);
				cr.delete();// 删除已发送的报告
			}
		}
	}

	private void postReport(File file) {
		// TODO 使用HTTP Post 发送错误报告到服务器
//		HttpClient client = new DefaultHttpClient();
//		HttpPost post = new HttpPost(mContext.getResources().getString(R.string.exception_file_path));
//		FileBody fileBody= new FileBody(new File("E:\\1232434.jpg"));
//		MultipartEntity reqEntity = new MultipartEntity();  
//		reqEntity.addPart("file", fileBody);
//		post.setEntity(reqEntity);
//		HttpResponse resp = null;
//		try {
//			resp = client.execute(post);
//			System.out.println(resp.getStatusLine().getStatusCode());
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		PostMethod pm = new Post
	}

	/**
	 * 获取错误报告文件名
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = ctx.getFilesDir();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		return filesDir.list(filter);
	}
	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex,String result) {
		FileOutputStream trace = null;
		try {
			Log.e("error", result);
			result = "[EXCEPTION VERSION:"+version+"]=============================="+new Date().toString()+"=============================\n"+result+"\n======================================================================\n";
//			mDeviceCrashInfo.put(STACK_TRACE, result);

			String errorPath = application.getResources().getString(R.string.exception_file_path);
			File errorFile = new File(errorPath);
			if(!errorFile.isDirectory()){
				errorFile.mkdirs();
			}
			String fileName = errorPath+"/"+new SimpleDateFormat("yy-MM-dd").format(new Date())+CRASH_REPORTER_EXTENSION;
			File f = new File(fileName);
			if(f.isFile()){
				f.mkdir();
			}
			trace = new FileOutputStream(fileName,true);
//			mDeviceCrashInfo.store(trace, "");
//			mDeviceCrashInfo.save(trace, "");
			trace.write(result.getBytes());
			trace.flush();
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing report file...", e);
		}finally{
			try {
				if(trace!=null){
					trace.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 写log到文件
	 * @param log
	 */
	public static void saveLog(String log){
		FileOutputStream trace = null;
		log = "[INFO VERSION:"+version+"]=============================="+new Date().toString()+"=============================\n"+log+"\n======================================================================\n";
//		mDeviceCrashInfo.put(STACK_TRACE, result);

		String errorPath = application.getResources().getString(R.string.exception_file_path);
		File errorFile = new File(errorPath);
		if(!errorFile.isDirectory()){
			errorFile.mkdirs();
		}
		String fileName = errorPath+"/"+new SimpleDateFormat("yy-MM-dd").format(new Date())+CRASH_REPORTER_EXTENSION;
		try {
			trace = new FileOutputStream(fileName,true);
			trace.write(log.getBytes());
			trace.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(trace!=null){
					trace.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 收集程序崩溃的设备信息
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);
		}
		//使用反射来收集设备信息.在Build类中包含各种设备信息,
		//例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		//具体信息请参考后面的截图
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null));
				if (DEBUG) {
					Log.d(TAG, field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				Log.e(TAG, "Error while collect crash info", e);
			}

		}

	}

}