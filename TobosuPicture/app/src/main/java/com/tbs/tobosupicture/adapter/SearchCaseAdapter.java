package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean.SearchRecordBean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/21.
 */

public class SearchCaseAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SearchRecordBean> dataList;

    public SearchCaseAdapter(Context context, ArrayList<SearchRecordBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null?0:dataList.size();
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
            convertView = inflater.inflate(R.layout.adapter_item_search_record_layout, null);
            holder.tvRecord = (TextView) convertView.findViewById(R.id.tv_search_case_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String city = dataList.get(position).getCity_name();
        String layout = dataList.get(position).getLayout_value();
        String area = dataList.get(position).getArea_value();
        String price = dataList.get(position).getPrice_value();
        String style = dataList.get(position).getStyle_name();

        holder.tvRecord.setText(city+ " " +layout+ " " +area+ " " +price+ " " +style);
        return convertView;
    }

    private class ViewHolder{
        TextView tvRecord;
    }
}
