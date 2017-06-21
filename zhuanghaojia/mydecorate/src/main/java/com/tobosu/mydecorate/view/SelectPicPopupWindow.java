package com.tobosu.mydecorate.view;

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

import com.tobosu.mydecorate.R;

/**
 * Created by Mr.Lin on 2017/5/22 14:02.
 */
@SuppressLint("ViewConstructor")
public class SelectPicPopupWindow extends PopupWindow {
    private TextView image_detail_share_weixin_circle,
            image_detail_share_weixin, image_detail_share_sina,
            image_detail_share_qq;
    private Button image_detail_share_cancel;
    private View mMenuView;

    @SuppressLint("InflateParams")
    public SelectPicPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.activity_share, null);
        image_detail_share_weixin_circle = (TextView) mMenuView
                .findViewById(R.id.image_detail_share_weixin_circle);
        image_detail_share_weixin = (TextView) mMenuView
                .findViewById(R.id.image_detail_share_weixin);
        image_detail_share_sina = (TextView) mMenuView
                .findViewById(R.id.image_detail_share_sina);
        image_detail_share_qq = (TextView) mMenuView
                .findViewById(R.id.image_detail_share_qq);
        image_detail_share_cancel = (Button) mMenuView
                .findViewById(R.id.image_detail_share_cancel);
        image_detail_share_weixin_circle.setOnClickListener(itemsOnClick);
        image_detail_share_weixin.setOnClickListener(itemsOnClick);
        image_detail_share_sina.setOnClickListener(itemsOnClick);
        image_detail_share_qq.setOnClickListener(itemsOnClick);
        image_detail_share_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        this.setBackgroundDrawable(dw);
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
