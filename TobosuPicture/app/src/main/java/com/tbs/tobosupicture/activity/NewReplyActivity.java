package com.tbs.tobosupicture.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.NewReplyAdapter;
import com.tbs.tobosupicture.bean._NewReply;
import com.tbs.tobosupicture.bean._ReturnContent;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

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
 * 新的回复界面
 */
public class NewReplyActivity extends AppCompatActivity {

    @BindView(R.id.new_reply_back)
    LinearLayout newReplyBack;
    @BindView(R.id.new_reply_recyclerview)
    RecyclerView newReplyRecyclerview;
    @BindView(R.id.new_reply_swipe)
    SwipeRefreshLayout newReplySwipe;
    @BindView(R.id.new_reply_revert)
    EditText newReplyRevert;
    @BindView(R.id.new_reply_send)
    TextView newReplySend;

    private Context mContext;
    private String TAG = "NewReplyActivity";
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading = false;//是否正在加载数据
    private String comment_id = "";
    private Intent mIntent;
    private Gson mGson;
    private ArrayList<_NewReply.ChildCommentBean> childCommentBeanList = new ArrayList<>();
    private _NewReply mNewReply;
    private NewReplyAdapter mNewReplyAdapter;
    private Activity mActivity;
    private int mPage = 1;
    private HashMap<String, String> mDynamicMap;//存储评论当前作者的map
    private HashMap<String, String> mCommentMap;//存储当前评论用户的内容map
    private int mPosition = -1;//回复某用户时的定位标识
    //以下两个参数用于消息的定位
    private String from = "";
    private String location_id = "";
    private String msg_type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reply);
        mContext = this;
        mActivity = this;
        ButterKnife.bind(this);
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        comment_id = mIntent.getStringExtra("comment_id");
        from = mIntent.getStringExtra("from");
        location_id = mIntent.getStringExtra("location_id");
        msg_type = mIntent.getStringExtra("msg_type");

        mGson = new Gson();
        mCommentMap = new HashMap<>();
        mDynamicMap = new HashMap<>();
        //下拉刷新事件
        newReplySwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        newReplySwipe.setBackgroundColor(Color.WHITE);
        newReplySwipe.setSize(SwipeRefreshLayout.DEFAULT);
        newReplySwipe.setOnRefreshListener(onRefreshListener);
        //设置recyclerview相关的事件
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        newReplyRecyclerview.setLayoutManager(mLinearLayoutManager);
        newReplyRecyclerview.setOnTouchListener(onTouchListener);
        newReplyRecyclerview.addOnScrollListener(onScrollListener);
        //设置监听事件
        newReplyRevert.addTextChangedListener(tv);
        //一进来数据的请求
        if ((!TextUtils.isEmpty(from)) && from.equals("dynamic_msg_adapter")) {
            //从适配器进来的
            HttpGetNewReplyList(300);
        } else {
            HttpGetNewReplyList(20);
        }
    }

    //事件监听事件
    private TextWatcher tv = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (start == 0 && before == 1) {
                if (newReplyRevert.getHint().toString().equals("写评论...")) {
                    //作者的
                    mDynamicMap.put(comment_id, newReplyRevert.getText().toString());
                } else {
                    //用户的
                    mCommentMap.put(childCommentBeanList.get(mPosition - 1).getId(), newReplyRevert.getText().toString());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (newReplyRevert.getHint().toString().equals("写评论...")) {
                //写好之后存数据
                if (!TextUtils.isEmpty(newReplyRevert.getText().toString())) {
                    mDynamicMap.put(comment_id, newReplyRevert.getText().toString());
                }
            } else {
                if (!TextUtils.isEmpty(newReplyRevert.getText().toString())) {
                    mCommentMap.put(childCommentBeanList.get(mPosition - 1).getId(), newReplyRevert.getText().toString());
                }
            }
        }
    };

    //初始的加载数据
    private void HttpGetNewReplyList(int page_size) {
        isLoading = true;
        newReplySwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("comment_id", comment_id);
        param.put("page_size", page_size);
        if (Utils.userIsLogin(mContext)) {
            param.put("uid", SpUtils.getUserUid(mContext));
        }
        HttpUtils.doPost(UrlConstans.COMMENT_DETAIL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //获取数据失败
                Log.e(TAG, "链接失败=====" + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "服务器错误！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        if (mNewReply != null) {
                            mNewReply = null;
                        }
                        mNewReply = mGson.fromJson(data, _NewReply.class);
                        //设置适配器数据
                        childCommentBeanList.addAll(mNewReply.getChild_comment());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mNewReplyAdapter = new NewReplyAdapter(mContext, mActivity, mNewReply.getComment(), childCommentBeanList);
                                mNewReplyAdapter.setOnItemClickLister(onItemClickLister);
                                newReplyRecyclerview.setAdapter(mNewReplyAdapter);
                                mNewReplyAdapter.notifyDataSetChanged();
                                mNewReplyAdapter.changeAdapterState(2);

                                if ((!TextUtils.isEmpty(from)) && from.equals("dynamic_msg_adapter")) {
                                    if (msg_type.equals("2")) {
                                        //当前位评论消息
                                        int position = -1;
                                        for (int i = 0; i < childCommentBeanList.size(); i++) {
                                            if (childCommentBeanList.get(i).getId().equals(location_id)) {
                                                position = i;
                                                break;
                                            }
                                        }
                                        Log.e(TAG, "当前定位到的位置============" + position + "===当前定位消息的id===" + location_id);
                                        newReplyRecyclerview.scrollToPosition(position + 1);
                                    }
                                } else {
                                    newReplyRecyclerview.scrollToPosition(0);
                                }
                            }
                        });
                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mNewReplyAdapter != null) {
                                    mNewReplyAdapter.changeAdapterState(2);
                                }
                                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    isLoading = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "抛出异常=====" + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mNewReplyAdapter != null) {
                                mNewReplyAdapter.changeAdapterState(2);
                            }
                        }
                    });
                    isLoading = false;
                }
            }
        });
    }

    //适配器子项点击事件
    private NewReplyAdapter.OnItemClickLister onItemClickLister = new NewReplyAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            Log.e(TAG, "点击了子项======" + position);
            mPosition = position;
            newReplyRevert.setHint("回复 " + childCommentBeanList.get(position - 1).getNick());
            if (!TextUtils.isEmpty(mCommentMap.get(childCommentBeanList.get(position - 1).getId()))) {
                newReplyRevert.setText(mCommentMap.get(childCommentBeanList.get(position - 1).getId()));
            }
            newReplyRevert.requestFocus();
            InputMethodManager imm = (InputMethodManager) newReplyRevert.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    };

    //获取更多数据网络请求
    private void HttpGetMoreData(int mPage) {
        isLoading = true;
        newReplySwipe.setRefreshing(false);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("parent_id", comment_id);
        param.put("page", mPage);
        if ((!TextUtils.isEmpty(from)) && from.equals("dynamic_msg_adapter")) {
            param.put("page_size", "300");
        } else {
            param.put("page_size", "20");
        }
        HttpUtils.doPost(UrlConstans.COMMENT_DETAIL_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败======" + e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接数据成功=====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //有数据
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _NewReply.ChildCommentBean childCommentBean = mGson.fromJson(jsonArray.get(i).toString(), _NewReply.ChildCommentBean.class);
                            childCommentBeanList.add(childCommentBean);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mNewReplyAdapter == null) {
                                    mNewReplyAdapter = new NewReplyAdapter(mContext, mActivity, childCommentBeanList);
                                    newReplyRecyclerview.setAdapter(mNewReplyAdapter);
                                    mNewReplyAdapter.notifyDataSetChanged();
                                } else {
                                    mNewReplyAdapter.notifyDataSetChanged();
                                }
                                adapterRevert();
                            }
                        });
                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!childCommentBeanList.isEmpty()) {
                                    Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                                }
                                adapterRevert();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void adapterRevert() {
        if (mNewReplyAdapter != null) {
            mNewReplyAdapter.changeAdapterState(2);
        }
    }

    private void adapterLoadState() {
        if (mNewReplyAdapter != null) {
            mNewReplyAdapter.changeAdapterState(1);
        }
    }

    //获取更多的列表数据
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (newReplySwipe.isRefreshing()) {
                return true;
            } else {
                // TODO: 2017/10/16  在这个地方写键盘的相关操作 键盘收回，输入框的hint修改
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(newReplyRevert.getWindowToken(), 0);
                newReplyRevert.setText("");
                newReplyRevert.setHint("写评论...");
                return false;
            }
        }
    };
    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= mLinearLayoutManager.getItemCount()
                    && !newReplySwipe.isRefreshing()
                    && !isLoading) {
                //加载更多
                loadMore();
            }
        }
    };

    //加载更多
    private void loadMore() {
        mPage++;
        if (mNewReplyAdapter != null) {
            mNewReplyAdapter.changeAdapterState(1);
        }
        HttpGetMoreData(mPage);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新
            initHttpGet();
        }
    };

    private void initHttpGet() {
        from = "";
        mPage = 1;
        mNewReply = null;
        if (!childCommentBeanList.isEmpty()) {
            childCommentBeanList.clear();
        }
        HttpGetNewReplyList(20);
    }

    @OnClick({R.id.new_reply_back, R.id.new_reply_send, R.id.new_reply_revert})
    public void onViewClickedInNewReplyActivity(View view) {
        switch (view.getId()) {
            case R.id.new_reply_back:
                //返回键
                finish();
                break;
            case R.id.new_reply_send:
                //发送评论
                if (Utils.userIsLogin(mContext)) {
                    sendComment();
                } else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.new_reply_revert:
                if (!Utils.userIsLogin(mContext)) {
                    Utils.gotoLogin(mContext);
                } else {
                    //用户已经登录
                    if (newReplyRevert.getHint().equals("写评论...")) {
                        Log.e(TAG, "用户已经登录===设置信息中===" + mDynamicMap.get(comment_id));
                        if (!TextUtils.isEmpty(mDynamicMap.get(comment_id))) {
                            newReplyRevert.setText(mDynamicMap.get(comment_id));
                        }
                    }
                }
                break;
        }
    }

    private void sendComment() {
        //评论的内容
        String comment = newReplyRevert.getText().toString();
        if (TextUtils.isEmpty(comment.trim())) {
            Toast.makeText(mContext, "当前您没有输入评论内容~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newReplyRevert.getHint().toString().equals("写评论...")) {
            //回复作者本人
            HttpSendComment(comment);
        } else {
            //回复用户
            HttpReplyComment(comment, childCommentBeanList.get(mPosition - 1).getUid());
        }
    }

    //回复作者
    private void HttpSendComment(String comment) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("dynamic_id", mNewReply.getComment().getDynamic_id());//上一条动态的id
        param.put("parent_id", mNewReply.getComment().getId());//当前的id
        param.put("content", comment);
        param.put("type", "2");
        HttpUtils.doPost(UrlConstans.REPLY_COMMENT_TOW, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=======" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "回复某一用户返回的结果======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功，将数据添加到列表中展示  json数据转成对象处理直接添加到列表中
                        String data = jsonObject.getString("data");
                        //移除回显数据
                        mDynamicMap.remove(comment_id);
                        //新建回复数据
                        _ReturnContent returnContent = mGson.fromJson(data, _ReturnContent.class);
                        //创建添加对象
                        _NewReply.ChildCommentBean childCommentBean = new _NewReply.ChildCommentBean(returnContent.getId(), returnContent.getUid(), returnContent.getUser_type(), returnContent.getContent(), returnContent.getAdd_time(), returnContent.getNick(), returnContent.getIcon(), "");
                        //将对象添加在第一个位置
                        childCommentBeanList.add(0, childCommentBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //数据添加之后将列表刷新，将键盘收起
                                mNewReplyAdapter.notifyDataSetChanged();
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(newReplyRevert.getWindowToken(), 0);
                                newReplyRevert.setText("");
                                newReplyRevert.setHint("写评论...");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //回复用户
    private void HttpReplyComment(String comment, String childCommentId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("dynamic_id", mNewReply.getComment().getDynamic_id());
        param.put("parent_id", comment_id);
        param.put("commented_uid", childCommentId);
        param.put("is_virtual_user", childCommentBeanList.get(mPosition - 1).getUser_type());
        param.put("content", comment);
        param.put("type", "2");
        HttpUtils.doPost(UrlConstans.REPLY_COMMENT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=============" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "数据链接成功=============" + json);
                //获取数据成功 将返回的数据 生成数据
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功
                        JSONObject data = jsonObject.getJSONObject("data");
                        //将数据转换成对象
                        String id = data.getString("id");
                        String uid = data.getString("uid");
                        String user_type = data.getString("user_type");
                        String content = data.getString("content");
                        String add_time = data.getString("add_time");
                        String nick = data.getString("nick");
                        String icon = data.getString("icon");
                        String c_nick = data.getString("c_nick");
                        _NewReply.ChildCommentBean childCommentBean = new _NewReply.ChildCommentBean(id, uid, user_type, content, add_time, nick, icon, c_nick);
                        childCommentBeanList.add(0, childCommentBean);
                        //将map中存的当前数据移除
                        mCommentMap.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //将键盘收起 并设置提示
                                mNewReplyAdapter.notifyDataSetChanged();
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(newReplyRevert.getWindowToken(), 0);
                                newReplyRevert.setText("");
                                newReplyRevert.setHint("写评论...");
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
