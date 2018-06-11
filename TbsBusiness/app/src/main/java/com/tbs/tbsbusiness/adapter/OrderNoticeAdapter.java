package com.tbs.tbsbusiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.bean._OrderNotice;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/6/11 09:44.
 */
public class OrderNoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "OrderNoticeAdapter";
    private ArrayList<_OrderNotice> mOrderNoticeArrayList;

    public static interface OnOrderNoticeClickLister {
        void onItemClick(View view, int position);
    }

    private OnOrderNoticeClickLister onOrderNoticeClickLister = null;

    public void setOnOrderNoticeClickLister(OnOrderNoticeClickLister onOrderNoticeClickLister) {
        this.onOrderNoticeClickLister = onOrderNoticeClickLister;
    }

    public OrderNoticeAdapter(Context context, ArrayList<_OrderNotice> orderNoticeArrayList) {
        this.mContext = context;
        this.mOrderNoticeArrayList = orderNoticeArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_notice, parent, false);
        OrderNoticeItemViewHolder orderNoticeItemViewHolder = new OrderNoticeItemViewHolder(view);
        //设置点击事件
        orderNoticeItemViewHolder.item_order_notice_rl.setOnClickListener(this);
        return orderNoticeItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderNoticeItemViewHolder) {
            //设置TAG
            ((OrderNoticeItemViewHolder) holder).item_order_notice_rl.setTag(position);
            //设置时间
            ((OrderNoticeItemViewHolder) holder).item_order_notice_time_tv.setText("" + mOrderNoticeArrayList.get(position).getAdd_time());
            //设置是否是新信息
            if (mOrderNoticeArrayList.get(position).getIs_read().equals("0")) {
                //新信息
                ((OrderNoticeItemViewHolder) holder).item_order_notice_view.setVisibility(View.VISIBLE);
            } else {
                ((OrderNoticeItemViewHolder) holder).item_order_notice_view.setVisibility(View.GONE);
            }
            //设置内容
            ((OrderNoticeItemViewHolder) holder).item_order_notice_conten_tv.setText("" + mOrderNoticeArrayList.get(position).getContent());
            //设置消息的类型  新消息 客服反馈消息
            if (mOrderNoticeArrayList.get(position).getNotice_type().equals("4")) {
                //普通订单消息
                ((OrderNoticeItemViewHolder) holder).item_order_type.setText("新订单");
            } else if (mOrderNoticeArrayList.get(position).getNotice_type().equals("5")) {
                //客服反馈消息
                ((OrderNoticeItemViewHolder) holder).item_order_type.setText("订单" + mOrderNoticeArrayList.get(position).getOrder_id());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mOrderNoticeArrayList != null ? mOrderNoticeArrayList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (onOrderNoticeClickLister != null && !mOrderNoticeArrayList.isEmpty()) {
            onOrderNoticeClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class OrderNoticeItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_order_notice_rl;//整个可点击的层级
        private View item_order_notice_view;//红点提示
        private TextView item_order_notice_time_tv;//时间
        private TextView item_order_notice_conten_tv;//通知的内容
        private TextView item_order_type;//显示消息类型

        public OrderNoticeItemViewHolder(View itemView) {
            super(itemView);
            item_order_notice_rl = itemView.findViewById(R.id.item_order_notice_rl);
            item_order_notice_view = itemView.findViewById(R.id.item_order_notice_view);
            item_order_notice_time_tv = itemView.findViewById(R.id.item_order_notice_time_tv);
            item_order_notice_conten_tv = itemView.findViewById(R.id.item_order_notice_conten_tv);
            item_order_type = itemView.findViewById(R.id.item_order_type);
        }
    }
}
