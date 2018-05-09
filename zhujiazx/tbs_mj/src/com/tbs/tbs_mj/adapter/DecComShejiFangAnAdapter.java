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
 * Created by Mr.Lin on 2017/11/30 15:30.
 * 装修公司主页设计方案（横向列表）适配器
 */

public class DecComShejiFangAnAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "DecComShejiFangAnAdapter";
    private ArrayList<_CompanyDetail.SuitesBean> suitesBeanArrayList;

    public DecComShejiFangAnAdapter(Context context, ArrayList<_CompanyDetail.SuitesBean> suitesBeans) {
        this.mContext = context;
        this.suitesBeanArrayList = suitesBeans;
    }

    public static interface OnDCShejiItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnDCShejiItemClickLister onDCShejiItemClickLister = null;

    public void setOnDCShejiItemClickLister(OnDCShejiItemClickLister onDCShejiItemClickLister) {
        this.onDCShejiItemClickLister = onDCShejiItemClickLister;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//脚部层
        } else {
            return 2;//普通图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_dec_com_case, parent, false);
            DCShejiViewHolder dcShejiViewHolder = new DCShejiViewHolder(view);
            //设置控件的点击事件
            dcShejiViewHolder.item_dec_com_image_ll.setOnClickListener(this);
            return dcShejiViewHolder;
        } else {
            //脚部层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_v_foot_view, parent, false);
            FootView footView = new FootView(view);
            return footView;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DCShejiViewHolder) {
            //封面图
            GlideUtils.glideLoader(mContext, suitesBeanArrayList.get(position).getCover_url(),
                    R.drawable.iamge_loading, R.drawable.iamge_loading,
                    ((DCShejiViewHolder) holder).item_dec_com_image);
            //设置Tag
            ((DCShejiViewHolder) holder).item_dec_com_image_ll.setTag(position);
            //设置标题
            ((DCShejiViewHolder) holder).item_dec_com_title.setText("" + suitesBeanArrayList.get(position).getTitle());
            //设置设计师
            ((DCShejiViewHolder) holder).item_dec_com_desc.setText("设计师:" + suitesBeanArrayList.get(position).getDesigner_name());
            //
        }
    }

    @Override
    public int getItemCount() {
        return suitesBeanArrayList.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (onDCShejiItemClickLister != null) {
            onDCShejiItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class DCShejiViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_dec_com_image_ll;//封面图片的套层
        private ImageView item_dec_com_image;//封面图片
        private TextView item_dec_com_title;//名称
        private TextView item_dec_com_desc;//描述（有关于设计师等等）

        public DCShejiViewHolder(View itemView) {
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
