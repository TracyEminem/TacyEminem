package com.z.wolfkill.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;

public class DeviceInfo {

	private Context cntxt;

	public DeviceInfo(Context cntxt) {
		this.cntxt = cntxt;
	}
	/**
	 * 获取设备IMEI号
	 */
	public String getDeviceId() {
		TelephonyManager tm = (TelephonyManager) cntxt.getSystemService(Context.TELEPHONY_SERVICE);
		if (null != tm.getDeviceId()) {
			return tm.getDeviceId();
		} else {
			return "01012571";
		}
	}
	
	/**
	 * 获取应用版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionName = "1.0";
		try {
			PackageInfo mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = mPackageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public static Map<String, String> collectDeviceInfo(Context ctx) {
		// 用来存储设备信息和异常信息
		Map<String, String> infos = new HashMap<String, String>();
		String sysVersion;
		String softwareVersion;
		int softwareCode;
		String phoneSystem;
		String phoneType;
		String exceptionInfos;
		String currentDateAndTime;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis()); //获取当前时间     
		currentDateAndTime = formatter.format(curDate);     
		String date[] = currentDateAndTime.split(" ");
		infos.put("exceptionDate", date[0]); //出现异常的日期
		infos.put("exceptionTime", date[1]); //出现异常的时间
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				softwareVersion = pi.versionName == null ? "null": pi.versionName;
				softwareCode = pi.versionCode;
				infos.put("softwareVersion", softwareVersion);  //软件版本名称
				infos.put("softwareCode", softwareCode+"");  //软件版本号
			}
		} catch (NameNotFoundException e) {
		}
		 phoneSystem = Build.MODEL;
		 infos.put("phoneSystem", phoneSystem); //手机型号
		 phoneType = "Android";
		 infos.put("phoneType", phoneType);     //手机系统类型
		 sysVersion = Build.FINGERPRINT;
		 sysVersion = sysVersion.substring(sysVersion.indexOf(":")+1);
		 sysVersion = sysVersion.substring(0,sysVersion.indexOf("/"));
		 infos.put("sysVersion", sysVersion);  //手机系统版本
		 System.out.println(infos.toString());
		 return infos;
	}
}
