package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorationQuestionFragmentAdapter;
import com.tbs.tobosutype.adapter.DecorationQuestionViewPagerAdapter;
import com.tbs.tobosutype.adapter.QuestionGridViewAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.AskQuestionBean;
import com.tbs.tobosutype.bean.QuestionTypeListBean;
import com.tbs.tobosutype.customview.OnlyPointIndicator;
import com.tbs.tobosutype.fragment.DecorationQuestionFragment;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

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
 * Created by Mr.Wang on 2018/11/5 10:34.
 */
public class DecorationQuestionActivity extends BaseActivity implements ViewPager.OnPageChangeListener, TextWatcher {

    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;
    @BindView(R.id.rl_top_serach)
    RelativeLayout rlTopSerach;
    @BindView(R.id.relBackShoucang)
    RelativeLayout relBackShoucang;     //返回按钮父布局
    @BindView(R.id.ll_image_back)
    LinearLayout llImageBack;     //返回按钮父布局
    @BindView(R.id.searche_bar)
    RelativeLayout searcheBar;
    @BindView(R.id.image_top_search)
    ImageView imageTopSearch;          //搜索按钮
    @BindView(R.id.image_quesition)
    ImageView imageQuesition;          //提问
    @BindView(R.id.ivGongSiDelete)
    ImageView ivGongSiDelete;       //文本框删除按钮
    @BindView(R.id.kiksezchdfa)
    ImageView kiksezchdfa;
    @BindView(R.id.tvCancelSearch)
    TextView tvCancelSearch;    //取消
    @BindView(R.id.searchSwip)
    SwipeRefreshLayout searchSwip;  //搜索栏刷新
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
    RecyclerView searchList;    //搜索栏recycleView
    @BindView(R.id.rl_no_result)
    RelativeLayout rlNoResult;    //搜索栏暂无结果
    @BindView(R.id.dq_mid_rl)
    RelativeLayout dqMidRl;
    @BindView(R.id.etSearchGongsi)
    EditText etSearchGongsi;    //搜索输入框
    @BindView(R.id.mengceng4)
    View mengceng4;
    /**
     * 搜索状态
     */
    private boolean bSearchState = false;

    /**
     * 问答首页viewpager适配器
     */
    private DecorationQuestionViewPagerAdapter decorationQuestionViewPagerAdapter;

    /**
     * viewpager滑动fragment集合
     */
    private List<DecorationQuestionFragment> fragmentList;
    /**
     * 下拉type数据集
     */
    private List<QuestionTypeListBean> questionTypeListBeanList;
    private List<AskQuestionBean> askQuestionBeanList;
    /**
     * Gson解析
     */
    private Gson gson;
    /**
     * 下拉选框当前选中位置
     */
    private int currentSelectedPosition = 0;
    /**
     * 下拉选框pop视图
     */
    private View dropDownPopupWindow;
    /**
     * 下拉九宫格弹窗
     */
    private PopupWindow popupWindow;
    /**
     * 九宫格控件
     */
    private GridView mGridView;
    /**
     * 下拉刷新状态
     */
    private boolean isDownRefresh = false;
    private LinearLayoutManager layoutManager;
    private int mPage = 1;//用于分页的数据
    private int mPageSearch = 1;//搜索分页的数据
    private int mPageSize = 15;
    private String keyword = "";   //搜索关键字

    private DecorationQuestionFragment questionFragment = null;
    private DecorationQuestionFragmentAdapter fragmentAdapter = null;   //用于搜索适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decorationquest_layout);
        ButterKnife.bind(this);
        gson = new Gson();

