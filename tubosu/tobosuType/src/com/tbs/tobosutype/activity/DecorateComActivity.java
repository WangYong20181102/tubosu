package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorateComAdapter;
import com.tbs.tobosutype.adapter.PopJiatingAdapter;
import com.tbs.tobosutype.adapter.PopQuanBuQuYuAdapter;
import com.tbs.tobosutype.adapter.SearchGongSiAdapter;
import com.tbs.tobosutype.adapter.YouXuanGongSiAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean.GongsiItem;
import com.tbs.tobosutype.bean._CheckInfo;
import com.tbs.tobosutype.bean._DecorateCom;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.customview.MyListView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.SpUtil;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 装修公司新页面 4.0版本
 * create by lin+
 * 采用伸缩布局
 */
public class DecorateComActivity extends BaseActivity {
    @BindView(R.id.decorate_com_city_name_tv)
    TextView decorateComCityNameTv;
    @BindView(R.id.decorate_com_city_ll)
    LinearLayout decorateComCityLl;
    @BindView(R.id.decorate_com_search_rl)
    RelativeLayout decorateComSearchRl;
    @BindView(R.id.decorate_com_kefu_ll)
    LinearLayout decorateComKefuLl;
    @BindView(R.id.hp_title_rl)
    RelativeLayout hpTitleRl;
    @BindView(R.id.decorate_com_free_img)
    ImageView decorateComFreeImg;
    @BindView(R.id.decorate_com_free_cv)
    CardView decorateComFreeCv;
    @BindView(R.id.decorate_com_zero_img)
    ImageView decorateComZeroImg;
    @BindView(R.id.decorate_com_zero_cv)
    CardView decorateComZeroCv;
    @BindView(R.id.decorate_com_hot_img)
    ImageView decorateComHotImg;
    @BindView(R.id.decorate_com_hot_cv)
    CardView decorateComHotCv;
    @BindView(R.id.decorate_com_test_img)
    ImageView decorateComTestImg;
    @BindView(R.id.decorate_com_test_cv)
    CardView decorateComTestCv;
    @BindView(R.id.decorate_com_zonghe_jiantou_img)
    ImageView decorateComZongheJiantouImg;
    @BindView(R.id.decorate_com_zonghe_ll)
    LinearLayout decorateComZongheLl;
    @BindView(R.id.decorate_com_quanbu_jiantou_img)
    ImageView decorateComQuanbuJiantouImg;
    @BindView(R.id.decorate_com_quanbu_ll)
    LinearLayout decorateComQuanbuLl;
    @BindView(R.id.decorate_com_gengduo_jiantou_img)
    ImageView decorateComGengduoJiantouImg;
    @BindView(R.id.decorate_com_gengduo_ll)
    LinearLayout decorateComGengduoLl;
    @BindView(R.id.decorate_com_recycler)
    RecyclerView decorateComRecycler;
    @BindView(R.id.decorate_com_toolbar)
    Toolbar decorateComToolbar;
    @BindView(R.id.decorate_com_device_view)
    View decorateComDeviceView;
    @BindView(R.id.decorate_com_dec_push_img)
    ImageView decorateComDecPushImg;


    @BindView(R.id.etSearchGongsi)
    EditText etSearchGongsi;
    @BindView(R.id.ivGongSiDelete)
    ImageView ivGongSiDelete;
    @BindView(R.id.searche_bar)
    RelativeLayout searcheBar;
    @BindView(R.id.kiksezchdfa)
    ImageView kiksezchdfa;
    @BindView(R.id.tvCancelSearch)
    TextView tvCancelSearch;
    @BindView(R.id.topSearch)
    RelativeLayout topSearch;
    @BindView(R.id.noSearchData1)
    RelativeLayout noSearchData1;
    @BindView(R.id.sdfgddafsdaer)
    TextView sdfgddafsdaer;
    @BindView(R.id.ifyouxuan)
    RelativeLayout ifyouxuan;
    @BindView(R.id.youxuanList)
    MyListView youxuanList;
    @BindView(R.id.relYouxuan)
    RelativeLayout relYouxuan;
    @BindView(R.id.searchList)
    RecyclerView searchList;
    @BindView(R.id.searchSwip)
    SwipeRefreshLayout searchSwip;
    @BindView(R.id.nothingData)
    RelativeLayout nothingData;
    @BindView(R.id.mengceng4)
    View mengceng4;
    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;
    @BindView(R.id.findCompanyLayout)
    RelativeLayout findCompanyLayout;

    @BindView(R.id.findComIcon)
    ImageView findComIcon;
    @BindView(R.id.cancelFindComIcon)
    ImageView cancelFindComIcon;
    @BindView(R.id.relfindComLayout)
    RelativeLayout relfindComLayout;
    @BindView(R.id.decorate_com_zonghe_tv)
    TextView decorateComZongheTv;
    @BindView(R.id.decorate_com_quanbu_tv)
    TextView decorateComQuanbuTv;
    @BindView(R.id.decorate_com_gengduo_tv)
    TextView decorateComGengduoTv;
    private PopupWindow popWnd;

