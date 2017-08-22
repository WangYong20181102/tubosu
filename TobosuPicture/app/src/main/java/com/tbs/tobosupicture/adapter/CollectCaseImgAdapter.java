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
import com.tbs.tobosupicture.activity.CaseDetailActivity;
import com.tbs.tobosupicture.activity.DesignerActivity;
import com.tbs.tobosupicture.activity.GetPriceActivity;
import com.tbs.tobosupicture.activity.SeeImageActivity;
import com.tbs.tobosupicture.bean.CollectCaseJsonEntity;
import com.tbs.tobosupicture.bean.CollectionSampleJsonEntity;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/15.
 */

public class CollectCaseImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int TYPE_ITEM = 1;
    private View itmeView;
    private static final int TYPE_FOOTER = 2;
    private View footerView;
    private LinearLayout footerLayout;
    private TextView tvLoadMore;
    private ProgressBar progressBar;

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<CollectCaseJsonEntity.CollectCaseEntity> dataList;

    public CollectCaseImgAdapter(Context mContext, ArrayList<CollectCaseJsonEntity.CollectCaseEntity> dataList){
        this.mContext = mContext;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            itmeView = inflater.inflate(R.layout.adapter_item_case_layout, parent, false);
            CasePicViewHolder holder = new CasePicViewHolder(itmeView);
            itmeView.setOnClickListener(this);
            return holder;
        }

        if (viewType == TYPE_FOOTER) {
            footerView = inflater.inflate(R.layout.item_foot, parent, false);
            footerLayout = (LinearLayout) footerView.findViewById(R.id.foot_load_more_layout);
            tvLoadMore = (TextView) footerView.findViewById(R.id.tv_loadmore_text);
            return new FootViewHolder(footerView);
        }
        return null;
    }

    public void showLoadMoreMessage(){
        if(footerLayout!=null){
            footerLayout.setVisibility(View.VISIBLE);
        }

        if(tvLoadMore!=null){
            tvLoadMore.setText("加载更多数据...");
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
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof CasePicViewHolder){
            CasePicViewHolder itmeHolder = (CasePicViewHolder) holder;
            String title = "";
            String description = "";
            GlideUtils.glideLoader(mContext, dataList.get(position).getImg_url(), R.mipmap.loading_img_fail,R.mipmap.loading_img,itmeHolder.iv_case_big_sample_pic,1);

            if(!"".equals(dataList.get(position).getDesigner_icon())){
                GlideUtils.glideLoader(mContext, dataList.get(position).getDesigner_icon(), R.mipmap.pic,R.mipmap.pic,itmeHolder.iv_case_designer_pic, 0);
                itmeHolder.re_case_desiner_layout.setVisibility(View.VISIBLE);
            }else {
                itmeHolder.re_case_desiner_layout.setVisibility(View.GONE);
            }
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

            itmeHolder.tv_case_title.setText(title);
            itmeHolder.tv_case_decription.setText(description);

            itmeHolder.iv_case_designer_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DesignerActivity.class);
                    Bundle b = new Bundle();
                    b.putString("designer_id", dataList.get(position).getDesigner_id());
                    intent.putExtra("designer_bundle", b);
                    mContext.startActivity(intent);
                }
            });

            itmeHolder.tv_case_design.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, GetPriceActivity.class));
                }
            });
            itmeHolder.iv_case_big_sample_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CaseDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id", dataList.get(position).getCaseid());
                    intent.putExtra("case_bundle", b);
                    mContext.startActivity(intent);
                }
            });
            itmeHolder.itemView.setTag(dataList.get(position));

        }

//        if(holder instanceof FootViewHolder){
//            FootViewHolder footHolder = (FootViewHolder) holder;
//
//        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position<dataList.size()){
            return TYPE_ITEM;
        }else {
            return TYPE_FOOTER;
        }
    }


    @Override
    public int getItemCount() {
        return dataList==null?0:dataList.size() + 1;
    }

    @Override
    public void onClick(View v) {

    }

    public class CasePicViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout re_case_desiner_layout;
        ImageView iv_case_big_sample_pic;
        ImageView iv_case_designer_pic;
        TextView tv_case_title;
        TextView tv_case_decription;
        TextView tv_case_design;

        public CasePicViewHolder(View itemView) {
            super(itemView);
            re_case_desiner_layout = (RelativeLayout) itemView.findViewById(R.id.re_case_desiner_layout);
            iv_case_big_sample_pic = (ImageView) itemView.findViewById(R.id.iv_case_big_sample_pic);
            iv_case_designer_pic = (ImageView) itemView.findViewById(R.id.iv_case_designer_pic);
            tv_case_title = (TextView) itemView.findViewById(R.id.tv_case_title);
            tv_case_decription = (TextView) itemView.findViewById(R.id.tv_case_decription);
            tv_case_design = (TextView) itemView.findViewById(R.id.tv_case_design);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder{
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
