package com.tobosu.mydecorate.wxapi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class WXEntryActivity1 extends Activity implements IWXAPIEventHandler {

    /*----------------- 微信登录相关 ----------------*/
    private String weixin_login_url = Constant.ZHJ + "tapp/passport/login_third_party";


    /*----------------- 微信登录相关 ----------------*/

    private IWXAPI api;  // IWXAPI 是第三方app和微信通信的openapi接口

    private String access_token = "";
    private String openid = "";
    private String unionid = "";

    private String nickName = "";
    private String icon_url = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID, true);
        api.registerApp(Constant.WEIXIN_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";


        api.sendReq(req);//执行完毕这句话之后，会在WXEntryActivity回调

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                finish();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                finish();
                break;
            default:
                break;
        }
    }


    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                // 成功
                if (resp instanceof SendAuth.Resp) {
                    SendAuth.Resp newResp = (SendAuth.Resp) resp;
                    //获取微信传回的code
                    String code = newResp.code;
                    getWXData(code);
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                System.out.println("-------------b");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                System.out.println("-------------c");
                break;
            default:
                System.out.println("-------------d");
                break;
        }
        finish();
    }


    private void getWXData(String code) {
        final OKHttpUtil okHttpUtil = new OKHttpUtil();

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.WEIXIN_APP_ID + "&secret=" + Constant.WEIXIN_APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
        okHttpUtil.get(url, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject obj = new JSONObject(json);
                    access_token = obj.getString("access_token");
                    openid = obj.getString("openid");

                    getToken(okHttpUtil, access_token, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });

    }


    private void getToken(OKHttpUtil okHttpUtil, String access_token, String openid) {
        String url_string = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        okHttpUtil.get(url_string, new OKHttpUtil.BaseCallBack() {

            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    nickName = object.getString("nickname");
                    icon_url = object.getString("headimgurl");
                    unionid = object.getString("unionid");

                    weixinLogin(icon_url, nickName, unionid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }


    private void weixinLogin(String head_url, String nick, String id) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("kind", "weixin");
        params.put("icon", head_url);
        params.put("nickname", nick);
        params.put("account", id);
        okHttpUtil.post(weixin_login_url, params, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                System.out.println("--登录成功json-- " + json);

                String headPicUrl = "";
                String userName = "";
                String mark = "";
                String token = "";
                String userid = "";
                String cityname = "";

                try {
                    JSONObject object = new JSONObject(json);
                    if (object.getInt("error_code") == 0) {
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

                        setResult(Constant.WEIXIN_LOGIN_RESULTCODE);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }
}