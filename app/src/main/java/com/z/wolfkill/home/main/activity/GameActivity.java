package com.z.wolfkill.home.main.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.LogUtils;
import com.z.wolfkill.common.utils.ToastUtil;
import com.z.wolfkill.home.base.activity.BaseActivity;
import com.z.wolfkill.home.main.adapter.PlayerListAdapter;
import com.z.wolfkill.home.main.bean.Player;
import com.z.wolfkill.home.main.bean.Role;
import com.z.wolfkill.home.main.dialog.AssignRoleDialog;
import com.z.wolfkill.home.main.dialog.LittleGirlDialog;
import com.z.wolfkill.home.main.dialog.RogueChoiceDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.activity
 * FUNCTIONAL_DESCRIPTION: 分配角色页面
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/10 14:54
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */
public class GameActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    //    @BindView(R.id.button_open_menu)
//    Button buttonOpenMenu;
    @BindView(R.id.button_back)
    Button buttonBack;
    @BindView(R.id.gridview_player)
    StickyGridHeadersGridView gridviewPlayer;
    @BindView(R.id.button_control)
    Button buttonControl;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.imageview_bg_night)
    ImageView imageviewBgNight;
    @BindView(R.id.button_yes)
    Button buttonYes;
    @BindView(R.id.button_no)
    Button buttonNo;
    @BindView(R.id.textview_notice)
    TextView textviewNotice;
    @BindView(R.id.linearlayout_control)
    LinearLayout relativelayoutControl;
    @BindView(R.id.relativelayout_control_notice)
    RelativeLayout relativelayoutControlNotice;

    //待分配角色的玩家列表
    ArrayList<Player> tempPlayerList;
    //最终分配角色的玩家列表
    ArrayList<Player> playerList;
    PlayerListAdapter playerListAdapter;

    //盗贼双牌选择列表
    ArrayList<Role> rogueRoleList;

    //游戏开局玩家优先级排序List
    ArrayList<Player> controlPlayerLevelList;
    //玩家座位号排序List
    ArrayList<Player> controlPlayerSeatList;
    //丘比特连人集合
    ArrayList<Player> cupidPlayerList;
    //野孩子榜样集合
    ArrayList<Player> wildChildExamplePlayerList;
    //守卫守护集合
    ArrayList<Player> guardPlayerList;
    //当前回合守护玩家临时存放
    Player curGuardPlayer;

    //狼人当夜杀害集合
    ArrayList<Player> wolfKillPlayerList;
    //女巫当夜毒杀集合
    ArrayList<Player> poisonKillPlayerList;

    //昨夜死亡玩家集合
    ArrayList<Player> lastNightDeadPlayerList;

    //昨晚如果猎人死亡，天亮，猎人击杀集合
    ArrayList<Player> hunterPlayerKillList;

    //当日第一阶段阵亡集合
    ArrayList<Player> dayFirstDeadPlayerList;
    //当日第二阶段阵亡集合
    ArrayList<Player> daySecondDeadPlayerList;
    //当日死亡总集合
    ArrayList<Player> dayDeadPlayerList;

    //盗贼是否切换了角色标记
    public boolean isChange = false;

    //某一个角色是否操作完毕
    public boolean haveDone;
    //当前操作状态，例如：狼人操作？丘比特操作?
    public int state;

    //猎人是否在当夜被杀，白天来临时猎人发动技能
    public int hunterState;

    private ControlTask controlTask;
    //当前玩家列表显示状态
    private boolean isGroup;

    //女巫操作目前处于解药环节还是毒药环节
    public int antidoteOrPoison = 0;

    //当夜当前操作的玩家以及角色
    public Player currentPlayer;

    //是否为第一夜
    public int isFirstNight;

    //游戏结束提示
    public String resultNotice;

    //List集合排序类型
    private static final int TYPE_LEVEL = 1;
    private static final int TYPE_SEAT = 2;
    private static final int TYPE_CAMP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        refresh(null, "", null);
        relativelayoutControlNotice.setVisibility(View.GONE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
    }

    @Override
    public void initData() {
        isFirstNight = 1;
        isChange = false;
        initDrawerEvent();
        playerList = initPlayerList();

        AssignRoleDialog assignRoleDialog = new AssignRoleDialog(this, R.style.DialogTheme, R.layout.dialog_assign_role);
        assignRoleDialog.setCancelable(false);
        assignRoleDialog.setCanceledOnTouchOutside(false);
        assignRoleDialog.initList(playerList);
        assignRoleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                playerListAdapter.notifyDataSetChanged();
            }
        });

        assignRoleDialog.show();
    }

    @Override
    public void initListener() {
//        gridviewPlayer.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controlTask != null && controlTask.getStatus() != AsyncTask.Status.FINISHED) {
            controlTask.cancel(true);
        }
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 随机分配角色
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:55
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    private ArrayList<Player> initPlayerList() {
        //初始化丘比特集合
        cupidPlayerList = new ArrayList<>(2);
        //初始化野孩子榜样集合
        wildChildExamplePlayerList = new ArrayList<>(1);
        //初始化守卫守护集合
        guardPlayerList = new ArrayList<>(1);
        //狼人当夜杀害集合
        wolfKillPlayerList = new ArrayList<>(1);
        //女巫当夜毒杀集合
        poisonKillPlayerList = new ArrayList<>(1);
        //昨夜死亡玩家集合
        lastNightDeadPlayerList = new ArrayList<>();

        //昨晚如果猎人死亡，天亮，猎人击杀集合
        hunterPlayerKillList = new ArrayList<>();

        //当日第一阶段阵亡集合
        dayFirstDeadPlayerList = new ArrayList<>();
        //当日第二阶段阵亡集合
        daySecondDeadPlayerList = new ArrayList<>();
        //当日死亡总集合
        dayDeadPlayerList = new ArrayList<>();

        tempPlayerList = getIntent().getBundleExtra("PLAYER_LIST").getParcelableArrayList("PLAYER_LIST");
        //是否包含盗贼
        boolean isHaveRogue = false;
        for (Player player : tempPlayerList) {
            if (player.role.icon == R.drawable.ic_role_rogue) {
                isHaveRogue = true;
                break;
            }
        }

        //如果包含盗贼，则初始化盗贼逻辑
        if (isHaveRogue) {
            initRogueRoleList();
        }

        playerList = new ArrayList<>();
        //随机分配角色并分配座位号
        int listSize = tempPlayerList.size();
        for (int i = listSize - 1; i >= 0; i--) {
            int randomIndex = (int) (Math.random() * tempPlayerList.size());
            Player player = tempPlayerList.remove(randomIndex);
            //配置座位号
            player.seat = listSize - i;
            //配置玩家头像
            player.avatar = R.drawable.ic_avatar_default;
            //配置玩家是否存活，设定：当玩家查看角色时，分配该角色为存活。
            playerList.add(player);
        }

        playerListAdapter = new PlayerListAdapter(this, playerList,
                cupidPlayerList,
                wildChildExamplePlayerList,
                guardPlayerList,
                wolfKillPlayerList,
                poisonKillPlayerList,
                dayFirstDeadPlayerList,
                daySecondDeadPlayerList,
                hunterPlayerKillList);
        gridviewPlayer.setAdapter(playerListAdapter);

        //打印盗贼候选区卡牌
        if (rogueRoleList != null) {
            StringBuilder sb = new StringBuilder("盗贼候选区列表:");
            for (Role xRole : rogueRoleList) {
                sb.append(xRole.desc + "##");
            }
            LogUtils.print(sb.toString());
        }

        return playerList;
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 盗贼预留选牌逻辑,将两张村民牌混入选定角色集合,交换出来的卡牌交由另一个集合暂存
     * 例如：原集合大小：15位玩家，那么随机范围为0 ~ 16, 如果随机到下标15~16，则意味着原定排序无变化
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/11 17:12
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void initRogueRoleList() {
        rogueRoleList = new ArrayList<>();
        //取出原集合的村民对象
        Role villagerRole = null;
        for (int i = 0; i < tempPlayerList.size(); i++) {
            Role xRole = tempPlayerList.get(i).role;
            if (xRole.icon == R.drawable.ic_role_villager) {
                villagerRole = xRole;
                break;
            }
        }

        for (int i = 0; i < 2; i++) {
            //+2为两张平民牌
            int randomIndex = -1;
            int tempRandom;
            do {
                //如果第二次随机和第一次随机的位置一样，则重新随机,该随机算法可以优化，暂时没有必要
                tempRandom = (int) (Math.random() * (tempPlayerList.size() + 2));
            } while (randomIndex == tempRandom);

            randomIndex = tempRandom;
            //注意判断，否则下标越界
            if (randomIndex >= tempPlayerList.size()) {
                //则玩家分配的角色维持原状，新增的两张村民牌放入盗贼待选卡区的List集合,注意，此时村民牌数量不要+1。
                //因为可能出现玩家不选择交换待选区卡牌。
                //插入2遍相同的角色对象。
                rogueRoleList.add(villagerRole);
            } else {
                //意味着玩家分配的角色牌和村民牌混入了，需要交换待选区卡牌
                Role tempRole = tempPlayerList.get(randomIndex).role;

                tempRole.count--;
                villagerRole.count++;

                tempPlayerList.get(randomIndex).role = villagerRole;

                rogueRoleList.add(tempRole);
            }
        }
    }

//    @OnClick({R.id.button_open_menu})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.button_open_menu:
//                drawerLayout.openDrawer(Gravity.RIGHT);
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
//                break;
//        }
//    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 初始化抽屉动画事件
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 14:55
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    private void initDrawerEvent() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = drawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                //取消缩放
                scale = 1;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT")) {
                    float leftScale = 1 - 0.3f * scale;
                    //取消缩放
                    leftScale = 1;

                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);

                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));

                    ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));

                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);

                    mContent.invalidate();

                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                } else {
                    ViewHelper.setTranslationX(mContent, -mMenu.getMeasuredWidth() * slideOffset);

                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();

                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }


    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 显示或隐藏角色名称方法
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 20:17
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void showOrHideRoleDesc(boolean isShow) {
        for (Player player : playerList) {
            player.isShow = isShow;
        }
        playerListAdapter.notifyDataSetChanged();
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 显示或隐藏玩家角色分组
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/10 20:17
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void showOrHideRoleGroup(boolean isShow, boolean isNeedClearList) {
        isGroup = isShow;
        if (isShow) {//显示分组
            sortList(playerList, TYPE_CAMP);
        } else {//不显示分组
            sortList(playerList, TYPE_SEAT);
        }

        clearCurrentControlList(state, isNeedClearList);
        playerListAdapter.showOrHideGroup(isShow);
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 天黑方法
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/11 11:39
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void toNight() {
        //昨夜死亡玩家中是否存在殉情
        if (cupidPlayerList.size() > 0) {
            outFor:
            for (int i = 0; i < dayDeadPlayerList.size(); i++) {
                if (cupidPlayerList.contains(dayDeadPlayerList.get(i))) {
                    //殉情关联致死
                    for (Player yPlayer : cupidPlayerList) {
                        yPlayer.isAlive = 0;
                        if (!dayDeadPlayerList.contains(yPlayer)) {
                            dayDeadPlayerList.add(yPlayer);
                            playerListAdapter.clearCheckedWithNotify();
                            break outFor;
                        }
                    }
                }
            }
        }

        //猎人夜晚被杀，白天判断逻辑 标记归回
        hunterState = 0;
        if (isContinue(playerList)) {
            LogUtils.print("投票完毕，游戏继续");
        } else {
            LogUtils.print("投票完毕，游戏结束");
            refresh(null, resultNotice, null);
            buttonControl.setEnabled(false);
            return;
        }
        //守卫守护集合
        guardPlayerList.clear();
        //狼人当夜杀害集合
        wolfKillPlayerList.clear();
        //女巫当夜毒杀集合
        poisonKillPlayerList.clear();
        //昨夜死亡玩家集合
        lastNightDeadPlayerList.clear();

        hunterPlayerKillList.clear();
        //当日第一阶段阵亡集合
        dayFirstDeadPlayerList.clear();
        //当日第二阶段阵亡集合
        daySecondDeadPlayerList.clear();
        //当日死亡总集合
        dayDeadPlayerList.clear();

        imageviewBgNight.setVisibility(View.VISIBLE);
        Keyframe keyframe1 = Keyframe.ofFloat(0.0f, 1.0f);
        Keyframe keyframe2 = Keyframe.ofFloat(1.0f, 0.3f);

        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofKeyframe("alpha", keyframe1, keyframe2);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(imageviewBgNight, propertyValuesHolder);
        objectAnimator.setDuration(2000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                buttonControl.setEnabled(false);
                buttonControl.setText("夜幕之中");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageviewBgNight.setVisibility(View.GONE);
                startNightLogic();
                relativelayoutControlNotice.setVisibility(View.VISIBLE);

            }
        });
        objectAnimator.start();
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 天亮方法，第一阶段（第一阶段与第二阶段的分割主要是因为猎人夜晚是否发生死亡）
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/11 11:40
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    StringBuilder sb;
    public void toDay1() {
        sb = new StringBuilder("天亮了，昨夜");
        state = Integer.MAX_VALUE;
        Player wolfKillPlayer = null;
        Player poisonKillPlayer = null;
        //狼刀杀
        if (wolfKillPlayerList.size() > 0) {
            wolfKillPlayer = wolfKillPlayerList.get(0);

            if (wolfKillPlayer.isAlive <= 0) {
                lastNightDeadPlayerList.add(wolfKillPlayer);
            }
        }

        //女巫毒杀
        if (poisonKillPlayerList.size() > 0) {
            poisonKillPlayer = poisonKillPlayerList.get(0);

            //防止狼刀与毒杀重复添加死亡玩家
            if (poisonKillPlayer.isAlive <= 0 && wolfKillPlayer != poisonKillPlayer) {
                lastNightDeadPlayerList.add(poisonKillPlayer);
            }
        }


        //熊是否咆哮
        boolean isRoar = false;
        boolean isContainBear = false;
        roarFor:
        for (int i = 0; i < controlPlayerSeatList.size(); i++) {
            if (controlPlayerSeatList.get(i).role.icon == R.drawable.ic_role_bear) {
                isContainBear = true;
                Player bearPlayer = controlPlayerSeatList.get(i);
                if (bearPlayer.isAlive <= 0 && !lastNightDeadPlayerList.contains(bearPlayer)) {
                    //熊已经阵亡，并且不是当夜阵亡，所以不需要咆哮
                    return;
                }

                if (bearPlayer.role.skillCanUse) {
                    int preSeat = i;
                    int nextSeat = i;
                    while (true) {
                        preSeat--;
                        preSeat = preSeat >= 0 ? preSeat : controlPlayerSeatList.size() - 1;

                        if (controlPlayerSeatList.get(preSeat).isAlive > 0 || lastNightDeadPlayerList.contains(controlPlayerSeatList.get(preSeat))) {
                            if (controlPlayerSeatList.get(preSeat).role.icon == R.drawable.ic_role_wolf ||
                                    controlPlayerSeatList.get(preSeat).role.icon == R.drawable.ic_role_white_wolf_king) {
                                isRoar = true;
                                break roarFor;
                            }
                        }

                        nextSeat++;
                        nextSeat = nextSeat < controlPlayerSeatList.size() ? nextSeat : 0;
                        if (controlPlayerSeatList.get(nextSeat).isAlive > 0 || lastNightDeadPlayerList.contains(controlPlayerSeatList.get(nextSeat))) {
                            if (controlPlayerSeatList.get(nextSeat).role.icon == R.drawable.ic_role_wolf ||
                                    controlPlayerSeatList.get(nextSeat).role.icon == R.drawable.ic_role_white_wolf_king) {
                                isRoar = true;
                                break roarFor;
                            }
                        }

                        if (preSeat == nextSeat) {
                            break roarFor;
                        }
                    }
                } else {
                    LogUtils.print("熊失去技能，不再咆哮");
                    break;
                }
            }
        }
        if (isContainBear) {
            if (isRoar) {
                sb.append("熊咆哮了;");
            } else {
                sb.append("熊没有咆哮;");
            }
        }

        //野孩子是否变态
        if (wildChildExamplePlayerList.size() > 0 && wildChildExamplePlayerList.get(0).isAlive <= 0) {
            Player wildChildPlayer = null;
            for (int i = 0; i < playerList.size(); i++) {
                if (playerList.get(i).role.icon == R.drawable.ic_role_wild_child) {
                    wildChildPlayer = playerList.get(i);
                    if (wildChildPlayer.isAlive <= 0) {
                        wildChildPlayer = null;
                    }
                    break;
                }
            }

            if (wildChildPlayer != null) {
                //取出现存狼人角色对象
                Role tempRole = null;
                for (Player player : playerList) {
                    if (player.role.camp == Role.CAMP_WOLF && player.role.icon == R.drawable.ic_role_wolf) {
                        tempRole = player.role;
                    }
                }

                if (tempRole != null) {
                    tempRole.count++;
                    wildChildPlayer.role = tempRole;
                }
            }
        }


        showOrHideRoleGroup(isGroup, false);
        //小女孩是否被发现 弹出框
        Player littleGirlPlayer = null;
        for (Player littleGirl : playerList) {
            if (littleGirl.role.icon == R.drawable.ic_role_little_girl) {
                if (littleGirl.isAlive > 0 && littleGirl.role.skillCanUse) {
                    littleGirlPlayer = littleGirl;
                    break;
                }
            }
        }

        if (littleGirlPlayer != null) {
            LittleGirlDialog littleGirlDialog = new LittleGirlDialog(this, R.style.DialogTheme);
            littleGirlDialog.preInit(littleGirlPlayer);

            littleGirlDialog.setCancelable(false);
            littleGirlDialog.setCanceledOnTouchOutside(false);

            final Player finalLittleGirlPlayer = littleGirlPlayer;
            littleGirlDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (finalLittleGirlPlayer.isAlive <= 0) {
                        lastNightDeadPlayerList.add(finalLittleGirlPlayer);
                    }

                    //昨夜死亡玩家中是否存在殉情
                    if (cupidPlayerList.size() > 0) {
                        outFor:
                        for (int i = 0; i < lastNightDeadPlayerList.size(); i++) {
                            if (cupidPlayerList.contains(lastNightDeadPlayerList.get(i))) {
                                //殉情关联致死
                                for (Player yPlayer : cupidPlayerList) {
                                    yPlayer.isAlive = 0;
                                    if (!lastNightDeadPlayerList.contains(yPlayer)) {
                                        lastNightDeadPlayerList.add(yPlayer);
                                        playerListAdapter.clearCheckedWithNotify();
                                        break outFor;
                                    }
                                }

                            }
                        }
                    }

                    boolean hunterPlayerIsAlive = true;
                    //以上角色走完，判断夜晚是否存在猎人死亡，如果存在猎人死亡，则猎人可以选择带走一个人或放弃
                    for (Player hunterPlayer : lastNightDeadPlayerList) {
                        if (hunterPlayer.role.icon == R.drawable.ic_role_hunter) {
                            hunterPlayerIsAlive = false;
                        }
                    }

                    //猎人没有死亡
                    if (hunterPlayerIsAlive) {
                        toDay2(sb);
                    } else {//猎人死亡
                        hunterState = -1;
                        state = R.drawable.ic_role_hunter;
                        refresh("放弃", "天亮了，猎人昨夜死亡，请选择是否带走一名玩家", "击杀");
                    }

                }
            });
            littleGirlDialog.show();
        } else {
            //昨夜死亡玩家中是否存在殉情
            if (cupidPlayerList.size() > 0) {
                outFor:
                for (int i = 0; i < lastNightDeadPlayerList.size(); i++) {
                    if (cupidPlayerList.contains(lastNightDeadPlayerList.get(i))) {
                        //殉情关联致死
                        for (Player yPlayer : cupidPlayerList) {
                            yPlayer.isAlive = 0;
                            if (!lastNightDeadPlayerList.contains(yPlayer)) {
                                lastNightDeadPlayerList.add(yPlayer);
                                playerListAdapter.clearCheckedWithNotify();
                                break outFor;
                            }
                        }
                    }
                }
            }

            boolean hunterPlayerIsAlive = true;
            //以上角色走完，判断夜晚是否存在猎人死亡，如果存在猎人死亡，则猎人可以选择带走一个人或放弃
            for (Player hunterPlayer : lastNightDeadPlayerList) {
                if (hunterPlayer.role.icon == R.drawable.ic_role_hunter) {
                    hunterPlayerIsAlive = false;
                }
            }

            //猎人没有死亡
            if (hunterPlayerIsAlive) {
                toDay2(sb);
            } else {//猎人死亡
                hunterState = -1;
                state = R.drawable.ic_role_hunter;
                refresh("放弃", "天亮了，猎人昨夜死亡，请选择是否带走一名玩家", "击杀");
            }
        }


    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 天亮方法，第二阶段（第一阶段与第二阶段的分割主要是因为猎人夜晚是否发生死亡）
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/11 11:40
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void toDay2(final StringBuilder sb) {
        if (lastNightDeadPlayerList.size() > 0) {
            sb.append(lastNightDeadPlayerList.size() + "位玩家死亡，分别是：");
            for (Player player : lastNightDeadPlayerList) {
                player.role.count = player.role.count - 1;
                sb.append(player.seat + "号;");
                //如果有猎人，则发动技能
            }
        } else {
            sb.append("平安夜");
        }

        if (isContinue(playerList)) {
            LogUtils.print("天亮了，游戏继续");
            for (Player player : playerList) {
                if (player.role.icon == R.drawable.ic_role_white_wolf_king && player.isAlive > 0) {
                    refresh("白狼自爆", sb.toString(), "放逐");
                    playerListAdapter.clearCheckedWithNotify();
                    return;
                }
            }
            refresh(null, sb.toString(), "放逐");
            playerListAdapter.clearCheckedWithNotify();
        } else {
            LogUtils.print("天亮了，游戏结束");
            buttonControl.setEnabled(false);
            refresh(null, resultNotice, null);
            playerListAdapter.clearCheckedWithNotify();
        }
    }


    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 游戏逻辑控制方法
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/12 02:13
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void startNightLogic() {
        controlTask = new ControlTask();
        controlTask.execute();
    }

    public void refresh(String buttonNoNoticeString, String textviewNoticeString, String buttonYesNoticeString) {
        if (buttonNoNoticeString == null) {
            buttonNoNoticeString = "";
            buttonNo.setVisibility(View.INVISIBLE);
        } else {
            buttonNo.setVisibility(View.VISIBLE);
        }

        if (textviewNoticeString == null) {
            textviewNoticeString = "";
            textviewNotice.setVisibility(View.INVISIBLE);
        } else {
            textviewNotice.setVisibility(View.VISIBLE);
        }

        if (buttonYesNoticeString == null) {
            buttonYesNoticeString = "";
            buttonYes.setVisibility(View.INVISIBLE);
        } else {
            buttonYes.setVisibility(View.VISIBLE);
        }

        buttonNo.setText(buttonNoNoticeString);
        textviewNotice.setText(textviewNoticeString);
        buttonYes.setText(buttonYesNoticeString);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        int tempPosition = (int) parent.getItemIdAtPosition(position);
//        if(parent.getAdapter() instanceof HeaderViewListAdapter) {
//            HeaderViewListAdapter listAdapter = (HeaderViewListAdapter)parent.getAdapter();
//            adapter = (PlayerListAdapter) listAdapter.getWrappedAdapter();
//        }else{
//            adapter = ((PlayerListAdapter) parent.getAdapter());
//        }
    }

    public void sortList(List<Player> list, int type) {
        switch (type) {
            case TYPE_CAMP:
                Collections.sort(list, new Comparator<Player>() {
                    /*
                    * int compare(Player o1, Player o2) 返回一个基本类型的整型，
                    * 返回负数表示：o1 小于 o2，
                    * 返回 0 表示：o1和o2相等，
                    * 返回正数表示：o1大于o2。
                    */
                    @Override
                    public int compare(Player o1, Player o2) {
                        //按照玩家headerId进行升序排列
                        if (o1.role.camp > o2.role.camp) {
                            return 1;
                        }
                        if (o1.role.camp == o2.role.camp) {
                            return 0;
                        }
                        return -1;
                    }
                });
                break;
            case TYPE_LEVEL:
                Collections.sort(list, new Comparator<Player>() {
                    /*
                    * int compare(Player o1, Player o2) 返回一个基本类型的整型，
                    * 返回负数表示：o1 小于 o2，
                    * 返回 0 表示：o1和o2相等，
                    * 返回正数表示：o1大于o2。
                    */
                    @Override
                    public int compare(Player o1, Player o2) {
                        //按照玩家操作优先级level进行升序排列
                        if (o1.role.level > o2.role.level) {
                            return 1;
                        }
                        if (o1.role.level == o2.role.level) {
                            return 0;
                        }
                        return -1;
                    }
                });
                break;
            case TYPE_SEAT:
                Collections.sort(list, new Comparator<Player>() {
                    /*
                    * int compare(Player o1, Player o2) 返回一个基本类型的整型，
                    * 返回负数表示：o1 小于 o2，
                    * 返回 0 表示：o1和o2相等，
                    * 返回正数表示：o1大于o2。
                    */
                    @Override
                    public int compare(Player o1, Player o2) {
                        //按照玩家操作优先级level进行升序排列
                        if (o1.seat > o2.seat) {
                            return 1;
                        }
                        if (o1.seat == o2.seat) {
                            return 0;
                        }
                        return -1;
                    }
                });
                break;
        }
    }


    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 子线程与主线程协同控制夜晚操作
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/13 00:22
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    private class ControlTask extends AsyncTask<Void, Player, Void> {

        @Override
        protected void onPreExecute() {

            controlPlayerLevelList = new ArrayList<>();
            controlPlayerSeatList = new ArrayList<>();

            controlPlayerLevelList.addAll(playerList);
            controlPlayerSeatList.addAll(playerList);

            //按照夜晚操作优先级排序
            sortList(controlPlayerLevelList, TYPE_LEVEL);
            //按照座位号排序
            sortList(controlPlayerSeatList, TYPE_SEAT);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //如果遍历时，角色出现重复，比如狼人角色，则只操作一次，所以临时记录上次的角色对象
            int preRoleIcon = Integer.MIN_VALUE;
            //除了白狼王，是否有其他狼人
            boolean isHaveOtherWolf;
            //按照操作优先级从高到低进行遍历操作
            levelFor:
            for (int i = controlPlayerLevelList.size() - 1; i >= 0; i--) {

                //如果盗贼切换了角色，那么优先级列表进行重新排序，则需要从头遍历，反之，盗贼没有切换角色，则继续下一位角色操作
                if (isChange) {
                    i++;
                    isChange = false;
                }

                Player player = controlPlayerLevelList.get(i);
                LogUtils.print(player.role.desc);

                //如果是夜晚不用睁眼的角色，则直接跳过操作
                if (player.role.isFirstNightControl == 0) {
                    continue;
                }

                //如果只是第一夜需要睁眼，并且当前不是第一夜，则跳过。
                if (player.role.isFirstNightControl == 1 && isFirstNight != 1) {
                    continue;
                }

                //这一次和上一次是否为同一个角色，true则跳过
                if (player.role.icon == preRoleIcon) {
                    continue;
                }

                //技能已无法使用，则跳过
                if (!player.role.skillCanUse) {
                    continue;
                }

                if (player.role.camp != Role.CAMP_PROTOSS && (preRoleIcon == R.drawable.ic_role_wolf || preRoleIcon == R.drawable.ic_role_white_wolf_king)) {
                    //有其他狼人时，白狼王夜晚不会与其他狼人分开行动，所以，跳过
                    //没有其他狼人时，白狼王单独行动
                    continue;
                }


                preRoleIcon = player.role.icon;

                haveDone = false;
                publishProgress(player);
                //如果当前角色还有操作未完成，则等待
                while (!haveDone) {
                    try {
                        //当Activity退出时，终止该子线程
                        if (GameActivity.this.isFinishing()) {
                            break levelFor;
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                    }
                }

//                if(GameActivity.this.isFinishing()){
//                    break levelFor;
//                }
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Player... players) {
            doNightNext(players[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refresh("狼人自爆", "天亮了", "投票");
            buttonControl.setText("天亮了");
            LogUtils.print("第" + isFirstNight + "夜操作完成");
            isFirstNight++;
            //准备天亮
            toDay1();
        }
    }


    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.logic
     * FUNCTIONAL_DESCRIPTION: 夜晚操作控制
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/12 13:29
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void doNightNext(Player player) {
        currentPlayer = player;
        state = player.role.icon;
        switch (player.role.icon) {
            case R.drawable.ic_role_rogue://盗贼
                rogue(player);
                break;
            case R.drawable.ic_role_wild_child://野孩子
                wildChild(player);
                break;
            case R.drawable.ic_role_cupid://丘比特
                cupid(player);
                break;
            case R.drawable.ic_role_guard://守卫
                guard(player);
                break;
            case R.drawable.ic_role_seer://预言家
                seer(player);
                break;
            case R.drawable.ic_role_white_wolf_king://白狼王
            case R.drawable.ic_role_wolf://狼人
                wolf(player);
                break;
            case R.drawable.ic_role_witch://女巫
                //0为解药环节
                //1为毒药环节
                witch(player, 0);
                break;
            default:
                haveDone = true;
                break;
        }
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.logic
     * FUNCTIONAL_DESCRIPTION: 白天操作控制
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/12 13:29
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void doDayNext(Player player) {
        switch (player.role.icon) {
            case R.drawable.ic_role_hunter:

                break;
        }
    }

    //盗贼 Level 1150
    public void rogue(Player player) {
        RogueChoiceDialog rogueChoiceDialog = new RogueChoiceDialog(this, R.style.DialogTheme);
        rogueChoiceDialog.initList(rogueRoleList, controlPlayerLevelList);

        rogueChoiceDialog.setCancelable(false);
        rogueChoiceDialog.setCanceledOnTouchOutside(false);
        rogueChoiceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isChange) {
                    //盗贼切换了角色，则进行集合操作，即：玩家优先级列表进行重新排序
                    sortList(controlPlayerLevelList, TYPE_LEVEL);

                    showOrHideRoleGroup(isGroup, false);
                    playerListAdapter.clearCheckedWithNotify();
                }

                printPlayerList(controlPlayerLevelList);
                LogUtils.print("替换卡牌区：" + rogueRoleList.get(0).desc + " , " + rogueRoleList.get(1).desc);
                haveDone = true;
            }
        });
        rogueChoiceDialog.show();
    }


    //野孩子 Level 1140
    public void wildChild(Player player) {
        refresh(null, player.role.notice, "确定");
    }

    //丘比特 Level 1120
    public void cupid(Player player) {
        refresh("放弃", player.role.notice, "连接");
    }

    //守卫 Level 1110
    public void guard(Player player) {
        refresh(null, player.role.notice, "守护");
    }

    //猎人 Level 1100
    public void hunter(Player player) {

    }

    //熊 Level 1090
    //长老 Level 1080
    //白痴神 Level 1070
    //预言家 Level 1060
    public void seer(Player player) {
        String notice = null;
        if (player.isAlive > 0) {
            notice = player.role.notice;
        } else {
            notice = "预言家已经阵亡，只需宣读验人流程，不需要真正验人";
        }
        refresh(null, notice, "完毕");
    }

    //替罪羊 Level 1050
    //白狼 Level 1040
    //狼 Level 1020
    public void wolf(Player player) {
        refresh(null, player.role.notice, "杀害");
    }

    //女巫 Level 1010
    public void witch(Player player, int type) {
        antidoteOrPoison = type;
        String right = null;
        String center = null;
        String left = "放弃";
        switch (type) {
            case 0://解药
                if (player.role.antidote >= 1) {
                    right = "救人";
                    center = "昨夜狼刀的是他：" + "(手势)" + wolfKillPlayerList.get(0).seat + "号玩家, 是否使用解药";
                } else {
                    center = "昨夜狼刀的是他：" + "(无需手势告知)" + "号玩家, 是否使用解药";
                }

                break;
            case 1://毒药
                if (player.role.poison >= 1) {
                    right = "毒杀";
                }

                center = "是否使用毒药";
                break;
        }

        refresh(left, center, right);
    }
    //村民 Level 1000

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.logic
     * FUNCTIONAL_DESCRIPTION: 游戏是否继续
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/12 17:48
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public boolean isContinue(ArrayList<Player> list) {
        boolean isContinue = true;
        debugPrint();

        int civilian = 0;
        int protoss = 0;
        int wolf = 0;
        int third = 0;

        //注意，count: 此处村民玩家角色数量为叠加状态，神民为非叠加状态，普通狼人为叠加状态，带技能狼人为非叠加状态，第三方阵营为叠加状态
        for (int i = 0; i < list.size(); i++) {
            Player player = list.get(i);
            switch (player.role.camp) {
                case Role.CAMP_CIVILIAN:
                    civilian = player.role.count;
                    break;
                case Role.CAMP_PROTOSS:
                    protoss += player.role.count;
                    break;
                case Role.CAMP_WOLF:
                    if (player.role.icon == R.drawable.ic_role_wolf) {
                        wolf = player.role.count;
                    } else {
                        wolf += player.role.count;
                    }
                    break;
                case Role.CAMP_THIRD:
                    third = player.role.count;
                    break;
            }
        }

        if (third == 0) {
            if (civilian == 0 || protoss == 0 || wolf == 0) {
                if (wolf == 0) {
                    resultNotice = "游戏结束，平(神)民获胜";
                } else if (civilian == 0 || protoss == 0) {
                    resultNotice = "游戏结束，狼人获胜";
                }

                isContinue = false;
            }
        } else if (third != 0) {
            if (civilian == 0 && protoss == 0 && wolf == 0) {
                resultNotice = "游戏结束，第三方阵营获胜";
                isContinue = false;
            }
        }
        return isContinue;
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.activity
     * FUNCTIONAL_DESCRIPTION: 当法官进行夜晚操作时，如果在操作过程中切换玩家列表显示方式，则可能出现逻辑错误，需要清除本次的切换前的集合操作
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/14 09:49
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void clearCurrentControlList(int state, boolean isNeedClearList) {
        switch (state) {
            case R.drawable.ic_role_wild_child://野孩子
                if(isNeedClearList){
                    wildChildExamplePlayerList.clear();
                }

                playerListAdapter.clearCheckedWithNotify(false);
                break;
            case R.drawable.ic_role_cupid://丘比特
                if(isNeedClearList){
                    cupidPlayerList.clear();
                }

                playerListAdapter.clearCheckedWithNotify(false);
                break;
            case R.drawable.ic_role_guard://守卫
                if(isNeedClearList){
                    guardPlayerList.clear();
                }

                playerListAdapter.clearCheckedWithNotify(false);
                break;
            case R.drawable.ic_role_wolf://狼人
            case R.drawable.ic_role_white_wolf_king:
                if(isNeedClearList){
                    wolfKillPlayerList.clear();
                }

                playerListAdapter.clearCheckedWithNotify(false);
                break;
        }
    }

    /**
     * PACKAGE_NAME: com.z.wolfkill.home.main.logic
     * FUNCTIONAL_DESCRIPTION: 打印当前玩家集合角色
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/12 17:42
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void printPlayerList(ArrayList<Player> list) {
        StringBuilder sb = new StringBuilder("Player Level List:\n");
        for (int i = list.size() - 1; i >= 0; i--) {
            Player player = list.get(i);
            sb.append("Role:" + player.role.desc + ", Level:" + player.role.level + "\n");
        }
        LogUtils.print("替换后玩家角色：" + sb.toString());
    }

    @OnClick({R.id.button_back, R.id.button_control, R.id.button_yes, R.id.button_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.button_control:
                toNight();
                break;
            case R.id.button_yes:
                switch (state) {
                    case R.drawable.ic_role_wild_child://野孩子
                        if (wildChildExamplePlayerList.size() < 1) {
                            ToastUtil.showToast(this, "请选择一名玩家作为榜样");
                            return;
                        }

                        if (wildChildExamplePlayerList.get(0).role.icon == R.drawable.ic_role_wild_child) {
                            ToastUtil.showToast(this, "不能选择自己作为榜样");
                            return;
                        }
                        playerListAdapter.clearCheckedWithNotify();
                        break;
                    case R.drawable.ic_role_little_girl://小女孩
                        break;
                    case R.drawable.ic_role_cupid://丘比特
                        if (cupidPlayerList.size() < 2) {
                            ToastUtil.showToast(this, "请选择两名玩家进行连接");
                            return;
                        }
                        //判断连接的两名玩家状态，决定是否改变阵营
                        Player p1 = cupidPlayerList.get(0);
                        Player p2 = cupidPlayerList.get(1);

                        if (p1.role.camp != p2.role.camp) {
                            if ((p1.role.camp != Role.CAMP_WOLF && p2.role.camp != Role.CAMP_WOLF) || (p1.role.camp == Role.CAMP_WOLF && p2.role.camp == Role.CAMP_WOLF)) {
                                playerListAdapter.clearCheckedWithNotify();
                                LogUtils.print("连接玩家维持原阵营");
                            } else {
                                p1.role.camp = Role.CAMP_THIRD;
                                p2.role.camp = Role.CAMP_THIRD;

                                //该第三方阵营人数必定为2
                                p1.role.count = 2;
                                p2.role.count = 2;

                                //此处不需要刷新，下一句话会刷新，否则刷新两遍造成浪费
                                showOrHideRoleGroup(isGroup, false);
                                LogUtils.print("连接玩家结为第三阵营");
                            }
                        } else {
                            playerListAdapter.clearCheckedWithNotify();
                            LogUtils.print("连接玩家维持原阵营");
                        }

                        break;
                    case R.drawable.ic_role_guard://守卫
                        if (guardPlayerList.size() < 1) {
                            ToastUtil.showToast(this, "请选择一名玩家作为守护目标");
                            return;
                        }

                        if (curGuardPlayer != null && curGuardPlayer == guardPlayerList.get(0)) {
                            ToastUtil.showToast(this, "不能连夜守护同一个目标");
                            return;
                        }
                        curGuardPlayer = guardPlayerList.get(0);

                        playerListAdapter.clearCheckedWithNotify();
                        break;
                    case R.drawable.ic_role_hunter://猎人
                        if(hunterState != 0){
                            if (hunterPlayerKillList.size() < 1) {
                                ToastUtil.showToast(this, "请选择一名要击杀的玩家");
                                return;
                            }

                            Player killPlayer = hunterPlayerKillList.get(0);
                            killPlayer.role.count = killPlayer.role.count - 1;
                            killPlayer.isAlive = 0;

                            playerListAdapter.clearCheckedWithNotify();

                            state = Integer.MAX_VALUE;
                            hunterState = 0;
                            toDay2(sb);
                        }
                        break;
                    case R.drawable.ic_role_seer://预言家
                        break;
                    case R.drawable.ic_role_white_wolf_king://白狼王
                    case R.drawable.ic_role_wolf://狼人
                        if (wolfKillPlayerList.size() < 1) {
                            ToastUtil.showToast(this, "请选择一名要杀害的玩家");
                            return;
                        }

                        if ((guardPlayerList.size() > 0 && wolfKillPlayerList.get(0) != guardPlayerList.get(0)) || guardPlayerList.size() <= 0) {
                            Player killPlayer = wolfKillPlayerList.get(0);
                            killPlayer.isAlive = killPlayer.isAlive - 1;
                        }

                        playerListAdapter.clearCheckedWithNotify();
                        break;
                    case R.drawable.ic_role_witch://女巫
                        if (antidoteOrPoison == 0) {
                            LogUtils.print("解药阶段");

                            Player savePlayer = wolfKillPlayerList.get(0);
                            savePlayer.isAlive = 1;

                            currentPlayer.role.antidote = currentPlayer.role.antidote - 1;

                            playerListAdapter.clearCheckedWithNotify();
                            witch(currentPlayer, 1);
                            return;
                        }

                        if (antidoteOrPoison == 1) {
                            LogUtils.print("毒药阶段");
                            if (poisonKillPlayerList.size() < 1) {
                                ToastUtil.showToast(this, "请选择一名玩家作为毒杀目标");
                                return;
                            }
                            Player poisonPlayer = poisonKillPlayerList.get(0);
                            poisonPlayer.isAlive = 0;

                            currentPlayer.role.poison = currentPlayer.role.poison - 1;

                            poisonKillPlayerList.add(poisonPlayer);
                            playerListAdapter.clearCheckedWithNotify();
                        }
                        break;
                    case Integer.MAX_VALUE://白天状态阶段 第一阶段 放逐
                        if (dayFirstDeadPlayerList.size() < 1) {
                            ToastUtil.showToast(this, "请选择一名玩家进行放逐");
                            return;
                        }

                        //如果是白狼王自爆到白天第二阶段，则标记中间的TextView为F
                        textviewNotice.setTag("F");

                        Player vPlayer = dayFirstDeadPlayerList.get(0);
                        vPlayer.role.count = vPlayer.role.count - 1;
                        vPlayer.isAlive = 0;

                        dayDeadPlayerList.add(vPlayer);

                        switch (vPlayer.role.icon) {
                            case R.drawable.ic_role_elder:
                                LogUtils.print("长老被放逐，发动技能，所有神民失去技能");
                                for (Player player : playerList) {
                                    if (player.role.icon != R.drawable.ic_role_wolf && player.role.icon != R.drawable.ic_role_white_wolf_king) {
                                        player.role.skillCanUse = false;
                                    }
                                }
                                ToastUtil.showToast(this, "长老被放逐，发动技能，所有神民失去技能");
                                break;
                            case R.drawable.ic_role_hunter:
                                state = Integer.MIN_VALUE;
                                refresh("放弃", "猎人发动技能，请选择带走一名玩家或放弃", "带走");
                                return;
                        }

                        playerListAdapter.clearCheckedWithNotify();
//                        buttonControl.setEnabled(true);
                        toNight();
                        break;
                    case Integer.MIN_VALUE://白天状态阶段 第二阶段 是否带走其他玩家
                        Player killPlayer;

                        String tag = textviewNotice.getTag().toString();

                        if (tag.equals("F")) {//猎人逻辑
                            if (daySecondDeadPlayerList.size() < 1) {
                                ToastUtil.showToast(this, "猎人请选择一名玩家进行击杀");
                                return;
                            }

                            killPlayer = daySecondDeadPlayerList.get(0);
                            killPlayer.role.count = killPlayer.role.count - 1;
                            killPlayer.isAlive = 0;

                            dayDeadPlayerList.add(killPlayer);

                        } else if (tag.equals("K")) {//白狼逻辑
                            if (daySecondDeadPlayerList.size() < 1) {
                                ToastUtil.showToast(this, "白狼王请选择一名玩家进行击杀");
                                return;
                            }

                            killPlayer = daySecondDeadPlayerList.get(0);
                            killPlayer.role.count = killPlayer.role.count - 1;
                            killPlayer.isAlive = 0;

                            dayDeadPlayerList.add(killPlayer);

                            //白狼王自爆，爆中了猎人
                            if (killPlayer.role.icon == R.drawable.ic_role_hunter) {
                                textviewNotice.setTag("F");
                                refresh("放弃", "猎人发动技能，请选择带走一名玩家或放弃", "带走");
                                daySecondDeadPlayerList.clear();
                                playerListAdapter.clearCheckedWithNotify();
                                return;
                            }
                        }
//                        buttonControl.setEnabled(true);
                        toNight();
                        break;
                }
                LogUtils.print("YES");
                haveDone = true;
                break;
            case R.id.button_no:
                switch (state) {
                    case R.drawable.ic_role_witch:
                        if (antidoteOrPoison == 0) {
                            witch(currentPlayer, 1);
                            LogUtils.print("解药阶段");
                            return;
                        }
                        //如果放弃使用毒药需要清除刚才的选择
                        poisonKillPlayerList.clear();
                        LogUtils.print("毒药阶段");
                        break;
                    case R.drawable.ic_role_cupid://丘比特，放弃连接，清空刚才点选的玩家
                        cupidPlayerList.clear();
                        break;
                    case R.drawable.ic_role_hunter://猎人
                        state = Integer.MAX_VALUE;
                        hunterState = 0;
                        toDay2(sb);
                        break;
                    case Integer.MAX_VALUE://白天状态阶段 第一阶段 白狼自爆
                        //如果是白狼王自爆到白天第二阶段，则标记中间的TextView为K
                        textviewNotice.setTag("K");
                        //选择带走一人
                        Player wolfKingPlayer = null;
                        for (Player player : playerList) {
                            if (player.role.icon == R.drawable.ic_role_white_wolf_king && player.isAlive > 0) {
                                wolfKingPlayer = player;
                                break;
                            }
                        }

                        if (wolfKingPlayer != null) {
                            wolfKingPlayer.isAlive = 0;
                            wolfKingPlayer.role.count = wolfKingPlayer.role.count - 1;
                        }

                        dayDeadPlayerList.add(wolfKingPlayer);
                        state = Integer.MIN_VALUE;
                        refresh("放弃", "白狼王发动技能，请选择带走一名玩家或放弃", "带走");
                        playerListAdapter.clearCheckedWithNotify();
                        return;

                    case Integer.MIN_VALUE://白天状态阶段 第二阶段 是否带走其他玩家
//                        buttonControl.setEnabled(true);
                        toNight();
                        break;
                }
                LogUtils.print("NO");

                playerListAdapter.clearCheckedWithNotify();
                haveDone = true;
                break;
        }
    }

    public void debugPrint() {
        StringBuilder sb = new StringBuilder("玩家状态:");
        for (Player player : playerList) {
            sb.append("玩家座位号：" + player.seat + ", 玩家生命值：" + player.isAlive + ", 玩家角色：" + player.role.desc + ", 玩家阵营：" + player.role.camp + ", 玩家所在阵营角色数量：" + player.role.count + "\n");
        }
        LogUtils.print(sb.toString());
    }

}