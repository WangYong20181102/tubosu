package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.MyCollentAdapter;
import com.tobosu.mydecorate.entity._Collect;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的收藏界面
 */
public class NewMycollectActivity extends AppCompatActivity {
    private Context mContext;
    private String TAG = "NewMycollectActivity";
    private int mPage = 1;
    //是否显示了删除复选框
    private boolean isShowDelete;
    //loading图层
    private CustomWaitDialog customWaitDialog;
    //我的收藏数据集合
    private List<_Collect> collectList = new ArrayList<>();
    //临时装箱集合
    private List<_Collect> tempList = new ArrayList<>();

    //我的收藏适配器
    private MyCollentAdapter myCollentAdapter;

    //界面中的回退按钮
    private ImageView collectBack;
    //界面中的删除图标
    private ImageView collectDelImg;
    //界面下拉刷新控件
    private SwipeRefreshLayout collectSwipe;
    //列表
    private RecyclerView collectRecycleView;
    //列表的样式
    private LinearLayoutManager mLinearLayoutManager;

    //删除的图层
    private RelativeLayout collectDelRL;
    //一键清空按钮
    private TextView collectDelAll;
    //删除按钮
    private TextView collectDelBtn;
    //当前没有数据的图层
    private ImageView collect_data_empty;
    //数据列表层
    private LinearLayout collect_img_empty_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mycollect);
        mContext = this;
        customWaitDialog = new CustomWaitDialog(mContext);
        customWaitDialog.show();

        bindView();
        initViewEvent();
        HttpGetMyCollect(mPage);
    }

    private void bindView() {
        collectBack = (ImageView) findViewById(R.id.collect_back);
        collectDelImg = (ImageView) findViewById(R.id.mylike_del_img);
        collectSwipe = (SwipeRefreshLayout) findViewById(R.id.mylike_swipe);
        collectRecycleView = (RecyclerView) findViewById(R.id.mylike_recyclerview);
        collectDelRL = (RelativeLayout) findViewById(R.id.collect_del_rl);
        collectDelAll = (TextView) findViewById(R.id.mylike_del_all);
        collectDelBtn = (TextView) findViewById(R.id.mylike_del_btn);
        collect_data_empty = (ImageView) findViewById(R.id.collect_data_empty);
        collect_img_empty_ll = (LinearLayout) findViewById(R.id.collect_img_empty_ll);
    }

    //初始化视图事件
    private void initViewEvent() {
        //控件的点击事件
        collectBack.setOnClickListener(occl);
        collectDelImg.setOnClickListener(occl);
        collectDelAll.setOnClickListener(occl);
        collectDelBtn.setOnClickListener(occl);

        /**
         *collectRecycleView 设置布局排布以及事件的监听
         */
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        collectRecycleView.setLayoutManager(mLinearLayoutManager);
//        collectRecycleView.
        collectRecycleView.setOnScrollListener(onScrollListener);
        collectRecycleView.setOnTouchListener(onTouchListener);
        /**
         * 下拉控件进行修饰 以及添加下拉事件
         */
        collectSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);//下拉转圈圈颜色
        collectSwipe.setBackgroundColor(Color.WHITE);//整个界面的颜色  白色
        collectSwipe.setSize(SwipeRefreshLayout.DEFAULT);//圆圈的大小
        collectSwipe.setOnRefreshListener(swipeLister);
    }

    //显示列表的滑动监听事件 上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
