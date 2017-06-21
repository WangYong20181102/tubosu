package com.tobosu.mydecorate.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

/**
 * Created by dec on 2016/10/28.
 */

public class ShareViewPopupWindow extends PopupWindow{

    private TextView tv_share_weixin;
    private TextView tv_share_weixin_circle;
    private TextView tv_share_qq_space;
    private TextView tv_share_sina;
    private TextView tv_copy_linck;

    private View mMenuView;



    @SuppressLint("InflateParams")
    public ShareViewPopupWindow(Context context, View.OnClickListener itemOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_share_layout, null);
        tv_share_weixin = (TextView) mMenuView.findViewById(R.id.tv_share_weixin);
        tv_share_weixin_circle = (TextView) mMenuView.findViewById(R.id.tv_share_weixin_circle);
        tv_share_qq_space = (TextView) mMenuView.findViewById(R.id.tv_share_qq_space);
        tv_share_sina = (TextView) mMenuView.findViewById(R.id.tv_share_sina);
        tv_copy_linck = (TextView) mMenuView.findViewById(R.id.tv_copy_linck);

        tv_share_weixin.setOnClickListener(itemOnClick);
        tv_share_weixin_circle.setOnClickListener(itemOnClick);
        tv_share_qq_space.setOnClickListener(itemOnClick);
        tv_share_sina.setOnClickListener(itemOnClick);
        tv_copy_linck.setOnClickListener(itemOnClick);


        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
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
