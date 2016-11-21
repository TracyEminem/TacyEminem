package com.z.wolfkill.home.crash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.z.wolfkill.home.base.activity.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    //系统默认的catchExcepting处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的全局Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    //用于格式化日期，作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandler(){}

    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        mContext = context;
        //获取系统默认的UncaughException对象
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //如果用户没有处理，则让系统默认的异常处理机制处理
        if(!handleException(e) && mDefaultHandler != null){
            mDefaultHandler.uncaughtException(t, e);
        }else{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                Log.e(TAG, "error:" + e);
            }
        }
        BaseActivity.exitApplication();
        //如果你需要在异常发生后，重启该应用，则在此处进行
//        Intent intent = new Intent(mContext, WelcomeActivity.class);
//        mContext.startActivity(intent);
    }

    /**
     * 自定义错误处理方法，收集错误信息，发送错误报告
     * 保存错误报告等等
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex){
        if(ex == null){
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，应用出现异常！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
        collectDeviceInfo(mContext);
        String fileNameString = saveCrashInfo2File(ex);
        //如果需要将错误日志上传到服务器，则在此行下面进行。

        return true;
    }

    /**
     * 手机设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx){
        try{
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if(pi != null){
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                Field[] fields = Build.class.getDeclaredFields();
                for(Field field : fields){
                    field.setAccessible(true);
                    infos.put(field.getName(), field.get(null).toString());
                    Log.i(TAG, field.getName() + " : " + field.get(null));
                }
            }
        }catch (Exception e){
            Log.e(TAG, "An error occured while collec crash info...", e);
        }
    }

    /**
     * 将错误信息保存到文件中
     * @param ex
     * @return
     */
    public String saveCrashInfo2File(Throwable ex){
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, String> entry : infos.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while(cause != null){
            cause.printStackTrace(printWriter);
            cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        Log.i(TAG, result);
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            //例如：crash-2016-10-20-15-23-16-123423423423.log
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            String path = getSavePath();
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
        }catch (Exception e){
            Log.i(TAG, "An error occured while writing file...", e);
        }
        return sb.toString();
    }

    /**
     * 获取错误日志的保存路径
     * @return
     */
    public String getSavePath(){
        String ERROR_PATH = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(sdCardExist){
            ERROR_PATH = Environment.getExternalStorageDirectory() + "/WolfKill/CrashHandler/";
        }else{
            ERROR_PATH = mContext.getCacheDir() + "/";
        }
        return ERROR_PATH;
    }
}
