package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._SelectMsg;
import com.tbs.tobosutype.bean._Style;
import com.tbs.tobosutype.customview.RoundImageView;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/4/25 09:26.
 */

public class MyGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<_SelectMsg> btnName;
    private LayoutInflater inflater;
    private int mPosition;//用于回显的下标位置


    public MyGridViewAdapter(Context context, List<_SelectMsg> btnnames, int mPosition) {
        this.mContext = context;
        this.btnName = btnnames;
        this.mPosition = mPosition;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return btnName.size();
    }

    @Override
    public _SelectMsg getItem(int position) {
        return btnName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pop_grid, parent, false);
            holder = new MyViewHolder();
            holder.BtntextView = (TextView) convertView.findViewById(R.id.item_pop_tv);
            holder.BtnDotView = (TextView) convertView.findViewById(R.id.item_pop_dot);
            holder.BtnDotImageView = (ImageView) convertView.findViewById(R.id.item_pop_dot_colorful);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.BtntextView.setText("" + btnName.get(position).getName());
        if (!TextUtils.isEmpty(btnName.get(position).getValue())
                && !btnName.get(position).getValue().equals("colour")
                && !btnName.get(position).getValue().equals("ffffff")) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.parseColor(btnName.get(position).getValue()));
            gradientDrawable.setCornerRadius(30);
            holder.BtnDotView.setBackgroundDrawable(gradientDrawable);
        } else if (!TextUtils.isEmpty(btnName.get(position).getValue()) &&
                btnName.get(position).getValue().equals("#ffffff")) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.parseColor(btnName.get(position).getValue()));
            gradientDrawable.setStroke(2, Color.parseColor("#cccccc"));
            gradientDrawable.setCornerRadius(30);
            holder.BtnDotView.setBackgroundDrawable(gradientDrawable);
//            holder.BtnDotView.setVisibility(View.GONE);
//            holder.BtnDotImageView.setVisibility(View.VISIBLE);
//            holder.BtnDotImageView.setImageResource(R.drawable.white_image_dot);

        } else if (!TextUtils.isEmpty(btnName.get(position).getValue()) &&
                btnName.get(position).getValue().equals("colour")) {
            //设置彩色
            holder.BtnDotView.setVisibility(View.GONE);
            holder.BtnDotImageView.setVisibility(View.VISIBLE);
        }
        if (position == mPosition) {
            holder.BtntextView.setTextColor(Color.parseColor("#ffffff"));
            holder.BtntextView.setBackgroundColor(Color.parseColor("#ff882e"));
        }
        return convertView;
    }

    class MyViewHolder {
        private TextView BtntextView;
        private TextView BtnDotView;
        private ImageView BtnDotImageView;
    }
}
