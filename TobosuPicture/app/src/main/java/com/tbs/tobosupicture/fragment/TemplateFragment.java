package com.tbs.tobosupicture.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

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
 * Created by Mr.Lin on 2017/6/29 11:00.
 * 样板图fragment
 */

public class TemplateFragment extends BaseFragment {

    @BindView(R.id.temp_location)
    TextView tempLocation;
    @BindView(R.id.tvHouseDecorateText)
    TextView tvHouseDecorateText;
    @BindView(R.id.tvHouseDecorateLine)
    TextView tvHouseDecorateLine;
    @BindView(R.id.tvFactoryDecorateText)
    TextView tvFactoryDecorateText;
    @BindView(R.id.tvFactoryDecorateLine)
    TextView tvFactoryDecorateLine;
    @BindView(R.id.picViewpager)
    ViewPager picViewpager;

    private ArrayList<BaseFragment> listFragments =new ArrayList<BaseFragment>();
    private PictureAdapter adapter;

    public TemplateFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_template, null);
        ButterKnife.bind(this, rootView);
        initView();
        getDataFromNet();
        return rootView;
    }

    private void initView() {
        tempLocation.setText("".equals(SpUtils.getLocationCity(getActivity()))?"深圳":SpUtils.getLocationCity(getActivity()));

        listFragments.add(new HouseFragment());
        listFragments.add(new FactoryFragment());

        adapter=new PictureAdapter(getChildFragmentManager(),listFragments);
        picViewpager.setAdapter(adapter);
        picViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTextStylePosition(position);
                listFragments.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getDataFromNet(){
        if(Utils.isNetAvailable(getActivity())){
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("token", Utils.getDateToken());
            HttpUtils.doPost(UrlConstans.GET_HOUSE_DECORATE_STYLE_URL, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(getActivity(),"系统繁忙请稍后再试!");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    SpUtils.setHouseStyleJson(getActivity(), json);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(getActivity()).unbind();
    }

    @OnClick({R.id.temp_location, R.id.tvHouseDecorateText, R.id.tvFactoryDecorateText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.temp_location:
//                startActivityforresul(new Intent(getActivity(), SelectCityActivity.class));
                break;
            case R.id.tvHouseDecorateText:
                picViewpager.setCurrentItem(0);
                setTextStylePosition(0);
                break;
            case R.id.tvFactoryDecorateText:
                picViewpager.setCurrentItem(1);
                setTextStylePosition(1);
                break;
        }
    }

    private void setTextStylePosition(int position){
        if (position==0){
            tvHouseDecorateLine.setBackgroundColor(Color.parseColor("#ff882e"));
            tvFactoryDecorateLine.setBackgroundColor(Color.parseColor("#202124"));
            tvHouseDecorateText.setTextColor(Color.parseColor("#FFFFFF"));
            tvFactoryDecorateText.setTextColor(Color.parseColor("#B2ABAB"));
        }else if(position==1){
            tvHouseDecorateLine.setBackgroundColor(Color.parseColor("#202124"));
            tvFactoryDecorateLine.setBackgroundColor(Color.parseColor("#ff882e"));
            tvHouseDecorateText.setTextColor(Color.parseColor("#B2ABAB"));
            tvFactoryDecorateText.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }


    private class PictureAdapter extends FragmentPagerAdapter {
        private ArrayList<BaseFragment> dataList;

        public PictureAdapter(FragmentManager fm, ArrayList<BaseFragment> dataList) {
            super(fm);
            this.dataList = dataList;
        }

        @Override
        public Fragment getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
