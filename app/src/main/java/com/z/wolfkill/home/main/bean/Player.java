package com.z.wolfkill.home.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.bean
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/8 17:20
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class Player implements Parcelable {
    //角色信息
    public Role role;
    //是否已准备（即查看过角色）
    public boolean isReady;
    //玩家名称
    public String name;
    //玩家头像
    public int avatar;
    //玩家座位号
    public int seat;
    //是否显示角色描述
    public boolean isShow;
    //是否存活 0为为存活 1为一滴血，2为2滴血，例如长老
    public int isAlive = 0;

    public Player(Role role, int isAlive) {
        this.isReady = false;
        this.role = role;
        this.isAlive = isAlive;
    }

    protected Player(Parcel in) {
        role = in.readParcelable(Role.class.getClassLoader());
        isReady = in.readByte() != 0;
        name = in.readString();
        avatar = in.readInt();
        seat = in.readInt();
        isShow = in.readByte() != 0;
        isAlive = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(role, flags);
        dest.writeByte((byte) (isReady ? 1 : 0));
        dest.writeString(name);
        dest.writeInt(avatar);
        dest.writeInt(seat);
        dest.writeByte((byte) (isShow ? 1 : 0));
        dest.writeInt(isAlive);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
