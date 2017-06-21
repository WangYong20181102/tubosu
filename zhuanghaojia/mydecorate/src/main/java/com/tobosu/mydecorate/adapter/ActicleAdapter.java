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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dec on 2016/10/14.
 */

public class ActicleAdapter extends RecyclerView.Adapter<ActicleAdapter.ActicleHolder> implements View.OnClickListener{
    private static final String TAG = ActicleAdapter.class.getSimpleName();

    private Context mContext;

    private LayoutInflater inflater = null;

    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    public ActicleAdapter(Context mContext,ArrayList<HashMap<String, String>> data) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.data = data;
    }

    @Override
    public ActicleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_user_acticle_adapter_layout, null, false);
        ActicleAdapter.ActicleHolder holder = new ActicleAdapter.ActicleHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ActicleHolder holder, int position) {

        holder.tv_time.setText(data.get(position).get("time"));
        holder.tv_time_text.setText(data.get(position).get("time_unit"));
        holder.tv_title.setText(data.get(position).get("title"));
        holder._tv_type.setText(data.get(position).get("type_name"));
        holder.tv_see_num.setText(data.get(position).get("view_count")+"人看过");
        Picasso.with(mContext)
                .load(data.get(position).get("image_url"))
                .fit()
                .placeholder(R.mipmap.occupied1)
                .into(holder.iv_image);

        holder.itemView.setTag(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public void onClick(View view) {
        if(onRecyclerViewItemClickListener!=null){
            onRecyclerViewItemClickListener.onRecyclerViewItemClick(view, (HashMap<String, String>) view.getTag());
        }
    }

    static class ActicleHolder extends RecyclerView.ViewHolder {

        TextView tv_time;
        TextView tv_time_text;
        TextView tv_title;
        TextView tv_see_num;
        TextView _tv_type;
        ImageView iv_image;

        public ActicleHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_time_text = (TextView) itemView.findViewById(R.id.tv_time_text);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_see_num = (TextView) itemView.findViewById(R.id.tv_see_num);
            _tv_type = (TextView)itemView.findViewById(R.id._tv_type);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view , HashMap<String, String> itemData);
    }
}
