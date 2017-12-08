package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.CompanyDistrictBean;
import java.util.ArrayList;


public class ComDisctrictAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CompanyDistrictBean> dataList;
    private LayoutInflater inflater;
    private int selectedPosition = -1;


    public ComDisctrictAdapter(Context context, ArrayList<CompanyDistrictBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void clearPosition(){
        this.selectedPosition = -1;
        notifyDataSetChanged();
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

        holder.tv.setText(dataList.get(i).getDistrict_name());
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
