package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.PhotoFragmentPagerAdapter;
import com.tbs.tobosupicture.adapter.ShowCommentAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._DynamicDetail;
import com.tbs.tobosupicture.bean._PhotoDetail;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.fragment.PhotoDetailFragment;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
 * 动态点击图片进入该页面
 */
public class PhotoDetail extends BaseActivity {

    @BindView(R.id.photo_detail_back)
    LinearLayout photoDetailBack;
    @BindView(R.id.photo_detail_viewpager)
    ViewPager photoDetailViewpager;
    @BindView(R.id.photo_detail_revert)
    EditText photoDetailRevert;
    @BindView(R.id.photo_detail_ll_pinlun)
    LinearLayout photoDetailLlPinlun;
    @BindView(R.id.photo_detail_pinlun_num)
    TextView photoDetailPinlunNum;
    @BindView(R.id.photo_detail_pinlun)
    RelativeLayout photoDetailPinlun;
    @BindView(R.id.photo_detail_shoucang)
    ImageView photoDetailShoucang;
    @BindView(R.id.photo_detail_xiazai)
    ImageView photoDetailXiazai;
    @BindView(R.id.photo_detail_send)
    TextView photoDetailSend;
    @BindView(R.id.photo_detail_rl_gongneng)
    RelativeLayout photoDetailRlGongneng;
    @BindView(R.id.photo_detail_shoucang_big)
    ImageView photoDetailShoucangBig;


    private Context mContext;
    private String TAG = "PhotoDetail";
    private Intent mIntent;
    private Gson mGson;
    private _PhotoDetail mPhotoDetail;
    //伪循环使用的数据集
    private List<_PhotoDetail.ImageDetail> tempImageDetailList = new ArrayList<>();
    //请求回来的数据集
    private List<_PhotoDetail.ImageDetail> mImageDetailList = new ArrayList<>();
    private PhotoFragmentPagerAdapter mPhotoFragmentPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    //TODO 图片选择的位置mImagePosition和动态的id mDynamicId 从上一个界面传来 用于定位具体的位置显示图片默认从0开始
    private int mImagePosition;
    private String mDynamicId;//动态的id

