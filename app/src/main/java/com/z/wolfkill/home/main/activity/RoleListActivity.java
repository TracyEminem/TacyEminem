package com.z.wolfkill.home.main.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.ScreenUtils;
import com.z.wolfkill.home.base.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 *
 * PACKAGE_NAME: com.z.wolfkill.home.main.activity
 * FUNCTIONAL_DESCRIPTION: 暂时无用，可能废弃
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/10 14:56
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */
public class RoleListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_role_list);
        ButterKnife.bind(this);

        // 将宽度设置为屏幕宽度
        Window window = this.getWindow();
        WindowManager.LayoutParams dialogLayoutParams = window.getAttributes();
        dialogLayoutParams.dimAmount = 0.8f;
        dialogLayoutParams.width = ScreenUtils.getScreenWidth(this);
        dialogLayoutParams.height = (int) (ScreenUtils.getScreenHeight(this) * 0.5);

        window.setAttributes(dialogLayoutParams);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 设置Dialog窗口位置
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogTheme);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
