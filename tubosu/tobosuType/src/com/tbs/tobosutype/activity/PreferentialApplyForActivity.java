package com.tbs.tobosutype.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.PreferentialApplyForAdapter;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.xlistview.XListView;
import com.tbs.tobosutype.xlistview.XListView.IXListViewListener;
import com.umeng.socialize.utils.Log;

/**
 * 优惠报名页面 (由装修公司用户 跳转至 该页面)
 *
 * @author dec
 */
public class PreferentialApplyForActivity extends Activity implements IXListViewListener {
    private static final String TAG = "PreferentialApplyForActivity";
    public static final String CHECK_PREFERENTIAL_APPLYFOR_NUM = "check_preferential_applyfor_num";

    private Context mContext;
    //banner
    private RelativeLayout rel_bar;
    /**
     * 返回
     */
    private ImageView iv_preferential_applyfor_back;

    /**
     * 订单提示信息
     */
    private TextView tv_tips;

    /**
     * 订单listview
     */
    private XListView preferentailOrderListView;

    /**
     * 正在加载数据时显示的布局
     */
    private LinearLayout ll_loading;

    /**
     * 无网络数据时显示没有数据的view
     */
    private ImageView no_data_view;

    /**
     * 接口
     */
    private String preferentialApplyForUrl = AllConstants.DECORATION_COMPANY_PREFERENTIAL_APPLYFOR;

    private RequestParams myParams;

    /**
     * 未被查看的数目
     */
    private int notCheckedNum = 0;


    /**
     * 优惠报名订单集合
     */
    private ArrayList<HashMap<String, Object>> preferentialDataList = new ArrayList<HashMap<String, Object>>();

    /*** 优惠报名订单适配器 */
    private PreferentialApplyForAdapter preferentialApplyForAdapter;

    private int page = 1;
    private int pageSize = page * 10;

    private CheckedBroadcastReceiver receiver;

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00001:
                    int num = notCheckedNum - msg.arg1;
                    if (num > 0) {
                        tv_tips.setText(Html.fromHtml("<font color='#ff1919'>" + num + "</font>" + "<font color='#1f1f1f'>个未查看报名</font>"));
                    } else if (num == 0) {
                        tv_tips.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_preferential_applyfor);
        mContext = PreferentialApplyForActivity.this;

