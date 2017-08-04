package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosupicture.activity.MyDesignerListActivity;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.LoginActivity;
import com.tbs.tobosupicture.activity.MyFansActivity;
import com.tbs.tobosupicture.activity.MyFriendsActivity;
import com.tbs.tobosupicture.activity.MySampleListActivity;
import com.tbs.tobosupicture.activity.PersonInfoActivity;
import com.tbs.tobosupicture.activity.ShareWeixinActivity;
import com.tbs.tobosupicture.activity.SystemActivity;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean._HomePage;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/6/29 11:11.
 * 我的 fragment  这个界面分为装修公司的页面和业主页面
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.fm_head_bg)
    ImageView fmHeadBg;
    @BindView(R.id.fm_user_icon)
    ImageView fmUserIcon;
    @BindView(R.id.fm_user_name)
    TextView fmUserName;
    @BindView(R.id.fm_dongtai_num)
    TextView fmDongtaiNum;
    @BindView(R.id.fm_dongtai_ll)
    LinearLayout fmDongtaiLl;
    @BindView(R.id.fm_tumi_num)
    TextView fmTumiNum;
    @BindView(R.id.fm_tumi_ll)
    LinearLayout fmTumiLl;
    @BindView(R.id.fm_tuyou_num)
    TextView fmTuyouNum;
    @BindView(R.id.fm_tuyou_ll)
    LinearLayout fmTuyouLl;
    @BindView(R.id.fm_sousuoguo_anli)
    RelativeLayout fmSousuoguoAnli;
    @BindView(R.id.fm_guanzhu_shejishi)
    RelativeLayout fmGuanzhuShejishi;
    @BindView(R.id.fm_shoucang_xiaoguotu)
    RelativeLayout fmShoucangXiaoguotu;
    @BindView(R.id.fm_shoucang_anli)
    RelativeLayout fmShoucangAnli;
    @BindView(R.id.fm_guanfang_weixin)
    RelativeLayout fmGuanfangWeixin;
    @BindView(R.id.fm_kefu_dianhua)
    RelativeLayout fmKefuDianhua;
    @BindView(R.id.fm_shezhi)
    RelativeLayout fmShezhi;

    @BindView(R.id.fm_co_icon)
    ImageView fmCoIcon;//公司图标
    @BindView(R.id.fm_co_name)
    TextView fmCoName;//公司名称
    @BindView(R.id.fm_co_address)
    TextView fmCoAddress;//公司地址
    @BindView(R.id.fm_co_info)
    LinearLayout fmCoInfo;//整个公司信息层
    @BindView(R.id.fm_user_sign)
    TextView fmUserSign;
    Unbinder unbinder;


    private Context mContext;
    private String TAG = "MineFragment";
    private Gson mGson;

    public MineFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        mGson = new Gson();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utils.userIsLogin(mContext)) {
            //更新用户的信息
            if (SpUtils.getUserType(mContext).equals("3")) {
                //装修公司的界面
                fmCoInfo.setVisibility(View.VISIBLE);
                fmUserName.setVisibility(View.GONE);
                fmUserIcon.setVisibility(View.INVISIBLE);
                fmUserIcon.setClickable(false);
                fmUserSign.setVisibility(View.GONE);
            } else {
                //业主的页面
                fmCoInfo.setVisibility(View.GONE);
                fmUserName.setVisibility(View.VISIBLE);
                fmUserIcon.setVisibility(View.VISIBLE);
                fmUserSign.setVisibility(View.VISIBLE);
                fmUserIcon.setClickable(true);
            }
            HttpGetMineInfo();
        } else {
            //当前没有用户登录过显示默认
            fmUserSign.setVisibility(View.GONE);
            fmCoInfo.setVisibility(View.GONE);
            fmUserName.setVisibility(View.VISIBLE);
            fmUserIcon.setVisibility(View.VISIBLE);
            fmUserIcon.setClickable(true);
            fmUserName.setText("未登录");
            fmUserIcon.setImageResource(R.mipmap.weidenglu);
            fmTumiNum.setText("0");
            fmTuyouNum.setText("0");
            fmDongtaiNum.setText("0");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fm_head_bg, R.id.fm_user_icon, R.id.fm_dongtai_ll, R.id.fm_tumi_ll,
            R.id.fm_tuyou_ll, R.id.fm_sousuoguo_anli, R.id.fm_guanzhu_shejishi,
            R.id.fm_shoucang_xiaoguotu, R.id.fm_shoucang_anli, R.id.fm_guanfang_weixin,
            R.id.fm_kefu_dianhua, R.id.fm_shezhi, R.id.fm_co_icon})
    public void onViewClickedInMineFragment(View view) {
        switch (view.getId()) {
            case R.id.fm_head_bg:
                //点击更换背景
                break;
            case R.id.fm_user_icon:
                //TODO 判断用户登录的情况   用户头像点击事件
                if (Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, PersonInfoActivity.class);
                    intent.putExtra("from", "MineFragment");
                    mContext.startActivity(intent);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.fm_co_icon:
                //TODO 判断用户登录的情况   用户头像点击事件
                if (Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, PersonInfoActivity.class);
                    intent.putExtra("from", "MineFragment");
                    mContext.startActivity(intent);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.fm_dongtai_ll:
                //进入用户的动态

                break;
            case R.id.fm_tumi_ll:
                //进入我的图谜
                if (Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, MyFansActivity.class);
                    mContext.startActivity(intent);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.fm_tuyou_ll:
                //点击进入我的图友
                if (Utils.userIsLogin(mContext)) {
                    Intent intent=new Intent(mContext, MyFriendsActivity.class);
                    mContext.startActivity(intent);
                }else {
                    gotoLogin();
                }
                break;
            case R.id.fm_sousuoguo_anli:
                //点击展开搜索过的案例
                break;
            case R.id.fm_guanzhu_shejishi:
                //点击进入关注的设计师
                if(Utils.userIsLogin(mContext)){
                    mContext.startActivity(new Intent(mContext, MyDesignerListActivity.class));
                }else {
                    Utils.gotoLogin(mContext);
                }

                break;
            case R.id.fm_shoucang_xiaoguotu:
                //点击进入收藏的效果图
                if(Utils.userIsLogin(mContext)){
                    mContext.startActivity(new Intent(mContext, MySampleListActivity.class));
                }else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.fm_shoucang_anli:
                //点击进入收藏的案例
                if(Utils.userIsLogin(mContext)){
//                    mContext.startActivity(new Intent(mContext, MyCaseListActivity.class));
                }else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.fm_guanfang_weixin:
                //分享官方微信
                Intent intentToweixin = new Intent(mContext, ShareWeixinActivity.class);
                mContext.startActivity(intentToweixin);
                break;
            case R.id.fm_kefu_dianhua:
                //开启电话
                call("4006062221");
                break;
            case R.id.fm_shezhi:
                //进入系统设置
                Intent intent = new Intent(mContext, SystemActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }

    //拨打客服电话
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //去登录页面
    private void gotoLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    //获取数据
    private void HttpGetMineInfo() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        HttpUtils.doPost(UrlConstans.MINE_HOME_PAGE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //成功
                        String data = jsonObject.getString("data");
                        final _HomePage homePage = mGson.fromJson(data, _HomePage.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initView(homePage);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //布局
    private void initView(_HomePage homePage) {
        //昵称
        fmUserName.setText(homePage.getNick());//业主
        fmCoName.setText(homePage.getNick());//公司
        //头像
        GlideUtils.glideLoader(mContext, homePage.getIcon(), 0, 0, fmUserIcon, 0);//业主
        GlideUtils.glideLoader(mContext, homePage.getIcon(), 0, 0, fmCoIcon);
        //业主个性签名
        if (TextUtils.isEmpty(homePage.getPersonal_signature())) {
            fmUserSign.setText("未填写");
        } else {
            fmUserSign.setText(homePage.getPersonal_signature());
        }
        //动态数
        fmDongtaiNum.setText(homePage.getDynamic_count());
        //图谜数
        fmTumiNum.setText(homePage.getFollowed_count());
        //图友
        fmTuyouNum.setText(homePage.getFollow_count());
        //公司的地址
        fmCoAddress.setText(homePage.getProvince_name() + homePage.getCity_name() + homePage.getAddress());

    }
}
