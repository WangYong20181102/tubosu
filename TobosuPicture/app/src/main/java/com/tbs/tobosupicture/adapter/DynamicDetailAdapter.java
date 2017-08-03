package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.LoginActivity;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.activity.PhotoDetail;
import com.tbs.tobosupicture.activity.ReplyActivity;
import com.tbs.tobosupicture.bean._DynamicDetail;
import com.tbs.tobosupicture.bean._ZanUser;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.tbs.tobosupicture.R.mipmap.zan;

/**
 * Created by Mr.Lin on 2017/7/18 16:39.
 * 动态详情 适配器
 */

public class DynamicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private _DynamicDetail dynamicDetail;
    private ArrayList<_DynamicDetail.Comment> commentArrayList;
    private int adapterState = 1;//子项的状态 1.加载更多 2.正常状态
    private String TAG = "DynamicDetailAdapter";
    private Activity mActivity;

    private View popView;//显示点赞列表
    private ShowZanAdapter showZanAdapter;
    private boolean isLoading = false;
    private _ZanUser zanUser;
    private RecyclerView pop_show_zan_recycle;
    private TextView pop_show_zan_num;
    private LinearLayout pop_show_zan_back;
    private LinearLayoutManager linearLayoutManager;
    private PopupWindow popupWindow;
    private int mPage = 1;//点赞列表
    private Gson gson = new Gson();
    private List<_ZanUser.PraiseUser> praiseUserList = new ArrayList<>();

    public DynamicDetailAdapter(Context context, Activity activity, _DynamicDetail dynamicDetail, ArrayList<_DynamicDetail.Comment> commentArrayList) {
        this.mContext = context;
        this.dynamicDetail = dynamicDetail;
        this.commentArrayList = commentArrayList;
        this.mActivity = activity;
        initPopWindow();
    }

    public DynamicDetailAdapter(Context context, Activity activity, ArrayList<_DynamicDetail.Comment> commentArrayList) {
        this.mContext = context;
        this.mActivity = activity;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DynamicDetailHeadHolder) {
            //设置头像
            GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getIcon(), R.mipmap.default_icon,
                    R.mipmap.default_icon, ((DynamicDetailHeadHolder) holder).DynamicDetailIcon, 0);
            //设置头像点击事件
            ((DynamicDetailHeadHolder) holder).DynamicDetailIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", dynamicDetail.getDynamic().getUid());
                    intent.putExtra("is_virtual_user", dynamicDetail.getDynamic().getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            //设置昵称
            ((DynamicDetailHeadHolder) holder).DynamicDetailNick.setText("" + dynamicDetail.getDynamic().getNick());
            //设置内容
            ((DynamicDetailHeadHolder) holder).DynamicDetailTitle.setText("" + dynamicDetail.getDynamic().getTitle());
            //设置图片
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag1.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag1);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 0);
                        mContext.startActivity(intent);
                    }
                });
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url2())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag2
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url2(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag2);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 1);
                        mContext.startActivity(intent);
                    }
                });
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url3())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag3
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url3(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag3);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 2);
                        mContext.startActivity(intent);
                    }
                });
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url4())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag4
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url4(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag4);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 3);
                        mContext.startActivity(intent);
                    }
                });
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url5())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag5
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url5(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag5);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 4);
                        mContext.startActivity(intent);
                    }
                });
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url6())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag6
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url6(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag6);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 5);
                        mContext.startActivity(intent);
                    }
                });
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url7())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag7
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url7(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag7);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 6);
                        mContext.startActivity(intent);
                    }
                });
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url8())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag8
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url8(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag8);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 7);
                        mContext.startActivity(intent);
                    }
                });
            }
            if (!TextUtils.isEmpty(dynamicDetail.getDynamic().getImage_url9())) {
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag9
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicDetail.getDynamic().getImage_url9(),
                        R.mipmap.test, R.mipmap.test, ((DynamicDetailHeadHolder) holder).DynamicDetailImag9);
                ((DynamicDetailHeadHolder) holder).DynamicDetailImag9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicDetail.getDynamic().getId());
                        intent.putExtra("mImagePosition", 8);
                        mContext.startActivity(intent);
                    }
                });
            }
            ((DynamicDetailHeadHolder) holder).DynamicDetailAddTime.setText("" + dynamicDetail.getDynamic().getAdd_time());
            //设置用户的点赞状态以及评论状态 以及点赞事件的触发
            if (dynamicDetail.getDynamic().getIs_praise().equals("0")) {
                ((DynamicDetailHeadHolder) holder).dynamic_detail_praise.setImageResource(R.mipmap.zan2);
            } else {
                ((DynamicDetailHeadHolder) holder).dynamic_detail_praise.setImageResource(R.mipmap.zan_after);
            }
            if (dynamicDetail.getDynamic().getIs_comment().equals("0")) {
                ((DynamicDetailHeadHolder) holder).dynamic_detail_comment.setImageResource(R.mipmap.pinglun);
            } else {
                ((DynamicDetailHeadHolder) holder).dynamic_detail_comment.setImageResource(R.mipmap.pinglun_after);
            }
            //点击显示popwindow
            ((DynamicDetailHeadHolder) holder).dynamic_zan_ll_pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPage = 1;
                    if (!praiseUserList.isEmpty()) {
                        praiseUserList.clear();
                    }
                    HttpGetUserZanList(mPage);
                }
            });
            //点赞人数为0时隐藏该条框
            if (dynamicDetail.getPraise_count().equals("0")) {
                ((DynamicDetailHeadHolder) holder).dynamic_zan_ll_pop.setVisibility(View.GONE);
            }
            //点赞
            ((DynamicDetailHeadHolder) holder).dynamic_detail_ll_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.userIsLogin(mContext)) {
                        HttpZan(((DynamicDetailHeadHolder) holder).dynamic_detail_praise,
                                ((DynamicDetailHeadHolder) holder).dynamic_dynamic_zan_add, ((DynamicDetailHeadHolder) holder).DynamicDetailPraiseCount);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            });

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
            //头像点击事件
            ((CommentViewHolder) holder).commentIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", dynamicDetail.getComment().get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", dynamicDetail.getComment().get(position - 1).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            //点赞事件
            ((CommentViewHolder) holder).dynamic_detail_comment_ll_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.userIsLogin(mContext)) {
                        HttpCommentZan(commentArrayList.get(position - 1).getId(),
                                commentArrayList.get(position - 1).getUid(),
                                ((CommentViewHolder) holder).commentZan,
                                ((CommentViewHolder) holder).dynamic_detail_comment_zan_add,
                                ((CommentViewHolder) holder).commentZanNum);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }

                }
            });
            //用户是否点过赞
            if (commentArrayList.get(position - 1).getIs_praise().equals("1")) {
                //点过赞
                ((CommentViewHolder) holder).commentZan.setImageResource(R.mipmap.zan_after);
            } else {
                ((CommentViewHolder) holder).commentZan.setImageResource(R.mipmap.zan2);

            }
            //昵称
            ((CommentViewHolder) holder).commentNick.setText("" + commentArrayList.get(position - 1).getNick());
            //时间
            ((CommentViewHolder) holder).commentTime.setText("" + commentArrayList.get(position - 1).getAdd_time());
            //点赞数量
            ((CommentViewHolder) holder).commentZanNum.setText("" + commentArrayList.get(position - 1).getPraise_count());
            //回复数量
            ((CommentViewHolder) holder).commentRevert.setText("" + commentArrayList.get(position - 1).getReply_count() + "回复");
            //回复的点击事件
            ((CommentViewHolder) holder).commentRevert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReplyActivity.class);
                    Log.e(TAG, "检测回复的id====" + commentArrayList.get(position - 1).getId());
                    intent.putExtra("comment_id", commentArrayList.get(position - 1).getId());
                    intent.putExtra("dynamic_id", dynamicDetail.getDynamic().getId());
                    mContext.startActivity(intent);
                }
            });
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
        return commentArrayList != null ? commentArrayList.size() + 2 : 2;
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
        private ImageView dynamic_detail_comment;//评论的图标
        private ImageView dynamic_detail_praise;//点赞的图标

        private LinearLayout dynamic_detail_ll_praise;//点赞
        private TextView dynamic_dynamic_zan_add;//点赞加一字符

        private LinearLayout dynamic_zan_ll_pop;//点赞pop
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

            dynamic_detail_ll_praise = (LinearLayout) itemView.findViewById(R.id.dynamic_detail_ll_praise);
            dynamic_detail_praise = (ImageView) itemView.findViewById(R.id.dynamic_detail_praise);
            dynamic_detail_comment = (ImageView) itemView.findViewById(R.id.dynamic_detail_comment);

            dynamic_zan_ll_pop = (LinearLayout) itemView.findViewById(R.id.dynamic_zan_ll_pop);
            dynamic_zan1 = (ImageView) itemView.findViewById(R.id.dynamic_zan1);
            dynamic_zan2 = (ImageView) itemView.findViewById(R.id.dynamic_zan2);
            dynamic_zan3 = (ImageView) itemView.findViewById(R.id.dynamic_zan3);
            dynamic_zan4 = (ImageView) itemView.findViewById(R.id.dynamic_zan4);
            dynamic_zan5 = (ImageView) itemView.findViewById(R.id.dynamic_zan5);
            dynamic_zan6 = (ImageView) itemView.findViewById(R.id.dynamic_zan6);
            dynamic_none_comment = (ImageView) itemView.findViewById(R.id.dynamic_none_comment);
            dynamic_zan_num = (TextView) itemView.findViewById(R.id.dynamic_zan_num);
            dynamic_dynamic_zan_add = (TextView) itemView.findViewById(R.id.dynamic_dynamic_zan_add);
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
        private TextView dynamic_detail_comment_zan_add;//点赞数

        private LinearLayout dynamic_detail_comment_ll_zan;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentIcon = (ImageView) itemView.findViewById(R.id.dynamic_detail_comment_icon);
            commentZan = (ImageView) itemView.findViewById(R.id.dynamic_detail_comment_zan);
            commentNick = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_nick);
            commentTitle = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_title);
            commentTime = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_time);
            commentRevert = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_revert);
            commentZanNum = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_zannum);
            dynamic_detail_comment_zan_add = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_zan_add);
            dynamic_detail_comment_ll_zan = (LinearLayout) itemView.findViewById(R.id.dynamic_detail_comment_ll_zan);

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

    //TODO 对动态进行点赞 其中uid暂时固定的值
    private void HttpZan(final ImageView zan, final TextView tvAdd, final TextView tvShowNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("dynamic_id", dynamicDetail.getDynamic().getId());
        param.put("praised_uid", dynamicDetail.getDynamic().getUid());
        HttpUtils.doPost(UrlConstans.USER_PRAISE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    Log.e(TAG, "链接成功===" + json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        final String msg = jsonObject.getString("msg");
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.equals("点赞成功")) {
                                    zan.setImageResource(R.mipmap.zan_after);
                                    zanAddAnimation(tvAdd, tvShowNum);
                                } else {
                                    int num = Integer.parseInt(tvShowNum.getText().toString());
                                    int numAddone = num - 1;
                                    tvShowNum.setText("" + numAddone);
                                    zan.setImageResource(R.mipmap.zan2);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //TODO 对用户得回复进行点赞 当前用户的id暂时固定
    private void HttpCommentZan(String comment_id, String praised_uid, final ImageView zan, final TextView tvAdd, final TextView tvShowNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("comment_id", comment_id);
        param.put("praised_uid", praised_uid);
        HttpUtils.doPost(UrlConstans.COMMENT_PRAISE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        //点赞成功
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.equals("点赞成功")) {
                                    zan.setImageResource(R.mipmap.zan_after);
                                    zanAddAnimation(tvAdd, tvShowNum);
                                } else {
                                    int num = Integer.parseInt(tvShowNum.getText().toString());
                                    int numAddone = num - 1;
                                    tvShowNum.setText("" + numAddone);
                                    zan.setImageResource(R.mipmap.zan2);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //点赞数加一的动画效果
    private void zanAddAnimation(final TextView tvAdd, final TextView showNum) {
        tvAdd.setVisibility(View.VISIBLE);
        tvAdd.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvAdd.setVisibility(View.GONE);
                int num = Integer.parseInt(showNum.getText().toString());
                int numAddone = num + 1;
                showNum.setText("" + numAddone);
            }
        }, 1000);
    }

    //显示多少人赞过
    private void showPopWindow() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        mActivity.getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 30);
    }

    private void initPopWindow() {
        mPage = 1;
        popView = mActivity.getLayoutInflater().inflate(R.layout.pop_show_zan, null);
        pop_show_zan_recycle = (RecyclerView) popView.findViewById(R.id.pop_show_zan_recycle);
        pop_show_zan_num = (TextView) popView.findViewById(R.id.pop_show_zan_num);
        pop_show_zan_back = (LinearLayout) popView.findViewById(R.id.pop_show_zan_back);

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        pop_show_zan_recycle.setLayoutManager(linearLayoutManager);
        pop_show_zan_recycle.addOnScrollListener(onScrollListener);
        int height = mActivity.getResources().getDisplayMetrics().heightPixels * 3 / 4;
        ;
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, height);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f3f2")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1.0f;
                mActivity.getWindow().setAttributes(lp);
            }
        });
        pop_show_zan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 > linearLayoutManager.getItemCount() && !isLoading) {
                LoadMoreZanUser();
            }
        }
    };

    private void LoadMoreZanUser() {
        mPage++;
        HttpGetUserZanList(mPage);
    }

    //获取点赞列表
    private void HttpGetUserZanList(final int mPage) {
        isLoading = true;
        final HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("dynamic_id", dynamicDetail.getDynamic().getId());
        param.put("page", mPage);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.DYNAMIC_PRAISE_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                isLoading = false;
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        final String data = jsonObject.getString("data");
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                zanUser = gson.fromJson(data, _ZanUser.class);
                                praiseUserList.addAll(zanUser.getPraise_user());
                                if (showZanAdapter == null) {
                                    showZanAdapter = new ShowZanAdapter(mContext, praiseUserList);
                                    pop_show_zan_recycle.setAdapter(showZanAdapter);
                                    showZanAdapter.notifyDataSetChanged();
                                } else {
                                    showZanAdapter.notifyDataSetChanged();
                                }
                                pop_show_zan_num.setText(zanUser.getCount() + "人赞过");
                                showPopWindow();
                            }
                        });
                    } else if (status.equals("201")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "当前没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    isLoading = false;
                    e.printStackTrace();
                }
            }
        });
    }
}
