package com.tbs.tbsbusiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.bean._MyStore;
import com.tbs.tbsbusiness.util.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/6/7 14:07.
 */
public class ChangeRongyuAdapter   extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private ArrayList<_MyStore.HonorImgBean> mHonorImgBeanArrayList;

    public ChangeRongyuAdapter(Context context, ArrayList<_MyStore.HonorImgBean> honorImgBeanArrayList) {
        this.mContext = context;
        this.mHonorImgBeanArrayList = honorImgBeanArrayList;
    }

    public static interface OnItemClickLister {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    private OnItemClickLister onItemClickLister = null;

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//添加更多的按钮
        } else {
            return 2;//普通图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            //显示图片view
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_suggestion_img, parent, false);
            ImageItemViewHolder imageItemViewHolder = new ImageItemViewHolder(view);
            imageItemViewHolder.item_suggestion_img_rl.setOnClickListener(this);
            imageItemViewHolder.item_rongyu_close.setOnClickListener(this);
            return imageItemViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_suggestion_add, parent, false);
            ImageAddItemViewHolder imageAddItemViewHolder = new ImageAddItemViewHolder(view);
            imageAddItemViewHolder.item_image_add.setOnClickListener(this);
            return imageAddItemViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageItemViewHolder) {
            //设置TAG
            ((ImageItemViewHolder) holder).item_rongyu_close.setTag(position);
            ((ImageItemViewHolder) holder).item_suggestion_img_rl.setTag(position);
            //加载图片
            GlideUtils.glideLoader(mContext, mHonorImgBeanArrayList.get(position).getImg_url(),
                    R.drawable.iamge_loading,
                    R.drawable.iamge_loading, ((ImageItemViewHolder) holder).item_rongyu_img);
        } else if (holder instanceof ImageAddItemViewHolder) {
            ((ImageAddItemViewHolder) holder).item_image_add.setTag(position);
            if (mHonorImgBeanArrayList.size() >= 10) {
                ((ImageAddItemViewHolder) holder).item_image_add.setVisibility(View.GONE);
            } else {
                ((ImageAddItemViewHolder) holder).item_image_add.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mHonorImgBeanArrayList != null ? mHonorImgBeanArrayList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null) {
            onItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    //图片加载
    private class ImageItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_rongyu_img;//单图
        private ImageView item_rongyu_close;//删除按钮
        private RelativeLayout item_suggestion_img_rl;//外层点击框

        public ImageItemViewHolder(View itemView) {
            super(itemView);
            item_rongyu_close = (ImageView) itemView.findViewById(R.id.item_suggestion_close);
            item_rongyu_img = (ImageView) itemView.findViewById(R.id.item_suggestion_img);
            item_suggestion_img_rl = (RelativeLayout) itemView.findViewById(R.id.item_suggestion_img_rl);

        }
    }

    //添加按钮
    private class ImageAddItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_image_add;//添加按钮

        public ImageAddItemViewHolder(View itemView) {
            super(itemView);
            item_image_add = (ImageView) itemView.findViewById(R.id.item_suggestion_img_add);
        }
    }
}