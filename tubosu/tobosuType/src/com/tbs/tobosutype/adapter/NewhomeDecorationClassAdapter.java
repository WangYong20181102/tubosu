package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.NewHomeDataItem;

import java.util.List;

/**
 * Created by Lie on 2017/10/26.
 */

public class NewhomeDecorationClassAdapter extends RecyclerView.Adapter<NewhomeDecorationClassAdapter.ClassHolder> implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private int[] icons = new int[]{R.drawable.zhuangxiuqian, R.drawable.zhuangxiuzhong, R.drawable.zhuangxiuhou,R.drawable.xuancai, R.drawable.sheji, R.drawable.fengshui};

    public NewhomeDecorationClassAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public NewhomeDecorationClassAdapter.ClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_item_class_adapter, parent, false);
        ClassHolder holder = new ClassHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewhomeDecorationClassAdapter.ClassHolder holder, int position) {
        holder.iv.setBackgroundResource(icons[position]);
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return icons==null?0:icons.length;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onRecyclerViewItemClick(v,(int)v.getTag());
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view , int position);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    class ClassHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        public ClassHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_class_imgsd);
        }
    }
}
