package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.DynamicDetailActivity;
import com.tbs.tobosupicture.activity.HisFansActivity;
import com.tbs.tobosupicture.activity.HisFriendsActivity;
import com.tbs.tobosupicture.activity.LoginActivity;
import com.tbs.tobosupicture.activity.MyFansActivity;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.activity.PhotoDetail;
import com.tbs.tobosupicture.bean._PersonHomePage;
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
 * Created by Mr.Lin on 2017/7/21 09:18.
 * 个人主页的适配器 在preson honmepage 页面调用显示
 */

public class PersonHomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private _PersonHomePage personHomePage;
    private List<_PersonHomePage.Dynamic> dynamicList;
    private int adapterLoadState = 1;//适配器的状态 默认加载更多 2--正常状态
    private String TAG = "PersonHomePageAdapter";
    private Activity mActivity;
    private boolean isZaning = false;

    public PersonHomePageAdapter(Context context, Activity activity, _PersonHomePage personHomePage, List<_PersonHomePage.Dynamic> dynamicList) {
        this.mContext = context;
        this.mActivity = activity;
        this.personHomePage = personHomePage;
        this.dynamicList = dynamicList;
    }

    public PersonHomePageAdapter(Context context, Activity activity, List<_PersonHomePage.Dynamic> dynamicList) {
        this.mActivity = activity;
        this.mContext = context;
        this.dynamicList = dynamicList;
    }

    public void changeLoadState(int state) {
        this.adapterLoadState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//返回加载更多
        } else if (position == 0) {
            return 0;//返回头部
        } else {
            return 2;//中间图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_person_homepage_head, parent, false);
            PhpHeadHolder headHolder = new PhpHeadHolder(view);
            return headHolder;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            LoadMoreViewHolder loadMoreViewHolder = new LoadMoreViewHolder(view);
            return loadMoreViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_person_homepage, parent, false);
            PhpItemHolder phpItemHolder = new PhpItemHolder(view);
            return phpItemHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PhpHeadHolder) {
            //返回按钮
            ((PhpHeadHolder) holder).item_php_head_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
            //根据用户的身份加载不同的显示
            if (personHomePage.getUser_info().getUser_type().equals("3")) {
                //装修公司  整个业主层隐藏
                ((PhpHeadHolder) holder).item_php_head_yz.setVisibility(View.GONE);
                ((PhpHeadHolder) holder).item_php_head_zxgs.setVisibility(View.VISIBLE);
            } else {
                ((PhpHeadHolder) holder).item_php_head_yz.setVisibility(View.VISIBLE);
                ((PhpHeadHolder) holder).item_php_head_zxgs.setVisibility(View.GONE);
            }
            //设置装修公司头像
            GlideUtils.glideLoader(mContext, personHomePage.getUser_info().getIcon(), 0, 0,
                    ((PhpHeadHolder) holder).item_php_head_zxgs_icon);
            //设置装修公司的名称
            ((PhpHeadHolder) holder).item_php_head_zxgs_name.setText(personHomePage.getUser_info().getNick());
            //设置装修公司的地址
            ((PhpHeadHolder) holder).item_php_head_zxgs_address.setText(personHomePage.getUser_info().getAddress());
            //设置装修公司的验证情况
            if (personHomePage.getUser_info().getCertification().equals("1")) {
                //已认证
                ((PhpHeadHolder) holder).item_php_head_zxgs_renzheng.setVisibility(View.VISIBLE);
            } else {
                ((PhpHeadHolder) holder).item_php_head_zxgs_renzheng.setVisibility(View.GONE);
            }
            //头像
            GlideUtils.glideLoader(mContext, personHomePage.getUser_info().getIcon(),
                    R.mipmap.default_icon, R.mipmap.default_icon,
                    ((PhpHeadHolder) holder).item_php_head_icon, 0);
            //背景图
            if (!TextUtils.isEmpty(personHomePage.getUser_info().getCover_url())) {
                GlideUtils.glideLoader(mContext, personHomePage.getUser_info().getCover_url(), R.mipmap.me_bg, R.mipmap.me_bg, ((PhpHeadHolder) holder).item_php_head_bg);
            } else {
                Glide.with(mContext).load(R.mipmap.me_bg).into(((PhpHeadHolder) holder).item_php_head_bg);
            }
            //昵称
            ((PhpHeadHolder) holder).item_php_head_nick.setText("" + personHomePage.getUser_info().getNick());
            //性别以及所在地
            if (personHomePage.getUser_info().getSex().equals("1")) {
                ((PhpHeadHolder) holder).item_php_head_user_info.setText("男" + " " + personHomePage.getUser_info().getProvince_name() + " " + personHomePage.getUser_info().getCity_name());
            } else if (personHomePage.getUser_info().getSex().equals("2")) {
                ((PhpHeadHolder) holder).item_php_head_user_info.setText("女" + " " + personHomePage.getUser_info().getProvince_name() + " " + personHomePage.getUser_info().getCity_name());
            } else {
                ((PhpHeadHolder) holder).item_php_head_user_info.setText("" + personHomePage.getUser_info().getProvince_name() + " " + personHomePage.getUser_info().getCity_name());
            }
            //个性签名
            ((PhpHeadHolder) holder).item_php_head_signature.setText("" + personHomePage.getUser_info().getPersonal_signature());
            //设置是否加为图友  1--已经关注 0--未关注  当用户进入自己的个人主页时隐藏加为好友的按钮
            if (Utils.userIsLogin(mContext)) {
                if (personHomePage.getUser_info().getId().equals(SpUtils.getUserUid(mContext))) {
                    //进入了自己的主页
                    ((PhpHeadHolder) holder).item_php_head_addfriend.setVisibility(View.GONE);
                } else {
                    ((PhpHeadHolder) holder).item_php_head_addfriend.setVisibility(View.VISIBLE);
                }
            }
            if (personHomePage.getUser_info().getIs_follow().equals("1")) {
                ((PhpHeadHolder) holder).item_php_head_addfriend.setText("取消关注");
                ((PhpHeadHolder) holder).item_php_head_addfriend.setBackgroundResource(R.drawable.shape_quxiaoguanzhu);
            } else {
                ((PhpHeadHolder) holder).item_php_head_addfriend.setText("+加为图友");
                ((PhpHeadHolder) holder).item_php_head_addfriend.setBackgroundResource(R.drawable.shape_send_comment);
            }
            ((PhpHeadHolder) holder).item_php_head_addfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 请求接口加为图友或者取消关注(还差用户未登录以及用户点击进入的是自己的个人详情页的判断)
                    if (Utils.userIsLogin(mContext)) {
                        HttpFollow(((PhpHeadHolder) holder).item_php_head_addfriend);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }

                }
            });
            //动态数
            ((PhpHeadHolder) holder).item_php_head_dynamic_count.setText("" + personHomePage.getUser_info().getDynamic_count());
            //图谜数
            ((PhpHeadHolder) holder).item_php_head_followed_count.setText("" + personHomePage.getUser_info().getFollowed_count());
            ((PhpHeadHolder) holder).item_php_head_ll_followed_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 点击跳转到图谜页
                    Intent intent = new Intent(mContext, HisFansActivity.class);
                    if (personHomePage.getUser_info().getId().equals(SpUtils.getUserUid(mContext))) {
                        intent.putExtra("title", "我的图谜");
                    } else {
                        intent.putExtra("title", "他的图谜");
                    }
                    intent.putExtra("uid", personHomePage.getUser_info().getId());//被查看用户的id
                    intent.putExtra("is_virtual_user", personHomePage.getUser_info().getIs_virtual_user());//是否是虚拟用户
                    mContext.startActivity(intent);
                }
            });
            //图友数
            ((PhpHeadHolder) holder).item_php_head_follow_count.setText("" + personHomePage.getUser_info().getFollow_count());
            ((PhpHeadHolder) holder).item_php_head_ll_follow_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 点击跳转到图友页面
                    Intent intent = new Intent(mContext, HisFriendsActivity.class);
                    if (personHomePage.getUser_info().getId().equals(SpUtils.getUserUid(mContext))) {
                        intent.putExtra("title", "我的图友");
                    } else {
                        intent.putExtra("title", "他的图友");
                    }
                    intent.putExtra("uid", personHomePage.getUser_info().getId());//被查看用户的id
                    intent.putExtra("user_type", personHomePage.getUser_info().getIs_virtual_user());//是否是虚拟用户
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof PhpItemHolder) {
            //今天的时间
            ((PhpItemHolder) holder).item_php_item_day.setText("" + dynamicList.get(position - 1).getDay());
            //月份
            ((PhpItemHolder) holder).item_php_item_month.setText("" + dynamicList.get(position - 1).getMonth());
            //动态标题
            ((PhpItemHolder) holder).item_php_item_title.setText("" + dynamicList.get(position - 1).getTitle());
            //动态类型
            if (dynamicList.get(position - 1).getType().equals("0")) {
                ((PhpItemHolder) holder).item_php_item_type.setText("参与");
                ((PhpItemHolder) holder).item_php_item_type.setTextColor(Color.parseColor("#ff882e"));
                ((PhpItemHolder) holder).item_php_item_type.setBackgroundResource(R.drawable.shape_canjia);
            } else {
                ((PhpItemHolder) holder).item_php_item_type.setText("发起");
                ((PhpItemHolder) holder).item_php_item_type.setTextColor(Color.parseColor("#2bcca7"));
                ((PhpItemHolder) holder).item_php_item_type.setBackgroundResource(R.drawable.shape_faqi);
            }
            //设置图片
            if (dynamicList.get(position - 1).getImage_detail().size() >= 1
                    && dynamicList.get(position - 1).getImage_detail().get(0) != null) {
                ((PhpItemHolder) holder).item_php_item_img1
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(0),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img1);
                ((PhpItemHolder) holder).item_php_item_img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 0);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img1
                        .setVisibility(View.GONE);
            }
            if (dynamicList.get(position - 1).getImage_detail().size() >= 2
                    && dynamicList.get(position - 1).getImage_detail().get(1) != null) {
                ((PhpItemHolder) holder).item_php_item_img2
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(1),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img2);
                ((PhpItemHolder) holder).item_php_item_img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 1);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img2
                        .setVisibility(View.GONE);
            }
            if (dynamicList.get(position - 1).getImage_detail().size() >= 3
                    && dynamicList.get(position - 1).getImage_detail().get(2) != null) {
                ((PhpItemHolder) holder).item_php_item_img3
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(2),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img3);
                ((PhpItemHolder) holder).item_php_item_img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 2);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img3
                        .setVisibility(View.GONE);
            }
            if (dynamicList.get(position - 1).getImage_detail().size() >= 4
                    && dynamicList.get(position - 1).getImage_detail().get(3) != null) {
                ((PhpItemHolder) holder).item_php_item_img4
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(3),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img4);
                ((PhpItemHolder) holder).item_php_item_img4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 3);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img4
                        .setVisibility(View.GONE);
            }
            if (dynamicList.get(position - 1).getImage_detail().size() >= 5
                    && dynamicList.get(position - 1).getImage_detail().get(4) != null) {
                ((PhpItemHolder) holder).item_php_item_img5
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(4),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img5);
                ((PhpItemHolder) holder).item_php_item_img5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 4);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img5
                        .setVisibility(View.GONE);
            }
            if (dynamicList.get(position - 1).getImage_detail().size() >= 6
                    && dynamicList.get(position - 1).getImage_detail().get(5) != null) {
                ((PhpItemHolder) holder).item_php_item_img6
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(5),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img6);
                ((PhpItemHolder) holder).item_php_item_img6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 5);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img6
                        .setVisibility(View.GONE);
            }
            if (dynamicList.get(position - 1).getImage_detail().size() >= 7
                    && dynamicList.get(position - 1).getImage_detail().get(6) != null) {
                ((PhpItemHolder) holder).item_php_item_img7
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(6),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img7);
                ((PhpItemHolder) holder).item_php_item_img7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 6);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img7
                        .setVisibility(View.GONE);
            }
            if (dynamicList.get(position - 1).getImage_detail().size() >= 8
                    && dynamicList.get(position - 1).getImage_detail().get(7) != null) {
                ((PhpItemHolder) holder).item_php_item_img8
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(7),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img8);
                ((PhpItemHolder) holder).item_php_item_img8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 7);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img8
                        .setVisibility(View.GONE);
            }
            if (dynamicList.get(position - 1).getImage_detail().size() >= 9
                    && dynamicList.get(position - 1).getImage_detail().get(8) != null) {
                ((PhpItemHolder) holder).item_php_item_img9
                        .setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(mContext, dynamicList.get(position - 1).getImage_detail().get(8),
                        R.mipmap.loading_img_fail, R.mipmap.loading_img, ((PhpItemHolder) holder).item_php_item_img9);
                ((PhpItemHolder) holder).item_php_item_img9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoDetail.class);
                        intent.putExtra("mDynamicId", dynamicList.get(position - 1).getId());
                        intent.putExtra("mImagePosition", 8);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((PhpItemHolder) holder).item_php_item_img9
                        .setVisibility(View.GONE);
            }
            //参与距今多长时间
            ((PhpItemHolder) holder).item_php_item_add_time.setText(dynamicList.get(position - 1).getDistance_time());
            //点赞数
            ((PhpItemHolder) holder).item_php_item_praise_count.setText("" + dynamicList.get(position - 1).getPraise_count());
            //回复数
            ((PhpItemHolder) holder).item_php_item_comment_count.setText("" + dynamicList.get(position - 1).getComment_count());
            //点击回复按钮进入动态详情
            ((PhpItemHolder) holder).item_dynamic_pinglun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("dynamic_id", dynamicList.get(position - 1).getId());
                    intent.putExtra("commented_uid", dynamicList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", dynamicList.get(position - 1).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            //点赞事件
            ((PhpItemHolder) holder).item_dynamic_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isZaning) {
                        if (Utils.userIsLogin(mContext)) {
                            isZaning = true;
                            //用户已经登录可以进行点赞
                            HttpPraise(SpUtils.getUserUid(mContext), dynamicList.get(position - 1).getId(),dynamicList.get(position-1).getIs_virtual_user(),
                                    dynamicList.get(position - 1).getUid(), ((PhpItemHolder) holder).item_php_item_praise_img,
                                    ((PhpItemHolder) holder).item_php_dynamic_zan_add, ((PhpItemHolder) holder).item_php_item_praise_count);
                        } else {
                            Utils.gotoLogin(mContext);
                        }
                    }
                }
            });
            //点赞和回复的状态改变
            if (dynamicList.get(position - 1).getIs_praise().equals("1")) {
                //是否点赞过
                ((PhpItemHolder) holder).item_php_item_praise_img.setImageResource(R.mipmap.zan_after);
            } else {
                ((PhpItemHolder) holder).item_php_item_praise_img.setImageResource(R.mipmap.zan2);
            }
            if (dynamicList.get(position - 1).getIs_comment().equals("1")) {
                //是否评论过
                ((PhpItemHolder) holder).item_php_item_comment_img.setImageResource(R.mipmap.pinglun_after);
            } else {
                ((PhpItemHolder) holder).item_php_item_comment_img.setImageResource(R.mipmap.pinglun);
            }
        } else if (holder instanceof LoadMoreViewHolder) {
            if (position == 1) {
                if (position == 1) {
                    ((LoadMoreViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                    ((LoadMoreViewHolder) holder).mTextView.setVisibility(View.GONE);
                    return;
                }
                if (adapterLoadState == 2) {
                    ((LoadMoreViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                    ((LoadMoreViewHolder) holder).mTextView.setVisibility(View.GONE);
                } else if (adapterLoadState == 1) {
                    ((LoadMoreViewHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                    ((LoadMoreViewHolder) holder).mTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dynamicList != null ? dynamicList.size() + 2 : 0;
    }

    //个人主页头部布局
    class PhpHeadHolder extends RecyclerView.ViewHolder {
        private ImageView item_php_head_bg;//背景图片
        private ImageView item_php_head_icon;//用户头像
        private ImageView item_php_head_zxgs_icon;//装修公司头像
        private TextView item_php_head_nick;//用户昵称
        private TextView item_php_head_zxgs_name;//装修公司名称
        private TextView item_php_head_zxgs_address;//装修公司地址
        private TextView item_php_head_user_info;//用户信息
        private TextView item_php_head_signature;//用户签名
        private TextView item_php_head_addfriend;//添加好友

        private ImageView item_php_head_zxgs_renzheng;//装修公司是否认证

        private TextView item_php_head_followed_count;//图谜数量
        private TextView item_php_head_follow_count;//图友数量
        private TextView item_php_head_dynamic_count;//动态数量

        private LinearLayout item_php_head_ll_followed_count;//整个图谜布局可点击
        private LinearLayout item_php_head_back;//返回
        private LinearLayout item_php_head_ll_follow_count;//整个图友布局可点击

        private LinearLayout item_php_head_yz;//业主层
        private LinearLayout item_php_head_zxgs;//装修公司层


        public PhpHeadHolder(View itemView) {
            super(itemView);
            item_php_head_bg = (ImageView) itemView.findViewById(R.id.item_php_head_bg);
            item_php_head_icon = (ImageView) itemView.findViewById(R.id.item_php_head_icon);
            item_php_head_zxgs_icon = (ImageView) itemView.findViewById(R.id.item_php_head_zxgs_icon);
            item_php_head_zxgs_renzheng = (ImageView) itemView.findViewById(R.id.item_php_head_zxgs_renzheng);

            item_php_head_nick = (TextView) itemView.findViewById(R.id.item_php_head_nick);
            item_php_head_user_info = (TextView) itemView.findViewById(R.id.item_php_head_user_info);
            item_php_head_signature = (TextView) itemView.findViewById(R.id.item_php_head_signature);
            item_php_head_addfriend = (TextView) itemView.findViewById(R.id.item_php_head_addfriend);
            item_php_head_zxgs_name = (TextView) itemView.findViewById(R.id.item_php_head_zxgs_name);
            item_php_head_zxgs_address = (TextView) itemView.findViewById(R.id.item_php_head_zxgs_address);

            item_php_head_followed_count = (TextView) itemView.findViewById(R.id.item_php_head_followed_count);
            item_php_head_follow_count = (TextView) itemView.findViewById(R.id.item_php_head_follow_count);
            item_php_head_dynamic_count = (TextView) itemView.findViewById(R.id.item_php_head_dynamic_count);

            item_php_head_ll_followed_count = (LinearLayout) itemView.findViewById(R.id.item_php_head_ll_followed_count);
            item_php_head_ll_follow_count = (LinearLayout) itemView.findViewById(R.id.item_php_head_ll_follow_count);
            item_php_head_back = (LinearLayout) itemView.findViewById(R.id.item_php_head_back);

            item_php_head_yz = (LinearLayout) itemView.findViewById(R.id.item_php_head_yz);
            item_php_head_zxgs = (LinearLayout) itemView.findViewById(R.id.item_php_head_zxgs);
        }
    }

    //个人主页的列表子项布局
    class PhpItemHolder extends RecyclerView.ViewHolder {
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
        //点赞加一
        private TextView item_php_dynamic_zan_add;

        private ImageView item_php_item_comment_img;//评论的图标
        private TextView item_php_item_comment_count;//评论数
        private ImageView item_php_item_praise_img;//点赞的图标
        private TextView item_php_item_praise_count;//点赞数
        //点击评论按钮
        private LinearLayout item_dynamic_pinglun;
        private LinearLayout item_dynamic_zan;

        public PhpItemHolder(View itemView) {
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

    class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }

    //TODO 请求加为图友或者取消图友关系  现在暂时写入用户的id=23109
    private void HttpFollow(final TextView guanzhuTv) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("user_type", "2");//当前登录的用户的类型是否是虚拟用户
        param.put("followed_id", personHomePage.getUser_info().getId());
        param.put("followed_user_type", personHomePage.getUser_info().getIs_virtual_user());//被关注的用户是否是虚拟用户
        HttpUtils.doPost(UrlConstans.USER_FOLLOW, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    Log.e(TAG, "链接成功==" + json);
                    if (status.equals("200")) {
                        //获取数据成功
                        final String msg = jsonObject.getString("msg");
                        if (msg.equals("添加关注成功")) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "添加关注成功", Toast.LENGTH_SHORT).show();
                                    guanzhuTv.setText("取消关注");
                                    guanzhuTv.setBackgroundResource(R.drawable.shape_quxiaoguanzhu);
                                }
                            });
                        } else if (msg.equals("取消关注成功")) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
                                    guanzhuTv.setText("+加为图友");
                                    guanzhuTv.setBackgroundResource(R.drawable.shape_send_comment);
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 用户点赞
     * uid 用户的id
     * dynamicId 动态id
     * praisedUid 被点赞用户的id号
     * is_praise 点赞前的状态
     */
    private void HttpPraise(String uid, String dynamic_id,String is_virtual_user,
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
                        mActivity.runOnUiThread(new Runnable() {
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
