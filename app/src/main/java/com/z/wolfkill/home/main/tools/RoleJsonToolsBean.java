package com.z.wolfkill.home.main.tools;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.bean
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/8 15:31
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class RoleJsonToolsBean {
    public String icon_name;
    public String desc;
    public int count;
    public int level;
    public int limit_count;
    public int camp;
    public String notice;
    public int is_first_night_control;

    public RoleJsonToolsBean(String icon_name, String desc, int count, int level, int limit_count, int camp, String notice, int is_first_night_control) {
        this.icon_name = icon_name;
        this.desc = desc;
        this.count = count;
        this.level = level;
        this.limit_count = limit_count;
        this.camp = camp;
        this.notice = notice;
        this.is_first_night_control = is_first_night_control;
    }

    @JSONField(name="camp")
    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    @JSONField(name="limit_count")
    public int getLimit_count() {
        return limit_count;
    }

    public void setLimit_count(int limit_count) {
        this.limit_count = limit_count;
    }

    @JSONField(name="icon_name")
    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    @JSONField(name="desc")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JSONField(name="count")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @JSONField(name="level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JSONField(name="notice")
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }


    @JSONField(name="is_first_night_control")
    public int getIs_frist_night_control() {
        return is_first_night_control;
    }

    public void setIs_frist_night_control(int is_first_night_control) {
        this.is_first_night_control = is_first_night_control;
    }
}
