package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._CompanySuiteList;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/11/30 17:11.
 * 设计方案适配器 作用于设计方案列表页
 */

public class DesignCaseAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "DesignCaseAdapter";
    private ArrayList<_CompanySuiteList> companySuiteListArrayList;

    public DesignCaseAdapter(Context context, ArrayList<_CompanySuiteList> companySuiteLists) {
        this.mContext = context;
        this.companySuiteListArrayList = companySuiteLists;
    }

    //点击事件
    public static interface OnItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnItemClickLister onItemClickLister = null;

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_design_case, parent, false);
        DesignCaseHolder designCaseHolder = new DesignCaseHolder(view);
        designCaseHolder.item_design_case_image_rl.setOnClickListener(this);
        designCaseHolder.item_design_case_get_design.setOnClickListener(this);
        return designCaseHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DesignCaseHolder) {
            //设置tag
            ((DesignCaseHolder) holder).item_design_case_image_rl.setTag(position);
            ((DesignCaseHolder) holder).item_design_case_get_design.setTag(position);
            //设置封面图
            GlideUtils.glideLoader(mContext, companySuiteListArrayList.get(position).getCover_url(),
                    R.drawable.iamge_loading, R.drawable.iamge_loading,
                    ((DesignCaseHolder) holder).item_design_case_image);
            //设置设计师的头像
            GlideUtils.glideLoader(mContext, companySuiteListArrayList.get(position).getDesigner_icon(),
                    R.drawable.iamge_loading, R.drawable.iamge_loading,
                    ((DesignCaseHolder) holder).item_design_case_designer_icon, 0);
            //设置设计师的名字
            ((DesignCaseHolder) holder).item_design_case_designer_name.setText("" + companySuiteListArrayList.get(position).getDesigner_name());
            //设置标题
            ((DesignCaseHolder) holder).item_design_case_title.setText("" + companySuiteListArrayList.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return companySuiteListArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null) {
            onItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    private class DesignCaseHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_design_case_image_rl;//图层
        private ImageView item_design_case_image;//显示的封面图
        private ImageView item_design_case_designer_icon;//设计师的头像
        private TextView item_design_case_designer_name;//设计师的名字
        private TextView item_design_case_title;//标题
        private TextView item_design_case_get_design;//发单按钮

        public DesignCaseHolder(View itemView) {
            super(itemView);
            item_design_case_image_rl = itemView.findViewById(R.id.item_design_case_image_rl);
            item_design_case_image = itemView.findViewById(R.id.item_design_case_image);
            item_design_case_designer_icon = itemView.findViewById(R.id.item_design_case_designer_icon);
            item_design_case_designer_name = itemView.findViewById(R.id.item_design_case_designer_name);
            item_design_case_title = itemView.findViewById(R.id.item_design_case_title);
            item_design_case_get_design = itemView.findViewById(R.id.item_design_case_get_design);
        }
    }
}
