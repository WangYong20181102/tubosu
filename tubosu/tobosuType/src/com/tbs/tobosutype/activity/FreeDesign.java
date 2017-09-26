package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomWaitDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 智能报价 页面
 * author: L
 */

public class FreeDesign extends Activity {
    private Context mContext;
    private String TAG = "FreeDesign";
    private String mCityName = "";
    private CustomWaitDialog customWaitDialog;

    private ImageView fdBack;//顶部返回按钮
    private TextView fdTextCity;//选择的城市
    private ImageView fdCitySelect;//选择城市按钮
    private RelativeLayout fdCity;//城市选择框
    private RelativeLayout fd_title;//顶部banner

    private EditText fdTextMyHomeAreaNum;//我家的面积大小

    private EditText fdTextMyHomeRoomNum;//房间数量
    private ImageView fdBtnRoomDown;//按钮房间数量减少
    private ImageView fdBtnRoomUp;//按钮房间数量减少

    private EditText fdTextMyHomeTing;
    private ImageView fdBtnTingDown;//按钮厅数量减少
    private ImageView fdBtnTingUp;//按钮厅数量减少

    private EditText fdTextMyHomeChu;
    private ImageView fdBtnChuDown;
    private ImageView fdBtnChuUp;

    private EditText fdTextMyHomeWei;
    private ImageView fdBtnWeiDown;
    private ImageView fdBtnWeiUp;

    private EditText fdTextMyHomeYangTai;
    private ImageView fdBtnYangTaiDown;
    private ImageView fdBtnYangTaiUp;

    private EditText fdEditPhoneNum;
    private ImageView fdBtnOk;//发单跳转按钮

    private final int FIRST_TIER_CITITES = 1;//一线城市
    private final int SECOND_TIER_STRONG_CITITES = 2;//二线强城市
    private final int SECOND_TIER_CITITES = 3;//二线城市
    private final int OTHER_CITITES = 4;//其他城市

    private RequestParams pubOrderParams;//请求参数

