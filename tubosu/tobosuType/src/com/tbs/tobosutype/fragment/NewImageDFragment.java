package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.LoginActivity;
import com.tbs.tobosutype.adapter.MyGridViewAdapter;
import com.tbs.tobosutype.adapter.NewImageDAdapter;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._ImageD;
import com.tbs.tobosutype.bean._SelectMsg;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

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
 * Created by Mr.Lin on 2017/11/10 14:01.
 * NewImageDFragment 套图的Fragment
 * 作用于瀑布流 newImageActivity
 */

public class NewImageDFragment extends BaseFragment {
    @BindView(R.id.frag_new_img_fengge)
    TextView fragNewImgFengge;
    @BindView(R.id.frag_new_img_fengge_san)
    ImageView fragNewImgFenggeSan;
    @BindView(R.id.frag_new_img_fengge_ll)
    LinearLayout fragNewImgFenggeLl;
    @BindView(R.id.frag_new_img_huxing)
    TextView fragNewImgHuxing;
    @BindView(R.id.frag_new_img_huxing_san)
    ImageView fragNewImgHuxingSan;
    @BindView(R.id.frag_new_img_huxing_ll)
    LinearLayout fragNewImgHuxingLl;
    @BindView(R.id.frag_new_img_mianji)
    TextView fragNewImgMianji;
    @BindView(R.id.frag_new_img_mianji_san)
    ImageView fragNewImgMianjiSan;
    @BindView(R.id.frag_new_img_mianji_ll)
    LinearLayout fragNewImgMianjiLl;
    @BindView(R.id.frag_new_img_yanse)
    TextView fragNewImgYanse;
    @BindView(R.id.frag_new_img_yanse_san)
    ImageView fragNewImgYanseSan;
    @BindView(R.id.frag_new_img_yanse_ll)
    LinearLayout fragNewImgYanseLl;
    @BindView(R.id.frag_new_img_tab)
    LinearLayout fragNewImgTab;
    @BindView(R.id.frag_new_img_recycler)
    RecyclerView fragNewImgRecycler;
    @BindView(R.id.frag_new_img_swipe)
    SwipeRefreshLayout fragNewImgSwipe;
    @BindView(R.id.frag_shardow_view)
    View fragShardowView;
    @BindView(R.id.frag_new_img_none_data_rl)
    RelativeLayout fragNewImgNoneDataRl;
    Unbinder unbinder;


    private Context mContext;
    private String TAG = "NewImageDFragment";
    private Gson mGson;//数据解析
    private int mPage = 1;//用于分页的数据
    private int mPageSize = 20;//用于分页的数据
    //数据源
    private ArrayList<_ImageD> mImageDArrayList = new ArrayList<>();
    //瀑布流的管理者
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    //瀑布流适配器
    private NewImageDAdapter mNewImageDAdapter;

    //赋值参数 用于数据的请求 每次切换数据都会变动的值
    private String mStyleParam = "";//风格的参数
    private String mLayoutParam = "";//户型的参数
    private String mAreaParam = "";//面积的参数
    private String mColorParam = "";//颜色的参数
    private boolean isDownRefresh = false;//是否是下拉刷新

    //回显用的位置数据
    private int mPositionStyle = 0;
    private int mPositionLayout = 0;
    private int mPositionArea = 0;
    private int mPositionColor = 0;

    //选择框的popwindow
    private GridView mGridView;
    private PopupWindow popupWindow;
    private View popView;//承载体
    //条件选择框的适配器
    private MyGridViewAdapter myGridViewAdapterStyle;//风格
    private MyGridViewAdapter myGridViewAdapterLayout;//户型
    private MyGridViewAdapter myGridViewAdapterArea;//面积
    private MyGridViewAdapter myGridViewAdapterColor;//颜色  颜色的适配器到时再想
    //弹窗的数据集合
    private ArrayList<_SelectMsg> popStyleList = new ArrayList<>();
    private ArrayList<_SelectMsg> popLayoutList = new ArrayList<>();
    private ArrayList<_SelectMsg> popAreaList = new ArrayList<>();
    private ArrayList<_SelectMsg> popColorList = new ArrayList<>();

