package com.tbs.tbs_mj.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean._PresonerInfo;
import com.tbs.tbs_mj.customview.CustomDialog;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.MyApplication;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.CacheManager;
import com.tbs.tbs_mj.utils.GlideUtils;
import com.tbs.tbs_mj.utils.MD5Util;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.ToastUtil;
import com.tbs.tbs_mj.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 装修公司端 我的界面
 * 3.7版本新增
 */
public class CompanyOfMineActivity extends BaseActivity {
    @BindView(R.id.company_of_mine_setting_rl)
    RelativeLayout companyOfMineSettingRl;
    @BindView(R.id.company_of_mine_icon_iv)
    ImageView companyOfMineIconIv;
    @BindView(R.id.company_of_mine_name_tv)
    TextView companyOfMineNameTv;
    @BindView(R.id.company_of_mine_huiyuan)
    ImageView companyOfMineHuiyuan;
    @BindView(R.id.company_of_mine_msg_rl)
    RelativeLayout companyOfMineMsgRl;
    @BindView(R.id.company_of_mine_dingdan_num)
    TextView companyOfMineDingdanNum;
    @BindView(R.id.company_of_mine_none_dingdan_tv)
    TextView companyOfMineNoneDingdanTv;
    @BindView(R.id.company_of_mine_weiliangfang_ll)
    LinearLayout companyOfMineWeiliangfangLl;
    @BindView(R.id.company_of_mine_yiliangfang_ll)
    LinearLayout companyOfMineYiliangfangLl;
    @BindView(R.id.company_of_mine_all_dingdan_rl)
    RelativeLayout companyOfMineAllDingdanRl;
    @BindView(R.id.company_of_mine_wangdian_rl)
    RelativeLayout companyOfMineWangdianRl;
    @BindView(R.id.company_of_mine_kefu_rl)
    RelativeLayout companyOfMineKefuRl;
    @BindView(R.id.company_of_mine_xiaochengxu_rl)
    RelativeLayout companyOfMineXiaochengxuRl;
    @BindView(R.id.company_of_mine_share_rl)
    RelativeLayout companyOfMineShareRl;
    @BindView(R.id.company_of_mine_pingjia_rl)
    RelativeLayout companyOfMinePingjiaRl;
    @BindView(R.id.company_of_mine_all_dingdan_num_rl)
    RelativeLayout companyOfMineAllDingdanNumRl;
    @BindView(R.id.company_of_mine_ll)
    LinearLayout companyOfMineLl;
    @BindView(R.id.xindingdan_num_tv)
    TextView xindingdanNumTv;
    @BindView(R.id.weiliangfang_num_tv)
    TextView weiliangfangNumTv;
    @BindView(R.id.yiliangfang_num_tv)
    TextView yiliangfangNumTv;
    @BindView(R.id.company_of_mine_note_iv)
    ImageView companyOfMineNoteIv;
    @BindView(R.id.company_of_mine_note_rl)
    RelativeLayout companyOfMineNoteRl;
    @BindView(R.id.company_of_mine_xindingdan_ll)
    LinearLayout companyOfMineXindingdanLl;
    @BindView(R.id.company_of_mine_xiaochengxu_tv)
    TextView companyOfMineXiaochengxuTv;
    @BindView(R.id.company_of_mine_kefu_phone_tv)
    TextView companyOfMineKefuPhoneTv;
    private String TAG = "CompanyOfMineActivity";
    private Context mContext;
    private UMShareAPI umShareAPI;
    private _PresonerInfo mPresonerInfo;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_of_mine);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: 2018/3/22  时时修改用户的信息   新增订单的数量
        companyOfMineLl.setBackgroundColor(Color.parseColor("#ffffff"));
        if (Util.isNetAvailable(mContext)) {
            //有网络的情况下
            HttpGetUserInfo();
        } else {
            //没有网络的情况下
            initUserInfoInNotNet();
        }
    }

    //在有网络的情况下获取网络的信息
    private void HttpGetUserInfo() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        param.put("version", AppInfoUtil.getAppVersionName(mContext));
        OKHttpUtil.post(Constant.USER_INFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败~", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接服务器成功===============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.optString("data");
                        mPresonerInfo = mGson.fromJson(data, _PresonerInfo.class);
                        //信息存本地
                        AppInfoUtil.setUserNickname(mContext, mPresonerInfo.getNickname());//昵称
                        AppInfoUtil.setId(mContext, mPresonerInfo.getId());//id
                        AppInfoUtil.setUserIcon(mContext, mPresonerInfo.getIcon());//用户的icon
                        AppInfoUtil.setMark(mContext, mPresonerInfo.getUser_type());//用户的身份标识
                        AppInfoUtil.setUserid(mContext, mPresonerInfo.getUid());//用户的id
                        AppInfoUtil.setToken(mContext, mPresonerInfo.getToken());//token
                        AppInfoUtil.setUserCity(mContext, mPresonerInfo.getCity_name());//用户所在的城市
                        AppInfoUtil.setUserProvince(mContext, mPresonerInfo.getProvince_name());//用户所在的省份
                        AppInfoUtil.setUuid(mContext, mPresonerInfo.getId());//用户的id 本质和id一致
                        AppInfoUtil.setTypeid(mContext, mPresonerInfo.getUser_type());//用户的身份标识
                        AppInfoUtil.setUserGrade(mContext, mPresonerInfo.getGrade());//用户的会员等级
                        AppInfoUtil.setUserCellphone_check(mContext, mPresonerInfo.getCellphone_check());//用户是否绑定手机号码
                        AppInfoUtil.setUserOrder_count(mContext, mPresonerInfo.getOrder_count());//用户订单数量

                        AppInfoUtil.setUserNewOrderCount(mContext, mPresonerInfo.getNew_order_count() + "");//用户新订单数量
                        AppInfoUtil.setUserNotLfOrderCount(mContext, mPresonerInfo.getNot_lf_order_count() + "");//用户未量房数量
                        AppInfoUtil.setUserLfOrderCount(mContext, mPresonerInfo.getLf_order_count() + "");//用户量房数量
                        AppInfoUtil.setUserIsNewSms(mContext, mPresonerInfo.getIs_new_sms() + "");//用户是否有新消息  1-有  0-无

                        CacheManager.setDecorateBudget(mContext, mPresonerInfo.getExpected_cost());//装修日志的花费
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initUserInfoInNotNet();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initUserInfoInNotNet();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //在没有网络的情况下获取本地的数据
    private void initUserInfoInNotNet() {
        //用户的头像
        GlideUtils.glideLoader(mContext,
                AppInfoUtil.getUserIcon(mContext),
                R.drawable.iamge_loading,
                R.drawable.iamge_loading, companyOfMineIconIv, 0);
        //昵称
        companyOfMineNameTv.setText("" + AppInfoUtil.getUserNickname(mContext));
        //会员等级
        if (AppInfoUtil.getUserGrade(mContext).equals("0")) {
            //业主或者非会员 不显示 会员的等级
            companyOfMineHuiyuan.setVisibility(View.GONE);
        } else if (AppInfoUtil.getUserGrade(mContext).equals("1")) {
            //普通会员
            companyOfMineHuiyuan.setImageResource(R.drawable.huiyuan);
        } else if (AppInfoUtil.getUserGrade(mContext).equals("2")) {
            //初级会员
            companyOfMineHuiyuan.setImageResource(R.drawable.chuji);
        } else if (AppInfoUtil.getUserGrade(mContext).equals("3")) {
            //高级会员
            companyOfMineHuiyuan.setImageResource(R.drawable.gaoji);
        } else if (AppInfoUtil.getUserGrade(mContext).equals("4")) {
            //钻石会员
            companyOfMineHuiyuan.setImageResource(R.drawable.zuanshi);
        } else if (AppInfoUtil.getUserGrade(mContext).equals("5")) {
            //皇冠会员
            companyOfMineHuiyuan.setImageResource(R.drawable.huangguan);
        }
        //订单数
        companyOfMineDingdanNum.setText("" + AppInfoUtil.getUserOrder_count(mContext));
        if (AppInfoUtil.getUserOrder_count(mContext).equals("0")) {
            //用户的订单数为0隐藏查单的入口
            companyOfMineAllDingdanRl.setVisibility(View.GONE);
            companyOfMineNoneDingdanTv.setVisibility(View.VISIBLE);
        } else {
            companyOfMineAllDingdanRl.setVisibility(View.VISIBLE);
            companyOfMineNoneDingdanTv.setVisibility(View.GONE);
        }
        //设置新订单数据
        if (AppInfoUtil.getUserNewOrderCount(mContext).equals("0")) {
            xindingdanNumTv.setVisibility(View.GONE);
        } else {
            xindingdanNumTv.setVisibility(View.VISIBLE);
            int xindindanNum = Integer.parseInt(AppInfoUtil.getUserNewOrderCount(mContext));
            if (xindindanNum > 99) {
                xindingdanNumTv.setText("" + "99+");
            } else {
                xindingdanNumTv.setText("" + AppInfoUtil.getUserNewOrderCount(mContext));
            }
        }
        //设置未量房数据
        if (AppInfoUtil.getUserNotLfOrderCount(mContext).equals("0")) {
            weiliangfangNumTv.setVisibility(View.GONE);
        } else {
            weiliangfangNumTv.setVisibility(View.VISIBLE);
            int weiliangfangNum = Integer.parseInt(AppInfoUtil.getUserNotLfOrderCount(mContext));
            if (weiliangfangNum > 99) {
                weiliangfangNumTv.setText("" + "99+");
            } else {
                weiliangfangNumTv.setText("" + AppInfoUtil.getUserNotLfOrderCount(mContext));
            }
        }
        //设置已量房数据
        if (AppInfoUtil.getUserLfOrderCount(mContext).equals("0")) {
            yiliangfangNumTv.setVisibility(View.GONE);
        } else {
            yiliangfangNumTv.setVisibility(View.VISIBLE);
            int yiliangfangNum = Integer.parseInt(AppInfoUtil.getUserLfOrderCount(mContext));
            if (yiliangfangNum > 99) {
                yiliangfangNumTv.setText("" + "99+");
            } else {
                yiliangfangNumTv.setText("" + AppInfoUtil.getUserLfOrderCount(mContext));
            }
        }
        //设置是否有新消息
        if (AppInfoUtil.getUserIsNewSms(mContext).equals("1")) {
            companyOfMineNoteIv.setImageResource(R.drawable.xiaoxi_2);
        } else {
            companyOfMineNoteIv.setImageResource(R.drawable.xiaoxi);
        }
    }

    private void initViewEvent() {
        mGson = new Gson();
        umShareAPI = UMShareAPI.get(mContext);
        companyOfMineXiaochengxuTv.setText("" + SpUtil.getApplets_name(mContext));
        companyOfMineKefuPhoneTv.setText("" + SpUtil.getCustom_service_tel(mContext));
    }

    @OnClick({R.id.company_of_mine_setting_rl, R.id.company_of_mine_icon_iv,
            R.id.company_of_mine_msg_rl, R.id.company_of_mine_weiliangfang_ll,
            R.id.company_of_mine_yiliangfang_ll, R.id.company_of_mine_xindingdan_ll,
            R.id.company_of_mine_wangdian_rl, R.id.company_of_mine_kefu_rl,
            R.id.company_of_mine_xiaochengxu_rl, R.id.company_of_mine_share_rl,
            R.id.company_of_mine_pingjia_rl, R.id.company_of_mine_all_dingdan_num_rl,
            R.id.company_of_mine_note_rl})
    public void onViewClickedInCompanyOfMineActivity(View view) {
        switch (view.getId()) {
            case R.id.company_of_mine_setting_rl:
                //点击了设置按钮
                startActivity(new Intent(mContext, NewSettingActivity.class));
                break;
            case R.id.company_of_mine_icon_iv:
            case R.id.company_of_mine_msg_rl:
                //整个装修公司的图层
                startActivity(new Intent(mContext, CoPresonerMsgActivity.class));
                break;
            case R.id.company_of_mine_all_dingdan_num_rl:
                //所有订单的栏
                if (!MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                    showChadanPassWordPop("0");
                } else {
                    //进入所有订单
                    startActivity(new Intent(mContext, AllOrderActivity.class));
                }
                break;
            case R.id.company_of_mine_weiliangfang_ll:
                //未量房按钮
                if (!MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                    showChadanPassWordPop("2");
                } else {
                    //进入未量房界面
                    goAllOrder("2");
                }
                break;
            case R.id.company_of_mine_yiliangfang_ll:
                //已量房按钮
                if (!MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                    showChadanPassWordPop("3");
                } else {
                    //进入已量房界面
                    goAllOrder("3");
                }
                break;
            case R.id.company_of_mine_xindingdan_ll:
                //新订单按钮
                if (!MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                    showChadanPassWordPop("1");
                } else {
                    //进入新订单界面
                    goAllOrder("1");
                }
                break;
            case R.id.company_of_mine_wangdian_rl:
                //网店设置
                startActivity(new Intent(mContext, OnlineStoreActivity.class));
                break;
            case R.id.company_of_mine_kefu_rl:
                //客服
                showKefuPopWindow();
                break;
            case R.id.company_of_mine_xiaochengxu_rl:
                //复制小程序
                copyWeChat();
                break;
            case R.id.company_of_mine_share_rl:
                //分享我们的App
                shareAppToFriend();
                break;
            case R.id.company_of_mine_pingjia_rl:
                //评价App
                praiseApp();
                break;
            case R.id.company_of_mine_note_rl:
                // TODO: 2018/3/22 点击进入消息界面
                Intent intent = new Intent(mContext, OrderNoteActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void goAllOrder(String kind) {
//        Intent allOrderIntent = new Intent(mContext, AllOrderListActivity.class);
//        Bundle allOrderBundle = new Bundle();
//        allOrderBundle.putString("kind", kind);
//        allOrderIntent.putExtras(allOrderBundle);
//        mContext.startActivity(allOrderIntent);
        Intent intentIntoAllOrderActivity = new Intent(mContext, AllOrderActivity.class);
        intentIntoAllOrderActivity.putExtra("mIndex", kind);
        startActivity(intentIntoAllOrderActivity);
    }

    /**
     * 订单输入密码的弹窗
     * 0--全部订单
     * 1--已量房
     * 2--未量房
     * 3--已签单
     */

    private void showChadanPassWordPop(final String kind) {
        /**
         * 用户输入了密码之后 在App没有kill 是可以直接打开订单查询的
         */
        View popview = View.inflate(mContext, R.layout.pop_check_pass_word, null);
        TextView pop_check_pw_quxiao = (TextView) popview.findViewById(R.id.pop_check_pw_quxiao);
        TextView pop_check_pw_ok = (TextView) popview.findViewById(R.id.pop_check_pw_ok);
        final EditText pop_check_pw_edit = popview.findViewById(R.id.pop_check_pw_edit);
        //全局蒙层
        RelativeLayout phone_pop_window_rl = (RelativeLayout) popview.findViewById(R.id.phone_pop_window_rl);
        //白色显示层
        RelativeLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //点击确认按钮
        pop_check_pw_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pop_check_pw_edit.getText().toString())) {
                    Toast.makeText(mContext, "请输入查单密码~", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> param = new HashMap<>();
                    param.put("token", Util.getDateToken());
                    param.put("id", AppInfoUtil.getUuid(mContext));
                    param.put("password", MD5Util.md5(pop_check_pw_edit.getText().toString()));
                    OKHttpUtil.post(Constant.CHECK_ORDER_PWD, param, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "链接服务器失败~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = new String(response.body().string());
                            Log.e(TAG, "验证订单密码链接成功=============" + json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String status = jsonObject.optString("status");
                                final String msg = jsonObject.optString("msg");
                                if (status.equals("200")) {
                                    //验证密码成功 跳转查单页
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD = true;
                                            if (kind.equals("0")) {
                                                //全部
                                                goAllOrder("0");
                                            } else if (kind.equals("1")) {
                                                //新订单
                                                goAllOrder("1");
                                            } else if (kind.equals("2")) {
                                                //未量房
                                                goAllOrder("2");
                                            } else if (kind.equals("3")) {
                                                //已量房
                                                goAllOrder("3");
                                            } else {
                                                goAllOrder("0");
                                            }
                                            popupWindow.dismiss();
                                        }
                                    });

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        //取消
        pop_check_pw_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        phone_pop_window_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    //开启客服电话
    private void showKefuPopWindow() {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView phone_num = (TextView) popview.findViewById(R.id.phone_num);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        LinearLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        phone_num.setText("" + "土拨鼠热线：" + SpUtil.getCustom_service_tel(mContext));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //打电话
        open_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + SpUtil.getCustom_service_tel(mContext)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        pop_phone_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    //复制微信号
    private void copyWeChat() {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("土拨鼠查订单");
        ToastUtil.showShort(this, "已复制，请到微信查看小程序");
    }

    //进行App的评价
    private void praiseApp() {
        try {
            String mAddress = "market://details?id=" + getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            startActivity(marketIntent);
        } catch (Exception e) {
            Toast.makeText(this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //进行App的分享
    private void shareAppToFriend() {

        Log.e(TAG, "点击了分享按钮。。");
        UMWeb web = new UMWeb("http://a.app.qq.com/o/simple.jsp?pkgname=com.tbs.tobosutype");
        web.setTitle("土拨鼠，用心装好家，专注做装修。");
        web.setThumb(new UMImage(mContext, R.drawable.app_icon));
        web.setDescription("一款装修神器App，轻松搞定装修大小事~");
        new ShareAction(this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("你确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //退出前将数据上传
                                    Util.HttpPostUserUseInfo();
                                    finish();
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("再看看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
