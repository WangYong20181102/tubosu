package com.tbs.tobosutype.activity;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 手机绑定成功页面
 *
 * @author dec
 */
public class BundleSuccessActivity extends Activity {
    private ImageView bundlesuccess_back;
    private RelativeLayout rl_banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_bundlesuccess);

        bundlesuccess_back = (ImageView) findViewById(R.id.bundlesuccess_back);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
        bundlesuccess_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
}
