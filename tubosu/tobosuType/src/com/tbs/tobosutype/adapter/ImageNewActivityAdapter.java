package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._ImageItem;
import com.tbs.tobosutype.adapter.utils.ImageLoaderUtil;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/4/22 16:24.
 */

public class ImageNewActivityAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private List<_ImageItem> imageItemList;
    private int TYPE_ITEM = 0;
    private int TYPE_FOOTER = 1;
    private int footState = 1;//角标状态 默认加载更多
    private static final int LOADING_MORE = 1;//加载更多
    private static final int NOMORE = 2;//普通状态
    private int w;
    private int h;

    //单项的点击事件 接口
    public static interface OnImageNewItemClickLister {
        void onItemClick(View view);

        void onItemLongClick(View view);
    }

    //实现单项点击事件
    private OnImageNewItemClickLister onImageNewItemClickLister = null;

    public void setOnImageNewItemClickLister(OnImageNewItemClickLister lister) {
        onImageNewItemClickLister = lister;
    }

    //构造该适配器
    public ImageNewActivityAdapter(Context context, List<_ImageItem> imageItemList) {
        this.mContext = context;
        this.imageItemList = imageItemList;
        w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    }

    //为了适应返回角标图层显示
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;//返回脚标图层（加载更多图层）
        } else {
            return TYPE_ITEM;//返回普通图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_new, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_new_foot, parent, false);
            ImageFootViewHolder holder = new ImageFootViewHolder(view);
            return holder;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            //设置图片的高 宽度固定
//            ((MyViewHolder) holder).imgItem.measure(w, h);
//            int getWidth = imageItemList.get(position).getIndexImageWidth();//获取实际的宽度
//            int getHight = imageItemList.get(position).getIndexImageHeight();//获取的实际的高度
//            int imageWidth = ((MyViewHolder) holder).imgItem.getMeasuredWidth();//在布局中固定的高度
//            int imageHight = (imageWidth * getHight) / getWidth;
//            ((MyViewHolder) holder).imgItem.setMaxHeight(imageHight);
//            Log.e("在适配器ImageNewAcAdpter中", "获得当前的图片宽===" + imageWidth);
            //设置图片
            ImageLoaderUtil.loadImage(mContext, ((MyViewHolder) holder).imgItem,
                    imageItemList.get(position).getIndexImageUrl());
            //设置标题
            if (imageItemList.get(position).getTitle2().length() > 11) {
                ((MyViewHolder) holder).imgTitle.setText(imageItemList.get(position).getTitle2().substring(0, 10) + "...");
            } else {
                ((MyViewHolder) holder).imgTitle.setText(imageItemList.get(position).getTitle2());
            }

            ((MyViewHolder) holder).imgPrice.setText(imageItemList.get(position).getPlanPrice());
        } else if (holder instanceof ImageFootViewHolder) {
            ImageFootViewHolder footHolder = (ImageFootViewHolder) holder;
            if (position == 0) {
                footHolder.mtextView.setVisibility(View.GONE);
                footHolder.mProgressBar.setVisibility(View.GONE);
            }

            switch (footState) {
                case LOADING_MORE:
                    Log.e("适配器角脚标", "===" + footHolder.itemView.getId());
                    footHolder.mtextView.setVisibility(View.VISIBLE);
                    footHolder.mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case NOMORE:
                    footHolder.mtextView.setVisibility(View.GONE);
                    footHolder.mProgressBar.setVisibility(View.GONE);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return imageItemList != null ? imageItemList.size() + 1 : 0;
    }

    //点击事件的回调
    @Override
    public void onClick(View v) {
        if (onImageNewItemClickLister != null && !imageItemList.isEmpty()) {
            onImageNewItemClickLister.onItemClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void changeState(int state) {
        this.footState = state;
        notifyDataSetChanged();
    }

    //单项的布局文件  单纯的显示一张图文信息
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private TextView imgTitle;
        private TextView imgPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgItem = (ImageView) itemView.findViewById(R.id.item_image_new_img);
            imgTitle = (TextView) itemView.findViewById(R.id.item_img_title);
            imgPrice = (TextView) itemView.findViewById(R.id.item_img_price);
        }
    }

    //加载更多布局
    public static class ImageFootViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mtextView;//显示加载更多的字段

        public ImageFootViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.image_new_bar);
            mtextView = (TextView) itemView.findViewById(R.id.iamge_new_tv);
        }
    }
}
