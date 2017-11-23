package com.tbs.tobosutype.activity;
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
import com.google.gson.Gson;
import com.tbs.tobosutype.bean._ImageD;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.TaoTuAdapter;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.SpUtil;
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

/**
 * 套图页
 */
public class TaoTuAcitivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.relBackTao)
    RelativeLayout relBackTao;
    @BindView(R.id.recyclerviewTaotu)
    RecyclerView recyclerviewTaotu;
    private String TAG = "TaoTuAcitivity";
    @BindView(R.id.tvEditTaotu)
    TextView tvEditTaotu;
    @BindView(R.id.relDeleteTaotu)
    RelativeLayout relDeleteTaotu;
    @BindView(R.id.taotuSwipe)
    SwipeRefreshLayout taotuSwipe;
    private boolean isEditTaoTu = false;
    @BindView(R.id.rel_no_taotu)
    RelativeLayout rel_no_taotu;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<_ImageD> taotuEntityArrayList = new ArrayList<_ImageD>();
    private TaoTuAdapter taoTuAdapter;
    private ArrayList<String> deletTaotuSelectIdList = new ArrayList<String>();
    private ArrayList<_ImageD> deletingEntity = new ArrayList<_ImageD>();
    private boolean isLoadMore = false;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = TaoTuAcitivity.this;
        setContentView(R.layout.activity_taotu);
        ButterKnife.bind(this);
        initViews();
    }


    private void initViews(){
        taotuSwipe.setColorSchemeColors(Color.RED, Color.BLUE);
        taotuSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(!isEditTaoTu){
                    page = 1;
                    taotuEntityArrayList.clear();
                    getNetData();
                }
                taotuSwipe.setRefreshing(false);

            }
        });

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerviewTaotu.setLayoutManager(staggeredGridLayoutManager);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        ((SimpleItemAnimator) recyclerviewTaotu.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerviewTaotu.setOnTouchListener(onTouchListener);
        recyclerviewTaotu.addOnScrollListener(onScrollListener);
        getNetData();
    }


    private int page = 1;
    private int pageSize = 20;
    private void getNetData(){
        if(Util.isNetAvailable(mContext)){
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String type = sp.getString("typeid", "1");
            String userid = sp.getString("userid", Constant.DEFAULT_USER_ID);
            String _id = sp.getString("id", "");
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("uid", _id);
            hashMap.put("type", "1");
            hashMap.put("user_type", type);
            hashMap.put("page_size", pageSize);
            hashMap.put("page", page);

            okHttpUtil.post(Constant.FAV_TAO_TU_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "服务繁忙，稍后再试。");
                            taotuSwipe.setRefreshing(false);
                            if(taoTuAdapter!=null){
                                taoTuAdapter.loadMoreData(false);
                            }
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Util.setErrorLog(TAG, json);

                    try {
                        JSONObject object = new JSONObject(json);
                        final String msg = object.getString("msg");
                        if(object.getInt("status") == 200){
                            JSONArray array = object.getJSONArray("data");
                            for(int i=0;i<array.length();i++){
                                _ImageD entity = gson.fromJson(array.getJSONObject(i).toString(), _ImageD.class);
                                entity.setSeleteStatus(false);
                                taotuEntityArrayList.add(entity);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    taotuSwipe.setRefreshing(false);
                                    if(taoTuAdapter!=null){
                                        taoTuAdapter.loadMoreData(false);
                                    }

                                    if(taoTuAdapter == null){
                                        taoTuAdapter = new TaoTuAdapter(mContext, taotuEntityArrayList);
                                        recyclerviewTaotu.setAdapter(taoTuAdapter);
                                        taoTuAdapter.notifyDataSetChanged();
                                    }else {
                                        if(isLoadMore){
                                            isLoadMore = false;
                                            taoTuAdapter.notifyDataSetChanged();
                                        }else {
                                            taoTuAdapter.notifyItemInserted(taotuEntityArrayList.size() - pageSize);
                                        }
                                    }

                                    taoTuAdapter.setTaotuItemClickListener(new TaoTuAdapter.OnTaotuItemClickListener() {

                                        @Override
                                        public void OnTaotuItemClickListener(int position, ArrayList<_ImageD> taotuList) {

                                            if(isEditTaoTu){
                                                // 正在编辑删除中
                                                _ImageD bean = taotuList.get(position);
                                                boolean isSelect = bean.isSeleteStatus();
                                                if (!isSelect) {
                                                    bean.setSeleteStatus(true);
                                                    deletTaotuSelectIdList.add(bean.getCollect_id());
                                                    deletingEntity.add(bean);
                                                } else {
                                                    deletingEntity.remove(bean);
                                                    deletTaotuSelectIdList.remove(bean.getCollect_id());
                                                    bean.setSeleteStatus(false);
                                                }
                                                taoTuAdapter.notifyDataSetChanged();
                                            }else {
                                                // 没有编辑删除时
                                                String DImageJson = gson.toJson(taotuEntityArrayList);
                                                Util.setErrorLog(TAG, DImageJson);
                                                SpUtil.setDoubleImageListJson(mContext, DImageJson);
                                                Intent intent = new Intent(mContext, DImageLookingActivity.class);
                                                intent.putExtra("mPosition", position);
                                                intent.putExtra("mWhereFrom", "TaotuActivity");
                                                startActivity(intent);

                                            }
                                        }
                                    });

                                    showNoData();
                                }
                            });
                        }else if(object.getInt("status") == 201 || object.getInt("status") == 0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    taotuSwipe.setRefreshing(false);
                                    Util.setToast(mContext, msg);
                                    if(taoTuAdapter!=null){
                                        taoTuAdapter.notifyDataSetChanged();
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
        if(!isEditTaoTu){
            isLoadMore = true;
            page++;
            getNetData();
            if(taoTuAdapter!=null){
                taoTuAdapter.loadMoreData(true);
            }
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
                // 非删除状态，隐藏删除选择标识
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
                    relDeleteTaotu.setVisibility(View.GONE);
                    isDeletingTaotu = false;
                    setDeleteFlag(isDeletingTaotu);
                }else {
                    finish();
                }
                break;
            case R.id.tvDelelteTaotu:
                tvEditTaotu.setText("编辑");
                relDeleteTaotu.setVisibility(View.GONE);
                isDeletingTaotu = false;
                setDeleteFlag(isDeletingTaotu);
                isEditTaoTu = !isEditTaoTu;

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
                                    Util.setToast(mContext, "取消收藏失败!");
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

                                        if(object.getInt("status") == 200){
                                            for(int i=0; i<deletingEntity.size();i++){
                                                taoTuAdapter.getTaotuEntityList().remove(deletingEntity.get(i));
                                            }
                                            taoTuAdapter.setDeletingStutas(false);
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
            case R.id.tvEditTaotu:
                if (isEditTaoTu){
                    tvEditTaotu.setText("编辑");
                    relDeleteTaotu.setVisibility(View.GONE);
                    isDeletingTaotu = false;
                    setDeleteFlag(isDeletingTaotu);
                }else {
                    tvEditTaotu.setText("取消");
                    relDeleteTaotu.setVisibility(View.VISIBLE);
                    isDeletingTaotu = true;
                    setDeleteFlag(isDeletingTaotu);
                }
                isEditTaoTu = !isEditTaoTu;
                break;
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case EC.EventCode.DELETE_TAOTU_LIST_CODE:
                int pos = (int) event.getData();
                if(taoTuAdapter!=null){
                    taotuEntityArrayList.remove(pos);
                    taoTuAdapter.notifyDataSetChanged();
                    EventBusUtil.sendEvent(new Event(EC.EventCode.DELETE_TAOTU_CODE));
                }
                showNoData();
                break;
        }
    }

    private void showNoData(){
        if(taoTuAdapter!=null){
            if(taotuEntityArrayList.size()==0){
                rel_no_taotu.setVisibility(View.VISIBLE);
            }else {
                rel_no_taotu.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isEditTaoTu){
            isEditTaoTu = !isEditTaoTu;
            relDeleteTaotu.setVisibility(View.GONE);
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
