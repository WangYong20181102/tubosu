package com.tobosu.mydecorate.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.ChannelActivity;
import com.tobosu.mydecorate.activity.PopOrderActivity;
import com.tobosu.mydecorate.activity.SearchActivity;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.database.ChannelManage;
import com.tobosu.mydecorate.entity.DecorateTitleEntity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CategoryTabBar;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.HashMap;


/** 装修宝典 页面
 * Created by dec on 2016/9/12.
 */
public class DecorateArticleListFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DecorateArticleListFragment.class.getSimpleName();
    private Context context;
    private TextView tvGoSearch;

    /**顶部tab名称*/
    private ArrayList<String> tabTextList = new ArrayList<String>();

    /**顶部tab的id*/
    private ArrayList<Integer> tabIdList = new ArrayList<Integer>();

    /**顶部tab更多*/
    private RelativeLayout rel_more_channel;

    private CategoryTabBar tabs;
    private ViewPager childViewPager;
    private ChildPagerAdapter childAdapter;

    private View rootView;

    private ImageView ivPop;

    private RelativeLayout relNetOut;
    private TextView tvNetout;


    private ChannelManage channelManager;


    /** 本地用户栏目列表 【显示】 */
    private ArrayList<DecorateTitleEntity.ChannelItem> userChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();

    /** 本地用户栏目列表 【不显示】 */
    private ArrayList<DecorateTitleEntity.ChannelItem> userUnShowChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();



    /** 调整返回的RESULTCODE */
    public final static int CHANNELRESULT = 10;
    /** 请求CODE */
    public final static int CHANNELREQUEST = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if(rootView==null){
            rootView = inflater.inflate(R.layout.fragment_decorate_artical_list, null);
        }
//        缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if(parent!=null){
            parent.removeView(rootView);
        }
        initView(rootView);
        // 装修宝典 标题
        initTabTextAndFragment(ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).getUserChannel());

        return rootView;
    }

    private int decorateTitlePosition = 0;
    private void getActivityFragmentData(){
        if(getActivity().getIntent()!=null && getActivity().getIntent().getBundleExtra("switch_channel_bundle")!=null){
            decorateTitlePosition = getActivity().getIntent().getBundleExtra("switch_channel_bundle").getInt("channel_position");
            childViewPager.setCurrentItem(decorateTitlePosition);
        }
    }




    private ArrayList<ChildFragment> fragmentData = new ArrayList<ChildFragment>();

    private void initView(View view) {
        rel_more_channel = (RelativeLayout) view.findViewById(R.id.rel_more_channel);
        rel_more_channel.setOnClickListener(this);

        tabs = (CategoryTabBar) view.findViewById(R.id.category_strip);

        tvGoSearch = (TextView) view.findViewById(R.id.tv_gosearch);
        tvGoSearch.setOnClickListener(this);

        childViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ivPop = (ImageView) view.findViewById(R.id.iv_bible_pop);

        relNetOut = (RelativeLayout) view.findViewById(R.id.child_reload_layout);
        relNetOut.setVisibility(View.GONE);
        tvNetout = (TextView) view.findViewById(R.id.tv_reload);
        tvNetout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent netIntent = null;
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                    netIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                } else {
                    netIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }
                startActivityForResult(netIntent, 0x00019);
            }
        });

        ivPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goPopIntent = new Intent();
                goPopIntent.setClass(getActivity(), PopOrderActivity.class);
                Bundle b = new Bundle();
                b.putInt("gopoporder_code", 134);
                goPopIntent.putExtra("home_go_pop_bundle", b);
                getActivity().startActivityForResult(goPopIntent, Constant.HOMEFRAGMENT_REQUESTCODE);
            }
        });

        channelManager = ChannelManage.getManage(MyApplication.getApp().getSQLHelper());
        userChannelList = channelManager.getUserChannel();
        userUnShowChannelList = channelManager.getOtherChannel();
    }

    /**
     * 初始化tab标题和页面数据
     */
    private void initTabTextAndFragment(ArrayList<DecorateTitleEntity.ChannelItem> dataList) {
        tabIdList.clear();
        tabTextList.clear();
        for(int i=0;i<dataList.size();i++){
            tabTextList.add(dataList.get(i).getName());
            tabIdList.add(dataList.get(i).getId());

            Util.setLog(" -initTabText(dataList) - DecorateArticleListFragment", tabTextList.get(i)+"");
            Util.setLog(" -修改后返回来的 ---title_id--->>>", tabTextList.get(i)+"<<>>>" +tabTextList.get(i));
        }


        fragmentData.clear();
        for(int i=0; i<tabIdList.size(); i++){
            //网络请求
            fragmentData.add(new ChildFragment().newInstance(tabIdList.get(i), context));
            Util.setLog(" -initData() 网络请求- DecorateArticleListFragment", tabIdList.get(i)+"");
        }

        childAdapter = new ChildPagerAdapter(getChildFragmentManager());
        childViewPager.setAdapter(childAdapter);
        childViewPager.setOffscreenPageLimit(fragmentData.size());

        tabs.setViewPager(childViewPager,tabTextList);

        childAdapter.notifyDataSetChanged();

        if(childAdapter.getCount()==0){
            initTabTextAndFragment(ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).getUserChannel());
        }

        getActivityFragmentData();

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_more_channel:
                Intent it = new Intent(getActivity(), ChannelActivity.class);
//                Bundle b = new Bundle();
//                b.putString("titleItemJson", titleItemJson);
//                it.putExtra("bible_title_list_bundle", b);
//                startActivityForResult(it, CHANNELREQUEST);
                startActivity(it);
                getActivity().finish();
                break;
            case R.id.tv_gosearch:
                startActivity(new Intent(context, SearchActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Util.setLog(TAG, "----DecorateArticleListFragment-----------------------------" + requestCode + "--" + resultCode);
        switch (resultCode){
            case CHANNELRESULT:
//                userChannelList.clear();
//                userChannelList= ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).getUserChannel();
//                for(int i=0;i<userChannelList.size();i++){
//                    Util.setErrorLog(TAG, " onActivityResult --> "+userChannelList.get(i).getName());
//                }
                initTabTextAndFragment(ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).getUserChannel());
                break;
            case 0x00019:
                initTabTextAndFragment(ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).getUserChannel());
                break;
        }

    }


    public class ChildPagerAdapter extends FragmentPagerAdapter{
        FragmentManager fm;
        public ChildPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTextList.get(position);
        }

        @Override
        public int getCount() {
            return tabTextList.size();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 1:
                    MobclickAgent.onEvent(getActivity(),"click_index_sheji");
                    break;
                case 2:
                    MobclickAgent.onEvent(getActivity(),"click_index_jiaju");
                    break;
                case 3:
                    MobclickAgent.onEvent(getActivity(),"click_index_jiancai ");
                    break;
                case 4:
                    MobclickAgent.onEvent(getActivity(),"click_index_jianzhuang");
                    break;
                default:
                    MobclickAgent.onEvent(getActivity(),"click_index_others");
                    break;
            }
            return fragmentData.get(position);
        }
    }
}
