package com.z.wolfkill.home.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.z.wolfkill.R;
import com.z.wolfkill.home.main.bean.Role;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PACKAGE_NAME: com.z.wolfkill.home.main.adapter
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/8 14:30
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class RoleAdapter extends BaseAdapter {
    Context context;
    ArrayList<Role> list;

    public RoleAdapter(Context context, ArrayList<Role> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Role getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_role_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageviewIcRole.setImageResource(list.get(position).icon);
        viewHolder.textviewRoleDesc.setText(list.get(position).desc);

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.imageview_ic_role)
        ImageView imageviewIcRole;
        @BindView(R.id.textview_role_desc)
        TextView textviewRoleDesc;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
