package com.tbs.tobosupicture.activity;

import android.content.Context;
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
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 用黄色的发单页面，此蓝色的发单页面产品说不使用。
 */
public class FreeQuoteActivity extends BaseActivity {
    private String TAG = "FreeQuoteActivity";
    private Context mContext;
    private String mCityName = "";//选择的城市名称
    private int mPrice = 0;//计算的总价
    private ArrayList<String> mImageList = new ArrayList<>();//临时测试用的集合


    private final int FIRST_TIER_CITITES = 1;//一线城市
    private final int SECOND_TIER_STRONG_CITITES = 2;//二线强城市
    private final int SECOND_TIER_CITITES = 3;//二线城市
    private final int OTHER_CITITES = 4;//其他城市


    @BindView(R.id.fd_back)
    ImageView fdBack;//返回按钮
    @BindView(R.id.fd_title)
    RelativeLayout fdTitle;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.fd_num_preson)
    TextView fdNumPreson;//使用报价的人数  已经+请求回来的数据+有位用户计算了报价
    @BindView(R.id.fd_text_city)
    TextView fdTextCity;//定位的城市 可点击进行城市的切换
    @BindView(R.id.fd_city_select)
    ImageView fdCitySelect;//选择切换城市
    @BindView(R.id.fd_city)
    RelativeLayout fdCity;//整个城市选择框
    @BindView(R.id.fd_text_my_home_area)
    TextView fdTextMyHomeArea;
    @BindView(R.id.fd_text_my_home_area_num)
    EditText fdTextMyHomeAreaNum;//输入的家庭面积数
    @BindView(R.id.fd_my_home_area)
    RelativeLayout fdMyHomeArea;
    @BindView(R.id.fd_text_my_home_room_num)
    EditText fdTextMyHomeRoomNum;//输入房间的数量
    @BindView(R.id.fd_btn_room_down)
    ImageView fdBtnRoomDown;//房间数量减少
    @BindView(R.id.fd_btn_room_up)
    ImageView fdBtnRoomUp;//房间数量增加
    @BindView(R.id.fd_my_home_room)
    RelativeLayout fdMyHomeRoom;
    @BindView(R.id.fd_text_my_home_ting_num)
    EditText fdTextMyHomeTingNum;//客厅的数量
    @BindView(R.id.fd_btn_ting_down)
    ImageView fdBtnTingDown;//客厅数量减少
    @BindView(R.id.fd_btn_ting_up)
    ImageView fdBtnTingUp;//客厅数量增加
    @BindView(R.id.fd_my_home_ting)
    RelativeLayout fdMyHomeTing;
    @BindView(R.id.fd_text_my_home_chu_num)
    EditText fdTextMyHomeChuNum;//厨房数量
    @BindView(R.id.fd_btn_chu_down)
    ImageView fdBtnChuDown;//厨房数量减少
    @BindView(R.id.fd_btn_chu_up)
    ImageView fdBtnChuUp;//厨房数量增加
    @BindView(R.id.fd_my_home_chu)
    RelativeLayout fdMyHomeChu;
    @BindView(R.id.fd_text_my_home_wei_num)
    EditText fdTextMyHomeWeiNum;//卫生间数量
    @BindView(R.id.fd_btn_wei_down)
    ImageView fdBtnWeiDown;//卫生间数量减少
    @BindView(R.id.fd_btn_wei_up)
    ImageView fdBtnWeiUp;//卫生间数量增加
    @BindView(R.id.fd_my_home_wei)
    RelativeLayout fdMyHomeWei;
    @BindView(R.id.fd_text_my_home_yangtai_num)
    EditText fdTextMyHomeYangtaiNum;//阳台数量
    @BindView(R.id.fd_btn_yangtai_down)
    ImageView fdBtnYangtaiDown;//阳台数量减少
    @BindView(R.id.fd_btn_yangtai_up)
    ImageView fdBtnYangtaiUp;//阳台数量增加
    @BindView(R.id.fd_my_home_yangtai)
    RelativeLayout fdMyHomeYangtai;
    @BindView(R.id.fd_btn_ok)
    TextView fdBtnOk;//进行报价计算的按钮
    @BindView(R.id.fdp_price)
    TextView fdpPrice;//显示总价
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.fdp_pie)
    ImageView fdpPie;//饼状图  计算出的结果要显示彩色的饼状图
    @BindView(R.id.fdp_weishengjian)
    TextView fdpWeishengjian;//显示的卫生间字段 计算完数据之后要进行数据的更改 将文字描述的数据后面的问号去掉
    @BindView(R.id.fdp_text_weishengjian)
    TextView fdpTextWeishengjian;//计算之后卫生间的价格
    @BindView(R.id.fdp_keting)
    TextView fdpKeting;//字段的显示
    @BindView(R.id.fdp_text_keting)
    TextView fdpTextKeting;//计算之后的客厅价格
    @BindView(R.id.fdp_qita)
    TextView fdpQita;//其他字段
    @BindView(R.id.fdp_text_qita)
    TextView fdpTextQita;//计算之后的其他的价格
    @BindView(R.id.fdp_chufang)
    TextView fdpChufang;//厨房字段
    @BindView(R.id.fdp_text_chufang)
    TextView fdpTextChufang;//显示计算之后的厨房价格
    @BindView(R.id.fdp_woshi)
    TextView fdpWoshi;//卧室字段
    @BindView(R.id.fdp_text_woshi)
    TextView fdpTextWoshi;//显示计算之后的卧室价格
    @BindView(R.id.fdp_yangtai)
    TextView fdpYangtai;//阳台字段
    @BindView(R.id.fdp_text_yangtai)
    TextView fdpTextYangtai;//显示计算之后的阳台字段
    @BindView(R.id.pf_piangao)
    TextView pfPiangao;//处理计算价格之后的反馈
    @BindView(R.id.pf_fuhe)
    TextView pfFuhe;
    @BindView(R.id.pf_piandi)
    TextView pfPiandi;
    @BindView(R.id.fd_phone)
    TextView fdPhone;
    @BindView(R.id.fd_edit_phone_num)
    EditText fdEditPhoneNum;//输入手机号码的输入框
    @BindView(R.id.fd_phone_num)
    RelativeLayout fdPhoneNum;
    @BindView(R.id.fd_huoqu_baojia)
    TextView fdHuoquBaojia;//获取详细的报价  进行发单的请求
    @BindView(R.id.fd_open_phone)
    TextView fdOpenPhone;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.into_download)
    TextView intoDownload;//去土拨鼠App的下载
    @BindView(R.id.download_close)
    ImageView downloadClose;//关闭下载的浮层
    @BindView(R.id.re_download)
    RelativeLayout reDownload;
    @BindView(R.id.fd_result_view)
    LinearLayout fdResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_quote);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    //初始化
    private void initViewEvent() {
        initLocationCity();    //初始化用户的定位的城市
        HttpGetNumPreson();//获取使用该页面的人数
        fdTextMyHomeAreaNum.addTextChangedListener(tw);
        fdTextMyHomeRoomNum.addTextChangedListener(roomTv);
    }


    private void initLocationCity() {
        //读取本地存储的定位信息
        fdTextCity.setText("" + SpUtils.getLocationCity(mContext));
    }

    private TextWatcher roomTv = new TextWatcher() {
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
    //输入面积数时的文本监听
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
                    fdTextMyHomeTingNum.setText("" + 1);
                    fdTextMyHomeChuNum.setText("" + 1);
                    fdTextMyHomeWeiNum.setText("" + 1);
                    fdTextMyHomeYangtaiNum.setText("" + 1);
                } else if (homeArea >= 60 && homeArea <= 89) {
                    fdTextMyHomeRoomNum.setText("" + 2);
                    fdTextMyHomeTingNum.setText("" + 1);
                    fdTextMyHomeChuNum.setText("" + 1);
                    fdTextMyHomeWeiNum.setText("" + 1);
                    fdTextMyHomeYangtaiNum.setText("" + 1);
                } else if (homeArea >= 90 && homeArea <= 149) {
                    fdTextMyHomeRoomNum.setText("" + 3);
                    fdTextMyHomeTingNum.setText("" + 2);
                    fdTextMyHomeChuNum.setText("" + 1);
                    fdTextMyHomeWeiNum.setText("" + 2);
                    fdTextMyHomeYangtaiNum.setText("" + 1);
                } else if (homeArea >= 150) {
                    fdTextMyHomeRoomNum.setText("" + 4);
                    fdTextMyHomeTingNum.setText("" + 2);
                    fdTextMyHomeChuNum.setText("" + 1);
                    fdTextMyHomeWeiNum.setText("" + 2);
                    fdTextMyHomeYangtaiNum.setText("" + 2);
                } else {
                    Toast.makeText(mContext, "请输入正确的面积", Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @OnClick({R.id.fd_back, R.id.fd_title, R.id.iv_img, R.id.fd_num_preson,
            R.id.fd_text_city, R.id.fd_city_select, R.id.fd_city,
            R.id.fd_text_my_home_area, R.id.fd_text_my_home_area_num,
            R.id.fd_my_home_area, R.id.fd_text_my_home_room_num,
            R.id.fd_btn_room_down, R.id.fd_btn_room_up, R.id.fd_my_home_room,
            R.id.fd_text_my_home_ting_num, R.id.fd_btn_ting_down,
            R.id.fd_btn_ting_up, R.id.fd_my_home_ting, R.id.fd_text_my_home_chu_num,
            R.id.fd_btn_chu_down, R.id.fd_btn_chu_up, R.id.fd_my_home_chu,
            R.id.fd_text_my_home_wei_num, R.id.fd_btn_wei_down, R.id.fd_btn_wei_up,
            R.id.fd_my_home_wei, R.id.fd_text_my_home_yangtai_num,
            R.id.fd_btn_yangtai_down, R.id.fd_btn_yangtai_up,
            R.id.fd_my_home_yangtai, R.id.fd_btn_ok, R.id.fdp_price,
            R.id.relativeLayout, R.id.fdp_pie, R.id.fdp_weishengjian,
            R.id.fdp_text_weishengjian, R.id.fdp_keting, R.id.fdp_text_keting,
            R.id.fdp_qita, R.id.fdp_text_qita, R.id.fdp_chufang, R.id.fdp_text_chufang,
            R.id.fdp_woshi, R.id.fdp_text_woshi, R.id.fdp_yangtai,
            R.id.fdp_text_yangtai, R.id.pf_piangao, R.id.pf_fuhe, R.id.pf_piandi,
            R.id.fd_phone, R.id.fd_edit_phone_num, R.id.fd_phone_num,
            R.id.fd_huoqu_baojia, R.id.fd_open_phone, R.id.imageView2,
            R.id.into_download, R.id.download_close, R.id.re_download,
            R.id.fd_result_view})
    public void onViewClickedInFreeQuoteActivity(View view) {
        switch (view.getId()) {
            case R.id.fd_back:
                finish();
                break;
            case R.id.iv_img:
                break;
            case R.id.fd_num_preson:
                break;
            case R.id.fd_text_city:
                break;
            case R.id.fd_city_select:
                break;
            case R.id.fd_city:
                break;
            case R.id.fd_text_my_home_area:
                break;
            case R.id.fd_text_my_home_area_num:
                break;
            case R.id.fd_my_home_area:
                break;
            case R.id.fd_text_my_home_room_num:
                break;
            case R.id.fd_btn_room_down:
                onClickChangeNum(fdTextMyHomeRoomNum, 0);
                break;
            case R.id.fd_btn_room_up:
                onClickChangeNum(fdTextMyHomeRoomNum, 1);
                break;
            case R.id.fd_my_home_room:
                break;
            case R.id.fd_text_my_home_ting_num:
                break;
            case R.id.fd_btn_ting_down:
                onClickChangeNum(fdTextMyHomeTingNum, 0);
                break;
            case R.id.fd_btn_ting_up:
                onClickChangeNum(fdTextMyHomeTingNum, 1);
                break;
            case R.id.fd_my_home_ting:
                break;
            case R.id.fd_text_my_home_chu_num:
                break;
            case R.id.fd_btn_chu_down:
                onClickChangeNum(fdTextMyHomeChuNum, 0);
                break;
            case R.id.fd_btn_chu_up:
                onClickChangeNum(fdTextMyHomeChuNum, 1);
                break;
            case R.id.fd_my_home_chu:
                break;
            case R.id.fd_text_my_home_wei_num:
                break;
            case R.id.fd_btn_wei_down:
                onClickChangeNum(fdTextMyHomeWeiNum, 0);
                break;
            case R.id.fd_btn_wei_up:
                onClickChangeNum(fdTextMyHomeWeiNum, 1);
                break;
            case R.id.fd_my_home_wei:
                break;
            case R.id.fd_text_my_home_yangtai_num:
                break;
            case R.id.fd_btn_yangtai_down:
                onClickChangeNum(fdTextMyHomeYangtaiNum, 0);
                break;
            case R.id.fd_btn_yangtai_up:
                onClickChangeNum(fdTextMyHomeYangtaiNum, 1);
                break;
            case R.id.fd_my_home_yangtai:
                break;
            case R.id.fd_btn_ok:
                //计算并显示装修报价的结果
                fdpPie.setImageResource(R.mipmap.fd_pie);
                countResult();
                break;
            case R.id.fdp_price:
                break;
            case R.id.relativeLayout:
                break;
            case R.id.fdp_pie:
                break;
            case R.id.fdp_weishengjian:
                break;
            case R.id.fdp_text_weishengjian:
                break;
            case R.id.fdp_keting:
                break;
            case R.id.fdp_text_keting:
                break;
            case R.id.fdp_qita:
                break;
            case R.id.fdp_text_qita:
                break;
            case R.id.fdp_chufang:
                break;
            case R.id.fdp_text_chufang:
                break;
            case R.id.fdp_woshi:
                break;
            case R.id.fdp_text_woshi:
                break;
            case R.id.fdp_yangtai:
                break;
            case R.id.fdp_text_yangtai:
                break;
            case R.id.pf_piangao:
                break;
            case R.id.pf_fuhe:
                break;
            case R.id.pf_piandi:
                break;
            case R.id.fd_phone:
                break;
            case R.id.fd_edit_phone_num:
                break;
            case R.id.fd_phone_num:
                break;
            case R.id.fd_huoqu_baojia:
                //获取详细报价 进行发单处理
                break;
            case R.id.fd_open_phone:
                break;
            case R.id.imageView2:
                break;
            case R.id.into_download:
                break;
            case R.id.download_close:
                break;
            case R.id.re_download:
                break;
            case R.id.fd_result_view:
                break;
        }
    }

    //处理反馈按钮事件
    private void changBtnState(int type) {
        switch (type) {
            case 0:
                pfPiangao.setBackgroundResource(R.drawable.item_shape_blue);
                pfPiangao.setTextColor(mContext.getResources().getColor(R.color.blue));
                pfFuhe.setBackgroundResource(R.drawable.item_shape_white);
                pfFuhe.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                pfPiandi.setBackgroundResource(R.drawable.item_shape_white);
                pfPiandi.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                break;
            case 1:
                pfFuhe.setBackgroundResource(R.drawable.item_shape_blue);
                pfFuhe.setTextColor(mContext.getResources().getColor(R.color.blue));
                pfPiangao.setBackgroundResource(R.drawable.item_shape_white);
                pfPiangao.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                pfPiandi.setBackgroundResource(R.drawable.item_shape_white);
                pfPiandi.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                break;
            case 2:
                pfPiandi.setBackgroundResource(R.drawable.item_shape_blue);
                pfPiandi.setTextColor(mContext.getResources().getColor(R.color.blue));
                pfPiangao.setBackgroundResource(R.drawable.item_shape_white);
                pfPiangao.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                pfFuhe.setBackgroundResource(R.drawable.item_shape_white);
                pfFuhe.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                break;
        }
    }

    /**
     * 处理文本框数据的变化
     *
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

    //根据定位的城市返回对应的城市价格
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

    //根据城市的选择情况计算城市的半包总价   参数mAreas：用户输入烦人面积  参数:mRegion用户定位的地区
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

    //计算总价
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

    //在饼图上赋值计算的结果
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

        fdpWeishengjian.setText("卫生间12.63%");
        fdpQita.setText("其他20.00%");
        fdpYangtai.setText("阳台9.00%");
        fdpWoshi.setText("卧室26.37%");
        fdpChufang.setText("厨房9.19%");
        fdpKeting.setText("客厅22.81%");
    }

    //发单请求
    private void HttpGetFaDan() {
        String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
        if (!TextUtils.isEmpty(fdEditPhoneNum.getText().toString())) {
            if (fdEditPhoneNum.getText().toString().matches(MOBILE)) {
                HashMap<String, Object> param = new HashMap<>();
                param.put("cellphone", fdEditPhoneNum.getText().toString());
                param.put("housearea", fdTextMyHomeAreaNum.getText().toString());
                param.put("city", fdTextCity.getText().toString());
                param.put("orderprice", (float) (mPrice / 10000));
                param.put("device", "android");
                param.put("urlhistory", UrlConstans.PIPE_CODE);
                param.put("comeurl", UrlConstans.PIPE_CODE);
                param.put("source", "1111");
                HttpUtils.doPost(UrlConstans.PUB_ORDER_URL, param, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });

            } else {
                Toast.makeText(mContext, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "输入的手机号码不为空！", Toast.LENGTH_SHORT);
        }
    }

    //获取使用发单人口数量
    private void HttpGetNumPreson() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        HttpUtils.doPost(UrlConstans.GET_BILL_COUNT_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "获取数据失败====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "请求回来的数据====" + json);
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        final String num = data.getString("bill_count");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fdNumPreson.setText("已经有" + num + "位用户计算了报价");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
