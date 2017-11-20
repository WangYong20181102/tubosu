package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._ImageD;
import com.tbs.tobosutype.customview.TouchImageView;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;

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
    private String TAG = "SImageLookFragment";

    public DImageLookFragment() {

    }

    @Override
    protected boolean isRegisterEventBus() {
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
        unbinder.unbind();
    }
}
