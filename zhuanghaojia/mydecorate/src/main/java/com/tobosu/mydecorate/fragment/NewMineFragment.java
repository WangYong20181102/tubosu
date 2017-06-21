package com.tobosu.mydecorate.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.FeedBackActivity;
import com.tobosu.mydecorate.activity.LoginActivity;
import com.tobosu.mydecorate.activity.MessageCenterActivity;
import com.tobosu.mydecorate.activity.MyAttentionActivity;
import com.tobosu.mydecorate.activity.NewAuthorDetailActivity;
import com.tobosu.mydecorate.activity.NewMyHistory;
import com.tobosu.mydecorate.activity.NewMycollectActivity;
import com.tobosu.mydecorate.activity.NewMylikeActivity;
import com.tobosu.mydecorate.activity.SettingActivity;
import com.tobosu.mydecorate.activity.UserInfoActivity;
import com.tobosu.mydecorate.activity.WriterActivity;
import com.tobosu.mydecorate.adapter.MyRecommendAdapter;
import com.tobosu.mydecorate.entity._MinePage;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/5/26 14:45.
 */

public class NewMineFragment extends Fragment {
    private String TAG = "NewMineFragment";
    private Context mContext;
    private CustomWaitDialog customWaitDialog;
    private Boolean isShowTuijian = false;//是否显示推荐
    private _MinePage minePage;//我的界面数据


    private ImageView nmfUserIcon;//用户的头像
    private RelativeLayout nmf_user;//整个图层
    private ImageView nmfIntoUserMsg;//进入用户详细资料
    private TextView nmfUserName;//用户的昵称

    private LinearLayout nmfUserMsg;//用户的浏览数，收藏，关注数量等等

    private LinearLayout nmfLLliulan;//用户的浏览层
    private LinearLayout nmfLLshoucang;//用户的收藏层
    private LinearLayout nmfLLdianzan;//用户的收藏层

    private TextView nmfZongliulan;//用户的总浏览数
    private TextView nmfShouCang;//用户的收藏数
    private TextView nmfDianZan;//用户的点赞数

    //在界面中显示的我的关注层 采用的是固定式布局  考虑过用listView 但是在排版布局可能吗，没有那么灵活
    private ImageView nmfIntoGuanzhu;//进入我的关注
    private TextView nmfMyAttention;//进入我的关注
    private LinearLayout nmfLLguanzhu;//显示开头的5个对象
    //第一个关注对象
    private LinearLayout nmfGuanzhu1;
    private ImageView nmfGuanzhuIcon1;
    private TextView nmfGuanzhuName1;
    //第二个关注对象
    private LinearLayout nmfGuanzhu2;
    private ImageView nmfGuanzhuIcon2;
    private TextView nmfGuanzhuName2;
    //第三个关注对象
    private LinearLayout nmfGuanzhu3;
    private ImageView nmfGuanzhuIcon3;
    private TextView nmfGuanzhuName3;
    //第四个关注对象
    private LinearLayout nmfGuanzhu4;
    private ImageView nmfGuanzhuIcon4;
    private TextView nmfGuanzhuName4;
    //第五个关注对象
    private LinearLayout nmfGuanzhu5;
    private ImageView nmfGuanzhuIcon5;
    private TextView nmfGuanzhuName5;

    //推荐关注层
    private RelativeLayout nmfRLTuijian;
    private ImageView nmfShowTuijian;
    private RecyclerView nmfTuijianRecycleView;
    private LinearLayoutManager linearManager;
    private MyRecommendAdapter recommendAdapter;