    //TODO 显示popwindow 所要用的相关数据
    private View popView;//popwindow的载体
    private ShowCommentAdapter showCommentAdapter;
    private boolean isLoading = false;//是否正在加载
    private RecyclerView pop_detail_pinglun_recycle;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout pop_show_comment_back;
    private PopupWindow popupWindow;//窗口
    private int mPage = 1;//显示评论的页数
    private List<_DynamicDetail.Comment> commentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
        HttpGetImageDetail();

    }

    private void initEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mImagePosition = mIntent.getIntExtra("mImagePosition", 0);
        mDynamicId = mIntent.getStringExtra("mDynamicId");
        photoDetailRevert.addTextChangedListener(textWatcher);
    }

    //输入框的事件监听
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                //有文本键入 显示发送按钮 隐藏三个功能按键
                photoDetailSend.setVisibility(View.VISIBLE);
                photoDetailRlGongneng.setVisibility(View.GONE);
            } else {
                photoDetailSend.setVisibility(View.GONE);
                photoDetailRlGongneng.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick({R.id.photo_detail_back, R.id.photo_detail_revert, R.id.photo_detail_ll_pinlun,
            R.id.photo_detail_pinlun, R.id.photo_detail_shoucang, R.id.photo_detail_xiazai, R.id.photo_detail_send})
    public void onViewClickedInPhotoDetail(View view) {
        switch (view.getId()) {
            case R.id.photo_detail_back:
                finish();
                break;
            case R.id.photo_detail_revert:
                //TODO 文本输入框 点击事件  判断用户是否登录
                if (!Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }

                break;
            case R.id.photo_detail_ll_pinlun:
                //评论填写
                if (!Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.photo_detail_send:
                //发送评论
                if (Utils.userIsLogin(mContext)) {
                    //用户登录的情况下 才可以评论
                    if (TextUtils.isEmpty(photoDetailRevert.getText().toString())) {
                        Toast.makeText(mContext, "请输入评论内容", Toast.LENGTH_SHORT).show();
                    } else {
                        HttpSendPinglun(photoDetailRevert.getText().toString());
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }

                break;
            case R.id.photo_detail_pinlun:
                //点击滑出评论的popwindow
                mPage = 1;
                if (!commentList.isEmpty()) {
                    commentList.clear();
                }
                HttpGetCommentList(mPage);
                break;
            case R.id.photo_detail_shoucang:
                if (Utils.userIsLogin(mContext)) {
                    HttpShoucan();
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.photo_detail_xiazai:
                //进行图片的下载
                int position = photoDetailViewpager.getCurrentItem();
                Log.e(TAG, "获取当前的子项Url===" + tempImageDetailList.get(position).getImage_url());
                HttpDownLoadImg(tempImageDetailList.get(position).getImage_url());
                break;
        }
    }

    //初始化pop
    private void initPinglunPop() {
        mPage = 1;
        popView = getLayoutInflater().inflate(R.layout.pop_show_detail_pinglun, null);
        pop_detail_pinglun_recycle = (RecyclerView) popView.findViewById(R.id.pop_detail_pinglun_recycle);
        pop_show_comment_back = (LinearLayout) popView.findViewById(R.id.pop_detail_pinglun_back);

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        pop_detail_pinglun_recycle.setLayoutManager(linearLayoutManager);
        pop_detail_pinglun_recycle.addOnScrollListener(onScrollListener);
        int height = getResources().getDisplayMetrics().heightPixels * 6 / 8;
        ;
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, height);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f3f2")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        pop_show_comment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    //显示pop
    private void showPopWindow() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 30);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
            if (newState == 0 && lastVisiableItem + 5 > linearLayoutManager.getItemCount() && !isLoading) {
                LoadMoreZanUser();
            }
        }
    };

    private void LoadMoreZanUser() {
        mPage++;
        HttpGetCommentList(mPage);
    }

    //TODO 发送评论 暂时传入uid=23109
    private void HttpSendPinglun(String content) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("user_type", "2");
        param.put("dynamic_id", mDynamicId);
        param.put("commented_uid", mPhotoDetail.getUid());
        param.put("is_virtual_user", mPhotoDetail.getUser_type());
        param.put("content", content);
        HttpUtils.doPost(UrlConstans.USER_SEND_COMMENT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "评论成功！", Toast.LENGTH_SHORT).show();
                                photoDetailRevert.setText("");
                                //进行评论数的变化
                                int num = Integer.parseInt(photoDetailPinlunNum.getText().toString());
                                int Nownum = num + 1;
                                photoDetailPinlunNum.setText("" + Nownum);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "评论失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //TODO 获取评论列表  这个接口还得传uid   用于判断用户是否收藏该动态
    private void HttpGetCommentList(int page) {
        isLoading = true;
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("dynamic_id", mDynamicId);
        param.put("page", page);
        param.put("page_size", "10");
        HttpUtils.doPost(UrlConstans.DYNAMIC_COMMETN_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isLoading = false;
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                isLoading = false;
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            _DynamicDetail.Comment comment = mGson.fromJson(jsonArray.get(i).toString(), _DynamicDetail.Comment.class);
                            commentList.add(comment);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (showCommentAdapter == null) {
                                    showCommentAdapter = new ShowCommentAdapter(mContext, PhotoDetail.this, mDynamicId, commentList);
                                    pop_detail_pinglun_recycle.setAdapter(showCommentAdapter);
                                    showCommentAdapter.notifyDataSetChanged();
                                } else {
                                    showCommentAdapter.notifyDataSetChanged();
                                }
                                showPopWindow();
                            }
                        });
                    } else if (status.equals("201")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "暂无更多评论数据~", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    isLoading = false;
                    e.printStackTrace();
                }
            }
        });
    }

    //用户收藏该图集 要进行登录判断
    private void HttpShoucan() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        if(Utils.userIsLogin(mContext)){
            param.put("uid",SpUtils.getUserUid(mContext));
        }
        param.put("dynamic_id", mDynamicId);
        HttpUtils.doPost(UrlConstans.DYNAMIC_COLLECT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.equals("收藏成功")) {
                                    photoDetailShoucang.setImageResource(R.mipmap.shoucang2);
                                    Toast.makeText(mContext, "收藏成功！", Toast.LENGTH_SHORT).show();
                                    getConcren(photoDetailShoucangBig);
                                } else {
                                    photoDetailShoucang.setImageResource(R.mipmap.shoucang3);
                                    Toast.makeText(mContext, "取消收藏成功！", Toast.LENGTH_SHORT).show();
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


    //用户在登录的
    private void HttpGetImageDetail() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("id", mDynamicId);
        if (Utils.userIsLogin(mContext)) {
            param.put("uid", SpUtils.getUserUid(mContext));
        }
        HttpUtils.doPost(UrlConstans.VIEW_DYNAMIC_IMG, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        mPhotoDetail = mGson.fromJson(data, _PhotoDetail.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //正式场景使用的数据集
                                mImageDetailList.addAll(mPhotoDetail.getImage_detail());

                                //*************** 制作伪循环  将数据集变大100倍  仅作为显示使用  ↓↓
                                for (int j = 0; j < 100; j++) {
                                    tempImageDetailList.addAll(mPhotoDetail.getImage_detail());
                                }
                                for (int i = 0; i < tempImageDetailList.size(); i++) {
                                    fragmentList.add(PhotoDetailFragment.newInstance(tempImageDetailList.get(i)));
                                }
                                mPhotoFragmentPagerAdapter = new PhotoFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);

                                photoDetailViewpager.setAdapter(mPhotoFragmentPagerAdapter);
                                //设置显示的位置  从列表的正中间开始显示
                                photoDetailViewpager.setCurrentItem((50 * mImageDetailList.size() + mImagePosition));
                                //*************** 制作伪循环  将数据集变大100倍  仅作为显示使用  ↑ ↑

                                //设置评论数
                                photoDetailPinlunNum.setText("" + mPhotoDetail.getComment_count());
                                //判断用户是否收藏
                                if (mPhotoDetail.getIs_collect().equals("1")) {
                                    photoDetailShoucang.setImageResource(R.mipmap.shoucang2);
                                } else {
                                    photoDetailShoucang.setImageResource(R.mipmap.shoucang3);
                                }
                            }
                        });
                        initPinglunPop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "解析数据出错====" + e.toString());
                }
            }
        });
    }

    //TODO 下载图片
    private void HttpDownLoadImg(String downloadUrl) {
        //创建文件夹
        File dirFile = new File(UrlConstans.IMG_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        HttpUtils.downFile(downloadUrl, dirFile.getPath(), fileName);
        if (Utils.isNetAvailable(mContext)) {
            Toast.makeText(mContext, "图片下载成功!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "图片下载失败！", Toast.LENGTH_SHORT).show();
        }
    }

    //显示收藏动画
    private void getConcren(final View view) {
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.clearAnimation();
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(animation);
    }
}
