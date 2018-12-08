package com.tbs.tobosutype.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.ReplyFragmentAdapter;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean.AskQuestionBean;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/12/3 13:55.
 * 提问
 */
public class ReplyFragment extends BaseFragment {
    @BindView(R.id.rv_reply)
    RecyclerView recyclerView;
    @BindView(R.id.dq_swipe)
    SwipeRefreshLayout dqSwipe;
    @BindView(R.id.rl_no_content)
    RelativeLayout rlNoContent;
    private ReplyFragmentAdapter adapter;
    private List<AskQuestionBean> questionBeanList;
    private Unbinder unbinder;
    private int mPage = 1;//用于分页的数据
    private boolean isDownRefresh = false;//是否是下拉刷新
    private int mPageSize = 10;//用于分页的数据
    private LinearLayoutManager layoutManager;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, null);
        unbinder = ButterKnife.bind(this, view);
        gson = new Gson();
        initData();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        recyclerView.setBackgroundColor(Color.WHITE);
        rlNoContent.setBackgroundColor(Color.WHITE);
        //下拉刷新
        dqSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dqSwipe.setBackgroundColor(Color.WHITE);
        dqSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dqSwipe.setOnRefreshListener(onRefreshListener);

        questionBeanList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(onTouchListener);
        recyclerView.setOnScrollListener(onScrollListener);

        replyHttpRequest();

    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        super.receiveEvent(event);
        switch (event.getCode()){
            case EC.EventCode.SEND_SUCCESS_REPLY:
                initRequest();
                break;
        }
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
    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (adapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastVisibleItemPosition() + 1 == adapter.getItemCount()) {
                    LoadMore();
                }
            }
        }
    };

    //加载更多数据
    private void LoadMore() {
        mPage++;
        replyHttpRequest();
    }

    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            dqSwipe.setRefreshing(true);
            initRequest();
        }

    };

    private void initRequest() {
        mPage = 1;
        isDownRefresh = true;
        if (adapter != null) {
            adapter = null;
        }
        if (!questionBeanList.isEmpty()) {
            questionBeanList.clear();
        }
        replyHttpRequest();
    }

    /**
     * 网络请求
     */
    private void replyHttpRequest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("uid", AppInfoUtil.getUserid(getActivity()));
        params.put("page", mPage);
        params.put("pagesize", mPageSize);
        OKHttpUtil.post(Constant.ASK_MYQUESTIONLIST, params, new Callback() {
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
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String meg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AskQuestionBean questionBean = gson.fromJson(jsonArray.get(i).toString(), AskQuestionBean.class);
                            questionBeanList.add(questionBean);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter == null) {
                                    adapter = new ReplyFragmentAdapter(getActivity(), questionBeanList);
                                    recyclerView.setAdapter(adapter);
                                }
                                if (isDownRefresh) {
                                    isDownRefresh = false;
                                    recyclerView.scrollToPosition(0);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    adapter.notifyItemInserted(questionBeanList.size() - mPageSize);
                                }
                                dqSwipe.setRefreshing(false);

                            }
                        });


                    } else if (status.equals("201")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!questionBeanList.isEmpty()) {
                                    ToastUtil.showShort(getActivity(), meg);
                                } else {
                                    rlNoContent.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(getActivity(), meg);
                            }
                        });
                    }
            } catch(
            JSONException e)

            {
                e.printStackTrace();
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
