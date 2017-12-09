package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean._CompanyDetail;
import com.tbs.tobosutype.customview.TouchImageView;
import com.tbs.tobosutype.utils.ImageLoaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/12/6 14:44.
 */

public class CoLookPhotoFragment extends BaseFragment {
    @BindView(R.id.look_fragment_rl)
    RelativeLayout lookFragmentRl;
    Unbinder unbinder;
    private Context mContext;
    private TouchImageView mTouchImageView;
    private _CompanyDetail.QualificationBean mQualificationBean;//数据源
    private String TAG = "CoLookPhotoFragment";

    public CoLookPhotoFragment() {

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    public static final CoLookPhotoFragment newInstance(_CompanyDetail.QualificationBean qualificationBean) {
        CoLookPhotoFragment coPhotoFragment = new CoLookPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mQualificationBean", qualificationBean);
        coPhotoFragment.setArguments(bundle);
        return coPhotoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mQualificationBean = (_CompanyDetail.QualificationBean) args.getSerializable("mQualificationBean");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_look_photo, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView() {
        mTouchImageView = new TouchImageView(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mTouchImageView.setLayoutParams(layoutParams);
        lookFragmentRl.addView(mTouchImageView);
        ImageLoaderUtil.loadImage(mContext, mTouchImageView, mQualificationBean.getImg_url());
//        mTouchImageView.setOnClickListener(onClickListener);
//        mTouchImageView.setOnLongClickListener(onLongClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
