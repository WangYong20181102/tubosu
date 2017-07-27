package com.tbs.tobosupicture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.CaseSearchStyleAdapter;
import com.tbs.tobosupicture.adapter.SearchCaseAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.CaseConditionType;
import com.tbs.tobosupicture.bean.SearchCaseConditionEntity;
import com.tbs.tobosupicture.bean.SearchDataEntity;
import com.tbs.tobosupicture.bean.SearchRecordBean;
import com.tbs.tobosupicture.bean.SearchRecordJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.CustomExpandableListView;

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

public class ConditionActivity extends BaseActivity {

    @BindView(R.id.searchCaseRecord)
    ListView searchCaseRecord;
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

    private SearchDataEntity searchDataEntity;
    private SearchCaseConditionEntity searchCaseConditionEntity;

    private String param_area;
    private String param_layout;
    private String param_price;
    private String param_style;
    private String conditionText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = "ConditionActivity";
        mContext = ConditionActivity.this;
        setContentView(R.layout.activity_condition);
        getDataFromNet();
        ButterKnife.bind(this);

    }

    private void getDataFromNet() {
        String uid = "";
//        if(isUserLogin()){
//            uid = "**************************";
//        }

        if (Utils.isNetAvailable(mContext)) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Utils.getDateToken());
            hashMap.put("uid", uid);
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
    private boolean seeMoreRecord = false;

    private void setDataInView() {
        if (searchDataEntity != null) {
            recordBeenList = searchDataEntity.getSearch_record();


            if (recordBeenList != null && recordBeenList.size() > 0) {
                // 历史记录
                if (seeMoreRecord) {
                    if (recordBeenList.size() > 20) {
                        for (int i = 0; i < 20; i++) {
                            tempRecordBeenList.add(recordBeenList.get(i));
                        }
                    } else {
                        tempRecordBeenList.addAll(recordBeenList);
                    }
                } else {
                    if (recordBeenList.size() > 5) {
                        for (int i = 0; i < 5; i++) {
                            tempRecordBeenList.add(recordBeenList.get(i));
                        }
                    } else {
                        tempRecordBeenList.addAll(recordBeenList);
                    }
                }
                searchCaseAdapter = new SearchCaseAdapter(mContext, tempRecordBeenList);
                searchCaseRecord.setAdapter(searchCaseAdapter);
                searchCaseRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 历史记录的
                        getParamBack();
                    }
                });
            } else {
                tvHistorySearch.setVisibility(View.GONE);
                historyLinearlayout.setVisibility(View.GONE);
            }
        }

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


    private void getParamBack() {
        // 获取数据
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putString("param_area", param_area);
        b.putString("param_layout", param_layout);
        b.putString("param_price", param_price);
        b.putString("param_style", param_style);
        b.putString("condition_text", conditionText);
        intent.putExtra("params", b);

        Utils.setErrorLog(TAG, param_area + "   " + param_layout + "  " + param_price + "  " + param_style);
        setResult(10101, intent);
        finish();
    }

    @OnClick({R.id.tvSeeMoreCaseHistory, R.id.tvClearCaseHistory, R.id.tvCaseSearch, R.id.relSearchBack})
    public void onViewClickedCondictionActivity(View view) {
        switch (view.getId()) {
            case R.id.tvSeeMoreCaseHistory:
                // 显示20条

                break;
            case R.id.tvClearCaseHistory:

                break;
            case R.id.tvCaseSearch:
                getParamBack();
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

    @OnClick(R.id.relSearchBack)
    public void onViewClicked() {
    }
}
