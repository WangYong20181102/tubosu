package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import com.tbs.tobosutype.adapter.ImageNewActivityAdapter;
import com.tbs.tobosutype.adapter.MyGridViewAdapter;
import com.tbs.tobosutype.adapter.MyGridViewAdapters;
import com.tbs.tobosutype.bean._ImageItem;
import com.tbs.tobosutype.bean._Style;
import com.tbs.tobosutype.customview.CustomWaitDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImageNewActivity extends BaseActivity {
    /**
     * 逛图库中显示当地装修公司列表的接口
     */
    private String getListUrl = Constant.TOBOSU_URL + "tapp/impression/get_list";

    /**
     * 逛图库顶部广告位接口
     */
    private String adsenseUrl = Constant.TOBOSU_URL + "tapp/util/adsense";

    /**
     * 上个月冠亚季军三个公司
     */
    private String getLastMonthCompanyListUrl = Constant.TOBOSU_URL + "tapp/impression/getLastMonthCompanyList";

    /**
     * 搜索逛图库 ，输入小区名称
     */
    private String searchCommunityUrl = Constant.TOBOSU_URL + "tapp/impression/searchCommunity";
    /**
     * 获取风格户型面积
     */
    private String getStytleUrl = Constant.TOBOSU_URL + "tapp/impression/style";

    private Context mContext;
    private String TAG = "ImageNewActivity";
    private Map<String, String> LoadMoreParam = new HashMap<>();//用户选择了条件查询 暂存对象
    private int page = 1;//获取数据时所要用的页面数

    private boolean isUpRefresh;//是否处于上拉状态
    private HashMap<String, Object> getImgItemListParams;//获取图库列表的参数
    private HashMap<String, Object> getStyleListParams;//获取条件筛选的参数

    private View viewLoading;//loading图
    private View viewNetOut;//网络断开图
    private CustomWaitDialog customWaitDialog;
    //下拉列表按钮的父控件
    private RelativeLayout imgNewReLL;
    //下拉列表按钮
    private TextView HuxingTv;
    private TextView FenggeTv;
    private TextView MianjiTv;
    //下拉列表按钮指示箭头
    private ImageView imgNewMianji_jt;
    private ImageView imgNewFengge_jt;
    private ImageView imgNewHuxing_jt;

    private PopupWindow popupWindow;//显示菜单的泡泡window
    private View popView;//承载pop自定义样式的布局
    private GridView mGridView;//显示的网格布局
    private MyGridViewAdapters myGridViewAdapterFg;//网格布局的适配器
    private MyGridViewAdapters myGridViewAdapterHx;//网格布局的适配器
    private MyGridViewAdapters myGridViewAdapterMj;//网格布局的适配器
    //显示咨询的popwindows
    private PopupWindow zixunPopupWindow;
    private View zixunPopView;
    //pop风格窗口按钮数据
    private List<_Style> popBtnListFg = new ArrayList<_Style>();
    //pop户型窗口按钮数据
    private List<_Style> popBtnListHx = new ArrayList<_Style>();
    //pop面积窗口按钮数据
    private List<_Style> popBtnListMj = new ArrayList<_Style>();


    private SwipeRefreshLayout imgNewSwipLayout;//下拉刷新控件

    private RecyclerView imgNewRecycleView;//列表
    private GridLayoutManager mGridLayoutManager;//列表管理者
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private List<_ImageItem> tempList = new ArrayList<_ImageItem>();//临时装载容器 用于多次装载
    private List<_ImageItem> imageItemsList = new ArrayList<_ImageItem>();//装载数据的容器
    private ImageNewActivityAdapter imageNewActivityAdapter;//列表适配器

    private TextView img_new_design;//免费设计的按钮
    private ImageView img_new_zixun;//咨询按钮
    private ImageView img_new_design_gif;//免费设计gif图
    private RelativeLayout img_new_banner;
    private LinearLayout img_new_zixun_ll;

    private ImageView huxing_down;
    private ImageView fengge_down;
    private ImageView mianji_down;
    private RelativeLayout img_not_found_rl;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_new);
        mContext = ImageNewActivity.this;
        mGson = new Gson();
        getStyleListParams = new HashMap<String, Object>();
        getImgItemListParams = AppInfoUtil.getPublicHashMapParams(mContext);
        customWaitDialog = new CustomWaitDialog(mContext);
        initCache();
        bindView();
        initView();
        getImageListData();
    }

    //绑定布局
    private void bindView() {

        imgNewReLL = (RelativeLayout) findViewById(R.id.imgNewReLL);
        HuxingTv = (TextView) findViewById(R.id.imgNewHuxing);
        FenggeTv = (TextView) findViewById(R.id.imgNewFengge);
        MianjiTv = (TextView) findViewById(R.id.imgNewMianji);

        imgNewMianji_jt = (ImageView) findViewById(R.id.imgNewMianji_jt);
        imgNewFengge_jt = (ImageView) findViewById(R.id.imgNewFengge_jt);
        imgNewHuxing_jt = (ImageView) findViewById(R.id.imgNewHuxing_jt);

        viewLoading = findViewById(R.id.imgNewLoading);
        viewNetOut = findViewById(R.id.imgNetout);
        viewNetOut.findViewById(R.id.tv_reload_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "重新加载数据中", Toast.LENGTH_LONG).show();
                getImgItemListParams.remove("token");
                getImgItemListParams.remove("pageSize");
                customWaitDialog.show();
                getImageListData();
            }
        });

        imgNewSwipLayout = (SwipeRefreshLayout) findViewById(R.id.imgNewSwipLayout);
        imgNewRecycleView = (RecyclerView) findViewById(R.id.imgNewRecycleView);
        //获取泡泡布局
        popView = ImageNewActivity.this.getLayoutInflater().inflate(R.layout.pop_window_layout, null);
        mGridView = (GridView) popView.findViewById(R.id.pop_window_show);

        //免费设计
        img_new_design = (TextView) findViewById(R.id.img_new_design);
        //咨询
        img_new_zixun = (ImageView) findViewById(R.id.img_new_zixun);
        img_new_design_gif = (ImageView) findViewById(R.id.img_new_design_gif);
        img_new_banner = (RelativeLayout) findViewById(R.id.img_new_banner);
        img_new_zixun_ll = (LinearLayout) findViewById(R.id.img_new_zixun_ll);

        huxing_down = (ImageView) findViewById(R.id.huxing_down);
        mianji_down = (ImageView) findViewById(R.id.mianji_down);
        fengge_down = (ImageView) findViewById(R.id.fengge_down);

        img_not_found_rl = (RelativeLayout) findViewById(R.id.img_not_found_rl);
    }

    //初始化布局
    private void initView() {
        /**
         * 点击事件的注册
         */
        HuxingTv.setOnClickListener(occl);
        FenggeTv.setOnClickListener(occl);
        MianjiTv.setOnClickListener(occl);
        //免费设计
        img_new_design.setOnClickListener(occl);
        img_new_zixun.setOnClickListener(occl);
        img_new_zixun_ll.setOnClickListener(occl);
        img_new_design_gif.setOnClickListener(occl);
        Glide.with(mContext).load(R.drawable.img_new_design_gif).asGif().into(img_new_design_gif);

        /**
         * 下拉控件进行修饰 以及添加下拉事件
         */
        imgNewSwipLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);//下拉转圈圈颜色
        imgNewSwipLayout.setBackgroundColor(Color.WHITE);//整个界面的颜色  白色
        imgNewSwipLayout.setSize(SwipeRefreshLayout.DEFAULT);//圆圈的大小
        imgNewSwipLayout.setOnRefreshListener(swipeLister);

        /**
         * 瀑布流列表布局设置
         */
        mGridLayoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);//表格
        imgNewRecycleView.setLayoutManager(mGridLayoutManager);//设置显示的样式
