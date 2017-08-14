package com.tbs.tobosupicture.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.SendDynamicAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._ImageUpLoad;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.fragment.ImageToFriendFragment;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.FileUtil;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.ImageCompress;
import com.tbs.tobosupicture.utils.ImgCompressUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 发送动态页面
 * creat by lin
 */
public class SendDynamicActivity extends BaseActivity {
    @BindView(R.id.send_back_btn)
    LinearLayout sendBackBtn;
    @BindView(R.id.send_dynamic_btn)
    LinearLayout sendDynamicBtn;
    @BindView(R.id.send_dynamic_title)
    EditText sendDynamicTitle;
    @BindView(R.id.send_dynamic_recycle)
    RecyclerView sendDynamicRecycle;
    @BindView(R.id.add_dynamic)
    ImageView addDynamic;
    @BindView(R.id.img_show_pop)
    View imgShowPop;
    private Context mContext;
    private Gson mGson;
    private String TAG = "SendDynamicActivity";
    //本地的图片集 可以从上一个界面传来也可以在这个页面添加
    private ArrayList<String> mImageUriPath = new ArrayList<>();
    //压缩图片的地址 最终将压缩的图片上传
    private ArrayList<String> mCompressImageUriPath = new ArrayList<>();
    //上传图片后返回的地址集合
    private ArrayList<String> mImageUrlList = new ArrayList<>();
    //标题集合
    private ArrayList<String> mTitleList = new ArrayList<>();

    private SelectPersonalPopupWindow menuWindow;

    private Intent mIntent;
    private LinearLayoutManager mLinearLayoutManager;
    private SendDynamicAdapter sendDynamicAdapter;
    private String resultStr = "";
    //上传时候的loading
    private ProgressDialog pd;

    //TODO 调取相机要用的值
    private static final int REQUESTCODE_PICK = 0;
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private static final int REQUEST_IMAGE = 3;//图片选择器用到的code
    //存储的名称
    private static final String IMAGE_FILE_NAME = Utils.getNowTime() + "avatarImage.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_dynamic);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();//初化相关数据
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initViewEvent() {
        mGson = new Gson();
        mIntent = getIntent();
        //获取本地图片的集合
        mImageUriPath.addAll(mIntent.getStringArrayListExtra("ImageList"));
        //适配器的布局
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        //设置列表
        sendDynamicRecycle.setLayoutManager(mLinearLayoutManager);
        sendDynamicAdapter = new SendDynamicAdapter(mContext, mImageUriPath);
        sendDynamicRecycle.setAdapter(sendDynamicAdapter);

        menuWindow = new SelectPersonalPopupWindow(mContext, popClickLister);
    }

    @OnClick({R.id.send_back_btn, R.id.send_dynamic_btn, R.id.add_dynamic})
    public void onViewClickedInSendDynamicActivity(View view) {
        switch (view.getId()) {
            case R.id.send_back_btn:
                finish();
                break;
            case R.id.send_dynamic_btn:
                //将动态发出去
                sendEvent();
                break;
            case R.id.add_dynamic:
                //弹出选择框添加图片
                menuWindow.showAtLocation(imgShowPop, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

    //点击发送处理逻辑
    private void sendEvent() {
        //拿到子标题
        if (!mTitleList.isEmpty()) {
            mTitleList.clear();
        }
        //压缩图片
        for (int i = 0; i < mImageUriPath.size(); i++) {
//            Log.e(TAG, "压缩前的图片本地地址=====" + mImageUriPath.get(i)+"==大小=="+new File(mImageUriPath.get(i)).length()/1024);
            mCompressImageUriPath.add(ImgCompressUtils.CompressAndGetPath(mContext, mImageUriPath.get(i)));
//            Log.e(TAG, "压缩过后的图片本地地址=====" + mCompressImageUriPath.get(i)+"==大小=="+new File(mCompressImageUriPath.get(i)).length()/1024);
        }
        mTitleList.addAll(sendDynamicAdapter.getmTitleList());
        if (!mCompressImageUriPath.isEmpty()
                && mCompressImageUriPath.size() == mImageUriPath.size()
                && !TextUtils.isEmpty(sendDynamicTitle.getText().toString())) {
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            new Thread(uploadImageRunnable).start();
        } else {
            Toast.makeText(mContext, "标题不能为空~", Toast.LENGTH_SHORT).show();
        }
    }

    //上传图片的线程
    //上传图片的线程
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            for (int i = 0; i < mImageUriPath.size(); i++) {

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
                    File file = new File(mCompressImageUriPath.get(i));
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
        }
    };
    private Handler handlerUpload = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    _ImageUpLoad imageUpLoad = mGson.fromJson(resultStr, _ImageUpLoad.class);
                    mImageUrlList.add(imageUpLoad.getUrl());
                    if (mImageUrlList.size() == mCompressImageUriPath.size()) {
                        pd.dismiss();
                        //检测图片返回
                        for (int i = 0; i < mImageUrlList.size(); i++) {
                            Log.e(TAG, "返回的图片地址=====" + mImageUrlList.get(i));
                        }
                        HashMap<String, Object> param = new HashMap<>();
                        param.put("token", Utils.getDateToken());
                        param.put("uid", SpUtils.getUserUid(mContext));
                        param.put("title", sendDynamicTitle.getText().toString());
                        for (int j = 0; j < mImageUrlList.size(); j++) {
                            param.put("image_url" + j, mImageUrlList.get(j));
                        }
                        for (int k = 0; k < mTitleList.size(); k++) {
                            param.put("sub_title" + k, mTitleList.get(k));
                        }
                        Log.e(TAG, "参数===第一张图片======" + param.get("image_url0"));
                        HttpUtils.doPost(UrlConstans.PUBLISH_DYNAMIC, param, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = new String(response.body().string());
                                Log.e(TAG, "链接成功======" + json);
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("200")) {
                                        Log.e(TAG, "发布动态成功=====");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pd.dismiss();
                                                EventBusUtil.sendEvent(new Event(EC.EventCode.REFRESH_MY_ORGIN_NUM));
                                                finish();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, "动态发布失败，请重新发送~", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    break;
            }
            return false;
        }
    });

    private void HttpSendDynamic() {

    }

    private View.OnClickListener popClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    //开启相机 调用原生相机
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                case R.id.pickPhotoBtn:
                    //开启图册 采用图册选择框架
                    MultiImageSelector.create(mContext)
                            .showCamera(false)
                            .count(9)
                            .multi()
                            .origin(mImageUriPath)
                            .start(SendDynamicActivity.this, REQUEST_IMAGE);
                    break;
            }
        }
    };

    /**
     * 裁剪照片
     *
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

    //获取裁剪之后的图片地址
    private void getImagePath(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String urlpath = FileUtil.saveFile(mContext, Utils.getNowTime() + "temphead.jpg", photo);
            mImageUriPath.add(urlpath);
            sendDynamicAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_TAKE:
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                if (resultCode == Activity.RESULT_CANCELED) {
                    //用户取消操作
                    return;
                } else {
                    startPhotoZoom(Uri.fromFile(temp));
                }
                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    getImagePath(data);
                }
                break;
            case REQUEST_IMAGE:
                Log.e(TAG, "图片选择器=======");
                if (resultCode == RESULT_OK) {
                    ArrayList<String> imaPathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!mImageUriPath.isEmpty()) {
                        mImageUriPath.clear();
                    }
                    mImageUriPath.addAll(imaPathList);
                    Log.e(TAG, "进入图片选择器选择图片完成===最终图片的长度===" + imaPathList.size());
                    sendDynamicAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
