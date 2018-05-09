package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tbs_mj.R;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/1/26 13:46.
 */

public class UpdateDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mUpdateContent;

    public UpdateDialogAdapter(Context context, ArrayList<String> updateContent) {
        this.mContext = context;
        this.mUpdateContent = updateContent;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_update_msg, parent, false);
        UpdateItemViewHolder itemViewHolder = new UpdateItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UpdateItemViewHolder) {
            ((UpdateItemViewHolder) holder).item_update_msg_tv.setText("" + mUpdateContent.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mUpdateContent != null ? mUpdateContent.size() : 0;
    }

    private class UpdateItemViewHolder extends RecyclerView.ViewHolder {
        private TextView item_update_msg_tv;

        public UpdateItemViewHolder(View itemView) {
            super(itemView);
            item_update_msg_tv = itemView.findViewById(R.id.item_update_msg_tv);
        }
    }
}
