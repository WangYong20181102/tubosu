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
import com.tbs.tobosupicture.bean._ZuiXin;
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
 * Created by Mr.Lin on 2017/8/2 18:19.
 */

public class ZuiXinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private Activity activity;
    private String TAG = "ZuiXinAdapter";
    //人气榜（活跃）用户
    private ArrayList<_ZuiXin.ActiveUser> activeUserArrayList;
    //动态列表
    private ArrayList<_ZuiXin.Dynamic> dynamicArrayList;
    //子项的状态 1.加载更多  2.恢复正常状态
    private int adapterLoadState = 1;
    private boolean isZaning = false;

    public ZuiXinAdapter(Context context, Activity activity, ArrayList<_ZuiXin.ActiveUser> activeUserArrayList, ArrayList<_ZuiXin.Dynamic> dynamicArrayList) {
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_zuixin_head, parent, false);
            zuiXinHeadViewHolder headViewHolderolder = new zuiXinHeadViewHolder(view);
            return headViewHolderolder;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            zuiXinLoadMoreHolder zuiXinLoadMoreHolder = new zuiXinLoadMoreHolder(view);
            return zuiXinLoadMoreHolder;
        } else {
            //返回普通图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_zuixin_dynamic, parent, false);
            ZuiXinDynamicHolder ZuiXinDynamicHolder = new ZuiXinDynamicHolder(view);
            return ZuiXinDynamicHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof zuiXinHeadViewHolder) {
            //TODO 绑定活跃榜数据  用户点击某一活跃者头像可以跳转到用户的详情 (已完成)
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            ZuiXinHeadAdapter zuiXinHeadAdapter = new ZuiXinHeadAdapter(mContext, activeUserArrayList);
            ((zuiXinHeadViewHolder) holder).zuiXinHeadRecycleView.setLayoutManager(linearLayoutManager);
            ((zuiXinHeadViewHolder) holder).zuiXinHeadRecycleView.setAdapter(zuiXinHeadAdapter);
        } else if (holder instanceof ZuiXinDynamicHolder) {
            //TODO 显示动态 点赞请求接口 回复
            //用户头像
            GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getIcon(),
                    R.mipmap.default_icon, R.mipmap.default_icon, ((ZuiXinDynamicHolder) holder).zuiXinDynamicIcon, 0);
            //跳转事件  跳转到用户的主页
            ((ZuiXinDynamicHolder) holder).zuiXinDynamicIcon.setOnClickListener(new View.OnClickListener() {
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
            ((ZuiXinDynamicHolder) holder).zuiXinDynamicNick.setText("" + dynamicArrayList.get(position - 1).getNick());
            //动态标题
            ((ZuiXinDynamicHolder) holder).zuiXinDynamicTitle.setText("" + dynamicArrayList.get(position - 1).getTitle());
            //添加时间
            ((ZuiXinDynamicHolder) holder).zuiXinDynamicViewCount.setText(dynamicArrayList.get(position - 1).getAdd_time());
            //点赞数
            ((ZuiXinDynamicHolder) holder).zuiXinDynamicPraiseCount.setText("" + dynamicArrayList.get(position - 1).getPraise_count());
            //是否点过赞
            if (dynamicArrayList.get(position - 1).getIs_praise().equals("1")) {
                ((ZuiXinDynamicHolder) holder).zuiXinImgZan.setImageResource(R.mipmap.zan_after);
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinImgZan.setImageResource(R.mipmap.zan2);
            }
            //是否回复过
            if (dynamicArrayList.get(position - 1).getIs_comment().equals("1")) {
                ((ZuiXinDynamicHolder) holder).zuiXinImgPinglun.setImageResource(R.mipmap.pinglun_after);
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinImgPinglun.setImageResource(R.mipmap.pinglun);
            }
            //点赞事件
            ((ZuiXinDynamicHolder) holder).zuiXinDynamicPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isZaning) {
                        if (Utils.userIsLogin(mContext)) {
                            isZaning = true;
                            //用户已经登录
                            HttpPraise(SpUtils.getUserUid(mContext), dynamicArrayList.get(position - 1).getId(), dynamicArrayList.get(position - 1).getIs_virtual_user(),
                                    dynamicArrayList.get(position - 1).getUid(),
                                    ((ZuiXinDynamicHolder) holder).zuiXinImgZan,
                                    ((ZuiXinDynamicHolder) holder).zuiXinDynamicZanAdd, ((ZuiXinDynamicHolder) holder).zuiXinDynamicPraiseCount);

                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                    }

                }
            });
            //回复数
            ((ZuiXinDynamicHolder) holder).zuiXinDynamicCommentCount.setText("" + dynamicArrayList.get(position - 1).getComment_count());
            ((ZuiXinDynamicHolder) holder).zuiXinDynamicPinlun.setOnClickListener(new View.OnClickListener() {
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
            ((ZuiXinDynamicHolder) holder).zuiXinItemLL.setOnClickListener(new View.OnClickListener() {
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
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag1.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(0),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag1);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 0);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag1.setVisibility(View.GONE);
            }
            //第二张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 2 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(1))) {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag2.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(1),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag2);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 1);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag2.setVisibility(View.GONE);
            }
            //第三张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 3 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(2))) {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag3.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(2),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag3);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 2);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag3.setVisibility(View.GONE);
            }
            //第四张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 4 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(3))) {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag4.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(3),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag4);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 3);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag4.setVisibility(View.GONE);
            }
            //第五张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 5 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(4))) {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag5.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(4),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag5);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 4);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag5.setVisibility(View.GONE);
            }
            //第六张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 6 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(5))) {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag6.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(5),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag6);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 5);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag6.setVisibility(View.GONE);
            }
            //第七张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 7 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(6))) {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag7.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(6),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag7);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 6);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag7.setVisibility(View.GONE);
            }
            //第八张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 8 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(7))) {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag8.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(7),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag8);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 7);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag8.setVisibility(View.GONE);
            }
            //第九张
            if (dynamicArrayList.get(position - 1).getImage_url().size() >= 9 && !TextUtils.isEmpty(dynamicArrayList.get(position - 1).getImage_url().get(8))) {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag9.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicArrayList.get(position - 1).getImage_url().get(8),
                        R.mipmap.test, R.mipmap.test, ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag9);
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicArrayList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 8);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((ZuiXinDynamicHolder) holder).zuiXinDynamicImag9.setVisibility(View.GONE);
            }
        } else if (holder instanceof zuiXinLoadMoreHolder) {
            //加载更多
            if (position == 1) {
                ((zuiXinLoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((zuiXinLoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
            if (adapterLoadState == 1) {
                ((zuiXinLoadMoreHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((zuiXinLoadMoreHolder) holder).mTextView.setVisibility(View.VISIBLE);
            } else {
                ((zuiXinLoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((zuiXinLoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
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
    class zuiXinHeadViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView zuiXinHeadRecycleView;

        public zuiXinHeadViewHolder(View itemView) {
            super(itemView);
            zuiXinHeadRecycleView = (RecyclerView) itemView.findViewById(R.id.zuixin_head_recycle);
        }
    }

    //加载更多
    class zuiXinLoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public zuiXinLoadMoreHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }

    //普通图层  显示动态
    class ZuiXinDynamicHolder extends RecyclerView.ViewHolder {
        private LinearLayout zuiXinItemLL;//整个最新的组层
        private ImageView zuiXinDynamicIcon;//用户的头像
        private TextView zuiXinDynamicNick;//用户的昵称
        private TextView zuiXinDynamicTitle;//用户发布的内容
        private ImageView zuiXinDynamicImag1;//动态图一
        private ImageView zuiXinDynamicImag2;//
        private ImageView zuiXinDynamicImag3;//
        private ImageView zuiXinDynamicImag4;//
        private ImageView zuiXinDynamicImag5;//
        private ImageView zuiXinDynamicImag6;//
        private ImageView zuiXinDynamicImag7;//
        private ImageView zuiXinDynamicImag8;//
        private ImageView zuiXinDynamicImag9;//

        private ImageView zuiXinImgZan;//点赞图标
        private ImageView zuiXinImgPinglun;//点赞图标

        private TextView zuiXinDynamicViewCount;//动态浏览数
        private TextView zuiXinDynamicCommentCount;//动态评论数
        private TextView zuiXinDynamicPraiseCount;//动态点赞数
        private TextView zuiXinDynamicZanAdd;//动态点赞数加一

        private LinearLayout zuiXinDynamicPinlun;//评论按钮可点击
        private LinearLayout zuiXinDynamicPraise;//评论按钮可点击

        public ZuiXinDynamicHolder(View itemView) {
            super(itemView);
            zuiXinItemLL = (LinearLayout) itemView.findViewById(R.id.item_zuixin_ll);
            zuiXinDynamicIcon = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_icon);
            zuiXinDynamicNick = (TextView) itemView.findViewById(R.id.zuixin_dynamic_nick);
            zuiXinDynamicTitle = (TextView) itemView.findViewById(R.id.zuixin_dynamic_title);
            zuiXinDynamicImag1 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img1);
            zuiXinDynamicImag2 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img2);
            zuiXinDynamicImag3 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img3);
            zuiXinDynamicImag4 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img4);
            zuiXinDynamicImag5 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img5);
            zuiXinDynamicImag6 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img6);
            zuiXinDynamicImag7 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img7);
            zuiXinDynamicImag8 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img8);
            zuiXinDynamicImag9 = (ImageView) itemView.findViewById(R.id.zuixin_dynamic_img9);

            zuiXinImgZan = (ImageView) itemView.findViewById(R.id.zuixin_img_zan);
            zuiXinImgPinglun = (ImageView) itemView.findViewById(R.id.zuixin_img_pinglun);

            zuiXinDynamicViewCount = (TextView) itemView.findViewById(R.id.zuixin_dynamic_view_count);
            zuiXinDynamicCommentCount = (TextView) itemView.findViewById(R.id.zuixin_dynamic_comment_count);
            zuiXinDynamicPraiseCount = (TextView) itemView.findViewById(R.id.zuixin_dynamic_praise_count);
            zuiXinDynamicZanAdd = (TextView) itemView.findViewById(R.id.zuixin_dynamic_zan_add);

            zuiXinDynamicPinlun = (LinearLayout) itemView.findViewById(R.id.item_dynamic_pinlun);
            zuiXinDynamicPraise = (LinearLayout) itemView.findViewById(R.id.zuixin_dynamic_praise);
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
                                    int num = Integer.parseInt(tvShowNum.getText().toString());
                                    int numAddone = num - 1;
                                    tvShowNum.setText("" + numAddone);
                                    zan.setImageResource(R.mipmap.zan2);
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
