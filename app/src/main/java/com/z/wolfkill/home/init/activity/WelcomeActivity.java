package com.z.wolfkill.home.init.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.z.wolfkill.R;
import com.z.wolfkill.home.base.activity.BaseActivity;
import com.z.wolfkill.home.main.activity.StartActivity;
import com.z.wolfkill.home.main.tools.JsonTools;
import com.z.wolfkill.home.main.tools.RoleJsonTools;

public class WelcomeActivity extends BaseActivity implements Animation.AnimationListener{
    ImageView imageview_moon;
    ImageView imageview_sun;

//    LinearLayout linearlayout_textview;
    TextView textview_en_app_name;
    TextView textview_ch_app_name;

    TextView textview_version;

    //三条斜线
    ImageView imageview_line_0;
    ImageView imageview_line_1;
    ImageView imageview_line_2;



    Animation animLine0;
    Animation animLine1;
    Animation animLine2;

    Animation animMoon;
    Animation animSun;

    Animation animAppEnName;
    Animation animAppChName;


//    LayoutAnimationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_welcome);

        imageview_moon = (ImageView) findViewById(R.id.imageview_moon);
        imageview_sun = (ImageView) findViewById(R.id.imageview_sun);

//        linearlayout_textview = (LinearLayout) findViewById(R.id.linearlayout_textview);
        textview_en_app_name = (TextView) findViewById(R.id.textview_en_app_name);
        textview_ch_app_name = (TextView) findViewById(R.id.textview_ch_app_name);

        textview_version = (TextView) findViewById(R.id.textview_version);

        imageview_line_0 = (ImageView) findViewById(R.id.imageview_line_0);
        imageview_line_1 = (ImageView) findViewById(R.id.imageview_line_1);
        imageview_line_2 = (ImageView) findViewById(R.id.imageview_line_2);

        imageview_moon.setVisibility(View.INVISIBLE);
        imageview_sun.setVisibility(View.INVISIBLE);

//        linearlayout_textview.setVisibility(View.INVISIBLE);
        textview_en_app_name.setVisibility(View.INVISIBLE);
        textview_ch_app_name.setVisibility(View.INVISIBLE);

        textview_version.setVisibility(View.INVISIBLE);

        imageview_line_0.setVisibility(View.INVISIBLE);
        imageview_line_1.setVisibility(View.INVISIBLE);
        imageview_line_2.setVisibility(View.INVISIBLE);

        //太阳和月亮的帧动画，需要配合XML中的设置
//        AnimationDrawable adMoon = (AnimationDrawable) imageview_moon.getBackground();
//        adMoon.start();

//        AnimationDrawable adSun = (AnimationDrawable) imageview_sun.getBackground();
//        adSun.start();

        animMoon = AnimationUtils.loadAnimation(this, R.anim.anim_moon);
        animSun = AnimationUtils.loadAnimation(this, R.anim.anim_sun);

        animAppEnName = AnimationUtils.loadAnimation(this, R.anim.anim_bottom2top);
        animAppChName = AnimationUtils.loadAnimation(this, R.anim.anim_bottom2top);

//        controller = com.z.wolfkill.common.utils.AnimationUtils.getBottom2TopAnimationController(this);

        animLine0 = AnimationUtils.loadAnimation(this, R.anim.anim_line0);
        animLine1 = AnimationUtils.loadAnimation(this, R.anim.anim_line1);
        animLine2 = AnimationUtils.loadAnimation(this, R.anim.anim_line2);

        animMoon.setFillAfter(true);
        animMoon.setDuration(1500);
        animMoon.setInterpolator(new LinearInterpolator());
        animMoon.setAnimationListener(this);

        animSun.setFillAfter(true);
        animSun.setDuration(1500);
        animSun.setInterpolator(new LinearInterpolator());
        animSun.setAnimationListener(this);

        animLine0.setFillAfter(true);
        animLine0.setDuration(600);
        animLine0.setInterpolator(new DecelerateInterpolator());
        animLine0.setAnimationListener(this);

        animLine1.setFillAfter(true);
        animLine1.setDuration(800);
        animLine1.setInterpolator(new DecelerateInterpolator());
        animLine1.setAnimationListener(this);

        animLine2.setFillAfter(true);
        animLine2.setDuration(300);
        animLine2.setInterpolator(new DecelerateInterpolator());
        animLine2.setAnimationListener(this);


        animAppEnName.setFillAfter(true);
        animAppEnName.setDuration(200);
        animAppEnName.setInterpolator(new OvershootInterpolator());
        animAppEnName.setAnimationListener(this);

        animAppChName.setFillAfter(true);
        animAppChName.setDuration(200);
        animAppChName.setInterpolator(new OvershootInterpolator());
        animAppChName.setAnimationListener(this);

        imageview_moon.startAnimation(animMoon);
    }

    @Override
    public void initData() {
        checkPermission();
    }

    @Override
    public void initListener() {

    }

    /**
     *
     * @return true ： 权限已经开启 FALSE ：权限未开启，等待开启
     */
    private boolean checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            if(!Settings.System.canWrite(this)){
                Toast.makeText(this, "开启写入权限以继续应用", Toast.LENGTH_SHORT).show();
                Uri selfPackageUri = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, selfPackageUri);
                startActivityForResult(intent, 100);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if(animMoon == animation){
            imageview_moon.setVisibility(View.VISIBLE);
            //调试工具：游戏配置文件的创建，一般不需要
//            JsonTools jsonTools = new JsonTools();
//            jsonTools.optionRoleJson();
        }
        if(animSun == animation){
            imageview_sun.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animMoon == animation){
            //月亮动画播放完成
            imageview_sun.startAnimation(animSun);
            imageview_line_0.setVisibility(View.VISIBLE);
            imageview_line_0.startAnimation(animLine0);

            imageview_line_1.setVisibility(View.VISIBLE);
            imageview_line_1.startAnimation(animLine1);

            imageview_line_2.setVisibility(View.VISIBLE);
            imageview_line_2.startAnimation(animLine2);
        }

        if(animSun == animation){
//            linearlayout_textview.setVisibility(View.VISIBLE);
//            linearlayout_textview.setLayoutAnimation(controller);
//            linearlayout_textview.startLayoutAnimation();

            //播放英文标题
            textview_en_app_name.setVisibility(View.VISIBLE);
            textview_en_app_name.startAnimation(animAppEnName);

            //版本号透明度动画
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setInterpolator(new LinearInterpolator());
            alphaAnimation.setDuration(500);
            alphaAnimation.setFillAfter(true);
            //版本号动画播放
            textview_version.startAnimation(alphaAnimation);
            textview_version.setVisibility(View.VISIBLE);
        }

        if(animAppEnName == animation){
            //播放中文标题
            textview_ch_app_name.setVisibility(View.VISIBLE);
            textview_ch_app_name.startAnimation(animAppChName);
        }

        //如果中文播放完成，则跳转
        if(animAppChName == animation){
            //暂时跳转到游戏开始页面
            Intent intent = new Intent();
            intent.setClass(this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
