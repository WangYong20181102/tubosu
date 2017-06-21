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
 * Created by dec on 2016/9/27.
 */

public class DragAdapter1 extends BaseAdapter {
    private static final String TAG = DragAdapter1.class.getSimpleName();
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> data;
    private OnSelectedItemListerner onSelectedItemListerner;
    private List<TextView> tvList = new ArrayList<TextView>();



    public DragAdapter1(Context mContext, List<String> data){
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public OnSelectedItemListerner getOnSelectedItemListerner() {
        return onSelectedItemListerner;
    }

    public void setOnSelectedItemListerner(OnSelectedItemListerner onSelectedItemListerner) {
        this.onSelectedItemListerner = onSelectedItemListerner;
    }

    @Override
    public int getCount() {
        return (data==null)?0:data.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder itemHolder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_adapter_layout, null);
            itemHolder = new ViewHolder();
            itemHolder.tv_item = (TextView) convertView.findViewById(R.id.tv_gridview_item);
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ViewHolder) convertView.getTag();
        }
        itemHolder.tv_item.setText(data.get(position));

        tvList.add(itemHolder.tv_item);

        itemHolder.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItemListerner.setOnSelectedItemListerner(position, tvList);
            }
        });
        return convertView;
    }


    static class ViewHolder{
        TextView tv_item;
    }

    public interface OnSelectedItemListerner{
        void setOnSelectedItemListerner(int postion, List<TextView> list);
    }
}
