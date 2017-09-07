package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.CaseDetailActivity;
import com.tbs.tobosupicture.activity.DecorateCaseSearchActivity;
import com.tbs.tobosupicture.bean.OwnerSearchRecordJsonEntity;
import com.tbs.tobosupicture.utils.Utils;

import java.util.List;

/**
 * Created by Lie on 2017/8/13.
 */

public class OwenerSearchRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<OwnerSearchRecordJsonEntity.OwnerSearchRecordEntity> dataList;

    private View itemtView;
    private View footerView;

    private int viewItemType = 1;
    private int viewItemFooter = 0;

    private RelativeLayout footerLayout;
    private TextView tvLoadMore;
    private ProgressBar progressBar;

    public OwenerSearchRecordAdapter(Context context, List<OwnerSearchRecordJsonEntity.OwnerSearchRecordEntity> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == viewItemType){
            itemtView = inflater.inflate(R.layout.adapter_item_owener_search_record_layout, null);
            OwenerViewHolder holder = new OwenerViewHolder(itemtView);
            return holder;
        }

        if(viewType == viewItemFooter){
            footerView = inflater.inflate(R.layout.item_foot_case_fragment, null);
            footerLayout = (RelativeLayout) footerView.findViewById(R.id.foot_load_more_layout_case);
            tvLoadMore = (TextView) footerView.findViewById(R.id.tv_loadmore_text_case);
            progressBar = (ProgressBar) footerView.findViewById(R.id.progressBar_case);
            return new FootViewHolder(footerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OwenerViewHolder){
            OwenerViewHolder ownerHolder = (OwenerViewHolder)holder;
            ownerHolder.tvTime.setText(dataList.get(position).getAdd_time());
            ownerHolder.tvDesc.setText(dataList.get(position).getSearch_condition());

            String newCaseCount = dataList.get(position).getNew_case_count();
            if("0".equals(newCaseCount)){
                ownerHolder.tvCheck.setVisibility(View.GONE);
            }else {
                ownerHolder.tvCheck.setVisibility(View.VISIBLE);
                String text = "有" + newCaseCount + "套新的装修案例，点击查看";
                ownerHolder.tvCheck.setText(text);
                ownerHolder.tvCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DecorateCaseSearchActivity.class);
                        intent.putExtra("case_group_id", dataList.get(position).getId());
                        context.startActivity(intent);
                    }
                });
            }
            hideLoadMoreMessage();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position<dataList.size()){
            return viewItemType;
        }else {
            return viewItemFooter;
        }
    }


    @Override
    public int getItemCount() {
        return dataList==null? 0:dataList.size()+1;
    }

    public class OwenerViewHolder extends RecyclerView.ViewHolder{
        TextView tvTime;
        TextView tvDesc;
        TextView tvCheck;

        public OwenerViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvOwenCaseTime);
            tvDesc = (TextView) itemView.findViewById(R.id.tvOwnerCaseDesc);
            tvCheck = (TextView) itemView.findViewById(R.id.tvOwnerCaseCheck);
        }
    }


    public class FootViewHolder extends RecyclerView.ViewHolder{
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }


    public void showLoadMoreMessage(){
        if(footerLayout!=null){
            footerLayout.setVisibility(View.VISIBLE);
        }

        if(tvLoadMore!=null){
            tvLoadMore.setVisibility(View.VISIBLE);
            tvLoadMore.setText("加载更多数据...");
        }
        if(progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoadMoreMessage(){
        if(footerLayout!=null){
            footerLayout.setVisibility(View.GONE);
        }
        if(tvLoadMore!=null){
            tvLoadMore.setVisibility(View.GONE);
        }
        if(progressBar!=null){
            progressBar.setVisibility(View.GONE);
        }
    }

    public void noMoreData(){
        if(footerLayout!=null){
            footerLayout.setVisibility(View.VISIBLE);
        }
        if(tvLoadMore!=null){
            tvLoadMore.setText("别扯了，到底了！");
        }
        if(progressBar!=null){
            progressBar.setVisibility(View.GONE);
        }

    }
}
