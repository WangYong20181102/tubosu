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

public class NewhomeKetangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<NewHomeDataItem.NewhomeDataBean.CourseBean> dataList;

    public NewhomeKetangAdapter(Context context, List<NewHomeDataItem.NewhomeDataBean.CourseBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_item_ketang_adapter, parent, false);
        KetangHolder holder = new KetangHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        KetangHolder ketangHolder = (KetangHolder) holder;
        Glide.with(context).load(dataList.get(position).getImage_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(ketangHolder.iv);
        ketangHolder.tvtitle.setText(dataList.get(position).getTitle());
        ketangHolder.tvdesc.setText(dataList.get(position).getAdd_time());
        ketangHolder.itemView.setTag(position);
    }

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


    class KetangHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvtitle;
        TextView tvdesc;

        public KetangHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_ketang_img);
            tvtitle= (TextView) itemView.findViewById(R.id.tv_ketang_title);
            tvdesc= (TextView) itemView.findViewById(R.id.tv_ketang_desc);
        }
    }



}
