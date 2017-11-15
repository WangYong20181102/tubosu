package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.CompanyAdapter;
import com.tbs.tobosutype.bean.CompanyJsonItem;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
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

public class ZhuangxiuConmpanyAcitivity extends AppCompatActivity {

    @BindView(R.id.relBackGongsi)
    RelativeLayout relBackGongsi;
    @BindView(R.id.reclerviewGongsi)
    RecyclerView reclerviewGongsi;
    @BindView(R.id.gongsiRefreshLayout)
    SwipeRefreshLayout gongsiRefreshLayout;
    @BindView(R.id.tvDelelteZhuangxiuGongsi)
    TextView tvDelelteZhuangxiuGongsi;
    @BindView(R.id.tvEditZhuangxiuGongsi)
    TextView tvEditZhuangxiuGongsi;
    @BindView(R.id.iv_no_company)
    ImageView ivNoCompany;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private String TAG = "ConmpanyListActivity";
    private int page = 1;
    private boolean isEdittext = false;
    private CompanyAdapter adapter;
    private ArrayList<CompanyJsonItem.CompanyBean> companyBeanArrayList = new ArrayList<CompanyJsonItem.CompanyBean>();
    private boolean isDeletingCompany = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuangxiu_gongsi);
        context = ZhuangxiuConmpanyAcitivity.this;
        ButterKnife.bind(this);

        initView();
        getNetData();
    }

    private void initView(){
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reclerviewGongsi.setLayoutManager(linearLayoutManager);
        gongsiRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        gongsiRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isDeletingCompany){
                    //下拉刷新数据 重新初始化各种数据
                    companyBeanArrayList.clear();
                    adapter = null;
                    gongsiRefreshLayout.setRefreshing(false);
                    page = 1;
                    getNetData();
                }
            }
        });
        gongsiRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //处于下拉刷新时列表不允许点击  死锁问题
                if (gongsiRefreshLayout.isRefreshing()) {
                    return true;
                } else {
                    return false;
                }
            }
        });


    }

    public void getNetData() {
        if (Util.isNetAvailable(context)) {
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String type = sp.getString("typeid", "1");
            String userid = sp.getString("userid", "333568");
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("uid", userid);
            hashMap.put("user_type", type); //type
            hashMap.put("page_size", "10");
            hashMap.put("page", page);
            okHttpUtil.post(Constant.GONGSI_URL, hashMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(context, "系统繁忙，请稍后再试。");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (json.contains("data")) {
                                ivNoCompany.setVisibility(View.GONE);
                                Gson gson = new Gson();
                                CompanyJsonItem companyJsonItem = gson.fromJson(json, CompanyJsonItem.class);
                                String msg = companyJsonItem.getMsg();
                                if(companyJsonItem.getStatus() == 200){
                                    companyBeanArrayList.addAll(companyJsonItem.getData());
                                    initAdapter();
                                }else if(companyJsonItem.getStatus() == 0 || companyJsonItem.getStatus() == 201){
                                    Util.setToast(context, msg);
                                }

                            } else {
                                ivNoCompany.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            });
        }
    }


    private void initAdapter(){
        if(adapter == null){
            adapter = new CompanyAdapter(context, companyBeanArrayList);
            reclerviewGongsi.setAdapter(adapter);
            adapter.notifyAdapter(false);
        }else {
            adapter.notifyAdapter(false);
        }

        if(isEdittext){
            adapter.setCompanyItemClickListener(new CompanyAdapter.OnCompanyItemClickListener() {
                @Override
                public void onCompanyItemClickListener(int position) {
                    Util.setToast(context, "跳转 " + position);
                }
            });
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick({R.id.relBackGongsi, R.id.tvDelelteZhuangxiuGongsi, R.id.tvEditZhuangxiuGongsi})
    public void onZhuangxiuConmpanyAcitivityViewClicked(View v) {
        switch (v.getId()){
            case R.id.relBackGongsi:
                if(isEdittext){
                    isDeletingCompany = false;
                    setDeleteFlag(isDeletingCompany);
                    tvDelelteZhuangxiuGongsi.setVisibility(View.GONE);
                    tvEditZhuangxiuGongsi.setText("编辑");
                    isEdittext = !isEdittext;
                }else {
                    finish();
                }
                break;
            case R.id.tvEditZhuangxiuGongsi:
                if(isEdittext){
                    // 正在编辑中
                    tvEditZhuangxiuGongsi.setText("编辑");
                    tvDelelteZhuangxiuGongsi.setVisibility(View.GONE);
                    isDeletingCompany = false;
                    setDeleteFlag(isDeletingCompany);
                }else {
                    // 没有编辑
                    tvEditZhuangxiuGongsi.setText("取消");
                    tvDelelteZhuangxiuGongsi.setVisibility(View.VISIBLE);
                    isDeletingCompany = true;
                    setDeleteFlag(isDeletingCompany);
                }
                isEdittext = !isEdittext;
                break;
            case R.id.tvDelelteZhuangxiuGongsi:
                // 删除请求
                if(adapter!=null){

                    adapter.setOnDeleteCompanyListener(new CompanyAdapter.OnDeleteCompanyListener() {
                        @Override
                        public void deleteCompanyListener(ArrayList<Boolean> deleteList) {
                            for(int i=0;i<deleteList.size();i++){
                                if(deleteList.get(i)){
                                    Util.setErrorLog(TAG, "删除的id是" + companyBeanArrayList.get(i));
                                }

                            }
                        }
                    });
                    if(adapter.getDeleteData()){
                        Util.setToast(context, "你没有选择公司");
                    }
                }
                break;
        }
    }


    private void setDeleteFlag(boolean flag){
        if(adapter!=null){
            adapter.setDeleting(flag);
            if(flag){
                adapter.notifyDataSetChanged();
            }else {
                adapter.notifyAdapter(flag);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isEdittext){
            isDeletingCompany = false;
            setDeleteFlag(isDeletingCompany);
            isEdittext = !isEdittext;
            tvEditZhuangxiuGongsi.setText("编辑");
            tvDelelteZhuangxiuGongsi.setVisibility(View.GONE);
            return true;
        }else {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}