package com.tbs.tobosutype.activity;

import android.content.Intent;
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
 * Created by Mr.Wang on 2019/1/3 09:36.
 * 地砖计算
 */
public class FloorTileCalculationActivity extends BaseActivity {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tv_edit)
    ImageView tvEdit;
    @BindView(R.id.edit_room_long)
    DecorationToolCalculationItem editRoomLong;
    @BindView(R.id.edit_room_width)
    DecorationToolCalculationItem editRoomWidth;
    @BindView(R.id.edit_brick_long)
    DecorationToolCalculationItem editBrickLong;
    @BindView(R.id.edit_brick_thickness)
    DecorationToolCalculationItem editBrickThickness;
    @BindView(R.id.edit_price)
    DecorationToolCalculationItem editPrice;
    @BindView(R.id.btn_start_calculation)
    Button btnStartCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_tile_calculation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rlBack, R.id.tv_edit, R.id.btn_start_calculation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
                finish();
                break;
            case R.id.tv_edit:  //历史记录
                break;
            case R.id.btn_start_calculation:    //开始计算
                if (editRoomLong.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房间长度");
                    return;
                }
                if (editRoomWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房间宽度");
                    return;
                }
                if (editBrickLong.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入地砖宽度");
                    return;
                }
                if (editBrickThickness.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入地砖厚度");
                    return;
                }

                startActivity(new Intent(FloorTileCalculationActivity.this, DecorationCalculationResultActivity.class));
                break;
        }
    }
}
