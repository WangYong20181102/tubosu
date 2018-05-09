package com.tbs.tbs_mj.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean.CompanyCityBean;
import java.util.ArrayList;

public class ChoseCityAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CompanyCityBean> dataList;
    private int selectPosition = -1;


    public ChoseCityAdapter(Context context, ArrayList<CompanyCityBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null? 0:dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ProHolder proHolder = null;
        if(view == null){
            proHolder = new ProHolder();
            view =  inflater.inflate(R.layout.city_layout_adapter, null);
            proHolder.textView = (TextView) view.findViewById(R.id.cityTExt);
            view.setTag(proHolder);
        }else {
            proHolder = (ProHolder) view.getTag();
        }

        proHolder.textView.setText(dataList.get(i).getCity_name());
        if(selectPosition == i){
            proHolder.textView.setTextColor(Color.parseColor("#FF6F20"));
        }else {
            proHolder.textView.setTextColor(Color.parseColor("#000000"));
        }
        return view;
    }



    class ProHolder{
        TextView textView;
    }
}
