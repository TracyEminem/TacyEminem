package com.z.wolfkill.common.utils;

import android.util.Log;

import com.z.wolfkill.common.constants.Constants;

/**
 *
 * 功能描述:日志打印工具类,当打生产版本时可将debugVer改为false
 * 创建作者:Z
 * 创建时间：
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class LogUtils {
	public static final int TYPE_LOG = 0;
	public static final int TYPE_CONSOLE = 1;
	public static final int TYPE_CRASH_EXCEPTION = 2;


	public static void print(String msg) {
		print(TYPE_LOG, msg);
	}

	public static void print(int logType, String msg) {
		if (!Constants.isShowLog || null == msg) {
			return;
		}

		switch (logType) {
			case TYPE_LOG:
				Log.i("WolfKill", msg);
				break;
			case TYPE_CONSOLE:
				System.out.println(msg);
				break;
			case TYPE_CRASH_EXCEPTION:
				Log.e("WolfKill", msg);
				break;
		}
	}
}
