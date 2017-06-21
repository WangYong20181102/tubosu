package com.tobosu.mydecorate.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.entity._UserInfo;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.FileUtil;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.MiPictureHelper;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.util.WriteUtil;
import com.tobosu.mydecorate.view.RoundImageView;
import com.tobosu.mydecorate.view.SelectPictureWindow;
import com.tobosu.mydecorate.view.SetSexPopWindow;
import com.tobosu.mydecorate.view.SetZhuangxiuPopWindow;
import com.umeng.analytics.MobclickAgent;

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
import java.util.Map;

import static com.tobosu.mydecorate.global.Constant.TAKE_PHOTO_REQUEST_CODE;

/**
 * Created by dec on 2016/9/26.
 */
public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private Context mContext;


    private RelativeLayout rel_userinfo_back;


    private RelativeLayout rel_username;

    private RelativeLayout rel_userpict;

    private TextView tv_changename;
    private RelativeLayout rel_usersex;//修改性别
    private RelativeLayout rel_usercity;//修改城市
    private RelativeLayout rel_userjieduan;//修改城市
    private RelativeLayout rel_user_login_out;//退出当前账户
    private TextView tv_changesex;//改变性别
    private TextView tv_changecity;//改变城市
    private TextView tv_changejieduan;//改变装修阶段

    private SelectPictureWindow selectWindow;

    private String head_picture = "";
    private ImageView iv_userchange_picture;
    private _UserInfo userInfo;


    //底部弹窗
    private SetSexPopWindow setSexPopWindow;
    private SetZhuangxiuPopWindow setZhuangxiuPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        mContext = UserInfoActivity.this;
        initView();
        HttpGetUserInfo();
    }


    private void initView() {
        rel_userinfo_back = (RelativeLayout) findViewById(R.id.rel_userinfo_back);
        rel_user_login_out = (RelativeLayout) findViewById(R.id.rel_user_login_out);
        rel_user_login_out.setOnClickListener(occl);
        rel_userinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent fromInent = getIntent();
        Bundle b = fromInent.getBundleExtra("Change_UserName_Bundle");

        tv_changename = (TextView) findViewById(R.id.tv_changename);

        rel_usersex = (RelativeLayout) findViewById(R.id.rel_usersex);
        rel_usercity = (RelativeLayout) findViewById(R.id.rel_usercity);
        rel_userjieduan = (RelativeLayout) findViewById(R.id.rel_userjieduan);
        if (Util.getUserMark(mContext).equals("3")) {
            rel_userjieduan.setVisibility(View.GONE);
            rel_usersex.setVisibility(View.GONE);
        }
        tv_changesex = (TextView) findViewById(R.id.tv_changesex);

        tv_changecity = (TextView) findViewById(R.id.tv_changecity);
        tv_changejieduan = (TextView) findViewById(R.id.tv_changejieduan);

        rel_usersex.setOnClickListener(occl);
        rel_usercity.setOnClickListener(occl);
        rel_userjieduan.setOnClickListener(occl);
        tv_changesex.setOnClickListener(occl);

        tv_changecity.setOnClickListener(occl);
        tv_changejieduan.setOnClickListener(occl);

        String tempName = b.getString("change_user_name");
        if (tempName.length() >= 8) {
            tv_changename.setText(tempName.substring(0, 8) + "...");
        } else {
            tv_changename.setText(tempName);
        }

        head_picture = getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("head_pic_url", "");

        iv_userchange_picture = (ImageView) findViewById(R.id.iv_userchange_picture);
//        Picasso.with(mContext)
//                .load(head_picture)
//                .fit()
////                .placeholder(R.mipmap.occupied1)
//                .error(R.mipmap.icon_head_default)
//                .into(iv_userchange_picture);
        GlideUtils.glideLoader(mContext, head_picture, R.mipmap.icon_head_default, R.mipmap.jiazai_loading, iv_userchange_picture, GlideUtils.CIRCLE_IMAGE);

        rel_username = (RelativeLayout) findViewById(R.id.rel_username);
        rel_username.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ChangeUserNameActivity.class);
                Bundle b = new Bundle();
                b.putString("username", tv_changename.getText().toString().trim());
                it.putExtra("Change_User_Name_Bundle", b);
                startActivityForResult(it, Constant.CHANGE_USERNAME_REQUESTCODE);
            }
        });

        rel_userpict = (RelativeLayout) findViewById(R.id.rel_userpict);
        rel_userpict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 添加
                selectWindow = new SelectPictureWindow(mContext, itemOnClick);
                selectWindow.showAtLocation(findViewById(R.id.userinfo_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    private View.OnClickListener popClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.pop_set_man:
                    //设置性别男性
                    HttpSetSex(1);
                    tv_changesex.setText("男");
                    setSexPopWindow.dismiss();
                    break;
                case R.id.pop_set_women:
                    //设置性别女性
                    HttpSetSex(0);
                    tv_changesex.setText("女");
                    setSexPopWindow.dismiss();
                    break;
                case R.id.pop_set_zhuangxiuzhunbei:
                    //装修前
                    HttpSetDecorate(1);
                    tv_changejieduan.setText("准备装修");
                    setZhuangxiuPopWindow.dismiss();
                    break;
                case R.id.pop_set_zhuangxiuqian:
                    //装修前
                    HttpSetDecorate(2);
                    tv_changejieduan.setText("装修前期");
                    setZhuangxiuPopWindow.dismiss();
                    break;
                case R.id.pop_set_zhuangxiuzhong:
                    //装修中
                    HttpSetDecorate(3);
                    tv_changejieduan.setText("装修中期");
                    setZhuangxiuPopWindow.dismiss();
                    break;
                case R.id.pop_set_zhuangxiuhou:
                    //装修后
                    HttpSetDecorate(4);
                    tv_changejieduan.setText("装修后期");
                    setZhuangxiuPopWindow.dismiss();
                    break;
                case R.id.pop_set_zhuangxiuruzhu:
                    //装修前
                    HttpSetDecorate(5);
                    tv_changejieduan.setText("已入住");
                    setZhuangxiuPopWindow.dismiss();
                    break;
            }
        }
    };
    private View.OnClickListener itemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_takePhoto:
