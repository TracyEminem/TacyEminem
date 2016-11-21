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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.ScreenUtils;
import com.z.wolfkill.home.main.activity.GameActivity;
import com.z.wolfkill.home.main.bean.Player;
import com.z.wolfkill.home.main.bean.Role;

import java.util.ArrayList;

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

public class SeerDialog extends Dialog {
    @BindView(R.id.textview_role_desc_left)
    TextView textviewRoleDescLeft;
    @BindView(R.id.imageview_avatar_left)
    ImageView imageviewAvatarLeft;
    @BindView(R.id.button_next)
    Button buttonNext;

    private Context context;
    private int resLayout;

    Player player;

    public SeerDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.resLayout = R.layout.dialog_seer;
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
        textviewRoleDescLeft.setText(player.role.desc);
        imageviewAvatarLeft.setImageResource(player.role.icon);
        String result;
        Role role = player.role;
        if(role.icon == R.drawable.ic_role_wolf || role.icon == R.drawable.ic_role_white_wolf_king){
            result = "狼人";
        }else{
            result = "好人";
        }
        buttonNext.setText(result);
    }

    public void preInit(Player player){
        this.player = player;
    }

    @OnClick({ R.id.button_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next:
                cancel();
                break;
        }
    }
}
