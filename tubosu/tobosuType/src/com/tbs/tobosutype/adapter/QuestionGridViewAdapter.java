package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.QuestionTypeListBean;
import com.tbs.tobosutype.bean._SelectMsg;

import java.util.List;

/**
 * Created by WangYong on 2018/11/8 11:24.
 */

public class QuestionGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<QuestionTypeListBean> btnName;
    private LayoutInflater inflater;
    private int mPosition;//用于回显的下标位置

    public QuestionGridViewAdapter(Context context, List<QuestionTypeListBean> btnnames, int mPosition) {
        this.mContext = context;
        this.btnName = btnnames;
        this.mPosition = mPosition;
        btnName.remove(0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return btnName.size();
    }

    @Override
    public QuestionTypeListBean getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_question_pop_grid, parent, false);
            holder = new MyViewHolder();
            holder.BtntextView = (TextView) convertView.findViewById(R.id.item_pop_tv);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.BtntextView.setText("" + btnName.get(position).getCategory_name());
        if (position == mPosition && mPosition != -1) {
            holder.BtntextView.setTextColor(Color.parseColor("#ff6b14"));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.parseColor("#fff4ed"));
            gradientDrawable.setStroke(1, Color.parseColor("#ffa773"));
            gradientDrawable.setCornerRadius(40);
            holder.BtntextView.setBackgroundDrawable(gradientDrawable);
        } else {
            holder.BtntextView.setTextColor(Color.parseColor("#363650"));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setStroke(1, Color.parseColor("#eeeeee"));
            gradientDrawable.setColor(Color.parseColor("#ffffff"));
            gradientDrawable.setCornerRadius(40);
            holder.BtntextView.setBackgroundDrawable(gradientDrawable);
        }
        return convertView;
    }

    public void setSelectPosition(int selectPosition) {
        this.mPosition = selectPosition;

    }

    class MyViewHolder {
        private TextView BtntextView;
    }
}
