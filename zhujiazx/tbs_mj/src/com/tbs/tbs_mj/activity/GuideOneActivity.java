package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideOneActivity extends BaseActivity {
    @BindView(R.id.guide_one_city_tv)
    TextView guideOneCityTv;
    @BindView(R.id.guide_one_next_step_tv)
    TextView guideOneNextStepTv;
    //引导发单页第一页 确定城市的页面
    private String TAG = "GuideOneActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_one);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.guide_one_city_tv, R.id.guide_one_next_step_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guide_one_city_tv:
                //进入城市选择页面
                break;
            case R.id.guide_one_next_step_tv:
                //进入下一步
                break;
        }
    }
}
