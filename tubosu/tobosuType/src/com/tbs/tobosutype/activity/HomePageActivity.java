package com.tbs.tobosutype.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.NewHomePageAdapter;
import com.tbs.tobosutype.adapter.UpdateDialogAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.base.BaseDialog;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._HomePage;
import com.tbs.tobosutype.bean._UpdateInfo;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * creat by Lin   2018/08/31
 * 土拨鼠App 4.0版本的新首页
 * 采用Recyclerview嵌套模式
 */


public class HomePageActivity extends BaseActivity {

    @BindView(R.id.hp_recycle)
    RecyclerView hpRecycle;
    @BindView(R.id.hp_swipe)
    SwipeRefreshLayout hpSwipe;
    @BindView(R.id.hp_title_rl)
    RelativeLayout hpTitleRl;
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.tuisong_kaiqi_tv)
    TextView tuisongKaiqiTv;
    @BindView(R.id.tuisong_guanbi_rl)
    RelativeLayout tuisongGuanbiRl;
    @BindView(R.id.tuisong_rl)
    RelativeLayout tuisongRl;
    @BindView(R.id.hp_city_tm_ll)
    LinearLayout hpCityTmLl;
    @BindView(R.id.hp_search_tm_rl)
    RelativeLayout hpSearchTmRl;
    @BindView(R.id.hp_kefu_tm_ll)
    LinearLayout hpKefuTmLl;
    @BindView(R.id.hp_title_tm_rl)
    RelativeLayout hpTitleTmRl;
    @BindView(R.id.hp_city_ll)
    LinearLayout hpCityLl;
    @BindView(R.id.hp_search_rl)
    RelativeLayout hpSearchRl;
    @BindView(R.id.hp_kefu_ll)
    LinearLayout hpKefuLl;
    @BindView(R.id.hp_city_name_tm_tv)
    TextView hpCityNameTmTv;
    @BindView(R.id.hp_city_name_tv)
    TextView hpCityNameTv;
    @BindView(R.id.home_page_click_rl)
    RelativeLayout homePageClickRl;
    private String TAG = "HomePageActivity";
    private Context mContext;
    private Intent mIntent;

    private LinearLayoutManager mLinearLayoutManager;
    private NewHomePageAdapter mHomePageAdapter;
    private boolean isLoading = false;//数据是否正在加载
    private int mPage = 1;
    private int mPageSize = 5;
    private String mCityName;
    private _HomePage mHomePage;
    private Gson mGson;
    private _UpdateInfo mUpdateInfo;
    private DownloadBuilder builder;
    private String activityId;
    private String activityImg_url;
    private String activityH5_url;
    private String activityType;
    private String activityName;
    private int totalDy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.NOTICE_HOME_PAGE_CHANGE_CITY_NAME:
                //城市选择成功通知
                if (!TextUtils.isEmpty((String) event.getData())) {
                    hpCityNameTv.setText("" + (String) event.getData());
                    hpCityNameTmTv.setText("" + (String) event.getData());
                    initData();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //初始化相关事件
    private void initEvent() {
        HttpGetArticleType();
        showHomeDialog();
    }


    //弹窗逻辑
    private void showHomeDialog() {
        //每天的时间判断
        if (!SpUtil.getTodayToken(mContext).equals(Util.getDateToken())) {
            //更新每天的弹窗顺序数据
            SpUtil.setTodayToken(mContext, Util.getDateToken());
            SpUtil.cleanDialogInfo(mContext);
        }
        if (TextUtils.isEmpty(SpUtil.getIsShowUpdataDialog(mContext))) {
            //没有开启过更新提示
            HttpCheckAppUpdata();//检测更新与否  在检测完成之后设置更新弹窗的flag（有强制更新的时候不设置flag） 如果没有更新提示 走 getHuoDongPicture()方法
            return;
        }
        if (TextUtils.isEmpty(SpUtil.getIsShowActivityDialog(mContext))) {
            //今天还没有开启过运营弹窗
            getHuoDongPicture();//开启运营弹窗 有运营弹窗时设置flag 没有运营弹窗时 走  notifyOpenNotice() 方法
            return;
        }
        if (TextUtils.isEmpty(SpUtil.getIsShowPushDialog(mContext))) {
            notifyOpenNotice();
        }
    }

    //检测是否需要更新
    private void HttpCheckAppUpdata() {
        SpUtil.setIsShowUpdataDialog(mContext, "showing");
        HashMap<String, Object> param = new HashMap<>();
        param.put("system_plat", "1");
        param.put("chcode", AppInfoUtil.getChannType(mContext));
        param.put("version", AppInfoUtil.getAppVersionName(mContext));
        Log.e(TAG, "检测用户的更新数据=====参数=====chcode====" + AppInfoUtil.getChannType(mContext) + "=====version=====" + AppInfoUtil.getAppVersionName(mContext));
        OKHttpUtil.post(Constant.CHECK_APP_IS_UPDATA, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "数据获取失败===============" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "检测用户的更新数据==========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //数据获取成功
                        String data = jsonObject.optString("data");
                        mUpdateInfo = mGson.fromJson(data, _UpdateInfo.class);
                        if (mUpdateInfo.getIs_update().equals("1")) {
                            //有更新的情况
                            if (mUpdateInfo.getType().equals("2")) {
                                //非强制更新的情况下不设置更新的flag
                                SpUtil.setIsShowUpdataDialog(mContext, "showing");
                            } else {
                                SpUtil.setIsShowUpdataDialog(mContext, "");
                            }
                            //有更新
                            if (Util.isWifiConnected(mContext)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showUpdateDialog(mUpdateInfo);
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getHuoDongPicture();
                                }
                            });
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getHuoDongPicture() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = sdf.format(new Date());

        if (!"".equals(CacheManager.getLoadingHUODONG(mContext)) && date.equals(CacheManager.getLoadingHUODONG(mContext))) {
            // 已经保存过当天日期

        } else {
            // 这是当天第一次
            if (Util.isNetAvailable(HomePageActivity.this)) {
                final Dialog dialog = new Dialog(HomePageActivity.this, R.style.popupDialog);
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", Util.getDateToken());
                OKHttpUtil.post(Constant.ACTIVITY_URL, hashMap, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        showTap(null, "", "");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String json = response.body().string();
                        Util.setErrorLog(TAG, json);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    if (jsonObject.getInt("status") == 200) {
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        activityId = data.getString("id");
                                        activityImg_url = data.getString("img_url");
                                        activityH5_url = data.getString("h5_url");
                                        activityType = data.getString("type");
                                        activityName = data.getString("name");
//                                        picUrl = activityImg_url.replace("\\/\\/", "\\");
                                        CacheManager.setLoadingHUODONG(mContext, date);
                                        Log.e(TAG, "获取的活动的图片==================" + activityImg_url);
                                        if (!TextUtils.isEmpty(activityImg_url)) {
                                            //已经弹了活动弹窗
                                            SpUtil.setIsShowActivityDialog(mContext, "showing");
                                            showTap(dialog, activityImg_url, activityH5_url);
                                        } else {
                                            //没有弹活动弹窗
                                            notifyOpenNotice();
                                        }
                                    } else {
                                        //没有弹活动弹窗
                                        notifyOpenNotice();
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
    }


    //提示开启推送弹窗
    private void notifyOpenNotice() {
        if (Util.isNotificationEnabled(mContext)) {
            return;
        } else {
            Log.e(TAG, "当前用户是否开启推送通知====notifyOpenNotice===" + Util.isNotificationEnabled(mContext));
            /**
             * 用户未开启推送
             * 通过时间的对比 7天
             */
            if (TextUtils.isEmpty(SpUtil.getNoticeTime(mContext))) {
                //存储的时间为空 设置时间 并开启推送提示
                Log.e(TAG, "当前用户是否开启推送通知====showNoticPopWindow===" + Util.isNotificationEnabled(mContext));
                tuisongRl.setVisibility(View.VISIBLE);
                SpUtil.setNoticeTime(mContext, Util.getNowTime());
            } else {
                Log.e(TAG, "当前用户是否开启推送通知====有存储时间===" + Util.isNotificationEnabled(mContext));
                //有记录上一次的提示时间
                if (Util.differentDays(SpUtil.getNoticeTime(mContext), Util.getNowTime()) >= 7) {
                    //当前时间大于7天  开启推送提示
                    SpUtil.setNoticeTime(mContext, Util.getNowTime());
                    tuisongRl.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 显示蒙层wel
     *
     * @param adUrl 是否显示蒙层
     */
    private void showTap(final Dialog dialog, String adUrl, final String h5Url) {
        if (dialog == null) {
            return;
        }

        if (!"".equals(adUrl)) {
            View view = LayoutInflater.from(HomePageActivity.this).inflate(R.layout.layout_home_tab_layout, null);
            Display display = this.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            //设置dialog的宽高为屏幕的宽高
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
            dialog.setContentView(view, layoutParams);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            FrameLayout layout = (FrameLayout) dialog.findViewById(R.id.fr_dialog_layout);
            ImageView adIv = (ImageView) dialog.findViewById(R.id.iv_main_ad);
            RelativeLayout adIvClose = (RelativeLayout) dialog.findViewById(R.id.iv_main_ad_close);
            Glide.with(HomePageActivity.this).load(adUrl).into(adIv);
            Util.setErrorLog(TAG, adUrl);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            adIvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            adIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = null;
                    if (!"".equals(h5Url)) {
                        Bundle b = new Bundle();
                        b.putString("link", h5Url);
                        it = new Intent(mContext, WebViewActivity.class);
                        it.putExtras(b);
                    } else {
                        it = new Intent(mContext, GetPriceActivity.class);
                    }

                    startActivity(it);
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();

            }
        }
    }

    private void showUpdateDialog(final _UpdateInfo updateInfo) {
        builder = AllenVersionChecker.getInstance().downloadOnly(UIData.create()
                .setDownloadUrl(updateInfo.getApk_url()));
        builder.setCustomVersionDialogListener(new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.dialog_updata);
                RecyclerView update_dialog_msg = baseDialog.findViewById(R.id.update_dialog_msg);
                TextView versionchecklib_version_dialog_cancel = baseDialog.findViewById(R.id.versionchecklib_version_dialog_cancel);
                if (updateInfo.getType().equals("1")) {
                    versionchecklib_version_dialog_cancel.setText("退出");
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                UpdateDialogAdapter updateDialogAdapter = new UpdateDialogAdapter(mContext, updateInfo.getContent());
                update_dialog_msg.setLayoutManager(linearLayoutManager);
                update_dialog_msg.setAdapter(updateDialogAdapter);
                updateDialogAdapter.notifyDataSetChanged();
                return baseDialog;
            }
        });
        //设置是否得强制更新
        if (updateInfo.getType().equals("1")) {
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    finish();
                    System.exit(0);
                }
            });
        }
        builder.setShowDownloadingDialog(false);
        builder.setNotificationBuilder(NotificationBuilder.create().setRingtone(true)
                .setIcon(R.drawable.app_icon).setContentTitle("土拨鼠装修").setContentText("正在下载最新土拨鼠安装包..."));
        builder.excuteMission(mContext);
    }

    //初始化相关的配置
    private void initView() {
        homePageClickRl.setVisibility(View.GONE);

        hpSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        hpSwipe.setBackgroundColor(Color.WHITE);
        hpSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        hpSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        hpRecycle.setLayoutManager(mLinearLayoutManager);
        hpRecycle.setOnTouchListener(onTouchListener);
        hpRecycle.addOnScrollListener(onScrollListener);

        //设置城市
        if (SpUtil.getCity(mContext).contains("市")) {
            hpCityNameTmTv.setText("" + SpUtil.getCity(mContext).replace("市", ""));
            hpCityNameTv.setText("" + SpUtil.getCity(mContext).replace("市", ""));
        } else {
            hpCityNameTmTv.setText("" + SpUtil.getCity(mContext));
            hpCityNameTv.setText("" + SpUtil.getCity(mContext));
        }


        //设置标题栏
        hpTitleRl.getBackground().setAlpha(0);
        hpTitleRl.setVisibility(View.GONE);

        mGson = new Gson();

        initData();
    }


    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新
            homePageClickRl.setVisibility(View.VISIBLE);
            initData();
        }
    };


    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (hpSwipe.isRefreshing() || isLoading) {
                return true;
            } else {
                return false;
            }
        }
    };

    //滑动事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //滑动加载更多
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0
                    && lastVisiableItem + 3 >= mLinearLayoutManager.getItemCount()
                    && !hpSwipe.isRefreshing()
                    && !isLoading) {
                //加载更多
                isLoading = true;//正在加载
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            totalDy += dy;
//            Log.e(TAG, "首页滑动=====" + totalDy);
            //控制显示与否
            if (totalDy > 3) {
                hpTitleRl.setVisibility(View.VISIBLE);
                hpTitleTmRl.setVisibility(View.GONE);
            } else {
                hpTitleRl.setVisibility(View.GONE);
                hpTitleTmRl.setVisibility(View.VISIBLE);
            }
            //设置控件的透明度 totaldy==700zuo you
            if (totalDy <= 450) {
                hpTitleRl.getBackground().setAlpha((int) (totalDy * (255 / 450.1)));
//                hpCityNameTv.getBackground().setAlpha((int) (totalDy * (255 / 450.1)));
                bannerDever.setVisibility(View.GONE);
            } else {
                hpTitleRl.getBackground().setAlpha(255);
                bannerDever.setVisibility(View.VISIBLE);
            }
        }
    };


    //初始化数据  在下拉刷新时也调用  所以在初始化的时候要将数据清空
    private void initData() {
        //设置标题栏
        hpTitleRl.getBackground().setAlpha(0);
        hpTitleRl.setVisibility(View.GONE);
        hpTitleTmRl.setVisibility(View.VISIBLE);
        totalDy = 0;
        mCityName = hpCityNameTmTv.getText().toString();
        mPage = 1;
        //正在加载的标识
        isLoading = true;
        //将之前的数据清空
        if (mHomePage != null) {
            //清除banner
            if (mHomePage.getData().getBanner() != null &&
                    !mHomePage.getData().getBanner().isEmpty()) {
                mHomePage.getData().getBanner().clear();
            }
            //清除cases
            if (mHomePage.getData().getCases() != null &&
                    !mHomePage.getData().getCases().isEmpty()) {
                mHomePage.getData().getCases().clear();
            }
            //清除impression
            if (mHomePage.getData().getImpression() != null &&
                    !mHomePage.getData().getImpression().isEmpty()) {
                mHomePage.getData().getImpression().clear();
            }
            //清除article_type
            if (mHomePage.getData().getArticle_type() != null &&
                    !mHomePage.getData().getArticle_type().isEmpty()) {
                mHomePage.getData().getArticle_type().clear();
            }
            //清除article
            if (mHomePage.getData().getArticle() != null &&
                    !mHomePage.getData().getArticle().isEmpty()) {
                mHomePage.getData().getArticle().clear();
            }
            //清除topic
            if (mHomePage.getData().getTopic() != null &&
                    !mHomePage.getData().getTopic().isEmpty()) {
                mHomePage.getData().getTopic().clear();
            }
            //清除index_advert_1
            if (mHomePage.getData().getIndex_advert_1() != null &&
                    !mHomePage.getData().getIndex_advert_1().isEmpty()) {
                mHomePage.getData().getIndex_advert_1().clear();
            }
            //清除index_advert_2
            if (mHomePage.getData().getIndex_advert_2() != null &&
                    !mHomePage.getData().getIndex_advert_2().isEmpty()) {
                mHomePage.getData().getIndex_advert_2().clear();
            }
        }
        //注销homepage
        mHomePage = null;
        mHomePageAdapter = null;
        //重新获取数据
        HttpGetHomePageData();
    }

    //获取数据
    private void HttpGetHomePageData() {
        hpSwipe.setRefreshing(true);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("city_name", mCityName);
        param.put("uid", AppInfoUtil.getUserid(mContext));
        param.put("user_type", AppInfoUtil.getTypeid(mContext));
        param.put("subchannel", "android");
        param.put("chcode", AppInfoUtil.getChannType(mContext));
        OKHttpUtil.post(Constant.NEWVERSION_INDEX, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //链接失败
                Log.e(TAG, "4.0首页数据获取失败======" + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        hpSwipe.setRefreshing(false);
                        homePageClickRl.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "4.0首页数据获取成功======" + json);
                mHomePage = mGson.fromJson(json, _HomePage.class);
                if (mHomePage.getStatus() == 200) {
                    //数据获取成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //初始化适配器
                            if (mHomePageAdapter == null) {
                                mHomePageAdapter = new NewHomePageAdapter(mContext, mHomePage, getSupportFragmentManager());
                                hpRecycle.setAdapter(mHomePageAdapter);
                                mHomePageAdapter.notifyDataSetChanged();
                            } else {
                                mHomePageAdapter.notifyDataSetChanged();
                            }
                            isLoading = false;
                            hpSwipe.setRefreshing(false);
                            homePageClickRl.setVisibility(View.GONE);

                            //我 的 底部红点
                            MainActivity.showOrHideHotDot(mHomePage.getData().getIs_see());
                            AppInfoUtil.setHotDot(mContext,mHomePage.getData().getIs_see());


                        }
                    });

                } else {
                    //当前没有数据
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "当前没有数据~", Toast.LENGTH_SHORT).show();
                            isLoading = false;
                            hpSwipe.setRefreshing(false);
                            homePageClickRl.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
    }

    //加载更多数据
    private void loadMore() {
        mPage++;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("page", mPage);
        param.put("page_size", mPageSize);
        OKHttpUtil.post(Constant.ZHUANTI_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "获取专题信息失败", Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        hpSwipe.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "加载更多专题成功======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = jsonObject.optInt("status");
                    if (status == 200) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        //获取新的数据
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _HomePage.DataBean.TopicBean topicBean = mGson.fromJson(jsonArray.get(i).toString(), _HomePage.DataBean.TopicBean.class);
                            //数据去重
                            if (!mHomePage.getData().getTopic().contains(topicBean)) {
                                //添加数据
                                mHomePage.getData().getTopic().add(topicBean);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //刷新适配器
                                if (mHomePageAdapter != null) {
                                    mHomePageAdapter.notifyDataSetChanged();
                                }
                                isLoading = false;
                                hpSwipe.setRefreshing(false);
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有更多专题信息", Toast.LENGTH_SHORT).show();
                                isLoading = false;
                                hpSwipe.setRefreshing(false);
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isLoading = false;
                            hpSwipe.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }


    //网络获取学装修的分类
    private void HttpGetArticleType() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        OKHttpUtil.post(Constant.Z_ARTICLE_GET_TYPE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //获取学装修分类失败
                Log.e(TAG, "获取数据失败===========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取数据成功 将数据存储
                String json = new String(response.body().string());
                Log.e(TAG, "======获取分类数据成功=====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //数据请求成功将json数据存储本地
                        String typeJson = jsonObject.optString("data");
                        SpUtil.setArticleType(mContext, typeJson);
                        Log.e(TAG, "======Sp获取数据=====" + SpUtil.getArticleType(mContext));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.tuisong_kaiqi_tv, R.id.tuisong_guanbi_rl, R.id.tuisong_rl,
            R.id.hp_city_tm_ll, R.id.hp_search_tm_rl,
            R.id.hp_kefu_tm_ll, R.id.hp_title_tm_rl,
            R.id.hp_city_ll, R.id.hp_search_rl,
            R.id.hp_kefu_ll, R.id.hp_title_rl, R.id.home_page_click_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tuisong_kaiqi_tv:
                goToSet();
                tuisongRl.setVisibility(View.GONE);
                break;
            case R.id.tuisong_guanbi_rl:
                //去关闭推送通知
                tuisongRl.setVisibility(View.GONE);
                break;
            case R.id.tuisong_rl:
                //不做任何处理防止点击事件的透传
                break;

            case R.id.hp_city_tm_ll:
            case R.id.hp_city_ll:
                //城市的点击 跳转到城市选择页面
                mIntent = new Intent(mContext, SelectCtiyActivity.class);
                mIntent.putExtra("mWhereFrom", "HomePageActivity");
                startActivity(mIntent);
                break;
            case R.id.hp_search_tm_rl:
            case R.id.hp_search_rl:
                //点击搜索框 进入的是报价页面
                mIntent = new Intent(mContext, NewWebViewActivity.class);
                mIntent.putExtra("mLoadingUrl", SpUtil.getTbsAj34(mContext));
                startActivity(mIntent);
                break;
            case R.id.hp_kefu_tm_ll:
            case R.id.hp_kefu_ll:
//                showZixunPopwindow();
                Intent intent = new Intent(HomePageActivity.this, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", "https://webchat.7moor.com/wapchat.html?accessId=0e1ca6b0-ec8e-11e8-a1ba-07d6a6237cdc");
                intent.putExtra("bAnswer",true);
                startActivity(intent);
                break;
            case R.id.home_page_click_rl:
                break;
        }
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

    //去设置界面开启
    private void goToSet() {
        if (Build.VERSION.SDK_INT >= 26) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            startActivity(intent);
        } else if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 26) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            startActivity(intent);
        } else if (Build.VERSION.SDK_INT < 21) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivity(intent);
        }
    }


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
