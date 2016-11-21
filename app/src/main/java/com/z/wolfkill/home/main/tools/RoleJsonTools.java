package com.z.wolfkill.home.main.tools;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.bean
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/8 15:24
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class RoleJsonTools {
    List<RoleJsonToolsBean> list;

    @JSONField(name="roles")
    public List<RoleJsonToolsBean> getList() {
        return list;
    }

    public void setList(List<RoleJsonToolsBean> list) {
        this.list = list;
    }
}
