package com.tobosu.mydecorate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.GuessYouLikeAdapter;
import com.tobosu.mydecorate.adapter.SearchAdapter;
import com.tobosu.mydecorate.adapter.SearchHistoryAdapter;
import com.tobosu.mydecorate.database.HistoryManager;
import com.tobosu.mydecorate.entity.BibleEntity;
import com.tobosu.mydecorate.entity.GuessYouLike;
import com.tobosu.mydecorate.entity.SearchHistoryEntity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Lie on 2017/6/2.
 */

public class SearchActivity extends com.tobosu.mydecorate.activity.BaseActivity {
    private TextView tvOperate;
    private EditText etSearch;
    private RelativeLayout relDeleteWord;

    private String opert;
    private String decorateKey;

    private RelativeLayout relDeleteHistory;
    private ListView lvHistory;
    private RelativeLayout relHistory;

    private ImageView ivSee;
    private RelativeLayout relHideYoulike;
    private GridView gvYoulike;

    private LinearLayout nonSearchLayout;
    private RecyclerView rvSearchResult;
    //列表的样式
    private LinearLayoutManager mLinearLayoutManager;
    private RelativeLayout search_include_netout_layout;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**临时装箱*/
    private ArrayList<BibleEntity> tempSearchDataList = new ArrayList<BibleEntity>();

    private ArrayList<BibleEntity> searchDataList = new ArrayList<BibleEntity>();

    private SearchAdapter searchAdapter;

    private HistoryManager manager;

    /**
     * 搜索历史记录
     */
    private ArrayList<String> historyList = new ArrayList<String>();

    /**
     * 猜你喜欢
     */
    private ArrayList<String> youlikeList = new ArrayList<String>();

    private ImageView ivNoData;
    private TextView tvGetNet;

    private boolean isShow = true;

    private int page = 1;

