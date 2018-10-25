package com.tbs.tobosutype.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewMineNoneLoginActivity extends BaseActivity {
    @BindView(R.id.none_login_icon)
    ImageView noneLoginIcon;
    @BindView(R.id.none_login_name)
    TextView noneLoginName;
    @BindView(R.id.none_login_icon_right)
    ImageView noneLoginIconRight;
    @BindView(R.id.none_login_card)
    RelativeLayout noneLoginCard;
    @BindView(R.id.none_login_rl)
    RelativeLayout noneLoginRl;
    @BindView(R.id.none_login_kefu_rl)
    RelativeLayout noneLoginKefuRl;
    @BindView(R.id.none_login_fuwuhao_rl)
    RelativeLayout noneLoginFuwuhaoRl;
    @BindView(R.id.none_login_fenxiang_rl)
    RelativeLayout noneLoginFenxiangRl;
    @BindView(R.id.none_login_pingjia_rl)
    RelativeLayout noneLoginPingjiaRl;
    @BindView(R.id.none_login_shezhi_rl)
    RelativeLayout noneLoginShezhiRl;
    @BindView(R.id.none_login_kefu_num_tv)
    TextView noneLoginKefuNumTv;
    @BindView(R.id.none_login_fuwuhao_tv)
    TextView noneLoginFuwuhaoTv;
    @BindView(R.id.none_login_ll)
    LinearLayout noneLoginLl;
    private Context mContext;
    private String TAG = "NewMineNoneLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mine_none_login);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
//        umShareAPI = UMShareAPI.get(mContext);
        noneLoginKefuNumTv.setText("" + SpUtil.getCustom_service_tel(mContext));
        noneLoginFuwuhaoTv.setText("" + SpUtil.getPublic_number(mContext));
    }

    @Override
    protected void onResume() {
        super.onResume();
        noneLoginLl.setBackgroundColor(Color.parseColor("#ffffff"));

    }

    @OnClick({R.id.none_login_rl, R.id.none_login_kefu_rl, R.id.none_login_fuwuhao_rl,
            R.id.none_login_fenxiang_rl, R.id.none_login_pingjia_rl, R.id.none_login_shezhi_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.none_login_rl:
                Intent intentToRegister = new Intent(mContext, NewLoginActivity.class);
                intentToRegister.putExtra("mWhereComeFrom", "NoneLoginOfMineActivity");
                mContext.startActivity(intentToRegister);
                break;
            case R.id.none_login_kefu_rl:
                showKefuPopWindow();
                break;
            case R.id.none_login_fuwuhao_rl:
                copyWeChat();
                break;
            case R.id.none_login_fenxiang_rl:
                shareAppToFriend();
                break;
            case R.id.none_login_pingjia_rl:
                praiseApp();
                break;
            case R.id.none_login_shezhi_rl:
                //跳转到设置
                startActivity(new Intent(mContext, NewSettingActivity.class));
                break;
        }
    }

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

    //复制微信号
    private void copyWeChat() {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("itobosu");
        ToastUtil.showShort(this, "已复制微信服务号");
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
