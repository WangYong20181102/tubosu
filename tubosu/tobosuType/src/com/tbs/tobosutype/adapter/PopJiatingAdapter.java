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
 * Created by Mr.Lin on 2018/9/11 11:20.
 */
public class PopJiatingAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "PopJiatingAdapter";
    private List<_CheckInfo.DataBean.MoreBean.SubTitleBean> mSubTitleBeanList;
    private int mPosition;

    public static interface onJiatingItemClickLister {
        void onItemClick(View view, int position);
    }

    private onJiatingItemClickLister onJiatingItemClickLister = null;

    public void setOnJiatingItemClickLister(PopJiatingAdapter.onJiatingItemClickLister onJiatingItemClickLister) {
        this.onJiatingItemClickLister = onJiatingItemClickLister;
    }

    public PopJiatingAdapter(Context context, List<_CheckInfo.DataBean.MoreBean.SubTitleBean> subTitleBeanList, int position) {
        this.mContext = context;
        this.mSubTitleBeanList = subTitleBeanList;
        this.mPosition = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pop_quanbu, parent, false);
        PopGengduoViewHolder popGengduoViewHolder = new PopGengduoViewHolder(view);
        popGengduoViewHolder.item_pop_quanbu_tv.setOnClickListener(this);
        return popGengduoViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PopGengduoViewHolder) {
            ((PopGengduoViewHolder) holder).item_pop_quanbu_tv.setTag(position);
            ((PopGengduoViewHolder) holder).item_pop_quanbu_tv.setText("" + mSubTitleBeanList.get(position).getName());
            if (mPosition != -1 && mPosition == position) {
                ((PopGengduoViewHolder) holder).item_pop_quanbu_tv.setTextColor(Color.parseColor("#ff6b14"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#ff6b14"));
                gradientDrawable.setCornerRadius(40);
                gradientDrawable.setColor(Color.parseColor("#FFFFF4ED"));
                ((PopGengduoViewHolder) holder).item_pop_quanbu_tv.setBackgroundDrawable(gradientDrawable);
            } else if (mPosition == -1) {
                ((PopGengduoViewHolder) holder).item_pop_quanbu_tv.setTextColor(Color.parseColor("#999999"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#FFEEEEEE"));
                gradientDrawable.setCornerRadius(40);
                gradientDrawable.setColor(Color.parseColor("#ffffff"));
                ((PopGengduoViewHolder) holder).item_pop_quanbu_tv.setBackgroundDrawable(gradientDrawable);

            }
        }
    }

    @Override
    public int getItemCount() {
        return mSubTitleBeanList == null ? 0 : mSubTitleBeanList.size();
    }

    @Override
    public void onClick(View v) {
        if (onJiatingItemClickLister != null) {
            onJiatingItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class PopGengduoViewHolder extends RecyclerView.ViewHolder {
        private TextView item_pop_quanbu_tv;

        public PopGengduoViewHolder(View itemView) {
            super(itemView);
            item_pop_quanbu_tv = itemView.findViewById(R.id.item_pop_quanbu_tv);
        }
    }
}
