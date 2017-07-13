package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Lie on 2017/5/31.
 */

public class KeepAccountActivity extends Activity{
    private Context mContext;
    private static final String TAG = KeepAccountActivity.class.getSimpleName();
    private RelativeLayout relOpenAccount;
    private ImageView ivBack;
    private EditText etBudget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setActivityStatusColor(KeepAccountActivity.this);
        setContentView(R.layout.activity_keep_account);
        mContext = KeepAccountActivity.this;
        initView();
        setClick();
    }
    private void initView(){
        relOpenAccount = (RelativeLayout) findViewById(R.id.rel_open_account);
        ivBack = (ImageView) findViewById(R.id.keep_account_back);
        etBudget = (EditText) findViewById(R.id.et_budget_account);
    }


    private void setClick(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        relOpenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budget = etBudget.getText().toString().trim();
                float budeget = 0;
                if("".equals(budget)){
                    Util.setToast(mContext, "预算不能为空");
                    return;
                }else {
                    budeget = Float.parseFloat(budget);
                    if(budeget - 1 < 0){
                        Util.setToast(mContext, "预算从1万开始");
                        return;
                    }else if(budeget - 100 > 0) {
                        Util.setToast(mContext, "预算不能超过100万");
                        return;
                    }else{
                        if(Util.isNetAvailable(mContext)){
                            summitBudget(etBudget.getText().toString());
                        }
                    }
                }
            }
        });
    }

    private void summitBudget(String budget) {
        float f = Float.parseFloat(budget) * 10000;

        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, String> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        param.put("expected_cost", f + "");

        okHttpUtil.post(Constant.SUMMIT_BUDGET_URL, param, new OKHttpUtil.BaseCallBack() {

            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "上传预算=====" + json);
                parseJson(json);
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    private void parseJson(String json){

        try {
            JSONObject object = new JSONObject(json);
            if(object.getInt("status") == 200){
                Intent intent = new Intent(mContext, DecorateAccountActivity.class);
                startActivity(intent);
                finish();
            }else{
                Util.setToast(mContext, "提交预算失败，稍后再试~");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
