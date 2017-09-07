package com.tbs.tobosupicture.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.FactoryStyleAdapter;
import com.tbs.tobosupicture.adapter.SamplePictureAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.ChildData;
import com.tbs.tobosupicture.bean.DecorateFactoryStyle;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean.FactoryStyrlBean;
import com.tbs.tobosupicture.bean.SamplePicBeanEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
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
 * 工装
 * Created by Lie on 2017/7/14.
 */
public class FactoryFragment extends BaseFragment {
    private final String TAG = "FactoryFragment";
    @BindView(R.id.relStyleLayout)
    RelativeLayout relStyleLayout;

    private String class_id = "0";

    private String city;

    @BindView(R.id.ivFactoryChooseStyle)
    ImageView ivFactoryChooseStyle;
    @BindView(R.id.tvChooseStyle)
    TextView tvChooseStyle;
    @BindView(R.id.expandableListview)
    ExpandableListView expandableListview;
    @BindView(R.id.factoryRecyclerView)
    RecyclerView factoryRecyclerView;
    @BindView(R.id.factorySwipRefreshLayout)
    SwipeRefreshLayout factorySwipRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.iv_factory_no_data)
    ImageView iv_factory_no_data;

    private LinearLayoutManager linearLayoutManager;

    private HashMap<String, Object> param;
    private int page = 1;
    private int pageSize = 10;

    private ArrayList<SamplePicBeanEntity> samplePicList = new ArrayList<SamplePicBeanEntity>();
    private SamplePictureAdapter samplePicAdapter;

    private boolean closeState = true; // 收起状态


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_factory, null, false);
        unbinder = ButterKnife.bind(this, view);
        initCity();
        initListViewSetting();
        getDataFromNet();
        initData();
        return view;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case EC.EventCode.CHOOSE_CITY_TO_GET_DATA_FROM_NET_FACTORY:
                city = (String) event.getData();
                page = 1;
                samplePicList.clear();
                getDataFromNet();
                break;
        }
    }

    private void initCity(){
        city = SpUtils.getTemplateFragmentCity(getActivity());
    }

    private void getDataFromNet() {
        if(Utils.isNetAvailable(getActivity())){

            factoryRecyclerView.setVisibility(View.VISIBLE);
            factorySwipRefreshLayout.setVisibility(View.VISIBLE);
            expandableListview.setVisibility(View.GONE);

            param = new HashMap<String, Object>();
            param.put("token", Utils.getDateToken());
            param.put("type", "2");
            param.put("class_id", class_id);
            param.put("city_name", city);
            param.put("page", page);
            param.put("page_size", pageSize);

            Utils.setErrorLog(TAG, "oiy>> 城市-" + city + "  daxiao  class_id  >" +class_id);



            HttpUtils.doPost(UrlConstans.GET_LIST, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(getActivity(), "系统繁忙，稍后再试!");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String json = response.body().string();
                    Utils.setErrorLog(TAG, "what_the_factory >>" + json);
                    try {
                        final JSONObject object = new JSONObject(json);
                        if (object.getInt("status") == 200) {
                            JSONArray arr = object.getJSONArray("data");
                            for (int i = 0, len = arr.length(); i < len; i++) {
                                SamplePicBeanEntity entity = new SamplePicBeanEntity(arr.getJSONObject(i));
                                samplePicList.add(entity);
                            }
                        } else if (object.getInt("status") == 201) {
                            final String msg = object.getString("msg");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(getActivity(), msg);
                                    if (samplePicAdapter != null) {
                                        samplePicAdapter.noMoreData();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utils.setErrorLog(TAG, json);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initListViewAdapter();
                        }
                    });

                }
            });
        }else{
            // 无网络时提醒的图片

        }

    }

    private ArrayList<FactoryStyrlBean> factoryStyrlBeenList;
    private ArrayList<ArrayList<ChildData>> childDataListList = new ArrayList<ArrayList<ChildData>>();

    private void initData() {
        String json = SpUtils.getFactoryStyleJson(getActivity());
        if (!"".equals(json)) {
            DecorateFactoryStyle factoryStyle = new DecorateFactoryStyle(json);
            if (factoryStyle.getStatus() == 200) {
                factoryStyrlBeenList = factoryStyle.getFactoryStyrlBeanList();

            } else if (factoryStyle.getStatus() == 201) {

            } else {
                Utils.setErrorLog(TAG, "类型无数据");
            }
        } else {
            // 需要发一个event通知TemplateFragment重新请求网络
            iv_factory_no_data.setVisibility(View.VISIBLE);
        }
    }

    private void initListViewAdapter() {

        factorySwipRefreshLayout.setRefreshing(false);
        if (samplePicAdapter == null) {
            samplePicAdapter = new SamplePictureAdapter(getActivity(), samplePicList);
            factoryRecyclerView.setAdapter(samplePicAdapter);
        } else {
            samplePicAdapter.notifyDataSetChanged();
        }

        if(samplePicList.size()==0){
            iv_factory_no_data.setVisibility(View.VISIBLE);
        }else {
            iv_factory_no_data.setVisibility(View.GONE);
        }

        if (samplePicAdapter != null) {
            samplePicAdapter.hideLoadMoreMessage();
        }


    }


    private void initListViewSetting() {
        // 设置下拉进度的背景颜色，默认就是白色的
        factorySwipRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        factorySwipRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        factorySwipRefreshLayout.setOnRefreshListener(swipeLister);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        factoryRecyclerView.setLayoutManager(linearLayoutManager);

        factoryRecyclerView.setOnScrollListener(onScrollListener);
        factoryRecyclerView.setOnTouchListener(onTouchListener);
    }


    //显示列表的滑动监听事件 上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
