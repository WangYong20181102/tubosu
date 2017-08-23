package com.tbs.tobosupicture.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.SelectCityActivity;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import java.io.FileReader;
import java.io.FileWriter;
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
    private static final String TAG = "TemplateFragment";
    private String chosenCity;

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
        String city = SpUtils.getLocationCity(getActivity());
        if(city.contains("市") || city.contains("县")){
            city = city.substring(0, city.length()-1);
        }
        chosenCity = "".equals(city)?"深圳": city;
        tempLocation.setText(chosenCity);
        SpUtils.setTemplateFragmentCity(getActivity(), chosenCity);


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

    @Override
    protected boolean isRegisterEventBus() {
        return true;
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


            HttpUtils.doPost(UrlConstans.GET_FACTORY_DECORATE_STYLE_SURL, param, new Callback() {
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
                    SpUtils.setFactoryStyleJson(getActivity(), json);
                    Utils.setErrorLog(TAG, json);
                }
            });
        }
    }

    private void writeJsonToText(String json){
        FileWriter fw = null;//在外面创建，避免里面直接新建后面无法读取fw,fr
        FileReader fr = null;
        try {
            fw = new FileWriter("F:\\git_workspace\\TobosuPicture\\app\\src\\main\\java\\com\\tbs\\tobosupicture\\json.txt");//创建一个h.txt文件
            fr = new FileReader(json);//读取h.java文件

            char [] buf = new char[1024];//定义数组
            int num = 0;

            while((num = fr.read(buf))!=-1)//当读取不等于-1时写入
            {
                fw.write(buf);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fr!=null){
                //不为空则关闭
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fw!=null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
//                startActivityForResult(new Intent(getActivity(), SelectCityActivity.class), 10);
                Intent intent=new Intent(getActivity(), SelectCityActivity.class);
                intent.putExtra("from","TemplateFragment");
                startActivity(intent);
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

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case EC.EventCode.CHOOSE_CITY_CODE:
                chosenCity = (String)event.getData();
                tempLocation.setText((String)event.getData());
                EventBusUtil.sendEvent(new Event(EC.EventCode.CHOOSE_CITY_TO_GET_DATA_FROM_NET_HOUSE, chosenCity));
                EventBusUtil.sendEvent(new Event(EC.EventCode.CHOOSE_CITY_TO_GET_DATA_FROM_NET_FACTORY, chosenCity));
                break;
        }
    }
}
