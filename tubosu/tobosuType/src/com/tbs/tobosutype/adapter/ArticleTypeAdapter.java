package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._ArticleTypeItem;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/1/2 09:25.
 * 学装修 适配器
 */

public class ArticleTypeAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "ArticleTypeAdapter";
    private ArrayList<_ArticleTypeItem> articleTypeItemArrayList;

    //单项点击事件
    public static interface OnArticleTypeItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnArticleTypeItemClickLister onArticleTypeItemClickLister = null;

    public void setOnArticleTypeItemClickLister(OnArticleTypeItemClickLister onArticleTypeItemClickLister) {
        this.onArticleTypeItemClickLister = onArticleTypeItemClickLister;
    }

    public ArticleTypeAdapter(Context context, ArrayList<_ArticleTypeItem> articleTypeItemArrayList) {
        this.mContext = context;
        this.articleTypeItemArrayList = articleTypeItemArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 2;//展示假尾部  占位 防止发单按钮遮挡内容
        } else {
            return 1;//显示正常的子项
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            //尾部的占位
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_foot_viewholder_white, parent, false);
            FootViewHolders footViewHolders = new FootViewHolders(view);
            return footViewHolders;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_type_item, parent, false);
            ArticleTypeViewHolder articleTypeViewHolder = new ArticleTypeViewHolder(view);
            articleTypeViewHolder.item_article_type_item_ll.setOnClickListener(this);
            return articleTypeViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleTypeViewHolder) {
            //设置item TAG
            ((ArticleTypeViewHolder) holder).item_article_type_item_ll.setTag(position);
            //设置封面图
//            GlideUtils.glideLoader(mContext,
//                    articleTypeItemArrayList.get(position).getImage_url(),
//                    R.drawable.iamge_loading, R.drawable.iamge_loading,
//                    ((ArticleTypeViewHolder) holder).item_article_type_item_img);
            //设置封面图
            Glide.with(mContext).load(articleTypeItemArrayList.get(position).getImage_url())
                    .asBitmap()
                    .placeholder(R.drawable.iamge_loading).error(R.drawable.iamge_loading).centerCrop()
                    .into(((ArticleTypeViewHolder) holder).item_article_type_item_img);
            //设置标题
            ((ArticleTypeViewHolder) holder).item_article_type_item_title.setText("" + articleTypeItemArrayList.get(position).getTitle());
            //设置浏览量
            ((ArticleTypeViewHolder) holder).item_article_type_item_view_num.setText("" + articleTypeItemArrayList.get(position).getView_count());
            //设置时间
            ((ArticleTypeViewHolder) holder).item_article_type_item_time.setText("" + articleTypeItemArrayList.get(position).getAdd_time());
        }
    }

    @Override
    public int getItemCount() {
        return articleTypeItemArrayList != null ? articleTypeItemArrayList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onArticleTypeItemClickLister != null) {
            onArticleTypeItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    //数据ViewHolder
    class ArticleTypeViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_article_type_item_img;//封面图
        private TextView item_article_type_item_title;//标题
        private TextView item_article_type_item_view_num;//浏览量
        private TextView item_article_type_item_time;//浏览量
        private LinearLayout item_article_type_item_ll;//整个图层

        public ArticleTypeViewHolder(View itemView) {
            super(itemView);
            item_article_type_item_img = itemView.findViewById(R.id.item_article_type_item_img);
            item_article_type_item_title = itemView.findViewById(R.id.item_article_type_item_title);
            item_article_type_item_view_num = itemView.findViewById(R.id.item_article_type_item_view_num);
            item_article_type_item_time = itemView.findViewById(R.id.item_article_type_item_time);
            item_article_type_item_ll = itemView.findViewById(R.id.item_article_type_item_ll);
        }
    }

    //假尾巴
    class FootViewHolders extends RecyclerView.ViewHolder {
        private View item_foot_white;

        public FootViewHolders(View itemView) {
            super(itemView);
            item_foot_white = (View) itemView.findViewById(R.id.item_foot_white);
        }
    }
}
