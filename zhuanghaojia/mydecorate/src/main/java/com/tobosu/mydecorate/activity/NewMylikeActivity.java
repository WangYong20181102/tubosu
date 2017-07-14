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
import com.tobosu.mydecorate.adapter.MyLikeAdapter;
import com.tobosu.mydecorate.entity.Mylike;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewMylikeActivity extends AppCompatActivity {
    /**
     * 我的点赞
     */
    private Context mContext;
    private String TAG = "NewMylikeActivity";
    private int mPage = 1;//请求列表页数

    //是否显示了删除复选框
    private boolean isShowDelete;
    //loading图层
    private CustomWaitDialog customWaitDialog;
    //我的点赞数据集合
    private List<Mylike> mylikeList = new ArrayList<>();
    //临时数据集合
    private List<Mylike> tempList = new ArrayList<>();
    //删除的选项所在位置集合
    private List<Integer> delPositionList = new ArrayList<>();
    //适配器
    private MyLikeAdapter myLikeAdapter;

    private ImageView mylike_back;//返回按钮
    private ImageView mylike_del_img;//删除按钮
    private SwipeRefreshLayout mylike_swipe;//下拉刷新控件
    private RecyclerView mylike_recyclerview;//列表集合
    private RelativeLayout mylike_del_rl;//删除按钮组件集合
    private TextView mylike_del_all;//一键清空按钮
    private TextView mylike_del_btn;//单一删除按钮
    private LinearLayout mylike_img_empty_ll;

    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mylike);
        mContext = this;
        bindView();
        initView();
        initViewEvent();
        HttpGetMylikeList(1);
    }

    private void bindView() {
        mylike_back = (ImageView) findViewById(R.id.mylike_back);
        mylike_del_img = (ImageView) findViewById(R.id.mylike_del_img);
        mylike_swipe = (SwipeRefreshLayout) findViewById(R.id.mylike_swipe);
        mylike_recyclerview = (RecyclerView) findViewById(R.id.mylike_recyclerview);
        mylike_del_rl = (RelativeLayout) findViewById(R.id.mylike_del_rl);
        mylike_del_all = (TextView) findViewById(R.id.mylike_del_all);
        mylike_del_btn = (TextView) findViewById(R.id.mylike_del_btn);
        mylike_img_empty_ll = (LinearLayout) findViewById(R.id.mylike_img_empty_ll);
    }

    private void initView() {
        customWaitDialog = new CustomWaitDialog(mContext);
    }

    private void initViewEvent() {
        mylike_back.setOnClickListener(occl);
        mylike_del_img.setOnClickListener(occl);
        mylike_del_all.setOnClickListener(occl);
        mylike_del_btn.setOnClickListener(occl);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mylike_recyclerview.setLayoutManager(mLinearLayoutManager);
        mylike_recyclerview.setOnScrollListener(onScrollListener);
        mylike_recyclerview.setOnTouchListener(onTouchListener);


        mylike_swipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);//下拉转圈圈颜色
        mylike_swipe.setBackgroundColor(Color.WHITE);//整个界面的颜色  白色
        mylike_swipe.setSize(SwipeRefreshLayout.DEFAULT);//圆圈的大小
        mylike_swipe.setOnRefreshListener(swipeLister);

    }

    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            if (mylikeList.size() > 0) {
                initData();
            } else {
                mylike_swipe.setRefreshing(false);
            }
        }
    };

    private void initData() {
        mPage = 1;
        mylikeList.clear();
        myLikeAdapter.getCheckList().clear();
        mylike_del_rl.setVisibility(View.GONE);
        isShowDelete = false;
        HttpGetMylikeList(mPage);
    }

    //列表触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (mylike_swipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };
    //列表滑动事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
