package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.NewHomeDataItem;
import com.tbs.tobosutype.utils.TRoundView;

import java.util.List;

/**
 * Created by Lie on 2017/10/26.
 */

public class KeTangAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<NewHomeDataItem.NewhomeDataBean.CourseBean> dataList;


    public KeTangAdapter(Context context, List<NewHomeDataItem.NewhomeDataBean.CourseBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
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
        KetangHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.layout_item_ketang_adapter, null, false);
            holder = new KetangHolder();
            holder.iv = (TRoundView) convertView.findViewById(R.id.iv_ketang_img);
            holder.tvtitle= (TextView) convertView.findViewById(R.id.tv_ketang_title);
            holder.tvdesc= (TextView) convertView.findViewById(R.id.tv_ketang_desc);
            convertView.setTag(holder);
        }else {
            holder = (KetangHolder) convertView.getTag();
        }

        holder.iv.setType(1);
        Glide.with(context).load(dataList.get(position).getImage_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(holder.iv);
        holder.tvtitle.setText(dataList.get(position).getTitle());
        holder.tvdesc.setText(dataList.get(position).getAdd_time());
        return convertView;
    }

    class KetangHolder{
        TRoundView iv;
        TextView tvtitle;
        TextView tvdesc;
    }
}
