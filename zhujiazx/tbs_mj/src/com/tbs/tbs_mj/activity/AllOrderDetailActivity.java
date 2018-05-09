package com.tbs.tbs_mj.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.customview.CallDialogCompany;
import com.tbs.tbs_mj.customview.CheckOrderPwdPopupWindow;
import com.tbs.tbs_mj.customview.InputWarnDialog;
import com.tbs.tbs_mj.customview.LfPwdPopupWindow;
import com.tbs.tbs_mj.customview.SettingOrderPwdPopupWindow;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.MyApplication;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.MD5Util;
import com.tbs.tbs_mj.utils.Util;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 全部订单的明细页面
 *
 * @author dec
 */
@SuppressLint("SimpleDateFormat")
public class AllOrderDetailActivity extends com.tbs.tbs_mj.base.BaseActivity implements OnClickListener {
    private static final String TAG = AllOrderDetailActivity.class.getSimpleName();
    private Context mContext;

    private ImageView detail_allorder_back;

    /***订单标题*/
    private TextView detail_allorder_split;

    /**
     * 订单状态
     */
    private ImageView detail_allorder_state;

    /**
     * 业主
     */
    private TextView detail_allorder_owner;

    /**
     * 小区名称
     */
    private TextView detail_allorder_district;

    /**
     * 房的类型
     */
    private TextView detail_allorder_type;

    /**
     * 房子面积
     */
    private TextView detail_allorder_area;

    /**
     * 风格
     */
    private TextView detail_allorder_style;

    /**
     * 装修预算
     */
    private TextView detail_allorder_budget;

    /**
     * 订单详情
     */
    private TextView detail_allorder_title;

    /**
     * 装修方式text
     */
    private TextView detail_allorder_zxmode;

    /**
     * 户型结构text
     */
    private TextView detail_allorder_hxstructure;

    /**
     * 旧房改造text
     */
    private TextView detail_allorder_oldreform;

    /**
     * 是否拿到钥匙text
     */
    private TextView detail_allorder_takekey;

    /**
     * 装修类型text
     */
    private TextView detail_allorder_zxtype;

    /**
     * 跟踪记录text
     */
    private TextView tv_orderOperationNum;

    /**
     * 跟踪反馈text
     */
    private TextView tv_followup_feedback;

    /***小区地址*/
    private TextView detail_allorder_addressqu;

    /**
     * 其他需求
     */
    private TextView detail_allorder_other;

    /**
     * 收到订单布局
     */
    private LinearLayout layout_one;

    /***客户回访布局*/
    private LinearLayout layout_two;

    /**
     * 已量房布局
     */
    private LinearLayout layout_three;

    /**
     * 签订合同布局
     */
    private LinearLayout layout_four;

    /**
     * 收到订单左边的圆点
     */
    private ImageView detail_image_start;

    /**
     * 收到订单text
     */
    private TextView detail_allorder_start;

    /**
     * 收到订单时间
     */
    private TextView detail_allorder_start_time;

    /**
     * 客户回访左边的圆点
     */
    private ImageView detail_image_visit;

    /**
     * 客户回访text
     */
    private TextView detail_allorder_visit;

    /**
     * 客户回访时间
     */
    private TextView detail_allorder_visit_time;

    /**
     * 已量房左边的圆点
     */
    private ImageView detail_image_lf;

    /**
     * 已量房text
     */
    private TextView detail_allorder_lf;

    /**
     * 已量房时间
     */
    private TextView detail_allorder_lf_time;

    @SuppressWarnings("unused")
    /**签订合同左边的圆点*/
    private ImageView detail_image_trade;

    @SuppressWarnings("unused")
    /**签订合同text*/
    private TextView detail_allorder_trade;

    /**
     * 签订合同时间
     */
    private TextView detail_allorder_trade_time;

    /**
     * 业主姓名
     */
    private TextView detail_allorder_user;

    /**
     * 确认量房
     */
    private TextView detail_allorder_submit;

    /**
     * 业主电话沟通
     */
    private TextView detail_allorder_phone;

    /**
     * 订单详情接口
     */
    private String orderDetailUrl = Constant.TOBOSU_URL + "tapp/order/order_detail";

    public String order_id;
    private String order_issee;
    public String token;
    private String start_time;
    private String visit_time;
    private String lftime;
    private String tradetime;
    private String nick;

    private String mobile;
    private Window window;

    private LayoutParams params;
    private HashMap<String, Object> orderDetailParams;
    private HashMap<String, Object> orderParams;
    private RelativeLayout rl_top;

