package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.bean._ZuiRe;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/7/17 17:59.
 * 以图会友最热 人气榜 适配器
 */

public class ZuiReHeadAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private ArrayList<_ZuiRe.ActiveUser> activeUserArrayList;

    public ZuiReHeadAdapter(Context context, ArrayList<_ZuiRe.ActiveUser> activeUserArrayList) {
        this.mContext = context;
        this.activeUserArrayList = activeUserArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_zuire_head_item, parent, false);
        ZuiReHeadItemHolder zuiReHeadItemHolder = new ZuiReHeadItemHolder(view);
        return zuiReHeadItemHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ZuiReHeadItemHolder) {
            GlideUtils.glideLoader(mContext, activeUserArrayList.get(position).getIcon(),
                    R.mipmap.default_icon, R.mipmap.default_icon,
                    ((ZuiReHeadItemHolder) holder).reqiIcon, 0);
            ((ZuiReHeadItemHolder) holder).reqiIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 点击人气榜的头像进入详情页
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", activeUserArrayList.get(position).getUid());
                    intent.putExtra("is_virtual_user", activeUserArrayList.get(position).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return activeUserArrayList != null ? activeUserArrayList.size() : 0;
    }

    @Override
    public void onClick(View v) {

    }

    class ZuiReHeadItemHolder extends RecyclerView.ViewHolder {
        private ImageView reqiIcon;

        public ZuiReHeadItemHolder(View itemView) {
            super(itemView);
            reqiIcon = (ImageView) itemView.findViewById(R.id.reqi_icon);
        }
    }
}
