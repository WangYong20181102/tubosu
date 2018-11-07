package com.tbs.tobosutype.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorationQuestionViewPagerAdapter;
import com.tbs.tobosutype.adapter.MyGridViewAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._SelectMsg;
import com.tbs.tobosutype.customview.OnlyPointIndicator;
import com.tbs.tobosutype.fragment.DecorationQuestionFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2018/11/5 10:34.
 */
public class DecorationQuestionActivity extends BaseActivity {

    @BindView(R.id.tl_top_title)
    RelativeLayout tlTopTitle;      //标题父布局
    @BindView(R.id.topSearch)
    RelativeLayout topSearch;       //搜索栏
    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;
    @BindView(R.id.rl_top_serach)
    RelativeLayout rlTopSerach;
    @BindView(R.id.relBackShoucang)
    RelativeLayout relBackShoucang;     //返回按钮父布局
    @BindView(R.id.searche_bar)
    RelativeLayout searcheBar;
    @BindView(R.id.image_top_search)
    ImageView imageTopSearch;          //搜索按钮
    @BindView(R.id.ivGongSiDelete)
    ImageView ivGongSiDelete;       //文本框删除按钮
    @BindView(R.id.kiksezchdfa)
    ImageView kiksezchdfa;
    @BindView(R.id.tvCancelSearch)
    TextView tvCancelSearch;    //取消
    @BindView(R.id.dq_swipe)
    SwipeRefreshLayout dqSwipe;     //刷新
    @BindView(R.id.searchSwip)
    SwipeRefreshLayout searchSwip;
    @BindView(R.id.dq_viewpager)
    ViewPager dqViewPager;
    @BindView(R.id.dq_mid)
    MagicIndicator dqMid;
    @BindView(R.id.frag_shardow_view)
    View fragShardowView;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.nothingData)
    RelativeLayout nothingData;
    @BindView(R.id.image_dq_more)
    ImageView imageDqMore;
    @BindView(R.id.searchList)
    RecyclerView searchList;
    @BindView(R.id.dq_mid_rl)
    RelativeLayout dqMidRl;
    @BindView(R.id.etSearchGongsi)
    EditText etSearchGongsi;    //搜索输入框
    @BindView(R.id.mengceng4)
    View mengceng4;

    private DecorationQuestionViewPagerAdapter decorationQuestionViewPagerAdapter;

    private List<Fragment> dqList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private Gson gson;
    private int mQuyuPosition = 0;
    private View quanbuquyuPopView;
    private PopupWindow quanbuquyuPopupWindow;
    private GridView mGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decorationquest_layout);
        ButterKnife.bind(this);
        gson = new Gson();
        initViewEvent();

    }

    //初始化
    private void initViewEvent() {

        mQuyuPosition = 0;
        stringList.add("推荐");
        stringList.add("装修流程");
        stringList.add("装修建材");
        stringList.add("家装设计");
        stringList.add("家居家电");
        stringList.add("工装设计");
        stringList.add("装饰搭配");
        stringList.add("房产知识");
        stringList.add("风水知识");

        for (int i = 0; i < stringList.size(); i++) {
            dqList.add(DecorationQuestionFragment.newInstance());
        }

        quanbuquyuPopView = LayoutInflater.from(this).inflate(R.layout.pop_window_layout, null);
        mGridView = quanbuquyuPopView.findViewById(R.id.pop_window_show);

        decorationQuestionViewPagerAdapter = new DecorationQuestionViewPagerAdapter(getSupportFragmentManager(), this, dqList);
        dqViewPager.setOffscreenPageLimit(dqList.size());
        dqViewPager.setAdapter(decorationQuestionViewPagerAdapter);
        decorationQuestionViewPagerAdapter.notifyDataSetChanged();
        dqViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dqMid.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return stringList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(stringList.get(i));
                simplePagerTitleView.setTextSize(13);
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ff882e"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dqViewPager.setCurrentItem(i);
                    }
                });

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                OnlyPointIndicator indicator = new OnlyPointIndicator(context);
                indicator.setColors(Color.parseColor("#ff882e"));
                return indicator;
            }
        });

        dqMid.setNavigator(commonNavigator);
        ViewPagerHelper.bind(dqMid, dqViewPager);


