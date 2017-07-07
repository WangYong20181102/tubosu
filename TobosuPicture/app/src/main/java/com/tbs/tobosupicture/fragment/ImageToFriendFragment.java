package com.tbs.tobosupicture.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.FreeQuoteActivity;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Lin on 2017/6/29 11:10.
 * 以图会友
 */

public class ImageToFriendFragment extends BaseFragment {
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";

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

    @OnClick({R.id.into_free_quote, R.id.into_imgae})
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photoImg.setImageBitmap(photo);
        }
    }

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
