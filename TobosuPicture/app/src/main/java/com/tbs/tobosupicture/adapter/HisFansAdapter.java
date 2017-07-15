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
import com.tbs.tobosupicture.bean._HisFans;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/7/14 10:06.
 */

public class HisFansAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private ArrayList<_HisFans> hisFansArrayList;
    private int adapterState = 1;//列表的状态  加载更多和普通状态

    public HisFansAdapter(Context context, ArrayList<_HisFans> hisFansArrayList) {
        this.hisFansArrayList = hisFansArrayList;
        this.mContext = context;
    }

    //图层的转换
    public void changeAdapterState(int state) {
        this.adapterState = state;
        notifyDataSetChanged();
    }
    //对应图层的改变

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//加载更多的图层
        } else {
            return 2;//普通的图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            //显示正常的图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_his_fans, parent, false);
            HisFansViewHolder holder = new HisFansViewHolder(view);
            return holder;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            HisFansLoadMore loadMoreHolder = new HisFansLoadMore(view);
            return loadMoreHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HisFansViewHolder) {
            //TODO 数据的适配 当个性签名为空时要判断 将个性签名的栏目隐藏 思考一下点击头像事件写在adapter中
        } else if (holder instanceof HisFansLoadMore) {
            if (position == 0) {
                ((HisFansLoadMore) holder).mProgressBar.setVisibility(View.GONE);
                ((HisFansLoadMore) holder).mTextView.setVisibility(View.GONE);
            }
            if (adapterState == 1) {
                //显示加载更多
                ((HisFansLoadMore) holder).mTextView.setVisibility(View.VISIBLE);
                ((HisFansLoadMore) holder).mProgressBar.setVisibility(View.VISIBLE);
            } else {
                //正常状态
                ((HisFansLoadMore) holder).mProgressBar.setVisibility(View.GONE);
                ((HisFansLoadMore) holder).mTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return hisFansArrayList != null ? hisFansArrayList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {

    }

    //普通的图层
    class HisFansViewHolder extends RecyclerView.ViewHolder {
        private ImageView hisfansIcon;//我的粉丝头像
        private TextView hisfansName;//我的粉丝名字
        private TextView hisfansSign;//我的粉丝签名

        public HisFansViewHolder(View itemView) {
            super(itemView);
            hisfansIcon = (ImageView) itemView.findViewById(R.id.item_hisfans_icon);
            hisfansName = (TextView) itemView.findViewById(R.id.item_hisfans_name);
            hisfansSign = (TextView) itemView.findViewById(R.id.item_hisfans_sign);
        }
    }

    //加载更多 图层
    class HisFansLoadMore extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public HisFansLoadMore(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }
}