    public NewImageDFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_image_d, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        return view;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.CLOSE_POP_WINDOW_IN_NEW_IMAGE_ACTIVITY:
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;
        }
    }

    //初始化view事件
    private void initView() {
        mGson = new Gson();
        //下拉刷新配置
        fragNewImgSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        fragNewImgSwipe.setBackgroundColor(Color.WHITE);
        fragNewImgSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        fragNewImgSwipe.setOnRefreshListener(onRefreshListener);
        //配置列表信息
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        fragNewImgRecycler.setLayoutManager(mStaggeredGridLayoutManager);
        ((SimpleItemAnimator) fragNewImgRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        fragNewImgRecycler.setOnTouchListener(onTouchListener);
        fragNewImgRecycler.addOnScrollListener(onScrollListener);//上拉加载更多
        //初始化popview
        popView = getActivity().getLayoutInflater().inflate(R.layout.pop_window_layout, null);
        mGridView = (GridView) popView.findViewById(R.id.pop_window_show);
        //请求筛选条件数据
        HttpGetSelectData();
        //请求数据
        HttpGetImageList(mPage);

    }

    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int[] lastVisiableItems = mStaggeredGridLayoutManager.findLastVisibleItemPositions(new int[mStaggeredGridLayoutManager.getSpanCount()]);
            int lastVisiableItem = getMaxElem(lastVisiableItems);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 5 >= mStaggeredGridLayoutManager.getItemCount()) {
                LoadMore();
            }
        }
    };

    //加载更多数据
    private void LoadMore() {
        mPage++;
        HttpGetImageList(mPage);
    }

    //用于判断角标最大者
    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    //touch事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (fragNewImgSwipe.isRefreshing()) {
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
            //处理下拉刷新的逻辑
            initData();
        }
    };

    //刷新(更新)数据
    private void initData() {
        isDownRefresh = true;
        mPage = 1;
        if (!mImageDArrayList.isEmpty()) {
            mImageDArrayList.clear();
        }
        HttpGetImageList(mPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.frag_new_img_fengge_ll, R.id.frag_new_img_huxing_ll, R.id.frag_new_img_mianji_ll, R.id.frag_new_img_yanse_ll})
    public void onViewClickedInNewImageDFragment(View view) {
        switch (view.getId()) {
            case R.id.frag_new_img_fengge_ll:
                //点击风格按钮出现风格选择框
                showPopSelect(0);
                break;
            case R.id.frag_new_img_huxing_ll:
                //点击户型按钮出现风格选择框
                showPopSelect(1);
                break;
            case R.id.frag_new_img_mianji_ll:
                //点击面积按钮出现风格选择框
                showPopSelect(2);
                break;
            case R.id.frag_new_img_yanse_ll:
                //点击颜色按钮出现风格选择框
                showPopSelect(3);
                break;
        }
    }

    //点击条件的选择框显示条件选择的页面 0--风格  1--户型  2--面积  3--颜色
    private void showPopSelect(int witch) {
        switch (witch) {
            case 0:
                /**
                 * 0--风格 style
                 * 选中触发的事件
                 * 1.按钮旁边的箭头的变化
                 * 2.选中的按钮字体颜色的变化
                 */
                Glide.with(mContext).load(R.drawable.img_san_up).into(fragNewImgFenggeSan);
                fragNewImgFengge.setTextColor(Color.parseColor("#ff882e"));
//                if (myGridViewAdapterStyle == null) {
                myGridViewAdapterStyle = new MyGridViewAdapter(mContext, popStyleList, mPositionStyle);
                mGridView.setAdapter(myGridViewAdapterStyle);
//                }
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (popStyleList.get(position).getId().equals("0")) {
                            //用户选择了不限的按钮
                            fragNewImgFengge.setText("风格");
                        } else {
                            fragNewImgFengge.setText(popStyleList.get(position).getName());
                        }
                        //改变参数 以及显示的位置
                        mPositionStyle = position;
                        mStyleParam = popStyleList.get(position).getId();
                        initData();
                        popupWindow.dismiss();
                    }
                });
                break;
            case 1:
                /**
                 * 1--户型  layout
                 * 选中触发的事件
                 * 1.按钮旁边的箭头的变化
                 * 2.选中的按钮字体颜色的变化
                 */
                Glide.with(mContext).load(R.drawable.img_san_up).into(fragNewImgHuxingSan);
                fragNewImgHuxing.setTextColor(Color.parseColor("#ff882e"));
                myGridViewAdapterLayout = new MyGridViewAdapter(mContext, popLayoutList, mPositionLayout);
                mGridView.setAdapter(myGridViewAdapterLayout);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (popLayoutList.get(position).getId().equals("0")) {
                            //用户选择了不限的按钮
                            fragNewImgHuxing.setText("户型");
                        } else {
                            fragNewImgHuxing.setText(popLayoutList.get(position).getName());
                        }
                        //改变参数 以及位置
                        mPositionLayout = position;
                        mLayoutParam = popLayoutList.get(position).getId();
                        popupWindow.dismiss();
                        initData();
                    }
                });
                break;
            case 2:
                /**
                 * 2--面积  area
                 * 选中触发的事件
                 * 1.按钮旁边的箭头的变化
                 * 2.选中的按钮字体颜色的变化
                 * 3.选择之后的选框文字
                 * 4.重新请求数据
                 */
                Glide.with(mContext).load(R.drawable.img_san_up).into(fragNewImgMianjiSan);
                fragNewImgMianji.setTextColor(Color.parseColor("#ff882e"));
                myGridViewAdapterArea = new MyGridViewAdapter(mContext, popAreaList, mPositionArea);
                mGridView.setAdapter(myGridViewAdapterArea);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (popAreaList.get(position).getId().equals("0")) {
                            //用户选择了不限的按钮
                            fragNewImgMianji.setText("面积");
                        } else {
                            fragNewImgMianji.setText(popAreaList.get(position).getName());
                        }
                        //改变参数
                        mPositionArea = position;
                        mAreaParam = popAreaList.get(position).getId();
                        popupWindow.dismiss();
                        initData();
                    }
                });
                break;
            case 3:
                /**
                 * 3--颜色  color
                 * 选中触发的事件
                 * 1.按钮旁边的箭头的变化
                 * 2.选中的按钮字体颜色的变化
                 * 3.选择之后的选框文字
                 * 4.重新请求数据
                 */
                Glide.with(mContext).load(R.drawable.img_san_up).into(fragNewImgYanseSan);
                fragNewImgYanse.setTextColor(Color.parseColor("#ff882e"));
                myGridViewAdapterColor = new MyGridViewAdapter(mContext, popColorList, mPositionColor);
                mGridView.setAdapter(myGridViewAdapterColor);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (popColorList.get(position).getId().equals("0")) {
                            //用户选择了不限的按钮
                            fragNewImgYanse.setText("颜色");
                        } else {
                            fragNewImgYanse.setText(popColorList.get(position).getName());
                        }
                        //改变参数
                        mPositionColor = position;
                        mColorParam = popColorList.get(position).getId();
                        popupWindow.dismiss();
                        initData();
                    }
                });
                break;

        }
        fragShardowView.setVisibility(View.VISIBLE);
        //在这之前要完成GridView的布局
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f3f2")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAsDropDown(fragNewImgTab);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                fragShardowView.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fragNewImgFenggeSan);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fragNewImgHuxingSan);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fragNewImgMianjiSan);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fragNewImgYanseSan);
                fragNewImgFengge.setTextColor(Color.parseColor("#808080"));
                fragNewImgHuxing.setTextColor(Color.parseColor("#808080"));
                fragNewImgMianji.setTextColor(Color.parseColor("#808080"));
                fragNewImgYanse.setTextColor(Color.parseColor("#808080"));
            }
        });
    }

    //网络获取类型数据
    private void HttpGetSelectData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        OKHttpUtil.post(Constant.SUITE_CLASS, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "数据获取成功=============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = jsonObject.optInt("status");
                    if (status == 200) {
                        //数据链接成功 解析数据 判断数据和本地对比是否正确 之后将数据缓存
                        String data = jsonObject.optString("data");
                        if (AppInfoUtil.getSelectMsg(mContext).equals(data)) {
                            //数据一致不做处理
                            Log.e(TAG, "缓存数据和服务器一致=======" + data);
                            Log.e(TAG, "缓存一致,获取本地的缓存数据=======" + AppInfoUtil.getSelectMsg(mContext));
                            //添加筛选条件数据
                            initPopSelectList();
                        } else {
                            //数据不一致将数据重新缓存
                            AppInfoUtil.setSelectMsg(mContext, data);
                            Log.e(TAG, "缓存数据和服务器不一致=======" + data);
                            Log.e(TAG, "缓存不一致，获取重新缓存的数据=======" + AppInfoUtil.getSelectMsg(mContext));
                            //添加筛选条件数据
                            initPopSelectList();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //拿到缓存的数据将其添加到列表在
    private void initPopSelectList() {
        //添加前清空数据
        if (!popAreaList.isEmpty()) {
            popAreaList.clear();
        }
        if (!popColorList.isEmpty()) {
            popColorList.clear();
        }
        if (!popLayoutList.isEmpty()) {
            popLayoutList.clear();
        }
        if (!popStyleList.isEmpty()) {
            popStyleList.clear();
        }
        //添加数据
        String jsonData = AppInfoUtil.getSelectMsg(mContext);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            //风格的数据
            JSONArray styleArray = jsonObject.optJSONArray("style");
            for (int i = 0; i < styleArray.length(); i++) {
                _SelectMsg styleMsg = mGson.fromJson(styleArray.get(i).toString(), _SelectMsg.class);
                popStyleList.add(styleMsg);
            }
            //户型的数据
            JSONArray layoutArray = jsonObject.optJSONArray("layout");
            for (int i = 0; i < layoutArray.length(); i++) {
                _SelectMsg layoutMsg = mGson.fromJson(layoutArray.get(i).toString(), _SelectMsg.class);
                popLayoutList.add(layoutMsg);
            }
            //面积的数据
            JSONArray areaArray = jsonObject.optJSONArray("area");
            for (int i = 0; i < areaArray.length(); i++) {
                _SelectMsg areaMsg = mGson.fromJson(areaArray.get(i).toString(), _SelectMsg.class);
                popAreaList.add(areaMsg);
            }
            //颜色数据
            JSONArray colorArray = jsonObject.optJSONArray("color");
            for (int i = 0; i < colorArray.length(); i++) {
                _SelectMsg colorMsg = mGson.fromJson(colorArray.get(i).toString(), _SelectMsg.class);
                popColorList.add(colorMsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //网络获取数据列表
    private void HttpGetImageList(int mPage) {
        //隐藏没有数据的蒙层
        fragNewImgNoneDataRl.setVisibility(View.GONE);
        //下拉刷新停止
        fragNewImgSwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        //风格
        if (!TextUtils.isEmpty(mStyleParam)) {
            //设置的选择条件不为空时加入参数数据
            Log.e(TAG, "获取请求参数====mStyleParam====" + mStyleParam);
            param.put("style_id", mStyleParam);
        }
        //户型
        if (!TextUtils.isEmpty(mLayoutParam)) {
            //设置的选择条件不为空时加入参数数据
            param.put("layout_id", mLayoutParam);
        }
        //面积
        if (!TextUtils.isEmpty(mAreaParam)) {
            //设置的选择条件不为空时加入参数数据
            param.put("area_id", mAreaParam);
        }
        //颜色
        if (!TextUtils.isEmpty(mColorParam)) {
            //设置的选择条件不为空时加入参数数据
            param.put("color_id", mColorParam);
        }
        //用户的id
        if (!TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
            param.put("uid", AppInfoUtil.getUserid(mContext));
        }
        //用户的类型
        if (!TextUtils.isEmpty(AppInfoUtil.getTypeid(mContext))) {
            param.put("user_type", AppInfoUtil.getTypeid(mContext));
        }
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.SUITE_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "数据链接失败=============" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //数据链接成功
                String json = new String(response.body().string());
                Log.e(TAG, "数据链接成功==============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = jsonObject.optInt("status");
                    if (status == 200) {
                        //数据请求正确
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _ImageD imageD = mGson.fromJson(jsonArray.get(i).toString(), _ImageD.class);
                            mImageDArrayList.add(imageD);
                        }
                        //数据集合获取成之后开始布局
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mNewImageDAdapter == null) {
                                    mNewImageDAdapter = new NewImageDAdapter(mContext, mImageDArrayList);
                                    mNewImageDAdapter.setOnNewImageAdapterClickLister(onNewImageAdapterClickLister);
                                    fragNewImgRecycler.setAdapter(mNewImageDAdapter);
                                    mNewImageDAdapter.notifyDataSetChanged();
                                } else {
                                    if (isDownRefresh) {
                                        isDownRefresh = false;
                                        fragNewImgRecycler.scrollToPosition(0);
                                        mNewImageDAdapter.notifyDataSetChanged();
                                    } else {
                                        mNewImageDAdapter.notifyItemInserted(mImageDArrayList.size() - mPageSize);
                                    }

                                }
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "当前没有更多数据", Toast.LENGTH_SHORT).show();
                                //当某一个条件组合没有数据时的操作
                                if (mImageDArrayList.isEmpty()) {
                                    if (mNewImageDAdapter != null) {
                                        mNewImageDAdapter.notifyDataSetChanged();
                                    }
                                    //显示没有搜索到任何数据的占位图
                                    fragNewImgNoneDataRl.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //列表子项点击事件
    private NewImageDAdapter.OnNewImageAdapterClickLister onNewImageAdapterClickLister = new NewImageDAdapter.OnNewImageAdapterClickLister() {
        @Override
        public void onNewImageAdapterClick(View view, int position) {
            switch (view.getId()) {
                case R.id.item_new_image_shoucan_icon:
                case R.id.item_new_image_shoucan_icon_ll:
                    if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
                        //用户未登录 跳转到登录页面
                        Toast.makeText(mContext, "您还没有登陆,请登陆后再来收藏!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("isFav", true);
                        startActivityForResult(intent, 0);
                    } else {
                        HttpCollection(mImageDArrayList.get(position).getId(), AppInfoUtil.getUserid(mContext), AppInfoUtil.getTypeid(mContext), position);
                    }
                    break;
            }
        }
    };

    //收藏或者取消收藏请求
    private void HttpCollection(String id, String uid, String user_type, final int position) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("state", mImageDArrayList.get(position).getIs_collect());
        param.put("id", id);//套图或者单图的id
        param.put("uid", uid);//用户id
        param.put("user_type", user_type);//用户类型
        Log.e(TAG, "用户的类型================" + user_type);
        param.put("type", 2);//收藏图片的类型
        OKHttpUtil.post(Constant.IMAGE_COLLECT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=======" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "点赞成功=============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = jsonObject.optInt("status");
                    if (status == 200) {
                        final String msg = jsonObject.optString("msg");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //处理显示逻辑
                                if (msg.equals("收藏成功")) {
                                    //收藏成功将当前的数据模型改变
                                    mImageDArrayList.get(position).setIs_collect("1");//修改收藏状态
                                    //之前的收藏数量
                                    int collectNum = Integer.parseInt(mImageDArrayList.get(position).getCollect_count());
                                    mImageDArrayList.get(position).setCollect_count("" + (collectNum + 1));//修改收藏数量
                                    mNewImageDAdapter.notifyItemChanged(position);
                                } else {
                                    //改变当前的数据模型 取消收藏状态
                                    mImageDArrayList.get(position).setIs_collect("0");//修改收藏状态
                                    //之前的收藏数量
                                    int collectNum = Integer.parseInt(mImageDArrayList.get(position).getCollect_count());
                                    mImageDArrayList.get(position).setCollect_count("" + (collectNum - 1));//修改收藏数量
                                    mNewImageDAdapter.notifyItemChanged(position);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
