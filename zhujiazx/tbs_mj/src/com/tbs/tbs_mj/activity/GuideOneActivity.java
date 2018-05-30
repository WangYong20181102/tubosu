package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideOneActivity extends BaseActivity {
    @BindView(R.id.guide_one_city_tv)
    TextView guideOneCityTv;
    @BindView(R.id.guide_one_next_step_tv)
    TextView guideOneNextStepTv;
    @BindView(R.id.guide_one_close_rl)
    RelativeLayout guideOneCloseRl;
    //引导发单页第一页 确定城市的页面
    private String TAG = "GuideOneActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_one);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    //城市定位
    private void initEvent() {
        if (TextUtils.isEmpty(SpUtil.getCity(mContext))) {
            guideOneCityTv.setText("");
        } else {
            guideOneCityTv.setText("" + SpUtil.getCity(mContext));
        }

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.SELECE_CITY_IN_GUIDE_ON_ACTIVITY:
                if (!TextUtils.isEmpty((String) event.getData())) {
                    //设置城市
                    guideOneCityTv.setText("" + (String) event.getData());
                }
                break;
            case EC.EventCode.FINISH_GUIDE_ACTIVITY:
                finish();
                break;
        }
    }

    @OnClick({R.id.guide_one_city_tv, R.id.guide_one_next_step_tv, R.id.guide_one_close_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guide_one_city_tv:
                //进入城市选择页面
                Intent intentToSelectCity = new Intent(mContext, SelectCtiyActivity.class);
                intentToSelectCity.putExtra("mWhereFrom", "GuideOneActivity");
                mContext.startActivity(intentToSelectCity);
                break;
            case R.id.guide_one_next_step_tv:
                //进入下一步
                gotoNextActivity();
                break;
            case R.id.guide_one_close_rl:
                startActivity(new Intent(mContext, MainActivity.class));
                EventBusUtil.sendEvent(new Event(EC.EventCode.FINISH_GUIDE_ACTIVITY));
                break;
        }
    }

    private void gotoNextActivity() {
        if (TextUtils.isEmpty(guideOneCityTv.getText())) {
            //提示获取城市定位
            Toast.makeText(mContext, "请选择您所在的城市", Toast.LENGTH_SHORT).show();
        } else {
            //进入下一个页面 选择年龄
            startActivity(new Intent(mContext, GuideTwoActivity.class));
        }
    }
}
