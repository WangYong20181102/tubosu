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
import com.tbs.tobosutype.activity.SImageLookingActivity;
import com.tbs.tobosutype.adapter.MyGridViewAdapter;
import com.tbs.tobosutype.adapter.NewImageSAdapter;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._ImageS;
import com.tbs.tobosutype.bean._SelectMsg;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.SpUtil;
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
 * Created by Mr.Lin on 2017/11/14 10:10.
 * 单图的fragment   imgesign
 * 作用于3.5版本逛图库的单图显示
 */

public class NewImageSFragment extends BaseFragment {
    @BindView(R.id.frag_new_img_single_recycler)
    RecyclerView fragNewImgSingleRecycler;
    @BindView(R.id.frag_new_img_single_swipe)
    SwipeRefreshLayout fragNewImgSingleSwipe;
    @BindView(R.id.frag_shardow_view)
    View fragShardowView;
    @BindView(R.id.frag_new_img_single_none_data_rl)
    RelativeLayout fragNewImgSingleNoneDataRl;
    @BindView(R.id.frag_new_img_single_fengge)
    TextView fragNewImgSingleFengge;
    @BindView(R.id.frag_new_img_single_fengge_san)
    ImageView fragNewImgSingleFenggeSan;
    @BindView(R.id.frag_new_img_single_fengge_ll)
    LinearLayout fragNewImgSingleFenggeLl;
    @BindView(R.id.frag_new_img_single_kongjian)
    TextView fragNewImgSingleKongjian;
    @BindView(R.id.frag_new_img_single_kongjian_san)
    ImageView fragNewImgSingleKongjianSan;
    @BindView(R.id.frag_new_img_single_kongjian_ll)
    LinearLayout fragNewImgSingleKongjianLl;
    @BindView(R.id.frag_new_img_single_jubu)
    TextView fragNewImgSingleJubu;
    @BindView(R.id.frag_new_img_single_jubu_san)
    ImageView fragNewImgSingleJubuSan;
    @BindView(R.id.frag_new_img_single_jubu_ll)
    LinearLayout fragNewImgSingleJubuLl;
    @BindView(R.id.frag_new_img_single_yanse)
    TextView fragNewImgSingleYanse;
    @BindView(R.id.frag_new_img_single_yanse_san)
    ImageView fragNewImgSingleYanseSan;
    @BindView(R.id.frag_new_img_single_yanse_ll)
    LinearLayout fragNewImgSingleYanseLl;
    @BindView(R.id.frag_new_img_single_tab)
    LinearLayout fragNewImgSingleTab;
    Unbinder unbinder;
    private Context mContext;
    private String TAG = "NewImageSFragment";
    private Gson mGson;
    private int mPage = 1;
    private int mPageSize = 20;
    //数据源
    private ArrayList<_ImageS> mImageSArrayList = new ArrayList<>();
    //瀑布流的管理者
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    //瀑布流的适配器
    private NewImageSAdapter mNewImageSAdapter;
    //赋值参数 用于数据的请求 每次切换数据都会变动的值
    private String mStyleParam = "";//风格的参数
    private String mSpaceParam = "";//空间的参数
    private String mPartParam = "";//局部的参数
    private String mColorParam = "";//颜色的参数
    private boolean isDownRefresh = false;//是否是下拉刷新
    private boolean isLoadingData = false;//列表集合是否在加载数据
    //回显用的位置数据
    private int mPositionStyle = 0;
    private int mPositionSpace = 0;
    private int mPositionPart = 0;
    private int mPositionColor = 0;
    //选择框的popwindow
    private GridView mGridView;
    private PopupWindow popupWindow;
    private View popView;//承载体
    //条件选择框的适配器
    private MyGridViewAdapter myGridViewAdapterStyle;//风格
    private MyGridViewAdapter myGridViewAdapterSpace;//空间
    private MyGridViewAdapter myGridViewAdapterPart;//局部
    private MyGridViewAdapter myGridViewAdapterColor;//颜色
    //弹窗的数据集合
    private ArrayList<_SelectMsg> popStyleList = new ArrayList<>();
    private ArrayList<_SelectMsg> popSpaceList = new ArrayList<>();
    private ArrayList<_SelectMsg> popPartList = new ArrayList<>();
    private ArrayList<_SelectMsg> popColorList = new ArrayList<>();

