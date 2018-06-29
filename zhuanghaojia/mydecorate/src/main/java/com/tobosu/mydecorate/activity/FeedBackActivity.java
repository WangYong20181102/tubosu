package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.global.AppUtil;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.LoadingView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dec on 2016/9/27.
 */

public class FeedBackActivity extends BaseActivity {
    private static final String TAG = FeedBackActivity.class.getSimpleName();
    private Context mContext;
    private RelativeLayout rel_feedback_back;
    private TextView tv_send_feedback;
    private EditText et_feedback_content;
    private EditText et_feedback_contact;

    private String feedback_url = Constant.ZHJ + "tapp/util/feedBackMt";

    LoadingView loadingView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mContext = FeedBackActivity.this;
        initViews();
    }

    private void initViews() {
        rel_feedback_back = (RelativeLayout) findViewById(R.id.rel_feedback_back);
        rel_feedback_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_feedback_content = (EditText) findViewById(R.id.et_feedback_content);
        et_feedback_contact = (EditText) findViewById(R.id.et_feedback_contact);
        tv_send_feedback = (TextView) findViewById(R.id.tv_send_feedback);
        tv_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView = new LoadingView(mContext);
                loadingView.showAtLocation(findViewById(R.id.et_feedback_contact), Gravity.CENTER, 0,0);
                sendFeedback();
            }
        });

    }

    private void sendFeedback() {
        if ("".equals(et_feedback_content.getText().toString().trim())) {
            Toast.makeText(mContext, "反馈不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Util.checkNetwork(mContext)){
            post_feedback();
        }else{
            Toast.makeText(mContext, "请检查网络~", Toast.LENGTH_SHORT).show();
        }
    }

    private void post_feedback() {
        OKHttpUtil okhttp = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("mode", android.os.Build.MODEL); // 手机型号
        param.put("mode_version", AppUtil.getAppVersion(mContext));
        param.put("mobile", et_feedback_contact.getText().toString().trim());
        param.put("content", et_feedback_content.getText().toString().trim());
        param.put("token", AppUtil.getToekn(mContext));


        okhttp.post(feedback_url, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                System.out.println("-----提交反馈信息啦---------"+json);
                try {
                    JSONObject obj = new JSONObject(json);
                    if(obj.getInt("error_code")==0){
                        Toast.makeText(mContext,"反馈成功", Toast.LENGTH_SHORT).show();
                        loadingView.dismiss();
                        finish();
                    }else if(obj.getInt("error_code")==40301) {
                        Toast.makeText(mContext,"反馈失败", Toast.LENGTH_SHORT).show();
                    }else if(obj.getInt("error_code")==40302) {
                        Toast.makeText(mContext,"1分钟之内不能再次反馈", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(Request request, IOException e) {
                Toast.makeText(mContext,"反馈失败, 请稍后再试~", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response response, int code) {
                Toast.makeText(mContext, "后台繁忙, 请稍后再试~", Toast.LENGTH_SHORT).show();
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
