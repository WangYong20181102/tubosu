package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/13 09:06.
 */
public class SelectTypeLeftAdapter extends BaseAdapter {
    private Context context;
    private List<String> listLeft;
    private int selectItem=0;
    private LayoutInflater inflater;
    public SelectTypeLeftAdapter(Context context, List<String> listLeft) {
        this.context = context;
        this.listLeft = listLeft;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listLeft.size();
    }

    @Override
    public Object getItem(int position) {
        return listLeft.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.selecttype_left,parent,false);
            holder = new MyHolder();
            holder.tvLeftContent = convertView.findViewById(R.id.tv_left_content);
            holder.rlBg = convertView.findViewById(R.id.rl_left);
            holder.viewIcon = convertView.findViewById(R.id.view_icon);
            convertView.setTag(holder);
        }else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.tvLeftContent.setText(listLeft.get(position));
        if (selectItem == position) {
            holder.rlBg.setBackgroundColor(Color.WHITE);
            holder.tvLeftContent.setTypeface(Typeface.DEFAULT_BOLD);
            holder.viewIcon.setVisibility(View.VISIBLE);
        }else {
            holder.rlBg.setBackgroundColor(Color.parseColor("#f6f6f6"));
            holder.tvLeftContent.setTypeface(Typeface.DEFAULT);
            holder.viewIcon.setVisibility(View.GONE);
        }

        return convertView;
    }
    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }


    class MyHolder{
        private TextView tvLeftContent;
        private RelativeLayout rlBg;
        private View viewIcon;
    }
}
