package com.tbs.tobosupicture.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Lin on 2017/6/29 11:00.
 * 样板图fragment
 */

public class TemplateFragment extends BaseFragment {
    @BindView(R.id.temp_location)
    TextView tempLocation;

    public TemplateFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_template, null);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        tempLocation.setText("当前的定位城市为=======" + SpUtils.getLocationCity(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