    private GuessYouLikeAdapter youlikeAdapter;
    private SearchHistoryAdapter historyAdapter;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // 猜你喜欢
                    youlikeAdapter = new GuessYouLikeAdapter(mContext, youlikeList);
                    gvYoulike.setAdapter(youlikeAdapter);
                    youlikeAdapter.notifyDataSetChanged();
                    break;
                case 200:
                    Bundle b = msg.getData();
                    String key = b.getString("key");
                    hideLoadingView();
                    if (searchDataList.size() > 0) {
                        initSearchAdapter(searchDataList, key);
                    } else {
                        ivNoData.setVisibility(View.VISIBLE);
                        hideLoadingView();
                    }
                    break;
                case 201:
                    Util.setToast(mContext, "无更多数据了");
                    hideLoadingView();
                    if(searchAdapter!=null){
                        searchAdapter.hideFootView();
                    }
                    break;
            }
        }
    };


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_search);
        TAG = SearchActivity.class.getSimpleName();
        mContext = SearchActivity.this;
    }

    @Override
    protected void initView() {
        tvOperate = (TextView) findViewById(R.id.tv_operate);
        etSearch = (EditText) findViewById(R.id.et_search);
        relDeleteWord = (RelativeLayout) findViewById(R.id.rel_delete_keyword);

        relDeleteHistory = (RelativeLayout) findViewById(R.id.rel_delete_history);
        lvHistory = (ListView) findViewById(R.id.lv_history);
        relHistory = (RelativeLayout) findViewById(R.id.rel_history);

        ivSee = (ImageView) findViewById(R.id.iv_see);
        relHideYoulike = (RelativeLayout) findViewById(R.id.rel_hide_youlike);
        gvYoulike = (GridView) findViewById(R.id.gv_youlike);

        nonSearchLayout = (LinearLayout) findViewById(R.id.non_search_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_search_container);
        swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(swipeLister);

        rvSearchResult = (RecyclerView) findViewById(R.id.recyclerview_search_result);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearchResult.setLayoutManager(mLinearLayoutManager);

        rvSearchResult.setOnScrollListener(onScrollListener);
        rvSearchResult.setOnTouchListener(onTouchListener);


        ivNoData = (ImageView) findViewById(R.id.iv_search_empty_data);
        tvGetNet = (TextView) findViewById(R.id.tv_search_getnet);
        search_include_netout_layout = (RelativeLayout) findViewById(R.id.search_include_netout_layout);

    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (swipeRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //显示列表的滑动监听事件 上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
//            Log.e(TAG, "最后可见目标===" + lastVisiableItem + "集合总数===" + mLinearLayoutManager.getItemCount() + "==newState==" + newState + "==刷新状态==" + swipeRefreshLayout.isRefreshing());
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !swipeRefreshLayout.isRefreshing()) {
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };


    private void loadMore(){
        page++;
        swipeRefreshLayout.setRefreshing(false);
        goSearch(decorateKey);
        System.out.println("-----**-onScrolled load more completed------");
    }

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            searchDataList.clear();
            page = 1;
            goSearch(decorateKey);
        }
    };


    private void initHistory(){
        if (manager == null) {
            manager = HistoryManager.getInstance(mContext);
        }
        ArrayList<SearchHistoryEntity> historyEntityList = manager.queryHistory();
        int size = historyEntityList.size();
        if (size > 0) {
            relHistory.setVisibility(View.VISIBLE);
            for (int i = 0; i < size; i++) {
                historyList.add(historyEntityList.get(i).getHistoryText());
            }
            historyAdapter = new SearchHistoryAdapter(mContext, historyList);
            lvHistory.setAdapter(historyAdapter);
            historyAdapter.notifyDataSetChanged();
        } else {
            relHistory.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initNetData() {
        initActivityData();
    }

    private void initActivityData(){
        // 是否有历史记录
        initHistory();
        gvYoulike.setVisibility(View.VISIBLE);
        ivSee.setBackgroundResource(R.mipmap.youlik_show);
        getGuessYouLike();
    }

    private void getGuessYouLike() {
        if (Util.isNetAvailable(mContext)) {
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("token", Util.getDateToken());
            okHttpUtil.post(Constant.GUESS_YOULIKE_URL, param, new OKHttpUtil.BaseCallBack() {
                @Override
                public void onSuccess(Response response, String json) {
                    youlikeList.clear();
                    Util.setLog(TAG, json);
                    Gson gson = new Gson();
                    GuessYouLike jsonBean = gson.fromJson(json, GuessYouLike.class);
                    if (jsonBean.getStatus() == 200) {
                        int size = jsonBean.getData().size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                youlikeList.add(jsonBean.getData().get(i).getKey_word());
                            }
                            handler.sendEmptyMessage(0);
                        }
                    }
                }

                @Override
                public void onFail(Request request, IOException e) {
                    Util.setErrorLog(TAG, e.getMessage());
                }

                @Override
                public void onError(Response response, int code) {
                    Util.setErrorLog(TAG, response.toString());
                }
            });
        } else {

        }

    }


    private void goSearch(final String keyword) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("token", Util.getDateToken());
        param.put("title", keyword);
        param.put("page", page);
        param.put("page_size", 10);
        okHttpUtil.post(Constant.SEARCH_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                swipeRefreshLayout.setRefreshing(false);
                parse(json, keyword);
            }

            @Override
            public void onFail(Request request, IOException e) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Response response, int code) {
                swipeRefreshLayout.setRefreshing(false);
                Util.setErrorLog(TAG, response.toString());
            }
        });
    }

    private CustomWaitDialog waitDialog;

    private void showLoadingView() {
        waitDialog = new CustomWaitDialog(mContext);
        waitDialog.show();
    }

    private void hideLoadingView() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }

    private void parse(String json, String key) {
        Util.setErrorLog(TAG, json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getInt("status") == 200) {
                JSONArray array = jsonObject.getJSONArray("data");
                Util.setErrorLog(TAG, array.toString());
                for (int i = 0; i < array.length(); i++) {
                    searchDataList.add(new BibleEntity(array.get(i).toString()));
                }
                Message message = new Message();
                message.what = 200;
                Bundle b = new Bundle();
                b.putString("key", key);
                message.setData(b);
                handler.sendMessage(message);
            } else if (jsonObject.getInt("status") == 201) {
                handler.sendEmptyMessage(201);
                System.out.println("------error_code==201--------->>>---");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initSearchAdapter(ArrayList<BibleEntity> resultDataList, String searchKeyword) {
        Log.e(TAG, "SearchActivity获取的集合长度======" + resultDataList.size());
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        rvSearchResult.setVisibility(View.VISIBLE);
        if (resultDataList != null && resultDataList.size() > 0) {

            if (searchAdapter == null) {
                searchAdapter = new SearchAdapter(mContext, resultDataList);
                rvSearchResult.setAdapter(searchAdapter);
            } else {
                searchAdapter.notifyDataSetChanged();
            }
            searchAdapter.setKeyWord(searchKeyword);
            ivNoData.setVisibility(View.GONE);
            // 隐藏其他布局 历史记录，猜你喜欢的布局
            nonSearchLayout.setVisibility(View.GONE);
            searchAdapter.notifyDataSetChanged();
            hideLoadingView();
            searchAdapter.setOnItemClickListener(new SearchAdapter.OnRecyclerViewItemClickListener() {

                @Override
                public void onRecyclerViewItemClick(View view, BibleEntity data) {
                    Intent it = new Intent(mContext, NewArticleDetailActivity.class);
                    it.putExtra("id", data.getAid());
                    it.putExtra("author_id", data.getAuthor_id());
                    startActivity(it);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3:
                search_include_netout_layout.setVisibility(View.GONE);
                tvGetNet.setVisibility(View.GONE);
                ivNoData.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void setClickListener() {
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = historyList.get(position);
                etSearch.setText(str);
                etSearch.setSelection(str.length());
            }
        });

        tvGetNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                startActivityForResult(intent, 3);
            }
        });

        etSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tvOperate.setText("搜索");
            }
        });

        relDeleteWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                relDeleteWord.setVisibility(View.GONE);
                rvSearchResult.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                searchDataList.clear();
                nonSearchLayout.setVisibility(View.VISIBLE);
