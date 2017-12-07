package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._YouHui;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/12/1 13:40.
 * 优惠活动列表的适配器 3.6版本新增
 */

public class CoYouHuiAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private String TAG = "CoYouHuiAdapter";
    private Context mContext;
    private ArrayList<_YouHui> mYouHuiArrayList;

    public CoYouHuiAdapter(Context context, ArrayList<_YouHui> youHuiArrayList) {
        this.mContext = context;
        this.mYouHuiArrayList = youHuiArrayList;
    }

    public static interface OnYouHuiItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnYouHuiItemClickLister onYouHuiItemClickLister = null;

    public void setOnYouHuiItemClickLister(OnYouHuiItemClickLister onYouHuiItemClickLister) {
        this.onYouHuiItemClickLister = onYouHuiItemClickLister;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_co_youhui, parent, false);
        YouHuiViewHolder youHuiViewHolder = new YouHuiViewHolder(view);
        youHuiViewHolder.item_co_you_hui_fadan.setOnClickListener(this);
        return youHuiViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof YouHuiViewHolder) {
            //设置点击的tag
            ((YouHuiViewHolder) holder).item_co_you_hui_fadan.setTag(position);
            //设置优惠说明
            ((YouHuiViewHolder) holder).item_co_you_hui_title.setText("" + mYouHuiArrayList.get(position).getTitle());
            //设置优惠的时间
            ((YouHuiViewHolder) holder).item_co_you_hui_time.setText("活动日期：" + mYouHuiArrayList.get(position).getStart_time() + "-" + mYouHuiArrayList.get(position).getEnd_time());
        }
    }

    @Override
    public int getItemCount() {
        return mYouHuiArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (onYouHuiItemClickLister != null) {
            onYouHuiItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class YouHuiViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_co_you_hui_rl;
        private TextView item_co_you_hui_title;
        private TextView item_co_you_hui_time;
        private TextView item_co_you_hui_fadan;

        public YouHuiViewHolder(View itemView) {
            super(itemView);
            item_co_you_hui_rl = itemView.findViewById(R.id.item_co_you_hui_rl);
            item_co_you_hui_title = itemView.findViewById(R.id.item_co_you_hui_title);
            item_co_you_hui_time = itemView.findViewById(R.id.item_co_you_hui_time);
            item_co_you_hui_fadan = itemView.findViewById(R.id.item_co_you_hui_fadan);
        }
    }
}
