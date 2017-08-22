package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.SeeImageActivity;
import com.tbs.tobosupicture.bean.CaseTypeChild;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.view.RoundAngleImageView;

import java.util.ArrayList;

/**
 *
 * Created by Lie on 2017/7/17.
 */

public class GvStageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> dataList;
    private LayoutInflater inflater;
    private String id;

    public GvStageAdapter(Context context, ArrayList<String> dataList, String id){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
        this.id = id;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_img, null);
            holder.iv = (RoundAngleImageView) convertView.findViewById(R.id.iv_imgs);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        GlideUtils.glideLoader(context, dataList.get(position),R.mipmap.loading_img_fail, R.mipmap.loading_img,holder.iv);

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, SeeImageActivity.class);
                Bundle b = new Bundle();
                b.putString("img_id", id);
                i.putExtra("img_bundle", b);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    class ViewHolder {
        RoundAngleImageView iv;
    }
}
