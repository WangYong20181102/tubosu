package com.tobosu.mydecorate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

/**
 * Created by Mr.Lin on 2017/6/8 15:02.
 */

public class SetSexPopWindow extends PopupWindow {
    private String TAG = "SetSexPopWindow";
    private View mView;
    private TextView menBtn;//选择男性的按钮
    private TextView womenBtn;//选择女性的按钮
    private TextView dismiss;//取消按钮

    public SetSexPopWindow(Context context, View.OnClickListener onClickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.set_sex_popw, null);
        menBtn = (TextView) mView.findViewById(R.id.pop_set_man);
        womenBtn = (TextView) mView.findViewById(R.id.pop_set_women);
        dismiss = (TextView) mView.findViewById(R.id.pop_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        menBtn.setOnClickListener(onClickListener);
        womenBtn.setOnClickListener(onClickListener);
        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
    }
}
