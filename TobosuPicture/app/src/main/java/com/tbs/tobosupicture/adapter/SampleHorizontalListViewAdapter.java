package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.activity.SeeImageActivity;
import com.tbs.tobosupicture.bean.DesignerImpressionEntity;

import java.util.ArrayList;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.utils.GlideUtils;

/**
 * Created by Lie on 2017/7/25.
 */

public class SampleHorizontalListViewAdapter extends BaseAdapter {
    private ArrayList<DesignerImpressionEntity> dataList;
    private Context mContext;
    private LayoutInflater inflater;

    public SampleHorizontalListViewAdapter(Context mContext, ArrayList<DesignerImpressionEntity> dataList) {
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(mContext);
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0:dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SampleViewHolder vh = null;
        if (convertView == null) {
            vh = new SampleViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_samplepic_layout, null);
            vh.iv_sample_img = (ImageView) convertView.findViewById(R.id.iv_sample_img);
            vh.tv_sample_num = (TextView) convertView.findViewById(R.id.tv_sample_num);
            vh.tv_sample_desc = (TextView) convertView.findViewById(R.id.tv_sample_desc);
            convertView.setTag(vh);
        } else {
            vh = (SampleViewHolder) convertView.getTag();
        }
        GlideUtils.glideLoader(mContext, dataList.get(position).getImg_url(), R.mipmap.case_loading_img_fail, R.mipmap.loading_img,vh.iv_sample_img);
        vh.tv_sample_num.setText(dataList.get(position).getImage_count());
        String des = dataList.get(position).getStyle_name() +" " + dataList.get(position).getLayout_name() + " 预算"
                + dataList.get(position).getPlan_price() + "万";
        vh.tv_sample_desc.setText(des);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, SeeImageActivity.class);
                Bundle b = new Bundle();
                b.putString("img_id", dataList.get(position).getId());
                i.putExtra("img_bundle", b);
                mContext.startActivity(i);
            }
        });
        return convertView;
    }

    class SampleViewHolder{
        ImageView iv_sample_img;
        TextView tv_sample_num;
        TextView tv_sample_desc;
    }
}
