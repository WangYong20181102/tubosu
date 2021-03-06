package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CaseSearchStyleAdapter;
import com.tbs.tobosupicture.adapter.DistrictAdapter;
import com.tbs.tobosupicture.adapter.SearchCaseAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.AddressDtailsEntity;
import com.tbs.tobosupicture.bean.AddressModel;
import com.tbs.tobosupicture.bean.CaseConditionType;
import com.tbs.tobosupicture.bean.DistrictEntity;
import com.tbs.tobosupicture.bean.SearchCaseConditionEntity;
import com.tbs.tobosupicture.bean.SearchDataEntity;
import com.tbs.tobosupicture.bean.SearchDistrictJsonEntity;
import com.tbs.tobosupicture.bean.SearchRecordBean;
import com.tbs.tobosupicture.bean.SearchRecordJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.JsonUtil;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.CustomExpandableListView;
import com.tbs.tobosupicture.view.MyListView;
import com.tbs.tobosupicture.view.PullToRefreshBase;
import com.tbs.tobosupicture.view.wheelviews.ChooseAddressWheel;
import com.tbs.tobosupicture.view.wheelviews.listener.OnAddressChangeListener;
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
 * Created by Lie on 2017/7/19.
 */

public class ConditionActivity extends BaseActivity implements OnAddressChangeListener {

    @BindView(R.id.searchCaseRecord)
    MyListView searchCaseRecord;
    @BindView(R.id.tvSeeMoreCaseHistory)
    TextView tvSeeMoreCaseHistory;
    @BindView(R.id.tvClearCaseHistory)
    TextView tvClearCaseHistory;
    @BindView(R.id.tvCaseSearch)
    TextView tvCaseSearch;
    @BindView(R.id.expandableConditionListview)
    CustomExpandableListView expandableConditionListview;
    @BindView(R.id.tvHistorySearch)
    TextView tvHistorySearch;
    @BindView(R.id.historyLinearlayout)
    LinearLayout historyLinearlayout;
    @BindView(R.id.relSearchBack)
    RelativeLayout relSearchBack;
    @BindView(R.id.relSearchCaseLayout)
    RelativeLayout relSearchCaseLayout;
    @BindView(R.id.v_middle)
    View v_middle;
    @BindView(R.id.relShowAndHideCity)                // 城市 + 小区 布局
    RelativeLayout relShowAndHideCity;                // 城市 + 小区 布局
    @BindView(R.id.tvChooseCity)                      // 选择城市 按钮
    TextView tvChooseCity;                            // 选择城市 按钮
    @BindView(R.id.tvChooseDisctrict)                 // 选择小区 按钮
    TextView tvChooseDisctrict;                       // 选择小区 按钮
    @BindView(R.id.hideCityLayout)
    LinearLayout hideCityLayout;
    @BindView(R.id.show_cittext)
    TextView show_cittext;

    private boolean isHide = true;

    private SearchDataEntity searchDataEntity;
    private SearchCaseConditionEntity searchCaseConditionEntity;

    private String param_area;
    private String param_layout;
    private String param_price;
    private String param_style;

