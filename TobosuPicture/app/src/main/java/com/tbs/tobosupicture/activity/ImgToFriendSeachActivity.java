package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.DynamicBaseAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._DynamicBase;
import com.tbs.tobosupicture.bean._Guess;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.CustomWaitDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 以图会友的搜索页面
 * 从以图会友的搜索按钮进入
 */
public class ImgToFriendSeachActivity extends BaseActivity {
    @BindView(R.id.img_to_friend_search_input)
    EditText imgToFriendSearchInput;//输入框
    @BindView(R.id.img_to_friend_search_btn)
    LinearLayout imgToFriendSearchBtn;//搜索按钮兼取消按钮
    @BindView(R.id.img_to_friend_id_search_ll)
    LinearLayout imgToFriendIdSearchLl;//整个大家都在搜的显示框
    @BindView(R.id.img_to_friend_show_search)
    RecyclerView imgToFriendShowSearch;//显示搜索结果的列表
    @BindView(R.id.mg_to_friend_show_search_ll)
    LinearLayout mgToFriendShowSearchLl;//显示搜索结果列表的显示框
    @BindView(R.id.img_to_friend_search_btn_text)
    TextView imgToFriendSearchBtnText;//搜索按钮兼取消按钮中的文字显示
    @BindView(R.id.itf_search_tv1)
    TextView itfSearchTv1;
    @BindView(R.id.itf_search_rl1)
    RelativeLayout itfSearchRl1;
    @BindView(R.id.itf_search_tv2)
    TextView itfSearchTv2;
    @BindView(R.id.itf_search_rl2)
    RelativeLayout itfSearchRl2;
    @BindView(R.id.itf_search_tv3)
    TextView itfSearchTv3;
    @BindView(R.id.itf_search_rl3)
    RelativeLayout itfSearchRl3;
    @BindView(R.id.itf_search_tv4)
    TextView itfSearchTv4;
    @BindView(R.id.itf_search_rl4)
    RelativeLayout itfSearchRl4;
    @BindView(R.id.itf_search_tv5)
    TextView itfSearchTv5;
    @BindView(R.id.itf_search_rl5)
    RelativeLayout itfSearchRl5;
    @BindView(R.id.itf_search_tv6)
    TextView itfSearchTv6;
    @BindView(R.id.itf_search_rl6)
    RelativeLayout itfSearchRl6;
    @BindView(R.id.itf_search_tv7)
    TextView itfSearchTv7;
    @BindView(R.id.itf_search_rl7)
    RelativeLayout itfSearchRl7;
    @BindView(R.id.itf_search_tv8)
    TextView itfSearchTv8;
    @BindView(R.id.itf_search_rl8)
    RelativeLayout itfSearchRl8;
    @BindView(R.id.itf_search_tv9)
    TextView itfSearchTv9;
    @BindView(R.id.itf_search_rl9)
    RelativeLayout itfSearchRl9;
    @BindView(R.id.itf_search_tv10)
    TextView itfSearchTv10;
    @BindView(R.id.itf_search_rl10)
    RelativeLayout itfSearchRl10;
    @BindView(R.id.img_to_friend_search_none_data)
    LinearLayout imgToFriendSearchNoneData;


