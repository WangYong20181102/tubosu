package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.HistoryRecordBean;
import com.tbs.tobosutype.utils.MoneyFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Wang on 2019/1/2 14:19.
 */
public class HistoryRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HistoryRecordBean> recordBeanList;
    private boolean bEdit = false;
    private OnItenClickListener onItenClickListener;
    private OnReNewClickListener onReNewClickListener;
    private int type;

    public HistoryRecordAdapter(Context context, List<HistoryRecordBean> recordBeanList, int type) {
        this.context = context;
        this.recordBeanList = recordBeanList;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_record_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            if (getItemCount() > 0 && position == 0) {
                ((MyViewHolder) holder).viewTopBg.setVisibility(View.VISIBLE);
            } else {
                ((MyViewHolder) holder).viewTopBg.setVisibility(View.GONE);
            }
            if (bEdit) {
                ((MyViewHolder) holder).imageCheck.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).btnRecalculate.setClickable(false);
                ((MyViewHolder) holder).btnRecalculate.setEnabled(false);
                ((MyViewHolder) holder).btnRecalculate.setTextColor(Color.parseColor("#999999"));
                if (recordBeanList.get(position).isCheck()) {
                    ((MyViewHolder) holder).imageCheck.setImageResource(R.drawable.edit_selected);
                } else {
                    ((MyViewHolder) holder).imageCheck.setImageResource(R.drawable.edit_unselected);
                }
                ((MyViewHolder) holder).imageCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItenClickListener.onItemClickListener(holder.getAdapterPosition(), recordBeanList);
                    }
                });

            } else {
                ((MyViewHolder) holder).imageCheck.setVisibility(View.GONE);
                ((MyViewHolder) holder).btnRecalculate.setClickable(true);
                ((MyViewHolder) holder).btnRecalculate.setEnabled(true);
                ((MyViewHolder) holder).btnRecalculate.setTextColor(Color.WHITE);
            }
            //数量
            if (type == 1 || type == 2 || type == 3) { //地砖、墙砖、地板
                ((MyViewHolder) holder).tvNum.setText(recordBeanList.get(position).getNumber() + "块");
            } else if (type == 4) { //壁纸
                ((MyViewHolder) holder).tvNum.setText(recordBeanList.get(position).getNumber() + "卷");
            } else if (type == 5) { //涂料
                ((MyViewHolder) holder).tvNum.setText(recordBeanList.get(position).getNumber() + "升");
            } else if (type == 6) { //窗帘
                ((MyViewHolder) holder).tvNum.setText(recordBeanList.get(position).getNumber() + "米");
            }

            //日期
            ((MyViewHolder) holder).tvDataTime.setText(recordBeanList.get(position).getAdd_time());
            //价格
            if (recordBeanList.get(position).getTotal_price().trim().isEmpty() || recordBeanList.get(position).getTotal_price().trim().equals("0")) {
                ((MyViewHolder) holder).llPrice.setVisibility(View.GONE);
            } else {
                double price = Double.parseDouble(recordBeanList.get(position).getTotal_price());
                ((MyViewHolder) holder).llPrice.setVisibility(View.VISIBLE);
                if (price > 10000) {
                    ((MyViewHolder) holder).tvPrice.setText(MoneyFormatUtil.format2(price / 10000) + "万元");
                } else {
                    ((MyViewHolder) holder).tvPrice.setText(MoneyFormatUtil.format2(price) + "元");
                }
            }
            //点击重新计算发送广播，回传参数
            ((MyViewHolder) holder).btnRecalculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReNewClickListener.onReNewClick(recordBeanList.get(position));
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return recordBeanList.size();
    }

    public List<HistoryRecordBean> getHistoryRecordList() {
        if (recordBeanList == null) {
            recordBeanList = new ArrayList<>();
        }
        return recordBeanList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageCheck;//选中按钮
        private TextView tvNum;//数量
        private TextView tvPrice;//价格
        private LinearLayout llPrice;//价格父布局
        private TextView tvDataTime;//日期
        private Button btnRecalculate;//重新计算
        private View viewTopBg;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageCheck = itemView.findViewById(R.id.image_check);
            tvNum = itemView.findViewById(R.id.tv_num);
            tvPrice = itemView.findViewById(R.id.tv_price);
            llPrice = itemView.findViewById(R.id.linear_price);
            tvDataTime = itemView.findViewById(R.id.tv_data);
            btnRecalculate = itemView.findViewById(R.id.btn_recalculate);
            viewTopBg = itemView.findViewById(R.id.view_top_bg);
        }
    }

    /**
     * 是否是编辑状态
     */
    public void isEditState(boolean isEdit) {
        bEdit = isEdit;
        notifyDataSetChanged();
    }

    public interface OnItenClickListener {
        void onItemClickListener(int position, List<HistoryRecordBean> beanList);
    }

    public void setOnItenClickListener(OnItenClickListener onItenClickListener) {
        this.onItenClickListener = onItenClickListener;
    }

    public interface OnReNewClickListener {
        void onReNewClick(HistoryRecordBean recordBean);
    }

    public void setOnReNewClickListener(OnReNewClickListener onReNewClickListener) {
        this.onReNewClickListener = onReNewClickListener;
    }

}
