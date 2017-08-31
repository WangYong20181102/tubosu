package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.tbs.tobosupicture.activity.GetPriceActivity;
import com.tbs.tobosupicture.activity.SeeImageActivity;
import com.tbs.tobosupicture.bean.SamplePicBeanEntity;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.RoundAngleImageView;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/15.
 */

public class SamplePictureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final String TAG = "SamplePictureAdapter";
    private static final int TYPE_ITEM = 1;
    private View itmeView;
    private static final int TYPE_FOOTER = 2;
    private View footerView;
    private LinearLayout footerLayout;
    private TextView tvLoadMore;
    private ProgressBar progressBar;

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<SamplePicBeanEntity> dataList;

    public SamplePictureAdapter(Context mContext, ArrayList<SamplePicBeanEntity> dataList){
        this.mContext = mContext;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            itmeView = inflater.inflate(R.layout.adapter_item_sample_pic_layout, parent, false);
            SamplePicViewHolder holder = new SamplePicViewHolder(itmeView);
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
        if(holder instanceof SamplePicViewHolder){
            SamplePicViewHolder itmeHolder = (SamplePicViewHolder) holder;
            GlideUtils.glideLoader(mContext, dataList.get(position).getImg_url(), R.mipmap.loading_img_fail,R.mipmap.loading_img,itmeHolder.iv_big_sample_pic);
            String picUrl = dataList.get(position).getDesigner_icon();
            if(!"".equals(picUrl)){
                GlideUtils.glideLoader(mContext, picUrl, R.mipmap.pic,R.mipmap.pic,itmeHolder.iv_designer_pic, 0);
                itmeHolder.rel_desiner_layout.setVisibility(View.VISIBLE);
            }else {
                itmeHolder.rel_desiner_layout.setVisibility(View.GONE);
            }
            itmeHolder.tv_samplepic_title.setText(dataList.get(position).getTitle());
            itmeHolder.tv_pic_city.setText(dataList.get(position).getCity_name());
            Utils.setErrorLog(TAG, "你要的面积是--> "+dataList.get(position).getArea_name());
//            itmeHolder.tv_areah.setText("面积:"+ dataList.get(position).getArea_name());
            if(!"".equals(dataList.get(position).getArea_name())){
                itmeHolder.tv_areah.setText("面积:"+ dataList.get(position).getArea_name());
            }else {
                itmeHolder.tv_areah.setVisibility(View.GONE);
            }
            itmeHolder.tv_buget.setText("预算: " + dataList.get(position).getPlan_price() + "万");
            itmeHolder.tv_pic_num.setText(dataList.get(position).getImage_count());
            itmeHolder.tv_pic_like_num.setText(dataList.get(position).getClick_count());
            itmeHolder.tv_free_design_pic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, GetPriceActivity.class));
                }
            });


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


            itmeHolder.iv_big_sample_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, SeeImageActivity.class);
                    Bundle b = new Bundle();
                    b.putString("img_id", dataList.get(position).getId());
                    i.putExtra("img_bundle", b);
                    mContext.startActivity(i);
                }
            });

            holder.itemView.setTag(dataList.get(position));
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

    public class SamplePicViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rel_desiner_layout;
        private RoundAngleImageView iv_big_sample_pic;
        private ImageView iv_designer_pic;
        private TextView tv_samplepic_title;
        private TextView tv_pic_city;
        private TextView tv_buget;
        private TextView tv_areah;
        private TextView tv_pic_num;
        private TextView tv_pic_like_num;
        private TextView tv_free_design_pic;

        public SamplePicViewHolder(View itemView) {
            super(itemView);
            rel_desiner_layout = (RelativeLayout) itemView.findViewById(R.id.rel_desiner_layout);
            iv_big_sample_pic = (RoundAngleImageView) itemView.findViewById(R.id.iv_big_sample_pic);
            iv_designer_pic = (ImageView) itemView.findViewById(R.id.iv_designer_pic);
            tv_samplepic_title = (TextView) itemView.findViewById(R.id.tv_samplepic_title);
            tv_pic_city = (TextView) itemView.findViewById(R.id.tv_pic_city);
            tv_areah = (TextView) itemView.findViewById(R.id.tv_areah);
            tv_buget = (TextView) itemView.findViewById(R.id.tv_buget);
            tv_pic_num = (TextView) itemView.findViewById(R.id.tv_pic_num);
            tv_pic_like_num = (TextView) itemView.findViewById(R.id.tv_pic_like_num);
            tv_free_design_pic = (TextView) itemView.findViewById(R.id.tv_free_design_pic);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder{
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