//            Log.e(TAG, "最后可见目标===" + lastVisiableItem + "集合总数===" + mLinearLayoutManager.getItemCount() + "==newState==" + newState + "==刷新状态==" + swipeRefreshLayout.isRefreshing());
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= linearLayoutManager.getItemCount()
                    && !factorySwipRefreshLayout.isRefreshing()) {
                loadMore();
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
            if (factorySwipRefreshLayout.isRefreshing()) {
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
            samplePicList.clear();
            page = 1;
            if (samplePicAdapter != null) {
                samplePicAdapter.hideLoadMoreMessage();
            }
            getDataFromNet();

        }
    };

    private void loadMore() {
        page++;
        if (samplePicAdapter != null) {
            samplePicAdapter.showLoadMoreMessage();
        }

        factorySwipRefreshLayout.setRefreshing(false);
        getDataFromNet();
        System.out.println("-----**-onScrolled load more completed------");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tvChooseStyle})
    public void onViewClickedFactoryFragment(View view) {
        switch (view.getId()) {
            case R.id.tvChooseStyle:
//                Utils.setToast(getActivity(), closeState? "t": "f");

                if(closeState){
                    factoryRecyclerView.setVisibility(View.GONE);
                    factorySwipRefreshLayout.setVisibility(View.GONE);
                    expandableListview.setVisibility(View.VISIBLE);
                    iv_factory_no_data.setVisibility(View.GONE);
                    initExpandableListView();
//                    samplePicList.clear();
                }else {
                    factoryRecyclerView.setVisibility(View.VISIBLE);
                    factorySwipRefreshLayout.setVisibility(View.VISIBLE);
                    expandableListview.setVisibility(View.GONE);
                    iv_factory_no_data.setVisibility(View.GONE);
//                    samplePicList.clear();
//                    getDataFromNet();
                }
                closeState = !closeState;
                break;
        }
    }

    private void initExpandableListView() {
        FactoryStyleAdapter adapter = new FactoryStyleAdapter(getActivity(), factoryStyrlBeenList, new FactoryStyleAdapter.OnFactoryStyleItemClickListener() {
            @Override
            public void onFactoryStyleItemClickListener(String classID, String text) {
//                Utils.setToast(getActivity(), "class_id 是" + classID);
                samplePicList.clear();
                class_id = classID;
                page = 1;
                getDataFromNet();
                tvChooseStyle.setText(text);
                closeState = true;
            }

            @Override
            public void onFactoryStyleParentClickListener(String parentId, String text) {
//                Utils.setToast(getActivity(), "group_ class_id 是" + parentId);
                samplePicList.clear();
                class_id = parentId;
                page = 1;
                if("全部".equals(text)){
                    tvChooseStyle.setText("类型");
                }else {
                    tvChooseStyle.setText(text);
                }
                closeState = true;
                getDataFromNet();
            }
        });


        expandableListview.setAdapter(adapter);
        expandableListview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = expandableListview.getExpandableListAdapter().getGroupCount();
                for(int j = 0; j < count; j++){
                    if(j != groupPosition){
                        expandableListview.collapseGroup(j);
                    }
                }
            }
        });
        expandableListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int len = factoryStyrlBeenList.get(groupPosition).getChild_data().size();
                if(len==0){
                    return true;
                }
                return false;
            }
        });

    }


    private void setAnimation(final View view, boolean flag) {
        Animation animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setBackgroundResource(R.mipmap.choose_style_up);
            }
        });
    }

}
