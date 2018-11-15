package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorationQuestionFragmentAdapter;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean.AskQuestionBean;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/11/5 13:46.
 */
public class DecorationQuestionFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.fragment_dq_rv)
    RecyclerView fragmentDqRv;
    @BindView(R.id.dq_swipe)
    SwipeRefreshLayout dqSwipe;     //刷新
    private DecorationQuestionFragmentAdapter decorationQuestionFragmentAdapter;
    private List<AskQuestionBean> asklist = new ArrayList<>();
    private Context context;
    private int mPage = 1;//用于分页的数据
    private boolean isDownRefresh = false;//是否是下拉刷新
    private int mPageSize = 15;//用于分页的数据
    private LinearLayoutManager layoutManager;
    private String categoryId = "0";
    private Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getString("position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decorationquestion, null);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        gson = new Gson();
        initViewEvent();
        return view;
    }


    /**
     * 构造单例
     *
     * @param position
     * @return
     */
    public static DecorationQuestionFragment newInstance(String position) {
        DecorationQuestionFragment newFragment = new DecorationQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("position", position);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    private void initViewEvent() {

        //下拉刷新
        dqSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dqSwipe.setBackgroundColor(Color.WHITE);
        dqSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dqSwipe.setOnRefreshListener(onRefreshListener);

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentDqRv.setLayoutManager(layoutManager);
        ((SimpleItemAnimator) fragmentDqRv.getItemAnimator()).setSupportsChangeAnimations(false);
        fragmentDqRv.setOnTouchListener(onTouchListener);
        fragmentDqRv.setOnScrollListener(onScrollListener);

        HttpGetImageList(mPage, categoryId);

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
    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initData();
        }
    };

    private void initData() {
        mPage = 1;
        isDownRefresh = true;
        dqSwipe.setRefreshing(true);
        if (decorationQuestionFragmentAdapter != null) {
            decorationQuestionFragmentAdapter = null;
        }
        if (!asklist.isEmpty()) {
            asklist.clear();
        }
        HttpGetImageList(mPage, categoryId);
    }

    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (decorationQuestionFragmentAdapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastVisibleItemPosition() + 1 == decorationQuestionFragmentAdapter.getItemCount()) {
                    LoadMore();
                }
            }
        }
    };

    //加载更多数据
    private void LoadMore() {
        mPage++;
        HttpGetImageList(mPage, categoryId);
    }

    //网络请求
    private void HttpGetImageList(int mPage, String currentid) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("category_id", currentid);
        params.put("system_plat", 1);
        params.put("page", mPage);
        params.put("pagesize", mPageSize);
        OKHttpUtil.post(Constant.ASK_QUESTION_LIST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isDownRefresh = false;
                        dqSwipe.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = data.optJSONArray("dataList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AskQuestionBean askQuestionBean = gson.fromJson(jsonArray.get(i).toString(), AskQuestionBean.class);
                            asklist.add(askQuestionBean);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (decorationQuestionFragmentAdapter == null) {
                                    decorationQuestionFragmentAdapter = new DecorationQuestionFragmentAdapter(getActivity(), asklist);
                                    fragmentDqRv.setAdapter(decorationQuestionFragmentAdapter);
                                    decorationQuestionFragmentAdapter.notifyDataSetChanged();
                                }
                                if (isDownRefresh) {
                                    isDownRefresh = false;
                                    fragmentDqRv.scrollToPosition(0);
                                    decorationQuestionFragmentAdapter.notifyDataSetChanged();
                                } else {
                                    decorationQuestionFragmentAdapter.notifyItemInserted(asklist.size() - mPageSize);
                                }
                                dqSwipe.setRefreshing(false);
                            }
                        });


                    }
                    if (status.equals("201")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