    private ChooseAddressWheel chooseAddressWheel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = "ConditionActivity";
        mContext = ConditionActivity.this;
        setContentView(R.layout.activity_condition);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent!=null){
            if(intent.getStringExtra("default_city")!=null){
                cityName = intent.getStringExtra("default_city");
            }else {
                cityName = "深圳市";
            }
            getDataFromNet();   // 获取搜索条件
            initCityDisctrictWheelView();  // 初始化省市区滚轮
        }else {
            Utils.setErrorLog(TAG, "intent是空");
        }
    }

    private ArrayList<HashMap<String, String>> conditionTextList = new ArrayList<HashMap<String, String>>();
    private void initCondition(int size){
        conditionTextList.clear();
        for(int i=0; i<size; i++){
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("" + i, "");
            conditionTextList.add(hashMap);
        }
    }

    /**
     * 初始化省市区滚轮
     */
    private void initCityDisctrictWheelView() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);

        // 省市区
        String address = Utils.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) {
                return;
            }else {
                tvChooseCity.setText("请选择城市");
                if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                    chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                    chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
                }
            }
        }
    }

    /***
     * 获取搜索条件
     */
    private void getDataFromNet() {
        tvChooseCity.setText(defaultAddr); // 默认
        if (Utils.isNetAvailable(mContext)) {

            if(Utils.userIsLogin(mContext)){

            }else {
                relSearchCaseLayout.setVisibility(View.GONE);
            }

            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Utils.getDateToken());
            hashMap.put("uid", SpUtils.getUserUid(mContext));
            HttpUtils.doPost(UrlConstans.CASE_SEARCH_URL, hashMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(mContext, "系统繁忙，稍后再试!");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Utils.setErrorLog(TAG, json);
                    final SearchRecordJsonEntity jsonEntity = new SearchRecordJsonEntity(json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonEntity.getStatus() == 200) {
                                searchDataEntity = jsonEntity.getSearchDataEntity();
                                searchCaseConditionEntity = jsonEntity.getSearchCaseConditionEntity();
                                setDataInView();
                            } else {
                                Utils.setToast(mContext, jsonEntity.getMsg());
                            }
                        }
                    });
                }
            });
        }
    }


    private int listViewCount;
    private ArrayList<SearchRecordBean> recordBeenList;
    private ArrayList<CaseConditionType> caseConditionTypeList;
    private ArrayList<SearchRecordBean> tempRecordBeenList = new ArrayList<SearchRecordBean>();
    private SearchCaseAdapter searchCaseAdapter;
    private void setDataInView() {

        recordBeenList = searchDataEntity.getSearch_record(); // 一次性获取搜索记录
        if (recordBeenList.size() == 0) {
            relSearchCaseLayout.setVisibility(View.GONE);
        } else {
            relSearchCaseLayout.setVisibility(View.VISIBLE);
        }

        if(recordBeenList!=null){
            initSearchRecord(recordBeenList, false);
        }

        if (searchCaseConditionEntity != null) {
            caseConditionTypeList = searchCaseConditionEntity.getConditionTypeList();
            listViewCount = caseConditionTypeList.size();
            initCondition(listViewCount);
            // 初始化expandablelistview
            CaseSearchStyleAdapter caseSearchStyleAdapter = new CaseSearchStyleAdapter(mContext, caseConditionTypeList, new CaseSearchStyleAdapter.OnSearchCaseStyleItemClickListener() {
                @Override
                public void onSearchCaseStyleParentClickListener(TextView textView, int group, String id, int position, String condition) {
//                    Utils.setToast(mContext, group + "<<<<group===>>>>" + id);

                    for(int i=0; i<listViewCount; i++){

                        if(group == i){
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("" + i, condition + " ");
                            conditionTextList.set(i, hashMap);
                        }
                    }

//                    textView.setText(condition);
//                    groupTextView.setText(caseConditionTypeList.get(group).getCaseTypeChildList().get(position).getValue());
//                    Utils.setToast(mContext, ""+groupPosition + "******" + childPosition);


                    switch (group) {
                        case 0: // 面积
                            param_area = id;
                            SpUtils.setSearchCaseColorAreaNum(mContext, position);
                            break;
                        case 1: // 户型
                            param_layout = id;
                            break;
                        case 2: // 价格
                            param_price = id;
                            break;
                        case 3: // 风格
                            param_style = id;
                            break;
                    }
                }
            });

            expandableConditionListview.setAdapter(caseSearchStyleAdapter);
            caseSearchStyleAdapter.notifyDataSetChanged();
        }
    }


    private Intent setIntent(){
//        if(isHide){
//            city_id = "";
//            district_id = "";
//            param_vilige_id = "";
//            Utils.setErrorLog(TAG, "===关闭了城市小区===");
//        }else {
//            if("".equals(param_vilige_id)){
//                city_id = "";
//                district_id = "";
//                param_vilige_id = "";
//            }
//        }

        String conditionText = "";
        for(int i=0;i<conditionTextList.size();i++){
            String temp = conditionTextList.get(i).get("" + i);
            if(temp != null && !"".equals(temp)){
                conditionText += temp;
            }
        }

        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putInt("param_history_record", 0);
        b.putString("param_area", param_area);
        b.putString("param_layout", param_layout);
        b.putString("param_price", param_price);
        b.putString("param_style", param_style);
//        b.putString("pass_param_city_id", cityName);
        city_id = cityName;
        b.putString("param_district_id", district_id);//小区id
        Utils.setErrorLog(TAG, param_area + "   返回小区id  " + district_id);
                b.putString("param_vilige_id", param_vilige_id);//花园小区id  param_vilige_id
        if("".equals(cityName)){
            b.putInt("getcity", 0);// 无选择城市
            conditionText = cityName + " " +conditionText;
            b.putString("param_city_id", cityName);
            Utils.setErrorLog(TAG,"返回 无选择城市：" + city_id);
        }else {
            b.putInt("getcity", 1);// 有选择城市
            b.putString("param_city_id", city_id);
            Utils.setErrorLog(TAG,"返回 有选择城市：" + city_id);
        }

        b.putString("condition_text", cityName + " " + conditionText);
        intent.putExtra("params", b);

        Utils.setErrorLog(TAG, "[" +param_area + "]   [" + param_layout + "]  [" + param_price + "]  [" + param_style + "]  返回城市id[" + city_id+  "]  返回小区id[" + district_id+ "]  返回花园小区id[" + param_vilige_id+"]");
        return intent;
    }

    private void getParamBack(Intent intent) {
        // 获取数据
        setResult(10101, intent);
        finish();
    }

    private void initSearchRecord(ArrayList<SearchRecordBean> _recordBeenList, boolean seemore) {
        if (_recordBeenList != null && _recordBeenList.size() > 0) {
            // 历史记录
            if (seemore) {
                // 查看更多
                tempRecordBeenList.clear();
                tempRecordBeenList.addAll(_recordBeenList);
                tvSeeMoreCaseHistory.setVisibility(View.GONE);
                v_middle.setVisibility(View.GONE);
            } else {
                if (_recordBeenList.size() > 5) {
                    for(int i = 0; i < 5; i++) {
                        // 最多5条
                        tempRecordBeenList.add(_recordBeenList.get(i));
                    }
                } else {
                    tempRecordBeenList.addAll(_recordBeenList);
                    tvSeeMoreCaseHistory.setVisibility(View.GONE);
                    v_middle.setVisibility(View.GONE);
                }
            }
            if (searchCaseAdapter == null) {
                searchCaseAdapter = new SearchCaseAdapter(mContext, tempRecordBeenList);
                searchCaseRecord.setAdapter(searchCaseAdapter);
            } else {
                searchCaseAdapter.notifyDataSetChanged();
            }

            searchCaseRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 历史记录的
                    Intent intent = new Intent();
                    Bundle b = new Bundle();
                    b.putInt("param_history_record", 1);
                    b.putString("param_area", tempRecordBeenList.get(position).getArea_key());
                    b.putString("param_layout", tempRecordBeenList.get(position).getLayout_key());
                    b.putString("param_price", tempRecordBeenList.get(position).getPrice_key());
                    b.putString("param_style", tempRecordBeenList.get(position).getStyle_id());
                    b.putString("param_city_id", tempRecordBeenList.get(position).getCity_id());
                    b.putString("param_discturds_id", tempRecordBeenList.get(position).getDistrict_id());
                    b.putInt("getcity", 1);
                    String _condiction = tempRecordBeenList.get(position).getCity_name() + " " +
                            tempRecordBeenList.get(position).getLayout_value() + " " +
                            tempRecordBeenList.get(position).getArea_value() + " " +
                            tempRecordBeenList.get(position).getPrice_value() + " " +
                            tempRecordBeenList.get(position).getStyle_name();
                    b.putString("condition_text", _condiction);
                    intent.putExtra("params", b);

                    Utils.setErrorLog(TAG, param_area + "   " + param_layout + "  " + param_price + "  " + param_style + "  新增>>> " + tempRecordBeenList.get(position).getDistrict_id());
                    setResult(10101, intent);
                    finish();
                }
            });
        } else {
            tvHistorySearch.setVisibility(View.GONE);
            historyLinearlayout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tvSeeMoreCaseHistory, R.id.tvClearCaseHistory, R.id.tvCaseSearch, R.id.relSearchBack, R.id.relShowAndHideCity,
            R.id.tvChooseCity, R.id.tvChooseDisctrict})
    public void onViewClickedCondictionActivity(View view) {
        switch (view.getId()) {
            case R.id.tvChooseCity:  //  选择城市
                Utils.hideKeyBoard(this);
                // 显示小区
                chooseAddressWheel.show(findViewById(R.id.tvChooseCity));
                break;
            case R.id.tvChooseDisctrict:   // 显示花园小区
                if("0".equals(province_id)){
                    Utils.setToast(mContext, "请选择省市区");
                    return;
                }else {
                    adapter = null;
                    tempGardenDataList.clear();
                    Utils.setErrorLog(TAG, "返回：【传入 city_id:" + city_id+ "】    【传入 district_id：" + district_id + "】");
                    showGardenWindow(false, null, city_id, district_id);
                }

                break;
            case R.id.relShowAndHideCity:
                //  城市 + 小区 布局
                if(isHide){
                    hideCityLayout.setVisibility(View.VISIBLE);
                }else {
                    hideCityLayout.setVisibility(View.GONE);
                }
                isHide = !isHide;
                break;
            case R.id.tvSeeMoreCaseHistory:
                // 显示20条
                if(recordBeenList!=null){
                    initSearchRecord(recordBeenList, true);
                }

                break;
            case R.id.tvClearCaseHistory:
                if(Utils.userIsLogin(mContext)){
                    if (Utils.isNetAvailable(mContext)) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("uid", SpUtils.getUserUid(mContext));
                        hashMap.put("token", Utils.getDateToken());
                        HttpUtils.doPost(UrlConstans.CLEAR_CASE_URL, hashMap, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.setToast(mContext, "系统繁忙，稍后再试~");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String json = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(json);

                                            if (jsonObject.getInt("status") == 200) {
                                                Utils.setToast(mContext, jsonObject.getString("msg"));
                                                relSearchCaseLayout.setVisibility(View.GONE);
                                            } else {
                                                Utils.setToast(mContext, jsonObject.getString("msg"));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        });
                    }
                }else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.tvCaseSearch:
                getParamBack(setIntent());
                break;
            case R.id.relSearchBack:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }


    @Override
    public void onAddressChange(String province, String city, String district) {
        tvChooseCity.setText(province + " " + city + " " + district);
        defaultAddr = province + " " + city + " " + district;
        cityName = city;
        city_id = city;
        show_cittext.setText(city);
//        Utils.setToast(mContext, cityName + " -1- " + city_id);
    }


    @Override
    public void onAddressChangeId(String provinceId, String cityId, String districtId) {
        province_id = provinceId;
//        city_id = cityId;
        district_id = districtId;
//        Utils.setToast(mContext, province_id + " - "+cityId+" - " + district_id);
    }

    private String province_id = "0";      //  默认为 未选择 未选择 未选择
    private String city_id = "0";          //  默认为 未选择 未选择 未选择
    private String district_id = "0";      //  默认为 未选择 未选择 未选择
    private String param_vilige_id = "";
    private int districtPage = 1;
    private int districtPageSize = 12;

    /***
     * 获取花园小区数据
     * @param isTextSearch
     * @param text
     * @param _ciyt_id
     * @param _disc_id
     */
    private void getGardenDistrictData(boolean isTextSearch, String text, String _ciyt_id, String _disc_id, final GridView gv, final ImageView noDataImg, final RelativeLayout hasDataLayout){
        Utils.setErrorLog(TAG, "进入了================1============");
        if (Utils.isNetAvailable(mContext)) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Utils.getDateToken());
            if(isTextSearch){
                hashMap.put("name", text);
            }
            hashMap.put("city_id", _ciyt_id);
            hashMap.put("district_id", _disc_id);
