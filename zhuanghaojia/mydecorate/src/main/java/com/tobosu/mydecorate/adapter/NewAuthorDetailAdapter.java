package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.entity._AuthorDetail;
import com.tobosu.mydecorate.util.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/15 13:57.
 */

public class NewAuthorDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;

    private LayoutInflater inflater = null;
    private List<_AuthorDetail.Article> articleList;
    private int adapterState = 1;//适配器的状态   1--默认状态  2--加载更多

    public NewAuthorDetailAdapter(Context mContext, List<_AuthorDetail.Article> articleList) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.articleList = articleList;
    }

    //图层的变换
    public void changeAdapterState(int state) {
        this.adapterState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {
            return 2;//加载更多
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_user_acticle_adapter_layout, parent, false);
            ActicleHolder holder = new ActicleHolder(view);
            view.setOnClickListener(this);
            return holder;
        } else if (viewType == 2) {
            //加载更多
            View view = inflater.inflate(R.layout.item_loadmore, parent, false);
            FootViewHolder holder = new FootViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ActicleHolder) {
            ((ActicleHolder) holder).tv_time.setText("" + articleList.get(position).getTime());
            ((ActicleHolder) holder).tv_time_text.setText("" + articleList.get(position).getTime2());
            ((ActicleHolder) holder).tv_title.setText("" + articleList.get(position).getTitle());
            ((ActicleHolder) holder)._tv_type.setText("" + articleList.get(position).getType_name());
            ((ActicleHolder) holder).tv_see_num.setText("" + articleList.get(position).getView_count() + "人看过");
            GlideUtils.glideLoader(mContext, articleList.get(position).getImage_url(), 0, R.mipmap.jiazai_loading, ((ActicleHolder) holder).iv_image);
        } else if (holder instanceof FootViewHolder) {
            //显示加载更多的角标
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            if (position == 1) {
                footViewHolder.mProgressBar.setVisibility(View.GONE);
                footViewHolder.mtextView.setVisibility(View.GONE);
            }
            switch (adapterState) {
                case 2://显示角标
                    footViewHolder.mtextView.setVisibility(View.VISIBLE);
                    footViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case 1://显示普通图层
                    footViewHolder.mtextView.setVisibility(View.GONE);
                    footViewHolder.mProgressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return articleList == null ? 0 : articleList.size() + 1;
    }

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (onRecyclerViewItemClickListener != null) {
            onRecyclerViewItemClickListener.onRecyclerViewItemClick(view);
        }
    }

    static class ActicleHolder extends RecyclerView.ViewHolder {

        TextView tv_time;
        TextView tv_time_text;
        TextView tv_title;
        TextView tv_see_num;
        TextView _tv_type;
        ImageView iv_image;

        public ActicleHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_time_text = (TextView) itemView.findViewById(R.id.tv_time_text);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_see_num = (TextView) itemView.findViewById(R.id.tv_see_num);
            _tv_type = (TextView) itemView.findViewById(R.id._tv_type);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    //加载更多角标
    class FootViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mtextView;//显示加载更多的字段

        public FootViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.image_new_bar);
            mtextView = (TextView) itemView.findViewById(R.id.iamge_new_tv);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view);
    }
}
