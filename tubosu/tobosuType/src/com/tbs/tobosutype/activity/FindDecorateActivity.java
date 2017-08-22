package com.tbs.tobosutype.activity;

import android.Manifest;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.AreaAdapter;
import com.tbs.tobosutype.adapter.DecorateListAdapter;
import com.tbs.tobosutype.adapter.RecommandBrandAdapter;
import com.tbs.tobosutype.adapter.SortAdapter;
import com.tbs.tobosutype.adapter.TypeAdapter;
import com.tbs.tobosutype.customview.BaoPopupWindow;
import com.tbs.tobosutype.customview.BusinessLicensePopupWindow;
import com.tbs.tobosutype.customview.FirstLookDecoratDialog;
import com.tbs.tobosutype.customview.GalleryGridView;
import com.tbs.tobosutype.customview.VouchersPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.tbs.tobosutype.utils.PrseJsonUtil;
import com.tbs.tobosutype.utils.Util;
import com.tbs.tobosutype.xlistview.XListView;
import com.tbs.tobosutype.xlistview.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 找装修页面
 *
 * @author dec
 */
public class FindDecorateActivity extends BaseActivity implements IXListViewListener, OnClickListener {

    private static final String TAG = FindDecorateActivity.class.getSimpleName();

    private LinearLayout findDecorateLinearLayout;

    private RelativeLayout rel_find_decorate_titlebar;

    private Context mContext;

    /**
     * 装修工公司ListView
     */
    private XListView decorate_listView_company;

    /**
     * 找装修页面中选择[区域]的布局
     */
    private LinearLayout headview_linearlayout_area;

    /**
     * 找装修页面中选择[区域]的布局
     */
    private LinearLayout linearlayout_area;

    /**
     * 找装修页面中选择[区域]text
     */
    private TextView headview_decorate_textView_area;
    private TextView decorate_textView_area;

    /**
     * 找装修页面中选择[区域]右边的下拉图
     */
    private ImageView headview_imageView_area;

    /**
     * 找装修页面中选择[类型]的布局
     */
    private LinearLayout headview_linearlayout_type;

    /**
     * 找装修页面中选择[类型]的布局
     */
    private LinearLayout linearlayout_type;

    /**
     * 找装修页面中选择[类型]text
     */
    private TextView headview_decorate_textView_type;
    private TextView decorate_textView_type;

    /**
     * 找装修页面中选择[类型]右边的下拉图
     */
    private ImageView headview_imageView_type;

    /**
     * 找装修页面中选择[排序]的布局
     */
    private LinearLayout headview_linearlayout_sort;

    /**
     * 找装修页面中选择[排序]的布局
     */
    private LinearLayout linearlayout_sort;

    /**
     * 找装修页面中选择[排序]text
     */
    private TextView headview_decorate_textView_sort;
    private TextView decorate_textView_sort;

    /**
     * 找装修页面中选择[排序]右边的下拉图
     */
    private ImageView headview_imageView_sort;

    /**
     * 顶部预约装修的大图
     */
    private ImageView iv_yuyue;

    private ImageView imageView_area;

    private ImageView imageView_type;

    private ImageView imageView_sort;

    /**
     * 正在加载数据显示的土拨鼠图
     */
    private LinearLayout page_loading;

    private String[] sorts = {"默认排序", "离我最近", "案例最多", "签单最多"};

    private String[] decorateType = {"全部", "实体认证", "装修保"};

    private int page = 1;
    private int pageSize = 10;

    public static String COMPANY_LIST_PER_NUM = "5";

    private String cityName;

    /**
     * 区域
     */
    private String districtid = "0";

    /**户型  改为 装修保 实体认证*/
//	private String hometype = "0";

    /**
     * 排序
     */
    private String sort = "0";

    private View popupArea = null;
    private View popupType = null;
    private View popupSort = null;

    /**
     * 区域选择listview
     */
    private ListView lvArea;

    /**
     * 类型选择listview
     */
    private ListView lvType;

    /**
     * 排序选择listview
     */
    private ListView lvSort;

    /**
     * 装载区域 类型 排序的listview的window
     */
    private PopupWindow mPopupWindow;

//	/**类型搜索适配器的数据集合*/
//	private ArrayList<HashMap<String, String>> typesList = new ArrayList<HashMap<String, String>>(); // 类型
    /**
     * 区域搜索适配器的数据集合
     */
    private ArrayList<HashMap<String, String>> districtList = new ArrayList<HashMap<String, String>>(); // 区域
    /**
     * 排序搜索适配器的数据集合
     */
    private ArrayList<HashMap<String, String>> sortList = new ArrayList<HashMap<String, String>>(); // 排序

    /**
     * 区域集合
     */
    private ArrayList<String> districts = new ArrayList<String>();

    /**
     * 类型集合 【全部  装修保  实体认证】
     */
    private ArrayList<String> types = new ArrayList<String>();

    /**
     * 类型集合
     */
    private ArrayList<String> sortNames = new ArrayList<String>();

    /**
     * 区域适配器
     */
    private AreaAdapter areaWindowAdapter;

    /**
     * 类型适配器  【全部  装修保  实体认证】
     */
    private TypeAdapter typeWindowAdapter;

