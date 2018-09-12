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
import com.tbs.tobosutype.bean._HomePage;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.Util;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/9/5 09:42.
 * 首页文章的适配器
 */
public class HomePageArticleAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private String TAG = "HomePageArticleAdapter";
    private List<_HomePage.DataBean.ArticleBean> mArticleBeanList;

    public HomePageArticleAdapter(Context context, List<_HomePage.DataBean.ArticleBean> articleBeanList) {
        this.mContext = context;
        this.mArticleBeanList = articleBeanList;
    }

    public static interface OnArticleItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnArticleItemClickLister onArticleItemClickLister = null;

    public void setOnDecComCaseItemClickLister(OnArticleItemClickLister onArticleItemClickLister) {
        this.onArticleItemClickLister = onArticleItemClickLister;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hp_article, parent, false);
        HomePageArticleViewHolder homePageArticleViewHolder = new HomePageArticleViewHolder(view);
        return homePageArticleViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HomePageArticleViewHolder) {
            //设置时间
            ((HomePageArticleViewHolder) holder).item_hp_article_time_tv.setText("" + mArticleBeanList.get(position).getAdd_time());
            //设置文本
            if (mArticleBeanList.get(position).getTitle() != null && mArticleBeanList.get(position).getTitle().length() > 13) {
                String title = mArticleBeanList.get(position).getTitle().substring(0, 12);
                ((HomePageArticleViewHolder) holder).item_hp_article_dec_tv.setText("" + title + "...");
            } else {
                ((HomePageArticleViewHolder) holder).item_hp_article_dec_tv.setText("" + mArticleBeanList.get(position).getTitle());
            }
            //设置图片
            GlideUtils.glideLoader(mContext, mArticleBeanList.get(position).getImage_url(), ((HomePageArticleViewHolder) holder).item_hp_article_bg_img);
            //设置点击事件
            ((HomePageArticleViewHolder) holder).item_hp_article_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.HttpArticleClickCount(mArticleBeanList.get(position).getId());
                    Intent intent = new Intent(mContext, ArticleWebViewActivity.class);
                    if (mArticleBeanList.get(position).getJump_url().contains("?")) {
                        intent.putExtra("mLoadingUrl", mArticleBeanList.get(position).getJump_url() + "&app_type=4");
                    } else {
                        intent.putExtra("mLoadingUrl", mArticleBeanList.get(position).getJump_url() + "?app_type=4");
                    }
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mArticleBeanList != null ? mArticleBeanList.size() : 0;
    }

    @Override
    public void onClick(View v) {

    }

    class HomePageArticleViewHolder extends RecyclerView.ViewHolder {
        private CardView item_hp_article_cv;
        private TextView item_hp_article_time_tv;
        private TextView item_hp_article_dec_tv;
        private ImageView item_hp_article_bg_img;

        public HomePageArticleViewHolder(View itemView) {
            super(itemView);
            item_hp_article_cv = itemView.findViewById(R.id.item_hp_article_cv);
            item_hp_article_time_tv = itemView.findViewById(R.id.item_hp_article_time_tv);
            item_hp_article_dec_tv = itemView.findViewById(R.id.item_hp_article_dec_tv);
            item_hp_article_bg_img = itemView.findViewById(R.id.item_hp_article_bg_img);
        }
    }
}
