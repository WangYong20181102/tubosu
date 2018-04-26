package com.tbs.tobosutype.activity;

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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.CoChangeServiceAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._MyStore;
import com.tbs.tobosutype.utils.EventBusUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 装修公司网店修改 服务区域
 */
public class ChangeCoServiceActivity extends BaseActivity {

    @BindView(R.id.co_change_fuquyu_dissmiss_rl)
    RelativeLayout coChangeFuquyuDissmissRl;
    @BindView(R.id.co_change_fuquyu_ok_rl)
    RelativeLayout coChangeFuquyuOkRl;
    @BindView(R.id.co_change_fuquyu_recycler)
    RecyclerView coChangeFuquyuRecycler;
    private Context mContext;
    private String TAG = "ChangeCoServiceActivity";
    private Gson mGson;
    private Intent mIntent;
    private String mFuwuquyuJson = "";
    private ArrayList<_MyStore.ServiceAreaBean> mServiceAreaBeanList = new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    private CoChangeServiceAdapter mCoChangeServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_co_service);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    private void initEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mIntent = getIntent();
        //获取上个页面传来的数据
        mFuwuquyuJson = mIntent.getStringExtra("mFuwuquyuJson");
        initList(mFuwuquyuJson);
        //设置列表的显示
        mGridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        //设置Recycler
        coChangeFuquyuRecycler.setLayoutManager(mGridLayoutManager);
        mCoChangeServiceAdapter = new CoChangeServiceAdapter(mContext, mServiceAreaBeanList);
        coChangeFuquyuRecycler.setAdapter(mCoChangeServiceAdapter);
        mCoChangeServiceAdapter.setOnItemClickLister(onItemClickLister);
        mCoChangeServiceAdapter.notifyDataSetChanged();

    }

    private CoChangeServiceAdapter.OnItemClickLister onItemClickLister = new CoChangeServiceAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            //点击事件
            if (mServiceAreaBeanList.get(position).getIs_selected() == 1) {
                //当前是选中状态改为未选中状态
                mServiceAreaBeanList.get(position).setIs_selected(0);
            } else {
                mServiceAreaBeanList.get(position).setIs_selected(1);
            }
            //刷新数据
            mCoChangeServiceAdapter.notifyDataSetChanged();
        }
    };

    private void initList(String mListJson) {
        try {
            JSONArray jsonArray = new JSONArray(mListJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _MyStore.ServiceAreaBean serviceAreaBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.ServiceAreaBean.class);
                mServiceAreaBeanList.add(serviceAreaBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //点击保存
    private void okChangeMsg() {
        int SelectNum = 0;//选中的个数
        //遍历选项
        for (int i = 0; i < mServiceAreaBeanList.size(); i++) {
            if (mServiceAreaBeanList.get(i).getIs_selected() == 1) {
                //选中数量
                SelectNum = SelectNum + 1;
            }
        }
        Log.e(TAG, "选中的数量============" + SelectNum);
        if (SelectNum == 0) {
            Toast.makeText(mContext, "至少选择一项~", Toast.LENGTH_SHORT).show();
        } else {
            //通知页面修改数据
//            String isChangeListData = mGson.toJson(mServiceAreaBeanList);
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_SERVICE_MSG, mServiceAreaBeanList));
//            Toast.makeText(mContext, "已保存", Toast.LENGTH_SHORT).show();
            finish();

        }
    }

    @OnClick({R.id.co_change_fuquyu_dissmiss_rl, R.id.co_change_fuquyu_ok_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_fuquyu_dissmiss_rl:
                finish();
                break;
            case R.id.co_change_fuquyu_ok_rl:
                //保存信息
                okChangeMsg();
                break;
        }
    }
}