//            Utils.setErrorLog(TAG, "获取花园小区数据 == 城市id:" + _ciyt_id + "     小区id:" + _disc_id + "  城市 " + text  + " === " + districtPage);
            hashMap.put("page", districtPage);
            hashMap.put("page_size", districtPageSize);
            HttpUtils.doPost(UrlConstans.DISTRICT_LIST_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.setErrorLog(TAG, "getGardenDistrictData请求花园小区数据失败---"); // FIXME: 2017/8/16
                    Utils.setToast(mContext, "获取小区数据失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Utils.setErrorLog(TAG, "json ====1==>>> "+json);
                    final SearchDistrictJsonEntity jsonEntity = new SearchDistrictJsonEntity(json);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (jsonEntity.getStatus() == 200) {
                                districtDataList = jsonEntity.getDistrictEntityList();
                                tempGardenDataList.addAll(districtDataList);

                                if(tempGardenDataList.size()==0){
                                    hasDataLayout.setVisibility(View.GONE);
                                    noDataImg.setVisibility(View.VISIBLE);
                                }else {
                                    initGardenDistrictAdapter(gv, tempGardenDataList);
                                    hasDataLayout.setVisibility(View.VISIBLE);
                                    noDataImg.setVisibility(View.GONE);
                                }
                            }else{
                                Utils.setToast(mContext, "没有更多小区数据了");
                                // FIXME: 2017/8/16  占位图


                            }
                        }
                    });
                }
            });
        }
    }


    private PopupWindow popupWindow;
    private ArrayList<DistrictEntity> districtDataList;
    private ArrayList<DistrictEntity> tempGardenDataList = new ArrayList<DistrictEntity>();
    private DistrictAdapter adapter;
    private String defaultAddr = "请选择城市";
    private String cityName = "";
    private void showGardenWindow(boolean isTextSearch, String text, String cityID, String districtID){
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popuplayout_district, null);
        final ImageView ivNoDistrictDatas = (ImageView) contentView.findViewById(R.id.ivNoDistrictDatas);
        final RelativeLayout relHasData = (RelativeLayout) contentView.findViewById(R.id.relHasData);
        final com.tbs.tobosupicture.view.PullToRefreshGridView mPullRefreshGridView = (com.tbs.tobosupicture.view.PullToRefreshGridView) contentView.findViewById(R.id.pullGridView);
        RelativeLayout relClose = (RelativeLayout) contentView.findViewById(R.id.relCloseWindown);
        TextView tvLocation = (TextView) contentView.findViewById(R.id.tvLocation);
        final EditText editTextSearchDistrct = (EditText) contentView.findViewById(R.id.editTextSearchDistrct);
        TextView tvSearchDistrict = (TextView) contentView.findViewById(R.id.tvSearchDistrict); // 搜索按钮
        mPullRefreshGridView.init(com.tbs.tobosupicture.view.PullToRefreshGridView.MODE_BOTH);
        final GridView mGridView = mPullRefreshGridView.getRefreshableView();
        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(int mode) {
                if (com.tbs.tobosupicture.view.PullToRefreshGridView.MODE_PULL_DOWN_TO_REFRESH == mPullRefreshGridView.getCurrentMode()) {
                    districtPage = 1;
                    tempGardenDataList.clear();
                    getGardenDistrictData(false, null, city_id, district_id,mGridView, ivNoDistrictDatas, relHasData);
                    mPullRefreshGridView.onRefreshComplete();
                } else if (mode == com.tbs.tobosupicture.view.PullToRefreshGridView.MODE_PULL_UP_TO_REFRESH) {
                    // 加载更多
//                    Utils.setToast(mContext, "上拉加载更多");
                    districtPage++;
                    getGardenDistrictData(false, null, city_id, district_id,mGridView, ivNoDistrictDatas, relHasData);
                    mPullRefreshGridView.onRefreshComplete();
                }
            }
        });
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // 请求花园数据
        getGardenDistrictData(isTextSearch, text, cityID, districtID, mGridView, ivNoDistrictDatas, relHasData);

        // 点击搜索
        tvSearchDistrict.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String searchText = editTextSearchDistrct.getText().toString().trim();
                if("".equals(searchText)){
                    Utils.setToast(mContext, "请输入小区名");
                    return;
                }else{
                    tempGardenDataList.clear();
                    districtPage = 1;
                    // 输入小区名称搜索花园小区
                    getGardenDistrictData(true, searchText, city_id, district_id, mGridView, ivNoDistrictDatas, relHasData);
                }

            }
        });
        tvLocation.setText("所在地 " + defaultAddr);
        relClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                param_vilige_id = tempGardenDataList.get(position).getId();
                tvChooseDisctrict.setText(tempGardenDataList.get(position).getName());
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(findViewById(R.id.condictionMainLayout), 0, 0, Gravity.BOTTOM);

    }
    
    private void initGardenDistrictAdapter(GridView gridView, ArrayList<DistrictEntity> dataList){
        Utils.setErrorLog(TAG, "====1===123=");
        if(adapter==null){
            adapter = new DistrictAdapter(mContext, dataList);
            gridView.setAdapter(adapter);
            Utils.setErrorLog(TAG, "====1===134=");
        }else {
            adapter.notifyDataSetChanged();
            Utils.setErrorLog(TAG, "====1===642=");
        }
    }
}
