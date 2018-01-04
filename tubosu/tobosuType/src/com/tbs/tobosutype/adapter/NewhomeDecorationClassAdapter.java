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
    private int[] icons = new int[]{R.drawable.new_sheji, R.drawable.xuancai, R.drawable.fangshui,R.drawable.ruanzhuang, R.drawable.fengshui};

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
        if(position == 0){
            holder.left_six.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
            holder.right_six.setVisibility(View.GONE);

        }else if(position == icons.length -1){
            holder.left_six.setVisibility(View.GONE);
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.VISIBLE);
            holder.right_six.setVisibility(View.VISIBLE);
        }else{
            holder.left_six.setVisibility(View.GONE);           //    6
            holder.left.setVisibility(View.VISIBLE);            //    10
            holder.right.setVisibility(View.GONE);              //    10
            holder.right_six.setVisibility(View.GONE);          //    6
        }
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
        TextView left, left_six;
        TextView right, right_six;
        public ClassHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_class_imgsd);
            right=(TextView) itemView.findViewById(R.id.right);
            left=(TextView) itemView.findViewById(R.id.left);
            right_six=(TextView) itemView.findViewById(R.id.right_six);
            left_six=(TextView) itemView.findViewById(R.id.left_six);
        }
    }
}
