package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.SelectPicPopupWindow;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ShareUtil;

/**
 * 装修公司的网店设置
 *
 * @author dec
 */
public class NetworkSetActivity extends Activity implements OnClickListener {

    private ImageView networkset_back;
    private TextView networkset_company;
    private TextView networkset_companyname;
    private TextView networkset_district;
    private TextView networkset_mapmarker;
    private TextView tv_iscertification_network;
    private ImageView iv_membership_grade_network;
    private String lng;
    private String name;
    private String lat;
    private String nickname;
    private String district;
    private RelativeLayout networkset_markers_layout;
    private RelativeLayout rel_check_home_page;
    private RelativeLayout rl_share_network;
    private String id;
    private String memberdegree;
    private String cityname;
    private String shareUrl;
    private String certification;
    private SelectPicPopupWindow popupWindow;
    private RelativeLayout rl_banner;

    private String title = "我在土拨鼠网发发现一家不错的装修公司，推荐给大家";


    /**
     * memberdegree string 1：非会员，2：高级会员，3：初级会员，4：广告会员，5：延期会员,9:高级会员,10:钻石会员,11:皇冠会员
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_networkset);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        networkset_back = (ImageView) findViewById(R.id.networkset_back);
        iv_membership_grade_network = (ImageView) findViewById(R.id.iv_membership_grade_network);
        networkset_company = (TextView) findViewById(R.id.networkset_company);
        tv_iscertification_network = (TextView) findViewById(R.id.tv_iscertification_network);
        networkset_companyname = (TextView) findViewById(R.id.networkset_companyname);
        networkset_district = (TextView) findViewById(R.id.networkset_district);
        networkset_mapmarker = (TextView) findViewById(R.id.networkset_mapmarker);
        networkset_markers_layout = (RelativeLayout) findViewById(R.id.networkset_markers_layout);
        rl_share_network = (RelativeLayout) findViewById(R.id.rl_share_network);
        rel_check_home_page = (RelativeLayout) findViewById(R.id.rel_check_home_page);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        lng = getIntent().getExtras().getString("lng");
        name = getIntent().getExtras().getString("name");
        lat = getIntent().getExtras().getString("lat");

        nickname = getIntent().getExtras().getString("nickname");
        district = getIntent().getExtras().getString("district");
        id = getIntent().getExtras().getString("id");
        memberdegree = getIntent().getExtras().getString("memberdegree");
        cityname = getIntent().getExtras().getString("cityname");
        shareUrl = getIntent().getExtras().getString("shareUrl");
        certification = getIntent().getExtras().getString("certification");
        networkset_company.setText(nickname);
        networkset_companyname.setText(name);
        networkset_district.setText(cityname + "-" + district);

        if (lat != null && lng != null) {
            networkset_mapmarker.setTextColor(Color.parseColor("#01ca5f"));
            networkset_mapmarker.setText("已标记 ");
        } else {
            networkset_mapmarker.setTextColor(Color.parseColor("#e90000"));
            networkset_mapmarker.setText("未标记 ");
        }
        if ("1".equals(certification)) {
            tv_iscertification_network.setTextColor(Color.parseColor("#01ca5f"));
            tv_iscertification_network.setText("已认证");
        } else {
            tv_iscertification_network.setTextColor(Color.parseColor("#e90000"));
            tv_iscertification_network.setText("未认证");
        }

        if (!"".equals(memberdegree) && memberdegree != null) {
            switch (Integer.parseInt(memberdegree)) {
                case 1:
                    iv_membership_grade_network.setBackgroundResource(R.drawable.img_not_member);
                    break;
                case 2:
                case 9:
                    iv_membership_grade_network.setBackgroundResource(R.drawable.img_senior_member);
                    break;
                case 3:
                    iv_membership_grade_network.setBackgroundResource(R.drawable.img_junior_member);
                    break;
                case 4:
                    iv_membership_grade_network.setBackgroundResource(R.drawable.img_ad_member);
                    break;
                case 5:
                    iv_membership_grade_network.setBackgroundResource(R.drawable.img_yq_member);
                    break;

                case 10:
                    // 钻石
                    iv_membership_grade_network.setBackgroundResource(R.drawable.img_diamon);
                    break;
                case 11:
                    // 皇冠
                    iv_membership_grade_network.setBackgroundResource(R.drawable.thrown);
                    break;

                default:
                    break;
            }
        }

    }

    private void initEvent() {
        networkset_back.setOnClickListener(this);
        networkset_markers_layout.setOnClickListener(this);
        rel_check_home_page.setOnClickListener(this);
        rl_share_network.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rel_check_home_page:
                Intent detailIntent = new Intent(this, DecorateCompanyDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("comid", id);
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
                break;
            case R.id.networkset_back:
                finish();
                break;
            case R.id.rl_share_network:
                new ShareUtil(NetworkSetActivity.this, rl_share_network, title, title, shareUrl);
                break;
            case R.id.networkset_markers_layout:
                Intent intent = new Intent(NetworkSetActivity.this, MapMarkersActivity.class);
                Bundle mapMarkersBundle = new Bundle();
                mapMarkersBundle.putString("lng", lng);
                mapMarkersBundle.putString("lat", lat);
                intent.putExtras(mapMarkersBundle);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

}
