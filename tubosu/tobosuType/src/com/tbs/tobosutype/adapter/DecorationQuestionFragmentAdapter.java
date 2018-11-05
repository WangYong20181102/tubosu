package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.ArticleWebViewActivity;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.Util;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/5 13:54.
 */
public class DecorationQuestionFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> stringList;

    public DecorationQuestionFragmentAdapter(Context context, List<String> list) {
        this.context = context;
        this.stringList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hp_article, parent, false);
        DQViewHolder dqViewHolder = new DQViewHolder(view);
        return dqViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DQViewHolder) {
            //设置文本
            ((DQViewHolder) holder).item_hp_article_dec_tv.setText(stringList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class DQViewHolder extends RecyclerView.ViewHolder {
        private CardView item_hp_article_cv;
        private TextView item_hp_article_time_tv;
        private TextView item_hp_article_dec_tv;
        private ImageView item_hp_article_bg_img;

        public DQViewHolder(View itemView) {
            super(itemView);
            item_hp_article_cv = itemView.findViewById(R.id.item_hp_article_cv);
            item_hp_article_time_tv = itemView.findViewById(R.id.item_hp_article_time_tv);
            item_hp_article_dec_tv = itemView.findViewById(R.id.item_hp_article_dec_tv);
            item_hp_article_bg_img = itemView.findViewById(R.id.item_hp_article_bg_img);
        }
    }

}
