package com.z.wolfkill.home.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.sax.RootElement;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.z.wolfkill.R;
import com.z.wolfkill.common.constants.Constants;
import com.z.wolfkill.common.parser.JsonUtils;
import com.z.wolfkill.common.utils.DisplayUtil;
import com.z.wolfkill.common.utils.LogUtils;
import com.z.wolfkill.common.utils.ToastUtil;
import com.z.wolfkill.common.utils.reflect.IDHelper;
import com.z.wolfkill.home.base.activity.BaseActivity;
import com.z.wolfkill.home.main.adapter.RoleAdapter;
import com.z.wolfkill.home.main.bean.Player;
import com.z.wolfkill.home.main.bean.Role;
import com.z.wolfkill.home.main.dialog.RoleListDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *
 * PACKAGE_NAME: ${PACKAGE_NAME}
 * FUNCTIONAL_DESCRIPTION: 
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/10 14:44
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */
public class AddRoleActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gridview_role)
    GridView gridviewRole;
    @BindView(R.id.textview_list)
    TextView textviewList;
    @BindView(R.id.textview_player_count)
    TextView textviewPlayerCount;
    @BindView(R.id.relativelayout_list)
    RelativeLayout relativelayoutList;


    RoleAdapter roleAdapter;
    ArrayList<Role> roleList;

    //玩家列表
    ArrayList<Player> playerList;
    //已选角色列表
    ArrayList<Role> alreadyRoleList;

    @BindView(R.id.relativelayout_content)
    RelativeLayout relativelayoutContent;
    @BindView(R.id.button_complete)
    Button buttonComplete;
    @BindView(R.id.button_back)
    Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_role);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        roleList = initRoleList();

        playerList = initPlayer();
        alreadyRoleList = initChosenRole();

        roleAdapter = new RoleAdapter(this, roleList);
        gridviewRole.setAdapter(roleAdapter);
    }

    @Override
    public void initListener() {
        gridviewRole.setOnItemClickListener(this);
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION:初始化角色
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:52
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public ArrayList<Role> initRoleList() {
        ArrayList<Role> list = new ArrayList<>();
        //读取角色配置文件
        try {
            InputStream inputStream = getResources().getAssets().open("role_options.txt");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int curLen = -1;

            while ((curLen = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, curLen);
            }

            String roleJsonString = baos.toString();

            baos.close();
            inputStream.close();

            List<Map<String, Object>> listMap = JsonUtils.getListMap(JsonUtils.getMapStr(roleJsonString).get("roles"));
            for (int i = 0; i < listMap.size(); i++) {
                Map<String, Object> roleMap = listMap.get(i);

                int icon = IDHelper.getDrawable(this, roleMap.get("icon_name").toString());
                String desc = roleMap.get("desc").toString();
                int count = Integer.valueOf(roleMap.get("count").toString());
                int level = Integer.valueOf(roleMap.get("level").toString());
                int limitCount = Integer.valueOf(roleMap.get("limit_count").toString());
                int camp = Integer.valueOf(roleMap.get("camp").toString());
                String notice = roleMap.get("notice").toString();
                int isFirstNightControl = Integer.valueOf(roleMap.get("is_first_night_control").toString());

                Role role = new Role(icon, desc, count, level, limitCount, camp, notice, isFirstNightControl);
                list.add(role);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 初始化玩家列表
     */
    private ArrayList<Player> initPlayer() {
        ArrayList<Player> playerList = new ArrayList<>();
        return playerList;
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 初始化已选角色列表
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:52
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    private ArrayList<Role> initChosenRole() {
        ArrayList<Role> alreadyRoleList = new ArrayList<>();
        return alreadyRoleList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RoleAdapter roleAdapter = (RoleAdapter) parent.getAdapter();
        Role role = roleAdapter.getItem(position);

        boolean canAdd = true;
        if (playerList.size() < Constants.MAX_PLAYERS) {
            for (int i = 0; i < playerList.size(); i++) {
                Player player = playerList.get(i);
                if (player.role.icon == role.icon && role.limitCount == 1) {
                    canAdd = false;
                    break;
                }
            }

        } else {
            ToastUtil.showToast(this, "游戏人数过多!");
            canAdd = false;
        }

        if (canAdd) {
            playAnim(relativelayoutContent, view, textviewList, role);
            role.count++;

            int hp;
            if(role.icon == R.drawable.ic_role_elder){
                hp = 2;
            }else{
                hp = 1;
            }
            Player player = new Player(role, hp);

            playerList.add(player);

            //判断是否有重复角色
            if (!alreadyRoleList.contains(role)) {
                alreadyRoleList.add(role);
            }
        }

        int playerCount = playerList.size();
        textviewPlayerCount.setText(String.valueOf(playerCount));
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 选择角色动画
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:52
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     *
     * @param viewGroup 点击的View所在的Layout组
     * @param startView 所点击的出发动画的View，即从该View开始移动
     * @param endView   动画最终移动到指定endView所在位置。
     * @param role      所点击的角色
     */
    public void playAnim(final ViewGroup viewGroup, View startView, View endView, Role role) {
        int[] startViewLoc = new int[2];
        int[] endViewLoc = new int[2];

        startView.getLocationInWindow(startViewLoc);
        endView.getLocationInWindow(endViewLoc);

        LogUtils.print("被点击的View的位置：" + startViewLoc[0] + "," + startViewLoc[1]);
        LogUtils.print("终点View的位置：" + endViewLoc[0] + "," + endViewLoc[1]);

        final ImageView animImageView = new ImageView(this);
        animImageView.setImageResource(role.icon);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtil.dp2px(this, 50), DisplayUtil.dp2px(this, 50));
        layoutParams.leftMargin = startViewLoc[0] + DisplayUtil.dp2px(this, 10);
        layoutParams.topMargin = startViewLoc[1];

        animImageView.setLayoutParams(layoutParams);

        viewGroup.addView(animImageView);


        TranslateAnimation xyTransAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                1.0f,
                Animation.ABSOLUTE,
                endViewLoc[0] - layoutParams.leftMargin,
                Animation.RELATIVE_TO_SELF,
                1.0f,
                Animation.ABSOLUTE,
                endViewLoc[1] - layoutParams.topMargin);

        xyTransAnimation.setDuration(500);
        xyTransAnimation.setInterpolator(new AnticipateInterpolator());
        xyTransAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(animImageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animImageView.startAnimation(xyTransAnimation);

    }


    @OnClick({R.id.relativelayout_list, R.id.button_complete, R.id.button_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.relativelayout_list:
                RoleListDialog roleListDialog = new RoleListDialog(this, R.style.DialogTheme, R.layout.dialog_role_list, buttonComplete);
                roleListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        int playerCount = playerList.size();
                        textviewPlayerCount.setText(String.valueOf(playerCount));
                    }
                });

                roleListDialog.initList(alreadyRoleList, playerList);

                roleListDialog.show();

                break;
            case R.id.button_complete:
                //开局条件判断
                if (canStart()) {
                    Intent intent = new Intent(this, GameActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("PLAYER_LIST", playerList);
                    intent.putExtra("PLAYER_LIST", bundle);

                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 当前配置是否能够开局
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:52
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    private boolean canStart() {
        boolean civilian = false;
        boolean protoss = false;
        boolean wolf = false;

        for (Role role : alreadyRoleList) {
            if (role.camp == Role.CAMP_CIVILIAN) {
                civilian = true;
            }
            if (role.camp == Role.CAMP_PROTOSS) {
                protoss = true;
            }
            if (role.camp == Role.CAMP_WOLF) {
                wolf = true;
            }
        }

        boolean canStart = civilian && protoss && wolf;
        if (!canStart) {
            StringBuilder result = new StringBuilder("缺少：");
            if (!civilian) result.append("村民，");
            if (!protoss) result.append("神民，");
            if (!wolf) result.append("狼人，");
            result.append("请完善角色搭配");
            ToastUtil.showToast(this, result);
        }

        return canStart;
    }


}