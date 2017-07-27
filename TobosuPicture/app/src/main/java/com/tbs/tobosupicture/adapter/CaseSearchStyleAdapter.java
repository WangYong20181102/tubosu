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
import com.tbs.tobosupicture.bean.CaseConditionType;
import com.tbs.tobosupicture.bean.FactoryStyrlBean;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lie on 2017/7/17.
 */

public class CaseSearchStyleAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CaseSearchStyleAdapter";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CaseConditionType> groupDataList;
    private ArrayList<Integer> positionList = new ArrayList<Integer>();

    public CaseSearchStyleAdapter(Context context, ArrayList<CaseConditionType> groupDataList, OnSearchCaseStyleItemClickListener onSearchCaseStyleItemClickListener){
        this.context = context;
        this.onSearchCaseStyleItemClickListener = onSearchCaseStyleItemClickListener;
        this.inflater = LayoutInflater.from(context);
        this.groupDataList = groupDataList;
        for(int i=0;i<groupDataList.size();i++){
            this.positionList.add(-1);
        }
    }


    @Override
    public int getGroupCount() {
        return groupDataList==null? 0 : groupDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupDataList.get(groupPosition).getCaseTypeChildList()==null? 0 : 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupDataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupDataList.get(groupPosition).getCaseTypeChildList().get(childPosition);
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

        holder.tvBigStyleText.setText(groupDataList.get(groupPosition).getType());
        if(isExpanded){
            holder.ivBigStyleImg.setBackgroundResource(R.mipmap.shangla2);
        }else{
            holder.ivBigStyleImg.setBackgroundResource(R.mipmap.xiala);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if(convertView==null){
            holder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_case_child_style_layout, null);
            holder.gvChild = (MyGridView) convertView.findViewById(R.id.gv_case_child);

            convertView.setTag(holder);
        }else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        final GvCaseStyleAdapter adapter = new GvCaseStyleAdapter(context, groupDataList.get(groupPosition).getCaseTypeChildList(), positionList.get(groupPosition));
        holder.gvChild.setAdapter(adapter);

        holder.gvChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _id = groupDataList.get(groupPosition).getCaseTypeChildList().get(position).getId();
                String cc = groupDataList.get(groupPosition).getCaseTypeChildList().get(position).getValue();
                onSearchCaseStyleItemClickListener.onSearchCaseStyleParentClickListener(groupPosition, _id, position, cc);
                // 保存点击position
//                switch (groupPosition){
//                    case 0: // 面积
//                        if(SpUtils.getSearchCaseColorAreaNum(context)==position){
////                            positionList.add(groupPosition, -1);
//                            adapter.setSelectionPosition(-1);
//                            SpUtils.setSearchCaseColorAreaNum(context, -1);
//                        }else {
//                            SpUtils.setSearchCaseColorAreaNum(context, position);
//                            adapter.setSelectionPosition(positionList.get(groupPosition));
////                            positionList.add(groupPosition, position);
//                        }
//
//                        break;
//                    case 1: // 布局
//                        SpUtils.setSearchCaseColorLayoutNum(context, position);
//                        break;
//                    case 2: // 价格
//                        SpUtils.setSearchCaseColorPriceNum(context, position);
//                        break;
//                    case 3: // 户型
//                        SpUtils.setSearchCaseColorStyleNum(context, position);
//                        break;
//                }
                positionList.add(groupPosition, position);
                adapter.setSelectionPosition(positionList.get(groupPosition));
                adapter.notifyDataSetChanged();
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

    private OnSearchCaseStyleItemClickListener onSearchCaseStyleItemClickListener;
    public interface OnSearchCaseStyleItemClickListener{
        void onSearchCaseStyleParentClickListener(int group, String id, int position, String condiction);
    }

    public OnSearchCaseStyleItemClickListener getOnSearchCaseStyleItemClickListener() {
        return onSearchCaseStyleItemClickListener;
    }

    public void setOnSearchCaseStyleItemClickListener(OnSearchCaseStyleItemClickListener onSearchCaseStyleItemClickListener) {
        this.onSearchCaseStyleItemClickListener = onSearchCaseStyleItemClickListener;
    }
}