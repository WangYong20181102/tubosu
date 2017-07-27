package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.activity.ReplyActivity;
import com.tbs.tobosupicture.bean._Reply;
import com.tbs.tobosupicture.bean._ZanUser;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
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

/**
 * Created by Mr.Lin on 2017/7/25 16:17.
 * 查看回复页面的适配器
 */

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "ReplyAdapter";
    private Context mContext;
    private List<_Reply.Comment> commentList;
    private _Reply mReply;
    private Activity mActivity;
    private int adapterState = 1;
    private String dynamic_id;//动态id

    //pop
    private View popView;
    private ShowZanAdapter showZanAdapter;
    private boolean isLoading = false;
    private _ZanUser zanUser;
    private RecyclerView pop_show_zan_recycle;
    private TextView pop_show_zan_num;
    private LinearLayout pop_show_zan_back;
    private LinearLayoutManager linearLayoutManager;
    private PopupWindow popupWindow;
    private int mPage = 1;//点赞列表的页数
    private Gson gson = new Gson();
    private List<_ZanUser.PraiseUser> praiseUserList = new ArrayList<>();

    public ReplyAdapter(Context context, Activity activity, _Reply reply, List<_Reply.Comment> commentList, String dynamic_id) {
        this.mContext = context;
        this.mActivity = activity;
        this.mReply = reply;
        this.commentList = commentList;
        this.dynamic_id = dynamic_id;
        initPopWindow();
    }

    public void changeAdapState(int state) {
        this.adapterState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;//头部布局
        } else if (position + 1 == getItemCount()) {
            return 1;//加载更多
        } else {
            return 2;//正常显示
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_reply_head, parent, false);
            HeadViewHolder holder = new HeadViewHolder(view);
            return holder;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            LoadMoreHolder holder = new LoadMoreHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_reply_item, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            //显示头像
            GlideUtils.glideLoader(mContext, mReply.getCommented().getIcon(),
                    R.mipmap.default_icon, R.mipmap.default_icon, ((HeadViewHolder) holder).reply_head_icon, 0);
            //头像点击事件
            ((HeadViewHolder) holder).reply_head_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", mReply.getCommented().getUid());
                    intent.putExtra("is_virtual_user", mReply.getCommented().getUser_type());
                    mContext.startActivity(intent);
                }
            });
            //昵称
            ((HeadViewHolder) holder).reply_head_nick.setText(mReply.getCommented().getNick());
            //内容
            ((HeadViewHolder) holder).reply_head_title.setText(mReply.getCommented().getContent());
            //时间
            ((HeadViewHolder) holder).reply_head_add_time.setText(mReply.getCommented().getAdd_time());
            //点赞数
            ((HeadViewHolder) holder).reply_head_zan_num.setText(mReply.getCommented().getPraise_count());
            //当前用户是否赞过
            if (mReply.getCommented().getIs_praise().equals("1")) {
                //点赞过
                ((HeadViewHolder) holder).reply_head_praise.setImageResource(R.mipmap.zan_after);
            } else {
                ((HeadViewHolder) holder).reply_head_praise.setImageResource(R.mipmap.zan2);

            }
            //用户点赞接口
            ((HeadViewHolder) holder).reply_head_ll_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用点赞接口
                    HttpCommnetZan(mReply.getCommented().getId(), mReply.getCommented().getUid()
                            , ((HeadViewHolder) holder).reply_head_praise,
                            ((HeadViewHolder) holder).reply_head_reply_head_zan_add,
                            ((HeadViewHolder) holder).reply_head_praise_count);
                }
            });
            //显示总点赞数  按需求当点赞人数为0时隐藏
            if (mReply.getCommented().getPraise_count().equals("0")) {
                ((HeadViewHolder) holder).reply_head_zan_ll_pop.setVisibility(View.GONE);
            } else {
                ((HeadViewHolder) holder).reply_head_zan_ll_pop.setVisibility(View.VISIBLE);
            }
            //TODO 点击pop显示点赞过的人
            ((HeadViewHolder) holder).reply_head_zan_ll_pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPage = 1;
                    if (!praiseUserList.isEmpty()) {
                        praiseUserList.clear();
                    }
                    HttpGetUserZanList(mPage);
                }
            });
            //TODO 当没有人评论是显示没有评论的占位符
            if (commentList.isEmpty()) {
                //为空显示占位图
                ((HeadViewHolder) holder).reply_head_none_comment.setVisibility(View.VISIBLE);
                ((HeadViewHolder) holder).reply_head_reply_text_all.setVisibility(View.GONE);
            } else {
                //有评论时显示“全部评论”字段
                ((HeadViewHolder) holder).reply_head_none_comment.setVisibility(View.GONE);
                ((HeadViewHolder) holder).reply_head_reply_text_all.setVisibility(View.VISIBLE);
            }
            ((HeadViewHolder) holder).reply_head_zan_num.setText(mReply.getCommented().getPraise_count() + "人赞过");
            //点赞图标上的点赞数
            ((HeadViewHolder) holder).reply_head_praise_count.setText(mReply.getCommented().getPraise_count());
            //显示赞过的人
            if (mReply.getPraise_user().size() >= 1
                    && mReply.getPraise_user().get(0) != null) {
                ((HeadViewHolder) holder).reply_head_zan1.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, mReply.getPraise_user().get(0)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((HeadViewHolder) holder).reply_head_zan1
                        , 0);
            } else {
                ((HeadViewHolder) holder).reply_head_zan1.setVisibility(View.GONE);
            }
            //第二个人
            if (mReply.getPraise_user().size() >= 2
                    && mReply.getPraise_user().get(1) != null) {
                ((HeadViewHolder) holder).reply_head_zan2
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, mReply.getPraise_user().get(1)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((HeadViewHolder) holder).reply_head_zan2
                        , 0);
            } else {
                ((HeadViewHolder) holder).reply_head_zan2.setVisibility(View.GONE);
            }
            //第三个人
            if (mReply.getPraise_user().size() >= 3
                    && mReply.getPraise_user().get(2) != null) {
                ((HeadViewHolder) holder).reply_head_zan3
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, mReply.getPraise_user().get(2)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((HeadViewHolder) holder).reply_head_zan3
                        , 0);
            } else {
                ((HeadViewHolder) holder).reply_head_zan3.setVisibility(View.GONE);
            }
            //第四个人
            if (mReply.getPraise_user().size() >= 4
                    && mReply.getPraise_user().get(3) != null) {
                ((HeadViewHolder) holder).reply_head_zan4
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, mReply.getPraise_user().get(3)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((HeadViewHolder) holder).reply_head_zan4
                        , 0);
            } else {
                ((HeadViewHolder) holder).reply_head_zan4.setVisibility(View.GONE);
            }
            //第五个人
            if (mReply.getPraise_user().size() >= 5
                    && mReply.getPraise_user().get(4) != null) {
                ((HeadViewHolder) holder).reply_head_zan5
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, mReply.getPraise_user().get(4)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((HeadViewHolder) holder).reply_head_zan5
                        , 0);
            } else {
                ((HeadViewHolder) holder).reply_head_zan5.setVisibility(View.GONE);
            }
            //第六个人
            if (mReply.getPraise_user().size() >= 6
                    && mReply.getPraise_user().get(5) != null) {
                ((HeadViewHolder) holder).reply_head_zan6
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, mReply.getPraise_user().get(5)
                                .getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((HeadViewHolder) holder).reply_head_zan6
                        , 0);
            } else {
                ((HeadViewHolder) holder).reply_head_zan6.setVisibility(View.GONE);
            }
        } else if (holder instanceof ItemViewHolder) {
            //显示头像
            GlideUtils.glideLoader(mContext, commentList.get(position - 1).getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((ItemViewHolder) holder).item_reply_item_comment_icon, 0);
            //头像点击事件
            ((ItemViewHolder) holder).item_reply_item_comment_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", commentList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", commentList.get(position - 1).getUser_type());
                    mContext.startActivity(intent);
                }
            });
            //显示昵称
            ((ItemViewHolder) holder).item_reply_item_comment_nick.setText("" + commentList.get(position - 1).getNick());
            //显示回复的内容
            ((ItemViewHolder) holder).item_reply_item_comment_title.setText("" + commentList.get(position - 1).getContent());
            //显示回复的时间
            ((ItemViewHolder) holder).item_reply_item_comment_time.setText("" + commentList.get(position - 1).getAdd_time());
            //用户是否点过赞这条评论
            if (commentList.get(position - 1).getIs_praise().equals("1")) {
                ((ItemViewHolder) holder).item_reply_item_comment_zan.setImageResource(R.mipmap.zan_after);
            } else {
                ((ItemViewHolder) holder).item_reply_item_comment_zan.setImageResource(R.mipmap.zan2);
            }
            //TODO 该条评论的点赞事件
            ((ItemViewHolder) holder).item_reply_item_comment_ll_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpCommnetZan(commentList.get(position - 1).getId(), commentList.get(position - 1).getUid(),
                            ((ItemViewHolder) holder).item_reply_item_comment_zan,
                            ((ItemViewHolder) holder).item_reply_item_comment_zan_add,
                            ((ItemViewHolder) holder).item_reply_item_comment_zannum);
                }
            });
            //该条回复的点赞数量
            ((ItemViewHolder) holder).item_reply_item_comment_zannum.setText("" + commentList.get(position - 1).getPraise_count());
            //TODO 回复按钮 循环跳转这个页面
            ((ItemViewHolder) holder).item_reply_item_comment_revert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReplyActivity.class);
                    intent.putExtra("comment_id", commentList.get(position - 1).getId());
                    intent.putExtra("dynamic_id", dynamic_id);
                    Log.e(TAG, "传入的参数===comment_id===" + commentList.get(position - 1).getId());
                    Log.e(TAG, "传入的参数===dynamic_id===" + dynamic_id);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof LoadMoreHolder) {
            if (position == 1) {
                ((LoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((LoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
            if (adapterState == 1) {
                ((LoadMoreHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((LoadMoreHolder) holder).mTextView.setVisibility(View.VISIBLE);
            } else {
                ((LoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((LoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        //加入了加载更多 以及头部显示
        return commentList != null ? commentList.size() + 2 : 2;
    }

    //显示的头部布局
    class HeadViewHolder extends RecyclerView.ViewHolder {
        private ImageView reply_head_icon;//用户的头像
        private TextView reply_head_nick;//用户的昵称
        private TextView reply_head_title;//用户发布的内容
        private TextView reply_head_add_time;//时间
        private TextView reply_head_praise_count;//动态点赞数
        private TextView reply_head_reply_text_all;//显示全部评论字段的textview
        private ImageView reply_head_praise;//点赞的图标

        private LinearLayout reply_head_ll_praise;//点赞(包含点赞数以及点赞的图标)
        private LinearLayout reply_head_zan_ll_pop;//点赞pop
        private TextView reply_head_reply_head_zan_add;//点赞加一字符

        private ImageView reply_head_zan1;//点赞用户的头像
        private ImageView reply_head_zan2;
        private ImageView reply_head_zan3;
        private ImageView reply_head_zan4;
        private ImageView reply_head_zan5;
        private ImageView reply_head_zan6;
        private TextView reply_head_zan_num;//点赞人数

        private ImageView reply_head_none_comment;//没人回复时显示的图片

        public HeadViewHolder(View itemView) {
            super(itemView);
            reply_head_icon = (ImageView) itemView.findViewById(R.id.reply_head_icon);
            reply_head_nick = (TextView) itemView.findViewById(R.id.reply_head_nick);
            reply_head_title = (TextView) itemView.findViewById(R.id.reply_head_title);
            reply_head_add_time = (TextView) itemView.findViewById(R.id.reply_head_add_time);
            reply_head_add_time = (TextView) itemView.findViewById(R.id.reply_head_add_time);
            reply_head_praise_count = (TextView) itemView.findViewById(R.id.reply_head_praise_count);
            reply_head_reply_text_all = (TextView) itemView.findViewById(R.id.reply_head_reply_text_all);
            reply_head_praise = (ImageView) itemView.findViewById(R.id.reply_head_praise);

            reply_head_ll_praise = (LinearLayout) itemView.findViewById(R.id.reply_head_ll_praise);
            reply_head_zan_ll_pop = (LinearLayout) itemView.findViewById(R.id.reply_head_zan_ll_pop);

            reply_head_reply_head_zan_add = (TextView) itemView.findViewById(R.id.reply_head_reply_head_zan_add);

            reply_head_zan1 = (ImageView) itemView.findViewById(R.id.reply_head_zan1);
            reply_head_zan2 = (ImageView) itemView.findViewById(R.id.reply_head_zan2);
            reply_head_zan3 = (ImageView) itemView.findViewById(R.id.reply_head_zan3);
            reply_head_zan4 = (ImageView) itemView.findViewById(R.id.reply_head_zan4);
            reply_head_zan5 = (ImageView) itemView.findViewById(R.id.reply_head_zan5);
            reply_head_zan6 = (ImageView) itemView.findViewById(R.id.reply_head_zan6);

            reply_head_zan_num = (TextView) itemView.findViewById(R.id.reply_head_zan_num);
            reply_head_none_comment = (ImageView) itemView.findViewById(R.id.reply_head_none_comment);
        }
    }

    //显示内容的布局
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_reply_item_comment_icon;//头像
        private TextView item_reply_item_comment_nick;//昵称
        private TextView item_reply_item_comment_title;//内容
        private TextView item_reply_item_comment_time;//回复时间
        private TextView item_reply_item_comment_revert;//回复按钮
        private ImageView item_reply_item_comment_zan;//点赞图标
        private TextView item_reply_item_comment_zannum;//点赞数
        private TextView item_reply_item_comment_zan_add;//点赞数加一

        private LinearLayout item_reply_item_comment_ll_zan;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_reply_item_comment_icon = (ImageView) itemView.findViewById(R.id.item_reply_item_comment_icon);
            item_reply_item_comment_nick = (TextView) itemView.findViewById(R.id.item_reply_item_comment_nick);
            item_reply_item_comment_title = (TextView) itemView.findViewById(R.id.item_reply_item_comment_title);
            item_reply_item_comment_time = (TextView) itemView.findViewById(R.id.item_reply_item_comment_time);
            item_reply_item_comment_revert = (TextView) itemView.findViewById(R.id.item_reply_item_comment_revert);
            item_reply_item_comment_zan = (ImageView) itemView.findViewById(R.id.item_reply_item_comment_zan);
            item_reply_item_comment_zannum = (TextView) itemView.findViewById(R.id.item_reply_item_comment_zannum);
            item_reply_item_comment_zan_add = (TextView) itemView.findViewById(R.id.item_reply_item_comment_zan_add);
            item_reply_item_comment_ll_zan = (LinearLayout) itemView.findViewById(R.id.item_reply_item_comment_ll_zan);
        }
    }

    class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public LoadMoreHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }

    //TODO 用户点赞 当前用户的Uid固定  23109

    /**
     * @param comment_id  内容id
     * @param praised_uid 被点赞用户的id
     * @param zan         赞的图标
     * @param tvAdd       显示加一的字段tv
     * @param tvShowNum   显示点赞数值的tv
     */
    private void HttpCommnetZan(String comment_id, String praised_uid, final ImageView zan, final TextView tvAdd, final TextView tvShowNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", "23109");
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
                        //点赞成功 动画
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

    //加载更多
    private void LoadMoreZanUser() {
        mPage++;
        HttpGetUserZanList(mPage);
    }

    //获取点赞列表
    private void HttpGetUserZanList(final int mPage) {
        isLoading = true;
        final HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("dynamic_id", mReply.getCommented().getId());
        param.put("page", mPage);
        param.put("page_size", "10");
        Log.e(TAG, "请求参数==page==" + mPage);
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

    //显示多少人赞过
    private void showPopWindow() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        mActivity.getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 30);
    }
}
