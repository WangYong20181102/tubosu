package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.MyFragmentPagerStateAdapter;
import com.tbs.tobosutype.adapter.PopMoreStyleAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean._ArticleType;
import com.tbs.tobosutype.fragment.ArticleTypeFragment;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.SpUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 学装修页面  3.7版本新增
 * 包含发单动画 底部发单按钮
 * create by lin
 */
public class LearnRenovationActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.learn_rovat_back_ll)
    LinearLayout learnRovatBackLl;//返回按钮
    @BindView(R.id.learn_rovat_tab_layout)
    XTabLayout learnRovatTabLayout;//顶部的滑动tab
    @BindView(R.id.learn_rovat_more_rl)
    RelativeLayout learnRovatMoreRl;//更多选项
    @BindView(R.id.learn_rovat_view_pager)
    ViewPager learnRovatViewPager;//滑动页面
    @BindView(R.id.learn_rovat_fadan_rl)
    RelativeLayout learnRovatFadanRl;//底部发单地址
    @BindView(R.id.learn_rovat_right_anim)
    ImageView learnRovatRightAnim;//右侧的动画
    @BindView(R.id.learn_rovat_more_iv)
    ImageView learnRovatMoreIv;
    @BindView(R.id.learn_rovat_up_divide)
    View learnRovatUpDivide;
    @BindView(R.id.learn_rovat_mengceng)
    View learnRovatMengceng;

    private Context mContext;
    private String TAG = "LearnRenovationActivity";
    private Gson mGson;//解析数据
    private Intent mIntent;
    private String mIndex;
    //分类数据的Json
    private String mArticleTypeJson;
    //分类的数据集合
    private ArrayList<_ArticleType> mArticleTypeArrayList = new ArrayList<>();
    //fragment 数据集合
    private ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();
    //viewPage适配器
    private MyFragmentPagerStateAdapter myFragmentPagerStateAdapter;
    //展示更多的菜单pop页面
    private PopupWindow mMoreStylePopWindow;
    private View mMorePopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_renovation);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    //初始化 页面相关的事务
    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mIndex = mIntent.getStringExtra("mIndex");
        //设置tab长度
        //加载右侧的动画
        showRightGifAnim();
        //获取存储的分类数据
        mArticleTypeJson = SpUtil.getArticleType(mContext);
        initTabAndViewPager();
        if (!TextUtils.isEmpty(mIndex)) {
            learnRovatViewPager.setCurrentItem(Integer.parseInt(mIndex));
        }
    }

    //处理Type
    private void initTabAndViewPager() {

        try {
            //将标签json数据生成集合
            JSONArray jsonArray = new JSONArray(SpUtil.getArticleType(mContext));
            for (int i = 0; i < jsonArray.length(); i++) {
                _ArticleType articleType = mGson.fromJson(jsonArray.get(i).toString(), _ArticleType.class);
                mArticleTypeArrayList.add(articleType);
                //遍历数据
//                Log.e(TAG, "===遍历出来的数据===id===" + articleType.getId() + "===title===" + articleType.getTitle());
            }
            //初始化fragmentList
            for (int i = 0; i < mArticleTypeArrayList.size(); i++) {
                mFragmentArrayList.add(ArticleTypeFragment.newInstance(mArticleTypeArrayList.get(i).getId()));
//                Log.e(TAG, "=====实例化Fragment时传的参数====" + mArticleTypeArrayList.get(i).getId());
            }
            //设置适配器
            myFragmentPagerStateAdapter = new MyFragmentPagerStateAdapter(getSupportFragmentManager(), mFragmentArrayList);
            learnRovatViewPager.setAdapter(myFragmentPagerStateAdapter);
            learnRovatTabLayout.setupWithViewPager(learnRovatViewPager);
            learnRovatViewPager.setOffscreenPageLimit(mArticleTypeArrayList.size());

            myFragmentPagerStateAdapter.notifyDataSetChanged();
            for (int i = 0; i < mArticleTypeArrayList.size(); i++) {
                learnRovatTabLayout.getTabAt(i).setText(mArticleTypeArrayList.get(i).getTitle());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //加载gif动画
    private void showRightGifAnim() {
        learnRovatRightAnim.setImageResource(R.drawable.anim_learn_renovation_right);
        AnimationDrawable animationDrawable = (AnimationDrawable) learnRovatRightAnim.getDrawable();
        animationDrawable.start();
    }

    @OnClick({R.id.learn_rovat_back_ll, R.id.learn_rovat_more_rl, R.id.learn_rovat_fadan_rl, R.id.learn_rovat_right_anim})
    public void onViewClickedInLearnRenovation(View view) {
        switch (view.getId()) {
            case R.id.learn_rovat_back_ll:
                finish();
                break;
            case R.id.learn_rovat_more_rl:
                //弹出更多的选项卡
                showCaidan();
                break;
            case R.id.learn_rovat_fadan_rl:
                //底部的发单页
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", SpUtil.getTbsAj29(mContext));
                mContext.startActivity(intent);
                break;
            case R.id.learn_rovat_right_anim:
                //点击右侧的动画进行发单
                Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj30(mContext));
                startActivity(intent1);
                break;
        }
    }

    //弹出菜单栏
    private void showCaidan() {
        learnRovatMengceng.setVisibility(View.VISIBLE);
        mMorePopView = View.inflate(mContext, R.layout.pop_more_style, null);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        RelativeLayout pop_more_style_rl = mMorePopView.findViewById(R.id.pop_more_style_rl);
        RecyclerView pop_more_style_recyclerview = mMorePopView.findViewById(R.id.pop_more_style_recyclerview);
        RelativeLayout pop_more_style_close_ll = mMorePopView.findViewById(R.id.pop_more_style_close_ll);
        final ImageView pop_more_style_close_iv = mMorePopView.findViewById(R.id.pop_more_style_close_iv);
        final ImageView pop_more_style_close_iv_02 = mMorePopView.findViewById(R.id.pop_more_style_close_iv_02);
        //开始动画
        Animation rotateAnimation = new RotateAnimation(0f, 45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(250);
        rotateAnimation.setStartOffset(1);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pop_more_style_close_iv.setVisibility(View.GONE);
                pop_more_style_close_iv_02.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pop_more_style_close_iv.startAnimation(rotateAnimation);
        //设置动画结束 按钮消失显示另一个按钮
        //设置背景色
        pop_more_style_rl.setBackgroundColor(Color.parseColor("#ffffff"));
        //初始化数据
        PopMoreStyleAdapter popMoreStyleAdapter = new PopMoreStyleAdapter(mContext, mArticleTypeArrayList);
        pop_more_style_recyclerview.setLayoutManager(mGridLayoutManager);
        pop_more_style_recyclerview.setAdapter(popMoreStyleAdapter);

        popMoreStyleAdapter.setOnPopMoreStyleOnClickLister(new PopMoreStyleAdapter.OnPopMoreStyleOnClickLister() {
            @Override
            public void onItemClick(View view, int position) {
                learnRovatViewPager.setCurrentItem(position);
                mMoreStylePopWindow.dismiss();
            }
        });
        popMoreStyleAdapter.notifyDataSetChanged();
        mMoreStylePopWindow = new PopupWindow(mMorePopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMoreStylePopWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f3f2")));
        mMoreStylePopWindow.setFocusable(true);
        mMoreStylePopWindow.setOutsideTouchable(true);
        mMoreStylePopWindow.update();
        pop_more_style_close_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载动画
//                pop_more_style_close_iv.setVisibility(View.GONE);
//                Animation rotateAnimation = new RotateAnimation(0f, -45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                rotateAnimation.setDuration(250);
//                rotateAnimation.setFillAfter(true);
//                rotateAnimation.setAnimationListener(animationListener);
//                pop_more_style_close_iv_02.startAnimation(rotateAnimation);
                mMoreStylePopWindow.dismiss();
            }
        });
        mMoreStylePopWindow.showAsDropDown(learnRovatUpDivide);
        mMoreStylePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                learnRovatMengceng.setVisibility(View.GONE);
            }
        });
//        mMoreStylePopWindow.showAtLocation(learnRovatUpDivide, Gravity.CENTER, 0, 0);
    }

    //动画结束监听事件
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mMoreStylePopWindow.dismiss();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
