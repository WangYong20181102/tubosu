package com.tobosu.mydecorate.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.NewArticleDetailActivity;
import com.tobosu.mydecorate.adapter.TempChildAdapter;
import com.tobosu.mydecorate.entity.BibleEntity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChildFragment extends Fragment {
    private static Context context;
    private static final String TAG = ChildFragment.class.getSimpleName();

    private CustomWaitDialog waitDialog;

    private int type_id = 0;

    /**装箱集合*/
    private ArrayList<BibleEntity> tempChildDataList = new ArrayList<BibleEntity>();

    /**数据*/
    private ArrayList<BibleEntity> bibleEntityList = new ArrayList<BibleEntity>();

    private RecyclerView childFragmentRecycleview;

    private SwipeRefreshLayout swipeRefreshLayout;

    //列表的样式
    private LinearLayoutManager mLinearLayoutManager;

    private TempChildAdapter childAdapter;

    private View rootView; //缓存Fragment view

    private RelativeLayout homechild_include_netout_layout;


    /**
     * 单例获取
     *
     * @param type_id
     * @param mContext
     * @return
     */
    public ChildFragment newInstance(int type_id, Context mContext) {
        context = mContext;
        ChildFragment f = new ChildFragment();
        Bundle b = new Bundle();
        b.putInt("type_id", type_id);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type_id = getArguments().getInt("type_id");
        Util.setErrorLog(TAG,"------------onCreate-----title_id->>>" + type_id + "<<<---");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Util.setErrorLog(TAG, "----onCreateView----title_id->>>" + type_id);
        rootView = inflater.inflate(R.layout.fragment_child_layout, null, false);
        initView(rootView);
        do_post(type_id);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Util.setErrorLog(TAG, "onResume----title_id->>>" + type_id);

    }

    private void showLoadingView() {
        waitDialog = new CustomWaitDialog(getActivity());
        waitDialog.show();
    }

    private void hideLoadingView() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }

    private void initView(View view) {
        showLoadingView();
        homechild_include_netout_layout = (RelativeLayout) view.findViewById(R.id.homechild_include_netout_layout);
        childFragmentRecycleview = (RecyclerView) view.findViewById(R.id.child_fragment_recycleview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_scrollview_container);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        if (type_id == 0) {
            swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        }

        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(swipeLister);

        mLinearLayoutManager = new LinearLayoutManager(context);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        childFragmentRecycleview.setLayoutManager(mLinearLayoutManager);

        childFragmentRecycleview.setAdapter(childAdapter);
        childFragmentRecycleview.setOnScrollListener(onScrollListener);
        childFragmentRecycleview.setOnTouchListener(onTouchListener);
    }


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
        do_post(type_id);
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

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            bibleEntityList.clear();
            page = 1;
            do_post(type_id);
        }
    };

    private void initChildBibleAdapter(ArrayList<BibleEntity> _dataList) {
//        swipeRefreshLayout.setRefreshing(false);
//        for(int i=0;i<_dataList.size();i++){
//            Util.setLog(TAG, _dataList.get(i).getTitle());
//        }
//        Util.setLog(TAG, "传进来的列表的长度是 = " + _dataList.size());
        if (_dataList != null && _dataList.size() > 0) {
            if (childAdapter == null) {
                childAdapter = new TempChildAdapter(context, _dataList);
                childFragmentRecycleview.setAdapter(childAdapter);
            }else {
                childAdapter.notifyDataSetChanged();
            }
            hideLoadingView();
            childAdapter.setOnItemClickListener(new TempChildAdapter.OnRecyclerViewItemClickListener() {

                @Override
                public void onRecyclerViewItemClick(View view, BibleEntity data) {
                    goStartActivity(data.getAid(), data.getAuthor_id());
                }
            });
        }else {
            Util.showNetOutView(context, homechild_include_netout_layout, false);
        }
    }


    private void goStartActivity(String _articleId, String _writerUserId) {
        Intent it = new Intent(context, NewArticleDetailActivity.class);
        it.putExtra("id",_articleId);
        it.putExtra("author_id",_writerUserId);
        startActivity(it);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constant.DETAIL_FINISH_RESULTCODE:
                Bundle b = data.getBundleExtra("Detail_Goback_Bundle");
                type_id = b.getInt("goback_which_position");
                break;
        }
    }


    private void parse(String json) {
        System.out.println("------childfragment正在解析-------json--->>>>");

        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getInt("status") == 200) {

                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    tempChildDataList.add(new BibleEntity(array.get(i).toString()));
                }
                Log.e(TAG, "获取的集合长度======" + tempChildDataList.size());

