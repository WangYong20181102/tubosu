package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
import com.tbs.tobosupicture.activity.PhotoDetail;
import com.tbs.tobosupicture.bean._MyDynamic;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/8/7 09:48.
 */

public class MyDynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<_MyDynamic> myDynamicList;
    private int adapterLoadState = 1;//适配器的加载状态 默认加载更多
    private String TAG = "MyDynamicAdapter";
    private boolean isZaning = false;
    private Activity mActivity;

    public MyDynamicAdapter(Context context, Activity activity, List<_MyDynamic> myDynamicList) {
        this.mContext = context;
        this.myDynamicList = myDynamicList;
        this.mActivity = activity;
    }

    public void changeLoadState(int state) {
        this.adapterLoadState = state;
    }

    //获取列表状态
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//加载更多
        } else {
            return 2;//普通图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_person_homepage, parent, false);
            MyDynamicHolder myDynamicHolder = new MyDynamicHolder(view);
            return myDynamicHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            loadMoreHolder loadMoreHolder = new loadMoreHolder(view);
            return loadMoreHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyDynamicHolder) {
            //今天的时间
            ((MyDynamicHolder) holder).item_php_item_day.setText("" + myDynamicList.get(position).getDay());
            //月份
            ((MyDynamicHolder) holder).item_php_item_month.setText("" + myDynamicList.get(position).getMonth());
            //动态标题
            ((MyDynamicHolder) holder).item_php_item_title.setText("" + myDynamicList.get(position).getTitle());
            //动态类型
            if (myDynamicList.get(position).getType().equals("参与")) {
                ((MyDynamicHolder) holder).item_php_item_type.setText("参与");
                ((MyDynamicHolder) holder).item_php_item_type.setTextColor(Color.parseColor("#ff882e"));
                ((MyDynamicHolder) holder).item_php_item_type.setBackgroundResource(R.drawable.shape_canjia);
            } else {
                ((MyDynamicHolder) holder).item_php_item_type.setText("发起");
                ((MyDynamicHolder) holder).item_php_item_type.setTextColor(Color.parseColor("#2bcca7"));
                ((MyDynamicHolder) holder).item_php_item_type.setBackgroundResource(R.drawable.shape_faqi);
            }
            //设置图片
            if (myDynamicList.get(position).getImage_detail().size() >= 1
                    && myDynamicList.get(position).getImage_detail().get(0) != null) {
                ((MyDynamicHolder) holder).item_php_item_img1
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(0),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img1);
                ((MyDynamicHolder) holder).item_php_item_img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 0);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img1
                        .setVisibility(View.GONE);
            }
            if (myDynamicList.get(position).getImage_detail().size() >= 2
                    && myDynamicList.get(position).getImage_detail().get(1) != null) {
                ((MyDynamicHolder) holder).item_php_item_img2
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(1),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img2);
                ((MyDynamicHolder) holder).item_php_item_img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 1);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img2
                        .setVisibility(View.GONE);
            }
            if (myDynamicList.get(position).getImage_detail().size() >= 3
                    && myDynamicList.get(position).getImage_detail().get(2) != null) {
                ((MyDynamicHolder) holder).item_php_item_img3
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(2),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img3);
                ((MyDynamicHolder) holder).item_php_item_img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 2);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img3
                        .setVisibility(View.GONE);
            }
            if (myDynamicList.get(position).getImage_detail().size() >= 4
                    && myDynamicList.get(position).getImage_detail().get(3) != null) {
                ((MyDynamicHolder) holder).item_php_item_img4
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(3),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img4);
                ((MyDynamicHolder) holder).item_php_item_img4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 3);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img4
                        .setVisibility(View.GONE);
            }
            if (myDynamicList.get(position).getImage_detail().size() >= 5
                    && myDynamicList.get(position).getImage_detail().get(4) != null) {
                ((MyDynamicHolder) holder).item_php_item_img5
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(4),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img5);
                ((MyDynamicHolder) holder).item_php_item_img5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 4);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img5
                        .setVisibility(View.GONE);
            }
            if (myDynamicList.get(position).getImage_detail().size() >= 6
                    && myDynamicList.get(position).getImage_detail().get(5) != null) {
                ((MyDynamicHolder) holder).item_php_item_img6
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(5),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img6);
                ((MyDynamicHolder) holder).item_php_item_img6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 5);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img6
                        .setVisibility(View.GONE);
            }
            if (myDynamicList.get(position).getImage_detail().size() >= 7
                    && myDynamicList.get(position).getImage_detail().get(6) != null) {
                ((MyDynamicHolder) holder).item_php_item_img7
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(6),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img7);
                ((MyDynamicHolder) holder).item_php_item_img7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 6);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img7
                        .setVisibility(View.GONE);
            }
            if (myDynamicList.get(position).getImage_detail().size() >= 8
                    && myDynamicList.get(position).getImage_detail().get(7) != null) {
                ((MyDynamicHolder) holder).item_php_item_img8
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(7),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img8);
                ((MyDynamicHolder) holder).item_php_item_img8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 7);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img8
                        .setVisibility(View.GONE);
            }
            if (myDynamicList.get(position).getImage_detail().size() >= 9
                    && myDynamicList.get(position).getImage_detail().get(8) != null) {
                ((MyDynamicHolder) holder).item_php_item_img9
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, myDynamicList.get(position).getImage_detail().get(8),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((MyDynamicHolder) holder).item_php_item_img9);
                ((MyDynamicHolder) holder).item_php_item_img9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", myDynamicList.get(position).getId());
                        intent.putExtra("mImagePosition", 8);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((MyDynamicHolder) holder).item_php_item_img9
                        .setVisibility(View.GONE);
            }
            //参与距今多长时间
            ((MyDynamicHolder) holder).item_php_item_add_time.setText(myDynamicList.get(position).getDistance_time());
            //点赞数
            ((MyDynamicHolder) holder).item_php_item_praise_count.setText("" + myDynamicList.get(position).getPraise_count());
            //回复数
            ((MyDynamicHolder) holder).item_php_item_comment_count.setText("" + myDynamicList.get(position).getComment_count());
            //点击回复按钮进入动态详情
            ((MyDynamicHolder) holder).item_dynamic_pinglun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("dynamic_id", myDynamicList.get(position).getId());
                    intent.putExtra("commented_uid", myDynamicList.get(position).getUid());
                    intent.putExtra("is_virtual_user", myDynamicList.get(position).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            //点赞事件
            ((MyDynamicHolder) holder).item_dynamic_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isZaning) {
                        if (Utils.userIsLogin(mContext)) {
                            //用户在登录的情况下才可以点赞
                            isZaning = true;
                            HttpPraise(SpUtils.getUserUid(mContext),myDynamicList.get(position).getId(),myDynamicList.get(position).getIs_virtual_user(),
                                    myDynamicList.get(position).getUid(),((MyDynamicHolder) holder).item_php_item_praise_img,
                                    ((MyDynamicHolder) holder).item_php_dynamic_zan_add,((MyDynamicHolder) holder).item_php_item_praise_count,position);
                        } else {
                            Utils.gotoLogin(mContext);
                        }
                    }
                }
            });
            //点赞和回复的状态改变
            if (myDynamicList.get(position).getIs_praise().equals("1")) {
                ((MyDynamicHolder) holder).item_php_item_praise_img.setImageResource(R.mipmap.zan_after);
            } else {
                ((MyDynamicHolder) holder).item_php_item_praise_img.setImageResource(R.mipmap.zan2);
            }
            if (myDynamicList.get(position).getIs_comment().equals("1")) {
                ((MyDynamicHolder) holder).item_php_item_comment_img.setImageResource(R.mipmap.pinglun_after);
            } else {
                ((MyDynamicHolder) holder).item_php_item_comment_img.setImageResource(R.mipmap.pinglun);
            }

