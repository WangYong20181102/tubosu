package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._Style;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/11/23 17:54.
 */

public class MyGridViewAdapters extends BaseAdapter {
    private Context mContext;
    private List<_Style> btnName;
    private LayoutInflater inflater;

    public MyGridViewAdapters(Context context, List<_Style> btnnames) {
        this.mContext = context;
        this.btnName = btnnames;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return btnName.size();
    }

    @Override
    public _Style getItem(int position) {
        return btnName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pop_grid, parent, false);
            holder = new MyViewHolder();
            holder.BtntextView = (TextView) convertView.findViewById(R.id.item_pop_tv);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.BtntextView.setText("" + btnName.get(position).getClass_name());
        return convertView;
    }

    class MyViewHolder {
        private TextView BtntextView;
    }
}
