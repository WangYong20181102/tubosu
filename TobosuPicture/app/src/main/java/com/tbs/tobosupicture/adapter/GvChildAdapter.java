package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean.ChildData;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/17.
 */

public class GvChildAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChildData> dataList;
    private LayoutInflater inflater;

    public GvChildAdapter(Context context, ArrayList<ChildData> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList==null?0:dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_child_style_layout, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_child_style);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(dataList.get(position).getClass_name());
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}