    private String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTitleBarColor(FreeDesign.this, R.color.bg_fd_title);
        setContentView(R.layout.activity_free_design);
        mContext = FreeDesign.this;
        customWaitDialog = new CustomWaitDialog(mContext);
        pubOrderParams = AppInfoUtil.getPublicParams(getApplicationContext());//初始化参数
        bindView();
        initView();
        Log.e("获取绑定状态", "=====" + AppInfoUtil.getCellPhone(this));
    }


    private void bindView() {
        fdBack = (ImageView) findViewById(R.id.fd_back);
        fdTextCity = (TextView) findViewById(R.id.fd_text_city);
        fdCitySelect = (ImageView) findViewById(R.id.fd_city_select);
        fdTextMyHomeAreaNum = (EditText) findViewById(R.id.fd_text_my_home_area_num);
        fdCity = (RelativeLayout) findViewById(R.id.fd_city);
        fd_title = (RelativeLayout) findViewById(R.id.fd_title);
        fd_title.setBackgroundColor(Color.parseColor("#ff882e"));

        fdTextMyHomeRoomNum = (EditText) findViewById(R.id.fd_text_my_home_room_num);
        fdTextMyHomeRoomNum.setTag("myRoomNum");
        fdBtnRoomDown = (ImageView) findViewById(R.id.fd_btn_room_down);
        fdBtnRoomUp = (ImageView) findViewById(R.id.fd_btn_room_up);

        fdTextMyHomeTing = (EditText) findViewById(R.id.fd_text_my_home_ting_num);
        fdBtnTingDown = (ImageView) findViewById(R.id.fd_btn_ting_down);
        fdBtnTingUp = (ImageView) findViewById(R.id.fd_btn_ting_up);

        fdTextMyHomeChu = (EditText) findViewById(R.id.fd_text_my_home_chu_num);
        fdBtnChuDown = (ImageView) findViewById(R.id.fd_btn_chu_down);
        fdBtnChuUp = (ImageView) findViewById(R.id.fd_btn_chu_up);

        fdTextMyHomeWei = (EditText) findViewById(R.id.fd_text_my_home_wei_num);
        fdBtnWeiDown = (ImageView) findViewById(R.id.fd_btn_wei_down);
        fdBtnWeiUp = (ImageView) findViewById(R.id.fd_btn_wei_up);

        fdTextMyHomeYangTai = (EditText) findViewById(R.id.fd_text_my_home_yangtai_num);
        fdBtnYangTaiDown = (ImageView) findViewById(R.id.fd_btn_yangtai_down);
        fdBtnYangTaiUp = (ImageView) findViewById(R.id.fd_btn_yangtai_up);

        fdEditPhoneNum = (EditText) findViewById(R.id.fd_edit_phone_num);
        fdBtnOk = (ImageView) findViewById(R.id.fd_btn_ok);
    }

    private void initView() {
        //定位城市初始化
        initLocationCity();
        //按钮点击事件
        fdBack.setOnClickListener(occl);
        fdCitySelect.setOnClickListener(occl);
        fdCity.setOnClickListener(occl);

        fdBtnRoomDown.setOnClickListener(occl);
        fdBtnRoomUp.setOnClickListener(occl);

        fdBtnTingDown.setOnClickListener(occl);
        fdBtnTingUp.setOnClickListener(occl);

        fdBtnChuDown.setOnClickListener(occl);
        fdBtnChuUp.setOnClickListener(occl);

        fdBtnWeiDown.setOnClickListener(occl);
        fdBtnWeiUp.setOnClickListener(occl);

        fdBtnYangTaiDown.setOnClickListener(occl);
        fdBtnYangTaiUp.setOnClickListener(occl);

        fdBtnOk.setOnClickListener(occl);

        //文本输入框监听事件
        fdTextMyHomeAreaNum.addTextChangedListener(tw);
        fdTextMyHomeRoomNum.addTextChangedListener(roomTw);

    }

    private void initLocationCity() {
        mCityName = getSharedPreferences("Save_City_Info", MODE_PRIVATE).getString("save_city_now", "未定位");
        fdTextCity.setText("" + mCityName);
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fd_back:
                    finish();
                    break;
                case R.id.fd_city_select:
                case R.id.fd_city:
                    //跳转到选择城市界面
                    Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                    Bundle b = new Bundle();
                    b.putString("fromFreeDesign", "66");
                    selectCityIntent.putExtra("freeDesignBundle", b);
                    startActivityForResult(selectCityIntent, 3);
                    break;
                case R.id.fd_btn_room_down:
                    onClickChangeNum(fdTextMyHomeRoomNum, 0);
                    break;
                case R.id.fd_btn_room_up:
                    onClickChangeNum(fdTextMyHomeRoomNum, 1);
                    break;
                case R.id.fd_btn_ting_down:
                    onClickChangeNum(fdTextMyHomeTing, 0);
                    break;
                case R.id.fd_btn_ting_up:
                    onClickChangeNum(fdTextMyHomeTing, 1);
                    break;
                case R.id.fd_btn_chu_down:
                    onClickChangeNum(fdTextMyHomeChu, 0);
                    break;
                case R.id.fd_btn_chu_up:
                    onClickChangeNum(fdTextMyHomeChu, 1);
                    break;
                case R.id.fd_btn_wei_down:
                    onClickChangeNum(fdTextMyHomeWei, 0);
                    break;
                case R.id.fd_btn_wei_up:
                    onClickChangeNum(fdTextMyHomeWei, 1);
                    break;
                case R.id.fd_btn_yangtai_down:
                    onClickChangeNum(fdTextMyHomeYangTai, 0);
                    break;
                case R.id.fd_btn_yangtai_up:
                    onClickChangeNum(fdTextMyHomeYangTai, 1);
                    break;
                case R.id.fd_btn_ok:
//                    Toast.makeText(mContext, "当前渠道版本" + AppInfoUtil.getChannType(mContext) + "====当前的PIPE===" + Constant.PIPE, Toast.LENGTH_LONG).show();
                    if (AppInfoUtil.getTypeid(mContext).equals("3")) {
                        //当前为装修公司用户拦截其发单
                        Toast.makeText(mContext, "当前登录用户为装修公司无法获取报价", Toast.LENGTH_LONG).show();
                    } else {
                        //非装修公司进入发单页面
                        okBtnMessag();
                    }
                    break;
            }
        }
    };

    /**
     * 判断网络是否可用
     * 检测发布需求的手机号是否为业主
     * 若是业主用户且已经绑定了该手机号则不显示下一个界面的获取验证码界面
     * 若是业主未绑定手机号码则显示验证框 且用户验证后绑定该手机号
     * 非业主用户填写手机验证后则变为业主  相当于手机注册
     * 将获取的数据做发单处理并且跳转页面
     */
    private void okBtnMessag() {
        String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
        if (Constant.checkNetwork(mContext)) {
            if (!TextUtils.isEmpty(fdEditPhoneNum.getText().toString())) {
                if (fdEditPhoneNum.getText().toString().matches(MOBILE)) {
                    if (fdTextMyHomeRoomNum.getText().toString().equals("0")
                            || fdTextMyHomeAreaNum.getText().toString().equals("0")) {
                        Toast.makeText(mContext, "当前输入的面积或卧室数量不能为0", Toast.LENGTH_LONG).show();

                    } else {
                        //计算半包价格
                        if (!TextUtils.isEmpty(fdTextCity.getText().toString().trim())) {
                            //城市选择不为空 判断手机号码是否是业主绑定的手机号码 将计算出的半包价格传入下一个页面
                            if (fdTextCity.getText().toString().trim().equals("未定位")) {
                                Toast.makeText(mContext, "当前城市未定位，请选择城市~", Toast.LENGTH_SHORT).show();
                            } else {
                                customWaitDialog.show();
                                setParams();//设置请求接口参数
                                HttpRequestPubOrder(pubOrderParams);
                            }

                        } else {
                            Toast.makeText(mContext, "当前定位的城市为空，请选择城市", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, "当前无网络连接", Toast.LENGTH_LONG).show();
        }

    }


    /**
     * creat by lin
     * 用于计算半包总价格
     * mAreas 用户输入选择的面积
     * mRegion地区
     */

    private int countMoney(int mAreas, int mRegion) {
        if (mRegion == FIRST_TIER_CITITES) {
            //一线城市计算价格
            return mAreas * 550;
        } else if (mRegion == SECOND_TIER_STRONG_CITITES) {
            //二线强城市
            return mAreas * 480;

        } else if (mRegion == SECOND_TIER_CITITES) {
            //二线城市
            return mAreas * 440;
        } else if (mRegion == OTHER_CITITES) {
            //其他城市
            return mAreas * 380;
        } else {
            return 0;
        }
    }

    /**
     * 根据获取定位的城市返回城市类型
     * 类型一线 二线等城市
     */
    private int getCityClass(String mCityName) {
        String[] firstTier = {"北京", "上海", "广州", "深圳", "天津"};
        String[] secondStrongTier = {"杭州", "南京", "济南", "重庆", "青岛", "大连", "宁波", "厦门"};
        String[] secondTier = {"成都", "武汉", "哈尔滨", "沈阳", "西安", "长春", "长沙", "福州", "郑州", "石家庄", "苏州", "佛山", "东莞", "无锡", "烟台", "太原", "合肥", "南昌", "南宁", "昆明", "温州", "淄博", "唐山"};
        for (String s : firstTier) {
            if (s.equals(mCityName)) {
                return FIRST_TIER_CITITES;
            }
        }
        for (String s : secondStrongTier) {
            if (s.equals(mCityName)) {
                return SECOND_TIER_STRONG_CITITES;
            }
        }
        for (String s : secondTier) {
            if (s.equals(mCityName)) {
                return SECOND_TIER_CITITES;
            }
        }
        return OTHER_CITITES;
    }

    private TextWatcher roomTw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int roomNum;
                if (s.toString().equals("") || TextUtils.isEmpty(s.toString())) {
                    roomNum = 0;
                } else {
                    Log.e(TAG, "键入的数据=========" + s.toString());
                    roomNum = Integer.valueOf(s.toString()).intValue();
                }
                if (roomNum == 0) {
                    Toast.makeText(mContext, "请输入正确的卧室数量", Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    };
    //输入面积数时文本输入监听
    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //根据监听的数据改变房间的组合
            try {
                int homeArea;
                if ("".equals(s.toString()) || TextUtils.isEmpty(s.toString())) {
                    homeArea = 0;
                } else {
                    homeArea = Integer.valueOf(s.toString()).intValue();
                }

                if (homeArea > 0 && homeArea <= 59) {
                    fdTextMyHomeRoomNum.setText("" + 1);
                    fdTextMyHomeTing.setText("" + 1);
                    fdTextMyHomeChu.setText("" + 1);
                    fdTextMyHomeWei.setText("" + 1);
                    fdTextMyHomeYangTai.setText("" + 1);
                } else if (homeArea >= 60 && homeArea <= 89) {
                    fdTextMyHomeRoomNum.setText("" + 2);
                    fdTextMyHomeTing.setText("" + 1);
                    fdTextMyHomeChu.setText("" + 1);
                    fdTextMyHomeWei.setText("" + 1);
                    fdTextMyHomeYangTai.setText("" + 1);
                } else if (homeArea >= 90 && homeArea <= 149) {
                    fdTextMyHomeRoomNum.setText("" + 3);
                    fdTextMyHomeTing.setText("" + 2);
                    fdTextMyHomeChu.setText("" + 1);
                    fdTextMyHomeWei.setText("" + 2);
                    fdTextMyHomeYangTai.setText("" + 1);
                } else if (homeArea >= 150) {
                    fdTextMyHomeRoomNum.setText("" + 4);
                    fdTextMyHomeTing.setText("" + 2);
                    fdTextMyHomeChu.setText("" + 1);
                    fdTextMyHomeWei.setText("" + 2);
                    fdTextMyHomeYangTai.setText("" + 2);
                } else {
                    Toast.makeText(mContext, "请输入正确的面积", Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 39) {
            if (TextUtils.isEmpty(data.getStringExtra("result"))) {
                mCityName = getSharedPreferences("Save_City_Info", MODE_PRIVATE).getString("save_city_now", mCityName);
                fdTextCity.setText("" + mCityName);
            } else {
                mCityName = data.getStringExtra("result");
                fdTextCity.setText("" + mCityName);
            }
        }
    }

    /**
     * @param edit  文本框
     * @param mType 点击事件类型 0--减少   1--增加
     */
    private void onClickChangeNum(EditText edit, int mType) {
        try {

            int houseNum;
            if (TextUtils.isEmpty(edit.getText().toString())) {
                houseNum = 0;
            } else {
                houseNum = Integer.valueOf(edit.getText().toString()).intValue();
            }
            if (mType == 0) {
                //减少数据
                if (houseNum > 0) {
                    edit.setText("" + (houseNum - 1));
                } else {
                    Toast.makeText(mContext, "数量不能小于0", Toast.LENGTH_LONG).show();
                }

            } else {
                //增加数据
                edit.setText("" + (houseNum + 1));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //设定参数
    private void setParams() {
        pubOrderParams.put("housearea", fdTextMyHomeAreaNum.getText().toString());//装修面积
        int area = Integer.valueOf(fdTextMyHomeAreaNum.getText().toString()).intValue();
        int mPrice = countMoney(area, getCityClass(fdTextCity.getText().toString().trim()));
//        pubOrderParams.put("orderprice", mPrice);//装修预算总价
        pubOrderParams.put("decorate_price", mPrice);
        pubOrderParams.put("cellphone", fdEditPhoneNum.getText().toString());//用户的手机号码
        pubOrderParams.put("city", fdTextCity.getText().toString());//用户所选的城市
        pubOrderParams.put("urlhistory", Constant.PIPE);
        // 发单入口
        pubOrderParams.put("comeurl", Constant.PIPE);
        if (!TextUtils.isEmpty(userid)) {
            pubOrderParams.put("userid", userid);
        } else {
            pubOrderParams.put("userid", "0");
        }
        pubOrderParams.put("source", "1111");//渠道Id  Android版本渠道

        Log.e("上传参数", "==定位城市==" + fdTextCity.getText().toString() + "==总价==" + mPrice + "==面积==" + fdTextMyHomeAreaNum.getText().toString());
    }

    //发单请求
    private void HttpRequestPubOrder(RequestParams params) {
        HttpServer.getInstance().requestPOST(Constant.PUB_ORDERS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    Log.e("发单", "===接口返回的数据====" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("error_code") == 0) {
                        //发单成功，进行页面的跳转
                        Log.e("发单成功", "===接口返回的数据====" + result);
                        IntoFreeDesignPrice();
                    } else {
                        //请求失败
                        customWaitDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                customWaitDialog.dismiss();
                Log.e("服务器请求失败", "======" + throwable.toString());
                if (throwable.toString().equals("java.net.SocketTimeoutException")) {
                    Toast.makeText(mContext, "网络请求超时请稍后重试！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "服务端请求失败！", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //页面的跳转
    private void IntoFreeDesignPrice() {
        customWaitDialog.dismiss();
        int area = Integer.valueOf(fdTextMyHomeAreaNum.getText().toString()).intValue();
        int mPrice = countMoney(area, getCityClass(fdTextCity.getText().toString().trim()));
        Intent intent = new Intent(FreeDesign.this, FreeDesignPrice.class);
        intent.putExtra("mPhoneNum", fdEditPhoneNum.getText().toString());//下个界面要用的电话号码

        intent.putExtra("mIsLogin", AppInfoUtil.getToekn(FreeDesign.this));

        intent.putExtra("mPrice", mPrice);//半包总价
        intent.putExtra("mWeiPrice", (float) (mPrice * 0.1263));//卫生间装修价格
        intent.putExtra("mTingPrice", (float) (mPrice * 0.2281));//客厅装修价格
        intent.putExtra("mChuPrice", (float) (mPrice * 0.0919));//厨房装修价格
        intent.putExtra("mWoshiPrice", (float) (mPrice * 0.2637));//卧室装修价格
        intent.putExtra("mYangtaiPrice", (float) (mPrice * 0.09));//阳台装修价格
        intent.putExtra("mOtherPrice", (float) (mPrice * 0.2));//其他装修价格
        startActivity(intent);
        finish();
    }

}
