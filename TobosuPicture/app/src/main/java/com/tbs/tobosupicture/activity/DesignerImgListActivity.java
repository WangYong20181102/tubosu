package com.tbs.tobosupicture.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.DesignerPictureListAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.AnLiJsonEntity;
import com.tbs.tobosupicture.bean.XiaoGuoTuJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;
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
 * 设计师案例 样板图 列表
 * Created by Lie on 2017/7/25.
 */

public class DesignerImgListActivity extends BaseActivity {
    private final int SAMPLE_TYPE = 0; // 样板图
    private final int CASE_TYPE = 1;   // 案例图

    @BindView(R.id.designerImgListRecyclerView)
    RecyclerView designerImgListRecyclerView;
    @BindView(R.id.designerImgListSwipRefreshLayout)
    SwipeRefreshLayout designerImgListSwipRefreshLayout;
    @BindView(R.id.relConcernDesigner)
    RelativeLayout relConcernDesigner;
    @BindView(R.id.relDesignerGetDesign)
    RelativeLayout relDesignerGetDesign;
    @BindView(R.id.llDesignerlPicBack)
    FrameLayout llDesignerlPicBack;

    private String name = "";
    private String viewCount = "";
    private String fansCount = "";
    private String designerIcon = "";
    private String designerId;
    private String des = ""; // 描述

    private DesignerPictureListAdapter designerPictureListAdapter;
    private ArrayList<XiaoGuoTuJsonEntity.XiaoGuoTu> samplePicDataList = new ArrayList<XiaoGuoTuJsonEntity.XiaoGuoTu>(); // 样板图
    private ArrayList<AnLiJsonEntity.AnLiEntity> casePicDataList = new ArrayList<AnLiJsonEntity.AnLiEntity>(); //  案例图

    private LinearLayoutManager linearLayoutManager;

    private int page = 1;
    private int pageSize = 10;
    private int dataType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_list);
        TAG = "DesignerImgListActivity";
        mContext = DesignerImgListActivity.this;
        ButterKnife.bind(this);
        initView();
        getIntentData();

    }

    private void initView(){
        llDesignerlPicBack.bringToFront();
        initSetting();
    }

    private void getIntentData() {
        if(getIntent()!=null && getIntent().getBundleExtra("designerBundle")!=null){
            Bundle b = getIntent().getBundleExtra("designerBundle");
            name = b.getString("designerName");
            viewCount = b.getString("viewNum");
            fansCount = b.getString("fanNum");
            designerIcon = b.getString("iconUrl");
            designerId = b.getString("desid");
            dataType = b.getInt("type");  // 1 案例，  0 样板
            des ="粉丝 "+fansCount + " / 浏览" + viewCount;

        }else{
            des ="粉丝 0  / 浏览 0";
        }

        getDataFromNet(dataType);
    }


    private void getDataFromNet(final int sourceType) {
        if(Utils.isNetAvailable(mContext)){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Utils.getDateToken());
            hashMap.put("designer_id", designerId);
            hashMap.put("page", page);
            hashMap.put("page_size", pageSize);
            HttpUtils.doPost(UrlConstans.getListUrl(sourceType), hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(mContext, "系统繁忙");
                            designerImgListSwipRefreshLayout.setRefreshing(false);
                            designerPictureListAdapter.hideLoadMoreMessage();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();

                    Utils.setErrorLog(TAG, json);

                    Gson gson = new Gson();
                    // 1 案例，  0 样板效果
                    if(sourceType == 0){
                        XiaoGuoTuJsonEntity xiaoGuoTuJsonEntity = gson.fromJson(json, XiaoGuoTuJsonEntity.class);
                        if(xiaoGuoTuJsonEntity.getStatus() == 200){
                            samplePicDataList = xiaoGuoTuJsonEntity.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    designerImgListSwipRefreshLayout.setRefreshing(false);
                                    initAdapter(sourceType);
                                }
                            });
                        }else {
                            final String msg = xiaoGuoTuJsonEntity.getMsg();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, msg);
                                    designerImgListSwipRefreshLayout.setRefreshing(false);
                                    designerPictureListAdapter.hideLoadMoreMessage();
                                }
                            });
                        }
                    }else{
                        AnLiJsonEntity anLiJsonEntity = gson.fromJson(json, AnLiJsonEntity.class);
                        if(anLiJsonEntity.getStatus() == 200){
                            casePicDataList = anLiJsonEntity.getData();
                            initAdapter(sourceType);
                        }else {
                            final String msg = anLiJsonEntity.getMsg();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, msg);
                                }
                            });
                        }
                    }


                }
            });
        }
    }

    private void initAdapter(int type){
        if(type == 0){
            designerPictureListAdapter = new DesignerPictureListAdapter(mContext, samplePicDataList, null, SAMPLE_TYPE, designerIcon, name, des);
            designerImgListRecyclerView.setAdapter(designerPictureListAdapter);
            designerPictureListAdapter.notifyDataSetChanged();
        }else{
            designerPictureListAdapter = new DesignerPictureListAdapter(mContext, null, casePicDataList, CASE_TYPE, designerIcon, name, des);
            designerImgListRecyclerView.setAdapter(designerPictureListAdapter);
            designerPictureListAdapter.notifyDataSetChanged();
        }
        designerImgListSwipRefreshLayout.setRefreshing(false);
        designerPictureListAdapter.hideLoadMoreMessage();
    }

    @OnClick({R.id.relConcernDesigner, R.id.relDesignerGetDesign,R.id.llDesignerlPicBack})
    public void onViewClickedDesignerImgActivity(View view) {
        switch (view.getId()) {
            case R.id.relConcernDesigner:
                Utils.setToast(mContext, "54613");
                break;
            case R.id.relDesignerGetDesign:

                break;
            case R.id.llDesignerlPicBack:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    private void initSetting(){
// 设置下拉进度的背景颜色，默认就是白色的
        designerImgListSwipRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        designerImgListSwipRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        designerImgListSwipRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        designerImgListRecyclerView.setLayoutManager(linearLayoutManager);

        designerImgListRecyclerView.addOnScrollListener(onScrollListener);
        designerImgListRecyclerView.setOnTouchListener(onTouchListener);
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
                    && !designerImgListSwipRefreshLayout.isRefreshing()) {
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
            if (designerImgListSwipRefreshLayout.isRefreshing()) {
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
            if(samplePicDataList!=null){
                samplePicDataList.clear();
            }

            if(casePicDataList!=null){
                casePicDataList.clear();
            }

            page = 1;
            if(designerPictureListAdapter!=null){
                designerPictureListAdapter.hideLoadMoreMessage();
            }

            getDataFromNet(dataType);

        }
    };

    private void loadMore(){
        page++;
        if(designerPictureListAdapter!=null){
            designerPictureListAdapter.showLoadMoreMessage();
        }

        designerImgListSwipRefreshLayout.setRefreshing(false);
        getDataFromNet(dataType);
        System.out.println("-----**-onScrolled load more completed------");
    }
}
