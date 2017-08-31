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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.NewSendDynamicAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._DynamicImageUpload;
import com.tbs.tobosupicture.bean._ImageUpLoad;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.FileUtil;
import com.tbs.tobosupicture.utils.HttpUtils;
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
 * 新的发布动态页面
 * 仿微博模式
 * creat by lin
 */
public class NewSendDynamicActivity extends BaseActivity {
    @BindView(R.id.new_send_back_btn)
    LinearLayout newSendBackBtn;
    @BindView(R.id.new_send_dynamic_btn)
    LinearLayout newSendDynamicBtn;
    @BindView(R.id.new_send_dynamic_recycler)
    RecyclerView newSendDynamicRecycler;
    @BindView(R.id.new_send_edit_text)
    EditText newSendEditText;
    @BindView(R.id.new_send_show_pop)
    View newSendShowPop;

    private Context mContext;
    private String TAG = "NewSendDynamicActivity";
    private Intent mIntent;
    private ProgressDialog pd;
    private Gson mGson;

    //上个界面传来的本地图片地址
    private ArrayList<String> mImageUriList = new ArrayList<>();
    //压缩图片的地址集合
    private ArrayList<String> mCompressImageUriPath = new ArrayList<>();
    //上传至图片服务器的url地址集合
    private ArrayList<String> mUpLoadImageUrlList = new ArrayList<>();
    //单张图片描述的标题集合  用于上传
    private ArrayList<String> mTitleList = new ArrayList<>();
    //单张图的描述对应关系
    private HashMap<String, String> mTitleMap = new HashMap<>();
    //网格布局的manager
    private GridLayoutManager mGridLayoutManager;
    //适配器
    private NewSendDynamicAdapter mNewSendDynamicAdapter;
    //上传返回的结果
    private String resultStr = "";
    //弹出的窗口
    private SelectPersonalPopupWindow menuWindow;

