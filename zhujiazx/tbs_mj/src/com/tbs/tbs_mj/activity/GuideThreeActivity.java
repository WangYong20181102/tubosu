package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.utils.EventBusUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideThreeActivity extends BaseActivity {

    @BindView(R.id.guide_three_select_iv_01)
    ImageView guideThreeSelectIv01;
    @BindView(R.id.guide_three_select_kuang_iv_01)
    ImageView guideThreeSelectKuangIv01;
    @BindView(R.id.guide_three_select_iv_02)
    ImageView guideThreeSelectIv02;
    @BindView(R.id.guide_three_select_kuang_iv_02)
    ImageView guideThreeSelectKuangIv02;
    @BindView(R.id.guide_three_select_iv_03)
    ImageView guideThreeSelectIv03;
    @BindView(R.id.guide_three_select_kuang_iv_03)
    ImageView guideThreeSelectKuangIv03;
    @BindView(R.id.guide_three_select_iv_04)
    ImageView guideThreeSelectIv04;
    @BindView(R.id.guide_three_select_kuang_iv_04)
    ImageView guideThreeSelectKuangIv04;
    @BindView(R.id.guide_three_select_iv_05)
    ImageView guideThreeSelectIv05;
    @BindView(R.id.guide_three_select_kuang_iv_05)
    ImageView guideThreeSelectKuangIv05;
    @BindView(R.id.guide_three_select_iv_06)
    ImageView guideThreeSelectIv06;
    @BindView(R.id.guide_three_select_kuang_iv_06)
    ImageView guideThreeSelectKuangIv06;
    @BindView(R.id.guide_three_select_iv_07)
    ImageView guideThreeSelectIv07;
    @BindView(R.id.guide_three_select_kuang_iv_07)
    ImageView guideThreeSelectKuangIv07;
    @BindView(R.id.guide_three_select_iv_08)
    ImageView guideThreeSelectIv08;
    @BindView(R.id.guide_three_select_kuang_iv_08)
    ImageView guideThreeSelectKuangIv08;
    @BindView(R.id.guide_three_select_iv_09)
    ImageView guideThreeSelectIv09;
    @BindView(R.id.guide_three_select_kuang_iv_09)
    ImageView guideThreeSelectKuangIv09;
    @BindView(R.id.guide_three_back_rl)
    RelativeLayout guideThreeBackRl;
    @BindView(R.id.guide_three_close_rl)
    RelativeLayout guideThreeCloseRl;
    @BindView(R.id.guide_three_next_step_tv)
    TextView guideThreeNextStepTv;
    private String TAG = "GuideThreeActivity";
    private Context mContext;

    private boolean mSelect01 = false;
    private boolean mSelect02 = false;
    private boolean mSelect03 = false;
    private boolean mSelect04 = false;
    private boolean mSelect05 = false;
    private boolean mSelect06 = false;
    private boolean mSelect07 = false;
    private boolean mSelect08 = false;
    private boolean mSelect09 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_three);
        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.FINISH_GUIDE_ACTIVITY:
                finish();
                break;
        }
    }

    @OnClick({R.id.guide_three_select_iv_01, R.id.guide_three_select_iv_02,
            R.id.guide_three_select_iv_03, R.id.guide_three_select_iv_04,
            R.id.guide_three_select_iv_05, R.id.guide_three_select_iv_06,
            R.id.guide_three_select_iv_07, R.id.guide_three_select_iv_08,
            R.id.guide_three_select_iv_09, R.id.guide_three_back_rl,
            R.id.guide_three_close_rl, R.id.guide_three_next_step_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guide_three_select_iv_01:
                if (mSelect01) {
                    guideThreeSelectKuangIv01.setVisibility(View.GONE);
                    mSelect01 = false;
                } else {
                    mSelect01 = true;
                    guideThreeSelectKuangIv01.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_select_iv_02:
                if (mSelect02) {
                    guideThreeSelectKuangIv02.setVisibility(View.GONE);
                    mSelect02 = false;
                } else {
                    mSelect02 = true;
                    guideThreeSelectKuangIv02.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_select_iv_03:
                if (mSelect03) {
                    guideThreeSelectKuangIv03.setVisibility(View.GONE);
                    mSelect03 = false;
                } else {
                    mSelect03 = true;
                    guideThreeSelectKuangIv03.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_select_iv_04:
                if (mSelect04) {
                    guideThreeSelectKuangIv04.setVisibility(View.GONE);
                    mSelect04 = false;
                } else {
                    mSelect04 = true;
                    guideThreeSelectKuangIv04.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_select_iv_05:
                if (mSelect05) {
                    guideThreeSelectKuangIv05.setVisibility(View.GONE);
                    mSelect05 = false;
                } else {
                    mSelect05 = true;
                    guideThreeSelectKuangIv05.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_select_iv_06:
                if (mSelect06) {
                    guideThreeSelectKuangIv06.setVisibility(View.GONE);
                    mSelect06 = false;
                } else {
                    mSelect06 = true;
                    guideThreeSelectKuangIv06.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_select_iv_07:
                if (mSelect07) {
                    guideThreeSelectKuangIv07.setVisibility(View.GONE);
                    mSelect07 = false;
                } else {
                    mSelect07 = true;
                    guideThreeSelectKuangIv07.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_select_iv_08:
                if (mSelect08) {
                    guideThreeSelectKuangIv08.setVisibility(View.GONE);
                    mSelect08 = false;
                } else {
                    mSelect08 = true;
                    guideThreeSelectKuangIv08.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_select_iv_09:
                if (mSelect09) {
                    guideThreeSelectKuangIv09.setVisibility(View.GONE);
                    mSelect09 = false;
                } else {
                    mSelect09 = true;
                    guideThreeSelectKuangIv09.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.guide_three_back_rl:
                //返回
                finish();
                break;
            case R.id.guide_three_close_rl:
                //回到主页
                startActivity(new Intent(mContext, MainActivity.class));
                EventBusUtil.sendEvent(new Event(EC.EventCode.FINISH_GUIDE_ACTIVITY));
                break;
            case R.id.guide_three_next_step_tv:
                if (!mSelect01 && !mSelect02
                        && !mSelect03 && !mSelect04
                        && !mSelect05 && !mSelect06
                        && !mSelect07 && !mSelect08 && !mSelect09) {
                    Toast.makeText(mContext, "请选择您喜欢的效果图", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(mContext, GuideFourActivity.class));
                }
                break;
        }
    }
}
