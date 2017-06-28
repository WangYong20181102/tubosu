package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorateExpendAdapter;
import com.tbs.tobosutype.adapter.SwipeAdapter;
import com.tbs.tobosutype.bean._DecorationExpent;
import com.tbs.tobosutype.customview.MyChatView;
import com.tbs.tobosutype.customview.MySeekBar;
import com.tbs.tobosutype.customview.SwipeListView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
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

    private ScrollView sv;
    private RelativeLayout relBack;
    private ImageView ivEditAccount;
    private TextView tvTotalBuduget;
    private MySeekBar seekProgress;
    private TextView tvState;
    private TextView tvTotalCost;
    private TextView tvStartAccount;
    private LinearLayout relDataEmpty;
    private LinearLayout relDataLaout;
    private RelativeLayout decorateAccBar;
    //进度条以下的控件 以及相关数据
    private MyChatView myChatView;//饼图
    private List<Float> floatList;//饼图的占比
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
    private LinearLayoutManager mLinearLayoutManager;//列表布局
    private DecorateExpendAdapter decorateExpendAdapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Util.setActivityStatusColor(DecorateAccountActivity.this);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_decorate_account);
        mContext = DecorateAccountActivity.this;
        initData();
        bindView();//绑定布局
        setClick();
    }

    private void initData(){
        if(getIntent()!=null && getIntent().getStringExtra("budget")!=null){
            String budget = getIntent().getStringExtra("budget");
            if("".equals(budget)){
                decorateBudget = 0;
            }else {
                decorateBudget = Float.parseFloat(budget) * 10000;
            }
        }else {
            decorateBudget = CacheManager.getDecorateBudget(mContext) * 10000;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
                relDataEmpty.setVisibility(View.GONE);
                relDataLaout.setVisibility(View.VISIBLE);
                decorationExpent = new _DecorationExpent(jsonObject.getString("data"));
                initView(decorationExpent);
            } else/* if (status.equals("201"))*/ {
                //没有数据显示没有数据的浮层 并且隐藏显示数据的图层
                relDataEmpty.setVisibility(View.VISIBLE);
                relDataLaout.setVisibility(View.GONE);

                tvTotalCost.setText("0");
                seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_green_style));
                tvState.setText(budgetTips[0]);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindView() {
        sv = (ScrollView) findViewById(R.id.sv_decorate_acc);
        relBack = (RelativeLayout) findViewById(R.id.decorate_account_back);
        ivEditAccount = (ImageView) findViewById(R.id.iv_edit_account);
        tvTotalBuduget = (TextView) findViewById(R.id.tv_total_buduget);
        tvTotalBuduget.setText(decorateBudget+"");
        seekProgress = (MySeekBar) findViewById(R.id.seek_progress);
        tvTotalCost = (TextView) findViewById(R.id.tv_total_cost);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvStartAccount = (TextView) findViewById(R.id.tv_start_account);
        relDataEmpty = (LinearLayout) findViewById(R.id.rel_data_empty);
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
        sv.smoothScrollTo(0,0);
        tvTotalCost.setText(decorationExpentList.getAll_cost());
        if(decorateBudget==0){
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_red_style));
            tvState.setText("你的预算竟然是0");
        }else {
            float totalCost = Float.parseFloat(decorationExpentList.getAll_cost());
            float f = totalCost / decorateBudget;
//            DecimalFormat fnum = new DecimalFormat("##0.00");
//            int fs = (int) Float.parseFloat(fnum.format(f));

//            Util.setToast(mContext, "" + f);
            if (f >= 0 && f <= 0.20) {
                seekProgress.setProgress(20);
                seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_green_style));
                tvState.setText(budgetTips[0]);
            } else if (f >= 0.20 && f < 0.40) {
                seekProgress.setProgress(37);
                seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_blue_style));
                tvState.setText(budgetTips[0]);
            } else if (f >= 0.40 && f < 0.60) {
                seekProgress.setProgress(57);
                seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_orange_style));
                tvState.setText(budgetTips[0]);
            } else if (f >= 0.60 && f < 0.80) {
                seekProgress.setProgress(77);
                seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_yellow_style));
                tvState.setText(budgetTips[0]);
            } else if (f >= 0.80 && f < 1.00){
                seekProgress.setProgress(96);
                seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_red_style));
                tvState.setText(budgetTips[1]);
            }else {
                seekProgress.setProgress(100);
                seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_red_style));
                tvState.setText(budgetTips[2]);
            }
        }



        final ArrayList<_DecorationExpent.decorate_record> recordList = decorationExpentList.getDecorate_recordList();
        SwipeAdapter mAdapter = new SwipeAdapter(mContext, recordList, mListView.getRightViewWidth());
        mListView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mListView);
        mAdapter.setOnRightItemClickListener(new SwipeAdapter.onRightItemClickListener() {

            @Override
            public void onRightItemClick(View v, int position) {
//                recordList.get(position).getExpend_name();
                if(Util.isNetAvailable(mContext)){
                    httpDeleteData(recordList.get(position).getId());
                }

            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Util.setToast(mContext,"item onclick " + position);
//            }
//        });


        //处理相关页面数据
        //处理饼状图的显示
//        if (!decorationExpent.getDecorateExpenseList().isEmpty()) {
//            for (int i = 0; i < decorationExpent.getDecorateExpenseList().size(); i++) {
//                if (decorationExpent.getDecorateExpenseList().get(i).getType_id().equals("1")) {
//                    da_rengong.setText("" + decorationExpent.getDecorateExpenseList().get(i).getType_name() +
//                            decorationExpent.getDecorateExpenseList().get(i).getCount_cost());
//                }
//            }
//        }
    }

    private void httpDeleteData(String deleteId) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, String> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", deleteId);
        okHttpUtil.post(Constant.DELETE_DECORATE_RECORD, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "数据请求=====" + json);
                try {
                    JSONObject obj = new JSONObject(json);
                    if(obj.getInt("status") == 200){
                        Util.setToast(mContext, "删除开支记录成功");
                        HttpGetData();
                    }else if(obj.getInt("status") == 0) {
                        Util.setToast(mContext, obj.getString("msg"));
                    }else {
                        Util.setToast(mContext, "删除开支记录失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0x00013:
                HttpGetData();
                break;
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
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
