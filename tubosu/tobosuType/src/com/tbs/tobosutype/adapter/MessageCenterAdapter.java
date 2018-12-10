package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.AnswerItemDetailsActivity;
import com.tbs.tobosutype.bean.MessageCenterBean;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/12/3 16:58.
 */
public class MessageCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<MessageCenterBean> messageCenterBeanList;
    private Context context;

    public MessageCenterAdapter(Context context, List<MessageCenterBean> messageCenterBeanList) {
        this.context = context;
        this.messageCenterBeanList = messageCenterBeanList;
    }

    private OnMessageCenterClickListener onMessageCenterClickListener;

    public void setOnMessageCenterClickListener(OnMessageCenterClickListener onMessageCenterClickListener) {
        this.onMessageCenterClickListener = onMessageCenterClickListener;
    }

    public static interface OnMessageCenterClickListener {
        void onClickPosition(int mPosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_center_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            //头像
            GlideUtils.glideLoader(context, messageCenterBeanList.get(position).getIcon(), R.drawable.iamge_loading, R.drawable.iamge_loading, ((MyViewHolder) holder).imageHeadIcon, 0);
            //消息红点
            if (messageCenterBeanList.get(position).getIs_see().equals("0")) {
                ((MyViewHolder) holder).vHotDot.setVisibility(View.VISIBLE);
            } else {
                ((MyViewHolder) holder).vHotDot.setVisibility(View.GONE);
            }
            //标题
            ((MyViewHolder) holder).tvTitle.setText(messageCenterBeanList.get(position).getTitle());
            //内容
            ((MyViewHolder) holder).tvContent.setText(messageCenterBeanList.get(position).getContent());
            //日期
            ((MyViewHolder) holder).tvData.setText(messageCenterBeanList.get(position).getAdd_time());
            ((MyViewHolder) holder).llMessageCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMessageCenterClickListener != null){
                        onMessageCenterClickListener.onClickPosition(position);
                        if (messageCenterBeanList.get(position).getIs_see().equals("0")){
                            ((MyViewHolder) holder).vHotDot.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return messageCenterBeanList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llMessageCenter;   //父布局
        private ImageView imageHeadIcon;    //头像
        private View vHotDot;//红点
        private TextView tvTitle;   //标题
        private TextView tvContent;//内容
        private TextView tvData;    //日期

        public MyViewHolder(View itemView) {
            super(itemView);
            imageHeadIcon = itemView.findViewById(R.id.image_head_icon);
            llMessageCenter = itemView.findViewById(R.id.ll_message_center);
            vHotDot = itemView.findViewById(R.id.view_hot_dot);
            tvTitle = itemView.findViewById(R.id.tv_tittle);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvData = itemView.findViewById(R.id.tv_data_time);
        }
    }
}
