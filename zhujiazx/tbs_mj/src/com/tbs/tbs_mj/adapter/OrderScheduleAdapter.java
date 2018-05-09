package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._OrderDetail;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/3/26 14:55.
 * 订单进度适配器
 * 3.7.2版本新增
 */

public class OrderScheduleAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
        private ImageView item_order_sd_sigin_iv;//进度图标
        private TextView item_order_sd_state_tv;//显示进度状态
        private TextView item_order_sd_time_tv;//进度时间

        public OrderScheduleItemViewHolder(View itemView) {
            super(itemView);
            item_order_sd_up_dev_view = itemView.findViewById(R.id.item_order_sd_up_dev_view);
            item_order_sd_down_dev_view = itemView.findViewById(R.id.item_order_sd_down_dev_view);
            item_order_sd_sigin_iv = itemView.findViewById(R.id.item_order_sd_sigin_iv);
            item_order_sd_state_tv = itemView.findViewById(R.id.item_order_sd_state_tv);
            item_order_sd_time_tv = itemView.findViewById(R.id.item_order_sd_time_tv);
        }
    }
}
