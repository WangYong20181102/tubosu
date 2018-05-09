package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._OrderFeedBackMsg;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/3/30 11:45.
 */
public class OrderFeedMsgBackAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private String TAG = "OrderFeedMsgBackAdapter";
    private ArrayList<_OrderFeedBackMsg> mOrderFeedBackMsgArrayList;

    public OrderFeedMsgBackAdapter(Context context, ArrayList<_OrderFeedBackMsg> orderFeedBackMsgArrayList) {
        this.mContext = context;
        this.mOrderFeedBackMsgArrayList = orderFeedBackMsgArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mOrderFeedBackMsgArrayList.get(position).getSend_type() == 1) {
            //返回发送的类型
            return 1;
        } else if (mOrderFeedBackMsgArrayList.get(position).getSend_type() == 2) {
            //返回接收类型
            return 2;
        } else {
            //返回时间
            return 3;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            //发送类型
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_back_send, parent, false);
            SendViewHolder sendViewHolder = new SendViewHolder(view);
            return sendViewHolder;
        } else if (viewType == 2) {
            //接收类型
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_back_receive, parent, false);
            ReceiveViewHolder receiveViewHolder = new ReceiveViewHolder(view);
            return receiveViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_back_time, parent, false);
            TiemViewHolder tiemViewHolder = new TiemViewHolder(view);
            return tiemViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SendViewHolder) {
            ((SendViewHolder) holder).item_feed_back_seed_tv.setText("" + mOrderFeedBackMsgArrayList.get(position).getContent());
        } else if (holder instanceof ReceiveViewHolder) {
            ((ReceiveViewHolder) holder).item_feed_back_receive_tv.setText("" + mOrderFeedBackMsgArrayList.get(position).getContent());
        } else if (holder instanceof TiemViewHolder) {
            ((TiemViewHolder) holder).item_feed_back_time_tv.setText("" + mOrderFeedBackMsgArrayList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mOrderFeedBackMsgArrayList.size();
    }

    //发送的ViewHolder
    private class SendViewHolder extends RecyclerView.ViewHolder {
        private TextView item_feed_back_seed_tv;

        public SendViewHolder(View itemView) {
            super(itemView);
            item_feed_back_seed_tv = itemView.findViewById(R.id.item_feed_back_seed_tv);
        }
    }

    //接收的ViewHolder
    private class ReceiveViewHolder extends RecyclerView.ViewHolder {
        private TextView item_feed_back_receive_tv;

        public ReceiveViewHolder(View itemView) {
            super(itemView);
            item_feed_back_receive_tv = itemView.findViewById(R.id.item_feed_back_receive_tv);
        }
    }

    //时间的ViewHolder
    private class TiemViewHolder extends RecyclerView.ViewHolder {
        private TextView item_feed_back_time_tv;

        public TiemViewHolder(View itemView) {
            super(itemView);
            item_feed_back_time_tv = itemView.findViewById(R.id.item_feed_back_time_tv);

        }
    }
}
