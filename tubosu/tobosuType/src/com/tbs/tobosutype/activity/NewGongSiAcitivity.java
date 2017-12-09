package com.tbs.tobosutype.activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.ChoseCityAdapter;
import com.tbs.tobosutype.adapter.ChoseProvinceAdapter;
import com.tbs.tobosutype.adapter.ComDisctrictAdapter;
import com.tbs.tobosutype.adapter.GongSiAdapter;
import com.tbs.tobosutype.adapter.GongsiAdViewpagerAdapter;
import com.tbs.tobosutype.adapter.RCompanyAdapter;
import com.tbs.tobosutype.adapter.SearchGongSiAdapter;
import com.tbs.tobosutype.adapter.YouXuanGongSiAdapter;
import com.tbs.tobosutype.bean.CompanyBannerItem;
import com.tbs.tobosutype.bean.CompanyCityBean;
import com.tbs.tobosutype.bean.CompanyDistrictBean;
import com.tbs.tobosutype.bean.CompanyProvinceBean;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean.GongsiItem;
import com.tbs.tobosutype.bean.RCompanyBean;
import com.tbs.tobosutype.bean.ShaixuanBean;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.customview.MyListView;
import com.tbs.tobosutype.customview.ShaixuanDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppBarStateChangeListener;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class NewGongSiAcitivity extends com.tbs.tobosutype.base.BaseActivity implements View.OnClickListener {
    private static final String TAG = NewGongSiAcitivity.class.getSimpleName();
    private Context mContext;


    private YouXuanGongSiAdapter youxuanCompanyAdapter;
    private TextView tvGongsiCity;
    private String gongsiCity = "深圳市";
    private String shaixuanCity = "";
    private MyListView youxuanList;
    private EditText etSearchGongsi;
    private ImageView ivGongSiDelete;
    private TextView tvCancelSearch;
    private RelativeLayout relTopSearch;
    private RelativeLayout reUpSelectLayout;
    private RelativeLayout relGoClick, nothingData;
    private ImageView ivGoFadan;
    private TextView tvZonghe, tvLiulanzuiduo, tvAnlizuiduo, tvLiwozuijin;
    private TextView shitirenzheng,ctuijian,fuwuquyu;
    private RelativeLayout reSearvice;
    private RelativeLayout reCompanDataEmpty, relYouxuan;
    private AppBarLayout mainAppbar;
    private LinearLayoutManager linearLayoutManager1, linearLayoutManager;
    private boolean isLoading = false;
    private boolean isSearchLoading = false;
    private RecyclerView findcompanyrecycler;
    private SwipeRefreshLayout findcompanyswiperefresh;

    private GongSiAdapter companyAdapter;
    private List<GongsiItem> gongsiList = new ArrayList<>();

    private List<GongsiItem> searchGongsiList = new ArrayList<>();
    private SearchGongSiAdapter searchCompanyAdapter;

    private ImageView findComIcon;
    private ImageView cancelFindComIcon;
    private RelativeLayout relfindComLayout;

    private List<GongsiItem> youxuanSearchGongsiList = new ArrayList<>();
    private Gson gson;
    private boolean normalData = true;

    private boolean isMoving = false;

    private int topBarHeight;
    private int mCurrentPosition = 0;
    private int page = 1;
    private int page_size = 10;
    private int pageS = 1;
    // 网络请求所请求的参数
    private String city_name = gongsiCity;
    private String sort_type = "1";            // 排序类型：1：综合；2：浏览最多；3：案例最多；4：距离最近（默认：1：综合)
    private String certification = "";         // 实体认证：1：是；0：否certification
    private String recommend = "";             // 是否推荐：1：是；0：否recommend
    private String district_id = "";           // 区ID号district_id
    private String home_id = "";               //家装范围ID号home_id
    private String tool_id = "";               // 工装范围ID号tool_id
    private String lat = "";                   // 经度lat
    private String lng = "";                   // 经度lng

    private float X1;
    private float X2;
    private float Y1;
    private float Y2;
    private float ComparedX;
    private float ComparedY;

    private int fixHeight = 0;


    private ArrayList<CompanyDistrictBean> discList = new ArrayList<CompanyDistrictBean>();

    private int pageState = 0;

    private String searchText = "";

    private RelativeLayout searchLayout, findCompanyLayout;
    private SwipeRefreshLayout searchSwip;
    private RecyclerView searchList;
    private boolean youxuan = false;



    // 筛选
    private RelativeLayout relShaiXuan;
    private ArrayList<ShaixuanBean> jiatingList = new ArrayList<ShaixuanBean>();
    private ArrayList<ShaixuanBean> shangyeList = new ArrayList<ShaixuanBean>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = NewGongSiAcitivity.this;
        setContentView(R.layout.activity_gocompany1);
        bindViews();
        initViews();
        getCityJson();
        showFadan();
    }


    private void bindViews() {
        gongsiViewpager = (ViewPager) findViewById(R.id.gongsiViewpager);
        gognsiDotLayout = (LinearLayout) findViewById(R.id.gognsiDotLayout);
        gongsiRecyclerViewTuijian = (RecyclerView) findViewById(R.id.gongsiRecyclerViewTuijian);
        mainAppbar = (AppBarLayout) findViewById(R.id.mainAppbar);
        findcompanyswiperefresh = (SwipeRefreshLayout) findViewById(R.id.find_company_swipe_refresh);
        findcompanyrecycler = (RecyclerView) findViewById(R.id.find_company_recycler);
        relTopSearch = (RelativeLayout) findViewById(R.id.relTopSearch);
        reUpSelectLayout = (RelativeLayout) findViewById(R.id.reUpSelectLayout);
        ivGongSiDelete = (ImageView) findViewById(R.id.ivGongSiDelete);
        etSearchGongsi = (EditText) findViewById(R.id.etSearchGongsi);
        tvCancelSearch = (TextView) findViewById(R.id.tvCancelSearch);
        youxuanList = (MyListView) findViewById(R.id.youxuanList);
        tvGongsiCity = (TextView) findViewById(R.id.tvGongsiCity);
        ivGoFadan = (ImageView) findViewById(R.id.ivGoFadan);

        tvZonghe = (TextView) findViewById(R.id.tvZonghe);
        tvLiulanzuiduo = (TextView) findViewById(R.id.tvLiulanzuiduo);
        tvAnlizuiduo = (TextView) findViewById(R.id.tvAnlizuiduo);
        tvLiwozuijin = (TextView) findViewById(R.id.tvLiwozuijin);
        nothingData = (RelativeLayout) findViewById(R.id.nothingData);
        findComIcon = (ImageView) findViewById(R.id.findComIcon);
        cancelFindComIcon = (ImageView) findViewById(R.id.cancelFindComIcon);
        relfindComLayout = (RelativeLayout) findViewById(R.id.relfindComLayout);

        shitirenzheng = (TextView) findViewById(R.id.shitirenzheng);
        ctuijian = (TextView) findViewById(R.id.ctuijian);
        fuwuquyu = (TextView) findViewById(R.id.fuwuquyu);
        reSearvice = (RelativeLayout) findViewById(R.id.reSearvice);
        reCompanDataEmpty = (RelativeLayout) findViewById(R.id.reCompanDataEmpty);
        relYouxuan = (RelativeLayout) findViewById(R.id.relYouxuan);

        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        searchSwip = (SwipeRefreshLayout) findViewById(R.id.searchSwip);
        searchList = (RecyclerView) findViewById(R.id.searchList);
        relGoClick = (RelativeLayout) findViewById(R.id.relGoClick);
        findCompanyLayout = (RelativeLayout) findViewById(R.id.findCompanyLayout);
        relShaiXuan = (RelativeLayout) findViewById(R.id.relShaiXuan);

    }

    private void initViews(){
        ivGoFadan.setOnClickListener(this);
        tvZonghe.setOnClickListener(this);
        tvLiulanzuiduo.setOnClickListener(this);
        tvAnlizuiduo.setOnClickListener(this);
        tvLiwozuijin.setOnClickListener(this);

        shitirenzheng.setOnClickListener(this);
        ctuijian.setOnClickListener(this);
        fuwuquyu.setOnClickListener(this);
        reSearvice.setOnClickListener(this);
        tvGongsiCity.setOnClickListener(this);
        relShaiXuan.setOnClickListener(this);

        findComIcon.setOnClickListener(this);
        cancelFindComIcon.setOnClickListener(this);

        ivGongSiDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearchGongsi.setText("");
                ivGongSiDelete.setVisibility(View.GONE);
                tvCancelSearch.setText("取消");
            }
        });
        tvCancelSearch.setOnClickListener(this);
        relGoClick.setOnClickListener(this);
        relfindComLayout.setOnClickListener(this);
        etSearchGongsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(etSearchGongsi.getText().toString().trim().length()>0){
                    ivGongSiDelete.setVisibility(View.VISIBLE);
                    tvCancelSearch.setText("搜索");
                }else {
                    tvCancelSearch.setText("取消");
                    ivGongSiDelete.setVisibility(View.GONE);
                }
            }
        });

        findcompanyswiperefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        findcompanyswiperefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        findcompanyswiperefresh.setOnRefreshListener(swipeLister);


        mainAppbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if(state == State.EXPANDED ) {
//                    Util.setToast(mContext, "展开");
                    pageState = 0;


                    //展开状态
                }else if(state == State.COLLAPSED){
                    pageState = 1;

//                    Util.setToast(mContext, "折叠");
                    //折叠状态

                }else {
                    pageState = 2;

//                    Util.setToast(mContext, "中间");
                    //中间状态

                }
            }
        });


        findcompanyrecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                topBarHeight = relTopSearch.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //得到当前显示的最后一个item的view
                int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && lastPosition + 2 >= recyclerView.getLayoutManager().getItemCount()) {

                    if (companyAdapter != null) {
                        companyAdapter.loadMoreGongsi(true);
                        page++;
                        getNetData();
                    }
                }