//            Log.e(TAG, "最后可见目标===" + lastVisiableItem + "集合总数===" + mLinearLayoutManager.getItemCount() + "==newState==" + newState + "==刷新状态==" + collectSwipe.isRefreshing());
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !mylike_swipe.isRefreshing()
                    && !isShowDelete) {
                loadMore();
                myLikeAdapter.changeState(1);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void loadMore() {
        mPage++;
        HttpGetMylikeList(mPage);
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mylike_back:
                    finish();
                    break;
                case R.id.mylike_del_img:
                    /**
                     * 删除按钮点击事件处理逻辑
                     * 1.显示底部两个角标
                     * 2.在当前显示的列表中 显示复选框
                     */
                    if (mylikeList.size() > 0) {
                        Log.e(TAG, "点击了选中checkbox按钮");
                        isShowDelete = true;
                        mylike_del_rl.setVisibility(View.VISIBLE);
                        myLikeAdapter.changeState(2);
                    } else {
                        Toast.makeText(mContext, "当前没有数据可以删除~", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.mylike_del_btn:
                    //删除选中的项目
                    Log.e(TAG, "点击了删除单个选项按钮");
                    HttpDelCheck();
                    break;
                case R.id.mylike_del_all:
                    //删除所有收藏
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("删除提示");
                    builder.setMessage("确定要清空所有点赞？");
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

    private void HttpDelAll() {
        customWaitDialog.show();
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        okHttpUtil.post(Constant.DEL_ALL_MYLIKE_URL, param, new OKHttpUtil.BaseCallBack() {
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

    private void HttpDelCheck() {
        customWaitDialog.show();
        String id = "";//删除选中的id
        delPositionList.addAll(myLikeAdapter.getCheckList());
        for (int i = 0; i < delPositionList.size(); i++) {
            id = id + mylikeList.get(delPositionList.get(i)).getId() + ",";
        }
        if (id.length() > 1) {
            id = id.substring(0, id.length() - 1);
        } else {
            customWaitDialog.dismiss();
            Toast.makeText(mContext, "当前未选中要删除的选项！", Toast.LENGTH_SHORT).show();
            return;
        }
        myLikeAdapter.getCheckList().clear();
        myLikeAdapter.notifyDataSetChanged();
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", id);
        okHttpUtil.post(Constant.DEL_MYLIKE_URL, param, new OKHttpUtil.BaseCallBack() {
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

    //获取我的点赞列表
    private void HttpGetMylikeList(int page) {
        customWaitDialog.show();
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("page", page);
        param.put("page_size", "10");
        okHttpUtil.post(Constant.GET_MYLIKE_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                initNetData(json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                customWaitDialog.dismiss();
                mylike_swipe.setRefreshing(false);
                myLikeAdapter.changeState(3);
            }

            @Override
            public void onError(Response response, int code) {
                customWaitDialog.dismiss();
                mylike_swipe.setRefreshing(false);
                myLikeAdapter.changeState(3);
            }
        });
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
                    Mylike mylike = new Mylike(jsonArray.get(i).toString());
                    tempList.add(mylike);
                }
                Log.e(TAG, "临时数据集合的长度====" + tempList.size());
                mylikeList.addAll(tempList);
                tempList.clear();
                if (myLikeAdapter == null) {
                    myLikeAdapter = new MyLikeAdapter(mContext, mylikeList);
                    if (isShowDelete) {
                    } else {
                        myLikeAdapter.setOnMyLikeItemClickLister(adapterClickLister);
                    }
                    mylike_recyclerview.setAdapter(myLikeAdapter);
                    myLikeAdapter.notifyDataSetChanged();
                } else {
                    myLikeAdapter.notifyDataSetChanged();
                }
                //移除加载图层
                customWaitDialog.dismiss();
                mylike_swipe.setRefreshing(false);
                myLikeAdapter.changeState(3);
            } else if (status.equals("201")) {
                Log.e(TAG, "没有更多数据了");
                Toast.makeText(mContext, "没有更多数据了~", Toast.LENGTH_LONG).show();
                mylike_swipe.setRefreshing(false);
                customWaitDialog.dismiss();
                if (mylikeList.size() > 0) {
                    myLikeAdapter.changeState(3);
                } else if (mylikeList.size() == 0) {
                    mylike_img_empty_ll.setVisibility(View.VISIBLE);
                }

            } else {
                mylike_swipe.setRefreshing(false);
                customWaitDialog.dismiss();
                myLikeAdapter.changeState(3);
                Log.e(TAG, "请求失败！");
            }
        } catch (JSONException e) {
            mylike_swipe.setRefreshing(false);
            customWaitDialog.dismiss();
            myLikeAdapter.changeState(3);
        }
    }

    private MyLikeAdapter.OnMyLikeItemClickLister adapterClickLister = new MyLikeAdapter.OnMyLikeItemClickLister() {
        @Override
        public void onItemClick(View view) {
            int position = mylike_recyclerview.getChildPosition(view);
            Intent intent = new Intent(mContext, NewArticleDetailActivity.class);
            intent.putExtra("id", mylikeList.get(position).getAid());
            intent.putExtra("author_id", mylikeList.get(position).getAuthor_id());
            startActivity(intent);
        }
    };
}
