package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.customview.SelectPersonalPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.FileUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.Util;
import com.tbs.tobosutype.utils.WriteUtil;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGPushManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的页面【业主】
 *
 * @author dec
 */
public class MyOwnerActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = MyOwnerActivity.class.getSimpleName();
    private Context mContext;

    private String icon;
    private String nickname;
    private String token;

    /**
     * 用户头像
     */
    private ImageView my_owner_pic;

    /**
     * 用户信息布局
     */
    private RelativeLayout rel_my_owenr_user_layout;

    private TextView my_owner_name;
    private TextView tv_my_owner_allorder;
    private TextView tv_location;
    private TextView tv_addtime;
    private TextView tv_delstatus;
    private TextView tv_orderid;
    private TextView tv_icommunity;

    /**
     * 业主可查看装修公司的订单详情 <br/>
     * <p>
     * 订单状态
     */
    private TextView tv_delstatusDes;

    private TextView tv_order_now;
    private TextView tv_find_order;

    private LinearLayout myowner_layout_personal_data;

    /**
     * 我的收藏布局
     */
    private LinearLayout myowner_layout_store_pic;

    /**
     * 我要反馈布局
     */
    private LinearLayout my_layout_feedback;

//    /**消息*/
//    private LinearLayout my_layout_ower_msg;

    /**
     * 开支
     */
    private LinearLayout my_layout_ower_outcome;

    /**
     * 头像下面显示一些订单信息的布局
     */
    private LinearLayout linearlayout_have_order_information;

    /**
     * 暂时没有订单的布局
     */
    private LinearLayout ll_not_order_information;

    /**
     * 装修保障布局
     */
    private LinearLayout ll_decorate_security;

    private ImageView iv_myowner_set;

    private ProgressDialog pd;

    private SelectPersonalPopupWindow menuWindow;

    private static final int REQUESTCODE_PICK = 0;
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;

    /**
     * 头像存储的名称
     */
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";
    private String urlpath;
    private Bitmap photo = null;

    /*** 上传头像的接口*/
    private String imgUrl = Constant.TOBOSU_URL + "cloud/upload/upload_for_ke?";

    /***我的[业主]接口*/
    private String userMyUrl = Constant.TOBOSU_URL + "tapp/user/my";

    /***找回订单接口*/
    private String retrieveOrderUrl = Constant.TOBOSU_URL + "tapp/order/retrieveOrder";

    /***修改用户信息接口*/
    private String userChageInfoUrl = Constant.TOBOSU_URL + "tapp/user/chage_user_info";

    private String resultStr = "";

    private RequestParams userChageInfoParams;
    private RequestParams userMyParams;
    private RequestParams retrieveOrderParams;

    private String gender;
    private String icommunity;
    private String province;
    private String cityname;
    private String cellphone;
    private String lastOrderFlag;
    private String orderid;
    private String addtime;
    private String delstatus;
    private String delstatusDes;
    private String wechat_check;
    private String housename;
    private String position;
    private String clickOrNot;
    private String orderDetailUrl;
    private String viewcount;
    private ImageView iv_system_message_user;
    private TextView tv_not_see_sysmsg;
    private LinearLayout ll_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myowner);
        mContext = MyOwnerActivity.this;
        initReceiver();
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        rel_my_owenr_user_layout = (RelativeLayout) findViewById(R.id.rel_my_owenr_user_layout);
        my_owner_pic = (ImageView) findViewById(R.id.my_owner_pic);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        my_owner_name = (TextView) findViewById(R.id.my_owner_name);
        tv_not_see_sysmsg = (TextView) findViewById(R.id.tv_not_see_sysmsg);
        tv_icommunity = (TextView) findViewById(R.id.tv_icommunity);
        tv_addtime = (TextView) findViewById(R.id.tv_addtime_myowner_activity);
        tv_orderid = (TextView) findViewById(R.id.tv_orderid);
        tv_find_order = (TextView) findViewById(R.id.tv_find_order);
        tv_order_now = (TextView) findViewById(R.id.tv_order_now);
        tv_delstatus = (TextView) findViewById(R.id.tv_delstatus);
        tv_delstatusDes = (TextView) findViewById(R.id.tv_delstatusDes);
        tv_my_owner_allorder = (TextView) findViewById(R.id.tv_my_owner_allorder);
        tv_location = (TextView) findViewById(R.id.tv_location);
        myowner_layout_personal_data = (LinearLayout) findViewById(R.id.myowner_layout_personal_data);
        myowner_layout_store_pic = (LinearLayout) findViewById(R.id.myowner_layout_store_pic);
        linearlayout_have_order_information = (LinearLayout) findViewById(R.id.linearlayout_have_order_information);
        ll_not_order_information = (LinearLayout) findViewById(R.id.ll_not_order_information);
        my_layout_feedback = (LinearLayout) findViewById(R.id.my_layout_feedback);
