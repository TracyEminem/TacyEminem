package com.z.wolfkill.home.base.app;

import android.app.Application;

import com.z.wolfkill.common.constants.Constants;
import com.z.wolfkill.home.crash.CrashHandler;

/**
 * Created by Administrator on 2016/9/20.
 */
public class IApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        if(Constants.isCollectException){
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }

    }
}
