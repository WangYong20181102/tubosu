package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean.FactoryStyrlBean;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.MyGridView;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/17.
 */

public class FactoryStyleAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "FactoryStyleAdapter";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<FactoryStyrlBean> groupDataList;

    public FactoryStyleAdapter(Context context, ArrayList<FactoryStyrlBean> groupDataList, OnFactoryStyleItemClickListener onFactoryStyleItemClickListener){
        this.context = context;
        this.onFactoryStyleItemClickListener = onFactoryStyleItemClickListener;
        this.inflater = LayoutInflater.from(context);
        this.groupDataList = groupDataList;
    }


    @Override
    public int getGroupCount() {
        return groupDataList==null? 0 : groupDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return groupDataList.get(groupPosition).getChild_data()==null? 0 : groupDataList.get(groupPosition).getChild_data().size();
        return groupDataList.get(groupPosition).getChild_data()==null? 0 : 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupDataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupDataList.get(groupPosition).getChild_data().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if(convertView==null){
            holder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_group_style_layout,null);
            holder.tvBigStyleText = (TextView) convertView.findViewById(R.id.tv_group_style_text);
            holder.ivBigStyleImg = (ImageView) convertView.findViewById(R.id.iv_group_style_icon);
            convertView.setTag(holder);
        }else{
            holder = (GroupViewHolder) convertView.getTag();
        }

        holder.tvBigStyleText.setText(groupDataList.get(groupPosition).getClass_name());
        if(isExpanded){
            holder.ivBigStyleImg.setBackgroundResource(R.mipmap.shangla2);
        }else{
            holder.ivBigStyleImg.setBackgroundResource(R.mipmap.xiala);
        }

        holder.tvBigStyleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = groupDataList.get(groupPosition).getId();
                String text = groupDataList.get(groupPosition).getClass_name();
                onFactoryStyleItemClickListener.onFactoryStyleParentClickListener(id, text);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;

        if(convertView==null){
            holder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_gv_child_style_layout, null);
            holder.gvChild = (MyGridView) convertView.findViewById(R.id.gv_child);

            convertView.setTag(holder);
        }else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        GvChildAdapter adapter = new GvChildAdapter(context, groupDataList.get(groupPosition).getChild_data());
        holder.gvChild.setAdapter(adapter);

        holder.gvChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String style_id = groupDataList.get(groupPosition).getChild_data().get(position).getId();
                String text = groupDataList.get(groupPosition).getChild_data().get(position).getClass_name();
                onFactoryStyleItemClickListener.onFactoryStyleItemClickListener(style_id,text);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder{
        TextView tvBigStyleText;
        ImageView ivBigStyleImg;
    }

    class ChildViewHolder{
        MyGridView gvChild;
//        TextView tv;
    }

    private OnFactoryStyleItemClickListener onFactoryStyleItemClickListener;
    public interface OnFactoryStyleItemClickListener{
        void onFactoryStyleItemClickListener(String classID, String classText);
        void onFactoryStyleParentClickListener(String parentId, String parentText);
    }

    public OnFactoryStyleItemClickListener getOnFactoryStyleItemClickListener() {
        return onFactoryStyleItemClickListener;
    }

    public void setOnFactoryStyleItemClickListener(OnFactoryStyleItemClickListener onFactoryStyleItemClickListener) {
        this.onFactoryStyleItemClickListener = onFactoryStyleItemClickListener;
    }
}