//        文本输入框加入监听事件
        etSearchGongsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearchGongsi.getText().toString().trim().length() > 0) {
                    tvCancelSearch.setText("搜索");
                } else {
                    tvCancelSearch.setText("取消");
                }
            }
        });

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        super.receiveEvent(event);
    }

    @OnClick({R.id.image_top_search, R.id.tl_top_title, R.id.topSearch, R.id.relBackShoucang, R.id.tvCancelSearch, R.id.rl_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_top_search:
                searchLayout.setVisibility(View.VISIBLE);
                rlTopSerach.setVisibility(View.GONE);
                showSearchView();

                break;
            case R.id.tvCancelSearch:       //取消按钮
                if (tvCancelSearch.getText().toString().trim().equals("搜索"))
                {
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DecorationQuestionActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    mengceng4.setVisibility(View.GONE);
                    searchLayout.setBackgroundResource(R.drawable.wht);
                    System.gc();

                }else {
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DecorationQuestionActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    rlTopSerach.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.GONE);
                    System.gc();
                }
                tvCancelSearch.setText("取消");
                break;
            case R.id.tl_top_title:

                break;
            case R.id.topSearch:

                break;
            case R.id.relBackShoucang:
                finish();
                break;
            case R.id.rl_more:  //下拉展开箭头
                showPopSelect();
                break;
        }
    }

    /*搜索界面*/
    private void showSearchView() {
        searchLayout.setBackgroundResource(R.color.cal_pressed_color_trancspar);
        mengceng4.setVisibility(View.VISIBLE);
        // 点击 跳转去搜索页面
        etSearchGongsi.setText("");

        etSearchGongsi.setFocusable(true);
        etSearchGongsi.setFocusableInTouchMode(true);
        etSearchGongsi.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        isShowingInput = true;

        tvCancelSearch.setText("取消");
        ivGongSiDelete.setVisibility(View.GONE);
        nothingData.setVisibility(View.GONE);
        searchLayout.setVisibility(View.VISIBLE);

//        if (youxuanCompanyAdapter != null) {
//            youxuanSearchGongsiList.clear();
//            youxuanCompanyAdapter.notifyDataSetChanged();
//            youxuanCompanyAdapter = null;
//        }
//
//        if (searchCompanyAdapter != null) {
//            searchGongsiList.clear();
//            searchCompanyAdapter.loadMoreGongsi(false);
//            searchCompanyAdapter.notifyDataSetChanged();
//            searchCompanyAdapter = null;
//
//        }
        System.gc();
    }


    private ArrayList<_SelectMsg> popStyleList = new ArrayList<>();
    private MyGridViewAdapter myGridViewAdapterStyle;//风格

    private void showPopSelect() {
        Glide.with(this).load(R.drawable.drop_down_c).into(imageDqMore);
        popStyleList.clear();
        _SelectMsg districtIdBean = new _SelectMsg();
        districtIdBean.setId("0");
        districtIdBean.setName("推荐");
        _SelectMsg districtIdBean1 = new _SelectMsg();
        districtIdBean1.setId("1");
        districtIdBean1.setName("装修流程");
        _SelectMsg districtIdBean2 = new _SelectMsg();
        districtIdBean2.setId("2");
        districtIdBean2.setName("装修建材");
        _SelectMsg districtIdBean3 = new _SelectMsg();
        districtIdBean3.setId("3");
        districtIdBean3.setName("家装设计");
        _SelectMsg districtIdBean4 = new _SelectMsg();
        districtIdBean4.setId("4");
        districtIdBean4.setName("家具家电");
        popStyleList.add(districtIdBean);
        popStyleList.add(districtIdBean1);
        popStyleList.add(districtIdBean2);
        popStyleList.add(districtIdBean3);
        popStyleList.add(districtIdBean4);
        myGridViewAdapterStyle = new MyGridViewAdapter(this, popStyleList, mQuyuPosition);
        mGridView.setAdapter(myGridViewAdapterStyle);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mQuyuPosition = position;
                quanbuquyuPopupWindow.dismiss();
                dqViewPager.setCurrentItem(position);
            }
        });
        fragShardowView.setVisibility(View.VISIBLE);
        quanbuquyuPopupWindow = new PopupWindow(quanbuquyuPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        quanbuquyuPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f3f2")));
        quanbuquyuPopupWindow.setFocusable(true);
        quanbuquyuPopupWindow.setOutsideTouchable(true);
        quanbuquyuPopupWindow.update();
        quanbuquyuPopupWindow.showAsDropDown(dqMidRl, 0, 0);
        quanbuquyuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                fragShardowView.setVisibility(View.GONE);
                imageDqMore.setImageResource(R.drawable.drop_down_p);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
