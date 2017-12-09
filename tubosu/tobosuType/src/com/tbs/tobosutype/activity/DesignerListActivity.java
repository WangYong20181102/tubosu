package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DesignerListAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean._DesignerItem;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
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
 * 装修公司设计师列表页
 * 3.6版本新增 create by lin  20171201
 * 从装修公司首页点解更多进入
 */
public class DesignerListActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.designer_list_back_ll)
    LinearLayout designerListBackLl;
    @BindView(R.id.designer_list_case_recycler)
    RecyclerView designerListCaseRecycler;
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.designer_list_price_rl)
    RelativeLayout designerListPriceRl;

    private String TAG = "DesignerListActivity";
    private Context mContext;
    private LinearLayoutManager mLinearLayoutManager;
    private DesignerListAdapter mDesignerListAdapter;//设计师列表适配器
    private Intent mIntent;
    private Gson mGson;
    private int mPage = 1;
    private int mPageSize = 10;
    private String mCompanyId = "";
    private ArrayList<_DesignerItem> designerItemArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_list);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mCompanyId = mIntent.getStringExtra("mCompanyId");
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        designerListCaseRecycler.setLayoutManager(mLinearLayoutManager);
        designerListCaseRecycler.addOnScrollListener(onScrollListener);
        HttpGetList(mPage);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()) {
                //加载更多
                loadMore();
            }
        }
    };

    private void loadMore() {
        mPage++;
        HttpGetList(mPage);
    }

    @OnClick({R.id.designer_list_back_ll, R.id.designer_list_price_rl})
    public void onViewClickedInDesigerListActivity(View view) {
        switch (view.getId()) {
            case R.id.designer_list_back_ll:
                finish();
                break;
            case R.id.designer_list_price_rl:
                /// TODO: 2017/12/6  跳转到发单页
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", Constant.DEC_COOM_DESIGNER_GET_PRICE);
                mContext.startActivity(intent);
                break;
        }
    }

    private void HttpGetList(int mPage) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("comid", mCompanyId);
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.DESIGNER_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败======" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //数据获取成功
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //填充数据
                            _DesignerItem designerItem = mGson.fromJson(jsonArray.get(i).toString(), _DesignerItem.class);
                            designerItemArrayList.add(designerItem);
                        }
                        //布局
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //设置适配器
                                if (mDesignerListAdapter == null) {
                                    mDesignerListAdapter = new DesignerListAdapter(mContext, designerItemArrayList);
                                    designerListCaseRecycler.setAdapter(mDesignerListAdapter);
                                    mDesignerListAdapter.notifyDataSetChanged();
                                    mDesignerListAdapter.setOnDesignerItemClickLister(onDesignerItemClickLister);
                                } else {
                                    mDesignerListAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有更多数据。", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private DesignerListAdapter.OnDesignerItemClickLister onDesignerItemClickLister = new DesignerListAdapter.OnDesignerItemClickLister() {
        @Override
        public void onItemClickLister(View view, int position) {
            switch (view.getId()) {
                case R.id.item_designer_icon_ll:
                    /// TODO: 2017/12/6  跳转到设计师主页
                    Intent it = new Intent(mContext, SheJiShiActivity.class);
                    it.putExtra("designer_id",designerItemArrayList.get(position).getDesid());
                    startActivity(it);
                    break;
                case R.id.item_designer_find_he_design:
                    /// TODO: 2017/12/6  跳转到发单接口
                    Intent intent = new Intent(mContext, NewWebViewActivity.class);
                    intent.putExtra("mLoadingUrl", Constant.DEC_COOM_DESIGNER_FIND_DISIGN);
                    mContext.startActivity(intent);
                    break;
            }
        }
    };
}
