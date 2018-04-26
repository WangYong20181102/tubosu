package com.tbs.tobosutype.activity;

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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.CoChangeFengGeAdapter;
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

public class ChangeCoFengGeRangeActivity extends BaseActivity {
    @BindView(R.id.co_change_fengge_dissmiss_rl)
    RelativeLayout coChangeFenggeDissmissRl;
    @BindView(R.id.co_change_fengge_ok_rl)
    RelativeLayout coChangeFenggeOkRl;
    @BindView(R.id.co_change_fengge_recycler)
    RecyclerView coChangeFenggeRecycler;
    private String TAG = "ChangeCoJiaZhuangRange";
    private Context mContext;
    private Intent mIntent;
    private Gson mGson;
    //上一个界面传来的json数据
    private String mListJson = "";
    //公装数据列表
    private ArrayList<_MyStore.GoodAtStyleBean> mGoodAtStyleBeanArrayList = new ArrayList<>();
    //列表控制器
    private GridLayoutManager mGridLayoutManager;
    //适配器
    private CoChangeFengGeAdapter mCoChangeFengGeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_co_feng_ge_range);
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
        coChangeFenggeRecycler.setLayoutManager(mGridLayoutManager);
        mCoChangeFengGeAdapter = new CoChangeFengGeAdapter(mContext, mGoodAtStyleBeanArrayList);
        coChangeFenggeRecycler.setAdapter(mCoChangeFengGeAdapter);
        mCoChangeFengGeAdapter.setOnItemClickLister(onItemClickLister);
        mCoChangeFengGeAdapter.notifyDataSetChanged();

    }

    private CoChangeFengGeAdapter.OnItemClickLister onItemClickLister = new CoChangeFengGeAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            //点击事件
            if (mGoodAtStyleBeanArrayList.get(position).getIs_selected() == 1) {
                //当前是选中状态改为未选中状态
                mGoodAtStyleBeanArrayList.get(position).setIs_selected(0);
            } else {
                mGoodAtStyleBeanArrayList.get(position).setIs_selected(1);
            }
            //刷新数据
            mCoChangeFengGeAdapter.notifyDataSetChanged();
        }
    };

    //初始化列表
    private void initList(String mListJson) {
        try {
            JSONArray jsonArray = new JSONArray(mListJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _MyStore.GoodAtStyleBean goodAtStyleBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.GoodAtStyleBean.class);
                mGoodAtStyleBeanArrayList.add(goodAtStyleBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //点击保存
    private void okChangeMsg() {
        int SelectNum = 0;//选中的个数
        //遍历选项
        for (int i = 0; i < mGoodAtStyleBeanArrayList.size(); i++) {
            if (mGoodAtStyleBeanArrayList.get(i).getIs_selected() == 1) {
                //选中数量
                SelectNum = SelectNum + 1;
            }
        }
        Log.e(TAG, "选中的数量============" + SelectNum);
        if (SelectNum == 0) {
            Toast.makeText(mContext, "至少选择一项~", Toast.LENGTH_SHORT).show();
        } else {
            //通知页面修改数据
            String isChangeListData = mGson.toJson(mGoodAtStyleBeanArrayList);
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_FENGGE_FANWEI, isChangeListData));
            finish();

        }
    }

    @OnClick({R.id.co_change_fengge_dissmiss_rl, R.id.co_change_fengge_ok_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_fengge_dissmiss_rl:
                finish();
                break;
            case R.id.co_change_fengge_ok_rl:
                okChangeMsg();
                break;
        }
    }
}
