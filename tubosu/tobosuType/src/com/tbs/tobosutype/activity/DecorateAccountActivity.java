package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

/**
 * Created by Lie on 2017/5/31.
 */

public class DecorateAccountActivity extends Activity {
    private Context mContext;
    private static final String TAG = DecorateAccountActivity.class.getSimpleName();

    /**
     *  总开支占比总预算在80%-0%之间    --->>   小主，一切还在预算当中
     *  总开支占比总预算在80%-100%      --->>   要死了，花钱如流水啊
     *  总开支占比总预算超过100%        --->>   就知道你是土豪，你任性就使劲花吧
     */
    private String[] budgetTips = {"小主，一切还在预算当中", "要死了，花钱如流水啊","就知道你是土豪，你任性就使劲花吧"};

    private RelativeLayout relBack;
    private ImageView ivEditAccount;
    private TextView tvTotalBuduget;
    private SeekBar seekProgress;
    private TextView tvState;
    private TextView tvTotalCost;
    private TextView tvStartAccount;
    private RelativeLayout relDataEmpty;
    private RelativeLayout relDataLaout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Util.setActivityStatusColor(DecorateAccountActivity.this);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_decorate_account);
        mContext = DecorateAccountActivity.this;
        initView();
        initData();
        setClick();
    }

    private void initData(){

    }

    private void initView() {
        relBack = (RelativeLayout) findViewById(R.id.decorate_account_back);
        ivEditAccount = (ImageView) findViewById(R.id.iv_edit_account);
        tvTotalBuduget = (TextView) findViewById(R.id.tv_total_buduget);
        seekProgress = (SeekBar) findViewById(R.id.seek_progress);
        tvTotalCost = (TextView) findViewById(R.id.tv_total_cost);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvStartAccount = (TextView) findViewById(R.id.tv_start_account);
        relDataEmpty = (RelativeLayout) findViewById(R.id.rel_data_empty);
        relDataLaout = (RelativeLayout) findViewById(R.id.rel_data_layout);


        if(seekProgress.getProgress()>=0 && seekProgress.getProgress()<=20){
            seekProgress.setProgress(20);
            seekProgress.setBackgroundResource(R.color.budget_green);
            tvState.setText(budgetTips[0]);
        }else if(seekProgress.getProgress()>=20 && seekProgress.getProgress()<40){
            seekProgress.setProgress(37);
            seekProgress.setBackgroundResource(R.color.budget_blue);
            tvState.setText(budgetTips[0]);
        }else if(seekProgress.getProgress()>=40 && seekProgress.getProgress()<60){
            seekProgress.setProgress(57);
            seekProgress.setBackgroundResource(R.color.budget_orange);
            tvState.setText(budgetTips[0]);
        }else if(seekProgress.getProgress()>=60 && seekProgress.getProgress()<80){
            seekProgress.setProgress(77);
            seekProgress.setBackgroundResource(R.color.budget_yellow);
            tvState.setText(budgetTips[1]);
        }else{
            seekProgress.setProgress(90);
            seekProgress.setBackgroundResource(R.color.budget_red);
            tvState.setText(budgetTips[2]);
        }

    }

    private void setClick() {
        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, EditAccountAcitivity.class), 0x00013);
            }
        });

        tvStartAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, EditAccountAcitivity.class),0x00013);
            }
        });

        seekProgress.setBackgroundResource(R.color.budget_green);

//        seekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

    }
}
