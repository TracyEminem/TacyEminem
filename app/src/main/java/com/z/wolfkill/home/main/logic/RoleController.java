//package com.z.wolfkill.home.main.logic;
//
//import android.content.DialogInterface;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.z.wolfkill.R;
//import com.z.wolfkill.common.utils.LogUtils;
//import com.z.wolfkill.home.main.activity.GameActivity;
//import com.z.wolfkill.home.main.bean.Player;
//import com.z.wolfkill.home.main.bean.Role;
//import com.z.wolfkill.home.main.dialog.RogueChoiceDialog;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
///**
// * PROJECT_NAME: WolfKill 暂时废弃
// * PACKAGE_NAME: com.z.wolfkill.home.main.logic
// * FUNCTIONAL_DESCRIPTION:
// * CREATE_BY: 尽际
// * CREATE_TIME: 2016/11/12 13:28
// * MODIFICATORY_DESCRIPTION:
// * MODIFY_BY:
// * MODIFICATORY_TIME:
// */
//
//public class RoleController {
//    //某一个角色是否操作完毕
//    public boolean haveDone;
//    //当前操作状态，例如：狼人操作？丘比特操作?
//    public int state;
//    public boolean isFirstNight;
//    private GameActivity activity;
//    private static RoleController instantce;
//
//    //盗贼双牌选择列表
//    ArrayList<Role> rogueRoleList;
//
//    //游戏开局玩家优先级排序List
//    ArrayList<Player> controlPlayerLevelList;
//    //玩家座位号排序List
//    ArrayList<Player> controlPlayerSeatList;
//
//    private RoleController(){}
//
//    public static RoleController getInstantce(){
//        if(instantce == null){
//            instantce = new RoleController();
//        }
//        return instantce;
//    }
//
//    public void init(GameActivity activity, ArrayList<Role> rogueRoleList, ArrayList<Player> controlPlayerLevelList, ArrayList<Player> controlPlaytSeatList){
//        this.activity = activity;
//        this.rogueRoleList = rogueRoleList;
//        this.controlPlayerLevelList = controlPlayerLevelList;
//        this.controlPlayerSeatList = controlPlaytSeatList;
//        isFirstNight = true;
//    }
//    /**
//     *
//     * PACKAGE_NAME: com.z.wolfkill.home.main.logic
//     * FUNCTIONAL_DESCRIPTION: 盗贼逻辑
//     * CREATE_BY: 尽际
//     * CREATE_TIME: 2016/11/12 13:29
//     * MODIFICATORY_DESCRIPTION:
//     * MODIFY_BY:
//     * MODIFICATORY_TIME:
//     */
//    public void doNext(Role role){
//        haveDone = false;
//        switch (role.icon){
//            case R.drawable.ic_role_rogue://盗贼
//                rogue(role);
//                break;
//            case R.drawable.ic_role_cupid://丘比特
//                cupid(role);
//                break;
//        }
//    }
//
//    //盗贼 Level 1150
//    public void rogue(final Role role){
//        RogueChoiceDialog rogueChoiceDialog = new RogueChoiceDialog(activity, R.style.DialogTheme);
//        rogueChoiceDialog.initList(rogueRoleList, controlPlayerLevelList);
//
//        rogueChoiceDialog.setCancelable(false);
//        rogueChoiceDialog.setCanceledOnTouchOutside(false);
//        rogueChoiceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if(activity.isChange){
//                    //盗贼切换了角色，则进行集合操作，即：玩家优先级列表进行重新排序
//                    Collections.sort(controlPlayerLevelList, new Comparator<Player>() {
//                        /*
//                        * int compare(Player o1, Player o2) 返回一个基本类型的整型，
//                        * 返回负数表示：o1 小于 o2，
//                        * 返回 0 表示：o1和o2相等，
//                        * 返回正数表示：o1大于o2。
//                        */
//                        @Override
//                        public int compare(Player o1, Player o2) {
//                            //按照玩家操作优先级level进行升序排列
//                            if (o1.role.level > o2.role.level) {
//                                return 1;
//                            }
//                            if (o1.role.level == o2.role.level) {
//                                return 0;
//                            }
//                            return -1;
//                        }
//                    });
//                }
//
//                printPlayerList(controlPlayerLevelList);
//                LogUtils.print("替换卡牌区：" + rogueRoleList.get(0).desc + " , " + rogueRoleList.get(1).desc);
//                haveDone = true;
//            }
//        });
//        rogueChoiceDialog.show();
//    }
//
//    //野孩子 Level 1140
//    //小女孩 Level 1130
//    //丘比特 Level 1120
//    public void cupid(Role role){
//        activity.refresh("放弃", "丘比特请选择连接玩家", "连接");
//        state = role.icon;
//
//    }
//    //守卫 Level 1110
//    //猎人 Level 1100
//    //熊 Level 1090
//    //长老 Level 1080
//    //白痴神 Level 1070
//    //预言家 Level 1060
//    //替罪羊 Level 1050
//    //白狼 Level 1040
//    //狼 Level 1020
//    //女巫 Level 1010
//    //村民 Level 1000
//
//    /**
//     *
//     * PACKAGE_NAME: com.z.wolfkill.home.main.logic
//     * FUNCTIONAL_DESCRIPTION: 游戏是否继续
//     * CREATE_BY: 尽际
//     * CREATE_TIME: 2016/11/12 17:48
//     * MODIFICATORY_DESCRIPTION:
//     * MODIFY_BY:
//     * MODIFICATORY_TIME:
//     */
//    public boolean isContinue(ArrayList<Player> list){
//        boolean isContinue = true;
//
//        int civilian = 0;
//        int protoss = 0;
//        int wolf = 0;
//        int third = 0;
//
//        for(int i = 0; i < list.size(); i++){
//            Player player = list.get(i);
//            switch (player.role.camp){
//                case Role.CAMP_CIVILIAN:
//                    civilian = player.role.count;
//                    break;
//                case Role.CAMP_PROTOSS:
//                    protoss = player.role.count;
//                    break;
//                case Role.CAMP_WOLF:
//                    wolf = player.role.count;
//                    break;
//                case Role.CAMP_THIRD:
//                    third = player.role.count;
//                    break;
//            }
//        }
//
//        if(third == 0){
//            if(civilian == 0 || protoss == 0 || wolf == 0){
//                isContinue = false;
//            }
//        }else if(third != 0){
//            if(civilian == 0 && protoss == 0 && wolf == 0){
//                isContinue = false;
//            }
//        }
//        return isContinue;
//    }
//
//
//    /**
//     *
//     * PACKAGE_NAME: com.z.wolfkill.home.main.logic
//     * FUNCTIONAL_DESCRIPTION: 打印当前玩家集合角色
//     * CREATE_BY: 尽际
//     * CREATE_TIME: 2016/11/12 17:42
//     * MODIFICATORY_DESCRIPTION:
//     * MODIFY_BY:
//     * MODIFICATORY_TIME:
//     */
//    public void printPlayerList(ArrayList<Player> list){
//        StringBuilder sb = new StringBuilder("Player Level List:\n");
//        for(int i = list.size() - 1; i >= 0; i--){
//            Player player = list.get(i);
//            sb.append("Role:" + player.role.desc + ", Level:" + player.role.level + "\n");
//        }
//        LogUtils.print(sb.toString());
//    }
//}
