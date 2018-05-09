package com.tbs.tbs_mj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.ChangeRongyuAdapter;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._ImageUpload;
import com.tbs.tbs_mj.bean._MyStore;
import com.tbs.tbs_mj.customview.CustomWaitDialog;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.ImgCompressUtils;
import com.tbs.tbs_mj.utils.Util;
import com.tbs.tbs_mj.utils.WriteUtil;

import org.json.JSONArray;
import org.json.JSONException;

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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class  ChangeRongYuImageActivity extends BaseActivity {

    @BindView(R.id.co_change_rongyuzizhi_dissmiss_rl)
    RelativeLayout coChangeRongyuzizhiDissmissRl;
    @BindView(R.id.co_change_rongyuzizhi_ok_rl)
    RelativeLayout coChangeRongyuzizhiOkRl;
    @BindView(R.id.co_change_rongyuzizhi_recycler)
    RecyclerView coChangeRongyuzizhiRecycler;
    private Context mContext;
    private String TAG = "ChangeRongYuImageActivity";
    private GridLayoutManager mGridLayoutManager;
    private Gson mGson;
    //原始数据jihe
    private ArrayList<_MyStore.HonorImgBean> mHonorImgBeanArrayList = new ArrayList<>();
    //原始网络图片数据
    private ArrayList<String> mFristImageUrlList = new ArrayList<>();
    //添加的图片的URL集合
    private ArrayList<String> mLocalAddImageUrl = new ArrayList<>();//本地添加的图片集合
    private ArrayList<String> mNetAddImageUrl = new ArrayList<>();//网络上传之后的图片集合
    //删除的图片的URL集合
    private ArrayList<String> mDelImageUrl = new ArrayList<>();
    //图片上传框架 图片临时处理集合
    private ArrayList<String> mTempImageArrayList = new ArrayList<>();
    //压缩图片生成的集合路径
    private ArrayList<String> mCompressImageUriPath = new ArrayList<>();
    //上个界面传来的json字符串
    private String mImageListJson = "";
    //处理数据的适配器
    private ChangeRongyuAdapter mChangeRongyuAdapter;
    //intern
    private Intent mIntent;
    private static final int REQUEST_IMAGE = 3;//图片选择器用到的code
    //现阶段有的图片数量
    private int mHaveImageNum = 0;
    //loading加载页面
    private CustomWaitDialog mCustomWaitDialog;
    //上传返回的结果
    private String resultStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_rong_yu_image);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }


    private void initEvent() {
        Log.e(TAG, "荣誉资质页面初始化=========");
        mGson = new Gson();
        mIntent = getIntent();
        mImageListJson = mIntent.getStringExtra("mImageListJson");
        Log.e(TAG, "获取上一个界面的数据=================" + mImageListJson);
        initImageData(mImageListJson);
        //处理列表数据
        mGridLayoutManager = new GridLayoutManager(mContext, 4,
                GridLayoutManager.VERTICAL, false);
        coChangeRongyuzizhiRecycler.setLayoutManager(mGridLayoutManager);
        mChangeRongyuAdapter = new ChangeRongyuAdapter(mContext, mHonorImgBeanArrayList);
        mChangeRongyuAdapter.setOnItemClickLister(onItemClickLister);
        coChangeRongyuzizhiRecycler.setAdapter(mChangeRongyuAdapter);
        mChangeRongyuAdapter.notifyDataSetChanged();
        mCustomWaitDialog = new CustomWaitDialog(mContext);
    }

    //子项的点击事件
    private ChangeRongyuAdapter.OnItemClickLister onItemClickLister = new ChangeRongyuAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            switch (view.getId()) {
                case R.id.item_suggestion_img_add:
                    //图片添加
                    showOpenPhotoPop();
                    break;
                case R.id.item_suggestion_img_rl:
                    //查看大图
                    Intent intentToLookBigImage = new Intent(mContext, BigImageLookActivity.class);
                    intentToLookBigImage.putExtra("mImageIconUrl", mHonorImgBeanArrayList.get(position).getImg_url());
                    startActivity(intentToLookBigImage);
                    break;
                case R.id.item_suggestion_close:
                    //删除图片
                    delImage(position);
                    break;
            }
        }
    };

    //删除图片的操作
    private void delImage(int position) {
        //整合数删除的数据
        mDelImageUrl.add(mHonorImgBeanArrayList.get(position).getImg_url());
        //移除了网络原始数据将扩容
        if (mFristImageUrlList.contains(mHonorImgBeanArrayList.get(position).getImg_url())) {
            mHaveImageNum = mHaveImageNum - 1;
        }
        //移除添加的图片
        if (mLocalAddImageUrl.contains(mHonorImgBeanArrayList.get(position).getImg_url())) {
            mLocalAddImageUrl.remove(mHonorImgBeanArrayList.get(position).getImg_url());
        }
        //移除框架本身含有的图片
        if (mTempImageArrayList.contains(mHonorImgBeanArrayList.get(position).getImg_url())) {
            mTempImageArrayList.remove(mHonorImgBeanArrayList.get(position).getImg_url());
        }
        //刷新数据
        mHonorImgBeanArrayList.remove(position);
        mChangeRongyuAdapter.notifyDataSetChanged();

    }


    private void initImageData(String dataJson) {
        //处理从上个界面来的数据
        if (dataJson != null && !TextUtils.isEmpty(dataJson)) {
            try {
                JSONArray jsonArray = new JSONArray(dataJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    _MyStore.HonorImgBean honorImgBean = mGson.fromJson(jsonArray.get(i).toString(), _MyStore.HonorImgBean.class);
                    mHonorImgBeanArrayList.add(honorImgBean);
                }
                for (int i = 0; i < mHonorImgBeanArrayList.size(); i++) {
                    mFristImageUrlList.add(mHonorImgBeanArrayList.get(i).getImg_url());
                }
                mHaveImageNum = mHonorImgBeanArrayList.size();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    //点击弹出照片选择器
    private void showOpenPhotoPop() {
        View popview = View.inflate(mContext, R.layout.pop_get_photo, null);
        //取消按钮
        TextView pop_get_photo_dismiss = (TextView) popview.findViewById(R.id.pop_get_photo_dismiss);
        //从相册中获取
        TextView pop_get_photo_from_pic = (TextView) popview.findViewById(R.id.pop_get_photo_from_pic);
        //整个可点击层
        RelativeLayout pop_get_photo_rl = (RelativeLayout) popview.findViewById(R.id.pop_get_photo_rl);
        //按键层
        LinearLayout pop_get_photo_11 = (LinearLayout) popview.findViewById(R.id.pop_get_photo_11);

        pop_get_photo_11.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //取消
        pop_get_photo_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //从相册中获取
        pop_get_photo_from_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/1/12  开启相册的选择器
                MultiImageSelector.create(mContext)
                        .showCamera(false)
                        .count(10 - mHaveImageNum)
                        .multi()
                        .origin(mTempImageArrayList)
                        .start(ChangeRongYuImageActivity.this, REQUEST_IMAGE);
                popupWindow.dismiss();
            }
        });
        //点击非按区界面消失
        pop_get_photo_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @OnClick({R.id.co_change_rongyuzizhi_dissmiss_rl, R.id.co_change_rongyuzizhi_ok_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_rongyuzizhi_dissmiss_rl:
                finish();
                break;
            case R.id.co_change_rongyuzizhi_ok_rl:
                //保存数据
                saveImage();
                break;
        }
    }

    //保存数据
    private void saveImage() {

        if (!mTempImageArrayList.isEmpty()) {
            //有图片新增
            //出现蒙层
            mCustomWaitDialog.show();
            if (!mCompressImageUriPath.isEmpty()) {
                mCompressImageUriPath.clear();
            }
            for (int i = 0; i < mTempImageArrayList.size(); i++) {
                mCompressImageUriPath.add(ImgCompressUtils.CompressAndGetPath(mContext, mTempImageArrayList.get(i)));
            }
            //上传图片
            new Thread(uploadImageRunnable).start();
        } else {
            //仅仅是删除了图片
            //将删除的数据发送给详情页处理
            String delImageJson = mGson.toJson(mDelImageUrl);
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_RYZZ_DEL_IMAGE, delImageJson));
            //处理 荣誉数组 (没必要处理  在点击删除按钮的时候已经处理了)
//            for (int j = 0; j < mDelImageUrl.size(); j++) {
//                if (mFristImageUrlList.contains(mDelImageUrl.get(j))) {
//                    mFristImageUrlList.remove(mDelImageUrl.get(j));
//                }
//            }
//            //生成新的荣誉资质对象数组
//            mHonorImgBeanArrayList.clear();
//            for (int i = 0; i < mFristImageUrlList.size(); i++) {
//                _MyStore.HonorImgBean honorImgBean = new _MyStore.HonorImgBean();
//                honorImgBean.setImg_url(mFristImageUrlList.get(i));
//                mHonorImgBeanArrayList.add(honorImgBean);
//            }
            //发送给详情页
            String honorListJson = mGson.toJson(mHonorImgBeanArrayList);
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_RYZZ_ALLDATE, honorListJson));
            finish();
        }

    }

    //上传图片的线程
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            for (int i = 0; i < mCompressImageUriPath.size(); i++) {
                Log.e(TAG, "要上传的照片======" + mCompressImageUriPath.get(i));
                if (TextUtils.isEmpty(Constant.UPLOAD_DYNAMIC_IMAGE)) {
                    Toast.makeText(mContext, "还没有设置上传服务器的路径！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> textParams = null;
                Map<String, File> fileparams = null;
                Map<String, String> token = null;
                try {
                    URL url = new URL(Constant.UPLOAD_DYNAMIC_IMAGE);
                    textParams = new HashMap<String, String>();
                    fileparams = new HashMap<String, File>();
                    token = new HashMap<String, String>();
                    File file = new File(mCompressImageUriPath.get(i));
                    token.put("token", Util.getDateToken());
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
                    _ImageUpload imageUpload = mGson.fromJson(resultStr, _ImageUpload.class);
                    mNetAddImageUrl.add(imageUpload.getData().getUrl());
                    Log.e(TAG, "图片上传返回的图片地址==============" + imageUpload.getData().getUrl());

                    if (mNetAddImageUrl.size() == mCompressImageUriPath.size()) {
                        //图片上传完成
                        Log.e(TAG, "图片上传返回的图片地址============上传完成===");
                        mCustomWaitDialog.dismiss();
                        Toast.makeText(mContext, "已保存！", Toast.LENGTH_SHORT).show();
                        //将处理过后的荣誉资质对象传给详情页
                        for (int i = 0; i < mTempImageArrayList.size(); i++) {
                            for (int j = 0; j < mHonorImgBeanArrayList.size(); j++) {
                                if (mTempImageArrayList.get(i).equals(mHonorImgBeanArrayList.get(j).getImg_url())) {
                                    mHonorImgBeanArrayList.remove(j);
                                }
                            }
                        }
                        for (int i = 0; i < mNetAddImageUrl.size(); i++) {
                            _MyStore.HonorImgBean honorImgBean = new _MyStore.HonorImgBean();
                            honorImgBean.setImg_url(mNetAddImageUrl.get(i));
                            mHonorImgBeanArrayList.add(honorImgBean);
                        }
                        String honorListJson = mGson.toJson(mHonorImgBeanArrayList);
                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_RYZZ_ALLDATE, honorListJson));
                        //将删除的数据发送给详情页处理
                        String delImageJson = mGson.toJson(mDelImageUrl);
                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_RYZZ_DEL_IMAGE, delImageJson));
                        //将新增的数据发送给详情页处理
                        String addImageJson = mGson.toJson(mNetAddImageUrl);
                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_RYZZ_ADD_IMAGE, addImageJson));
                        finish();
                    }
                    break;
            }
            return false;
        }
    });


    //图片上传
