package com.tbs.tbs_mj.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.NewHomeAdapter;
import com.tbs.tbs_mj.adapter.UpdateDialogAdapter;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean.NewHomeDataItem;
import com.tbs.tbs_mj.bean._ImageD;
import com.tbs.tbs_mj.bean._SelectCity;
import com.tbs.tbs_mj.bean._UpdateInfo;
import com.tbs.tbs_mj.customview.CustomDialog;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.CacheManager;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.ToastUtil;
import com.tbs.tbs_mj.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class NewHomeActivity extends com.tbs.tbs_mj.base.BaseActivity {
    @BindView(R.id.tuisong_kaiqi_tv)
    TextView tuisongKaiqiTv;
    @BindView(R.id.tuisong_guanbi_rl)
    RelativeLayout tuisongGuanbiRl;
    @BindView(R.id.tuisong_rl)
    RelativeLayout tuisongRl;
    private ImageView home_view;
    private ImageView ivYingying;
    private View rel_newhomebar;
    private ImageView iv_sanjiaoxing;
    private ImageView iv_add;
    private ImageView home_kefu;
    private TextView newhomeCity;
    private String findCompanyChosenCity;
    private TextView app_title_text;
    private RelativeLayout relSelectCity;
    private String choose;
    private String chooseId;
    private String cityName;
    private RecyclerView recyclerView;
    private TextView tubosu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private NewHomeAdapter newHomeAdapter;
    private boolean isSheji = true;
    private Gson mGson;
    private ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean> topicBeansList = new ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean>();
    private ArrayList<_ImageD> shejiArrayList = new ArrayList<_ImageD>();
    private Context mContext;
    private String TAG = "NewHomeActivity";
    private TextView new_home_test;
    private DownloadBuilder builder;
    private _UpdateInfo mUpdateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "NewHomeActivity执行的生命周期========onCreate()");
        mContext = NewHomeActivity.this;
        TAG = NewHomeActivity.class.getSimpleName();
        setContentView(R.layout.layout_new_activity);
        ButterKnife.bind(this);
        initView();
        needPermissions();
        chooseId = "0";
        setClick();
        //获取学装修的分类  create by lin  3.7版本新增
        HttpGetArticleType();
        //获取网络数据
        getDataFromNet(false);
        initReceiver();
        //显示弹窗
        showHomeDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "NewHomeActivity执行的生命周期========onPause()");
    }

    /**
     * 首页弹窗逻辑关系
     * 1.有更新的情况下 弹出更新提示（非强更的情况设置更新的flag）
     * 2.更新提示完成 弹运营弹窗（设置运营的flag）
     * 3.运营弹窗完成 推送弹窗逻辑
     */
    //弹窗逻辑
    private void showHomeDialog() {
        //每天的时间判断
        if (!SpUtil.getTodayToken(mContext).equals(Util.getDateToken())) {
            //更新每天的弹窗顺序数据
            SpUtil.setTodayToken(mContext, Util.getDateToken());
            SpUtil.cleanDialogInfo(mContext);
        }
//        if (TextUtils.isEmpty(SpUtil.getIsShowUpdataDialog(mContext))) {
//            // TODO: 2018/5/14 马甲包注释了App更新
//            //没有开启过更新提示
////            HttpCheckAppUpdata();//检测更新与否  在检测完成之后设置更新弹窗的flag（有强制更新的时候不设置flag） 如果没有更新提示 走 getHuoDongPicture()方法
//            return;
//        }
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

    //显示更新弹窗
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
                .setIcon(R.drawable.app_icon).setContentTitle("住家装修").setContentText("正在下载最新住家装修安装包..."));
        builder.excuteMission(mContext);
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


    private void initView() {
        new_home_test = findViewById(R.id.new_home_test);
        new_home_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showUpdateDialog();
            }
        });
        mGson = new Gson();
        home_view = (ImageView) findViewById(R.id.home_view);
        ivYingying = (ImageView) findViewById(R.id.ivYingying);
        ivYingying.setVisibility(View.GONE);
        home_view.setVisibility(View.VISIBLE);
        tubosu = (TextView) findViewById(R.id.app_title_text);
        rel_newhomebar = (View) findViewById(R.id.newhomeView);
        rel_newhomebar.setAlpha(0);
        relSelectCity = (RelativeLayout) findViewById(R.id.relSelectCity);
        newhomeCity = (TextView) findViewById(R.id.newhomeCity);

        iv_sanjiaoxing = (ImageView) findViewById(R.id.iv_sanjiaoxing);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        home_kefu = (ImageView) findViewById(R.id.home_kefu);
        app_title_text = (TextView) findViewById(R.id.app_title_text);

        recyclerView = (RecyclerView) findViewById(R.id.newhome_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.newhome_swiprefreshlayout);

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        //初始化swipeRreshLayout
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(swipeLister);
        swipeRefreshLayout.setOnTouchListener(onTouchListener);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //得到当前显示的最后一个item的view
                int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && lastPosition + 2 >= recyclerView.getLayoutManager().getItemCount()) {

                    if (newHomeAdapter != null) {
                        newHomeAdapter.setLoadMoreFlag(true);
                        page++;
                        getDataFromNet(true);
                    }
                }


                //设置其透明度
                float alpha = 0;
                int scollYHeight = getScollYHeight(true, tubosu.getHeight());

                int baseHeight = 574;
                if (scollYHeight >= baseHeight) {
                    alpha = 1;
                } else {
                    alpha = scollYHeight / (baseHeight * 1.0f);
                    if (alpha > 0.44) {
                        ivYingying.setVisibility(View.VISIBLE);
                        home_view.setVisibility(View.INVISIBLE);// 白色渐变 隐藏
                        rel_newhomebar.setVisibility(View.VISIBLE);
                        iv_sanjiaoxing.setBackgroundResource(R.drawable.tt);
                        iv_add.setBackgroundResource(R.drawable.sdf);
                        home_kefu.setBackgroundResource(R.drawable.kefu_black);
                        newhomeCity.setTextColor(Color.parseColor("#000000"));
                        app_title_text.setTextColor(Color.parseColor("#000000"));
                        rel_newhomebar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    } else {

                        ivYingying.setVisibility(View.GONE);
                        home_view.setVisibility(View.VISIBLE);
                        rel_newhomebar.setVisibility(View.INVISIBLE);
                        iv_sanjiaoxing.setBackgroundResource(R.drawable.sanjiaoxing);
                        iv_add.setBackgroundResource(R.drawable.ad_icon);
                        home_kefu.setBackgroundResource(R.drawable.home_kefu);
                        newhomeCity.setTextColor(Color.parseColor("#FFFFFF"));
                        app_title_text.setTextColor(Color.parseColor("#FFFFFF"));
                        rel_newhomebar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    }

                }
                rel_newhomebar.setAlpha(alpha);
                CacheManager.setChentaoFlag(mContext, 44);
            }
        });
        newhomeCity.setText("" + SpUtil.getCity(mContext));
        home_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZixunPopwindow();
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

    private int page = 1;
    private boolean isLoading = false;//是否正在加载数据

    private int getScollYHeight(boolean hasHead, int headerHeight) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取到第一个可见的position,其添加的头部不算其position当中
        int position = layoutManager.findFirstVisibleItemPosition();
        //通过position获取其管理器中的视图
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        //获取自身的高度
        int itemHeight = firstVisiableChildView.getHeight();
        //有头部
        if (hasHead) {
            return headerHeight + itemHeight * position - firstVisiableChildView.getTop();
        } else {
            return itemHeight * position - firstVisiableChildView.getTop();
        }
    }


    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            topicBeansList.clear();
            newHomeAdapter = null;
            swipeRefreshLayout.setRefreshing(false);
            page = 1;
            getDataFromNet(false);
        }
    };

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

    private void setClick() {
        relSelectCity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle cityBundle = new Bundle();
                cityBundle.putString("fromHomeActivity", "101");
                selectCityIntent.putExtra("HomeActivitySelectcityBundle", cityBundle);
                startActivityForResult(selectCityIntent, 3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //重新选择城市 重新加载网络加载数据
        if (requestCode == 3) {
            if (data != null && data.getBundleExtra("city_bundle") != null) {
                choose = data.getBundleExtra("city_bundle").getString("ci");
                chooseId = data.getBundleExtra("city_bundle").getString("cid");
                getSharedPreferences("Save_City_Info", MODE_PRIVATE).edit().putString("save_city_now", cityName).commit();
                AppInfoUtil.setCityName(mContext, cityName);
                newhomeCity.setText(choose);
                //存储一个全局的城市信息  并通知装修公司页面更改数据 3.7版本修改
                SpUtil.setHomeAndCompanyUsingCity(mContext, choose);
                _SelectCity mSelectCity = new _SelectCity(choose, chooseId);
                EventBusUtil.sendEvent(new Event(EC.EventCode.CHOOSE_CITY_CODE, mSelectCity));//通知装修公司修改城市定位信息 并且将数据刷新
                CacheManager.setStartFlag(NewHomeActivity.this, 1);
//                getDataFromNet(false);
            }


            Intent selectCityIntent = new Intent(Constant.ACTION_HOME_SELECT_CITY);
            Bundle b = new Bundle();
            b.putString("city_selected", cityName);
            selectCityIntent.putExtra("f_select_city_bundle", b);
            sendBroadcast(selectCityIntent);
            getSharedPreferences("city", 0).edit().putString("cityName", cityName).commit();

        }
    }


    /**
     * @param num
     * @return
     */
    private HashMap<String, Object> getParam(int num) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("token", Util.getDateToken());
        String uid = getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("userid", "");
        String user_type = getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("mark", "");
        param.put("uid", uid);
        param.put("user_type", user_type);
        String city = newhomeCity.getText().toString();
        Log.e(TAG, "切换城市后获取的城市名=====" + city + "============当所用的num==========" + num);
        if (num == 0) {
            param.put("city_id", "0");
            param.put("city_name", city);
            param.put("type", "1");
        } else {
            // 选择
            param.put("city_id", chooseId);
            param.put("city_name", choose);
            param.put("type", "1");
            Util.setErrorLog("-zengzhaozhong-", "#id = " + chooseId + "    cityname = " + city);
        }
        return param;
    }

    private void getDataFromNet(boolean more) {
        if (Util.isNetAvailable(mContext)) {
            isLoading = true;
            // start 1 选择过城市，  start 0 未选择过城市
            int start = CacheManager.getStartFlag(NewHomeActivity.this);
            Util.setErrorLog(TAG, "---zengzhaozhong--start>>" + start);
            if (more) {
                // 加载更多
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", Util.getDateToken());
                hashMap.put("page", page);
                hashMap.put("type", "1");
                hashMap.put("page_size", 5);
                OKHttpUtil.post(Constant.ZHUANTI_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        isLoading = false;
                        Util.setErrorLog(TAG, "请求失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Util.setToast(mContext, "加载更多专题失败");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        final String json = new String(response.body().string());
                        Util.setErrorLog(TAG, json);
                        // 有数据 这样处理是不至于无数据的时候出现app闪退
                        if (json.contains("data")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<NewHomeDataItem.NewhomeDataBean.TopicBean> zhuantiList = new ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean>();
                                        JSONObject zhuantiObject = new JSONObject(json);
                                        int status = zhuantiObject.getInt("status");
                                        String msg = zhuantiObject.getString("msg");
                                        if (status == 200) {
                                            JSONArray arr = zhuantiObject.getJSONArray("data");
                                            for (int i = 0; i < arr.length(); i++) {
                                                NewHomeDataItem.NewhomeDataBean.TopicBean bean = mGson.fromJson(arr.get(i).toString(), NewHomeDataItem.NewhomeDataBean.TopicBean.class);
                                                zhuantiList.add(bean);
                                            }
                                            topicBeansList.addAll(zhuantiList);
                                            initData();
                                        } else if (status == 0) {
                                            Util.setToast(mContext, msg);
                                        } else if (status == 201) {
                                            Util.setToast(mContext, msg);
                                        } else {
                                            Util.setErrorLog(TAG, " 错误请求码是 [" + status + "]");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            // 无数据 这样处理是不至于无数据的时候出现app闪退
                            Util.setErrorLog(TAG, "后台无数据返回给我===1===如下：" + json);
                        }
                    }
                });
            } else {
                newHomeAdapter = null;
                bigData = null;

                // 第一次加载
                OKHttpUtil.post(Constant.NEWHOME_URL, getParam(start), new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String result = new String(response.body().string());
                        // 有数据 这样处理是不至于无数据的时候出现app闪退
                        if (result.contains("data")) {
//                            Log.e(TAG, "首页数据链接成功============" + result);
                            CacheManager.setNewhomeJson(mContext, result);
                            Gson gson = new Gson();
                            final NewHomeDataItem dataItem = gson.fromJson(result, NewHomeDataItem.class);
                            final String msg = dataItem.getMsg();
                            Util.setErrorLog(TAG, dataItem.getMsg());

                            try {
                                JSONObject resultJson = new JSONObject(result);
                                JSONObject shejiObject = resultJson.getJSONObject("data");
                                JSONArray shejiArr = shejiObject.getJSONArray("impression");
                                if (shejiArrayList.size() > 0) {
                                    shejiArrayList.clear();
                                }
                                for (int i = 0; i < shejiArr.length(); i++) {
                                    _ImageD shejiImg = gson.fromJson(shejiArr.getJSONObject(i).toString(), _ImageD.class);
                                    shejiArrayList.add(shejiImg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (dataItem.getStatus() == 200) {
                                bigData = dataItem.getData();
//                                Log.e(TAG, "数据赋值=================" + bigData.getArticle_type().size());
                                initData();
                            } else if (dataItem.getStatus() == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Util.setToast(mContext, msg);
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Util.setToast(mContext, msg);
                                    }
                                });
                            }
                            if (swipeRefreshLayout.isRefreshing()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        } else {

                        }
                    }
                });
            }
        } else {
            Util.setErrorLog(TAG, "无网络");
        }

    }


    private NewHomeDataItem.NewhomeDataBean bigData;

    private void initData() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                isLoading = false;
                if (newHomeAdapter == null) {
                    newHomeAdapter = new NewHomeAdapter(mContext, bigData, shejiArrayList, topicBeansList);
                    recyclerView.setAdapter(newHomeAdapter);
//                    recyclerView.scrollToPosition(0);
                    newHomeAdapter.notifyDataSetChanged();
                } else {
//                    recyclerView.scrollToPosition(0);
                    if (topicBeansList.size() > 0) {
                        newHomeAdapter.setTopicData(topicBeansList);
                    }
                    newHomeAdapter.notifyDataSetChanged();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (anliReceiver != null) {
            unregisterReceiver(anliReceiver);
        }
    }


    private void needPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permission = Util.getPermissionList(mContext);
            if (permission.size() > 0) {
                requestPermissions(permission.toArray(new String[permission.size()]), 101);
            }
        }
    }

    @OnClick({R.id.tuisong_kaiqi_tv, R.id.tuisong_guanbi_rl, R.id.tuisong_rl})
    public void onViewClickedInNewHomeActivity(View view) {
        switch (view.getId()) {
            case R.id.tuisong_kaiqi_tv:
                //去开启推送通知
                goToSet();
                tuisongRl.setVisibility(View.GONE);
                break;
            case R.id.tuisong_guanbi_rl:
                //去关闭推送通知
                tuisongRl.setVisibility(View.GONE);
                break;
            case R.id.tuisong_rl:
                //不做任何处理只是为了防点击事件穿透
                break;
        }
    }


    private String activityId;
    private String activityImg_url;
    private String activityH5_url;
    private String activityType;
    private String activityName;

    private void getHuoDongPicture() {
//        Log.e(TAG, "获取弹窗活动===========开始=====");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = sdf.format(new Date());

        if (!"".equals(CacheManager.getLoadingHUODONG(mContext)) && date.equals(CacheManager.getLoadingHUODONG(mContext))) {
            // 已经保存过当天日期

        } else {
            // 这是当天第一次
            if (Util.isNetAvailable(NewHomeActivity.this)) {
                final Dialog dialog = new Dialog(NewHomeActivity.this, R.style.popupDialog);
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
//                                        Log.e(TAG, "获取的活动的图片==================" + activityImg_url);
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

    private void initReceiver() {
        anliReceiver = new AnliReceiver();
        IntentFilter intentFilter = new IntentFilter("anli_list_is_empty");
        registerReceiver(anliReceiver, intentFilter);
    }

    private AnliReceiver anliReceiver;

    private class AnliReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isSheji) {
                isSheji = false;
                if (intent.getAction().equals("anli_list_is_empty")) {
//                    getDataFromNet(false);
                    Util.setErrorLog(TAG, "重新请求案例了");
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
            View view = LayoutInflater.from(NewHomeActivity.this).inflate(R.layout.layout_home_tab_layout, null);
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
            Glide.with(NewHomeActivity.this).load(adUrl).into(adIv);
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

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.HOMEACTIVITY_CITY_CODE:
                findCompanyChosenCity = (String) event.getData();
                newhomeCity.setText(findCompanyChosenCity);
                CacheManager.setStartFlag(NewHomeActivity.this, 1);
                getDataFromNet(false);
                break;
            case EC.EventCode.CHOOSE_CITY_CODE:
                _SelectCity selectCity = (_SelectCity) event.getData();
                findCompanyChosenCity = selectCity.getCityName();
                newhomeCity.setText(findCompanyChosenCity);
                cityName = selectCity.getCityName();
                chooseId = selectCity.getCityId();
                CacheManager.setStartFlag(NewHomeActivity.this, 1);
                getDataFromNet(false);
                break;
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