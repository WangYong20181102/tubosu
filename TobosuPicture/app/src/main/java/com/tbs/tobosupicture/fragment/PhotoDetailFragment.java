package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean._PhotoDetail;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.view.TouchImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/7/27 11:05.
 * 用来呈现图片和描述的fragment
 */

public class PhotoDetailFragment extends BaseFragment {
    @BindView(R.id.frgment_textview)
    TextView frgmentTextview;
    Unbinder unbinder;
    @BindView(R.id.fragment_rl)
    RelativeLayout fragmentRl;

    private _PhotoDetail.ImageDetail mImageDetail;
    private Context mContext;
    private TouchImageView mTouchImageView;

    public PhotoDetailFragment() {

    }

//    public PhotoDetailFragment(_PhotoDetail.ImageDetail imageDetail) {
//        this.mImageDetail = imageDetail;
//    }

    public static final PhotoDetailFragment newInstance(_PhotoDetail.ImageDetail imageDetail) {
        PhotoDetailFragment newPhotoDetailFragment = new PhotoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ImageDetail", imageDetail);
        newPhotoDetailFragment.setArguments(bundle);
        return newPhotoDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null){
            this.mImageDetail= (_PhotoDetail.ImageDetail) args.getSerializable("ImageDetail");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView() {
        mTouchImageView = new TouchImageView(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mTouchImageView.setLayoutParams(layoutParams);
        fragmentRl.addView(mTouchImageView);
        GlideUtils.glideLoader(mContext, mImageDetail.getImage_url(),
                R.mipmap.loading_img_fail, R.mipmap.loading_img, mTouchImageView);

//        if (mImageDetail.getTitle().length()>=40){
//            //文字截取
//            frgmentTextview.setText(mImageDetail.getTitle().substring(0,40)+"...");
//        }else {
//            frgmentTextview.setText("" + mImageDetail.getTitle());
//        }
        frgmentTextview.setText("" + mImageDetail.getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
