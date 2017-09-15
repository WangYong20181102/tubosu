package com.tbs.tobosupicture.fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.DecorateCompanyCaseActivity;
import com.tbs.tobosupicture.activity.LoginActivity;
import com.tbs.tobosupicture.activity.MyCaseListActivity;
import com.tbs.tobosupicture.activity.MyDesignerListActivity;
import com.tbs.tobosupicture.activity.MyDynamicActivity;
import com.tbs.tobosupicture.activity.MyFansActivity;
import com.tbs.tobosupicture.activity.MyFriendsActivity;
import com.tbs.tobosupicture.activity.MySampleListActivity;
import com.tbs.tobosupicture.activity.OwnerCaseActivity;
import com.tbs.tobosupicture.activity.PersonInfoActivity;
import com.tbs.tobosupicture.activity.ShareWeixinActivity;
import com.tbs.tobosupicture.activity.SystemActivity;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._HomePage;
import com.tbs.tobosupicture.bean._ImageUpLoad;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.FileUtil;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.utils.WriteUtil;
import com.tbs.tobosupicture.view.SelectPersonalPopupWindow;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/6/29 11:11.
 * 我的 fragment  这个界面分为装修公司的页面和业主页面
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.fm_head_bg)
    ImageView fmHeadBg;
    @BindView(R.id.fm_user_icon)
    ImageView fmUserIcon;
    @BindView(R.id.fm_user_name)
    TextView fmUserName;
    @BindView(R.id.fm_dongtai_num)
    TextView fmDongtaiNum;
    @BindView(R.id.fm_dongtai_ll)
    LinearLayout fmDongtaiLl;
    @BindView(R.id.fm_tumi_num)
    TextView fmTumiNum;
    @BindView(R.id.fm_tumi_ll)
    LinearLayout fmTumiLl;
    @BindView(R.id.fm_tuyou_num)
    TextView fmTuyouNum;
    @BindView(R.id.fm_tuyou_ll)
    LinearLayout fmTuyouLl;
    @BindView(R.id.fm_sousuoguo_anli)
    RelativeLayout fmSousuoguoAnli;
    @BindView(R.id.fm_guanzhu_shejishi)
    RelativeLayout fmGuanzhuShejishi;
    @BindView(R.id.fm_shoucang_xiaoguotu)
    RelativeLayout fmShoucangXiaoguotu;
    @BindView(R.id.fm_shoucang_anli)
    RelativeLayout fmShoucangAnli;
    @BindView(R.id.fm_guanfang_weixin)
    RelativeLayout fmGuanfangWeixin;
    @BindView(R.id.fm_kefu_dianhua)
    RelativeLayout fmKefuDianhua;
    @BindView(R.id.fm_shezhi)
    RelativeLayout fmShezhi;
    @BindView(R.id.text3)
    TextView text3;  // 搜索过的历史记录
    @BindView(R.id.fm_co_icon)
    ImageView fmCoIcon;//公司图标
    @BindView(R.id.fm_co_name)
    TextView fmCoName;//公司名称
    @BindView(R.id.fm_co_address)
    TextView fmCoAddress;//公司地址
    @BindView(R.id.fm_co_info)
    LinearLayout fmCoInfo;//整个公司信息层
    @BindView(R.id.fm_user_sign)
    TextView fmUserSign;
    @BindView(R.id.fm_pop_location)
    View fmPopLocation;
    Unbinder unbinder;
    @BindView(R.id.fm_user_icon_ll)
    LinearLayout fmUserIconLl;
    @BindView(R.id.mf_01)
    ImageView mf01;
    @BindView(R.id.mf_02)
    ImageView mf02;
    @BindView(R.id.mf_03)
    ImageView mf03;
    @BindView(R.id.mf_04)
    ImageView mf04;
    @BindView(R.id.mf_05)
    ImageView mf05;
    @BindView(R.id.mf_06)
    ImageView mf06;
    @BindView(R.id.mf_07)
    ImageView mf07;
    @BindView(R.id.mine_reddot)
    TextView mineReddot;

    private Context mContext;
    private String TAG = "MineFragment";
    private Gson mGson;

    //开启图片调取
    private SelectPersonalPopupWindow menuWindow;

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

    public MineFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        mGson = new Gson();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utils.userIsLogin(mContext)) {
            //更新用户的信息
            if (SpUtils.getUserType(mContext).equals("3")) {
                //装修公司的界面
                fmCoInfo.setVisibility(View.VISIBLE);
                fmUserName.setVisibility(View.GONE);
                fmUserIcon.setVisibility(View.INVISIBLE);
                fmUserIcon.setClickable(false);
                fmUserSign.setVisibility(View.GONE);
                fmUserIconLl.setVisibility(View.INVISIBLE);
                text3.setText("同城用户搜索的案例");
            } else {
                //业主的页面
                fmCoInfo.setVisibility(View.GONE);
                fmUserName.setVisibility(View.VISIBLE);
                fmUserIcon.setVisibility(View.VISIBLE);
                fmUserSign.setVisibility(View.VISIBLE);
                fmUserIconLl.setVisibility(View.VISIBLE);
                fmUserIcon.setClickable(true);
                text3.setText("搜索过的装修案例");
            }
            HttpGetMineInfo();
        } else {
            //当前没有用户登录过显示默认
            fmUserSign.setVisibility(View.GONE);
            fmCoInfo.setVisibility(View.GONE);
            fmUserName.setVisibility(View.VISIBLE);
            fmUserIconLl.setVisibility(View.VISIBLE);
            fmUserIcon.setVisibility(View.VISIBLE);
            fmUserIcon.setClickable(true);
            fmUserName.setText("未登录");
            fmUserIcon.setImageResource(R.mipmap.weidenglu);
            fmTumiNum.setText("0");
            fmTuyouNum.setText("0");
            fmDongtaiNum.setText("0");
            text3.setText("搜索过的装修案例");
            Glide.with(mContext).load(R.mipmap.me_bg).into(fmHeadBg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case EC.EventCode.SHOW_MINE_RED_DOT:
                //显示红点
                mineReddot.setVisibility(View.VISIBLE);
                break;
            case EC.EventCode.HINT_MINE_RED_DOT:
                //隐藏红点
                mineReddot.setVisibility(View.GONE);
                break;
            case EC.EventCode.USER_LOGIN_TYPE:
                String type = (String) event.getData();
                if("1".equals(type)){
                    // 业主
                    text3.setText("搜索过的装修案例");
                }else if("3".equals(type)) {
                    // 公司
                    text3.setText("同城用户搜索的案例");
                }
                break;
        }
    }

    @OnClick({R.id.fm_head_bg, R.id.fm_user_icon, R.id.fm_dongtai_ll, R.id.fm_tumi_ll,
            R.id.fm_tuyou_ll, R.id.fm_sousuoguo_anli, R.id.fm_guanzhu_shejishi,
            R.id.fm_shoucang_xiaoguotu, R.id.fm_shoucang_anli, R.id.fm_guanfang_weixin,
            R.id.fm_kefu_dianhua, R.id.fm_shezhi, R.id.fm_co_icon})
    public void onViewClickedInMineFragment(View view) {
        switch (view.getId()) {
            case R.id.fm_head_bg:
                //点击更换背景
                if (Utils.userIsLogin(mContext)) {
                    changeBg();
                }
                break;
            case R.id.fm_user_icon:
                //TODO 判断用户登录的情况   用户头像点击事件
                if (Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, PersonInfoActivity.class);
                    intent.putExtra("from", "MineFragment");
                    mContext.startActivity(intent);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.fm_co_icon:
                //TODO 判断用户登录的情况   用户头像点击事件
                if (Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, PersonInfoActivity.class);
                    intent.putExtra("from", "MineFragment");
                    mContext.startActivity(intent);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.fm_dongtai_ll:
                //进入用户的动态
                if (Utils.userIsLogin(mContext)) {
                    Intent intent1 = new Intent(mContext, MyDynamicActivity.class);
                    mContext.startActivity(intent1);
                } else {
                    Utils.gotoLogin(mContext);
                }

                break;
            case R.id.fm_tumi_ll:
                //进入我的图谜
                if (Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, MyFansActivity.class);
                    mContext.startActivity(intent);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.fm_tuyou_ll:
                //点击进入我的图友
                if (Utils.userIsLogin(mContext)) {
                    Intent intent = new Intent(mContext, MyFriendsActivity.class);
                    mContext.startActivity(intent);
                } else {
                    gotoLogin();
                }
                break;
            case R.id.fm_sousuoguo_anli:
                //点击展开搜索过的案例
                if ("1".equals(SpUtils.getUserType(mContext))) {
                    // 业主
                    mContext.startActivity(new Intent(mContext, OwnerCaseActivity.class));
                } else if ("3".equals(SpUtils.getUserType(mContext))) {
                    // 公司
                    mContext.startActivity(new Intent(mContext, DecorateCompanyCaseActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

                break;
            case R.id.fm_guanzhu_shejishi:
                //点击进入关注的设计师
                if (Utils.userIsLogin(mContext)) {
                    mContext.startActivity(new Intent(mContext, MyDesignerListActivity.class));
                } else {
                    Utils.gotoLogin(mContext);
                }

                break;
            case R.id.fm_shoucang_xiaoguotu:
                //点击进入收藏的效果图
                if (Utils.userIsLogin(mContext)) {
                    mContext.startActivity(new Intent(mContext, MySampleListActivity.class));
                } else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.fm_shoucang_anli:
                //点击进入收藏的案例
                if (Utils.userIsLogin(mContext)) {
                    mContext.startActivity(new Intent(mContext, MyCaseListActivity.class));
                } else {
                    Utils.gotoLogin(mContext);
                }
                break;
            case R.id.fm_guanfang_weixin:
                //分享官方微信
                Intent intentToweixin = new Intent(mContext, ShareWeixinActivity.class);
                mContext.startActivity(intentToweixin);
                break;
            case R.id.fm_kefu_dianhua:
                //开启电话
                call("4006062221");
                break;
            case R.id.fm_shezhi:
                //进入系统设置
                Intent intent = new Intent(mContext, SystemActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }

    //更换背景
    private void changeBg() {
        menuWindow = new SelectPersonalPopupWindow(mContext, popClickLister);
        menuWindow.showAtLocation(fmPopLocation, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener popClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    //开启相机
                    if (Build.BRAND.equals("Xiaomi")) {
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
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
            }
        }
    };

    /***
     * 裁剪图片
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
            urlpath = FileUtil.saveFile(mContext, Utils.getNowTime() + "temphead.jpg", photo);
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            // 开启线程上传
            new Thread(uploadImageRunnable).start();
        }
    }

    //上传图片的线程
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
                textParams.put("s_code", "app");
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
                    _ImageUpLoad imageUpLoad = mGson.fromJson(resultStr, _ImageUpLoad.class);
                    HttpChangeUserBg(imageUpLoad.getUrl());
                    Log.e(TAG, "获取的结果=====" + resultStr + "图片的地址===" + imageUpLoad.getUrl());
                    GlideUtils.glideLoader(mContext, imageUpLoad.getUrl(), R.mipmap.me_bg,
                            R.mipmap.me_bg, fmHeadBg);
                    break;
            }
            return false;
        }
    });

    //网络修改封面
    private void HttpChangeUserBg(String url) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("is_virtual_user", "2");
        param.put("cover_url", url);
        HttpUtils.doPost(UrlConstans.MODIFY_COVER_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        Log.e(TAG, "上传成功====");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //拨打客服电话
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //去登录页面
    private void gotoLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    //获取数据
    private void HttpGetMineInfo() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        HttpUtils.doPost(UrlConstans.MINE_HOME_PAGE, param, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //成功
                        String data = jsonObject.getString("data");
                        final _HomePage homePage = mGson.fromJson(data, _HomePage.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initView(homePage);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //布局
    private void initView(_HomePage homePage) {
        //封面背景
        if (!TextUtils.isEmpty(homePage.getCover_url())) {
            GlideUtils.glideLoader(mContext, homePage.getCover_url(), R.mipmap.me_bg, R.mipmap.me_bg, fmHeadBg);
        } else {
            Glide.with(mContext).load(R.mipmap.me_bg).into(fmHeadBg);
        }

        //昵称
        fmUserName.setText(homePage.getNick());//业主
        fmCoName.setText(homePage.getNick());//公司
        //头像
        GlideUtils.glideLoader(mContext, homePage.getIcon(), R.mipmap.loading_img_fail, R.mipmap.loading_img, fmUserIcon, 0);//业主
        GlideUtils.glideLoader(mContext, homePage.getIcon(), 0, 0, fmCoIcon);
        //业主个性签名
        if (TextUtils.isEmpty(homePage.getPersonal_signature())) {
            fmUserSign.setText("未填写");
        } else {
            fmUserSign.setText(homePage.getPersonal_signature());
        }
        //动态数
        fmDongtaiNum.setText(homePage.getDynamic_count());
        //图谜数
        fmTumiNum.setText(homePage.getFollowed_count());
        //图友
        fmTuyouNum.setText(homePage.getFollow_count());
        //公司的地址
        fmCoAddress.setText(homePage.getProvince_name() + homePage.getCity_name() + homePage.getAddress());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_TAKE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
//                if (resultCode == 0) {
//                    Intent it = new Intent(mContext, PersonInfoActivity.class);
//                    startActivity(it);
//                } else {
                startPhotoZoom(Uri.fromFile(temp));
//                }
                break;
            case REQUESTCODE_CUTTING:
                if (resultCode == Activity.RESULT_CANCELED) {
                    //用户取消操作
                    return;
                }
                if (data != null) {
                    setPicToView(data);
                }
                break;
            case REQUESTCODE_PICK:
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case REQUESTCODE_XIAO_MI_TAKE:
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
