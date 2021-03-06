package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.CoChangeGongZhuangAdapter;
import com.tbs.tbs_mj.adapter.CoChangeJiaZhuangAdapter;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._MyStore;
import com.tbs.tbs_mj.utils.EventBusUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 装修公司修改公装范围
 */

public class ChangeCoGongZhuangRangeActivity extends BaseActivity {


    @BindView(R.id.co_change_gongzhuang_dissmiss_rl)
    RelativeLayout coChangeGongzhuangDissmissRl;
    @BindView(R.id.co_change_gongzhuang_ok_rl)
    RelativeLayout coChangeGongzhuangOkRl;
    @BindView(R.id.co_change_gongzhuang_recycler)
    RecyclerView coChangeGongzhuangRecycler;

    private String TAG = "ChangeCoJiaZhuangRange";
    private Context mContext;
    private Intent mIntent;
    private Gson mGson;
    //上一个界面传来的json数据
    private String mListJson = "";
    //数据
    private ArrayList<_MyStore.ToolImprovementBean> mToolImprovementBeanList = new ArrayList<>();
    //列表控制器
    private GridLayoutManager mGridLayoutManager;
    //适配器
    private CoChangeGongZhuangAdapter mCoChangeGongZhuangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_co_gong_zhuang_range);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        //从上个界面传来的json
        mListJson = mIntent.getStringExtra("mListJson");
        //解析数据
        initList(mListJson);
        //设置列表的显示
        mGridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        //设置Recycler
        coChangeGongzhuangRecycler.setLayoutManager(mGridLayoutManager);
        mCoChangeGongZhuangAdapter = new CoChangeGongZhuangAdapter(mContext, mToolImprovementBeanList);
        coChangeGongzhuangRecycler.setAdapter(mCoChangeGongZhuangAdapter);
        mCoChangeGongZhuangAdapter.setOnItemClickLister(onItemClickLister);
        mCoChangeGongZhuangAdapter.notifyDataSetChanged();

    }

    //初始化列表
    private void initList(String mListJson) {
        try {
            JSONArray jsonArray = new JSONArray(mListJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _MyStore.ToolImprovementBean toolImprovementBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.ToolImprovementBean.class);
                mToolImprovementBeanList.add(toolImprovementBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //子项的点击事件
    private CoChangeGongZhuangAdapter.OnItemClickLister onItemClickLister = new CoChangeGongZhuangAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            //点击事件
            if (mToolImprovementBeanList.get(position).getIs_selected() == 1) {
                //当前是选中状态改为未选中状态
                mToolImprovementBeanList.get(position).setIs_selected(0);
            } else {
                mToolImprovementBeanList.get(position).setIs_selected(1);
            }
            //刷新数据
            mCoChangeGongZhuangAdapter.notifyDataSetChanged();
        }
    };


    //点击保存
    private void okChangeMsg() {
        int SelectNum = 0;//选中的个数
        //遍历选项
        for (int i = 0; i < mToolImprovementBeanList.size(); i++) {
            if (mToolImprovementBeanList.get(i).getIs_selected() == 1) {
                //选中数量
                SelectNum = SelectNum + 1;
            }
        }
        Log.e(TAG, "选中的数量============" + SelectNum);
        if (SelectNum == 0) {
            Toast.makeText(mContext, "至少选择一项~", Toast.LENGTH_SHORT).show();
        } else {
            //通知页面修改数据
            String isChangeListData = mGson.toJson(mToolImprovementBeanList);
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_GONGZHUANG_FANWEI, isChangeListData));
            finish();

        }
    }

    @OnClick({R.id.co_change_gongzhuang_dissmiss_rl, R.id.co_change_gongzhuang_ok_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_gongzhuang_dissmiss_rl:
                finish();
                break;
            case R.id.co_change_gongzhuang_ok_rl:
                okChangeMsg();
                break;
        }
    }
}
