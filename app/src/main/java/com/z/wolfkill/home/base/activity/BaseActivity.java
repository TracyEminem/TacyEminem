package com.z.wolfkill.home.base.activity;

import android.app.Activity;
import android.os.Bundle;

import java.util.Stack;

public abstract class BaseActivity extends Activity {
    private static Stack<Activity> activities = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);

        initView();
        initData();
        initListener();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    /**
     * CREATE_TIME:2016/09/20 15:37
     * FUNCTION_DESC:退出当前应用程序
     * CREATE_BY:尽际
     */
    public static void exitApplication(){
        for(Activity activity : activities){
            if(activity != null){
                activity.finish();
            }
        }
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
