package com.tbs.tobosupicture.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tbs.tobosupicture.R;

/**
 * Created by Mr.Lin on 2017/8/8 14:40.
 */
@SuppressLint("ViewConstructor")
public class SelectPersonalPopupWindow extends PopupWindow {
    private TextView takePhotoBtn, pickPhotoBtn, cancelBtn;
    private View mMenuView;

    @SuppressLint("InflateParams")
    public SelectPersonalPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_setting_image, null);
        takePhotoBtn = (TextView) mMenuView.findViewById(R.id.takePhotoBtn);
        pickPhotoBtn = (TextView) mMenuView.findViewById(R.id.pickPhotoBtn);
        cancelBtn = (TextView) mMenuView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(itemsOnClick);
        pickPhotoBtn.setOnClickListener(itemsOnClick);
        takePhotoBtn.setOnClickListener(itemsOnClick);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
//        ColorDrawable dw = new ColorDrawable(0x80000000);
//        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.custom_popupwindow_animstyle);
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