    /**
     * 排序适配器
     */
    private SortAdapter sortWindowAdapter;

//    /**
//     * 装修公司的参数对象
//     */
//    private RequestParams mapParams;
    private HashMap<String,String> mapParams;


    /**
     * 品牌logo的参数对象
     */
    private RequestParams getAdsParams;

    /**
     * 广告的参数对象
     */
    private RequestParams adsenseParams;

    /**
     * 装修公司列表接口
     */
//    private String dataUrl = Constant.TOBOSU_URL + "/tapp/company/company_list";

    /**
     * 品牌logo接口
     */
    private String getAdsUrl = Constant.TOBOSU_URL + "tapp/util/getAds";

    /**
     * 广告位接口
     */
    private String adsenseUrl = Constant.TOBOSU_URL + "tapp/util/adsense";

    /**
     * 装修公司列表
     */
    private ArrayList<HashMap<String, String>> decorateCompanyList = new ArrayList<HashMap<String, String>>();

    /**
     * 找装修页面上半部分布局
     */
    private View headView;

    /**
     * 实体认证 布局
     */
    private LinearLayout ll_business_license;

    /**
     * 装修保 布局
     */
    private LinearLayout ll_image_bao;

    /**
     * 用券抵现 布局
     */
    private LinearLayout ll_images_vouchers;

    /**
     * 找装修页面中选择[区域][类型][排序]的布局
     */
    private LinearLayout decorate_company_select_items;

    /**
     * 推荐品牌text信息布局
     */
    private LinearLayout LL_recommand_basic;

    /**
     * 推荐品牌gallery布局
     */
    private LinearLayout ll_recommandBrand;

    /**
     * 推荐品牌布局内的公司
     */
    private GalleryGridView recomandBrandGridView;

    /**
     * 推荐品牌公司适配器
     */
    private RecommandBrandAdapter recommandBrandAdapter;

    /**
     * 推荐品牌公司名称集合
     */
    private ArrayList<HashMap<String, String>> recommandBrandListData;

    /**
     * 右上角搜索 装修公司
     */
    private ImageView iv_search_decoration;

    /**
     * 找装修页面的广告位
     */
    private ImageView iv_advertising_banner_decoration;

    /**
     * 左上角城市
     */
    private ImageView iv_select_city;

    /**
     * 左上角所显示的城市名称
     */
    private TextView tv_city;

    private String content_url;

    /**
     * 装修公司列表适配器
     */
    private DecorateListAdapter decorateAdapter;

    /**
     * 重新保存本地的城市参数
     */
    private SharedPreferences saveCitySharePre;

