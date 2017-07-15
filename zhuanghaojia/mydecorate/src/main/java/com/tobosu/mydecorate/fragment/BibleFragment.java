package com.tobosu.mydecorate.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.ChannelActivity;
import com.tobosu.mydecorate.activity.PopOrderActivity;
import com.tobosu.mydecorate.activity.SearchActivity;
import com.tobosu.mydecorate.adapter.NewsFragmentPagerAdapter;
import com.tobosu.mydecorate.entity.DecorateTitleEntity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.BaseTools;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CategoryTabBar;
import com.tobosu.mydecorate.view.ColumnHorizontalScrollView;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/** 装修宝典 页面
 * Created by dec on 2016/9/12.
 */
public class BibleFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = BibleFragment.class.getSimpleName();
    private Context context;
    private TextView tvGoSearch;
    private ImageView ivMoreChannel;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private LinearLayout mRadioGroup_content;

    /**顶部tab名称*/
    private ArrayList<String> tabTextList = new ArrayList<String>();

    /**顶部tab的id*/
    private ArrayList<Integer> tabIdList = new ArrayList<Integer>();

    /**顶部tab数据源*/
    private ArrayList<HashMap<String, String>> topTitleHashMapList = new ArrayList<HashMap<String, String>>();

    private View rootView;
    private ViewPager childViewPager;


    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;
    /** 当前选中的栏目*/
    private int columnSelectIndex = 0;
    /** 左阴影部分*/
    public ImageView shade_left;
    /** 右阴影部分 */
    public ImageView shade_right;

    private LinearLayout ll_more_columns;
    private RelativeLayout rl_column;

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

//    private ArrayList<DecorateTitleEntity.TitleBean> userChannelList = new ArrayList<DecorateTitleEntity.TitleBean>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();


        if(rootView==null){
            rootView = inflater.inflate(R.layout.fragment_bible, null);
        }

//        缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if(parent!=null){
            parent.removeView(rootView);
        }
        initView(rootView);
        initData();
        return rootView;
    }


    private void getTopTitle(){
//        if(){
//
//        }else{
//
//        }
        // FIXME 这里需要判断是否登录，是否已经修改过title


        if(Util.isNetAvailable(context)){
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> titleParam = new HashMap<String, Object>();
            titleParam.put("token", Util.getDateToken());
            okHttpUtil.post(Constant.COMMON_ARTICLE_LIST_TOP_TITLE_URL, titleParam, new OKHttpUtil.BaseCallBack() {
                @Override
                public void onSuccess(Response response, String json) {
                    Util.setLog(TAG, json);
                    Gson gson = new Gson();
                    DecorateTitleEntity titleEntity = gson.fromJson(json, DecorateTitleEntity.class);
                    if(titleEntity.getStatus()==200){
                        int size = titleEntity.getData().size();
                        HashMap<String, String> hashMap = null;
                        if(size>0){
                            for(int i =0; i<size; i++){
                                hashMap = new HashMap<String, String>();
                                hashMap.put("name", titleEntity.getData().get(i).getName());
                                hashMap.put("id", titleEntity.getData().get(i).getId()+"");
                                topTitleHashMapList.add(hashMap);
                            }

                        }
                    }
                }

                @Override
                public void onFail(Request request, IOException e) {
                    // 网络不好 重试！
                }

                @Override
                public void onError(Response response, int code) {
                    // 网络不好 重试！
                }
            });
        }

    }

    private ArrayList<ChildFragment> fragmentData = new ArrayList<ChildFragment>();

    private void initView(View view) {
        mScreenWidth = BaseTools.getWindowsWidth(getActivity());
        mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7


        tvGoSearch = (TextView) view.findViewById(R.id.tv_go_search);
        tvGoSearch.setOnClickListener(this);
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view.findViewById(R.id.mColumnHorizontalScrollView);
        ivMoreChannel = (ImageView) view.findViewById(R.id.iv_more_columns);
        ivMoreChannel.setOnClickListener(this);
        mRadioGroup_content = (LinearLayout) view.findViewById(R.id.mRadioGroup_content);
        shade_left = (ImageView) view.findViewById(R.id.shade_left);
        shade_right = (ImageView) view.findViewById(R.id.shade_right);
        ll_more_columns = (LinearLayout) view.findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);

        childViewPager = (ViewPager) view.findViewById(R.id.mViewPager);


        setChangelView();
    }

    /**
     *  当栏目项发生变化时候调用
     * */
    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }

    /** 获取Column栏目 数据*/
    private void initColumnData() {
        // 装修宝典获取标题
        getTopTitle();
    }

    /**
     *  初始化Column栏目项
     * */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count =  topTitleHashMapList.size();
        mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
            TextView columnTextView = new TextView(getActivity());
            columnTextView.setTextAppearance(getActivity(), R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(topTitleHashMapList.get(i).get("name"));
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if(columnSelectIndex == i){
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            childViewPager.setCurrentItem(i);
                        }
                    }
                    Util.setToast(getActivity(),topTitleHashMapList.get(v.getId()).get("name"));
                }
            });
            mRadioGroup_content.addView(columnTextView, i ,params);
        }
    }


    /**
     *  初始化Fragment
     * */
    private void initFragment() {
        fragments.clear();//清空
        int count =  topTitleHashMapList.size();
        for(int i = 0; i< count;i++){
            Bundle data = new Bundle();
            data.putString("text", topTitleHashMapList.get(i).get("name"));
            data.putInt("id", Integer.parseInt(topTitleHashMapList.get(i).get("id")));
            NewsFragment newfragment = new NewsFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
//		mViewPager.setOffscreenPageLimit(0);
        childViewPager.setAdapter(mAdapetr);
        childViewPager.setOnPageChangeListener(pageListener);
    }

    /**
     *  ViewPager切换监听方法
     * */
    public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            childViewPager.setCurrentItem(position);
            selectTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     *  选择的Column里面的Tab
     * */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        //判断是否选中
        for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }


    /**
     * 初始化tab标题
     */
    private void initTabText() {

//        DBManager manager = DBManager.getInstance(getActivity());
//        tabTextData = manager.queryTypeData();
//        for(int i=0, len=tabTextData.size(); i<len;i++){
//            tabTextList.add(tabTextData.get(i).get("title"));
//        }

        for(int i=0;i<topTitleHashMapList.size();i++){
            tabTextList.add(topTitleHashMapList.get(i).get("name"));
            tabIdList.add(Integer.parseInt(topTitleHashMapList.get(i).get("id")));
        }

    }

    private void initData(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_more_columns:
                Intent it = new Intent(getActivity(), ChannelActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("tab_text_list", tabTextList);
                it.putExtra("home_tabtext_bundle", b);
                startActivityForResult(it, 1026);
                break;
            case R.id.tv_go_search:
                startActivity(new Intent(context, SearchActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 102:

                break;

        }

    }



}
