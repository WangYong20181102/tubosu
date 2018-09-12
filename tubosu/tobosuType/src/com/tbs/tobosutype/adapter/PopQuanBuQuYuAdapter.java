package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._CheckInfo;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/9/10 17:49.
 */
public class PopQuanBuQuYuAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "PopQuanBuQuYuAdapter";
    private List<_CheckInfo.DataBean.DistrictIdBean> mDistrictIdBeanList;
    private int mPosition;//用于回显的下标位置

    public static interface onPopQuanBuItemClickLister {
        void onItemClick(View view, int position);
    }

    private onPopQuanBuItemClickLister onPopQuanBuItemClickLister = null;

    public void setOnPopQuanBuItemClickLister(PopQuanBuQuYuAdapter.onPopQuanBuItemClickLister onPopQuanBuItemClickLister) {
        this.onPopQuanBuItemClickLister = onPopQuanBuItemClickLister;
    }

    public PopQuanBuQuYuAdapter(Context context, List<_CheckInfo.DataBean.DistrictIdBean> districtIdBeanList, int position) {
        this.mContext = context;
        this.mDistrictIdBeanList = districtIdBeanList;
        this.mPosition = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pop_quanbu, parent, false);
        PopQuanBuViewHolder popQuanBuViewHolder = new PopQuanBuViewHolder(view);
        popQuanBuViewHolder.item_pop_quanbu_tv.setOnClickListener(this);

        return popQuanBuViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PopQuanBuViewHolder) {
            ((PopQuanBuViewHolder) holder).item_pop_quanbu_tv.setTag(position);
            ((PopQuanBuViewHolder) holder).item_pop_quanbu_tv.setText("" + mDistrictIdBeanList.get(position).getName());
            if (mPosition != -1 && mPosition == position) {
                ((PopQuanBuViewHolder) holder).item_pop_quanbu_tv.setTextColor(Color.parseColor("#ff6b14"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#ff6b14"));
                gradientDrawable.setCornerRadius(40);
                gradientDrawable.setColor(Color.parseColor("#FFFFF4ED"));
                ((PopQuanBuViewHolder) holder).item_pop_quanbu_tv.setBackgroundDrawable(gradientDrawable);
            } else if (mPosition == -1) {
                ((PopQuanBuViewHolder) holder).item_pop_quanbu_tv.setTextColor(Color.parseColor("#999999"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#FFEEEEEE"));
                gradientDrawable.setCornerRadius(40);
                gradientDrawable.setColor(Color.parseColor("#ffffff"));
                ((PopQuanBuViewHolder) holder).item_pop_quanbu_tv.setBackgroundDrawable(gradientDrawable);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDistrictIdBeanList == null ? 0 : mDistrictIdBeanList.size();
    }

    @Override
    public void onClick(View v) {
        if (onPopQuanBuItemClickLister != null) {
            onPopQuanBuItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class PopQuanBuViewHolder extends RecyclerView.ViewHolder {
        private TextView item_pop_quanbu_tv;

        public PopQuanBuViewHolder(View itemView) {
            super(itemView);
            item_pop_quanbu_tv = itemView.findViewById(R.id.item_pop_quanbu_tv);
        }
    }
}
