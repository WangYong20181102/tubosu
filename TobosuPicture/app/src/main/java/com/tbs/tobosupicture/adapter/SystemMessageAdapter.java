package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.WebViewActivity;
import com.tbs.tobosupicture.bean._SystemMessage;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/8/9 14:54.
 */

public class SystemMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<_SystemMessage> systemMessageList;
    private Context mContext;

    public SystemMessageAdapter(Context context, List<_SystemMessage> systemMessageList) {
        this.mContext = context;
        this.systemMessageList = systemMessageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_system_message, parent, false);
        SysmItemHolder sysmItemHolder = new SysmItemHolder(view);
        return sysmItemHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SysmItemHolder) {
            ((SysmItemHolder) holder).tv_time.setText(systemMessageList.get(position).getAdd_time());
            ((SysmItemHolder) holder).tv_title.setText(systemMessageList.get(position).getTitle());
            ((SysmItemHolder) holder).tv_content.setText(systemMessageList.get(position).getContent());
            if (systemMessageList.get(position).getIs_read().equals("1")) {
                //已读
                ((SysmItemHolder) holder).iv_isread.setVisibility(View.GONE);
            } else {
                ((SysmItemHolder) holder).iv_isread.setVisibility(View.VISIBLE);
            }
            ((SysmItemHolder) holder).iv_read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("web_url", systemMessageList.get(position).getUrl());
                    mContext.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return systemMessageList != null ? systemMessageList.size() : 0;
    }

    class SysmItemHolder extends RecyclerView.ViewHolder {
        private TextView tv_time;//时间
        private TextView tv_title;//标题
        private TextView tv_content;//消息内容
        private ImageView iv_isread;//是否已读的红点提示
        private ImageView iv_read;//点击查看按钮

        public SysmItemHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            iv_isread = (ImageView) itemView.findViewById(R.id.iv_isread);
            iv_read = (ImageView) itemView.findViewById(R.id.iv_read);
        }
    }
}
