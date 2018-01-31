package com.tbs.tobosutype.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户未登录界面
 * 3.7版本新增
 * 整个页面纯展示
 */
public class NoneLoginOfMineActivity extends AppCompatActivity {
    @BindView(R.id.none_login_setting)
    ImageView noneLoginSetting;
    @BindView(R.id.none_login_register_rl)
    RelativeLayout noneLoginRegisterRl;
    @BindView(R.id.none_login_goto_login_rl)
    RelativeLayout noneLoginGotoLoginRl;
    @BindView(R.id.none_login_dalibao_iv)
    ImageView noneLoginDalibaoIv;
    @BindView(R.id.none_login_kefu_rl)
    RelativeLayout noneLoginKefuRl;
    @BindView(R.id.none_login_fuwuhao_rl)
    RelativeLayout noneLoginFuwuhaoRl;
    @BindView(R.id.none_login_share_rl)
    RelativeLayout noneLoginShareRl;
    @BindView(R.id.none_login_pingjia_rl)
    RelativeLayout noneLoginPingjiaRl;
    @BindView(R.id.none_login_setting_rl)
    RelativeLayout noneLoginSettingRl;
    @BindView(R.id.none_login_company_of_mine_all_ll)
    LinearLayout noneLoginCompanyOfMineAllLl;
    private String TAG = "NoneLoginOfMineActivity";
    private Context mContext;
    private UMShareAPI umShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_none_login_of_mine);
        ButterKnife.bind(this);
        Log.e(TAG, "初始化================" + TAG);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected void onResume() {
        noneLoginCompanyOfMineAllLl.setBackgroundColor(Color.parseColor("#ffffff"));
        super.onResume();
    }

    private void initViewEvent() {
        umShareAPI = UMShareAPI.get(mContext);
    }

    @OnClick({R.id.none_login_setting, R.id.none_login_register_rl,
            R.id.none_login_goto_login_rl, R.id.none_login_dalibao_iv,
            R.id.none_login_kefu_rl, R.id.none_login_fuwuhao_rl,
            R.id.none_login_share_rl, R.id.none_login_pingjia_rl, R.id.none_login_setting_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.none_login_setting:
            case R.id.none_login_setting_rl:
                //跳转到设置
                startActivity(new Intent(mContext, NewSettingActivity.class));
                break;
            case R.id.none_login_register_rl:
            case R.id.none_login_goto_login_rl:
                //跳转到注册界面
                //跳转到登录页面
                Intent intentToRegister = new Intent(mContext, NewLoginActivity.class);
                intentToRegister.putExtra("mWhereComeFrom", "NoneLoginOfMineActivity");
                mContext.startActivity(intentToRegister);
                break;
            case R.id.none_login_dalibao_iv:
                //点击大礼包发单条 跳转到发单页面
                Intent intentToWeb = new Intent(mContext, NewWebViewActivity.class);
                // TODO: 2018/1/11 发单地址
                intentToWeb.putExtra("mLoadingUrl", Constant.LINK_HOME_DALIBAO);
                mContext.startActivity(intentToWeb);
                break;
            case R.id.none_login_kefu_rl:
                //开启客服电话
                showKefuPopWindow();
                break;
            case R.id.none_login_fuwuhao_rl:
                //复制服务服务号 在微信中开启
                copyWeChat();
                break;
            case R.id.none_login_share_rl:
                //分享App下载
                shareAppToFriend();
                break;
            case R.id.none_login_pingjia_rl:
                //进入应用市场评价我们的App
                praiseApp();
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
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4006062221"));
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
        cmb.setText("itobosu");
        ToastUtil.showShort(this, "已复制微信服务号");
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
