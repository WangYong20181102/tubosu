package com.tbs.tobosutype.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.CalculationResultsBean;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.MoneyFormatUtil;
import com.tbs.tobosutype.utils.SpUtil;

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
    @BindView(R.id.linear_price)
    LinearLayout linearPrice;
    @BindView(R.id.btn_renew_calculation)
    Button btnRenewCalculation;
    @BindView(R.id.image_bottom_ad)
    ImageView imageBottomAd;
    private String total_price; //价格
    private String number;  //数量
    private int type;       //1地砖 2 墙砖  3 地板  4 壁纸 5 涂料 6 窗帘
    private CalculationResultsBean resultsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration_calculation_result);
        ButterKnife.bind(this);
        initIntent();
        initData();
    }

    /**
     * 初始化Intent数据
     */
    private void initIntent() {
        resultsBean = (CalculationResultsBean) getIntent().getSerializableExtra(CalculationResultsBean.class.getName());
        total_price = resultsBean.getTotal_price();
        number = resultsBean.getNumber();
        type = getIntent().getIntExtra("type", 0);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (!TextUtils.isEmpty(SpUtil.getTenDecorationPrice(mContext))) {
            if (SpUtil.getTenDecorationPrice(mContext).contains(".gif")) {
                Glide.with(mContext)
                        .load(SpUtil.getTenDecorationPrice(mContext))
                        .asGif()
                        .into(imageBottomAd);
            } else {
                Glide.with(mContext)
                        .load(SpUtil.getTenDecorationPrice(mContext))
                        .into(imageBottomAd);
            }
        }
        if (total_price.trim().isEmpty() || total_price.trim().equals("0")) {
            linearPrice.setVisibility(View.GONE);
        } else {
            linearPrice.setVisibility(View.VISIBLE);
            if (Double.parseDouble(total_price) > 10000) {
                textResultPriceNum.setText(MoneyFormatUtil.format2(Double.parseDouble(total_price) / 10000));
                textResultPriceNumUnit.setText("万元");
            } else {
                textResultPriceNum.setText(MoneyFormatUtil.format2(total_price));
                textResultPriceNumUnit.setText("元");
            }
        }
        if (type == 1) { //地砖
            setTextContent("地砖", "块");
        } else if (type == 2) {//墙砖
            setTextContent("墙砖", "块");
        } else if (type == 3) { //地板
            setTextContent("地板", "块");
        } else if (type == 4) { //壁纸
            setTextContent("壁纸", "卷");
        } else if (type == 5) { //涂料
            setTextContent("涂料", "升");
        } else if (type == 6) { //窗帘
            setTextContent("窗帘", "米");
        }


    }

    /**
     * 数量、价格、单位
     */
    @SuppressLint("SetTextI18n")
    private void setTextContent(String sType, String sUnit) {
        //标题
        textResultTittle.setText(sType + "计算结果");

        textResultNumTittle.setText(sType + "的数量为");
        textResultNum.setText(number);
        textResultPriceTittle.setText(sType + "的价格为");
        textResultNumUnit.setText(sUnit);
    }

    @OnClick({R.id.rlBack, R.id.btn_renew_calculation, R.id.image_bottom_ad})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
            case R.id.btn_renew_calculation:    //重新计算返回上一个界面
                finish();
                break;
            case R.id.image_bottom_ad:
                Intent mIntent = new Intent(mContext, NewWebViewActivity.class);
                mIntent.putExtra("mLoadingUrl", SpUtil.getTbsAj44(mContext));
                mContext.startActivity(mIntent);
                break;
        }
    }
}
