package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tbs.tbs_mj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dec on 2017/3/1.
 */

public class PopAdapter extends BaseAdapter {
    private static final String TAG = PopAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> data;
    private int selectedPosition = 0;
    private ArrayList<Boolean> isSelectedList;
    public ArrayList<Boolean> getIsSelectedList() {
        return isSelectedList;
    }
    public void setIsSelectedList(ArrayList<Boolean> isSelectedList) {
        this.isSelectedList = isSelectedList;
    }
    public PopAdapter(Context mContext, ArrayList<String> data){
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
    public void clearSelection(int position) {
        selectedPosition = position;
    }
    public void setSelection(int selected){
        this.selectedPosition = selected;
    }
    @Override
    public int getCount() {
        return (data==null)?0:data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    public interface SelectStyleListener{
        public void onSelectItemListener(String style, int po);
    }
    private SelectStyleListener selectStyleListener;

    public void setSelectStyleListener(SelectStyleListener selectStyleListener) {
        this.selectStyleListener = selectStyleListener;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder itemHolder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_adapter_gvstytle_layout, null);
            itemHolder = new ViewHolder();
            itemHolder.tvStyleitem = (TextView) convertView.findViewById(R.id.tv_style_name);
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ViewHolder) convertView.getTag();
        }

        itemHolder.tvStyleitem.setText(data.get(position));
        if(selectedPosition==position){
            itemHolder.tvStyleitem.setBackgroundResource(R.drawable.shape_style_textview_selected);
            itemHolder.tvStyleitem.setTextColor(mContext.getResources().getColor(R.color.style_textcolor_selected));
        }else{
            itemHolder.tvStyleitem.setBackgroundResource(R.drawable.shape_style_textview_unselected);
            itemHolder.tvStyleitem.setTextColor(mContext.getResources().getColor(R.color.style_textcolor_unselected));
        }


        convertView.setOnClickListener(new OnStyleTextClickListener(position, itemHolder.tvStyleitem));

        return convertView;
    }


    static class ViewHolder{
        TextView tvStyleitem;
    }

    private class OnStyleTextClickListener implements View.OnClickListener{
        private int po;
        private TextView tv;

        public OnStyleTextClickListener(int po, TextView tv){
            this.po = po;
            this.tv = tv;
        }

        @Override
        public void onClick(View v) {
            selectStyleListener.onSelectItemListener(data.get(po), po);
        }
    }
}