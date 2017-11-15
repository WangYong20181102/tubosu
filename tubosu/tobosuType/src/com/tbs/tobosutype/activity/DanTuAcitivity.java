package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DanTuAdapter;
import com.tbs.tobosutype.bean.DanTuJsonItem;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.SpacingItemDecoration;
import com.tbs.tobosutype.utils.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;



public class DanTuAcitivity extends AppCompatActivity {
    @BindView(R.id.relBackDantu)
    RelativeLayout relBackDantu;
    @BindView(R.id.reclerviewDantu)
    RecyclerView reclerviewDantu;
    @BindView(R.id.tvEditDantu)
    TextView tvEditDantu;
    private boolean isEditting =  false;
    @BindView(R.id.tvDelelteDantu)
    TextView tvDelelteDantu;
    @BindView(R.id.dantuRefreshLayout)
    SwipeRefreshLayout dantuRefreshLayout;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private Context context;
    private String TAG = "DanTuAcitivity";

    private ArrayList<DanTuJsonItem.DantuBean> dantuArrayList = new ArrayList<DanTuJsonItem.DantuBean>();
    private DanTuAdapter danTuAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = DanTuAcitivity.this;
        setContentView(R.layout.activity_dantu);
        ButterKnife.bind(this);

        initView();
        getNetData();
    }


    private void initView(){

        dantuRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        dantuRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        dantuRefreshLayout.setOnRefreshListener(swipeLister);
        dantuRefreshLayout.setOnTouchListener(onTouchListener);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        reclerviewDantu.setLayoutManager(staggeredGridLayoutManager);

        reclerviewDantu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        reclerviewDantu.addItemDecoration(new SpacingItemDecoration(2, 3, true));
        reclerviewDantu.setHasFixedSize(true);
    }

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            dantuArrayList.clear();
            danTuAdapter = null;
            dantuRefreshLayout.setRefreshing(false);
            page = 1;
            getNetData();
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (dantuRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    private int page = 1;
    private void getNetData(){
        if(Util.isNetAvailable(context)){
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String type = sp.getString("typeid", "1");
            String userid = sp.getString("userid", "272286");

            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> h = new HashMap<String, Object>();
            h.put("token", Util.getDateToken());
            h.put("uid", userid);
            h.put("user_type", type);
            h.put("page_size", "10");
            h.put("page", page);
            okHttpUtil.post(Constant.FAV_TU_URL, h, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(context, "系统繁忙，请求失败。");
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setErrorLog(TAG, json);
                            if(json.contains("data")){
                                Gson gson = new Gson();
                                DanTuJsonItem tuJsonItem = gson.fromJson(json, DanTuJsonItem.class);
                                String msg = tuJsonItem.getMsg();
                                if(tuJsonItem.getStatus() == 200){
                                    dantuArrayList.addAll(tuJsonItem.getData());
                                    initAdapter();
                                }else if(tuJsonItem.getStatus() == 201 || tuJsonItem.getStatus() == 0){
                                    Util.setToast(context, msg);
                                }
                            }else {

                            }
                        }
                    });
                }
            });
        }
    }

    private void initAdapter(){
        if(danTuAdapter == null){
            danTuAdapter = new DanTuAdapter(context, dantuArrayList);
            reclerviewDantu.setAdapter(danTuAdapter);
            danTuAdapter.notifyDataSetChanged();
        }else{
            danTuAdapter.notifyDataSetChanged();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick({R.id.relBackDantu, R.id.tvDelelteDantu,R.id.tvEditDantu})
    public void onDanTuAcitivityViewClicked(View v) {
        switch (v.getId()){
            case R.id.relBackDantu:
                if(isEditting){
                    isEditting = !isEditting;
                    tvDelelteDantu.setVisibility(View.GONE);
                    tvEditDantu.setText("编辑");
                }else{
                    finish();
                }
                break;
            case R.id.tvEditDantu:
                if(isEditting){
                    // 关闭编辑状态
                    tvDelelteDantu.setVisibility(View.GONE);
                    tvEditDantu.setText("编辑");
                }else {
                    // 开启编辑状态
                    tvDelelteDantu.setVisibility(View.VISIBLE);
                    tvEditDantu.setText("取消");
                }
                isEditting = !isEditting;
                break;

            case R.id.tvDelelteDantu:
                //  删除套图的请求

                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!isEditting){
            isEditting = true;
            tvDelelteDantu.setVisibility(View.GONE);
            tvEditDantu.setText("编辑");
            return true;
        }else{
            if(keyCode == KeyEvent.KEYCODE_BACK){
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
