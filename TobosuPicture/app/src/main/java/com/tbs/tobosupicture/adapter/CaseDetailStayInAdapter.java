package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.utils.GlideUtils;
import java.util.ArrayList;

/**
 * Created by Lie on 2017/8/2.
 */

public class CaseDetailStayInAdapter extends BaseAdapter {
    private String TAG = "CaseDetailImgAdapter";
    private ArrayList<String> dataList;
    private Context context;
    private LayoutInflater inflater;

    public CaseDetailStayInAdapter(Context context, ArrayList<String> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return dataList == null ?0 : dataList.size();
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
        ImgViewHolder holder;
        if(convertView==null){
            holder = new ImgViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_case_001_img, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_detail_case21);
//            holder.tv = (TextView) convertView.findViewById(R.id.tvTitleCaseDetail);
            convertView.setTag(holder);
        }else {
            holder = (ImgViewHolder) convertView.getTag();
        }


        GlideUtils.glideLoader(context, dataList.get(position), R.mipmap.loading_img_fail, R.mipmap.loading_img, holder.iv, 1);
//        holder.tv.setText(dataList.get(position).getSpace_name());

        return convertView;
    }

    class ImgViewHolder{
        ImageView iv;
//        TextView tv;
    }
}
