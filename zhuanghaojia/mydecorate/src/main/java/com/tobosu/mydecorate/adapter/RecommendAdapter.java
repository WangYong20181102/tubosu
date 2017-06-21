package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.view.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tobosu.mydecorate.R.id.iv_recommand_img;

/**
 * Created by dec on 2016/11/18.
 */

public class RecommendAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<HashMap<String, String>> dataList;

    private LayoutInflater inflater;

    public ArrayList<HashMap<String, String>> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<HashMap<String, String>> dataList) {
        this.dataList = dataList;
    }

    public AddConcernListener getAddConcernListener() {
        return addConcernListener;
    }

    public void setAddConcernListener(AddConcernListener addConcernListener) {
        this.addConcernListener = addConcernListener;
    }

    private AddConcernListener addConcernListener;

    public RecommendAdapter(Context context, ArrayList<HashMap<String, String>> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList==null?0:dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RecommendHolder holder = null;
        if(convertView==null){
            holder = new RecommendHolder();
            convertView = inflater.inflate(R.layout.layout_recommand_concern,null);
            holder.iv_recommand_img = (RoundImageView) convertView.findViewById(iv_recommand_img);
            holder.tv_writer_name = (TextView) convertView.findViewById(R.id.tv_writer_name);
            holder.tv_recommand_description = (TextView) convertView.findViewById(R.id.tv_recommand_description);
            holder.iv_is_concerned = (ImageView) convertView.findViewById(R.id.iv_is_concerned);
            convertView.setTag(holder);
        }else{
            holder = (RecommendHolder) convertView.getTag();
        }


        com.squareup.picasso.Picasso.with(context)
                .load(dataList.get(position).get("header_pic_url"))
                .fit()
                .placeholder(R.mipmap.occupied1)
                .error(R.mipmap.icon_head_default)
                .into(holder.iv_recommand_img);
        holder.tv_writer_name.setText(dataList.get(position).get("nick"));
        holder.tv_recommand_description.setText(dataList.get(position).get("sort_description"));

        if(dataList.get(position).get("is_att").equals("1")){
            holder.iv_is_concerned.setBackgroundResource(R.mipmap.concern_after);
        }else if(dataList.get(position).get("is_att").equals("0")){
            holder.iv_is_concerned.setBackgroundResource(R.mipmap.concern_before);
            holder.iv_is_concerned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addConcernListener.onAddConcernListener(position);
                }
            });
        }

        return convertView;
    }


    static class RecommendHolder{
        RoundImageView iv_recommand_img;
        TextView tv_writer_name;
        TextView tv_recommand_description;
        ImageView iv_is_concerned;
    }


    public interface AddConcernListener{
        void onAddConcernListener(int position);
    }
}
