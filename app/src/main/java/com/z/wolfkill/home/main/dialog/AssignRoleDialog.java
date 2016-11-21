package com.z.wolfkill.home.main.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.ScreenUtils;
import com.z.wolfkill.home.main.bean.Player;

import java.util.ArrayList;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.dialog
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/9 11:15
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class AssignRoleDialog extends Dialog implements View.OnClickListener {
    //按钮当前状态：准备查看身份
    private final int STATE_BUTTON_AVATAR = 1;
    //按钮当前状态：准备查看头像
    private final int STATE_BUTTON_ROLE = 2;

    int resLayout;
    Context context;

    TextView textview_role_desc;

    RelativeLayout relativelayout_avatar;
    ImageView imageview_avatar;
    TextView textview_seat_code;

    Button button_default_avatar;
    Button button_camera_avatar;
    Button button_next;

    ArrayList<Player> playerList;

    //目前是第几位玩家查看身份标记
    int indexPlayer = 0;
    //头像类型 0为默认，1为相机
    int avatarType = 0;

    public AssignRoleDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AssignRoleDialog(Context context, int resLayout) {
        super(context);
        this.context = context;
        this.resLayout = resLayout;
    }


    public AssignRoleDialog(Context context, int themeResId, int resLayout) {
        super(context, themeResId);
        this.context = context;
        this.resLayout = resLayout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void initView() {
        this.setContentView(resLayout);


        textview_role_desc = (TextView) findViewById(R.id.textview_role_desc);

        relativelayout_avatar = (RelativeLayout) findViewById(R.id.relativelayout_avatar);
        imageview_avatar = (ImageView) findViewById(R.id.imageview_avatar);
        textview_seat_code = (TextView) findViewById(R.id.textview_seat_code);

        button_default_avatar = (Button) findViewById(R.id.button_default_avatar);
        button_camera_avatar = (Button) findViewById(R.id.button_camera_avatar);
        button_next = (Button) findViewById(R.id.button_next);

        button_default_avatar.setOnClickListener(this);
        button_camera_avatar.setOnClickListener(this);
        button_next.setOnClickListener(this);

        button_next.setTag(STATE_BUTTON_AVATAR);
        textview_seat_code.setText(String.valueOf(indexPlayer + 1));

        // 将宽度设置为屏幕宽度
        Window window = this.getWindow();
        WindowManager.LayoutParams dialogLayoutParams = window.getAttributes();
        dialogLayoutParams.dimAmount = 0.8f;
        dialogLayoutParams.width = (int) (ScreenUtils.getScreenWidth(context) * 0.8);
        dialogLayoutParams.height = (int) (ScreenUtils.getScreenHeight(context) * 0.7);

        window.setAttributes(dialogLayoutParams);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 设置Dialog窗口位置
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.DialogTheme);
    }

    private void initData() {

    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.dialog
     * FUNCTIONAL_DESCRIPTION: 将随机分配好角色的玩家列表从Activity中传入
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:59
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void initList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_default_avatar:
                avatarType = 0;
                break;
            case R.id.button_camera_avatar:
                Toast.makeText(context, "暂未开启", Toast.LENGTH_SHORT).show();
//                avatarType = 1;
                break;
            case R.id.button_next:
                int state = (int) button_next.getTag();
                switch (state) {
                    case STATE_BUTTON_AVATAR:
                        rotateViewByKeyFrame(playerList.get(indexPlayer).role.icon, false);
                        break;
                    case STATE_BUTTON_ROLE:
                        switch (avatarType){
                            case 0://默认头像
                                playerList.get(indexPlayer).avatar = R.drawable.ic_avatar_male;
                                break;
                            case 1://相机头像

                                break;
                        }

                        indexPlayer++;
                        if (indexPlayer == playerList.size()) {
                            //已经全部查看身份
                            AssignRoleDialog.this.cancel();
                            return;
                        }
                        rotateViewByKeyFrame(R.drawable.ic_avatar_male, true);
                        break;
                }
                break;

        }
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.dialog
     * FUNCTIONAL_DESCRIPTION: 属性动画旋转角色图片
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 15:03
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void rotateViewByKeyFrame(@DrawableRes final int resId, final boolean isNext) {
        Keyframe keyframe1 = Keyframe.ofFloat(0.0f, 0);
        Keyframe keyframe2 = Keyframe.ofFloat(1.0f, 360);

        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofKeyframe("rotationY", keyframe1, keyframe2);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(relativelayout_avatar, propertyValuesHolder);
        objectAnimator.setDuration(400);

        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                if (fraction >= 0.75) {
                    imageview_avatar.setImageResource(resId);
                    if (isNext) {
                        textview_seat_code.setVisibility(View.VISIBLE);
                        textview_seat_code.setText(String.valueOf(indexPlayer + 1));
                    }else{
                        textview_seat_code.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        objectAnimator.addListener(new RoleAnimatorListener(indexPlayer, playerList));
        objectAnimator.start();
    }

    private class RoleAnimatorListener extends AnimatorListenerAdapter {
        private int indexPlayer;
        private ArrayList<Player> playerList;

        public RoleAnimatorListener(int indexPlayer, ArrayList<Player> playerList) {
            this.indexPlayer = indexPlayer;
            this.playerList = playerList;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            button_next.setEnabled(false);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            int state = (int) button_next.getTag();
            switch (state) {
                case STATE_BUTTON_AVATAR:
                    //按钮状态：从 头像 → 角色
                    button_next.setTag(STATE_BUTTON_ROLE);
                    button_next.setText("查看完毕");
                    playerList.get(indexPlayer).isReady = true;
                    textview_role_desc.setText("身份：" + playerList.get(indexPlayer).role.desc);
                    break;
                case STATE_BUTTON_ROLE:
                    //按钮状态：角色 → 头像
                    button_next.setTag(STATE_BUTTON_AVATAR);
                    textview_role_desc.setText("身份：待查看");
                    button_next.setText("查看身份");

                    break;
            }
            button_next.setEnabled(true);
        }
    }
}
