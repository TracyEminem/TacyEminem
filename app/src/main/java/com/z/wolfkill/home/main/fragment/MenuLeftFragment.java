package com.z.wolfkill.home.main.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.LogUtils;
import com.z.wolfkill.home.main.activity.GameActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuLeftFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.relativelayout_role_desc)
    RelativeLayout relativelayoutRoleDesc;
    @BindView(R.id.relativelayout_desc_time)
    RelativeLayout relativelayoutDescTime;
    @BindView(R.id.relativelayout_restart)
    RelativeLayout relativelayoutRestart;
    @BindView(R.id.switch_role_desc)
    SwitchCompat switchRoleDesc;
    @BindView(R.id.switch_role_group)
    SwitchCompat switchRoleGroup;

    GameActivity gameActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.print("onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_layout_left, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }


    public void initData(){
        Activity activity = getActivity();
        if(activity instanceof GameActivity){
            gameActivity = (GameActivity) activity;
        }

    }

    public void initListener(){
        switchRoleDesc.setOnCheckedChangeListener(this);
        switchRoleGroup.setOnCheckedChangeListener(this);
    }


    @OnClick({R.id.relativelayout_role_desc, R.id.relativelayout_desc_time, R.id.relativelayout_restart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativelayout_role_desc:
                Toast.makeText(gameActivity, "角色介绍", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativelayout_desc_time:
                Toast.makeText(gameActivity, "陈述计时", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativelayout_restart:
                getActivity().finish();
                Toast.makeText(gameActivity, "重新开始", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_role_desc:
                gameActivity.showOrHideRoleDesc(isChecked);
                break;
            case R.id.switch_role_group:
                gameActivity.showOrHideRoleGroup(isChecked, true);
                break;
        }

    }
}
