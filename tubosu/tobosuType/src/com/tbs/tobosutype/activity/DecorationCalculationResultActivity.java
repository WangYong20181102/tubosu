package com.tbs.tobosutype.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2019/1/3 15:09.
 * 装修计算结果
 */
public class DecorationCalculationResultActivity extends BaseActivity {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.text_result_tittle)
    TextView textResultTittle;
    @BindView(R.id.rl_top_title)
    RelativeLayout rlTopTitle;
    @BindView(R.id.text_result_num_tittle)
    TextView textResultNumTittle;
    @BindView(R.id.text_result_num)
    TextView textResultNum;
    @BindView(R.id.text_result_num_unit)
    TextView textResultNumUnit;
    @BindView(R.id.text_result_price_tittle)
    TextView textResultPriceTittle;
    @BindView(R.id.text_result_price_num)
    TextView textResultPriceNum;
    @BindView(R.id.text_result_price_num_unit)
    TextView textResultPriceNumUnit;
    @BindView(R.id.linear_two)
    LinearLayout linearTwo;
    @BindView(R.id.text_result_num_tittle1)
    TextView textResultNumTittle1;
    @BindView(R.id.text_result_num1)
    TextView textResultNum1;
    @BindView(R.id.text_result_num_unit1)
    TextView textResultNumUnit1;
    @BindView(R.id.linear_one)
    LinearLayout linearOne;
    @BindView(R.id.btn_renew_calculation)
    Button btnRenewCalculation;
    @BindView(R.id.image_bottom_ad)
    ImageView imageBottomAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration_calculation_result);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rlBack, R.id.btn_renew_calculation, R.id.image_bottom_ad})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
                finish();
                break;
            case R.id.btn_renew_calculation:    //重新计算返回上一个界面
                finish();
                break;
            case R.id.image_bottom_ad:
                break;
        }
    }
}
