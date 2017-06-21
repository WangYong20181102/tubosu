package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

/**
 * Created by Mr.Lin on 2017/5/31 10:18.
 * 我的关注对象适配器
 */

public class AttentionAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;

    public AttentionAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_attention, parent, false);
        MyAttenViewHolder holder = new MyAttenViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null) {
            onItemClickLister.onItemClick(v);
        }
    }

    //单项点击事件的接口
    public static interface OnItemClickLister {
        void onItemClick(View view);
    }

    //实现单项点击事件
    private OnItemClickLister onItemClickLister = null;

    public void setOnItemClickLister(OnItemClickLister lister) {
        onItemClickLister = lister;
    }

    //单项的显示
    public class MyAttenViewHolder extends RecyclerView.ViewHolder {
        private ImageView attenIcon;//用户的头像
        private TextView attenName;//关注用户的名称
        private TextView attenPageNum;//关注用户的文章数量
        private TextView attenTime;//关注用户的文章更新的时间
        private TextView attenTitle;//关注用户的文章更新的标题
        private TextView attenTag;//关注用户的文章更新的标签

        public MyAttenViewHolder(View itemView) {
            super(itemView);
            attenIcon = (ImageView) itemView.findViewById(R.id.atten_icon);
            attenName = (TextView) itemView.findViewById(R.id.atten_name);
            attenPageNum = (TextView) itemView.findViewById(R.id.atten_pagenum);
            attenTime = (TextView) itemView.findViewById(R.id.atten_time);
            attenTitle = (TextView) itemView.findViewById(R.id.atten_title);
            attenTag = (TextView) itemView.findViewById(R.id.atten_tag);
        }
    }
}
