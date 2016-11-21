package com.z.wolfkill.home.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.ScreenUtils;
import com.z.wolfkill.home.main.activity.AddRoleActivity;
import com.z.wolfkill.home.main.adapter.RoleListAdapter;
import com.z.wolfkill.home.main.bean.Player;
import com.z.wolfkill.home.main.bean.Role;

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

public class RoleListDialog extends Dialog implements View.OnClickListener{
    int resLayout;
    Context context;

    Button button_close;
    ListView listview_role;
    TextView textview_role_count;
    Button button_complete;

    RoleListAdapter roleListAdapter;
    ArrayList<Role> alreadyRoleList;
    ArrayList<Player> playerList;
    Button buttonComplete;

    public RoleListDialog(Context context) {
        super(context);
        this.context = context;
    }

    public RoleListDialog(Context context, int resLayout) {
        super(context);
        this.context = context;
        this.resLayout = resLayout;
    }


    public RoleListDialog(Context context, int themeResId, int resLayout, Button buttonComplete) {
        super(context, themeResId);
        this.context = context;
        this.resLayout = resLayout;
        this.buttonComplete = buttonComplete;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void initView(){
        this.setContentView(resLayout);

        // 将宽度设置为屏幕宽度
        Window window = this.getWindow();
        WindowManager.LayoutParams dialogLayoutParams = window.getAttributes();
        dialogLayoutParams.dimAmount = 0.8f;
        dialogLayoutParams.width = ScreenUtils.getScreenWidth(context);
        dialogLayoutParams.height = (int) (ScreenUtils.getScreenHeight(context) * 0.6);

        window.setAttributes(dialogLayoutParams);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 设置Dialog窗口位置
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogTheme);

        button_close = (Button) findViewById(R.id.button_close);
        listview_role = (ListView) findViewById(R.id.listview_role);
        textview_role_count = (TextView) findViewById(R.id.textview_role_count);
        button_complete = (Button) findViewById(R.id.button_complete);

        button_close.setOnClickListener(this);
        button_complete.setOnClickListener(this);
    }

    private void initData(){
        roleListAdapter = new RoleListAdapter(context, alreadyRoleList, playerList, this);
        listview_role.setAdapter(roleListAdapter);

        refreshCount();
    }

    public void initList(ArrayList<Role> alreadyRoleList, ArrayList<Player> playerList){
        this.alreadyRoleList = alreadyRoleList;
        this.playerList = playerList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_close:
                cancel();
                break;
            case R.id.button_complete:
                cancel();
                ((AddRoleActivity)context).onClick(buttonComplete);
                break;
        }
    }

    public void refreshCount(){
        textview_role_count.setText("共计 " + playerList.size() + " 位玩家");
    }
}
