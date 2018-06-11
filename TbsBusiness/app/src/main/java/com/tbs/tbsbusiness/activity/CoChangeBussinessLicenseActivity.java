package com.tbs.tbsbusiness.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._ImageUpload;
import com.tbs.tbsbusiness.bean._MyStore;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.util.EventBusUtil;
import com.tbs.tbsbusiness.util.FileUtil;
import com.tbs.tbsbusiness.util.GetImagePath;
import com.tbs.tbsbusiness.util.GlideUtils;
import com.tbs.tbsbusiness.util.Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * 修改营业执照
 */
public class CoChangeBussinessLicenseActivity extends BaseActivity {

    @BindView(R.id.co_change_yingyezhizhao_dissmiss_rl)
    RelativeLayout coChangeYingyezhizhaoDissmissRl;
    @BindView(R.id.co_change_yingyezhizhao_ok_rl)
    RelativeLayout coChangeYingyezhizhaoOkRl;
    @BindView(R.id.co_change_yingyezhizhao_add_rl)
    RelativeLayout coChangeYingyezhizhaoAddRl;
    @BindView(R.id.co_change_yingyezhizhao_image_img)
    ImageView coChangeYingyezhizhaoImageImg;
    @BindView(R.id.co_change_yingyezhizhao_imgclose_img)
    ImageView coChangeYingyezhizhaoImgcloseImg;
    @BindView(R.id.co_change_yingyezhizhao_image_rl)
    RelativeLayout coChangeYingyezhizhaoImageRl;
    @BindView(R.id.co_change_yingyezhizhao_time_rl)
    RelativeLayout coChangeYingyezhizhaoTimeRl;
    @BindView(R.id.co_change_yingyezhizhao_num_et)
    EditText coChangeYingyezhizhaoNumEt;
    @BindView(R.id.co_change_yingyezhizhao_name_et)
    EditText coChangeYingyezhizhaoNameEt;
    @BindView(R.id.co_change_yingyezhizhao_time_tv)
    TextView coChangeYingyezhizhaoTimeTv;
    @BindView(R.id.co_change_yingyezhizhao_num_clean_iv)
    ImageView coChangeYingyezhizhaoNumCleanIv;
    @BindView(R.id.co_change_yingyezhizhao_name_clean_iv)
    ImageView coChangeYingyezhizhaoNameCleanIv;
    private Context mContext;
    private String TAG = "CoChangeBussinessLicenseActivity";
    private Intent mIntent;
    private String mYingYeZhiZhaoJson = "";
    private _MyStore.BusinessLicenseBean mBusinessLicenseBean;
    private Gson mGson;
    private File mGalleryFile;//相册文件
    private File mCropFile;//切图文件
    private final int RESULT_REQUEST_CODE = 2;
    private final int SELECT_PIC_KITKAT = 3;
    private final int IMAGE_REQUEST_CODE = 4;
    private Uri uritempFile;
    private _ImageUpload mImageUpload;
    private String mImageUrl = "";//图片地址
    //上传文件名称
    private static final String MGALLERYFILE_NAME = Util.getNowTime() + "mgalleryfile_name.jpg";
    private static final String MCROPFILE_NAME = Util.getNowTime() + "mcropfile_name.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_change_bussiness_license);
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
        mGalleryFile = new File(Environment.getExternalStorageDirectory(), MGALLERYFILE_NAME);
        mCropFile = new File(Environment.getExternalStorageDirectory(), MCROPFILE_NAME);
        mYingYeZhiZhaoJson = mIntent.getStringExtra("mYingYeZhiZhaoJson");
        mBusinessLicenseBean = mGson.fromJson(mYingYeZhiZhaoJson, _MyStore.BusinessLicenseBean.class);
        initView(mBusinessLicenseBean);
        //字号名称修改
        coChangeYingyezhizhaoNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    coChangeYingyezhizhaoNameCleanIv.setVisibility(View.GONE);
                } else {
                    coChangeYingyezhizhaoNameCleanIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //营业执照号码清除
        coChangeYingyezhizhaoNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    coChangeYingyezhizhaoNumCleanIv.setVisibility(View.GONE);
                } else {
                    coChangeYingyezhizhaoNumCleanIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView(_MyStore.BusinessLicenseBean businessLicenseBean) {
//        if (businessLicenseBean != null) {
        //布局数据
        //图片
        mImageUrl = businessLicenseBean.getImg_url();
        if (businessLicenseBean.getImg_url() != null && !TextUtils.isEmpty(businessLicenseBean.getImg_url())) {
            //隐藏加载图片的按钮
            coChangeYingyezhizhaoAddRl.setVisibility(View.GONE);
            //显示图片
            coChangeYingyezhizhaoImageRl.setVisibility(View.VISIBLE);
            GlideUtils.glideLoader(mContext, businessLicenseBean.getImg_url(), R.drawable.iamge_loading,
                    R.drawable.iamge_loading, coChangeYingyezhizhaoImageImg);
        } else {
            //图片为空
            coChangeYingyezhizhaoAddRl.setVisibility(View.VISIBLE);
            coChangeYingyezhizhaoImageRl.setVisibility(View.GONE);
        }
        //文本信息
        //有效期
        Log.e(TAG, "有效期=================" + businessLicenseBean.getEffect_time());
        if (businessLicenseBean.getEffect_time() != null && !TextUtils.isEmpty(businessLicenseBean.getEffect_time())) {
            coChangeYingyezhizhaoTimeTv.setText("" + businessLicenseBean.getEffect_time());
        } else {
            coChangeYingyezhizhaoTimeTv.setText("");
        }
        //注册号
        if (businessLicenseBean.getRegistration_number() != null && !TextUtils.isEmpty(businessLicenseBean.getRegistration_number())) {
            coChangeYingyezhizhaoNumEt.setText("" + businessLicenseBean.getRegistration_number());
        } else {
            coChangeYingyezhizhaoNumEt.setText("");
        }
        //字号名称
        if (businessLicenseBean.getFont_name() != null && !TextUtils.isEmpty(businessLicenseBean.getFont_name())) {
            coChangeYingyezhizhaoNameEt.setText("" + businessLicenseBean.getFont_name());
        } else {
            coChangeYingyezhizhaoNameEt.setText("");
        }

//        }
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
            Uri uriForFile = FileProvider.getUriForFile(mContext, "com.tbs.tbsbusiness.fileprovider", mGalleryFile);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
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


    //删除图片
    private void cleanImage() {
        //将图片项隐藏
        coChangeYingyezhizhaoImageRl.setVisibility(View.GONE);
        //将添加框显示
        coChangeYingyezhizhaoAddRl.setVisibility(View.VISIBLE);
        //将图片的url置空
        mImageUrl = "";
    }

    //查看大图
    private void lookBigImage(String imageUrl) {
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            Intent intent = new Intent(mContext, BigImageLookActivity.class);
            Log.e(TAG, "大图的原图url=============" + imageUrl);
            intent.putExtra("mImageIconUrl", imageUrl);
            mContext.startActivity(intent);
        }
    }

    @OnClick({R.id.co_change_yingyezhizhao_dissmiss_rl,
            R.id.co_change_yingyezhizhao_ok_rl,
            R.id.co_change_yingyezhizhao_add_rl,
            R.id.co_change_yingyezhizhao_imgclose_img,
            R.id.co_change_yingyezhizhao_image_rl,
            R.id.co_change_yingyezhizhao_time_rl,
            R.id.co_change_yingyezhizhao_num_clean_iv,
            R.id.co_change_yingyezhizhao_name_clean_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_yingyezhizhao_dissmiss_rl:
                //取消
                finish();
                break;
            case R.id.co_change_yingyezhizhao_ok_rl:
                //保存
                saveData();
                break;
            case R.id.co_change_yingyezhizhao_add_rl:
                //开启照片选择器
                showOpenPhotoPop();
                break;
            case R.id.co_change_yingyezhizhao_imgclose_img:
                //删除图片
                cleanImage();
                break;
            case R.id.co_change_yingyezhizhao_image_rl:
                //查看大图
                lookBigImage(mBusinessLicenseBean.getImg_url());
                break;
            case R.id.co_change_yingyezhizhao_time_rl:
                //修改营业执照的时间
                //如果键盘弹出然键盘隐藏
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                showTiemPicker();
                break;
            case R.id.co_change_yingyezhizhao_num_clean_iv:
                //清除营业执照号码
                coChangeYingyezhizhaoNumEt.setText("");
                coChangeYingyezhizhaoNumCleanIv.setVisibility(View.GONE);
                break;
            case R.id.co_change_yingyezhizhao_name_clean_iv:
                //清除营业执照号码
                coChangeYingyezhizhaoNameEt.setText("");
                coChangeYingyezhizhaoNameCleanIv.setVisibility(View.GONE);
                break;
        }
    }

    //提交保存数据
    private void saveData() {
        //营业执照照片
        if (mImageUrl != null && !TextUtils.isEmpty(mImageUrl)) {
            mBusinessLicenseBean.setImg_url("" + mImageUrl);
        } else {
            mBusinessLicenseBean.setImg_url("");
        }

        //设置有效期
        mBusinessLicenseBean.setEffect_time("" + coChangeYingyezhizhaoTimeTv.getText().toString());
        //设置注册号
        mBusinessLicenseBean.setRegistration_number("" + coChangeYingyezhizhaoNumEt.getText().toString());
        //设置字号名称
        mBusinessLicenseBean.setFont_name("" + coChangeYingyezhizhaoNameEt.getText().toString());
        //将数据发送
        mYingYeZhiZhaoJson = mGson.toJson(mBusinessLicenseBean);
        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_YINGYEZHIZHAO_ALLDATE, mYingYeZhiZhaoJson));
        finish();
    }

    //展示时间选择器
    private void showTiemPicker() {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
//                Toast.makeText(TimeOrCitySelectActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                //设置选择的时间
                coChangeYingyezhizhaoTimeTv.setText("" + getTime(date));
            }
        }).setCancelColor(Color.parseColor("#333333"))
                .setTitleSize(15)
                .setSubmitText("确定")
                .setSubmitColor(Color.parseColor("#333333"))
                .setBgColor(Color.parseColor("#ffffff"))
                .build();

        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
                Uri dataUri = FileProvider.getUriForFile(mContext, "com.tbs.tbsbusiness.fileprovider", imgUri);
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
                    coChangeYingyezhizhaoAddRl.setVisibility(View.GONE);
                    //显示图片框
                    coChangeYingyezhizhaoImageRl.setVisibility(View.VISIBLE);
                    //加载图片
                    GlideUtils.glideLoaderUriImage(mContext, uritempFile, R.drawable.iamge_loading, R.drawable.iamge_loading, coChangeYingyezhizhaoImageImg);
                    File file = FileUtil.getFileByUri(uritempFile, mContext);
                    Log.e(TAG, "获取转换的File大小===========" + file.length());
                    //上传文件
                    HttpUpLoadImage(file);
                }
                break;
        }
    }
}
