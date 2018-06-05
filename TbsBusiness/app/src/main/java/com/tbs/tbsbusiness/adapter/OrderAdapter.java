package com.tbs.tbsbusiness.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.bean._OrderItem;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/6/5 15:19.
 */
public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "OrderAdapter";
    private ArrayList<_OrderItem> mOrderItemArrayList;

    //单项点击事件
    public static interface OnOrderItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnOrderItemClickLister onOrderItemClickLister = null;

    public void setOnOrderItemClickLister(OnOrderItemClickLister onOrderItemClickLister) {
        this.onOrderItemClickLister = onOrderItemClickLister;
    }

    public OrderAdapter(Context context, ArrayList<_OrderItem> orderItemArrayList) {
        this.mContext = context;
        this.mOrderItemArrayList = orderItemArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
        OrderItemViewHolder orderItemViewHolder = new OrderItemViewHolder(view);
        //整个层级设置点击事件
        orderItemViewHolder.item_order_all_ll.setOnClickListener(this);
        orderItemViewHolder.item_order_right_01.setOnClickListener(this);
        orderItemViewHolder.item_order_right_02.setOnClickListener(this);
        orderItemViewHolder.item_order_right_03.setOnClickListener(this);
        return orderItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderItemViewHolder) {
            //设置TAG
            ((OrderItemViewHolder) holder).item_order_all_ll.setTag(position);
            ((OrderItemViewHolder) holder).item_order_right_01.setTag(position);
            ((OrderItemViewHolder) holder).item_order_right_02.setTag(position);
            ((OrderItemViewHolder) holder).item_order_right_03.setTag(position);
            //设置分单时间
            ((OrderItemViewHolder) holder).item_order_time_tv.setText("分单日期: " + mOrderItemArrayList.get(position).getAdd_time());
            //设置订单类型
            if (mOrderItemArrayList.get(position).getState().equals("1")) {
                ((OrderItemViewHolder) holder).item_order_state.setText("新订单");
                ((OrderItemViewHolder) holder).item_order_state.setTextColor(Color.parseColor("#ff2414"));
                ((OrderItemViewHolder) holder).item_order_right_01.setText("查看");
                ((OrderItemViewHolder) holder).item_order_right_01.setTextColor(Color.parseColor("#ff6c14"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.parseColor("#ffffff"));
                gradientDrawable.setCornerRadius(50);
                gradientDrawable.setStroke(1, Color.parseColor("#ff6c14"));
                ((OrderItemViewHolder) holder).item_order_right_01.setBackgroundDrawable(gradientDrawable);
                ((OrderItemViewHolder) holder).item_order_right_02.setVisibility(View.GONE);
                ((OrderItemViewHolder) holder).item_order_right_03.setVisibility(View.GONE);
            } else if (mOrderItemArrayList.get(position).getState().equals("2")) {
                ((OrderItemViewHolder) holder).item_order_right_02.setVisibility(View.VISIBLE);
                ((OrderItemViewHolder) holder).item_order_right_03.setVisibility(View.VISIBLE);
                //标签的变化
                ((OrderItemViewHolder) holder).item_order_state.setText("未量房");
                ((OrderItemViewHolder) holder).item_order_state.setTextColor(Color.parseColor("#ff6c14"));
                //右一的按钮
                ((OrderItemViewHolder) holder).item_order_right_01.setText("确认量房");
                ((OrderItemViewHolder) holder).item_order_right_01.setTextColor(Color.parseColor("#ff6c14"));
                GradientDrawable gradientDrawable01 = new GradientDrawable();
                gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
                gradientDrawable01.setCornerRadius(50);//角度
                gradientDrawable01.setStroke(1, Color.parseColor("#ff6c14"));//边框以及颜色
                ((OrderItemViewHolder) holder).item_order_right_01.setBackgroundDrawable(gradientDrawable01);
                //中间的按钮
                ((OrderItemViewHolder) holder).item_order_right_02.setText("量房失败");
                ((OrderItemViewHolder) holder).item_order_right_02.setTextColor(Color.parseColor("#666666"));
                GradientDrawable gradientDrawable02 = new GradientDrawable();
                gradientDrawable02.setColor(Color.parseColor("#ffffff"));//背景
                gradientDrawable02.setCornerRadius(50);//角度
                gradientDrawable02.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
                ((OrderItemViewHolder) holder).item_order_right_02.setBackgroundDrawable(gradientDrawable02);
                //左侧按钮
                ((OrderItemViewHolder) holder).item_order_right_03.setText("联系业主");
                ((OrderItemViewHolder) holder).item_order_right_03.setTextColor(Color.parseColor("#666666"));
                GradientDrawable gradientDrawable03 = new GradientDrawable();
                gradientDrawable03.setColor(Color.parseColor("#ffffff"));//背景
                gradientDrawable03.setCornerRadius(50);//角度
                gradientDrawable03.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
                ((OrderItemViewHolder) holder).item_order_right_03.setBackgroundDrawable(gradientDrawable03);
            } else if (mOrderItemArrayList.get(position).getState().equals("3")) {
                ((OrderItemViewHolder) holder).item_order_right_02.setVisibility(View.VISIBLE);
                ((OrderItemViewHolder) holder).item_order_right_03.setVisibility(View.VISIBLE);
                //标签的变化
                ((OrderItemViewHolder) holder).item_order_state.setText("已量房");
                ((OrderItemViewHolder) holder).item_order_state.setTextColor(Color.parseColor("#ff6c14"));
                //右一的按钮
                ((OrderItemViewHolder) holder).item_order_right_01.setText("确认签单");
                ((OrderItemViewHolder) holder).item_order_right_01.setTextColor(Color.parseColor("#ff6c14"));
                GradientDrawable gradientDrawable01 = new GradientDrawable();
                gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
                gradientDrawable01.setCornerRadius(50);//角度
                gradientDrawable01.setStroke(1, Color.parseColor("#ff6c14"));//边框以及颜色
                ((OrderItemViewHolder) holder).item_order_right_01.setBackgroundDrawable(gradientDrawable01);
                //中间的按钮
                ((OrderItemViewHolder) holder).item_order_right_02.setText("未签单");
                ((OrderItemViewHolder) holder).item_order_right_02.setTextColor(Color.parseColor("#666666"));
                GradientDrawable gradientDrawable02 = new GradientDrawable();
                gradientDrawable02.setColor(Color.parseColor("#ffffff"));//背景
                gradientDrawable02.setCornerRadius(50);//角度
                gradientDrawable02.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
                ((OrderItemViewHolder) holder).item_order_right_02.setBackgroundDrawable(gradientDrawable02);
                //左侧按钮
                ((OrderItemViewHolder) holder).item_order_right_03.setText("联系业主");
                ((OrderItemViewHolder) holder).item_order_right_03.setTextColor(Color.parseColor("#666666"));
                GradientDrawable gradientDrawable03 = new GradientDrawable();
                gradientDrawable03.setColor(Color.parseColor("#ffffff"));//背景
                gradientDrawable03.setCornerRadius(50);//角度
                gradientDrawable03.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
                ((OrderItemViewHolder) holder).item_order_right_03.setBackgroundDrawable(gradientDrawable03);
            } else if (mOrderItemArrayList.get(position).getState().equals("4")) {
                //标签的变化
                //标签的变化
                ((OrderItemViewHolder) holder).item_order_state.setText("已签单");
                ((OrderItemViewHolder) holder).item_order_state.setTextColor(Color.parseColor("#ff6c14"));
                //右一的按钮
                ((OrderItemViewHolder) holder).item_order_right_01.setText("联系业主");
                ((OrderItemViewHolder) holder).item_order_right_01.setTextColor(Color.parseColor("#666666"));
                GradientDrawable gradientDrawable01 = new GradientDrawable();
                gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
                gradientDrawable01.setCornerRadius(50);//角度
                gradientDrawable01.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
                ((OrderItemViewHolder) holder).item_order_right_01.setBackgroundDrawable(gradientDrawable01);
                ((OrderItemViewHolder) holder).item_order_right_02.setVisibility(View.GONE);
                ((OrderItemViewHolder) holder).item_order_right_03.setVisibility(View.GONE);

            } else if (mOrderItemArrayList.get(position).getState().equals("5")) {

                ((OrderItemViewHolder) holder).item_order_state.setText("未签单");
                ((OrderItemViewHolder) holder).item_order_state.setTextColor(Color.parseColor("#ff6c14"));
                //右一的按钮
                ((OrderItemViewHolder) holder).item_order_right_01.setText("反馈");
                ((OrderItemViewHolder) holder).item_order_right_01.setTextColor(Color.parseColor("#666666"));
                GradientDrawable gradientDrawable01 = new GradientDrawable();
                gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
                gradientDrawable01.setCornerRadius(50);//角度
                gradientDrawable01.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
                ((OrderItemViewHolder) holder).item_order_right_01.setBackgroundDrawable(gradientDrawable01);
                ((OrderItemViewHolder) holder).item_order_right_02.setVisibility(View.GONE);
                ((OrderItemViewHolder) holder).item_order_right_03.setVisibility(View.GONE);
            }
            //设置是否赠送
            if (mOrderItemArrayList.get(position).getIs_give().equals("1")) {
                ((OrderItemViewHolder) holder).item_order_order_zeng_iv.setVisibility(View.VISIBLE);
            } else {
                ((OrderItemViewHolder) holder).item_order_order_zeng_iv.setVisibility(View.GONE);
            }
            //设置区域
            ((OrderItemViewHolder) holder).item_order_qu_yu.setText("" + mOrderItemArrayList.get(position).getDistrict_name());
            //设置面积
            ((OrderItemViewHolder) holder).item_order_mian_ji.setText("" + mOrderItemArrayList.get(position).getArea());
            //设置户型
            ((OrderItemViewHolder) holder).item_order_hu_xing.setText("" + mOrderItemArrayList.get(position).getLayout_name());
            //设置房屋类型
            ((OrderItemViewHolder) holder).item_order_lei_xing.setText("" + mOrderItemArrayList.get(position).getHouse_type());
            //设置小区
            ((OrderItemViewHolder) holder).item_order_xiao_qu.setText("" + mOrderItemArrayList.get(position).getCommunity_name());

        }
    }

    @Override
    public int getItemCount() {
        return mOrderItemArrayList != null ? mOrderItemArrayList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (onOrderItemClickLister != null && !mOrderItemArrayList.isEmpty()) {
            onOrderItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_order_all_ll;//整个层级
        private TextView item_order_time_tv;//分单日期
        private TextView item_order_state;//订单的状态
        private TextView item_order_qu_yu;//订单的区域
        private TextView item_order_mian_ji;//面积
        private TextView item_order_hu_xing;//户型
        private TextView item_order_lei_xing;//房屋类型
        private TextView item_order_xiao_qu;//小区
        private TextView item_order_right_01;//动态变化按钮
        private TextView item_order_right_02;//动态变化按钮
        private TextView item_order_right_03;//动态变化按钮
        private ImageView item_order_order_zeng_iv;//是否是赠送订单的图标

        public OrderItemViewHolder(View itemView) {
            super(itemView);
            item_order_all_ll = itemView.findViewById(R.id.item_order_all_ll);
            item_order_time_tv = itemView.findViewById(R.id.item_order_time_tv);
            item_order_state = itemView.findViewById(R.id.item_order_state);
            item_order_qu_yu = itemView.findViewById(R.id.item_order_qu_yu);
            item_order_mian_ji = itemView.findViewById(R.id.item_order_mian_ji);
            item_order_hu_xing = itemView.findViewById(R.id.item_order_hu_xing);
            item_order_lei_xing = itemView.findViewById(R.id.item_order_lei_xing);
            item_order_xiao_qu = itemView.findViewById(R.id.item_order_xiao_qu);
            item_order_right_01 = itemView.findViewById(R.id.item_order_right_01);
            item_order_right_02 = itemView.findViewById(R.id.item_order_right_02);
            item_order_right_03 = itemView.findViewById(R.id.item_order_right_03);
            item_order_order_zeng_iv = itemView.findViewById(R.id.item_order_order_zeng_iv);

        }
    }
}