    private Context mContext;
    private String TAG = "ImgToFriendSeach";
    private int mPage = 1;
    private Gson mGson;
    private CustomWaitDialog customWaitDialog;
    private List<RelativeLayout> relativeLayoutList = new ArrayList<>();//大家都在搜的布局
    private List<TextView> textViewList = new ArrayList<>();//大家都在搜的文本显示
    private List<_Guess> guessList = new ArrayList<>();//猜你想搜的请求回来的集合
    private LinearLayoutManager linearLayoutManager;
    private List<_DynamicBase> dynamicBaseList = new ArrayList<>();
    private DynamicBaseAdapter dynamicBaseAdapter;
    private boolean isLoading = false;
    private String key_word = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_to_friend_seach);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        //获取搜索历史（猜你想搜）
        HttpGetGuess();
    }

    private void initViewEvent() {
        mGson = new Gson();
        imgToFriendSearchInput.addTextChangedListener(textWatcher);
        customWaitDialog = new CustomWaitDialog(mContext);
        //设置搜索之后的集合
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        imgToFriendShowSearch.setLayoutManager(linearLayoutManager);
        imgToFriendShowSearch.addOnScrollListener(onScrollListener);
        //添加布局进集合
        relativeLayoutList.add(itfSearchRl1);
        relativeLayoutList.add(itfSearchRl2);
        relativeLayoutList.add(itfSearchRl3);
        relativeLayoutList.add(itfSearchRl4);
        relativeLayoutList.add(itfSearchRl5);
        relativeLayoutList.add(itfSearchRl6);
        relativeLayoutList.add(itfSearchRl7);
        relativeLayoutList.add(itfSearchRl8);
        relativeLayoutList.add(itfSearchRl9);
        relativeLayoutList.add(itfSearchRl10);
        //添加文本进集合
        textViewList.add(itfSearchTv1);
        textViewList.add(itfSearchTv2);
        textViewList.add(itfSearchTv3);
        textViewList.add(itfSearchTv4);
        textViewList.add(itfSearchTv5);
        textViewList.add(itfSearchTv6);
        textViewList.add(itfSearchTv7);
        textViewList.add(itfSearchTv8);
        textViewList.add(itfSearchTv9);
        textViewList.add(itfSearchTv10);
    }

    //recyclerview 滑动事件
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0
                    && lastVisiableItem + 4 >= linearLayoutManager.getItemCount()
                    && !isLoading) {
                loadMore();
            }
        }
    };

    //加载更多数据
    private void loadMore() {
        Log.e(TAG, "执行加载更多======");
        mPage++;
        HttpGetSeachList(mPage, key_word);
    }

    //文本输入监听事件
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                imgToFriendSearchBtnText.setText("搜索");
            } else if (s.length() == 0) {
                imgToFriendSearchBtnText.setText("取消");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick({R.id.img_to_friend_search_btn, R.id.itf_search_tv1, R.id.itf_search_tv2, R.id.itf_search_tv3,
            R.id.itf_search_tv4, R.id.itf_search_tv5, R.id.itf_search_tv6,
            R.id.itf_search_tv7, R.id.itf_search_tv8, R.id.itf_search_tv9, R.id.itf_search_tv10})
    public void onViewClickedInImgToFriendSeacher(View view) {
        switch (view.getId()) {
            case R.id.img_to_friend_search_btn:
                if (imgToFriendSearchBtnText.getText().toString().equals("取消")) {
                    finish();
                } else {
                    //得到key
                    key_word = imgToFriendSearchInput.getText().toString();
                    //大家都在搜的显示框隐藏
                    imgToFriendIdSearchLl.setVisibility(View.GONE);
                    //显示加载的蒙层
                    customWaitDialog.show();
                    //显示搜索的列表
                    mgToFriendShowSearchLl.setVisibility(View.VISIBLE);
                    //进行搜索请求
                    mPage = 1;
                    if (!dynamicBaseList.isEmpty()) {
                        dynamicBaseList.clear();
                    }
                    HttpGetSeachList(mPage, key_word);
                }
                break;
            case R.id.itf_search_tv1:
                tvClickChange(itfSearchTv1);
                break;
            case R.id.itf_search_tv2:
                tvClickChange(itfSearchTv2);
                break;
            case R.id.itf_search_tv3:
                tvClickChange(itfSearchTv3);
                break;
            case R.id.itf_search_tv4:
                tvClickChange(itfSearchTv4);
                break;
            case R.id.itf_search_tv5:
                tvClickChange(itfSearchTv5);
                break;
            case R.id.itf_search_tv6:
                tvClickChange(itfSearchTv6);
                break;
            case R.id.itf_search_tv7:
                tvClickChange(itfSearchTv7);
                break;
            case R.id.itf_search_tv8:
                tvClickChange(itfSearchTv8);
                break;
            case R.id.itf_search_tv9:
                tvClickChange(itfSearchTv9);
                break;
            case R.id.itf_search_tv10:
                tvClickChange(itfSearchTv10);
                break;
        }
    }

    //大家都在搜的单独项点击事件
    private void tvClickChange(TextView textView) {
        //拿到点击的数据
        key_word = textView.getText().toString();
        //大家都在搜的显示框隐藏
        imgToFriendIdSearchLl.setVisibility(View.GONE);
        //输入框显示点击的文字
        imgToFriendSearchInput.setText(key_word);
        //显示加载的蒙层
        customWaitDialog.show();
        //显示搜索的列表
        mgToFriendShowSearchLl.setVisibility(View.VISIBLE);
        //进行搜索请求
        HttpGetSeachList(mPage, key_word);
    }

    //获取搜索结果列表
    private void HttpGetSeachList(int page, String key_word) {
        imgToFriendSearchBtnText.setText("取消");
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        if (Utils.userIsLogin(mContext)) {
            param.put("uid", SpUtils.getUserUid(mContext));
        }
        param.put("title", key_word);
        param.put("page", page);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.SOCIAL_SEARCH, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功======" + "=page=" + mPage + "===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        final JSONArray jsonArray = jsonObject.getJSONArray("data");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        _DynamicBase dynamicBase = mGson.fromJson(jsonArray.get(i).toString(), _DynamicBase.class);
                                        dynamicBaseList.add(dynamicBase);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "json解析出错====" + e.toString());
                                    }
                                }
                                imgToFriendSearchNoneData.setVisibility(View.GONE);
                                if (dynamicBaseAdapter == null) {
                                    dynamicBaseAdapter = new DynamicBaseAdapter(mContext, ImgToFriendSeachActivity.this, dynamicBaseList);
                                    imgToFriendShowSearch.setAdapter(dynamicBaseAdapter);
                                    dynamicBaseAdapter.notifyDataSetChanged();
                                } else {
                                    dynamicBaseAdapter.notifyDataSetChanged();
                                }
                                customWaitDialog.dismiss();
                                isLoading = false;
                            }
                        });
                    } else if (status.equals("201")) {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customWaitDialog.dismiss();
                                isLoading = false;
                                if (dynamicBaseAdapter != null) {
                                    dynamicBaseAdapter.changeAdapterState(2);
                                }
                                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                                if (dynamicBaseList.isEmpty()) {
                                    //没有添加任何数据显示占位图
                                    imgToFriendSearchNoneData.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }

            }
        });

    }

    //获取猜你想搜（大家都在搜）列表
    private void HttpGetGuess() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        HttpUtils.doPost(UrlConstans.GET_SEARCH_RECORD, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _Guess guess = mGson.fromJson(jsonArray.get(i).toString(), _Guess.class);
                            guessList.add(guess);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < guessList.size(); i++) {
                                    relativeLayoutList.get(i).setVisibility(View.VISIBLE);
                                    textViewList.get(i).setText(guessList.get(i).getKey_word());
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