//            Log.e(TAG, "最后可见目标===" + lastVisiableItem + "集合总数===" + mLinearLayoutManager.getItemCount() + "==newState==" + newState + "==刷新状态==" + collectSwipe.isRefreshing());
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !collectSwipe.isRefreshing() && !isShowDelete) {
                loadMore();
                myCollentAdapter.changeState(1);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (collectSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            if (collectList.size() > 0) {
                initData();
            } else {
                collectSwipe.setRefreshing(false);
            }

        }
    };

    //初始化数据
    private void initData() {
        mPage = 1;
        collectList.clear();
        myCollentAdapter.getCheckList().clear();
        collectDelRL.setVisibility(View.GONE);
        isShowDelete = false;
        HttpGetMyCollect(mPage);
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.collect_back:
                    finish();
                    break;
                case R.id.mylike_del_img:
                    /**
                     * 删除按钮点击事件处理逻辑
                     * 1.显示底部两个角标
                     * 2.在当前显示的列表中 显示复选框
                     */
                    if (collectList.size() > 0) {
                        Log.e(TAG, "点击了选中checkbox按钮");
                        isShowDelete = true;
                        collectDelRL.setVisibility(View.VISIBLE);
                        myCollentAdapter.changeState(2);
                    } else {
                        Toast.makeText(mContext, "当前没有数据可以删除~", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.mylike_del_btn:
                    //删除选中的项目
                    Log.e(TAG, "点击了删除单个选项按钮");
                    Log.e(TAG, "获取列表长度=====" + myCollentAdapter.getCheckList().size());
                    HttpDelCheck();
                    break;
                case R.id.mylike_del_all:
                    //删除所有收藏
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("删除提示");
                    builder.setMessage("确定要清空所有收藏？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HttpDelAll();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
            }
        }
    };

    //删除选中项
    private void HttpDelCheck() {
        customWaitDialog.show();
        String id = "";//删除选中的id
        for (int i = 0; i < myCollentAdapter.getCheckList().size(); i++) {
            id = id + collectList.get(myCollentAdapter.getCheckList().get(i)).getId() + ",";
        }
        if (id.length() > 1) {
            id = id.substring(0, id.length() - 1);
        } else {
            customWaitDialog.dismiss();
            Toast.makeText(mContext, "当前未选中要删除的选项！", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e(TAG, "获取选中的id====" + id);
        myCollentAdapter.getCheckList().clear();
        myCollentAdapter.notifyDataSetChanged();
        Log.e(TAG, "清空后集合的长度===" + myCollentAdapter.getCheckList().size());
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", id);
        okHttpUtil.post(Constant.DEL_COLLECT_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "删除数据接口返回====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        initData();
                        customWaitDialog.dismiss();
                        Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        customWaitDialog.dismiss();
                        Toast.makeText(mContext, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    customWaitDialog.dismiss();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                customWaitDialog.dismiss();
            }

            @Override
            public void onError(Response response, int code) {
                customWaitDialog.dismiss();
            }
        });
    }

    private void HttpDelAll() {
        customWaitDialog.show();
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        okHttpUtil.post(Constant.DEL_ALL_COLLECT_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        initData();
                        customWaitDialog.dismiss();
                        Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        customWaitDialog.dismiss();
                        Toast.makeText(mContext, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    customWaitDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                customWaitDialog.dismiss();
            }

            @Override
            public void onError(Response response, int code) {
                customWaitDialog.dismiss();
            }
        });
    }

    private void loadMore() {
        mPage++;
        HttpGetMyCollect(mPage);
    }

    //初始化网络请求回来的数据
    private void initNetData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
                Log.e(TAG, "数据请求成功！" + json);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    _Collect collect = new _Collect(jsonArray.get(i).toString());
                    tempList.add(collect);
                }

                collectList.addAll(tempList);
                tempList.clear();
                if (myCollentAdapter == null) {
                    myCollentAdapter = new MyCollentAdapter(mContext, collectList);
                    if (isShowDelete) {
                        //在显示复选框时禁止使用点击事件
                    } else {
                        myCollentAdapter.setOnItemClickLister(adapterClickLister);
                    }
                    collectRecycleView.setAdapter(myCollentAdapter);
                    myCollentAdapter.notifyDataSetChanged();
                } else {
                    myCollentAdapter.notifyDataSetChanged();
                }
                //移除加载图层
                customWaitDialog.dismiss();
                collectSwipe.setRefreshing(false);
                myCollentAdapter.changeState(3);
            } else if (status.equals("201")) {
                Toast.makeText(mContext, "没有更多数据了~", Toast.LENGTH_LONG).show();
                collectSwipe.setRefreshing(false);
                customWaitDialog.dismiss();
                if (collectList.size() > 0) {
                    myCollentAdapter.changeState(3);
                } else if (collectList.size() == 0) {
                    Log.e(TAG, "当前数据为空");
                    collect_img_empty_ll.setVisibility(View.VISIBLE);
                    collect_data_empty.setVisibility(View.VISIBLE);
                }
            } else {
                collectSwipe.setRefreshing(false);
                customWaitDialog.dismiss();
                myCollentAdapter.changeState(3);
                Log.e(TAG, "请求失败！");
            }
        } catch (JSONException e) {
            collectSwipe.setRefreshing(false);
            customWaitDialog.dismiss();
            myCollentAdapter.changeState(3);
        }
    }

    //列表单项点击事件
    private MyCollentAdapter.OnItemClickLister adapterClickLister = new MyCollentAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view) {
            int position = collectRecycleView.getChildPosition(view);
            Intent intent = new Intent(mContext, NewArticleDetailActivity.class);
            intent.putExtra("id", collectList.get(position).getAid());
            intent.putExtra("author_id", collectList.get(position).getAuthor_id());
            startActivity(intent);
        }
    };

    //请求我的收藏列表
    private void HttpGetMyCollect(int page) {
        customWaitDialog.show();
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", CacheManager.getUserUid(mContext));
        param.put("page", page);
        param.put("page_size", 10);
        okHttpUtil.post(Constant.GET_MY_COLLECT, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                initNetData(json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e(TAG, "请求失败！==原因==" + e.toString());
                customWaitDialog.dismiss();
                collectSwipe.setRefreshing(false);
                myCollentAdapter.changeState(3);
            }

            @Override
            public void onError(Response response, int code) {
                Log.e(TAG, "请求错误！==原因==" + code);
                customWaitDialog.dismiss();
                collectSwipe.setRefreshing(false);
                myCollentAdapter.changeState(3);
            }
        });
    }
}
