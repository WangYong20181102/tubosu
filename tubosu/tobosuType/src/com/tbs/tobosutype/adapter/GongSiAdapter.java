package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.CompanyBannerItem;
import com.tbs.tobosutype.bean.GongsiItem;
import com.tbs.tobosutype.bean.RCompanyBean;
import java.util.List;


public class GongSiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<GongsiItem> dataList;
    private List<CompanyBannerItem> bannerList;
    private LayoutInflater inflater;
    private static final int BODY_ITEM_TYPE = 1;
    private static final int FOOTER_ITEM_TYPE = 2;
    private boolean gongsiMore = false;
    private boolean hideMore = false;

    private int headViewTopUp = 0;
    private int headViewTopDown = 0;
    private int headViewHeight = 0;
    private List<RCompanyBean> rCompanyBeanList;

    public GongSiAdapter(Context context, List<GongsiItem> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == BODY_ITEM_TYPE){
            View bodyView = inflater.inflate(R.layout.gongsi_layout, parent, false);
            GongsiViewHodler hodler = new GongsiViewHodler(bodyView);
            bodyView.setOnClickListener(this);
            return hodler;
        }else {
            View footView = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            GongsiFoot foot = new GongsiFoot(footView);
            return foot;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() - 1){
            return FOOTER_ITEM_TYPE;
        }else {
            return BODY_ITEM_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof GongsiViewHodler){
            GongsiViewHodler gongsiViewHodler = (GongsiViewHodler) holder;
            gongsiViewHodler.name.setText(dataList.get(position).getName());
            Glide.with(context).load(dataList.get(position).getImg_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(gongsiViewHodler.iv);

            if(dataList.get(position).getRecommend()!=null && "1".equals(dataList.get(position).getRecommend())){
                gongsiViewHodler.tuijianGongsi.setVisibility(View.VISIBLE);
            }else {
                gongsiViewHodler.tuijianGongsi.setVisibility(View.GONE);
            }

            if(dataList.get(position).getCertification()!=null && "1".equals(dataList.get(position).getCertification())){
                gongsiViewHodler.vGongsi.setVisibility(View.VISIBLE);
            }else {
                gongsiViewHodler.vGongsi.setVisibility(View.GONE);
            }

            String rate = dataList.get(position).getGrade();
            if(rate!=null && !"".equals(rate)){
                gongsiViewHodler.ratingBar.setRating(Float.parseFloat(rate));
            }

            String shejiText = "";
            String caseText = "";
            String shejiNum = dataList.get(position).getSuite_count();
            String caseNum = dataList.get(position).getCase_count();
            if(!"".equals(shejiNum)){
                shejiText = "设计: " + shejiNum;
            }
            if(!"".equals(caseNum)){
                caseText = "案例: " + caseNum;
            }
            gongsiViewHodler.tvCaseAndCase.setText(shejiText + "    " + caseText);
            String zxNum = dataList.get(position).getView_count();
            gongsiViewHodler.tvZixunNum.setText("("+zxNum+")");
            String prop = dataList.get(position).getPromotion_title();
            if(prop!=null && !"".equals(prop)){
                gongsiViewHodler.tvYouhui.setText(" "+prop);
                gongsiViewHodler.tvYouhui.setVisibility(View.VISIBLE);
            }else {
                gongsiViewHodler.tvYouhui.setVisibility(View.GONE);
            }

            String ds = dataList.get(position).getDistance();
            if(!TextUtils.isEmpty(ds)){
                gongsiViewHodler.ccDistance.setText(dataList.get(position).getDistance());
                gongsiViewHodler.ccDistance.setVisibility(View.VISIBLE);
            }else {
                gongsiViewHodler.ccDistance.setVisibility(View.GONE);
            }
            gongsiViewHodler.itemView.setTag(position);

        }

        if(holder instanceof GongsiFoot){
            GongsiFoot gongsiFoot = (GongsiFoot) holder;
            if (this.gongsiMore) {
                if(this.hideMore){
                    gongsiFoot.bar.setVisibility(View.GONE);
                    gongsiFoot.textLoadMore.setVisibility(View.GONE);
                    gongsiFoot.textLoadMore.setText("--  我是有底线的  --");
                }else {
                    gongsiFoot.bar.setVisibility(View.VISIBLE);
                    gongsiFoot.textLoadMore.setVisibility(View.VISIBLE);
                    gongsiFoot.textLoadMore.setText("加载更多...");
                }
            } else {
                gongsiFoot.bar.setVisibility(View.GONE);
                gongsiFoot.textLoadMore.setVisibility(View.GONE);
            }

        }
    }

    public int getSelectItemHeadViewTopUpHeight(){
        return headViewTopUp;
    }

    public int getSelectItemHeadViewTopDownHeight(){
        return headViewTopDown;
    }

    public int getHeadViewHeight(){
        return headViewHeight;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 :dataList.size() + 1;
    }


    public void loadMoreGongsi(boolean gongsiMore){
        this.gongsiMore = gongsiMore;
        notifyDataSetChanged();
    }


    public void setHideMore(boolean hideMore){
        this.hideMore = hideMore;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if(onCompanyItemClickListener!=null){
            onCompanyItemClickListener.onCompanyItemClickListener((int)view.getTag());
        }

    }


    class GongsiViewHodler extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView name;
        ImageView vGongsi;
        ImageView tuijianGongsi;
        RatingBar ratingBar;
        TextView tvZixunNum;
        TextView tvCaseAndCase;
        TextView ccDistance;
        TextView tvYouhui;

        public GongsiViewHodler(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.ivCComopanyIcon);
            name = (TextView) itemView.findViewById(R.id.tvGongsiName);
            vGongsi = (ImageView) itemView.findViewById(R.id.vGongsi);
            tuijianGongsi = (ImageView) itemView.findViewById(R.id.tuijianGongsi);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            tvZixunNum = (TextView) itemView.findViewById(R.id.zixunNum);
            tvCaseAndCase = (TextView) itemView.findViewById(R.id.tvCaseAndCase);
            ccDistance = (TextView) itemView.findViewById(R.id.ccDistance);
            tvYouhui = (TextView) itemView.findViewById(R.id.tvYouhui);
        }
    }

    class GongsiFoot extends RecyclerView.ViewHolder {
        ProgressBar bar;
        TextView textLoadMore;

        public GongsiFoot(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
            textLoadMore = (TextView) itemView.findViewById(R.id.newhome_loadmore);
        }
    }



    public interface OnCompanyItemClickListener{
        void onCompanyItemClickListener(int position);
    }

    private OnCompanyItemClickListener onCompanyItemClickListener;

    public OnCompanyItemClickListener getOnCompanyItemClickListener() {
        return onCompanyItemClickListener;
    }

    public void setOnCompanyItemClickListener(OnCompanyItemClickListener onCompanyItemClickListener) {
        this.onCompanyItemClickListener = onCompanyItemClickListener;
    }
}
