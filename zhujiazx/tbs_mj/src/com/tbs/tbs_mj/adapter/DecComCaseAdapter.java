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
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/11/30 15:24.
 * 作用于 装修公司主页的装修方案的适配器
 */

public class DecComCaseAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "DecComCaseAdapter";
    private ArrayList<_CompanyDetail.CasesBean> casesBeanArrayList;

    public DecComCaseAdapter(Context context, ArrayList<_CompanyDetail.CasesBean> casesBeans) {
        this.mContext = context;
        this.casesBeanArrayList = casesBeans;
    }

    public static interface OnDecComCaseItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnDecComCaseItemClickLister onDecComCaseItemClickLister = null;

    public void setOnDecComCaseItemClickLister(OnDecComCaseItemClickLister onDecComCaseItemClickLister) {
        this.onDecComCaseItemClickLister = onDecComCaseItemClickLister;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_dec_com_case, parent, false);
            DCCaseViewHolder dcCaseViewHolder = new DCCaseViewHolder(view);
            dcCaseViewHolder.item_dec_com_image_ll.setOnClickListener(this);
            return dcCaseViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_v_foot_view, parent, false);
            FootView footView = new FootView(view);
            return footView;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DCCaseViewHolder) {
            //封面图
            GlideUtils.glideLoader(mContext, casesBeanArrayList.get(position).getCover_url(),
                    R.drawable.iamge_loading, R.drawable.iamge_loading,
                    ((DCCaseViewHolder) holder).item_dec_com_image);
            //设置tag
            ((DCCaseViewHolder) holder).item_dec_com_image_ll.setTag(position);
            //设置标题
            ((DCCaseViewHolder) holder).item_dec_com_title.setText(casesBeanArrayList.get(position).getTitle());
            //设置子标题
            ((DCCaseViewHolder) holder).item_dec_com_desc.setText(""+casesBeanArrayList.get(position).getSub_title());
        }
    }

    @Override
    public int getItemCount() {
        return casesBeanArrayList.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (onDecComCaseItemClickLister != null) {
            onDecComCaseItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class DCCaseViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_dec_com_image;//封面图片
        private LinearLayout item_dec_com_image_ll;//封面套层
        private TextView item_dec_com_title;//名称
        private TextView item_dec_com_desc;//描述

        public DCCaseViewHolder(View itemView) {
            super(itemView);
            item_dec_com_image_ll = itemView.findViewById(R.id.item_dec_com_image_ll);
            item_dec_com_image = itemView.findViewById(R.id.item_dec_com_image);
            item_dec_com_title = itemView.findViewById(R.id.item_dec_com_title);
            item_dec_com_desc = itemView.findViewById(R.id.item_dec_com_desc);
        }
    }

    private class FootView extends RecyclerView.ViewHolder {
        private LinearLayout item_v_foot_view_ll;

        public FootView(View itemView) {
            super(itemView);
            item_v_foot_view_ll = itemView.findViewById(R.id.item_v_foot_view_ll);
        }
    }
}
