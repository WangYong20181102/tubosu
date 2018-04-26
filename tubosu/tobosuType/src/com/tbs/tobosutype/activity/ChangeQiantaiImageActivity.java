package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._ImageUpload;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.FileUtil;
import com.tbs.tobosutype.utils.GetImagePath;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.Util;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 修改前台图片
 */

public class ChangeQiantaiImageActivity extends BaseActivity {

    @BindView(R.id.co_change_qtimage_dissmiss_rl)
    RelativeLayout coChangeQtimageDissmissRl;
    @BindView(R.id.co_change_qtimage_ok_rl)
    RelativeLayout coChangeQtimageOkRl;
    @BindView(R.id.co_change_qtimage_add_rl)
    RelativeLayout coChangeQtimageAddRl;
    @BindView(R.id.co_change_qtimage_image_img)
    ImageView coChangeQtimageImageImg;
    @BindView(R.id.co_change_qtimage_imgclose_img)
    ImageView coChangeQtimageImgcloseImg;
    @BindView(R.id.co_change_qtimage_image_rl)
    RelativeLayout coChangeQtimageImageRl;
    private String TAG = "ChangeQiantaiImageActivity";
    private Context mContext;
    private Intent mIntent;
    private String mImageUrl = "";//图片地址
    private final int RESULT_REQUEST_CODE = 2;
    private final int SELECT_PIC_KITKAT = 3;
    private final int IMAGE_REQUEST_CODE = 4;
    private File mGalleryFile;//相册文件
    private File mCropFile;//切图文件
    private Uri uritempFile;
    //上传文件名称
    private static final String MGALLERYFILE_NAME = Util.getNowTime() + "mgalleryfile_name.jpg";
    private static final String MCROPFILE_NAME = Util.getNowTime() + "mcropfile_name.jpg";
    private Gson mGson;
    private _ImageUpload mImageUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_qiantai_image);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        //创建文件
        mGalleryFile = new File(Environment.getExternalStorageDirectory(), MGALLERYFILE_NAME);
        mCropFile = new File(Environment.getExternalStorageDirectory(), MCROPFILE_NAME);

        //获取显示的图片
        mImageUrl = mIntent.getStringExtra("mImageUrl");
        if (mImageUrl != null && !TextUtils.isEmpty(mImageUrl)) {
            //隐藏添加按钮
            coChangeQtimageAddRl.setVisibility(View.GONE);
            //显示图片
            coChangeQtimageImageRl.setVisibility(View.VISIBLE);
            //加载原有的图片
            GlideUtils.glideLoader(mContext, mImageUrl, R.drawable.iamge_loading, R.drawable.iamge_loading, coChangeQtimageImageImg);
        } else {
            //隐藏添加按钮
            coChangeQtimageAddRl.setVisibility(View.VISIBLE);
            //显示图片
            coChangeQtimageImageRl.setVisibility(View.GONE);
        }
    }

    //删除图片
    private void cleanImage() {
        //将图片项隐藏
        coChangeQtimageImageRl.setVisibility(View.GONE);
        //将添加框显示
        coChangeQtimageAddRl.setVisibility(View.VISIBLE);
        //将图片的url置空
        mImageUrl = "";
    }

    //查看大图
    private void lookBigImage(String imageUrl) {
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            Intent intent = new Intent(mContext, BigIconLookActivity.class);
            Log.e(TAG, "大图的原图url=============" + imageUrl);
            intent.putExtra("mImageIconUrl", imageUrl);
            mContext.startActivity(intent);
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
                getPhotoFormPictrue();
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

    //开启相册选取照片
    private void getPhotoFormPictrue() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            Uri uriForFile = FileProvider.getUriForFile(mContext, "com.tbs.tobosutype.fileprovider", mGalleryFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, SELECT_PIC_KITKAT);
        } else {
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        }

    }

    //裁剪照片
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
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 600);
        intent.putExtra("return-data", false);
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + Util.getNowTime() + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        startActivityForResult(intent, RESULT_REQUEST_CODE);//这里就将裁剪后的图片的Uri返回了
    }

    //图片上传
    private void HttpUpLoadImage(File mImageFile) {
        OkHttpClient client = new OkHttpClient();
        MediaType IMG_TYPE = MediaType.parse("image/*");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("filedata", mImageFile.getName(), RequestBody.create(IMG_TYPE, mImageFile));
        builder.addFormDataPart("token", Util.getDateToken());
        builder.addFormDataPart("app_type", "1");
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(Constant.UPLOAD_DYNAMIC_IMAGE)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "上传图片链接成功======" + json);
                mImageUpload = mGson.fromJson(json, _ImageUpload.class);
                if (mImageUpload.getStatus() == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "图片上传成功~", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mImageUrl = mImageUpload.getData().getUrl();

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "图片上传失败~", Toast.LENGTH_SHORT).show();
                            cleanImage();
                        }
                    });
                }

            }
        });
    }

    //确定保存logo
    private void okChangeLogo() {
        if (mImageUrl == null || TextUtils.isEmpty(mImageUrl)) {
            //图片为空 修改外部参数设置删除之前的图片  置空id以及图片
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CLEAN_QIANTAI_LOGO));
            finish();
        } else {
            //通知外部界面将数据修改
            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_QIANTAI_LOGO, mImageUrl));
            finish();
        }
    }

    @OnClick({R.id.co_change_qtimage_dissmiss_rl, R.id.co_change_qtimage_ok_rl,
            R.id.co_change_qtimage_add_rl, R.id.co_change_qtimage_image_img, R.id.co_change_qtimage_imgclose_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_qtimage_dissmiss_rl:
                //取消
                finish();
                break;
            case R.id.co_change_qtimage_ok_rl:
                //确定保存
                okChangeLogo();
                break;
            case R.id.co_change_qtimage_add_rl:
                //图片添加
                showOpenPhotoPop();
                break;
            case R.id.co_change_qtimage_image_img:
                //点击查看大图
                lookBigImage(mImageUrl);
                break;
            case R.id.co_change_qtimage_imgclose_img:
                //删除图片
                cleanImage();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SELECT_PIC_KITKAT://版本>= 7.0
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                File imgUri = new File(GetImagePath.getPath(mContext, data.getData()));
                Uri dataUri = FileProvider.getUriForFile(mContext, "com.tbs.tobosutype.fileprovider", imgUri);
                startPhotoZoom(dataUri);
                break;
            case IMAGE_REQUEST_CODE://版本<7.0  图库后返回
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case RESULT_REQUEST_CODE:
//                Log.e(TAG, "裁剪成功===============");
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                //设置数据
                if (data != null) {
                    //隐藏选项框
                    coChangeQtimageAddRl.setVisibility(View.GONE);
                    //显示图片框
                    coChangeQtimageImageRl.setVisibility(View.VISIBLE);
                    //加载图片
                    GlideUtils.glideLoaderUriImage(mContext, uritempFile, R.drawable.iamge_loading, R.drawable.iamge_loading, coChangeQtimageImageImg);
                    File file = FileUtil.getFileByUri(uritempFile, mContext);
                    Log.e(TAG, "获取转换的File大小===========" + file.length());
                    //上传文件
                    HttpUpLoadImage(file);
                }
                break;
        }
    }
}
