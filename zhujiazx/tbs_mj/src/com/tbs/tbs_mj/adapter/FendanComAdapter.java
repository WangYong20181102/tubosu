package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._OrderDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2018/3/24 17:36.
 * 订单详情页面分单公司适配器
 */

public class FendanComAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private String TAG = "FendanComAdapter";
    private List<_OrderDetail.CompanyListBean> mCompanyListBeanArrayList;

    public FendanComAdapter(Context context, List<_OrderDetail.CompanyListBean> companyListBeanArrayList) {
        this.mContext = context;
        this.mCompanyListBeanArrayList = companyListBeanArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fendan_company, parent, false);
        FenDanItemViewHolder fenDanItemViewHolder = new FenDanItemViewHolder(view);
        return fenDanItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FenDanItemViewHolder) {
            ((FenDanItemViewHolder) holder).item_fendan_com_name.setText("" + mCompanyListBeanArrayList.get(position).getCompany_name());
            if (mCompanyListBeanArrayList.get(position).getState() == 1) {
                ((FenDanItemViewHolder) holder).item_fendan_state.setText("新订单");
            } else if (mCompanyListBeanArrayList.get(position).getState() == 2) {
                ((FenDanItemViewHolder) holder).item_fendan_state.setText("未量房");
            } else if (mCompanyListBeanArrayList.get(position).getState() == 3) {
                ((FenDanItemViewHolder) holder).item_fendan_state.setText("已量房");
            } else if (mCompanyListBeanArrayList.get(position).getState() == 4) {
                ((FenDanItemViewHolder) holder).item_fendan_state.setText("已签单");
            } else if (mCompanyListBeanArrayList.get(position).getState() == 5) {
                ((FenDanItemViewHolder) holder).item_fendan_state.setText("未签单");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCompanyListBeanArrayList != null ? mCompanyListBeanArrayList.size() : 0;
    }

    private class FenDanItemViewHolder extends RecyclerView.ViewHolder {
        private TextView item_fendan_com_name;
        private TextView item_fendan_state;

        public FenDanItemViewHolder(View itemView) {
            super(itemView);
            item_fendan_state = itemView.findViewById(R.id.item_fendan_state);
            item_fendan_com_name = itemView.findViewById(R.id.item_fendan_com_name);

        }
    }
}
