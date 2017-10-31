package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.NewHomeDataItem;

import java.util.List;

/**
 * Created by Lie on 2017/10/26.
 */

public class NewhomeZhuantiAdapter extends RecyclerView.Adapter<NewhomeZhuantiAdapter.ZhuanTiHolder> implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<NewHomeDataItem.NewhomeDataBean.TopicBean> dataList;
    private final int ITEM_ITEM = 1;
    private final int ITEM_FOOT = 0;
    private boolean loadmore = true;

    public NewhomeZhuantiAdapter(Context context, List<NewHomeDataItem.NewhomeDataBean.TopicBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < getItemCount()){
            return ITEM_ITEM;
        }else {
            return ITEM_FOOT;
        }
    }

    @Override
    public ZhuanTiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_ITEM){
            View view = inflater.inflate(R.layout.layout_zhuanti_item_adapter, parent, false);
            ZhuanTiHolder holder = new ZhuanTiHolder(view);
            view.setOnClickListener(this);
            return holder;
        }
//        if(viewType == ITEM_FOOT){
//            View foot = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
//            ZhuanTiFoot zhuanTiFoot = new ZhuanTiFoot(foot);
//            return zhuanTiFoot;
//        }
        return null;
    }

    @Override
    public void onBindViewHolder(ZhuanTiHolder holder, int position) {
//        if(holder instanceof ZhuanTiHolder){
        ZhuanTiHolder zhuanTiHolder = (ZhuanTiHolder) holder;
        Glide.with(context).load(dataList.get(position).getImage_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(zhuanTiHolder.iv);
        zhuanTiHolder.tvZhuantiTime.setText(dataList.get(position).getAdd_time());
        zhuanTiHolder.tvZhuantiTitle.setText(dataList.get(position).getTitle());
        zhuanTiHolder.tvZhuantiDesc.setText(dataList.get(position).getDesc());
        zhuanTiHolder.itemView.setTag(position);
//        }

//        if(holder instanceof ZhuanTiFoot){
//            ZhuanTiFoot footHolder = (ZhuanTiFoot) holder;
//            if(loadmore){
//                footHolder.bar.setVisibility(View.VISIBLE);
//                footHolder.textLoadMore.setText("加载更多...");
//            }else {
//                footHolder.bar.setVisibility(View.GONE);
//                footHolder.textLoadMore.setVisibility(View.GONE);
//            }
//        }
    }

//    public void loadMore(boolean loadmore){
//        this.loadmore = loadmore;
//    }

    @Override
    public int getItemCount() {
        return dataList==null?0:dataList.size();
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
        void onRecyclerViewItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    class ZhuanTiHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvZhuantiTime;
        TextView tvZhuantiTitle;
        TextView tvZhuantiDesc;
        public ZhuanTiHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.newhome_zhuanti_item_img);
            tvZhuantiTime = (TextView) itemView.findViewById(R.id.tv_newhome_zhuanti_item_time);
            tvZhuantiTitle = (TextView) itemView.findViewById(R.id.newhome_zhuanti_item_title);
            tvZhuantiDesc = (TextView) itemView.findViewById(R.id.tv_newhome_zhuanti_item_desc);
        }
    }

//    class ZhuanTiFoot extends RecyclerView.ViewHolder{
//        ProgressBar bar;
//        TextView textLoadMore;
//        public ZhuanTiFoot(View itemView) {
//            super(itemView);
//            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
//            textLoadMore = (TextView) itemView.findViewById(R.id.newhome_loadmore);
//        }
//    }
}