//                if(mCurrentPosition > 0){
//                    View view = linearLayoutManager.findViewByPosition(mCurrentPosition+1); // 获取第一个item
//                    if (view != null) {
//                        if (view.getTop() <= topBarHeight) {
//                            relTopSearch.setY(-(topBarHeight - view.getTop()));
//                        } else {
//                            relTopSearch.setY(0);
//                        }
//                    }
//                }
//
//
//                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
//                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();
//                    relTopSearch.setY(0);
//                }



//                View view = linearLayoutManager.findViewByPosition(mCurrentPosition + 1);
//                if (view != null) {
//                    if (view.getTop() <= topBarHeight) {
//                        relTopSearch.setY(-(topBarHeight - view.getTop()));
//                    } else {
//                        relTopSearch.setY(0);
//                    }
//                }
//
//                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
//                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();
//                    relTopSearch.setY(0);
//                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        findcompanyrecycler.setLayoutManager(linearLayoutManager);



        searchSwip.setProgressBackgroundColorSchemeColor(Color.WHITE);
        searchSwip.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        if(normalData){
            searchSwip.setOnRefreshListener(searchSwipeLister);
        }

        linearLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        searchList.setLayoutManager(linearLayoutManager1);
        if(youxuan){
            searchList.setNestedScrollingEnabled(!youxuan);
        }else {
            searchList.setNestedScrollingEnabled(youxuan);
        }
        searchList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = linearLayoutManager1.findLastVisibleItemPosition();
