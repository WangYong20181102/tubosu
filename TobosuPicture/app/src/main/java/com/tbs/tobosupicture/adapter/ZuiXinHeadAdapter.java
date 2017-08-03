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
import com.tbs.tobosupicture.bean._ZuiXin;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/8/2 18:11.
 */

public class ZuiXinHeadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private ArrayList<_ZuiXin.ActiveUser> activeUserArrayList;

    public ZuiXinHeadAdapter(Context context, ArrayList<_ZuiXin.ActiveUser> activeUserArrayList) {
        this.mContext = context;
        this.activeUserArrayList = activeUserArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_zuire_head_item, parent, false);
        ZuiXinHeadItemHolder zuiReHeadItemHolder = new ZuiXinHeadItemHolder(view);
        return zuiReHeadItemHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ZuiXinHeadItemHolder) {
            GlideUtils.glideLoader(mContext, activeUserArrayList.get(position).getIcon(),
                    R.mipmap.default_icon, R.mipmap.default_icon,
                    ((ZuiXinHeadItemHolder) holder).reqiIcon, 0);
            ((ZuiXinHeadItemHolder) holder).reqiIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    class ZuiXinHeadItemHolder extends RecyclerView.ViewHolder {
        private ImageView reqiIcon;

        public ZuiXinHeadItemHolder(View itemView) {
            super(itemView);
            reqiIcon = (ImageView) itemView.findViewById(R.id.reqi_icon);
        }
    }
}
