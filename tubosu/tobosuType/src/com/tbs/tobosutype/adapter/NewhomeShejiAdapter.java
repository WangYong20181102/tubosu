package com.tbs.tobosutype.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.NewHomeDataItem;
import com.tbs.tobosutype.utils.Util;

import java.util.List;

/**
 * Created by Lie on 2017/10/26.
 */

public class NewhomeShejiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<NewHomeDataItem.NewhomeDataBean.ImpressionBean> dataList;
    private final int ITEM_ITEM = 4;
    private final int ITEM_RIGHT_FOOT =6;
    private boolean loadmore = true;


    public NewhomeShejiAdapter(Context context, List<NewHomeDataItem.NewhomeDataBean.ImpressionBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }


    @Override
    public int getItemViewType(int position) {
        if(position < dataList.size()){
            return ITEM_ITEM;
        }else {
            return ITEM_RIGHT_FOOT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_ITEM){
            View view = inflater.inflate(R.layout.newhome_adapter_sheji, parent, false);
            ShejiHolder holder = new ShejiHolder(view);
            view.setOnClickListener(this);
            return holder;
        }

        if(viewType == ITEM_RIGHT_FOOT){
            View view = inflater.inflate(R.layout.newhome_adapter_sheji_foot, parent, false);
            ShejiFoot foot = new ShejiFoot(view);
            return foot;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position<dataList.size() - 1) {
            if (holder instanceof ShejiHolder) {
                ShejiHolder shejiHolder = (ShejiHolder) holder;
                Glide.with(context).load(dataList.get(position).getCover_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(shejiHolder.iv);
                shejiHolder.tvTitle.setText(dataList.get(position).getDesigner_name());
                shejiHolder.tvDesc.setText(dataList.get(position).getSub_title());
                shejiHolder.itemView.setTag(position);
            }
        }

        if(position == getItemCount() - 1){
            if(holder instanceof ShejiFoot){
                ShejiFoot shejiFoot = (ShejiFoot) holder;
                if(loadmore){
                    shejiFoot.foot.setVisibility(View.VISIBLE);
                }else {
                    shejiFoot.foot.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList==null? 0 : dataList.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onRecyclerViewItemClick(v,(int)v.getTag());
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view , int position);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void showLoadMore(boolean loadmore){
        this.loadmore = loadmore;
        notifyDataSetChanged();
    }

    class ShejiFoot extends RecyclerView.ViewHolder{

        ImageView foot;
        public ShejiFoot(View itemView) {
            super(itemView);
            foot = (ImageView) itemView.findViewById(R.id.iv_sheji_foot);
        }
    }

    class ShejiHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvTitle;
        TextView tvDesc;

        public ShejiHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.newhome_sheji_item_img);
            tvTitle = (TextView) itemView.findViewById(R.id.newhome_sheji_item_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_newhome_sheji_item_desc);
        }
    }
}
