package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.tobosu.mydecorate.global.Constant.TRANSMIT_TO_LOGIN_REQUESTCODE;

/**
 * Created by dec on 2016/10/10.
 * 过渡Activity
 */

public class TransitActivity extends AppCompatActivity {
    private static final String TAG = TransitActivity.class.getSimpleName();
    private Context mContext;

    private RelativeLayout rel_look_first;

    private RelativeLayout rel_weixin_login;

    private TextView tv_phone_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transmit);
        mContext = TransitActivity.this;

        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        weixinConfig();
        initView();
    }

    private void initView() {
        rel_look_first = (RelativeLayout) findViewById(R.id.rel_look_first);
        rel_look_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "click_load_leading_kankan");
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        });
        tv_phone_login = (TextView) findViewById(R.id.tv_phone_login);
        tv_phone_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext,"click_load_leading_other_way");
                startActivityForResult(new Intent(mContext, LoginActivity.class), TRANSMIT_TO_LOGIN_REQUESTCODE);
            }
        });
        rel_weixin_login = (RelativeLayout) findViewById(R.id.rel_weixin_login);
        rel_weixin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 微信登录
                do_weixin_login();
            }
        });
    }

    // 友盟分享的服务
    private com.umeng.socialize.controller.UMSocialService controller = null;
    private String weixin_login_url = Constant.ZHJ + "tapp/passport/login_third_party";
    private void do_weixin_login(){
        MobclickAgent.onEvent(mContext, "click_load_leading_weixin");

        controller.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA platform) {
                Toast.makeText(mContext, "正在加载中...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                // 获取uid
                String uid = value.getString("uid");
                System.out.println("============>>> uid是" + uid);
                // uid不为空，说明授权成功
                if (!TextUtils.isEmpty(uid)) {
                    // 记录用的是哪个平台的授权，退出时要用到
                    getUserInfo(platform);
                } else {
                    Toast.makeText(mContext, "授权失败...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 获取用户信息
     *
     * @param platform
     */
    private void getUserInfo(SHARE_MEDIA platform) {
        controller.getPlatformInfo(mContext, platform, new SocializeListeners.UMDataListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {

                Log.d("----LoginActivity--->>", info.toString());
                if(info!=null){
                    String head_url = info.get("headimgurl").toString();
                    String nickname = info.get("nickname").toString();
                    String weixin_id = info.get("unionid").toString();
                    getWeixinInfoLogin(head_url, nickname, weixin_id);

                    getWeixinInfoLogin(head_url, nickname, weixin_id);
                }
//                parseInfo(info.toString());

            }
        });
    }

    private void parseInfo(String infoString){
        String head_url = "";
        String nickname = "";
        String weixin_id = "";

        String temp = infoString.substring(1,infoString.length()-1);
        String[] arr = temp.split(",");

        for(int i=0,len=arr.length; i<len; i++){
            String [] textArr = arr[i].split("=");
            for(int j=0, textLen=textArr.length;j<textLen;j++){
                if(textArr[0].equals("unionid")){
                    weixin_id = textArr[1];
                }else if(textArr[0].equals("nickname")){
                    nickname = textArr[1];
                } else if(textArr[0].equals("headimgurl")){
                    head_url = textArr[1];
                }
            }

        }
        getWeixinInfoLogin(head_url, nickname, weixin_id);
    }

    private void getWeixinInfoLogin(String head_url, String nick, String id){
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("kind", "weixin");
        params.put("icon", head_url);
        params.put("nickname", nick);
        params.put("account", id);
        okHttpUtil.post(weixin_login_url, params, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(com.squareup.okhttp.Response response, String json) {
                System.out.println("--登录成功json-- " +json);

                String headPicUrl = "";
                String userName = "";
                String mark ="";
                String token = "";
                String userid = "";
                String cityname = "";

                try {
                    JSONObject object = new JSONObject(json);
                    if(object.getInt("error_code")==0){
                        MobclickAgent.onEvent(mContext,"click_load_leading_weixin_succeed");
                        //微信登录成功
                        JSONObject data = object.getJSONObject("data");
                        headPicUrl = data.getString("icon");
                        userName = data.getString("name");
                        mark = data.getString("mark");
                        token = data.getString("token");
                        userid = data.getString("uid");
                        cityname = data.getString("cityname");

                        SharedPreferences saveInfo = getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = saveInfo.edit();
                        editor.putString("user_name", userName);
                        editor.putString("head_pic_url", headPicUrl);
                        editor.putString("mark", mark);
                        editor.putString("user_id", userid);
                        editor.putString("token", token);
                        editor.putString("city_name", cityname);
                        editor.commit();

//                        setResult(Constant.WEIXIN_LOGIN_RESULTCODE);
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
//                        Intent it = new Intent(Constant.WEIXIN_LOGIN_ACTION);
//                        sendBroadcast(it);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(com.squareup.okhttp.Request request, IOException e) {

            }

            @Override
            public void onError(com.squareup.okhttp.Response response, int code) {

            }
        });
    }

    private void weixinConfig(){
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        switch (resultCode){
            case Constant.LOGIN_RESULTCODE:
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
                break;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
