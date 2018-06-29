package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.ActicleAdapter;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.database.DBManager;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;
import com.tobosu.mydecorate.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dec on 2016/10/14.
 */

public class WriterActivity extends BaseActivity {
    private static final String TAG = WriterActivity.class.getSimpleName();
    private Context mContext;

    private FrameLayout framelayout_top;

    private RoundImageView riv_concerned_head_picture;

    private TextView tv_concern_username;
    private TextView tv_is_concernuser_concerned;

    private TextView tv_total_num;

    private RecyclerView recycle_user_acticles;

    private String writer_url = Constant.ZHJ + "tapp/mt/readUser";

    /**** 取消关注接口 */
    private String cancel_concern_url = Constant.ZHJ + "tapp/mt/cancelAttention";

    private String user_head_pic_url = "";

    private String userName = "";

    private String totalNum = "";

    private ArrayList<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();

    private CustomWaitDialog waitDialog;

    //    private RelativeLayout writer_include_loading_layout;
    private RelativeLayout writer_include_netout_layout;
    private NestedScrollView writer_container;
    private String is_att = "";
    private String aid = "";
    private String writer_uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern_user);
        mContext = WriterActivity.this;

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "====onStart===");
        initView();
        initDatas();
    }

    private void initView() {
        framelayout_top = (FrameLayout) findViewById(R.id.framelayout_top);
        framelayout_top.bringToFront();
        framelayout_top = (FrameLayout) findViewById(R.id.framelayout_top);
        framelayout_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Constant.GOBACK_MAINACTIVITY_RESULTCODE);
                finish();
            }
        });

        riv_concerned_head_picture = (RoundImageView) findViewById(R.id.riv_concerned_head_picture);
        tv_total_num = (TextView) findViewById(R.id.tv_total_num);
        tv_concern_username = (TextView) findViewById(R.id.tv_concern_username);
        tv_is_concernuser_concerned = (TextView) findViewById(R.id.tv_is_concernuser_concerned);
        recycle_user_acticles = (RecyclerView) findViewById(R.id.recycle_user_acticles);

        writer_include_netout_layout = (RelativeLayout) findViewById(R.id.writer_include_netout_layout);
        writer_container = (NestedScrollView) findViewById(R.id.writer_container);
    }

    private void showLoadingView() {
        waitDialog = new CustomWaitDialog(mContext);
        waitDialog.show();
    }

    private void hideLoadingView() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }

    private void initDatas() {

        Bundle b = getIntent().getBundleExtra("Writer_User_Bundle");
//        aid = b.getString("aid");
        writer_uid = b.getString("writer_uid");
        if (Util.isLogin(mContext)) {
            HttpIsFollow();
        } else {
            is_att = "0";
            tv_is_concernuser_concerned.setText("关注");
            tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color_normal));
            tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concern);

            tv_is_concernuser_concerned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            });
        }
        Log.e(TAG, "当前获取的用户的关注状态=======" + is_att);
        userName = b.getString("nick");
        user_head_pic_url = b.getString("header_pic_url");

        tv_concern_username.setText(userName);

        Picasso.with(mContext)
                .load(user_head_pic_url)
                .fit()
                .placeholder(R.mipmap.occupied1)
                .into(riv_concerned_head_picture);

        writer_container.setVisibility(View.GONE);
        if (Util.isNetAvailable(mContext)) {
            // 有网络
            showLoadingView();
            Util.showNetOutView(mContext, writer_include_netout_layout, true);

            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("page", "0");
            params.put("pageSize", "10");
            params.put("uid", getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("user_id", "")); // 登錄用戶id
            params.put("aid", writer_uid); // 被查看的用戶id

            System.out.println("------查看作者用戶頁面-----------");

            okHttpUtil.post(writer_url, params, new OKHttpUtil.BaseCallBack() {
                @Override
                public void onSuccess(Response response, String json) {
                    System.out.println("------查看作者用戶 結果---" + json);
                    hideLoadingView();
                    writer_container.setVisibility(View.VISIBLE);
                    try {
                        JSONObject writerObject = new JSONObject(json);

                        if (writerObject.getInt("error_code") == 0) {
                            JSONObject data = writerObject.getJSONObject("data");
                            JSONArray dataArray = data.getJSONArray("data");

                            for (int i = 0, len = dataArray.length(); i < len; i++) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("aid", dataArray.getJSONObject(i).getString("aid"));
                                hashMap.put("uid", dataArray.getJSONObject(i).getString("uid"));
                                hashMap.put("title", dataArray.getJSONObject(i).getString("title"));
                                hashMap.put("type_id", dataArray.getJSONObject(i).getString("type_id"));
                                hashMap.put("tup_count", dataArray.getJSONObject(i).getString("tup_count"));
                                hashMap.put("collect_count", dataArray.getJSONObject(i).getString("collect_count"));
                                hashMap.put("show_count", dataArray.getJSONObject(i).getString("show_count"));
                                hashMap.put("image_url", dataArray.getJSONObject(i).getString("image_url"));
                                hashMap.put("time", dataArray.getJSONObject(i).getJSONObject("time_create").getString("time"));
                                hashMap.put("time_unit", dataArray.getJSONObject(i).getJSONObject("time_create").getString("time_unit"));
                                hashMap.put("view_count", dataArray.getJSONObject(i).getString("view_count"));
                                hashMap.put("type_name", dataArray.getJSONObject(i).getString("type_name"));
                                itemList.add(hashMap);
                            }

                            tv_total_num.setText(data.getString("article_num"));


                        } else if (writerObject.getInt("error_code") == 201) {
                            // 沒有數據
                        } else if (writerObject.getInt("error_code") == 250) {

                        }
                        initAdapter();
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
        } else {
            Util.showNetOutView(mContext, writer_include_netout_layout, false);
        }

    }

    private String concern_writer_user_url = Constant.ZHJ + "tapp/mt/attentionUser";

    //获取作者详情(新的接口)


    //查看用户与作者之间的关系
    private void HttpIsFollow() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("author_id", writer_uid);
        okHttpUtil.post(Constant.IS_FOLLOW_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        is_att = data.getString("is_follow");
                        Log.e(TAG, "获取当前用户和作者的关系====" + json + "当前用户与作者的关系==" + is_att);
                        if (is_att.equals("1")) {
                            tv_is_concernuser_concerned.setText("已关注");
                            tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concerned);
                            tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color));
                            tv_is_concernuser_concerned.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HttpGetFollow();
                                }
                            });

                        } else if (is_att.equals("0")) {
                            tv_is_concernuser_concerned.setText("关注");
                            tv_is_concernuser_concerned.setTextColor(getResources().getColor(R.color.concern_color_normal));
                            tv_is_concernuser_concerned.setBackgroundResource(R.drawable.shape_bg_button_article_concern);
                            tv_is_concernuser_concerned.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HttpGetFollow();
                                }
                            });
                        }
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


    private void initAdapter() {


        ActicleAdapter articleAdapter = new ActicleAdapter(mContext, itemList);


        //创建默认的线性LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recycle_user_acticles.setLayoutManager(layoutManager);

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recycle_user_acticles.setHasFixedSize(true);
        recycle_user_acticles.setNestedScrollingEnabled(false);

        recycle_user_acticles.setAdapter(articleAdapter);
        articleAdapter.setOnRecyclerViewItemClickListener(new ActicleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View view, HashMap<String, String> itemData) {
                goDetailActivity(itemData.get("aid"),
                        itemData.get("type_id"),
                        itemData.get("title"),
                        itemData.get("image_url"),
                        itemData.get("uid"));
            }
        });

    }

    private void goDetailActivity(String _articleId, String _typeId, String _articleTitle, String _articleTitlePicUrl,
                                  String _writerUserId) {
        //新版的跳转
        Intent intent = new Intent(mContext, NewArticleDetailActivity.class);
        intent.putExtra("id", _articleId);
        intent.putExtra("author_id", _writerUserId);
        startActivity(intent);
    }


    //用户的关注与取消关注
    private void HttpGetFollow() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("author_id", writer_uid);
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


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Log.e(TAG, "====onResume===");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        Log.e(TAG, "====onPause===");
    }
}
