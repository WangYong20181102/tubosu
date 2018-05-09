package com.tbs.tbs_mj.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.ArticleWebViewActivity;
import com.tbs.tbs_mj.adapter.ArticleTypeAdapter;
import com.tbs.tbs_mj.base.BaseFragment;
import com.tbs.tbs_mj.bean._ArticleTypeItem;
import com.tbs.tbs_mj.customview.MyLinearLayoutManager;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/12/29 18:10.
 * 学装修分类页面（复用页面）
 * 每一个页面的对应一个标题
 * 3.7版本新增
 */

public class ArticleTypeFragment extends BaseFragment {

    @BindView(R.id.frag_article_recycler)
    RecyclerView fragArticleRecycler;
    @BindView(R.id.frag_article_swipe_layout)
    SwipeRefreshLayout fragArticleSwipeLayout;
    @BindView(R.id.frag_article_none_data_rl)
    RelativeLayout fragArticleNoneDataRl;
    Unbinder unbinder;
    private Context mContext;
    private String TAG = "ArticleTypeFragment";
    private String mArticleTypeId;
    private Gson mGson;
    private boolean isLoading = false;//是否正在加载数据
    private boolean isDownRefresh = false;//是否是下拉刷新
    private int mPage = 1;
    private int mPageSize = 20;
    //适配器管理
    private LinearLayoutManager mLinearLayoutManager;
    //显示的适配器
    private ArticleTypeAdapter mArticleTypeAdapter;
    //数据集合
    private ArrayList<_ArticleTypeItem> mArticleTypeItemArrayList = new ArrayList<>();


    public ArticleTypeFragment() {

    }

    public static ArticleTypeFragment newInstance(String mArticleTypeId) {
        ArticleTypeFragment articleTypeFragment = new ArticleTypeFragment();
        Bundle bundle = new Bundle();
        Log.e("ArticleType newInstanc", "=======mArticleTypeId=====" + mArticleTypeId);
        bundle.putString("mArticleTypeId", mArticleTypeId);
        articleTypeFragment.setArguments(bundle);
        return articleTypeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mArticleTypeId = args.getString("mArticleTypeId");
            Log.e(TAG, "======onCreate获取id=====" + mArticleTypeId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_type, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initViewEvent();
        return view;
    }

    //初始化页面
    private void initViewEvent() {
        mGson = new Gson();
        //初始化下拉刷新控件
        fragArticleSwipeLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        fragArticleSwipeLayout.setBackgroundColor(Color.WHITE);
        fragArticleSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
        fragArticleSwipeLayout.setOnRefreshListener(onRefreshListener);
        //设置RecycleView相关事务
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        fragArticleRecycler.setLayoutManager(mLinearLayoutManager);
        fragArticleRecycler.addOnScrollListener(onScrollListener);
        fragArticleRecycler.setOnTouchListener(onTouchListener);
        //请求网络获取数据
        HttpGetArticleTypeList(mPage);
    }

    //点击触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (fragArticleSwipeLayout.isRefreshing() || isLoading) {
                return true;
            } else {
                return false;
            }
        }
    };
    //上拉加载更多
    //列表上拉加载更多事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !fragArticleSwipeLayout.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        mPage++;
        HttpGetArticleTypeList(mPage);
    }

    //下拉刷新事件监听
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据  重置页码 清空数据集合 重新请求数据
            if (!isLoading) {
//                fragArticleClickView.setVisibility(View.VISIBLE);
                mPage = 1;
                if (!mArticleTypeItemArrayList.isEmpty()) {
                    mArticleTypeItemArrayList.clear();
                }
                //重新获取数据
                HttpGetArticleTypeList(mPage);
            } else {
                //停止刷新
                fragArticleSwipeLayout.setRefreshing(false);
            }
        }
    };

    //子项的店家事件
    private ArticleTypeAdapter.OnArticleTypeItemClickLister onArticleTypeItemClickLister = new ArticleTypeAdapter.OnArticleTypeItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            // TODO: 2018/1/2 点击子项跳转到文章详情的Web页面
            Util.HttpArticleClickCount(mArticleTypeItemArrayList.get(position).getId());
            Intent intent = new Intent(mContext, ArticleWebViewActivity.class);
            intent.putExtra("mLoadingUrl", mArticleTypeItemArrayList.get(position).getJump_url() + "?app_type=1");
            mContext.startActivity(intent);
        }
    };

    //请求列表数据
    private void HttpGetArticleTypeList(int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("type_id", mArticleTypeId);
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.Z_ARTICLE_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e(TAG, "获取数据失败=====" + e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragArticleSwipeLayout.setRefreshing(false);
//                        fragArticleClickView.setVisibility(View.GONE);
                        isLoading = false;
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "数据获取成功======" + json);
                Log.e(TAG, "参数==mArticleTypeId==" + mArticleTypeId);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //正确拿到适配的数据
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //将数据解析添加到集合中
                            _ArticleTypeItem articleTypeItem = mGson.fromJson(jsonArray.get(i).toString(), _ArticleTypeItem.class);
                            mArticleTypeItemArrayList.add(articleTypeItem);
                        }
                        //将数据布局到列表中
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mArticleTypeAdapter == null) {
                                    mArticleTypeAdapter = new ArticleTypeAdapter(mContext, mArticleTypeItemArrayList);
                                    fragArticleRecycler.setAdapter(mArticleTypeAdapter);
                                    mArticleTypeAdapter.setOnArticleTypeItemClickLister(onArticleTypeItemClickLister);
                                    mArticleTypeAdapter.notifyDataSetChanged();
//                                    fragArticleClickView.setVisibility(View.GONE);
                                    fragArticleSwipeLayout.setRefreshing(false);
                                    isLoading = false;
                                } else {
//                                    if (isDownRefresh) {
//                                        isDownRefresh = false;
//                                        fragArticleRecycler.scrollToPosition(0);
//                                        mArticleTypeAdapter.notifyDataSetChanged();
//                                    } else {
//                                        mArticleTypeAdapter.notifyItemInserted(mArticleTypeItemArrayList.size() - mPageSize);
//                                    }
                                    mArticleTypeAdapter.notifyDataSetChanged();
//                                    fragArticleClickView.setVisibility(View.GONE);
                                    fragArticleSwipeLayout.setRefreshing(false);
                                    isLoading = false;
                                }

                            }
                        });
                    } else {
                        //没有数据
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!mArticleTypeItemArrayList.isEmpty()) {
                                    Toast.makeText(mContext, "当前没有更多数据~", Toast.LENGTH_SHORT).show();
//                                    fragArticleClickView.setVisibility(View.GONE);
                                    fragArticleSwipeLayout.setRefreshing(false);
                                    isLoading = false;
                                } else {
                                    //显示数据为空的时候的占位图
                                    fragArticleNoneDataRl.setVisibility(View.VISIBLE);
//                                    fragArticleClickView.setVisibility(View.GONE);
                                    fragArticleSwipeLayout.setRefreshing(false);
                                    isLoading = false;
                                }

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
