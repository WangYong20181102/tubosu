package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lie on 2017/6/2.
 */

public class SearchHistoryAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<String> dataList = new ArrayList<String>();

    public SearchHistoryAdapter(Context context, List<String> dataList) {
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
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_search_history_adapter_layout, null);
            holder.item = (TextView) convertView.findViewById(R.id.tv_search_history_item);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item.setText(dataList.get(position));
        return convertView;
    }

    class ViewHolder{
        TextView item;
    }
}
