package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
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

public class GuideTwoActivity extends BaseActivity {
    @BindView(R.id.guide_two_nl_1_tv)
    TextView guideTwoNl1Tv;
    @BindView(R.id.guide_two_nl_2_tv)
    TextView guideTwoNl2Tv;
    @BindView(R.id.guide_two_nl_3_tv)
    TextView guideTwoNl3Tv;
    @BindView(R.id.guide_two_back_rl)
    RelativeLayout guideTwoBackRl;
    @BindView(R.id.guide_two_close_rl)
    RelativeLayout guideTwoCloseRl;
    @BindView(R.id.guide_two_next_step_tv)
    TextView guideTwoNextStepTv;
    private Context mContext;
    private String TAG = "GuideTwoActivity";
    private boolean isSelectAge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_two);
        ButterKnife.bind(this);
        mContext = this;
        isSelectAge = false;
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

    @OnClick({R.id.guide_two_nl_1_tv, R.id.guide_two_nl_2_tv, R.id.guide_two_nl_3_tv,
            R.id.guide_two_back_rl, R.id.guide_two_close_rl, R.id.guide_two_next_step_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guide_two_nl_1_tv:
                //点击了年龄一
                changeTvState(1);
                break;
            case R.id.guide_two_nl_2_tv:
                //点击了年龄2
                changeTvState(2);
                break;
            case R.id.guide_two_nl_3_tv:
                //点击年龄3
                changeTvState(3);
                break;
            case R.id.guide_two_back_rl:
                //点击了返回按钮
                finish();
                break;
            case R.id.guide_two_close_rl:
                //点击了关闭按钮 回到主界面
                startActivity(new Intent(mContext, MainActivity.class));
                EventBusUtil.sendEvent(new Event(EC.EventCode.FINISH_GUIDE_ACTIVITY));
                break;
            case R.id.guide_two_next_step_tv:
                //点击进入下一步
                if (isSelectAge) {
                    startActivity(new Intent(mContext, GuideThreeActivity.class));
                } else {
                    //未选择
                    Toast.makeText(mContext, "请选择您的年龄阶段", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    //按钮点击触发的文本变化
    private void changeTvState(int position) {
        isSelectAge = true;
        switch (position) {
            case 1:
                //第一个按钮的变化
                guideTwoNl1Tv.setBackgroundResource(R.drawable.shape_guide_blue);
                guideTwoNl1Tv.setTextColor(Color.parseColor("#ffffff"));
                guideTwoNl2Tv.setBackgroundResource(R.drawable.shape_guide_btn_white);
                guideTwoNl2Tv.setTextColor(Color.parseColor("#000000"));
                guideTwoNl3Tv.setBackgroundResource(R.drawable.shape_guide_btn_white);
                guideTwoNl3Tv.setTextColor(Color.parseColor("#000000"));
                break;
            case 2:
                //第二个按钮的变化
                guideTwoNl2Tv.setBackgroundResource(R.drawable.shape_guide_blue);
                guideTwoNl2Tv.setTextColor(Color.parseColor("#ffffff"));
                guideTwoNl1Tv.setBackgroundResource(R.drawable.shape_guide_btn_white);
                guideTwoNl1Tv.setTextColor(Color.parseColor("#000000"));
                guideTwoNl3Tv.setBackgroundResource(R.drawable.shape_guide_btn_white);
                guideTwoNl3Tv.setTextColor(Color.parseColor("#000000"));
                break;
            case 3:
                //第三个按钮的变化
                guideTwoNl3Tv.setBackgroundResource(R.drawable.shape_guide_blue);
                guideTwoNl3Tv.setTextColor(Color.parseColor("#ffffff"));
                guideTwoNl2Tv.setBackgroundResource(R.drawable.shape_guide_btn_white);
                guideTwoNl2Tv.setTextColor(Color.parseColor("#000000"));
                guideTwoNl1Tv.setBackgroundResource(R.drawable.shape_guide_btn_white);
                guideTwoNl1Tv.setTextColor(Color.parseColor("#000000"));
                break;
        }
    }
}
