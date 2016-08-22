package com.du.easysignin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.du.easysignin.R;
import com.du.easysignin.bean.SignInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * listview适配器
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class ListAdapter extends BaseAdapter {
    List<SignInfo> list;
    Context context;

    public ListAdapter(Context context, List<SignInfo> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {

        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
         View v;
        if (convertView == null) {
            holder = new ViewHolder();
            v = View.inflate(context, R.layout.listitem_signinfo, null);
            holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            holder.tv_cardnumber = (TextView) v.findViewById(R.id.tv_cardnumber);
            holder.tv_ipaddress= (TextView) v.findViewById(R.id.tv_ipaddress);

            v.setTag(holder);
        } else {
             holder= (ViewHolder) convertView.getTag();
            v=convertView;
        }
        SignInfo info = list.get(position);
        holder.tv_ipaddress.setText(info.getIp_address());
        holder.tv_cardnumber.setText(info.getCard_name());
        holder.tv_name.setText(info.getPersonname());


        return v;
    }

    public static class ViewHolder {
        TextView tv_name;
        TextView tv_cardnumber;
        TextView tv_ipaddress;

    }

}