//                for(int i=0;i<tempChildDataList.size();i++){
//                    Util.setErrorLog(TAG, tempChildDataList.get(i).getTitle() + "=++++=" + type_id);
//                    if(bibleEntityList.contains(tempChildDataList.get(i))){
//                        Log.e(TAG,"移除的选项======"+tempChildDataList.get(i));
//                        tempChildDataList.remove(i);
//                    }
//                }
//
//                for(int i=0;i<bibleEntityList.size();i++){
//                    Util.setErrorLog(TAG, bibleEntityList.get(i).getTitle() + "=++++#=" + type_id);
//                }

                bibleEntityList.addAll(tempChildDataList);



                if(childAdapter!=null){
                    childAdapter.notifyDataSetChanged();
                }
                tempChildDataList.clear();



                // 适配器加载数据
                if (context != null) {
                    initChildBibleAdapter(bibleEntityList);
                }

            } else if (jsonObject.getInt("status") == 201){
                myHandler.sendEmptyMessage(201);
            }else{
                myHandler.sendEmptyMessage(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Util.setErrorLog(TAG, "onDestroyView" + type_id);
    }


    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Util.setErrorLog(TAG, "onAttach" + type_id);
        mActivity = activity;
    }

    //得到可靠地Activity
    public Activity getMyActivity() {
        return mActivity;
    }

    private int page = 1;
    private int pageSize = 10;

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 201:
                    Util.setToast(context, "无更多数据了");
                    childAdapter.hideFootView();
                    hideLoadingView();
                    break;
                case 0:
                    Util.setToast(context, "系统繁忙，请稍后再试~");
                    childAdapter.hideFootView();
                    hideLoadingView();
                    break;
            }
        }
    };

    /**
     * 加载网络数据
     *
     * @param position
     */
    private void do_post(int position) {
//        Util.setToast(context, type_id+"");

        if (Util.isNetAvailable(getMyActivity())) {
            OKHttpUtil okHttpUtil = new OKHttpUtil();

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("page", page + "");
            params.put("pageSize", pageSize + "");
            params.put("token", Util.getDateToken());
            params.put("type_id", position);

            okHttpUtil.post(Constant.DECORATE_CHILD_FRAGMENT_URL, params, new OKHttpUtil.BaseCallBack() {

                @Override
                public void onSuccess(Response response, String json) {
                    System.out.println("-------请求网络后的数据是---------->>>" + json + "<<<<");
                    swipeRefreshLayout.setRefreshing(false);
                    parse(json);
                }

                @Override
                public void onFail(Request request, IOException e) {
                    // 请求失败
                    swipeRefreshLayout.setRefreshing(false);
                    System.out.println("-------请求网络数据失败情况----------->>>" + e.getMessage() + "<<<<");
                }

                @Override
                public void onError(Response response, int code) {
                    myHandler.sendEmptyMessage(0);
                    // 请求出错
                    swipeRefreshLayout.setRefreshing(false);
                    System.out.println("-------请求网络数据出错----------->>>" + response.message() + "<<<<");
                }
            });
        } else {
            //TODO 无网络 占位图
            Util.showNetOutView(context, homechild_include_netout_layout, false);
            childFragmentRecycleview.setVisibility(View.GONE);
            hideLoadingView();
        }
    }
}