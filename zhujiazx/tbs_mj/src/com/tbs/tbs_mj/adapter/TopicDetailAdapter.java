package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._TopicDetail;
import com.tbs.tbs_mj.utils.ImageLoaderUtil;

/**
 * Created by Mr.Lin on 2017/10/27 18:16.
 * 专题详情的适配器
 */

public class TopicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private _TopicDetail mTopicDetail;

    public TopicDetailAdapter(Context context, _TopicDetail mTopicDetail) {
        this.mContext = context;
        this.mTopicDetail = mTopicDetail;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;//返回头部
        } else {
            return 1;//返回图文
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_topic_detail_head, parent, false);
            HeadHolder headHolder = new HeadHolder(view);
            return headHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_topic_detail_foot, parent, false);
            FootHolder footHolder = new FootHolder(view);
            return footHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder) {
            //标题的显示
            if (TextUtils.isEmpty(mTopicDetail.getTitle())) {
                ((HeadHolder) holder).item_topic_head_title.setVisibility(View.GONE);
            } else {
                ((HeadHolder) holder).item_topic_head_title.setVisibility(View.VISIBLE);
                ((HeadHolder) holder).item_topic_head_title.setText(mTopicDetail.getTitle());
            }
            //时间的显示
            if (TextUtils.isEmpty(mTopicDetail.getAdd_time())) {
                ((HeadHolder) holder).item_topic_head_time.setVisibility(View.GONE);
            } else {
                ((HeadHolder) holder).item_topic_head_time.setVisibility(View.VISIBLE);
                ((HeadHolder) holder).item_topic_head_time.setText(mTopicDetail.getAdd_time());
            }
            //简介的显示
            if (TextUtils.isEmpty(mTopicDetail.getDesc())) {
                ((HeadHolder) holder).item_topic_head_content.setVisibility(View.GONE);
                ((HeadHolder) holder).item_topic_head_down_dou.setVisibility(View.GONE);
                ((HeadHolder) holder).item_topic_head_up_dou.setVisibility(View.GONE);
            } else {
                ((HeadHolder) holder).item_topic_head_down_dou.setVisibility(View.VISIBLE);
                ((HeadHolder) holder).item_topic_head_up_dou.setVisibility(View.VISIBLE);
                ((HeadHolder) holder).item_topic_head_content.setVisibility(View.VISIBLE);
                ((HeadHolder) holder).item_topic_head_content.setText(mTopicDetail.getDesc());
            }
        } else if (holder instanceof FootHolder) {
            if (mTopicDetail.getDetail_info().size() < 10) {
                ((FootHolder) holder).item_topic_foot_num.setText("0" + position + "/");
            } else {
                ((FootHolder) holder).item_topic_foot_num.setText(position + "/");
            }
            if (TextUtils.isEmpty(mTopicDetail.getDetail_info().get(position - 1).getImage_url())) {
                ((FootHolder) holder).item_topic_foot_img.setVisibility(View.GONE);
            } else {
                ((FootHolder) holder).item_topic_foot_img.setVisibility(View.VISIBLE);
                ImageLoaderUtil.loadImage(mContext, ((FootHolder) holder).item_topic_foot_img, mTopicDetail.getDetail_info().get(position - 1).getImage_url());
            }
            if (TextUtils.isEmpty(mTopicDetail.getDetail_info().get(position - 1).getSub_title())) {
                ((FootHolder) holder).item_topic_foot_title.setVisibility(View.GONE);
            } else {
                ((FootHolder) holder).item_topic_foot_title.setVisibility(View.VISIBLE);
                ((FootHolder) holder).item_topic_foot_title.setText(mTopicDetail.getDetail_info().get(position - 1).getSub_title());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mTopicDetail.getDetail_info() != null ? mTopicDetail.getDetail_info().size() + 1 : 1;
    }

    //头部的标签
    class HeadHolder extends RecyclerView.ViewHolder {
        private TextView item_topic_head_title;//标题
        private TextView item_topic_head_time;//时间
        private TextView item_topic_head_content;//内容
        private TextView item_topic_head_up_dou;//简介上边的逗号
        private TextView item_topic_head_down_dou;//简介下边的逗号

        public HeadHolder(View itemView) {
            super(itemView);
            item_topic_head_title = (TextView) itemView.findViewById(R.id.item_topic_head_title);
            item_topic_head_time = (TextView) itemView.findViewById(R.id.item_topic_head_time);
            item_topic_head_content = (TextView) itemView.findViewById(R.id.item_topic_head_content);
            item_topic_head_up_dou = (TextView) itemView.findViewById(R.id.item_topic_head_up_dou);
            item_topic_head_down_dou = (TextView) itemView.findViewById(R.id.item_topic_head_down_dou);
        }
    }

    //底部显示的图文列表
    class FootHolder extends RecyclerView.ViewHolder {
        private ImageView item_topic_foot_img;
        private TextView item_topic_foot_num;
        private TextView item_topic_foot_title;

        public FootHolder(View itemView) {
            super(itemView);
            item_topic_foot_img = (ImageView) itemView.findViewById(R.id.item_topic_foot_img);
            item_topic_foot_num = (TextView) itemView.findViewById(R.id.item_topic_foot_num);
            item_topic_foot_title = (TextView) itemView.findViewById(R.id.item_topic_foot_title);

        }
    }
}
