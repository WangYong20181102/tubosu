package com.tbs.tobosutype.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.RoundImageView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CheckOrderUtils;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.Util;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import com.umeng.socialize.utils.Log;

/**
 * 装修公司主页
 *
 * @author dec
 */
public class MyCompanyActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = MyCompanyActivity.class.getSimpleName();
    private Context mContext;
    private RoundImageView my_decorate_pic;
    private TextView my_decorate_company_name;

    /**
     * 顶部用户信息布局
     */
    private RelativeLayout rel_mycompany_info;

    /**
     * 全部订单布局
     */
    private RelativeLayout my_layout_allorder;

    /**
     * 全部订单数
     */
    private TextView tv_orderTotal;

    private ImageView image_business_license;

    /**
     * 装修保
     */
    private ImageView image_bao;

    /**
     * 未读订单数量
     */
    private String notReadNumber;

    /**
     * 未量房布局
     */
    private LinearLayout my_notyet_order;

    /**
     * 已量房布局
     */
    private LinearLayout my_yet_order;

    /**
     * 网店设置布局
     */
    private LinearLayout my_layout_network;

    /**
     * 问题反馈布局
     */
    private LinearLayout my_layout_feedback;

    /**
     * 优惠报名布局
     */
    private LinearLayout my_layout_preferential_applyfor;

    /**
     * 已签单布局
     */
    private LinearLayout my_has_sign_bill;

    /**
     * 账户管理布局
     */
    private LinearLayout my_layout_userinfo;

    /**
     * 右上角 设置按钮
     */
    private ImageView mycompany_set;

    private String id;
    private String icon;
    private String lng;
    private String name;
    private String company_name;
    private String lat;
    private String nickname;
    private String district;
    private String orderTotal;

    /**
     * 装修保字段
     */
    private String jjb_logo;

    /**
     * 实体认证字段
     */
    private String certification;
    private String cityname;

    /**
     * 会员等级
     * <p>
     * 1非会员
     * 2高级会员 X
     * 3初级会员
     * 4广告会员
     * 5延期会员
     * 6高级会员 X
     */
    private String memberdegree;
    private String shareUrl;

    /**
     * 是否绑定
     * 1 已绑定
     * 0 未绑定
     */
    private String wechat_check;

    private String cellphone;

    /**
     * 已读消息数量
     */
    private String already_count;

    /**
     * 未读消息数量
     */
    private String unalready_count;

    private String token;
    private RequestParams myParams;

    /**
     * 我的【装修公司】接口
     */
    private String myUrl = Constant.TOBOSU_URL + "tapp/user/my";

    private LinearLayout ll_loading;
    private LinearLayout ll_msgnote;//消息

    public MainActivity mainActivity;
    private ImageView iv_system_message_company;

    /**
     * 系统消息
     */
    private String sysmesscount;
    private TextView tv_not_see_sysmsg;

    /**
     * 未量房若有未看订单时出现红点
     */
    private TextView tv_notyet_order_num;

    private SharedPreferences spPushConfig;

    private String companyCellphone = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_company);
        my_decorate_pic = (RoundImageView) findViewById(R.id.my_decorate_pic);

        mContext = MyCompanyActivity.this;
        initReceiver();
        initView();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        tv_not_see_sysmsg = (TextView) findViewById(R.id.tv_not_see_sysmsg);
        rel_mycompany_info = (RelativeLayout) findViewById(R.id.rel_mycompany_info);
        my_decorate_company_name = (TextView) findViewById(R.id.my_decorate_name);
        tv_orderTotal = (TextView) findViewById(R.id.tv_orderTotal);
        image_business_license = (ImageView) findViewById(R.id.image_business_license);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        ll_msgnote = (LinearLayout) findViewById(R.id.ll_msgnote);
        my_layout_userinfo = (LinearLayout) findViewById(R.id.my_layout_userinfo);
        my_layout_preferential_applyfor = (LinearLayout) findViewById(R.id.my_layout_preferential_applyfor);
        image_bao = (ImageView) findViewById(R.id.image_bao);
        my_notyet_order = (LinearLayout) findViewById(R.id.my_notyet_order);
        my_yet_order = (LinearLayout) findViewById(R.id.my_yet_order);
        my_layout_allorder = (RelativeLayout) findViewById(R.id.my_layout_allorder);
        my_layout_network = (LinearLayout) findViewById(R.id.my_layout_network);
        my_layout_feedback = (LinearLayout) findViewById(R.id.my_layout_feedback);
        my_has_sign_bill = (LinearLayout) findViewById(R.id.my_has_sign_bill);
        mycompany_set = (ImageView) findViewById(R.id.mycompany_set);
        iv_system_message_company = (ImageView) findViewById(R.id.iv_system_message_company);
        tv_notyet_order_num = (TextView) findViewById(R.id.tv_notyet_order_num);
    }

    private void initEvent() {
        my_notyet_order.setOnClickListener(this);
        my_yet_order.setOnClickListener(this);
        my_layout_allorder.setOnClickListener(this);
        my_layout_network.setOnClickListener(this);
        my_layout_feedback.setOnClickListener(this);
        my_has_sign_bill.setOnClickListener(this);
        rel_mycompany_info.setOnClickListener(this);
        mycompany_set.setOnClickListener(this);
        my_layout_userinfo.setOnClickListener(this);
        my_layout_preferential_applyfor.setOnClickListener(this);
        iv_system_message_company.setOnClickListener(this);
        ll_msgnote.setOnClickListener(this);
    }

    private void initData() {
        mainActivity = new MainActivity();
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


//		Log.d(TAG, "mark标记是" + mark);

        // 根据不同mark标记，注册推送
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
            if (!TextUtils.isEmpty(userid) && getSharedPreferences("Push_Config", MODE_PRIVATE).getBoolean("Is_Push_Message", true)) {
                XGPushManager.registerPush(getApplicationContext(), userid);
                spPushConfig = getSharedPreferences("Push_Config", MODE_PRIVATE);
                Editor editor = spPushConfig.edit();
                editor.putBoolean("Is_Push_Message", true);
                editor.commit();

                XGPushManager.deleteTag(this, "loginOff");
                XGPushManager.setTag(this, "loginOn");
                XGPushManager.setTag(this, "isCompany");
                Intent service = new Intent(mContext, XGPushService.class);
                Log.d(TAG, "注册成功");
                startService(service);
            }
        }

        if (!TextUtils.isEmpty(token)) {
            myParams = AppInfoUtil.getPublicParams(getApplicationContext());
            myParams.put("token", token);
            requstMyPost();
        }
    }

    /***
     *  我的【装修公司】接口请求方法
     *
     * */
    private void requstMyPost() {
//		Log.d(TAG, "--我的【装修公司】接口请求方法--");
        HttpServer.getInstance().requestPOST(myUrl, myParams, new AsyncHttpResponseHandler() {

            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Log.d(TAG, "我的【装修公司】接口请求失败" + arg1);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String myData = new String(responseBody);
                Log.d(TAG, "我的【装修公司】接口请求成功" + myData);
                try {
                    JSONObject myObject = new JSONObject(myData);
                    JSONObject object = myObject.getJSONObject("data");
                    id = (String) object.get("id");
                    icon = (String) object.get("icon");
                    MyApplication.iconUrl = icon;
                    nickname = (String) object.get("nickname");
                    company_name = (String) object.get("company_name");
                    lat = object.getString("lat");
                    lng = object.getString("lng");
                    name = object.getString("name");
                    district = object.getString("district");
                    orderTotal = object.getString("orderTotal");
                    jjb_logo = object.getString("jjb_logo");
                    sysmesscount = object.getString("sysmesscount");
                    cityname = object.getString("cityname");
                    memberdegree = object.getString("memberdegree");
                    shareUrl = object.getString("shareUrl");
                    wechat_check = object.getString("wechat_check");
                    cellphone = object.getString("cellphone");
                    // 存电话号码
                    if ("未绑定".equals(cellphone)) {
                        getSharedPreferences("userInfo", 0).edit().putString("cellphone", "").commit();
                    } else {
                        getSharedPreferences("userInfo", 0).edit().putString("cellphone", cellphone).commit();
                    }
                    notReadNumber = object.getString("not_see_orders_count");

                    Log.d("sysmesscount", sysmesscount);
                    if (Integer.parseInt(sysmesscount) > 0) {
                        tv_not_see_sysmsg.setVisibility(View.VISIBLE);
                        // 2016-07-08 上午，  产品说不需要显示数字，仅仅显示红点就是了
//								if(Integer.parseInt(sysmesscount)>10){
//									tv_not_see_sysmsg.setText("10+");
//								}else{
//									tv_not_see_sysmsg.setText(sysmesscount);
//								}
                    } else {
                        tv_not_see_sysmsg.setVisibility(View.GONE);
                    }
//							Log.d(TAG, "你有未看订单外 "+notReadNumber);
                    Log.d("sysmesscount是未看系统信息数", notReadNumber);

                    if (notReadNumber != null && !notReadNumber.equals("0")) {
                        tv_notyet_order_num.setVisibility(View.VISIBLE);
                        tv_notyet_order_num.setText(notReadNumber);
                    } else {
                        tv_notyet_order_num.setVisibility(View.GONE);
                    }


                    certification = object.getString("certification");
                    already_count = object.getString("already_count");
                    unalready_count = object.getString("unalready_count");
                    MyApplication.imageLoader.displayImage(icon, my_decorate_pic, MyApplication.options, null);
                    my_decorate_company_name.setText(name);
                    if ("0".equals(jjb_logo)) {
                        image_bao.setVisibility(View.GONE);
                    } else {
                        image_bao.setVisibility(View.VISIBLE);
                    }
                    if ("0".equals(certification)) {
                        image_business_license.setVisibility(View.GONE);
                    } else {
                        image_business_license.setVisibility(View.VISIBLE);
                    }
                    ll_loading.setVisibility(View.GONE);
                    tv_orderTotal.setText(orderTotal);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onClick(View v) {

        Util.setLog(TAG, "点击了吗？？？？？");
        switch (v.getId()) {
            case R.id.my_notyet_order: // 2-->> 未量房
                MyApplication.ISPUSHLOOKORDER = false;
//			ll_loading.setVisibility(View.VISIBLE);
                new CheckOrderUtils(my_layout_allorder, mContext, token, "2");
                break;
            case R.id.iv_system_message_company: // 消息
            case R.id.ll_msgnote:
                startActivity(new Intent(this, SystemMessageActivity.class));
                break;
            case R.id.my_yet_order: // 1-->>  已量房
//			ll_loading.setVisibility(View.VISIBLE);
                MyApplication.ISPUSHLOOKORDER = false;
                new CheckOrderUtils(my_layout_allorder, mContext, token, "1");
                break;
            case R.id.my_layout_allorder: // 0-->>  全部订单
//			ll_loading.setVisibility(View.VISIBLE);
                MyApplication.ISPUSHLOOKORDER = false;
                new CheckOrderUtils(my_layout_allorder, mContext, token, "0");
                break;
            case R.id.my_has_sign_bill: // 3-->>  已签单
//			ll_loading.setVisibility(View.VISIBLE);
                MyApplication.ISPUSHLOOKORDER = false;
                new CheckOrderUtils(my_layout_allorder, mContext, token, "3");
                break;

            case R.id.my_layout_preferential_applyfor: // 优惠报名
                startActivity(new Intent(mContext, PreferentialApplyForActivity.class));
                break;
            case R.id.my_layout_network: // 网店设置
                Intent networksetIntent = new Intent(mContext, NetworkSetActivity.class);
                networksetIntent.putExtra("nickname", nickname);
                networksetIntent.putExtra("id", id);
                networksetIntent.putExtra("memberdegree", memberdegree);
                networksetIntent.putExtra("cityname", cityname);
                networksetIntent.putExtra("shareUrl", shareUrl);
                networksetIntent.putExtra("name", name);
                networksetIntent.putExtra("lng", lng);// 经度
                networksetIntent.putExtra("lat", lat);// 纬度
                networksetIntent.putExtra("district", district);
                networksetIntent.putExtra("token", token);
                networksetIntent.putExtra("certification", certification);
                startActivity(networksetIntent);
                break;
            case R.id.mycompany_set: // 设置按钮
                Intent setIntent = new Intent(mContext, SettingActivity.class);
                setIntent.putExtra("url", "");
                startActivity(setIntent);
                break;
            case R.id.my_layout_feedback:
                Intent intent = new Intent(mContext, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_mycompany_info:
            case R.id.my_layout_userinfo: // 账户管理
                Intent userInfoIntent = new Intent(mContext, MyCompanyAccountManagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("company_name", company_name);
                bundle.putString("token", token);
                bundle.putString("wechat_check", wechat_check);
                bundle.putString("cellphone", cellphone);
                userInfoIntent.putExtra("data", bundle);
                startActivityForResult(userInfoIntent, 16);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 44:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initReceiver(){
        receiver = new DataRefreshReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.LOGOUT_ACTION);
        registerReceiver(receiver, filter);
    }
    private DataRefreshReceiver receiver;
    private class DataRefreshReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constant.LOGOUT_ACTION.equals(intent.getAction())){
                Util.setToast(mContext, "怎？？？");
//                requstMyPost();
            }
        }
    }

}
