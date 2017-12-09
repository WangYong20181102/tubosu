package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._CompanyDetail;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/11/30 16:23.
 * 装修公司主页 公司介绍适配器
 */

public class DecComJieshaoAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "DecComJieshaoAdapter";
    private ArrayList<String> imageUrls;

    public DecComJieshaoAdapter(Context context, ArrayList<String> imageUrls) {
        this.mContext = context;
        this.imageUrls = imageUrls;
    }

    public static interface OnDCJieshaoClickLister {
        void onItemClick(View view, int position);
    }

    private OnDCJieshaoClickLister onDCJieshaoClickLister = null;

    public void setOnDCJieshaoClickLister(OnDCJieshaoClickLister onDCJieshaoClickLister) {
        this.onDCJieshaoClickLister = onDCJieshaoClickLister;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dec_com_jieshao, parent, false);
        DCJSViewHolder dcjsViewHolder = new DCJSViewHolder(view);
        dcjsViewHolder.item_dec_com_jieshao_img_ll.setOnClickListener(this);
        return dcjsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DCJSViewHolder) {
            //设置图片
            GlideUtils.glideLoader(mContext, imageUrls.get(position), R.drawable.iamge_loading, R.drawable.iamge_loading, ((DCJSViewHolder) holder).item_dec_com_jieshao_img);
            //设置tag
            ((DCJSViewHolder) holder).item_dec_com_jieshao_img_ll.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    private class DCJSViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_dec_com_jieshao_img_ll;
        private ImageView item_dec_com_jieshao_img;

        public DCJSViewHolder(View itemView) {
            super(itemView);
            item_dec_com_jieshao_img_ll = itemView.findViewById(R.id.item_dec_com_jieshao_img_ll);
            item_dec_com_jieshao_img = itemView.findViewById(R.id.item_dec_com_jieshao_img);
        }
    }

    @Override
    public void onClick(View v) {
        if (onDCJieshaoClickLister != null) {
            onDCJieshaoClickLister.onItemClick(v, (int) v.getTag());
        }
    }
}
