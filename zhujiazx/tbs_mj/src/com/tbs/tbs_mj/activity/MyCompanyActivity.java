package com.tbs.tbs_mj.activity;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.customview.CheckOrderPwdPopupWindow;
import com.tbs.tbs_mj.customview.InputWarnDialog;
import com.tbs.tbs_mj.customview.RoundImageView;
import com.tbs.tbs_mj.customview.SettingOrderPwdPopupWindow;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.MyApplication;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.MD5Util;
import com.tbs.tbs_mj.utils.Util;
import com.umeng.socialize.utils.Log;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyCompanyActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = MyCompanyActivity.class.getSimpleName();
    private Context mContext;
    private RoundImageView my_decorate_pic;
    private TextView my_decorate_company_name;
    private RelativeLayout rel_mycompany_info;
    private RelativeLayout my_layout_allorder;
    private TextView tv_orderTotal;
    private ImageView image_business_license;
    private static final int NEVER_SET_PSD= 20301;
    private static final int PC_RESET_PSD = 20302;
    private static final int HAS_SET_PSD = 0;
    private String hasOrderPwdUrl = Constant.TOBOSU_URL + "tapp/order/hasOrderPwd";
    private String setOrderPwdUrl = Constant.TOBOSU_URL + "tapp/passport/setOrderPwd";
    private String requestOrderPwdUrl = Constant.TOBOSU_URL + "tapp/order/checkOrderPwd";
    private ImageView image_bao;
    private String notReadNumber;
    private LinearLayout my_notyet_order;
    private LinearLayout my_yet_order;
    private LinearLayout my_layout_network;
    private LinearLayout my_layout_feedback;
    private LinearLayout my_layout_preferential_applyfor;
    private LinearLayout my_has_sign_bill;
    private LinearLayout my_layout_userinfo;
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
    private String jjb_logo;
    private String certification;
    private String cityname;
    private String memberdegree;
    private String shareUrl;
    private String wechat_check;
    private String cellphone;
    private String already_count;
    private String unalready_count;
    private String token;
    private HashMap<String, Object> myParams;
    private String myUrl = Constant.TOBOSU_URL + "tapp/user/my";
    private LinearLayout ll_loading;
    private LinearLayout ll_msgnote;//消息
    private ImageView iv_system_message_company;
    private String sysmesscount;
    private TextView tv_not_see_sysmsg;
    private TextView tv_notyet_order_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company);
        my_decorate_pic = (RoundImageView) findViewById(R.id.my_decorate_pic);
        mContext = MyCompanyActivity.this;
        initView();
        initEvent();
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
        token = AppInfoUtil.getToekn(getApplicationContext());
        if (!TextUtils.isEmpty(token)) {
            myParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
            myParams.put("token", token);
            requstMyPost();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
    private void requstMyPost() {
        OKHttpUtil.post(myUrl, myParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "我的【装修公司】接口请求失败" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myData = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                            if ("未绑定".equals(cellphone)) {
                                getSharedPreferences("userInfo", 0).edit().putString("cellphone", "").commit();
                            } else {
                                getSharedPreferences("userInfo", 0).edit().putString("cellphone", cellphone).commit();
                            }
                            notReadNumber = object.getString("not_see_orders_count");

                            Log.d("sysmesscount", sysmesscount);
                            if (Integer.parseInt(sysmesscount) > 0) {
                                tv_not_see_sysmsg.setVisibility(View.VISIBLE);
                            } else {
                                tv_not_see_sysmsg.setVisibility(View.GONE);
                            }
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
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.my_notyet_order: // 2-->> 未量房
                MyApplication.ISPUSHLOOKORDER = false;
                requestHasOrderPwd("2");
                break;
            case R.id.iv_system_message_company: // 消息
            case R.id.ll_msgnote:
                startActivity(new Intent(this, SystemMessageActivity.class));
                break;
            case R.id.my_yet_order: // 1-->>  已量房
                MyApplication.ISPUSHLOOKORDER = false;
                requestHasOrderPwd("1");
                break;
            case R.id.my_layout_allorder: // 0-->>  全部订单
                MyApplication.ISPUSHLOOKORDER = false;
                requestHasOrderPwd("0");
                break;
            case R.id.my_has_sign_bill: // 3-->>  已签单
                MyApplication.ISPUSHLOOKORDER = false;
                requestHasOrderPwd("3");
                break;
            case R.id.my_layout_preferential_applyfor: // 优惠报名
                startActivity(new Intent(mContext, PreferentialApplyForActivity.class));
                break;
            case R.id.my_layout_network: // 网店设置
                Intent networksetIntent = new Intent(mContext, NetworkSetActivity.class);
                networksetIntent.putExtra("nickname", nickname);//
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



    private boolean isCheckOrderPwd;
    private void requestHasOrderPwd(final String kind) {
        HashMap hasOrderParams = new HashMap<String, Object>();
        hasOrderParams.put("token", token);
        isCheckOrderPwd = getSharedPreferences("CheckOrderPwd", 0).getBoolean("CheckOrderPwd", false);
        OKHttpUtil.post(hasOrderPwdUrl, hasOrderParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);

                            //订单密码已设置     -->>[debug得知]
                            if (isCheckOrderPwd && jsonObject.getInt("error_code") == HAS_SET_PSD) {
                                goAllOrder(kind);
                                return;
                            }

                            if (jsonObject.getInt("error_code") == NEVER_SET_PSD) {
                                InputWarnDialog.Builder builder = new InputWarnDialog.Builder(mContext);
                                builder.setTitle("提示")/*.setMessage("您还没设置订单密码，请先设置")*/;
                                builder.setMessage("你确定退出吗？").setPositiveButton("设置订单密码", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        operSetOrderPwd(kind);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();
                                return;
                            }
                            if (jsonObject.getInt("error_code") == PC_RESET_PSD || !isCheckOrderPwd) {
                                operCheckOrderPwd(kind);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void operSetOrderPwd(final String kind) {
        final SettingOrderPwdPopupWindow popuWindow = new SettingOrderPwdPopupWindow(mContext);
        popuWindow.showAtLocation(my_layout_allorder.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popuWindow.bt_subit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd1 = popuWindow.et_setting_order_pwd1.getText().toString().trim();
                String pwd2 = popuWindow.et_setting_order_pwd2.getText().toString().trim();

                if("".equals(pwd1) || "".equals(pwd2)){
                    Util.setToast(mContext, "密码不能为空");
                    return;
                }else{
                    if(pwd1.equals(pwd2)){
                        if(pwd1.length()>16 || pwd1.length()<6){
                            Util.setToast(mContext, "密码长度在6-16位之间");
                            return;
                        }
                        requestSetOrderPwd(popuWindow, pwd1, pwd2, kind);
                    }else{
                        Util.setToast(mContext, "两次输入密码不一样，请重新输入");
                        return;
                    }
                }

            }
        });
    }

    protected void requestSetOrderPwd(final SettingOrderPwdPopupWindow popuWindow, String pwd1, String pwd2, final String kind){
        HashMap<String, Object> setOrderParams = new HashMap<String, Object>();
        setOrderParams.put("token", token);
        setOrderParams.put("orderpwd", MD5Util.md5(pwd1));
        setOrderParams.put("orderpwd1", MD5Util.md5(pwd2));

        OKHttpUtil.post(setOrderPwdUrl, setOrderParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int code = jsonObject.getInt("error_code");
                            switch (code) {
                                case HAS_SET_PSD:  // 已经设置密码  HAS_SET_PSD == 0
                                case PC_RESET_PSD: // pc端重置过密码，需重新验证   PC_RESET_PSD == 20302
                                    getSharedPreferences("CheckOrderPwd", 0).edit().putBoolean("CheckOrderPwd", false).commit();

                                    // 输入密码来查看
                                    operCheckOrderPwd(kind);
                                    Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    popuWindow.dismiss();
                                    break;

                                default:
                                    Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
    private void operCheckOrderPwd(final String kind) {
        final CheckOrderPwdPopupWindow checkOrderPwdPopupWindow = new CheckOrderPwdPopupWindow(mContext);
        checkOrderPwdPopupWindow.showAtLocation(my_layout_allorder.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        checkOrderPwdPopupWindow.bt_subit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd = checkOrderPwdPopupWindow.et_check_order_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(mContext, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 验证订单秘密
                requestCheckOrderPwd(pwd, kind, checkOrderPwdPopupWindow);
            }
        });
    }


    private void requestCheckOrderPwd(String pwd, final String kind, final PopupWindow poWindow) {
        HashMap<String, Object> requestOrderParams = new HashMap<String, Object>();
        requestOrderParams.put("token", token);
        requestOrderParams.put("orderpwd", MD5Util.md5(pwd));
        OKHttpUtil.post(requestOrderPwdUrl, requestOrderParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("error_code") == HAS_SET_PSD) {
                                goAllOrder(kind);
                                getSharedPreferences("CheckOrderPwd", 0).edit().putBoolean("CheckOrderPwd", true).commit();
                                poWindow.dismiss();
                            } else {
                                Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
    public void goAllOrder(String kind) {
        Intent allOrderIntent = new Intent(mContext, AllOrderListActivity.class);
        Bundle allOrderBundle = new Bundle();
        allOrderBundle.putString("kind", kind);
        allOrderIntent.putExtras(allOrderBundle);
        mContext.startActivity(allOrderIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 44:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
