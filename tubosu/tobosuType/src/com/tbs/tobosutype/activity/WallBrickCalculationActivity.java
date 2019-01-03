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
 * Created by Mr.Wang on 2019/1/3 09:38.
 * 墙砖计算
 */
public class WallBrickCalculationActivity extends BaseActivity {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tv_edit)
    ImageView tvEdit;
    @BindView(R.id.edit_room_long)
    DecorationToolCalculationItem editRoomLong;
    @BindView(R.id.edit_room_width)
    DecorationToolCalculationItem editRoomWidth;
    @BindView(R.id.edit_room_height)
    DecorationToolCalculationItem editRoomHeight;
    @BindView(R.id.edit_room_door_height)
    DecorationToolCalculationItem editRoomDoorHeight;
    @BindView(R.id.edit_room_door_width)
    DecorationToolCalculationItem editRoomDoorWidth;
    @BindView(R.id.edit_room_door_num)
    DecorationToolCalculationItem editRoomDoorNum;
    @BindView(R.id.edit_window_height)
    DecorationToolCalculationItem editWindowHeight;
    @BindView(R.id.edit_window_width)
    DecorationToolCalculationItem editWindowWidth;
    @BindView(R.id.edit_window_num)
    DecorationToolCalculationItem editWindowNum;
    @BindView(R.id.edit_wall_brick_long)
    DecorationToolCalculationItem editWallBrickLong;
    @BindView(R.id.edit_wall_brick_width)
    DecorationToolCalculationItem editWallBrickWidth;
    @BindView(R.id.edit_price)
    DecorationToolCalculationItem editPrice;
    @BindView(R.id.btn_start_calculation)
    Button btnStartCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_brick_calculation);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        editRoomDoorNum.setNumInputTypeInt(4);
        editWindowNum.setNumInputTypeInt(4);

    }

    @OnClick({R.id.rlBack, R.id.tv_edit, R.id.btn_start_calculation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
                finish();
                break;
            case R.id.tv_edit://历史记录
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
                if (editRoomHeight.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房间高度");
                    return;
                }
                if (editRoomDoorHeight.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房门长度");
                    return;
                }
                if (editRoomDoorWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房门宽度");
                    return;
                }
                if (editRoomDoorNum.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房门数量");
                    return;
                }
                if (editWindowHeight.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户高度");
                    return;
                }
                if (editWindowWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户宽度");
                    return;
                }
                if (editWindowNum.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户数量");
                    return;
                }
                if (editWallBrickLong.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入墙砖长度");
                    return;
                }
                if (editWallBrickWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入墙砖宽度");
                    return;
                }

                break;
        }
    }
}
