package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/1/12 09:47.
 */

public class SuggestionAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private ArrayList<String> mImageUriList;

    public SuggestionAdapter(Context context, ArrayList<String> mImageUriList) {
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
            return 1;//添加更多的按钮
        } else {
            return 2;//普通
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            //正常显示的图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_suggestion_img, parent, false);
            ImageItemViewHolder imageItemViewHolder = new ImageItemViewHolder(view);
            imageItemViewHolder.item_suggestion_close.setOnClickListener(this);
            return imageItemViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_suggestion_add, parent, false);
            ImageAddItemViewHolder imageAddItemViewHolder = new ImageAddItemViewHolder(view);
            imageAddItemViewHolder.item_suggestion_img_add.setOnClickListener(this);
            return imageAddItemViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageItemViewHolder) {
            //设置TAG
            ((ImageItemViewHolder) holder).item_suggestion_close.setTag(position);
            //加载图片
            GlideUtils.glideLoader(mContext, mImageUriList.get(position),
                    R.drawable.iamge_loading,
                    R.drawable.iamge_loading,
                    ((ImageItemViewHolder) holder).item_suggestion_img);
        } else if (holder instanceof ImageAddItemViewHolder) {
            ((ImageAddItemViewHolder) holder).item_suggestion_img_add.setTag(position);
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

    //加载图片的holder
    private class ImageItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_suggestion_img;//单图
        private ImageView item_suggestion_close;//删除按钮

        public ImageItemViewHolder(View itemView) {
            super(itemView);
            item_suggestion_close = (ImageView) itemView.findViewById(R.id.item_suggestion_close);
            item_suggestion_img = (ImageView) itemView.findViewById(R.id.item_suggestion_img);

        }
    }

    //添加的按钮
    private class ImageAddItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_suggestion_img_add;//添加按钮

        public ImageAddItemViewHolder(View itemView) {
            super(itemView);
            item_suggestion_img_add = (ImageView) itemView.findViewById(R.id.item_suggestion_img_add);
        }
    }
}
