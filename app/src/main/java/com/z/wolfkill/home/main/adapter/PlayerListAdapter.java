package com.z.wolfkill.home.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.z.wolfkill.R;
import com.z.wolfkill.common.utils.LogUtils;
import com.z.wolfkill.common.utils.ToastUtil;
import com.z.wolfkill.home.main.activity.GameActivity;
import com.z.wolfkill.home.main.bean.Player;
import com.z.wolfkill.home.main.bean.Role;
import com.z.wolfkill.home.main.dialog.SeerDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.adapter
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/10 09:28
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class PlayerListAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    GameActivity activity;
    //玩家集合
    ArrayList<Player> playerList;
    //丘比特连人集合
    ArrayList<Player> cupidPlayerList;
    //野孩子榜样集合
    ArrayList<Player> wildChildPlayerList;
    //守卫守护目标集合
    ArrayList<Player> guardPlayerList;
    //狼人当夜杀害集合
    ArrayList<Player> wolfKillPlayerList;
    //女巫当夜毒杀集合
    ArrayList<Player> poisonKillPlayerList;


    ArrayList<Player> hunterPlayerKillList;

    //当日第一阶段阵亡集合
    ArrayList<Player> dayFirstDeadPlayerList;
    //当日第二阶段阵亡集合
    ArrayList<Player> daySecondDeadPlayerList;

    //是否以分组的方式显示角色
    private boolean isShowGroup;

    //清除选中状态标记
    private boolean clear;

    public PlayerListAdapter(GameActivity activity, ArrayList<Player> playerList,
                             ArrayList<Player> cupidPlayerList,
                             ArrayList<Player> wildChildPlayerList,
                             ArrayList<Player> guardPlayerList,
                             ArrayList<Player> wolfKillPlayerList,
                             ArrayList<Player> poisonKillPlayerList,
                             ArrayList<Player> dayFirstDeadPlayerList,
                             ArrayList<Player> daySecondDeadPlayerList,
                             ArrayList<Player> hunterPlayerKillList) {

        this.activity = activity;
        this.playerList = playerList;
        this.cupidPlayerList = cupidPlayerList;
        this.wildChildPlayerList = wildChildPlayerList;
        this.guardPlayerList = guardPlayerList;
        this.wolfKillPlayerList = wolfKillPlayerList;
        this.poisonKillPlayerList = poisonKillPlayerList;
        this.dayFirstDeadPlayerList = dayFirstDeadPlayerList;
        this.daySecondDeadPlayerList = daySecondDeadPlayerList;
        this.hunterPlayerKillList = hunterPlayerKillList;
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Player getItem(int position) {
        return playerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_player_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Player player = getItem(position);

        //头像配置
        if (player.isReady) {//是否已准备
            viewHolder.textviewRoleDesc.setText(player.role.desc);

            if (player.isAlive > 0) {//是否存活
                viewHolder.imageviewIcPlayer.setImageResource(player.avatar);
            } else {
                viewHolder.imageviewIcPlayer.setImageResource(R.drawable.ic_avatar_default);
            }
        } else {//尚未准备
            viewHolder.imageviewIcPlayer.setImageResource(R.drawable.ic_avatar_default);
            viewHolder.textviewRoleDesc.setText("未分配");
        }

        if (player.isShow) {//显示身份描述
            viewHolder.textviewRoleDesc.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textviewRoleDesc.setVisibility(View.INVISIBLE);
        }

        viewHolder.textviewSeatCode.setText(String.valueOf(player.seat));

        viewHolder.relativelayoutPlayerItem.setOnClickListener(new PlayerOnClick(viewHolder.imageviewPlayerChecked, position));

        if(clear){
            viewHolder.imageviewPlayerChecked.setVisibility(View.GONE);
        }

        ((ViewGroup)convertView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        return convertView;
    }


    @Override
    public long getHeaderId(int position) {
        long headerId = -1;
        if (isShowGroup) {
            headerId = playerList.get(position).role.camp;
        }
        return headerId;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_player_header_item, parent, false);
            headerViewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(headerViewHolder);
        }else{
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }

        Player player = playerList.get(position);
        int camp = player.role.camp;
        String header = null;

        switch (camp){
            case Role.CAMP_CIVILIAN:
                header = "村民阵营";
                break;
            case Role.CAMP_PROTOSS:
                header = "神民阵营";
                break;
            case Role.CAMP_WOLF:
                header = "狼人阵营";
                break;
            case Role.CAMP_THIRD:
                header = "其他阵营";
                break;
            default:
                header = "未知阵营";
                break;
        }

        headerViewHolder.textviewHeaderTitle.setText(header);

        if(getHeaderId(position) == -1) convertView.setVisibility(View.GONE); else convertView.setVisibility(View.VISIBLE);
        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.relativelayout_player_item)
        RelativeLayout relativelayoutPlayerItem;
        @BindView(R.id.imageview_ic_player)
        ImageView imageviewIcPlayer;
        @BindView(R.id.textview_seat_code)
        TextView textviewSeatCode;
        @BindView(R.id.textview_role_desc)
        TextView textviewRoleDesc;
        @BindView(R.id.imageview_player_checked)
        ImageView imageviewPlayerChecked;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class HeaderViewHolder {
        @BindView(R.id.textview_header_title)
        TextView textviewHeaderTitle;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
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
    public void showOrHideGroup(boolean isShowGroup){
        this.isShowGroup = isShowGroup;
        notifyDataSetChanged();
    }

    private class PlayerOnClick implements View.OnClickListener{
        private ImageView imageviewPlayerChecked;
        private int position;

        public PlayerOnClick(ImageView imageviewPlayerChecked, int position) {
            this.imageviewPlayerChecked = imageviewPlayerChecked;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int isVisible;
            Player player = playerList.get(position);
            switch (activity.state){
                case R.drawable.ic_role_wild_child://野孩子
                    isVisible = imageviewPlayerChecked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                    if(isVisible == View.GONE){
                        wildChildPlayerList.remove(player);
                        imageviewPlayerChecked.setVisibility(isVisible);
                    }else{
                        if(wildChildPlayerList.size() < 1){
                            wildChildPlayerList.add(player);
                            imageviewPlayerChecked.setVisibility(isVisible);
                        }
                    }
                    break;
                case R.drawable.ic_role_little_girl://小女孩
                    break;
                case R.drawable.ic_role_cupid://丘比特
                    isVisible = imageviewPlayerChecked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                    if(isVisible == View.GONE){
                        cupidPlayerList.remove(player);
                        imageviewPlayerChecked.setVisibility(isVisible);
                    }else{
                        if(cupidPlayerList.size() < 2){
                            cupidPlayerList.add(player);
                            imageviewPlayerChecked.setVisibility(isVisible);
                        }
                    }
                    break;
                case R.drawable.ic_role_guard://守卫
                    isVisible = imageviewPlayerChecked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                    if(isVisible == View.GONE){
                        guardPlayerList.remove(player);
                        imageviewPlayerChecked.setVisibility(isVisible);
                    }else{
                        if(guardPlayerList.size() < 1) {
                            guardPlayerList.add(player);
                            imageviewPlayerChecked.setVisibility(isVisible);
                        }
                    }
                    break;
                case R.drawable.ic_role_hunter://猎人
                    if(activity.hunterState != 0){
                        isVisible = imageviewPlayerChecked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                        if(isVisible == View.GONE){
                            hunterPlayerKillList.remove(player);

                            imageviewPlayerChecked.setVisibility(isVisible);
                        }else{
                            if(hunterPlayerKillList.size() < 1) {
                                if(player.isAlive <= 0){
                                    ToastUtil.showToast(activity, "不能选择已出局玩家");
                                }else{
                                    hunterPlayerKillList.add(player);
                                    imageviewPlayerChecked.setVisibility(isVisible);
                                }
                            }
                        }
                    }
                    break;
                case R.drawable.ic_role_seer://预言家
                    if(player.isAlive > 0){
                        SeerDialog seerDialog = new SeerDialog(activity, R.style.DialogTheme);
                        seerDialog.preInit(playerList.get(position));

                        seerDialog.setCancelable(true);
                        seerDialog.setCanceledOnTouchOutside(true);
                        seerDialog.show();
                    }else{
                        ToastUtil.showToast(activity, "不能验证已出局玩家");
                    }
                    break;
                case R.drawable.ic_role_white_wolf_king://白狼王
                case R.drawable.ic_role_wolf://狼人
                    if(player.isAlive > 0){
                        isVisible = imageviewPlayerChecked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;

                        if(isVisible == View.GONE){
                            wolfKillPlayerList.remove(player);
                            imageviewPlayerChecked.setVisibility(isVisible);
                        }else{
                            if(wolfKillPlayerList.size() < 1) {
                                wolfKillPlayerList.add(player);
                                imageviewPlayerChecked.setVisibility(isVisible);
                            }
                        }
                    }else{
                        ToastUtil.showToast(activity, "不能选择已出局玩家");
                    }

                    break;
                case R.drawable.ic_role_witch://女巫
                    if(activity.antidoteOrPoison == 1){
                        isVisible = imageviewPlayerChecked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                        if(player == wolfKillPlayerList.get(0) || player.isAlive > 0){
                            if(isVisible == View.GONE){
                                poisonKillPlayerList.remove(player);
                                imageviewPlayerChecked.setVisibility(isVisible);
                            }else{
                                if(poisonKillPlayerList.size() < 1) {
                                    poisonKillPlayerList.add(player);
                                    imageviewPlayerChecked.setVisibility(isVisible);
                                }
                            }
                        }else{
                            ToastUtil.showToast(activity, "不能选择已出局玩家");
                        }
                    }
                    break;
                case Integer.MAX_VALUE://白天第一阶段 投票放逐
                    isVisible = imageviewPlayerChecked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                    if(isVisible == View.GONE){
                        dayFirstDeadPlayerList.remove(player);

                        imageviewPlayerChecked.setVisibility(isVisible);
                    }else{
                        if(dayFirstDeadPlayerList.size() < 1) {
                            if(player.isAlive <= 0){
                                ToastUtil.showToast(activity, "不能选择已出局玩家");
                            }else{
                                dayFirstDeadPlayerList.add(player);
                                imageviewPlayerChecked.setVisibility(isVisible);
                            }
                        }
                    }
                    break;

                case Integer.MIN_VALUE://白天第二阶段 发动技能
                    isVisible = imageviewPlayerChecked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                    if(isVisible == View.GONE){
                        daySecondDeadPlayerList.remove(player);

                        imageviewPlayerChecked.setVisibility(isVisible);
                    }else{
                        if(daySecondDeadPlayerList.size() < 1) {
                            if(player.isAlive <= 0){
                                ToastUtil.showToast(activity, "不能选择已出局玩家");
                            }else{
                                daySecondDeadPlayerList.add(player);
                                imageviewPlayerChecked.setVisibility(isVisible);
                            }
                        }
                    }
                    break;
            }
            LogUtils.print("Item发生了点击:" + player.role.desc);
        }
    }

    /**
     *
     * PACKAGE_NAME: com.z.wolfkill.home.main.adapter
     * FUNCTIONAL_DESCRIPTION: 清除角色之前的选定状态
     * CREATE_BY: 尽际
     * CREATE_TIME: 2016/11/13 01:09
     * MODIFICATORY_DESCRIPTION:
     * MODIFY_BY:
     * MODIFICATORY_TIME:
     */
    public void clearCheckedWithNotify(){
        clearCheckedWithNotify(true);
    }

    public void clearCheckedWithNotify(boolean isNeed2Notify){
        clear = true;
        if (isNeed2Notify) {
            notifyDataSetChanged();
        }
    }

}
