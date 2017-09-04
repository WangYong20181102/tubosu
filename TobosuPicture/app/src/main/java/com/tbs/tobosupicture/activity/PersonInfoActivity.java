package com.tbs.tobosupicture.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._ImageUpLoad;
import com.tbs.tobosupicture.bean._PersonInfo;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.FileUtil;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.utils.WriteUtil;
import com.tbs.tobosupicture.view.CustomWaitDialog;
import com.tbs.tobosupicture.view.SelectPersonalPopupWindow;
import com.tbs.tobosupicture.view.SetSexPopWindow;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 个人信息页面
 * 1--从注册成功页面跳转过来的 底部的按钮变成“确定”
 * 2--登录后点击头像进入的页面 底部的按钮变成“退出登录”
 */
public class PersonInfoActivity extends BaseActivity {

    @BindView(R.id.person_info_back)
    LinearLayout personInfoBack;
    @BindView(R.id.person_info_icon)
    ImageView personInfoIcon;
    @BindView(R.id.person_info_icon_rl)
    RelativeLayout personInfoIconRl;
    @BindView(R.id.person_info_nick_rl)
    RelativeLayout personInfoNickRl;
    @BindView(R.id.person_info_sex)
    TextView personInfoSex;
    @BindView(R.id.person_info_sex_rl)
    RelativeLayout personInfoSexRl;
    @BindView(R.id.person_info_city)
    TextView personInfoCity;
    @BindView(R.id.person_info_city_rl)
    RelativeLayout personInfoCityRl;
    @BindView(R.id.person_info_sign)
    TextView personInfoSign;
    @BindView(R.id.person_info_sign_rl)
    RelativeLayout personInfoSignRl;
    @BindView(R.id.person_info_bind_wx)
    TextView personInfoBindWx;
    @BindView(R.id.person_info_bind_wx_rl)
    RelativeLayout personInfoBindWxRl;
    @BindView(R.id.person_info_bind_phone)
    TextView personInfoBindPhone;
    @BindView(R.id.person_info_bind_phone_rl)
    RelativeLayout personInfoBindPhoneRl;
    @BindView(R.id.person_info_btn)
    TextView personInfoBtn;
    @BindView(R.id.person_info_btn_rl)
    RelativeLayout personInfoBtnRl;
    @BindView(R.id.person_info_nick)
    TextView personInfoNick;

    private Context mContext;
    private String TAG = "PersonInfoActivity";
    private Gson mGson;
    private Intent mIntent;
    private _PersonInfo personInfo;
    //TODO 从上一个界面传来的 标识是从那个Activity来的
    private String whereFrom = "";
    private SetSexPopWindow setSexPopWindow;//底部弹窗

    private UMShareAPI mShareAPI;

    private SelectPersonalPopupWindow menuWindow;
    private CustomWaitDialog customWaitDialog;
    //TODO 调取相机要用的值
    private static final int REQUESTCODE_PICK = 0;
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private static final int REQUESTCODE_XIAO_MI_TAKE = 4;//小米专用拍照获取码


    /**
     * 头像存储的名称
     */
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";
    private String urlpath;
    private Bitmap photo = null;

    //上传时候的loading
    private ProgressDialog pd;

