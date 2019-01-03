package com.tbs.tobosutype.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.widget.DecorationToolCalculationItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2019/1/3 09:43.
 * 窗帘计算
 */
public class CurtainCalculationActivity extends BaseActivity {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tv_edit)
    ImageView tvEdit;
    @BindView(R.id.edit_window_height)
    DecorationToolCalculationItem editWindowHeight;
    @BindView(R.id.edit_window_width)
    DecorationToolCalculationItem editWindowWidth;
    @BindView(R.id.edit_curtain_width)
    DecorationToolCalculationItem editCurtainWidth;
    @BindView(R.id.edit_price)
    DecorationToolCalculationItem editPrice;
    @BindView(R.id.btn_start_calculation)
    Button btnStartCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain_calculation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rlBack, R.id.tv_edit, R.id.btn_start_calculation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
                finish();
                break;
            case R.id.tv_edit:
                break;
            case R.id.btn_start_calculation:
                if (editWindowHeight.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户高度");
                    return;
                }
                if (editWindowWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户宽度");
                    return;
                }
                if (editCurtainWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入布料宽度");
                    return;
                }
                break;
        }
    }
}
