package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.NewAuthorDetailAdapter;
import com.tobosu.mydecorate.entity._AuthorDetail;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewAuthorDetailActivity extends AppCompatActivity {
    private Context mContext;
    private String TAG = "NewAuthorDetailActivity";
    private _AuthorDetail authorDetail;

    private String author_id = "";//作者的id 要从上一个界面中传来
    private String page_num;//文章数量
    private Intent mIntent;
    private int mPage = 1;

    private LinearLayout new_author_back;//返回键
    private CustomWaitDialog mCustomWaitDialog;
    private ImageView riv_concerned_head_picture;//用户的头像
    private TextView tv_concern_username;//作者的名称
    private TextView tv_is_concernuser_concerned;//用户对作者的关注状态
    private TextView tv_total_num;//作者的文章总数
    private RecyclerView recycle_user_acticles;//文章的列表

    private LinearLayoutManager mLinearLayoutManager;//列表管理者
    private NewAuthorDetailAdapter authorDetailAdapter;//列表适配器
    private List<_AuthorDetail.Article> articleList = new ArrayList<>();//文章列表集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_new_author_detail);
        mPage = 1;
        initBaseData();
        bindView();
        initView();
        initViewEvent();
        HttpGetAuthorDetail(mPage, page_num);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.isLogin(mContext)) {
            HttpGetIsFollow();
        }
    }

    private void initBaseData() {
        mIntent = getIntent();
        author_id = mIntent.getStringExtra("author_id");
        page_num = mIntent.getStringExtra("page_num");
    }

    private void bindView() {
        new_author_back = (LinearLayout) findViewById(R.id.new_author_back);
        riv_concerned_head_picture = (ImageView) findViewById(R.id.riv_concerned_head_picture);
        tv_concern_username = (TextView) findViewById(R.id.tv_concern_username);
        tv_is_concernuser_concerned = (TextView) findViewById(R.id.tv_is_concernuser_concerned);
        tv_total_num = (TextView) findViewById(R.id.tv_total_num);
        recycle_user_acticles = (RecyclerView) findViewById(R.id.recycle_user_acticles);
    }

    private void initView() {
        mCustomWaitDialog = new CustomWaitDialog(mContext);
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mLinearLayoutManager.setSmoothScrollbarEnabled(true);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        recycle_user_acticles.setLayoutManager(mLinearLayoutManager);
        recycle_user_acticles.setHasFixedSize(true);
        recycle_user_acticles.setNestedScrollingEnabled(false);
    }

    private void initViewEvent() {
        new_author_back.setOnClickListener(occl);
        tv_is_concernuser_concerned.setOnClickListener(occl);
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.new_author_back:
                    //返回按钮
                    finish();
                    break;
                case R.id.tv_is_concernuser_concerned:
                    //关注作者按钮
                    break;
            }
        }
    };


    private void HttpGetAuthorDetail(int mPage, String mPageSize) {
        mCustomWaitDialog.show();
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("author_id", author_id);
        if (Util.isLogin(mContext)) {
            param.put("uid", Util.getUserId(mContext));
        }
        param.put("page", mPage);
        if (TextUtils.isEmpty(mPageSize)) {
            param.put("page_size", "100");
        } else {
            param.put("page_size", "100");
        }

        okHttpUtil.post(Constant.AUTHOR_DETAIL_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "请求回来的数据=====" + json);
                parseJson(json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e(TAG, "请求错误=====" + e.toString());
            }

            @Override
            public void onError(Response response, int code) {
                Log.e(TAG, "请求失败=====" + code);
            }
        });
    }

    private void parseJson(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
                //数据请求成功  解析数据
                authorDetail = new _AuthorDetail(jsonObject.getString("data"));
                GlideUtils.glideLoader(mContext, authorDetail.getAuthor().getIcon(),
                        0, R.mipmap.jiazai_loading, riv_concerned_head_picture, GlideUtils.CIRCLE_IMAGE);
                tv_concern_username.setText("" + authorDetail.getAuthor().getNick());
                if (authorDetail.getAuthor().getIs_follow().equals("0")) {
                    tv_is_concernuser_concerned.setText("关注");
                    tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color_normal));
                    tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concern);
                    tv_is_concernuser_concerned.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Util.isLogin(mContext)) {
                                HttpGetFollow();
                            } else {
                                startActivity(new Intent(mContext, LoginActivity.class));
                            }
                        }
                    });
                }