//                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST_CODE);

                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, TAKE_PHOTO_REQUEST_CODE);


                    break;
                case R.id.btn_pickPhoto:
                    Intent pickphotoIntent = new Intent(Intent.ACTION_PICK, null);
                    pickphotoIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickphotoIntent, Constant.PICK_PHOTO_REQUEST_CODE);
                    break;
            }

            if (selectWindow != null && selectWindow.isShowing()) {
                selectWindow.dismiss();
            }
        }
    };
    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_usersex:
                case R.id.tv_changesex:
                    //点击修改性别
                    //实例化popwindow
                    setSexPopWindow = new SetSexPopWindow(mContext, popClickLister);
                    setSexPopWindow.showAtLocation(UserInfoActivity.this.findViewById(R.id.pop_window_showing),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;
                case R.id.tv_changecity:
                case R.id.rel_usercity:
                    //点击修改城市
                    Intent cityIntent = new Intent(mContext, SelectCityActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("frompop", 31);
                    cityIntent.putExtra("pop_bundle", b);
                    startActivityForResult(cityIntent, 124);
                    break;
                case R.id.tv_changejieduan:
                case R.id.rel_userjieduan:
                    //修改阶段
                    setZhuangxiuPopWindow = new SetZhuangxiuPopWindow(mContext, popClickLister);
                    setZhuangxiuPopWindow.showAtLocation(UserInfoActivity.this.findViewById(R.id.pop_window_showing),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;
                case R.id.rel_user_login_out:
                    //退出当前账户
                    SharedPreferences saveInfo = getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = saveInfo.edit();
                    editor.putString("user_name", "");
                    editor.putString("head_pic_url", "");
                    editor.putString("mark", "");
                    editor.putString("user_id", "");
                    editor.putString("token", "");
                    editor.putString("city_name", "");
                    editor.commit();
                    CacheManager.setUserUid(mContext, "");
                    finish();
                    break;
            }
        }
    };
    /**
     * 头像存储的名称
     */
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case Constant.CHANGE_USERNAME_RESULTCODE:
                Bundle b = data.getBundleExtra("new_user_name_bundle");
                String newUserName = b.getString("new_user_name_string");
                tv_changename.setText(newUserName);
                break;
            case 77:
                String city = data.getBundleExtra("city_bundle").getString("ci");
                CacheManager.setCity(mContext, city);
                tv_changecity.setText("" + city);
                HttpSetCity(city);
                break;
        }

        switch (requestCode) {

            case TAKE_PHOTO_REQUEST_CODE:
                // TODO 拍照之后
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);

                if (resultCode == Activity.RESULT_CANCELED) {
                    Intent it = new Intent(mContext, UserInfoActivity.class);
                    startActivity(it);
                } else {
                    if (data != null) {
                        try {
                            Uri uri = data.getData();
                            String pickPath = MiPictureHelper.getPath(mContext, uri);  // 获取图片路径的方法调用
                            System.out.println("图片路径 -->> " + pickPath);
                            startPhotoZoom(Uri.fromFile(new File(pickPath)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        startPhotoZoom(Uri.fromFile(temp));
                    }

                }


                break;
            case Constant.PICK_PHOTO_REQUEST_CODE:
                // TODO 获取到照片之后
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;

            case Constant.PHOTO_CROP:
                if (data != null) {
                    setPicToView(data);
                } else {
                    System.out.println("--------上传时 data 为空-------");
                }

                break;
        }
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
        startActivityForResult(intent, Constant.PHOTO_CROP);
        System.out.println("--------上传来到这里1-------");
    }

    private ProgressDialog pd;
    private String resultStr = "";
    private Bitmap photo = null;
    private String upLoadHeadPictureUrl = Constant.ZHJ + "tapp/user/chage_user_info";
    private String urlpath;
    /*** 上传头像的接口*/
    private String imgUrl = Constant.ZHJ + "cloud/upload/upload_for_ke?";

    /***
     * 上传用户头像至服务器
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            // 开启线程上传
            new Thread(uploadImageRunnable).start();
            System.out.println("--------上传来到这里2-------");
        }
    }

    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();

            Map<String, String> textParams = null;
            Map<String, File> fileparams = null;
            try {
                URL url = new URL(imgUrl);
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
                    System.out.println("--------上传来到这里3-------resultStr  " + resultStr);
                } else {
                    Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            handlerUpload.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
        }
    };

    private Handler handlerUpload = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.dismiss();
                    System.out.println("--------上传来到这里4-------");
                    try {
                        final JSONObject jsonObject = new JSONObject(resultStr);
                        System.out.println("--------上传来到这里5-------" + jsonObject.optString("error"));
                        if (jsonObject.optString("error").equals("0")) {
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            option.inSampleSize = 1;
                            String imageUrl = jsonObject.optString("url");
                            OKHttpUtil okHttpUtil = new OKHttpUtil();
                            HashMap<String, Object> hashMap = new HashMap<String, Object>();
                            hashMap.put("field", "5");
                            hashMap.put("new", imageUrl);
                            hashMap.put("token", getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("token", ""));
                            okHttpUtil.post(upLoadHeadPictureUrl, hashMap, new OKHttpUtil.BaseCallBack() {
                                @Override
                                public void onSuccess(Response response, String json) {
                                    System.out.println("--------上传后结果是" + json);
                                    try {
                                        JSONObject obj = new JSONObject(json);
                                        Toast.makeText(mContext, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                        if ("操作成功！".equals(obj.getString("msg"))) {

//                                            {
//                                                "error":0,
//                                                    "url":"http://cdn01.tobosu.net/head_file/2016-11-18/582eb8e4b75c1.jpg"
//                                            }

                                            JSONObject urlObj = new JSONObject(resultStr);
                                            String pic_url = urlObj.getString("url");

                                            getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).edit().putString("head_pic_url", pic_url).commit();

                                            //重新设置图片
//                                            Picasso.with(mContext)
//                                                    .load(pic_url)
//                                                    .fit()
////                                                    .placeholder(R.mipmap.occupied1)
//                                                    .error(R.mipmap.icon_head_default)
//                                                    .into(iv_userchange_picture);
//                                            initView();
                                            GlideUtils.glideLoader(mContext, pic_url, R.mipmap.icon_head_default, R.mipmap.jiazai_loading, iv_userchange_picture, GlideUtils.CIRCLE_IMAGE);

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFail(Request request, IOException e) {
                                    System.out.println("-------上传失败了------");
                                }

                                @Override
                                public void onError(Response response, int code) {
                                    System.out.println("-------上传错误code-->>>" + code);
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.optString("url"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
            return false;
        }
    });

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    //获取用户的个人信息请求
    private void HttpGetUserInfo() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        okHttpUtil.post(Constant.USER_INFO_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                //请求成功
                parseUserInfoJson(json);
                Log.e(TAG, "获取用户的信息===" + json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e(TAG, "获取用户的信息失败===" + e.toString());
            }

            @Override
            public void onError(Response response, int code) {
                Log.e(TAG, "获取用户的信息错误===" + response.toString() + code);
            }
        });
    }

    //解析用户的个人信息
    private void parseUserInfoJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
                String data = jsonObject.getString("data");
                userInfo = new _UserInfo(data);
                tv_changesex.setText("" + userInfo.getGender());
                tv_changecity.setText("" + userInfo.getCity());
                tv_changejieduan.setText("" + userInfo.getDecorate_type());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //设置用户的性别信息  参数 性别 1--男 0--女
    private void HttpSetSex(int sex) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("gender", sex);
        okHttpUtil.post(Constant.SET_SEX_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                //设置成功！
                Log.e(TAG, "设置用户的性别信息===" + json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e(TAG, "设置用户的性别信息失败===" + e.toString());
            }

            @Override
            public void onError(Response response, int code) {
                Log.e(TAG, "设置用户的性别信息错误===" + code + response.toString());
            }
        });
    }

    //修改用户城市信息
    private void HttpSetCity(String cityName) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        if (cityName.indexOf("市") != -1) {
            //含有“市”字
        } else {
            cityName = cityName + "市";
        }
        param.put("city_name", cityName);
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        okHttpUtil.post(Constant.SET_CITY_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                //设置成功！
                Log.e(TAG, "设置用户的城市信息===" + json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e(TAG, "设置用户的城市信息失败===" + e);
            }

            @Override
            public void onError(Response response, int code) {
                Log.e(TAG, "设置用户的城市信息错误===" + code + response.toString());
            }
        });
    }

    //设置用户的装修阶段
    private void HttpSetDecorate(int decorataType) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("decorate_type", decorataType);
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        okHttpUtil.post(Constant.SET_DECORATE_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                //设置成功！
                Log.e(TAG, "设置用户的装修阶段===" + json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e(TAG, "设置用户的装修阶段失败===" + e);
            }

            @Override
            public void onError(Response response, int code) {
                Log.e(TAG, "设置用户的装修阶段===" + code + response.toString());
            }
        });
    }

}
