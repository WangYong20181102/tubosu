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
 * Created by Mr.Wang on 2019/1/3 09:41.
 * 壁纸计算
 */
public class WallpaperCalculationActivity extends BaseActivity {
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
    @BindView(R.id.edit_wallpaper_specification)
    DecorationToolCalculationItem editWallpaperSpecification;
    @BindView(R.id.edit_price)
    DecorationToolCalculationItem editPrice;
    @BindView(R.id.btn_start_calculation)
    Button btnStartCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_calculation);
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
                if (editWallpaperSpecification.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入壁纸规格");
                    return;
                }
                break;
        }
    }
}
