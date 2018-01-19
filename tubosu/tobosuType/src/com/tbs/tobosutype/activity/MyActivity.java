package com.tbs.tobosutype.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.Util;

/**
 * Created by Lie on 2017/5/25.
 */

public class MyActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout myUnloginMsg;
    private LinearLayout myUnloginFeedback;
    private LinearLayout myUnloginSecurity;
    private LinearLayout myUnloginPersonalData;
    private LinearLayout myUnloginStore;
    private RelativeLayout relUnlogin;
    private ImageView ivSetting;
    private ImageView ivSystemMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mContext = MyActivity.this;
        TAG = MyActivity.class.getSimpleName();

        initView();
        setClick();
    }


    private void initView(){
        myUnloginMsg = (LinearLayout) findViewById(R.id.my_unlogin_msg);
        myUnloginFeedback = (LinearLayout) findViewById(R.id.my_unlogin_feedback);
        myUnloginSecurity = (LinearLayout) findViewById(R.id.my_unlogin_security);
        myUnloginPersonalData = (LinearLayout) findViewById(R.id.my_unlogin_personal_data);
        myUnloginStore = (LinearLayout) findViewById(R.id.my_unlogin_store);
        relUnlogin = (RelativeLayout) findViewById(R.id.rel_go_login);
        ivSetting = (ImageView) findViewById(R.id.iv_unlogin_setting);
        ivSystemMessage = (ImageView) findViewById(R.id.iv_unlogin_system_message);
    }

    private void setClick(){
        myUnloginMsg.setOnClickListener(this);
        myUnloginFeedback.setOnClickListener(this);
        myUnloginSecurity.setOnClickListener(this);
        myUnloginPersonalData.setOnClickListener(this);
        myUnloginStore.setOnClickListener(this);
        relUnlogin.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        ivSystemMessage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_unlogin_msg:
            case R.id.my_unlogin_feedback:
            case R.id.my_unlogin_security:
            case R.id.my_unlogin_personal_data:
            case R.id.my_unlogin_store:
            case R.id.rel_go_login:
                startActivityForResult(new Intent(mContext, NewLoginActivity.class), 0x00017);
                break;
            case R.id.iv_unlogin_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.iv_unlogin_system_message:
                startActivity(new Intent(mContext, SystemMessageActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case Constant.UNLOGIN_TO_LOGIN_RESULTCODE:
                Util.setToast(mContext, "更新登录状态哦");
                finish();
                break;
            case 404:
//                finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
