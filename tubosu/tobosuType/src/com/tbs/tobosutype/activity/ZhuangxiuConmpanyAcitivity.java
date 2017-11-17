package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.CompanyAdapter;
import com.tbs.tobosutype.bean.CompanyBean;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.DividerItemDecoration;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ZhuangxiuConmpanyAcitivity extends AppCompatActivity {

    @BindView(R.id.relBackGongsi)
    RelativeLayout relBackGongsi;
    @BindView(R.id.reclerviewGongsi)
    RecyclerView reclerviewGongsi;
    @BindView(R.id.gongsiRefreshLayout)
    SwipeRefreshLayout gongsiRefreshLayout;
    @BindView(R.id.tvDelelteZhuangxiuGongsi)
    TextView tvDelelteZhuangxiuGongsi;
    @BindView(R.id.tvEditZhuangxiuGongsi)
    TextView tvEditZhuangxiuGongsi;
    @BindView(R.id.iv_no_company)
    ImageView ivNoCompany;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private String TAG = "ZhuangxiuConmpanyAcitivity";
    private int page = 1;
    private boolean isEdittext = false;
    private CompanyAdapter adapter;
    private ArrayList<CompanyBean> companyBeanArrayList = new ArrayList<CompanyBean>();
    private boolean isDeletingCompany = false;
    private ArrayList<String> deletComannaySelectIdList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuangxiu_gongsi);
        context = ZhuangxiuConmpanyAcitivity.this;
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reclerviewGongsi.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider_main_bg_height_1));
        reclerviewGongsi.addItemDecoration(itemDecorationHeader);
        gongsiRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        gongsiRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(!isDeletingCompany){
                    //下拉刷新数据 重新初始化各种数据
                    companyBeanArrayList.clear();
                    adapter = null;
                    gongsiRefreshLayout.setRefreshing(false);
                    page = 1;
                    getNetData();
                    if(adapter!=null){
                        adapter.loadMoreData(false);
                    }
                }
            }
        });

        gongsiRefreshLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //处于下拉刷新时列表不允许点击  死锁问题
                if (gongsiRefreshLayout.isRefreshing()) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        reclerviewGongsi.setOnTouchListener(onTouchListener);
        reclerviewGongsi.addOnScrollListener(onScrollListener);
        getNetData();
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItems = linearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItems + 2 >= adapter.getItemCount()) {
                LoadMore();
            }
        }
    };

    private void LoadMore(){
        page++;
        getNetData();
        if(adapter!=null){
            adapter.loadMoreData(true);
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (gongsiRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getNetData();
//    }

    public void getNetData() {
        if (Util.isNetAvailable(context)) {
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String type = sp.getString("typeid", "1");
            String userid = sp.getString("userid", "272286");
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("uid", userid);
            hashMap.put("user_type", type); //type
            hashMap.put("page_size", "10");
            hashMap.put("page", page);
            okHttpUtil.post(Constant.GONGSI_URL, hashMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(context, "系统繁忙，请稍后再试。");
                            gongsiRefreshLayout.setRefreshing(false);
                            if(adapter!=null){
                                adapter.loadMoreData(false);
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(adapter!=null){
                                adapter.loadMoreData(false);
                            }
                            gongsiRefreshLayout.setRefreshing(false);
                            if (json.contains("data")) {
                                try {
                                    JSONObject object = new JSONObject(json);
                                    String msg = object.getString("msg");
                                    if(object.getInt("status") == 200){
                                        JSONArray arr = object.getJSONArray("data");
                                        for(int i=0;i<arr.length();i++){
                                            CompanyBean bean = new CompanyBean();
                                            bean.setId(arr.getJSONObject(i).getString("id"));
                                            bean.setCollect_id(arr.getJSONObject(i).getString("collect_id"));
                                            bean.setName(arr.getJSONObject(i).getString("name"));
                                            bean.setIcon(arr.getJSONObject(i).getString("icon"));
                                            bean.setIs_certified(arr.getJSONObject(i).getString("is_certified"));
                                            bean.setDistrict_name(arr.getJSONObject(i).getString("district_name"));
                                            bean.setIs_discount(arr.getJSONObject(i).getString("is_discount"));
                                            bean.setSuite_count(arr.getJSONObject(i).getString("suite_count"));
                                            bean.setCase_count(arr.getJSONObject(i).getString("case_count"));
                                            bean.setSelected(false);
                                            companyBeanArrayList.add(bean);
                                        }
                                        initAdapter();
                                    }else if(object.getInt("status") == 201 || object.getInt("status") == 0){
                                        Util.setToast(context, msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void initAdapter(){
        if(companyBeanArrayList.size()>0){
            tvEditZhuangxiuGongsi.setVisibility(View.VISIBLE);
        }else {
            tvEditZhuangxiuGongsi.setVisibility(View.GONE);
        }

        if(adapter == null){
            adapter = new CompanyAdapter(context, companyBeanArrayList);
            reclerviewGongsi.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {
            adapter.notifyDataSetChanged();
        }

        if(adapter.getItemCount()>1){
            ivNoCompany.setVisibility(View.GONE);
        }else {
            ivNoCompany.setVisibility(View.VISIBLE);
        }

        adapter.setCompanyItemClickListener(new CompanyAdapter.OnCompanyItemClickListener() {

            @Override
            public void onCompanyItemClickListener(int position, ArrayList<CompanyBean> companyList) {
                if(isEdittext){
                    // 正在编辑删除中
                    CompanyBean bean = companyList.get(position);
                    boolean isSelect = bean.isSelected();
                    if (!isSelect) {
                        bean.setSelected(true);
                        deletComannaySelectIdList.add(bean.getCollect_id());
                    } else {
                        deletComannaySelectIdList.remove(bean.getCollect_id());
                        bean.setSelected(false);
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    // 没有编辑删除中
                    String comid = companyBeanArrayList.get(position).getId(); //comid
                    Util.setLog(TAG, "收藏公司 传过去" + comid);
                    Intent detailIntent = new Intent(context, DecorateCompanyDetailActivity.class);
                    Bundle bundle = new Bundle();
                    // 从列表带过去的公司id
                    bundle.putString("comid", comid);
                    detailIntent.putExtras(bundle);
                    startActivity(detailIntent);

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick({R.id.relBackGongsi, R.id.tvDelelteZhuangxiuGongsi, R.id.tvEditZhuangxiuGongsi})
    public void onZhuangxiuConmpanyAcitivityViewClicked(View v) {
        switch (v.getId()){
            case R.id.relBackGongsi:
                if(isEdittext){
                    isDeletingCompany = false;
                    setDeleteFlag(isDeletingCompany);
                    tvDelelteZhuangxiuGongsi.setVisibility(View.GONE);
                    tvEditZhuangxiuGongsi.setText("编辑");
                    isEdittext = !isEdittext;
                }else {
                    finish();
                }
                break;
            case R.id.tvEditZhuangxiuGongsi:
                if(isEdittext){
                    // 正在编辑中
                    tvEditZhuangxiuGongsi.setText("编辑");
                    tvDelelteZhuangxiuGongsi.setVisibility(View.GONE);
                    isDeletingCompany = false;
                    setDeleteFlag(isDeletingCompany);
                }else {
                    // 没有编辑
                    tvEditZhuangxiuGongsi.setText("取消");
                    tvDelelteZhuangxiuGongsi.setVisibility(View.VISIBLE);
                    isDeletingCompany = true;
                    setDeleteFlag(isDeletingCompany);
                }
                isEdittext = !isEdittext;
                break;
            case R.id.tvDelelteZhuangxiuGongsi:
                // 删除请求
                int size = deletComannaySelectIdList.size();
                if(size>0){
                    String idString = "";
                    for(int i=0;i<size;i++){
                        if(i!=size-1){
                            idString += deletComannaySelectIdList.get(i) + ",";
                        }else {
                            idString += deletComannaySelectIdList.get(i);
                        }
                    }
                    OKHttpUtil okHttpUtil = new OKHttpUtil();
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("token", Util.getDateToken());
                    hashMap.put("ids", idString); // idString
                    Util.setErrorLog(TAG, "==收藏id号=>>" + idString);
                    okHttpUtil.post(Constant.SHANCHU_URL1, hashMap, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.setToast(context, "删除失败");
                                }
                            });
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String json = response.body().string();
                            Util.setErrorLog(TAG, "==删除=>>" + json);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        JSONObject object = new JSONObject(json);
                                        String msg = object.getString("msg");
                                        Util.setToast(context, msg);
                                        if(object.getInt("status") == 200){
                                            adapter.setDeletingStutas(false);
                                            adapter.notifyDataSetChanged();
                                        }else {

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    Util.setToast(context, "你没有选择");
                }
                break;
        }
    }


    private void setDeleteFlag(boolean flag){
        if(adapter!=null){
            adapter.setDeletingStutas(flag);
            if(flag){
                adapter.notifyDataSetChanged();
            }else {
                adapter.setDeletingStutas(flag);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isEdittext){
            isDeletingCompany = false;
            setDeleteFlag(isDeletingCompany);
            isEdittext = !isEdittext;
            tvEditZhuangxiuGongsi.setText("编辑");
            tvDelelteZhuangxiuGongsi.setVisibility(View.GONE);
            return true;
        }else {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}