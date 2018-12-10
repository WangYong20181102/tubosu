package com.tbs.tobosutype.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.MessageCenterAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean._PresonerInfo;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;
import com.umeng.socialize.ShareAction;
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

public class NewMineActivity extends BaseActivity {
    @BindView(R.id.new_mine_icon_img)
    ImageView newMineIconImg;
    @BindView(R.id.new_mine_name_tv)
    TextView newMineNameTv;
    @BindView(R.id.new_mine_address_tv)
    TextView newMineAddressTv;
    @BindView(R.id.new_mine_next_img)
    ImageView newMineNextImg;
    @BindView(R.id.new_mine_shoucang_ll)
    LinearLayout newMineShoucangLl;
    @BindView(R.id.new_mine_reply_ll)
    LinearLayout newMineReplyLl;
    @BindView(R.id.new_mine_message_ll)
    LinearLayout newMineMessageLl;
    @BindView(R.id.new_mine_kefu_num_tv)
    TextView newMineKefuNumTv;
    @BindView(R.id.new_mine_kefu_rl)
    RelativeLayout newMineKefuRl;
    @BindView(R.id.new_mine_fuwuhao_num_tv)
    TextView newMineFuwuhaoNumTv;
    @BindView(R.id.new_mine_fuwuhao_rl)
    RelativeLayout newMineFuwuhaoRl;
    @BindView(R.id.new_mine_fenxiang_rl)
    RelativeLayout newMineFenxiangRl;
    @BindView(R.id.new_mine_pingjia_rl)
    RelativeLayout newMinePingjiaRl;
    @BindView(R.id.new_mine_shezhi_rl)
    RelativeLayout newMineShezhiRl;
    @BindView(R.id.new_mine_ll)
    LinearLayout newMineLl;
    @BindView(R.id.rl_unlogin)
    RelativeLayout rlUnLogin;
    @BindView(R.id.rl_login)
    RelativeLayout rlLogin;
    @BindView(R.id.message_red_dot)
    View messageRedDot; //消息红点
    private Context mContext;
    private String TAG = "NewMineActivity";
    private Gson mGson;
    private _PresonerInfo mPresonerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mine);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mGson = new Gson();
        newMineFuwuhaoNumTv.setText("" + SpUtil.getPublic_number(mContext));
        newMineKefuNumTv.setText("" + SpUtil.getCustom_service_tel(mContext));
    }


    @Override
    protected void onResume() {
        super.onResume();
        newMineLl.setBackgroundColor(Color.parseColor("#ffffff"));
//        Log.e(TAG, "数据更新==========onResume======");
        if (AppInfoUtil.getMark(mContext).equals("0")) {
            rlUnLogin.setVisibility(View.VISIBLE);
            rlLogin.setVisibility(View.GONE);
        } else {
            rlUnLogin.setVisibility(View.GONE);
            rlLogin.setVisibility(View.VISIBLE);
        }
        if (Util.isNetAvailable(mContext)) {
            //有网
            HttpGetUserInfo();
        } else {
            //无网
            initUserInfoInNotNet();
        }
        if (AppInfoUtil.getHotDot(mContext).equals("0")){
            messageRedDot.setVisibility(View.VISIBLE);
        }else {
            messageRedDot.setVisibility(View.GONE);
        }
    }

    //没有网络的情况下
    private void initUserInfoInNotNet() {
        //用户的头像
        GlideUtils.glideLoader(mContext,
                AppInfoUtil.getUserIcon(mContext),
                R.drawable.iamge_loading, R.drawable.iamge_loading,
                newMineIconImg, 0);
        //用户的昵称
        newMineNameTv.setText("" + AppInfoUtil.getUserNickname(mContext));
        //地址
        newMineAddressTv.setText("" + AppInfoUtil.getUserProvince(mContext) + " " + AppInfoUtil.getUserCity(mContext));

    }

    //在有网络的情况下获取网络的信息
    private void HttpGetUserInfo() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        param.put("version", AppInfoUtil.getAppVersionName(mContext));
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
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
//                Log.e(TAG, "链接服务器成功===============" + json);
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


    @OnClick({R.id.new_mine_icon_img, R.id.new_mine_next_img, R.id.new_mine_shoucang_ll,
            R.id.new_mine_reply_ll, R.id.new_mine_message_ll, R.id.new_mine_kefu_rl,
            R.id.new_mine_fuwuhao_rl, R.id.new_mine_fenxiang_rl, R.id.new_mine_pingjia_rl,
            R.id.new_mine_shezhi_rl, R.id.rl_unlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_mine_icon_img:
            case R.id.new_mine_next_img:
                startActivity(new Intent(mContext, PresonerMsgActivity.class));
                break;
            case R.id.new_mine_shoucang_ll:
                Intent it = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    it = new Intent(mContext, ShoucangAcitivity.class);
                } else {
                    it = new Intent(mContext, MyFavActivity.class);
                }
                startActivity(it);
                break;
            case R.id.new_mine_reply_ll:    //我的提问
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(this))) {
                    Toast.makeText(this, "您还没有登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, NewLoginActivity.class);
                    startActivityForResult(intent, 0);
                    return;
                }
                startActivity(new Intent(mContext,MyReplyActivity.class));
                break;
            case R.id.new_mine_message_ll:  //消息中心
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(this))) {
                    Toast.makeText(this, "您还没有登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, NewLoginActivity.class);
                    startActivityForResult(intent, 0);
                    return;
                }
                //点击消息中心隐藏红点显示
                MainActivity.showOrHideHotDot("1"); //调用mainactivity方法，隐藏我的底部红点
                AppInfoUtil.setHotDot(mContext,"1");    //改变本地消息红点状态
                startActivity(new Intent(mContext,MessageCenterActivity.class));
                break;
            case R.id.new_mine_kefu_rl:
                //开启客服电话
//                showKefuPopWindow();
                Intent intent = new Intent(NewMineActivity.this, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", "https://webchat.7moor.com/wapchat.html?accessId=0e1ca6b0-ec8e-11e8-a1ba-07d6a6237cdc");
                intent.putExtra("bAnswer",true);
                startActivity(intent);
                break;
            case R.id.new_mine_fuwuhao_rl:
                //复制服务号
                copyWeChat();
                break;
            case R.id.new_mine_fenxiang_rl:
                //分享App
                shareAppToFriend();
                break;
            case R.id.new_mine_pingjia_rl:
                //进入App评价
                praiseApp();
                break;
            case R.id.new_mine_shezhi_rl:
                startActivity(new Intent(mContext, NewSettingActivity.class));
                break;
            case R.id.rl_unlogin:
                Intent intentToRegister = new Intent(mContext, NewLoginActivity.class);
                intentToRegister.putExtra("mWhereComeFrom", "NoneLoginOfMineActivity");
                startActivity(intentToRegister);
                break;
        }
    }


    //开启客服电话
    private void showKefuPopWindow() {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        LinearLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
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
        cmb.setText("" + SpUtil.getPublic_number(mContext));
        ToastUtil.showShort(this, "已复制微信公众号");
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
