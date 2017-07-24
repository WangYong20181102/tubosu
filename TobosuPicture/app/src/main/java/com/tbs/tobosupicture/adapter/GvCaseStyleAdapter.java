package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean.CaseTypeChild;
import com.tbs.tobosupicture.bean.ChildData;
import com.tbs.tobosupicture.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lie on 2017/7/17.
 */

public class GvCaseStyleAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CaseTypeChild> dataList;
    private LayoutInflater inflater;
    private int selectionPosition;

    public GvCaseStyleAdapter(Context context, ArrayList<CaseTypeChild> dataList, int selectionPosition){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
        this.selectionPosition = selectionPosition;
    }

    public int getSelectionPosition() {
        return selectionPosition;
    }

    public void setSelectionPosition(int selectionPosition) {
        this.selectionPosition = selectionPosition;
    }

    public void clearSelectionPosition() {
        this.selectionPosition = -1;
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

        if(selectionPosition == position){
            holder.tv.setTextColor(Color.parseColor("#F97B0C"));
        }else{
            holder.tv.setTextColor(Color.parseColor("#2B2F3A"));
        }

        holder.tv.setText(dataList.get(position).getValue());
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}