//        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);//流式
//        imgNewRecycleView.setLayoutManager(mStaggeredGridLayoutManager);
        imgNewRecycleView.setOnScrollListener(scrollListener);//添加上拉加载
        imgNewRecycleView.setOnTouchListener(onTouchLis);
        imgNewRecycleView.getItemAnimator().setChangeDuration(0);
        img_new_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    //初始化图库的公共参数
    private void initImageListParams() {
        getImgItemListParams.put("token", AppInfoUtil.getToekn(this));
        getImgItemListParams.put("page", page + "");
        getImgItemListParams.put("pageSize", "20");
    }

    //初始化imageList
    private void getImageListData() {
        if (!imageItemsList.isEmpty()) {
            imageItemsList.clear();
        }
        page = 1;
        LoadMoreParam.clear();
        initImageListParams();
        HttpRequestGetImgItemList(getImgItemListParams);
    }

    private void initCache() {
//        if (TextUtils.isEmpty(AppInfoUtil.getStyleFgCache(mContext))
//                || TextUtils.isEmpty(AppInfoUtil.getStyleHxCache(mContext))
//                || TextUtils.isEmpty(AppInfoUtil.getStyleMjCache(mContext))) {
////            当App中的缓存为空时进行数据的请求
//            Log.e(TAG, "当前本地无缓存===" + AppInfoUtil.getStyleHxCache(mContext));
//            HttpRequestGetStyleList("7");
//            HttpRequestGetStyleList("9");
//            HttpRequestGetStyleList("12");
//        } else {
        //本地有缓存数据
        HttpRequestGetStyleList("7");
        HttpRequestGetStyleList("9");
        HttpRequestGetStyleList("12");
        Log.e(TAG, "当前本地有缓存===" + AppInfoUtil.getStyleHxCache(mContext));
        setAdapterListDatas(7);
        setAdapterListDatas(9);
        setAdapterListDatas(12);
//        }
    }

    /**
     * 处理缓存数据加入集合列表
     */
    private void setAdapterListDatas(int setType) {
        try {
            if (setType == 7) {
                if (!popBtnListFg.isEmpty()) {
                    popBtnListFg.clear();
                }
                String json = AppInfoUtil.getStyleFgCache(mContext);
                if (!"".equals(json)) {
                    JSONObject jsonObjectFg = new JSONObject(json);
                    JSONArray dataArrayFg = jsonObjectFg.getJSONArray("data");
                    for (int i = 0; i < dataArrayFg.length(); i++) {
                        _Style styleFg = new _Style(dataArrayFg.get(i).toString());
                        popBtnListFg.add(styleFg);
                    }
                }

            } else if (setType == 9) {
                if (!popBtnListHx.isEmpty()) {
                    popBtnListHx.clear();
                }
                String mJson = AppInfoUtil.getStyleHxCache(mContext);
                if (!"".equals(mJson)) {
                    JSONObject jsonObjectHx = new JSONObject(mJson);
                    JSONArray dataArrayHx = jsonObjectHx.getJSONArray("data");
                    for (int i = 0; i < dataArrayHx.length(); i++) {
                        _Style styleHx = new _Style(dataArrayHx.get(i).toString());
                        popBtnListHx.add(styleHx);
                    }
                }

            } else if (setType == 12) {
                if (!popBtnListMj.isEmpty()) {
                    popBtnListMj.clear();
                }
                String jsonm = AppInfoUtil.getStyleMjCache(mContext);
                if (!"".equals(jsonm)) {
                    JSONObject jsonObjectMj = new JSONObject(jsonm);
                    JSONArray dataArrayMj = jsonObjectMj.getJSONArray("data");
                    for (int i = 0; i < dataArrayMj.length(); i++) {
                        _Style styleMj = new _Style(dataArrayMj.get(i).toString());
                        popBtnListMj.add(styleMj);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下拉刷新事件
     */
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            imgNewSwipLayout.setRefreshing(true);
            initCache();
            getImageListData();
        }
    };
    private View.OnTouchListener onTouchLis = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (imgNewSwipLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }

        }
    };
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //瀑布流式用到的加载更多
//            int[] lastVisiableItems = mStaggeredGridLayoutManager
//                    .findLastVisibleItemPositions(new int[mStaggeredGridLayoutManager
//                            .getSpanCount()]);
//            int lastVisiableItem = getMaxElem(lastVisiableItems);
//            if (newState == RecyclerView.SCROLL_STATE_IDLE
//                    && lastVisiableItem + 14 > mStaggeredGridLayoutManager.getItemCount()
//                    && !imgNewSwipLayout.isRefreshing()
//                    && !isUpRefresh) {
//                LoadMore();
//                imageNewActivityAdapter.changeState(1);
//            }
            //网格式的布局形式所用到的加载更多
            int lastVisiableItem = mGridLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 14 >= mGridLayoutManager.getItemCount() && !imgNewSwipLayout.isRefreshing()) {
                Log.e(TAG, "当前的加载数据的显示要求====" + isUpRefresh);
                if (!isUpRefresh) {
                    //加载更多数据
                    LoadMore();
                    imageNewActivityAdapter.changeState(1);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    //采用流式布局（stage）时选择最大角标的item
    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    /***
     * 用户一点击逛图库时  或者网络连接失败时点击继续加载
     * 在进行数据请求加载的时候判断网络是否连接
     * 进行数据的请求
     * 并将数据进行缓存
     * 缓存的目的是当用户再次点击逛图库时不用再去数据加载
     * 逛图库中显示当地装修公司列表的接口请求
     * 传入的参数
     */
    private void HttpRequestGetImgItemList(final HashMap<String, Object> params) {
        Log.e(TAG, "进入网络请求数据中。。。。");
        img_not_found_rl.setVisibility(View.GONE);
        //判断当前的网络连接情况
        if (Constant.checkNetwork(mContext)) {
            //请求
            OKHttpUtil.post(getListUrl, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });

            OKHttpUtil.post(getListUrl, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewLoading.setVisibility(View.GONE);
                            viewNetOut.setVisibility(View.VISIBLE);
                            imgNewSwipLayout.setRefreshing(false);
                            if (imageNewActivityAdapter == null) {
                                imageNewActivityAdapter = new ImageNewActivityAdapter(mContext, imageItemsList);
                            }
                            imageNewActivityAdapter.changeState(2);
                            isUpRefresh = false;
                            customWaitDialog.dismiss();
                            Toast.makeText(mContext, "请求超时，数据获取失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             *数据请求成功之后 将请求的json缓存
                             * 并且将之前的加载缓存页面隐藏
                             * 缓存的目的在断网的时候能预览 不至于一片空白
                             */
                            viewLoading.setVisibility(View.GONE);//隐藏加载图层
                            viewNetOut.setVisibility(View.GONE);//隐藏断网图层
                            imgNewSwipLayout.setRefreshing(false);//获取数据成功停止刷新
                            customWaitDialog.dismiss();
                            Log.e(TAG, "进入网络请求数据中。。。。请求成功！！" + json);
                            //获取接口数据成功将数据添加到list中
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                Log.e(TAG, "请求数据成功===" + json);
                                if (jsonObject.getString("error_code").equals("0")) {
                                    JSONArray dataArray = jsonObject.getJSONArray("data");
                                    isUpRefresh = false;
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        _ImageItem item = new _ImageItem(dataArray.get(i).toString());
                                        //临时的容器 装载当前请求的数据
                                        tempList.add(item);
                                    }

                                    imageItemsList.addAll(tempList);
                                    tempList.clear();
                                    if (imageNewActivityAdapter == null) {
                                        imageNewActivityAdapter = new ImageNewActivityAdapter(mContext, imageItemsList);
                                        imageNewActivityAdapter.setOnImageNewItemClickLister(onImageNewItemClickLister);
                                        imgNewRecycleView.setAdapter(imageNewActivityAdapter);
                                    } else {
                                        imageNewActivityAdapter.changeState(2);
                                        imageNewActivityAdapter.notifyDataSetChanged();
                                    }
                                    Log.e(TAG, "当前的集合长度=====" + imageItemsList.size());

                                } else if (jsonObject.getString("error_code").equals("250")) {
                                    //没有更多数据的处理情况
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (imageItemsList.isEmpty()) {
                                                img_not_found_rl.setVisibility(View.VISIBLE);
                                            }
                                            Log.e(TAG, "没有更多数据===数据的集合的长度===" + imageItemsList.size());
                                            imageNewActivityAdapter.changeState(2);
                                            imageNewActivityAdapter.notifyDataSetChanged();
                                            Toast.makeText(mContext, "当前没有更多数据！", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(mContext, "服务器错误", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } else {
            //当前无网络连接
            viewNetOut.setVisibility(View.VISIBLE);//显示断网图层
            viewLoading.setVisibility(View.GONE);//隐藏加载图层
            imgNewSwipLayout.setRefreshing(false);
            isUpRefresh = false;
            customWaitDialog.dismiss();
        }
    }

    //图库列表子项点击事件
    private ImageNewActivityAdapter.OnImageNewItemClickLister onImageNewItemClickLister = new ImageNewActivityAdapter.OnImageNewItemClickLister() {
        @Override
        public void onItemClick(View view) {
            if (Constant.checkNetwork(mContext)) {
                int position = imgNewRecycleView.getChildPosition(view);
                String title = imageItemsList.get(position).getTitle();
                String url = imageItemsList.get(position).getIndexImageUrl();
                String id = imageItemsList.get(position).getId();
                Log.e(TAG, "==点击了图片列表项==" + id + "==" + title + "==" + url);
                Intent intent = new Intent(ImageNewActivity.this, ImageDetailNewActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("url", url);
                intent.putExtra("fav_conid", id);
                startActivity(intent);
            } else {
                Constant.toastNetOut(mContext);
            }

        }

        @Override
        public void onItemLongClick(View view) {

        }
    };
    //普通按钮点击事件
    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgNewHuxing:
                    //显示泡泡window
                    HuxingTv.setTextColor(Color.parseColor("#ff882e"));
                    showPopwindow(9);
                    break;
                case R.id.imgNewFengge:
                    FenggeTv.setTextColor(Color.parseColor("#ff882e"));
                    showPopwindow(7);
                    break;
                case R.id.imgNewMianji:
                    MianjiTv.setTextColor(Color.parseColor("#ff882e"));
                    showPopwindow(12);
                    break;
                case R.id.img_new_design:
                case R.id.img_new_design_gif:
                    startActivity(new Intent(mContext, GetPriceActivity.class));
                    break;
                case R.id.img_new_zixun:
                case R.id.img_new_zixun_ll:
                    //弹窗显示免费咨询
                    showZixunPopwindow();
                    break;

            }
        }
    };

    //显示联系的popwindow
    private void showZixunPopwindow() {
        zixunPopView = View.inflate(mContext, R.layout.popwindow_zixun, null);
        TextView qq_lianxi = (TextView) zixunPopView.findViewById(R.id.qq_lianxi);
        TextView dianhua_lianxi = (TextView) zixunPopView.findViewById(R.id.dianhua_lianxi);
        RelativeLayout pop_zixun_rl = (RelativeLayout) zixunPopView.findViewById(R.id.pop_zixun_rl);
        zixunPopupWindow = new PopupWindow(zixunPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        zixunPopupWindow.setFocusable(true);
        zixunPopupWindow.setOutsideTouchable(true);
        zixunPopupWindow.update();
        //打开QQ
        qq_lianxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹窗  尝试打开QQ
                String url = "http://wpa.b.qq.com/cgi/wpa.php?ln=2&uin=4006062221";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                zixunPopupWindow.dismiss();
            }
        });
        //打开电话联系
        dianhua_lianxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出窗口电话联系
                showOpenPhone();
                zixunPopupWindow.dismiss();
            }
        });
        //界面消失
        pop_zixun_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zixunPopupWindow.dismiss();
            }
        });
        //窗口显示的位置
        zixunPopupWindow.showAtLocation(zixunPopView, Gravity.CENTER, 0, 0);
    }


    //显示开启打电话的弹窗
    private void showOpenPhone() {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //打电话
        open_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4006062221"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        pop_phone_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    //显示条件的下拉框
    private void showPopwindow(int whichBtn) {
        switch (whichBtn) {
            case 7:
                //选择风格按钮
                Glide.with(mContext).load(R.drawable.img_san_up).into(fengge_down);
                imgNewFengge_jt.setVisibility(View.VISIBLE);
                imgNewHuxing_jt.setVisibility(View.GONE);
                imgNewMianji_jt.setVisibility(View.GONE);
                myGridViewAdapterFg = new MyGridViewAdapters(mContext, popBtnListFg);
                mGridView.setAdapter(myGridViewAdapterFg);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Log.e(TAG, "风格列表子项点击事件====getClass_name" + position + "====" + popBtnListFg.get(position).getClass_name());
                        Log.e(TAG, "风格列表子项点击事件====getParent_id" + position + "====" + popBtnListFg.get(position).getParent_id());
                        if (popBtnListFg.get(position).getId().equals("0")) {
                            FenggeTv.setText("风格");
                        } else {
                            FenggeTv.setText("" + popBtnListFg.get(position).getClass_name());
                        }
//                        if (!imageItemsList.isEmpty()) {
                        Log.e(TAG, "点击的户型下拉窗数据列表长度====" + imageItemsList.size());
                        page = 1;
                        imageItemsList.clear();
//                            ClearParam();
//                            LoadMoreParam.clear();
                        LoadMoreParam.put("style_id", popBtnListFg.get(position).getId());
                        Log.e(TAG, "存储的数据===style_id===" + LoadMoreParam.get("style_id"));
//                            getImgItemListParams.put("style_id", popBtnListFg.get(position).getId());
//                            HttpRequestGetImgItemList(getImgItemListParams);
                        loadDataByCondition();
//                        }
                        popupWindow.dismiss();
                        customWaitDialog.show();
                    }
                });
                break;
            case 9:
                //选择户型按钮
                Glide.with(mContext).load(R.drawable.img_san_up).into(huxing_down);
                imgNewHuxing_jt.setVisibility(View.VISIBLE);
                imgNewMianji_jt.setVisibility(View.GONE);
                imgNewFengge_jt.setVisibility(View.GONE);
                myGridViewAdapterHx = new MyGridViewAdapters(mContext, popBtnListHx);
                mGridView.setAdapter(myGridViewAdapterHx);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Log.e(TAG, "户型子项点击事件====" + position + "==" + popBtnListHx.get(position).getClass_name());
                        if (popBtnListHx.get(position).getId().equals("0")) {
                            HuxingTv.setText("户型");
                        } else {
                            HuxingTv.setText("" + popBtnListHx.get(position).getClass_name());
                        }
