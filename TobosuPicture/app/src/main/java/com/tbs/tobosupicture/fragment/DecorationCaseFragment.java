package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.ConditionActivity;
import com.tbs.tobosupicture.adapter.CaseAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.CaseJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/6/29 11:08.
 * 装修案例fragment
 */

public class DecorationCaseFragment extends BaseFragment {
    private final String TAG = "DecorationCaseFragment";
    @BindView(R.id.tvSearchCase)
    TextView tvSearchCase;
    @BindView(R.id.tvSearchTipText)
    TextView tvSearchTipText;
    @BindView(R.id.caseRecyclerView)
    RecyclerView caseRecyclerView;
    @BindView(R.id.caseSwipRefreshLayout)
    SwipeRefreshLayout caseSwipRefreshLayout;
    @BindView(R.id.caseLocation)
    TextView caseLocation;

    private LinearLayoutManager linearLayoutManager;
    private Context mContext;

    Unbinder unbinder;

    private CaseJsonEntity caseJsonEntity;
    private ArrayList<CaseJsonEntity.CaseEntity> caseList = new ArrayList<CaseJsonEntity.CaseEntity>();
    private CaseAdapter caseAdapter;

    private int page = 1;
    private int pageSize = 10;
    private String param_area;
    private String param_layout;
    private String param_price;
    private String param_style;
    private String param_city_id = "";

    private boolean isFromCondictionActivity = false;

    public DecorationCaseFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decoration_case, null);
        mContext = getActivity();
        getDataFromNet(getCommonHashMap());
        unbinder = ButterKnife.bind(this, view);
        initListViewSetting();
        return view;
    }

    private HashMap<String, Object> getCommonHashMap(){
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", Utils.getDateToken());
        hashMap.put("page", page);
        hashMap.put("pageSize", pageSize);
        return hashMap;
    }

    private void getDataFromNet(HashMap<String, Object> hashMap) {
        if (Utils.isNetAvailable(getActivity())) {
            HttpUtils.doPost(UrlConstans.SEARCH_CASE_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(getActivity(), "网络繁忙,稍后再试");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Utils.setErrorLog(TAG, json);
                    caseJsonEntity = new CaseJsonEntity(json);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (caseJsonEntity.getStatus() == 200) {
                                caseList.addAll(caseJsonEntity.getDataList());
                                initListView();
                            } else {
                                if (caseAdapter != null) {
                                    caseAdapter.hideLoadMoreMessage();
                                    caseAdapter.noMoreData();
                                }
//                                caseList.clear();
//                                Utils.setToast(getActivity(), caseJsonEntity.getMsg());
                                // 无数据页面在此显示
                            }
                        }
                    });
                }
            });
        }else {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tvSearchCase, R.id.caseLocation})
    public void onViewClickedDecorateCaseFragment(View view) {
        switch (view.getId()) {
            case R.id.tvSearchCase:
                if (Utils.isNetAvailable(mContext)){
                    startActivityForResult(new Intent(getActivity(), ConditionActivity.class), 0);
                    isFromCondictionActivity = false;
                    param_city_id = "";
                    tvSearchTipText.setText("搜索");
                }
                break;
            case R.id.caseLocation:
//                startActivityForResult(new Intent(getActivity(), SelectCityActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 10101:
                if(data!=null && data.getBundleExtra("params")!=null){
                    isFromCondictionActivity = true;
                    Bundle bundle = data.getBundleExtra("params");
                    param_area = bundle.getString("param_area");
                    param_layout = bundle.getString("param_layout");
                    param_price = bundle.getString("param_price");
                    param_style = bundle.getString("param_style");
                    param_city_id = bundle.getString("param_city_id");
                    tvSearchTipText.setText(bundle.getString("condition_text"));
                }

                caseList.clear();
                Utils.setErrorLog(TAG, param_area + "   "+ param_layout + "  "  + param_price + "  " +param_style + " " + param_city_id);
                page = 1;
                getDataFromNet(getPareaHashMap(param_area,param_layout,param_price,param_style, param_city_id));
                break;
        }
    }

    private HashMap<String, Object> getPareaHashMap(String area, String layout, String price, String style, String cityId){
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", Utils.getDateToken());
        hashMap.put("page", page);
        hashMap.put("pageSize", pageSize);
        hashMap.put("area", area);
        hashMap.put("layout", layout);
        hashMap.put("city_id", cityId);
        hashMap.put("price", price);
        hashMap.put("style", style);
        return hashMap;
    }

    private void initListViewSetting() {
        // 设置下拉进度的背景颜色，默认就是白色的
        caseSwipRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        caseSwipRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        caseSwipRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        caseRecyclerView.setLayoutManager(linearLayoutManager);

        caseRecyclerView.setOnScrollListener(onScrollListener);
        caseRecyclerView.setOnTouchListener(onTouchListener);
    }

    //显示列表的滑动监听事件 上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
//            Log.e(TAG, "最后可见目标===" + lastVisiableItem + "集合总数===" + mLinearLayoutManager.getItemCount() + "==newState==" + newState + "==刷新状态==" + swipeRefreshLayout.isRefreshing());
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= linearLayoutManager.getItemCount()
                    && !caseSwipRefreshLayout.isRefreshing()) {
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (caseSwipRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void initListView() {
        caseSwipRefreshLayout.setRefreshing(false);
        if (caseAdapter == null) {
            caseAdapter = new CaseAdapter(mContext, caseList);
            caseRecyclerView.setAdapter(caseAdapter);
        } else {
            caseAdapter.notifyDataSetChanged();
        }
        if (caseAdapter != null) {
            caseAdapter.hideLoadMoreMessage();
        }
    }

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            caseList.clear();
            page = 1;
            if (caseAdapter != null) {
                caseAdapter.hideLoadMoreMessage();
            }
            if(isFromCondictionActivity){
                getDataFromNet(getPareaHashMap(param_area,param_layout,param_price,param_style,param_city_id));
            }else{
                getDataFromNet(getCommonHashMap());
            }
        }
    };

    private void loadMore() {
        page++;
        if (caseAdapter != null) {
            caseAdapter.showLoadMoreMessage();
        }

        caseSwipRefreshLayout.setRefreshing(false);
        if(isFromCondictionActivity){
            getDataFromNet(getPareaHashMap(param_area,param_layout,param_price,param_style,param_city_id));
        }else{
            getDataFromNet(getCommonHashMap());
        }
    }

}
