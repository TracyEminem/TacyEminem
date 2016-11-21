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
import com.z.wolfkill.common.utils.ToastUtil;
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

public class RogueChoiceDialog extends Dialog {
    @BindView(R.id.textview_role_desc_left)
    TextView textviewRoleDescLeft;
    @BindView(R.id.imageview_avatar_left)
    ImageView imageviewAvatarLeft;
    @BindView(R.id.relativelayout_left)
    RelativeLayout relativelayoutLeft;
    @BindView(R.id.textview_role_desc_right)
    TextView textviewRoleDescRight;
    @BindView(R.id.imageview_avatar_right)
    ImageView imageviewAvatarRight;
    @BindView(R.id.relativelayout_right)
    RelativeLayout relativelayoutRight;
    @BindView(R.id.button_next)
    Button buttonNext;


    private GameActivity activity;
    private int resLayout;

    //盗贼双牌选择列表
    ArrayList<Role> rogueRoleList;

    //游戏开局玩家优先级排序List
    ArrayList<Player> controlPlayerLevelList;


    public RogueChoiceDialog(GameActivity activity, int themeResId) {
        super(activity, themeResId);
        this.activity = activity;
        this.resLayout = R.layout.dialog_rogue_choice;
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
        dialogLayoutParams.width = (int) (ScreenUtils.getScreenWidth(activity) * 0.8);
        dialogLayoutParams.height = (int) (ScreenUtils.getScreenHeight(activity) * 0.7);

        window.setAttributes(dialogLayoutParams);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 设置Dialog窗口位置
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.DialogTheme);
    }

    public void initData() {
        //左边卡牌
        textviewRoleDescLeft.setText(rogueRoleList.get(0).desc);
        imageviewAvatarLeft.setImageResource(rogueRoleList.get(0).icon);

        //右边卡牌
        textviewRoleDescRight.setText(rogueRoleList.get(1).desc);
        imageviewAvatarRight.setImageResource(rogueRoleList.get(1).icon);
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.dialog
     * FUNCTIONAL_DESCRIPTION: 将盗贼待选区卡牌集合，与玩家优先级排序集合传入，等待交换
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:59
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void initList(ArrayList<Role> rogueRoleList, ArrayList<Player> controlPlayerLevelList) {
        this.rogueRoleList = rogueRoleList;
        this.controlPlayerLevelList = controlPlayerLevelList;
    }

    @OnClick({R.id.relativelayout_left, R.id.relativelayout_right, R.id.button_next})
    public void onClick(View view) {
        Role role = null;
        switch (view.getId()) {
            case R.id.relativelayout_left:
                activity.isChange = true;
                role = rogueRoleList.remove(0);
                for(int i = 0; i < controlPlayerLevelList.size(); i++){
                    Player player = controlPlayerLevelList.get(i);
                    if(player.role.icon == R.drawable.ic_role_rogue){
                        player.role.count--;
                        rogueRoleList.add(0, player.role);

                        role.count++;
                        player.role = role;
                        break;
                    }
                }
                cancel();
                break;
            case R.id.relativelayout_right:
                activity.isChange = true;
                role = rogueRoleList.remove(1);
                for(int i = 0; i < controlPlayerLevelList.size(); i++){
                    Player player = controlPlayerLevelList.get(i);
                    if(player.role.icon == R.drawable.ic_role_rogue){
                        player.role.count--;
                        rogueRoleList.add(1, player.role);

                        role.count++;
                        player.role = role;
                        break;
                    }
                }
                cancel();
                break;
            case R.id.button_next:
                activity.isChange = false;
                cancel();
                break;
        }
    }
}
