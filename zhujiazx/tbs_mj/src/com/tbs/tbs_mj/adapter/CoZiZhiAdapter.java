package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._CompanyDetail;
import com.tbs.tbs_mj.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/12/1 16:25.
 * 公司资质的适配器
 * 作用于公司资质列表
 */

public class CoZiZhiAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private String TAG = "CoZiZhiAdapter";
    private Context mContext;
    private ArrayList<_CompanyDetail.QualificationBean> qualificationBeanArrayList;

    public static interface OnZiZhiItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnZiZhiItemClickLister onZiZhiItemClickLister = null;

    public void setOnZiZhiItemClickLister(OnZiZhiItemClickLister onZiZhiItemClickLister) {
        this.onZiZhiItemClickLister = onZiZhiItemClickLister;
    }

    public CoZiZhiAdapter(Context context, ArrayList<_CompanyDetail.QualificationBean> qualificationBeanArrayList) {
        this.mContext = context;
        this.qualificationBeanArrayList = qualificationBeanArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_co_zizhi, parent, false);
        CoZizhiViewHolder holder = new CoZizhiViewHolder(view);
        holder.item_co_zizhi_image_ll.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CoZizhiViewHolder) {
            //设置点击tag
            ((CoZizhiViewHolder) holder).item_co_zizhi_image_ll.setTag(position);
            //设置名称
            ((CoZizhiViewHolder) holder).item_co_zizhi_name.setText(qualificationBeanArrayList.get(position).getIntroduction());
            //设置图片
            GlideUtils.glideLoader(mContext, qualificationBeanArrayList.get(position).getImg_url(),
                    R.drawable.iamge_loading, R.drawable.iamge_loading,
                    ((CoZizhiViewHolder) holder).item_co_zizhi_image);
        }
    }

    @Override
    public int getItemCount() {
        return qualificationBeanArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (onZiZhiItemClickLister != null) {
            onZiZhiItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class CoZizhiViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_co_zizhi_image;
        private LinearLayout item_co_zizhi_image_ll;
        private TextView item_co_zizhi_name;

        public CoZizhiViewHolder(View itemView) {
            super(itemView);
            item_co_zizhi_image = itemView.findViewById(R.id.item_co_zizhi_image);
            item_co_zizhi_image_ll = itemView.findViewById(R.id.item_co_zizhi_image_ll);
            item_co_zizhi_name = itemView.findViewById(R.id.item_co_zizhi_name);
        }
    }
}
