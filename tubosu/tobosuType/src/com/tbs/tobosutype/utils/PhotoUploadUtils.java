package com.tbs.tobosutype.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.tbs.tobosutype.bean._ImageUpload;
import com.tbs.tobosutype.global.Constant;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr.Wang on 2018/11/24 08:58.
 */
public class PhotoUploadUtils {
    private static Gson gson;
    //上传返回的结果
    private String resultStr = "";
    private static ArrayList<String> mNetAddImageUrl = new ArrayList<>();//网络上传之后的图片集合
    private String addImageJson = ""; //图片路径
    public static PhotoUploadUtils photoUploadUtils = null;
    private static List<String> listPath;
    private static Context context;

    public void setOnPhotoUploadListener(OnPhotoUploadListener onPhotoUploadListener) {
        this.onPhotoUploadListener = onPhotoUploadListener;
    }

    private OnPhotoUploadListener onPhotoUploadListener = null;

    public static interface OnPhotoUploadListener {
        void imagePath(String path);
    }

    public static PhotoUploadUtils getInstences(Context context1, List<String> listPaths) {
        gson = new Gson();
        listPath = listPaths;
        context = context1;
        if (photoUploadUtils == null) {
            photoUploadUtils = new PhotoUploadUtils();
        }
        if (!mNetAddImageUrl.isEmpty()){
            mNetAddImageUrl.clear();
        }
        return photoUploadUtils;
    }

    /**
     * 开始图片上传
     */
    public void startUpload() {
        new Thread(uploadImageRunnable).start();
    }

    //上传图片的线程
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            for (int i = 0; i < listPath.size(); i++) {
                Map<String, String> textParams = null;
                Map<String, File> fileparams = null;
                Map<String, String> token = null;
                try {
                    URL url = new URL(Constant.UPLOAD_DYNAMIC_IMAGE);
                    textParams = new HashMap<String, String>();
                    fileparams = new HashMap<String, File>();
                    token = new HashMap<String, String>();
                    File file = new File(listPath.get(i));
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
                    } else {
                        Toast.makeText(context, "请求URL失败！", Toast.LENGTH_SHORT).show();
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
                    _ImageUpload imageUpload = gson.fromJson(resultStr, _ImageUpload.class);
                    mNetAddImageUrl.add(imageUpload.getData().getUrl());
                    if (mNetAddImageUrl.size() == listPath.size()) { //图片上传完成
                        addImageJson = Joiner.on(",").join(mNetAddImageUrl);
                        if (onPhotoUploadListener != null) {
                            onPhotoUploadListener.imagePath(addImageJson);
                        }
                    }
                    break;
            }
            return false;
        }
    });


}
