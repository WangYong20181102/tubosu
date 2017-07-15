package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.MyAttentionDesigner;
import com.tbs.tobosupicture.bean._MyAttentionDesigner;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/7/14 13:50.
 */

public class MyAttentionDesignerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private ArrayList<_MyAttentionDesigner> myAttentionDesignerArrayList;
    private int adapterLoaderState = 1;//1.加载更多 2.恢复正常状态

    public MyAttentionDesignerAdapter(Context context, ArrayList<_MyAttentionDesigner> myAttentionDesignerArrayList) {
        this.mContext = context;
        this.myAttentionDesignerArrayList = myAttentionDesignerArrayList;
    }

    //图层切换 加载更多和普通图层
    public void changeLoadState(int state) {
        this.adapterLoaderState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//加载更多
        } else {
            return 2;//普通图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_attention_designer, parent, false);
            MyAttentionDesignerHolder holder = new MyAttentionDesignerHolder(view);
            return holder;
        } else if (viewType == 1) {
            //加载更多
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            MyAttentionDesignerLoadMore loadMoreHoulder = new MyAttentionDesignerLoadMore(view);
            return loadMoreHoulder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyAttentionDesignerHolder) {
            //TODO 在这里实现点击头像事件
//            ((MyAttentionDesignerHolder) holder).myAttentionDesignerIcon
        } else if (holder instanceof MyAttentionDesignerLoadMore) {
            if (position == 0) {
                ((MyAttentionDesignerLoadMore) holder).mProgressBar.setVisibility(View.GONE);
                ((MyAttentionDesignerLoadMore) holder).mTextView.setVisibility(View.GONE);
            }
            if (adapterLoaderState == 1) {
                ((MyAttentionDesignerLoadMore) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((MyAttentionDesignerLoadMore) holder).mTextView.setVisibility(View.VISIBLE);
            } else {
                ((MyAttentionDesignerLoadMore) holder).mProgressBar.setVisibility(View.GONE);
                ((MyAttentionDesignerLoadMore) holder).mTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return myAttentionDesignerArrayList != null ? myAttentionDesignerArrayList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {

    }

    class MyAttentionDesignerHolder extends RecyclerView.ViewHolder {
        private ImageView myAttentionDesignerIcon;
        private TextView myAttentionDesignerName;
        private TextView myAttentionDesignerFansNum;//粉丝数
        private TextView myAttentionDesignerFansBrowse;//浏览数
        private TextView myAttentionDesignerFansPic;//效果图数
        private TextView myAttentionDesignerFansCase;//案例数

        public MyAttentionDesignerHolder(View itemView) {
            super(itemView);
            myAttentionDesignerIcon = (ImageView) itemView.findViewById(R.id.item_my_attention_designer_icon);
            myAttentionDesignerName = (TextView) itemView.findViewById(R.id.item_my_attention_designer_name);
            myAttentionDesignerFansNum = (TextView) itemView.findViewById(R.id.item_my_attention_designer_fansnum);
            myAttentionDesignerFansBrowse = (TextView) itemView.findViewById(R.id.item_my_attention_designer_browse);
            myAttentionDesignerFansPic = (TextView) itemView.findViewById(R.id.item_my_attention_designer_pic);
            myAttentionDesignerFansCase = (TextView) itemView.findViewById(R.id.item_my_attention_designer_case);
        }
    }

    class MyAttentionDesignerLoadMore extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public MyAttentionDesignerLoadMore(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }
}
