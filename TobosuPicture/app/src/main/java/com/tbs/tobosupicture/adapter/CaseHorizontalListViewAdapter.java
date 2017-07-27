package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean.DesignerCaseEntity;
import com.tbs.tobosupicture.bean.DesignerImpressionEntity;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/25.
 */

public class CaseHorizontalListViewAdapter extends BaseAdapter {
    private ArrayList<DesignerCaseEntity> dataList;
    private Context mContext;
    private LayoutInflater inflater;

    public CaseHorizontalListViewAdapter(Context mContext, ArrayList<DesignerCaseEntity> dataList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        SampleViewHolder vh = null;
        if (convertView == null) {
            vh = new SampleViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_samplepic_layout, null);
            vh.iv_case_img = (ImageView) convertView.findViewById(R.id.iv_sample_img);
            vh.tv_case_num = (TextView) convertView.findViewById(R.id.tv_sample_num);
            vh.tv_case_desc = (TextView) convertView.findViewById(R.id.tv_sample_desc);
            convertView.setTag(vh);
        } else {
            vh = (SampleViewHolder) convertView.getTag();
        }
        GlideUtils.glideLoader(mContext, dataList.get(position).getImg_url(), R.mipmap.case_loading_img_fail, R.mipmap.loading_img,vh.iv_case_img);
//        vh.tv_sample_num.setText(dataList.get(position).get());
        vh.tv_case_num.setVisibility(View.GONE);
        String des = dataList.get(position).getShi() +"室" + dataList.get(position).getTing() + "厅" +
                dataList.get(position).getWei()+"卫 " + dataList.get(position).getArea() + "m²  "
                + dataList.get(position).getPrice() + "万 (" + dataList.get(position).getDesmethod() + ")";
        vh.tv_case_desc.setText(des);
        return convertView;
    }

    class SampleViewHolder{
        ImageView iv_case_img;
        TextView tv_case_num;
        TextView tv_case_desc;
    }
}