    /***订单量房接口*/
    private String lfangUrl = Constant.TOBOSU_URL + "tapp/order/lfang";

    /***加载更多*/
    private LinearLayout detail_allorder_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_allorder_detail);
        mContext = AllOrderDetailActivity.this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        detail_allorder_loading = (LinearLayout) findViewById(R.id.ll_loading);
        detail_allorder_back = (ImageView) findViewById(R.id.detail_allorder_back);
        detail_allorder_split = (TextView) findViewById(R.id.detail_allorder_split);
        tv_orderOperationNum = (TextView) findViewById(R.id.tv_orderOperationNum);
        detail_allorder_state = (ImageView) findViewById(R.id.detail_allorder_state);
        detail_allorder_title = (TextView) findViewById(R.id.detail_allorder_title);
        detail_allorder_owner = (TextView) findViewById(R.id.detail_allorder_owner);
        detail_allorder_district = (TextView) findViewById(R.id.detail_allorder_district);
        detail_allorder_type = (TextView) findViewById(R.id.detail_allorder_type);
        detail_allorder_area = (TextView) findViewById(R.id.detail_allorder_area);
        tv_followup_feedback = (TextView) findViewById(R.id.tv_followup_feedback);

        detail_allorder_style = (TextView) findViewById(R.id.detail_allorder_style);
        detail_allorder_budget = (TextView) findViewById(R.id.detail_allorder_budget);
        detail_allorder_zxmode = (TextView) findViewById(R.id.detail_allorder_zxmode);
        detail_allorder_hxstructure = (TextView) findViewById(R.id.detail_allorder_hxstructure);
        detail_allorder_oldreform = (TextView) findViewById(R.id.detail_allorder_oldreform);

        detail_allorder_takekey = (TextView) findViewById(R.id.detail_allorder_takekey);
        detail_allorder_zxtype = (TextView) findViewById(R.id.detail_allorder_zxtype);
        detail_allorder_addressqu = (TextView) findViewById(R.id.detail_allorder_addressqu);
        detail_allorder_other = (TextView) findViewById(R.id.detail_allorder_other);

        layout_one = (LinearLayout) findViewById(R.id.layout_one);
        layout_two = (LinearLayout) findViewById(R.id.layout_two);
        layout_three = (LinearLayout) findViewById(R.id.layout_three);
        layout_four = (LinearLayout) findViewById(R.id.layout_four);

        detail_image_start = (ImageView) findViewById(R.id.detail_image_start);
        detail_allorder_start = (TextView) findViewById(R.id.detail_allorder_start);
        detail_allorder_start_time = (TextView) findViewById(R.id.detail_allorder_start_time);

        detail_image_visit = (ImageView) findViewById(R.id.detail_image_visit);
        detail_allorder_visit = (TextView) findViewById(R.id.detail_allorder_visit);
        detail_allorder_visit_time = (TextView) findViewById(R.id.detail_allorder_visit_time);

        detail_image_lf = (ImageView) findViewById(R.id.detail_image_lf);
        detail_allorder_lf = (TextView) findViewById(R.id.detail_allorder_lf);
        detail_allorder_lf_time = (TextView) findViewById(R.id.detail_allorder_lf_time);

        detail_image_trade = (ImageView) findViewById(R.id.detail_image_trade);
        detail_allorder_trade = (TextView) findViewById(R.id.detail_allorder_trade);
        detail_allorder_trade_time = (TextView) findViewById(R.id.detail_allorder_trade_time);

        detail_allorder_user = (TextView) findViewById(R.id.detail_allorder_user);
        detail_allorder_submit = (TextView) findViewById(R.id.detail_allorder_submit);
        detail_allorder_phone = (TextView) findViewById(R.id.detail_allorder_phone);
        rl_top.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        order_id = getIntent().getExtras().getString("id");
        order_issee = getIntent().getExtras().getString("issee");
        detail_allorder_title.setText("订单" + order_id);
        token = AppInfoUtil.getToekn(getApplicationContext());
        orderDetailParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
        orderDetailParams.put("token", token);
        orderDetailParams.put("order_id", order_id);

        if (MyApplication.ISPUSHLOOKORDER) {
            requestHasOrderPwd();
        } else {
            requestOrderDetailPost();
        }
    }

    /***订单是否设置密码*/
    private String hasOrderPwdUrl = Constant.TOBOSU_URL + "tapp/order/hasOrderPwd";
    /**是否验证了订单密码*/
    private boolean isCheckOrderPwd;
    /***未设置过订单密码*/
    private static final int NEVER_SET_PSD= 20301;

    /**pc端重新设置过密码*/
    private static final int PC_RESET_PSD = 20302;