    //我的关注层
    private RelativeLayout nmfRLGuanzhu;
    //历史记录层
    private RelativeLayout nmfRLHistory;
    //消息中心层
    private RelativeLayout nmfRLMessage;
    //意见反馈层
    private RelativeLayout nmfRLOpinion;
    //分享好友层
    private RelativeLayout nmfRLShareFriend;
    private com.umeng.socialize.controller.UMSocialService controller = null; // 友盟分享的服务
    //系统设置层
    private RelativeLayout nmfRLSetting;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "==执行==onAttach()方法==");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.e(TAG, "==执行==onCreate()方法==");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "==执行==onCreateView()方法==");
        View rootView = inflater.inflate(R.layout.fragment_new_mine, null);
        setUmengShareSetting();
        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        bindView(rootView);
        initViewEvent();
        initUserBasicInfo();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "==执行==onActivityCreated()方法==");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "==执行==onStart()方法==");
        initUserBasicInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "==执行==onResume()方法==");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "==执行==onPause()方法==");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "==执行==onStop()方法==");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "==执行==onDestroyView()方法==");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "==执行==onDetach()方法==");
    }

    private void bindView(View rootView) {
        nmfUserIcon = (ImageView) rootView.findViewById(R.id.nmf_user_icon);
        nmf_user = (RelativeLayout) rootView.findViewById(R.id.nmf_user);
        nmfIntoUserMsg = (ImageView) rootView.findViewById(R.id.nmf_into_user_msg);
        nmfUserName = (TextView) rootView.findViewById(R.id.nmf_user_name);

        nmfUserMsg = (LinearLayout) rootView.findViewById(R.id.nmf_user_msg);

        nmfLLliulan = (LinearLayout) rootView.findViewById(R.id.nmf_ll_liulan);
        nmfLLshoucang = (LinearLayout) rootView.findViewById(R.id.nmf_ll_shoucang);
        nmfLLdianzan = (LinearLayout) rootView.findViewById(R.id.nmf_ll_dianzan);

        nmfZongliulan = (TextView) rootView.findViewById(R.id.nmf_zongliulan);
        nmfShouCang = (TextView) rootView.findViewById(R.id.nmf_shoucang);
        nmfDianZan = (TextView) rootView.findViewById(R.id.nmf_dianzan);

        nmfIntoGuanzhu = (ImageView) rootView.findViewById(R.id.nmf_into_guanzhu);
        nmfMyAttention = (TextView) rootView.findViewById(R.id.nmf_my_attention);
        nmfLLguanzhu = (LinearLayout) rootView.findViewById(R.id.nmf_ll_guanzhu);

        nmfGuanzhu1 = (LinearLayout) rootView.findViewById(R.id.nmf_guanzhu1);
        nmfGuanzhuIcon1 = (ImageView) rootView.findViewById(R.id.nmf_guanzhu_icon1);
        nmfGuanzhuName1 = (TextView) rootView.findViewById(R.id.nmf_guanzhu_name1);

        nmfGuanzhu2 = (LinearLayout) rootView.findViewById(R.id.nmf_guanzhu2);
        nmfGuanzhuIcon2 = (ImageView) rootView.findViewById(R.id.nmf_guanzhu_icon2);
        nmfGuanzhuName2 = (TextView) rootView.findViewById(R.id.nmf_guanzhu_name2);

        nmfGuanzhu3 = (LinearLayout) rootView.findViewById(R.id.nmf_guanzhu3);
        nmfGuanzhuIcon3 = (ImageView) rootView.findViewById(R.id.nmf_guanzhu_icon3);
        nmfGuanzhuName3 = (TextView) rootView.findViewById(R.id.nmf_guanzhu_name3);

        nmfGuanzhu4 = (LinearLayout) rootView.findViewById(R.id.nmf_guanzhu4);
        nmfGuanzhuIcon4 = (ImageView) rootView.findViewById(R.id.nmf_guanzhu_icon4);
        nmfGuanzhuName4 = (TextView) rootView.findViewById(R.id.nmf_guanzhu_name4);

        nmfGuanzhu5 = (LinearLayout) rootView.findViewById(R.id.nmf_guanzhu5);
        nmfGuanzhuIcon5 = (ImageView) rootView.findViewById(R.id.nmf_guanzhu_icon5);
        nmfGuanzhuName5 = (TextView) rootView.findViewById(R.id.nmf_guanzhu_name5);

        nmfShowTuijian = (ImageView) rootView.findViewById(R.id.nmf_show_tuijian);
        nmfTuijianRecycleView = (RecyclerView) rootView.findViewById(R.id.nmf_tuijian_recyvire);

        nmfRLGuanzhu = (RelativeLayout) rootView.findViewById(R.id.nmf_rl_guanzhu);
        nmfRLTuijian = (RelativeLayout) rootView.findViewById(R.id.nmf_rl_tuijian);
        nmfRLMessage = (RelativeLayout) rootView.findViewById(R.id.nmf_rl_messgae);
        nmfRLOpinion = (RelativeLayout) rootView.findViewById(R.id.nmf_rl_opinion);
        nmfRLShareFriend = (RelativeLayout) rootView.findViewById(R.id.nmf_rl_share_friend);
        nmfRLSetting = (RelativeLayout) rootView.findViewById(R.id.nmf_rl_setting);
        nmfRLHistory = (RelativeLayout) rootView.findViewById(R.id.nmf_rl_history);
    }

    /**
     * 初始化用户的详情布局  请求接口
     * 一.当前的用户处于未登录状态
     * 1.显示头像的地方显示默认的图片
     * 2.用户相关的数据不显示：如 总浏览数，收藏数，点赞数
     * 3.我的关注下边不显示
     * 4.推荐关注不显示
     * 5.“我的关注”，“推荐关注”，“历史记录”等走的是用户的登录
     * 二.当前用户处于登录状态
     * 1.“我的关注”，“推荐关注”，“历史记录”跳转到相应的界面
     */
    private void initViewEvent() {
        customWaitDialog = new CustomWaitDialog(mContext);
        nmfUserIcon.setOnClickListener(occl);
        nmf_user.setOnClickListener(occl);
        nmfIntoUserMsg.setOnClickListener(occl);

        nmfLLliulan.setOnClickListener(occl);
        nmfLLshoucang.setOnClickListener(occl);
        nmfLLdianzan.setOnClickListener(occl);

        nmfIntoGuanzhu.setOnClickListener(occl);

        nmfGuanzhu1.setOnClickListener(occl);
        nmfGuanzhu2.setOnClickListener(occl);
        nmfGuanzhu3.setOnClickListener(occl);
        nmfGuanzhu4.setOnClickListener(occl);
        nmfGuanzhu5.setOnClickListener(occl);

        nmfRLGuanzhu.setOnClickListener(occl);
        nmfRLTuijian.setOnClickListener(occl);
        nmfRLMessage.setOnClickListener(occl);
        nmfRLOpinion.setOnClickListener(occl);
        nmfRLShareFriend.setOnClickListener(occl);
        nmfRLSetting.setOnClickListener(occl);
        nmfRLHistory.setOnClickListener(occl);
        linearManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        nmfTuijianRecycleView.setLayoutManager(linearManager);
    }

    //初始化用户的基础的信息  源于用户登录之后拉取的信息
    private void initUserBasicInfo() {
        SharedPreferences sp = mContext.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
        if (Util.isLogin(mContext)) {
            //已经登录状态
            String userIconUrl = sp.getString("head_pic_url", "");
            nmfUserMsg.setVisibility(View.VISIBLE);
            nmfLLguanzhu.setVisibility(View.VISIBLE);
            GlideUtils.glideLoader(mContext, userIconUrl, 0, R.mipmap.jiazai_loading, nmfUserIcon, GlideUtils.CIRCLE_IMAGE);
            nmfUserName.setText("" + sp.getString("user_name", ""));
            customWaitDialog.show();
            HttpGetMineMessage();
        } else {
            //未登录状态
            nmfUserMsg.setVisibility(View.GONE);
            nmfUserName.setText("点击登录");
            nmfUserIcon.setImageResource(R.mipmap.icon_head_default);
            nmfLLguanzhu.setVisibility(View.GONE);
            nmfTuijianRecycleView.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nmf_user:
                case R.id.nmf_user_icon:
                    /**
                     * 用户的头像点击处理逻辑
                     * 1.用户未登录状态----》进入登录页面
                     * 2.用户已经登录状态----》进入用户的详情界面
                     */
                    if (Util.isLogin(mContext)) {
                        SharedPreferences sp = mContext.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
                        //已经登录状态
                        Intent it = new Intent(getActivity(), UserInfoActivity.class);
                        Bundle b = new Bundle();
                        b.putString("change_user_name", sp.getString("user_name", ""));
                        it.putExtra("Change_UserName_Bundle", b);
                        startActivityForResult(it, Constant.CHANGE_USER_INFO_REQUESTCODE);
                    } else {
                        //未登录
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                    break;
                case R.id.nmf_ll_shoucang:
                    //依据用户的登录情况，进入用户的收藏界面
                    startActivity(new Intent(mContext, NewMycollectActivity.class));
                    break;
                case R.id.nmf_ll_dianzan:
                    //依据用户的登录情况，进入用户的点赞界面
                    startActivity(new Intent(mContext, NewMylikeActivity.class));
                    break;
                case R.id.nmf_rl_guanzhu:
                case R.id.nmf_into_guanzhu:
                    //用户点击我的关注右边的箭头进入我的关注界面
                    if (Util.isLogin(mContext)) {
                        if (minePage.getAttentionList().isEmpty()) {
                            Toast.makeText(mContext, "您还没有任何关注", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(mContext, MyAttentionActivity.class));
                        }

                    } else {
                        //未登录
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                    break;
                case R.id.nmf_guanzhu1:
                    //逻辑：当前数据有则加载没有则不加载
                    //进入关注对象的主页
                    Intent intent1 = new Intent(mContext, NewAuthorDetailActivity.class);
                    intent1.putExtra("author_id", minePage.getAttentionList().get(0).getAid());
                    intent1.putExtra("page_num", minePage.getAttentionList().get(0).getArticle_count());
                    startActivity(intent1);
                    break;
                case R.id.nmf_guanzhu2:
                    //逻辑：当前数据有则加载没有则不加载
                    Intent intent2 = new Intent(mContext, NewAuthorDetailActivity.class);
                    intent2.putExtra("author_id", minePage.getAttentionList().get(1).getAid());
                    intent2.putExtra("page_num", minePage.getAttentionList().get(1).getArticle_count());
                    startActivity(intent2);
                    break;
                case R.id.nmf_guanzhu3:
                    //逻辑：当前数据有则加载没有则不加载
                    Intent intent3 = new Intent(mContext, NewAuthorDetailActivity.class);
                    intent3.putExtra("author_id", minePage.getAttentionList().get(2).getAid());
                    intent3.putExtra("page_num", minePage.getAttentionList().get(2).getArticle_count());
                    startActivity(intent3);
                    break;
                case R.id.nmf_guanzhu4:
                    //逻辑：当前数据有则加载没有则不加载
                    Intent intent4 = new Intent(mContext, NewAuthorDetailActivity.class);
                    intent4.putExtra("author_id", minePage.getAttentionList().get(3).getAid());
                    intent4.putExtra("page_num", minePage.getAttentionList().get(3).getArticle_count());
                    startActivity(intent4);
                    break;
                case R.id.nmf_guanzhu5:
                    //逻辑：当前数据有则加载没有则不加载
                    Intent intent5 = new Intent(mContext, NewAuthorDetailActivity.class);
                    intent5.putExtra("author_id", minePage.getAttentionList().get(4).getAid());
                    intent5.putExtra("page_num", minePage.getAttentionList().get(4).getArticle_count());
                    startActivity(intent5);
                    break;
                case R.id.nmf_rl_tuijian:
                    //显示和隐藏推荐关注列表
                    if (Util.isLogin(mContext)) {
                        if (isShowTuijian) {
                            //隐藏推荐关注列表
                            nmfTuijianRecycleView.setVisibility(View.GONE);
                            nmfShowTuijian.setImageResource(R.mipmap.nmf_back);
                            isShowTuijian = false;
                        } else {
                            nmfTuijianRecycleView.setVisibility(View.VISIBLE);
                            nmfShowTuijian.setImageResource(R.mipmap.nmf_back_down);
                            isShowTuijian = true;
                        }
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case R.id.nmf_rl_history:
                    //进入历史记录页面
                    if (Util.isLogin(mContext)) {
                        //已登录跳转到历史记录页面
                        startActivity(new Intent(mContext, NewMyHistory.class));
                    } else {
                        //未登录
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case R.id.nmf_rl_messgae:
                    //进入消息中心页面
                    startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                    break;
                case R.id.nmf_rl_opinion:
                    //进入意见反馈界面
                    startActivity(new Intent(getActivity(), FeedBackActivity.class));
                    break;
                case R.id.nmf_rl_share_friend:
                    //进行App的分享
                    controller.openShare(getActivity(), false);
                    break;
                case R.id.nmf_rl_setting:
                    //进入系统设置界面
                    startActivityForResult(new Intent(getActivity(), SettingActivity.class), 0);
                    break;

            }
        }
    };

    private void refreshUserMessage(String data) {
        minePage = new _MinePage(data);
        nmfZongliulan.setText("" + minePage.getView_count());
        nmfShouCang.setText("" + minePage.getCollect_count());
        nmfDianZan.setText("" + minePage.getTup_count());
        if (minePage.getAttentionList().size() == 0 || minePage.getAttentionList().isEmpty()) {
            nmfLLguanzhu.setVisibility(View.GONE);
        } else {
            //显示我的关注
            nmfLLguanzhu.setVisibility(View.VISIBLE);
            showGZuser(minePage.getAttentionList().size(), minePage);
        }
        //设置推荐
        showTuijianGz(minePage.getFollowList());
        customWaitDialog.dismiss();
    }

    //显示关注的用户
    private void showGZuser(int num, _MinePage minePage) {
        switch (num) {
            case 1:
                nmfGuanzhu1.setVisibility(View.VISIBLE);
                nmfGuanzhu2.setVisibility(View.GONE);
                nmfGuanzhu3.setVisibility(View.GONE);
                nmfGuanzhu4.setVisibility(View.GONE);
                nmfGuanzhu5.setVisibility(View.GONE);
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(0).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon1, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName1.setText("" + minePage.getAttentionList().get(0).getNick());
                break;
            case 2:
                nmfGuanzhu1.setVisibility(View.VISIBLE);
                nmfGuanzhu2.setVisibility(View.VISIBLE);
                nmfGuanzhu3.setVisibility(View.GONE);
                nmfGuanzhu4.setVisibility(View.GONE);
                nmfGuanzhu5.setVisibility(View.GONE);
                //设置第一个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(0).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon1, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName1.setText("" + minePage.getAttentionList().get(0).getNick());
                //设置第二个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(1).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon2, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName2.setText("" + minePage.getAttentionList().get(1).getNick());
                break;
            case 3:
                nmfGuanzhu1.setVisibility(View.VISIBLE);
                nmfGuanzhu2.setVisibility(View.VISIBLE);
                nmfGuanzhu3.setVisibility(View.VISIBLE);
                nmfGuanzhu4.setVisibility(View.GONE);
                nmfGuanzhu5.setVisibility(View.GONE);
                //设置第一个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(0).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon1, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName1.setText("" + minePage.getAttentionList().get(0).getNick());
                //设置第二个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(1).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon2, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName2.setText("" + minePage.getAttentionList().get(1).getNick());
                //设置第三个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(2).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon3, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName3.setText("" + minePage.getAttentionList().get(2).getNick());
                break;
            case 4:
                nmfGuanzhu1.setVisibility(View.VISIBLE);
                nmfGuanzhu2.setVisibility(View.VISIBLE);
                nmfGuanzhu3.setVisibility(View.VISIBLE);
                nmfGuanzhu4.setVisibility(View.VISIBLE);
                nmfGuanzhu5.setVisibility(View.GONE);
                //设置第一个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(0).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon1, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName1.setText("" + minePage.getAttentionList().get(0).getNick());
                //设置第二个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(1).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon2, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName2.setText("" + minePage.getAttentionList().get(1).getNick());
                //设置第三个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(2).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon3, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName3.setText("" + minePage.getAttentionList().get(2).getNick());
                //设置第四个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(3).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon4, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName4.setText("" + minePage.getAttentionList().get(3).getNick());
                break;
            case 5:
                //设置第一个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(0).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon1, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName1.setText("" + minePage.getAttentionList().get(0).getNick());
                //设置第二个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(1).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon2, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName2.setText("" + minePage.getAttentionList().get(1).getNick());
                //设置第三个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(2).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon3, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName3.setText("" + minePage.getAttentionList().get(2).getNick());
                //设置第四个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(3).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon4, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName4.setText("" + minePage.getAttentionList().get(3).getNick());
                //设置第五个关注对象信息
                GlideUtils.glideLoader(mContext,
                        minePage.getAttentionList().get(4).getHeader_pic_url(),
                        0, R.mipmap.jiazai_loading, nmfGuanzhuIcon5, GlideUtils.CIRCLE_IMAGE);
                nmfGuanzhuName5.setText("" + minePage.getAttentionList().get(4).getNick());
                break;
        }
    }

    //显示推荐关注
    private void showTuijianGz(List<_MinePage.Follow> followList) {
        if (recommendAdapter == null) {
            recommendAdapter = new MyRecommendAdapter(mContext, followList);
            recommendAdapter.setOnMyRecommendItemClick(onMyRecommendItemClick);
            nmfTuijianRecycleView.setAdapter(recommendAdapter);
        } else {
            recommendAdapter = new MyRecommendAdapter(mContext, followList);
            recommendAdapter.setOnMyRecommendItemClick(onMyRecommendItemClick);
            nmfTuijianRecycleView.setAdapter(recommendAdapter);
            recommendAdapter.notifyDataSetChanged();
        }
    }

    private MyRecommendAdapter.OnMyRecommendItemClick onMyRecommendItemClick = new MyRecommendAdapter.OnMyRecommendItemClick() {
        @Override
        public void onItemClick(View view) {
            int position = nmfTuijianRecycleView.getChildPosition(view);
            //请求关注
        }
    };

    private void HttpGetMineMessage() {
        SharedPreferences sp = mContext.getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", sp.getString("user_id", ""));
        okHttpUtil.post(Constant.USER_PAGE_MESSAGE, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        Log.e(TAG, "数据请求成功" + json);
                        String data = jsonObject.getString("data");
                        //进行数据的处理
                        refreshUserMessage(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                Toast.makeText(mContext, "请求失败！", Toast.LENGTH_LONG).show();
                customWaitDialog.dismiss();
            }

            @Override
            public void onError(Response response, int code) {
                Toast.makeText(mContext, "请求错误！", Toast.LENGTH_LONG).show();
                customWaitDialog.dismiss();
            }
        });
    }

    //友盟社会化分享相关设置
    private void setUmengShareSetting() {
        setShareConfig();
        setSocialShareContent("");
    }

    private void setShareConfig() {
        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        weixinConfig();
        qqConfig();
        sinaConfig();
    }

    private void setSocialShareContent(String pictureUrl) {

        // 图片分享
        UMImage urlImage = new UMImage(mContext, pictureUrl);

        // 分享到微信好友的内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent("邀您下载装好家App"); // 描述
        weixinContent.setTitle("邀您下载装好家App"); // 标题
        weixinContent.setTargetUrl("http://t.cn/R4p6kwr"); // 分享的url
        weixinContent.setShareImage(urlImage); // 分享时呈现图片
        controller.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("邀您下载装好家App");
        circleMedia.setTitle("邀您下载装好家App");
        circleMedia.setShareImage(urlImage);
        circleMedia.setTargetUrl("http://t.cn/R4p6kwr");
        controller.setShareMedia(circleMedia);
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent("邀您下载装好家App");
        qzone.setTargetUrl("http://t.cn/R4p6kwr");
        qzone.setTitle("邀您下载装好家App");
        qzone.setShareImage(urlImage);
        controller.setShareMedia(qzone);

        // 设置QQ 分享内容
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("邀您下载装好家App");
        qqShareContent.setTitle("邀您下载装好家App");
        qqShareContent.setShareImage(urlImage);
        qqShareContent.setTargetUrl("http://t.cn/R4p6kwr");
        controller.setShareMedia(qqShareContent);

//         新浪微博分享的内容
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent("邀您下载装好家App");
        sinaContent.setShareImage(urlImage);
        controller.setShareMedia(sinaContent);
    }

    private void weixinConfig() {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private void qqConfig() {
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((AppCompatActivity) mContext, Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
        qqSsoHandler.addToSocialSDK();

//         添加QQ空间分享平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((AppCompatActivity) mContext, Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
        qZoneSsoHandler.addToSocialSDK();
    }

    private void sinaConfig() {
        controller.getConfig().setSsoHandler(new SinaSsoHandler(getActivity()));
    }
}
