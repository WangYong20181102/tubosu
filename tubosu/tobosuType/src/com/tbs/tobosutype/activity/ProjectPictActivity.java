package com.tbs.tobosutype.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.MyProjectViewPagerLayout;
import com.tbs.tobosutype.fragment.ProjectListFragment;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/***
 *  从首页的精选图片的【更多】处跳转而来
 * @author dec
 *
 */
public class ProjectPictActivity extends FragmentActivity {
    private Context mContext;
    private static final String url = Constant.TOBOSU_URL + "tapp/spcailpic/get_list";
    private ImageView project_viewpager_back;

    /**
     * viewpager的线性布局
     */
    private MyProjectViewPagerLayout vpLayout;

    private MyProjectFragmentAdapter adapter;

    /**
     * fragment 页面
     */
    private ArrayList<ProjectListFragment> mTabFragments = new ArrayList<ProjectListFragment>();
    private RelativeLayout rl_banner;

    //	private String[] titleStringList = new String[] {/*"全部专题",*/ "百变空间", "局部之美", "户型设计", "风格欣赏", "装修面积", "特色系列", "工装"};
    private ArrayList<String> titleList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_projectfragment_list);
        mContext = ProjectPictActivity.this;
        initView();
        getTitleTextList();
    }

    private void initView() {
        project_viewpager_back = (ImageView) findViewById(R.id.project_viewpager_back);
        project_viewpager_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vpLayout = (MyProjectViewPagerLayout) findViewById(R.id.myviewpager_layout);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }


    private void initPager(ArrayList<String> list) {
        Bundle b = getIntent().getBundleExtra("JingXuanBundle");
        String _type = b.getString("type");

        String[] titleStringList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            titleStringList[i] = list.get(i);
        }

        for (String itemData : list) {
            ProjectListFragment f = ProjectListFragment.newInstance(itemData);
            mTabFragments.add(f);
        }

        adapter = new MyProjectFragmentAdapter(getSupportFragmentManager(), mTabFragments);
        vpLayout.setTitles(titleStringList);
        vpLayout.setAdapter(adapter, _type);
        vpLayout.ok();


    }


    /**
     * fragment 适配器
     */
    static class MyProjectFragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<ProjectListFragment> fData;

        public MyProjectFragmentAdapter(FragmentManager fm, ArrayList<ProjectListFragment> fData) {
            super(fm);
            this.fData = fData;
        }

        @Override
        public Fragment getItem(int position) {
            return fData.get(position);
        }

        @Override
        public int getCount() {
            return fData.size();
        }
    }


    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private void getTitleTextList() {
        requestQueue = Volley.newRequestQueue(mContext);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getInt("error_code") == 0) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray array = data.getJSONArray("rangeList");
                        for (int i = 0; i < array.length(); i++) {
                            titleList.add(array.getJSONObject(i).getString("range_name"));
                        }

                        if (titleList != null && titleList.size() > 0) {
                            initPager(titleList);
                        }
                    } else {
                        System.out.println(" --ProjectPictActivity-- 请求出错--");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("page", "1");
                hashMap.put("pageSize", "1");
                hashMap.put("range_name", "");
                hashMap.put("version", AppInfoUtil.getAppVersionName(mContext));
                return hashMap;
            }
        };
        stringRequest.setTag("title");
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll("title");
        }
    }
}
