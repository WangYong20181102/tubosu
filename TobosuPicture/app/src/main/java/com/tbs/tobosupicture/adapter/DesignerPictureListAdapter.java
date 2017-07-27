package com.tbs.tobosupicture.adapter;

import android.content.Context;
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
import com.tbs.tobosupicture.bean.DesignerCaseEntity;
import com.tbs.tobosupicture.bean.DesignerImpressionEntity;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/15.
 */

public class DesignerPictureListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private View headView;
    private static final int TYPE_ITEM = 1;
    private View itmeView;
    private static final int TYPE_FOOTER = 2;
    private View footerView;
    private LinearLayout footerLayout;
    private TextView tvLoadMore;
    private ProgressBar progressBar;

    private Context mContext;
    private LayoutInflater inflater;
//    private ArrayList<SamplePicBeanEntity> dataList;
    private ArrayList<DesignerImpressionEntity> sampleDataList; // 样板图
    private ArrayList<DesignerCaseEntity> caseDataList; //  案例图
    private int type = -1;
    private String headUrl;
    private String desingerName;
    private String designerDesc;

    public DesignerPictureListAdapter(Context mContext, ArrayList<DesignerImpressionEntity> sampleDataList,
                                      ArrayList<DesignerCaseEntity> caseDataList, int type,
                                      String headUrl, String desingerName, String designerDesc){
        this.mContext = mContext;
        this.sampleDataList = sampleDataList;
        this.caseDataList = caseDataList;
        this.inflater = LayoutInflater.from(mContext);
        this.type = type;
        this.headUrl = headUrl;
        this.desingerName = desingerName;
        this.designerDesc = designerDesc;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            headView = inflater.inflate(R.layout.adapter_item_desinger_img_list_head_layout, parent, false);
            HeadViewHolder holder = new HeadViewHolder(headView);
            return holder;
        }

        if(viewType == TYPE_ITEM){
            itmeView = inflater.inflate(R.layout.adapter_item_desing_img_pic_layout, parent, false);
            PicViewHolder holder = new PicViewHolder(itmeView);
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
        if(holder instanceof PicViewHolder){
            PicViewHolder itemHolder = (PicViewHolder) holder;
            if(type == 0){// 样板图  sampleDataList
                GlideUtils.glideLoader(mContext, sampleDataList.get(position).getImg_url(), R.mipmap.loading_img_fail,R.mipmap.loading_img,itemHolder.iv_big_pic,1);
                itemHolder.tv_big_pic_num.setText(sampleDataList.get(position).getImage_count());
                itemHolder.tv_designer_collect_count.setText(sampleDataList.get(position).getCollect_count());
                String desc = "";
                String style = sampleDataList.get(position).getStyle_name();
                String layout = sampleDataList.get(position).getLayout_name();
                String budget =sampleDataList.get(position).getPlan_price();
                if(!"".equals(style)){
                    desc += style + " ";
                }
                if(!"".equals(layout)){
                    desc += layout + " ";
                }
                if(!"".equals(budget)){
                   desc += "预算" + budget + "万";
                }
                itemHolder.tv_pic_desa.setText(desc);
                itemHolder.tv_designer_collect_count.setText(sampleDataList.get(position).getCollect_count());
                String isCollect = sampleDataList.get(position).getIs_collect();
                if("0".equals(isCollect)){
                    // 未被收藏
                    itemHolder.iv_designer_image_like.setBackgroundResource(R.mipmap.shoucang3);
                    itemHolder.rel_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  FIXME 点击收藏

                        }
                    });
                }else{
                    // 被收藏
                    itemHolder.iv_designer_image_like.setBackgroundResource(R.mipmap.shoucang);
                    itemHolder.rel_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  FIXME 点击取消收藏

                        }
                    });
                }

                itemHolder.iv_big_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent i = new Intent(mContext, DesignerActivity.class);
