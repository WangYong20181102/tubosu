package com.tbs.tobosupicture.fragment;

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
import com.tbs.tobosupicture.adapter.SamplePictureAdapter;
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

    private LinearLayoutManager linearLayoutManager;
    
    Unbinder unbinder;

    private CaseJsonEntity caseJsonEntity;
    private ArrayList<CaseJsonEntity.CaseEntity> caseList;
    private CaseAdapter caseAdapter;

    private int page = 1;
    private int pageSize = 10;

    public DecorationCaseFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decoration_case, null);
        getDataFromNet();
        unbinder = ButterKnife.bind(this, view);
        initListViewSetting();
        return view;
    }

    private void getDataFromNet() {
        if(Utils.isNetAvailable(getActivity())){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Utils.getDateToken());
            hashMap.put("page", page);
            hashMap.put("pageSize", pageSize);
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
                            if(caseJsonEntity.getStatus()==200){
                                caseList = caseJsonEntity.getDataList();
                                initListView();
                            }else {
                                Utils.setToast(getActivity(), "无更多数据");
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tvSearchCase)
    public void onViewClickedDecorateCaseFragment(View view) {
        switch (view.getId()){
            case R.id.tvSearchCase:
                startActivityForResult(new Intent(getActivity(), ConditionActivity.class), 0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                getDataFromNet();
                break;
        }
    }

    private void initListViewSetting(){
        // 设置下拉进度的背景颜色，默认就是白色的
        caseSwipRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        caseSwipRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        caseSwipRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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

    private void initListView(){
        caseSwipRefreshLayout.setRefreshing(false);
        if(caseAdapter==null){
            caseAdapter = new CaseAdapter(getActivity(), caseList);
            caseRecyclerView.setAdapter(caseAdapter);
        }else {
            caseAdapter.notifyDataSetChanged();
        }
        if(caseAdapter!=null){
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
            if(caseAdapter!=null){
                caseAdapter.hideLoadMoreMessage();
            }
            getDataFromNet();

        }
    };

    private void loadMore(){
        page++;
        if(caseAdapter!=null){
            caseAdapter.showLoadMoreMessage();
        }

        caseSwipRefreshLayout.setRefreshing(false);
        getDataFromNet();
        System.out.println("-----**-onScrolled load more completed------");
    }
}
