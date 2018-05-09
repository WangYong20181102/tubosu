package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._MyStore;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/4/24 09:13.
 */
public class CoChangeServiceAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "CoChangeServiceAdapter";
    private ArrayList<_MyStore.ServiceAreaBean> mServiceAreaBeanArrayList;

    //子项点击事件
    public static interface OnItemClickLister {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    private OnItemClickLister onItemClickLister = null;

    public CoChangeServiceAdapter(Context context, ArrayList<_MyStore.ServiceAreaBean> serviceAreaBeanArrayList) {
        this.mContext = context;
        this.mServiceAreaBeanArrayList = serviceAreaBeanArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_co_change, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        itemViewHolder.item_co_change_rl.setOnClickListener(this);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            //设置TAG
            ((ItemViewHolder) holder).item_co_change_rl.setTag(position);
            //设置文本显示信息
            ((ItemViewHolder) holder).item_co_change_name_tv.setText("" + mServiceAreaBeanArrayList.get(position).getName());
            //设置状态
            if (mServiceAreaBeanArrayList.get(position).getIs_selected() == 1) {
                //选中状态
                ((ItemViewHolder) holder).item_co_change_name_tv.setTextColor(Color.parseColor("#ffffff"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.parseColor("#ff6b14"));
                gradientDrawable.setCornerRadius(10);
                ((ItemViewHolder) holder).item_co_change_rl.setBackground(gradientDrawable);
            } else {
                //未选择
                ((ItemViewHolder) holder).item_co_change_name_tv.setTextColor(Color.parseColor("#333333"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.parseColor("#f8f8f8"));
                gradientDrawable.setCornerRadius(10);
                ((ItemViewHolder) holder).item_co_change_rl.setBackground(gradientDrawable);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mServiceAreaBeanArrayList != null ? mServiceAreaBeanArrayList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null && !mServiceAreaBeanArrayList.isEmpty()) {
            onItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_co_change_rl;
        private TextView item_co_change_name_tv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_co_change_rl = itemView.findViewById(R.id.item_co_change_rl);
            item_co_change_name_tv = itemView.findViewById(R.id.item_co_change_name_tv);
        }
    }
}
