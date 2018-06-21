package com.tbs.tbsbusiness.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._LocalCity;
import com.tbs.tbsbusiness.bean._MyStore;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.customview.CustomDialog;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OnlineStoreActivity extends BaseActivity {

    @BindView(R.id.ns_back_ll)
    LinearLayout nsBackLl;
    @BindView(R.id.ns_goto_detail_tv)
    TextView nsGotoDetailTv;
    @BindView(R.id.ns_co_full_name_tv)
    TextView nsCoFullNameTv;
    @BindView(R.id.ns_co_full_name_rl)
    RelativeLayout nsCoFullNameRl;
    @BindView(R.id.ns_co_simple_name_tv)
    TextView nsCoSimpleNameTv;
    @BindView(R.id.ns_co_simple_name_rl)
    RelativeLayout nsCoSimpleNameRl;
    @BindView(R.id.ns_shop_logo_tv)
    TextView nsShopLogoTv;
    @BindView(R.id.ns_shop_logo_rl)
    RelativeLayout nsShopLogoRl;
    @BindView(R.id.ns_pinpai_logo_tv)
    TextView nsPinpaiLogoTv;
    @BindView(R.id.ns_pinpai_logo_rl)
    RelativeLayout nsPinpaiLogoRl;
    @BindView(R.id.ns_co_address_tv)
    TextView nsCoAddressTv;
    @BindView(R.id.ns_co_address_rl)
    RelativeLayout nsCoAddressRl;
    @BindView(R.id.ns_fuwuquyu_tv)
    TextView nsFuwuquyuTv;
    @BindView(R.id.ns_fuwuquyu_rl)
    RelativeLayout nsFuwuquyuRl;
    @BindView(R.id.ns_jiazhuangfanwei_tv)
    TextView nsJiazhuangfanweiTv;
    @BindView(R.id.ns_jiazhuangfanwei_rl)
    RelativeLayout nsJiazhuangfanweiRl;
    @BindView(R.id.ns_gongzhuangfanwei_tv)
    TextView nsGongzhuangfanweiTv;
    @BindView(R.id.ns_gongzhuangfanwei_rl)
    RelativeLayout nsGongzhuangfanweiRl;
    @BindView(R.id.ns_shanchangfengge_tv)
    TextView nsShanchangfenggeTv;
    @BindView(R.id.ns_shanchangfengge_rl)
    RelativeLayout nsShanchangfenggeRl;
    @BindView(R.id.ns_yingyezhizhao_tv)
    TextView nsYingyezhizhaoTv;
    @BindView(R.id.ns_yingyezhizhao_rl)
    RelativeLayout nsYingyezhizhaoRl;
    @BindView(R.id.ns_qiantaitupian_tv)
    TextView nsQiantaitupianTv;
    @BindView(R.id.ns_qiantaitupian_rl)
    RelativeLayout nsQiantaitupianRl;
    @BindView(R.id.ns_rongyuzizhi_tv)
    TextView nsRongyuzizhiTv;
    @BindView(R.id.ns_rongyuzizhi_rl)
    RelativeLayout nsRongyuzizhiRl;
    @BindView(R.id.ns_tuozhanxinxi_ll)
    LinearLayout nsTuozhanxinxiLl;
    @BindView(R.id.ns_send_btn)
    TextView nsSendBtn;
    private String TAG = "OnlineStoreActivity";
    private Context mContext;
    private Gson mGson;
    //信息是否被修改
    private boolean isChangeMessage = false;
    //数据对象
    private _MyStore mMyStore;
    //比对 对象
    private _MyStore mTempMyStore;
    //荣誉资质操操作相关的数据集合
    private ArrayList<String> mRyzzOnNetRawImageData = new ArrayList<>();//初次加载的原始数据
    private ArrayList<String> mRyzzDelImageData = new ArrayList<>();//删除的数据 从修改界面传来的数据
    private ArrayList<String> mRyzzAddImageData = new ArrayList<>();//删除的数据 从修改界面传来的数据
    private ArrayList<String> mRyzzNeedDelImageData = new ArrayList<>();//真实要删除的数据 比对过后
    private ArrayList<String> mRyzzNeedUploadImageData = new ArrayList<>();//需要上传的数据  比对过后


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_store);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    //初始化相关事件
    private void initEvent() {
        mGson = new Gson();
        HtttpGetNetData();
    }

    @Override
    protected void receiveEvent(Event event) {
//        if (mMyStore != null
//                &&(event.getCode()==)) {
//            isChangeMessage = true;
//        }
//        Log.e(TAG, "修改信息标识========" + isChangeMessage + "==事件标识码==" + event.getCode());
        switch (event.getCode()) {
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_FULL_NAME:
                //修改了全称
                if (mMyStore != null) {
                    isChangeMessage = true;
                    mMyStore.setName((String) event.getData());
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_SIMP_NAME:
                //修改简称
                if (mMyStore != null) {
                    isChangeMessage = true;
                    mMyStore.setSimp_name((String) event.getData());
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_STORE_LOGO:
                //修改网店的logo
                if (mMyStore != null) {
                    isChangeMessage = true;
                    mMyStore.setStore_logo((String) event.getData());
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_PINPAI_LOGO:
                //修改品牌的logo
                if (mMyStore != null) {
                    isChangeMessage = true;
                    mMyStore.setBrands_logo((String) event.getData());
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_QIANTAI_LOGO:
                //修改前台图片
                if (mMyStore != null) {
                    isChangeMessage = true;
                    mMyStore.getFront_desk_img().setImg_url((String) event.getData());
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_QIANTAI_LOGO:
                //删除前台图片
                if (mMyStore != null) {
                    isChangeMessage = true;
                    mMyStore.getFront_desk_img().setImg_url("");
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_JIAZHUANG_FANWEI:
                //处理家装范围
                if (mMyStore != null) {
                    isChangeMessage = true;
                    String listJson = (String) event.getData();
                    mMyStore.getHome_improvement().clear();
                    try {
                        JSONArray jsonArray = new JSONArray(listJson);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _MyStore.HomeImprovementBean homeImprovementBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.HomeImprovementBean.class);
                            mMyStore.getHome_improvement().add(homeImprovementBean);
                            Log.e(TAG, "修改家装范围BUG==id==" + homeImprovementBean.getId() + "==name==" + homeImprovementBean.getName() + "==是否选中==" + homeImprovementBean.getIs_selected());
                        }
                        initView(mMyStore);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_GONGZHUANG_FANWEI:
                //处理公装范围
                if (mMyStore != null) {
                    isChangeMessage = true;
                    String listJson = (String) event.getData();
                    mMyStore.getTool_improvement().clear();
                    try {
                        JSONArray jsonArray = new JSONArray(listJson);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _MyStore.ToolImprovementBean toolImprovementBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.ToolImprovementBean.class);
                            mMyStore.getTool_improvement().add(toolImprovementBean);
                        }
                        initView(mMyStore);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_FENGGE_FANWEI:
                //处理风格范围
                if (mMyStore != null) {
                    isChangeMessage = true;
                    String listJson = (String) event.getData();
                    mMyStore.getGood_at_style().clear();
                    try {
                        JSONArray jsonArray = new JSONArray(listJson);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _MyStore.GoodAtStyleBean goodAtStyleBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.GoodAtStyleBean.class);
                            mMyStore.getGood_at_style().add(goodAtStyleBean);
                        }
                        initView(mMyStore);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_RYZZ_DEL_IMAGE:
                //处理删除数据
                isChangeMessage = true;
                String delImageJson = (String) event.getData();
                Log.e(TAG, "处理删除的照片===========" + delImageJson);

                try {
                    JSONArray jsonArray = new JSONArray(delImageJson);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mRyzzDelImageData.add(jsonArray.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_RYZZ_ADD_IMAGE:
                //处理新增的荣誉数据
                isChangeMessage = true;
                String addImageJson = (String) event.getData();
                try {
                    JSONArray jsonArray = new JSONArray(addImageJson);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mRyzzAddImageData.add(jsonArray.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_RYZZ_ALLDATE:
                //更新荣誉资质
                if (mMyStore != null) {
                    isChangeMessage = true;
                    String honorAlldataJson = (String) event.getData();
                    mMyStore.getHonor_img().clear();
                    try {
                        JSONArray jsonArray = new JSONArray(honorAlldataJson);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _MyStore.HonorImgBean honorImgBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.HonorImgBean.class);
                            mMyStore.getHonor_img().add(honorImgBean);
                        }
                        initView(mMyStore);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_YINGYEZHIZHAO_ALLDATE:
                if (mMyStore != null) {
                    isChangeMessage = true;
                    String yingyezhizhaoJson = (String) event.getData();
                    _MyStore.BusinessLicenseBean businessLicenseBean = mGson.fromJson(yingyezhizhaoJson, _MyStore.BusinessLicenseBean.class);
                    mMyStore.setBusiness_license(businessLicenseBean);
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_ADDRESS:
                if (mMyStore != null) {
                    isChangeMessage = true;
                    _MyStore.AddressBean addressBean = (_MyStore.AddressBean) event.getData();
                    mMyStore.setAddress(addressBean);
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_FUWUQUYU_MSG:
                //修改了城市信息 要把服务区域修改
                if (mMyStore != null) {
                    isChangeMessage = true;
                    List<_LocalCity.CityBean.DistrictBean> districtBeanList = new ArrayList<>();
                    districtBeanList = (ArrayList) event.getData();
//                    Log.e(TAG, "获取的服务区域======" + districtBeanList.get(0).getDistrict_name());
                    //生成区域数据
                    mMyStore.getService_area().clear();
                    if (!districtBeanList.isEmpty()) {
                        for (int i = 0; i < districtBeanList.size(); i++) {
                            _MyStore.ServiceAreaBean serviceAreaBean = new _MyStore.ServiceAreaBean();
                            serviceAreaBean.setId(districtBeanList.get(i).getDistrict_id());//设置id
                            serviceAreaBean.setName(districtBeanList.get(i).getDistrict_name());//设置名称
                            serviceAreaBean.setIs_selected(0);
                            mMyStore.getService_area().add(serviceAreaBean);
                        }
                    }
                    initView(mMyStore);
                }
                break;
            case EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_SERVICE_MSG:
                //修改服务区域信息
                if (mMyStore != null) {
                    isChangeMessage = true;
                    mMyStore.getService_area().clear();
                    mMyStore.setService_area((List) event.getData());
                    initView(mMyStore);
                }
                break;
        }
    }

    //初始化布局
    private void initView(_MyStore myStore) {
        //设置公司全称
        nsCoFullNameTv.setText("" + myStore.getName());
        //设置公司的简称
        nsCoSimpleNameTv.setText("" + myStore.getSimp_name());
        //处理网店logo
        if (myStore.getStore_logo() != null && !TextUtils.isEmpty(myStore.getStore_logo())) {
            //网店logo不为空 设置已经上传了网店的logo
            nsShopLogoTv.setText("已上传");
        }
        //处理品牌logo
        if (myStore.getBrands_logo() != null && !TextUtils.isEmpty(myStore.getBrands_logo())) {
            //设置已上传
            nsPinpaiLogoTv.setText("已上传");
        }
        //设置公司的详细地址
        nsCoAddressTv.setText("" + myStore.getAddress().getAddress());
        //设置服务区域
        if (!myStore.getService_area().isEmpty()) {
            String serviceName = "";
            for (int i = 0; i < myStore.getService_area().size(); i++) {
                if (myStore.getService_area().get(i).getIs_selected() == 1) {
                    //获取到选择的服务区 将数据拼接
                    serviceName = serviceName + myStore.getService_area().get(i).getName() + "、";
                }
            }
            if (!TextUtils.isEmpty(serviceName)) {
                serviceName = serviceName.substring(0, serviceName.length() - 1);
            }
            nsFuwuquyuTv.setText("" + serviceName);
        } else {
            nsFuwuquyuTv.setText("该地区没有服务区域");
        }
        //设置家装范围
        if (!myStore.getHome_improvement().isEmpty() || !myStore.getTool_improvement().isEmpty()) {
            //家装
            String jiazhuangFangwei = "";
            for (int i = 0; i < myStore.getHome_improvement().size(); i++) {
                if (myStore.getHome_improvement().get(i).getIs_selected() == 1) {
                    jiazhuangFangwei = jiazhuangFangwei + myStore.getHome_improvement().get(i).getName() + "、";
                }
            }
            //遍历公装
            for (int i = 0; i < myStore.getTool_improvement().size(); i++) {
                if (myStore.getTool_improvement().get(i).getIs_selected() == 1) {
                    jiazhuangFangwei = jiazhuangFangwei + myStore.getTool_improvement().get(i).getName() + "、";
                }
            }
            if (!TextUtils.isEmpty(jiazhuangFangwei)) {
                jiazhuangFangwei = jiazhuangFangwei.substring(0, jiazhuangFangwei.length() - 1);
            }
            Log.e(TAG, "处理家装范围======结果=====" + jiazhuangFangwei);

            nsJiazhuangfanweiTv.setText("" + jiazhuangFangwei);
        } else {
            nsJiazhuangfanweiTv.setText("");
        }
        //设置公装范围
//        if (!myStore.getTool_improvement().isEmpty()) {
//            String gongzhuangFangwei = "";
//            for (int i = 0; i < myStore.getTool_improvement().size(); i++) {
//                if (myStore.getTool_improvement().get(i).getIs_selected() == 1) {
//                    gongzhuangFangwei = gongzhuangFangwei + myStore.getTool_improvement().get(i).getName() + "、";
//                }
//            }
//            if (!TextUtils.isEmpty(gongzhuangFangwei)) {
//                gongzhuangFangwei = gongzhuangFangwei.substring(0, gongzhuangFangwei.length() - 1);
//            }
//            nsGongzhuangfanweiTv.setText("" + gongzhuangFangwei);
//        } else {
//            nsGongzhuangfanweiTv.setText("");
//        }
        //设置擅长风格
        if (!myStore.getGood_at_style().isEmpty()) {
            String fengge = "";
            for (int i = 0; i < myStore.getGood_at_style().size(); i++) {
                if (myStore.getGood_at_style().get(i).getIs_selected() == 1) {
                    fengge = fengge + myStore.getGood_at_style().get(i).getName() + "、";
                }
            }
            if (!TextUtils.isEmpty(fengge)) {
                fengge = fengge.substring(0, fengge.length() - 1);
            }
            nsShanchangfenggeTv.setText("" + fengge);
        } else {
            nsShanchangfenggeTv.setText("");
        }
        //根据会员等级显示拓展信息
        if (myStore.getGrade().equals("0")) {
            nsTuozhanxinxiLl.setVisibility(View.GONE);
        } else {
            nsTuozhanxinxiLl.setVisibility(View.VISIBLE);
            //营业执照信息
            if (myStore.getBusiness_license().getImg_url() != null && !TextUtils.isEmpty(myStore.getBusiness_license().getImg_url())) {
                nsYingyezhizhaoTv.setText("已上传");
            } else {
                nsYingyezhizhaoTv.setText("");
            }
            //前台图片
            if (myStore.getFront_desk_img() != null && !TextUtils.isEmpty(myStore.getFront_desk_img().getImg_url())) {
                nsQiantaitupianTv.setText("已上传");
            } else {
                nsQiantaitupianTv.setText("");
            }
            //荣誉资质
            if (myStore.getHonor_img() != null && !myStore.getHonor_img().isEmpty()) {
                nsRongyuzizhiTv.setText("已上传" + myStore.getHonor_img().size() + "张");
            } else {
                nsRongyuzizhiTv.setText("");
            }
        }
    }

    //检测数据是否完整
    private void checkMessgIsComplete() {
        //全称
        nsCoFullNameTv.setHintTextColor(Color.parseColor("#ed124a"));
        //简称
        nsCoSimpleNameTv.setHintTextColor(Color.parseColor("#ed124a"));
        //网店logo
        nsShopLogoTv.setHintTextColor(Color.parseColor("#ed124a"));
        //品牌logo
        nsPinpaiLogoTv.setHintTextColor(Color.parseColor("#ed124a"));
        //公司地址
        nsCoAddressTv.setHintTextColor(Color.parseColor("#ed124a"));
        //服务区域
        nsFuwuquyuTv.setHintTextColor(Color.parseColor("#ed124a"));
        //家装范围
        nsJiazhuangfanweiTv.setHintTextColor(Color.parseColor("#ed124a"));
        //公装范围
        nsGongzhuangfanweiTv.setHintTextColor(Color.parseColor("#ed124a"));
        //擅长风格
        nsShanchangfenggeTv.setHintTextColor(Color.parseColor("#ed124a"));
    }

    //用户点击提交信息
    private void sendInfo() {
        if (isChangeMessage) {
            //信息已修改过
            if (TextUtils.isEmpty(nsCoFullNameTv.getText().toString())
                    || TextUtils.isEmpty(nsCoSimpleNameTv.getText().toString())
                    || TextUtils.isEmpty(nsShopLogoTv.getText().toString())
                    || TextUtils.isEmpty(nsPinpaiLogoTv.getText().toString())
                    || TextUtils.isEmpty(nsCoAddressTv.getText().toString())
                    || TextUtils.isEmpty(nsFuwuquyuTv.getText().toString())
                    || TextUtils.isEmpty(nsJiazhuangfanweiTv.getText().toString())
                    || TextUtils.isEmpty(nsShanchangfenggeTv.getText().toString())) {
                //数据未填写完整
                checkMessgIsComplete();
                Toast.makeText(mContext, "还有信息未完善", Toast.LENGTH_SHORT).show();
            } else {
                //将数据提交  操作mMyStore对象
                //处理荣誉资质修改的数据
                if (!mRyzzNeedDelImageData.isEmpty()) {
                    mRyzzNeedDelImageData.clear();
                }
                if (!mRyzzNeedUploadImageData.isEmpty()) {
                    mRyzzNeedUploadImageData.clear();
                }
                //真实添加的数据
                for (int i = 0; i < mRyzzDelImageData.size(); i++) {
                    if (mRyzzAddImageData.contains(mRyzzDelImageData.get(i))) {
                        mRyzzAddImageData.remove(mRyzzDelImageData.get(i));
                    }
                }
                mRyzzNeedUploadImageData.addAll(mRyzzAddImageData);
                //真实要删除的数据
                for (int i = 0; i < mRyzzDelImageData.size(); i++) {
                    if (mRyzzOnNetRawImageData.contains(mRyzzDelImageData.get(i))) {
                        mRyzzNeedDelImageData.add(mRyzzDelImageData.get(i));
                    }
                }
                //将真实要修改的数据提交接口
                HttpSubmitChangeMsg(mMyStore, mRyzzNeedUploadImageData, mRyzzNeedDelImageData);
            }
        } else {
            //数据没有修改
            if (TextUtils.isEmpty(nsCoFullNameTv.getText().toString())
                    || TextUtils.isEmpty(nsCoSimpleNameTv.getText().toString())
                    || TextUtils.isEmpty(nsShopLogoTv.getText().toString())
                    || TextUtils.isEmpty(nsPinpaiLogoTv.getText().toString())
                    || TextUtils.isEmpty(nsCoAddressTv.getText().toString())
                    || TextUtils.isEmpty(nsFuwuquyuTv.getText().toString())
                    || TextUtils.isEmpty(nsJiazhuangfanweiTv.getText().toString())
//                    || TextUtils.isEmpty(nsGongzhuangfanweiTv.getText().toString())
                    || TextUtils.isEmpty(nsShanchangfenggeTv.getText().toString())) {

                checkMessgIsComplete();
                Toast.makeText(mContext, "还有信息未完善", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "信息已修改~", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    //网络获取用户的信息
    private void HtttpGetNetData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", SpUtil.getCompany_id(mContext));
        OKHttpUtil.post(Constant.COMPANY_MY_STORE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==========" + e.getMessage());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败~请检查网络链接", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "获取数据成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    final String data = jsonObject.optString("data");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMyStore = mGson.fromJson(data, _MyStore.class);
                                mTempMyStore = mGson.fromJson(data, _MyStore.class);
                                //初始化荣誉资质的原始数据
                                initRyzzRawData(mMyStore);
                                initView(mMyStore);
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //荣誉资质解析的原始数据
    private void initRyzzRawData(_MyStore mMyStore) {
        for (int i = 0; i < mMyStore.getHonor_img().size(); i++) {
            mRyzzOnNetRawImageData.add(mMyStore.getHonor_img().get(i).getImg_url());
        }
    }

    //修改了信息退出的弹窗
    private void showOutDiaog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setMessage("修改未保存，是否确认退出？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        builder.create().show();
    }

    //提交数据
    private void HttpSubmitChangeMsg(_MyStore mMyStore,
                                     ArrayList<String> mRyzzNeedUploadImageData,
                                     ArrayList<String> mRyzzNeedDelImageData) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", mMyStore.getId());
        //修改公司全称
        if (mMyStore.getName() != null) {
            param.put("name", mMyStore.getName());
        }
        //修改公司简称
        if (mMyStore.getSimp_name() != null) {
            param.put("simp_name", mMyStore.getSimp_name());
        }
        //修改公司的网店LOGO
        if (mMyStore.getStore_logo() != null) {
            param.put("store_logo", mMyStore.getStore_logo());
        }
        //修改网店LOGO
        if (mMyStore.getBrands_logo() != null) {
            param.put("brands_logo", mMyStore.getBrands_logo());
        }
        //修改省
        param.put("province_id", mMyStore.getAddress().getProvince_id());
        //修改市
        param.put("city_id", mMyStore.getAddress().getCity_id());
        //修改区
        if (mMyStore.getAddress().getDistrict_id() == 0) {
            param.put("district_id", "-1");
        } else {
            param.put("district_id", mMyStore.getAddress().getDistrict_id());
        }
        //修改地址
        param.put("address", mMyStore.getAddress().getAddress());
        //修改服务区域
        if (!mMyStore.getService_area().isEmpty()) {
            String fuwuquyu = "";
            for (int i = 0; i < mMyStore.getService_area().size(); i++) {
                if (mMyStore.getService_area().get(i).getIs_selected() == 1) {
                    fuwuquyu = fuwuquyu + mMyStore.getService_area().get(i).getId() + ",";
                }
            }
            if (!TextUtils.isEmpty(fuwuquyu)) {
                fuwuquyu = fuwuquyu.substring(0, fuwuquyu.length() - 1);
            }
            param.put("service_area", fuwuquyu);
        }

        //修改家装范围
        if (!mMyStore.getHome_improvement().isEmpty()) {
            Log.e(TAG, "getHome_improvement()集合不为空==========" + mMyStore.getHome_improvement().size());
            String jiazhuangFanwei = "";
            for (int i = 0; i < mMyStore.getHome_improvement().size(); i++) {
                if (mMyStore.getHome_improvement().get(i).getIs_selected() == 1) {
                    jiazhuangFanwei = jiazhuangFanwei + mMyStore.getHome_improvement().get(i).getId() + ",";
                }
            }
            if (!TextUtils.isEmpty(jiazhuangFanwei)) {
                jiazhuangFanwei = jiazhuangFanwei.substring(0, jiazhuangFanwei.length() - 1);
                param.put("home_improvement", jiazhuangFanwei);
            }else {
                param.put("home_improvement", "-1");
            }

            Log.e(TAG, "提交家装范围BUG================" + jiazhuangFanwei);
        }
        //修改公装范围
        if (!mMyStore.getTool_improvement().isEmpty()) {
            Log.e(TAG, "getTool_improvement()集合不为空==========" + mMyStore.getTool_improvement().size());
            String gongzhuangFanwei = "";
            for (int i = 0; i < mMyStore.getTool_improvement().size(); i++) {
                if (mMyStore.getTool_improvement().get(i).getIs_selected() == 1) {
                    gongzhuangFanwei = gongzhuangFanwei + mMyStore.getTool_improvement().get(i).getId() + ",";
                }
            }
            if (!TextUtils.isEmpty(gongzhuangFanwei)) {
                gongzhuangFanwei = gongzhuangFanwei.substring(0, gongzhuangFanwei.length() - 1);
                param.put("tool_improvement", gongzhuangFanwei);
            }else {
                param.put("tool_improvement", "-1");
            }
        }
        //修改擅长风格
        if (!mMyStore.getGood_at_style().isEmpty()) {
            String fengge = "";
            for (int i = 0; i < mMyStore.getGood_at_style().size(); i++) {
                if (mMyStore.getGood_at_style().get(i).getIs_selected() == 1) {
                    fengge = fengge + mMyStore.getGood_at_style().get(i).getId() + ",";
                }
            }
            if (!TextUtils.isEmpty(fengge)) {
                fengge = fengge.substring(0, fengge.length() - 1);
            }
            param.put("good_at_style", fengge);
        }
        //修改营业执照
        if (mMyStore.getBusiness_license().getId() != null) {
            param.put("license_id", mMyStore.getBusiness_license().getId());
        }
        //修改营业执照图片
        if (mMyStore.getBusiness_license().getImg_url() != null) {
            param.put("license_img", mMyStore.getBusiness_license().getImg_url());
//            Log.e(TAG, "上传的图片===============" + mMyStore.getBusiness_license().getImg_url());
        }
        //修改营业执照的有效期
        if (mMyStore.getBusiness_license().getEffect_time() != null) {
            param.put("license_effect_time", mMyStore.getBusiness_license().getEffect_time());
        } else {
            param.put("license_effect_time", "");
        }
        //注册号
        if (mMyStore.getBusiness_license().getRegistration_number() != null) {
            param.put("license_reg_number", mMyStore.getBusiness_license().getRegistration_number());
        } else {
            param.put("license_reg_number", "");
        }
        //字号名称
        if (mMyStore.getBusiness_license().getFont_name() != null) {
            param.put("license_font_name", mMyStore.getBusiness_license().getFont_name());
        } else {
            param.put("license_font_name", "");
        }
        //是否删除营业执照照片
        if (TextUtils.isEmpty(mMyStore.getBusiness_license().getImg_url())
                && mMyStore.getBusiness_license().getId() != null) {
            param.put("license_is_del", "1");
        }
        //前台照片id
        if (mMyStore.getFront_desk_img().getId() != null) {
            param.put("front_id", mMyStore.getFront_desk_img().getId());
        }
        //前台的照片url
        if (mMyStore.getFront_desk_img().getImg_url() != null) {
            param.put("front_img", mMyStore.getFront_desk_img().getImg_url());
        }

        //是否删除前台照片
        if (TextUtils.isEmpty(mMyStore.getFront_desk_img().getImg_url())
                && mMyStore.getBusiness_license().getId() != null) {
            param.put("front_is_del", "1");
        }
        //荣誉资质的图片处理
        //删除
        if (!mRyzzNeedDelImageData.isEmpty()) {
            String mDelImageUrl = "";
            for (int i = 0; i < mRyzzNeedDelImageData.size(); i++) {
                mDelImageUrl = mDelImageUrl + mRyzzNeedDelImageData.get(i) + ",";
            }
            mDelImageUrl = mDelImageUrl.substring(0, mDelImageUrl.length() - 1);
            param.put("honor_del_img", mDelImageUrl);
        }
        //添加
        if (!mRyzzNeedUploadImageData.isEmpty()) {
            String mUploadImageUrl = "";
            for (int i = 0; i < mRyzzNeedUploadImageData.size(); i++) {
                mUploadImageUrl = mUploadImageUrl + mRyzzNeedUploadImageData.get(i) + ",";
            }
            mUploadImageUrl = mUploadImageUrl.substring(0, mUploadImageUrl.length() - 1);
            param.put("honor_add_img", mUploadImageUrl);
        }

//        //参数输出
//        Iterator iterator = param.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry entry = (Map.Entry) iterator.next();
//            Log.e(TAG + "请求的参数:", "==key==" + entry.getKey() + "==val==" + entry.getValue());
//        }

        OKHttpUtil.post(Constant.COMPANY_MODIFY_STORE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        //修改成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.ns_back_ll, R.id.ns_co_full_name_rl, R.id.ns_co_simple_name_rl,
            R.id.ns_shop_logo_rl, R.id.ns_pinpai_logo_rl, R.id.ns_co_address_rl,
            R.id.ns_fuwuquyu_rl, R.id.ns_jiazhuangfanwei_rl, R.id.ns_gongzhuangfanwei_rl,
            R.id.ns_shanchangfengge_rl, R.id.ns_yingyezhizhao_rl, R.id.ns_goto_detail_tv,
            R.id.ns_qiantaitupian_rl, R.id.ns_rongyuzizhi_rl, R.id.ns_send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ns_back_ll:
                if (isChangeMessage) {
                    //用户信息已经修改弹窗提示 顺便检验必填项
                    checkMessgIsComplete();
                    showOutDiaog();
                } else {
                    finish();
                }

                break;
            case R.id.ns_co_full_name_rl:
                //公司全称 点击进入修改全称界面
                if (mMyStore != null) {
                    Intent gotoChangeFullName = new Intent(mContext, CoChangeFullNameActivity.class);
                    gotoChangeFullName.putExtra("mFullName", nsCoFullNameTv.getText().toString());
                    startActivity(gotoChangeFullName);
                }
                break;
            case R.id.ns_co_simple_name_rl:
                //公司简称 点击进入修改公司简称的界面
                if (mMyStore != null) {
                    Intent gotoChangeSimpName = new Intent(mContext, CoChangeSimpNameActivity.class);
                    gotoChangeSimpName.putExtra("mSimpName", nsCoSimpleNameTv.getText().toString());
                    startActivity(gotoChangeSimpName);
                }
                break;
            case R.id.ns_shop_logo_rl:
                //公司的网店logo点击进入网店的logo修改
                if (mMyStore != null) {
                    Intent intentToChangeStoreLogoActivity = new Intent(mContext, ChangeStoreLogoActivity.class);
                    intentToChangeStoreLogoActivity.putExtra("mImageUrl", mMyStore.getStore_logo());
                    startActivity(intentToChangeStoreLogoActivity);
                }
                break;
            case R.id.ns_pinpai_logo_rl:
                //公司的品牌logo 点击进入公司的品牌logo修改
                if (mMyStore != null) {
                    Intent intentToChangePinPaiLogoActivity = new Intent(mContext, ChangePinPaiLogoActivity.class);
                    intentToChangePinPaiLogoActivity.putExtra("mImageUrl", mMyStore.getBrands_logo());
                    startActivity(intentToChangePinPaiLogoActivity);
                }
                break;
            case R.id.ns_co_address_rl:
                //公司地址  点击进入修改公司地址
                if (mMyStore != null) {
                    String mAddressJson = mGson.toJson(mMyStore.getAddress());
                    Intent intentToChangeAddressActivity = new Intent(mContext, ChangeCoAddressActivity.class);
                    intentToChangeAddressActivity.putExtra("mAddressJson", mAddressJson);
                    startActivity(intentToChangeAddressActivity);
                }
                break;
            case R.id.ns_fuwuquyu_rl:
                //公司服务区域  点击进入修改公司的服务区域
                if (mMyStore != null) {
                    if (!mMyStore.getService_area().isEmpty()) {
                        String mFuwuquyuJson = mGson.toJson(mMyStore.getService_area());
                        Intent intentToChangeFuwuQuYu = new Intent(mContext, ChangeCoServiceActivity.class);
                        intentToChangeFuwuQuYu.putExtra("mFuwuquyuJson", mFuwuquyuJson);
                        startActivity(intentToChangeFuwuQuYu);
                    } else {
                        Toast.makeText(mContext, "当前城市没有城市区域", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.ns_jiazhuangfanwei_rl:
            case R.id.ns_gongzhuangfanwei_rl:
                //家装范围 点击进入修改家装范围
                if (mMyStore != null) {
                    //进入家装范围 将当前的数据装换成json
                    String mListJson = mGson.toJson(mMyStore.getHome_improvement());
                    //公装的信息
                    String mGongZListJson = mGson.toJson(mMyStore.getTool_improvement());
                    Intent intentToChangeCoJiaZhuang = new Intent(mContext, ChangeCoJiaZhuangRange.class);
                    intentToChangeCoJiaZhuang.putExtra("mListJson", mListJson);
                    intentToChangeCoJiaZhuang.putExtra("mGongZListJson", mGongZListJson);
                    startActivity(intentToChangeCoJiaZhuang);
                }
                break;
//            case R.id.ns_gongzhuangfanwei_rl:
//                //公装范围 点击进入修改公装范围
//                if (mMyStore != null) {
//                    //进入家装范围 将当前的数据装换成json
//                    String mListJson = mGson.toJson(mMyStore.getTool_improvement());
//                    Intent intentToChangeCoGongZhuang = new Intent(mContext, ChangeCoGongZhuangRangeActivity.class);
//                    intentToChangeCoGongZhuang.putExtra("mListJson", mListJson);
//                    startActivity(intentToChangeCoGongZhuang);
//                }
//                break;
            case R.id.ns_shanchangfengge_rl:
                //擅长风格 点击进入擅长的风格修改
                if (mMyStore != null) {
                    //进入风格 将当前的数据装换成json
                    String mListJson = mGson.toJson(mMyStore.getGood_at_style());
                    Intent intentToChangeFengGeZhuang = new Intent(mContext, ChangeCoFengGeRangeActivity.class);
                    intentToChangeFengGeZhuang.putExtra("mListJson", mListJson);
                    startActivity(intentToChangeFengGeZhuang);
                }
                break;
            case R.id.ns_yingyezhizhao_rl:
                //营业执照  点击进入营业执照修改界面
                if (mMyStore != null) {
                    String mYingYeZhiZhaoJson = mGson.toJson(mMyStore.getBusiness_license());
                    Intent intentToChangeYingYeZhiZhao = new Intent(mContext, CoChangeBussinessLicenseActivity.class);
                    intentToChangeYingYeZhiZhao.putExtra("mYingYeZhiZhaoJson", mYingYeZhiZhaoJson);
                    startActivity(intentToChangeYingYeZhiZhao);
                }
                break;
            case R.id.ns_qiantaitupian_rl:
                //前台图片  点击进入前台图片的修改
                if (mMyStore != null) {
                    Intent intentToChangeQianTaiActivity = new Intent(mContext, ChangeQiantaiImageActivity.class);
                    intentToChangeQianTaiActivity.putExtra("mImageUrl", mMyStore.getFront_desk_img().getImg_url());
                    startActivity(intentToChangeQianTaiActivity);
                }
                break;
            case R.id.ns_rongyuzizhi_rl:
                //荣誉资质  点击进入上传荣誉资质图片
                if (mMyStore != null) {
                    Log.e(TAG, "点击进入荣誉资质界面============");
                    String mImageListJson = mGson.toJson(mMyStore.getHonor_img());
                    Intent intentToChangeRongYuActivity = new Intent(mContext, ChangeRongYuImageActivity.class);
                    intentToChangeRongYuActivity.putExtra("mImageListJson", mImageListJson);
                    startActivity(intentToChangeRongYuActivity);
                }
                break;
            case R.id.ns_send_btn:
                //提交上传按钮
                sendInfo();
                break;
            case R.id.ns_goto_detail_tv:

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isChangeMessage) {
                showOutDiaog();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

}
