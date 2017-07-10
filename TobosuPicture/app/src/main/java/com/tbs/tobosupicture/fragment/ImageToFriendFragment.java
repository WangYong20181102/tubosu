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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.FreeQuoteActivity;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();

    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private static final int REQUEST_IMAGE = 3;
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.into_free_quote, R.id.into_imgae, R.id.into_img, R.id.into_photo})
    public void onViewClickedAtImageToFriendFragment(View view) {
        switch (view.getId()) {
            case R.id.into_free_quote:
                Log.e(TAG, "点击了进入智能报价");
                startActivity(new Intent(getActivity(), FreeQuoteActivity.class));
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
                break;
        }
    }

    /**
     * 显示popwindow 选择去相册拿照片还是去拍照拿相片
     */
    private void showPopWindow() {

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
                Intent intent = new Intent();
                intent.putStringArrayListExtra("list", path);
                for (int i = 0; i < path.size(); i++) {
                    Log.e(TAG, "选择的图片地址====" + path.get(i));
                }
                GlideUtils.glideLoader(mContext, path.get(0), 0, 0, photoImg);
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
