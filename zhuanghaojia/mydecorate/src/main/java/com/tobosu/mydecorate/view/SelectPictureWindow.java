package com.tobosu.mydecorate.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.tobosu.mydecorate.R;

/**
 * Created by dec on 2016/9/27.
 * 从本地选择图片
 */

public class SelectPictureWindow extends PopupWindow {
    private static final String TAG = SelectPictureWindow.class.getSimpleName();

    private Button btnTakePhoto;
    private Button btnPickPhoto;
    private Button btnCancel;

    private View view;

    public SelectPictureWindow(Context mContext, View.OnClickListener buttonClickListener){
        super(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_select_photo,null);
        btnTakePhoto = (Button) view.findViewById(R.id.btn_takePhoto);
        btnPickPhoto = (Button) view.findViewById(R.id.btn_pickPhoto);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnTakePhoto.setOnClickListener(buttonClickListener);
        btnPickPhoto.setOnClickListener(buttonClickListener);
        btnCancel.setOnClickListener(buttonClickListener);
        this.setContentView(view);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);

        ColorDrawable dw = new ColorDrawable(0x80000000);
        this.setBackgroundDrawable(dw);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.select_photo_layout).getTop();
                int touchHeight = (int) event.getY();
                if (height>touchHeight && event.getAction()==MotionEvent.ACTION_UP) {
                    dismiss();
                }
                return true;
            }
        });
    }

}
