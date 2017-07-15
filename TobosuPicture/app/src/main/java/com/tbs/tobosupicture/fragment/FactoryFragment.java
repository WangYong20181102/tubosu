package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.SamplePictureAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.SamplePicBeanEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * 工装
 * Created by Lie on 2017/7/14.
 */

public class FactoryFragment extends BaseFragment {
    private final String TAG = "FactoryFragment";
    private Context context = getActivity();


    @BindView(R.id.ivFactoryChooseStyle)
    ImageView ivFactoryChooseStyle;
    @BindView(R.id.tvChooseStyle)
    TextView tvChooseStyle;
    @BindView(R.id.expandableListview)
    ExpandableListView expandableListview;
    @BindView(R.id.factoryRecyclerView)
    RecyclerView factoryRecyclerView;
    @BindView(R.id.factorySwipRefreshLayout)
    SwipeRefreshLayout factorySwipRefreshLayout;
    Unbinder unbinder;

    private LinearLayoutManager linearLayoutManager;
    
    private HashMap<String, Object> param;
    private int page = 1;
    private int pageSize = 10;

    private ArrayList<SamplePicBeanEntity> samplePicList = new ArrayList<SamplePicBeanEntity>();
    private SamplePictureAdapter samplePicAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_factory, null, false);
        unbinder = ButterKnife.bind(this, view);
        initListViewSetting();
        getDataFromNet();
        initData();
        return view;
    }

    private void getDataFromNet() {
        param = new HashMap<String, Object>();
        param.put("token", Utils.getDateToken());
        param.put("type", "2");
        param.put("page", page);
        param.put("page_size", pageSize);
        HttpUtils.doPost(UrlConstans.GET_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.setToast(getActivity(), "系统繁忙，稍后再试!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().string();
                try {
                    JSONObject object = new JSONObject(json);
                    if(object.getInt("status")==200){
                        JSONArray arr = object.getJSONArray("data");
                        SamplePicBeanEntity entity = null;
                        for(int i=0, len=arr.length();i<len;i++){
                            entity = new SamplePicBeanEntity(arr.getJSONObject(i));
                            samplePicList.add(entity);
                        }
                    }else if(object.getInt("status")==201){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(samplePicAdapter!=null){
                                    samplePicAdapter.noMoreData();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.setErrorLog(TAG, json);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initListViewAdapter();
                    }
                });

            }
        });

    }

    private void initData() {

    }

    private void initListViewAdapter(){

        factorySwipRefreshLayout.setRefreshing(false);
        if(samplePicAdapter==null){
            samplePicAdapter = new SamplePictureAdapter(getActivity(), samplePicList);
            factoryRecyclerView.setAdapter(samplePicAdapter);
        }else{
            samplePicAdapter.notifyDataSetChanged();
        }

        if(samplePicAdapter!=null){
            samplePicAdapter.hideLoadMoreMessage();
        }

    }



    private void initListViewSetting(){
        // 设置下拉进度的背景颜色，默认就是白色的
        factorySwipRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        factorySwipRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        factorySwipRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        factoryRecyclerView.setLayoutManager(linearLayoutManager);

        factoryRecyclerView.setOnScrollListener(onScrollListener);
        factoryRecyclerView.setOnTouchListener(onTouchListener);
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
                    && !factorySwipRefreshLayout.isRefreshing()) {
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
            if (factorySwipRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            samplePicList.clear();
            page = 1;
            if(samplePicAdapter!=null){
                samplePicAdapter.hideLoadMoreMessage();
            }
            getDataFromNet();

        }
    };

    private void loadMore(){
        page++;
        if(samplePicAdapter!=null){
            samplePicAdapter.showLoadMoreMessage();
        }

        factorySwipRefreshLayout.setRefreshing(false);
        getDataFromNet();
        System.out.println("-----**-onScrolled load more completed------");
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tvChooseStyle)
    public void onViewClicked() {

    }
}
