package com.tbs.tobosutype.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.NewHomeAdapter;
import com.tbs.tobosutype.bean.NewHomeDataItem;
import com.tbs.tobosutype.customview.HomeTopFrameLayout;
import com.tbs.tobosutype.customview.MyItemDecoration;
import com.tbs.tobosutype.customview.ScrollViewExtend;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.Util;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lie on 2017/10/23.
 */

public class NewHomeActivity extends BaseActivity {
    private RelativeLayout rel_newhomebar;
    private RelativeLayout relSelectCity;
    private TextView newhomeCity;
    private String cityId;
    private String cityName;
    private Drawable drawable;
    private static final int START_ALPHA = 0;
    private static final int END_ALPHA = 255;
    private int fadingHeight = 300;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private NewHomeAdapter newHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = NewHomeActivity.this;
        TAG = NewHomeActivity.class.getSimpleName();
        setContentView(R.layout.layout_new_activity);
        initView();
        cityId = "0";
        cityName = "深圳";
//        String temp = CacheManager.getNewhomeJson(mContext);
//        if(!"".equals(temp)){
//            initData(temp);
//        }
        getDataFromNet();
        setClick();
    }

    private void initView(){
        rel_newhomebar = (RelativeLayout) findViewById(R.id.rel_newhomebar);
        relSelectCity = (RelativeLayout) findViewById(R.id.relSelectCity);
        newhomeCity = (TextView) findViewById(R.id.newhomeCity);
        drawable = getResources().getDrawable(R.drawable.color_white_head);
        drawable.setAlpha(START_ALPHA);
        rel_newhomebar.setBackgroundDrawable(drawable);




        recyclerView = (RecyclerView) findViewById(R.id.newhome_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.newhome_swiprefreshlayout);

        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //初始化swipeRreshLayout
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(swipeLister);
        swipeRefreshLayout.setOnTouchListener(onTouchListener);
//        recyclerView.setOnScrollChangeListener(scrollChangedListener);
    }

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            swipeRefreshLayout.setRefreshing(false);
            getDataFromNet();
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (swipeRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void setClick(){
        relSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle cityBundle = new Bundle();
                cityBundle.putString("fromHomeActivity", "101");
                selectCityIntent.putExtra("HomeActivitySelectcityBundle", cityBundle);
                startActivityForResult(selectCityIntent, 3);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //重新选择城市 重新加载网络加载数据
        if (requestCode == 3) {
            if(data!=null && data.getBundleExtra("city_bundle") != null){
                cityName = data.getBundleExtra("city_bundle").getString("ci");
                cityId = data.getBundleExtra("city_bundle").getString("cid");
                getSharedPreferences("Save_City_Info", MODE_PRIVATE).edit().putString("save_city_now", cityName).commit();
                AppInfoUtil.setCityName(mContext, cityName);
            }
            newhomeCity.setText(cityName);

            Intent selectCityIntent = new Intent(Constant.ACTION_HOME_SELECT_CITY);
            Bundle b = new Bundle();
            b.putString("city_selected", cityName);
            selectCityIntent.putExtra("f_select_city_bundle",b);
            sendBroadcast(selectCityIntent);
            getSharedPreferences("city", 0).edit().putString("cityName", cityName).commit();
            getDataFromNet();
        }
    }

    private void getDataFromNet(){
        if(Util.isNetAvailable(mContext)){
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("token", Util.getDateToken());
            param.put("city_id", getCityName_Id(0));
            param.put("city_name", getCityName_Id(1));
            OKHttpUtil.post(Constant.NEWHOME_URL, param, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Util.setErrorLog(TAG, "---onFailure-->>首页请求网络失败--");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Util.setErrorLog(TAG, "---banner结果-->>" + result);
                    CacheManager.setNewhomeJson(mContext, result);
                    initData(result);
                }
            });
        }
    }

    private void initData(final String json){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                final NewHomeDataItem dataItem = gson.fromJson(json, NewHomeDataItem.class);
                String msg = dataItem.getMsg();
                Util.setErrorLog(TAG, dataItem.getMsg());
                if(dataItem.getStatus() == 200){
                    final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
                    mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(mLinearLayoutManager);
                    newHomeAdapter = new NewHomeAdapter(mContext, dataItem.getData());
                    recyclerView.setAdapter(newHomeAdapter);
//                    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//                            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
//                            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
//                                    && !swipeRefreshLayout.isRefreshing()) {
//                                Util.setToast(mContext, "外面 加载更多");
//                            }
//
//                        }
//                    });

                    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            //得到当前显示的最后一个item的view
                            View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                            //得到lastChildView的bottom坐标值
                            int lastChildBottom = lastChildView.getBottom();
                            //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                            int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                            //通过这个lastChildView得到这个view当前的position值
                            int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);

                            //判断lastChildView的bottom值跟recyclerBottom
                            //判断lastPosition是不是最后一个position
                            //如果两个条件都满足则说明是真正的滑动到了底部
                            if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                                newHomeAdapter.loadMoreData(true);
                            }
                        }
                    });


                }else if(dataItem.getStatus() == 0){
                    Util.setToast(mContext, msg);
                }else {
                    Util.setErrorLog(TAG, msg);
                }
            }
        });
    }


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }



    private RecyclerView.OnScrollChangeListener scrollChangedListener = new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (scrollY > fadingHeight) {
                scrollY = fadingHeight;
            }
            drawable.setAlpha(scrollY * (END_ALPHA - START_ALPHA) / fadingHeight + START_ALPHA);
            if (scrollY >= END_ALPHA) {
                rel_newhomebar.setVisibility(View.VISIBLE);
            } else {
                rel_newhomebar.setVisibility(View.GONE);
            }
        }
    };

    private String getCityName_Id(int flag){
        if(flag>0){
            // 城市名
            return cityName;
        }else{
            // 城市id
            return cityId;
        }

    }

    @Override
    protected void onDestroy() {

        if(newHomeAdapter!=null){
            newHomeAdapter.stopVerticalMarqueeView();
        }
        super.onDestroy();
    }
}
