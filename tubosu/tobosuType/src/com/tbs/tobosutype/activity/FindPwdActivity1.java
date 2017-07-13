package com.tbs.tobosutype.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.HintInput;
import com.tbs.tobosutype.adapter.utils.ToastUtil;

public class FindPwdActivity1 extends Activity implements OnClickListener {
    private Context mContext;
    private ImageView findpwd_one_back;

    /**
     * 输入电话号码
     */
    private EditText findpwd_one_inputbundlephone;

    /**
     * 下一步按钮
     */
    private Button findpwd_one_nextstep;

    /**
     * 输入电话号码
     */
    private String inputbundlephone;
    //顶部banner
    private RelativeLayout rl_banner;

//	/**输入验证码框*/
//	private EditText findpwd_inputverif1;

//	/**获取验证码按钮*/
//	private Button findpwd_obtain;


    /**
     * 获取验证码的接口
     */
    private String urlBase = Constant.TOBOSU_URL + "tapp/passport/get_pic_code?version=";

    private static int count = 60;


//	private Handler findPwdHandler = new Handler() {
//
//		// 在Handler中获取消息，重写handleMessage()方法
//		@Override
//		public void handleMessage(Message msg) {
//			if (msg.what == 1) {
//				JSONObject resultObject = (JSONObject) msg.obj;
//				try {
//					int result = (Integer) resultObject.get("error_code");
//					String msgData = resultObject.getString("msg");
//					if (result == 0) {
//						startCount();
//						Toast.makeText(FindPwdActivity1.this, msgData, Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(FindPwdActivity1.this, LoginActivity.class);
//						finish();
//						startActivity(intent);
//					} else {
//						Toast.makeText(FindPwdActivity1.this, msgData, Toast.LENGTH_SHORT).show();
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme1(this, R.color.whole_color_theme);
        setContentView(R.layout.activity_findpwd_one);
        AppInfoUtil.setTranslucentStatus(this);

        mContext = FindPwdActivity1.this;

        findpwd_one_back = (ImageView) findViewById(R.id.findpwd_one_back);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
        findpwd_one_inputbundlephone = (EditText) findViewById(R.id.findpwd_one_inputbundlephone);
        new HintInput(11, findpwd_one_inputbundlephone, this);

//		findpwd_inputverif1 = (EditText) findViewById(R.id.findpwd_inputverif1);
//		new HintInput(11, findpwd_inputverif1, this);


        findpwd_one_nextstep = (Button) findViewById(R.id.findpwd_one_nextstep);
//		findpwd_obtain = (Button) findViewById(R.id.findpwd_obtain1);


        findpwd_one_back.setOnClickListener(this);
        findpwd_one_nextstep.setOnClickListener(this);
//		findpwd_obtain.setOnClickListener(this);
    }

    /***
     * 判断电话号码是否合法
     * @param phoneNum
     * @return
     */
    public boolean judgePhone(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtil.showShort(mContext, getResources().getString(R.string.findpwd_toast));
            return false;
        } else {
            String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
            Pattern pattern = Pattern.compile(MOBILE);
            Matcher matcher = pattern.matcher(phoneNum);
            boolean flag = matcher.matches();
            if (!flag) {
                ToastUtil.showShort(mContext, "请输入合法电话号码");
            }
            return matcher.matches();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findpwd_one_back:
                finish();
                break;
            case R.id.findpwd_one_nextstep:
                inputbundlephone = findpwd_one_inputbundlephone.getText().toString().trim();
                if (judgePhone(inputbundlephone)) {
                    // 电话号码合法

                    // 获取验证码 提交


                    Intent oneIntent = new Intent(mContext, FindPwdActivity2.class);
                    Bundle oneBundle = new Bundle();
                    oneBundle.putString("mobile", inputbundlephone);
                    oneIntent.putExtras(oneBundle);
                    startActivityForResult(oneIntent, 12);
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                finish();
                break;
            default:
                break;
        }
    }


}
