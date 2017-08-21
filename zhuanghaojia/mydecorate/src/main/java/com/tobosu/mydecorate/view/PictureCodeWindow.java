package com.tobosu.mydecorate.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tobosu.mydecorate.global.Constant;

import java.util.HashMap;

/**
 * Created by Lie on 2017/8/17.
 */

public class PictureCodeWindow extends PopupWindow {

    private View mView;
    private ImageView iv_imageverif;
    private TextView tv_another;
    private TextView tv_cancel;
    private TextView tv_ensure;
    public EditText et_input;
    private HashMap<String, Object> headerMap = new HashMap<String, Object>();
    private String urlBase = Constant.ZHJ + "tapp/passport/get_pic_code?version=";
    private String header;
    private LinearLayout windowLayout;

    private Handler imageVerifHandler = new Handler();
    private Context mContext;
    public String phone = null;
    private String imageVerif;
    public String version = null;

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24){
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }
}
