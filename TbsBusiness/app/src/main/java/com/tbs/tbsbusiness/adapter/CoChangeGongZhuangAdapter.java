package com.tbs.tbsbusiness.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.bean._MyStore;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/6/7 15:34.
 */
public class CoChangeGongZhuangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private String TAG = "CoChangeGongZhuangAdapter";
    private ArrayList<_MyStore.ToolImprovementBean> mToolImprovementBeanArrayList;

    //子项点击事件
    public static interface OnItemClickLister {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    private OnItemClickLister onItemClickLister = null;


    public CoChangeGongZhuangAdapter(Context context, ArrayList<_MyStore.ToolImprovementBean> toolImprovementBeanArrayList) {
        this.mContext = context;
        this.mToolImprovementBeanArrayList = toolImprovementBeanArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_co_change, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        itemViewHolder.item_co_change_rl.setOnClickListener(this);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            //设置TAG
            ((ItemViewHolder) holder).item_co_change_rl.setTag(position);
            //设置文本显示信息
            ((ItemViewHolder) holder).item_co_change_name_tv.setText("" + mToolImprovementBeanArrayList.get(position).getName());
            //设置状态
            if (mToolImprovementBeanArrayList.get(position).getIs_selected() == 1) {
                //选中状态
                ((ItemViewHolder) holder).item_co_change_name_tv.setTextColor(Color.parseColor("#ffffff"));
                ((ItemViewHolder) holder).item_co_change_rl.setBackgroundResource(R.drawable.shpe_item_fengge_click);
            } else {
                //未选择
                ((ItemViewHolder) holder).item_co_change_name_tv.setTextColor(Color.parseColor("#333333"));
                ((ItemViewHolder) holder).item_co_change_rl.setBackgroundResource(R.drawable.shape_item_fengge_not_click);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mToolImprovementBeanArrayList != null ? mToolImprovementBeanArrayList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null && !mToolImprovementBeanArrayList.isEmpty()) {
            onItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_co_change_rl;
        private TextView item_co_change_name_tv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_co_change_rl = itemView.findViewById(R.id.item_co_change_rl);
            item_co_change_name_tv = itemView.findViewById(R.id.item_co_change_name_tv);
        }
    }
}
