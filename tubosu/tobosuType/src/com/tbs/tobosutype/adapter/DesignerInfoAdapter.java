package com.tbs.tobosutype.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.NewWebViewActivity;
import com.tbs.tobosutype.bean.DesignerInfoBean;
import com.tbs.tobosutype.bean.DesignerInfoCaseBean;
import com.tbs.tobosutype.bean.DesignerInfoDesignBean;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.TRoundView;

import java.util.ArrayList;
import java.util.List;

public class DesignerInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private int clickType = 0;
    private DesignerInfoBean designerInfoBean;
    private List<DesignerInfoDesignBean> shejiDataList = new ArrayList<DesignerInfoDesignBean>();
    private List<DesignerInfoCaseBean> anliDataList = new ArrayList<DesignerInfoCaseBean>();
    private DesignerInfoAdapter designerInfoAdapter;

    private final int SHEJI_TYPE = 0;
    private final int ANLI_TYPE = 1;

    private final int ITEM_TYPE_BODY = 3;
    private final int ITEM_TYPE_FOOT = 4;

    public DesignerInfoAdapter(Context context, DesignerInfoBean designerInfoBean, List<DesignerInfoDesignBean> shejiDataList, List<DesignerInfoCaseBean> anliDataList) {
        this.context = context;
        this.designerInfoBean = designerInfoBean;
        this.inflater = LayoutInflater.from(context);
        this.shejiDataList.addAll(shejiDataList);
        this.anliDataList.addAll(anliDataList);
    }

    public void setShejiDataList(List<DesignerInfoDesignBean> shejiDataList){
        this.shejiDataList.addAll(shejiDataList);
    }

    public void setAnliDataList(List<DesignerInfoCaseBean> anliDataList){
        this.anliDataList.addAll(anliDataList);
    }

    private void setClickType(int type){
        this.clickType = type;
    }

//    private void switchToShowView(){
//        if(shejiDataList.size()==0){
//            // 设计 数目为0  显示 案例
//            if(anliDataList.size() == 0){
//                // 案例 数目为0   显示无任何数据
//
//            }else {
//                // 显示案例的列表
//                // FIXME: 2017/12/9
//            }
//
//        }else{
//            // 显示 设计的列表
//
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_FOOT){
            View footView = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            ShejishiFoot shejishiFoot = new ShejishiFoot(footView);
            return shejishiFoot;
        }

        if(viewType == ITEM_TYPE_BODY){
            if(clickType == SHEJI_TYPE){
                // 显示左边 设计
                View shejiView = inflater.inflate(R.layout.layout_designer_info_sheji, parent, false);
                ShejiShiBodyHolder shejiShiBodyHolder = new ShejiShiBodyHolder(shejiView);
                return shejiShiBodyHolder;
            }else {
                // 显示右边 案例
                View anliView = inflater.inflate(R.layout.layout_designer_info_anli, parent, false);
                AnliBodyHolder anliBodyHolder = new AnliBodyHolder(anliView);
                return anliBodyHolder;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  ShejiShiBodyHolder){
            ShejiShiBodyHolder shejiHolder = (ShejiShiBodyHolder) holder;
            shejiHolder.shejiPic.setType(1);
            Glide.with(context).load(shejiDataList.get(position).getCover_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(shejiHolder.shejiPic);
            shejiHolder.shejiInfoText.setText(shejiDataList.get(position).getTitle());
            shejiHolder.shejiGetThis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", Constant.getSheJiUrl);
                    context.startActivity(webIntent);
                }
            });
        } else if (holder instanceof AnliBodyHolder){
            AnliBodyHolder anliHolder = (AnliBodyHolder) holder;

            anliHolder.anliPic.setType(1);
            Glide.with(context).load(anliDataList.get(position).getCover_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(anliHolder.anliPic);
            // 深圳  小区  先生
            anliHolder.anliInfoTextDes.setText(anliDataList.get(position).getCity_name() + " " + anliDataList.get(position).getCommunity_name()+ " " + anliDataList.get(position).getOwner_name());
            anliHolder.anliInfotest.setText(anliDataList.get(position).getSub_title());
//            anliHolder.anliGetThis.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
//                    webIntent.putExtra("mLoadingUrl", Constant.GETANLIURL);
//                    context.startActivity(webIntent);
//                }
//            });

        }else if(holder instanceof ShejishiFoot) {
            ShejishiFoot footHolder = (ShejishiFoot) holder;

        }
    }

    @Override
    public int getItemCount() {
        if(clickType == SHEJI_TYPE){
            // 左边设计
            return shejiDataList==null? 0 : shejiDataList.size() + 1;
        }else {
            return anliDataList==null? 0 : anliDataList.size() + 1;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount()){
            return ITEM_TYPE_BODY;
        }else{
            return ITEM_TYPE_FOOT;
        }
//        if(clickType == SHEJI_TYPE){
//            // 设计
//            if(position + 1 == getItemCount()){
//                return ITEM_TYPE_BODY;
//            }else{
//                return ITEM_TYPE_FOOT;
//            }
//        }else {
//            // 案例
//            if(position + 1 == getItemCount()){
//                return ITEM_TYPE_BODY;
//            }else{
//                return ITEM_TYPE_FOOT;
//            }
//        }
    }

    class ShejiShiBodyHolder extends RecyclerView.ViewHolder{
        TRoundView shejiPic;
        TextView shejiInfoText;
        TextView shejiGetThis;
        public ShejiShiBodyHolder(View itemView) {
            super(itemView);
            shejiGetThis = (TextView) itemView.findViewById(R.id.shejiGetThis);
            shejiInfoText = (TextView) itemView.findViewById(R.id.shejiInfoText);
            shejiPic = (TRoundView) itemView.findViewById(R.id.shejiPic);
        }


    }

    class AnliBodyHolder extends RecyclerView.ViewHolder{
        TRoundView anliPic;
        TextView anliInfoTextDes;
        TextView anliInfotest;
        TextView anliGetThis;
        public AnliBodyHolder(View itemView) {
            super(itemView);
            anliGetThis = (TextView) itemView.findViewById(R.id.anliGetThis);
            anliInfotest = (TextView) itemView.findViewById(R.id.anliInfotest);
            anliInfoTextDes = (TextView) itemView.findViewById(R.id.anliInfoTextDes);
            anliPic = (TRoundView) itemView.findViewById(R.id.anliPic);
        }
    }


    class ShejishiFoot extends RecyclerView.ViewHolder {
        ProgressBar bar;
        TextView textLoadMore;

        public ShejishiFoot(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
            textLoadMore = (TextView) itemView.findViewById(R.id.newhome_loadmore);
        }
    }

}
