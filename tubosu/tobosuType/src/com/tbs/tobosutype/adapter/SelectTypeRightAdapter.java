package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tbs.tobosutype.R;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/13 09:07.
 */
public class SelectTypeRightAdapter extends BaseAdapter {
    private Context context;
    private List<String> listRight;
    private LayoutInflater inflater;
    private int selectItem = 0;

    public SelectTypeRightAdapter(Context context, List<String> listRight) {
        this.context = context;
        this.listRight = listRight;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listRight.size();
    }

    @Override
    public Object getItem(int position) {
        return listRight.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.selecttype_right, parent, false);
            holder = new MyHolder();
            holder.tvRightContent = convertView.findViewById(R.id.tv_right_content);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.tvRightContent.setText(listRight.get(position));

        if (selectItem == position) {
            holder.tvRightContent.setTextColor(Color.parseColor("#ff6b14"));
            holder.tvRightContent.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.tvRightContent.setTextColor(Color.parseColor("#363650"));
            holder.tvRightContent.setTypeface(Typeface.DEFAULT);
        }


        return convertView;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    class MyHolder {
        private TextView tvRightContent;
    }

}
