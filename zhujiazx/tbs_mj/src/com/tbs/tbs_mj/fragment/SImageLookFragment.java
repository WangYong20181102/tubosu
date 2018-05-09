package com.tbs.tbs_mj.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseFragment;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._ImageS;
import com.tbs.tbs_mj.customview.TouchImageView;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.ImageLoaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/11/16 18:07.
 * 单图查看子项
 */

public class SImageLookFragment extends BaseFragment {
    @BindView(R.id.look_fragment_rl)
    RelativeLayout lookFragmentRl;
    Unbinder unbinder;
    private Context mContext;
    private TouchImageView mTouchImageView;
    private _ImageS mImageS;
    private String TAG = "SImageLookFragment";

    public SImageLookFragment() {

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    public static final SImageLookFragment newInstance(_ImageS mImageS) {
        SImageLookFragment sImageLookFragment = new SImageLookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mImageS", mImageS);
        sImageLookFragment.setArguments(bundle);
        return sImageLookFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mImageS = (_ImageS) args.getSerializable("mImageS");
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

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "SImageLookFragment执行onResume方法=======" + this.mImageS.getImage_url()+"====当前可见状态==="+getUserVisibleHint());
    }

    private void initView() {
        mTouchImageView = new TouchImageView(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mTouchImageView.setLayoutParams(layoutParams);
        lookFragmentRl.addView(mTouchImageView);
        ImageLoaderUtil.loadImage(mContext, mTouchImageView, mImageS.getImage_url());
        mTouchImageView.setOnClickListener(onClickListener);
        mTouchImageView.setOnLongClickListener(onLongClickListener);
    }

    //点击事件  单击
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventBusUtil.sendEvent(new Event(EC.EventCode.CLICK_SIMAGE_IN_LOOK_PHOTO));

        }
    };
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            EventBusUtil.sendEvent(new Event(EC.EventCode.LOOG_CLICK_SIMAGE_IN_LOOK_PHOTO, mImageS.getImage_url()));
            return true;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.clear(mTouchImageView);
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.clear(mTouchImageView);
    }
}
