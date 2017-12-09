package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.ServiceItemAdapter;
import com.tbs.tobosutype.bean._CompanyDetail;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * create by lin
 * 从装修公司主页进入
 * 服务项目列表
 */
public class ServiceItemActivity extends AppCompatActivity {
    @BindView(R.id.service_back_ll)
    LinearLayout serviceBackLl;
    @BindView(R.id.service_recycler)
    RecyclerView serviceRecycler;
    @BindView(R.id.banner_dever)
    View bannerDever;
    private String TAG = "ServiceItemActivity";
    private Context mContext;
    private LinearLayoutManager mLinearLayoutManager;
    private Gson mGson;
    private Intent mIntent;
    private ServiceItemAdapter mServiceItemAdapter;
    private String mServiceJson = "";//json数据从上一个界面传来
    private ArrayList<_CompanyDetail.ServiceBean> serviceBeanArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_item);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mGson = new Gson();
        mIntent = getIntent();
        mServiceJson = mIntent.getStringExtra("mServiceJson");
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        serviceRecycler.setLayoutManager(mLinearLayoutManager);
        //填充数据
        try {
            JSONArray jsonArray = new JSONArray(mServiceJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _CompanyDetail.ServiceBean serviceBean = mGson.fromJson(jsonArray.get(i).toString(), _CompanyDetail.ServiceBean.class);
                serviceBeanArrayList.add(serviceBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mServiceItemAdapter = new ServiceItemAdapter(mContext, serviceBeanArrayList);
        serviceRecycler.setAdapter(mServiceItemAdapter);
    }

    @OnClick(R.id.service_back_ll)
    public void onViewClickedInServiceItemActivity() {
        finish();
    }
}