//    private void HttpUpLoadImage(File mImageFile) {
//        OkHttpClient client = new OkHttpClient();
//        MediaType IMG_TYPE = MediaType.parse("image/*");
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        builder.addFormDataPart("filedata", mImageFile.getName(), RequestBody.create(IMG_TYPE, mImageFile));
//        builder.addFormDataPart("token", Util.getDateToken());
//        builder.addFormDataPart("app_type", "1");
//        MultipartBody requestBody = builder.build();
//        Request request = new Request.Builder()
//                .url(Constant.UPLOAD_DYNAMIC_IMAGE)
//                .post(requestBody)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "链接失败=====" + e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = new String(response.body().string());
//                Log.e(TAG, "上传图片链接成功======" + json);
//                _ImageUpload mImageUpload;
//                mImageUpload = mGson.fromJson(json, _ImageUpload.class);
//                if (mImageUpload.getStatus() == 200) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            Toast.makeText(mContext, "图片上传成功~", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    Log.e(TAG, "上传的图片============" + mImageUpload.getData().getUrl());
//                    mNetAddImageUrl.add(mImageUpload.getData().getUrl());
//                    //所有的图片上传完毕以后 将界面销毁
//                    if (mNetAddImageUrl.size() == mCompressImageUriPath.size()) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mCustomWaitDialog.dismiss();
//                                Toast.makeText(mContext, "图片上传成功~", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        });
//                    }
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mCustomWaitDialog.dismiss();
//                            Toast.makeText(mContext, "图片上传失败~", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    //选中图片
                    ArrayList<String> imagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!mTempImageArrayList.isEmpty()) {
                        for (int i = 0; i < mTempImageArrayList.size(); i++) {
                            for (int j = 0; j < mHonorImgBeanArrayList.size(); j++) {
                                if (mTempImageArrayList.get(i).equals(mHonorImgBeanArrayList.get(j).getImg_url())) {
                                    mHonorImgBeanArrayList.remove(j);
                                }
                            }
                        }
                        mTempImageArrayList.clear();
                    }
                    mTempImageArrayList.addAll(imagePathList);
                    for (int i = 0; i < mTempImageArrayList.size(); i++) {
                        mLocalAddImageUrl.add(mTempImageArrayList.get(i));
                        _MyStore.HonorImgBean honorImgBean = new _MyStore.HonorImgBean();
                        honorImgBean.setImg_url(mTempImageArrayList.get(i));
                        mHonorImgBeanArrayList.add(honorImgBean);
                    }
                    mChangeRongyuAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