    /**
     * 加载数据碰上没有网络时 出现断网自定义view
     */
    private LinearLayout netOutView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_decorate);
        mContext = FindDecorateActivity.this;
        initView();
        initData();
        inintReceiver();
        initEvent();
    }

    private void initView() {
        /*-------------------顶部区域------------------*/
        // 顶部topbar
        rel_find_decorate_titlebar = (RelativeLayout) findViewById(R.id.rel_find_decorate_titlebar);
        tv_city = (TextView) findViewById(R.id.tv_city_decorate);
        iv_select_city = (ImageView) findViewById(R.id.iv_select_city);
        iv_search_decoration = (ImageView) findViewById(R.id.iv_search_decoration);
        /*-------------------上部区域------------------*/
        // 上部 布局
        headView = getLayoutInflater().inflate(R.layout.decorate_listview_headview, null);
        // 我要装修
        iv_yuyue = (ImageView) headView.findViewById(R.id.iv_yuyue_findv_decorate_acitivty);

        ll_business_license = (LinearLayout) headView.findViewById(R.id.ll_business_license);
        ll_image_bao = (LinearLayout) headView.findViewById(R.id.ll_image_bao);
        ll_images_vouchers = (LinearLayout) headView.findViewById(R.id.ll_images_vouchers);

        iv_advertising_banner_decoration = (ImageView) headView.findViewById(R.id.iv_advertising_banner_decoration);

        LL_recommand_basic = (LinearLayout) headView.findViewById(R.id.LL_recommand_basic);
        ll_recommandBrand = (LinearLayout) headView.findViewById(R.id.linearlayout_decorate_class);
        recomandBrandGridView = (GalleryGridView) headView.findViewById(R.id.decorate_gridview);
        /*-------------------装修公司列表筛选条件区域------------------*/
        headview_linearlayout_area = (LinearLayout) headView.findViewById(R.id.linearlayout_area);
        headview_linearlayout_type = (LinearLayout) headView.findViewById(R.id.linearlayout_type);
        headview_linearlayout_sort = (LinearLayout) headView.findViewById(R.id.linearlayout_sort);
        headview_decorate_textView_area = (TextView) headView.findViewById(R.id.decorate_textView_area);
        headview_decorate_textView_type = (TextView) headView.findViewById(R.id.decorate_textView_type);
        headview_decorate_textView_sort = (TextView) headView.findViewById(R.id.decorate_textView_sort);
        headview_imageView_area = (ImageView) headView.findViewById(R.id.imageView_area);
        headview_imageView_type = (ImageView) headView.findViewById(R.id.imageView_type);
        headview_imageView_sort = (ImageView) headView.findViewById(R.id.imageView_sort);
        // 选择装修公司的条件布局 区域&类型&排序
        decorate_company_select_items = (LinearLayout) findViewById(R.id.decorate_company_select_items);
        linearlayout_area = (LinearLayout) findViewById(R.id.linearlayout_area);
        linearlayout_type = (LinearLayout) findViewById(R.id.linearlayout_type);
        linearlayout_sort = (LinearLayout) findViewById(R.id.linearlayout_sort);

        decorate_textView_area = (TextView) findViewById(R.id.decorate_textView_area);
        decorate_textView_type = (TextView) findViewById(R.id.decorate_textView_type);
        decorate_textView_sort = (TextView) findViewById(R.id.decorate_textView_sort);

        imageView_area = (ImageView) findViewById(R.id.imageView_area);
        imageView_type = (ImageView) findViewById(R.id.imageView_type);
        imageView_sort = (ImageView) findViewById(R.id.imageView_sort);

		/*-------------------装修公司列表区域------------------*/
        //找装修列表
        decorate_listView_company = (XListView) findViewById(R.id.decorate_listView_company);
        decorate_listView_company.addHeaderView(headView);
        /*-------------------------------------*/
        //加载更多公司动画
        page_loading = (LinearLayout) findViewById(R.id.ll_loading);
        netOutView = (LinearLayout) findViewById(R.id.find_decorate_netoutview);
        findDecorateLinearLayout = (LinearLayout) findViewById(R.id.linearlayout_find_decorate);


        // 第一次浏览此页面
        if (getSharedPreferences("FirstLookDecorat", 0).getBoolean("FirstLookDecorat", true)) {
            FirstLookDecoratDialog.Builder builder = new FirstLookDecoratDialog.Builder(this);
            builder.create().show();
            getSharedPreferences("FirstLookDecorat", 0).edit().putBoolean("FirstLookDecorat", false).commit();
        }
        rel_find_decorate_titlebar.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    /**
     * 初始化公司列表adapter
     */
    private void initDecorateListAdapter() {
        if (decorateAdapter == null) {
            decorateAdapter = new DecorateListAdapter(mContext, decorateCompanyList);
            decorateAdapter.setList(decorateCompanyList);
            decorate_listView_company.setAdapter(decorateAdapter);
        }

        decorateAdapter.notifyDataSetChanged();
        decorate_listView_company.setPullLoadEnable(true);
        decorate_listView_company.setXListViewListener(this);
        decorate_listView_company.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    private void initData() {

        // 获取城市名称
        cityName = AppInfoUtil.getCityName(getApplicationContext());
        tv_city.setText(cityName);
        initCacheCompanyList();


        Intent intent = new Intent();
        intent.setAction(Constant.NET_STATE_ACTION);
        Bundle b = null;

        loadData();

        if (Constant.checkNetwork(mContext)) { // 有网络
            netOutView.setVisibility(View.GONE);
            findDecorateLinearLayout.setVisibility(View.VISIBLE);
            b = new Bundle();
            b.putString("Net_Flag_message", "Good_Net");
            intent.putExtra("Net_Flag_String_Bundle", b);
            sendBroadcast(intent);
        } else {
            // 无网络
            findDecorateLinearLayout.setVisibility(View.GONE);
            netOutView.setVisibility(View.VISIBLE);
            page_loading.setVisibility(View.GONE);
            netOutView.findViewById(R.id.tv_reload_data).setOnClickListener(new OnClickListener() {

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

    /**
     * 首次进入该页面 先使用之前保存下来的缓存
     */
    private void initCacheCompanyList() {
        String result = getSharedPreferences("CompanyCache", 0).getString("jsonResult", "");
        if (!TextUtils.isEmpty(result)) {
            operDecorationCompany(result);
        }
    }

    /***
     * 装修公司列表接口
     */
    private void loadData() {
        // -------------请求网络获取公司列表 ---------
        initGetCompanyListParams();
        requestDecoratePost(mapParams);

        // -------------请求网络获取公司列表 ---------


        decorate_listView_company.setPullLoadEnable(true);
        decorate_listView_company.setXListViewListener(FindDecorateActivity.this);
        decorate_listView_company.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 2) {
                    decorate_company_select_items.setVisibility(View.VISIBLE);
                    return;
                }
                decorate_company_select_items.setVisibility(View.GONE);
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
        decorate_listView_company.setSelector(new ColorDrawable(Color.TRANSPARENT));
        /*--------------------------------归 类-----------------------------*/


        // 品牌logo接口请求 ------ 归 类-----------
        initGetAdsParamsRequest();
		/*----------------归 类-----------*/


        // 广告接口请求------ 归 类-----------
        initGetAdsenseParamsRequset();
		/*----------------归 类-----------*/


        // 所有的筛选条件适配器数据集合先清除
//		typesList.clear();
        districtList.clear();
        sortList.clear();

        /***
         * 这个是排序文字是本地固定写死的 并不是网络给的
         *
         * 	 但是 若要请求网络时 相应的请求参数字段则需要额外相应上传， 规则如下：
         * 	 	默认排序：不传或者任意不跟下面三个相同即可
         * 		离我最近：dis
         * 		案例最多：casenormalcount
         * 		签单最多：ordercount
         */
        for (int i = 0; i < sorts.length; i++) {
            HashMap<String, String> sortMap = new HashMap<String, String>();
            sortMap.put("id", i + ""); // 这是原来的，我不做修改
            sortMap.put("class_name", sorts[i]);
            sortList.add(sortMap);
        }

        //[@"默认排序", @"离我最近", @"案例最多", @"签单最多"]
//      排序方式：casenormalcount：案例数排序；ordercount：订单数排序，dis：距离排序 
    }


    /***
     * 初始化装修公司列表接口的参数
     */
    private void initGetCompanyListParams() {
        mapParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
        mapParams.put("page", page +"");
        mapParams.put("pageSize", pageSize + "");
        mapParams.put("city", cityName);
    }


    /***
     * 装修公司列表接口请求
     *
     * @param params
     */
    private void requestDecoratePost(HashMap<String, String> params) {
        if (Util.isNetAvailable(mContext)) {

            OKHttpUtil okHttpUtil = new OKHttpUtil();
            okHttpUtil.post(Constant.FIND_DECORATE_COMPANY_URL, params, new OKHttpUtil.BaseCallBack() {
                @Override
                public void onSuccess(Response response, String json) {
                    Log.e(TAG, "数据请求=====" + json);
//                    String result = new String(responseBody);
                    Util.setLog(TAG, "requestDecoratePost 请求网络返回成功 -->>> " + json);
                    try {
                        JSONObject obj = new JSONObject(json);
                        if (obj.getInt("error_code") == 0) {
                            // 缓存请求的结果
                            getSharedPreferences("CompanyCache", 0).edit().putString("jsonResult", json).commit();
                            operDecorationCompany(json);
                        } else {
                            handler.sendEmptyMessage(201);
                            Util.setLog(TAG, "201请求网络返回有问题 ===>>>>" + response.message());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(Request request, IOException e) {
                    Util.setLog(TAG, "onFail请求网络返回有问题 ===>>>>" + request.toString() + "<<<======>>>>" + AppInfoUtil.getLat(mContext) + "<<<======>>>>" + AppInfoUtil.getLng(mContext) + "<<<<===");
                    handler.sendEmptyMessage(201);
                }

                @Override
                public void onError(Response response, int code) {
                    Util.setLog(TAG, "onError请求网络返回有问题 ===>>>>" + response.message() + "<<<======>>>>" + AppInfoUtil.getLat(mContext) + "<<<<======>>>>" + AppInfoUtil.getLng(mContext) + "<<<<===");
                }
            });
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initDecorateListAdapter();
                    onLoad();
                    break;
                case 201:
//                    initCacheCompanyList();
                    Util.setToast(mContext, "服务器繁忙，请稍后再试~");
                    decorate_listView_company.stopRefresh();
                    decorate_listView_company.stopLoadMore();

                    break;
            }
        }
    };

    /**
     * 广告接口请求
     */
    private void initGetAdsenseParamsRequset() {
        adsenseParams = AppInfoUtil.getPublicParams(getApplicationContext());
        adsenseParams.put("position", "4");
        requestAdsense();
    }

    /**
     * 品牌logo接口请求
     */
    private void initGetAdsParamsRequest() {
        getAdsParams = AppInfoUtil.getPublicParams(getApplicationContext());
        requestGetAds();
    }


    /***
     * 请求的是广告位接口
     */
    private void requestAdsense() {
        HttpServer.getInstance().requestPOST(adsenseUrl, adsenseParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] result) {
                try {
                    JSONObject jsonString = new JSONObject(new String(result));
                    if (jsonString.getInt("error_code") == 0) {
                        JSONArray jsonArray = jsonString.getJSONArray("data");
                        ImageLoaderUtil.loadImage(mContext, iv_advertising_banner_decoration, jsonArray.getJSONObject(0).getString("img_url"));
                        content_url = jsonArray.getJSONObject(0).getString("content_url");
                        iv_advertising_banner_decoration.setVisibility(View.VISIBLE);
                    } else {
                        iv_advertising_banner_decoration.setVisibility(View.GONE);
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
     * 请求的是品牌logo接口
     */
    private void requestGetAds() {
        HttpServer.getInstance().requestPOST(getAdsUrl, getAdsParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                String result = new String(body);
//                System.out.println("---------装修公司--getAdsUrl-->>>" + result + "<<<");
                try {
                    JSONObject jsonString = new JSONObject(result);
                    recommandBrandListData = new ArrayList<HashMap<String, String>>();
                    if (jsonString.getInt("error_code") == 0) {
                        JSONArray jsonArray = jsonString.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("comid", jsonArray.getJSONObject(i).getString("comid"));
                            map.put("img", jsonArray.getJSONObject(i).getString("logosmall"));
                            map.put("comsimpname", jsonArray.getJSONObject(i).getString("comsimpname"));
                            recommandBrandListData.add((HashMap<String, String>) map);
                        }

                        // 取控件textView当前的布局参数
                        FrameLayout.LayoutParams frameParams = (android.widget.FrameLayout.LayoutParams) ll_recommandBrand.getLayoutParams();
                        frameParams.height = DensityUtil.dip2px(mContext, 100);// 控件的高强制设成90dp
                        frameParams.width = DensityUtil.dip2px(mContext, 110) * recommandBrandListData.size() + 1;// 控件的宽强制设成110dp*数据的个数
                        ll_recommandBrand.setLayoutParams(frameParams); // 使设置好的布局参数应用到控件
                        recommandBrandAdapter = new RecommandBrandAdapter(mContext, recommandBrandListData);

                        if (recommandBrandListData.size() > 0) {
                            LL_recommand_basic.setVisibility(View.VISIBLE);
                        } else {
                            LL_recommand_basic.setVisibility(View.GONE);
                        }
                        //设置数据显示的列数
                        recomandBrandGridView.setNumColumns(recommandBrandListData.size());
                        recomandBrandGridView.setAdapter(recommandBrandAdapter);
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

    private void initEvent() {
        linearlayout_area.setOnClickListener(this);
        linearlayout_type.setOnClickListener(this);
        linearlayout_sort.setOnClickListener(this);
        headview_linearlayout_area.setOnClickListener(this);
        headview_linearlayout_type.setOnClickListener(this);
        headview_linearlayout_sort.setOnClickListener(this);
        ll_business_license.setOnClickListener(this);
        ll_image_bao.setOnClickListener(this);
        ll_images_vouchers.setOnClickListener(this);
        iv_search_decoration.setOnClickListener(this);
        iv_select_city.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        iv_yuyue.setOnClickListener(this);
        iv_advertising_banner_decoration.setOnClickListener(this);
        recomandBrandGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        recomandBrandGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String comid = recommandBrandListData.get(position).get("comid");
                Util.setLog(TAG, "songchengcai传过去" + comid);
                Intent detailIntent = new Intent(mContext, DecorateCompanyDetailActivity.class);
                Bundle bundle = new Bundle();
                // 从列表带过去的公司id
                bundle.putString("comid", comid);
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
            }
        });

        recomandBrandGridView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.clearFocus();
                return false;
            }
        });
    }

    /***
     * 解析类型得到筛选列表的
     */
    // FIXME
    private List<HashMap<String, String>> initDecorateType()/*jsonToHomeServiceList(String jsonString)*/ {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map0 = new HashMap<String, String>();
        map0.put("pinyin", "0");
        map0.put("name", "全部类型");
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("pinyin", "1");
        map1.put("name", "实体认证");
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("pinyin", "2");
        map2.put("name", "装修保");
        list.add(map0);
        list.add(map1);
        list.add(map2);
//		try {
//
//			HashMap<String, String> map0 = new HashMap<String, String>();
//			map0.put("pinyin", "0");
//			map0.put("name", "全部类型");
//
//			JSONObject object1 = new JSONObject(jsonString);
//			JSONObject object2 = object1.getJSONObject("data");
//			JSONArray array = object2.getJSONArray("homeServiceList");
//			list.add(map0);
//			for (int i = 0; i < array.length(); i++) {
//				HashMap<String, String> dataMap = new HashMap<String, String>();
//				JSONObject dataString = array.getJSONObject(i);
//				dataMap.put("name", dataString.getString("name"));
//				dataMap.put("pinyin", dataString.getString("pinyin"));
////				Log.d(TAG, "name--"+dataMap.get("name"));
////				Log.d(TAG, "pinyin--"+dataMap.get("pinyin"));
//				list.add(dataMap);
//			}
//			return list;
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

        return null;

    }

    /***
     * json解析  为区域adapter得到list集合数据
     */
    private List<HashMap<String, String>> jsonToDistrictList(String jsonString) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {

            HashMap<String, String> map0 = new HashMap<String, String>();
            map0.put("districtid", "0");
            map0.put("districtname", "全部区域");

            JSONObject object1 = new JSONObject(jsonString);
            if (object1.getInt("error_code") == 200) {
                Util.setLog(TAG, object1.getString("msg"));
            } else if (object1.getInt("error_code") == 0) {
                JSONObject object2 = object1.getJSONObject("data");
                JSONArray array = object2.getJSONArray("district");
                list.add(map0);
                for (int i = 0; i < array.length(); i++) {
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    JSONObject dataString = array.getJSONObject(i);
                    dataMap.put("districtid", dataString.getString("districtid"));
                    dataMap.put("districtname", dataString.getString("districtname"));
                    list.add(dataMap);
                }
                return list;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearlayout_area:
                if (districtList.size() == 0) {
                    return;
                }
                getAreaPopupwindow();
                districts.clear();
                for (int i = 0; i < districtList.size(); i++) {
                    districts.add(districtList.get(i).get("districtname").toString().trim());
                }

                areaWindowAdapter = new AreaAdapter(mContext, districts);
                areaWindowAdapter.notifyDataSetChanged();
                lvArea.setAdapter(areaWindowAdapter);

                if (mPopupWindow.isShowing()) {
                    imageView_area.setImageResource(R.drawable.image_down);
                    headview_imageView_area.setImageResource(R.drawable.image_down);
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(v);
                    imageView_area.setImageResource(R.drawable.image_up);
                    headview_imageView_area.setImageResource(R.drawable.image_up);
                }

                break;
            case R.id.linearlayout_type:
                getTypePopupwindow();
//			if(districtList.size()==0){
//				return;
//			}
//			if (types.size() == 0) {
//				for (int i = 0; i < typesList.size(); i++) {
//					types.add(typesList.get(i).get("name").toString().trim());
//				}
//			}

                if (types.size() == 0) {
                    for (int i = 0; i < decorateType.length; i++) {
                        types.add(decorateType[i]);
                    }
                }
                typeWindowAdapter = new TypeAdapter(mContext, types);
                typeWindowAdapter.notifyDataSetChanged();
                lvType.setAdapter(typeWindowAdapter);

                if (mPopupWindow.isShowing()) {
                    imageView_type.setImageResource(R.drawable.image_down);
                    headview_imageView_type.setImageResource(R.drawable.image_down);
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(v);
                    imageView_type.setImageResource(R.drawable.image_up);
                    headview_imageView_type.setImageResource(R.drawable.image_up);
                }
                break;
            case R.id.linearlayout_sort:
                getSortPopupwindow();
//			if(districtList.size()==0){
//				return;
//			}
                if (sortNames.size() == 0) {
                    for (int i = 0; i < sortList.size(); i++) {
                        sortNames.add(sortList.get(i).get("class_name").toString().trim());
                    }
                }
                sortWindowAdapter = new SortAdapter(mContext, sortNames);
                sortWindowAdapter.notifyDataSetChanged();
                lvSort.setAdapter(sortWindowAdapter);

                if (mPopupWindow.isShowing()) {
                    imageView_sort.setImageResource(R.drawable.image_down);
                    headview_imageView_sort.setImageResource(R.drawable.image_down);
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(v);
                    imageView_sort.setImageResource(R.drawable.image_up);
                    headview_imageView_sort.setImageResource(R.drawable.image_up);
                }
                break;
            case R.id.iv_advertising_banner_decoration:
                Intent detailIntent = new Intent(mContext, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("link", content_url);
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
                break;
            case R.id.iv_yuyue_findv_decorate_acitivty:
                MobclickAgent.onEvent(mContext, "click_find_com_decoration_succed");
                startActivity(new Intent(mContext, FreeYuyueActivity.class));
                break;
            case R.id.tv_city_decorate:
            case R.id.iv_select_city:
                Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle b = new Bundle();
                b.putString("fromFindCompany", "64");
                selectCityIntent.putExtra("findDecorateCompanySelectcityBundle", b);
                startActivityForResult(selectCityIntent, 3);
                break;
            case R.id.iv_search_decoration: // 搜索装修公司
                MobclickAgent.onEvent(mContext, "click_find_com_serach");
                startActivity(new Intent(mContext, SearchDecorateActivity.class));
                break;
            case R.id.ll_business_license:
                BusinessLicensePopupWindow businessLicensePopupWindow = new BusinessLicensePopupWindow(mContext);
                businessLicensePopupWindow.showAtLocation(findViewById(R.id.ll_business_license), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_image_bao:
                BaoPopupWindow baoPopupWindow = new BaoPopupWindow(mContext);
                baoPopupWindow.showAtLocation(findViewById(R.id.ll_image_bao), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_images_vouchers:
                VouchersPopupWindow vouchersPopupWindow = new VouchersPopupWindow(mContext);
                vouchersPopupWindow.showAtLocation(findViewById(R.id.ll_images_vouchers), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            default:
                break;
        }
    }

    /***
     * 选择区域的window
     */
    public void getAreaPopupwindow() {
        popupArea = getLayoutInflater().inflate(R.layout.item_decorate_area, null);
        lvArea = (ListView) popupArea.findViewById(R.id.item_decorate_lvarea);
        lvArea.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                districtid = (String) districtList.get(position).get("districtid");
                Log.d(TAG, "返回给我的districtid-->" + districtid);
                String districtname = (String) districtList.get(position).get("districtname");
                if (position == 0) {
                    decorate_textView_area.setText("全部区域");
                    decorate_textView_area.setTextColor(getResources().getColor(R.color.mycolor));
                    imageView_area.setImageResource(R.drawable.image_down);
                    headview_decorate_textView_area.setText("全部区域");
                    headview_decorate_textView_area.setTextColor(getResources().getColor(R.color.mycolor));
                    headview_imageView_area.setImageResource(R.drawable.image_down);
                    areaWindowAdapter.setSelectedPosition(0);
                    districtid = "0";
                } else {
                    decorate_textView_area.setText(districtname);
                    decorate_textView_area.setTextColor(getResources().getColor(R.color.layout_color));
                    imageView_area.setImageResource(R.drawable.image_down);
                    headview_decorate_textView_area.setText(districtname);
                    headview_decorate_textView_area.setTextColor(getResources().getColor(R.color.layout_color));
                    headview_imageView_area.setImageResource(R.drawable.image_down);
                    areaWindowAdapter.setSelectedPosition(position);
                }

                initGetCompanyListParams();
                mapParams.put("districtid", districtid);
                Log.d(TAG, "我收到传上去的districtid-->" + districtid);
                requestDecoratePost(mapParams);
                if (decorateAdapter != null) {
                    decorateAdapter.notifyDataSetChanged();
                }
//				initDecorateListAdapter(); 不需要重新new适配器 直接刷新即可
                requestGetAds();
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow = new PopupWindow(popupArea, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        mPopupWindow.setBackgroundDrawable(dw);
//		mPopupWindow.setAnimationStyle(R.style.custom_popupwindow_animstyle);

        // 需要设置，点击之后取消popupview，即使点击外面，也可以捕获事件
        popupArea.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPopupWindow.isShowing()) {
                    imageView_area.setImageResource(R.drawable.image_down);
                    headview_imageView_area.setImageResource(R.drawable.image_down);
                    mPopupWindow.dismiss();
                }
                return false;
            }
        });
    }


    /***
     * 选择类型的window  全部 【不传】;  装修保jjb_logo  0或1; 实体认证 certification 0或1
     */
    public void getTypePopupwindow() {
        popupType = getLayoutInflater().inflate(R.layout.item_decorate_type, null);
        lvType = (ListView) popupType.findViewById(R.id.item_decorate_lvtype);
        lvType.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initGetCompanyListParams();
                switch (position) {
                    case 0:
                        // 全部
                        decorate_textView_type.setText(decorateType[0]);
                        decorate_textView_type.setTextColor(getResources().getColor(R.color.mycolor));
                        imageView_type.setImageResource(R.drawable.image_down);
                        headview_decorate_textView_type.setText(decorateType[0]);
                        headview_decorate_textView_type.setTextColor(getResources().getColor(R.color.mycolor));
                        headview_imageView_type.setImageResource(R.drawable.image_down);
                        mapParams.put("jjb_logo", "0");
                        mapParams.put("certification", "0");
                        break;
                    case 1:
                        //实体认证
                        decorate_textView_type.setText(decorateType[1]);
                        decorate_textView_type.setTextColor(getResources().getColor(R.color.layout_color));
                        imageView_type.setImageResource(R.drawable.image_down);
                        headview_decorate_textView_type.setText(decorateType[1]);
                        headview_decorate_textView_type.setTextColor(getResources().getColor(R.color.layout_color));
                        headview_imageView_type.setImageResource(R.drawable.image_down);
                        mapParams.put("jjb_logo", "0");
                        mapParams.put("certification", "1");
                        break;
                    case 2:
                        //装修保
                        decorate_textView_type.setText(decorateType[2]);
                        decorate_textView_type.setTextColor(getResources().getColor(R.color.layout_color));
                        imageView_type.setImageResource(R.drawable.image_down);
                        headview_decorate_textView_type.setText(decorateType[2]);
                        headview_decorate_textView_type.setTextColor(getResources().getColor(R.color.layout_color));
                        headview_imageView_type.setImageResource(R.drawable.image_down);
                        mapParams.put("jjb_logo", "1");
                        mapParams.put("certification", "0");
                        break;
                    default:
                        break;
                }

                typeWindowAdapter.setSelectedPosition(position);

//				Log.d(TAG, "我收到传上去的hometype-->"+hometype);
                requestDecoratePost(mapParams);
                if (decorateAdapter != null) {
                    decorateAdapter.notifyDataSetChanged();
                }
                requestGetAds();
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow = new PopupWindow(popupType, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setAnimationStyle(R.style.custom_popupwindow_animstyle);

        // 需要设置，点击之后取消popupview，即使点击外面，也可以捕获事件
        popupType.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPopupWindow.isShowing()) {
                    imageView_type.setImageResource(R.drawable.image_down);
                    headview_imageView_type.setImageResource(R.drawable.image_down);
                    mPopupWindow.dismiss();
                }
                return false;
            }
        });

    }

    /***
     * 选择排序的window
     */
    public void getSortPopupwindow() {
        popupSort = getLayoutInflater().inflate(R.layout.item_decorate_sort, null);
        lvSort = (ListView) popupSort.findViewById(R.id.item_decorate_lvsort);
        lvSort.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sort = (String) sortList.get(position).get("id");

                String name = (String) sortList.get(position).get("class_name");
                if (position == 0) {
                    decorate_textView_sort.setText("默认排序");
                    decorate_textView_sort.setTextColor(getResources().getColor(R.color.mycolor));
                    imageView_sort.setImageResource(R.drawable.image_down);
                    headview_decorate_textView_sort.setText("默认排序");
                    headview_decorate_textView_sort.setTextColor(getResources().getColor(R.color.mycolor));
                    headview_imageView_sort.setImageResource(R.drawable.image_down);
                    sortWindowAdapter.setSelectedPosition(0);
                    sort = "0";
                } else {
                    decorate_textView_sort.setText(name);
                    decorate_textView_sort.setTextColor(getResources().getColor(R.color.layout_color));
                    imageView_sort.setImageResource(R.drawable.image_down);
                    headview_decorate_textView_sort.setText(name);
                    headview_decorate_textView_sort.setTextColor(getResources().getColor(R.color.layout_color));
                    headview_imageView_sort.setImageResource(R.drawable.image_down);
                    sortWindowAdapter.setSelectedPosition(position);
                }

                if ("1".equals(sort)) {
                    sort = "dis";
                } else if ("2".equals(sort)) {
                    sort = "casenormalcount";
                } else if ("3".equals(sort)) {
                    sort = "ordercount";
                } else {
                    mPopupWindow.dismiss();
                }

                Log.d(TAG, "返回给我的sort-->" + sort);

                initGetCompanyListParams();
                mapParams.put("sort", sort);
                Log.d(TAG, "我收到传上去的sort-->" + sort);
                requestDecoratePost(mapParams);
                if (decorateAdapter != null) {
                    decorateAdapter.notifyDataSetChanged();
                }
                requestGetAds();
                mPopupWindow.dismiss();

            }
        });

        mPopupWindow = new PopupWindow(popupSort, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setAnimationStyle(R.style.custom_popupwindow_animstyle);

        // 需要设置，点击之后取消popupview，即使点击外面，也可以捕获事件
        popupSort.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPopupWindow.isShowing()) {
                    imageView_sort.setImageResource(R.drawable.image_down);
                    headview_imageView_sort.setImageResource(R.drawable.image_down);
                    mPopupWindow.dismiss();
                }
                return false;
            }
        });

    }

    @Override
    public void onRefresh() {
        page = 1;
        initGetCompanyListParams();
        requestDecoratePost(mapParams);
//		initDecorateListAdapter();
        if (decorateAdapter != null) {
            decorateAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        initGetCompanyListParams();
        requestDecoratePost(mapParams);
//		initDecorateListAdapter();
        if (decorateAdapter != null) {
            decorateAdapter.notifyDataSetChanged();
        }

    }

    private void onLoad() {
        decorate_listView_company.stopRefresh();
        decorate_listView_company.stopLoadMore();
        decorate_listView_company.setRefreshTime();
        if (decorateAdapter != null) {
            decorateAdapter.notifyDataSetChanged();
        }
    }


    /***
     * 找装修公司的列表
     *
     * @param result
     */
    private void operDecorationCompany(String result) {

        // 装修公司json解析后的列表
        ArrayList<HashMap<String, String>> temDataList = new ArrayList<HashMap<String, String>>();
        // 区域列表
        ArrayList<HashMap<String, String>> districtDataList = new ArrayList<HashMap<String, String>>();
        //
//		ArrayList<HashMap<String, String>> homeDataList = new ArrayList<HashMap<String, String>>();


        // 找装修页面 解析找到公司的json得到公司数据
        temDataList = PrseJsonUtil.jsonToComList(result);

        if (temDataList.size() == 0 && decorateCompanyList.size() != 0) {
            Toast.makeText(mContext, "没有更多装修公司了", Toast.LENGTH_SHORT).show();
        }

//		homeDataList = (ArrayList<HashMap<String, String>>) initDecorateType()/*jsonToHomeServiceList()*/;

        districtDataList = (ArrayList<HashMap<String, String>>) jsonToDistrictList(result);

        if (temDataList.size() != 0) {
            if (page == 1) {
                // 回到第一页
                decorateCompanyList.clear();
//				typesList.clear();
            }
            for (int i = 0; i < temDataList.size(); i++) {
                decorateCompanyList.add(temDataList.get(i));
            }
            districtList.clear();
            for (int i = 0; i < districtDataList.size(); i++) {
                districtList.add(districtDataList.get(i));
            }
//			for (int i = 0; i < homeDataList.size(); i++) {
//				typesList.add(homeDataList.get(i));
//			}
            page_loading.setVisibility(View.GONE);
        }
        handler.sendEmptyMessage(0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 644) {
            page = 1;
            if (data != null && data.getBundleExtra("city_bundle") != null) {
                cityName = data.getBundleExtra("city_bundle").getString("ci");
                saveCitySharePre = this.getSharedPreferences("Save_City_Info", MODE_PRIVATE);//将城市保存在Save_City_Info.xml文件中
                Editor editor = saveCitySharePre.edit();
                editor.putString("save_city_now", cityName);
                editor.commit();
                AppInfoUtil.setCityName(mContext, cityName);
            }

            tv_city.setText(cityName);
            // 及时存入
            getSharedPreferences("city", 0).edit().putString("cityName", cityName).commit();

            // 重新输入请求的参数
            mapParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
            mapParams.put("page", page + "");
            mapParams.put("pageSize", pageSize + "");
            mapParams.put("city", cityName);
            mapParams.put("districtid", "0");
            mapParams.put("jjb_logo", "0");
            mapParams.put("certification", "0");
            mapParams.put("sort", "0");

            // 重新请求请求
            requestDecoratePost(mapParams);
//            initDecorateListAdapter();
            decorate_textView_area.setText("全部区域");
            headview_decorate_textView_area.setText("全部区域");
            decorate_textView_type.setText(decorateType[0]);
            headview_decorate_textView_type.setText(decorateType[0]);
            decorate_textView_sort.setText("默认排序");
            headview_decorate_textView_sort.setText("默认排序");
            requestAdsense();
            requestGetAds();
//			refreshChoose();
        }
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private void inintReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_HOME_SELECT_CITY);
        receiver = new RefreshCityReceiver();
        registerReceiver(receiver, filter);
    }

    private RefreshCityReceiver receiver;

    private class RefreshCityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_HOME_SELECT_CITY)) {
                tv_city.setText(intent.getBundleExtra("f_select_city_bundle").getString("city_selected"));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        needPermissions();
    }

    // 动态获取权限
    private  void needPermissions(){
        if(Build.VERSION.SDK_INT >= 23){
            String[] permissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
            };

            requestPermissions(permissions, 101);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
