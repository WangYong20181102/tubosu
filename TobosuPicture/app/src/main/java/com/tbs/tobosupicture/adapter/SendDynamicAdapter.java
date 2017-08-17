package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mr.Lin on 2017/8/11 14:39.
 */

public class SendDynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mImageUrlPathList;
    private ArrayList<String> mTitleList;//标题集合
    private HashMap<String, String> mTitleMap = new HashMap<>();

    public SendDynamicAdapter(Context context, ArrayList<String> imageUrlPathList) {
        this.mContext = context;
        this.mImageUrlPathList = imageUrlPathList;
        mTitleList = new ArrayList<>();
        //初始将集合设置为空
        for (int i = 0; i < 9; i++) {
            mTitleList.add("");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_send_dynamic, parent, false);
        SendDynamicViewHolder viewHolder = new SendDynamicViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        holder.setIsRecyclable(false);
        if (holder instanceof SendDynamicViewHolder) {
            //显示图片
            GlideUtils.glideLoader(mContext, mImageUrlPathList.get(position), R.mipmap.loading_img_fail, R.mipmap.loading_img, ((SendDynamicViewHolder) holder).item_send_dynamic_image);
            //标题的添加
            ((SendDynamicViewHolder) holder).item_send_dynamic_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(((SendDynamicViewHolder) holder).item_send_dynamic_edit.getText().toString())) {
                        mTitleList.set(position, ((SendDynamicViewHolder) holder).item_send_dynamic_edit.getText().toString());
//                        mTitleMap.put(mImageUrlPathList.get(position), ((SendDynamicViewHolder) holder).item_send_dynamic_edit.getText().toString());
                    }
                }
            });
            ((SendDynamicViewHolder) holder).item_send_dynamic_edit.setText(mTitleList.get(position));
            //删除按钮点击事件
            ((SendDynamicViewHolder) holder).item_send_dynamic_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageUrlPathList.remove(position);
                    mTitleList.clear();
                    for (int i = 0; i < 9; i++) {
                        mTitleList.add("");
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    //获取子标题列表
    public ArrayList<String> getmTitleList() {
        return mTitleList;
    }

    @Override
    public int getItemCount() {
        return mImageUrlPathList != null ? mImageUrlPathList.size() : 0;
    }

    class SendDynamicViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_send_dynamic_image;//图片
        private ImageView item_send_dynamic_del;//删除按钮
        private EditText item_send_dynamic_edit;//单图描述

        public SendDynamicViewHolder(View itemView) {
            super(itemView);
            item_send_dynamic_image = (ImageView) itemView.findViewById(R.id.item_send_dynamic_image);
            item_send_dynamic_del = (ImageView) itemView.findViewById(R.id.item_send_dynamic_del);
            item_send_dynamic_edit = (EditText) itemView.findViewById(R.id.item_send_dynamic_edit);
        }
    }
}
