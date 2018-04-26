package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.CoChangeJiaZhuangAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._MyStore;
import com.tbs.tobosutype.utils.EventBusUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 装修公司修改家装范围
 */
public class ChangeCoJiaZhuangRange extends BaseActivity {

    @BindView(R.id.co_change_jiazhuang_dissmiss_rl)
    RelativeLayout coChangeJiazhuangDissmissRl;
    @BindView(R.id.co_change_jiazhuang_ok_rl)
    RelativeLayout coChangeJiazhuangOkRl;
    @BindView(R.id.co_change_jiazhuang_recycler)
    RecyclerView coChangeJiazhuangRecycler;
    private String TAG = "ChangeCoJiaZhuangRange";
    private Context mContext;
    private Intent mIntent;
    private Gson mGson;
    //上一个界面传来的json数据
    private String mListJson = "";
    //公装数据列表
    private ArrayList<_MyStore.HomeImprovementBean> mHomeImprovementBeansList = new ArrayList<>();
    //列表控制器
    private GridLayoutManager mGridLayoutManager;
    //适配器
    private CoChangeJiaZhuangAdapter mCoChangeJiaZhuangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_co_jia_zhuang_range);
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
        coChangeJiazhuangRecycler.setLayoutManager(mGridLayoutManager);
        mCoChangeJiaZhuangAdapter = new CoChangeJiaZhuangAdapter(mContext, mHomeImprovementBeansList);
        coChangeJiazhuangRecycler.setAdapter(mCoChangeJiaZhuangAdapter);
        mCoChangeJiaZhuangAdapter.setOnItemClickLister(onItemClickLister);
        mCoChangeJiaZhuangAdapter.notifyDataSetChanged();

    }

    //子项的点击事件
    private CoChangeJiaZhuangAdapter.OnItemClickLister onItemClickLister = new CoChangeJiaZhuangAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            //点击事件
            if (mHomeImprovementBeansList.get(position).getIs_selected() == 1) {
                //当前是选中状态改为未选中状态
                mHomeImprovementBeansList.get(position).setIs_selected(0);
            } else {
                mHomeImprovementBeansList.get(position).setIs_selected(1);
            }
            //刷新数据
            mCoChangeJiaZhuangAdapter.notifyDataSetChanged();
        }
    };

    //初始化列表
    private void initList(String mListJson) {
        try {
            JSONArray jsonArray = new JSONArray(mListJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _MyStore.HomeImprovementBean homeImprovementBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.HomeImprovementBean.class);
                mHomeImprovementBeansList.add(homeImprovementBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //点击保存
    private void okChangeMsg() {
        int SelectNum = 0;//选中的个数
        //遍历选项
        for (int i = 0; i < mHomeImprovementBeansList.size(); i++) {
            if (mHomeImprovementBeansList.get(i).getIs_selected() == 1) {
                //选中数量
                SelectNum = SelectNum + 1;
            }
        }
        Log.e(TAG, "选中的数量============" + SelectNum);
        if (SelectNum == 0) {
            Toast.makeText(mContext, "至少选择一项~", Toast.LENGTH_SHORT).show();
        } else {
            //通知页面修改数据
            String isChangeListData = mGson.toJson(mHomeImprovementBeansList);
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_JIAZHUANG_FANWEI, isChangeListData));
            finish();

        }
    }

    @OnClick({R.id.co_change_jiazhuang_dissmiss_rl, R.id.co_change_jiazhuang_ok_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_jiazhuang_dissmiss_rl:
                //点击取消
                finish();
                break;
            case R.id.co_change_jiazhuang_ok_rl:
                //保存所选择的项目
                okChangeMsg();
                break;
        }
    }
}
