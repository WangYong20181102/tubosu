package com.tbs.tobosutype.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._ImageS;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/11/14 10:23.
 */

public class NewImageSAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "NewImageSAdapter";
    private ArrayList<_ImageS> mImageSArrayList;

    public NewImageSAdapter(Context context, ArrayList<_ImageS> imageSArrayList) {
        this.mContext = context;
        this.mImageSArrayList = imageSArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_image_s, parent, false);
        NewImageSViewHolder newImageSViewHolder = new NewImageSViewHolder(view);
        newImageSViewHolder.item_new_image_s_img_click_view.setOnClickListener(this);
        return newImageSViewHolder;
    }

    public static interface OnImgaeSClickLister {
        void onImageSClick(View view, int position);
    }

    public void setOnImgaeSClickLister(OnImgaeSClickLister onImgaeSClickLister) {
        this.onImgaeSClickLister = onImgaeSClickLister;
    }

    private OnImgaeSClickLister onImgaeSClickLister = null;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewImageSViewHolder) {
            //设置封面图 宽度固定
            //获取屏幕的宽度
            ViewGroup.LayoutParams layoutParams = ((NewImageSViewHolder) holder).item_new_image_s_img.getLayoutParams();
            int spWidth = ((Activity) (((NewImageSViewHolder) holder).item_new_image_s_img.getContext())).getWindowManager().getDefaultDisplay().getWidth();
            layoutParams.width = spWidth / 2;
            float imgW = mImageSArrayList.get(position).getImage_width();
            float imgH = mImageSArrayList.get(position).getImage_height();
            layoutParams.height = (int) ((imgH / imgW) * (spWidth / 2));
            ((NewImageSViewHolder) holder).item_new_image_s_img.setLayoutParams(layoutParams);
            ((NewImageSViewHolder) holder).item_new_image_s_img_click_view.setLayoutParams(layoutParams);
            ((NewImageSViewHolder) holder).item_new_image_s_img_click_view.setTag(position);
            //绑定数据
            //封面图 设置宽高加载
            Glide.with(mContext)
                    .load(mImageSArrayList.get(position).getCover_url())
                    .placeholder(R.drawable.iamge_loading).error(R.drawable.iamge_loading).centerCrop()
                    .override(spWidth / 2, (int) ((imgH / imgW) * (spWidth / 2)))
                    .into(((NewImageSViewHolder) holder).item_new_image_s_img);
        }
    }

    @Override
    public int getItemCount() {
        return mImageSArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (onImgaeSClickLister != null) {
            onImgaeSClickLister.onImageSClick(v, (int) v.getTag());
        }
    }

    class NewImageSViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_new_image_s_img;
        private View item_new_image_s_img_click_view;

        public NewImageSViewHolder(View itemView) {
            super(itemView);
            item_new_image_s_img = (ImageView) itemView.findViewById(R.id.item_new_image_s_img);
            item_new_image_s_img_click_view = (View) itemView.findViewById(R.id.item_new_image_s_img_click_view);
        }
    }
}
