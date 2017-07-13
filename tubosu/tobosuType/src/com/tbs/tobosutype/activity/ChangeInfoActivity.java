package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;

/**
 * 业主修改信息页面
 *
 * @author dec
 */
public class ChangeInfoActivity extends Activity implements OnClickListener {
    private Context mContext;
    private RelativeLayout re_banner;
    private TextView tv_change_cancel;
    private TextView tv_change_title;
    private TextView tv_change_save;
    private EditText et_change_info;
    private String title;

    private String textString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_change_info);
        mContext = ChangeInfoActivity.this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        re_banner = (RelativeLayout) findViewById(R.id.re_banner);
        tv_change_cancel = (TextView) findViewById(R.id.tv_change_cancel);
        tv_change_title = (TextView) findViewById(R.id.tv_change_title);
        tv_change_save = (TextView) findViewById(R.id.tv_change_save);
        et_change_info = (EditText) findViewById(R.id.et_change_info);
        re_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        title = getIntent().getStringExtra("title");
        textString = getIntent().getStringExtra("textString");
        tv_change_title.setText(title);
    }

    private void initEvent() {
        tv_change_cancel.setOnClickListener(this);
        tv_change_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_cancel:
                finish();
                break;
            case R.id.tv_change_save:
                operChangeInfo();
                break;
            default:
                break;
        }
    }


    private void operChangeInfo() {
        String info = et_change_info.getText().toString().trim();
        if (TextUtils.isEmpty(info.trim())) {
            Toast.makeText(mContext, "修改信息不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (!"".equals(textString) && textString.equals(info)) {
            Toast.makeText(mContext, "您还没修改信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getIntent();
        intent.putExtra("result", info);
        this.setResult(0, intent);
        finish();
    }

}
