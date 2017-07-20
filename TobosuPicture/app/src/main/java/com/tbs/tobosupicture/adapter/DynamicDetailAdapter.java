package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean._DynamicDetail;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/7/18 16:39.
 */

public class DynamicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private _DynamicDetail dynamicDetail;
    private ArrayList<_DynamicDetail.Comment> commentArrayList;
    private int adapterState = 1;//子项的状态 1.加载更多 2.正常状态

    public DynamicDetailAdapter(Context context, _DynamicDetail dynamicDetail, ArrayList<_DynamicDetail.Comment> commentArrayList) {
        this.mContext = context;
        this.dynamicDetail = dynamicDetail;
        this.commentArrayList = commentArrayList;
    }

    public DynamicDetailAdapter(Context context, ArrayList<_DynamicDetail.Comment> commentArrayList) {
        this.mContext = context;
        this.commentArrayList = commentArrayList;
    }

    public void changeAdapterState(int state) {
        this.adapterState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;//返回头部
        } else if (position + 1 == getItemCount()) {
            return 1;//加载更多
        } else {
            return 2;//普通
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            //头部
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_detail_head, parent, false);
            DynamicDetailHeadHolder headHolder = new DynamicDetailHeadHolder(view);
            return headHolder;
        } else if (viewType == 1) {
            //加载更多
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            CommentViewHolder commentViewHolder = new CommentViewHolder(view);
            return commentViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DynamicDetailHeadHolder) {
            //设置头像
            GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getIcon(), R.mipmap.default_icon,
                    R.mipmap.default_icon, ((DynamicDetailHeadHolder) holder).DynamicDetailIcon, 0);
            //设置昵称
            ((DynamicDetailHeadHolder) holder).DynamicDetailNick.setText("" + dynamicDetail.getDynamic().getNick());
            //设置内容
            ((DynamicDetailHeadHolder) holder).DynamicDetailTitle.setText("" + dynamicDetail.getDynamic().getTitle());
            //设置图片
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag1.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag1);
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url2())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag2
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url2(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag2);
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url3())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag3
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url3(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag3);
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url4())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag4
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url4(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag4);
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url5())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag5
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url5(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag5);
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url6())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag6
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url6(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag6);
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url7())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag7
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url7(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag7);
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url8())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag8
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url8(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag8);
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url9())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag9
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url9(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag9);
            }
            ((DynamicDetailHeadHolder) holder).DynamicDetailAddTime.setText("" + dynamicDetail.getDynamic().getAdd_time());
            ((DynamicDetailHeadHolder) holder).DynamicDetailPraiseCount.setText("" + dynamicDetail.getPraise_count());
            ((DynamicDetailHeadHolder) holder).DynamicDetailCommentCount.setText("" + dynamicDetail.getDynamic().getComment_count());
            //点赞用户的头像

            if (dynamicDetail.getPraise_user().size() >= 1 && dynamicDetail.getPraise_user().get(0) != null) {
                ((DynamicDetailHeadHolder) holder).dynamic_zan1
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getPraise_user().get(0)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((DynamicDetailHeadHolder) holder).dynamic_zan1,
                        0);
            }
            if (dynamicDetail.getPraise_user().size() >= 2 && dynamicDetail.getPraise_user().get(1) != null) {
                ((DynamicDetailHeadHolder) holder).dynamic_zan2
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getPraise_user().get(1)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((DynamicDetailHeadHolder) holder).dynamic_zan2,
                        0);
            }
            if (dynamicDetail.getPraise_user().size() >= 3 && dynamicDetail.getPraise_user().get(2) != null) {
                ((DynamicDetailHeadHolder) holder).dynamic_zan3
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getPraise_user().get(2)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((DynamicDetailHeadHolder) holder).dynamic_zan3,
                        0);
            }
            if (dynamicDetail.getPraise_user().size() >= 4 && dynamicDetail.getPraise_user().get(3) != null) {
                ((DynamicDetailHeadHolder) holder).dynamic_zan4
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getPraise_user().get(3)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((DynamicDetailHeadHolder) holder).dynamic_zan4,
                        0);
            }
            if (dynamicDetail.getPraise_user().size() >= 5 && dynamicDetail.getPraise_user().get(4) != null) {
                ((DynamicDetailHeadHolder) holder).dynamic_zan5
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getPraise_user().get(4)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((DynamicDetailHeadHolder) holder).dynamic_zan5,
                        0);
            }
            if (dynamicDetail.getPraise_user().size() >= 6 && dynamicDetail.getPraise_user().get(5) != null) {
                ((DynamicDetailHeadHolder) holder).dynamic_zan6
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getPraise_user().get(5)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((DynamicDetailHeadHolder) holder).dynamic_zan6,
                        0);
            }
            ((DynamicDetailHeadHolder) holder).dynamic_zan_num.setText("" + dynamicDetail.getPraise_count() + "人赞过");
            if (commentArrayList.isEmpty()) {
                ((DynamicDetailHeadHolder) holder).dynamic_none_comment.setVisibility(View.VISIBLE);
            } else {
                ((DynamicDetailHeadHolder) holder).dynamic_none_comment.setVisibility(View.GONE);
            }
        } else if (holder instanceof CommentViewHolder) {
            //头像
            GlideUtils.glideLoader(mContext, commentArrayList.get(position - 1).getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((CommentViewHolder) holder).commentIcon, 0);
            //昵称
            ((CommentViewHolder) holder).commentNick.setText("" + commentArrayList.get(position - 1).getNick());
            //时间
            ((CommentViewHolder) holder).commentTime.setText("" + commentArrayList.get(position - 1).getAdd_time());
            //点赞数量
            ((CommentViewHolder) holder).commentZanNum.setText("" + commentArrayList.get(position - 1).getPraise_count());
            //回复数量
            ((CommentViewHolder) holder).commentRevert.setText("" + commentArrayList.get(position - 1).getReply_count() + "回复");
            //回复的内容
            ((CommentViewHolder) holder).commentTitle.setText("" + commentArrayList.get(position - 1).getContent());
        } else if (holder instanceof FootViewHolder) {
            if (position == 1) {
                ((FootViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((FootViewHolder) holder).mTextView.setVisibility(View.GONE);
                return;
            }
            if (adapterState == 2) {
                ((FootViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((FootViewHolder) holder).mTextView.setVisibility(View.GONE);
            } else if (adapterState == 1) {
                ((FootViewHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((FootViewHolder) holder).mTextView.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return commentArrayList != null ? commentArrayList.size() + 2 : 0;
    }

    class DynamicDetailHeadHolder extends RecyclerView.ViewHolder {
        private ImageView DynamicDetailIcon;//用户的头像
        private TextView DynamicDetailNick;//用户的昵称
        private TextView DynamicDetailTitle;//用户发布的内容
        private ImageView DynamicDetailImag1;//动态图一
        private ImageView DynamicDetailImag2;//
        private ImageView DynamicDetailImag3;//
        private ImageView DynamicDetailImag4;//
        private ImageView DynamicDetailImag5;//
        private ImageView DynamicDetailImag6;//
        private ImageView DynamicDetailImag7;//
        private ImageView DynamicDetailImag8;//
        private ImageView DynamicDetailImag9;//
        private TextView DynamicDetailAddTime;//时间
        private TextView DynamicDetailCommentCount;//动态评论数
        private TextView DynamicDetailPraiseCount;//动态点赞数

        private ImageView dynamic_zan1;
        private ImageView dynamic_zan2;
        private ImageView dynamic_zan3;
        private ImageView dynamic_zan4;
        private ImageView dynamic_zan5;
        private ImageView dynamic_zan6;
        private TextView dynamic_zan_num;

        private ImageView dynamic_none_comment;

        public DynamicDetailHeadHolder(View itemView) {
            super(itemView);
            DynamicDetailIcon = (ImageView) itemView.findViewById(R.id.dynamic_detail_icon);
            DynamicDetailNick = (TextView) itemView.findViewById(R.id.dynamic_detail_nick);
            DynamicDetailTitle = (TextView) itemView.findViewById(R.id.dynamic_detail_title);
            DynamicDetailImag1 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img1);
            DynamicDetailImag2 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img2);
            DynamicDetailImag3 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img3);
            DynamicDetailImag4 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img4);
            DynamicDetailImag5 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img5);
            DynamicDetailImag6 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img6);
            DynamicDetailImag7 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img7);
            DynamicDetailImag8 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img8);
            DynamicDetailImag9 = (ImageView) itemView.findViewById(R.id.dynamic_detail_img9);
            DynamicDetailAddTime = (TextView) itemView.findViewById(R.id.dynamic_detail_add_time);
            DynamicDetailCommentCount = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_count);
            DynamicDetailPraiseCount = (TextView) itemView.findViewById(R.id.dynamic_detail_praise_count);

            dynamic_zan1 = (ImageView) itemView.findViewById(R.id.dynamic_zan1);
            dynamic_zan2 = (ImageView) itemView.findViewById(R.id.dynamic_zan2);
            dynamic_zan3 = (ImageView) itemView.findViewById(R.id.dynamic_zan3);
            dynamic_zan4 = (ImageView) itemView.findViewById(R.id.dynamic_zan4);
            dynamic_zan5 = (ImageView) itemView.findViewById(R.id.dynamic_zan5);
            dynamic_zan6 = (ImageView) itemView.findViewById(R.id.dynamic_zan6);
            dynamic_none_comment = (ImageView) itemView.findViewById(R.id.dynamic_none_comment);
            dynamic_zan_num = (TextView) itemView.findViewById(R.id.dynamic_zan_num);
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView commentIcon;//头像
        private TextView commentNick;//昵称
        private TextView commentTitle;//内容
        private TextView commentTime;//回复时间
        private TextView commentRevert;//回复按钮
        private ImageView commentZan;//点赞图标
        private TextView commentZanNum;//点赞数

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentIcon = (ImageView) itemView.findViewById(R.id.dynamic_detail_comment_icon);
            commentZan = (ImageView) itemView.findViewById(R.id.dynamic_detail_comment_zan);
            commentNick = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_nick);
            commentTitle = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_title);
            commentTime = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_time);
            commentRevert = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_revert);
            commentZanNum = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_zannum);

        }
    }

    //加载更多角标
    class FootViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段


        public FootViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);

        }
    }
}
