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
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.DesignerActivity;
import com.tbs.tobosupicture.activity.GetPriceActivity;
import com.tbs.tobosupicture.activity.SeeImageActivity;
import com.tbs.tobosupicture.bean.CollectionSampleJsonEntity;
import com.tbs.tobosupicture.bean.DesignerListJsonEntity;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/15.
 */

public class MyDesignerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private String TAG = "MyDesignerAdapter";
    private static final int TYPE_ITEM = 1;
    private View itmeView;
    private static final int TYPE_FOOTER = 2;
    private View footerView;
    private LinearLayout footerLayout;
    private TextView tvLoadMore;
    private ProgressBar progressBar;

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<DesignerListJsonEntity.MyDesigner> dataList;

    public MyDesignerAdapter(Context mContext, ArrayList<DesignerListJsonEntity.MyDesigner> dataList){
        this.mContext = mContext;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            itmeView = inflater.inflate(R.layout.adapter_item_mydesigner_layout, parent, false);
            DesignerViewHolder holder = new DesignerViewHolder(itmeView);
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
        if(tvLoadMore!=null){
            tvLoadMore.setVisibility(View.GONE);
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
        if(holder instanceof DesignerViewHolder){
            DesignerViewHolder itmeHolder = (DesignerViewHolder) holder;
            GlideUtils.glideLoader(mContext, dataList.get(position).getIcon(), R.mipmap.loading_img_fail,R.mipmap.loading_img,itmeHolder.iv_designer_pic, 0);
            itmeHolder.tv_desinger_name.setText(dataList.get(position).getDesigner_name());


            String fan = dataList.get(position).getFans_count();
            String view = dataList.get(position).getView_count();
            String impres = dataList.get(position).getImpression_count();
            String anli = dataList.get(position).getCase_count();

            String text = "粉丝 " + fan + " | 浏览数 " + view + " | 效果图 " + impres + " | 案例 " + anli;

            Utils.setErrorLog(TAG, text + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            itmeHolder.tv_desinger_caokun_text.setText(text);

            itmeHolder.iv_designer_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, DesignerActivity.class);
                    Bundle b = new Bundle();
                    b.putString("designer_id", dataList.get(position).getDesigner_id());
                    i.putExtra("designer_bundle", b);
                    mContext.startActivity(i);
                }
            });


            itmeHolder.iv_designer_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, DesignerActivity.class);
                    Bundle b = new Bundle();
                    Utils.setErrorLog(TAG, dataList.get(position).getDesigner_id());
                    b.putString("designer_id", dataList.get(position).getDesigner_id());
                    i.putExtra("designer_bundle", b);
                    mContext.startActivity(i);
                }
            });

            holder.itemView.setTag(dataList.get(position));
        }

        if(holder instanceof FootViewHolder){
            FootViewHolder footHolder = (FootViewHolder) holder;

        }
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

    public class DesignerViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_desinger_caokun_text;
        private TextView  tv_desinger_name;
        private ImageView  iv_designer_pic;

        public DesignerViewHolder(View itemView) {
            super(itemView);
            iv_designer_pic = (ImageView) itemView.findViewById(R.id.iv_designer1_pic);
            tv_desinger_name = (TextView) itemView.findViewById(R.id.tv_desinger1_name);
            tv_desinger_caokun_text = (TextView) itemView.findViewById(R.id.tv_desinger_caokun_text);
        }
    }
    public class FootViewHolder extends RecyclerView.ViewHolder{
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
