package com.z.wolfkill.home.main.tools;

import com.z.wolfkill.common.parser.JsonUtils;
import com.z.wolfkill.common.utils.LogUtils;
import com.z.wolfkill.home.main.bean.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.tools
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/8 15:52
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class JsonTools {
    /**
     * 游戏工具类，生成角色配置菜单
     */
    public void optionRoleJson(){
        List<RoleJsonToolsBean> list = new ArrayList<>();

        RoleJsonToolsBean role = new RoleJsonToolsBean("ic_role_bear", "熊", 0, 1090, 1, Role.CAMP_PROTOSS, "熊请睁眼", 0);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_cupid", "丘比特", 0, 1120, 1, Role.CAMP_PROTOSS, "丘比特请睁眼，丘比特请选择连接玩家", 1);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_elder", "长老", 0, 1080, 1, Role.CAMP_CIVILIAN, "长老请睁眼", 0);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_fool", "白痴", 0, 1070, 1, Role.CAMP_PROTOSS, "白痴神请睁眼", 0);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_guard", "守卫", 0, 1110, 1, Role.CAMP_PROTOSS, "守卫请睁眼", 2);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_hunter", "猎人", 0, 1100, 1, Role.CAMP_PROTOSS, "猎人请睁眼", 0);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_little_girl", "小女孩", 0, 1130, 1, Role.CAMP_PROTOSS, "小女孩请睁眼", 2);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_white_wolf_king", "白狼王", 0, 1040, 1, Role.CAMP_WOLF, "白狼王请睁眼", 2);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_rogue", "盗贼", 0, 1150, 1, Role.CAMP_PROTOSS, "盗贼请睁眼，盗贼请选择角色牌", 1);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_seer", "预言家", 0, 1060, 1, Role.CAMP_PROTOSS, "预言家请睁眼，预言家请验人", 2);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_villager", "村民", 0, 1000, 0, Role.CAMP_CIVILIAN, "", 0);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_whipping_boy", "替罪羊", 0, 1050, 1, Role.CAMP_PROTOSS, "替罪羊请睁眼", 0);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_wild_child", "野孩子", 0, 1140, 1, Role.CAMP_PROTOSS, "野孩子请睁眼，野孩子请选择榜样", 1);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_witch", "女巫", 0, 1010, 1, Role.CAMP_PROTOSS, "女巫请睁眼", 2);
        list.add(role);

        role = new RoleJsonToolsBean("ic_role_wolf", "狼人", 0, 1020, 0, Role.CAMP_WOLF, "狼人请睁眼", 2);
        list.add(role);

        RoleJsonTools roleJsonTools = new RoleJsonTools();
        roleJsonTools.setList(list);

        try {
            String string = JsonUtils.createJsonString(roleJsonTools);
            LogUtils.print(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
