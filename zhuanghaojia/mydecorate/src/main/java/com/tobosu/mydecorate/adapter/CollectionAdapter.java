package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.view.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dec on 2016/10/12.
 */

public class CollectionAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String, String>> data = null;
    private LayoutInflater inflater = null;


    public CollectionAdapter(Context mContext, ArrayList<HashMap<String, String>> data){
        this.mContext = mContext;
        this.data = data;
        this.inflater =LayoutInflater.from(mContext);
    }


    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }

    public void setData(ArrayList<HashMap<String, String>> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return (data==null)? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CollectionHolder holder = null;
        if (convertView==null) {
            holder = new CollectionHolder();
            convertView = inflater.inflate(R.layout.item_collection_adapter_layout, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_collect_title);
            holder.tvCompany = (TextView) convertView.findViewById(R.id.tv_collection_company);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_collection_time);
            holder.tvSeeNum = (TextView) convertView.findViewById(R.id.tv_acticle_see_num);
            holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            holder.imageSmall = (RoundImageView) convertView.findViewById(R.id.iv_collection_lefticon);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_collection);

            convertView.setTag(holder);
        }else {
            holder = (CollectionHolder) convertView.getTag();
        }

        holder.tvTitle.setText(data.get(position).get("title"));
        holder.tvCompany.setText(data.get(position).get("nick"));
        holder.tvTime.setText(data.get(position).get("_time") + " " + data.get(position).get("_unit"));

        holder.tvSeeNum.setText(data.get(position).get("show_count"));
        holder.tvType.setText(data.get(position).get("type_name"));

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


        return convertView;
    }

    static class CollectionHolder{
        TextView tvTitle;
        TextView tvCompany;
        TextView tvTime;
        TextView tvSeeNum;
        TextView tvType;
        RoundImageView imageSmall;
        ImageView image;
    }

}
