package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dec on 2016/9/26.
 */
public class ChangeUserNameActivity extends BaseActivity{
    private Context mContext;
    private RelativeLayout rel_changeusername_back;

    private EditText et_change_username;
    private TextView tv_save_username;
    private ImageView iv_shut;

    private String name = "";

    private String change_name_url = Constant.ZHJ + "tapp/user/chage_user_info";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        mContext = ChangeUserNameActivity.this;
        initViews();
    }

    private void initDatas() {
        Bundle b = getIntent().getBundleExtra("Change_User_Name_Bundle");
        name = b.getString("username");
    }

    private void initViews() {
        initDatas();

        iv_shut = (ImageView) findViewById(R.id.iv_shut);
        iv_shut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_change_username.setText("");
                iv_shut.setVisibility(View.GONE);
            }
        });
        rel_changeusername_back = (RelativeLayout) findViewById(R.id.rel_changeusername_back);
        rel_changeusername_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_change_username = (EditText) findViewById(R.id.et_change_username);
        et_change_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iv_shut.setVisibility(View.VISIBLE);
                if(et_change_username.getText().toString().trim().length()>=8){
                    Toast.makeText(mContext, "昵称过长", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_save_username = (TextView) findViewById(R.id.tv_save_username);
        tv_save_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.checkNetwork(mContext)) {
                    //FIXME 请求网络哦
                    if("".equals(et_change_username.getText().toString().trim())){
                        Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!"name".equals(et_change_username.getText().toString().trim())){
                        do_postChangeName();
                    }else{
                        Toast.makeText(mContext, "您没做修改", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    Toast.makeText(mContext, "网络断开了~", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


    }

    private void do_postChangeName(){
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("token",""));
        hashMap.put("field", "1"); // 1是修改名称
        hashMap.put("new", et_change_username.getText().toString().trim());
        okHttpUtil.post(change_name_url, hashMap, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                System.out.println("---修改名称--" + json);
                try {
                    JSONObject obj = new JSONObject(json);

                    if(obj.getInt("error_code")==0){
                        Intent it = new Intent();
                        Bundle bundle=new Bundle();
                        bundle.putString("new_user_name_string", et_change_username.getText().toString().trim());
                        it.putExtras(bundle);
                        it.putExtra("new_user_name_bundle", bundle);
                        setResult(Constant.CHANGE_USERNAME_RESULTCODE, it);

                        // 存sharepreference
                        getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).edit().putString("user_name", et_change_username.getText().toString().trim()).commit();
                        finish();
                    }else if(obj.getInt("error_code")==511){
                        Toast.makeText(mContext, "装修公司不能修改昵称", Toast.LENGTH_SHORT).show();
                    }
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
