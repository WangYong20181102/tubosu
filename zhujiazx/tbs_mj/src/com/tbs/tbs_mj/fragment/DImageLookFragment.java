package com.tbs.tbs_mj.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseFragment;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._ImageD;
import com.tbs.tbs_mj.customview.TouchImageView;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.ImageLoaderUtil;
import com.tbs.tbs_mj.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/11/17 17:41.
 */

public class DImageLookFragment extends BaseFragment {
    @BindView(R.id.look_fragment_rl)
    RelativeLayout lookFragmentRl;
    Unbinder unbinder;
    private Context mContext;
    private TouchImageView mTouchImageView;
    private String mImageUrl;
    private String TAG = "DImageLookFragment";

    public DImageLookFragment() {

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    public static final DImageLookFragment newInstance(String mImageUrl) {
        DImageLookFragment dImageLookFragment = new DImageLookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mImageUrl", mImageUrl);
        dImageLookFragment.setArguments(bundle);
        return dImageLookFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mImageUrl = (String) args.getSerializable("mImageUrl");
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
        ImageLoaderUtil.loadImage(mContext, mTouchImageView, mImageUrl);
        mTouchImageView.setOnClickListener(onClickListener);
        mTouchImageView.setOnLongClickListener(onLongClickListener);
    }



    //点击事件  单击
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventBusUtil.sendEvent(new Event(EC.EventCode.CLICK_DIMAGE_IN_LOOK_PHOTO));

        }
    };
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            EventBusUtil.sendEvent(new Event(EC.EventCode.LOOG_CLICK_DIMAGE_IN_LOOK_PHOTO, mImageUrl));
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
