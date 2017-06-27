package com.tbs.tobosupicture.activity;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.bean.LbPicture;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    private Handler mHandler;

    private Button mainBtn;
    private ImageView mainImg;
    private TextView mainTv;
    private EditText city;
    private EditText device;
    private EditText version;

    private Map<String, String> param = new HashMap<>();
    private List<LbPicture> lbPictureList = new ArrayList<>();
    private String TAG = "MainActivity";
    private Context mContext;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mHandler = new Handler();
        bindView();
        initView();
    }

    private void bindView() {
        mainBtn = (Button) findViewById(R.id.main_btn);
        mainImg = (ImageView) findViewById(R.id.main_img);
        mainTv = (TextView) findViewById(R.id.main_tv);
        city = (EditText) findViewById(R.id.main_et_city);
        device = (EditText) findViewById(R.id.main_et_device);
        version = (EditText) findViewById(R.id.main_et_version);
    }

    private void initView() {
        mainBtn.setOnClickListener(occl);
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (param != null || lbPictureList != null) {
                param.clear();
                lbPictureList.clear();
            }
            Log.e(TAG, "开始请求====+url" + UrlConstans.LB_URL);
            apiGetPicture();
            Message message = new Message();
            message.obj = "这是一条数据。。";
            mHandler.sendMessage(message);
        }
    };

    private void apiGetPicture() {
        Log.e(TAG , "===请求zhongh》》》");
//        param.put("city", city.getText().toString());
//        param.put("device", device.getText().toString());
//        param.put("version", version.getText().toString());
        HttpUtils.doPost(UrlConstans.LB_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG + "===请求错误》》》", call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responses = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responses);
                    String errorCode = jsonObject.getString("error_code");
                    if (errorCode.equals("0")) {
                        Log.e(TAG, "请求成功！");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            LbPicture lbPicture = new LbPicture(jsonArray.get(i).toString());
                            lbPictureList.add(lbPicture);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(mContext).load(lbPictureList.get(0).getImgUrl()).into(mainImg);
                                mainTv.setText("" + responses);
                            }
                        });

                    } else {
                        Log.e(TAG, "请求失败！" + responses);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
