package com.z.wolfkill.home.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.z.wolfkill.R;
import com.z.wolfkill.common.constants.Constants;
import com.z.wolfkill.common.utils.ToastUtil;
import com.z.wolfkill.home.main.bean.Player;
import com.z.wolfkill.home.main.bean.Role;
import com.z.wolfkill.home.main.dialog.RoleListDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.adapter
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/9 11:46
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class RoleListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Role> alreadyRoleList;
    ArrayList<Player> playerList;
    RoleListDialog roleListDialog;

    //Activity中的完成按钮
    Button buttonComplete;

    public RoleListAdapter(Context context, ArrayList<Role> alreadyRoleList, ArrayList<Player> playerList, RoleListDialog roleListDialog) {
        this.context = context;
        this.alreadyRoleList = alreadyRoleList;
        this.playerList = playerList;
        this.roleListDialog = roleListDialog;
        this.buttonComplete = buttonComplete;
    }

    @Override
    public int getCount() {
        return alreadyRoleList.size();
    }

    @Override
    public Role getItem(int position) {
        return alreadyRoleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_role_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Role role = getItem(position);

        viewHolder.imageviewIcRole.setImageResource(role.icon);
        viewHolder.textviewRoleDesc.setText(role.desc);
        viewHolder.textviewRoleCount.setText(String.valueOf(role.count));

        new ButtonClickLinstener(viewHolder.buttonSubtract, viewHolder.buttonAdd, role);

        ((ViewGroup)convertView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.imageview_ic_role)
        ImageView imageviewIcRole;
        @BindView(R.id.textview_role_desc)
        TextView textviewRoleDesc;
        @BindView(R.id.button_subtract)
        android.widget.Button buttonSubtract;
        @BindView(R.id.textview_role_count)
        TextView textviewRoleCount;
        @BindView(R.id.button_add)
        android.widget.Button buttonAdd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class ButtonClickLinstener implements View.OnClickListener{
        Role role;
        Button buttonSubtract;
        Button buttonAdd;

        public ButtonClickLinstener(Button buttonSubtract, Button buttonAdd, Role role) {
            this.buttonSubtract = buttonSubtract;
            this.buttonAdd = buttonAdd;
            this.role = role;

            buttonAdd.setOnClickListener(this);
            buttonSubtract.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_add:
                    if((role.count < role.limitCount || role.limitCount == 0) && playerList.size() < Constants.MAX_PLAYERS){
                        role.count++;
                        int hp;
                        if(role.icon == R.drawable.ic_role_elder){
                            hp = 2;
                        }else{
                            hp = 1;
                        }
                        Player player = new Player(role, hp);

                        playerList.add(player);

                        notifyDataSetChanged();
                    }else{
                        ToastUtil.showToast(context, "角色或玩家数量已到达上限！");
                    }
                    break;
                case R.id.button_subtract:
                    role.count--;

                    for(int i = playerList.size() - 1; i >= 0; i--){
                        Player player = playerList.get(i);
                        if(player.role.icon == role.icon){
                            playerList.remove(i);
                            break;
                        }
                    }

                    if(role.count <= 0){
                        alreadyRoleList.remove(role);
                    }
                    notifyDataSetChanged();
                    break;
            }

            roleListDialog.refreshCount();
        }
    }
}
