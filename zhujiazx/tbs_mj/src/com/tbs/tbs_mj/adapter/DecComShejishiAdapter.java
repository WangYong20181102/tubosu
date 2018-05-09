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
 * Created by Mr.Lin on 2017/11/30 15:39.
 * 设计师横向列表页 适配器  作用于装修公司主页设计师
 */

public class DecComShejishiAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "DecComCaseAdapter";
    private ArrayList<_CompanyDetail.DesignersBean> designersBeanArrayList;

    public DecComShejishiAdapter(Context context, ArrayList<_CompanyDetail.DesignersBean> designersBeans) {
        this.mContext = context;
        this.designersBeanArrayList = designersBeans;
    }

    public static interface OnItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnItemClickLister onItemClickLister = null;

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dec_com_desginer, parent, false);
        SJSViewHolder sjsViewHolder = new SJSViewHolder(view);
        sjsViewHolder.item_dec_com_design_icon_ll.setOnClickListener(this);
        return sjsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SJSViewHolder) {
            //设置头像
            GlideUtils.glideLoader(mContext, designersBeanArrayList.get(position).getIcon(),
                    R.drawable.iamge_loading, R.drawable.iamge_loading,
                    ((SJSViewHolder) holder).item_dec_com_design_icon, 0);
            //头像设置点击的tag
            ((SJSViewHolder) holder).item_dec_com_design_icon_ll.setTag(position);
            //设置设计师的名字
            ((SJSViewHolder) holder).item_dec_com_design_name.setText("" + designersBeanArrayList.get(position).getName());
            //设置设计师的头衔
            ((SJSViewHolder) holder).item_dec_com_design_title.setText("" + designersBeanArrayList.get(position).getPosition());
        }
    }

    @Override
    public int getItemCount() {
        return designersBeanArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null) {
            onItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class SJSViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_dec_com_design_icon;//设计师的头像
        private LinearLayout item_dec_com_design_icon_ll;//设计师的头像
        private TextView item_dec_com_design_name;//设计师的头名字
        private TextView item_dec_com_design_title;//设计师的标签

        public SJSViewHolder(View itemView) {
            super(itemView);
            item_dec_com_design_icon_ll = itemView.findViewById(R.id.item_dec_com_design_icon_ll);
            item_dec_com_design_icon = itemView.findViewById(R.id.item_dec_com_design_icon);
            item_dec_com_design_name = itemView.findViewById(R.id.item_dec_com_design_name);
            item_dec_com_design_title = itemView.findViewById(R.id.item_dec_com_design_title);
        }
    }
}
