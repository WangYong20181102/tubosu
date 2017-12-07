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
import com.tbs.tobosutype.adapter.CoZiZhiAdapter;
import com.tbs.tobosutype.bean._CompanyDetail;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公司资质列表页 从公司主页进入 3.6版本新增
 * create by lin
 */

public class CoZizhiActivity extends AppCompatActivity {
    @BindView(R.id.co_zizhi_back_ll)
    LinearLayout coZizhiBackLl;
    @BindView(R.id.co_zizhi_recycler)
    RecyclerView coZizhiRecycler;
    @BindView(R.id.banner_dever)
    View bannerDever;
    private String TAG = "CoZizhiActivity";
    private Context mContext;
    private Gson mGson;
    private Intent mIntent;
    private String qualificationJson;
    private CoZiZhiAdapter mCoZiZhiAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<_CompanyDetail.QualificationBean> qualificationBeanArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_zizhi);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        mGson = new Gson();
        mIntent = getIntent();
        qualificationJson = mIntent.getStringExtra("qualificationJson");
        //解析数据 并填充数据
        try {
            JSONArray jsonArray = new JSONArray(qualificationJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _CompanyDetail.QualificationBean qualificationBean = mGson.fromJson(jsonArray.get(i).toString(), _CompanyDetail.QualificationBean.class);
                qualificationBeanArrayList.add(qualificationBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        coZizhiRecycler.setLayoutManager(mLinearLayoutManager);
        mCoZiZhiAdapter = new CoZiZhiAdapter(mContext, qualificationBeanArrayList);
        coZizhiRecycler.setAdapter(mCoZiZhiAdapter);
        mCoZiZhiAdapter.setOnZiZhiItemClickLister(onZiZhiItemClickLister);
    }

    //适配器点击事件
    private CoZiZhiAdapter.OnZiZhiItemClickLister onZiZhiItemClickLister = new CoZiZhiAdapter.OnZiZhiItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(mContext, ZizhiPhotoLookingActivity.class);
            intent.putExtra("qualificationJson", qualificationJson);
            intent.putExtra("position", position);
            intent.putExtra("positionDesc", "" + (position+1) + "/" + qualificationBeanArrayList.size());
            mContext.startActivity(intent);
        }
    };


    @OnClick(R.id.co_zizhi_back_ll)
    public void onViewClickedInCoZizhiActivity() {
        finish();
    }
}
