package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.MyAttentionAdapter;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.entity._MyAttention;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAttentionActivity extends BaseActivity {
    private Context mContext;
    private String TAG = "MyAttentionActivity";
    private ImageView attentionBack;//返回按钮
    private RecyclerView attentionRecycleView;//显示关注用户的列表
    private MyAttentionAdapter myAdapter;
    private List<_MyAttention> myAttentionList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        mContext = MyAttentionActivity.this;
        bindView();
        initView();
        HttpGetMyAttention();
        initViewEvent();
    }

    private void bindView() {
        attentionBack = (ImageView) findViewById(R.id.attention_back);
        attentionRecycleView = (RecyclerView) findViewById(R.id.attention_recycleview);
    }

    private void initView() {
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        attentionRecycleView.setLayoutManager(linearLayoutManager);
    }

    private void initViewEvent() {
        attentionBack.setOnClickListener(occl);
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.attention_back:
                    finish();
                    break;
            }
        }
    };

    private void HttpGetMyAttention() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        okHttpUtil.post(Constant.MY_ATTENTION_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        Log.e(TAG, "请求成功！====" + json);
                        String data = jsonObject.getString("data");
                        parseJson(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e(TAG, "请求失败！====" + e.toString());
            }

            @Override
            public void onError(Response response, int code) {
                Log.e(TAG, "请求错误！====" + code);
            }
        });
    }

    private void parseJson(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                _MyAttention attention = new _MyAttention(jsonArray.get(i).toString());
                myAttentionList.add(attention);
            }
            Log.e(TAG, "数据请求成功===布局中集合长度==" + myAttentionList.size());
            if (myAdapter == null) {
                myAdapter = new MyAttentionAdapter(mContext, myAttentionList);
                myAdapter.setOnMyAttentionItemClickLister(onMyAttentionClick);
                attentionRecycleView.setAdapter(myAdapter);
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private MyAttentionAdapter.OnMyAttentionItemClickLister onMyAttentionClick = new MyAttentionAdapter.OnMyAttentionItemClickLister() {
        @Override
        public void onItemClick(View view) {
            //进入关注对象的主页
            Intent intent1 = new Intent(mContext, NewAuthorDetailActivity.class);
            int position = attentionRecycleView.getChildPosition(view);
            intent1.putExtra("author_id", myAttentionList.get(position).getAid());
            intent1.putExtra("page_num", myAttentionList.get(position).getArticle_count());
            startActivity(intent1);
        }
    };
}
