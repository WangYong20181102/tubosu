package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.TopicDetailActivity;
import com.tbs.tobosutype.bean._HomePage;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/9/5 11:23.
 * 装修专题的适配器
 */
public class HomePageTopicAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private String TAG = "HomePageTopicAdapter";
    private List<_HomePage.DataBean.TopicBean> mTopicBeanList;

    public HomePageTopicAdapter(Context context, List<_HomePage.DataBean.TopicBean> topicBeanList) {
        this.mContext = context;
        this.mTopicBeanList = topicBeanList;
        Log.e(TAG, "专题的集合长度======" + topicBeanList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_page_topicle, parent, false);
        HPFootViewHolder hpFootViewHolder = new HPFootViewHolder(view);
        return hpFootViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HPFootViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return mTopicBeanList != null ? mTopicBeanList.size() : 0;
    }

    private class HPFootViewHolder extends RecyclerView.ViewHolder {
        private CardView item_hp_foot_cv;//整个布局
        private TextView item_hp_foot_time_tv;//时间
        private TextView item_hp_foot_title_tv;//标题
        private TextView item_hp_foot_dec_tv;//文本描述
        private ImageView item_hp_foot_image_img;//图片

        public HPFootViewHolder(View itemView) {
            super(itemView);
            item_hp_foot_cv = itemView.findViewById(R.id.item_hp_foot_cv);
            item_hp_foot_time_tv = itemView.findViewById(R.id.item_hp_foot_time_tv);
            item_hp_foot_title_tv = itemView.findViewById(R.id.item_hp_foot_title_tv);
            item_hp_foot_dec_tv = itemView.findViewById(R.id.item_hp_foot_dec_tv);
            item_hp_foot_image_img = itemView.findViewById(R.id.item_hp_foot_image_img);
        }
    }
}
