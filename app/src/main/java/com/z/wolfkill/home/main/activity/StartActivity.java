package com.z.wolfkill.home.main.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.DisplayUtil;
import com.z.wolfkill.common.utils.ScreenUtils;
import com.z.wolfkill.home.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.animation;

/**
 *
 * PACKAGE_NAME: com.z.wolfkill.home.main.activity
 * FUNCTIONAL_DESCRIPTION: 游戏开始页面
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/10 14:56
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */
public class StartActivity extends BaseActivity implements Animator.AnimatorListener{

    @BindView(R.id.button_menu)
    Button buttonMenu;
    @BindView(R.id.button_start)
    Button buttonStart;

    @BindView(R.id.imageview_star1)
    ImageView imageviewStar1;
    @BindView(R.id.imageview_star2)
    ImageView imageviewStar2;

    int screenWidth;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        screenWidth = ScreenUtils.getScreenWidth(this);
        screenHeight = ScreenUtils.getScreenHeight(this);

        animate1();
        animate2();
    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.button_menu, R.id.button_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_menu:
                showMenu(buttonMenu);
                break;
            case R.id.button_start:
                Intent intent = new Intent(this, AddRoleActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     *
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 弹出popupWindow
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:56
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void showMenu(View anchorView) {
        View menuView = getLayoutInflater().inflate(R.layout.popupwindow_layout, null);

        final PopupWindow popupWindow = new PopupWindow(menuView,
                DisplayUtil.dp2px(this, 110), ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        LinearLayout linearlayout_role = (LinearLayout) menuView.findViewById(R.id.linearlayout_role);
        LinearLayout linearlayout_story = (LinearLayout) menuView.findViewById(R.id.linearlayout_story);
        LinearLayout linearlayout_suggestion = (LinearLayout) menuView.findViewById(R.id.linearlayout_suggestion);
        LinearLayout linearlayout_exit = (LinearLayout) menuView.findViewById(R.id.linearlayout_exit);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.linearlayout_role:
                        Toast.makeText(StartActivity.this, "人物", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        break;
                    case R.id.linearlayout_story:
                        Toast.makeText(StartActivity.this, "故事", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        break;
                    case R.id.linearlayout_suggestion:
                        Toast.makeText(StartActivity.this, "建议", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        break;
                    case R.id.linearlayout_exit:
                        Toast.makeText(StartActivity.this, "退出", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        exitApplication();
                        break;
                }
            }
        };

        linearlayout_role.setOnClickListener(listener);
        linearlayout_story.setOnClickListener(listener);
        linearlayout_suggestion.setOnClickListener(listener);
        linearlayout_exit.setOnClickListener(listener);

        int windowPos[] = calculatePopWindowPos(anchorView, menuView);
        int xOff = 40;
        int yOff = 10;

        windowPos[0] -= DisplayUtil.dp2px(this, xOff);
        windowPos[1] += DisplayUtil.dp2px(this, yOff);

        popupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    /**
     *
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:57
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     *
     * @param rootView 呼出window的view
     * @param menuView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int[] calculatePopWindowPos(final View rootView, final View menuView) {

        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];

        //获取rootView在屏幕上的左上角坐标位置
        rootView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = rootView.getHeight();

        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(rootView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(rootView.getContext());

        menuView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        // 计算menuView的高宽
        final int windowHeight = menuView.getMeasuredHeight();
        final int windowWidth = menuView.getMeasuredWidth();

        //判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);

        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }

        return windowPos;
    }


    AnimatorSet animatorSet1;
    AnimatorSet animatorSet2;
    ObjectAnimator xAnimator;
    ObjectAnimator yAnimator;

    /**
     *
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 流星动画1
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:57
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void animate1(){
        xAnimator = ObjectAnimator.ofFloat(imageviewStar1, "translationX", 100, -(screenWidth + 500));
        yAnimator = ObjectAnimator.ofFloat(imageviewStar1, "translationY", 0, (float) (Math.random() + 1) * 1000);

        animatorSet1 = new AnimatorSet();
        animatorSet1.setDuration((long) ((Math.random() + 1) * 4000));
        animatorSet1.setTarget(imageviewStar1);
        animatorSet1.setInterpolator(new AccelerateInterpolator());

        animatorSet1.addListener(this);
        animatorSet1.playTogether(xAnimator, yAnimator);
        animatorSet1.start();
    }

    /**
     *
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 流星动画2
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:57
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void animate2(){
        xAnimator = ObjectAnimator.ofFloat(imageviewStar2, "translationX", 100, -(screenWidth + 500));
        yAnimator = ObjectAnimator.ofFloat(imageviewStar2, "translationY", 0, (float) (Math.random() + 1) * 1000);

        animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration((long) ((Math.random() + 1) * 4000));
        animatorSet2.setTarget(imageviewStar2);
        animatorSet2.setInterpolator(new AccelerateInterpolator());

        animatorSet2.addListener(this);
        animatorSet2.playTogether(xAnimator, yAnimator);
        animatorSet2.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(animation == animatorSet1){
            animate1();
        }

        if(animation == animatorSet2){
            animate2();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        animatorSet1.end();
        animatorSet2.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animate1();
        animate2();
    }
}
