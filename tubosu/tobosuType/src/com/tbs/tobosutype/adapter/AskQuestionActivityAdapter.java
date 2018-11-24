package com.tbs.tobosutype.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.AskQuestionActivity;

import java.util.ArrayList;

/**
 * Created by Mr.Wang on 2018/11/12 10:57.
 */
public class AskQuestionActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private AskQuestionActivity context;
    private ArrayList<String> stringList;
    private final LayoutInflater inflater;

    public AskQuestionActivityAdapter(AskQuestionActivity context, ArrayList<String> stringList) {
        this.context = context;
        this.stringList = stringList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(inflater.inflate(R.layout.release_message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder) {
           Glide.with(context)
                .load(stringList.get(position))
                .centerCrop()
                .into(((MyHolder) holder).imageview);
           ((MyHolder) holder).imageDelete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   removeData(position);
               }
           });
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size() <= 3 ? stringList.size() : 3;
    }

    //对外暴露方法  。点击添加图片（类似于上啦加载数据）
    public void addMoreItem(ArrayList<String> loarMoreDatas) {
        if (!stringList.isEmpty() || stringList.size() != 0){
            stringList.clear();
        }
        stringList.addAll(loarMoreDatas);
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ImageView imageview;
        private ImageView imageDelete;

        public MyHolder(View itemView) {
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.image_photo_item);
            imageDelete = (ImageView) itemView.findViewById(R.id.image_delete);
        }
    }

    /**
     * 图片删除
     * @param position
     */
    public void removeData(int position) {
        if (stringList.size() != 0 || !stringList.isEmpty()){
            stringList.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
            context.deleteResult(stringList,position);
        }
    }

}
