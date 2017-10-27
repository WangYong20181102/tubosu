package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._DecoCaseDetail;
import com.tbs.tobosutype.customview.TouchImageView;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/10/26 17:19.
 */

public class LookPhotoFragment extends BaseFragment {
    @BindView(R.id.look_fragment_rl)
    RelativeLayout lookFragmentRl;
    Unbinder unbinder;
    private Context mContext;
    private TouchImageView mTouchImageView;
    private _DecoCaseDetail.SpaceInfoBean mSpaceInfoBean;//数据源
    private String TAG = "LookPhotoFragment";

    public LookPhotoFragment() {

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    public static final LookPhotoFragment newInstance(_DecoCaseDetail.SpaceInfoBean spaceInfoBean) {
        LookPhotoFragment photoFragment = new LookPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mSpaceInfoBean", spaceInfoBean);
        photoFragment.setArguments(bundle);
        return photoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mSpaceInfoBean = (_DecoCaseDetail.SpaceInfoBean) args.getSerializable("mSpaceInfoBean");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        ImageLoaderUtil.loadImage(mContext, mTouchImageView, mSpaceInfoBean.getImg_url());
        mTouchImageView.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventBusUtil.sendEvent(new Event(EC.EventCode.CLICK_IMAGE_IN_LOOK_PHOTO));
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
