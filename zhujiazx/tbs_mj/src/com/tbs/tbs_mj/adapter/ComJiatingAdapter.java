package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean.ShaixuanBean;

import java.util.ArrayList;


public class ComJiatingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ShaixuanBean> dataList;
    private LayoutInflater inflater;
    private int selectedPosition = -1;


    public ComJiatingAdapter(Context context, ArrayList<ShaixuanBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void clearPosition(){
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public int getCount() {
        return dataList == null?0:dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ComViewHolder holder;
        if(view == null){
            holder = new ComViewHolder();
            view = inflater.inflate(R.layout.com_layout_item22, null);
            holder.tv = (TextView) view.findViewById(R.id.tvCompabn);
            view.setTag(holder);
        }else {
            holder = (ComViewHolder) view.getTag();
        }

        holder.tv.setText(dataList.get(i).getName());
        if(selectedPosition == i){
            holder.tv.setTextColor(Color.parseColor("#FF6F20"));
            holder.tv.setBackgroundResource(R.drawable.selected_servicearea_textview_bg);
        }else {
            holder.tv.setTextColor(Color.parseColor("#666666"));
            holder.tv.setBackgroundResource(R.drawable.select_servicearea_textview_bg);
        }
        return view;
    }


    class ComViewHolder {
        TextView tv;
    }
}
