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
import com.tbs.tobosupicture.bean._MyFans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/12 16:46.
 * 我的图谜适配器
 */

public class MyFansAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private ArrayList<_MyFans> myFansList;
    private int adapterLoadState = 1;//1.加载更多  2.恢复正常状态

    public MyFansAdapter(Context context, ArrayList<_MyFans> myFansList) {
        this.mContext = context;
        this.myFansList = myFansList;
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

    public static interface OnMyFansItemClickLister {
        void onItemClick(View view);
    }

    private OnMyFansItemClickLister onMyFansItemClickLister;

    public void setOnMyFansItemClickLister(OnMyFansItemClickLister onMyFansItemClickLister) {
        this.onMyFansItemClickLister = onMyFansItemClickLister;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            //正常图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_fans, parent, false);
            MyFansViewHolder holder = new MyFansViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        } else if (viewType == 1) {
            //加载更多图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            MyFansLoadMore loadMoreHolder = new MyFansLoadMore(view);
            return loadMoreHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyFansViewHolder) {
            //TODO 数据的适配 当个性签名为空时要判断 将个性签名的栏目隐藏 添加加为图友的按钮点击事件 思考一下点击头像事件以及图友添加事件写在adapter中
//            ((MyFansViewHolder) holder).myfansName.setText(myFansList.get(position));
        } else if (holder instanceof MyFansLoadMore) {
            if (position == 0) {
                ((MyFansLoadMore) holder).mTextView.setVisibility(View.GONE);
                ((MyFansLoadMore) holder).mProgressBar.setVisibility(View.GONE);
            }
            if (adapterLoadState == 1) {
                //显示加载更多
                ((MyFansLoadMore) holder).mTextView.setVisibility(View.VISIBLE);
                ((MyFansLoadMore) holder).mProgressBar.setVisibility(View.VISIBLE);
            } else {
                ((MyFansLoadMore) holder).mTextView.setVisibility(View.GONE);
                ((MyFansLoadMore) holder).mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return myFansList != null ? myFansList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onMyFansItemClickLister != null && !myFansList.isEmpty()) {
            onMyFansItemClickLister.onItemClick(v);
        }
    }

    //显示当前粉丝的列表子项
    class MyFansViewHolder extends RecyclerView.ViewHolder {
        private ImageView myfansIcon;//我的粉丝头像
        private TextView myfansName;//我的粉丝名字
        private TextView myfansSign;//我的粉丝签名
        private TextView myfansAdd;//添加图谜或者显示是否已为图友

        public MyFansViewHolder(View itemView) {
            super(itemView);
            myfansIcon = (ImageView) itemView.findViewById(R.id.item_myfans_icon);
            myfansName = (TextView) itemView.findViewById(R.id.item_myfans_name);
            myfansSign = (TextView) itemView.findViewById(R.id.item_myfans_sign);
            myfansAdd = (TextView) itemView.findViewById(R.id.item_myfans_add);
        }
    }

    //加载更多
    class MyFansLoadMore extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public MyFansLoadMore(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }
}
