package com.z.wolfkill.common.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtil {

	public static final int OPEN_INPUT_METHOD = 1; // 打开键盘
	public static final int CLOSE_INPUT_METHOD = 2; // 关闭键盘

	/**
	 * 控制虚拟键盘状态
	 * 
	 * @param context 上下文对象
	 * @param view  控件对象
	 * @param flag 1: 打开键盘 2： 关闭键盘
	 */
	public static void operateInputMethodWindow(Context context, View view,
			int flag) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && view != null) {
			switch (flag) {
			case OPEN_INPUT_METHOD:
				imm.showSoftInput(view, 0);
				break;
			case CLOSE_INPUT_METHOD:
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				break;
			}
		}
	}
}