    //结果字符串
    private String resultStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        HttpGetPersonInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mShareAPI = UMShareAPI.get(mContext);
        whereFrom = mIntent.getStringExtra("from");
        if (whereFrom.equals("RegisterActivity")) {
            //从注册进来
            personInfoBtn.setText("确定");
            if (TextUtils.isEmpty(SpUtils.getLocationCity(mContext))) {
                personInfoCity.setText("");
            } else {
                personInfoCity.setText(SpUtils.getLocationCity(mContext));
            }

        } else if (whereFrom.equals("MineFragment")) {
            //从“我的”进来
            personInfoBtn.setText("退出登录");
        }
        if (SpUtils.getUserType(mContext).equals("3")) {
            //装修公司的显示情况
            personInfoIconRl.setVisibility(View.GONE);//隐藏头像
            personInfoSignRl.setVisibility(View.GONE);//隐藏签名
            personInfoSexRl.setVisibility(View.GONE);//隐藏性别
            personInfoCityRl.setVisibility(View.GONE);//隐藏城市
        }
        customWaitDialog = new CustomWaitDialog(mContext);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.PERSON_INFO_ACTIVITY_CHANGE_CITY:
                personInfoCity.setText("" + (String) event.getData());
                HttpChangeCity((String) event.getData());
                break;
            case EC.EventCode.PERSON_INFO_ACTIVITY_CHANGE_MSG:
                //修改了 昵称 个性签名 绑定手机等等的信息后返回进行数据的刷新
                HttpGetPersonInfo();
                break;
        }
    }

    @OnClick({R.id.person_info_back, R.id.person_info_icon_rl, R.id.person_info_nick_rl,
            R.id.person_info_sex_rl, R.id.person_info_city_rl, R.id.person_info_sign_rl, R.id.person_info_bind_wx_rl, R.id.person_info_bind_phone_rl, R.id.person_info_btn_rl})
    public void onViewClickedInPersonInfoActivity(View view) {
        switch (view.getId()) {
            case R.id.person_info_back:
                finish();
                break;
            case R.id.person_info_icon_rl:
                //点击更换头像
                changeIcon();
                break;
            case R.id.person_info_nick_rl:
                //点击更换昵称
                changeNick();
                break;
            case R.id.person_info_sex_rl:
                //点击更换性别
                setSexPopWindow = new SetSexPopWindow(mContext, popClickLister);
                setSexPopWindow.showAtLocation(PersonInfoActivity.this.findViewById(R.id.pop_window_showing),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.person_info_city_rl:
                //点击更换城市
                changeCity();
                break;
            case R.id.person_info_sign_rl:
                //点击更换签名
                changeSigin();
                break;
            case R.id.person_info_bind_wx_rl:
                //点击进行微信绑定
                if (personInfoBindWx.getText().toString().equals("已绑定")) {
                    Toast.makeText(mContext, "您已经绑定了微信~", Toast.LENGTH_SHORT).show();
                } else {
                    customWaitDialog.show();
                    bindWeChat();
                }
                break;
            case R.id.person_info_bind_phone_rl:
                //点击进行手机号码的绑定
                bindPhone();
                break;
            case R.id.person_info_btn_rl:
                //底部按钮事件
                if (personInfoBtn.getText().equals("退出登录")) {
                    //清除用户数据
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("确定要退出登录吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SpUtils.saveUserNick(mContext, "");
                            SpUtils.saveUserIcon(mContext, "");
                            SpUtils.saveUserPersonalSignature(mContext, "");
                            SpUtils.saveUserType(mContext, "");
                            SpUtils.saveUserUid(mContext, "");
                            EventBusUtil.sendEvent(new Event(EC.EventCode.LOGIN_OUT));
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //注册之后的确定按钮 拿到城市信息将信息保存
                    HttpChangeCity(personInfoCity.getText().toString());
                    finish();
                }
                break;
        }
    }

    //更换城市
    private void changeCity() {
        Intent intent = new Intent(mContext, SelectCityActivity.class);
        intent.putExtra("from", "PersonInfoActivity");
        mContext.startActivity(intent);
    }

    //用户修改昵称
    private void changeNick() {
        if (SpUtils.getUserType(mContext).equals("3")) {
            Toast.makeText(mContext, "装修公司账号不允许修改昵称", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(mContext, ChangeNickActivity.class);
            mContext.startActivity(intent);
        }

    }

    //用户修改头像
    private void changeIcon() {
        menuWindow = new SelectPersonalPopupWindow(this, popClickLister);
        menuWindow.showAtLocation(findViewById(R.id.pop_window_showing), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //修改个性签名
    private void changeSigin() {
        Intent intent = new Intent(mContext, ChangeSiginActivity.class);
        mContext.startActivity(intent);
    }

    //绑定手机号
    private void bindPhone() {
        if (personInfoBindPhone.getText().equals("已绑定")) {
            Toast.makeText(mContext, "您已经绑定了手机~", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(mContext, BindPhoneNumActivity.class);
            mContext.startActivity(intent);
        }
    }

    //绑定微信
    private void bindWeChat() {
        mShareAPI.getPlatformInfo(PersonInfoActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                //授权成功 获取信息
                String icon = map.get("iconurl");//微信的头像
                String nickname = map.get("name");//微信的昵称
                String account = map.get("openid");//微信的openid
                Log.e(TAG, "授权成功==头像==" + icon + "===nickname===" + nickname + "===account===" + account);
                customWaitDialog.dismiss();
                HttpBindWeChat(account);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e(TAG, "授权失败====" + i + "==原因==" + throwable.toString());
                customWaitDialog.dismiss();
                Toast.makeText(mContext, "授权微信失败:" + throwable.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText(mContext, "取消微信授权绑定！", Toast.LENGTH_SHORT).show();
                customWaitDialog.dismiss();
            }
        });
    }

    //请求个人信息数据
    private void HttpGetPersonInfo() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        HttpUtils.doPost(UrlConstans.PERSONAL_INFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功
                        String data = jsonObject.getString("data");
                        personInfo = mGson.fromJson(data, _PersonInfo.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initPersonInfo(personInfo);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initPersonInfo(_PersonInfo personInfo) {
        //设置头像
        GlideUtils.glideLoader(getApplicationContext(), personInfo.getIcon(), R.mipmap.default_icon,
                R.mipmap.default_icon, personInfoIcon, 0);
        //设置昵称
        personInfoNick.setText(personInfo.getNick());
        //设置性别
        if (!TextUtils.isEmpty(personInfo.getGender())) {
            if (personInfo.getGender().equals("1")) {
                //男
                personInfoSex.setText("男");
            } else if (personInfo.getGender().equals("2")) {
                personInfoSex.setText("女");
            } else {
                personInfoSex.setText("未设置");
            }
        }
        //设置城市
        personInfoCity.setText(personInfo.getCity_name());
        //设置个性签名
        personInfoSign.setText(personInfo.getPersonal_signature());
        //设置绑定状态
        if (personInfo.getWechat_check().equals("1")) {
            personInfoBindWx.setText("已绑定");
            personInfoBindWx.setTextColor(Color.parseColor("#8a8f99"));
        }
        if (personInfo.getWechat_check().equals("2")) {
            personInfoBindWx.setTextColor(Color.parseColor("#f57a7a"));
            personInfoBindWx.setText("未绑定");
        }
        //设置手机号码绑定状态
        if (personInfo.getCellphone_check().equals("1")) {
            personInfoBindPhone.setText("已绑定");
            personInfoBindPhone.setTextColor(Color.parseColor("#8a8f99"));
        }
        if (personInfo.getCellphone_check().equals("2")) {
            personInfoBindPhone.setText("未绑定");
            personInfoBindPhone.setTextColor(Color.parseColor("#f57a7a"));
        }
    }

    //popwindow上的点击事件
    private View.OnClickListener popClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pop_set_man:
                    //设置性别男性
                    HttpSetSex(1);
                    personInfoSex.setText("男");
                    setSexPopWindow.dismiss();
                    break;
                case R.id.pop_set_women:
                    //设置性别女性
                    HttpSetSex(2);
                    personInfoSex.setText("女");
                    setSexPopWindow.dismiss();
                    break;
                case R.id.takePhotoBtn:
                    //开启相机
                    menuWindow.dismiss();
                    if (android.os.Build.BRAND.equals("Xiaomi")) {
                        Log.e(TAG, "进入的了小米专用==========");
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takeIntent, REQUESTCODE_XIAO_MI_TAKE);
                    } else {
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                        startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    }
                    break;
                case R.id.pickPhotoBtn:
                    //开启图册
                    menuWindow.dismiss();
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                case R.id.cancelBtn:
                    menuWindow.dismiss();
                    break;
            }
        }
    };

    //修改用户的性别  1--男 2--女
    private void HttpSetSex(int sex) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("sex", sex);
        HttpUtils.doPost(UrlConstans.MODIFY_SEX, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "修改成功~", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //请求绑定微信
    private void HttpBindWeChat(String account) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("account", account);
        HttpUtils.doPost(UrlConstans.BIND_WECHAT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "绑定微信链接失败====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "绑定微信======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "绑定成功！");
                                Toast.makeText(mContext, "绑定成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                        HttpGetPersonInfo();
                    } else if (status.equals("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "绑定失败,原因:" + msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /***
     * 裁剪头像图片
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /***
     * 上传用户头像至服务器
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
            Log.e(TAG, "上传头像的文件流地址======" + urlpath);
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            // 开启线程上传
            new Thread(uploadImageRunnable).start();
        }
    }

    //上传图片的线程 这个可以使用OkHttp上传  在测试的以图会友的界面中可以看到
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            if (TextUtils.isEmpty(UrlConstans.UPLOAD_IMAGE)) {
                Toast.makeText(mContext, "还没有设置上传服务器的路径！", Toast.LENGTH_SHORT).show();
                return;
            }
            Map<String, String> textParams = null;
            Map<String, File> fileparams = null;
            try {
                URL url = new URL(UrlConstans.UPLOAD_IMAGE);
                textParams = new HashMap<String, String>();
                fileparams = new HashMap<String, File>();
                File file = new File(urlpath);
                fileparams.put("filedata", file);
                textParams.put("s_code", "head_file");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("ser-Agent", "Fiddler");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + WriteUtil.BOUNDARY);
                OutputStream os = conn.getOutputStream();
                DataOutputStream ds = new DataOutputStream(os);
                WriteUtil.writeStringParams(textParams, ds);
                WriteUtil.writeFileParams(fileparams, ds);
                WriteUtil.paramsEnd(ds);
                // 对文件流操作完,要记得及时关闭
                os.close();
                // 服务器返回的响应吗
                int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                // 对响应码进行判断
                if (code == 200) {// 返回的响应码200,是成功
                    // 得到网络返回的输入流
                    InputStream is = conn.getInputStream();
                    resultStr = WriteUtil.readString(is);
                } else {
                    Toast.makeText(mContext, "请求URL失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            handlerUpload.sendEmptyMessage(0);
        }
    };

    private Handler handlerUpload = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.dismiss();
                    Log.e(TAG, "获取的结果=====" + resultStr);
                    _ImageUpLoad imageUpLoad = mGson.fromJson(resultStr, _ImageUpLoad.class);
                    HttpChangeUserIcon(imageUpLoad.getUrl());
                    GlideUtils.glideLoader(mContext, imageUpLoad.getUrl(), R.mipmap.default_icon,
                            R.mipmap.default_icon, personInfoIcon, 0);
                    break;
            }
            return false;
        }
    });

    //修改城市
    private void HttpChangeCity(String cityName) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("city_name", cityName);
        HttpUtils.doPost(UrlConstans.MODIFY_CITY, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    Log.e(TAG, "修改城市信息=========" + json);
                    if (status.equals("200")) {
                        //修改成功
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //修改头像
    private void HttpChangeUserIcon(String icon) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("icon", icon);
        HttpUtils.doPost(UrlConstans.MODIFY_AVATAR, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        Log.e(TAG, "上传成功=====");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_TAKE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (data != null) {
                    setPicToView(data);
                }
                break;
            case REQUESTCODE_PICK:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case REQUESTCODE_XIAO_MI_TAKE:
                //小米专用拍照
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
    }
}
