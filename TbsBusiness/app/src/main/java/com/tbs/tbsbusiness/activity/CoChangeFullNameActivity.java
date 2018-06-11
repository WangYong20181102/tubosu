package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.util.EventBusUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoChangeFullNameActivity extends BaseActivity {
    @BindView(R.id.co_change_fullname_dissmiss_rl)
    RelativeLayout coChangeFullnameDissmissRl;
    @BindView(R.id.co_change_fullname_ok_rl)
    RelativeLayout coChangeFullnameOkRl;
    @BindView(R.id.co_change_fullname_edit)
    EditText coChangeFullnameEdit;
    @BindView(R.id.co_change_fullname_edit_clean)
    RelativeLayout coChangeFullnameEditClean;
    @BindView(R.id.co_chage_fullname_sign)
    TextView coChageFullnameSign;
    private String TAG = "CoChangeFullNameActivity";
    private Context mContext;
    private Intent mIntent;
    private String mFullName = "";//上个界面带来的名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_change_full_name);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initEvent() {
        mIntent = getIntent();
        mFullName = mIntent.getStringExtra("mFullName");//获取全称
        coChangeFullnameEdit.addTextChangedListener(textWatcher);
        coChangeFullnameEdit.setText("" + mFullName);//设置全称
        coChangeFullnameEdit.setSelection(coChangeFullnameEdit.getText().toString().length());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                //名称被清除 显示提示语
//                coChageFullnameSign.setVisibility(View.VISIBLE);
                Toast.makeText(mContext, "请输入公司全称！", Toast.LENGTH_SHORT).show();
            } else {
//                coChageFullnameSign.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick({R.id.co_change_fullname_dissmiss_rl, R.id.co_change_fullname_ok_rl, R.id.co_change_fullname_edit_clean})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_fullname_dissmiss_rl:
                //取消按钮
                finish();
                break;
            case R.id.co_change_fullname_ok_rl:
                //确定按钮
                if (!TextUtils.isEmpty(coChangeFullnameEdit.getText().toString())
                        && coChangeFullnameEdit.getText().toString().length() >= 2) {
                    changeNameOk(coChangeFullnameEdit.getText().toString());
                }
                break;
            case R.id.co_change_fullname_edit_clean:
                //清除名称
                coChangeFullnameEdit.setText("");
                break;
        }
    }

    //点击确定按钮  确认修改名称
    private void changeNameOk(String fullName) {
        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_FULL_NAME, fullName));
        finish();
    }
}
