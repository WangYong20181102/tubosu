package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.MyAttentionDesignerAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._MyAttentionDesigner;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.CustomWaitDialog;

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
 * 我关注的设计师列表页
 */
public class MyAttentionDesigner extends BaseActivity {

    @BindView(R.id.mad_back)
    RelativeLayout madBack;
    @BindView(R.id.mad_recyclelist)
    RecyclerView madRecyclelist;
    @BindView(R.id.mad_swip_refresh)
    SwipeRefreshLayout madSwipRefresh;

    private LinearLayoutManager mLinearLayoutManager;
    private Context mContext;
    private String TAG = "MyAttentionDesigner";
    private ArrayList<_MyAttentionDesigner> myAttentionDesignerArrayList = new ArrayList<>();//填充数据
    private ArrayList<_MyAttentionDesigner> tempMyAttenttionDesignerList = new ArrayList<>();//装箱数据
    private int mPage = 1;
    private MyAttentionDesignerAdapter attentionDesignerAdapter;
    private CustomWaitDialog customWaitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention_designer);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        //显示加载浮层
        customWaitDialog = new CustomWaitDialog(mContext);
        customWaitDialog.show();

        madSwipRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        madSwipRefresh.setBackgroundColor(Color.WHITE);
        madSwipRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        madSwipRefresh.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        madRecyclelist.setLayoutManager(mLinearLayoutManager);
        madRecyclelist.addOnScrollListener(onScrollListener);
        madRecyclelist.setOnTouchListener(onTouchListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据
        }
    };
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount() &&
                    !madSwipRefresh.isRefreshing()) {
                //加载更多
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
            if (madSwipRefresh.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    //网络请求数据
    private void HttpGetMyAttentionDesignerList(int mPage) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("page", mPage);
        param.put("token", Utils.getDateToken());
        HttpUtils.doPost(UrlConstans.GET_MY_ATTENTION_DESIGNER_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "请求成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //处理请求回来的数据将数据布局
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //加载更多
    private void loadMore() {
        mPage++;
        HttpGetMyAttentionDesignerList(mPage);
    }

    private void cleanLoadAction() {
        if (madSwipRefresh.isRefreshing()) {
            madSwipRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO 依照业务逻辑是否在这里重载数据
    }

    @OnClick({R.id.mad_back})
    public void onViewClickedInMyAttentionDesigner(View view) {
        switch (view.getId()) {
            case R.id.mad_back:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
