package com.tbs.tobosutype.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;
import com.tbs.tobosutype.BuildConfig;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._ImageUpload;
import com.tbs.tobosutype.bean._PresonerInfo;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.FileUtil;
import com.tbs.tobosutype.utils.GetImagePath;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;
import com.tbs.tobosutype.utils.WriteUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 业主端
 * 用户个人信息页 3.7版本新增
 */
public class PresonerMsgActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.pre_msg_back)
    RelativeLayout preMsgBack;
    @BindView(R.id.pre_msg_icon)
    ImageView preMsgIcon;
    @BindView(R.id.pre_msg_icon_rl)
    RelativeLayout preMsgIconRl;
    @BindView(R.id.pre_msg_nick)
    TextView preMsgNick;
    @BindView(R.id.pre_msg_nick_rl)
    RelativeLayout preMsgNickRl;
    @BindView(R.id.pre_msg_sex)
    TextView preMsgSex;
    @BindView(R.id.pre_msg_sex_rl)
    RelativeLayout preMsgSexRl;
    @BindView(R.id.pre_msg_city)
    TextView preMsgCity;
    @BindView(R.id.pre_msg_city_rl)
    RelativeLayout preMsgCityRl;
    @BindView(R.id.pre_msg_conmui)
    TextView preMsgConmui;
    @BindView(R.id.pre_msg_conmui_rl)
    RelativeLayout preMsgConmuiRl;
    @BindView(R.id.pre_msg_bandphone)
    TextView preMsgBandphone;
    @BindView(R.id.pre_msg_bandphone_rl)
    RelativeLayout preMsgBandphoneRl;
    @BindView(R.id.pre_msg_bandwechat)
    TextView preMsgBandwechat;
    @BindView(R.id.pre_msg_bandwechat_rl)
    RelativeLayout preMsgBandwechatRl;
    @BindView(R.id.pre_msg_login_out)
    TextView preMsgLoginOut;
    @BindView(R.id.banner_dever)
    View bannerDever;
    private String TAG = "PresonerMsgActivity";
    private Context mContext;
    private UMShareAPI umShareAPI;
    private Gson mGson;
    private String urlpath;
    private Bitmap photo = null;
    private ProgressDialog pd;
    private String resultStr = "";
    private static final String MCAMERAFILE_NAME = "mcamerafile_name.jpg";
    private static final String MCROPFILE_NAME = "mcropfile_name.jpg";
    private static final String MGALLERYFILE_NAME = "mgalleryfile_name.jpg";
    private _PresonerInfo presonerInfo;
    //图片上传
    private File mCameraFile;//拍照路径
    private File mCropFile;//切图路径
    private File mGalleryFile;//相册路径
    private Uri imageUri;
    private final int CAMERA_REQUEST_CODE = 1;
    private final int RESULT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presoner_msg);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        umShareAPI = UMShareAPI.get(mContext);//绑定微信用 以及退出登录用
        mGson = new Gson();
        mCameraFile = new File(Constant.IMAGE_PATH, MCAMERAFILE_NAME);
        mCropFile = new File(Constant.IMAGE_PATH, MCROPFILE_NAME);
        mGalleryFile = new File(Constant.IMAGE_PATH, MGALLERYFILE_NAME);
        HttpGetUserMsg();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    //处理接收的事件

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.PRESONER_MSG_CHANGE_CITY:
                //更改用户的城市信息
                Log.e(TAG, "从返回的界面修改===========" + (String) event.getData());
                if (!TextUtils.isEmpty((String) event.getData())) {
                    Log.e(TAG, "从返回的界面修改===========" + (String) event.getData());
                    preMsgCity.setText("" + (String) event.getData());
                    HttpChangeUserMsg("", "", "", (String) event.getData(), "");
                }
                break;
            case EC.EventCode.CHANGE_COMMUNITY:
                //更改用户社区信息
                if (!TextUtils.isEmpty((String) event.getData())) {
                    preMsgConmui.setText("" + (String) event.getData());
                    HttpChangeUserMsg("", "", "", "", (String) event.getData());
                }
                break;
            case EC.EventCode.CHANGE_NICK_NAME:
                //修改昵称
                if (!TextUtils.isEmpty((String) event.getData())) {
                    preMsgNick.setText("" + (String) event.getData());
                    HttpChangeUserMsg("", (String) event.getData(), "", "", "");
                }
                break;
            case EC.EventCode.BAND_PHONE_SUCCESS:
                //绑定手机号码成功 改变绑定手机字样的状态
                preMsgBandphone.setText("已绑定");
                preMsgBandphone.setTextColor(Color.parseColor("#999999"));
                AppInfoUtil.setUserCellphone_check(mContext, "1");
                break;
        }
    }

    @Override
    protected void onResume() {
        //时时更新用户的信息
        super.onResume();
    }

    @OnClick({R.id.pre_msg_back, R.id.pre_msg_icon_rl, R.id.pre_msg_nick_rl,
            R.id.pre_msg_sex_rl, R.id.pre_msg_city_rl, R.id.pre_msg_conmui_rl,
            R.id.pre_msg_bandphone_rl, R.id.pre_msg_bandwechat_rl, R.id.pre_msg_login_out})
    public void onViewClickedInPresonerMsgActivity(View view) {
        switch (view.getId()) {
            case R.id.pre_msg_back:
                //返回按钮
                finish();
                break;
            case R.id.pre_msg_icon_rl:
                //点击更换头像
                changeIcon();
                break;
            case R.id.pre_msg_nick_rl:
                //更换昵称
                chnageNick();
                break;
            case R.id.pre_msg_sex_rl:
                //更换性别
                changeSex();
                break;
            case R.id.pre_msg_city_rl:
                //更换城市
                changeCity();
                break;
            case R.id.pre_msg_conmui_rl:
                //小区信息
                changeCommiu();
                break;
            case R.id.pre_msg_bandphone_rl:
                //绑定手机号
                bindPhoneNum();
                break;
            case R.id.pre_msg_bandwechat_rl:
                //绑定微信
                bindWeChat();
                break;
            case R.id.pre_msg_login_out:
                //用户登出
                userLoginOut();
                break;
        }
    }

    //绑定微信
    private void bindWeChat() {
        if (preMsgBandwechat.getText().toString().equals("未绑定")) {
            //进行微信的绑定
            umShareAPI.getPlatformInfo(PresonerMsgActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    String weiXinUserName = map.get("name");//微信的昵称
                    String weiXinImageUrl = map.get("iconurl");//微信的头像
                    String weiXinUserId = map.get("openid");//微信的openid
                    HttpWeixinBind(weiXinUserId, weiXinUserName, weiXinImageUrl);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    Log.e(TAG, "授权出错=====" + throwable.getMessage());
                    Toast.makeText(mContext, "授权出错！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                    Toast.makeText(mContext, "取消微信登录！", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //微信已经绑定
            Toast.makeText(mContext, "您已绑定手机", Toast.LENGTH_SHORT).show();
        }
    }

    //绑定微信
    private void HttpWeixinBind(String openid, String nickname, String icon) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        param.put("openid", openid);
        param.put("nickname", nickname);
        param.put("icon", icon);
        OKHttpUtil.post(Constant.BIND_WE_CHAT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString(json);
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        //微信绑定成功 修改绑定的状态
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                preMsgBandwechat.setText("已绑定");
                                preMsgBandwechat.setTextColor(Color.parseColor("#999999"));
                            }
                        });

                    } else if (status.equals("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "绑定失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //绑定手机号码
    private void bindPhoneNum() {
        if (preMsgBandphone.getText().toString().equals("未绑定")) {
            //进入绑定
            startActivity(new Intent(mContext, BandPhoneActivity.class));
        } else {
            Toast.makeText(mContext, "您已绑定手机", Toast.LENGTH_SHORT).show();
        }
    }

    //修改小区信息
    private void changeCommiu() {
        Intent intentToChnageCommiu = new Intent(mContext, ChangeCommunityActivity.class);
        mContext.startActivity(intentToChnageCommiu);
    }

    //修改性别
    private void changeSex() {
        View popview = View.inflate(mContext, R.layout.pop_change_sex, null);
        //男
        TextView pop_change_sex_man = popview.findViewById(R.id.pop_change_sex_man);
        //女
        TextView pop_change_sex_woman = popview.findViewById(R.id.pop_change_sex_woman);
        //取消
        TextView pop_change_sex_dismiss = popview.findViewById(R.id.pop_change_sex_dismiss);

        RelativeLayout pop_change_sex_rl = (RelativeLayout) popview.findViewById(R.id.pop_change_sex_rl);
        //按键层
        LinearLayout pop_change_sex_11 = (LinearLayout) popview.findViewById(R.id.pop_change_sex_11);

        pop_change_sex_11.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //取消
        pop_change_sex_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //点击非按区界面消失
        pop_change_sex_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //点击层
        pop_change_sex_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        pop_change_sex_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择了  性别男
                preMsgSex.setText("" + "男");
                HttpChangeUserMsg("", "", "1", "", "");
                popupWindow.dismiss();
            }
        });
        pop_change_sex_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择了  性别男
                preMsgSex.setText("" + "女");
                HttpChangeUserMsg("", "", "0", "", "");
                popupWindow.dismiss();
            }
        });
        //点击整个非按钮区域
        pop_change_sex_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    //更换城市
    private void changeCity() {
        Intent intentToSelectCity = new Intent(mContext, SelectCtiyActivity.class);
        intentToSelectCity.putExtra("mWhereFrom", "PresonerMsgActivity");
        mContext.startActivity(intentToSelectCity);
    }

    //更换用户的头像
    private void changeIcon() {
        //点击出现弹窗
        View popview = View.inflate(mContext, R.layout.pop_change_icon, null);
        //查看大图
        TextView pop_change_icon_look_big_photo = popview.findViewById(R.id.pop_change_icon_look_big_photo);
        //拍照获取
        TextView pop_change_icon_take_photo = popview.findViewById(R.id.pop_change_icon_take_photo);
        //从相册中获取
        TextView pop_change_icon_get_photo = popview.findViewById(R.id.pop_change_icon_get_photo);
        //取消
        TextView pop_change_icon_dissmiss = popview.findViewById(R.id.pop_change_icon_dissmiss);
        //整个可点击层
        RelativeLayout pop_change_icon_rl = popview.findViewById(R.id.pop_change_icon_rl);
        //按键层
        LinearLayout pop_change_icon_11 = popview.findViewById(R.id.pop_change_icon_11);
        pop_change_icon_11.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //取消事件
        pop_change_icon_dissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //从相册中获取
        pop_change_icon_get_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/1/17 从相册中获取
                popupWindow.dismiss();
            }
        });
        //拍照获取
        pop_change_icon_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前系统是否高于或等于6.0
                // TODO: 2018/1/17  拍照获取
                takeCamerGetPhoto();
                popupWindow.dismiss();
            }
        });
        //查看大图
        pop_change_icon_look_big_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BigIconLookActivity.class);
                intent.putExtra("mImageIconUrl", presonerInfo.getIcon());
                mContext.startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //点击整个层级
        pop_change_icon_dissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //点击整个非按钮区域
        pop_change_icon_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    private void takeCamerGetPhoto() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Util.hasSDCard()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
                Uri uriForFile = FileProvider.getUriForFile(mContext, "com.tbs.tobosutype.fileprovider", mCameraFile);
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                intentFromCapture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentFromCapture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
            }
        }
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (Util.hasSDCard()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri inputUri = FileProvider.getUriForFile(mContext, "com.tbs.tobosutype.fileprovider", mCameraFile);//通过FileProvider创建一个content类型的Uri
                        startPhotoZoom(inputUri);//设置输入类型
                    } else {
                        Uri inputUri = Uri.fromFile(mCameraFile);
                        startPhotoZoom(inputUri);
                    }
                } else {
                    Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }
                break;
            case RESULT_REQUEST_CODE:

                break;
        }
    }

    //裁剪
    public void startPhotoZoom(Uri inputUri) {
        if (inputUri == null) {
            Log.e(TAG, "The uri is not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            Uri outPutUri = Uri.fromFile(mCropFile);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = GetImagePath.getPath(mContext, inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        startActivityForResult(intent, RESULT_REQUEST_CODE);//这里就将裁剪后的图片的Uri返回了
    }

    //用户更换昵称
    private void chnageNick() {
        Intent intentToChangeNickName = new Intent(mContext, ChangeNickNameActivity.class);
        mContext.startActivity(intentToChangeNickName);
    }

    //更改用户的信息
    private void HttpChangeUserMsg(String icon, final String nickname, String gender,
                                   final String city_name, String community) {
        Log.e(TAG, "进入信息修改================");
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        //头像
        if (!TextUtils.isEmpty(icon)) {
            param.put("icon", icon);
        }
        //昵称
        if (!TextUtils.isEmpty(nickname)) {
            param.put("nickname", nickname);
        }
        //性别
        if (!TextUtils.isEmpty(gender)) {
            param.put("gender", gender);
        }
        //城市信息
        if (!TextUtils.isEmpty(city_name)) {
            param.put("city_name", city_name);
        }
        //小区信息
        if (!TextUtils.isEmpty(community)) {
            param.put("community", community);
        }
        OKHttpUtil.post(Constant.MODIFY_USER_INFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "修改数据链接成功==========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //修改本地存储的昵称
                                if (!TextUtils.isEmpty(nickname)) {
                                    AppInfoUtil.setUserNickname(mContext, nickname);
                                }
                                if (!TextUtils.isEmpty(city_name)) {
                                    AppInfoUtil.setUserProvince(mContext, "");
                                    AppInfoUtil.setUserCity(mContext, city_name);
                                }
                                Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取用户的信息
    private void HttpGetUserMsg() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        OKHttpUtil.post(Constant.USER_INFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败~", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    String data = jsonObject.optString("data");
                    if (status.equals("200")) {
                        //请求回来的数据正确
                        presonerInfo = mGson.fromJson(data, _PresonerInfo.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //用户的头像
                                GlideUtils.glideLoader(mContext, presonerInfo.getIcon(),
                                        R.drawable.iamge_loading, R.drawable.iamge_loading,
                                        preMsgIcon, 0);
                                AppInfoUtil.setUserIcon(mContext, presonerInfo.getIcon());//重置信息
                                //用户的昵称
                                preMsgNick.setText("" + presonerInfo.getNickname());
                                AppInfoUtil.setUserNickname(mContext, presonerInfo.getNickname());//重置信息
                                //用户的性别
                                if (presonerInfo.getGender().equals("0")) {
                                    preMsgSex.setText("女");
                                } else {
                                    preMsgSex.setText("男");
                                }
                                //用户的所在城市
                                preMsgCity.setText("" + presonerInfo.getCity_name());
                                //用户所在的小区
                                preMsgConmui.setText("" + presonerInfo.getCommunity());
                                //用户是否绑定手机
                                if (presonerInfo.getCellphone_check().equals("0")) {
                                    //未绑定
                                    preMsgBandphone.setTextColor(Color.parseColor("#ff4529"));
                                    preMsgBandphone.setText("未绑定");
                                } else {
                                    //已绑定
                                    preMsgBandphone.setTextColor(Color.parseColor("#999999"));
                                    preMsgBandphone.setText("" + presonerInfo.getCellphone());
                                }
                                //微信的绑定
                                if (presonerInfo.getWechat_check().equals("0")) {
                                    //未绑定
                                    preMsgBandwechat.setTextColor(Color.parseColor("#ff4529"));
                                    preMsgBandwechat.setText("未绑定");
                                } else {
                                    //已绑定
                                    preMsgBandwechat.setTextColor(Color.parseColor("#999999"));
                                    preMsgBandwechat.setText("已绑定");
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //用户退出 清空本地用户存储的信息
    private void userLoginOut() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);

        builder.setMessage("你确定退出账号吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                AppInfoUtil.ISJUSTLOGIN = true;
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                dialog.cancel();
                umShareAPI.deleteOauth(PresonerMsgActivity.this, SHARE_MEDIA.WEIXIN, null);
                MobclickAgent.onProfileSignOff();
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

}
