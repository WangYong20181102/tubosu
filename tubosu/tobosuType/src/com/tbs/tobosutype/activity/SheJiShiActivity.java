package com.tbs.tobosutype.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DesignerInfoAdapter;
import com.tbs.tobosutype.bean.DesignerInfoBean;
import com.tbs.tobosutype.bean.DesignerInfoCaseBean;
import com.tbs.tobosutype.bean.DesignerInfoDesignBean;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
    底部发单【找TA免费设计】：http://m.tobosu.com/quote
    设计方案【获取此设计】：http://m.tobosu.com/quote
    装修案例【获取报价】：http://m.tobosu.com/free_price_page
*/

public class SheJiShiActivity extends com.tbs.tobosutype.base.BaseActivity implements View.OnClickListener {
    private Intent dataIntent;
    private String des_id;
    private android.widget.RelativeLayout relShejishiBack;
    private android.widget.ImageView shejishiShare;
    private android.support.v7.widget.RecyclerView shejishiRecyclerView;
    private android.support.v4.widget.SwipeRefreshLayout shejishiSwip;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private int shejiPage = 2;
    private int anliPage = 2;
    private int page = 1;
    private int page_size = 10;
    private DesignerInfoBean designerInfoBean;
    private List<DesignerInfoCaseBean> anliList = new ArrayList<DesignerInfoCaseBean>();
    private List<DesignerInfoDesignBean> shejiList = new ArrayList<DesignerInfoDesignBean>();
    private DesignerInfoAdapter shejishiAdapter;
    private String shareUrl;
    private int loadMoreDataType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shijishi);
        mContext = SheJiShiActivity.this;
        TAG = SheJiShiActivity.class.getSimpleName();
        bindViews();
    }

    private void bindViews(){
        shejishiSwip = (SwipeRefreshLayout) findViewById(R.id.shejishiSwip);
        shejishiRecyclerView = (RecyclerView) findViewById(R.id.shejishiRecyclerView);
        shejishiShare = (ImageView) findViewById(R.id.shejishiShare);
        relShejishiBack = (RelativeLayout) findViewById(R.id.relShejishiBack);

        shejishiShare.setOnClickListener(this);
        relShejishiBack.setOnClickListener(this);

        dataIntent = getIntent();
        des_id = dataIntent.getStringExtra("designer_id");
        initViews();
    }

    private void initViews(){
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shejishiRecyclerView.setLayoutManager(linearLayoutManager);
        shejishiSwip.setProgressBackgroundColorSchemeColor(Color.WHITE);
        shejishiSwip.setColorSchemeResources(R.color.colorAccent);
        shejishiSwip.setOnRefreshListener(swipeLister);
        shejishiSwip.setOnTouchListener(onTouchListener);

        shejishiRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(shejishiAdapter != null && !shejishiAdapter.cantLoadMore()){
                    loadMoreDataType = shejishiAdapter.getClickType();
                    //得到当前显示的最后一个item的view
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && lastPosition + 2 >= recyclerView.getLayoutManager().getItemCount()) {
                        isLoading = true;
                        loadMoreData(loadMoreDataType);
                    }
                }
