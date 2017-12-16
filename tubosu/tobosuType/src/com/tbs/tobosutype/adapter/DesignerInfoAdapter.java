package com.tbs.tobosutype.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.NewWebViewActivity;
import com.tbs.tobosutype.bean.DesignerInfoBean;
import com.tbs.tobosutype.bean.DesignerInfoCaseBean;
import com.tbs.tobosutype.bean._CompanyDetail;
import com.tbs.tobosutype.customview.ExpandableTextView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.TRoundView;
import com.tbs.tobosutype.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DesignerInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private final String TAG = DesignerInfoAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private int clickType = 0;
    private DesignerInfoBean designerInfoBean;
    private List<_CompanyDetail.SuitesBean> shejiDataList = new ArrayList<_CompanyDetail.SuitesBean>();
    private List<DesignerInfoCaseBean> anliDataList = new ArrayList<DesignerInfoCaseBean>();

    private final int SHEJI_TYPE = 0;
    private final int ANLI_TYPE = 1;
    private final int ITEM_TYPE_HEAD = 2;
    private final int ITEM_TYPE_ANLI = 3;
    private final int ITEM_TYPE_SHEJI = 6;
    private final int ITEM_TYPE_FOOT = 4;
    private boolean moreData = false;
    private boolean noShejiData = false;
    private boolean noAnliData = false;
    private boolean nothing = false;



    public DesignerInfoAdapter(Context context, DesignerInfoBean designerInfoBean) {
        this.context = context;
        this.designerInfoBean = designerInfoBean;
        this.inflater = LayoutInflater.from(context);
        noShejiData = true;
        noAnliData = true;
        nothing = true;
    }

    // 设计
    public DesignerInfoAdapter(Context context, DesignerInfoBean designerInfoBean, List<_CompanyDetail.SuitesBean> shejiDataList) {
        this.context = context;
        this.designerInfoBean = designerInfoBean;
        this.inflater = LayoutInflater.from(context);
        this.shejiDataList.addAll(shejiDataList);
        noShejiData = false;
        noAnliData = true;
        nothing = false;
    }

    // 案例
    public DesignerInfoAdapter(Context context, DesignerInfoBean designerInfoBean, List<DesignerInfoCaseBean> anliDataList, int clickType) {
        this.context = context;
        this.designerInfoBean = designerInfoBean;
        this.inflater = LayoutInflater.from(context);
        this.anliDataList.addAll(anliDataList);
        this.clickType = clickType;
        noShejiData = true;
        noAnliData = false;
        nothing = false;
    }

    // 设计 and 案例
    public DesignerInfoAdapter(Context context, DesignerInfoBean designerInfoBean, List<_CompanyDetail.SuitesBean> shejiDataList, List<DesignerInfoCaseBean> anliDataList) {
        this.context = context;
        this.designerInfoBean = designerInfoBean;
        this.inflater = LayoutInflater.from(context);
        this.shejiDataList.addAll(shejiDataList);
        this.anliDataList.addAll(anliDataList);
        noShejiData = false;
        noAnliData = false;
        nothing = false;
    }


    public void setShejiDataList(List<_CompanyDetail.SuitesBean> shejiDataList){
        this.shejiDataList.addAll(shejiDataList);
    }

    public void setAnliDataList(List<DesignerInfoCaseBean> anliDataList){
        this.anliDataList.addAll(anliDataList);
    }

    private void setClickType(int type){
        this.clickType = type;
    }

    public int getClickType(){
        return this.clickType;
    }

    public boolean cantLoadMore(){
        return this.nothing;
    }

    public boolean cantLoadMoreSheji(){
        return this.noShejiData;
    }

    public boolean cantLoadMoreAnli(){
        return this.noAnliData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_HEAD){
            View headView = inflater.inflate(R.layout.shejishi_head_layout, parent, false);
            ShejishiInfoViewHolder shejishiHead = new ShejishiInfoViewHolder(headView);
            return shejishiHead;
        }

        if(viewType == ITEM_TYPE_FOOT){
            View footView = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            ShejishiFoot shejishiFoot = new ShejishiFoot(footView);
            return shejishiFoot;
        }

        if(viewType == ITEM_TYPE_SHEJI){
//            if(clickType == SHEJI_TYPE){
                // 显示左边 设计
                View shejiView = inflater.inflate(R.layout.layout_designer_info_sheji, parent, false);
                ShejiShiBodyHolder shejiShiBodyHolder = new ShejiShiBodyHolder(shejiView);
                shejiView.setOnClickListener(this);
                return shejiShiBodyHolder;
        }/*else {
                // 显示右边 案例
                View anliView = inflater.inflate(R.layout.layout_designer_info_anli, parent, false);
                AnliBodyHolder anliBodyHolder = new AnliBodyHolder(anliView);
                anliView.setOnClickListener(this);
                return anliBodyHolder;
            }*/

        if(viewType == ITEM_TYPE_ANLI){
            // 显示右边 案例
            View anliView = inflater.inflate(R.layout.layout_designer_info_anli, parent, false);
            AnliBodyHolder anliBodyHolder = new AnliBodyHolder(anliView);
            anliView.setOnClickListener(this);
            return anliBodyHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ShejishiInfoViewHolder){
            final ShejishiInfoViewHolder shejishiHeadHolder = (ShejishiInfoViewHolder) holder;
            GlideUtils.glideLoader(context,designerInfoBean.getIcon(), R.drawable.new_home_loading, R.drawable.new_home_loading,shejishiHeadHolder.shejishiIcon, 0);
            GlideUtils.glideLoader(context,R.drawable.wht, R.drawable.new_home_loading, R.drawable.new_home_loading,shejishiHeadHolder.roundOut, 0);
            shejishiHeadHolder.shejishiName.setText(designerInfoBean.getName());
            shejishiHeadHolder.designGrade.setText(designerInfoBean.getPosition());
            shejishiHeadHolder.tvJingyan.setText(designerInfoBean.getWorktime());
            shejishiHeadHolder.tvfeiyong.setText(designerInfoBean.getPrice());
            shejishiHeadHolder.tvFengge.setText(designerInfoBean.getStyle_name());
            shejishiHeadHolder.tvLingyu.setText(designerInfoBean.getArea_name());
            shejishiHeadHolder.jianjie.setText(designerInfoBean.getIntro());

            shejishiHeadHolder.desigText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    onShowShejisDataListener.OnShowShejisDataListener();
                    shejishiHeadHolder.dataListBar.setVisibility(View.VISIBLE);
                    setClickType(0);
                    shejishiHeadHolder.desigText.setTextColor(Color.parseColor("#FF6F20"));
                    shejishiHeadHolder.designTextBar.setVisibility(View.VISIBLE);
                    shejishiHeadHolder.caseText.setTextColor(Color.parseColor("#333333"));
                    shejishiHeadHolder.caseTextBar.setVisibility(View.GONE);
                    moreData = false;
                    if(clickType == SHEJI_TYPE && noShejiData){
                        // 看设计  设计无数据
                        shejishiHeadHolder.relimgEmpty.setVisibility(View.VISIBLE);
                        shejishiHeadHolder.imgEmpty.setVisibility(View.VISIBLE);
                    }else if(clickType == SHEJI_TYPE && !noShejiData){
                        shejishiHeadHolder.relimgEmpty.setVisibility(View.GONE);
                        shejishiHeadHolder.imgEmpty.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });

            shejishiHeadHolder.caseText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    onshowAnliDataListener.OnshowAnliDataListener();
                    shejishiHeadHolder.dataListBar.setVisibility(View.VISIBLE);
                    setClickType(1);
                    shejishiHeadHolder.desigText.setTextColor(Color.parseColor("#333333"));
                    shejishiHeadHolder.designTextBar.setVisibility(View.GONE);
                    shejishiHeadHolder.caseText.setTextColor(Color.parseColor("#FF6F20"));
                    shejishiHeadHolder.caseTextBar.setVisibility(View.VISIBLE);
                    moreData = false;
                    if(clickType != SHEJI_TYPE && noAnliData){
                        // 看案例  案例无数据
                        shejishiHeadHolder.relimgEmpty.setVisibility(View.VISIBLE);
                        shejishiHeadHolder.imgEmpty.setVisibility(View.VISIBLE);
                    }else if(clickType != SHEJI_TYPE && !noAnliData){
                        shejishiHeadHolder.relimgEmpty.setVisibility(View.GONE);
                        shejishiHeadHolder.imgEmpty.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });

            if(noShejiData && noAnliData){
                //  无设计，也无案例
//                shejishiHeadHolder.dataListBar.setVisibility(View.GONE);
                shejishiHeadHolder.relimgEmpty.setVisibility(View.VISIBLE);
                shejishiHeadHolder.imgEmpty.setVisibility(View.VISIBLE);
                this.moreData = false;
            }else if(noShejiData){
                shejishiHeadHolder.dataListBar.setVisibility(View.VISIBLE);
                //  无设计，显示案例
                shejishiHeadHolder.desigText.setTextColor(Color.parseColor("#333333"));
                shejishiHeadHolder.designTextBar.setVisibility(View.GONE);
                shejishiHeadHolder.caseText.setTextColor(Color.parseColor("#FF6F20"));
                shejishiHeadHolder.caseTextBar.setVisibility(View.VISIBLE);
                if(clickType == SHEJI_TYPE){
                    // 点击的是设计
                    shejishiHeadHolder.relimgEmpty.setVisibility(View.VISIBLE);
                    shejishiHeadHolder.imgEmpty.setVisibility(View.VISIBLE);
                }else{
                    shejishiHeadHolder.relimgEmpty.setVisibility(View.GONE);
                    shejishiHeadHolder.imgEmpty.setVisibility(View.GONE);
                }
            }
        }

        if(this.clickType == SHEJI_TYPE){
            if(holder instanceof  ShejiShiBodyHolder){
                ShejiShiBodyHolder shejiHolder = (ShejiShiBodyHolder) holder;

                if(!noShejiData){
                    shejiHolder.shejiPic.setType(1);
                    Glide.with(context).load(shejiDataList.get(position-1).getCover_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(shejiHolder.shejiPic);
                    shejiHolder.shejiInfoText.setText(shejiDataList.get(position-1).getTitle());
                    shejiHolder.shejiGetThis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent webIntent = new Intent(context, NewWebViewActivity.class);
                            webIntent.putExtra("mLoadingUrl", Constant.getSheJiUrl);
                            context.startActivity(webIntent);
                        }
                    });

                    shejiHolder.itemView.setTag(position-1);
                }
            }
        }else{
            if (holder instanceof AnliBodyHolder){
                AnliBodyHolder anliHolder = (AnliBodyHolder) holder;
                if(!noAnliData){
                    anliHolder.anliPic.setType(1);
                    Glide.with(context).load(anliDataList.get(position-1).getCover_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(anliHolder.anliPic);
                    // 深圳  小区  先生
                    anliHolder.anliInfoTextDes.setText(anliDataList.get(position-1).getTitle());
                    anliHolder.anliInfotest.setText(anliDataList.get(position-1).getSub_title());
                    anliHolder.anliGetThis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent webIntent = new Intent(context, NewWebViewActivity.class);
                            webIntent.putExtra("mLoadingUrl", Constant.GETANLIURL);
                            context.startActivity(webIntent);
                        }
                    });
                }
                anliHolder.itemView.setTag(position-1);
            }
        }


        if(holder instanceof ShejishiFoot) {
            ShejishiFoot footHolder = (ShejishiFoot) holder;
            if (moreData) {
                footHolder.bar.setVisibility(View.VISIBLE);
                footHolder.textLoadMore.setVisibility(View.VISIBLE);
                footHolder.textLoadMore.setText("加载更多...");
            } else {
                footHolder.bar.setVisibility(View.GONE);
                footHolder.textLoadMore.setVisibility(View.GONE);
            }
        }
    }


    public void loadMore(boolean more){
        this.moreData = more;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(clickType == SHEJI_TYPE){
            // 左边设计
            return shejiDataList==null? 0 : shejiDataList.size() + 2;
        }else {
            return anliDataList==null? 0 : anliDataList.size() + 2;
        }
    }



    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return ITEM_TYPE_HEAD;
        }else if(position + 1 == getItemCount()){
            return ITEM_TYPE_FOOT;
        }else{
            if(this.clickType == SHEJI_TYPE){
                return ITEM_TYPE_SHEJI;
            }else {
                return ITEM_TYPE_ANLI;
            }

        }
    }

    @Override
    public void onClick(View view) {
        if(onShejiDataListener!=null && clickType == 0){
            onShejiDataListener.OnShejiDataListener(view, (int)view.getTag());
        }else {
//            Util.setToast(context, "==========4646===");
        }

        if(onAnliDataListener!=null && clickType == 1){
            onAnliDataListener.OnAnliDataListener(view, (int)view.getTag());
        }else {
//            Util.setToast(context, "====47447===");
        }
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

    class ShejishiInfoViewHolder extends RecyclerView.ViewHolder{
        ImageView shejishiIcon;
        ImageView roundOut;
        TextView shejishiName;
        TextView designGrade;
        TextView tvJingyan;
        TextView tvfeiyong;
        TextView tvFengge;
        TextView tvLingyu;
        ExpandableTextView jianjie;
        LinearLayout dataListBar;
        TextView desigText;
        View designTextBar;
        TextView caseText;
        View caseTextBar;
        RelativeLayout relimgEmpty;
        ImageView imgEmpty;
        public ShejishiInfoViewHolder(View itemView) {
            super(itemView);
            caseTextBar = (View) itemView.findViewById(R.id.caseTextBar);
            caseText = (TextView) itemView.findViewById(R.id.caseText);
            designTextBar = (View) itemView.findViewById(R.id.designTextBar);
            desigText = (TextView) itemView.findViewById(R.id.desigText);
            jianjie = (ExpandableTextView) itemView.findViewById(R.id.shejishi_jianjie);
            tvLingyu = (TextView) itemView.findViewById(R.id.tvLingyu);
            tvFengge = (TextView) itemView.findViewById(R.id.tvFengge);
            tvfeiyong = (TextView) itemView.findViewById(R.id.tvfeiyong);
            tvJingyan = (TextView) itemView.findViewById(R.id.tvJingyan);
            designGrade = (TextView) itemView.findViewById(R.id.designGrade);
            shejishiName = (TextView) itemView.findViewById(R.id.shejishiName);
            shejishiIcon = (ImageView) itemView.findViewById(R.id.shejishiIcon);
            roundOut = (ImageView) itemView.findViewById(R.id.roundOut);
            dataListBar = (LinearLayout) itemView.findViewById(R.id.dataListBar);
            relimgEmpty = (RelativeLayout) itemView.findViewById(R.id.relimgEmpty);
            imgEmpty = (ImageView) itemView.findViewById(R.id.imgEmpty);
        }
    }



    public interface OnShejiDataListener{
        void OnShejiDataListener(View view, int shejiPosition);
    }

    private OnShejiDataListener onShejiDataListener;

    public DesignerInfoAdapter.OnShejiDataListener getOnShejiDataListener() {
        return onShejiDataListener;
    }

    public void setOnShejiDataListener(DesignerInfoAdapter.OnShejiDataListener onShejiDataListener) {
        this.onShejiDataListener = onShejiDataListener;
    }

    public interface OnAnliDataListener{
        void OnAnliDataListener(View view, int anliPosition);
    }

    private OnAnliDataListener onAnliDataListener;

    public DesignerInfoAdapter.OnAnliDataListener getOnAnliDataListener() {
        return onAnliDataListener;
    }

    public void setOnAnliDataListener(DesignerInfoAdapter.OnAnliDataListener onAnliDataListener) {
        this.onAnliDataListener = onAnliDataListener;
    }
}
