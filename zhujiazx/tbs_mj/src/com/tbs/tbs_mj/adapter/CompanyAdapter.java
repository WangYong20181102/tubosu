package com.tbs.tbs_mj.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean.CompanyBean;
import com.tbs.tbs_mj.bean._ImageS;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/10/14.
 */

public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private String TAG = "CompanyAdapter";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CompanyBean> dataList;
    private boolean isDeleting = false;
    private int ITEM_BODY_TYPE = 0;
    private int ITEM_FOOT_TYPE = 1;


    public CompanyAdapter(Context context, ArrayList<CompanyBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    private boolean more = false;

    public void setDeletingStutas(boolean delete){
        isDeleting = delete;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){
            View view = inflater.inflate(R.layout.company_layout_item, parent, false);
            CompanyHolder holder = new CompanyHolder(view);
            return holder;
        }else{
            View fooot = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            FooterHolder footerHolder = new FooterHolder(fooot);
            return footerHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof CompanyHolder){
            final CompanyHolder cHolder = (CompanyHolder) holder;
            String certified = dataList.get(position).getIs_certified();
            if("1".equals(certified)){
                cHolder.comapanyFlag.setVisibility(View.VISIBLE);
            }else {
                cHolder.comapanyFlag.setVisibility(View.GONE);
            }
            cHolder.companyName.setText(dataList.get(position).getName());
            Glide.with(context).load(dataList.get(position).getIcon()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(cHolder.comapanyIcop);
            cHolder.tvDesignNum.setText(dataList.get(position).getSuite_count());
            cHolder.tvCaseNum.setText(dataList.get(position).getCase_count());
            cHolder.tvCompanyAddr.setText(dataList.get(position).getDistrict_name());

            if(isDeleting){
                // 在编辑状态下
                cHolder.delete_icon_company.setVisibility(View.VISIBLE);
                if(dataList.get(position).isSelected()){
                    cHolder.delete_icon_company.setBackgroundResource(R.drawable.d_selected);
                }else {
                    cHolder.delete_icon_company.setBackgroundResource(R.drawable.d_unselect);
                }
            }else {
                cHolder.delete_icon_company.setVisibility(View.GONE);
            }
            cHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    companyItemClickListener.onCompanyItemClickListener(cHolder.getAdapterPosition(), dataList);
                }
            });
        }

        if(holder instanceof FooterHolder){
            FooterHolder foot = (FooterHolder) holder;
            if(more){
                foot.bar.setVisibility(View.VISIBLE);
                foot.loadText.setVisibility(View.VISIBLE);
            }else{
                foot.bar.setVisibility(View.GONE);
                foot.loadText.setVisibility(View.GONE);
            }
        }
    }


    public void loadMoreData(boolean more){
        this.more = more;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1){
            return ITEM_FOOT_TYPE;
        }else{
            return ITEM_BODY_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null? 0:dataList.size() + 1;
    }



    public ArrayList<CompanyBean> getCompanyEntityList() {
        if (dataList == null) {
            dataList = new ArrayList<CompanyBean>();
        }
        return dataList;
    }

    class CompanyHolder extends RecyclerView.ViewHolder{
        ImageView comapanyIcop;
        TextView companyName;
        ImageView comapanyFlag;
        TextView tvDesignNum;
        TextView tvCaseNum;
        TextView tvCompanyAddr;
        ImageView delete_icon_company;
        public CompanyHolder(View itemView) {
            super(itemView);
            comapanyIcop = (ImageView) itemView.findViewById(R.id.comapanyIcop);
            companyName = (TextView) itemView.findViewById(R.id.companyName);
            comapanyFlag = (ImageView) itemView.findViewById(R.id.comapanyFlag);
            tvDesignNum = (TextView) itemView.findViewById(R.id.tvDesignNum);
            tvCaseNum = (TextView) itemView.findViewById(R.id.tvCaseNum);
            tvCompanyAddr = (TextView) itemView.findViewById(R.id.tvCompanyAddr);
            delete_icon_company = (ImageView) itemView.findViewById(R.id.delete_icon_company);
        }
    }


    class FooterHolder extends RecyclerView.ViewHolder{
        ProgressBar bar;
        TextView loadText;
        public FooterHolder(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
            loadText = (TextView) itemView.findViewById(R.id.newhome_loadmore);
        }
    }

    public interface OnCompanyItemClickListener{
        void onCompanyItemClickListener(int position, ArrayList<CompanyBean> companyList);
    }

    public OnCompanyItemClickListener getCompanyItemClickListener() {
        return companyItemClickListener;
    }

    public void setCompanyItemClickListener(OnCompanyItemClickListener companyItemClickListener) {
        this.companyItemClickListener = companyItemClickListener;
    }

    private OnCompanyItemClickListener companyItemClickListener;
}
