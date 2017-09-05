package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import java.util.StringTokenizer;

/**
 * Created by Lie on 2017/7/17.
 */

public class CaseSearchStyleAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CaseSearchStyleAdapter";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CaseConditionType> groupDataList;
    private ArrayList<Integer> positionList = new ArrayList<Integer>();
    private GroupViewHolder groupHolder;
    private ChildViewHolder childHolder;

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

        if(convertView==null){
            groupHolder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_group_style_layout,null);
            groupHolder.tvBigStyleText = (TextView) convertView.findViewById(R.id.tv_group_style_text);
            groupHolder.ivBigStyleImg = (ImageView) convertView.findViewById(R.id.iv_group_style_icon);
            groupHolder.show_group_style_text = (TextView) convertView.findViewById(R.id.show_group_style_text);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (GroupViewHolder) convertView.getTag();
        }

        groupHolder.tvBigStyleText.setText(groupDataList.get(groupPosition).getType());
        if(isExpanded){
            groupHolder.ivBigStyleImg.setBackgroundResource(R.mipmap.shangla2);
        }else{
            groupHolder.ivBigStyleImg.setBackgroundResource(R.mipmap.xiala);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView==null){
            childHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_case_child_style_layout, null);
            childHolder.gvChild = (MyGridView) convertView.findViewById(R.id.gv_case_child);

            convertView.setTag(childHolder);
        }else {
            childHolder = (ChildViewHolder) convertView.getTag();
        }

        final GvCaseStyleAdapter adapter = new GvCaseStyleAdapter(context, groupDataList.get(groupPosition).getCaseTypeChildList(), positionList.get(groupPosition));
        childHolder.gvChild.setAdapter(adapter);

        childHolder.gvChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _id = groupDataList.get(groupPosition).getCaseTypeChildList().get(position).getId();
                String cc = groupDataList.get(groupPosition).getCaseTypeChildList().get(position).getValue();
                onSearchCaseStyleItemClickListener.onSearchCaseStyleParentClickListener(groupPosition, _id, position, cc);
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
        TextView show_group_style_text;
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