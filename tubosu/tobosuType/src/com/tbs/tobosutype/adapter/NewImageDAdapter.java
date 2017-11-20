package com.tbs.tobosutype.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._ImageD;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/11/10 14:45.
 * 套图的适配器 单个瀑布流
 * 作用于逛图库的套图显示
 * 采用接口点击事件处理
 */

public class NewImageDAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "NewImageDAdapter";
    private ArrayList<_ImageD> mImageDArrayList;

    //点击事件的接口
    public static interface OnNewImageAdapterClickLister {
        void onNewImageAdapterClick(View view, int position);
    }

    private OnNewImageAdapterClickLister onNewImageAdapterClickLister = null;

    public void setOnNewImageAdapterClickLister(OnNewImageAdapterClickLister onNewImageAdapterClickLister) {
        this.onNewImageAdapterClickLister = onNewImageAdapterClickLister;
    }


    public NewImageDAdapter(Context context, ArrayList<_ImageD> imageDArrayList) {
        this.mContext = context;
        this.mImageDArrayList = imageDArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_image_d, parent, false);
        NewImageDViewHolder newImageDViewHolder = new NewImageDViewHolder(view);
        //设置含有点击事件的控件
        newImageDViewHolder.item_new_image_shoucan_icon.setOnClickListener(this);
        newImageDViewHolder.item_new_image_shoucan_icon_ll.setOnClickListener(this);
        newImageDViewHolder.item_new_image_img_ll.setOnClickListener(this);//封面点击事件
        return newImageDViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewImageDViewHolder) {
            //设置封面图 宽度固定
            //获取屏幕的宽度
            ViewGroup.LayoutParams layoutParams = ((NewImageDViewHolder) holder).item_new_image_img.getLayoutParams();
            int spWidth = ((Activity) (((NewImageDViewHolder) holder).item_new_image_img.getContext())).getWindowManager().getDefaultDisplay().getWidth();
            layoutParams.width = spWidth / 2;
            float imgW = mImageDArrayList.get(position).getImage_width();
            float imgH = mImageDArrayList.get(position).getImage_height();
            layoutParams.height = (int) ((imgH / imgW) * (spWidth / 2));
            ((NewImageDViewHolder) holder).item_new_image_img_ll.setTag(position);
            ((NewImageDViewHolder) holder).item_new_image_img.setLayoutParams(layoutParams);
            //绑定数据
            //封面图 设置宽高加载
            Glide.with(mContext)
                    .load(mImageDArrayList.get(position).getCover_url())
                    .placeholder(R.drawable.iamge_loading).error(R.drawable.iamge_loading).centerCrop()
                    .override(spWidth / 2, (int) ((imgH / imgW) * (spWidth / 2)))
                    .dontAnimate()
                    .into(((NewImageDViewHolder) holder).item_new_image_img);
            //设置标题
            ((NewImageDViewHolder) holder).item_new_image_title.setText(mImageDArrayList.get(position).getTitle());
            //设置设计师头像
            GlideUtils.glideLoader(mContext, mImageDArrayList.get(position).getDesigner_icon(), R.drawable.new_home_loading, R.drawable.new_home_loading, ((NewImageDViewHolder) holder).item_new_image_design_icon, 0);
            //设置设计师的名字
            ((NewImageDViewHolder) holder).item_new_image_design_name.setText(mImageDArrayList.get(position).getDesigner_name());
            //设置收藏图标
            ((NewImageDViewHolder) holder).item_new_image_shoucan_icon.setTag(position);
            ((NewImageDViewHolder) holder).item_new_image_shoucan_icon_ll.setTag(position);
            if (mImageDArrayList.get(position).getIs_collect().equals("0")) {
                //未收藏
                ((NewImageDViewHolder) holder).item_new_image_shoucan_icon.setImageResource(R.drawable.shoucang_before);
                //设置未收藏的字体颜色
                ((NewImageDViewHolder) holder).item_new_image_shoucan_num.setTextColor(Color.parseColor("#adb1b3"));
            } else {
                //已收藏
                ((NewImageDViewHolder) holder).item_new_image_shoucan_icon.setImageResource(R.drawable.shoucang_after);
                ((NewImageDViewHolder) holder).item_new_image_shoucan_num.setTextColor(Color.parseColor("#ff882e"));
            }
            //设置收藏数量
            ((NewImageDViewHolder) holder).item_new_image_shoucan_num.setText(mImageDArrayList.get(position).getCollect_count());
        }
    }

    @Override
    public int getItemCount() {
        return mImageDArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (onNewImageAdapterClickLister != null) {
            onNewImageAdapterClickLister.onNewImageAdapterClick(v, (int) v.getTag());
        }
    }

    class NewImageDViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_new_image_img;//套图的封面
        private TextView item_new_image_title;//套图的标题
        private ImageView item_new_image_design_icon;//设计师的头像
        private TextView item_new_image_design_name;//设计师的名字
        private ImageView item_new_image_shoucan_icon;//收藏的图标
        private TextView item_new_image_shoucan_num;//收藏的数量
        private LinearLayout item_new_image_shoucan_icon_ll;//收藏图层
        private LinearLayout item_new_image_img_ll;//套图的图层

        public NewImageDViewHolder(View itemView) {
            super(itemView);
            item_new_image_img = (ImageView) itemView.findViewById(R.id.item_new_image_img);
            item_new_image_title = (TextView) itemView.findViewById(R.id.item_new_image_title);
            item_new_image_design_icon = (ImageView) itemView.findViewById(R.id.item_new_image_design_icon);
            item_new_image_design_name = (TextView) itemView.findViewById(R.id.item_new_image_design_name);
            item_new_image_shoucan_icon = (ImageView) itemView.findViewById(R.id.item_new_image_shoucan_icon);
            item_new_image_shoucan_num = (TextView) itemView.findViewById(R.id.item_new_image_shoucan_num);
            item_new_image_shoucan_icon_ll = (LinearLayout) itemView.findViewById(R.id.item_new_image_shoucan_icon_ll);
            item_new_image_img_ll = (LinearLayout) itemView.findViewById(R.id.item_new_image_img_ll);
        }
    }
}
