package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._DesignerItem;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/12/1 10:19.
 * 设计师列表页的适配器 作用于设计师列表页
 */

public class DesignerListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private String TAG = "DesignerListAdapter";
    private Context mContext;
    private ArrayList<_DesignerItem> designerItemArrayList;

    public DesignerListAdapter(Context context, ArrayList<_DesignerItem> designerItemArrayList) {
        this.mContext = context;
        this.designerItemArrayList = designerItemArrayList;
    }

    public static interface OnDesignerItemClickLister {
        void onItemClickLister(View view, int position);
    }

    public void setOnDesignerItemClickLister(OnDesignerItemClickLister onDesignerItemClickLister) {
        this.onDesignerItemClickLister = onDesignerItemClickLister;
    }

    private OnDesignerItemClickLister onDesignerItemClickLister = null;

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            //脚部图层
            return 1;
        } else {
            return 2;//普通图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_designer_list, parent, false);
            DesignerViewHolder holder = new DesignerViewHolder(view);
            holder.item_designer_icon_ll.setOnClickListener(this);
            holder.item_designer_find_he_design.setOnClickListener(this);
            holder.item_designer_into_detail.setOnClickListener(this);
            holder.item_designer_item_ll.setOnClickListener(this);
            return holder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_list_foot, parent, false);
            FootView footView = new FootView(view);
            return footView;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DesignerViewHolder) {
            //设置tag
            ((DesignerViewHolder) holder).item_designer_icon_ll.setTag(position);
            ((DesignerViewHolder) holder).item_designer_find_he_design.setTag(position);
            ((DesignerViewHolder) holder).item_designer_into_detail.setTag(position);
            ((DesignerViewHolder) holder).item_designer_item_ll.setTag(position);
            //设置数据
            //头像
            GlideUtils.glideLoader(mContext, designerItemArrayList.get(position).getIcon(),
                    R.drawable.iamge_loading, R.drawable.iamge_loading,
                    ((DesignerViewHolder) holder).item_designer_icon, 0);
            //名称
            ((DesignerViewHolder) holder).item_designer_name.setText("" + designerItemArrayList.get(position).getName());
            //设计师的头衔
            ((DesignerViewHolder) holder).item_designer_title.setText("" + designerItemArrayList.get(position).getPosition());
            //设计师风格
            ((DesignerViewHolder) holder).item_designer_fenge.setText("" + designerItemArrayList.get(position).getStyle_name());
            //领域
            ((DesignerViewHolder) holder).item_designer_lingyu.setText("" + designerItemArrayList.get(position).getArea_name());
        }
    }

    @Override
    public int getItemCount() {
        return designerItemArrayList != null ? designerItemArrayList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onDesignerItemClickLister != null) {
            onDesignerItemClickLister.onItemClickLister(v, (int) v.getTag());
        }
    }

    private class DesignerViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_designer_item_ll;//整个可点击界面
        private ImageView item_designer_into_detail;//点击进入设计师详情
        private ImageView item_designer_icon;//设计师头像
        private LinearLayout item_designer_icon_ll;//设计师头像 套层
        private TextView item_designer_name;//设计师的名字
        private TextView item_designer_title;//设计师的头衔
        private TextView item_designer_find_he_design;//找他设计
        private TextView item_designer_fenge;//设计师风格
        private TextView item_designer_lingyu;//设计师的领域

        public DesignerViewHolder(View itemView) {
            super(itemView);
            item_designer_item_ll = itemView.findViewById(R.id.item_designer_item_ll);
            item_designer_into_detail = itemView.findViewById(R.id.item_designer_into_detail);
            item_designer_icon = itemView.findViewById(R.id.item_designer_icon);
            item_designer_icon_ll = itemView.findViewById(R.id.item_designer_icon_ll);
            item_designer_name = itemView.findViewById(R.id.item_designer_name);
            item_designer_title = itemView.findViewById(R.id.item_designer_title);
            item_designer_find_he_design = itemView.findViewById(R.id.item_designer_find_he_design);
            item_designer_fenge = itemView.findViewById(R.id.item_designer_fenge);
            item_designer_lingyu = itemView.findViewById(R.id.item_designer_lingyu);
        }
    }

    private class FootView extends RecyclerView.ViewHolder {
        private LinearLayout foot_item;

        public FootView(View itemView) {
            super(itemView);
            foot_item = itemView.findViewById(R.id.foot_item);
        }
    }
}
