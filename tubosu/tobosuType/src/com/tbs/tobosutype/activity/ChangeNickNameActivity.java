package com.tbs.tobosutype.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.utils.EventBusUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改用户的昵称
 * 3.7版本新增
 * creat by lin
 */
public class ChangeNickNameActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.change_nick_dissmiss_rl)
    RelativeLayout changeNickDissmissRl;
    @BindView(R.id.change_nick_ok_rl)
    RelativeLayout changeNickOkRl;
    @BindView(R.id.change_nick_edit)
    EditText changeNickEdit;
    @BindView(R.id.change_nick_edit_clean)
    RelativeLayout changeNickEditClean;
    @BindView(R.id.banner_dever)
    View bannerDever;
    private Context mContext;
    private String TAG = "ChangeNickNameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick_name);
        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @OnClick({R.id.change_nick_dissmiss_rl, R.id.change_nick_ok_rl, R.id.change_nick_edit_clean})
    public void onViewClickedChangeNickNameActivity(View view) {
        switch (view.getId()) {
            case R.id.change_nick_dissmiss_rl:
                finish();
                break;
            case R.id.change_nick_ok_rl:
                //修改昵称确认键
                if (!TextUtils.isEmpty(changeNickEdit.getText().toString())) {
                    //输入的昵称不能为空
                    EventBusUtil.sendEvent(new Event(EC.EventCode.CHANGE_NICK_NAME, changeNickEdit.getText().toString()));
                    finish();
                } else {
                    Toast.makeText(mContext, "昵称不能为空~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.change_nick_edit_clean:
                changeNickEdit.setText("");
                break;
        }
    }
}
