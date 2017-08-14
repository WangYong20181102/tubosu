package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CaseSearchStyleAdapter;
import com.tbs.tobosupicture.adapter.DistrictAdapter;
import com.tbs.tobosupicture.adapter.LoadMoreAdapter;
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
import com.tbs.tobosupicture.view.GridView2;
import com.tbs.tobosupicture.view.MyListView;
import com.tbs.tobosupicture.view.library.PullToRefreshGridView;
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
    @BindView(R.id.relShowAndHideCity)
    RelativeLayout relShowAndHideCity;
    @BindView(R.id.tvChooseCity)
    TextView tvChooseCity;
    @BindView(R.id.tvChooseDisctrict)
    TextView tvChooseDisctrict;
    @BindView(R.id.hideCityLayout)
    LinearLayout hideCityLayout;

    private boolean isHide = true;

    private SearchDataEntity searchDataEntity;
    private SearchCaseConditionEntity searchCaseConditionEntity;

    private String param_area;
    private String param_layout;
    private String param_price;
    private String param_style;
    private String conditionText = "";

    private ChooseAddressWheel chooseAddressWheel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = "ConditionActivity";
        mContext = ConditionActivity.this;
        setContentView(R.layout.activity_condition);

        ButterKnife.bind(this);
        getDataFromNet();
        init();
    }

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
                            } else {
                                Utils.setToast(mContext, jsonEntity.getMsg());
                            }
                            setDataInView();
                        }
                    });
                }
            });
        } else {

        }
    }

    private ArrayList<SearchRecordBean> recordBeenList;
    private ArrayList<CaseConditionType> caseConditionTypeList;
    private ArrayList<SearchRecordBean> tempRecordBeenList = new ArrayList<SearchRecordBean>();

    private SearchCaseAdapter searchCaseAdapter;

    private void setDataInView() {

        initSearchRecord(false);

        if (searchCaseConditionEntity != null) {
            caseConditionTypeList = searchCaseConditionEntity.getConditionTypeList();
            // 初始化expandablelistview
            CaseSearchStyleAdapter caseSearchStyleAdapter = new CaseSearchStyleAdapter(mContext, caseConditionTypeList, new CaseSearchStyleAdapter.OnSearchCaseStyleItemClickListener() {
                @Override
                public void onSearchCaseStyleParentClickListener(int group, String id, int position, String condition) {
//                    Utils.setToast(mContext, group + "<<<<===>>>>" + id);
                    conditionText += condition;
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
        if(isHide){
            city_id = "";
            district_id = "";
            param_vilige_id = "";
            Utils.setErrorLog(TAG, "===关闭了城市小区===");
        }

        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putString("param_area", param_area);
        b.putString("param_layout", param_layout);
        b.putString("param_price", param_price);
        b.putString("param_style", param_style);
        b.putString("param_city_id", city_id);
        b.putString("param_district_id", district_id);//小区id
        b.putString("param_vilige_id", param_vilige_id);//花园小区id  param_vilige_id
        b.putString("condition_text", conditionText);
        intent.putExtra("params", b);

        Utils.setErrorLog(TAG, param_area + "   " + param_layout + "  " + param_price + "  " + param_style + "  返回城市id " + city_id+  "  返回小区id " + district_id+ "  返回花园小区id " + param_vilige_id);
        return intent;
    }

    private void getParamBack(Intent intent) {
        // 获取数据
        setResult(10101, intent);
        finish();
    }

    private void initSearchRecord(boolean seemore) {
        if (searchDataEntity != null) {
            recordBeenList = searchDataEntity.getSearch_record();
            if (recordBeenList.size() == 0) {
                relSearchCaseLayout.setVisibility(View.GONE);
            } else {
                relSearchCaseLayout.setVisibility(View.VISIBLE);
            }


            if (recordBeenList != null && recordBeenList.size() > 0) {
                // 历史记录
                if (seemore) {
                    tempRecordBeenList.addAll(recordBeenList);
                } else {
                    if (recordBeenList.size() > 5) {
                        for(int i = 0; i < 5; i++) {
                            tempRecordBeenList.add(recordBeenList.get(i));
                        }
                    } else {
                        tempRecordBeenList.addAll(recordBeenList);
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
                        b.putString("param_area", tempRecordBeenList.get(position).getArea_key());
                        b.putString("param_layout", tempRecordBeenList.get(position).getLayout_key());
                        b.putString("param_price", tempRecordBeenList.get(position).getPrice_key());
                        b.putString("param_style", tempRecordBeenList.get(position).getStyle_id());
                        b.putString("param_city_id", tempRecordBeenList.get(position).getCity_id());
                        String _condiction = tempRecordBeenList.get(position).getCity_name() + " " +
                                tempRecordBeenList.get(position).getLayout_value() + " " +
                                tempRecordBeenList.get(position).getArea_value() + " " +
                                tempRecordBeenList.get(position).getPrice_value() + " " +
                                tempRecordBeenList.get(position).getStyle_name();
                        b.putString("condition_text", _condiction);
                        intent.putExtra("params", b);

                        Utils.setErrorLog(TAG, param_area + "   " + param_layout + "  " + param_price + "  " + param_style);
                        setResult(10101, intent);
                        finish();
                    }
                });
            } else {
                tvHistorySearch.setVisibility(View.GONE);
                historyLinearlayout.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.tvSeeMoreCaseHistory, R.id.tvClearCaseHistory, R.id.tvCaseSearch, R.id.relSearchBack, R.id.relShowAndHideCity,
            R.id.tvChooseCity, R.id.tvChooseDisctrict})
    public void onViewClickedCondictionActivity(View view) {
        switch (view.getId()) {
            case R.id.tvChooseCity:
                Utils.hideKeyBoard(this);
                // 显示小区
                chooseAddressWheel.show(view);
                break;
            case R.id.tvChooseDisctrict:
                // 显示 花园小区
                initDistrictPopupWindow(defaultAddr);
                break;
            case R.id.relShowAndHideCity:
                if(isHide){
                    hideCityLayout.setVisibility(View.VISIBLE);
                }else {
                    hideCityLayout.setVisibility(View.GONE);
                }
                isHide = !isHide;
                break;
            case R.id.tvSeeMoreCaseHistory:
                // 显示20条
                initSearchRecord(true);
                break;
            case R.id.tvClearCaseHistory:
                if(Utils.userIsLogin(mContext)){
                    if (Utils.isNetAvailable(mContext)) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("iud", SpUtils.getUserUid(mContext));
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
                                String json = response.body().string();
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

    private void initDistrictAdapter(PullToRefreshGridView gv, ArrayList<DistrictEntity> list){
        DistrictAdapter adapter = null;
        if(adapter==null){
            adapter = new DistrictAdapter(mContext, list);
            gv.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
        gv.onRefreshComplete();
    }

    private PopupWindow popupWindow;
    private ArrayList<DistrictEntity> districtDataList;
    private LoadMoreAdapter adapter;
    /***
     * 花园小区列表
     * @param text
     */
    private void initDistrictPopupWindow(String text){

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popuplayout_district, null);
        final GridView2 pullGridView = (GridView2) contentView.findViewById(R.id.pullGridView);
        LinearLayout pullHeadedLayout = (LinearLayout) contentView.findViewById(R.id.pullHeadedLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        // 获取PullToRefreshGridView里面的head布局
        pullHeadedLayout.addView(pullGridView.getHeaderView(), params);
        ImageView ivClose = (ImageView) contentView.findViewById(R.id.ivCloseWindown);
        TextView tvLocation = (TextView) contentView.findViewById(R.id.tvLocation);
        final EditText editTextSearchDistrct = (EditText) contentView.findViewById(R.id.editTextSearchDistrct);
        TextView tvSearchDistrict = (TextView) contentView.findViewById(R.id.tvSearchDistrict); // 搜索按钮

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);


        adapter = new LoadMoreAdapter(pullGridView, districtDataList, this);
        pullGridView.setAdapter(adapter);

        pullGridView.setOnPullToRefreshingListener(new GridView2.PullToRefreshListener() {
            @Override
            public void onPullToRefreshing() {
                if(pullGridView.isPullRefreshCompleted() && pullGridView.isLoadingMoreCompleted()){
                    pullGridView.setPullRefreshCompleted(false);
                    pullGridView.setLoadingMoreCompleted(false);
                    loadMore(0);
                }
            }
        });
//        initDistrictAdapter(gridView, districtDataList);
//
        pullGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //FIXME 获取 id
                param_vilige_id = districtDataList.get(position).getId();
                villigeName = districtDataList.get(position).getName();
                tvChooseDisctrict.setText(villigeName);
                setIntent();
                popupWindow.dismiss();
            }
        });


//        pullGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//                districtPage = 1;
//                districtDataList.clear();
//                getDstrictData(false, null, city_id, district_id);
//                initDistrictAdapter(gridView, districtDataList);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//                Utils.setToast(mContext, "Pull up!");
////                districtPage++;
////                getDstrictData(false, null, city_id, district_id);
////                initDistrictAdapter(gridView, districtDataList);
//            }
//        });


        tvSearchDistrict.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String searchText = editTextSearchDistrct.getText().toString().trim();
                if("".equals(searchText)){
                    Utils.setToast(mContext, "请输入小区名");
                    return;
                }else{
                    districtDataList.clear();
                    districtPage = 1;
                    Utils.setErrorLog(TAG, "两个id>" + city_id + "<====>" + district_id);
                    // 搜索花园小区
                    getDstrictData(true, searchText, city_id, district_id);
                }

            }
        });
//        tvLocation.setText("所在地" + "  " + SpUtils.getLocationCity(mContext));
        tvLocation.setText("所在地 " + text);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(findViewById(R.id.condictionMainLayout), 0, 0, Gravity.BOTTOM);
    }

    int index = 0;
    public void loadMore(int i) {
        if (i == 0) {
            index = 0;
        } else {
            ++index;
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    private void init() {
        initWheel();
        initData();
        getDstrictData(false, null, "1", "1"); // 先默认 北京 北京 东城区
    }

    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);
    }

    private String defaultAddr = "北京市 北京 东城区";
    private void initData() {
        String address = Utils.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) {
                return;
            }else {
//                tvChooseCity.setText(data.Province + " " + data.City + " " + data.Area);

                if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                    chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                    chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
                }
            }
        }
    }

    @Override
    public void onAddressChange(String province, String city, String district) {
        tvChooseCity.setText(province + " " + city + " " + district);
        defaultAddr = province + " " + city + " " + district;

    }


    @Override
    public void onAddressChangeId(String provinceId, String cityId, String districtId) {
        Utils.setToast(mContext, provinceId +" = " + cityId + " = "+districtId);
        province_id = provinceId;
        city_id = cityId;
        district_id = districtId;
        getDstrictData(false, null, city_id, district_id);
    }

    private String province_id = "";
    private String city_id = "1";         //  默认为 北京 北京 东城区
    private String district_id = "1";     //  默认为 北京 北京 东城区
    private String param_vilige_id = "";  //  默认为 北京 北京 东城区
    private String villigeName = "";
    private int districtPage = 1;
    private int districtPageSize = 12;

    /**
     * 小区数据
     */
    private void getDstrictData(boolean isTextSearch, String text, String _ciyt_id, String _disc_id){
        if (Utils.isNetAvailable(mContext)) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Utils.getDateToken());
            if(isTextSearch){
                hashMap.put("name", text);
            }else {
                hashMap.put("city_id", _ciyt_id);
                hashMap.put("district_id", _disc_id);
//                Utils.setErrorLog(TAG, "城市id:" + _ciyt_id + "     小区id:" + _disc_id);
            }

            hashMap.put("page", districtPage);
            hashMap.put("page_size", districtPageSize);

            HttpUtils.doPost(UrlConstans.DISTRICT_LIST_URL, hashMap, new Callback() {
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

                    final SearchDistrictJsonEntity jsonEntity = new SearchDistrictJsonEntity(json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonEntity.getStatus() == 200) {
                                districtDataList = jsonEntity.getDistrictEntityList();
                            } else {
                                Utils.setToast(mContext, "无更多数据");
                            }
                        }
                    });
                }
            });
        }
    }
}
