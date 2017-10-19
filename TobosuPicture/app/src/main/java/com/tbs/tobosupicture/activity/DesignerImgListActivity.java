package com.tbs.tobosupicture.activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.DesignerPictureListAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.AnLiJsonEntity;
import com.tbs.tobosupicture.bean.XiaoGuoTuJsonEntity;
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
 * 设计师 [样板图] [案例] 列表
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
    @BindView(R.id.tvGetConcern)
    TextView tvGetConcern;

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
    private String isCollect = "";

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
            isCollect = b.getString("isCollect");
            des ="粉丝 "+fansCount + " / 浏览" + viewCount;
//            Utils.setToast(mContext, Utils.getDateToken());

            if ("1".equals(isCollect)) {
                tvGetConcern.setTextColor(Color.parseColor("#FA8817"));
                tvGetConcern.setText("取消关注");
                Drawable leftDrawable = getResources().getDrawable(R.drawable.jiaguanzhu);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                tvGetConcern.setCompoundDrawables(leftDrawable, null, null, null);
            } else {
                tvGetConcern.setTextColor(Color.parseColor("#858585"));
                tvGetConcern.setText("关注");
                Drawable leftDrawable = getResources().getDrawable(R.drawable.jiaguanzhu2);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                tvGetConcern.setCompoundDrawables(leftDrawable, null, null, null);
            }

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

            Utils.setErrorLog(TAG, "当前设计师id是" + designerId + "当前页数 " + page);

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
                            samplePicDataList.addAll(xiaoGuoTuJsonEntity.getData());
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
                            casePicDataList.addAll(anLiJsonEntity.getData());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initAdapter(sourceType);
                                }
                            });
                        }else {
                            final String msg = anLiJsonEntity.getMsg();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, msg);
                                    designerImgListSwipRefreshLayout.setRefreshing(false);
                                    designerPictureListAdapter.hideLoadMoreMessage();
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
            if(designerPictureListAdapter == null){
                designerPictureListAdapter = new DesignerPictureListAdapter(mContext, samplePicDataList, null, SAMPLE_TYPE, designerIcon, name, des);
                designerImgListRecyclerView.setAdapter(designerPictureListAdapter);
            }else {
                designerPictureListAdapter.notifyDataSetChanged();
            }

        }else{
            if(designerPictureListAdapter==null){
                designerPictureListAdapter = new DesignerPictureListAdapter(mContext, null, casePicDataList, CASE_TYPE, designerIcon, name, des);
                designerImgListRecyclerView.setAdapter(designerPictureListAdapter);
            }else {
                designerPictureListAdapter.notifyDataSetChanged();
            }

        }
        designerImgListSwipRefreshLayout.setRefreshing(false);
        designerPictureListAdapter.hideLoadMoreMessage();
    }

    @OnClick({R.id.relConcernDesigner, R.id.relDesignerGetDesign,R.id.llDesignerlPicBack})
    public void onViewClickedDesignerImgActivity(View view) {
        switch (view.getId()) {
            case R.id.relConcernDesigner:
                if(Utils.userIsLogin(mContext)){
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("token", Utils.getDateToken());
                    hashMap.put("uid", SpUtils.getUserUid(mContext));
                    hashMap.put("designer_id", designerId);
                    HttpUtils.doPost(UrlConstans.CONCERN_URL, hashMap, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, "关注失败，稍后再试");
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
                                        JSONObject object = new JSONObject(json);
                                        if (object.getInt("status") == 200) {
                                            if (object.getString("msg").contains("取消")) {
                                                tvGetConcern.setTextColor(Color.parseColor("#858585"));
                                                tvGetConcern.setText("关注");
                                                Drawable leftDrawable = getResources().getDrawable(R.drawable.jiaguanzhu2);
                                                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                                                tvGetConcern.setCompoundDrawables(leftDrawable, null, null, null);
                                            } else {
                                                tvGetConcern.setTextColor(Color.parseColor("#FA8817"));
                                                tvGetConcern.setText("取消关注");
                                                Drawable leftDrawable = getResources().getDrawable(R.drawable.jiaguanzhu);
                                                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                                                tvGetConcern.setCompoundDrawables(leftDrawable, null, null, null);
                                            }
                                        }
                                        Utils.setToast(mContext, object.getString("msg"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.relDesignerGetDesign:
                startActivity(new Intent(mContext, GetPriceActivity.class));
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