//                        if (!imageItemsList.isEmpty()) {
                        Log.e(TAG, "点击的户型下拉窗数据列表长度====" + imageItemsList.size());
                        imageItemsList.clear();
                        page = 1;
//                            ClearParam();
//                            LoadMoreParam.clear();
                        LoadMoreParam.put("layout_id", popBtnListHx.get(position).getId());
                        Log.e(TAG, "存储的数据===layout_id===" + LoadMoreParam.get("layout_id"));
//                            getImgItemListParams.put("layout_id", popBtnListHx.get(position).getId());
//                            HttpRequestGetImgItemList(getImgItemListParams);
                        loadDataByCondition();
//                        }
                        popupWindow.dismiss();
                        customWaitDialog.show();
                    }
                });
                break;
            case 12:
                //选择面积按钮
                Glide.with(mContext).load(R.drawable.img_san_up).into(mianji_down);
                imgNewMianji_jt.setVisibility(View.VISIBLE);
                imgNewHuxing_jt.setVisibility(View.GONE);
                imgNewFengge_jt.setVisibility(View.GONE);
                myGridViewAdapterMj = new MyGridViewAdapters(mContext, popBtnListMj);
                mGridView.setAdapter(myGridViewAdapterMj);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Log.e(TAG, "面积子项点击事件====" + position + "==" + popBtnListMj.get(position).getClass_name() + "==" + popBtnListMj.get(position).getId());
                        if (popBtnListMj.get(position).getId().equals("0")) {
                            MianjiTv.setText("面积");
                        } else {
                            MianjiTv.setText("" + popBtnListMj.get(position).getClass_name());
                        }
