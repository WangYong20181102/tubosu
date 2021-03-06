package com.tbs.tbs_mj.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tbs_mj.bean._ImageS;
import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.DanTuAdapter;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.SpacingItemDecoration;
import com.tbs.tbs_mj.utils.Util;
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

public class DanTuAcitivity extends com.tbs.tbs_mj.base.BaseActivity {

    @BindView(R.id.relBackDantu)
    RelativeLayout relBackDantu;
    @BindView(R.id.reclerviewDantu)
    RecyclerView reclerviewDantu;
    @BindView(R.id.tvEditDantu)
    TextView tvEditDantu;
    private boolean isEditting =  false;
    @BindView(R.id.relDeleteDatu)
    RelativeLayout relDeleteDatu;
    @BindView(R.id.dantuRefreshLayout)
    SwipeRefreshLayout dantuRefreshLayout;
    @BindView(R.id.rel_no_dantu)
    RelativeLayout rel_no_dantu;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String TAG = "DanTuAcitivity";
    private boolean isLoadMore = false;
    private ArrayList<_ImageS> dantuArrayList = new ArrayList<_ImageS>();
    private DanTuAdapter danTuAdapter;
    private ArrayList<String> deletDantuSelectIdList = new ArrayList<String>();
    private ArrayList<_ImageS> deletingEntity = new ArrayList<_ImageS>();
    private boolean isDeletingDantu = false;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = DanTuAcitivity.this;
        setContentView(R.layout.activity_dantu);
        ButterKnife.bind(this);
        initView();
    }


    private void initView(){
        dantuRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        dantuRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        dantuRefreshLayout.setOnRefreshListener(swipeLister);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        reclerviewDantu.setLayoutManager(staggeredGridLayoutManager);
        ((SimpleItemAnimator) reclerviewDantu.getItemAnimator()).setSupportsChangeAnimations(false);
        reclerviewDantu.setOnTouchListener(onTouchListener);
        reclerviewDantu.addItemDecoration(new SpacingItemDecoration(2, 3, true));
        reclerviewDantu.setHasFixedSize(true);
        reclerviewDantu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int[] lastVisiableItems = staggeredGridLayoutManager.findLastVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
                int lastVisiableItem = getMaxElem(lastVisiableItems);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisiableItem + 5 >= staggeredGridLayoutManager.getItemCount()) {
                    LoadMore();
                }
            }
        });
        getNetData();
    }

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            if(!isEditting){
                dantuArrayList.clear();
                page = 1;
                getNetData();
            }
            dantuRefreshLayout.setRefreshing(false);
        }
    };

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    private void LoadMore(){
        if(!isEditting){
            isLoadMore = true;
            page++;
            getNetData();
            if(danTuAdapter!=null){
                danTuAdapter.loadMoreData(true);
            }
        }
    }


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (dantuRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    private int page = 1;
    private int pageSize = 20;
    private void getNetData(){
        if(Util.isNetAvailable(mContext)){
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String type = sp.getString("typeid", "1");
            String userid = sp.getString("userid", Constant.DEFAULT_USER_ID);
            String _id = sp.getString("id", "");
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> h = new HashMap<String, Object>();
            h.put("token", Util.getDateToken());
            h.put("uid", _id);
            h.put("user_type", type);
            h.put("page_size", pageSize);
            h.put("page", page);

            Util.setErrorLog(TAG, userid + " <<<=====>>  " + Util.getDateToken());

            okHttpUtil.post(Constant.FAV_TU_URL, h, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Util.setToast(mContext, "系统繁忙，请求失败。");
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    if(json.contains("data")){
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            final String msg = jsonObject.getString("msg");
                            if(jsonObject.getInt("status") == 200){
                                JSONArray array = jsonObject.getJSONArray("data");
                                for(int i=0;i<array.length();i++){
                                    _ImageS img = gson.fromJson(array.getJSONObject(i).toString(), _ImageS.class);
                                    img.setSeleteStatus(false);
                                    dantuArrayList.add(img);
                                }


                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        dantuRefreshLayout.setRefreshing(false);
                                        if(danTuAdapter!=null){
                                            danTuAdapter.loadMoreData(false);
                                        }

                                        if(danTuAdapter == null){
                                            danTuAdapter = new DanTuAdapter(mContext, dantuArrayList);
                                            reclerviewDantu.setAdapter(danTuAdapter);
                                            danTuAdapter.notifyDataSetChanged();
                                        }else{
                                            if(isLoadMore){
                                                isLoadMore = false;
                                                danTuAdapter.notifyDataSetChanged();
                                            }else {
                                                danTuAdapter.notifyItemInserted(dantuArrayList.size() - pageSize);
                                            }
                                        }


                                        danTuAdapter.setDantuItemClickListener(new DanTuAdapter.OnDantuItemClickListener() {
                                            @Override
                                            public void OnDantuItemClickListener(int position, ArrayList<_ImageS> dantuList) {
                                                if(isEditting){
                                                    // 正在编辑删除中
                                                    _ImageS bean = dantuList.get(position);
                                                    boolean isSelect = bean.getSeleteStatus();
                                                    if (!isSelect) {
                                                        bean.setSeleteStatus(true);
                                                        deletDantuSelectIdList.add(bean.getCollect_id());
                                                        deletingEntity.add(bean);
                                                    } else {
                                                        deletingEntity.remove(bean);
                                                        deletDantuSelectIdList.remove(bean.getCollect_id());
                                                        bean.setSeleteStatus(false);
                                                    }
                                                    danTuAdapter.notifyDataSetChanged();
                                                }else {
                                                    // 没有编辑删除中

                                                    String imgJson = new Gson().toJson(dantuArrayList);
                                                    SpUtil.setSingImageListJson(mContext, imgJson);
                                                    Intent intent = new Intent(mContext, SImageLookingActivity.class);
                                                    intent.putExtra("mPosition", position);
                                                    intent.putExtra("mWhereFrom","DanTuActivity");
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                        showNoData();

                                    }
                                });
                            }else if(jsonObject.getInt("status") == 200 || jsonObject.getInt("status") == 0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Util.setToast(mContext, msg);
                                        dantuRefreshLayout.setRefreshing(false);
                                        if(danTuAdapter!=null){
                                            danTuAdapter.loadMoreData(false);
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void setDeleteFlag(boolean flag){
        if(danTuAdapter!=null){
            danTuAdapter.setDeletingStutas(flag);
            if(flag){
                danTuAdapter.notifyDataSetChanged();
            }else {
                // 非删除状态，隐藏删除选择标识
                danTuAdapter.setDeletingStutas(flag);
            }
            danTuAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick({R.id.relBackDantu, R.id.tvDelelteDantu,R.id.tvEditDantu})
    public void onDanTuAcitivityViewClicked(View v) {
        switch (v.getId()){
            case R.id.relBackDantu:
                if(isEditting){
                    isEditting = !isEditting;
                    relDeleteDatu.setVisibility(View.GONE);
                    tvEditDantu.setText("编辑");
                    isDeletingDantu = false;
                    setDeleteFlag(isDeletingDantu);
                }else{
                    finish();
                }
                break;
            case R.id.tvEditDantu:
                if(isEditting){
                    // 关闭编辑状态
                    relDeleteDatu.setVisibility(View.GONE);
                    tvEditDantu.setText("编辑");
                    isDeletingDantu = false;
                    setDeleteFlag(isDeletingDantu);
                }else {
                    // 开启编辑状态
                    relDeleteDatu.setVisibility(View.VISIBLE);
                    tvEditDantu.setText("取消");
                    isDeletingDantu = true;
                    setDeleteFlag(isDeletingDantu);
                }
                isEditting = !isEditting;
                break;

            case R.id.tvDelelteDantu:

                tvEditDantu.setText("编辑");
                relDeleteDatu.setVisibility(View.GONE);
                isDeletingDantu = false;
                setDeleteFlag(isDeletingDantu);
                isEditting = !isEditting;


                //  删除套图的请求
                int size = deletDantuSelectIdList.size();
                if(size>0){
                    String idString = "";
                    for(int i=0;i<size;i++){
                        if(i!=size-1){
                            idString += deletDantuSelectIdList.get(i) + ",";
                        }else {
                            idString += deletDantuSelectIdList.get(i);
                        }
                    }
                    OKHttpUtil okHttpUtil = new OKHttpUtil();
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("token", Util.getDateToken());
                    hashMap.put("ids", idString); // idString
                    Util.setErrorLog(TAG, "==收藏单图的id号=>>" + idString);
                    okHttpUtil.post(Constant.SHANCHU_URL1, hashMap, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.setToast(mContext, "删除失败");
                                }
                            });
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String json = response.body().string();
                            Util.setErrorLog(TAG, "==删除单图结果=>>" + json);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    try {
                                        JSONObject object = new JSONObject(json);
                                        String msg = object.getString("msg");

                                        if(object.getInt("status") == 200){
                                            for(int i=0; i<deletingEntity.size();i++){
                                                danTuAdapter.getDantuEntityList().remove(deletingEntity.get(i));
                                            }
                                            danTuAdapter.setDeletingStutas(false);
                                            Util.setToast(mContext, "取消收藏!");
                                            EventBusUtil.sendEvent(new Event(EC.EventCode.DELETE_TAOTU_CODE));
                                        }else {
                                            Util.setToast(mContext, msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    showNoData();
                                }
                            });
                        }
                    });
                }else{
                    Util.setToast(mContext, "你没有选择套图");
                }
                break;
        }
    }


    private void showNoData(){
        if(danTuAdapter!=null){
            if(dantuArrayList.size()==0){
                rel_no_dantu.setVisibility(View.VISIBLE);
            }else {
                rel_no_dantu.setVisibility(View.GONE);
            }
        }
    }



    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case EC.EventCode.DELETE_DANTU_LIST_CODE:
                int pos = (int) event.getData();
                if(danTuAdapter!=null){
                    dantuArrayList.remove(pos);
                    danTuAdapter.notifyDataSetChanged();
                    EventBusUtil.sendEvent(new Event(EC.EventCode.DELETE_TAOTU_CODE));
                }
                break;
        }
        showNoData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!isEditting){
            isEditting = true;
            relDeleteDatu.setVisibility(View.GONE);
            tvEditDantu.setText("编辑");
            isDeletingDantu = false;
            setDeleteFlag(isDeletingDantu);
            return true;
        }else{
            if(keyCode == KeyEvent.KEYCODE_BACK){
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}