//
//                //设置其透明度
//                float alpha = 0;
//                int scollYHeight = getScollYHeight(true, tubosu.getHeight());
//
//                int baseHeight = 574;
//                if (scollYHeight >= baseHeight) {
//                    alpha = 1;
//                } else {
//                    alpha = scollYHeight / (baseHeight * 1.0f);
//                    if (alpha > 0.44) {
//                        ivYingying.setVisibility(View.VISIBLE);
//                        home_view.setVisibility(View.INVISIBLE);// 白色渐变 隐藏
//                        rel_newhomebar.setVisibility(View.VISIBLE);
//                        iv_sanjiaoxing.setBackgroundResource(R.drawable.tt);
//                        iv_add.setBackgroundResource(R.drawable.sdf);
//                        home_kefu.setBackgroundResource(R.drawable.kefu_black);
//                        newhomeCity.setTextColor(Color.parseColor("#000000"));
//                        app_title_text.setTextColor(Color.parseColor("#000000"));
//                        rel_newhomebar.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    } else {
//
//                        ivYingying.setVisibility(View.GONE);
//                        home_view.setVisibility(View.VISIBLE);
//                        rel_newhomebar.setVisibility(View.INVISIBLE);
//                        iv_sanjiaoxing.setBackgroundResource(R.drawable.sanjiaoxing);
//                        iv_add.setBackgroundResource(R.drawable.ad_icon);
//                        home_kefu.setBackgroundResource(R.drawable.home_kefu);
//                        newhomeCity.setTextColor(Color.parseColor("#FFFFFF"));
//                        app_title_text.setTextColor(Color.parseColor("#FFFFFF"));
//                        rel_newhomebar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
//                    }
//
//                }
//                rel_newhomebar.setAlpha(alpha);
//                CacheManager.setChentaoFlag(mContext, 44);
            }
        });
        getData();
    }


    private void loadMoreData(final int type){
        String url = "";
        HashMap<String, Object> hashMap = new HashMap<String,Object>();
        hashMap.put("token", Util.getDateToken());
        hashMap.put("des_id", des_id);
        hashMap.put("page_size",page_size);

        if(type == 0 && !shejishiAdapter.cantLoadMoreSheji()){
            // 设计
            shejiPage++;
            hashMap.put("page", shejiPage);
            url = Constant.MORE_SHEJI_URL;
        }else {
            if(type == 1 && !shejishiAdapter.cantLoadMoreAnli()){
                // 案例
                anliPage++;
                hashMap.put("page", anliPage);
                url = Constant.MORE_ANLI_URL;
            }else {
                return;
            }
        }
        if(Util.isNetAvailable(mContext)){

            OKHttpUtil.post(url, hashMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    isLoading = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "系统繁忙，稍后再试~");
                        }
                    });

                    e.printStackTrace();
                    Util.setErrorLog(TAG, "加载更多失败---");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            isLoading = false;
                            try {
                                JSONObject moreDataJson = new JSONObject(json);
                                String msg = moreDataJson.getString("msg");
                                if(moreDataJson.getInt("status") == 200){
                                    JSONObject data = moreDataJson.getJSONObject("data");
                                    if(type == 0){
                                        // 设计
                                        JSONArray moreShejiArr = data.getJSONArray("designer_pic");
                                        List<DesignerInfoDesignBean> tempMoreShejiList = new ArrayList<DesignerInfoDesignBean>();
                                        for (int i = 0; i < moreShejiArr.length(); i++) {
                                            Gson shejiGson = new Gson();
                                            DesignerInfoDesignBean designBean = shejiGson.fromJson(moreShejiArr.getJSONObject(i).toString(), DesignerInfoDesignBean.class);
                                            tempMoreShejiList.add(designBean);
                                        }
                                        if(shejishiAdapter != null){
                                            shejishiAdapter.setShejiDataList(tempMoreShejiList);
                                            shejishiAdapter.notifyDataSetChanged();
                                        }
                                    }else{
                                        //  案例
                                        JSONArray moreAnliArr = data.getJSONArray("anli");
                                        List<DesignerInfoCaseBean> tempMoreAnliList = new ArrayList<>();
                                        for (int i = 0; i < moreAnliArr.length(); i++) {
                                            Gson anliGson = new Gson();
                                            DesignerInfoCaseBean anliBean = anliGson.fromJson(moreAnliArr.getJSONObject(i).toString(), DesignerInfoCaseBean.class);
                                            tempMoreAnliList.add(anliBean);
                                        }
                                        if(shejishiAdapter != null){
                                            shejishiAdapter.setAnliDataList(tempMoreAnliList);
                                            shejishiAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }else if(moreDataJson.getInt("status") == 201){
                                    Util.setErrorLog(TAG, "加载更多来了 status = 201");
                                    Util.setToast(mContext, msg);
                                }else if(moreDataJson.getInt("status") == 0){
                                    Util.setErrorLog(TAG, "设加载更多来了 status = 0");
                                    Util.setToast(mContext, msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


        }
    }


    private void getData(){
        if(Util.isNetAvailable(mContext)){
            HashMap<String, Object> hashMap = new HashMap<String,Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("des_id", des_id);
            hashMap.put("page",page);
            hashMap.put("page_size",page_size);
            OKHttpUtil.post(Constant.SHEJISHI_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "获取设计师信息失败，稍后再试~");
                        }
                    });
                    e.printStackTrace();
                    Util.setErrorLog(TAG, "获取设计师信息失败---0");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                JSONObject sheji = new JSONObject(json);
                                if(sheji.getInt("status") == 200){
                                    JSONObject data = sheji.getJSONObject("data");
                                    JSONObject designerInfo = data.getJSONObject("designer_info");
                                    if(designerInfoBean == null){
                                        Gson infoGson = new Gson();
                                        designerInfoBean = infoGson.fromJson(designerInfo.toString(), DesignerInfoBean.class);
                                        shareUrl = designerInfoBean.getShare();
                                    }
                                    JSONArray shejiArr = data.getJSONArray("designer_pic");
                                    List<DesignerInfoDesignBean> tempShejiList = new ArrayList<>();
                                    for (int i = 0; i < shejiArr.length(); i++) {
                                        Gson shejiGson = new Gson();
                                        DesignerInfoDesignBean designBean = shejiGson.fromJson(shejiArr.getJSONObject(i).toString(), DesignerInfoDesignBean.class);
                                        tempShejiList.add(designBean);
                                    }
                                    shejiList.addAll(tempShejiList);


                                    JSONArray anliArr = data.getJSONArray("anli");
                                    List<DesignerInfoCaseBean> tempAnliList = new ArrayList<>();
                                    for (int i = 0; i < anliArr.length(); i++) {
                                        Gson anliGson = new Gson();
                                        DesignerInfoCaseBean anliBean = anliGson.fromJson(anliArr.getJSONObject(i).toString(), DesignerInfoCaseBean.class);
                                        tempAnliList.add(anliBean);
                                    }
                                    anliList.addAll(tempAnliList);

                                    if(shejishiAdapter == null){
                                        if(shejiList.size() == 0 && anliList.size() == 0){
                                            shejishiAdapter = new DesignerInfoAdapter(mContext, designerInfoBean);
                                        }else if(shejiList.size()>0 && anliList.size() == 0){
                                            shejishiAdapter = new DesignerInfoAdapter(mContext, designerInfoBean, shejiList);
                                        }else if(shejiList.size() == 0 && anliList.size() >0){
                                            shejishiAdapter = new DesignerInfoAdapter(mContext, designerInfoBean, anliList, 1);
                                        }else if(shejiList.size() > 0 && anliList.size() >0){
                                            shejishiAdapter = new DesignerInfoAdapter(mContext, designerInfoBean, shejiList, anliList);
                                        }
                                        shejishiRecyclerView.setAdapter(shejishiAdapter);
                                        shejishiAdapter.notifyDataSetChanged();
                                    }else {
                                        shejishiAdapter.notifyDataSetChanged();
                                    }
                                }else if(sheji.getInt("status") == 201){
                                    Util.setErrorLog(TAG, "设计师来了201");

                                }else if(sheji.getInt("status") == 0){
                                    Util.setErrorLog(TAG, "设计师来了0");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


        }
    }


    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            shejiList.clear();
            anliList.clear();
            shejiPage = 1;
            anliPage = 1;
            shejishiAdapter = null;
            shejishiSwip.setRefreshing(false);
            page = 1;
            getData();
        }
    };


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (shejishiSwip.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shejishiShare:
                Util.setToast(mContext,"分享啦链接是" + shareUrl);
                break;
            case R.id.relShejishiBack:
                finish();
                break;
        }
    }
}
