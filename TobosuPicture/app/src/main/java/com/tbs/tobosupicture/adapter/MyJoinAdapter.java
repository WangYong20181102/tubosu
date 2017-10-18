package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.DynamicDetailActivity;
import com.tbs.tobosupicture.activity.DynamicMsgActivity;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.activity.PhotoDetail;
import com.tbs.tobosupicture.bean._DynamicBase;
import com.tbs.tobosupicture.bean._ReceiveMsg;
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

/**
 * Created by Mr.Lin on 2017/8/12 19:32.
 */

public class MyJoinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<_DynamicBase> dynamicBaseList;
    private List<String> myJoinMsgList;
    private Activity mActivity;
    private String TAG = "MyJoinAdapter";
    private int adapterLoadState = 1;//子项加载情况
    private boolean isZaning = false;

    public MyJoinAdapter(Context context, Activity activity, List<String> myJoinMsgList, List<_DynamicBase> dynamicBaseList) {
        this.mContext = context;
        this.myJoinMsgList = myJoinMsgList;
        this.dynamicBaseList = dynamicBaseList;
        this.mActivity = activity;
    }

    //图层的变换
    public void changeLoadState(int state) {
        this.adapterLoadState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;//返回头部
        } else if (position + 1 == getItemCount()) {
            return 1;//加载更多
        } else {
            return 2;//返回普通图层
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_orgin_head, parent, false);
            MyJoinHeadHolder headHolder = new MyJoinHeadHolder(view);
            return headHolder;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            MyJoinLoadMoreHolder loadMoreHolder = new MyJoinLoadMoreHolder(view);
            return loadMoreHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_base, parent, false);
            MyJoinItemHolder itemHolder = new MyJoinItemHolder(view);
            return itemHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyJoinHeadHolder) {
            if (myJoinMsgList.isEmpty() || TextUtils.isEmpty(myJoinMsgList.get(0))) {
                ((MyJoinHeadHolder) holder).head_hint_rl.setVisibility(View.INVISIBLE);
            } else {
                ((MyJoinHeadHolder) holder).head_hint_rl.setVisibility(View.VISIBLE);
                ((MyJoinHeadHolder) holder).head_hint_text.setText(myJoinMsgList.get(1) + "条新消息");
                GlideUtils.glideLoader(mContext, myJoinMsgList.get(0), R.mipmap.default_icon, R.mipmap.default_icon, ((MyJoinHeadHolder) holder).head_hint_icon, 0);
                ((MyJoinHeadHolder) holder).head_hint_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DynamicMsgActivity.class);
                        intent.putExtra("type", "2");
                        mContext.startActivity(intent);
                        ((MyJoinHeadHolder) holder).head_hint_rl.setVisibility(View.INVISIBLE);
                    }
                });
            }
        } else if (holder instanceof MyJoinItemHolder) {
            List<ImageView> imageViewList = new ArrayList<>();//动态图集
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img1);
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img2);
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img3);
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img4);
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img5);
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img6);
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img7);
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img8);
            imageViewList.add(((MyJoinItemHolder) holder).dynamic_base_img9);
            //设置头像
            GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getIcon(), 0, 0, ((MyJoinItemHolder) holder).dynamic_base_icon, 0);
            //头像点击事件
            ((MyJoinItemHolder) holder).dynamic_base_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", dynamicBaseList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", dynamicBaseList.get(position - 1).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            //设置昵称
            ((MyJoinItemHolder) holder).dynamic_base_nick.setText(dynamicBaseList.get(position - 1).getNick());
            //设置内容
            ((MyJoinItemHolder) holder).dynamic_base_title.setText(dynamicBaseList.get(position - 1).getTitle());
            //第一张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 1 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(0))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img1.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(0),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img1);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 0);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img1.setVisibility(View.GONE);
            }
            //第二张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 2 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(1))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img2.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(1),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img2);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 1);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img2.setVisibility(View.GONE);
            }
            //第三张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 3 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(2))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img3.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(2),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img3);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 2);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img3.setVisibility(View.GONE);
            }
            //第四张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 4 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(3))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img4.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(3),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img4);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 3);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img4.setVisibility(View.GONE);
            }
            //第五张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 5 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(4))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img5.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(4),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img5);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 4);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img5.setVisibility(View.GONE);
            }
            //第六张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 6 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(5))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img6.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(5),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img6);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 5);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img6.setVisibility(View.GONE);
            }
            //第七张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 7 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(6))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img7.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(6),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img7);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 6);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img7.setVisibility(View.GONE);
            }
            //第八张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 8 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(7))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img8.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(7),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img8);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 7);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img8.setVisibility(View.GONE);
            }
            //第九张
            if (dynamicBaseList.get(position - 1).getImage_url().size() >= 9 && !TextUtils.isEmpty(dynamicBaseList.get(position - 1).getImage_url().get(8))) {
                //设置图片
                ((MyJoinItemHolder) holder).dynamic_base_img9.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicBaseList.get(position - 1).getImage_url().get(8),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyJoinItemHolder) holder).dynamic_base_img9);
                //点击事件
                ((MyJoinItemHolder) holder).dynamic_base_img9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicBaseList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 8);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_img9.setVisibility(View.GONE);
            }
            //设置用户是否对该动态点赞或者评论的状态
            if (dynamicBaseList.get(position - 1).getIs_praise().equals("0")) {
                ((MyJoinItemHolder) holder).dynamic_base_praise.setImageResource(R.mipmap.zan2);
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_praise.setImageResource(R.mipmap.zan_after);
            }
            if (dynamicBaseList.get(position - 1).getIs_comment().equals("0")) {
                ((MyJoinItemHolder) holder).dynamic_base_comment.setImageResource(R.mipmap.pinglun);
            } else {
                ((MyJoinItemHolder) holder).dynamic_base_comment.setImageResource(R.mipmap.pinglun_after);
            }
            //点赞数
            ((MyJoinItemHolder) holder).dynamic_base_praise_count.setText(dynamicBaseList.get(position - 1).getPraise_count());
            //评论数
            ((MyJoinItemHolder) holder).dynamic_base_comment_count.setText(dynamicBaseList.get(position - 1).getComment_count());
            Log.e(TAG, "点赞数====" + dynamicBaseList.get(position - 1).getPraise_count() + "评论数====" + dynamicBaseList.get(position - 1).getComment_count());
            //时间
            ((MyJoinItemHolder) holder).dynamic_base_add_time.setText(dynamicBaseList.get(position - 1).getAdd_time());
            //浏览数
            ((MyJoinItemHolder) holder).dynamic_base_view_count.setText("浏览数:" + dynamicBaseList.get(position - 1).getView_count());
            //点赞事件
            ((MyJoinItemHolder) holder).dynamic_base_zan_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isZaning) {
                        if (Utils.userIsLogin(mContext)) {
                            isZaning = true;
                            HttpPraise(SpUtils.getUserUid(mContext), dynamicBaseList.get(position - 1).getId(), dynamicBaseList.get(position - 1).getIs_virtual_user(),
                                    dynamicBaseList.get(position - 1).getUid(), ((MyJoinItemHolder) holder).dynamic_base_praise,
                                    ((MyJoinItemHolder) holder).dynamic_base_zan_add, ((MyJoinItemHolder) holder).dynamic_base_praise_count, position - 1);
                        } else {
                            Utils.gotoLogin(mContext);
                        }
                    }

                }
            });
            //进入评论事件
            ((MyJoinItemHolder) holder).dynamic_base_pinglun_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("dynamic_id", dynamicBaseList.get(position - 1).getId());
                    intent.putExtra("commented_uid", dynamicBaseList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", dynamicBaseList.get(position - 1).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            ((MyJoinItemHolder) holder).dynamic_base_item_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("dynamic_id", dynamicBaseList.get(position - 1).getId());
                    intent.putExtra("commented_uid", dynamicBaseList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", dynamicBaseList.get(position - 1).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof MyJoinLoadMoreHolder) {
            //加载更多
            if (position == 0) {
                ((MyJoinLoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((MyJoinLoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
            if (adapterLoadState == 1) {
                ((MyJoinLoadMoreHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((MyJoinLoadMoreHolder) holder).mTextView.setVisibility(View.VISIBLE);
            } else {
                ((MyJoinLoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((MyJoinLoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dynamicBaseList != null ? dynamicBaseList.size() + 2 : 0;
    }

    //头部viewHolder
    class MyJoinHeadHolder extends RecyclerView.ViewHolder {
        private ImageView head_hint_icon;//提醒所用的头像
        private TextView head_hint_text;//提醒用的文本框
        private RelativeLayout head_hint_rl;//整个布局

        public MyJoinHeadHolder(View itemView) {
            super(itemView);
            head_hint_text = (TextView) itemView.findViewById(R.id.head_hint_text);
            head_hint_icon = (ImageView) itemView.findViewById(R.id.head_hint_icon);
            head_hint_rl = (RelativeLayout) itemView.findViewById(R.id.head_hint_rl);
        }
    }

    //子项viewHolder
    class MyJoinItemHolder extends RecyclerView.ViewHolder {
        private ImageView dynamic_base_icon;//用户的头像
        private TextView dynamic_base_nick;//用户的昵称
        private TextView dynamic_base_title;//用户发布的内容
        private ImageView dynamic_base_img1;//动态图一
        private ImageView dynamic_base_img2;//
        private ImageView dynamic_base_img3;//
        private ImageView dynamic_base_img4;//
        private ImageView dynamic_base_img5;//
        private ImageView dynamic_base_img6;//
        private ImageView dynamic_base_img7;//
        private ImageView dynamic_base_img8;//
        private ImageView dynamic_base_img9;//

        private TextView dynamic_base_add_time;//时间
        private TextView dynamic_base_view_count;//浏览数

        private TextView dynamic_base_comment_count;//动态评论数
        private TextView dynamic_base_zan_add;//动态点赞加一
        private TextView dynamic_base_praise_count;//动态点赞数

        private ImageView dynamic_base_comment;//评论的图标
        private ImageView dynamic_base_praise;//点赞的图标

        private LinearLayout dynamic_base_pinglun_ll;//评论布局
        private LinearLayout dynamic_base_zan_ll;//赞布局
        private LinearLayout dynamic_base_item_ll;//整个布局

        public MyJoinItemHolder(View itemView) {
            super(itemView);
            dynamic_base_item_ll = (LinearLayout) itemView.findViewById(R.id.dynamic_base_item_ll);
            dynamic_base_icon = (ImageView) itemView.findViewById(R.id.dynamic_base_icon);
            dynamic_base_nick = (TextView) itemView.findViewById(R.id.dynamic_base_nick);
            dynamic_base_title = (TextView) itemView.findViewById(R.id.dynamic_base_title);
            dynamic_base_img1 = (ImageView) itemView.findViewById(R.id.dynamic_base_img1);
            dynamic_base_img2 = (ImageView) itemView.findViewById(R.id.dynamic_base_img2);
            dynamic_base_img3 = (ImageView) itemView.findViewById(R.id.dynamic_base_img3);
            dynamic_base_img4 = (ImageView) itemView.findViewById(R.id.dynamic_base_img4);
            dynamic_base_img5 = (ImageView) itemView.findViewById(R.id.dynamic_base_img5);
            dynamic_base_img6 = (ImageView) itemView.findViewById(R.id.dynamic_base_img6);
            dynamic_base_img7 = (ImageView) itemView.findViewById(R.id.dynamic_base_img7);
            dynamic_base_img8 = (ImageView) itemView.findViewById(R.id.dynamic_base_img8);
            dynamic_base_img9 = (ImageView) itemView.findViewById(R.id.dynamic_base_img9);
            dynamic_base_add_time = (TextView) itemView.findViewById(R.id.dynamic_base_add_time);
            dynamic_base_view_count = (TextView) itemView.findViewById(R.id.dynamic_base_view_count);
            dynamic_base_comment_count = (TextView) itemView.findViewById(R.id.dynamic_base_comment_count);
            dynamic_base_zan_add = (TextView) itemView.findViewById(R.id.dynamic_base_zan_add);
            dynamic_base_praise_count = (TextView) itemView.findViewById(R.id.dynamic_base_praise_count);
            dynamic_base_comment = (ImageView) itemView.findViewById(R.id.dynamic_base_comment);
            dynamic_base_praise = (ImageView) itemView.findViewById(R.id.dynamic_base_praise);

            dynamic_base_pinglun_ll = (LinearLayout) itemView.findViewById(R.id.dynamic_base_pinglun_ll);
            dynamic_base_zan_ll = (LinearLayout) itemView.findViewById(R.id.dynamic_base_zan_ll);
        }
    }

    //加载更多
    class MyJoinLoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public MyJoinLoadMoreHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }

    /**
     * 用户点赞
     * uid 用户的id
     * dynamicId 动态id
     * praisedUid 被点赞用户的id号
     * is_praise 点赞前的状态
     */
    private void HttpPraise(String uid, String dynamic_id, String is_virtual_user,
                            String praised_uid, final ImageView zan, final TextView tvAdd, final TextView tvShowNum, final int position) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", uid);
        param.put("dynamic_id", dynamic_id);
        param.put("praised_uid", praised_uid);
        param.put("is_virtual_user", is_virtual_user);
//        Log.e(TAG, "praised_uid====" + praised_uid + "====" + uid + "=====" + dynamic_id);
        HttpUtils.doPost(UrlConstans.USER_PRAISE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "点赞链接失败===" + e.toString());
                isZaning = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "点赞链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        final String msg = jsonObject.getString("msg");
                        Log.e(TAG, "======" + msg);
                        //操作成功
                        //点赞操作(之前没有点赞过)
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.equals("点赞成功")) {
                                    zan.setImageResource(R.mipmap.zan_after);
                                    zanAddAnimation(tvAdd, tvShowNum);
                                    //修改点赞状态
                                    dynamicBaseList.get(position).setIs_praise("1");
                                } else {
                                    int num = Integer.parseInt(tvShowNum.getText().toString());
                                    int numAddone = num - 1;
                                    tvShowNum.setText("" + numAddone);
                                    zan.setImageResource(R.mipmap.zan2);
                                    dynamicBaseList.get(position).setIs_praise("0");
                                    isZaning = false;
                                }
                            }
                        });


                    } else if (status.equals("202")) {
                        //点赞失败
                        isZaning = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isZaning = false;
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
                isZaning = false;
            }
        }, 1000);
    }
}
