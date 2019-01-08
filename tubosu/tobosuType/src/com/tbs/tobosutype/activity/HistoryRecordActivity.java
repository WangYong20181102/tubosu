package com.tbs.tobosutype.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.HistoryRecordAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.base.HistoryRecordBean;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2019/1/2 13:57.
 */
public class HistoryRecordActivity extends BaseActivity {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.rv_history_record)
    RecyclerView rvHistoryRecord;
    @BindView(R.id.srl_history_record)
    SwipeRefreshLayout dqSwipe;
    @BindView(R.id.image_bottom_icon)
    ImageView imageBottomIcon;
    @BindView(R.id.rl_all_select)
    RelativeLayout rlAllSelect;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tv_all_select)
    TextView tvAllSelect;
    @BindView(R.id.image_no_data)
    ImageView imageNoData;
    private HistoryRecordAdapter adapter;   //历史记录适配器
    private LinearLayoutManager manager;    //recycleView样式
    private List<HistoryRecordBean> recordBeanList;
    private boolean editorStatus = false;//编辑状态
    private int index = 0;//删除选中个数
    private boolean isSelectAll = false;//true全选  false非全选
    /**
     * 删除操作储存要删除的数据
     */
    private List<String> userIdList;//用来存放选中计算结果id
    private Gson gson;
    private int mPage = 1;//用于分页的数据
    private int mPageSize = 10;//用于分页的数据
    private int type = 0;   //1地砖  2墙砖  3地板   4壁纸   5涂料   6窗帘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyrecord);
        ButterKnife.bind(this);
        initData();
        gson = new Gson();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取type值判断哪个界面跳转历史记录
        type = getIntent().getIntExtra("historyType", 0);
        userIdList = new ArrayList<>();
        recordBeanList = new ArrayList<>();
        //下拉刷新
        dqSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dqSwipe.setBackgroundColor(Color.WHITE);
        dqSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dqSwipe.setEnabled(false);//禁止下拉刷新
