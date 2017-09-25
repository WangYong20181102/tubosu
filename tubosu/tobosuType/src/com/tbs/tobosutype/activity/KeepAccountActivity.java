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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
        HashMap<String, String> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        param.put("expected_cost", f + "");

        OKHttpUtil.post(Constant.SUMMIT_BUDGET_URL, param, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                Log.e(TAG, "上传预算=====" + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJson(json);
                    }
                });

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
