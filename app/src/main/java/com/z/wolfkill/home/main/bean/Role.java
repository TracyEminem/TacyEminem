package com.z.wolfkill.home.main.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.z.wolfkill.R;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.bean
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/8 14:31
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class Role implements Parcelable {
    //平民阵营
    public static final int CAMP_CIVILIAN = 0;
    //神民阵营
    public static final int CAMP_PROTOSS = 1;
    //狼人阵营
    public static final int CAMP_WOLF = 2;
    //第三阵营
    public static final int CAMP_THIRD = 3;
    //第四阵营
    public static final int CAMP_FOURTH = 4;

    //角色图标
    public int icon;
    //角色描述
    public String desc;
    //角色数量
    public int count;
    //角色夜晚操作优先级
    public int level;
    //角色数量限制 0为默认不限制
    public int limitCount;
    //角色阵营
    public int camp;
    //角色夜晚操作提示
    public String notice;

    //女巫解药数量
    public int antidote;
    //女巫毒药数量
    public int poison;

    //该角色是否只需要第一晚操作 0为夜晚不需要睁眼，1为只第一页睁眼，2为每夜都睁眼
    public int isFirstNightControl;

    //因为存在长老，所以技能是否可用
    public boolean skillCanUse;


    public Role(int icon, String desc, int count, int level, int limitCount, int camp, String notice, int isFirstNightControl) {
        this.icon = icon;
        this.desc = desc;
        this.count = count;
        this.level = level;
        this.limitCount = limitCount;
        this.camp = camp;
        this.notice = notice;
        this.isFirstNightControl = isFirstNightControl;
        if(icon == R.drawable.ic_role_witch){
            antidote = 1;
            poison = 1;
        }
        skillCanUse = true;
    }

    protected Role(Parcel in) {
        icon = in.readInt();
        desc = in.readString();
        count = in.readInt();
        level = in.readInt();
        limitCount = in.readInt();
        camp = in.readInt();
        notice = in.readString();
        antidote = in.readInt();
        poison = in.readInt();
        isFirstNightControl = in.readInt();
        skillCanUse = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(desc);
        dest.writeInt(count);
        dest.writeInt(level);
        dest.writeInt(limitCount);
        dest.writeInt(camp);
        dest.writeString(notice);
        dest.writeInt(antidote);
        dest.writeInt(poison);
        dest.writeInt(isFirstNightControl);
        dest.writeByte((byte) (skillCanUse ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Role> CREATOR = new Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel in) {
            return new Role(in);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };
}