//            if (myDynamicList.get(position).getParticipate_type().equals("1")) {
//                //参与了评论
//                ((MyDynamicHolder) holder).item_php_item_comment_img.setImageResource(R.mipmap.pinglun_after);
//            } else if (myDynamicList.get(position).getParticipate_type().equals("2")) {
//                //参加了点赞
//                ((MyDynamicHolder) holder).item_php_item_praise_img.setImageResource(R.mipmap.zan_after);
//            } else if (myDynamicList.get(position).getParticipate_type().equals("3")) {
//                //即参与了点赞也参与了评论
//                ((MyDynamicHolder) holder).item_php_item_comment_img.setImageResource(R.mipmap.pinglun_after);
//                ((MyDynamicHolder) holder).item_php_item_praise_img.setImageResource(R.mipmap.zan_after);
//            } else {
//                ((MyDynamicHolder) holder).item_php_item_comment_img.setImageResource(R.mipmap.pinglun);
//                ((MyDynamicHolder) holder).item_php_item_praise_img.setImageResource(R.mipmap.zan2);
//            }
        } else if (holder instanceof loadMoreHolder) {
            if (position == 1) {
                if (position == 1) {
                    ((loadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                    ((loadMoreHolder) holder).mTextView.setVisibility(View.GONE);
                    return;
                }
                if (adapterLoadState == 2) {
                    ((loadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                    ((loadMoreHolder) holder).mTextView.setVisibility(View.GONE);
                } else if (adapterLoadState == 1) {
                    ((loadMoreHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                    ((loadMoreHolder) holder).mTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return myDynamicList != null ? myDynamicList.size() + 1 : 0;
    }

    class MyDynamicHolder extends RecyclerView.ViewHolder {
        private TextView item_php_item_day;//当天的时间
        private TextView item_php_item_month;//月份
        private TextView item_php_item_title;//动态标题
        private TextView item_php_item_type;//动态类型 是参加还是发起
        //动态图片
        private ImageView item_php_item_img1;
        private ImageView item_php_item_img2;
        private ImageView item_php_item_img3;
        private ImageView item_php_item_img4;
        private ImageView item_php_item_img5;
        private ImageView item_php_item_img6;
        private ImageView item_php_item_img7;
        private ImageView item_php_item_img8;
        private ImageView item_php_item_img9;
        //参与动态的时间
        private TextView item_php_item_add_time;

        private ImageView item_php_item_comment_img;//评论的图标
        private TextView item_php_item_comment_count;//评论数
        private ImageView item_php_item_praise_img;//点赞的图标
        private TextView item_php_item_praise_count;//点赞数
        private TextView item_php_dynamic_zan_add;//点赞数加一的显示框
        //点击评论按钮
        private LinearLayout item_dynamic_pinglun;
        private LinearLayout item_dynamic_zan;//点赞区域

        public MyDynamicHolder(View itemView) {
            super(itemView);
            item_php_item_day = (TextView) itemView.findViewById(R.id.item_php_item_day);
            item_php_item_month = (TextView) itemView.findViewById(R.id.item_php_item_month);
            item_php_item_title = (TextView) itemView.findViewById(R.id.item_php_item_title);
            item_php_item_type = (TextView) itemView.findViewById(R.id.item_php_item_type);

            item_php_item_img1 = (ImageView) itemView.findViewById(R.id.item_php_item_img1);
            item_php_item_img2 = (ImageView) itemView.findViewById(R.id.item_php_item_img2);
            item_php_item_img3 = (ImageView) itemView.findViewById(R.id.item_php_item_img3);
            item_php_item_img4 = (ImageView) itemView.findViewById(R.id.item_php_item_img4);
            item_php_item_img5 = (ImageView) itemView.findViewById(R.id.item_php_item_img5);
            item_php_item_img6 = (ImageView) itemView.findViewById(R.id.item_php_item_img6);
            item_php_item_img7 = (ImageView) itemView.findViewById(R.id.item_php_item_img7);
            item_php_item_img8 = (ImageView) itemView.findViewById(R.id.item_php_item_img8);
            item_php_item_img9 = (ImageView) itemView.findViewById(R.id.item_php_item_img9);

            item_php_item_add_time = (TextView) itemView.findViewById(R.id.item_php_item_add_time);
            item_php_dynamic_zan_add = (TextView) itemView.findViewById(R.id.item_php_dynamic_zan_add);

            item_dynamic_pinglun = (LinearLayout) itemView.findViewById(R.id.item_dynamic_pinglun);
            item_dynamic_zan = (LinearLayout) itemView.findViewById(R.id.item_dynamic_zan);

            item_php_item_comment_img = (ImageView) itemView.findViewById(R.id.item_php_item_comment_img);
            item_php_item_comment_count = (TextView) itemView.findViewById(R.id.item_php_item_comment_count);
            item_php_item_praise_count = (TextView) itemView.findViewById(R.id.item_php_item_praise_count);
            item_php_item_praise_img = (ImageView) itemView.findViewById(R.id.item_php_item_praise_img);
        }
    }

    class loadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public loadMoreHolder(View itemView) {
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
    private void HttpPraise(String uid, String dynamic_id,String is_virtual_user,
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
                                    myDynamicList.get(position).setIs_praise("1");
                                } else {
                                    isZaning = false;
                                    int num = Integer.parseInt(tvShowNum.getText().toString());
                                    int numAddone = num - 1;
                                    tvShowNum.setText("" + numAddone);
                                    zan.setImageResource(R.mipmap.zan2);
                                    myDynamicList.get(position).setIs_praise("0");
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
