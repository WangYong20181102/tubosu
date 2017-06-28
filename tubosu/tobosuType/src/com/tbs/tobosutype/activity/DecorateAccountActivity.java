package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorateExpendAdapter;
import com.tbs.tobosutype.adapter.SwipeAdapter;
import com.tbs.tobosutype.bean._DecorationExpent;
import com.tbs.tobosutype.customview.MyChatView;
import com.tbs.tobosutype.customview.SwipeListView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lie on 2017/5/31.
 */

public class DecorateAccountActivity extends Activity {
    private Context mContext;
    private _DecorationExpent decorationExpent;//整个页面的数据
    private static final String TAG = DecorateAccountActivity.class.getSimpleName();
    private float decorateBudget = 0;
    /**
     * 总开支占比总预算在80%-0%之间    --->>   小主，一切还在预算当中
     * 总开支占比总预算在80%-100%      --->>   要死了，花钱如流水啊
     * 总开支占比总预算超过100%        --->>   就知道你是土豪，你任性就使劲花吧
     */
    private String[] budgetTips = {"小主，一切还在预算当中", "要死了，花钱如流水啊", "就知道你是土豪，你任性就使劲花吧"};

    private RelativeLayout relBack;
    private ImageView ivEditAccount;
    private TextView tvTotalBuduget;
    private SeekBar seekProgress;
    private TextView tvState;
    private TextView tvTotalCost;
    private TextView tvStartAccount;
    private RelativeLayout relDataEmpty;
    private LinearLayout relDataLaout;
    private RelativeLayout decorateAccBar;
    //进度条以下的控件 以及相关数据
    private MyChatView myChatView;//饼图
    private List<Float> floatList = new ArrayList<>();//饼图的占比
    private TextView da_rengong;//人工占比
    private TextView da_text_rengong;//人工价格
    private TextView da_jiancai;//建材占比
    private TextView da_text_jiancai;//建材价格
    private TextView da_wujin;//五金占比
    private TextView da_text_wujin;//五金价格
    private TextView da_jiaju;//家具占比
    private TextView da_text_jiaju;//家具价格
    private TextView da_chuwei;//厨卫占比
    private TextView da_text_chuwei;//厨卫价格
    private TextView da_qita;//其他占比
    private TextView da_text_qita;//其他价格
    private SwipeListView mListView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10086) {
                for (int i = 0; i < floatList.size(); i++) {
                    Log.e(TAG, "handler执行比例值====" + decorationExpent.getDecorateExpenseList().get(i).getType_name() + "===" + floatList.get(i));
                }
                myChatView.setFloatList(floatList);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Util.setActivityStatusColor(DecorateAccountActivity.this);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_decorate_account);
        mContext = DecorateAccountActivity.this;
        initData();
    }

    private void initData() {
//        Intent intent = new Intent(mContext, DecorateAccountActivity.class);
//        intent.putExtra("budget", etBudget.getText().toString());
        if (getIntent() != null && getIntent().getStringExtra("budget") != null) {
            String budget = getIntent().getStringExtra("budget");
            if ("".equals(budget)) {
                decorateBudget = 0;
            } else {
                decorateBudget = Float.parseFloat(budget);
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindView();//绑定布局
        setClick();
        HttpGetData();
    }

    private void HttpGetData() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, String> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        Log.e(TAG, "用户的uid=====" + Util.getUserId(mContext));
        param.put("uid", Util.getUserId(mContext));
        okHttpUtil.post(Constant.OUTCOME_HOMEPAGE_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "数据请求=====" + json);
                parseJson(json);
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    private void parseJson(String json) {
        try {
            final JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            decorationExpent = new _DecorationExpent(jsonObject.getString("data"));
                            initView(decorationExpent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else if (status.equals("201")) {
                //没有数据显示没有数据的浮层 并且隐藏显示数据的图层
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindView() {
        floatList.add(15.04f);//人工
        floatList.add(19.34f);//建材
        floatList.add(13.73f);//五金
        floatList.add(15.26f);//家具
        floatList.add(10.62f);//厨卫
        floatList.add(26.01f);//其他
        myChatView = (MyChatView) findViewById(R.id.da_my_chat_view);
        myChatView.setFloatList(floatList);

        relBack = (RelativeLayout) findViewById(R.id.decorate_account_back);
        ivEditAccount = (ImageView) findViewById(R.id.iv_edit_account);
        tvTotalBuduget = (TextView) findViewById(R.id.tv_total_buduget);
        tvTotalBuduget.setText(decorateBudget + "");
        seekProgress = (SeekBar) findViewById(R.id.seek_progress);
        tvTotalCost = (TextView) findViewById(R.id.tv_total_cost);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvStartAccount = (TextView) findViewById(R.id.tv_start_account);
        relDataEmpty = (RelativeLayout) findViewById(R.id.rel_data_empty);
        relDataLaout = (LinearLayout) findViewById(R.id.rel_data_layout);
        decorateAccBar = (RelativeLayout) findViewById(R.id.decorate_acc_bar);
        decorateAccBar.setBackgroundColor(Color.parseColor("#ff882e"));

        //绑定进度条控件以下的控件
        da_rengong = (TextView) findViewById(R.id.da_rengong);
        da_text_rengong = (TextView) findViewById(R.id.da_text_rengong);
        da_jiancai = (TextView) findViewById(R.id.da_jiancai);
        da_text_jiancai = (TextView) findViewById(R.id.da_text_jiancai);
        da_wujin = (TextView) findViewById(R.id.da_wujin);
        da_text_wujin = (TextView) findViewById(R.id.da_text_wujin);
        da_jiaju = (TextView) findViewById(R.id.da_jiaju);
        da_text_jiaju = (TextView) findViewById(R.id.da_text_jiaju);
        da_chuwei = (TextView) findViewById(R.id.da_chuwei);
        da_text_chuwei = (TextView) findViewById(R.id.da_text_chuwei);
        da_qita = (TextView) findViewById(R.id.da_qita);
        da_text_qita = (TextView) findViewById(R.id.da_text_qita);
        mListView = (SwipeListView) findViewById(R.id.record_listview);
        mListView.setSelector(R.color.transparent);
    }


    private void initView(_DecorationExpent decorationExpentList) {
        if (seekProgress.getProgress() >= 0 && seekProgress.getProgress() <= 20) {
            seekProgress.setProgress(20);
            seekProgress.setBackgroundResource(R.color.budget_green);
            tvState.setText(budgetTips[0]);
        } else if (seekProgress.getProgress() >= 20 && seekProgress.getProgress() < 40) {
            seekProgress.setProgress(37);
            seekProgress.setBackgroundResource(R.color.budget_blue);
            tvState.setText(budgetTips[0]);
        } else if (seekProgress.getProgress() >= 40 && seekProgress.getProgress() < 60) {
            seekProgress.setProgress(57);
            seekProgress.setBackgroundResource(R.color.budget_orange);
            tvState.setText(budgetTips[0]);
        } else if (seekProgress.getProgress() >= 60 && seekProgress.getProgress() < 80) {
            seekProgress.setProgress(77);
            seekProgress.setBackgroundResource(R.color.budget_yellow);
            tvState.setText(budgetTips[1]);
        } else {
            seekProgress.setProgress(90);
            seekProgress.setBackgroundResource(R.color.budget_red);
            tvState.setText(budgetTips[2]);
        }

        SwipeAdapter mAdapter = new SwipeAdapter(mContext, decorationExpentList.getDecorate_recordList(), mListView.getRightViewWidth());
        mListView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mListView);
        mAdapter.setOnRightItemClickListener(new SwipeAdapter.onRightItemClickListener() {

            @Override
            public void onRightItemClick(View v, int position) {

                Util.setToast(mContext, "删除第  " + (position + 1) + " 记录");

            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Util.setToast(mContext,"item onclick " + position);
//            }
//        });


        //处理相关页面数据
        //处理饼状图的周边显示
        if (!decorationExpent.getDecorateExpenseList().isEmpty()) {
            //人工
//            float rengong = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(4).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(4).getCount_cost().length() - 1));
//            floatList.add(rengong);
//            //建材
//            float jiancai = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(5).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(5).getCount_cost().length() - 1));
//            floatList.add(jiancai);
//            //五金
//            float wujin = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(0).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(0).getCount_cost().length() - 1));
//            floatList.add(wujin);
//            //家具
//            float jiaju = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(1).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(1).getCount_cost().length() - 1));
//            floatList.add(jiaju);
//            //厨卫
//            float chuwei = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(3).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(3).getCount_cost().length() - 1));
//            floatList.add(chuwei);
//            //其他
//            float qita = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(2).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(2).getCount_cost().length() - 1));
//            floatList.add(qita);
//
//            for (int i = 0; i < floatList.size(); i++) {
//                Log.e(TAG, "比例值====" + decorationExpent.getDecorateExpenseList().get(i).getType_name() + "===" + floatList.get(i));
//            }
            //线程处理
//            new MyThread().start();
//            myChatView.setFloatList(floatList);
//            for (int i = 0; i < myChatView.getFloatList().size(); i++) {
//                Log.e(TAG, "拿到的数据=====" + myChatView.getFloatList().get(i));
//            }
//            floatList.clear();

            /**
             * 显示饼图周边的数据
             */
            for (int i = 0; i < decorationExpent.getDecorateExpenseList().size(); i++) {
//                if (decorationExpent.getDecorateExpenseList().get(i).getCost().equals("0")) {
//                    if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("1")) {
//                        //将人工相关的描述隐藏
//                        da_rengong.setVisibility(View.GONE);
//                        da_text_rengong.setVisibility(View.GONE);
//                    }
//                    if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("2")) {
//                        //将建材相关的描述隐藏
//                        da_jiancai.setVisibility(View.GONE);
//                        da_text_jiancai.setVisibility(View.GONE);
//                    }
//                    if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("3")) {
//                        //将建材相关的描述隐藏
//                        da_wujin.setVisibility(View.GONE);
//                        da_text_wujin.setVisibility(View.GONE);
//                    }
//                    if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("4")) {
//                        //将建材相关的描述隐藏
//                        da_jiaju.setVisibility(View.GONE);
//                        da_text_jiaju.setVisibility(View.GONE);
//                    }
//                    if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("5")) {
//                        //将建材相关的描述隐藏
//                        da_chuwei.setVisibility(View.GONE);
//                        da_text_chuwei.setVisibility(View.GONE);
//                    }
//                    if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("6")) {
//                        //将建材相关的描述隐藏
//                        da_qita.setVisibility(View.GONE);
//                        da_text_qita.setVisibility(View.GONE);
//                    }
//                } else {
                //花费不为空
                if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("1")) {
                    //将人工相关的描述隐藏
                    da_rengong.setText("" + decorationExpent.getDecorateExpenseList().get(i).getType_name() + decorationExpent.getDecorateExpenseList().get(i).getCount_cost());
                    da_text_rengong.setText("¥ " + decorationExpent.getDecorateExpenseList().get(i).getCost());
                }
                if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("2")) {
                    //将建材相关的描述隐藏
                    da_jiancai.setText("" + decorationExpent.getDecorateExpenseList().get(i).getType_name() + decorationExpent.getDecorateExpenseList().get(i).getCount_cost());
                    da_text_jiancai.setText("¥ " + decorationExpent.getDecorateExpenseList().get(i).getCost());
                }
                if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("3")) {
                    //将建材相关的描述隐藏
                    da_wujin.setText("" + decorationExpent.getDecorateExpenseList().get(i).getType_name() + decorationExpent.getDecorateExpenseList().get(i).getCount_cost());
                    da_text_wujin.setText("¥ " + decorationExpent.getDecorateExpenseList().get(i).getCost());
                }
                if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("4")) {
                    //将建材相关的描述隐藏
                    da_jiaju.setText("" + decorationExpent.getDecorateExpenseList().get(i).getType_name() + decorationExpent.getDecorateExpenseList().get(i).getCount_cost());
                    da_text_jiaju.setText("¥ " + decorationExpent.getDecorateExpenseList().get(i).getCost());
                }
                if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("5")) {
                    //将建材相关的描述隐藏
                    da_chuwei.setText("" + decorationExpent.getDecorateExpenseList().get(i).getType_name() + decorationExpent.getDecorateExpenseList().get(i).getCount_cost());
                    da_text_chuwei.setText("¥ " + decorationExpent.getDecorateExpenseList().get(i).getCost());
                }
                if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("6")) {
                    //将建材相关的描述隐藏
                    da_qita.setText("" + decorationExpent.getDecorateExpenseList().get(i).getType_name() + decorationExpent.getDecorateExpenseList().get(i).getCount_cost());
                    da_text_qita.setText("¥ " + decorationExpent.getDecorateExpenseList().get(i).getCost());
                }
//                }
            }
        }
    }

    private void setClick() {
        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, EditAccountAcitivity.class), 0x00013);
            }
        });

        tvStartAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, EditAccountAcitivity.class), 0x00013);
            }
        });

        seekProgress.setBackgroundResource(R.color.budget_green);

//        seekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(10086);
        }
    }
}
