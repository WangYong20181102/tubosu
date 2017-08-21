package com.tbs.tobosupicture.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean.CompanySearchRecordJsonEntity;
import com.tbs.tobosupicture.utils.GlideUtils;
import java.util.List;

/**
 * Created by Lie on 2017/8/16.
 */

public class CompanySearchRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<CompanySearchRecordJsonEntity.CompanySearchRecordEntity> dataList;

    private static final int TYPE_HEADER = 0;
    private View headView;
    private static final int TYPE_ITEM = 1;
    private View itmeView;
    private static final int TYPE_FOOTER = 2;
    private View footerView;
    private LinearLayout footerLayout;
    private TextView tvLoadMore;
    private ProgressBar progressBar;

    public CompanySearchRecordAdapter(Context context, List<CompanySearchRecordJsonEntity.CompanySearchRecordEntity> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            headView = inflater.inflate(R.layout.company_header_layout, parent, false);
            HeadViewHolder holder = new HeadViewHolder(headView);
            return holder;
        }

        if(viewType == TYPE_ITEM){
            itmeView = inflater.inflate(R.layout.company_case_item_layout, parent, false);
            CompanyViewHolder holder = new CompanyViewHolder(itmeView);
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CompanyViewHolder){
            CompanyViewHolder companyViewHolder = (CompanyViewHolder) holder;
            String topText = "用户名:" + dataList.get(position-1).getNick() + " " +dataList.get(position-1).getAdd_time() + " " + dataList.get(position-1).getCity();
            String bottomText = dataList.get(position-1).getSearch_condition();

            companyViewHolder.tvTopText.setText(topText);
            companyViewHolder.tvBottomText.setText(bottomText);
            String isread = dataList.get(position-1).getIs_read();
            if("0".equals(isread)){
                companyViewHolder.ivCaseRedFlag.setVisibility(View.GONE);
            }else {
                companyViewHolder.ivCaseRedFlag.setVisibility(View.VISIBLE);
            }
            GlideUtils.glideLoader(context, dataList.get(position-1).getIcon(), R.mipmap.pic, R.mipmap.pic, companyViewHolder.ivCaseIcon, 0);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount()){
            return TYPE_FOOTER;
        }else if(position == 0){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size() + 2;
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTopText;
        TextView tvBottomText;
        ImageView ivCaseIcon;
        ImageView ivCaseRedFlag;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            tvTopText = (TextView)itemView.findViewById(R.id.tvTopText);
            tvBottomText = (TextView)itemView.findViewById(R.id.tvBottomText);
            ivCaseIcon = (ImageView)itemView.findViewById(R.id.ivCaseIcon);
            ivCaseRedFlag = (ImageView)itemView.findViewById(R.id.ivCaseRedFlag);
        }
    }
    public class HeadViewHolder extends RecyclerView.ViewHolder{
        public HeadViewHolder(View itemView) {
            super(itemView);
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
}
