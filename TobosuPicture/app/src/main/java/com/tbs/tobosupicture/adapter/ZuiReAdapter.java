package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.DynamicDetailActivity;
import com.tbs.tobosupicture.activity.LoginActivity;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.activity.PhotoDetail;
import com.tbs.tobosupicture.bean._ZuiRe;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/7/17 17:23.
 * 以图会友最热列表适配器(包含了人气榜的显示)
 */

public class ZuiReAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private Activity activity;
    private String TAG = "ZuiReAdapter";
    //人气榜（活跃）用户
    private ArrayList<_ZuiRe.ActiveUser> activeUserArrayList;
    //动态列表
    private ArrayList<_ZuiRe.Dynamic> dynamicArrayList;
    //子项的状态 1.加载更多  2.恢复正常状态
    private int adapterLoadState = 1;
    private boolean isZaning = false;

    public ZuiReAdapter(Context context, Activity activity, ArrayList<_ZuiRe.ActiveUser> activeUserArrayList, ArrayList<_ZuiRe.Dynamic> dynamicArrayList) {
        this.mContext = context;
        this.activeUserArrayList = activeUserArrayList;
        this.dynamicArrayList = dynamicArrayList;
        this.activity = activity;
    }

    //图层的变换
    public void changLoadState(int state) {
        this.adapterLoadState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //返回头部
            return 0;
        } else if (position + 1 == getItemCount()) {
            //返回加载更多
            return 1;
        } else {
            //返回普通图层
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_zuire_head, parent, false);
            ZuiReHeadViewHolder headViewHolderolder = new ZuiReHeadViewHolder(view);
            return headViewHolderolder;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            ZuiReLoadMoreHolder zuiReLoadMoreHolder = new ZuiReLoadMoreHolder(view);
            return zuiReLoadMoreHolder;
        } else {
            //返回普通图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_zuire_dynamic, parent, false);
            ZuiReDynamicHolder zuiReDynamicHolder = new ZuiReDynamicHolder(view);
            return zuiReDynamicHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ZuiReHeadViewHolder) {
            //TODO 绑定活跃榜数据  用户点击某一活跃者头像可以跳转到用户的详情 (已完成)
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            ZuiReHeadAdapter zuiReHeadAdapter = new ZuiReHeadAdapter(mContext, activeUserArrayList);
            ((ZuiReHeadViewHolder) holder).zuiReHeadRecycleView.setLayoutManager(linearLayoutManager);
            ((ZuiReHeadViewHolder) holder).zuiReHeadRecycleView.setAdapter(zuiReHeadAdapter);
        } else if (holder instanceof ZuiReDynamicHolder) {
            //TODO 显示动态 点赞请求接口 回复
            //用户头像
            GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getIcon(),
                    R.mipmap.default_icon, R.mipmap.default_icon, ((ZuiReDynamicHolder) holder).zuiReDynamicIcon, 0);
            //跳转事件  跳转到用户的主页
            ((ZuiReDynamicHolder) holder).zuiReDynamicIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 点击人气榜的头像进入详情页
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", dynamicArrayList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", dynamicArrayList.get(position - 1).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            //用户昵称
            ((ZuiReDynamicHolder) holder).zuiReDynamicNick.setText("" + dynamicArrayList.get(position - 1).getNick());
            //动态标题
            ((ZuiReDynamicHolder) holder).zuiReDynamicTitle.setText("" + dynamicArrayList.get(position - 1).getTitle());
            //浏览数
            ((ZuiReDynamicHolder) holder).zuiReDynamicViewCount.setText("浏览数:" + dynamicArrayList.get(position - 1).getView_count());
            //点赞数
            ((ZuiReDynamicHolder) holder).zuiReDynamicPraiseCount.setText("" + dynamicArrayList.get(position - 1).getPraise_count());
            if (dynamicArrayList.get(position - 1).getIs_praise().equals("1")) {
                ((ZuiReDynamicHolder) holder).zuiReImgZan.setImageResource(R.mipmap.zan_after);
            } else {
                ((ZuiReDynamicHolder) holder).zuiReImgZan.setImageResource(R.mipmap.zan2);
            }
            if (dynamicArrayList.get(position - 1).getIs_comment().equals("1")) {
                ((ZuiReDynamicHolder) holder).zuiReImgPinglun.setImageResource(R.mipmap.pinglun_after);
            } else {
                ((ZuiReDynamicHolder) holder).zuiReImgPinglun.setImageResource(R.mipmap.pinglun);
            }
            ((ZuiReDynamicHolder) holder).zuiReDynamicPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isZaning) {
                        if (Utils.userIsLogin(mContext)) {
                            //用户已经登录
                            isZaning = true;
                            HttpPraise(SpUtils.getUserUid(mContext),
                                    dynamicArrayList.get(position - 1).getId(),
                                    dynamicArrayList.get(position - 1).getUid(),
                                    dynamicArrayList.get(position - 1).getIs_virtual_user(),
                                    ((ZuiReDynamicHolder) holder).zuiReImgZan,
                                    ((ZuiReDynamicHolder) holder).zuiReDynamicZanAdd,
                                    ((ZuiReDynamicHolder) holder).zuiReDynamicPraiseCount);

                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                    }

                }
            });
            //回复数
            ((ZuiReDynamicHolder) holder).zuiReDynamicCommentCount.setText("" + dynamicArrayList.get(position - 1).getComment_count());
            ((ZuiReDynamicHolder) holder).zuiReDynamicPinlun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 跳转进入DynamicDetailActivity  回复详情页
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("dynamic_id", dynamicArrayList.get(position - 1).getId());
                    intent.putExtra("commented_uid", dynamicArrayList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", dynamicArrayList.get(position - 1).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            ((ZuiReDynamicHolder) holder).zuiReDynamicItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 跳转进入DynamicDetailActivity  回复详情页
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("dynamic_id", dynamicArrayList.get(position - 1).getId());
                    intent.putExtra("commented_uid", dynamicArrayList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", dynamicArrayList.get(position - 1).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            //显示动态图片
            //第一张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 1 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(0))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag1.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(0),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag1);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 0);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag1.setVisibility(View.GONE);
            }
            //第二张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 2 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(1))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag2.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(1),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag2);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 1);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag2.setVisibility(View.GONE);
            }
            //第三张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 3 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(2))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag3.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(2),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag3);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 2);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag3.setVisibility(View.GONE);
            }
            //第四张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 4 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(3))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag4.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(3),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag4);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 3);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag4.setVisibility(View.GONE);
            }
            //第五张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 5 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(4))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag5.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(4),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag5);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 4);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag5.setVisibility(View.GONE);
            }
            //第六张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 6 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(5))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag6.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(5),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag6);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 5);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag6.setVisibility(View.GONE);
            }
            //第七张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 7 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(6))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag7.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(6),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag7);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 6);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag7.setVisibility(View.GONE);
            }
            //第八张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 8 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(7))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag8.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(7),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag8);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 7);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag8.setVisibility(View.GONE);
            }
            //第九张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 9 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(8))) {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag9.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(8),
                        R.mipmap.test, R.mipmap.test, ((ZuiReDynamicHolder) holder).zuiReDynamicImag9);
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 8);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiReDynamicHolder) holder).zuiReDynamicImag9.setVisibility(View.GONE);
            }
        } else if (holder instanceof ZuiReLoadMoreHolder) {
            //加载更多
            if (position == 1) {
                ((ZuiReLoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((ZuiReLoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
            if (adapterLoadState == 1) {
                ((ZuiReLoadMoreHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((ZuiReLoadMoreHolder) holder).mTextView.setVisibility(View.VISIBLE);
            } else {
                ((ZuiReLoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((ZuiReLoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dynamicArrayList != null ? dynamicArrayList.size() + 2 : 0;
    }


    @Override
    public void onClick(View v) {

    }

    //头部人气榜
    class ZuiReHeadViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView zuiReHeadRecycleView;

        public ZuiReHeadViewHolder(View itemView) {
            super(itemView);
            zuiReHeadRecycleView = (RecyclerView) itemView.findViewById(R.id.zuire_head_recycle);
        }
    }

    //加载更多
    class ZuiReLoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public ZuiReLoadMoreHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }

    //普通图层  显示动态
    class ZuiReDynamicHolder extends RecyclerView.ViewHolder {
        private LinearLayout zuiReDynamicItem;//最热组层
        private ImageView zuiReDynamicIcon;//用户的头像
        private TextView zuiReDynamicNick;//用户的昵称
        private TextView zuiReDynamicTitle;//用户发布的内容
        private ImageView zuiReDynamicImag1;//动态图一
        private ImageView zuiReDynamicImag2;//
        private ImageView zuiReDynamicImag3;//
        private ImageView zuiReDynamicImag4;//
        private ImageView zuiReDynamicImag5;//
        private ImageView zuiReDynamicImag6;//
        private ImageView zuiReDynamicImag7;//
        private ImageView zuiReDynamicImag8;//
        private ImageView zuiReDynamicImag9;//

        private ImageView zuiReImgZan;//点赞图标
        private ImageView zuiReImgPinglun;//点赞图标

        private TextView zuiReDynamicViewCount;//动态浏览数
        private TextView zuiReDynamicCommentCount;//动态评论数
        private TextView zuiReDynamicPraiseCount;//动态点赞数
        private TextView zuiReDynamicZanAdd;//动态点赞数加一

        private LinearLayout zuiReDynamicPinlun;//评论按钮可点击
        private LinearLayout zuiReDynamicPraise;//评论按钮可点击

        public ZuiReDynamicHolder(View itemView) {
            super(itemView);
            zuiReDynamicItem = (LinearLayout) itemView.findViewById(R.id.zuire_dynamic_item);
            zuiReDynamicIcon = (ImageView) itemView.findViewById(R.id.zuire_dynamic_icon);
            zuiReDynamicNick = (TextView) itemView.findViewById(R.id.zuire_dynamic_nick);
            zuiReDynamicTitle = (TextView) itemView.findViewById(R.id.zuire_dynamic_title);
            zuiReDynamicImag1 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img1);
            zuiReDynamicImag2 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img2);
            zuiReDynamicImag3 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img3);
            zuiReDynamicImag4 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img4);
            zuiReDynamicImag5 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img5);
            zuiReDynamicImag6 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img6);
            zuiReDynamicImag7 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img7);
            zuiReDynamicImag8 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img8);
            zuiReDynamicImag9 = (ImageView) itemView.findViewById(R.id.zuire_dynamic_img9);

            zuiReImgZan = (ImageView) itemView.findViewById(R.id.zuire_img_zan);
            zuiReImgPinglun = (ImageView) itemView.findViewById(R.id.zuire_img_pinglun);

            zuiReDynamicViewCount = (TextView) itemView.findViewById(R.id.zuire_dynamic_view_count);
            zuiReDynamicCommentCount = (TextView) itemView.findViewById(R.id.zuire_dynamic_comment_count);
            zuiReDynamicPraiseCount = (TextView) itemView.findViewById(R.id.zuire_dynamic_praise_count);
            zuiReDynamicZanAdd = (TextView) itemView.findViewById(R.id.zuire_dynamic_zan_add);

            zuiReDynamicPinlun = (LinearLayout) itemView.findViewById(R.id.item_dynamic_pinlun);
            zuiReDynamicPraise = (LinearLayout) itemView.findViewById(R.id.zuire_dynamic_praise);
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
                            String praised_uid, final ImageView zan, final TextView tvAdd, final TextView tvShowNum) {
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
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.equals("点赞成功")) {
                                    zan.setImageResource(R.mipmap.zan_after);
                                    zanAddAnimation(tvAdd, tvShowNum);
                                } else {
                                    isZaning = false;
                                    int num = Integer.parseInt(tvShowNum.getText().toString());
                                    int numAddone = num - 1;
                                    tvShowNum.setText("" + numAddone);
                                    zan.setImageResource(R.mipmap.zan2);
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