//                Util.setErrorLog(TAG, "总共有" +recyclerView.getLayoutManager().getItemCount()+ "     最后一个的位置是" + lastPosition);
                if (lastPosition + 1 >= recyclerView.getLayoutManager().getItemCount() && normalData && !isSearchLoading) {
//                    Util.setErrorLog(TAG, "总 进入 ");
                    if (searchCompanyAdapter != null) {
                        searchCompanyAdapter.loadMoreGongsi(true);
                        pageS++;
                        getSearchData(searchText);
                    }
                }
            }
        });

        findcompanyrecycler.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //当手指第一次按下的时候，执行的操作
                        X1 = motionEvent.getX(); //获取刚按下屏幕位置的X坐标的值
                        Y1 = motionEvent.getY(); //获取刚按下屏幕位置的Y坐标的值
                        isMoving = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isMoving = false;
                        //当手指抬起，离开屏幕的时候，执行的操作
                        X2 = motionEvent.getX(); //当手指抬起时，再次获取屏幕位置的X值
                        Y2 = motionEvent.getY(); //同理
                        ComparedX = X2 - X1; //第二次的X坐标的位置减去第一次X坐标的位置，代表X坐标上的变化情况
                        ComparedY = Y2 -Y1; //同理
                        //当X坐标的变化量的绝对值大于Y坐标的变化量的绝对值，以X坐标的变化情况作为判断依据
                        //上下左右的判断，都在一条直线上，但手指的操作不可能划直线，所有选择变化量大的方向上的量
                        //作为判断依据
                        if (Math.abs(ComparedX) >= Math.abs(ComparedY)){
                            //leader X
                            if (ComparedX>0){
                                //手机屏幕的坐标值，从左上方到右下方增加，横为X轴，竖为Y轴
//                                Toast.makeText(mContext,"向右滑动",Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(getApplicationContext(),"向左滑动",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            // leader Y
                            if (ComparedY>0){
//                                Toast.makeText(mContext,"向下滑动",Toast.LENGTH_SHORT).show();
                                if(pageState == 0){
                                    // 展开

                                }else if(pageState == 1){
                                    // 折叠
                                    relTopSearch.setVisibility(View.VISIBLE);
                                    reUpSelectLayout.setVisibility(View.VISIBLE);
                                }else {
                                    // 中间
                                }

                            }else{
//                                Toast.makeText(mContext,"向上滑动",Toast.LENGTH_SHORT).show();
                                if(pageState == 0){
                                    // 展开

                                }else if(pageState == 1){
                                    // 折叠
                                    relTopSearch.setVisibility(View.GONE);
                                    reUpSelectLayout.setVisibility(View.GONE);
                                }else {
                                    // 中间

                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //当手指一直按在屏幕上，并且移动的时候执行的操作
                        isMoving = true;
                        break;
                }

                //处于下拉刷新时列表不允许点击  死锁问题
                if (findcompanyswiperefresh.isRefreshing()) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        findComIcon.setVisibility(View.VISIBLE);
        findComIcon.setVisibility(View.GONE);
        getData();
        getNetData();
    }


    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            gongsiList.clear();
            if(companyAdapter != null){
                companyAdapter.notifyDataSetChanged();
                companyAdapter = null;
            }

            findcompanyswiperefresh.setRefreshing(false);
            page = 1;
            getNetData();
        }
    };



    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener searchSwipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据

            searchGongsiList.clear();
            if(searchCompanyAdapter != null){
                searchCompanyAdapter.notifyDataSetChanged();
                searchCompanyAdapter = null;
            }

            searchSwip.setRefreshing(false);
            pageS = 1;
            getSearchData(searchText);

        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return findcompanyrecycler.onTouchEvent(event);
    }


    private void getData(){
        if(Util.isNetAvailable(mContext)){
            HashMap<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("token", Util.getDateToken());
            dataMap.put("city_name", gongsiCity);
            dataMap.put("district_id", district_id);
            OKHttpUtil.post(Constant.COMPANY_TOP_LIST, dataMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Util.setToast(mContext, "系统繁忙，稍后再试~");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String bannerJson = response.body().string();
                    Util.setErrorLog(TAG, bannerJson);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                JSONObject bannerObject = new JSONObject(bannerJson);
                                String msg = bannerObject.getString("msg");
                                if(bannerObject.getInt("status") == 200){
                                    JSONObject data = bannerObject.getJSONObject("data");
                                    JSONArray bannerArr = data.getJSONArray("company_banner");
                                    JSONArray comArr = data.getJSONArray("companys");
                                    List<CompanyBannerItem> bannerList = new ArrayList<CompanyBannerItem>();
                                    List<RCompanyBean> companysItemList = new ArrayList<RCompanyBean>();
                                    for (int i = 0; i < bannerArr.length(); i++) {
                                        CompanyBannerItem banner = new Gson().fromJson(bannerArr.getJSONObject(i).toString(), CompanyBannerItem.class);
                                        bannerList.add(banner);
                                    }

                                    for (int i = 0; i < comArr.length(); i++) {
                                        RCompanyBean item = new Gson().fromJson(comArr.getJSONObject(i).toString(), RCompanyBean.class);
                                        companysItemList.add(item);
                                    }

                                    initBannerAdapter(gongsiViewpager, gognsiDotLayout, bannerList);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                    gongsiRecyclerViewTuijian.setLayoutManager(linearLayoutManager);
                                    RCompanyAdapter adapter = new RCompanyAdapter(mContext, companysItemList);
                                    gongsiRecyclerViewTuijian.setAdapter(adapter);

                                }else if(bannerObject.getInt("status") == 201){
                                    Util.setToast(mContext, msg);
                                }else if(bannerObject.getInt("status") == 0){
                                    Util.setToast(mContext, msg);
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

    private void getNetData(){
        if(Util.isNetAvailable(mContext)){
            isLoading = true;
            HashMap<String, Object> gongsiMap = new HashMap<String, Object>();
            gongsiMap.put("token", Util.getDateToken());
            gongsiMap.put("page", page);
            gongsiMap.put("page_size", page_size);
            gongsiMap.put("city_name", city_name);
            gongsiMap.put("sort_type", sort_type);
            gongsiMap.put("certification", certification);
            gongsiMap.put("recommend", recommend);
            gongsiMap.put("district_id", district_id);
            gongsiMap.put("lat", SpUtil.getLatitude(mContext));
            gongsiMap.put("lng", SpUtil.getLongitude(mContext));


            OKHttpUtil.post(Constant.GETGONGSIURL, gongsiMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    isLoading = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "系统繁忙，稍后再试~");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    isLoading = false;
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if (companyAdapter != null) {
                                companyAdapter.loadMoreGongsi(false);
                                companyAdapter.notifyDataSetChanged();
                            }

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                String msg = jsonObject.getString("msg");

                                if(jsonObject.getInt("status") == 200){

                                    JSONArray gongsiArr = jsonObject.getJSONArray("data");
                                    gson = new Gson();
                                    for(int i=0;i<gongsiArr.length();i++){
                                        GongsiItem item = gson.fromJson(gongsiArr.getJSONObject(i).toString(), GongsiItem.class);
                                        gongsiList.add(item);
                                    }
                                    findcompanyrecycler.setVisibility(View.VISIBLE);
                                    if (companyAdapter == null) {
                                        companyAdapter = new GongSiAdapter(mContext, gongsiList);
                                        findcompanyrecycler.setAdapter(companyAdapter);
                                        companyAdapter.notifyDataSetChanged();
                                    }else {
                                        companyAdapter.notifyDataSetChanged();
                                    }

                                }else if(jsonObject.getInt("status") == 201){
                                    Util.setToast(mContext, msg);
                                    if(companyAdapter!=null){
                                        companyAdapter.setHideMore(true);
                                    }
                                }else if(jsonObject.getInt("status") == 0){
                                    Util.setToast(mContext, msg);
                                }

                                if(gongsiList.size()==0){
                                    reCompanDataEmpty.setVisibility(View.VISIBLE);
                                }else {
                                    reCompanDataEmpty.setVisibility(View.GONE);
                                }

                                companyAdapter.setOnCompanyItemClickListener(new GongSiAdapter.OnCompanyItemClickListener() {

                                    @Override
                                    public void onCompanyItemClickListener(int position) {
                                        Intent it = new Intent(mContext, DecComActivity.class);
                                        it.putExtra("mCompanyId", gongsiList.get(position).getId());
                                        startActivity(it);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case EC.EventCode.CHOOSE_CITY_CODE:
                gongsiCity = (String)event.getData();
                city_name = gongsiCity;
                tvGongsiCity.setText(gongsiCity);

                discList.clear();
                fuwuquyu.setText("服务区域");

                ctuijian.setBackgroundResource(R.drawable.select_item_textview_bg);
                ctuijian.setTextColor(Color.parseColor("#4D4D4D"));
                shitirenzheng.setBackgroundResource(R.drawable.select_item_textview_bg);
                shitirenzheng.setTextColor(Color.parseColor("#4D4D4D"));

                tvZonghe.setTextColor(getResources().getColor(R.color.gongsi_selected));
                tvLiulanzuiduo.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvAnlizuiduo.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvLiwozuijin.setTextColor(getResources().getColor(R.color.gongsi_unselected));

                page = 1;
                sort_type = "1";
                certification = "0";
                recommend = "0";
                district_id = "";
                home_id = "";
                tool_id = "";

                gongsiList.clear();
                if(companyAdapter != null){
                    companyAdapter.notifyDataSetChanged();
                    companyAdapter = null;
                }

                getData(); // banner
                getNetData(); // body
                sortDistrict(gongsiCity);  // 选择区域
                break;

            case EC.EventCode.SHAIXUAN_CITY_CODE:
                // 点击 筛选的城市  还没有确定
                shaixuanCity = (String)event.getData();
                break;
            case EC.EventCode.QUEDING_SHAIXUAN_CITY_CODE:
                // 确定 需要 修改外面的dialog的
                discList.clear();
                sortDistrict(shaixuanCity);



                if(discList.size() != 0){
                    shaixuanDialog.updateServiceAreaData(discList);
                }else{
                    Util.setErrorLog(TAG,"discList的长度为000000");
                }
                shaixuanDialog.setCity(shaixuanCity);
                if(!"".equals(shaixuanCity)){
                    // 确定是筛选了城市
                    fuwuquyu.setText("服务区域");
                    getNetData();
                    // 其他页面也要改

                }else{
                    Util.setErrorLog(TAG, "你没有筛选城市");
                }
                break;
            case EC.EventCode.CHOOSE_PROVINCE_CODE:
                clickProvince = 1;
                break;
            case EC.EventCode.CHOOSE_PROVINCE_CODE1:
                String city = (String) event.getData();
                city_name = city;
                tvGongsiCity.setText(city_name);
                getNetData();
                break;

        }
    }

    private void sortDistrict(String _city){
        if(companyCityBeanArrayList!=null && companyCityBeanArrayList.size()!=0){
            for(int i=0;i<companyCityBeanArrayList.size();i++){
                if(discList.size()==0){
                    if(companyCityBeanArrayList.get(i).getCity_name().contains(_city)){
                        discList.addAll(companyCityBeanArrayList.get(i).getDisctBeanList());
                        break;
                    }
                }else {
                    Log.d(TAG, "不包含该城市");
                }
            }
        }else {
            getCityJson();
        }
        for(int i=0;i<discList.size();i++){
            Util.setErrorLog(TAG, discList.get(i).getDistrict_name());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvGongsiCity:
                Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle b = new Bundle();
                b.putString("fromFindCompany", "64");
                selectCityIntent.putExtra("findDecorateCompanySelectcityBundle", b);
                startActivityForResult(selectCityIntent, 3);
                break;
            case R.id.ivGoFadan:
                relfindComLayout.setVisibility(View.GONE);
                CacheManager.setCompanyFlag(mContext, 1);
                Intent webIntent = new Intent(mContext, NewWebViewActivity.class);
                webIntent.putExtra("mLoadingUrl", Constant.COMPANY_FADAN_URL);
                startActivity(webIntent);
                break;
            case R.id.findComIcon:
                Intent web = new Intent(mContext, NewWebViewActivity.class);
                web.putExtra("mLoadingUrl", Constant.COMPANY_TANKUANG_URL);
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
            case R.id.relShaiXuan:
                // 点击 筛选 二字
                discList.clear();
                sortDistrict(tvGongsiCity.getText().toString().trim());
                getToolNetData();
                showOutDialog(tvGongsiCity.getText().toString().trim());
                break;
            case R.id.tvZonghe:
                tvZonghe.setTextColor(getResources().getColor(R.color.gongsi_selected));
                tvLiulanzuiduo.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvAnlizuiduo.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvLiwozuijin.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                gongsiList.clear();
                if(companyAdapter!=null){
                    companyAdapter.notifyDataSetChanged();
                    companyAdapter = null;
                }
                page = 1;
                sort_type = "1";
                getNetData();
                break;
            case R.id.tvLiulanzuiduo:
                tvZonghe.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvLiulanzuiduo.setTextColor(getResources().getColor(R.color.gongsi_selected));
                tvAnlizuiduo.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvLiwozuijin.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                gongsiList.clear();
                if(companyAdapter!=null){
                    companyAdapter.notifyDataSetChanged();
                    companyAdapter = null;
                }
                page = 1;
                sort_type = "2";
                getNetData();
                break;
            case R.id.tvAnlizuiduo:
                tvZonghe.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvLiulanzuiduo.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvAnlizuiduo.setTextColor(getResources().getColor(R.color.gongsi_selected));
                tvLiwozuijin.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                gongsiList.clear();
                if(companyAdapter!=null){
                    companyAdapter.notifyDataSetChanged();
                    companyAdapter = null;
                }
                page = 1;
                sort_type = "3";
                getNetData();
                break;
            case R.id.tvLiwozuijin:
                tvZonghe.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvLiulanzuiduo.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvAnlizuiduo.setTextColor(getResources().getColor(R.color.gongsi_unselected));
                tvLiwozuijin.setTextColor(getResources().getColor(R.color.gongsi_selected));
                gongsiList.clear();
                if(companyAdapter!=null){
                    companyAdapter.notifyDataSetChanged();
                    companyAdapter = null;
                }
                page = 1;
                sort_type = "4";
                getNetData();
                break;

            case R.id.shitirenzheng:
                shitirenzheng.setBackgroundResource(R.drawable.select_item_textview_bg_selected);
                shitirenzheng.setTextColor(Color.parseColor("#FF6F20"));

                ctuijian.setBackgroundResource(R.drawable.select_item_textview_bg);
                ctuijian.setTextColor(Color.parseColor("#4D4D4D"));

                gongsiList.clear();
                if(companyAdapter!=null){
                    companyAdapter.notifyDataSetChanged();
                    companyAdapter = null;
                }
                page = 1;
                certification = "1";
                recommend = "0";
                getNetData();
                break;
            case R.id.ctuijian:
                shitirenzheng.setBackgroundResource(R.drawable.select_item_textview_bg);
                shitirenzheng.setTextColor(Color.parseColor("#4D4D4D"));

                ctuijian.setBackgroundResource(R.drawable.select_item_textview_bg_selected);
                ctuijian.setTextColor(Color.parseColor("#FF6F20"));

                gongsiList.clear();
                if(companyAdapter!=null){
                    companyAdapter.notifyDataSetChanged();
                    companyAdapter = null;
                }
                page = 1;
                certification = "0";
                recommend = "1";
                getNetData();
                break;
            case R.id.fuwuquyu:
            case R.id.reSearvice:

                // 弹出 弹框
                sortDistrict(gongsiCity);
                View contentView = LayoutInflater.from(NewGongSiAcitivity.this).inflate(R.layout.popuplayout_district_layout, null);
                final PopupWindow popWnd = new PopupWindow(NewGongSiAcitivity.this);
                popWnd.setContentView(contentView);
                popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popWnd.setOutsideTouchable(true);
                popWnd.setTouchable(true);
                popWnd.setBackgroundDrawable(getResources().getDrawable(R.drawable.color_dra_white));

                GridView disctrictGrid = (GridView) contentView.findViewById(R.id.disctrictGrid);

                ComDisctrictAdapter adapter = new ComDisctrictAdapter(mContext, discList);
                disctrictGrid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                disctrictGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        district_id = discList.get(i).getDistrict_id();
                        fuwuquyu.setText(discList.get(i).getDistrict_name());
                        gongsiList.clear();
                        if(companyAdapter!=null){
                            companyAdapter.notifyDataSetChanged();
                            companyAdapter = null;
                        }
                        popWnd.dismiss();
                        getNetData();
                    }
                });

                for(int i=0;i<discList.size();i++){
                    String tempText = fuwuquyu.getText().toString().trim();
                    if(tempText.equals(discList.get(i).getDistrict_name())){
                        adapter.setSelectedPosition(i);
                    }
                }

                popWnd.showAsDropDown(reSearvice, 0, 10);
                break;
            case R.id.relGoClick:
                // 跳转到搜索页面

                // 展开 虚拟键盘
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(NewGongSiAcitivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED);

                // 点击 跳转去搜索页面
                etSearchGongsi.setText("");
                tvCancelSearch.setText("取消");
                ivGongSiDelete.setVisibility(View.GONE);
                nothingData.setVisibility(View.GONE);
                relYouxuan.setVisibility(View.GONE);
                findCompanyLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);

                if(youxuanCompanyAdapter!=null){
                    youxuanSearchGongsiList.clear();
                    youxuanCompanyAdapter.notifyDataSetChanged();
                    youxuanCompanyAdapter = null;
                }

                if(searchCompanyAdapter != null){
                    searchGongsiList.clear();
                    searchCompanyAdapter.loadMoreGongsi(false);
                    searchCompanyAdapter.notifyDataSetChanged();
                    searchCompanyAdapter = null;

                }
                System.gc();

                break;
            case R.id.ivGongSiDelete:
                etSearchGongsi.setText("");
                ivGongSiDelete.setVisibility(View.GONE);
                tvCancelSearch.setText("取消");
                break;
            case R.id.tvCancelSearch:
                if(tvCancelSearch.getText().toString().trim().equals("取消")){
                    // 搜索适配器， 优选适配器置空  均清除列表 返回正常页面
                    if(searchCompanyAdapter != null){
                        searchGongsiList.clear();
                        searchCompanyAdapter.loadMoreGongsi(false);
                        searchCompanyAdapter.notifyDataSetChanged();
                        searchCompanyAdapter = null;
                    }

                    if(youxuanCompanyAdapter!=null){
                        youxuanSearchGongsiList.clear();
                        youxuanCompanyAdapter.notifyDataSetChanged();
                        youxuanCompanyAdapter = null;

                    }
                    System.gc();
                    relYouxuan.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.GONE);
                    nothingData.setVisibility(View.GONE);
                    findCompanyLayout.setVisibility(View.VISIBLE); // 恢复到找装修公司页面
                }else if(tvCancelSearch.getText().toString().trim().equals("搜索")){
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(NewGongSiAcitivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索，请求网络  在搜索页面
                    searchText = etSearchGongsi.getText().toString().trim();
                    pageS = 1;
                    searchGongsiList.clear();
                    if(searchCompanyAdapter != null){
                        searchCompanyAdapter.notifyDataSetChanged();
                        searchCompanyAdapter = null;
                    }
                    if(youxuanCompanyAdapter!=null){
                        youxuanSearchGongsiList.clear();
                        youxuanCompanyAdapter.notifyDataSetChanged();
                        youxuanCompanyAdapter = null;

                    }
                    System.gc();
                    tvCancelSearch.setText("取消");
                    getSearchData(searchText);
                }
                break;
        }
    }


    private void showFadan(){
        if(CacheManager.getCompanyFlag(mContext) == 0){
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

    private Handler uihandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 4:
                    relfindComLayout.setVisibility(View.VISIBLE);
                    findComIcon.setImageResource(R.drawable.gongsifree);
                    findComIcon.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private ShaixuanDialog shaixuanDialog;
    // 筛选弹框数据
    private void showOutDialog(String currentCity){

        shaixuanDialog = new ShaixuanDialog(mContext,R.style.ActionSheetDialogStyle, currentCity);
        shaixuanDialog.setAreaData(discList);
        shaixuanDialog.setJiatingData(jiatingList);
        shaixuanDialog.setShangyeData(shangyeList);
        shaixuanDialog.setOnJaitingClickListener(new ShaixuanDialog.OnJaitingClickListener() {

            @Override
            public void OnJaitingClickListener(String jiatingId) {
                home_id = jiatingId;
            }
        });

        shaixuanDialog.setOnShangyeClickListener(new ShaixuanDialog.OnShangyeClickListener() {
            @Override
            public void OnShangyeClickListener(String shangyeId) {
                tool_id = shangyeId;
            }
        });

        shaixuanDialog.setOnServiceAreaClickListener(new ShaixuanDialog.OnServiceAreaClickListener() {
            @Override
            public void OnServiceAreaClickListener(String quyuId) {
                district_id = quyuId;
            }
        });

        shaixuanDialog.setOnChooseCityListener(new ShaixuanDialog.OnChooseCityListener() {
            @Override
            public void OnChooseCityListener() {
                showChooseCity();
            }
        });

        shaixuanDialog.setOnResetDataListener(new ShaixuanDialog.OnResetDataListener() {
            @Override
            public void OnResetDataListener() {
                district_id = "";
                tool_id = "";
                home_id = "";
            }
        });

        shaixuanDialog.setOnOkListener(new ShaixuanDialog.OnOkListener() {
            @Override
            public void OnOkListener() {

                gongsiCity = shaixuanCity;
                tvGongsiCity.setText(gongsiCity); // 找公司页面的左上角
                EventBusUtil.sendEvent(new Event(EC.EventCode.HOMEACTIVITY_CITY_CODE, gongsiCity));


                fuwuquyu.setText("服务区域");
                gongsiList.clear();
                companyAdapter.notifyDataSetChanged();
                companyAdapter = null;
                page = 1;
                getNetData();
            }
        });
        shaixuanDialog.show();//显示对话框
    }



    private Dialog cityDialog;
    private int clickProvince = 0;

    /**
     * 第二层dialog
     */
    private void showChooseCity(){
        clickProvince = 0;
        cityDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View view = LayoutInflater.from(NewGongSiAcitivity.this).inflate(R.layout.city_layout, null);

        RelativeLayout relBackDialog = (RelativeLayout) view.findViewById(R.id.relBackDialog);
        final TextView chooseProvince = (TextView) view.findViewById(R.id.chooseProvince);
        final TextView chooseCity = (TextView) view.findViewById(R.id.chooseCity);
        final View provincebar = (View) view.findViewById(R.id.provincebar);
        final View citybar = (View) view.findViewById(R.id.citybar);
        final ListView provincedatalist = (ListView) view.findViewById(R.id.provincedatalist);
        final ListView citydatalist = (ListView) view.findViewById(R.id.citydatalist);

        final ChoseProvinceAdapter choseProvinceAdapter = new ChoseProvinceAdapter(mContext, companyProvinceBeanArrayList);
        provincedatalist.setAdapter(choseProvinceAdapter);
        choseProvinceAdapter.notifyDataSetChanged();
        provincedatalist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int provincePosition, long l) {
                EventBusUtil.sendEvent(new Event(EC.EventCode.CHOOSE_PROVINCE_CODE));
                choseProvinceAdapter.setSelectPosition(provincePosition);
                citydatalist.setVisibility(View.VISIBLE);
                provincedatalist.setVisibility(View.GONE);
                chooseProvince.setTextColor(Color.parseColor("#999999"));
                provincebar.setVisibility(View.GONE);
                citybar.setVisibility(View.VISIBLE);
                chooseCity.setTextColor(Color.parseColor("#FF6F20"));

                final ArrayList<CompanyCityBean> cityList = companyProvinceBeanArrayList.get(provincePosition).getCityBeanList();
                final ChoseCityAdapter choseCityAdapter = new ChoseCityAdapter(mContext, cityList);
                citydatalist.setAdapter(choseCityAdapter);
                choseCityAdapter.notifyDataSetChanged();
                citydatalist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int cityPosition, long l) {
                        choseCityAdapter.setSelectPosition(cityPosition);
                        EventBusUtil.sendEvent(new Event(EC.EventCode.SHAIXUAN_CITY_CODE, cityList.get(cityPosition).getCity_name()));
                    }
                });
            }
        });

        chooseProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provincedatalist.setVisibility(View.VISIBLE);
                citydatalist.setVisibility(View.GONE);
                chooseProvince.setTextColor(Color.parseColor("#FF6F20"));
                provincebar.setVisibility(View.VISIBLE);
                citybar.setVisibility(View.GONE);
                chooseCity.setTextColor(Color.parseColor("#999999"));
            }
        });
        chooseCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickProvince > 0){
                    provincedatalist.setVisibility(View.GONE);
                    citydatalist.setVisibility(View.VISIBLE);
                    chooseProvince.setTextColor(Color.parseColor("#999999"));
                    provincebar.setVisibility(View.GONE);
                    citybar.setVisibility(View.VISIBLE);
                    chooseCity.setTextColor(Color.parseColor("#FF6F20"));
                }else {
                    Util.setToast(mContext, "请选择省份");
                }
            }
        });

        relBackDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBusUtil.sendEvent(new Event(EC.EventCode.QUEDING_SHAIXUAN_CITY_CODE));
                cityDialog.dismiss();
            }
        });


        //将布局设置给Dialog
        cityDialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window cityWindow = cityDialog.getWindow();
        //设置Dialog从窗体底部弹出
        cityWindow.setGravity( Gravity.RIGHT);
        //获得窗体的属性
        WindowManager.LayoutParams lp = cityWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        cityWindow.setAttributes(lp);
        cityDialog.show();//显示对话框
    }

    private void getSearchData(String text){
        if(Util.isNetAvailable(mContext)){
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
                                if(jsonObject.getInt("status") == 200){
                                    JSONArray gongsiArr = jsonObject.getJSONArray("data");
                                    gson = new Gson();

                                    List<GongsiItem> tempSearchGongsiList = new ArrayList<GongsiItem>();
                                    for(int i=0;i<gongsiArr.length();i++){
                                        GongsiItem item = gson.fromJson(gongsiArr.getJSONObject(i).toString(), GongsiItem.class);
                                        tempSearchGongsiList.add(item);
                                    }
                                    searchGongsiList.addAll(tempSearchGongsiList);

                                    // 这里用 若加载更多， 为何会连续加载好几个页呢？
                                    nothingData.setVisibility(View.GONE);
                                    relYouxuan.setVisibility(View.GONE);
                                    searchSwip.setVisibility(View.VISIBLE);
                                    searchList.setVisibility(View.VISIBLE);
                                    if (searchCompanyAdapter == null) {
                                        searchCompanyAdapter = new SearchGongSiAdapter(mContext, searchGongsiList);
                                        searchList.setAdapter(searchCompanyAdapter);
                                        searchCompanyAdapter.notifyDataSetChanged();
                                    }else {
                                        searchCompanyAdapter.notifyDataSetChanged();
                                    }

//                                    Util.setErrorLog(TAG, "打印page=" + pageS + " 目前有多少数据呢= " +searchCompanyAdapter.getItemCount());
                                    searchCompanyAdapter.setOnCompanyItemClickListener(new SearchGongSiAdapter.OnCompanyItemClickListener() {
                                        @Override
                                        public void onCompanyItemClickListener(int position) {
                                            Intent it = new Intent(mContext, DecComActivity.class);
                                            it.putExtra("mCompanyId", searchGongsiList.get(position).getId());
                                            startActivity(it);
                                        }
                                    });
                                }else if(jsonObject.getInt("status") == 201){

                                    normalData = true;
                                    youxuan = false;
                                    youxuanSearchGongsiList.clear();
                                    Util.setErrorLog(TAG, "搜素 201");
                                    Util.setToast(mContext, msg);
                                    if(searchCompanyAdapter!=null){
                                        searchCompanyAdapter.setHideMore(true);
                                    }

                                    if(searchCompanyAdapter==null && searchGongsiList.size() == 0){
                                        // 一条数据都没有的情况
                                        nothingData.setVisibility(View.VISIBLE);
                                        relYouxuan.setVisibility(View.GONE);
                                        searchSwip.setVisibility(View.GONE);
                                        searchList.setVisibility(View.GONE);
                                        findCompanyLayout.setVisibility(View.GONE);
                                    }else if(searchCompanyAdapter!=null){
                                        searchCompanyAdapter.notifyDataSetChanged();
                                    }

                                }else if(jsonObject.getInt("status") == 0){
                                    normalData = false;
                                    youxuan = false;
                                    Util.setErrorLog(TAG, "搜素 0");
                                    searchSwip.setVisibility(View.GONE);
                                    Util.setToast(mContext, msg);
                                }else if(jsonObject.getInt("status") == 204){
                                    searchGongsiList.clear();
                                    youxuan = true;
                                    normalData = false;
                                    Util.setErrorLog(TAG, "搜素 204  这个是为你优选的数据");

                                    //显示 noSearchData
                                    JSONArray gongsiArr = jsonObject.getJSONArray("data");
                                    gson = new Gson();
                                    List<GongsiItem> tempYouxuanList = new ArrayList<GongsiItem>();
                                    for(int i=0;i<gongsiArr.length();i++){
                                        GongsiItem item = gson.fromJson(gongsiArr.getJSONObject(i).toString(), GongsiItem.class);
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
                                            Intent it = new Intent(mContext, DecComActivity.class);
                                            it.putExtra("mCompanyId", searchGongsiList.get(i).getId());
                                            startActivity(it);
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


    /**
     * 获取 工装 家装 类型 接口
      */
    private void getToolNetData(){
        String shaixuan = CacheManager.getToolFlag(mContext);
        if(!TextUtils.isEmpty(shaixuan)){
            getShaixuanTool(shaixuan);
        }else {
            if(Util.isNetAvailable(mContext)){
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", Util.getDateToken());
                OKHttpUtil.post(Constant.GET_TOOL_URL, hashMap, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Util.setErrorLog(TAG, "获取刷选中的家庭，商业数据失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getInt("status") == 200){
                                CacheManager.setToolFlag(mContext,json);
                                getShaixuanTool(json);
                            }else {
                                Util.setErrorLog(TAG, "获取筛选中的家庭商业数据失败  status = " + jsonObject.getInt("status"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }

    }


    /**
     * 家庭 商业 类型
     * @param json
     */
    private void getShaixuanTool(String json){
        if(jiatingList.size() == 0 && shangyeList.size() == 0){
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject data = jsonObject.getJSONObject("data");

                JSONArray jiatingArr = data.getJSONArray("home_improvement");
                for(int i=0;i<jiatingArr.length();i++){
                    ShaixuanBean jiating = new ShaixuanBean();
                    jiating.setId(jiatingArr.getJSONObject(i).getString("id"));
                    jiating.setName(jiatingArr.getJSONObject(i).getString("name"));
                    jiatingList.add(jiating);
                }

                JSONArray shangyeArr = data.getJSONArray("tool_improvement");
                for(int i=0;i<shangyeArr.length();i++){
                    ShaixuanBean shangye = new ShaixuanBean();
                    shangye.setId(shangyeArr.getJSONObject(i).getString("id"));
                    shangye.setName(shangyeArr.getJSONObject(i).getString("name"));
                    shangyeList.add(shangye);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    private void getCityJson(){
        String temp = CacheManager.getSaveCityFlag(mContext);
        if("".equals(temp)){
            if(Util.isNetAvailable(mContext)){
                HashMap<String, Object> cityMap = new HashMap<String, Object>();
                cityMap.put("token", Util.getDateToken());
                OKHttpUtil.post(Constant.CITY_JSON, cityMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Util.setToast(mContext, "获取城市市区信息失败");
                            }
                        });
                        Util.setErrorLog(TAG, "--获取城市市区信息失败--");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String cityjson = response.body().string();
                        try {
                            JSONObject object = new JSONObject(cityjson);
                            if(object.getInt("status") == 200){
                                CacheManager.setSaveCityFlag(mContext, cityjson);
                                dealWithCityArr(cityjson);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }else{
            dealWithCityArr(temp);
        }
    }

    private ArrayList<CompanyCityBean> companyCityBeanArrayList = new ArrayList<CompanyCityBean>(); // 全国所有城市都在这里了。
    private ArrayList<CompanyProvinceBean> companyProvinceBeanArrayList = new ArrayList<CompanyProvinceBean>();  // 省
    private void dealWithCityArr(String json){
        if(companyCityBeanArrayList!=null && companyCityBeanArrayList.size()==0){
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray provinceArr = jsonObject.getJSONArray("data"); // 省列表

                for(int province =0;province<provinceArr.length();province++){
                    CompanyProvinceBean provinceBean = new CompanyProvinceBean();
                    provinceBean.setProvince_name(provinceArr.getJSONObject(province).getString("province_name"));
                    JSONArray cityArr = provinceArr.getJSONObject(province).getJSONArray("city");
                    ArrayList<CompanyCityBean> provinceCityList = new ArrayList<CompanyCityBean>();
                    for(int city=0;city<cityArr.length();city++){
                        CompanyCityBean cityBean = new CompanyCityBean();
                        cityBean.setCity_id(cityArr.getJSONObject(city).getString("city_id"));
                        cityBean.setCity_name(cityArr.getJSONObject(city).getString("city_name"));
                        JSONArray disctritArr = cityArr.getJSONObject(city).getJSONArray("district");

                        ArrayList<CompanyDistrictBean> beanList = new ArrayList<CompanyDistrictBean>();
                        for(int distr=0;distr<disctritArr.length();distr++){
                            CompanyDistrictBean dsBean = new CompanyDistrictBean();
                            dsBean.setDistrict_id(disctritArr.getJSONObject(distr).getString("district_id"));
                            dsBean.setDistrict_name(disctritArr.getJSONObject(distr).getString("district_name"));
                            beanList.add(dsBean);
                        }
                        cityBean.setDisctBeanList(beanList);
                        companyCityBeanArrayList.add(cityBean);
                        provinceCityList.add(cityBean);
                    }
                    provinceBean.setCityBeanList(provinceCityList);
                    companyProvinceBeanArrayList.add(provinceBean);
                }
                sortDistrict(gongsiCity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private ViewPager gongsiViewpager;
    private LinearLayout gognsiDotLayout;
    private RecyclerView gongsiRecyclerViewTuijian;
    private ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
    private ArrayList<View> dotViewsList = new ArrayList<View>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private ScheduledExecutorService scheduledExecutorService;
    private void initBannerAdapter(ViewPager bannerPager, LinearLayout dotLayout, List<CompanyBannerItem> banners) {

        dotLayout.removeAllViews();
        urlList.clear();
        List<String> clickId = new ArrayList<String>();
        if (banners != null && banners.size() > 0) {
            for (int i = 0; i < banners.size(); i++) {
                urlList.add(banners.get(i).getContent_url());
                ImageView view = new ImageView(mContext);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(mContext).load(banners.get(i).getImg_url()).into(view);
                imageViewList.add(view);
                ImageView dotView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 8;
                params.rightMargin = 8;
                dotLayout.addView(dotView, params);
                dotViewsList.add(dotView);
                clickId.add(banners.get(i).getId());
            }
            bannerPager.setFocusable(true);
            bannerPager.setAdapter(new GongsiAdViewpagerAdapter(mContext, imageViewList, urlList, clickId));
            bannerPager.setOnPageChangeListener(new MyPageChangeListener(bannerPager));
        }
        startSlide();
    }

    private int currentItem = 0;
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        ViewPager pager;
        boolean isAutoPlay = false;

        public MyPageChangeListener(ViewPager pager) {
            this.pager = pager;
        }

        @Override
        public void onPageScrollStateChanged(int position) {

            switch (position) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (pager.getCurrentItem() == pager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        pager.setCurrentItem(0);
                    } else if (pager.getCurrentItem() == 0 && !isAutoPlay) {
                        pager.setCurrentItem(pager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.selecteds);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.not_select);
                }
            }
        }

    }

    private void startSlide() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ShowTask(), 1, 4, TimeUnit.SECONDS);
    }

    private class ShowTask implements Runnable {
        public ShowTask() {

        }

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageViewList.size();
            handler.obtainMessage().sendToTarget();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            gongsiViewpager.setFocusable(false);
            gongsiViewpager.setFocusableInTouchMode(false);
            gongsiViewpager.clearFocus();
            gongsiViewpager.setCurrentItem(currentItem);
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