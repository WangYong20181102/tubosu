package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.PmTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class NewFreeOrderActivity extends AppCompatActivity {
    private Context mContext;
    private String TAG = "NewFreeOrderActivity";

    private ImageView getprice_back;//返回键
    private EditText getprice_name;//用户的名称
    private EditText getprice_phone;//用户的联系电话
    private TextView tv_getprice_city;//城市选择
    private RelativeLayout rel_choose_city;//选择城市层
    private Button getprice_submit;//确认提交
    private PmTextView cheat_textview;//跑马灯文字展示部位

    private RelativeLayout re_download;//下载图层
    private TextView into_download;//去下载按钮
    private ImageView download_close;//关闭下载图层

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_free_order);
        mContext = this;
        bindView();
        initView();
        initViewEvent();
    }

    private void bindView() {
        getprice_back = (ImageView) findViewById(R.id.getprice_back);
        getprice_name = (EditText) findViewById(R.id.getprice_name);
        getprice_phone = (EditText) findViewById(R.id.getprice_phone);
        tv_getprice_city = (TextView) findViewById(R.id.tv_getprice_city);
        cheat_textview = (PmTextView) findViewById(R.id.cheat_textview);
        rel_choose_city = (RelativeLayout) findViewById(R.id.rel_choose_city);
        getprice_submit = (Button) findViewById(R.id.getprice_submit);

        re_download = (RelativeLayout) findViewById(R.id.re_download);
        into_download = (TextView) findViewById(R.id.into_download);
        download_close = (ImageView) findViewById(R.id.download_close);
    }

    private void initView() {
        tv_getprice_city.setText("" + CacheManager.getCity(mContext) + "(点击可更改城市)");
        cheat_textview.setText("武汉的周先生已经获取4份报价 1分钟前 上海的李先生已经获取2份报价 1分钟前" +
                "北京的何女士已经获取2份报价 3分钟前 深圳的高先生已经获取2份报价 1分钟前"+
                "南昌的张女士已经获取1份报价 2分钟前 厦门的刘先生已经获取1份报价 1分钟前"+
                "湖南的于女士已经获取1份报价 6分钟前 武汉的刘先生已经获取1份报价 2分钟前");
    }

    private void initViewEvent() {
        tv_getprice_city.setOnClickListener(occl);
        rel_choose_city.setOnClickListener(occl);
        getprice_back.setOnClickListener(occl);
        getprice_submit.setOnClickListener(occl);
        into_download.setOnClickListener(occl);
        download_close.setOnClickListener(occl);

    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.getprice_back:
                    finish();
                    break;
                case R.id.rel_choose_city:
                case R.id.tv_getprice_city:
                    //选择城市
                    Intent cityIntent = new Intent(mContext, SelectCityActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("frompop", 31);
                    cityIntent.putExtra("pop_bundle", b);
                    startActivityForResult(cityIntent, 124);
                    break;
                case R.id.getprice_submit:
                    //发单提交
                    String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
                    if (!TextUtils.isEmpty(getprice_phone.getText().toString())
                            && getprice_phone.getText().toString().matches(MOBILE)) {
                        HttpPostMessage();
                    } else {
                        Toast.makeText(mContext, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.download_close:
                    //关闭下载图层
                    re_download.setVisibility(View.GONE);
                    break;
                case R.id.into_download:
                    //去下载
                    Uri uri = Uri.parse("http://t.cn/R4p6kwr");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    mContext.startActivity(intent);
                    break;
            }
        }
    };

    private void HttpPostMessage() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("cellphone", getprice_phone.getText().toString());
        param.put("urlhistory", Constant.PIPE_CODE);
        param.put("comeurl", Constant.PIPE_CODE);
        okHttpUtil.post(Constant.PUB_ORDER_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "发单请求===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 77:
                String city = data.getBundleExtra("city_bundle").getString("ci");
                CacheManager.setCity(mContext, city);
                tv_getprice_city.setText("" + city);
                break;
        }
    }
}
