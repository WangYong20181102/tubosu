package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._LocalCity;
import com.tbs.tbs_mj.bean._MyStore;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.Util;

import org.json.JSONArray;
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
 * 修改公司地址
 */
public class ChangeCoAddressActivity extends BaseActivity {

    @BindView(R.id.co_change_address_dissmiss_rl)
    RelativeLayout coChangeAddressDissmissRl;
    @BindView(R.id.co_change_address_ok_rl)
    RelativeLayout coChangeAddressOkRl;
    @BindView(R.id.co_change_address_city_tv)
    TextView coChangeAddressCityTv;
    @BindView(R.id.co_change_address_city_rl)
    RelativeLayout coChangeAddressCityRl;
    @BindView(R.id.co_change_address_detail_et)
    EditText coChangeAddressDetailEt;
    @BindView(R.id.co_change_address_detail_rl)
    RelativeLayout coChangeAddressDetailRl;
    private Context mContext;
    private String TAG = "ChangeCoAddressActivity";
    private Intent mIntent;
    private Gson mGson;
    private String mAddressJson = "";
    private _MyStore.AddressBean mAddressBean;
    private String mLocalCityJson = "";//城市JSON
    private ArrayList<_LocalCity> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String mTempCityName = "";
    //城市选择轮记录的位置
    private int mShengPosition = 0;
    private int mShiPosition = 0;
    private int mQuPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnage_co_address);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.INIT_CITY_DATA_IS_OK:
                initOptions();
                break;
        }
    }

    private void initEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        //上一个界面传来的信息
        mAddressJson = mIntent.getStringExtra("mAddressJson");
        //生成对象
        mAddressBean = mGson.fromJson(mAddressJson, _MyStore.AddressBean.class);
        mTempCityName = mAddressBean.getProvince_name() + mAddressBean.getCity_name() + mAddressBean.getDistrict_name();
        //初始化城市选择轮
        initCityWheel();
        //初始化页面的布局
        initViewEvent(mAddressBean);
    }

    private void initViewEvent(_MyStore.AddressBean addressBean) {
        //设置省市区
        coChangeAddressCityTv.setText("" + addressBean.getProvince_name() + addressBean.getCity_name() + addressBean.getDistrict_name());
        //设置详细的地址
        coChangeAddressDetailEt.setText("" + addressBean.getAddress());
        //详细地址输入框监听事件
        coChangeAddressDetailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 40) {
                    Toast.makeText(mContext, "最多可输入40个字", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initOptions() {
        //设置opion的position
        if (options1Items != null
                && !options1Items.isEmpty()
                && mAddressBean.getProvince_id() != null
                && mAddressBean.getCity_id() != null) {
            for (int i = 0; i < options1Items.size(); i++) {
                if (options1Items.get(i).getProvince_id().equals(mAddressBean.getProvince_id())) {
                    //记录opion 1
                    mShengPosition = i;
                    for (int j = 0; j < options1Items.get(i).getCity().size(); j++) {
                        if (options1Items.get(i).getCity().get(j).getCity_id().equals(mAddressBean.getCity_id())) {
                            mShiPosition = j;
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    //初始化城市选择轮
    private void initCityWheel() {
        if (TextUtils.isEmpty(SpUtil.getLocalCityJson(mContext))) {
            //本地的数据为空
            HttpGetCityJson();
        } else {
            //本地含有城市数据
            mLocalCityJson = SpUtil.getLocalCityJson(mContext);
            initJsonData(mLocalCityJson);
        }

    }

    //解析全部数据
    private void initJsonData(String jsonData) {
        ArrayList<_LocalCity> localCityArrayList = parseData(jsonData);
        options1Items = localCityArrayList;
        for (int i = 0; i < localCityArrayList.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省份的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的区域信息
            for (int c = 0; c < localCityArrayList.get(i).getCity().size(); c++) {//遍历该省份的所有城市
                String CityName = localCityArrayList.get(i).getCity().get(c).getCity_name();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市下的所有地区列表
                if (localCityArrayList.get(i).getCity().get(c).getDistrict() == null
                        || localCityArrayList.get(i).getCity().get(c).getDistrict().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int a = 0; a < localCityArrayList.get(i).getCity().get(c).getDistrict().size(); a++) {
                        City_AreaList.add(localCityArrayList.get(i).getCity().get(c).getDistrict().get(a).getDistrict_name());
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省的所有地区数据
            }
            options2Items.add(CityList);
            options3Items.add(Province_AreaList);
        }
        EventBusUtil.sendEvent(new Event(EC.EventCode.INIT_CITY_DATA_IS_OK));
    }

    //解析城市数据
    private ArrayList<_LocalCity> parseData(String result) {
        ArrayList<_LocalCity> localCityArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                _LocalCity localCity = mGson.fromJson(jsonArray.get(i).toString(), _LocalCity.class);
                localCityArrayList.add(localCity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localCityArrayList;
    }

    private void HttpGetCityJson() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        OKHttpUtil.post(Constant.CITY_JSON, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "获取网络城市失败=========" + e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //获取成功将数据保存
                        String cityDataJsonArrayList = jsonObject.optString("data");
                        mLocalCityJson = cityDataJsonArrayList;
                        SpUtil.setLocalCityJson(mContext, cityDataJsonArrayList);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
                        initJsonData(mLocalCityJson);
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //弹出城市选择轮
    private void showCityPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
//                SpUtil.setShengOption(mContext, options1);
//                SpUtil.setShiOption(mContext, options2);
//                SpUtil.setQuOption(mContext, options3);
//                Toast.makeText(JsonDataActivity.this, tx, Toast.LENGTH_SHORT).show();
                mShengPosition = options1;
                mShiPosition = options2;
                mQuPosition = options3;
                coChangeAddressCityTv.setText("" + tx);
            }
        })

                .setTitleText("")
                .setCancelColor(Color.parseColor("#333333"))
                .setSubmitText("保存")
                .setSubmitColor(Color.parseColor("#333333"))
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(16)
//                .setSelectOptions(SpUtil.getShengOption(mContext), SpUtil.getShiOption(mContext), SpUtil.getQuOption(mContext))
                .setSelectOptions(mShengPosition, mShiPosition, mQuPosition)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void saveInfo() {
        if (TextUtils.isEmpty(coChangeAddressCityTv.getText().toString())) {
            Toast.makeText(mContext, "请选择城市！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(coChangeAddressDetailEt.getText().toString())) {
            Toast.makeText(mContext, "请填写详细地址！", Toast.LENGTH_SHORT).show();
            return;
        }
        //保存数据
        if (mTempCityName.equals(coChangeAddressCityTv.getText().toString())) {
            //两者相等 数据未修改
            Log.e(TAG, "城市信息数据未修改========");
        } else {
            Log.e(TAG, "城市信息数据已修改========");
            //城市信息修改通知外部修改区域信息
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_FUWUQUYU_MSG, options1Items.get(mShengPosition).getCity().get(mShiPosition).getDistrict()));
            //城市数据修改了 将区的信息给详情页
            mAddressBean.setProvince_id("" + options1Items.get(mShengPosition).getProvince_id());//省id
            mAddressBean.setProvince_name("" + options1Items.get(mShengPosition).getProvince_name());//省名称
            mAddressBean.setCity_id("" + options1Items.get(mShengPosition).getCity().get(mShiPosition).getCity_id());//市id
            mAddressBean.setCity_name("" + options1Items.get(mShengPosition).getCity().get(mShiPosition).getCity_name());//市名称
            if(!options1Items.get(mShengPosition).getCity().get(mShiPosition).getDistrict().isEmpty()){
                mAddressBean.setDistrict_id(Integer.parseInt(options1Items.get(mShengPosition).getCity().get(mShiPosition).getDistrict().get(mQuPosition).getDistrict_id()));
                mAddressBean.setDistrict_name(options1Items.get(mShengPosition).getCity().get(mShiPosition).getDistrict().get(mQuPosition).getDistrict_name());
            }

        }
        //设置地址
        mAddressBean.setAddress(coChangeAddressDetailEt.getText().toString());
        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_ADDRESS, mAddressBean));
        finish();
    }

    @OnClick({R.id.co_change_address_dissmiss_rl,
            R.id.co_change_address_ok_rl, R.id.co_change_address_city_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_address_dissmiss_rl:
                finish();
                break;
            case R.id.co_change_address_ok_rl:
                //保存
                saveInfo();
                break;
            case R.id.co_change_address_city_rl:
                //弹出城市选择轮
                if (!options1Items.isEmpty()) {
                    showCityPickerView();
                } else {

                }
                break;
        }
    }
}
