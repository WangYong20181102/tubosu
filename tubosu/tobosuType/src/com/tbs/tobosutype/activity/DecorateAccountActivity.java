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
    /**
     * 总开支占比总预算在80%-0%之间    --->>   小主，一切还在预算当中
     * 总开支占比总预算在80%-100%      --->>   余额不多了，要节省开支哦
     * 总开支占比总预算超过100%        --->>   就知道你是土豪，你任性就使劲花吧
     */
    private String[] budgetTips = {"小主，一切还在预算当中", "余额不多了，要节省开支哦", "就知道你是土豪，你任性就使劲花吧"};

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
    private RelativeLayout rl_chat_pie;
    //进度条以下的控件 以及相关数据
//    private MyChatView myChatView;//饼图
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
        bindView();//绑定布局
        setClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!floatList.isEmpty()) {
            floatList.clear();
        }
        HttpGetData();
    }

    private void HttpGetData() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, String> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        Log.e(TAG, "用户的uid=====" + AppInfoUtil.getUserid(mContext));
        param.put("uid", AppInfoUtil.getUserid(mContext));
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
            int status = jsonObject.getInt("status");
            if (status==200) {
                relDataEmpty.setVisibility(View.GONE);
                relDataLaout.setVisibility(View.VISIBLE);
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

            } else if (status==201) {
                relDataEmpty.setVisibility(View.VISIBLE);
                relDataLaout.setVisibility(View.GONE);
                tvTotalCost.setText("0");
                seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_green_style));
                tvState.setText(budgetTips[0]);
            }else if(status==202){
                JSONObject data = jsonObject.getJSONObject("data");
                tvTotalCost.setText(data.getString("autual_cost"));
                tvTotalBuduget.setText(data.getString("expected_cost"));
                tvState.setText(budgetTips[0]);
                relDataEmpty.setVisibility(View.VISIBLE);
                relDataLaout.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindView() {
//        floatList.add(15.04f);//人工
//        floatList.add(19.34f);//建材
//        floatList.add(13.73f);//五金
//        floatList.add(15.26f);//家具
//        floatList.add(10.62f);//厨卫
//        floatList.add(26.01f);//其他
//        myChatView = (MyChatView) findViewById(R.id.da_my_chat_view);
//        myChatView.setFloatList(floatList);

        sv = (ScrollView) findViewById(R.id.sv_decorate_acc);
        sv.smoothScrollTo(0, 0);
        rl_chat_pie = (RelativeLayout) findViewById(R.id.rl_chat_pie);
        relBack = (RelativeLayout) findViewById(R.id.decorate_account_back);
        ivEditAccount = (ImageView) findViewById(R.id.iv_edit_account);
        tvTotalBuduget = (TextView) findViewById(R.id.tv_total_buduget);
        seekProgress = (MySeekBar) findViewById(R.id.seek_progress);


        tvTotalCost = (TextView) findViewById(R.id.tv_total_cost);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvStartAccount = (TextView) findViewById(R.id.tv_start_account);
        relDataEmpty = (LinearLayout) findViewById(R.id.rel_data_empty);
        relDataLaout = (LinearLayout) findViewById(R.id.rel_data_layout);
        decorateAccBar = (RelativeLayout) findViewById(R.id.decorate_acc_bar);

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

        tvTotalCost.setText(decorationExpentList.getAll_cost());
        String budget = decorationExpentList.getExpected_cost();
        tvTotalBuduget.setText(budget);

        float decorateBudget = Float.parseFloat(budget);
        float totalCost = Float.parseFloat(decorationExpentList.getAll_cost());
        float f = totalCost / decorateBudget;

        if (f == 0) {
            seekProgress.setProgress(0);
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_whole_green_style));
            tvState.setText(budgetTips[0]);
        }else if (f > 0 && f <= 0.20) {
            // 红色 80以上 剩余原色
            seekProgress.setProgress(94);
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_green_style)); //seekbar_green_style
            tvState.setText(budgetTips[0]);
        } else if (f >= 0.20 && f < 0.40) {
            // 橙色 80 剩余原色
            seekProgress.setProgress(84);
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_blue_style)); //seekbar_blue_style
            tvState.setText(budgetTips[0]);
        } else if (f >= 0.40 && f < 0.60) {
            // 橙色 60 剩余原色
            seekProgress.setProgress(56);
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_orange_style)); //seekbar_orange_style
            tvState.setText(budgetTips[0]);
        } else if (f >= 0.60 && f < 0.80) {
            // 黄色 40 剩余原色
            seekProgress.setProgress(36);
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_yellow_style)); //seekbar_yellow_style
            tvState.setText(budgetTips[0]);
        } else if (f >= 0.80 && f < 1.00) {
            // 红色 20 剩余原色
            seekProgress.setProgress(13);
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_red_style));
            tvState.setText(budgetTips[1]);
        }else if(f == 1.00){
            seekProgress.setProgress(100);
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_red_style));
            tvState.setText(budgetTips[1]);
        } else if(f > 1.00){
            seekProgress.setProgress(100);
            seekProgress.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_nomralsd_style));
            tvState.setText(budgetTips[2]);
        }



        final ArrayList<_DecorationExpent.decorate_record> recordList = decorationExpentList.getDecorate_recordList();
        SwipeAdapter mAdapter = new SwipeAdapter(mContext, recordList, mListView.getRightViewWidth());

        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(mListView);
        mAdapter.setOnRightItemClickListener(new SwipeAdapter.onRightItemClickListener() {

            @Override
            public void onRightItemClick(View v, int position) {

                if (Util.isNetAvailable(mContext)) {
                    httpDeleteData(recordList.get(position).getId()); // 删除id
                }

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _DecorationExpent.decorate_record record = recordList.get(position);
                Intent intent = new Intent(mContext, EditAccountAcitivity.class);
                Bundle b = new Bundle();
                String[] textArr = new String[]{record.getExpend_name(),record.getCost(),record.getExpend_time(),record.getContent()};
                CacheManager.setStringArrayList(mContext, textArr);
                b.putInt("outcome_position", Integer.parseInt(record.getType_id()));
                b.putString("record_id", record.getId());
                intent.putExtra("check_record_bundle",b);
                startActivityForResult(intent,0x00013);
            }
        });


        //处理相关页面数据
        //处理饼状图的周边显示
        if (!decorationExpent.getDecorateExpenseList().isEmpty() && floatList.isEmpty()) {
            //人工
            float rengong = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(4).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(4).getCount_cost().length() - 1));
            floatList.add(rengong);
            //建材
            float jiancai = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(5).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(5).getCount_cost().length() - 1));
            floatList.add(jiancai);
            //五金
            float wujin = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(0).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(0).getCount_cost().length() - 1));
            floatList.add(wujin);
            //家具
            float jiaju = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(1).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(1).getCount_cost().length() - 1));
            floatList.add(jiaju);
            //厨卫
            float chuwei = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(3).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(3).getCount_cost().length() - 1));
            floatList.add(chuwei);
            //其他
            float qita = Float.parseFloat(decorationExpent.getDecorateExpenseList().get(2).getCount_cost().substring(0, decorationExpent.getDecorateExpenseList().get(2).getCount_cost().length() - 1));
            floatList.add(qita);

            MyChatView myChatView = new MyChatView(mContext);
            myChatView.setmRadius(240f);
            myChatView.setmStrokeWidth(100f);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            myChatView.setLayoutParams(layoutParams);
            myChatView.setFloatList(floatList);
            rl_chat_pie.addView(myChatView);
        }

        /**
         * 显示饼图周边的数据
         */
        for (int i = 0; i < decorationExpent.getDecorateExpenseList().size(); i++) {
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
        }
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
                    if (obj.getInt("status") == 200) {
                        Util.setToast(mContext, "删除开支记录成功");

                        HttpGetData();
                    } else if (obj.getInt("status") == 0) {
                        Util.setToast(mContext, obj.getString("msg"));
                    } else {
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
        switch (requestCode) {
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
                CacheManager.clearStringArrayList(mContext);
                startActivityForResult(new Intent(mContext, EditAccountAcitivity.class), 0x00013);
            }
        });

        tvStartAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheManager.clearStringArrayList(mContext);
                startActivityForResult(new Intent(mContext, EditAccountAcitivity.class), 0x00013);
            }
        });

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
}
