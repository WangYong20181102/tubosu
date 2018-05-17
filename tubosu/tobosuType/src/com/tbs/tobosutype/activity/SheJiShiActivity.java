package com.tbs.tobosutype.activity;

import com.tbs.tobosutype.bean._CompanyDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DesignerInfoAdapter;
import com.tbs.tobosutype.bean.DesignerInfoBean;
import com.tbs.tobosutype.bean.DesignerInfoCaseBean;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
    底部发单【找TA免费设计】：http://m.tobosu.com/quote
    设计方案【获取此设计】：http://m.tobosu.com/quote
    装修案例【获取报价】：http://m.tobosu.com/free_price_page
*/

public class SheJiShiActivity extends com.tbs.tobosutype.base.BaseActivity implements View.OnClickListener {
    private Intent dataIntent;
    private String des_id;
    private android.widget.RelativeLayout shejishiBar, get_designe_layout;
    private android.widget.ImageView ivbacUpck, shejishiShare;
    private android.widget.RelativeLayout relShejishiBack;
    private android.support.v7.widget.RecyclerView shejishiRecyclerView;
    private android.support.v4.widget.SwipeRefreshLayout shejishiSwip;
    private LinearLayoutManager linearLayoutManager;

    private boolean isLoading = false;
    private int shejiPage = 2;
    private int anliPage = 2;
    private int page = 1;
    private int page_size = 10;
    private DesignerInfoBean designerInfoBean;
    private List<DesignerInfoCaseBean> anliList = new ArrayList<DesignerInfoCaseBean>();
    private List<_CompanyDetail.SuitesBean> shejiList = new ArrayList<_CompanyDetail.SuitesBean>();
    private DesignerInfoAdapter shejishiAdapter;
    private String shareUrl;
    private int loadMoreDataType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shijishi);
        mContext = SheJiShiActivity.this;
        TAG = SheJiShiActivity.class.getSimpleName();
        bindViews();
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    private void bindViews() {
        shejishiBar = (RelativeLayout) findViewById(R.id.shejishiBar);
        relShejishiBack = (RelativeLayout) findViewById(R.id.relShejishiBack);
        ivbacUpck = (ImageView) findViewById(R.id.ivbacUpck);
        shejishiShare = (ImageView) findViewById(R.id.shejishiShare);
        shejishiSwip = (SwipeRefreshLayout) findViewById(R.id.shejishiSwip);
        shejishiRecyclerView = (RecyclerView) findViewById(R.id.shejishiRecyclerView);
        get_designe_layout = (RelativeLayout) findViewById(R.id.get_designe_layout);

        shejishiShare.setOnClickListener(this);
        relShejishiBack.setOnClickListener(this);
        ivbacUpck.setOnClickListener(this);
        get_designe_layout.setOnClickListener(this);


        dataIntent = getIntent();
        des_id = dataIntent.getStringExtra("designer_id");
        SpUtil.setStatisticsEventPageId(mContext, des_id);
        Util.setErrorLog(TAG, ">>设计师id>>>>" + des_id);
        initViews();
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shejishiRecyclerView.setLayoutManager(linearLayoutManager);
        shejishiSwip.setProgressBackgroundColorSchemeColor(Color.WHITE);
        shejishiSwip.setColorSchemeResources(R.color.colorAccent);
        shejishiSwip.setOnRefreshListener(swipeLister);
        shejishiSwip.setOnTouchListener(onTouchListener);

        shejishiRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (shejishiAdapter != null && !shejishiAdapter.cantLoadMore()) {
                    loadMoreDataType = shejishiAdapter.getClickType();
                    //得到当前显示的最后一个item的view
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && lastPosition + 2 >= recyclerView.getLayoutManager().getItemCount()) {
                        isLoading = true;
                        shejishiAdapter.loadMore(true);
                        loadMoreData(loadMoreDataType);
                    }
                }

                int alpha = 0;
                int scollYHeight = getScollYHeight(true, relShejishiBack.getHeight());
                int baseHeight = 402;
                if (scollYHeight >= baseHeight) {
                    alpha = 255;
                    scollYHeight = 402;
                    shejishiBar.getBackground().setAlpha(255);
                } else {
                    alpha = (int) (255 - (baseHeight - scollYHeight));
                }
                shejishiBar.getBackground().setAlpha(alpha);
                if (alpha < 69) {
                    ivbacUpck.setImageResource(R.drawable.back_white);
                    shejishiShare.setBackgroundResource(R.drawable.img_detail_share);
                } else {
                    ivbacUpck.setImageResource(R.drawable.activity_back);
                    shejishiShare.setBackgroundResource(R.drawable.zh02);
                }
            }
        });

        getData();


    }

    private int getScollYHeight(boolean hasHead, int headerHeight) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) shejishiRecyclerView.getLayoutManager();
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


    private void loadMoreData(final int type) {
        String url = "";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", Util.getDateToken());
        hashMap.put("des_id", des_id);
        hashMap.put("page_size", page_size);

        if (type == 0 && !shejishiAdapter.cantLoadMoreSheji()) {
            // 设计
            shejiPage++;
            hashMap.put("page", shejiPage);
            String userType = getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("mark", "");
            String uid = getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("userid", "");
            hashMap.put("user_type", userType);
            hashMap.put("uid", uid);
            url = Constant.MORE_SHEJI_URL;
        } else {
            if (type == 1 && !shejishiAdapter.cantLoadMoreAnli()) {
                // 案例
                anliPage++;
                hashMap.put("page", anliPage);
                hashMap.put("user_type", "");
                hashMap.put("uid", "");
                url = Constant.MORE_ANLI_URL;
            } else {
                return;
            }
        }
        if (Util.isNetAvailable(mContext)) {

            OKHttpUtil.post(url, hashMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    isLoading = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (shejishiAdapter != null) {
                                shejishiAdapter.loadMore(false);
                            }

                            Util.setToast(mContext, "系统繁忙，稍后再试~");
                        }
                    });

                    e.printStackTrace();
                    Util.setErrorLog(TAG, "加载更多失败---");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            isLoading = false;
                            if (shejishiAdapter != null) {
                                shejishiAdapter.loadMore(false);
                            }
                            try {
                                JSONObject moreDataJson = new JSONObject(json);
                                String msg = moreDataJson.getString("msg");
                                if (moreDataJson.getInt("status") == 200) {
                                    JSONArray dataArr = moreDataJson.getJSONArray("data");
                                    if (type == 0) {
                                        // 设计
                                        List<_CompanyDetail.SuitesBean> tempMoreShejiList = new ArrayList<_CompanyDetail.SuitesBean>();
                                        for (int i = 0; i < dataArr.length(); i++) {
                                            Gson shejiGson = new Gson();
                                            _CompanyDetail.SuitesBean designBean = shejiGson.fromJson(dataArr.getJSONObject(i).toString(), _CompanyDetail.SuitesBean.class);
                                            tempMoreShejiList.add(designBean);
                                        }

                                        if (shejishiAdapter != null) {
                                            shejishiAdapter.setShejiDataList(tempMoreShejiList);
                                            shejishiAdapter.notifyDataSetChanged();

                                            shejishiAdapter.setOnShejiDataListener(new DesignerInfoAdapter.OnShejiDataListener() {

                                                @Override
                                                public void OnShejiDataListener(View view, int shejiPosition) {
                                                    //点击了整个图片层 进入图片查看器
                                                    String DImageJson = new Gson().toJson(shejiList.get(shejiPosition));
                                                    SpUtil.setDoubleImageListJson(mContext, DImageJson);
                                                    Intent intent = new Intent(mContext, DImageLookingActivity.class);
                                                    intent.putExtra("mPosition", shejiPosition);
                                                    intent.putExtra("mWhereFrom", "ShejishiAcitity");
                                                    startActivity(intent);
                                                }
                                            });

//                                            shejishiAdapter.setOnShowShejisDataListener(new DesignerInfoAdapter.OnShowShejisDataListener() {
//                                                @Override
//                                                public void OnShowShejisDataListener() {
//                                                    Util.setToast(mContext, "设计...");
//                                                }
//                                            });
                                        }
                                    } else {
                                        //  案例
                                        List<DesignerInfoCaseBean> tempMoreAnliList = new ArrayList<>();
                                        for (int i = 0; i < dataArr.length(); i++) {
                                            Gson anliGson = new Gson();
                                            DesignerInfoCaseBean anliBean = anliGson.fromJson(dataArr.getJSONObject(i).toString(), DesignerInfoCaseBean.class);
                                            tempMoreAnliList.add(anliBean);
                                        }
                                        if (shejishiAdapter != null) {
                                            shejishiAdapter.setAnliDataList(tempMoreAnliList);
                                            shejishiAdapter.notifyDataSetChanged();

                                            shejishiAdapter.setOnAnliDataListener(new DesignerInfoAdapter.OnAnliDataListener() {
                                                @Override
                                                public void OnAnliDataListener(View view, int anliPosition) {
                                                    Intent intent = new Intent(mContext, DecorationCaseDetailActivity.class);
                                                    intent.putExtra("deco_case_id", anliList.get(anliPosition).getId());
                                                    startActivity(intent);
                                                }
                                            });

//                                            shejishiAdapter.setOnshowAnliDataListener(new DesignerInfoAdapter.OnshowAnliDataListener() {
//                                                @Override
//                                                public void OnshowAnliDataListener() {
//                                                    Util.setToast(mContext, "我要显示案例列表啦");
//                                                }
//                                            });
                                        }
                                    }
                                } else if (moreDataJson.getInt("status") == 201) {
                                    Util.setErrorLog(TAG, "加载更多来了 status = 201");
//                                    Util.setToast(mContext, msg);
                                } else if (moreDataJson.getInt("status") == 0) {
                                    Util.setErrorLog(TAG, "设加载更多来了 status = 0");
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


    private void getData() {
        if (Util.isNetAvailable(mContext)) {
            String userType = getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("mark", "");
            String uid = getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("userid", "");
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("des_id", des_id);
            hashMap.put("page", page);
            hashMap.put("page_size", page_size);
            hashMap.put("user_type", userType);
            hashMap.put("uid", uid);
            OKHttpUtil.post(Constant.SHEJISHI_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "获取设计师信息失败，稍后再试~");
                        }
                    });
                    e.printStackTrace();
                    Util.setErrorLog(TAG, "获取设计师信息失败---0");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                JSONObject sheji = new JSONObject(json);
                                if (sheji.getInt("status") == 200) {
                                    JSONObject data = sheji.getJSONObject("data");
                                    JSONObject designerInfo = data.getJSONObject("designer_info");
                                    if (designerInfoBean == null) {
                                        Gson infoGson = new Gson();
                                        designerInfoBean = infoGson.fromJson(designerInfo.toString(), DesignerInfoBean.class);
                                        shareUrl = designerInfoBean.getShare();
                                    }
                                    JSONArray shejiArr = data.getJSONArray("designer_pic");
                                    List<_CompanyDetail.SuitesBean> tempShejiList = new ArrayList<_CompanyDetail.SuitesBean>();
                                    for (int i = 0; i < shejiArr.length(); i++) {
                                        Gson shejiGson = new Gson();
                                        _CompanyDetail.SuitesBean designBean = shejiGson.fromJson(shejiArr.getJSONObject(i).toString(), _CompanyDetail.SuitesBean.class);
                                        tempShejiList.add(designBean);
                                    }
                                    shejiList.addAll(tempShejiList);


                                    JSONArray anliArr = data.getJSONArray("anli");
                                    List<DesignerInfoCaseBean> tempAnliList = new ArrayList<>();
                                    for (int i = 0; i < anliArr.length(); i++) {
                                        Gson anliGson = new Gson();
                                        DesignerInfoCaseBean anliBean = anliGson.fromJson(anliArr.getJSONObject(i).toString(), DesignerInfoCaseBean.class);
                                        tempAnliList.add(anliBean);
                                    }
                                    anliList.addAll(tempAnliList);

                                    if (shejishiAdapter == null) {
                                        if (shejiList.size() == 0 && anliList.size() == 0) {
                                            shejishiAdapter = new DesignerInfoAdapter(mContext, designerInfoBean);
                                        } else if (shejiList.size() > 0 && anliList.size() == 0) {
                                            shejishiAdapter = new DesignerInfoAdapter(mContext, designerInfoBean, shejiList);
                                        } else if (shejiList.size() == 0 && anliList.size() > 0) {
                                            shejishiAdapter = new DesignerInfoAdapter(mContext, designerInfoBean, anliList, 1);
                                        } else if (shejiList.size() > 0 && anliList.size() > 0) {
                                            shejishiAdapter = new DesignerInfoAdapter(mContext, designerInfoBean, shejiList, anliList);
                                        }
                                        shejishiRecyclerView.setAdapter(shejishiAdapter);
                                        shejishiAdapter.notifyDataSetChanged();
                                    } else {
                                        shejishiAdapter.notifyDataSetChanged();
                                    }

                                    shejishiAdapter.setOnShejiDataListener(new DesignerInfoAdapter.OnShejiDataListener() {
                                        @Override
                                        public void OnShejiDataListener(View view, int shejiPosition) {
                                            //点击了整个图片层 进入图片查看器
                                            String DImageJson = new Gson().toJson(shejiList);
                                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                                            intent.putExtra("mPosition", shejiPosition);
                                            intent.putExtra("mWhereFrom", "ShejishiAcitity");
                                            startActivity(intent);
                                        }
                                    });

                                    shejishiAdapter.setOnAnliDataListener(new DesignerInfoAdapter.OnAnliDataListener() {
                                        @Override
                                        public void OnAnliDataListener(View view, int anliPosition) {
                                            Intent intent = new Intent(mContext, DecorationCaseDetailActivity.class);
                                            intent.putExtra("deco_case_id", anliList.get(anliPosition).getId());
                                            startActivity(intent);
                                        }
                                    });

//                                    shejishiAdapter.setOnshowAnliDataListener(new DesignerInfoAdapter.OnshowAnliDataListener() {
//                                        @Override
//                                        public void OnshowAnliDataListener() {
//                                            Util.setToast(mContext, "我要显示案例列表啦1");
//                                        }
//                                    });
//                                    shejishiAdapter.setOnShowShejisDataListener(new DesignerInfoAdapter.OnShowShejisDataListener() {
//                                        @Override
//                                        public void OnShowShejisDataListener() {
//                                            Util.setToast(mContext, "设计1...");
//                                        }
//                                    });

                                } else if (sheji.getInt("status") == 201) {
                                    Util.setErrorLog(TAG, "设计师来了201");
                                } else if (sheji.getInt("status") == 0) {
                                    Util.setErrorLog(TAG, "设计师来了0");
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


    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            shejiList.clear();
            anliList.clear();
            shejiPage = 1;
            anliPage = 1;
            shejishiAdapter = null;
            shejishiSwip.setRefreshing(false);
            page = 1;
            getData();
        }
    };


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (shejishiSwip.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shejishiShare:
                UMWeb umWeb = new UMWeb(shareUrl + "&channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(mContext));
                umWeb.setDescription("我是来着土拨鼠-" + designerInfoBean.getCom_name() + "的" + designerInfoBean.getName() + "，专注品质家装设计，提前遇见您未来的家");
                umWeb.setTitle("设计师-" + designerInfoBean.getName() + "-" + designerInfoBean.getCom_name());
                umWeb.setThumb(new UMImage(mContext, designerInfoBean.getIcon()));
                new ShareAction(SheJiShiActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                        .withMedia(umWeb).open();
                break;
            case R.id.relShejishiBack:
                finish();
                break;

            case R.id.get_designe_layout:
                Intent webIntent = new Intent(mContext, NewWebViewActivity.class);
                webIntent.putExtra("mLoadingUrl", Constant.QUOTE);
                startActivity(webIntent);
                break;
        }
    }
}