//                        Bundle b = new Bundle();
//                        b.putString("designer_id", sampleDataList.get(position).getId());
//                        i.putExtra("designer_bundle", b);
//                        mContext.startActivity(i);
                    }
                });
                holder.itemView.setTag(sampleDataList.get(position));

            }else if(type == 1){
                // 案例图

                GlideUtils.glideLoader(mContext, caseDataList.get(position).getImg_url(), R.mipmap.loading_img_fail,R.mipmap.loading_img,itemHolder.iv_big_pic,1);
//                itemHolder.tv_big_pic_num.setText(caseDataList.get(position).get());
                itemHolder.tv_big_pic_num.setText("曾");
                itemHolder.tv_designer_collect_count.setText(caseDataList.get(position).getCollect_count());
                String desc = "";
                String shi = caseDataList.get(position).getShi();
                String wei = caseDataList.get(position).getWei();
                String ting = caseDataList.get(position).getTing();
                String area = caseDataList.get(position).getArea();
                String budget = caseDataList.get(position).getPrice();
                String method = caseDataList.get(position).getDesmethod();
                if(!"".equals(shi)){
                    desc += shi + "室 ";
                }
                if(!"".equals(wei)){
                    desc += wei + "位 ";
                }
                if(!"".equals(ting)){
                    desc += ting + "厅 ";
                }
                if(!"".equals(area)){
                    desc += area + "m² ";
                }
                if(!"".equals(budget)) {
                    desc += budget+"万";
                }
                if(!"".equals(method)) {
                    desc += "(" + method+")";
                }
                itemHolder.tv_pic_desa.setText(desc);
                itemHolder.tv_designer_collect_count.setText(sampleDataList.get(position).getCollect_count());
                String isCollect = sampleDataList.get(position).getIs_collect();
                if("0".equals(isCollect)){
                    // 未被收藏
                    itemHolder.iv_designer_image_like.setBackgroundResource(R.mipmap.shoucang3);
                    itemHolder.rel_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  FIXME 点击收藏

                        }
                    });
                }else{
                    // 被收藏
                    itemHolder.iv_designer_image_like.setBackgroundResource(R.mipmap.shoucang);
                    itemHolder.rel_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  FIXME 点击取消收藏

                        }
                    });
                }

                itemHolder.iv_big_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent i = new Intent(mContext, DesignerActivity.class);
//                        Bundle b = new Bundle();
//                        b.putString("designer_id", sampleDataList.get(position).getId());
//                        i.putExtra("designer_bundle", b);
//                        mContext.startActivity(i);
                    }
                });
                holder.itemView.setTag(sampleDataList.get(position));
            }
        }

        if(holder instanceof HeadViewHolder){
            HeadViewHolder headHolder = (HeadViewHolder) holder;
            headHolder.tvDesignerName.setText(desingerName);
            GlideUtils.glideLoader(mContext, headUrl, R.mipmap.pic,R.mipmap.pic,headHolder.ivDesignerHeadPicture,0);
            headHolder.tvDesiNum.setText(designerDesc);
        }

        if(holder instanceof FootViewHolder){
            FootViewHolder footHolder = (FootViewHolder) holder;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(type==0){ // 样板
            if(position==1){
                return TYPE_HEADER;
            }else if(position<sampleDataList.size()){
                return TYPE_ITEM;
            }else {
                return TYPE_FOOTER;
            }
        }else { // 案例
            if(position==1){
                return TYPE_HEADER;
            }else if(position<caseDataList.size()){
                return TYPE_ITEM;
            }else {
                return TYPE_FOOTER;
            }
        }
    }


    @Override
    public int getItemCount() {
        if(type==0) { // 样板
            return sampleDataList==null?0:sampleDataList.size() + 2;
        }else {
            return caseDataList==null?0:caseDataList.size() + 2;
        }

    }


    public class HeadViewHolder extends RecyclerView.ViewHolder{
        ImageView ivDesignerHeadPicture;
        TextView tvDesignerName;
        TextView tvDesiNum;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ivDesignerHeadPicture = (ImageView) itemView.findViewById(R.id.ivDesignerHeadPicture);
            tvDesignerName = (TextView) itemView.findViewById(R.id.tvDesignerName);
            tvDesiNum = (TextView) itemView.findViewById(R.id.tvDesiNum);
        }
    }


    public class PicViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_big_pic;
        private TextView tv_big_pic_num;
        private TextView tv_pic_desa; // 描述
        private RelativeLayout rel_like;
        private ImageView iv_designer_image_like;
        private TextView tv_designer_collect_count;

        public PicViewHolder(View itemView) {
            super(itemView);
            iv_big_pic = (ImageView) itemView.findViewById(R.id.iv_big_pic);
            tv_big_pic_num = (TextView) itemView.findViewById(R.id.tv_big_pic_num);
            tv_pic_desa = (TextView) itemView.findViewById(R.id.tv_pic_desa);
            rel_like = (RelativeLayout) itemView.findViewById(R.id.rel_like);
            iv_designer_image_like = (ImageView) itemView.findViewById(R.id.iv_designer_image_like);
            tv_designer_collect_count = (TextView) itemView.findViewById(R.id.tv_designer_collect_count);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder{
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}