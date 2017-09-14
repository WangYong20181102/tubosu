package com.tbs.tobosupicture.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tbs.tobosupicture.R;

/**
 * Created by Mr.Lin on 2017/8/7 14:47.
 */

public class SetSexPopWindow extends PopupWindow {
    private String TAG = "SetSexPopWindow";
    private View mView;
    private TextView menBtn;//选择男性的按钮
    private TextView womenBtn;//选择女性的按钮
    private TextView dismiss;//取消按钮
    private LinearLayout pop_set_man_ll;
    private LinearLayout pop_set_women_ll;
    private LinearLayout pop_dismiss_ll;

    public SetSexPopWindow(Context context, View.OnClickListener onClickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.set_sex_popw, null);
        menBtn = (TextView) mView.findViewById(R.id.pop_set_man);
        womenBtn = (TextView) mView.findViewById(R.id.pop_set_women);
        pop_set_man_ll = (LinearLayout) mView.findViewById(R.id.pop_set_man_ll);
        pop_set_women_ll = (LinearLayout) mView.findViewById(R.id.pop_set_women_ll);
        pop_dismiss_ll = (LinearLayout) mView.findViewById(R.id.pop_dismiss_ll);

        dismiss = (TextView) mView.findViewById(R.id.pop_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pop_dismiss_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        menBtn.setOnClickListener(onClickListener);
        womenBtn.setOnClickListener(onClickListener);
        pop_set_man_ll.setOnClickListener(onClickListener);
        pop_set_women_ll.setOnClickListener(onClickListener);
        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        mView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.pop_sex_ll).getTop();
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