//                        if (!imageItemsList.isEmpty()) {
                        imageItemsList.clear();
                        page = 1;
//                            ClearParam();
//                            LoadMoreParam.clear();
                        LoadMoreParam.put("area_id", popBtnListMj.get(position).getId());
                        Log.e(TAG, "存储的数据===area_id===" + LoadMoreParam.get("area_id"));
//                            getImgItemListParams.put("area_id", popBtnListMj.get(position).getId());
//                            HttpRequestGetImgItemList(getImgItemListParams);
                        loadDataByCondition();
//                        }
                        popupWindow.dismiss();
                        customWaitDialog.show();
                    }
                });
                break;
        }
        //在这之前要完成GridView的布局
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f3f2")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAsDropDown(imgNewReLL);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                imgNewHuxing_jt.setVisibility(View.GONE);
                imgNewMianji_jt.setVisibility(View.GONE);
                imgNewFengge_jt.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.img_san_down).into(huxing_down);
                Glide.with(mContext).load(R.drawable.img_san_down).into(mianji_down);
                Glide.with(mContext).load(R.drawable.img_san_down).into(fengge_down);
                FenggeTv.setTextColor(Color.parseColor("#666666"));
                HuxingTv.setTextColor(Color.parseColor("#666666"));
                MianjiTv.setTextColor(Color.parseColor("#666666"));
            }
        });
    }

    //根据用户选择的条件请求数据
    private void loadDataByCondition() {
        if (!TextUtils.isEmpty(LoadMoreParam.get("area_id"))) {
            getImgItemListParams.remove("area_id");
            getImgItemListParams.put("area_id", LoadMoreParam.get("area_id"));
        }
        if (!TextUtils.isEmpty(LoadMoreParam.get("layout_id"))) {
            getImgItemListParams.remove("layout_id");
            getImgItemListParams.put("layout_id", LoadMoreParam.get("layout_id"));
        }
        if (!TextUtils.isEmpty(LoadMoreParam.get("style_id"))) {
            getImgItemListParams.remove("style_id");
            getImgItemListParams.put("style_id", LoadMoreParam.get("style_id"));
        }
        HttpRequestGetImgItemList(getImgItemListParams);
    }

    /**
     * 加载更多数据
     * 传入的处理以下事件
     * 1.上拉加载更多数据
     * 2.当有筛选条件的时候加载更多
     */
    private void LoadMore() {
        isUpRefresh = true;
        page++;
        Log.e(TAG, "加载更多page===》" + page);
        ClearParam();
        getImgItemListParams.put("page", page + "");
        if (LoadMoreParam.isEmpty()) {
            //没有选择条件的加载更多
            HttpRequestGetImgItemList(getImgItemListParams);
        } else {
//            if (!TextUtils.isEmpty(LoadMoreParam.get("style_id"))) {
//                //加载更多风格图库
//                getImgItemListParams.put("style_id", LoadMoreParam.get("style_id"));
//                HttpRequestGetImgItemList(getImgItemListParams);
//            } else if (!TextUtils.isEmpty(LoadMoreParam.get("layout_id"))) {
//                //加载更多户型图库
//                getImgItemListParams.put("layout_id", LoadMoreParam.get("layout_id"));
//                HttpRequestGetImgItemList(getImgItemListParams);
//            } else if (!TextUtils.isEmpty(LoadMoreParam.get("area_id"))) {
//                //加载更多的面积图库
//                getImgItemListParams.put("area_id", LoadMoreParam.get("area_id"));
//                HttpRequestGetImgItemList(getImgItemListParams);
//            }
            loadDataByCondition();
        }
    }

    //清除参数 清除集合
    private void ClearParam() {
//        getImgItemListParams.remove("layout_id");
//        getImgItemListParams.remove("style_id");
//        getImgItemListParams.remove("area_id");
        getImgItemListParams.remove("page");
    }

    private void HttpRequestGetStyleList(final String classid) {
        getStyleListParams.put("classid", classid);
        OKHttpUtil.post(getStytleUrl, getStyleListParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("error_code").equals("0")) {
                                //将数据缓存到本地
                                if (classid.equals("7")) {
                                    //缓存风格json 缓存的数据后台进行更新了则重新添加缓存
                                    if (!(AppInfoUtil.getStyleFgCache(mContext).equals(result))) {
                                        getSharedPreferences("StyleFgCache", 0).edit().clear();
                                        getSharedPreferences("StyleFgCache", 0).edit().putString("resultJson", result).commit();
                                    } else {
                                        Log.e(TAG, "当前缓存与服务器上的一致不需要再缓存===" + AppInfoUtil.getStyleFgCache(mContext));
                                    }
                                    setAdapterListDatas(7);
                                } else if (classid.equals("9")) {
                                    //缓存户型json 缓存的数据后台进行了更新则需要重新添加缓存
                                    if (!(AppInfoUtil.getStyleHxCache(mContext).equals(result))) {
                                        getSharedPreferences("StyleHxCache", 0).edit().clear();
                                        getSharedPreferences("StyleHxCache", 0).edit().putString("resultJson", result).commit();
                                    } else {
                                        Log.e(TAG, "当前缓存与服务器上的一致不需要再缓存===" + AppInfoUtil.getStyleHxCache(mContext));
                                    }
                                    setAdapterListDatas(9);
                                } else if (classid.equals("12")) {

                                    //缓存面积Json
                                    if (!(AppInfoUtil.getStyleMjCache(mContext).equals(result))) {
                                        getSharedPreferences("StyleMjCache", 0).edit().clear();
                                        getSharedPreferences("StyleMjCache", 0).edit().putString("resultJson", result).commit();
                                    } else {
                                        Log.e(TAG, "当前缓存与服务器上的一致不需要再缓存===" + AppInfoUtil.getStyleMjCache(mContext));
                                    }
                                    setAdapterListDatas(12);
                                }
                            } else {
                                Toast.makeText(mContext, "服务器错误！", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
