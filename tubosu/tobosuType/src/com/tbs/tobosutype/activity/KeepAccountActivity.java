package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.Util;

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
                Intent intent = new Intent(mContext, DecorateAccountActivity.class);
                intent.putExtra("budget", etBudget.getText().toString());
                CacheManager.setDecorateBudget(mContext, etBudget.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }
}
