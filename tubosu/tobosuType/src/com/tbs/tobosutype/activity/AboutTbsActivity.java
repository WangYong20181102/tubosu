package com.tbs.tobosutype.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutTbsActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.about_tbs_back)
    RelativeLayout aboutTbsBack;
    @BindView(R.id.banner_dever)
    View bannerDever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_tbs);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.about_tbs_back)
    public void onViewClickedInAboutTbsActivity() {
        finish();
    }
}
