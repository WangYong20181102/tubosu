package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.adapter.CoChangeGongZhuangAdapter;
import com.tbs.tbsbusiness.adapter.CoChangeJiaZhuangAdapter;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._MyStore;
import com.tbs.tbsbusiness.util.EventBusUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *修改装修范围
 */
public class ChangeCoJiaZhuangRange extends BaseActivity {

    @BindView(R.id.co_change_jiazhuang_dissmiss_rl)
    RelativeLayout coChangeJiazhuangDissmissRl;
    @BindView(R.id.co_change_jiazhuang_ok_rl)
    RelativeLayout coChangeJiazhuangOkRl;
    @BindView(R.id.co_change_jiazhuang_recycler)
    RecyclerView coChangeJiazhuangRecycler;
    @BindView(R.id.co_change_gongzhuang_recycler)
    RecyclerView coChangeGongzhuangRecycler;


    private String TAG = "ChangeCoJiaZhuangRange";
    private Context mContext;
    private Intent mIntent;
    private Gson mGson;
    //上一个界面传来的json数据
    private String mListJson = "";
    //上一个界面传来的工装json数据
    private String mGongZListJson = "";
    //家装数据列表
    private ArrayList<_MyStore.HomeImprovementBean> mHomeImprovementBeansList = new ArrayList<>();
    //数据
    private ArrayList<_MyStore.ToolImprovementBean> mToolImprovementBeanList = new ArrayList<>();

    //列表控制器
    private GridLayoutManager mGridLayoutManager;
    private GridLayoutManager mGongZGridLayoutManager;
    //适配器
    private CoChangeJiaZhuangAdapter mCoChangeJiaZhuangAdapter;
    //适配器
    private CoChangeGongZhuangAdapter mCoChangeGongZhuangAdapter;


    /**
     * 装修类型
     *
     * @param savedInstanceState
     */

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
        mGongZListJson = mIntent.getStringExtra("mGongZListJson");
        //解析家装数据
        initList(mListJson);
        //解析公装数据
        initGongZList(mGongZListJson);
        //设置列表的显示
        mGridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        mGongZGridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        //设置Recycler
        coChangeJiazhuangRecycler.setLayoutManager(mGridLayoutManager);
        mCoChangeJiaZhuangAdapter = new CoChangeJiaZhuangAdapter(mContext, mHomeImprovementBeansList);
        coChangeJiazhuangRecycler.setAdapter(mCoChangeJiaZhuangAdapter);
        mCoChangeJiaZhuangAdapter.setOnItemClickLister(onItemClickLister);
        mCoChangeJiaZhuangAdapter.notifyDataSetChanged();
        //设置公装Recycler
        coChangeGongzhuangRecycler.setLayoutManager(mGongZGridLayoutManager);
        mCoChangeGongZhuangAdapter = new CoChangeGongZhuangAdapter(mContext, mToolImprovementBeanList);
        coChangeGongzhuangRecycler.setAdapter(mCoChangeGongZhuangAdapter);
        mCoChangeGongZhuangAdapter.setOnItemClickLister(onGongZItemClickLister);
        mCoChangeGongZhuangAdapter.notifyDataSetChanged();

    }

    //公装的子项点击事件
    private CoChangeGongZhuangAdapter.OnItemClickLister onGongZItemClickLister = new CoChangeGongZhuangAdapter.OnItemClickLister() {
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

    //初始化公装列表
    private void initGongZList(String mListJson) {
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

    @OnClick({R.id.co_change_jiazhuang_dissmiss_rl,
            R.id.co_change_jiazhuang_ok_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_jiazhuang_dissmiss_rl:
                //取消
                finish();
                break;
            case R.id.co_change_jiazhuang_ok_rl:
                //保存
                //保存所选择的项目
                okChangeMsg();
                break;
        }
    }

    //点击保存
    private void okChangeMsg() {
        int SelectNum = 0;//选中的个数
        //遍历家装选项
        for (int i = 0; i < mHomeImprovementBeansList.size(); i++) {
            if (mHomeImprovementBeansList.get(i).getIs_selected() == 1) {
                //选中数量
                SelectNum = SelectNum + 1;
            }
        }
        //遍历公装选项
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
            String isChangeListData = mGson.toJson(mHomeImprovementBeansList);
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_JIAZHUANG_FANWEI, isChangeListData));
            //通知页面修改公装数据
            String isChangeGongZListData = mGson.toJson(mToolImprovementBeanList);
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_GONGZHUANG_FANWEI, isChangeGongZListData));

            finish();

        }
    }
}
