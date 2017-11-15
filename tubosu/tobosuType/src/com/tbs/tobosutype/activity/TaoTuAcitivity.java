package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.TaoTuAdapter;
import com.tbs.tobosutype.bean.TaotuEntity;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
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

public class TaoTuAcitivity extends AppCompatActivity {
    @BindView(R.id.relBackTao)
    RelativeLayout relBackTao;
    @BindView(R.id.recyclerviewTaotu)
    RecyclerView recyclerviewTaotu;
    private Context context;
    private String TAG = "TaoTuAcitivity";
    @BindView(R.id.tvEditTaotu)
    TextView tvEditTaotu;
    @BindView(R.id.tvDelelteTaotu)
    TextView tvDelelteTaotu;
    @BindView(R.id.taotuSwipe)
    SwipeRefreshLayout taotuSwipe;
    private boolean isEditTaoTu = false;
    @BindView(R.id.ivTaotuNoData)
    ImageView ivTaotuNoData;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<TaotuEntity> taotuEntityArrayList = new ArrayList<TaotuEntity>();
    private TaoTuAdapter taoTuAdapter;
    private ArrayList<String> deletTaotuSelectIdList = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = TaoTuAcitivity.this;
        setContentView(R.layout.activity_taotu);
        ButterKnife.bind(this);
        initViews();

    }


    private void initViews(){
        taotuSwipe.setColorSchemeColors(Color.RED, Color.BLUE);
        taotuSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isDeletingTaotu){
                    page = 1;
                    taotuEntityArrayList.clear();
                    taoTuAdapter = null;
                    getNetData();
                }
            }
        });



        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerviewTaotu.setLayoutManager(staggeredGridLayoutManager);
        recyclerviewTaotu.setOnTouchListener(onTouchListener);
        recyclerviewTaotu.addOnScrollListener(onScrollListener);
        getNetData();
    }


    private int page = 1;
    private void getNetData(){
        if(Util.isNetAvailable(context)){
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String type = sp.getString("typeid", "1");
            String userid = sp.getString("userid", "272286");
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("uid", userid);
            hashMap.put("user_type", type);
            hashMap.put("page_size", "10");
            hashMap.put("page", page);

            okHttpUtil.post(Constant.FAV_TAO_TU_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(context, "服务繁忙，稍后再试。");
                            taotuSwipe.setRefreshing(false);
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            taotuSwipe.setRefreshing(false);
                            if(taoTuAdapter!=null){
                                taoTuAdapter.loadMoreData(false);
                            }
                            try {
                                JSONObject object = new JSONObject(json);
                                String msg = object.getString("msg");
                                if(object.getInt("status") == 200){
                                    JSONArray array = object.getJSONArray("data");
                                    for(int i=0;i<array.length();i++){
                                        TaotuEntity entity = new TaotuEntity();
                                        entity.setId(array.getJSONObject(i).getString("id"));
                                        entity.setCollect_id(array.getJSONObject(i).getString("collect_id"));
                                        entity.setCover_url(array.getJSONObject(i).getString("cover_url"));
                                        entity.setImage_width(array.getJSONObject(i).getInt("image_width"));
                                        entity.setImage_height(array.getJSONObject(i).getInt("image_height"));
                                        entity.setTitle(array.getJSONObject(i).getString("title"));
                                        entity.setSeleteStatus(false);
                                        taotuEntityArrayList.add(entity);
                                    }

                                    initAdapter();
                                }else if(object.getInt("status") == 201 || object.getInt("status") == 0){
                                    Util.setToast(context, msg);
                                    if(taoTuAdapter!=null){
                                        taoTuAdapter.notifyDataSetChanged();
                                    }
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

    private void initAdapter(){
        if(taoTuAdapter == null){
            taoTuAdapter = new TaoTuAdapter(context, taotuEntityArrayList);
            recyclerviewTaotu.setAdapter(taoTuAdapter);
            taoTuAdapter.notifyDataSetChanged();
        }else {
            taoTuAdapter.notifyDataSetChanged();
        }
        taoTuAdapter.setTaotuItemClickListener(new TaoTuAdapter.OnTaotuItemClickListener() {
            @Override
            public void OnTaotuItemClickListener(int position, ArrayList<TaotuEntity> taotuList) {
                if(isEditTaoTu){
                    // 正在编辑删除中
                    Util.setToast(context, "选中 ");
                    TaotuEntity bean = taotuList.get(position);
                    boolean isSelect = bean.isSeleteStatus();
                    if (!isSelect) {
                        bean.setSeleteStatus(true);
                        deletTaotuSelectIdList.add(bean.getCollect_id());
                    } else {
                        deletTaotuSelectIdList.remove(bean.getCollect_id());
                        bean.setSeleteStatus(false);
                    }
                    taoTuAdapter.notifyDataSetChanged();
                }else {
                    // 没有编辑删除中
                    Util.setToast(context, "跳转 ");

                }
            }
        });

    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
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
        page++;
        getNetData();
        if(taoTuAdapter!=null){
            taoTuAdapter.loadMoreData(true);
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (taotuSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    private boolean isDeletingTaotu = false;
    private void setDeleteFlag(boolean flag){
        if(taoTuAdapter!=null){
            taoTuAdapter.setDeletingStutas(flag);
            if(flag){
                taoTuAdapter.notifyDataSetChanged();
            }else {
                taoTuAdapter.setDeletingStutas(flag);
            }
            taoTuAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.relBackTao, R.id.tvDelelteTaotu, R.id.tvEditTaotu})
    public void onTaoTuAcitivityViewClicked(View v) {
        switch (v.getId()){
            case R.id.relBackTao:
                if(isEditTaoTu){
                    isEditTaoTu = !isEditTaoTu;
                    tvEditTaotu.setText("编辑");
                    tvDelelteTaotu.setVisibility(View.GONE);
                    isDeletingTaotu = false;
                    setDeleteFlag(isDeletingTaotu);
                }else {
                    finish();
                }
                break;
            case R.id.tvDelelteTaotu:
                 // 删除套图的请求
                int size = deletTaotuSelectIdList.size();
                if(size>0){
                    String idString = "";
                    for(int i=0;i<size;i++){
                        if(i!=size-1){
                            idString += deletTaotuSelectIdList.get(i) + ",";
                        }else {
                            idString += deletTaotuSelectIdList.get(i);
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
                                            taoTuAdapter.setDeletingStutas(false);
                                            taoTuAdapter.notifyDataSetChanged();
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
                    Util.setToast(context, "你没有选择套图");
                }
                break;
            case R.id.tvEditTaotu:
                if (isEditTaoTu){
                    tvEditTaotu.setText("编辑");
                    tvDelelteTaotu.setVisibility(View.GONE);
                    isDeletingTaotu = false;
                    setDeleteFlag(isDeletingTaotu);
                }else {
                    tvEditTaotu.setText("取消");
                    tvDelelteTaotu.setVisibility(View.VISIBLE);
                    isDeletingTaotu = true;
                    setDeleteFlag(isDeletingTaotu);
                }
                isEditTaoTu = !isEditTaoTu;
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isEditTaoTu){
            isEditTaoTu = !isEditTaoTu;
            tvDelelteTaotu.setVisibility(View.GONE);
            tvEditTaotu.setText("编辑");
            isDeletingTaotu = false;
            setDeleteFlag(isDeletingTaotu);
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
