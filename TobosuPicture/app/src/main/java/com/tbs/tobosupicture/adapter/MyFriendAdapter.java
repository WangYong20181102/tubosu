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
import com.tbs.tobosupicture.bean._MyFriend;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/7/15 17:00.
 * 我的图友 适配器  涉及到点击头像进入个人主页 所以在适配器中完成点击事件的逻辑
 */

public class MyFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<_MyFriend> myFriendArrayList;
    private Context mContext;
    private int adapterLoadState = 1;//1.加载更多  2.恢复正常的状态

    public MyFriendAdapter(Context context, ArrayList<_MyFriend> myFriends) {
        this.mContext = context;
        this.myFriendArrayList = myFriends;
    }

    //图层变换 加载更多图层
    public void changLoadState(int state) {
        this.adapterLoadState = state;
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
            //正常状态的图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_friend, parent, false);
            MyFriendViewHolder holder = new MyFriendViewHolder(view);
            return holder;
        } else if (viewType == 1) {
            //加载更多图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            MyFriendLoadMore loadMoreHolder = new MyFriendLoadMore(view);
            return loadMoreHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyFriendViewHolder) {
            //TODO 数据的适配 当个性签名为空时要判断 将个性签名的栏目隐藏 互为图友时要显示 思考一下点击头像事件以及图友添加事件写在adapter中

        } else if (holder instanceof MyFriendLoadMore) {
            if (position == 0) {
                ((MyFriendLoadMore) holder).mTextView.setVisibility(View.GONE);
                ((MyFriendLoadMore) holder).mProgressBar.setVisibility(View.GONE);
            }
            if (adapterLoadState == 1) {
                //显示加载更多
                ((MyFriendLoadMore) holder).mTextView.setVisibility(View.VISIBLE);
                ((MyFriendLoadMore) holder).mProgressBar.setVisibility(View.VISIBLE);
            } else {
                ((MyFriendLoadMore) holder).mTextView.setVisibility(View.GONE);
                ((MyFriendLoadMore) holder).mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return myFriendArrayList != null ? myFriendArrayList.size() + 1 : 0;
    }

    class MyFriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView myfriendIcon;//我的好友头像
        private TextView myfriendName;//我的好友名字
        private TextView myfriendSign;//我的好友签名
        private TextView myfriendAdd;//显示是否互为好友

        public MyFriendViewHolder(View itemView) {
            super(itemView);
            myfriendIcon = (ImageView) itemView.findViewById(R.id.item_myfriend_icon);
            myfriendName = (TextView) itemView.findViewById(R.id.item_myfriend_name);
            myfriendSign = (TextView) itemView.findViewById(R.id.item_myfriend_sign);
            myfriendAdd = (TextView) itemView.findViewById(R.id.item_myfriend_add);
        }
    }

    //加载更多
    class MyFriendLoadMore extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public MyFriendLoadMore(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }
}
