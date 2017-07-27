package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.DesignerActivity;
import com.tbs.tobosupicture.activity.SeeImageActivity;
import com.tbs.tobosupicture.bean.CaseJsonEntity;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/19.
 */

public class CaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<CaseJsonEntity.CaseEntity> dataList;
    private LayoutInflater inflater;

    private View itemtView;
    private View footerView;

    private int viewItemType = 1;
    private int viewItemFooter = 0;

    private RelativeLayout footerLayout;
    private TextView tvLoadMore;
    private ProgressBar progressBar;


    public CaseAdapter(Context context, ArrayList<CaseJsonEntity.CaseEntity> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == viewItemType){
            itemtView = inflater.inflate(R.layout.adapter_item_case_layout, null);
            CaseViewHolder holder = new CaseViewHolder(itemtView);
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
        if(holder instanceof CaseViewHolder){
            CaseViewHolder caseViewHolder = (CaseViewHolder) holder;
            String title = "";
            String description = "";
            GlideUtils.glideLoader(context, dataList.get(position).getImg_url(), R.mipmap.loading_img_fail,R.mipmap.loading_img,caseViewHolder.iv_case_big_sample_pic,1);
            GlideUtils.glideLoader(context, dataList.get(position).getDesigner_icon(), R.mipmap.pic,R.mipmap.pic,caseViewHolder.iv_case_designer_pic, 0);
            String district = dataList.get(position).getDistrict_name();
            String budget = dataList.get(position).getPrice();
            String method = dataList.get(position).getDesmethod();
            String style = dataList.get(position).getStyle_name();
            String shi = dataList.get(position).getShi();
            String ting = dataList.get(position).getTing();
            String wei = dataList.get(position).getWei();
            String area = dataList.get(position).getArea();

            if("".equals(budget)){
                title = district + " ";
            }else{
                title = district + " " + budget + "万 " + method;
            }

            if(!"".equals(shi)){
                shi = shi + "室";
            }
            if(!"".equals(ting)){
                ting = ting + "厅";
            }
            if(!"".equals(wei)){
                wei = wei + "卫";
            }
            if(!"".equals(area)){
                area = area + "m²";
            }

            String str = shi + ting + wei;
            if(!"".equals(style)){
                if(!"".equals(str)){
                    if(!"".equals(area)){
                        description = style + " | " + str + " | " + area;
                    }else {
                        description = style + " | " + str;
                    }

                }else{
                    if(!"".equals(area)){
                        description = style + " | " + area;
                    }else{
                        description = style;
                    }
                }
            }else {
                if(!"".equals(str)){
                    if(!"".equals(area)){
                        description = str + " | " + area;
                    }else {
                        description = str;
                    }
                }else{
                    if(!"".equals(area)){
                        description = area;
                    }
                }
            }

            caseViewHolder.tv_case_title.setText(title);
            caseViewHolder.tv_case_decription.setText(description);

            caseViewHolder.iv_case_designer_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DesignerActivity.class);
                    Bundle b = new Bundle();
                    b.putString("designer_id", dataList.get(position).getDesigner_id());
                    intent.putExtra("designer_bundle", b);
                    context.startActivity(intent);
                }
            });

//            caseViewHolder.iv_case_big_sample_pic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, SeeImageActivity.class);
//
//                    context.startActivity(intent);
//                }
//            });
            caseViewHolder.itemView.setTag(dataList.get(position));
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
        return dataList==null?0:dataList.size() + 1;
    }

    public class CaseViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_case_big_sample_pic;
        ImageView iv_case_designer_pic;
        TextView tv_case_title;
        TextView tv_case_decription;

        public CaseViewHolder(View itemView) {
            super(itemView);
            iv_case_big_sample_pic = (ImageView) itemView.findViewById(R.id.iv_case_big_sample_pic);
            iv_case_designer_pic = (ImageView) itemView.findViewById(R.id.iv_case_designer_pic);
            tv_case_title = (TextView) itemView.findViewById(R.id.tv_case_title);
            tv_case_decription = (TextView) itemView.findViewById(R.id.tv_case_decription);
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
