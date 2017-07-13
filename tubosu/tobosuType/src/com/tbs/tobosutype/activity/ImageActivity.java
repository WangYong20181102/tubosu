package com.tbs.tobosutype.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.ImageAdapter;
import com.tbs.tobosutype.bean._ImageItem;
import com.tbs.tobosutype.customview.RoundImageView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.HttpServer;
import com.tbs.tobosutype.adapter.utils.ImageLoaderUtil;
import com.tbs.tobosutype.adapter.utils.PrseImageJsonUtil;
import com.tbs.tobosutype.xlistview.XListView;
import com.tbs.tobosutype.xlistview.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * 逛图库页面
 * @author dec
 *
 */
public class ImageActivity extends BaseActivity implements IXListViewListener, OnClickListener {
    private static final String TAG = ImageActivity.class.getSimpleName();
    public static final String REFLASH_DATA_ADAPTER = "com.tobosu.reflash_image_adapter_data";
    private Context mContext;
    /**
     * 顶部搜索布局
     */
    private RelativeLayout rl_search;

    /**
     * 装修公司列表ListView
     */
    private XListView image_listView_company;

    /**
     * 底部上拉加载更多布局
     */
    private LinearLayout ll_loading;

    /**
     * 顶部搜索
     */
    private TextView tv_name;

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

    private RequestParams getListParams;
    private RequestParams adsenseParams;
    private RequestParams getLastMonthCompanyListParams;
    private RequestParams searchCommunityParams;

    private int page = 1;

    private String layout_id;
    private String style_id;
    private String area_id;

    /**
     * 逛图库的适配器数据集合
     */
    private List<HashMap<String, String>> imageDatas;

    /**
     * 逛图库的适配器
     */
    private ImageAdapter imageAdapter;

    /**
     * 清空搜索框
     */
    private ImageView iv_del;

    /**
     * 小区名称
     */
    private String communityName;

    private boolean isSearch = false;

    private View headView;

    /**
     * 顶部广告位ImageView
     */
    private ImageView iv_adse;

    /**
     * 30天活跃榜标题布局
     */
    private RelativeLayout rl_active;

    /**
     * 30天活跃榜的冠亚季军三个公司的布局
     */
    private LinearLayout ll_active;

    /**
     * 冠军
     */
    private RoundImageView riv_champion;

    /**
     * 亚军
     */
    private RoundImageView riv_second;

    /**
     * 季军
     */
    private RoundImageView riv_bronze;

    /**
     * 冠亚季的数据集合
     */
    private List<String> comids;

    /**
     * 冠亚季的公司的图标
     */
    private List<String> logos;

    private String content_url;

    /**
     * 分割线
     */
    private LinearLayout ll_line;

    private ReflashDataBroadcastReceiver receiver;

