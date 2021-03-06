package com.tobosu.mydecorate.adapter;

import android.content.Context;
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
import com.tobosu.mydecorate.entity.BibleData;
import com.tobosu.mydecorate.entity.BibleEntity;
import com.tobosu.mydecorate.entity.HomeFragmentData;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.Util;

import java.util.ArrayList;


/**
 * Created by dec on 2016/10/19.
 */

public class TempChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;

    private static final int TYPE_ITEM6 = 6;
    private static final int TYPE_ITEM7 = 7;
    private static final int TYPE_ITEM8 = 8;

    private static final int TYPE_FOOTER = 2;

    private LayoutInflater inflater = null;

    private ArrayList<BibleEntity> data = new ArrayList<BibleEntity>();

    private String type;

    public TempChildAdapter(Context context, ArrayList<BibleEntity> data) {
        this.mContext = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }


    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private View itemView6;
    private View itemView7;
    private View itemView8;

    private View footView;
    private LinearLayout footLayout;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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
        if(position<data.size()){
            type = data.get(position).getStyle();
            if ("1".equals(type)) {
                return TYPE_ITEM6;
            } else if ("2".equals(type)) {
                return TYPE_ITEM7;
            } else if("3".equals(type)){
                return TYPE_ITEM8;
            }else {
                return TYPE_FOOTER;
            }
        }else {
            return TYPE_FOOTER;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position<data.size()){
            BibleEntity item = data.get(position);

            if (holder instanceof Item6Holder && "1".equals(type)) {
                Item6Holder newHolder = (Item6Holder) holder;

                newHolder.tv_item6_title.setText(item.getTitle());
                newHolder.tv_item6_name.setText(item.getAuthor_name());
                newHolder.tv_item6_time.setText(item.getTime());
                newHolder.tv_item6_browse_num.setText(item.getView_count());
                newHolder.tv_item6_fav_num.setText(item.getCollect_count());
                newHolder.tv_item6_good_num.setText(item.getTup_count());

                if (item.getImg_url()!=null && item.getImg_url().size()>0 && item.getImg_url().get(0) != null) {
                    GlideUtils.glideLoader(mContext,item.getImg_url().get(0),R.mipmap.occupied1,R.mipmap.occupied1,newHolder.iv_item6_image,1);
                }

                newHolder.itemView.setTag(data.get(position));

            } else if (holder instanceof Item7Holder && "2".equals(type)) {
                Item7Holder newHolder = (Item7Holder) holder;

                newHolder.tv_item7_title.setText(item.getTitle());
                newHolder.tv_item7_name.setText(item.getAuthor_name());
                newHolder.tv_item7_time.setText(item.getTime());
                newHolder.tv_item7_browse_num.setText(item.getView_count());
                newHolder.tv_item7_fav_num.setText(item.getCollect_count());
                newHolder.tv_item7_good_num.setText(item.getTup_count());


                if(item.getImg_url()!=null && item.getImg_url().size()>0){
                    if (item.getImg_url().get(0) != null) {
                        GlideUtils.glideLoader(mContext,item.getImg_url().get(0),R.mipmap.occupied2,R.mipmap.occupied2,newHolder.iv_item7_image_left,1);
                    }
                    if (item.getImg_url().get(1) != null) {
                        GlideUtils.glideLoader(mContext,item.getImg_url().get(1),R.mipmap.occupied2,R.mipmap.occupied2,newHolder.iv_item7_image_mid,1);
                    }
                    if (item.getImg_url().get(2) != null) {
                        GlideUtils.glideLoader(mContext,item.getImg_url().get(2),R.mipmap.occupied2,R.mipmap.occupied2,newHolder.iv_item7_image_right,1);
                    }
                }


                newHolder.itemView.setTag(data.get(position));
            } else if (holder instanceof Item8Holder && "3".equals(type)) {
                Item8Holder newHolder = (Item8Holder) holder;

                newHolder.tv_item8_title.setText(item.getTitle());
                newHolder.tv_item8_name.setText(item.getAuthor_name());
                newHolder.tv_item8_time.setText(item.getTime());
                newHolder.tv_item8_browse_num.setText(item.getView_count());
                newHolder.tv_item8_fav_num.setText(item.getCollect_count());
                newHolder.tv_item8_good_num.setText(item.getTup_count());

                if(item.getImg_url()!=null && item.getImg_url().size()>0 && item.getImg_url().get(0)!=null){
                    GlideUtils.glideLoader(mContext,item.getImg_url().get(0),R.mipmap.occupied4,R.mipmap.occupied4,newHolder.iv_item8_image_large,1);
                }
                newHolder.itemView.setTag(data.get(position));
            } else if (holder instanceof FootViewHolder) {
//            FootViewHolder newHolder = (FootViewHolder) holder;
            }
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

    public void showFootView() {
        if (footView != null) {
            footView.setVisibility(View.VISIBLE);
        }
        if (footLayout != null) {
            footLayout.setVisibility(View.VISIBLE);
        }
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        return data==null?0:data.size()+1;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onRecyclerViewItemClick(view, (BibleEntity) view.getTag());
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


    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, BibleEntity data);
    }
}