        initData();
        initViewEvent();
        initSerachViewData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        fragmentList = new ArrayList<>();
        questionTypeListBeanList = new ArrayList<>();
        askQuestionBeanList = new ArrayList<>();
    }

    /**
     * 搜索栏初始化
     */
    private void initSerachViewData() {

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchList.setLayoutManager(layoutManager);
        searchSwip.setOnRefreshListener(onRefreshListener);
        searchList.setOnScrollListener(onScrollListener);
        searchSwip.setEnabled(false);
        etSearchGongsi.addTextChangedListener(this);
    }

    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (fragmentAdapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastVisibleItemPosition() + 1 == fragmentAdapter.getItemCount()) {
                    LoadMore();
                }
            }
        }
    };

    //加载更多数据
    private void LoadMore() {
        mPageSearch++;
        HttpPostSearchResult(mPageSearch);
    }

    //下拉刷新监听
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            searchSwip.setRefreshing(false);
        }
    };

    //初始化
    private void initViewEvent() {
        initHttpRequest();
        currentSelectedPosition = 0;

        dropDownPopupWindow = LayoutInflater.from(this).inflate(R.layout.pop_window_layout, null);
        mGridView = dropDownPopupWindow.findViewById(R.id.pop_window_show);
        dqMid.setBackgroundColor(Color.WHITE);

        dqViewPager.setOnPageChangeListener(this);


//        文本输入框加入监听事件
        etSearchGongsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    tvCancelSearch.setText("搜索");
                } else {
                    tvCancelSearch.setText("取消");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * 网络请求(初始化加载类别)
     */
    private void initHttpRequest() {
        mPage = 1;
        HttpGetImageList(mPage);
    }

    @OnClick({R.id.image_top_search, R.id.relBackShoucang, R.id.tvCancelSearch, R.id.rl_more, R.id.image_quesition, R.id.ll_image_back, R.id.ivGongSiDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_top_search: //搜索按钮
                bSearchState = true;
                searchLayout.setVisibility(View.VISIBLE);
                rlTopSerach.setVisibility(View.GONE);
                showSearchView();

                break;
            case R.id.tvCancelSearch:       //取消按钮
                if (tvCancelSearch.getText().toString().trim().equals("搜索")) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DecorationQuestionActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mengceng4.setVisibility(View.GONE);
                    searchLayout.setBackgroundResource(R.drawable.wht);
                    showSearchResultView();
                    System.gc();
                } else {
                    bSearchState = false;
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DecorationQuestionActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    rlTopSerach.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.GONE);
                    System.gc();
                }
                tvCancelSearch.setText("取消");
                break;
            case R.id.image_quesition:  //提问
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
                    Toast.makeText(mContext, "您还没有登陆", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, NewLoginActivity.class);
                    startActivityForResult(intent, 0);
                    return;
                }
                Intent intent1 = new Intent(this, AskQuestionActivity.class);
                intent1.putExtra("type", 1); //从问答首页进入提问
                startActivity(intent1);
                break;
            case R.id.relBackShoucang:  //返回
                finish();
                break;
            case R.id.ll_image_back:    //搜索返回
                bSearchState = false;
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DecorationQuestionActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                rlTopSerach.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                System.gc();
                break;
            case R.id.rl_more:  //下拉展开箭头
                showPopSelect();
                break;
            case R.id.ivGongSiDelete://输入框清除文字按钮
                etSearchGongsi.setText("");
                if (fragmentAdapter != null) {
                    fragmentAdapter = null;
                    searchList.setAdapter(null);
                }
                break;
        }
    }

    //显示搜索结果
    private void showSearchResultView() {
        mPageSearch = 1;
        isDownRefresh = true;
        if (fragmentAdapter != null) {
            fragmentAdapter = null;
            searchList.setAdapter(null);
        }
        if (!askQuestionBeanList.isEmpty()) {
            askQuestionBeanList.clear();
        }

        HttpPostSearchResult(mPageSearch);
    }

    /**
     * 搜索页网络请求
     *
     * @param mPageSearch
     */
    private void HttpPostSearchResult(final int mPageSearch) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("keyword", keyword);
        params.put("system_plat", 1);
        params.put("page", mPageSearch);
        params.put("pagesize", mPageSize);
        OKHttpUtil.post(Constant.ASK_QUESTION_SEARCH, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isDownRefresh = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = data.optJSONArray("searchInfo");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AskQuestionBean askQuestionBean = gson.fromJson(jsonArray.get(i).toString(), AskQuestionBean.class);
                            askQuestionBeanList.add(askQuestionBean);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (fragmentAdapter == null) {
                                    rlNoResult.setVisibility(View.GONE);
                                    searchList.setVisibility(View.VISIBLE);
                                    fragmentAdapter = new DecorationQuestionFragmentAdapter(DecorationQuestionActivity.this, askQuestionBeanList);
                                    searchList.setAdapter(fragmentAdapter);
                                }
                                if (isDownRefresh) {
                                    isDownRefresh = false;
                                    fragmentAdapter.notifyDataSetChanged();
                                } else {
                                    fragmentAdapter.notifyItemInserted(askQuestionBeanList.size() - mPageSize);
                                }


                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rlNoResult.setVisibility(View.VISIBLE);
                                searchList.setVisibility(View.GONE);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 网络请求
     *
     * @param mPage
     */
    private void HttpGetImageList(int mPage) {

        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("system_plat", "1");
        param.put("page", mPage);
        param.put("pagesize", mPageSize);
        OKHttpUtil.post(Constant.ASK_QUESTION_HOME, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        questionTypeListBeanList = gson.fromJson(data.getString("category"), new TypeToken<List<QuestionTypeListBean>>() {
                        }.getType());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                CommonNavigator commonNavigator = new CommonNavigator(DecorationQuestionActivity.this);
                                commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                                    @Override
                                    public int getCount() {
                                        return questionTypeListBeanList.size();
                                    }

                                    @Override
                                    public IPagerTitleView getTitleView(Context context, final int i) {
                                        SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                                        simplePagerTitleView.setText(questionTypeListBeanList.get(i).getCategory_name());
                                        simplePagerTitleView.setTextSize(14);
                                        simplePagerTitleView.setNormalColor(Color.parseColor("#363650"));
                                        simplePagerTitleView.setSelectedColor(Color.parseColor("#ff6b14"));
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
                                        indicator.setColors(Color.parseColor("#ff6b14"));
                                        return indicator;
                                    }
                                });

                                dqMid.setNavigator(commonNavigator);
                                ViewPagerHelper.bind(dqMid, dqViewPager);

                                for (int i = 0; i < questionTypeListBeanList.size(); i++) {
                                    questionFragment = DecorationQuestionFragment.newInstance(questionTypeListBeanList.get(i).getId());
                                    fragmentList.add(questionFragment);
                                }

                                if (decorationQuestionViewPagerAdapter == null) {
                                    decorationQuestionViewPagerAdapter = new DecorationQuestionViewPagerAdapter(getSupportFragmentManager(), DecorationQuestionActivity.this, fragmentList);
                                    dqViewPager.setAdapter(decorationQuestionViewPagerAdapter);
                                    dqViewPager.setOffscreenPageLimit(0);
                                    decorationQuestionViewPagerAdapter.notifyDataSetChanged();
                                } else {
                                    decorationQuestionViewPagerAdapter.notifyDataSetChanged();
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

    /**
     * 搜索界面
     */
    private void showSearchView() {
        searchLayout.setBackgroundResource(R.color.cal_pressed_color_trancspar);
        mengceng4.setVisibility(View.VISIBLE);
        searchList.setVisibility(View.INVISIBLE);
        // 点击 跳转去搜索页面
        etSearchGongsi.setText("");

        etSearchGongsi.setFocusable(true);
        etSearchGongsi.setFocusableInTouchMode(true);
        etSearchGongsi.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        tvCancelSearch.setText("取消");
        ivGongSiDelete.setVisibility(View.GONE);
        nothingData.setVisibility(View.GONE);
        searchLayout.setVisibility(View.VISIBLE);

        System.gc();
    }


    private QuestionGridViewAdapter myGridViewAdapterStyle;//风格

    /**
     * 下拉按钮
     */
    private void showPopSelect() {
        Glide.with(this).load(R.drawable.drop_down_c).into(imageDqMore);

        if (myGridViewAdapterStyle == null) {
            myGridViewAdapterStyle = new QuestionGridViewAdapter(this, questionTypeListBeanList, currentSelectedPosition);
            mGridView.setAdapter(myGridViewAdapterStyle);
        } else {
            myGridViewAdapterStyle.setSelectPosition(currentSelectedPosition);
            myGridViewAdapterStyle.notifyDataSetChanged();
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSelectedPosition = position;
                popupWindow.dismiss();
                dqViewPager.setCurrentItem(position);
            }
        });
        fragShardowView.setVisibility(View.VISIBLE);
        popupWindow = new PopupWindow(dropDownPopupWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f3f2")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAsDropDown(dqMidRl, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentSelectedPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        keyword = s.toString().trim();
        if (!s.toString().trim().isEmpty()) {
            ivGongSiDelete.setVisibility(View.VISIBLE);
        } else {
            ivGongSiDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (bSearchState){
                bSearchState = false;
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DecorationQuestionActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                rlTopSerach.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                System.gc();
            }else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
