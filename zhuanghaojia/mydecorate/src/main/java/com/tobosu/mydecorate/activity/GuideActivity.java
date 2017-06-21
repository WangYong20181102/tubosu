package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

/**
 * Created by dec on 2016/9/12.
 */
public class GuideActivity extends AppCompatActivity {
    // 由于不需要引导页 该城市列表的请求放在WelcomeActivity中
    private String city_url = Constant.ZHJ + "tapp/util/getCityList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        initCityData();

        findViewById(R.id.tv_guild).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, TransitActivity.class));
                finish();;
            }
        });
    }


    private void initCityData(){
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        okHttpUtil.get(city_url, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                System.out.println("city数据请求成功");
                getSharedPreferences("City_Data_Json_SP", Context.MODE_PRIVATE).edit().putString("city_data_json", json).commit();
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
