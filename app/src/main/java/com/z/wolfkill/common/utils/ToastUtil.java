package com.z.wolfkill.common.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * PROJECT_NAME: WolfKill
 * PACKAGE_NAME: com.z.wolfkill.common.utils
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/11 16:57
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class ToastUtil {
    public static void showToast(Context context, CharSequence msg){
        if(Looper.myLooper() == null){
            Looper.prepare();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }else{
            if(Looper.myLooper() == Looper.getMainLooper()){
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }

    }

}
