package com.tbs.tobosutype.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.AnswerItemDetailsActivity;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/6 15:14.
 */
public class AnswerDetailsGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> integerList;
    private AnswerItemDetailsActivity activity;
    public AnswerDetailsGridViewAdapter(Context context, List<String> integerList) {
        this.context = context;
        this.integerList = integerList;
        activity = (AnswerItemDetailsActivity) context;
    }

    @Override
    public int getCount() {
        return integerList.size() > 3 ? 3 : integerList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.detail_gridview_item, parent, false);
            myViewHolder = new MyViewHolder();
            myViewHolder.imageItem = convertView.findViewById(R.id.image_item);
            myViewHolder.rlAddNum = convertView.findViewById(R.id.rl_add_num);
            myViewHolder.tvAddNum = convertView.findViewById(R.id.tv_add_num);
            convertView.setTag(myViewHolder);
        }else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        GlideUtils.glideLoader(context,integerList.get(position),myViewHolder.imageItem);
        if (position == 2){
            myViewHolder.rlAddNum.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.rlAddNum.setVisibility(View.GONE);
        }
        myViewHolder.rlAddNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViewPagerImage(2);
            }


        });
        myViewHolder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViewPagerImage(position);
            }
        });
        return convertView;
    }

    private void showViewPagerImage(int position) {
        activity.showBigPhoto(integerList,position);

    }

    class MyViewHolder {
        private TextView tvAddNum; //张数
        private ImageView imageItem; //图片
        private RelativeLayout rlAddNum; //阴影布局

    }

}