//                if (authorDetail.getAuthor().getIs_follow().equals("1")) {
//                    //用户处于关注状态
//                    tv_is_concernuser_concerned.setText("已关注");
//                    tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concerned);
//                    tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color));
//                    tv_is_concernuser_concerned.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //请求取消关注
//                            HttpGetFollow();
//                        }
//                    });
//                } else {
//                    //用户位未关注该作者
//                    tv_is_concernuser_concerned.setText("关注");
//                    tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color_normal));
//                    tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concern);
//                    tv_is_concernuser_concerned.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (Util.isLogin(mContext)) {
//                                HttpGetFollow();
//                            } else {
//                                startActivity(new Intent(mContext, LoginActivity.class));
//                            }
//                        }
//                    });
//                }
                tv_total_num.setText("" + authorDetail.getAuthor().getArticle_count());

                articleList.addAll(authorDetail.getArticleList());
                Log.e(TAG, "获取的集合长度====" + authorDetail.getArticleList().size() + "===" + articleList.size());
                if (authorDetail.getArticleList().size() == 0) {
                    //当前没有更多数据
                    Toast.makeText(mContext, "没有更多数据了~", Toast.LENGTH_SHORT).show();
                    if(authorDetailAdapter!=null){
                        authorDetailAdapter.changeAdapterState(1);
                    }
                }
                if (authorDetailAdapter == null) {
                    authorDetailAdapter = new NewAuthorDetailAdapter(mContext, articleList);
                    recycle_user_acticles.setAdapter(authorDetailAdapter);
                    authorDetailAdapter.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
                    authorDetailAdapter.notifyDataSetChanged();
                } else {
                    authorDetailAdapter.notifyDataSetChanged();
                }
                mCustomWaitDialog.dismiss();
            } else {
                mCustomWaitDialog.dismiss();
                Toast.makeText(mContext, "网络连接错误！", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            mCustomWaitDialog.dismiss();
            e.printStackTrace();
        }
    }

    //作者和当前用户的关系 是否关注
    private void HttpGetIsFollow() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("author_id", author_id);
        okHttpUtil.post(Constant.IS_FOLLOW_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                parseIsFollowJson(json);
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    private void parseIsFollowJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
                //请求成功
                String is_follow = jsonObject.getJSONObject("data").getString("is_follow");
                if (is_follow.equals("1")) {
                    //关注状态
                    tv_is_concernuser_concerned.setText("已关注");
                    tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concerned);
                    tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color));
                    tv_is_concernuser_concerned.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //请求取消关注
                            HttpGetFollow();
                        }
                    });
                } else {
                    //用户位未关注该作者
                    tv_is_concernuser_concerned.setText("关注");
                    tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color_normal));
                    tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concern);
                    tv_is_concernuser_concerned.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Util.isLogin(mContext)) {
                                HttpGetFollow();
                            } else {
                                startActivity(new Intent(mContext, LoginActivity.class));
                            }
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //用户的关注与取消关注
    private void HttpGetFollow() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("author_id", author_id);
        param.put("system_type", "1");
        okHttpUtil.post(Constant.FOLLOW_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "关注===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String msg = jsonObject.getString("msg");
                    if (msg.equals("关注成功")) {
                        Toast.makeText(mContext, "关注成功！", Toast.LENGTH_SHORT).show();
                        tv_is_concernuser_concerned.setText("已关注");
                        tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concerned);
                        tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color));
                    } else {
                        Toast.makeText(mContext, "取消关注成功！", Toast.LENGTH_SHORT).show();
                        tv_is_concernuser_concerned.setText("关注");
                        tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color_normal));
                        tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concern);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    private NewAuthorDetailAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = new NewAuthorDetailAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerViewItemClick(View view) {
            int position = recycle_user_acticles.getChildPosition(view);
            Intent intent = new Intent(mContext, NewArticleDetailActivity.class);
            intent.putExtra("id", articleList.get(position).getAid());
            intent.putExtra("author_id", articleList.get(position).getUid());
            startActivity(intent);
        }
    };
}
