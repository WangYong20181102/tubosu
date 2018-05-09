package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._CompanyDetail;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/12/1 15:36.
 * 装修公司的服务项目列表 从装修公司主页跳转进来
 */

public class ServiceItemAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private String TAG = "ServiceItemAdapter";
    private Context mContext;
    private ArrayList<_CompanyDetail.ServiceBean> serviceBeanArrayList;

    public ServiceItemAdapter(Context context, ArrayList<_CompanyDetail.ServiceBean> serviceBeanArrayList) {
        this.mContext = context;
        this.serviceBeanArrayList = serviceBeanArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_service_item, parent, false);
        ServiceViewHolder holder = new ServiceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ServiceViewHolder) {
            //绑定数据
            ((ServiceViewHolder) holder).item_servic_title.setText("" + serviceBeanArrayList.get(position).getTitle());
            ((ServiceViewHolder) holder).item_servic_content.setText("" + serviceBeanArrayList.get(position).getSub_title());
        }
    }

    @Override
    public int getItemCount() {
        return serviceBeanArrayList.size();
    }

    @Override
    public void onClick(View v) {

    }

    private class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView item_servic_title;//标题
        private TextView item_servic_content;//内容

        public ServiceViewHolder(View itemView) {
            super(itemView);
            item_servic_title = itemView.findViewById(R.id.item_servic_title);
            item_servic_content = itemView.findViewById(R.id.item_servic_content);
        }
    }
}
