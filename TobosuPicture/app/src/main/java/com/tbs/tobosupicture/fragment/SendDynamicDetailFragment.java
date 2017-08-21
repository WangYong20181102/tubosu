package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/8/17 17:28.
 */

public class SendDynamicDetailFragment extends BaseFragment {
    @BindView(R.id.fragment_sdd_img)
    ImageView fragmentSddImg;
    Unbinder unbinder;

    public SendDynamicDetailFragment() {

    }

    private Context mContext;
    private String mImageUri;

    public static final SendDynamicDetailFragment newInstance(String mImageUri) {
        SendDynamicDetailFragment sendDynamicDetailFragment = new SendDynamicDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mImageUri", mImageUri);
        sendDynamicDetailFragment.setArguments(bundle);
        return sendDynamicDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mImageUri = args.getString("mImageUri");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_dynamic_detail, null);
        mContext = getActivity();
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        GlideUtils.glideLoader(mContext,mImageUri,R.mipmap.loading_img_fail,R.mipmap.loading_img,fragmentSddImg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
