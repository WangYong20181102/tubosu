package com.tobosu.mydecorate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

/**
 * Created by Mr.Lin on 2017/6/8 16:13.
 */

public class SetZhuangxiuPopWindow extends PopupWindow {
    private String TAG = "SetSexPopWindow";
    private View mView;
    private TextView zhuangxiuzhunbeiBtn;//准备装修
    private TextView zhuangxiuqianBtn;//选择装修前
    private TextView zhuangxiuzhongBtn;//选择装修中
    private TextView zhuangxiuhouBtn;//选择装修后
    private TextView zhuangxiuruzhuBtn;//装修入住
    private TextView dismiss;//装修后

    public SetZhuangxiuPopWindow(Context context, View.OnClickListener onClickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.set_jieduan_popw, null);
        zhuangxiuzhunbeiBtn = (TextView) mView.findViewById(R.id.pop_set_zhuangxiuzhunbei);
        zhuangxiuqianBtn = (TextView) mView.findViewById(R.id.pop_set_zhuangxiuqian);
        zhuangxiuzhongBtn = (TextView) mView.findViewById(R.id.pop_set_zhuangxiuzhong);
        zhuangxiuhouBtn = (TextView) mView.findViewById(R.id.pop_set_zhuangxiuhou);
        zhuangxiuruzhuBtn = (TextView) mView.findViewById(R.id.pop_set_zhuangxiuruzhu);
        dismiss = (TextView) mView.findViewById(R.id.pop_zhuangxiu_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        zhuangxiuzhunbeiBtn.setOnClickListener(onClickListener);
        zhuangxiuqianBtn.setOnClickListener(onClickListener);
        zhuangxiuzhongBtn.setOnClickListener(onClickListener);
        zhuangxiuhouBtn.setOnClickListener(onClickListener);
        zhuangxiuruzhuBtn.setOnClickListener(onClickListener);
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