    private Context mContext;
    private String TAG = "DecorateComActivity";
    private Gson mGson;
    private LinearLayoutManager mLinearLayoutManager;
    private DecorateComAdapter mDecorateComAdapter;
    private boolean isLoading = false;//数据是否在加载
    private int mPage = 1;
    private int mPageSize = 30;
    private _DecorateCom mDecorateCom;
    private List<_DecorateCom.DataBean> mDecorateComList = new ArrayList<>();
    private String city_name;
    private String name;
    private String sort_type;
    private String district_id;
    private String home_id;
    private String business_id;
    private Intent mIntent;
    private _CheckInfo mCheckInfo;
    private int mQuyuPosition = -1;
    private int mJiatingPosition = -1;
    private int mShangyePosition = -1;
    //是否显示键盘
    private boolean isShowingInput = false;
    private YouXuanGongSiAdapter youxuanCompanyAdapter;
    private List<GongsiItem> youxuanSearchGongsiList = new ArrayList<GongsiItem>();
    private SearchGongSiAdapter searchCompanyAdapter;
    private List<GongsiItem> searchGongsiList = new ArrayList<>();
    private String searchText = "";
    private int pageS = 1;
    private boolean isSearchLoading = false;
    private int page_size = 10;
    private boolean normalData = true;
    private boolean youxuan = false;
    private LinearLayoutManager linearLayoutManager1, linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decorate_com);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.NOTICE_HOME_PAGE_CHANGE_CITY_NAME:
                if (!TextUtils.isEmpty((String) event.getData())) {
                    decorateComCityNameTv.setText("" + (String) event.getData());
                    city_name = (String) event.getData();
                    initCondition();
                    HttpGetCheckInfo();
                    initData();
                }
                break;
        }
    }

    private void initView() {
        mGson = new Gson();
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        decorateComRecycler.setLayoutManager(mLinearLayoutManager);
        decorateComRecycler.setOnTouchListener(onTouchListener);
        decorateComRecycler.addOnScrollListener(onScrollListener);
        //设置城市的名称
        decorateComCityNameTv.setText("" + SpUtil.getCity(mContext));
        city_name = SpUtil.getCity(mContext);
        //设置发单按钮的图片
        //免费报价
        if (!TextUtils.isEmpty(SpUtil.getDecComMianFeiBaoJiaImgUrl(mContext))) {
            if (SpUtil.getDecComMianFeiBaoJiaImgUrl(mContext).contains(".gif")) {
                Glide.with(mContext)
                        .load(SpUtil.getDecComMianFeiBaoJiaImgUrl(mContext))
                        .asGif()
                        .into(decorateComFreeImg);
            } else {
                Glide.with(mContext)
                        .load(SpUtil.getDecComMianFeiBaoJiaImgUrl(mContext))
                        .into(decorateComFreeImg);
            }
        }
        //零元设计
        if (!TextUtils.isEmpty(SpUtil.getDecComLingYuanSheJiImgUrl(mContext))) {
            if (SpUtil.getDecComLingYuanSheJiImgUrl(mContext).contains(".gif")) {
                Glide.with(mContext)
                        .load(SpUtil.getDecComLingYuanSheJiImgUrl(mContext))
                        .asGif()
                        .into(decorateComZeroImg);
            } else {
                Glide.with(mContext)
                        .load(SpUtil.getDecComLingYuanSheJiImgUrl(mContext))
                        .into(decorateComZeroImg);
            }
        }
        //专业推荐
        if (!TextUtils.isEmpty(SpUtil.getDecComZhuanYeTuiJianImgUrl(mContext))) {
            if (SpUtil.getDecComZhuanYeTuiJianImgUrl(mContext).contains(".gif")) {
                Glide.with(mContext)
                        .load(SpUtil.getDecComZhuanYeTuiJianImgUrl(mContext))
                        .asGif()
                        .into(decorateComHotImg);
            } else {
                Glide.with(mContext)
                        .load(SpUtil.getDecComZhuanYeTuiJianImgUrl(mContext))
                        .into(decorateComHotImg);
            }
        }
        //预算测试
        if (!TextUtils.isEmpty(SpUtil.getDecComYuSuanCeiShiImgUrl(mContext))) {
            if (SpUtil.getDecComYuSuanCeiShiImgUrl(mContext).contains(".gif")) {
                Glide.with(mContext)
                        .load(SpUtil.getDecComYuSuanCeiShiImgUrl(mContext))
                        .asGif()
                        .into(decorateComTestImg);
            } else {
                Glide.with(mContext)
                        .load(SpUtil.getDecComYuSuanCeiShiImgUrl(mContext))
                        .into(decorateComTestImg);
            }
        }
        //初始化条件
        initCondition();
        //初始化城市信息
        HttpGetCheckInfo();
        //初始化数据
        initData();
        //弹窗
        showFadan();

        //搜索页面的控件
        etSearchGongsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etSearchGongsi.getText().toString().trim().length() > 0) {
                    ivGongSiDelete.setVisibility(View.VISIBLE);
                    tvCancelSearch.setText("搜索");
                } else {
                    tvCancelSearch.setText("取消");
                    ivGongSiDelete.setVisibility(View.GONE);
                }
            }
        });


        linearLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        searchList.setLayoutManager(linearLayoutManager1);
        if (youxuan) {
            searchList.setNestedScrollingEnabled(!youxuan);
        } else {
            searchList.setNestedScrollingEnabled(youxuan);
        }
        searchList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = linearLayoutManager1.findLastVisibleItemPosition();
                if (lastPosition + 1 >= recyclerView.getLayoutManager().getItemCount() && normalData && !isSearchLoading) {
                    if (searchCompanyAdapter != null) {
                        searchCompanyAdapter.loadMoreGongsi(true);
                        pageS++;
                        searchLayout.setBackgroundResource(R.drawable.wht);
                        getSearchData(searchText);
//                        getBannerData(); // banner
                    }
                }
            }
        });
        searchSwip.setProgressBackgroundColorSchemeColor(Color.WHITE);
        searchSwip.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        if (normalData) {
            searchSwip.setOnRefreshListener(searchSwipeLister);
        }

    }

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener searchSwipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据

            searchGongsiList.clear();
            if (searchCompanyAdapter != null) {
                searchCompanyAdapter.notifyDataSetChanged();
                searchCompanyAdapter = null;
            }

            searchSwip.setRefreshing(false);
            pageS = 1;
            getSearchData(searchText);

        }
    };

    private void initCondition() {
        //筛选条件
        name = "";
        sort_type = "";
        district_id = "";
        home_id = "";
        business_id = "";

        mQuyuPosition = -1;
        mJiatingPosition = -1;
        mShangyePosition = -1;

        decorateComZongheTv.setText("综合排序");
        decorateComQuanbuTv.setText("全部区域");
        decorateComGengduoTv.setText("更多筛选");
    }

    private void initData() {
        //初始化数据
        mPage = 1;
        if (!mDecorateComList.isEmpty()) {
            mDecorateComList.clear();
        }
        HttpGetDeccomList(mPage);
    }

    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (isLoading) {
                return true;
            } else {
                return false;
            }
        }
    };

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0
                    && lastVisiableItem + 8 >= mLinearLayoutManager.getItemCount()
                    && !isLoading) {
                //加载更多
                loadMore();
            }

        }
    };

    //加载更多
    private void loadMore() {
        mPage++;
        HttpGetDeccomList(mPage);
    }

    //获取筛选条件
    private void HttpGetCheckInfo() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        if (!TextUtils.isEmpty(decorateComCityNameTv.getText().toString())) {
            param.put("city_name", decorateComCityNameTv.getText().toString());
        } else {
            param.put("city_name", SpUtil.getCity(mContext));
        }

        OKHttpUtil.post(Constant.COMPANY_GETCHECKINFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                mCheckInfo = mGson.fromJson(json, _CheckInfo.class);
                if (mCheckInfo.getStatus() == 200) {
                    //存储数据
                    SpUtil.setDecComCheckInfo(mContext, json);
                }
            }
        });
    }

    //获取网络数据
    private void HttpGetDeccomList(int page) {
        isLoading = true;//正在加载
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("city_name", city_name);
        param.put("name", name);
        param.put("sort_type", sort_type);
        param.put("district_id", district_id);
        param.put("home_id", home_id);
        param.put("business_id", business_id);
        param.put("lat", SpUtil.getLatitude(mContext));
        param.put("lng", SpUtil.getLongitude(mContext));
        param.put("page", page);
        param.put("page_size", mPageSize);
        Log.e(TAG, "请求参数===city_name===" + city_name + "===sort_type===" + sort_type);
        Log.e(TAG, "请求参数===district_id===" + district_id + "===home_id===" + home_id);
        Log.e(TAG, "请求参数===business_id===" + business_id);
        OKHttpUtil.post(Constant.COMPANY_LIST_NEW, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败====" + e.getMessage());
                isLoading = false;//正在加载
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=====" + json);
                mDecorateCom = mGson.fromJson(json, _DecorateCom.class);
                if (mDecorateCom.getStatus() == 200) {
                    //将数据布局
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDecorateComList.addAll(mDecorateCom.getData());
                            if (mDecorateComAdapter == null) {
                                mDecorateComAdapter = new DecorateComAdapter(mContext, mDecorateComList);
                                decorateComRecycler.setAdapter(mDecorateComAdapter);
                                mDecorateComAdapter.notifyDataSetChanged();
                            } else {
                                mDecorateComAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    isLoading = false;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, mDecorateCom.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    isLoading = false;
                }
            }
        });
    }

    @OnClick({R.id.decorate_com_city_ll, R.id.decorate_com_search_rl,
            R.id.decorate_com_kefu_ll, R.id.hp_title_rl,
            R.id.decorate_com_free_cv, R.id.decorate_com_zero_cv,
            R.id.decorate_com_hot_cv, R.id.decorate_com_test_cv,
            R.id.decorate_com_zonghe_ll, R.id.decorate_com_quanbu_ll,
            R.id.decorate_com_gengduo_ll, R.id.decorate_com_dec_push_img,
            R.id.ivGongSiDelete, R.id.mengceng4, R.id.tvCancelSearch,
            R.id.findComIcon, R.id.relfindComLayout, R.id.cancelFindComIcon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancelSearch:
                if (tvCancelSearch.getText().toString().trim().equals("取消")) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DecorateComActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索适配器， 优选适配器置空  均清除列表 返回正常页面
                    if (searchCompanyAdapter != null) {
                        searchGongsiList.clear();
                        searchCompanyAdapter.loadMoreGongsi(false);
                        searchCompanyAdapter.notifyDataSetChanged();
                        searchCompanyAdapter = null;
                    }

                    if (youxuanCompanyAdapter != null) {
                        youxuanSearchGongsiList.clear();
                        youxuanCompanyAdapter.notifyDataSetChanged();
                        youxuanCompanyAdapter = null;

                    }
                    System.gc();
                    relYouxuan.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.GONE);
                    nothingData.setVisibility(View.GONE);
                    mengceng4.setVisibility(View.GONE);
                    findCompanyLayout.setVisibility(View.VISIBLE); // 恢复到找装修公司页面
//                    relTopSearch1.setVisibility(View.VISIBLE);
//                    relTopSearch1.setBackgroundResource(R.drawable.wht);
                } else if (tvCancelSearch.getText().toString().trim().equals("搜索")) {
                    mengceng4.setVisibility(View.GONE);
                    searchLayout.setBackgroundResource(R.drawable.wht);
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DecorateComActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索，请求网络  在搜索页面
                    searchText = etSearchGongsi.getText().toString().trim();
                    pageS = 1;
                    searchGongsiList.clear();
                    if (searchCompanyAdapter != null) {
                        searchCompanyAdapter.notifyDataSetChanged();
                        searchCompanyAdapter = null;
                    }
                    if (youxuanCompanyAdapter != null) {
                        youxuanSearchGongsiList.clear();
                        youxuanCompanyAdapter.notifyDataSetChanged();
                        youxuanCompanyAdapter = null;

                    }
                    System.gc();
                    tvCancelSearch.setText("取消");
                    getSearchData(searchText);
                }
                break;
            case R.id.mengceng4:
                mengceng4.setVisibility(View.GONE);
                searchLayout.setVisibility(View.GONE);
                //todo   creat by lin 软键盘退出
                isShowingInput = false;
                InputMethodManager imm2 = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                break;
            case R.id.ivGongSiDelete:
                etSearchGongsi.setText("");
                ivGongSiDelete.setVisibility(View.GONE);
                tvCancelSearch.setText("取消");
                break;
            case R.id.decorate_com_dec_push_img:
                relfindComLayout.setVisibility(View.GONE);
                CacheManager.setCompanyFlag(mContext, 1);
                mIntent = new Intent(mContext, NewWebViewActivity.class);
                mIntent.putExtra("mLoadingUrl", SpUtil.getTbsAj15(mContext));
                startActivity(mIntent);
                break;
            case R.id.decorate_com_city_ll:
                //城市选择
                mIntent = new Intent(mContext, SelectCtiyActivity.class);
                mIntent.putExtra("mWhereFrom", "DecorateComActivity");
                startActivity(mIntent);
                break;
            case R.id.decorate_com_search_rl:
                //搜索
                showSearchView();
                break;
            case R.id.decorate_com_kefu_ll:
                //客服
                showZixunPopwindow();
                break;
            case R.id.decorate_com_free_cv:
                //免费报价
                mIntent = new Intent(mContext, NewWebViewActivity.class);
                mIntent.putExtra("mLoadingUrl", SpUtil.getTbsAj39(mContext));
                mContext.startActivity(mIntent);
                break;
            case R.id.decorate_com_zero_cv:
                //0元设计
                mIntent = new Intent(mContext, NewWebViewActivity.class);
                mIntent.putExtra("mLoadingUrl", SpUtil.getTbsAj40(mContext));
                mContext.startActivity(mIntent);
                break;
            case R.id.decorate_com_hot_cv:
                //专业推荐
                mIntent = new Intent(mContext, NewWebViewActivity.class);
                mIntent.putExtra("mLoadingUrl", SpUtil.getTbsAj41(mContext));
                mContext.startActivity(mIntent);
                break;
            case R.id.decorate_com_test_cv:
                //预算测试
                mIntent = new Intent(mContext, NewWebViewActivity.class);
                mIntent.putExtra("mLoadingUrl", SpUtil.getTbsAj42(mContext));
                mContext.startActivity(mIntent);
                break;
            case R.id.decorate_com_zonghe_ll:
                //综合筛选条件
                showZonghePopWindow();
                break;
            case R.id.decorate_com_quanbu_ll:
                //全部区域筛选条件
                showQuanbuquyuPopWindow();
                break;
            case R.id.decorate_com_gengduo_ll:
                //更多筛选条件
                showGengduoPopWindow();
                break;
            case R.id.findComIcon:
                Intent web = new Intent(mContext, NewWebViewActivity.class);
                web.putExtra("mLoadingUrl", SpUtil.getTbsAj14(mContext));
                startActivity(web);
                relfindComLayout.setVisibility(View.GONE);
                CacheManager.setCompanyFlag(mContext, 1);
                break;
            case R.id.cancelFindComIcon:
                relfindComLayout.setVisibility(View.GONE);
                CacheManager.setCompanyFlag(mContext, 1);
                break;
            case R.id.relfindComLayout:
                break;
        }
    }


    private void getSearchData(String text) {
        if (Util.isNetAvailable(mContext)) {
            isSearchLoading = true;
            final HashMap<String, Object> searchMap = new HashMap<String, Object>();
            searchMap.put("token", Util.getDateToken());
            searchMap.put("page", pageS);
            searchMap.put("page_size", page_size);
            searchMap.put("name", text);
//            searchMap.put("city_name", city_name);
//            searchMap.put("lat", lat);
//            searchMap.put("lng", lng);

            OKHttpUtil.post(Constant.GETGONGSIURL, searchMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    isSearchLoading = false;
                    normalData = true;
                    youxuan = false;
                    searchSwip.setRefreshing(false);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "系统繁忙，稍后再试~");

                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    isSearchLoading = false;
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    youxuanSearchGongsiList.clear();
                    searchSwip.setRefreshing(false);
                    youxuan = false;
                    normalData = true;

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                final JSONObject jsonObject = new JSONObject(json);
                                final String msg = jsonObject.getString("msg");
                                if (jsonObject.getInt("status") == 200) {
                                    JSONArray gongsiArr = jsonObject.getJSONArray("data");

                                    List<GongsiItem> tempSearchGongsiList = new ArrayList<GongsiItem>();
                                    for (int i = 0; i < gongsiArr.length(); i++) {
                                        GongsiItem item = mGson.fromJson(gongsiArr.getJSONObject(i).toString(), GongsiItem.class);
                                        tempSearchGongsiList.add(item);
                                    }
                                    searchGongsiList.addAll(tempSearchGongsiList);

                                    nothingData.setVisibility(View.GONE);
                                    relYouxuan.setVisibility(View.GONE);
                                    searchSwip.setVisibility(View.VISIBLE);
                                    searchList.setVisibility(View.VISIBLE);
                                    if (searchCompanyAdapter == null) {
                                        searchCompanyAdapter = new SearchGongSiAdapter(mContext, searchGongsiList);
                                        searchList.setAdapter(searchCompanyAdapter);
                                        searchCompanyAdapter.notifyDataSetChanged();
                                    } else {
                                        searchCompanyAdapter.notifyDataSetChanged();
                                    }

//                                    Util.setErrorLog(TAG, "打印page=" + pageS + " 目前有多少数据呢= " +searchCompanyAdapter.getItemCount());
                                    searchCompanyAdapter.setOnCompanyItemClickListener(new SearchGongSiAdapter.OnCompanyItemClickListener() {
                                        @Override
                                        public void onCompanyItemClickListener(int position) {
                                            if (Util.isNetAvailable(mContext)) {
                                                Intent it = new Intent(mContext, DecComActivity.class);
                                                it.putExtra("mCompanyId", searchGongsiList.get(position).getId());
                                                startActivity(it);
                                            }
                                        }
                                    });
                                } else if (jsonObject.getInt("status") == 201) {

                                    normalData = true;
                                    youxuan = false;
                                    youxuanSearchGongsiList.clear();
                                    Util.setErrorLog(TAG, "搜素 201");
                                    Util.setToast(mContext, msg);
                                    if (searchCompanyAdapter != null) {
                                        searchCompanyAdapter.setHideMore(true);
                                    }

                                    if (searchCompanyAdapter == null && searchGongsiList.size() == 0) {
                                        // 一条数据都没有的情况
                                        nothingData.setVisibility(View.VISIBLE);
                                        relYouxuan.setVisibility(View.GONE);
                                        searchSwip.setVisibility(View.GONE);
                                        searchList.setVisibility(View.GONE);
                                        findCompanyLayout.setVisibility(View.GONE);
                                    } else if (searchCompanyAdapter != null) {
                                        searchCompanyAdapter.notifyDataSetChanged();
                                    }

                                } else if (jsonObject.getInt("status") == 0) {
                                    normalData = false;
                                    youxuan = false;
                                    Util.setErrorLog(TAG, "搜素 0");
                                    searchSwip.setVisibility(View.GONE);
                                    Util.setToast(mContext, msg);
                                } else if (jsonObject.getInt("status") == 204) {
                                    searchGongsiList.clear();
                                    youxuan = true;
                                    normalData = false;
                                    Util.setErrorLog(TAG, "搜素 204  这个是为你优选的数据");

                                    //显示 noSearchData
                                    JSONArray gongsiArr = jsonObject.getJSONArray("data");
                                    List<GongsiItem> tempYouxuanList = new ArrayList<GongsiItem>();
                                    for (int i = 0; i < gongsiArr.length(); i++) {
                                        GongsiItem item = mGson.fromJson(gongsiArr.getJSONObject(i).toString(), GongsiItem.class);
                                        tempYouxuanList.add(item);
                                    }
                                    youxuanSearchGongsiList.addAll(tempYouxuanList);
                                    searchSwip.setVisibility(View.GONE);
                                    relYouxuan.setVisibility(View.VISIBLE);
                                    nothingData.setVisibility(View.GONE);
                                    youxuanCompanyAdapter = new YouXuanGongSiAdapter(mContext, youxuanSearchGongsiList);
                                    youxuanList.setAdapter(youxuanCompanyAdapter);
                                    youxuanCompanyAdapter.notifyDataSetChanged();
                                    youxuanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            if (Util.isNetAvailable(mContext)) {
                                                Intent it = new Intent(mContext, DecComActivity.class);
                                                it.putExtra("mCompanyId", youxuanSearchGongsiList.get(i).getId());
                                                startActivity(it);
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
            });
        }
    }

    //展示搜索界面
    private void showSearchView() {
        searchLayout.setBackgroundResource(R.color.cal_pressed_color_trancspar);
//        relTopSearch1.setVisibility(View.GONE);
        mengceng4.setVisibility(View.VISIBLE);
        // 跳转到搜索页面
        // 展开 虚拟键盘
//                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(NewGongSiAcitivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED);

        // 点击 跳转去搜索页面
        etSearchGongsi.setText("");
        // TODO: 2018/1/6 creat by lin 点击该按钮弹出键盘↓
        etSearchGongsi.setFocusable(true);
        etSearchGongsi.setFocusableInTouchMode(true);
        etSearchGongsi.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        isShowingInput = true;
        // TODO: 2018/1/6 creat by lin ↑
        tvCancelSearch.setText("取消");
        ivGongSiDelete.setVisibility(View.GONE);
        nothingData.setVisibility(View.GONE);
        relYouxuan.setVisibility(View.GONE);
        searchLayout.setVisibility(View.VISIBLE);

        if (youxuanCompanyAdapter != null) {
            youxuanSearchGongsiList.clear();
            youxuanCompanyAdapter.notifyDataSetChanged();
            youxuanCompanyAdapter = null;
        }

        if (searchCompanyAdapter != null) {
            searchGongsiList.clear();
            searchCompanyAdapter.loadMoreGongsi(false);
            searchCompanyAdapter.notifyDataSetChanged();
            searchCompanyAdapter = null;

        }
        System.gc();
    }

    //更多筛选
    private View gengduoPopView;
    private PopupWindow gengduoPopupWindow;

    private void showGengduoPopWindow() {
        decorateComGengduoJiantouImg.setImageResource(R.drawable.drop_down_c);
        gengduoPopView = View.inflate(mContext, R.layout.popwindow_dec_gengduo, null);
        RecyclerView pop_gengduo_jiatingzx_recycler = gengduoPopView.findViewById(R.id.pop_gengduo_jiatingzx_recycler);
        RecyclerView pop_gengduo_shangyezx_recycler = gengduoPopView.findViewById(R.id.pop_gengduo_shangyezx_recycler);
        LinearLayout pop_gengduo_ll = gengduoPopView.findViewById(R.id.pop_gengduo_ll);
        pop_gengduo_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        GridLayoutManager mGridLayoutManager1 = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        GridLayoutManager mGridLayoutManager2 = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        pop_gengduo_jiatingzx_recycler.setLayoutManager(mGridLayoutManager1);
        pop_gengduo_shangyezx_recycler.setLayoutManager(mGridLayoutManager2);
        final _CheckInfo checkInfo = mGson.fromJson(SpUtil.getDecComCheckInfo(mContext), _CheckInfo.class);
        //家庭装修
        PopJiatingAdapter popJiatingAdapter1 = new PopJiatingAdapter(mContext, checkInfo.getData().getMore().get(0).getSub_title(), mJiatingPosition);
        pop_gengduo_jiatingzx_recycler.setAdapter(popJiatingAdapter1);
        popJiatingAdapter1.notifyDataSetChanged();
        //公司装修
        PopJiatingAdapter popJiatingAdapter2 = new PopJiatingAdapter(mContext, checkInfo.getData().getMore().get(1).getSub_title(), mShangyePosition);
        pop_gengduo_shangyezx_recycler.setAdapter(popJiatingAdapter2);
        popJiatingAdapter2.notifyDataSetChanged();

        gengduoPopupWindow = new PopupWindow(gengduoPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gengduoPopupWindow.setFocusable(true);
        gengduoPopupWindow.setOutsideTouchable(true);
        gengduoPopupWindow.update();
        //设置点击事件  家庭装修
        popJiatingAdapter1.setOnJiatingItemClickLister(new PopJiatingAdapter.onJiatingItemClickLister() {
            @Override
            public void onItemClick(View view, int position) {
                home_id = checkInfo.getData().getMore().get(0).getSub_title().get(position).getId();
                business_id = "";
                decorateComGengduoTv.setText(""+checkInfo.getData().getMore().get(0).getSub_title().get(position).getName());
                mJiatingPosition = position;
                mShangyePosition = -1;
                gengduoPopupWindow.dismiss();
                initData();
            }
        });
        //商业装修
        popJiatingAdapter2.setOnJiatingItemClickLister(new PopJiatingAdapter.onJiatingItemClickLister() {
            @Override
            public void onItemClick(View view, int position) {
                home_id = "";
                business_id = checkInfo.getData().getMore().get(1).getSub_title().get(position).getId();
                decorateComGengduoTv.setText(""+checkInfo.getData().getMore().get(1).getSub_title().get(position).getName());
                mJiatingPosition = -1;
                mShangyePosition = position;
                gengduoPopupWindow.dismiss();
                initData();
            }
        });
        gengduoPopupWindow.showAsDropDown(decorateComDeviceView);
        gengduoPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                decorateComGengduoJiantouImg.setImageResource(R.drawable.drop_down_p);
            }
        });
    }

    //全部区域筛选
    private View quanbuquyuPopView;
    private PopupWindow quanbuquyuPopupWindow;

    private void showQuanbuquyuPopWindow() {
        decorateComQuanbuJiantouImg.setImageResource(R.drawable.drop_down_c);
        quanbuquyuPopView = View.inflate(mContext, R.layout.popwindow_dec_quanbuquyu, null);
        RecyclerView pop_dec_quanbuquyu_recycler = quanbuquyuPopView.findViewById(R.id.pop_dec_quanbuquyu_recycler);
        RelativeLayout pop_dec_quanbuquyu_rl = quanbuquyuPopView.findViewById(R.id.pop_dec_quanbuquyu_rl);
        //设置列表
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        pop_dec_quanbuquyu_recycler.setLayoutManager(mGridLayoutManager);
        final _CheckInfo checkInfo = mGson.fromJson(SpUtil.getDecComCheckInfo(mContext), _CheckInfo.class);

        PopQuanBuQuYuAdapter popQuanBuQuYuAdapter = new PopQuanBuQuYuAdapter(mContext, checkInfo.getData().getDistrict_id(), mQuyuPosition);
        pop_dec_quanbuquyu_recycler.setAdapter(popQuanBuQuYuAdapter);
        popQuanBuQuYuAdapter.notifyDataSetChanged();

        pop_dec_quanbuquyu_rl.setBackgroundColor(Color.parseColor("#ffffff"));
        quanbuquyuPopupWindow = new PopupWindow(quanbuquyuPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        quanbuquyuPopupWindow.setFocusable(true);
        quanbuquyuPopupWindow.setOutsideTouchable(true);
        quanbuquyuPopupWindow.update();

        //设置点击事件
        popQuanBuQuYuAdapter.setOnPopQuanBuItemClickLister(new PopQuanBuQuYuAdapter.onPopQuanBuItemClickLister() {
            @Override
            public void onItemClick(View view, int position) {
                //更改筛选参数
                district_id = checkInfo.getData().getDistrict_id().get(position).getId();
                decorateComQuanbuTv.setText("" + checkInfo.getData().getDistrict_id().get(position).getName());
                quanbuquyuPopupWindow.dismiss();
                mQuyuPosition = position;
                initData();
            }
        });


        quanbuquyuPopupWindow.showAsDropDown(decorateComDeviceView);
        quanbuquyuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                decorateComQuanbuJiantouImg.setImageResource(R.drawable.drop_down_p);
            }
        });
    }


    //综合筛选
    private View zonghePopView;
    private PopupWindow zonghePopupWindow;

    private void showZonghePopWindow() {
        decorateComZongheJiantouImg.setImageResource(R.drawable.drop_down_c);
        zonghePopView = View.inflate(mContext, R.layout.popwindow_dec_zonghe, null);
        LinearLayout pop_dec_zonghe_ll = zonghePopView.findViewById(R.id.pop_dec_zonghe_ll);
        RelativeLayout pop_dec_juli_rl = zonghePopView.findViewById(R.id.pop_dec_juli_rl);
        RelativeLayout pop_dec_fanganshu_rl = zonghePopView.findViewById(R.id.pop_dec_fanganshu_rl);
        RelativeLayout pop_dec_xiaoguotushuliang_rl = zonghePopView.findViewById(R.id.pop_dec_xiaoguotushuliang_rl);
        RelativeLayout pop_dec_redu_rl = zonghePopView.findViewById(R.id.pop_dec_redu_rl);

        TextView pop_dec_juli_tv = zonghePopView.findViewById(R.id.pop_dec_juli_tv);
        TextView pop_dec_fanganshu_tv = zonghePopView.findViewById(R.id.pop_dec_fanganshu_tv);
        TextView pop_dec_xiaoguotushuliang_tv = zonghePopView.findViewById(R.id.pop_dec_xiaoguotushuliang_tv);
        TextView pop_dec_redu_tv = zonghePopView.findViewById(R.id.pop_dec_redu_tv);

        pop_dec_zonghe_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        zonghePopupWindow = new PopupWindow(zonghePopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        zonghePopupWindow.setFocusable(true);
        zonghePopupWindow.setOutsideTouchable(true);
        zonghePopupWindow.update();
        //选择按钮颜色变化
        if (TextUtils.isEmpty(sort_type)) {
            //全灰
            pop_dec_juli_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_fanganshu_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_xiaoguotushuliang_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_redu_tv.setTextColor(Color.parseColor("#363650"));
        } else if (sort_type.equals("1")) {
            pop_dec_juli_tv.setTextColor(Color.parseColor("#ff6b14"));
            pop_dec_fanganshu_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_xiaoguotushuliang_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_redu_tv.setTextColor(Color.parseColor("#363650"));
        } else if (sort_type.equals("2")) {
            pop_dec_juli_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_fanganshu_tv.setTextColor(Color.parseColor("#ff6b14"));
            pop_dec_xiaoguotushuliang_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_redu_tv.setTextColor(Color.parseColor("#363650"));
        } else if (sort_type.equals("3")) {
            pop_dec_juli_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_fanganshu_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_xiaoguotushuliang_tv.setTextColor(Color.parseColor("#ff6b14"));
            pop_dec_redu_tv.setTextColor(Color.parseColor("#363650"));
        } else if (sort_type.equals("4")) {
            pop_dec_juli_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_fanganshu_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_xiaoguotushuliang_tv.setTextColor(Color.parseColor("#363650"));
            pop_dec_redu_tv.setTextColor(Color.parseColor("#ff6b14"));
        }
        //距离
        pop_dec_juli_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //条件更换
                sort_type = "1";
                //设置顶部按钮的名称
                decorateComZongheTv.setText("距离");
                //箭头回弹
                decorateComZongheJiantouImg.setImageResource(R.drawable.drop_down_p);
                //请求数据
                initData();
                //关闭pop
                zonghePopupWindow.dismiss();
            }
        });
        //方案数
        pop_dec_fanganshu_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //条件更换
                sort_type = "2";
                decorateComZongheTv.setText("方案数");
                //箭头回弹
                decorateComZongheJiantouImg.setImageResource(R.drawable.drop_down_p);
                //请求数据
                initData();
                //关闭pop
                zonghePopupWindow.dismiss();
            }
        });
        //效果图数量
        pop_dec_xiaoguotushuliang_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //条件更换
                sort_type = "3";
                decorateComZongheTv.setText("效果图数量");
                //箭头回弹
                decorateComZongheJiantouImg.setImageResource(R.drawable.drop_down_p);
                //请求数据
                initData();
                //关闭pop
                zonghePopupWindow.dismiss();
            }
        });
        //浏览热度
        pop_dec_redu_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //条件更换
                sort_type = "4";
                decorateComZongheTv.setText("浏览热度");
                //箭头回弹
                decorateComZongheJiantouImg.setImageResource(R.drawable.drop_down_p);
                //请求数据
                initData();
                //关闭pop
                zonghePopupWindow.dismiss();
            }
        });
        zonghePopupWindow.showAsDropDown(decorateComDeviceView);
        zonghePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                decorateComZongheJiantouImg.setImageResource(R.drawable.drop_down_p);
            }
        });
    }


    private View zixunPopView;
    private PopupWindow zixunPopupWindow;

    private void showZixunPopwindow() {
        zixunPopView = View.inflate(mContext, R.layout.popwindow_zixun, null);
        RelativeLayout popwindow_zixun_rl = zixunPopView.findViewById(R.id.popwindow_zixun_rl);
        TextView qq_lianxi = (TextView) zixunPopView.findViewById(R.id.qq_lianxi);
        TextView dianhua_lianxi = (TextView) zixunPopView.findViewById(R.id.dianhua_lianxi);
        RelativeLayout pop_zixun_rl = (RelativeLayout) zixunPopView.findViewById(R.id.pop_zixun_rl);
        popwindow_zixun_rl.setBackgroundColor(Color.parseColor("#ffffff"));
        zixunPopupWindow = new PopupWindow(zixunPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        zixunPopupWindow.setFocusable(true);
        zixunPopupWindow.setOutsideTouchable(true);
        zixunPopupWindow.update();

        //打开QQ
        qq_lianxi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //弹窗  尝试打开QQ
                if (Util.checkApkExist(mContext, "com.tencent.mobileqq")) {
                    String url = "http://wpa.b.qq.com/cgi/wpa.php?ln=2&uin=" + SpUtil.getCustom_service_qq(mContext);
                    Log.e(TAG, "获取QQ==============" + SpUtil.getCustom_service_qq(mContext));

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    Toast.makeText(mContext, "本机未安装QQ", Toast.LENGTH_SHORT).show();
                }
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

    private void showOpenPhone() {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        TextView phone_num = popview.findViewById(R.id.phone_num);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        LinearLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        phone_num.setText("土拨鼠热线：" + SpUtil.getCustom_service_tel(mContext));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //打电话
        open_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + SpUtil.getCustom_service_tel(mContext)));
                Log.e(TAG, "获取电话==============" + SpUtil.getCustom_service_tel(mContext));
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


    private void showFadan() {
        if (CacheManager.getCompanyFlag(mContext) == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        uihandler.sendEmptyMessage(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private Handler uihandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4:
                    if (popWnd != null) {
                        popWnd.dismiss();
                    }
                    //如果显示了蒙层则将不显示发单的弹窗
                    if (!isShowingInput) {
                        relfindComLayout.setVisibility(View.VISIBLE);
                        findComIcon.setImageResource(R.drawable.gongsifree);
                        findComIcon.setVisibility(View.VISIBLE);
                    }

                    break;
            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("你确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //关闭时上传数据
                                    Util.HttpPostUserUseInfo();
                                    finish();
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("再看看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
