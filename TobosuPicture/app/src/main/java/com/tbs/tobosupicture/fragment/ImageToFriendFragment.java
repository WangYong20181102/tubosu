package com.tbs.tobosupicture.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.FindPasswordActivity;
import com.tbs.tobosupicture.activity.FreeQuoteActivity;
import com.tbs.tobosupicture.activity.MyFansActivity;
import com.tbs.tobosupicture.activity.MyFriendsActivity;
import com.tbs.tobosupicture.activity.RegisterActivity;
import com.tbs.tobosupicture.activity.SystemActivity;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.ImgCompressUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Mr.Lin on 2017/6/29 11:10.
 * 以图会友
 */

public class ImageToFriendFragment extends BaseFragment {

    @BindView(R.id.photo_img2)
    ImageView photoImg2;
    @BindView(R.id.into_share)
    TextView intoShare;
    @BindView(R.id.into_thr_login)
    TextView intoThrLogin;
    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();

    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private static final int REQUEST_IMAGE = 3;//图片选择器用到的code
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";
    private UMShareAPI mShareAPI;


    @BindView(R.id.into_photo)
    TextView intoPhoto;
    @BindView(R.id.into_img)
    TextView intoImg;
    private Context mContext;
    @BindView(R.id.photo_img)
    ImageView photoImg;
    @BindView(R.id.into_free_quote)
    TextView intoFreeQuote;
    @BindView(R.id.into_imgae)
    TextView intoImgae;
    private String TAG = "ImageToFriendFragment";

    public ImageToFriendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_tofriend, null);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView() {
//        GlideUtils.glideLoader(mContext,"/storage/emulated/0/DCIM/Camera/IMG_20170706_152728.jpg",0,0,photoImg);
        mShareAPI = UMShareAPI.get(mContext);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.into_free_quote, R.id.into_imgae, R.id.into_img, R.id.into_photo, R.id.into_share, R.id.into_thr_login})
    public void onViewClickedAtImageToFriendFragment(View view) {
        switch (view.getId()) {
            case R.id.into_free_quote:
                Log.e(TAG, "点击了进入要测试的Activity");
                startActivity(new Intent(getActivity(), MyFriendsActivity.class));
                break;
            case R.id.into_imgae:
                //启动相机进行拍照处理
                // 拍照获取头像
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                break;
            case R.id.into_img:
                //点击进入相册
                MultiImageSelector.create(mContext)
                        .showCamera(true)
                        .count(9)
                        .multi()
                        .origin(imageList)
                        .start(ImageToFriendFragment.this, REQUEST_IMAGE);
                break;
            case R.id.into_photo:
                //点击进行相机拍照处理
                showPopWindow();
                break;
            case R.id.into_share:
                new ShareAction(getActivity()).withText("这只是一个分享")
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener).open();
                break;
            case R.id.into_thr_login:
                mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Log.e(TAG, "微信授权成功!===" + map.get("openid"));
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Log.e(TAG, "微信授权失败！===" + i + "===" + throwable.toString());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    /**
     * 显示popwindow 选择去相册拿照片还是去拍照拿相片
     */
    private void showPopWindow() {
        View popView = View.inflate(mContext, R.layout.view_popwindow_photo, null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);//开启相册的选择
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);//开启照相机的选择
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);//取消
        //获取屏幕的宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popupWindow = new PopupWindow(popView, weight, height);
        popupWindow.setFocusable(true);
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入相册选择
                MultiImageSelector.create(mContext)
                        .showCamera(true)
                        .count(9)
                        .multi()
                        .origin(imageList)
                        .start(ImageToFriendFragment.this, REQUEST_IMAGE);
            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入相机拍摄
            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消popwindow显示
                popupWindow.dismiss();
            }
        });

        //popwindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        //popwindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);
    }

    /**
     * 处理选择完照片的逻辑
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                String newPath = "";
                //原始图片
                File file = new File(path.get(0));
                //压缩的图片
                File newFile = new File(ImgCompressUtils.CompressAndGetPath(mContext, path.get(0)));
                newPath = ImgCompressUtils.CompressAndGetPath(mContext, path.get(0));
                Log.e(TAG, "压缩前图片的大小=" + (file.length() / 1024) + "压缩后的图片大小=" + newFile.length() / 1024);
                for (int i = 0; i < path.size(); i++) {
                    File files = new File(path.get(i));
                    Log.e(TAG, "选择的图片地址=" + path.get(i) + "当前文件的name=" + files.getName() + "当前文件大小=" + files.length() / 1024);
                }
                GlideUtils.glideLoader(mContext, path.get(0), 0, 0, photoImg);
                GlideUtils.glideLoader(mContext, newPath, 0, 0, photoImg2);
//                HttpUtils.doFile(UrlConstans.UPLOAD_IMAGE, path.get(0), file.getName(), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, "链接失败" + e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e(TAG, "连接成功" + response.body().string());
//                    }
//                });
                //下面这个是测试用的
//                Intent intent = new Intent(getActivity(), FreeQuoteActivity.class);
//                intent.putStringArrayListExtra("ImageList", path);
//                startActivity(intent);

            }
        }
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
                    setPicToView(data);
                }
                break;
        }
    }

    /**
     * 设置照片到指定的位置
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photoImg.setImageBitmap(photo);
        }
    }

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

}