//        my_layout_ower_msg = (LinearLayout) findViewById(R.id.my_layout_ower_msg);
        my_layout_ower_outcome = (LinearLayout) findViewById(R.id.my_layout_ower_outcome);
        iv_myowner_set = (ImageView) findViewById(R.id.iv_myowner_set);
        ll_decorate_security = (LinearLayout) findViewById(R.id.ll_decorate_security);
        iv_system_message_user = (ImageView) findViewById(R.id.iv_system_message_user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        token = AppInfoUtil.getToekn(getApplicationContext());
        String userid = AppInfoUtil.getUserid(getApplicationContext());
        String mark = AppInfoUtil.getMark(getApplicationContext());


        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        build.setSound(RingtoneManager.getActualDefaultRingtoneUri(
                mContext, RingtoneManager.TYPE_ALARM)) // 设置声音
                // setSound(
                // Uri.parse("android.resource://" + getPackageName()
                // + "/" + R.raw.wind)) 设定Raw下指定声音文件
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setFlags(Notification.FLAG_AUTO_CANCEL);
        // 设置自定义通知图片资源
        build.setLayoutIconDrawableId(R.drawable.app_icon);
        // 设置状态栏的通知小图标
        build.setIcon(R.drawable.app_icon);
        XGPushManager.setDefaultNotificationBuilder(mContext, build);


        requestUserMy();
        if (mark.equals("0")) {
            XGPushManager.setTag(this, "loginOff");
            XGPushManager.deleteTag(this, "loginOn");
            XGPushManager.deleteTag(this, "isNotCompany");
            XGPushManager.deleteTag(this, "isCompany");
        } else if (mark.equals("1")) {
            XGPushManager.deleteTag(this, "loginOff");
            XGPushManager.setTag(this, "loginOn");
            XGPushManager.setTag(this, "isNotCompany");

        } else if (mark.equals("3")) {
            if (!TextUtils.isEmpty(userid)) {
                XGPushManager.registerPush(getApplicationContext(), userid);
            }
            XGPushManager.deleteTag(this, "loginOff");
            XGPushManager.setTag(this, "loginOn");
            XGPushManager.setTag(this, "isCompany");
        }
    }

    private void initEvent() {
        rel_my_owenr_user_layout.setOnClickListener(this);
        tv_order_now.setOnClickListener(this);
        my_owner_pic.setOnClickListener(this);
        tv_my_owner_allorder.setOnClickListener(this);
        myowner_layout_personal_data.setOnClickListener(this);
        myowner_layout_store_pic.setOnClickListener(this);
        my_layout_feedback.setOnClickListener(this);
//        my_layout_ower_msg.setOnClickListener(this);
        my_layout_ower_outcome.setOnClickListener(this);
        iv_myowner_set.setOnClickListener(this);
        tv_find_order.setOnClickListener(this);
        tv_delstatusDes.setOnClickListener(this);
        linearlayout_have_order_information.setOnClickListener(this);
        ll_decorate_security.setOnClickListener(this);
        iv_system_message_user.setOnClickListener(this);
    }

    /***
     * 我的业主接口请求
     */
    private void requestUserMy() {
        userMyParams = AppInfoUtil.getPublicParams(getApplicationContext());
        userMyParams.put("token", token);

        HttpServer.getInstance().requestPOST(userMyUrl, userMyParams, new AsyncHttpResponseHandler() {

            private String sysmesscount;

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                String result = new String(body);
                System.out.println("MyOwnerActivity -- 请求结果" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("error_code") == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        icon = data.getString("icon");
                        nickname = data.getString("name");
                        gender = data.getString("gender");
                        icommunity = data.getString("icommunity");
                        province = data.getString("province");
                        cityname = data.getString("cityname");
                        sysmesscount = data.getString("sysmesscount");
                        //存电话号码
                        getSharedPreferences("userInfo", 0).edit().putString("cellphone", data.getString("cellphone")).commit();

                        if (Integer.parseInt(sysmesscount) > 0) {
                            tv_not_see_sysmsg.setVisibility(View.VISIBLE);
                            if (Integer.parseInt(sysmesscount) > 9) {
                                tv_not_see_sysmsg.setText("9+");
                            } else {
                                tv_not_see_sysmsg.setText(sysmesscount);
                            }
                        } else {
                            tv_not_see_sysmsg.setVisibility(View.GONE);
                        }
                        cellphone = data.getString("cellphone");
                        wechat_check = data.getString("wechat_check");
                        lastOrderFlag = data.getString("lastOrderFlag");
                        if (lastOrderFlag.equals("1")) {
                            JSONObject object = data.getJSONObject("lastOrderInfo");
                            orderid = object.getString("orderid");
                            addtime = object.getString("addtime");
                            delstatus = object.getString("delstatus");
                            delstatusDes = object.getString("delstatusDes");
                            housename = object.getString("housename");
                            viewcount = object.getString("viewcount");
                            position = object.getString("position");
                            clickOrNot = object.getString("clickOrNot");
//									if(clickOrNot.equals("1")) {
//										// 可点
                            orderDetailUrl = object.getString("orderDetailUrl");
//									}else if(clickOrNot.equals("0")){
//										//不可点
//										orderDetailUrl = "";
//									}
                        }
                    }
                    operView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

    /***
     * 设置用户信息
     */
    private void operView() {
        MyApplication.imageLoader.displayImage(icon, my_owner_pic, MyApplication.options);
        my_owner_name.setText(nickname);
        if (TextUtils.isEmpty(cityname)) {
            tv_location.setTextColor(getResources().getColor(R.color.color_red));
            tv_location.setText("城市未设置");
        } else {
            tv_location.setTextColor(getResources().getColor(R.color.white));
            tv_location.setText(province + " - " + cityname);
        }
        //FIXME 什么意思？
        if (lastOrderFlag.equals("1")) {

            //FIXME 显示红点
//			Drawable drawable = getResources().getDrawable(R.drawable.red_point_img);
//			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            //FIXME 什么意思？
//			if ("0".equals(viewcount)) {
//				tv_delstatus.setCompoundDrawables(drawable, null, null, null);
//			}
            tv_icommunity.setText("城市: " + position + "     小区:" + housename);
            tv_addtime.setText(addtime + " 预约");
            if ("已确认需求".equals(delstatus)) {
                tv_delstatus.setTextColor(Color.parseColor("#91D5FF"));
            }
            if ("预约装修".equals(delstatus)) {
                tv_delstatus.setTextColor(getResources().getColor(R.color.color_red));
            }

            tv_delstatus.setText(delstatus);
            tv_orderid.setText("订单号 : " + orderid);
            if (clickOrNot.equals("1")) {
                LayoutParams params = tv_delstatusDes.getLayoutParams();
                params.width = DensityUtil.dip2px(getApplicationContext(), 80);
                tv_delstatusDes.setLayoutParams(params);
                tv_delstatusDes.setClickable(true);
                tv_delstatusDes.setBackground(getResources().getDrawable(R.drawable.user_login_btn_background));
                tv_delstatusDes.setTextColor(getResources().getColor(R.color.white));
                if (delstatusDes.contains("评价")) {
                    tv_delstatusDes.setText("去评价");
                } else {
                    tv_delstatusDes.setText("查看");
                }
            } else {
                LayoutParams params = tv_delstatusDes.getLayoutParams();
                params.width = LayoutParams.WRAP_CONTENT;
                tv_delstatusDes.setLayoutParams(params);
                tv_delstatusDes.setClickable(false);
                tv_delstatusDes.setText(delstatusDes);
            }
            linearlayout_have_order_information.setVisibility(View.VISIBLE);
            ll_not_order_information.setVisibility(View.GONE);
        } else {
            linearlayout_have_order_information.setVisibility(View.GONE);
            ll_not_order_information.setVisibility(View.VISIBLE);
        }
        ll_loading.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_my_owner_allorder: // 查看更多订单
                Intent myOwnerOrderIntent = new Intent(mContext, MyOwnerOrderActivity.class);
                startActivity(myOwnerOrderIntent);
                break;
            case R.id.iv_system_message_user: // 系统消息
                startActivity(new Intent(mContext, SystemMessageActivity.class));
                break;
            case R.id.rel_my_owenr_user_layout:
            case R.id.myowner_layout_personal_data:
                if (Util.isNetAvailable(mContext)) {
                    Intent personalIntent = new Intent(mContext, MyOwnerAccountManagerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nickname", nickname);
                    bundle.putString("icon", icon);
                    bundle.putString("token", token);
                    bundle.putString("gender", gender);
                    bundle.putString("cityname", cityname);
                    bundle.putString("wechat_check", wechat_check);
                    bundle.putString("cellphone", cellphone);
                    bundle.putString("province", province);
                    bundle.putString("icommunity", icommunity);
                    personalIntent.putExtra("data", bundle);
                    startActivityForResult(personalIntent, 14);
                }
                break;
            case R.id.myowner_layout_store_pic:
//			Intent storeIntent = new Intent(mContext, MyStoreActivity.class);
                Intent storeIntent = new Intent(mContext, MyFavActivity.class);
                startActivity(storeIntent);
                break;
            case R.id.iv_myowner_set:
                Intent setIntent = new Intent(mContext, SettingActivity.class);
                setIntent.putExtra("url", "");
                startActivity(setIntent);
                break;
            case R.id.my_layout_feedback:
                Intent intent = new Intent(mContext, FeedbackActivity.class);
                startActivity(intent);
                break;
//        case R.id.my_layout_ower_msg:
//            startActivity(new Intent(mContext, SystemMessageActivity.class));
//            break;
            case R.id.my_layout_ower_outcome:
                Util.setErrorLog(TAG, "是否有设置预算>>>" + CacheManager.getDecorateBudget(mContext) + "<<<<<");
                if (CacheManager.getDecorateBudget(mContext) <= 0) {
                    startActivity(new Intent(mContext, KeepAccountActivity.class));
                } else {
                    startActivity(new Intent(mContext, DecorateAccountActivity.class));
                }

                break;
            case R.id.my_owner_pic:
                menuWindow = new SelectPersonalPopupWindow(this, itemsPersonOnClick);
                menuWindow.showAtLocation(findViewById(R.id.my_owner_pic), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_order_now:
                startActivity(new Intent(mContext, FreeYuyueActivity.class));
                break;
            case R.id.tv_find_order:
                operFindOrder();
                break;
            case R.id.linearlayout_have_order_information:
            case R.id.tv_delstatusDes:
                //业主查看装修公司的订单详情
                Intent orderDetailIntent = new Intent(mContext, OrderDetailActivity.class);
                orderDetailIntent.putExtra("url", orderDetailUrl);
                Log.d(TAG, "--该业主的订单详情url是【" + orderDetailUrl + "】-");
                startActivity(orderDetailIntent);
                break;
            case R.id.ll_decorate_security:
                startActivity(new Intent(mContext, DecorateSecurityActivity.class));
                break;
            default:
                break;
        }

    }

    /**
     * 对订单的操作
     */
    private void operFindOrder() {
        if (TextUtils.isEmpty(cellphone) || "未绑定".equals(cellphone)) {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("您还没有绑定手机号，请先绑定！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent bindingPhoneIntent = new Intent(mContext, BindingPhoneActivity.class);
                    bindingPhoneIntent.putExtra("token", token);
                    startActivityForResult(bindingPhoneIntent, 5);
                    dialog.dismiss();
                }
            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.create().show();
        } else {
            showFindOrderDialog();
        }
    }

    private OnClickListener itemsPersonOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    // 拍照获取头像
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                case R.id.pickPhotoBtn:
                    // 选择本地文件夹中图片获取头像
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                break;
            case REQUESTCODE_TAKE:

                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                if (resultCode == Activity.RESULT_CANCELED) {
                    Intent it = new Intent(mContext, MyOwnerActivity.class);
                    startActivity(it);

                } else {
                    startPhotoZoom(Uri.fromFile(temp));
                }

                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;

            case 5:
                if (data != null) {
                    cellphone = data.getStringExtra("result");
                    showFindOrderDialog();
                }
                break;
        }

        switch (resultCode) {
            case 44:

                break;

        }
    }

    /***
     * 找回订单
     */
    private void showFindOrderDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);

        builder.setMessage("将找回手机" + cellphone + "最近2个月内的装修订单！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                requestFindOrder(dialog);
            }

        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    /***
     * 找回订单接口请求方法
     * @param dialog
     */
    private void requestFindOrder(final DialogInterface dialog) {
        retrieveOrderParams = AppInfoUtil.getPublicParams(getApplicationContext());
        retrieveOrderParams.put("token", token);

        HttpServer.getInstance().requestPOST(retrieveOrderUrl, retrieveOrderParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
                    if (jsonObject.getInt("error_code") == 0) {
                        Toast.makeText(mContext, "订单已成功关联！", Toast.LENGTH_SHORT).show();
                        requestUserMy();
                    } else {
                        Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    dialog.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }


    /***
     * 裁剪头像图片
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /***
     * 上传用户头像至服务器
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
            Log.e(TAG, "上传头像的文件流地址======" + urlpath);
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            // 开启线程上传
            new Thread(uploadImageRunnable).start();

        }
    }

    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            if (TextUtils.isEmpty(imgUrl)) {
                Toast.makeText(mContext, "还没有设置上传服务器的路径！", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> textParams = null;
            Map<String, File> fileparams = null;
            try {
                URL url = new URL(imgUrl);
                textParams = new HashMap<String, String>();
                fileparams = new HashMap<String, File>();
                File file = new File(urlpath);
                fileparams.put("filedata", file);
                textParams.put("s_code", "head_file");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("ser-Agent", "Fiddler");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + WriteUtil.BOUNDARY);
                OutputStream os = conn.getOutputStream();
                DataOutputStream ds = new DataOutputStream(os);
                WriteUtil.writeStringParams(textParams, ds);
                WriteUtil.writeFileParams(fileparams, ds);
                WriteUtil.paramsEnd(ds);
                // 对文件流操作完,要记得及时关闭
                os.close();
                // 服务器返回的响应吗
                int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                // 对响应码进行判断
                if (code == 200) {// 返回的响应码200,是成功
                    // 得到网络返回的输入流
                    InputStream is = conn.getInputStream();
                    resultStr = WriteUtil.readString(is);
                } else {
                    Toast.makeText(mContext, "请求URL失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            handlerUpload.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
        }
    };

    private Handler handlerUpload = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(resultStr);
                        Log.e(TAG, "返回的结果=========" + resultStr);
                        if (jsonObject.optString("error").equals("0")) {
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            option.inSampleSize = 1;
                            String imageUrl = jsonObject.optString("url");
                            userChageInfoParams = AppInfoUtil.getPublicParams(getApplicationContext());
                            userChageInfoParams.put("field", "5");
                            userChageInfoParams.put("new", imageUrl);
                            userChageInfoParams.put("token", token);
                            MyApplication.imageLoader.displayImage(imageUrl, my_owner_pic, MyApplication.options);

                            HttpServer.getInstance().requestPOST(userChageInfoUrl, userChageInfoParams, new AsyncHttpResponseHandler() {

                                @Override
                                public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                                }

                                @Override
                                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.optString("url"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
            return false;
        }
    });


    private void initReceiver() {
        receiver = new DataRefreshReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.LOGOUT_ACTION);
        registerReceiver(receiver, filter);
    }

    private DataRefreshReceiver receiver;

    private class DataRefreshReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.LOGOUT_ACTION.equals(intent.getAction())) {
                Util.setToast(mContext, "怎？？？111");
//				requestUserMy();
            }
        }
    }

}
