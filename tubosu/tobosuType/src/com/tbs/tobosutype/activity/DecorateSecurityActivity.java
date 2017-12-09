package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.AppInfoUtil;

/**
 * 免费设计与报价
 *
 * @author dec
 */
public class DecorateSecurityActivity extends Activity implements OnClickListener {
    private Context mContext;
    private TextView title_name;
    private ImageView iv_back;
    private Button bt_want_decorate;
    private Button bt_cell;
    private ImageView ivShare;
    private RelativeLayout headfree_banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_decorate_security);
        mContext = DecorateSecurityActivity.this;

        initView();
        initEvent();
    }

    private void initView() {
        headfree_banner = (RelativeLayout) findViewById(R.id.headfree_banner);
        title_name = (TextView) findViewById(R.id.title_name);
        iv_back = (ImageView) findViewById(R.id.free_activity_back);
//        bt_want_decorate = (Button) findViewById(R.id.bt_want_decorate);
        bt_cell = (Button) findViewById(R.id.bt_cell);
        title_name.setText("装修保障");
        ivShare = (ImageView) findViewById(R.id.head_right_image_share);
        ivShare.setVisibility(View.INVISIBLE);
        headfree_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initEvent() {
        iv_back.setOnClickListener(this);
//        bt_want_decorate.setOnClickListener(this);
        bt_cell.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.free_activity_back:
                finish();
                break;
//            case R.id.bt_want_decorate:
//                startActivity(new Intent(this, FreeYuyueActivity.class));
//                break;
            case R.id.bt_cell:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "400-606-2221");
                intent.setData(data);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