    //TODO 调取相机要用的值
    private static final int REQUESTCODE_PICK = 0;
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private static final int REQUEST_IMAGE = 3;//图片选择器用到的code
    private static final int REQUESTCODE_XIAO_MI_TAKE = 4;//小米专用拍照获取码
    //存储的名称
    private static final String IMAGE_FILE_NAME = Utils.getNowTime() + "avatarImage.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_send_dynamic);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.SEND_TITLE_MAP:
                mTitleMap.clear();
                mTitleMap.putAll((Map<? extends String, ? extends String>) event.getData());
                for (int i = 0; i < mImageUriList.size(); i++) {
                    Log.e(TAG, "遍历map中=====" + mTitleMap.get(mImageUriList.get(i)));
                }
                break;
        }
    }

    private void initViewEvent() {
        mGson = new Gson();
        mIntent = getIntent();
        //获取本地图片的集合
        mImageUriList.addAll(mIntent.getStringArrayListExtra("ImageList"));
        //布局管理
        mGridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        newSendDynamicRecycler.setLayoutManager(mGridLayoutManager);
        mNewSendDynamicAdapter = new NewSendDynamicAdapter(mContext, mImageUriList);
        mNewSendDynamicAdapter.setOnImageViewClickLister(onImageViewClickLister);
        newSendDynamicRecycler.setAdapter(mNewSendDynamicAdapter);

        menuWindow = new SelectPersonalPopupWindow(mContext, popClickLister);
    }

    @OnClick({R.id.new_send_back_btn, R.id.new_send_dynamic_btn})
    public void onViewClickedInNewSendDynamicActivity(View view) {
        switch (view.getId()) {
            case R.id.new_send_back_btn:
                finish();
                break;
            case R.id.new_send_dynamic_btn:
                //发送按钮  获取标题集合
                if (!TextUtils.isEmpty(newSendEditText.getText().toString()) && !mImageUriList.isEmpty()) {
                    pd = ProgressDialog.show(mContext, null, "正在发布动态，请稍后...");
                    HttpSendDynamic();
                } else {
                    Toast.makeText(mContext, "标题或图片不能为空~", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void HttpSendDynamic() {
        if (!mTitleList.isEmpty()) {
            mTitleList.clear();
        }
        for (int i = 0; i < mImageUriList.size(); i++) {
            if (mTitleMap.get(mImageUriList.get(i)) == null) {
                mTitleList.add("");
            } else {
                mTitleList.add(mTitleMap.get(mImageUriList.get(i)));
            }
            Log.e(TAG, "发送动态前遍历标题=======" + mTitleList.get(i));
        }
        //压缩图片
        if (!mCompressImageUriPath.isEmpty()) {
            mCompressImageUriPath.clear();
        }
        for (int i = 0; i < mImageUriList.size(); i++) {
            mCompressImageUriPath.add(ImgCompressUtils.CompressAndGetPath(mContext, mImageUriList.get(i)));
        }
        new Thread(uploadImageRunnable).start();
    }

    //上传图片的线程
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            for (int i = 0; i < mCompressImageUriPath.size(); i++) {
                Log.e(TAG, "要上传的照片======" + mCompressImageUriPath.get(i));
                if (TextUtils.isEmpty(UrlConstans.UPLOAD_DYNAMIC_IMAGE)) {
                    Toast.makeText(mContext, "还没有设置上传服务器的路径！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> textParams = null;
                Map<String, File> fileparams = null;
                Map<String, String> token = null;
                try {
                    URL url = new URL(UrlConstans.UPLOAD_DYNAMIC_IMAGE);
                    textParams = new HashMap<String, String>();
                    fileparams = new HashMap<String, File>();
                    token = new HashMap<String, String>();
                    File file = new File(mCompressImageUriPath.get(i));
                    token.put("token", Utils.getDateToken());
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
                    WriteUtil.writeStringParams(token, ds);
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
                        Log.e(TAG, "上传操作之后的结果======" + resultStr);
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
                    _DynamicImageUpload dynamicImageUpload = mGson.fromJson(resultStr, _DynamicImageUpload.class);
                    mUpLoadImageUrlList.add(dynamicImageUpload.getData().getUrl());
                    if (mUpLoadImageUrlList.size() == mCompressImageUriPath.size()) {
                        pd.dismiss();
                        //检测图片返回
                        for (int i = 0; i < mUpLoadImageUrlList.size(); i++) {
                            Log.e(TAG, "返回的图片地址=====" + mUpLoadImageUrlList.get(i));
                        }
                        HashMap<String, Object> param = new HashMap<>();
                        param.put("token", Utils.getDateToken());
                        param.put("uid", SpUtils.getUserUid(mContext));
                        param.put("title", newSendEditText.getText().toString());
                        for (int j = 0; j < mUpLoadImageUrlList.size(); j++) {
                            param.put("image_url" + j, mUpLoadImageUrlList.get(j));
                        }
                        for (int k = 0; k < mTitleList.size(); k++) {
                            param.put("sub_title" + k, mTitleList.get(k));
                        }
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
                                                Toast.makeText(mContext, "发布动态成功~", Toast.LENGTH_SHORT).show();
                                                EventBusUtil.sendEvent(new Event(EC.EventCode.REFRESH_MY_ORGIN_NUM));
                                                EventBusUtil.sendEvent(new Event(EC.EventCode.REFRESH_MY_ORGIN));
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


    private View.OnClickListener popClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    //开启相机 调用原生相机
                    if (android.os.Build.BRAND.equals("Xiaomi")) {
                        //小米专用
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
                    //开启图册 采用图册选择框架
                    MultiImageSelector.create(mContext)
                            .showCamera(false)
                            .count(9)
                            .multi()
                            .origin(mImageUriList)
                            .start(NewSendDynamicActivity.this, REQUEST_IMAGE);
                    break;
            }
        }
    };
    private NewSendDynamicAdapter.OnImageViewClickLister onImageViewClickLister = new NewSendDynamicAdapter.OnImageViewClickLister() {
        @Override
        public void onImageClick(View view, int position) {
            switch (view.getId()) {
                case R.id.item_new_send_dynamic_close:
                    Log.e(TAG, "点击了删除图片=====" + position);
                    //进行删除事件
                    mImageUriList.remove(position);
                    mNewSendDynamicAdapter.notifyItemRemoved(position);
                    mNewSendDynamicAdapter.notifyItemRangeChanged(position, mImageUriList.size());
                    break;
                case R.id.item_new_send_dynamic_edit:
                    Log.e(TAG, "点击了编辑图片=====" + position);
                    //跳转到图片编辑页
                    if (mTitleMap.isEmpty()) {
                        //创建
                        for (int i = 0; i < mImageUriList.size(); i++) {
                            mTitleMap.put(mImageUriList.get(i), "");
                        }
                    }
                    Intent intent = new Intent(mContext, SendDynamicDetailActivity.class);
                    intent.putStringArrayListExtra("mImageUriList", mImageUriList);
                    intent.putExtra("mPosition", position);
                    intent.putExtra("mTitleMap", mTitleMap);
                    mContext.startActivity(intent);
                    break;
                case R.id.item_new_send_dynamic_add:
                    Log.e(TAG, "点击了添加图片=====" + position);
                    //开启popwindow 开始添加图片
                    menuWindow.showAtLocation(newSendShowPop, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
            mImageUriList.add(urlpath);
            mNewSendDynamicAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_TAKE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    //用户取消操作
                    return;
                } else {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(temp));
                }
                break;
            case REQUESTCODE_CUTTING:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (data != null) {
                    getImagePath(data);
                }
                break;
            case REQUEST_IMAGE:
                //图片选择器处理回调
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    ArrayList<String> imaPathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!mImageUriList.isEmpty()) {
                        mImageUriList.clear();
                    }
                    mImageUriList.addAll(imaPathList);
                    mNewSendDynamicAdapter.notifyDataSetChanged();
                }
                break;
            case REQUESTCODE_XIAO_MI_TAKE:
                //小米专用拍照
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (data != null) {
                    if (data.getExtras() != null) {
                        Bundle bundle = data.getExtras();
                        Bitmap photo = (Bitmap) bundle.get("data");
                        if (photo != null) {
                            String ImgPath = FileUtil.saveFile(mContext, Utils.getNowTime() + "lin_zxkk.jpg", photo);
                            mImageUriList.add(ImgPath);
                        }
                    }
                }
                break;
        }
    }
}