    private LinearLayout linearlayout_imagesactivity;
    private LinearLayout imagesactivity_netoutview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mContext = ImageActivity.this;
        initView();
        initReceiver();//初始化广播接收
        initData();//初始化数据
        initEvent();//初始化相关点击事件
    }

    /***
     * 初始化页面控件
     */
    private void initView() {
        linearlayout_imagesactivity = (LinearLayout) findViewById(R.id.linearlayout_imagesactivity);
        imagesactivity_netoutview = (LinearLayout) findViewById(R.id.imagesactivity_netoutview);

        headView = View.inflate(this, R.layout.layout_image_head_view, null);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        image_listView_company = (XListView) findViewById(R.id.image_listView_company);//自定义的列表布局
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);//等待加载的图层
        tv_name = (TextView) findViewById(R.id.tv_name);//搜索框
        iv_del = (ImageView) findViewById(R.id.iv_del);//搜索框数据清除
        image_listView_company.addHeaderView(headView);//活跃公司分布
        iv_adse = (ImageView) headView.findViewById(R.id.iv_adse);//广告
        rl_active = (RelativeLayout) headView.findViewById(R.id.rl_active);//热榜标题栏
        ll_active = (LinearLayout) headView.findViewById(R.id.ll_active);//活跃公司列表
        riv_champion = (RoundImageView) headView.findViewById(R.id.riv_champion);
        riv_second = (RoundImageView) headView.findViewById(R.id.riv_second);
        riv_bronze = (RoundImageView) headView.findViewById(R.id.riv_bronze);
        ll_line = (LinearLayout) headView.findViewById(R.id.ll_line);
    }


    private void initReceiver() {
        receiver = new ReflashDataBroadcastReceiver();
        IntentFilter filter = new IntentFilter(REFLASH_DATA_ADAPTER);
        registerReceiver(receiver, filter);
    }

    private void initData() {
        Intent intent = new Intent();
        intent.setAction(Constant.NET_STATE_ACTION);
        Bundle b = null;
        loadDatas();
        if (Constant.checkNetwork(mContext)) {
            linearlayout_imagesactivity.setVisibility(View.VISIBLE);
            imagesactivity_netoutview.setVisibility(View.GONE);
            b = new Bundle();
            b.putString("Net_Flag_message", "Good_Net");
            intent.putExtra("Net_Flag_String_Bundle", b);
            sendBroadcast(intent);
        } else {
            linearlayout_imagesactivity.setVisibility(View.GONE);
            imagesactivity_netoutview.setVisibility(View.VISIBLE);
            imagesactivity_netoutview.findViewById(R.id.tv_reload_data).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    initData();
                }
            });
            b = new Bundle();
            b.putString("Net_Flag_message", "Bad_Net");
            intent.putExtra("Net_Flag_String_Bundle", b);
            sendBroadcast(intent);
        }

    }

    private void loadDatas() {
        tv_name.setText("搜索图库");
        imageDatas = new ArrayList<HashMap<String, String>>();
        image_listView_company.setPullLoadEnable(true);
        image_listView_company.setXListViewListener(this);
        image_listView_company.setSelector(new ColorDrawable(Color.TRANSPARENT));
        String result = getSharedPreferences("IsImageCache", 0).getString("result", "");

        imageAdapter = new ImageAdapter(imageDatas, this, ll_loading);
        image_listView_company.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();


        if (!TextUtils.isEmpty(result)) {
            List<HashMap<String, String>> temDataList = PrseImageJsonUtil.parsingJson(result, mContext, null, "ImageActivity");
            imageDatas.addAll(temDataList);
            onLoad();
        }

        //不要缓存
//		if (!TextUtils.isEmpty(getSharedPreferences("MonthCompanyListCache", 0).getString("result", ""))) {
//			operMonthCompanyList(getSharedPreferences("MonthCompanyListCache", 0).getString("result", ""));
//		}

        requestGetLastMonthCompanyList();
        adsenseParams = AppInfoUtil.getPublicParams(this);
        requestAdsense();
        initParams();
    }


    /***
     * 广告位接口请求
     */
    private void requestAdsense() {
        adsenseParams.add("position", "3"); // 后面参数3是指逛图库的数字标记码
        HttpServer.getInstance().requestPOST(adsenseUrl, adsenseParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
                    if (jsonObject.getInt("error_code") == 0) {
                        iv_adse.setVisibility(View.VISIBLE);
                        JSONArray array = jsonObject.getJSONArray("data");
                        ImageLoaderUtil.loadImage(mContext, iv_adse, array.getJSONObject(0).getString("img_url"));
                        content_url = array.getJSONObject(0).getString("content_url");
                    } else {
                        iv_adse.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }


    /***
     * 活跃榜上的冠亚季军的三家公司请求接口
     */
    private void requestGetLastMonthCompanyList() {
        getLastMonthCompanyListParams = AppInfoUtil.getPublicParams(this);
        HttpServer.getInstance().requestPOST(getLastMonthCompanyListUrl,
                getLastMonthCompanyListParams, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                        String result = new String(body);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("error_code") == 0) {
                                getSharedPreferences("MonthCompanyListCache", 0).edit().putString("result", result).commit();
                                operMonthCompanyList(result);
                            } else {
                                rl_active.setVisibility(View.GONE);
                                ll_active.setVisibility(View.GONE);
                                ll_line.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }
                });
    }

    /***
     *
     * 展示冠亚季军
     * @param result
     */
    private void operMonthCompanyList(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("error_code") == 0) {
                JSONArray data = jsonObject.getJSONArray("data");
                comids = new ArrayList<String>();
                logos = new ArrayList<String>();
                for (int i = 0; i < data.length(); i++) {
                    comids.add(data.getJSONObject(i).getString("comid"));
                    logos.add(data.getJSONObject(i).getString("logosmall"));
                }
                if (logos.size() == 3) {
                    rl_active.setVisibility(View.VISIBLE);
                    ll_active.setVisibility(View.VISIBLE);
                    MyApplication.imageLoader.displayImage(logos.get(0), riv_champion, MyApplication.options);
                    MyApplication.imageLoader.displayImage(logos.get(1), riv_second, MyApplication.options);
                    MyApplication.imageLoader.displayImage(logos.get(2), riv_bronze, MyApplication.options);
                    riv_champion.setOnClickListener(this);
                    riv_second.setOnClickListener(this);
                    riv_bronze.setOnClickListener(this);
                    ll_line.setVisibility(View.VISIBLE);
                } else {
                    rl_active.setVisibility(View.GONE);
                    ll_active.setVisibility(View.GONE);
                    ll_line.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化参数以备请求网络使用
     */
    private void initParams() {
        getListParams = AppInfoUtil.getPublicParams(this);
        getListParams.put("page", page + "");
        getListParams.put("token", AppInfoUtil.getToekn(this));
        getListParams.put("layout_id", layout_id);
        getListParams.put("style_id", style_id);
        getListParams.put("area_id", area_id);
        requestGetList();
    }

    /***
     *  逛图库中显示当地装修公司列表的接口请求
     */
    private void requestGetList() {
        HttpServer.getInstance().requestPOST(getListUrl, getListParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                getSharedPreferences("IsImageCache", 0).edit().putString("result", new String(body)).commit();
                //**********************************************************************测试
                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
                    JSONArray dataArray = jsonObject.getJSONArray("data");
//                    Log.e(TAG, "获取的JSON数据集合" + dataArray.toString());
                    //将获取的数组转换成集合
                    List<_ImageItem> imageItemsList = new ArrayList<_ImageItem>();
                    for (int i = 0; i < dataArray.length(); i++) {
                        Log.e(TAG, "====查看数组到底是啥玩意====" + dataArray.get(i));
                        _ImageItem item = new _ImageItem(dataArray.get(i).toString());

                        imageItemsList.add(item);
                        Log.e(TAG, "获取集合中的数据" + imageItemsList.get(i).getComInfo().getComSimpName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //**********************************************************************测试
                List<HashMap<String, String>> temDataList = PrseImageJsonUtil.parsingJson(new String(body), mContext, null, "ImageActivity");
                if (temDataList.size() == 0 && imageDatas.size() != 0) {
                    Toast.makeText(mContext, "没有更多图库！", Toast.LENGTH_SHORT).show();
                }
                if (temDataList.size() != 0) {
                    if (page == 1) {
                        imageDatas.clear();
                    }
                    for (int i = 0; i < temDataList.size(); i++) {
                        imageDatas.add(temDataList.get(i));
                    }
                }
                onLoad();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

    private void initEvent() {
        rl_search.setOnClickListener(this);
        iv_del.setOnClickListener(this);
        iv_adse.setOnClickListener(this);
    }

    /***
     * 上拉 下拉 加载更多
     */
    private void onLoad() {
        imageAdapter.notifyDataSetChanged();
        image_listView_company.stopRefresh();
        image_listView_company.stopLoadMore();
        image_listView_company.setRefreshTime();
    }

    @Override
    public void onRefresh() {
        page = 1;
        if (isSearch) {
            initSearchParams();
        } else {
            initParams();
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        if (isSearch) {
            initSearchParams();
        } else {
            initParams();
        }
    }

    @Override
    public void onClick(View v) {
        Intent detailIntent = new Intent(this, DecorateCompanyDetailActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.iv_adse:
                Intent webViewIntent = new Intent(mContext, WebViewActivity.class);
                Bundle slideBundle = new Bundle();
                slideBundle.putString("link", content_url);
                webViewIntent.putExtras(slideBundle);
                startActivity(webViewIntent);
                break;
            case R.id.riv_champion:
                MobclickAgent.onEvent(mContext, "click_find_decoration_activite");
                bundle.putString("comid", comids.get(0));
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
                break;
            case R.id.riv_second:
                MobclickAgent.onEvent(mContext, "click_find_decoration_activite");
                bundle.putString("comid", comids.get(1));
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
                break;
            case R.id.riv_bronze:
                MobclickAgent.onEvent(mContext, "click_find_decoration_activite");
                bundle.putString("comid", comids.get(2));
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
                break;
            case R.id.rl_search:
                MobclickAgent.onEvent(mContext, "click_find_decoration_serach_XX");
                Intent intent = new Intent(mContext, SearchImageActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.iv_del:
                tv_name.setText("搜索图库");
                iv_del.setVisibility(View.GONE);
                layout_id = "";
                style_id = "";
                area_id = "";
                isSearch = false;
                initParams();
                break;

            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initParams();
        if (imageAdapter != null) {
            imageAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (data != null && !"".equals(data.getStringExtra("key"))) {
                    String key = data.getStringExtra("key");
                    if ("搜索图库".equals(key)) {
                        isSearch = false;
                    } else {
                        String value = data.getStringExtra("value");
                        String name = data.getStringExtra("name");
                        operResult(key, value, name);
                    }
                }
                break;
        }
    }

    /***
     * 搜索效果图返回的数据 刷新数据
     * @param key
     * @param value
     * @param name
     */
    private void operResult(String key, String value, String name) {
        if ("communityName".equals(key)) {
            communityName = value;
            tv_name.setText(name);
            iv_del.setVisibility(View.VISIBLE);
            isSearch = true;
            imageDatas.clear();
            imageAdapter.notifyDataSetChanged();
            initSearchParams();
            return;
        }
        if ("layout_id".equals(key)) {
            style_id = "";
            area_id = "";
            layout_id = value;
        }
        if ("style_id".equals(key)) {
            style_id = value;
            layout_id = "";
            area_id = "";
        }
        if ("area_id".equals(key)) {
            area_id = value;
            style_id = "";
            layout_id = "";
        }
        tv_name.setText(name);
        iv_del.setVisibility(View.VISIBLE);
        isSearch = false;
        initParams();
    }

    /***
     * 初始化搜索接口的请求参数
     */
    private void initSearchParams() {
        searchCommunityParams = AppInfoUtil.getPublicParams(getApplicationContext());
        page = 1;
        searchCommunityParams.add("page", page + "");
        searchCommunityParams.add("communityName", communityName);
        requestSearch();
    }

    /***
     * 搜索请求
     */
    private void requestSearch() {
        HttpServer.getInstance().requestPOST(searchCommunityUrl, searchCommunityParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
                    if (jsonObject.getInt("error_code") != 0) {
                        Toast.makeText(mContext, "没有搜索到图库！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<HashMap<String, String>> temDataList = PrseImageJsonUtil.parsingJson(new String(body), mContext, null, "ImageActivity");
                if (temDataList.size() == 0 && imageDatas.size() != 0) {
                    Toast.makeText(mContext, "没有搜索到更多的图库！", Toast.LENGTH_SHORT).show();
                } else if (temDataList.size() != 0) {
                    if (page == 1) {
                        imageDatas.clear();
                    }
                    for (int i = 0; i < temDataList.size(); i++) {
                        imageDatas.add(temDataList.get(i));
                    }
                }
                onLoad();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }


    /**
     * 刷新数据广播
     */
    class ReflashDataBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REFLASH_DATA_ADAPTER)) {
                initParams();
                imageAdapter = new ImageAdapter(imageDatas, mContext, ll_loading);
                image_listView_company.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "==onPause==");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "==onRestart==");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "==onStart==");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "==onStop==");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e(TAG, "==onPostResume==");
    }

}
