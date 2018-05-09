package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lie on 2017/9/22.
 */

public class FavImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "FavImageAdapter";
    private List<HashMap<String, Object>> list;
    private Context context;
    private LayoutInflater inflater;
    private View view;
    private View footView;

    private int NORMAL_VIEW_TYPE = 1;
    private int FOOT_VIEW_TYPE = 2;

    public FavImageAdapter(Context context, List<HashMap<String, Object>> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType ==  NORMAL_VIEW_TYPE){
            view =inflater.inflate(R.layout.item_gridview_imagestore, null);
            return new ImageViewHolder(view);
        }

        if(viewType == FOOT_VIEW_TYPE){
            footView = inflater.inflate(R.layout.layout_recylerview_loading_more, null);
            return new FootViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof  ImageViewHolder){
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            Glide.with(context).load(list.get(position).get("img_url").toString()).into(imageViewHolder.deleteImage);
            imageViewHolder.deleteBox.setOnCheckedChangeListener(null);
//            if(showF==false){
//                // 隐藏勾选
//                imageViewHolder.deleteBox.setVisibility(View.GONE);
//            }else{
//                // 显示勾选
//
//                if(isEditDel.get(position)==false){ //false
//                    imageViewHolder.deleteBox.setChecked(false);
//                }
//
//                imageViewHolder.deleteBox.setVisibility(View.VISIBLE);
//                imageViewHolder.deleteBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        Boolean flag = !isEditDel.get(position); // 将false改为true 显示勾选中
//                        if (flag) {
//                            delNum++;
//                        } else {
//                            delNum--;
//                        }
//                        imageViewHolder.isEditDel.remove(position);
//                        imageViewHolder.isEditDel.add(position, flag);
//                        sendDeleteNumBroadcast();
//                    }
//
//
//                });
//            }
        }

        if (holder instanceof FootViewHolder){
            FootViewHolder footViewHolder = (FootViewHolder) holder;

        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView deleteImage;
        CheckBox deleteBox;
        public ImageViewHolder(View itemView) {
            super(itemView);
            deleteImage = (ImageView) itemView.findViewById(R.id.item_gridview_image);
            deleteBox = (CheckBox) itemView.findViewById(R.id.item_gridview_edit);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        TextView loadMore;

        public FootViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_more);
            loadMore = (TextView) itemView.findViewById(R.id.tv_loading_more_text);
        }

    }
}
