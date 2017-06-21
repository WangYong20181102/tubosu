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
 * Created by dec on 2016/10/17.
 */

public class MyConcernWriterAdapter extends RecyclerView.Adapter<MyConcernWriterAdapter.WriterHolder> implements View.OnClickListener {
    private static final String TAG = MyConcernWriterAdapter.class.getSimpleName();

    private Context context;

    private ArrayList<HashMap<String, String>> data = null;

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }

    public void setData(ArrayList<HashMap<String, String>> data) {
        this.data = data;
    }

    private LayoutInflater inflater = null;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;

    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public MyConcernWriterAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public WriterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_my_concerned_writer_adapter_layout, null, false);
        MyConcernWriterAdapter.WriterHolder holder = new MyConcernWriterAdapter.WriterHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(WriterHolder holder, int position) {

        if(!"".equals(data.get(position).get("header_pic_url"))){
            Picasso.with(context)
                    .load(data.get(position).get("header_pic_url"))
                    .fit()
                    .placeholder(R.mipmap.occupied1)
                    .into(holder.headPic);
        }


        if(data.get(position).get("nick").equals("1")){
            holder.iv_read.setVisibility(View.VISIBLE);
        }else {
            holder.iv_read.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(data.get(position).get("nick"));
        holder.tvNum.setText(data.get(position).get("count_article")+"篇内容");
        holder.tvTime.setText(data.get(position).get("time_num"));
        holder.tvTimeUnit.setText(data.get(position).get("time_uint"));

        holder.itemView.setTag(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    @Override
    public void onClick(View view) {
        if(onRecyclerViewItemClickListener!=null){
            onRecyclerViewItemClickListener.onRecyclerViewItemClick(view, (HashMap<String, String>) view.getTag());
        }
    }

    static class WriterHolder extends RecyclerView.ViewHolder {
        RoundImageView headPic;
        TextView tvTitle;
        TextView tvNum;
        TextView tvTime;
        TextView tvTimeUnit;
        ImageView iv_read;

        public WriterHolder(View itemView) {
            super(itemView);
            headPic = (RoundImageView) itemView.findViewById(R.id.riv_my_writer);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_my_writer_tile);
            tvNum = (TextView) itemView.findViewById(R.id.tv_my_writer_num);
            tvTime = (TextView) itemView.findViewById(R.id.tv_my_writer_time);
            tvTimeUnit = (TextView) itemView.findViewById(R.id.tv_my_writer_time_unit);
            iv_read = (ImageView) itemView.findViewById(R.id.is_read);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view , HashMap<String, String> itemData);
    }
}
