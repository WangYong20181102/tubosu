package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/8/17 14:39.
 */

public class NewSendDynamicAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private ArrayList<String> mImageUriList;

    public NewSendDynamicAdapter(Context context, ArrayList<String> mImageUriList) {
        this.mContext = context;
        this.mImageUriList = mImageUriList;
    }

    public static interface OnImageViewClickLister {
        void onImageClick(View view, int position);
    }

    public void setOnImageViewClickLister(OnImageViewClickLister onImageViewClickLister) {
        this.onImageViewClickLister = onImageViewClickLister;
    }

    private OnImageViewClickLister onImageViewClickLister = null;


    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//天机更多的按钮
        } else {
            return 2;//普通
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            //正常图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_send_dynamic_img, parent, false);
            ImageItemHolder imageItemHolder = new ImageItemHolder(view);
            imageItemHolder.item_new_send_dynamic_close.setOnClickListener(this);
            imageItemHolder.item_new_send_dynamic_edit.setOnClickListener(this);
            return imageItemHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_send_dynamic_add, parent, false);
            AddImageHolder addImageHolder = new AddImageHolder(view);
            addImageHolder.item_new_send_dynamic_add.setOnClickListener(this);
            return addImageHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageItemHolder) {
            GlideUtils.glideLoader(mContext, mImageUriList.get(position), R.mipmap.loading_img_fail, R.mipmap.loading_img, ((ImageItemHolder) holder).item_new_send_dynamic_img);
            ((ImageItemHolder) holder).item_new_send_dynamic_close.setTag(position);
            ((ImageItemHolder) holder).item_new_send_dynamic_edit.setTag(position);
        } else if (holder instanceof AddImageHolder) {
            ((AddImageHolder) holder).item_new_send_dynamic_add.setTag(position);
            if (mImageUriList.size() == 9) {
                ((AddImageHolder) holder).item_new_send_dynamic_add.setVisibility(View.GONE);
            } else {
                ((AddImageHolder) holder).item_new_send_dynamic_add.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mImageUriList != null ? mImageUriList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onImageViewClickLister != null) {
            onImageViewClickLister.onImageClick(v, (int) v.getTag());
        }
    }

    class ImageItemHolder extends RecyclerView.ViewHolder {
        private ImageView item_new_send_dynamic_img;//单图
        private ImageView item_new_send_dynamic_close;//删除按钮
        private TextView item_new_send_dynamic_edit;//点击进入编辑页

        public ImageItemHolder(View itemView) {
            super(itemView);
            item_new_send_dynamic_edit = (TextView) itemView.findViewById(R.id.item_new_send_dynamic_edit);
            item_new_send_dynamic_close = (ImageView) itemView.findViewById(R.id.item_new_send_dynamic_close);
            item_new_send_dynamic_img = (ImageView) itemView.findViewById(R.id.item_new_send_dynamic_img);
        }
    }

    class AddImageHolder extends RecyclerView.ViewHolder {
        private ImageView item_new_send_dynamic_add;//添加按钮

        public AddImageHolder(View itemView) {
            super(itemView);
            item_new_send_dynamic_add = (ImageView) itemView.findViewById(R.id.item_new_send_dynamic_add);
        }
    }
}
