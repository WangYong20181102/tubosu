package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.DynamicDetailAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._DynamicDetail;
import com.tbs.tobosupicture.bean._ReturnContent;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.MyEditText;

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
 * create by lin
 * 动态详情页
 * 用户在以图会友页面中 的最热 最新 的列表中点击回复可以进入到该页面
 */
public class DynamicDetailActivity extends BaseActivity {

    @BindView(R.id.dynd_back)
    RelativeLayout dyndBack;
    @BindView(R.id.dynd_recycle)
    RecyclerView dyndRecycle;
    @BindView(R.id.dynd_revert)
    EditText dyndRevert;
    @BindView(R.id.dynd_send)
    TextView dyndSend;
    @BindView(R.id.dynd_swipe)
    SwipeRefreshLayout dyndSwipe;

    private Context mContext;
    private String TAG = "DynamicDetailActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private int mPage = 1;
    private Intent mIntent;
    //动态id
    private String dynamicId;
    //被评论的用户Id号(发动态的这个人的id号)
    private String commentedUid;
    //被评论的用户是否是虚拟用户 1--是 2--否（发动态的这个人是否是虚拟用户）
    private String is_virtual_user;
    //回复列表
    private ArrayList<_DynamicDetail.Comment> commentArrayList = new ArrayList<>();
    private ArrayList<_DynamicDetail.Comment> tempCommentArrayList = new ArrayList<>();
    //是否正在加载数据
    private boolean isLoading = false;
    //列表适配器
    private DynamicDetailAdapter dynamicDetailAdapter;
    //存储评论当前作者内容的Map
    private HashMap<String, String> mDynamicMap;
    //存储当前评论列表用户的内容
    private HashMap<String, String> mCommentMap;
    //列表的position 用于定位标识评论对象所在的位置
    private int mPosition = -1;
    //
    private Gson mGson;
    //以下三个参数(mFrom,msg_type,location_id)只有在定位的时候用到
    private String mFrom = "";//从哪个页面来的参数
    private String msg_type = "";//消息类型
    private String location_id = "";//定位到某一条消息的id
    //是否要监听键盘的弹起
    private boolean isListerSoftShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        ButterKnife.bind(this);
        mContext = this;
        mIntent = getIntent();
        initViewEvent();
    }

    private void initViewEvent() {
        mGson = new Gson();

        dynamicId = mIntent.getStringExtra("dynamic_id");
        commentedUid = mIntent.getStringExtra("commented_uid");
        is_virtual_user = mIntent.getStringExtra("is_virtual_user");
        mFrom = mIntent.getStringExtra("from");
        msg_type = mIntent.getStringExtra("msg_type");
        location_id = mIntent.getStringExtra("location_id");

        mDynamicMap = new HashMap<>();
        mCommentMap = new HashMap<>();

        dyndSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dyndSwipe.setBackgroundColor(Color.WHITE);
        dyndSwipe.setSize(SwipeRefreshLayout.DEFAULT);
        dyndSwipe.setOnRefreshListener(onRefreshListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        dyndRecycle.setLayoutManager(mLinearLayoutManager);
        dyndRecycle.setOnTouchListener(onTouchListener);
        dyndRecycle.addOnScrollListener(onScrollListener);

        dyndRevert.addTextChangedListener(tw);
        dyndRevert.setOnFocusChangeListener(focusChangeListener);

        if ((!TextUtils.isEmpty(mFrom)) && mFrom.equals("dynamic_msg_adapter")) {
            HttpGetDynamic(300);
        } else {
            HttpGetDynamic(10);
        }

    }

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.e(TAG, "键盘当前已经获取到焦点=======");
                CheckSoftIsShowing();
            }
        }
    };
    //输入框的监听事件
    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (start == 0 && before == 1) {
                if (dyndRevert.getHint().toString().equals("写评论...")) {
                    //作者的
                    mDynamicMap.put(dynamicId, dyndRevert.getText().toString());
                } else {
                    //用户的
                    mCommentMap.put(commentArrayList.get(mPosition - 1).getId(), dyndRevert.getText().toString());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (dyndRevert.getHint().toString().equals("写评论...")) {
                //写好之后存数据
                if (!TextUtils.isEmpty(dyndRevert.getText().toString())) {
                    mDynamicMap.put(dynamicId, dyndRevert.getText().toString());
                }
            } else {
                if (!TextUtils.isEmpty(dyndRevert.getText().toString())) {
                    mCommentMap.put(commentArrayList.get(mPosition - 1).getId(), dyndRevert.getText().toString());
                }
            }
        }
    };

    //判断软键盘是否开启
    private boolean isSoftShowing() {
        int screenHeight = getWindow().getDecorView().getHeight();
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    //监听键盘开启的监听器
    private void CheckSoftIsShowing() {
        Log.e(TAG, "执行监听----------");
        isListerSoftShowing = true;//开始监听键盘的弹起状态
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isListerSoftShowing) {
                    try {
                        Thread.sleep(800);
                        if (isSoftShowing()) {
                            //键盘处于开启的状态
                            Log.e(TAG, "键盘监听器开启中。。。。。当前键盘属于开启中");
                        } else {
                            //键盘属于收起的状态 设置输入框的hint 以及关闭键盘的监听
                            isListerSoftShowing = false;//关闭键盘的监听
                            Log.e(TAG, "键盘监听器开启中。。。。。当前键盘属于收起中");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dyndRevert.setText("");
                                    dyndRevert.setHint("写评论...");
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //上拉刷新
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !dyndSwipe.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
    //触碰事件
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (dyndSwipe.isRefreshing()) {
                return true;
            } else {
                // TODO: 2017/10/16  在这个地方写键盘的相关操作 键盘收回，输入框的hint修改
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(dyndRevert.getWindowToken(), 0);
                dyndRevert.setText("");
                dyndRevert.setHint("写评论...");
                return false;
            }
        }
    };
    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            reFresh();
        }
    };

    //下拉刷新数据
    private void reFresh() {
        mFrom = "";
        mPage = 1;
        if (!commentArrayList.isEmpty()) {
            commentArrayList.clear();
        }
        HttpGetDynamic(10);
    }

    //加载更多数据
    private void loadMore() {
        mPage++;
        adapterLoadState();
        //加载更多数据调取另一个接口
        HttpGetComment(mPage);
    }

    @OnClick({R.id.dynd_back, R.id.dynd_send, R.id.dynd_revert})
    public void onViewClickedInDynamicDetailActivity(View view) {
        switch (view.getId()) {
            case R.id.dynd_back:
                finish();
                break;
            case R.id.dynd_send:
                //发送评论
                if (Utils.userIsLogin(mContext)) {
                    sendComment();
                } else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.dynd_revert:
                //点击了回复的输入框
                if (!Utils.userIsLogin(mContext)) {
                    Utils.gotoLogin(mContext);
                } else {
                    //用户已经登录
                    if (dyndRevert.getHint().equals("写评论...")) {
                        if (!TextUtils.isEmpty(mDynamicMap.get(dynamicId))) {
                            dyndRevert.setText(mDynamicMap.get(dynamicId));
                            dyndRevert.setSelection(mDynamicMap.get(dynamicId).length());
                        }
                    }
                    CheckSoftIsShowing();
                }
                break;
        }
    }

    //发送评论
    private void sendComment() {
        //评论的内容
        String comment = dyndRevert.getText().toString();
        Log.e(TAG, "当前获取输入框的消息===============" + comment + ";");
        if (TextUtils.isEmpty(comment.trim())) {
            Toast.makeText(mContext, "当前您没有输入评论内容~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dyndRevert.getHint().toString().equals("写评论...")) {
            //回复作者本人
            HttpSendComment(comment);
        } else {
            //回复用户
            HttpReplyComment(comment, commentArrayList.get(mPosition - 1).getId());
        }
    }

    //发送评论 回复用户  接口:回复评论2
    private void HttpReplyComment(String content, String parent_id) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("dynamic_id", dynamicId);
        param.put("parent_id", parent_id);
        param.put("content", content);
        param.put("type", "2");
        HttpUtils.doPost(UrlConstans.REPLY_COMMENT_TOW, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "回复某一用户的返回结果=======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功 将键盘收起 输入框滞空 将数据填充 局部刷新数据
                        mCommentMap.remove(commentArrayList.get(mPosition - 1).getId());
                        String data = jsonObject.getString("data");
                        _ReturnContent returnContent = mGson.fromJson(data, _ReturnContent.class);
                        String content = returnContent.getContent();
                        String nick = returnContent.getNick();
                        String c_nick = "";
                        _DynamicDetail.Comment.ChildComment mChildComment = new _DynamicDetail.Comment.ChildComment(content, nick, c_nick);
                        commentArrayList.get(mPosition - 1).getChild_comment().add(0, mChildComment);//加载数据
                        int reply_count = Integer.parseInt(commentArrayList.get(mPosition - 1).getReply_count()) + 1;//获取数据
                        commentArrayList.get(mPosition - 1).setReply_count("" + reply_count);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(dyndRevert.getWindowToken(), 0);
                                dyndRevert.setText("");
                                dyndRevert.setHint("写评论...");
                                dynamicDetailAdapter.notifChildAdapterData();
                                dynamicDetailAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //发送评论动态 回复作者本人
    private void HttpSendComment(String comment) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("user_type", "2");
        param.put("dynamic_id", dynamicId);
        param.put("commented_uid", commentedUid);
        param.put("is_virtual_user", is_virtual_user);
        param.put("content", comment);
        param.put("type", "2");
        HttpUtils.doPost(UrlConstans.USER_SEND_COMMENT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "评论失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String id = data.getString("id");
                        String uid = data.getString("uid");
                        String is_virtual_user = data.getString("is_virtual_user");
                        String content = data.getString("content");
                        String praise_count = data.getString("praise_count");
                        String add_time = data.getString("add_time");
                        String nick = data.getString("nick");
                        String icon = data.getString("icon");
                        String reply_count = data.getString("reply_count");
                        _DynamicDetail.Comment comment1 = new _DynamicDetail.Comment(id, uid, is_virtual_user, content, praise_count, add_time, nick, icon, "0", reply_count, new ArrayList<_DynamicDetail.Comment.ChildComment>());
                        commentArrayList.add(0, comment1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //评论成功  添加数据
                                Toast.makeText(mContext, "评论成功！", Toast.LENGTH_SHORT).show();
                                dynamicDetailAdapter.notifyDataSetChanged();
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(dyndRevert.getWindowToken(), 0);
                                mDynamicMap.clear();
                                dyndRevert.setText("");
//                                reFresh();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "评论失败！", Toast.LENGTH_SHORT).show();
                                dyndRevert.setText("");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //查看详情页 用于点击进入详情页判断当前登录的用户是否点过赞或者评论过
    private void HttpGetDynamic(int page_size) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("dynamic_id", dynamicId);
        param.put("page_size", page_size);
        if (Utils.userIsLogin(mContext)) {
            param.put("uid", SpUtils.getUserUid(mContext));
        }

        HttpUtils.doPost(UrlConstans.DYNAMIC_DETAIL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
                isLoading = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dyndSwipe.setRefreshing(false);
                        if (dynamicDetailAdapter != null) {
                            dynamicDetailAdapter.changeAdapterState(2);
                        }
                        Toast.makeText(mContext, "服务器链接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        final _DynamicDetail dynamicDetail = gson.fromJson(data, _DynamicDetail.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dyndSwipe.setRefreshing(false);
                                commentArrayList.addAll(dynamicDetail.getComment());
                                dynamicDetailAdapter = new DynamicDetailAdapter(mContext, DynamicDetailActivity.this, dynamicDetail, commentArrayList);
                                dyndRecycle.setAdapter(dynamicDetailAdapter);
                                dynamicDetailAdapter.setOnDynamicDetailClickLister(onDynamicDetailClickLister);
                                dynamicDetailAdapter.notifyDataSetChanged();
                                if (dynamicDetailAdapter != null) {
                                    dynamicDetailAdapter.changeAdapterState(2);
                                }
                                if ((!TextUtils.isEmpty(mFrom)) && mFrom.equals("dynamic_msg_adapter")) {
                                    //如果消息类型是是评论则定位到某一条回复
                                    if (msg_type.equals("2")) {
                                        int position = -1;
                                        for (int i = 0; i < commentArrayList.size(); i++) {
                                            if (commentArrayList.get(i).getId().equals(location_id)) {
                                                position = i;
                                                break;
                                            }
                                        }
                                        Log.e(TAG, "当前定位到的位置============" + position);
                                        dyndRecycle.scrollToPosition(position + 1);
                                    }
                                } else {
                                    dyndRecycle.scrollToPosition(0);
                                }
                            }
                        });
                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dynamicDetailAdapter != null) {
                                    dynamicDetailAdapter.changeAdapterState(2);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }
                isLoading = false;
            }
        });
    }

    //列表中的点击回复事件
    private DynamicDetailAdapter.OnDynamicDetailClickLister onDynamicDetailClickLister = new DynamicDetailAdapter.OnDynamicDetailClickLister() {
        @Override
        public void onDetailClick(View view, int position) {
            switch (view.getId()) {
                case R.id.dynamic_detail_comment_revert:
                    Log.e(TAG, "点击了回复按钮======" + commentArrayList.get(position - 1).getNick());
                    mPosition = position;
                    Log.e(TAG, "点击回复获取的位置信息===================" + mPosition);
                    dyndRevert.setHint("回复 " + commentArrayList.get(position - 1).getNick() + ":");
//                    parent_id = commentArrayList.get(position - 1).getId();
                    if (!TextUtils.isEmpty(mCommentMap.get(commentArrayList.get(position - 1).getId()))) {
                        dyndRevert.setText(mCommentMap.get(commentArrayList.get(position - 1).getId()));
                        dyndRevert.setSelection(commentArrayList.get(position - 1).getId().length());
                    }
                    dyndRevert.requestFocus();
                    InputMethodManager imm = (InputMethodManager) dyndRevert.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    CheckSoftIsShowing();//开启监听器
                    break;
            }
        }
    };

    //获取更多的评论内容
    private void HttpGetComment(int mPage) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("dynamic_id", dynamicId);
        param.put("page", mPage);
        if ((!TextUtils.isEmpty(mFrom)) && mFrom.equals("dynamic_msg_adapter")) {
            //从适配器进来的 应服务端要求改变page_size查询数据
            param.put("page_size", 300);
        } else {
            param.put("page_size", 10);
        }
        HttpUtils.doPost(UrlConstans.DYNAMIC_COMMETN_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
                isLoading = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dyndSwipe.setRefreshing(false);
                        if (dynamicDetailAdapter != null) {
                            dynamicDetailAdapter.changeAdapterState(2);
                        }
                        Toast.makeText(mContext, "服务器链接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功
                        Gson gson = new Gson();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonArray.get(i).toString();
                            _DynamicDetail.Comment comment = gson.fromJson(jsonArray.get(i).toString(), _DynamicDetail.Comment.class);
                            tempCommentArrayList.add(comment);
                        }
                        commentArrayList.addAll(tempCommentArrayList);
                        tempCommentArrayList.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dynamicDetailAdapter == null) {
                                    dynamicDetailAdapter = new DynamicDetailAdapter(mContext, DynamicDetailActivity.this, commentArrayList);
                                    dyndRecycle.setAdapter(dynamicDetailAdapter);
                                    dynamicDetailAdapter.notifyDataSetChanged();
                                } else {
                                    dynamicDetailAdapter.notifyDataSetChanged();
                                }
                                adapterRevert();
                            }
                        });
                    } else if (status.equals("201")) {
                        //没有更多数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!commentArrayList.isEmpty()) {
                                    Toast.makeText(mContext, "没有更多评论~", Toast.LENGTH_SHORT).show();
                                }
                                adapterRevert();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //获取数据失败
                                Toast.makeText(mContext, "亲~数据获取出了问题，估计是服务器挂了！Waiting...", Toast.LENGTH_SHORT).show();
                                adapterRevert();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isLoading = false;
                }
                isLoading = false;
            }
        });
    }

    //适配器恢复正常状态
    private void adapterRevert() {
        if (dynamicDetailAdapter != null) {
            dynamicDetailAdapter.changeAdapterState(2);
        }
    }

    //适配器处于加载状态
    private void adapterLoadState() {
        if (dynamicDetailAdapter != null) {
            dynamicDetailAdapter.changeAdapterState(1);
        }
    }
}