//        dqSwipe.setOnRefreshListener(onRefreshListener);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistoryRecord.setLayoutManager(manager);

        rvHistoryRecord.setOnTouchListener(onTouchListener);
        rvHistoryRecord.setOnScrollListener(onScrollListener);


        //网络请求
        httpRequestHistory();


    }

    /**
     * 开始网络请求
     */
    private void httpRequestHistory() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        if (TextUtils.isEmpty(AppInfoUtil.getUserid(this))) {
            params.put("uid", "0");
        } else {
            params.put("uid", AppInfoUtil.getUserid(mContext));
        }
        params.put("device_id", AppInfoUtil.getNewMac());
        params.put("type", type);
        params.put("page", mPage);
        params.put("pageSize", mPageSize);
        OKHttpUtil.post(Constant.MAPP_DECORATIONTOOL_COMPUTE_RECORD, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HistoryRecordBean historyRecordBean = gson.fromJson(jsonArray.get(i).toString(), HistoryRecordBean.class);
                            historyRecordBean.setCheck(false);
                            recordBeanList.add(historyRecordBean);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter == null) {
                                    adapter = new HistoryRecordAdapter(mContext, recordBeanList);
                                    adapter.setOnItenClickListener(onItenClickListener);
                                    adapter.setOnReNewClickListener(onReNewClickListener);
                                    rvHistoryRecord.setAdapter(adapter);
                                }
                                if (mPage != 1) {   //不等于1上拉加载
                                    adapter.notifyItemInserted(recordBeanList.size() - mPageSize);
                                }
                            }
                        });


                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (recordBeanList.size() == 0) {   //无历史记录
                                    imageNoData.setVisibility(View.VISIBLE);
                                    rvHistoryRecord.setVisibility(View.GONE);
                                    rlBottom.setVisibility(View.GONE);
                                    tvEdit.setVisibility(View.GONE);
                                    dqSwipe.setVisibility(View.GONE);
                                } else {
                                    ToastUtil.showShort(mContext, jsonObject.optString("msg"));
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(mContext, jsonObject.optString("msg"));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //    //下拉刷新
//    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//        @Override
//        public void onRefresh() {
//
//        }
//    };
    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (adapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && manager.findLastVisibleItemPosition() + 1 == adapter.getItemCount()) {
                    LoadMore();
                }
            }
        }
    };

    /**
     * 加载更多
     */
    private void LoadMore() {
        mPage++;
        httpRequestHistory();
    }

    //touch
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (dqSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };


    @OnClick({R.id.rlBack, R.id.tv_edit, R.id.rl_all_select, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
                finish();
                break;
            case R.id.tv_edit:  //编辑
                String text = tvEdit.getText().toString().trim();
                if (text.equals("编辑")) {
                    tvEdit.setText("完成");
                    rlBottom.setVisibility(View.VISIBLE);
                    adapter.isEditState(true);
                    editorStatus = true;
                } else {
                    tvEdit.setText("编辑");
                    rlBottom.setVisibility(View.GONE);
                    adapter.isEditState(false);
                    editorStatus = false;
                    isSelectAll = true;
                    selectAll();
                }
                break;
            case R.id.rl_all_select:    //全选
                selectAll();
                break;
            case R.id.btn_delete:   //删除
                deleteData();
                break;
        }
    }

    /**
     * 删除
     */
    private void deleteData() {
        if (index == 0) {
            ToastUtil.customizeToast1(this, "选择需要删除的历史记录");
            return;
        }


        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("id", userIdList.toString().replace("[", "").replace("]", "").trim());
        if (userIdList.size() == 1) {    //单条删除
            params.put("type", 1);
        } else { //多条删除
            params.put("type", 2);
        }
        OKHttpUtil.post(Constant.MAPP_DECORATIONTOOL_DEL_RECORD, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = adapter.getHistoryRecordList().size(); i > 0; i--) {
                                    HistoryRecordBean bean = adapter.getHistoryRecordList().get(i - 1);
                                    if (bean.isCheck()) {
                                        adapter.getHistoryRecordList().remove(bean);
                                    }
                                }
                                index = 0;
                                imageBottomIcon.setImageResource(R.drawable.edit_unselected);
                                adapter.notifyDataSetChanged();
                                if (isSelectAll) {
                                    imageNoData.setVisibility(View.VISIBLE);
                                    rvHistoryRecord.setVisibility(View.GONE);
                                    rlBottom.setVisibility(View.GONE);
                                    tvEdit.setVisibility(View.GONE);
                                    dqSwipe.setVisibility(View.GONE);
                                    //删除所有记录之后通知上一界面将recordId置为空，否则计算没有新数据插入
                                    EventBusUtil.sendEvent(new Event(EC.EventCode.DECORATION_TOOL_RECORDID));
                                }
                                ToastUtil.customizeToast1(mContext, "删除成功");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.customizeToast1(mContext, jsonObject.optString("msg"));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 全选取消全选
     */
    private void selectAll() {
        if (adapter == null) {
            return;
        }
        userIdList.clear();
        if (!isSelectAll) {
            for (int i = 0; i < adapter.getHistoryRecordList().size(); i++) {
                adapter.getHistoryRecordList().get(i).setCheck(true);
                userIdList.add(adapter.getHistoryRecordList().get(i).getId());
            }
            index = adapter.getHistoryRecordList().size();
            tvAllSelect.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0; i < adapter.getHistoryRecordList().size(); i++) {
                adapter.getHistoryRecordList().get(i).setCheck(false);
            }
            index = 0;
            tvAllSelect.setText("全选");
            isSelectAll = false;
            userIdList.clear();
        }
        if (index == 0) {
            imageBottomIcon.setImageResource(R.drawable.edit_unselected);
        } else {
            imageBottomIcon.setImageResource(R.drawable.edit_selected);
        }
        adapter.notifyDataSetChanged();
    }

    private HistoryRecordAdapter.OnItenClickListener onItenClickListener = new HistoryRecordAdapter.OnItenClickListener() {
        @Override
        public void onItemClickListener(int position, List<HistoryRecordBean> beanList) {
            if (editorStatus) {
                HistoryRecordBean bean = beanList.get(position);
                boolean isCheck = bean.isCheck();
                if (!isCheck) {
                    index++;
                    bean.setCheck(true);
                    if (index == beanList.size()) {
                        isSelectAll = true;
                        tvAllSelect.setText("取消全选");
                    }
                    userIdList.add(bean.getId());
                } else {
                    index--;
                    bean.setCheck(false);
                    isSelectAll = false;
                    tvAllSelect.setText("全选");

                    for (int i = userIdList.size() - 1; i >= 0; i--) {
                        String item = userIdList.get(i);
                        if (bean.getId().equals(item)) {
                            userIdList.remove(item);
                        }
                    }
                }
                if (index == 0) {
                    imageBottomIcon.setImageResource(R.drawable.edit_unselected);
                } else {
                    imageBottomIcon.setImageResource(R.drawable.edit_selected);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    private HistoryRecordAdapter.OnReNewClickListener onReNewClickListener = new HistoryRecordAdapter.OnReNewClickListener() {
        @Override
        public void onReNewClick(HistoryRecordBean recordBean) {
            EventBusUtil.sendEvent(new Event(EC.EventCode.DECORATION_TOOL, recordBean));
            finish();
        }
    };
}
