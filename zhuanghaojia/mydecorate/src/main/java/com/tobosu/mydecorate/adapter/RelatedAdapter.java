package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.view.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dec on 2016/10/13.
 *
 */

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = RelatedAdapter.class.getSimpleName();

    private Context mContext;

    private LayoutInflater inflater = null;

    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    public RelatedAdapter(Context mContext,ArrayList<HashMap<String, String>> data) {
        this.mContext = mContext;
        this.data = data;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建新View，被LayoutManager所调用
        View view = inflater.inflate(R.layout.item_collection_adapter_layout, null, false);
        view.setOnClickListener(this);
        RelatedAdapter.ViewHolder holder = new RelatedAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将数据与界面进行绑定的操作

        holder.tvTitle.setText(data.get(position).get("title"));
        holder.tvCompany.setText(data.get(position).get("nick"));
        holder.tvTime.setText(data.get(position).get("_time") + data.get(position).get("_unit"));
        holder.tvSeeNum.setText(data.get(position).get("show_count"));
        holder.tvType.setText(data.get(position).get("type_name"));

        //公司头像
        Picasso.with(mContext)
                .load(data.get(position).get("header_pic_url"))
                .fit()
                .placeholder(R.mipmap.occupied1)
                .into(holder.imageSmall);


        Picasso.with(mContext)
                .load(data.get(position).get("image_url"))
                .fit()
                .placeholder(R.mipmap.occupied2)
                .error(R.mipmap.occupied4)
                .into(holder.image);

        holder.itemView.setTag(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data==null? 0:data.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onRecyclerViewItemClick(v, (HashMap<String, String>) v.getTag());
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvCompany;
        TextView tvTime;
        TextView tvSeeNum;
        TextView tvType;
        RoundImageView imageSmall;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_collect_title);
            tvCompany = (TextView) itemView.findViewById(R.id.tv_collection_company);
            tvTime = (TextView) itemView.findViewById(R.id.tv_collection_time);
            tvSeeNum = (TextView) itemView.findViewById(R.id.tv_acticle_see_num);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            imageSmall = (RoundImageView) itemView.findViewById(R.id.iv_collection_lefticon);
            image = (ImageView) itemView.findViewById(R.id.iv_collection);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, HashMap<String, String> data);
    }
}