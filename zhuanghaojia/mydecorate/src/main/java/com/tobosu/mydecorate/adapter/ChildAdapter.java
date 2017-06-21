package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.ArticleDetailActivity;
import com.tobosu.mydecorate.activity.MainActivity;
import com.tobosu.mydecorate.activity.WebViewActivity;
import com.tobosu.mydecorate.entity.BibleData;
import com.tobosu.mydecorate.entity.HomeFragmentData;
import com.tobosu.mydecorate.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by dec on 2016/10/19.
 */

public class ChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM6 = 6;
    private static final int TYPE_ITEM7 = 7;
    private static final int TYPE_ITEM8 = 8;

    private static final int TYPE_FOOTER = 2;

    private LayoutInflater inflater = null;

    private ArrayList<BibleData.DataBean> data = new ArrayList<BibleData.DataBean>();

    private int type;

    public ChildAdapter(Context context, ArrayList<BibleData.DataBean> data) {
        this.mContext = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }


    public ArrayList<BibleData.DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<BibleData.DataBean> data) {
        this.data = data;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private View itemView6;
    private View itemView7;
    private View itemView8;

    private View footView;
    private View headView;
    private LinearLayout footLayout;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        //头部返回 ViewPager 实现的轮播图片
//        if (viewType == TYPE_HEADER && child_position ==0) {
//            headView = inflater.inflate(R.layout.header_layout_childfragment, parent, false);
//            HeaderViewHolder headerViewHolder = new HeaderViewHolder(headView);
//            return headerViewHolder;
//        }


        if (viewType == TYPE_ITEM6) {
            itemView6 = inflater.inflate(R.layout.item_item6_adapter_layout, parent, false);
            Item6Holder holder = new Item6Holder(itemView6);
            itemView6.setOnClickListener(this);
            return holder;
        }

        if (viewType == TYPE_ITEM7) {
            itemView7 = inflater.inflate(R.layout.item_item7_adapter_layout, parent, false);
            Item7Holder holder = new Item7Holder(itemView7);
            itemView7.setOnClickListener(this);
            return holder;
        }

        if (viewType == TYPE_ITEM8) {
            itemView8 = inflater.inflate(R.layout.item_item8_adapter_layout, parent, false);
            Item8Holder holder = new Item8Holder(itemView8);
            itemView8.setOnClickListener(this);
            return holder;
        }

        if (viewType == TYPE_FOOTER) {
            footView = inflater.inflate(R.layout.item_foot, parent, false);
            footLayout = (LinearLayout) footView.findViewById(R.id.layout_foot);
            return new FootViewHolder(footView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            type = data.get(position).getStyle();
            if (type == 1) {
                return TYPE_ITEM6;
            } else if (type == 2) {
                return TYPE_ITEM7;
            } else{
                return TYPE_ITEM8;
            }
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BibleData.DataBean item = data.get(position);

        if (holder instanceof Item6Holder && type == 1) {
            Item6Holder newHolder = (Item6Holder) holder;

            newHolder.tv_item6_title.setText(item.getTitle());
            newHolder.tv_item6_name.setText(item.getAuthor_name());
            newHolder.tv_item6_time.setText(item.getTime());
            newHolder.tv_item6_browse_num.setText(item.getView_count());
            newHolder.tv_item6_fav_num.setText(item.getCollect_count());
            newHolder.tv_item6_good_num.setText(item.getTup_count());

            if (item.getImg_url()!=null && item.getImg_url().get(0) != null) {
                Picasso.with(mContext)
                        .load(item.getImg_url().get(0))
                        .fit()
                        .placeholder(R.mipmap.occupied1)
                        .error(R.mipmap.occupied1)
                        .into(newHolder.iv_item6_image);
            }

            newHolder.itemView.setTag(data.get(position));

        } else if (holder instanceof Item7Holder && type == 2) {
            Item7Holder newHolder = (Item7Holder) holder;

            newHolder.tv_item7_title.setText(item.getTitle());
            newHolder.tv_item7_name.setText(item.getAuthor_name());
            newHolder.tv_item7_time.setText(item.getTime());
            newHolder.tv_item7_browse_num.setText(item.getView_count());
            newHolder.tv_item7_fav_num.setText(item.getCollect_count());
            newHolder.tv_item7_good_num.setText(item.getTup_count());

            if (item.getImg_url()!=null && item.getImg_url().get(0) != null) {
                Picasso.with(mContext)
                        .load(item.getImg_url().get(0))
                        .fit()
                        .placeholder(R.mipmap.occupied1)
                        .error(R.mipmap.occupied1)
                        .into(newHolder.iv_item7_image_left);
            } else if (item.getImg_url()!=null && item.getImg_url().get(1) != null) {
                Picasso.with(mContext)
                        .load(item.getImg_url().get(1))
                        .fit()
                        .placeholder(R.mipmap.occupied1)
                        .error(R.mipmap.occupied1)
                        .into(newHolder.iv_item7_image_mid);
            } else if (item.getImg_url()!=null && item.getImg_url().get(2) != null) {
                Picasso.with(mContext)
                        .load(item.getImg_url().get(2))
                        .fit()
                        .placeholder(R.mipmap.occupied1)
                        .error(R.mipmap.occupied1)
                        .into(newHolder.iv_item7_image_right);
            }


            newHolder.itemView.setTag(data.get(position));
        } else if (holder instanceof Item8Holder && type == 3) {
            Item8Holder newHolder = (Item8Holder) holder;

            newHolder.tv_item8_title.setText(item.getTitle());
            newHolder.tv_item8_name.setText(item.getAuthor_name());
            newHolder.tv_item8_time.setText(item.getTime());
            newHolder.tv_item8_browse_num.setText(item.getView_count());
            newHolder.tv_item8_fav_num.setText(item.getCollect_count());
            newHolder.tv_item8_good_num.setText(item.getTup_count());

            if(item.getImg_url()!=null && item.getImg_url().get(0)!=null){
                Picasso.with(mContext)
                        .load(item.getImg_url().get(0))
                        .fit()
                        .placeholder(R.mipmap.occupied1)
                        .error(R.mipmap.occupied1)
                        .into(newHolder.iv_item8_image_large);
            }
            newHolder.itemView.setTag(data.get(position));
        } else if (holder instanceof FootViewHolder) {

//            HeaderViewHolder newHolder = (HeaderViewHolder) holder;
//            addImageView(mContext, ad_viewpagerDataList);
//            MyImageAdAdapter imageAdapter = new MyImageAdAdapter(ad_viewpagerDataList);
//
//            setUpViewPager(newHolder.homeRecommendPager, newHolder.dotLayout, imageAdapter, ad_viewpagerDataList);
        }
    }


    public void hideFootView() {
        if (footView != null) {
            footView.setVisibility(View.GONE);
        }
        if (footLayout != null) {
            footLayout.setVisibility(View.GONE);
        }
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onRecyclerViewItemClick(view, (HomeFragmentData) view.getTag());
        }
    }


    class Item6Holder extends RecyclerView.ViewHolder {
        TextView tv_item6_title;
        TextView tv_item6_name;
        TextView tv_item6_time;
        TextView tv_item6_browse_num;
        TextView tv_item6_fav_num;
        TextView tv_item6_good_num;
        ImageView iv_item6_image;

        public Item6Holder(View itemView) {
            super(itemView);
            tv_item6_title = (TextView) itemView.findViewById(R.id.tv_item6_title);
            tv_item6_name = (TextView) itemView.findViewById(R.id.tv_item6_name);
            tv_item6_time = (TextView) itemView.findViewById(R.id.tv_item6_time);
            tv_item6_browse_num = (TextView) itemView.findViewById(R.id.tv_item6_browse_num);
            tv_item6_fav_num = (TextView) itemView.findViewById(R.id.tv_item6_fav_num);
            tv_item6_good_num = (TextView) itemView.findViewById(R.id.tv_item6_good_num);
            iv_item6_image = (ImageView) itemView.findViewById(R.id.iv_item6_image);
        }
    }

    class Item7Holder extends RecyclerView.ViewHolder {
        TextView tv_item7_title;
        TextView tv_item7_name;
        TextView tv_item7_time;
        TextView tv_item7_browse_num;
        TextView tv_item7_fav_num;
        TextView tv_item7_good_num;
        ImageView iv_item7_image_left;
        ImageView iv_item7_image_mid;
        ImageView iv_item7_image_right;

        public Item7Holder(View itemView) {
            super(itemView);
            tv_item7_title = (TextView) itemView.findViewById(R.id.tv_item7_title);
            tv_item7_name = (TextView) itemView.findViewById(R.id.tv_item7_name);
            tv_item7_time = (TextView) itemView.findViewById(R.id.tv_item7_time);
            tv_item7_browse_num = (TextView) itemView.findViewById(R.id.tv_item7_browse_num);
            tv_item7_fav_num = (TextView) itemView.findViewById(R.id.tv_item7_fav_num);
            tv_item7_good_num = (TextView) itemView.findViewById(R.id.tv_item7_good_num);
            iv_item7_image_left = (ImageView) itemView.findViewById(R.id.iv_item7_image_left);
            iv_item7_image_mid = (ImageView) itemView.findViewById(R.id.iv_item7_image_mid);
            iv_item7_image_right = (ImageView) itemView.findViewById(R.id.iv_item7_image_right);
        }
    }

    class Item8Holder extends RecyclerView.ViewHolder {

        TextView tv_item8_title;
        TextView tv_item8_name;
        TextView tv_item8_time;
        TextView tv_item8_browse_num;
        TextView tv_item8_fav_num;
        TextView tv_item8_good_num;
        ImageView iv_item8_image_large;

        public Item8Holder(View itemView) {
            super(itemView);
            tv_item8_title = (TextView) itemView.findViewById(R.id.tv_item8_title);
            tv_item8_name = (TextView) itemView.findViewById(R.id.tv_item8_name);
            tv_item8_time = (TextView) itemView.findViewById(R.id.tv_item8_time);
            tv_item8_browse_num = (TextView) itemView.findViewById(R.id.tv_item8_browse_num);
            tv_item8_fav_num = (TextView) itemView.findViewById(R.id.tv_item8_fav_num);
            tv_item8_good_num = (TextView) itemView.findViewById(R.id.tv_item8_good_num);
            iv_item8_image_large = (ImageView) itemView.findViewById(R.id.iv_item8_image_large);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        LinearLayout dotLayout;
        ViewPager homeRecommendPager;
        RelativeLayout rel_home_ad_vp;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            rel_home_ad_vp = (RelativeLayout) itemView.findViewById(R.id.rel_home_ad_vp);
            dotLayout = (LinearLayout) itemView.findViewById(R.id.dotLayout);
            homeRecommendPager = (ViewPager) itemView.findViewById(R.id.homeRecommendPager);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, HomeFragmentData data);
    }


    //***************************************************************************************************


//    private void startActivity(String _articleId, String _typeId, String _articleTitle, String _articleTitlePicUrl, String _writerUserId, String _contentUrl){
//        if(!"".equals(_contentUrl)){
//            Intent it = new Intent(mContext, WebViewActivity.class);
//            Bundle b = new Bundle();
//            System.out.println("----------1------------------收到-->>"+_contentUrl);
//            b.putString("link", _contentUrl);
//            it.putExtra("WebView_Bundle",b);
//            mContext.startActivity(it);
//        }else {
//            System.out.println("----------2------------------收到-->>"+_contentUrl);
//            Intent it = new Intent(mContext, ArticleDetailActivity.class);
//            Bundle b = new Bundle();
//            b.putString("article_id", _articleId);
//            b.putString("type_id", _typeId);
//            b.putString("article_title", _articleTitle);
//            b.putString("article_title_pic_url", _articleTitlePicUrl);
//            b.putString("writer_id", _writerUserId); // 被查看的用戶id
//            b.putInt("child_position", 0);
//            if(Util.isLogin(mContext)){
//                b.putString("uid", Util.getUserId(mContext));
//            }else {
//                b.putString("uid", "0"); // uid-->>0 是游客模式
//            }
//            it.putExtra("Article_Detail_Bundle",b);
//            mContext.startActivity(it);
//        }
//    }
}