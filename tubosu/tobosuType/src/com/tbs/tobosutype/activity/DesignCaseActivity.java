package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DesignCaseAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._CompanySuiteList;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONArray;
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
 * 设计方案列表页
 * create by lin
 * 从装修公司主页 更多设计方案选项跳转进来 3.6版本新增
 */
public class DesignCaseActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.design_case_back_ll)
    LinearLayout designCaseBackLl;
    @BindView(R.id.design_case_recycler)
    RecyclerView designCaseRecycler;
    @BindView(R.id.design_case_find_price_rl)
    RelativeLayout designCaseFindPriceRl;


    private String TAG = "DesignCaseActivity";
    private Context mContext;
    private Intent mIntent;
    private String mCompanyId = "";//公司的Id 从上一个界面传来
    private boolean isLoading = false;
    private Gson mGson;
    private int mPage = 1;//加载页面
    private int mPageSize = 10;//加载的数据差长度
    private LinearLayoutManager mLinearLayoutManager;//纵向列表管理者
    private DesignCaseAdapter mDesignCaseAdapter;//案例列表适配器
    private ArrayList<_CompanySuiteList> companySuiteListArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_case);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            //修改模型的状态
            case EC.EventCode.NOTIF_DESIGN_CASE_ACTIVITY_MODE_IS_COLLECT:
                //已收藏
                companySuiteListArrayList.get((int) event.getData()).setIs_collect("1");
                break;
            case EC.EventCode.NOTIF_DESIGN_CASE_ACTIVITY_MODE_IS_NOT_COLLECT:
                //未收藏
                companySuiteListArrayList.get((int) event.getData()).setIs_collect("0");
                break;
        }
    }

    private void initViewEvent() {
        mGson = new Gson();
        mIntent = getIntent();
        mCompanyId = mIntent.getStringExtra("mCompanyId");
        SpUtil.setStatisticsEventPageId(mContext,mCompanyId);
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        designCaseRecycler.setLayoutManager(mLinearLayoutManager);
        designCaseRecycler.addOnScrollListener(onScrollListener);
        HttpGetDesignCaseList(mPage);
    }

    //列表上拉加载更多事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        //加载数据
        HttpGetDesignCaseList(mPage);
    }

    //网络请求数据
    private void HttpGetDesignCaseList(int mPage) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", mCompanyId);
        if (!TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
            param.put("uid", AppInfoUtil.getUuid(mContext));
            param.put("user_type", AppInfoUtil.getTypeid(mContext));
        }
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.COMPANY_SUITE_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _CompanySuiteList companySuiteList = mGson.fromJson(jsonArray.get(i).toString(), _CompanySuiteList.class);
                            companySuiteListArrayList.add(companySuiteList);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mDesignCaseAdapter == null) {
                                    mDesignCaseAdapter = new DesignCaseAdapter(mContext, companySuiteListArrayList);
                                    designCaseRecycler.setAdapter(mDesignCaseAdapter);
                                    mDesignCaseAdapter.notifyDataSetChanged();
                                    mDesignCaseAdapter.setOnItemClickLister(onItemClickLister);
                                } else {
                                    mDesignCaseAdapter.notifyItemInserted(companySuiteListArrayList.size() - mPageSize);
                                }
                            }
                        });
                    } else {
                        //没有更多的数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private DesignCaseAdapter.OnItemClickLister onItemClickLister = new DesignCaseAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            switch (view.getId()) {
                case R.id.item_design_case_image_rl:
                    //点击了整个图片层 进入图片查看器
                    String DImageJson = mGson.toJson(companySuiteListArrayList);
                    SpUtil.setDoubleImageListJson(mContext, DImageJson);
                    Intent intent = new Intent(mContext, DImageLookingActivity.class);
                    intent.putExtra("mPosition", position);
                    intent.putExtra("mWhereFrom", "DesignCaseActivity");
                    mContext.startActivity(intent);
                    break;
                case R.id.item_design_case_get_design:
                    /// TODO: 2017/12/5  点击了发单页 跳转到发单页面
                    Intent intent2 = new Intent(mContext, NewWebViewActivity.class);
                    intent2.putExtra("mLoadingUrl", Constant.DEC_COOM_GET_THIS_DISGIN);
                    mContext.startActivity(intent2);
                    break;
            }
        }
    };

    @OnClick({R.id.design_case_back_ll, R.id.design_case_find_price_rl})
    public void onViewClickedInDesignCaseActivity(View view) {
        switch (view.getId()) {
            case R.id.design_case_back_ll:
                finish();
                break;
            case R.id.design_case_find_price_rl:
                // 发单页
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", Constant.DEC_COOM_DESIGN_CASE_BUTTON);
                mContext.startActivity(intent);
                break;
        }
    }
}
