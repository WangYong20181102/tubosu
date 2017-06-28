package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class SmartQuote extends AppCompatActivity {
    private Context mContext;
    private String TAG = "SmartQuote";
    private String mCityName = "";
    private CustomWaitDialog customWaitDialog;
    private int mPrice = 0;//计算总价

    private TextView fd_num_preson;

    private ImageView fdBack;//顶部返回按钮
    private TextView fdTextCity;//选择的城市
    private ImageView fdCitySelect;//选择城市按钮
    private RelativeLayout fdCity;//城市选择框

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
    private TextView fdBtnOk;//发单跳转按钮

    private LinearLayout LLResultView;//发单结果视图层

    private final int FIRST_TIER_CITITES = 1;//一线城市
    private final int SECOND_TIER_STRONG_CITITES = 2;//二线强城市
    private final int SECOND_TIER_CITITES = 3;//二线城市
    private final int OTHER_CITITES = 4;//其他城市

    //饼图上计算结果界面
    private TextView fdpPrice;//显示的半包总价
    private TextView fdpTextWeishengjian;//显示卫生间的半包价格
    private TextView fdpTextKeting;//显示客厅的半包价格
    private TextView fdpTextQita;//显示其他的半包价格
    private TextView fdpTextChufang;//显示厨房的半包价格
    private TextView fdpTextWoshi;//显示卧室的半包价格
    private TextView fdpTextYangtai;//显示阳台的半包价格


    private TextView fdp_weishengjian;//百分比
    private TextView fdp_qita;//百分比
    private TextView fdp_yangtai;//百分比
    private TextView fdp_woshi;//百分比
    private TextView fdp_chufang;//百分比
    private TextView fdp_keting;//百分比


    private TextView fdOpenPhone;//打电话
    private TextView fdHuoqubaojia;//免费获取报价 发单

    private ImageView fdpPie;//显示的饼状图

    private TextView pf_piangao;//偏高
    private TextView pf_fuhe;//符合
    private TextView pf_piandi;//偏低

    private RelativeLayout re_download;//下载图层
    private TextView into_download;//去下载按钮
    private ImageView download_close;//关闭下载图层

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_quote);
        mContext = SmartQuote.this;
        bindView();
        initView();
    }

    private void bindView() {
        fd_num_preson = (TextView) findViewById(R.id.fd_num_preson);
        fdBack = (ImageView) findViewById(R.id.fd_back);
        fdTextCity = (TextView) findViewById(R.id.fd_text_city);
        fdCitySelect = (ImageView) findViewById(R.id.fd_city_select);
        fdTextMyHomeAreaNum = (EditText) findViewById(R.id.fd_text_my_home_area_num);
        fdCity = (RelativeLayout) findViewById(R.id.fd_city);

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
        fdBtnOk = (TextView) findViewById(R.id.fd_btn_ok);

        LLResultView = (LinearLayout) findViewById(R.id.fd_result_view);

        //饼图上的数据
        fdpPie = (ImageView) findViewById(R.id.fdp_pie);
        fdpPrice = (TextView) findViewById(R.id.fdp_price);
        fdpTextWeishengjian = (TextView) findViewById(R.id.fdp_text_weishengjian);
        fdpTextKeting = (TextView) findViewById(R.id.fdp_text_keting);
        fdpTextQita = (TextView) findViewById(R.id.fdp_text_qita);
        fdpTextChufang = (TextView) findViewById(R.id.fdp_text_chufang);
        fdpTextWoshi = (TextView) findViewById(R.id.fdp_text_woshi);
        fdpTextYangtai = (TextView) findViewById(R.id.fdp_text_yangtai);

        fdOpenPhone = (TextView) findViewById(R.id.fd_open_phone);
        fdHuoqubaojia = (TextView) findViewById(R.id.fd_huoqu_baojia);

        pf_piangao = (TextView) findViewById(R.id.pf_piangao);
        pf_fuhe = (TextView) findViewById(R.id.pf_fuhe);
        pf_piandi = (TextView) findViewById(R.id.pf_piandi);

        re_download = (RelativeLayout) findViewById(R.id.re_download);
        into_download = (TextView) findViewById(R.id.into_download);
        download_close = (ImageView) findViewById(R.id.download_close);

        fdp_weishengjian = (TextView) findViewById(R.id.fdp_weishengjian);
        fdp_qita = (TextView) findViewById(R.id.fdp_qita);
        fdp_yangtai = (TextView) findViewById(R.id.fdp_yangtai);
        fdp_woshi = (TextView) findViewById(R.id.fdp_woshi);
        fdp_chufang = (TextView) findViewById(R.id.fdp_chufang);
        fdp_keting = (TextView) findViewById(R.id.fdp_keting);
    }

    private void initView() {
        //定位城市初始化
        initLocationCity();
        HttpGetNumPreson();
        fdOpenPhone.setOnClickListener(occl);
        fdHuoqubaojia.setOnClickListener(occl);
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

        pf_piangao.setOnClickListener(occl);
        pf_fuhe.setOnClickListener(occl);
        pf_piandi.setOnClickListener(occl);

        into_download.setOnClickListener(occl);
        download_close.setOnClickListener(occl);
    }

    private void initLocationCity() {
        mCityName = CacheManager.getCity(mContext);
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
                    Intent cityIntent = new Intent(mContext, SelectCityActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("frompop", 31);
                    cityIntent.putExtra("pop_bundle", b);
                    startActivityForResult(cityIntent, 124);
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
                    //计算并显示装修报价的结果
                    fdpPie.setImageResource(R.mipmap.fd_pie);
                    countResult();
                    LLResultView.setVisibility(View.VISIBLE);
                    break;
                case R.id.fd_open_phone:
                    //打电话
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-606-2221"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case R.id.fd_huoqu_baojia:
                    HttpGetFaDan();
                    break;
                case R.id.pf_piangao:
                    if (mPrice == 0) {
                        Toast.makeText(mContext, "您当前还没有计算报价！", Toast.LENGTH_SHORT).show();
                    } else if (!Util.isLogin(mContext)) {
                        //用户未登录
                        startActivity(new Intent(mContext, LoginActivity.class));
                    } else {
                        changBtnState(0);
                        HttpPost(1);
                    }
                    break;
                case R.id.pf_piandi:
                    if (mPrice == 0) {
                        Toast.makeText(mContext, "您当前还没有计算报价！", Toast.LENGTH_SHORT).show();
                    } else if (!Util.isLogin(mContext)) {
                        //用户未登录
                        startActivity(new Intent(mContext, LoginActivity.class));
                    } else {
                        changBtnState(2);
                        HttpPost(3);
                    }
                    break;
                case R.id.pf_fuhe:
                    if (mPrice == 0) {
                        Toast.makeText(mContext, "您当前还没有计算报价！", Toast.LENGTH_SHORT).show();
                    } else if (!Util.isLogin(mContext)) {
                        //用户未登录
                        startActivity(new Intent(mContext, LoginActivity.class));
                    } else {
                        changBtnState(1);
                        HttpPost(2);
                    }
                    break;
                case R.id.download_close:
                    //关闭下载图层
                    re_download.setVisibility(View.GONE);
                    break;
                case R.id.into_download:
                    //去下载
                    Uri uri = Uri.parse("http://t.cn/R4p6kwr");
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                    break;
            }
        }
    };

    private void changBtnState(int type) {
        switch (type) {
            case 0:
                pf_piangao.setBackgroundResource(R.drawable.item_shape_blue);
                pf_piangao.setTextColor(mContext.getResources().getColor(R.color.blue));
                pf_fuhe.setBackgroundResource(R.drawable.item_shape_white);
                pf_fuhe.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                pf_piandi.setBackgroundResource(R.drawable.item_shape_white);
                pf_piandi.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                break;
            case 1:
                pf_fuhe.setBackgroundResource(R.drawable.item_shape_blue);
                pf_fuhe.setTextColor(mContext.getResources().getColor(R.color.blue));
                pf_piangao.setBackgroundResource(R.drawable.item_shape_white);
                pf_piangao.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                pf_piandi.setBackgroundResource(R.drawable.item_shape_white);
                pf_piandi.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                break;
            case 2:
                pf_piandi.setBackgroundResource(R.drawable.item_shape_blue);
                pf_piandi.setTextColor(mContext.getResources().getColor(R.color.blue));
                pf_piangao.setBackgroundResource(R.drawable.item_shape_white);
                pf_piangao.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                pf_fuhe.setBackgroundResource(R.drawable.item_shape_white);
                pf_fuhe.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                break;
        }
    }

    private void countResult() {
        int area;
        if (!TextUtils.isEmpty(fdTextMyHomeAreaNum.getText().toString())) {
            area = Integer.valueOf(fdTextMyHomeAreaNum.getText().toString()).intValue();
        } else {
            area = 0;
        }
        mPrice = countMoney(area, getCityClass(fdTextCity.getText().toString().trim()));
        setText((float) mPrice, (float) (mPrice * 0.1263),
                (float) (mPrice * 0.2281), (float) (mPrice * 0.0919),
                (float) (mPrice * 0.2637), (float) (mPrice * 0.09),
                (float) (mPrice * 0.2));
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

    //计算结果赋值在饼图上
    private void setText(float mPrice, float mWeiPrice, float mTingPrice,
                         float mChuPrice, float mWoshiPrice,
                         float mYangtaiPrice, float mOtherPrice) {
        fdpPrice.setText("¥" + mPrice);
        fdpTextWeishengjian.setText("¥" + mWeiPrice);
        fdpTextKeting.setText("¥" + mTingPrice);
        fdpTextChufang.setText("¥" + mChuPrice);
        fdpTextWoshi.setText("¥" + mWoshiPrice);
        fdpTextYangtai.setText("¥" + mYangtaiPrice);
        fdpTextQita.setText("¥" + mOtherPrice);

        fdp_weishengjian.setText("卫生间12.63%");
        fdp_qita.setText("其他20.00%");
        fdp_yangtai.setText("阳台9.00%");
        fdp_woshi.setText("卧室26.37%");
        fdp_chufang.setText("厨房9.19%");
        fdp_keting.setText("客厅22.81%");
    }

    //评价接口 score  1--偏高  2--符合 3--偏低
    private void HttpPost(int score) {
        Log.e(TAG, "当前获取的报价=======" + fdpPrice.getText().toString());
        String price = fdpPrice.getText().toString().substring(1, fdpPrice.getText().toString().length());
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        if (fdTextCity.getText().toString().contains("市")) {
            param.put("city_name", fdTextCity.getText().toString());
        } else {
            param.put("city_name", fdTextCity.getText().toString() + "市");
        }
        param.put("area", fdTextMyHomeAreaNum.getText().toString());
        param.put("room", fdTextMyHomeRoomNum.getText().toString());
        param.put("hall", fdTextMyHomeTing.getText().toString());
        param.put("kitchen", fdTextMyHomeChu.getText().toString());
        param.put("bathroom", fdTextMyHomeWei.getText().toString());
        param.put("balcony", fdTextMyHomeYangTai.getText().toString());
        param.put("quotes", price);
        param.put("score", score);
        okHttpUtil.post(Constant.QUOTE_RECORD_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "请求结果===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        Toast.makeText(mContext, "感谢您的评价！我们将竭力做到更智能！", Toast.LENGTH_SHORT).show();
                        pf_piandi.setClickable(false);
                        pf_piangao.setClickable(false);
                        pf_fuhe.setClickable(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    //或去使用发单人口数量
    private void HttpGetNumPreson() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        okHttpUtil.post(Constant.GET_BILL_COUNT_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String billCount = data.getString("bill_count");
                        fd_num_preson.setText("已经有" + billCount + "位用户计算了报价");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    private void HttpGetFaDan() {
        String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
        if (!TextUtils.isEmpty(fdEditPhoneNum.getText().toString())) {
            if (fdEditPhoneNum.getText().toString().matches(MOBILE)) {
                OKHttpUtil okHttpUtil = new OKHttpUtil();
                HashMap<String, Object> param = new HashMap<>();
                param.put("cellphone", fdEditPhoneNum.getText().toString());
                param.put("housearea", fdTextMyHomeAreaNum.getText().toString());
                param.put("city", fdTextCity.getText().toString());
                param.put("orderprice", (float) (mPrice / 10000));
                param.put("urlhistory", Constant.PIPE_CODE);
                param.put("comeurl", Constant.PIPE_CODE);
                param.put("source","1111");
                okHttpUtil.post(Constant.PUB_ORDER_URL, param, new OKHttpUtil.BaseCallBack() {
                    @Override
                    public void onSuccess(Response response, String json) {
                        Log.e(TAG, "获取的请求数据======" + json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String errorCode = jsonObject.getString("error_code");
                            if (errorCode.equals("0")) {
                                Toast.makeText(mContext, "获取成功！稍后请您稍等电话！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(Request request, IOException e) {
                        Log.e(TAG, "发单接口请求失败=====" + e.toString());
                    }

                    @Override
                    public void onError(Response response, int code) {
                        Log.e(TAG, "发单接口请求错误=====" + code);
                    }
                });
            } else {
                Toast.makeText(mContext, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "输入的手机号码不为空！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 77:
                String city = data.getBundleExtra("city_bundle").getString("ci");
                CacheManager.setCity(mContext, city);
                fdTextCity.setText("" + city);
                break;
        }
    }
}
