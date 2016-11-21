package com.z.wolfkill.common.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;

import com.z.wolfkill.R;

/**
 * Created by Administrator on 2016/10/24.
 */

public class AnimationUtils {
    /**
     * 上入动画
     * @param context
     * @return
     */
    public static LayoutAnimationController getBottom2TopAnimationController(Context context){
        int duration = 300;
        AnimationSet animationSet = new AnimationSet(true);

        Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.anim_bottom2top);
        animationSet.addAnimation(animation);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new OvershootInterpolator());

        LayoutAnimationController controller = new LayoutAnimationController(animationSet, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);

        return controller;
    }
}