        initView();
        initReceiver();
        initParams();
        requestPostPreferentailApplyFor();

    }


    /***
     * 初始化控件
     */
    private void initView() {
        rel_bar = (RelativeLayout) findViewById(R.id.rel_bar);
        iv_preferential_applyfor_back = (ImageView) findViewById(R.id.iv_preferential_applyfor_back);
        iv_preferential_applyfor_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_tips = (TextView) findViewById(R.id.tv_tips);

        preferentailOrderListView = (XListView) findViewById(R.id.preferential_list);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        no_data_view = (ImageView) findViewById(R.id.no_data_view);
        rel_bar.setBackgroundColor(Color.parseColor("#ff882e"));
        initLoadData();

    }

    private void initReceiver() {
        receiver = new CheckedBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CHECK_PREFERENTIAL_APPLYFOR_NUM);
        registerReceiver(receiver, filter);
    }

    private void initParams() {
        if (myParams == null) {
            myParams = AppInfoUtil.getPublicParams(getApplicationContext());
        }
        myParams.put("token", AppInfoUtil.getToekn(getApplicationContext()));
        myParams.put("page", page);
    }

    /***
     * 优惠报名订单接口请求
     */
    private void requestPostPreferentailApplyFor() {

        HttpServer.getInstance().requestPOST(preferentialApplyForUrl, myParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//						Log.d("result", new String(responseBody));
                ArrayList<HashMap<String, Object>> tempDataList = new ArrayList<HashMap<String, Object>>();
                ll_loading.setVisibility(View.GONE);
                String myData = new String(responseBody);
                Log.d(TAG, myData + "");

                // json解析
                tempDataList = jsonToDatas(myData);
//						initAdapter(preferentialDataList);

						/*--------------*/
                if (tempDataList != null) {
                    if (tempDataList.size() == 0) {
                        Toast.makeText(PreferentialApplyForActivity.this, "暂无活动报名", Toast.LENGTH_SHORT).show();
                    } else {
                        if (page == 1) {
                            preferentialDataList.clear();
                        }
                        for (int i = 0; i < tempDataList.size(); i++) {
                            preferentialDataList.add(tempDataList.get(i));
                        }
                    }
                    initAdapter(preferentialDataList);
                    onLoad();
                } else {
                    // 没有数据
                    no_data_view.setVisibility(View.VISIBLE);
                }
                        /*-------------------------------*/
            }

            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Log.d(TAG, arg0 + "");
            }

        });

        initLoadData();

    }

    /**
     * 初始化adapter
     */
    protected void initAdapter(final ArrayList<HashMap<String, Object>> data) {
        if (notCheckedNum > 0) {
            tv_tips.setText(Html.fromHtml("<font color='#ff1919'>" + notCheckedNum + "</font>" + "<font color='#1f1f1f'>个未查看报名</font>"));
        } else {
            tv_tips.setVisibility(View.GONE);
        }

        if (data.size() > 0) {
            preferentialApplyForAdapter = new PreferentialApplyForAdapter(mContext, data);
            preferentailOrderListView.setAdapter(preferentialApplyForAdapter);
            preferentialApplyForAdapter.notifyDataSetChanged();

            preferentailOrderListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it = new Intent();
                    it.putExtra("activityid", data.get(position - 1).get("activityid").toString());
                    it.setClass(PreferentialApplyForActivity.this, LocalDiscountDetailActivity.class);
                    startActivity(it);
                }
            });
            preferentialApplyForAdapter.notifyDataSetChanged();
        } else {
            // 没有数据
            no_data_view.setVisibility(View.VISIBLE);
            ll_loading.setVisibility(View.GONE);
        }
    }

    /***
     * 解析json数据
     */
    protected ArrayList<HashMap<String, Object>> jsonToDatas(String jsonData) {
        ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String, Object>>();

        try {
            JSONObject myObject = new JSONObject(jsonData);
            int error_code = myObject.getInt("error_code");
            // 需要判断是否有数据
            if (error_code == 0) {
                JSONArray jsonArray = myObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, Object> dataMap = new HashMap<String, Object>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    dataMap.put("signupid", jsonObject.getString("signupid"));
                    dataMap.put("activityid", jsonObject.getString("activityid"));
                    dataMap.put("signuptime", jsonObject.getString("signuptime"));
                    dataMap.put("mobile", jsonObject.getString("mobile"));
                    dataMap.put("issee", jsonObject.getString("issee"));
                    dataMap.put("cover", jsonObject.getString("cover"));
                    dataMap.put("mobileAddress", jsonObject.getString("mobileAddress"));
                    datalist.add(dataMap);

                    // 0是未查看  1是查看
                    if ("0".equals(jsonObject.getString("issee"))) {
                        notCheckedNum++;
                    }
                }
                if (preferentialDataList.size() % 10 != 0) {
                    preferentailOrderListView.mFooterView.setVisibility(View.GONE);
                } else {
                    preferentailOrderListView.mFooterView.setVisibility(View.VISIBLE);
                }
            }
            return datalist;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datalist;
    }


    private void initLoadData() {
        preferentailOrderListView.setPullLoadEnable(true);
        preferentailOrderListView.setXListViewListener(PreferentialApplyForActivity.this);
        preferentailOrderListView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去掉黄色
    }

    class CheckedBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (CHECK_PREFERENTIAL_APPLYFOR_NUM.equals(intent.getAction())) {
                Bundle b = intent.getBundleExtra("Checked_Bundle_Num");
                Message message = new Message();
                message.what = 0x00001;
                message.arg1 = b.getInt("CHECKED_NUM");
                myHandler.sendMessage(message);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }


    @Override
    public void onRefresh() {
        page = 1;
        initParams();
        requestPostPreferentailApplyFor();
    }

    @Override
    public void onLoadMore() {
        page++;
        if (!TextUtils.isEmpty(AppInfoUtil.getToekn(getApplicationContext()))) {
            initParams();
            requestPostPreferentailApplyFor();
        }
    }


    /***
     * 重新加载完毕之后UI变化
     */
    private void onLoad() {
        ll_loading.setVisibility(View.GONE);
        if (preferentialApplyForAdapter != null) {
            preferentialApplyForAdapter.notifyDataSetChanged();
            preferentailOrderListView.stopRefresh();
            preferentailOrderListView.stopLoadMore();
            preferentailOrderListView.setRefreshTime();
        }
    }

}
