package com.tbs.tobosutype.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.AnswerDetailsViewPagerAdapter;
import com.tbs.tobosutype.adapter.AnswerItemDetailsAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2018/11/6 11:39.
 */
public class AnswerItemDetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.relBackShoucang)
    RelativeLayout relBackShoucang;        //返回按钮
    @BindView(R.id.image_top_share)
    ImageView imageTopShare;    //分享
    @BindView(R.id.rv_answerdetail)
    RecyclerView rvAnswerDetails;   //滑动布局
    @BindView(R.id.rl_cover_ad_bg)
    RelativeLayout rlCoverAdBg;
    @BindView(R.id.iv_ad)
    ViewPager ivAd;     //滑动图片显示
    @BindView(R.id.tv_current_num)
    TextView tvCurrentNum;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;

    private AnswerDetailsViewPagerAdapter viewPagerAdapter;


    private AnswerItemDetailsAdapter adapter;   //适配器
    private List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_details);
        ButterKnife.bind(this);
        initViewEvent();
    }

    //    初始化
    private void initViewEvent() {
        stringList = new ArrayList<>();
        stringList.add("q");
        stringList.add("2");
        stringList.add("3");
        adapter = new AnswerItemDetailsAdapter(this, stringList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAnswerDetails.setLayoutManager(linearLayoutManager);
        rvAnswerDetails.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ivAd.addOnPageChangeListener(this);

    }

    @OnClick({R.id.relBackShoucang, R.id.image_top_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relBackShoucang:  //返回
                finish();
                break;
            case R.id.image_top_share:  //分享
                new ShareUtil(this, "装修tittle", "zhaugnxiu", Constant.PIPE);
                break;
        }
    }

    /**
     * 点击图片预览大图
     */
    public void showBigPhoto(List<String> stringList, int position) {
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new AnswerDetailsViewPagerAdapter(this, stringList);
            ivAd.setAdapter(viewPagerAdapter);
        } else {
            viewPagerAdapter.setActivityBases(stringList);
            viewPagerAdapter.notifyDataSetChanged();
        }
        ivAd.setCurrentItem(position);
        ivAd.setOffscreenPageLimit(stringList.size());
        tvTotalNum.setText("/" + stringList.size());
        rlCoverAdBg.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏轮播图
     */
    public void hindViewPagerImage() {
        if (viewPagerAdapter != null) {
            viewPagerAdapter = null;
        }
        rlCoverAdBg.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvCurrentNum.setText((position + 1) + "");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
