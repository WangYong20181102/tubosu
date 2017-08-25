package com.tbs.tobosupicture.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean.DistrictEntity;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.view.RoundAngleImageView;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/31.
 */

public class DistrictAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DistrictEntity> dataList;
    private LayoutInflater inflater;

    public DistrictAdapter(Context context, ArrayList<DistrictEntity> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList==null ?0 :dataList.size();
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
        DistrictViewHolder holder;
        if(convertView==null){
            holder = new DistrictViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_gv_district, null);
            holder.iv = (RoundAngleImageView) convertView.findViewById(R.id.ivDistrictIcon);
            holder.tv = (TextView) convertView.findViewById(R.id.tvDistrictName);

            convertView.setTag(holder);
        }else {
            holder = (DistrictViewHolder) convertView.getTag();
        }

        GlideUtils.glideLoader(context, dataList.get(position).getImage_url(), R.mipmap.loading_img_fail, R.mipmap.loading_img, holder.iv);
        holder.tv.setText(dataList.get(position).getName());
        return convertView;
    }

    class DistrictViewHolder{
        RoundAngleImageView iv;
        TextView tv;
    }



}
