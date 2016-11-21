package com.z.wolfkill.home.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.LogUtils;
import com.z.wolfkill.common.utils.ScreenUtils;
import com.z.wolfkill.home.main.bean.Player;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * PROJECT_NAME: WolfKill
 * PACKAGE_NAME: com.z.wolfkill.home.main.dialog
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/12 12:43
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class LittleGirlDialog extends Dialog {
    @BindView(R.id.textview_role_desc_left)
    TextView textviewRoleDescLeft;
    @BindView(R.id.imageview_avatar_left)
    ImageView imageviewAvatarLeft;
    @BindView(R.id.button_no)
    Button buttonNo;
    @BindView(R.id.button_yes)
    Button buttonYes;

    private Context context;
    private int resLayout;

    Player player;

    public LittleGirlDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.resLayout = R.layout.dialog_little_girl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void initView() {
        this.setContentView(resLayout);
        ButterKnife.bind(this, this);

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

    public void initData() {

    }

    public void preInit(Player player) {
        this.player = player;
    }

    @OnClick({R.id.button_no, R.id.button_yes})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_no:
                cancel();
                break;
            case R.id.button_yes:
                LogUtils.print("小女孩夜晚被狼人发现，阵亡，没有遗言");
                player.isAlive = 0;
                cancel();
                break;
        }
    }
}