    public NewImageSFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_image_s, null);
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
            case EC.EventCode.NOTIF_SHOUCANG_DATA_CHANGE_IS_COLLECT:
                //修改为已收藏状态
                mImageSArrayList.get((int) event.getData()).setIs_collect("1");
                break;
            case EC.EventCode.NOTIF_SHOUCANG_DATA_CHANGE_IS_NOT_COLLECT:
                //修改为未收藏状态
                mImageSArrayList.get((int) event.getData()).setIs_collect("0");
                break;
        }
    }

    private void initView() {
        mGson = new Gson();
        //下拉刷新配置
        fragNewImgSingleSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        fragNewImgSingleSwipe.setBackgroundColor(Color.WHITE);
        fragNewImgSingleSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        fragNewImgSingleSwipe.setOnRefreshListener(onRefreshListener);
        //配置列表信息
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        fragNewImgSingleRecycler.setLayoutManager(mStaggeredGridLayoutManager);
        ((SimpleItemAnimator) fragNewImgSingleRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        fragNewImgSingleRecycler.setOnTouchListener(onTouchListener);
        fragNewImgSingleRecycler.addOnScrollListener(onScrollListener);//上拉加载更多
        //初始化popview
        popView = getActivity().getLayoutInflater().inflate(R.layout.pop_window_layout, null);
        mGridView = (GridView) popView.findViewById(R.id.pop_window_show);
        //请求筛选条件数据
        HttpGetSelectData();
        //请求数据
        HttpGetSingleImageList(mPage);
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
        HttpGetSingleImageList(mPage);
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
            if (fragNewImgSingleSwipe.isRefreshing()) {
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
        if (!mImageSArrayList.isEmpty()) {
            mImageSArrayList.clear();
        }
        HttpGetSingleImageList(mPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.frag_new_img_single_fengge_ll, R.id.frag_new_img_single_kongjian_ll, R.id.frag_new_img_single_jubu_ll, R.id.frag_new_img_single_yanse_ll})
    public void onViewClickedInNewImageSFragment(View view) {
        switch (view.getId()) {
            case R.id.frag_new_img_single_fengge_ll:
                //风格按钮弹窗
                showPopSelect(0);
                break;
            case R.id.frag_new_img_single_kongjian_ll:
                //空间按钮弹窗
                showPopSelect(1);
                break;
            case R.id.frag_new_img_single_jubu_ll:
                //局部按钮弹窗
                showPopSelect(2);
                break;
            case R.id.frag_new_img_single_yanse_ll:
                //颜色按钮弹窗
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
                Glide.with(mContext).load(R.drawable.img_san_up).into(fragNewImgSingleFenggeSan);
                fragNewImgSingleFengge.setTextColor(Color.parseColor("#ff882e"));
//                if (myGridViewAdapterStyle == null) {
                myGridViewAdapterStyle = new MyGridViewAdapter(mContext, popStyleList, mPositionStyle);
                mGridView.setAdapter(myGridViewAdapterStyle);
//                }
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (popStyleList.get(position).getId().equals("0")) {
                            //用户选择了不限的按钮
                            fragNewImgSingleFengge.setText("风格");
                        } else {
                            fragNewImgSingleFengge.setText(popStyleList.get(position).getName());
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
                 * 1--空间  space
                 * 选中触发的事件
                 * 1.按钮旁边的箭头的变化
                 * 2.选中的按钮字体颜色的变化
                 */
                Glide.with(mContext).load(R.drawable.img_san_up).into(fragNewImgSingleKongjianSan);
                fragNewImgSingleKongjian.setTextColor(Color.parseColor("#ff882e"));
                myGridViewAdapterSpace = new MyGridViewAdapter(mContext, popSpaceList, mPositionSpace);
                mGridView.setAdapter(myGridViewAdapterSpace);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (popSpaceList.get(position).getId().equals("0")) {
                            //用户选择了不限的按钮
                            fragNewImgSingleKongjian.setText("空间");
                        } else {
                            fragNewImgSingleKongjian.setText(popSpaceList.get(position).getName());
                        }
                        //改变参数 以及位置
                        mPositionSpace = position;
                        mSpaceParam = popSpaceList.get(position).getId();
                        popupWindow.dismiss();
                        initData();
                    }
                });
                break;
            case 2:
                /**
                 * 2--局部  part
                 * 选中触发的事件
                 * 1.按钮旁边的箭头的变化
                 * 2.选中的按钮字体颜色的变化
                 * 3.选择之后的选框文字
                 * 4.重新请求数据
                 */
                Glide.with(mContext).load(R.drawable.img_san_up).into(fragNewImgSingleJubuSan);
                fragNewImgSingleJubu.setTextColor(Color.parseColor("#ff882e"));
                myGridViewAdapterPart = new MyGridViewAdapter(mContext, popPartList, mPositionPart);
                mGridView.setAdapter(myGridViewAdapterPart);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (popPartList.get(position).getId().equals("0")) {
                            //用户选择了不限的按钮
                            fragNewImgSingleJubu.setText("局部");
                        } else {
                            fragNewImgSingleJubu.setText(popPartList.get(position).getName());
                        }
                        //改变参数
                        mPositionPart = position;
                        mPartParam = popPartList.get(position).getId();
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
                Glide.with(mContext).load(R.drawable.img_san_up).into(fragNewImgSingleYanseSan);
                fragNewImgSingleYanse.setTextColor(Color.parseColor("#ff882e"));
                myGridViewAdapterColor = new MyGridViewAdapter(mContext, popColorList, mPositionColor);
                mGridView.setAdapter(myGridViewAdapterColor);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (popColorList.get(position).getId().equals("0")) {
                            //用户选择了不限的按钮
                            fragNewImgSingleYanse.setText("颜色");
                        } else {
                            fragNewImgSingleYanse.setText(popColorList.get(position).getName());
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
        popupWindow.showAsDropDown(fragNewImgSingleTab);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                fragShardowView.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fragNewImgSingleFenggeSan);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fragNewImgSingleKongjianSan);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fragNewImgSingleJubuSan);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fragNewImgSingleYanseSan);
                fragNewImgSingleFengge.setTextColor(Color.parseColor("#808080"));
                fragNewImgSingleKongjian.setTextColor(Color.parseColor("#808080"));
                fragNewImgSingleJubu.setTextColor(Color.parseColor("#808080"));
                fragNewImgSingleYanse.setTextColor(Color.parseColor("#808080"));
            }
        });
    }

    //网络获取类型数据
    private void HttpGetSelectData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        OKHttpUtil.post(Constant.SINGLE_CLASS, param, new Callback() {
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
                        if (AppInfoUtil.getSingleSelectMsg(mContext).equals(data)) {
                            //数据一致不做处理
                            Log.e(TAG, "缓存数据和服务器一致=======" + data);
                            Log.e(TAG, "缓存一致,获取本地的缓存数据=======" + AppInfoUtil.getSingleSelectMsg(mContext));
                            //添加筛选条件数据
                            initPopSelectList();
                        } else {
                            //数据不一致将数据重新缓存
                            AppInfoUtil.setSingleSelectMsg(mContext, data);
                            Log.e(TAG, "缓存数据和服务器不一致=======" + data);
                            Log.e(TAG, "缓存不一致，获取重新缓存的数据=======" + AppInfoUtil.getSingleSelectMsg(mContext));
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
        if (!popSpaceList.isEmpty()) {
            popSpaceList.clear();
        }
        if (!popColorList.isEmpty()) {
            popColorList.clear();
        }
        if (!popPartList.isEmpty()) {
            popPartList.clear();
        }
        if (!popStyleList.isEmpty()) {
            popStyleList.clear();
        }
        //添加数据
        String jsonData = AppInfoUtil.getSingleSelectMsg(mContext);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            //风格的数据
            JSONArray styleArray = jsonObject.optJSONArray("style");
            for (int i = 0; i < styleArray.length(); i++) {
                _SelectMsg styleMsg = mGson.fromJson(styleArray.get(i).toString(), _SelectMsg.class);
                popStyleList.add(styleMsg);
            }
            //户型的数据
            JSONArray layoutArray = jsonObject.optJSONArray("space");
            for (int i = 0; i < layoutArray.length(); i++) {
                _SelectMsg spaceMsg = mGson.fromJson(layoutArray.get(i).toString(), _SelectMsg.class);
                popSpaceList.add(spaceMsg);
            }
            //面积的数据
            JSONArray areaArray = jsonObject.optJSONArray("part");
            for (int i = 0; i < areaArray.length(); i++) {
                _SelectMsg partMsg = mGson.fromJson(areaArray.get(i).toString(), _SelectMsg.class);
                popPartList.add(partMsg);
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
    private void HttpGetSingleImageList(int mPage) {
        //正在加载数据
        isLoadingData = true;
        //隐藏没有数据的蒙层
        fragNewImgSingleNoneDataRl.setVisibility(View.GONE);
        //下拉刷新停止
        fragNewImgSingleSwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        //风格
        if (!TextUtils.isEmpty(mStyleParam)) {
            //设置的选择条件不为空时加入参数数据
            Log.e(TAG, "获取请求参数==风格==mStyleParam====" + mStyleParam);
            param.put("style_id", mStyleParam);
        }
        //kongjian
        if (!TextUtils.isEmpty(mSpaceParam)) {
            Log.e(TAG, "获取请求参数==空间==mSpaceParam====" + mSpaceParam);
            //设置的选择条件不为空时加入参数数据
            param.put("space_id", mSpaceParam);
        }
        //局部
        if (!TextUtils.isEmpty(mPartParam)) {
            //设置的选择条件不为空时加入参数数据
            Log.e(TAG, "获取请求参数==空间==mPartParam====" + mPartParam);
            param.put("part_id", mPartParam);
        }
        //颜色
        if (!TextUtils.isEmpty(mColorParam)) {
            //设置的选择条件不为空时加入参数数据
            Log.e(TAG, "获取请求参数==空间==mColorParam====" + mColorParam);
            param.put("color_id", mColorParam);
        }
        if (!TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
            //传入UID
            param.put("uid", AppInfoUtil.getUserid(mContext));
        }
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.SINGLE_MAP_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "数据链接失败=============" + e.getMessage());
                isLoadingData = false;//数据加载完成
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
                            _ImageS imageD = mGson.fromJson(jsonArray.get(i).toString(), _ImageS.class);
                            mImageSArrayList.add(imageD);
                        }
                        //数据集合获取成之后开始布局
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mNewImageSAdapter == null) {
                                    mNewImageSAdapter = new NewImageSAdapter(mContext, mImageSArrayList);
                                    mNewImageSAdapter.setOnImgaeSClickLister(onImgaeSClickLister);
                                    fragNewImgSingleRecycler.setAdapter(mNewImageSAdapter);
                                    mNewImageSAdapter.notifyDataSetChanged();
                                } else {
                                    if (isDownRefresh) {
                                        isDownRefresh = false;
                                        fragNewImgSingleRecycler.scrollToPosition(0);
                                        mNewImageSAdapter.notifyDataSetChanged();
                                    } else {
                                        mNewImageSAdapter.notifyItemInserted(mImageSArrayList.size() - mPageSize);
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
                                if (mImageSArrayList.isEmpty()) {
                                    if (mImageSArrayList != null) {
                                        mNewImageSAdapter.notifyDataSetChanged();
                                    }
                                    //显示没有搜索到任何数据的占位图
                                    fragNewImgSingleNoneDataRl.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    isLoadingData = false;
                }
            }
        });
    }

    private NewImageSAdapter.OnImgaeSClickLister onImgaeSClickLister = new NewImageSAdapter.OnImgaeSClickLister() {
        @Override
        public void onImageSClick(View view, int position) {
            //点击进入详情
            switch (view.getId()) {
                case R.id.item_new_image_s_img_click_view:
                    //进入详情  数据不在加载的情况下点击进入下一页
                    if (!isLoadingData) {
                        Log.e(TAG, "点击单图进入详情==============");
                        String mSImageListJson = mGson.toJson(mImageSArrayList);
                        SpUtil.setSingImageListJson(mContext, mSImageListJson);
                        Intent intent = new Intent(mContext, SImageLookingActivity.class);
                        intent.putExtra("mPosition", position);
                        intent.putExtra("mWhereFrom", "NewImageSFragment");
                        mContext.startActivity(intent);
                    } else {
                        Log.e(TAG, "正在加载数据，无法进入下一个页面！");
                    }
                    break;
            }
        }
    };
}
