package com.tbs.tbsbusiness.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.bean._OrderDetail;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/6/6 09:52.
 */
public class OrderScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private String TAG = "OrderScheduleAdapter";
    private List<_OrderDetail.OrderTrackBean> mOrderTrackBeanList;

    public OrderScheduleAdapter(Context context, List<_OrderDetail.OrderTrackBean> orderTrackBeanList) {
        this.mContext = context;
        this.mOrderTrackBeanList = orderTrackBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_schedule, parent, false);
        OrderScheduleItemViewHolder orderScheduleItemViewHolder = new OrderScheduleItemViewHolder(view);
        return orderScheduleItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderScheduleItemViewHolder) {
            if (position == 0) {
                ((OrderScheduleItemViewHolder) holder).item_order_sd_state_tv.setTextColor(Color.parseColor("#ff6c14"));
                ((OrderScheduleItemViewHolder) holder).item_order_sd_up_dev_view.setVisibility(View.GONE);
                ((OrderScheduleItemViewHolder) holder).item_order_sd_sigin_iv.setImageResource(R.drawable.xuanzhong);
            } else {
                ((OrderScheduleItemViewHolder) holder).item_order_sd_state_tv.setTextColor(Color.parseColor("#333333"));
                ((OrderScheduleItemViewHolder) holder).item_order_sd_sigin_iv.setImageResource(R.drawable.weixuanzhong);
                ((OrderScheduleItemViewHolder) holder).item_order_sd_up_dev_view.setVisibility(View.VISIBLE);
            }
            if (mOrderTrackBeanList != null && position == mOrderTrackBeanList.size() - 1) {
                //隐藏底部的虚线以及下部的线
                ((OrderScheduleItemViewHolder) holder).item_order_sd_down_dev_view.setVisibility(View.GONE);
                ((OrderScheduleItemViewHolder) holder).item_order_sd_button_xuxian.setVisibility(View.GONE);
            }
            //设置订单状态
            ((OrderScheduleItemViewHolder) holder).item_order_sd_state_tv.setText("" + mOrderTrackBeanList.get(position).getTitle());
            //设置时间
            ((OrderScheduleItemViewHolder) holder).item_order_sd_time_tv.setText("" + mOrderTrackBeanList.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return mOrderTrackBeanList != null ? mOrderTrackBeanList.size() : 0;
    }

    private class OrderScheduleItemViewHolder extends RecyclerView.ViewHolder {
        private View item_order_sd_up_dev_view;//进度上方的分割线标识
        private View item_order_sd_down_dev_view;//进度左下方的分割线标识
        private View item_order_sd_button_xuxian;//底部虚线
        private ImageView item_order_sd_sigin_iv;//进度图标
        private TextView item_order_sd_state_tv;//显示进度状态
        private TextView item_order_sd_time_tv;//进度时间

        public OrderScheduleItemViewHolder(View itemView) {
            super(itemView);
            item_order_sd_up_dev_view = itemView.findViewById(R.id.item_order_sd_up_dev_view);
            item_order_sd_down_dev_view = itemView.findViewById(R.id.item_order_sd_down_dev_view);
            item_order_sd_button_xuxian = itemView.findViewById(R.id.item_order_sd_button_xuxian);
            item_order_sd_sigin_iv = itemView.findViewById(R.id.item_order_sd_sigin_iv);
            item_order_sd_state_tv = itemView.findViewById(R.id.item_order_sd_state_tv);
            item_order_sd_time_tv = itemView.findViewById(R.id.item_order_sd_time_tv);
        }
    }
}