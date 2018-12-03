package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/12/3 16:58.
 */
public class MessageCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<String> stringList;
    private Context context;

    public MessageCenterAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_center_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            GlideUtils.glideLoader(context, AppInfoUtil.getUserIcon(context), R.drawable.iamge_loading, R.drawable.iamge_loading, ((MyViewHolder) holder).imageHeadIcon, 0);
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
    private class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageHeadIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageHeadIcon = itemView.findViewById(R.id.image_head_icon);
        }
    }
}