//	/**没有密码*/
//	private static final int NO_PSD = 0;

    /**订单密码已设置*/
    private static final int HAS_SET_PSD = 0;
    /***
     * 订单是否设置密码接口请求
     */
    private void requestHasOrderPwd() {
        HashMap hasOrderParams = new HashMap<String, Object>();
        hasOrderParams.put("token", token);
        isCheckOrderPwd = mContext.getSharedPreferences("CheckOrderPwd", 0).getBoolean("CheckOrderPwd", false);
        OKHttpUtil.post(hasOrderPwdUrl, hasOrderParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    //订单密码已设置     -->>[debug得知]
                    if (isCheckOrderPwd && jsonObject.getInt("error_code") == HAS_SET_PSD) {
                        requestOrderDetailPost();
                    }


                    // 没设密码，则需要设置密码
                    if (jsonObject.getInt("error_code") == NEVER_SET_PSD) {
                        InputWarnDialog.Builder builder = new InputWarnDialog.Builder(mContext);
                        builder.setTitle("提示")/*.setMessage("您还没设置订单密码，请先设置")*/;
                        builder.setMessage("你确定退出吗？").setPositiveButton("设置订单密码", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
//														Util.setToast(context, "-  标记 4  -");
                                //去设置密码
                                operSetOrderPwd();
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


                    // 20302
                    if (jsonObject.getInt("error_code") == PC_RESET_PSD || !isCheckOrderPwd) {
                        // 输入密码查看
//						Util.setToast(context, "-  标记 5  -");
                        operCheckOrderPwd();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 给订单设置密码
     */
    private void operSetOrderPwd() {
        final SettingOrderPwdPopupWindow popuWindow = new SettingOrderPwdPopupWindow(mContext);
        popuWindow.showAtLocation(detail_allorder_loading.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                        requestSetOrderPwd(popuWindow, pwd1, pwd2);
                    }else{
                        Util.setToast(mContext, "两次输入密码不一样，请重新输入");
                        return;
                    }
                }

            }
        });
    }

    /***订单填写密码*/
    private String setOrderPwdUrl = Constant.TOBOSU_URL + "tapp/passport/setOrderPwd";
    /***
     * 提交订单填写密码接口请求
     * @param popuWindow
     * @param pwd1
     * @param pwd2
     */
    protected void requestSetOrderPwd(final SettingOrderPwdPopupWindow popuWindow, String pwd1, String pwd2){
        HashMap setOrderParams = new HashMap<String, Object>();
        setOrderParams.put("token", token);
        setOrderParams.put("orderpwd", MD5Util.md5(pwd1));
        setOrderParams.put("orderpwd1", MD5Util.md5(pwd2));

        OKHttpUtil.post(setOrderPwdUrl, setOrderParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("error_code");
                    switch (code) {
                        case HAS_SET_PSD:  // 已经设置密码  HAS_SET_PSD == 0
                        case PC_RESET_PSD: // pc端重置过密码，需重新验证   PC_RESET_PSD == 20302
                            getSharedPreferences("CheckOrderPwd", 0).edit().putBoolean("CheckOrderPwd", false).commit();

                            // 输入密码来查看
                            operCheckOrderPwd();
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

    /****
     * 需要输入订单密码才能进入全部订单列表
     */
    private void operCheckOrderPwd() {
        final CheckOrderPwdPopupWindow checkOrderPwdPopupWindow = new CheckOrderPwdPopupWindow(mContext);
        checkOrderPwdPopupWindow.showAtLocation(detail_allorder_loading.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        checkOrderPwdPopupWindow.bt_subit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd = checkOrderPwdPopupWindow.et_check_order_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(mContext, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 验证订单秘密
                requestCheckOrderPwd(pwd, checkOrderPwdPopupWindow);
            }
        });
    }


    /***订单验证密码*/
    private String requestOrderPwdUrl = Constant.TOBOSU_URL + "tapp/order/checkOrderPwd";
    /***
     *
     * 订单验证接口请求
     * @param pwd
     * @param poWindow
     */
    private void requestCheckOrderPwd(String pwd, final PopupWindow poWindow) {
        HashMap requestOrderParams = new HashMap<String, Object>();
        requestOrderParams.put("token", token);
        requestOrderParams.put("orderpwd", MD5Util.md5(pwd));
        OKHttpUtil.post(requestOrderPwdUrl, requestOrderParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("error_code") == HAS_SET_PSD) {
                        requestOrderDetailPost();
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

    private void initEvent() {
        detail_allorder_back.setOnClickListener(this);
        detail_allorder_submit.setOnClickListener(this);
        detail_allorder_phone.setOnClickListener(this);
        tv_followup_feedback.setOnClickListener(this);
        tv_orderOperationNum.setOnClickListener(this);
    }


    /***
     * 订单详情接口请求
     */
    public void requestOrderDetailPost() {

        OKHttpUtil.post(orderDetailUrl, orderDetailParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> dataMap = jsonToOrderMap(result);
                        detail_allorder_split.setText(dataMap.get("addtime").toString().substring(0, 10) + " 分单");
                        String str = dataMap.get("order_state").toString();
                        if (str.equals("未量房")) {
                            detail_allorder_state.setBackground(getResources().getDrawable(R.drawable.img_not_lf));
                        } else if (str.equals("已量房")) {
                            detail_allorder_state.setBackground(getResources().getDrawable(R.drawable.img_lf));
                        } else if (str.equals("已签单")) {
                            detail_allorder_state.setBackground(getResources().getDrawable(R.drawable.img_qd));
                        } else if (str.equals("未签单")) {
                            detail_allorder_state.setBackground(getResources().getDrawable(R.drawable.img_not_qd));
                        }
                        detail_allorder_owner.setText(dataMap.get("nick").toString());
                        detail_allorder_district.setText(dataMap.get("village").toString());
                        if ("".equals(dataMap.get("house_type_name").toString())) {
                            detail_allorder_type.setText("其他");
                        } else {
                            detail_allorder_type.setText(dataMap.get("house_type_name").toString());
                        }
                        if ("0".equals(dataMap.get("house_area").toString())) {
                            detail_allorder_area.setText("0㎡");
                        } else {
                            detail_allorder_area.setText(dataMap.get("house_area").toString() + "㎡");
                        }
                        if ("".equals(dataMap.get("decoration_style_name").toString())) {
                            detail_allorder_style.setText("");
                        } else {
                            detail_allorder_style.setText(dataMap.get("decoration_style_name").toString());
                        }
                        detail_allorder_budget.setText(dataMap.get("decoration_budget").toString() + "万");
                        if ("".equals(dataMap.get("decoration_type_name").toString())) {
                            detail_allorder_zxmode.setText("其他");
                        } else {
                            detail_allorder_zxmode.setText(dataMap.get("decoration_type_name").toString());
                        }
                        detail_allorder_hxstructure.setText(dataMap.get("house_layout_name").toString());
                        if ("0".equals(dataMap.get("house_old").toString())) {
                            detail_allorder_oldreform.setText("否");
                        } else {
                            detail_allorder_oldreform.setText("是");
                        }
                        if ("0".equals(dataMap.get("house_key").toString())) {
                            detail_allorder_takekey.setText("否");
                        } else {
                            detail_allorder_takekey.setText("是");
                        }
                        if ("".equals(dataMap.get("decoration_class_name").toString())) {
                            detail_allorder_zxtype.setText("其他");
                        } else {
                            detail_allorder_zxtype.setText(dataMap.get("decoration_class_name").toString());

                        }

                        detail_allorder_addressqu.setText(dataMap.get("address").toString());
                        detail_allorder_other.setText(dataMap.get("attach_demand").toString());
                        nick = dataMap.get("nick").toString();
                        mobile = dataMap.get("mobile").toString();
                        detail_allorder_user.setText(nick + ":" + mobile);

                        if (start_time != null && start_time.length() > 0) {
                            layout_one.setVisibility(View.VISIBLE);
                            detail_allorder_start_time.setText(start_time);
                        } else {
                            layout_one.setVisibility(View.GONE);
                        }
                        if (visit_time != null && visit_time.length() > 0) {
                            layout_two.setVisibility(View.VISIBLE);
                            detail_allorder_visit_time.setText(visit_time);
                        } else {
                            layout_two.setVisibility(View.GONE);
                            detail_image_start.setImageResource(R.drawable.flag_bottom_on);
                            detail_allorder_start_time.setTextColor(Color.rgb(255, 156, 0));
                            detail_allorder_start.setTextColor(Color.rgb(255, 156, 0));
                        }
                        if (lftime != null && lftime.length() > 0) {
                            layout_three.setVisibility(View.VISIBLE);
                            detail_allorder_lf_time.setText(lftime);
                            detail_allorder_submit.setVisibility(View.GONE);
                        } else {
                            layout_three.setVisibility(View.GONE);
                            detail_image_visit.setImageResource(R.drawable.flag_on);
                            detail_allorder_visit_time.setTextColor(Color.rgb(255, 156, 0));
                            detail_allorder_visit.setTextColor(Color.rgb(255, 156, 0));
                            detail_allorder_submit.setVisibility(View.VISIBLE);
                        }
                        if (tradetime != null && tradetime.length() > 0) {
                            layout_four.setVisibility(View.VISIBLE);
                            detail_allorder_trade_time.setText(tradetime);
                        } else {
                            layout_four.setVisibility(View.GONE);
                            detail_image_lf.setImageResource(R.drawable.flag_on);
                            detail_allorder_lf_time.setTextColor(Color.rgb(255, 156, 0));
                            detail_allorder_lf.setTextColor(Color.rgb(255, 156, 0));
                        }
                    }
                });
            }
        });
    }

    ;

    private Map<String, Object> jsonToOrderMap(String jsonString) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject object1 = new JSONObject(jsonString);
            JSONObject object = object1.getJSONObject("data");

            map.put("addtime", object.get("addtime").toString());
            map.put("order_state", object.get("order_state").toString());
            map.put("nick", object.get("nick").toString());
            map.put("village", object.get("village").toString());
            map.put("house_type_name", object.get("house_type_name").toString());
            map.put("house_area", object.get("house_area").toString());
            map.put("decoration_style_name", object.get("decoration_style_name").toString());
            map.put("decoration_budget", object.get("decoration_budget").toString());
            map.put("decoration_type_name", object.get("decoration_type_name").toString());
            map.put("house_layout_name", object.get("house_layout_name").toString());

            map.put("house_old", object.get("house_old").toString());
            map.put("house_key", object.get("house_key").toString());
            map.put("decoration_class_name", object.get("decoration_class_name").toString());
            map.put("address", object.get("address").toString());
            map.put("attach_demand", object.get("attach_demand").toString());
            map.put("mobile", object.get("mobile").toString());
            start_time = object.get("start_time").toString();
            visit_time = object.get("visit_time").toString();
            lftime = object.get("lftime").toString();
            tradetime = object.get("tradetime").toString();
            if ("0".equals(object.getString("orderOperationNum"))) {
                tv_orderOperationNum.setVisibility(View.GONE);
            } else {
                tv_orderOperationNum.setVisibility(View.VISIBLE);
                tv_orderOperationNum.setText("跟踪记录 ( " + object.getString("orderOperationNum") + " )");
                Log.d(TAG, "跟踪记录数据：" + object.getString("orderOperationNum"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return map;
        }
        detail_allorder_loading.setVisibility(View.GONE);
        return map;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_allorder_back:
                finish();
                break;
            case R.id.tv_followup_feedback:
            case R.id.tv_orderOperationNum:
                Intent intent = new Intent(this, OrderFeedBackActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("order_id", order_id);
                startActivity(intent);
                break;
            case R.id.detail_allorder_submit:
                operLf();
                break;
            case R.id.detail_allorder_phone:
                String cellphone = "";
                String tel = mobile;
                CallDialogCompany callDialog = new CallDialogCompany(AllOrderDetailActivity.this, R.style.callDialogTheme, cellphone, tel);
                window = callDialog.getWindow();
                params = window.getAttributes();
                params.width = LayoutParams.MATCH_PARENT;
                window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                callDialog.show();
                break;
            default:
                break;
        }

    }

    /***
     * 操作量房
     */
    private void operLf() {
        final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final LfPwdPopupWindow popupWindow = new LfPwdPopupWindow(getApplicationContext());
        popupWindow.showAtLocation(detail_allorder_back.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.bt_subit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String recordcontent = popupWindow.et_lf_situation.getText().toString().trim();
                String lftime = sDateFormat.format(new java.util.Date());
                if (TextUtils.isEmpty(recordcontent)) {
                    Toast.makeText(mContext, "量房详情不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                orderParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
                orderParams.put("recordcontent", recordcontent);
                orderParams.put("token", token);
                orderParams.put("lftime", lftime);
                orderParams.put("orderid", order_id);

                OKHttpUtil.post(lfangUrl, orderParams, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("result", result);
                                try {
                                    JSONObject object = new JSONObject(result);
                                    if (object.getInt("error_code") == 0) {
                                        Toast.makeText(mContext, "量房成功！", Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    } else {
                                        Toast.makeText(mContext, "量房失败！", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

}
