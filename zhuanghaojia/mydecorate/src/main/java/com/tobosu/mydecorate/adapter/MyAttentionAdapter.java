package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.entity._MyAttention;
import com.tobosu.mydecorate.util.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/7 09:57.
 * 我的关注适配器
 */

public class MyAttentionAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private List<_MyAttention> myAttentionList;

    public MyAttentionAdapter(Context context, List<_MyAttention> myAttentionList) {
        this.mContext = context;
        this.myAttentionList = myAttentionList;
    }

    public static interface OnMyAttentionItemClickLister {
        void onItemClick(View view);
    }

    private OnMyAttentionItemClickLister onMyAttentionItemClickLister = null;

    public void setOnMyAttentionItemClickLister(OnMyAttentionItemClickLister onMyAttentionItemClickLister) {
        this.onMyAttentionItemClickLister = onMyAttentionItemClickLister;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_attention, parent, false);
        MyAttentionViewHolder holder = new MyAttentionViewHolder(v);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyAttentionViewHolder) {
            GlideUtils.glideLoader(mContext, myAttentionList.get(position).getIcon(),
                    0, R.mipmap.jiazai_loading, ((MyAttentionViewHolder) holder).attenIcon,
                    GlideUtils.CIRCLE_IMAGE);
            ((MyAttentionViewHolder) holder).attenName.setText("" + myAttentionList.get(position).getNick());
            ((MyAttentionViewHolder) holder).attenPageNum.setText("" + myAttentionList.get(position).getArticle_count() + "篇内容");
            if (TextUtils.isEmpty(myAttentionList.get(position).getType_name())) {
                ((MyAttentionViewHolder) holder).attenTag.setVisibility(View.GONE);
            } else {
                ((MyAttentionViewHolder) holder).attenTag.setText("" + myAttentionList.get(position).getType_name());
            }
            ((MyAttentionViewHolder) holder).attenTime.setText("" + myAttentionList.get(position).getTime() + "更新");
            ((MyAttentionViewHolder) holder).attenTitle.setText("" + myAttentionList.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return myAttentionList.size();
    }

    @Override
    public void onClick(View v) {
        if (onMyAttentionItemClickLister != null) {
            onMyAttentionItemClickLister.onItemClick(v);
        }
    }

    class MyAttentionViewHolder extends RecyclerView.ViewHolder {
        private ImageView attenIcon;//关注对象的头像
        private TextView attenName;//关注对象的名字
        private TextView attenPageNum;//关注对象文章数量
        private TextView attenTime;//关注对象文章更新时间
        private TextView attenTitle;//关注对象文章标题
        private TextView attenTag;//关注对象文章类型

        public MyAttentionViewHolder(View itemView) {
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