//                initActivityData();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                decorateKey = etSearch.getText().toString().trim();
                if ("".equals(decorateKey)) {
                    tvOperate.setText("取消");
                    relDeleteWord.setVisibility(View.GONE);
                } else {
                    tvOperate.setText("搜索");
                    relDeleteWord.setVisibility(View.VISIBLE);
                }
            }
        });

        tvOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opert = tvOperate.getText().toString().trim();

                if ("取消".equals(opert)) {
                    finish();
                } else if ("搜索".equals(opert)) {
                    // 插入数据库
                    if (manager == null) {
                        manager = HistoryManager.getInstance(mContext);
                    }
                    if (!manager.checkDuplicateSearchHistory(decorateKey)) {
                        String sql = "insert into Search_History_Data(history_text, history_date) values(?,?)";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String historyTime = sdf.format(new Date());
                        Object[] bindArgs = {decorateKey, historyTime};
                        manager.insertHistory(sql, bindArgs);
                    }

                    if (Util.isNetAvailable(mContext)) {
                        searchDataList.clear();
                        showLoadingView();
                        goSearch(decorateKey);
                        tvOperate.setText("取消");
                    } else {
                        Util.showNetOutView(mContext, search_include_netout_layout, false);
                    }

                }
            }
        });


        gvYoulike.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = youlikeList.get(position);
                etSearch.setText(str);
                etSearch.setSelection(str.length());
            }
        });


        relDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除 历史记录
                if (manager == null) {
                    manager = HistoryManager.getInstance(mContext);
                }
                manager.dropHistoryTable();
                historyList.clear();
                relHistory.setVisibility(View.GONE);
            }
        });

        relHideYoulike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    gvYoulike.setVisibility(View.GONE);
                    ivSee.setBackgroundResource(R.mipmap.hide_youlick);
                } else {
                    gvYoulike.setVisibility(View.VISIBLE);
                    ivSee.setBackgroundResource(R.mipmap.youlik_show);
                }

                isShow = !isShow;
            }
        });



    }


}
