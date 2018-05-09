package com.tbs.tbs_mj.activity;
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
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.customview.MyProjectViewPagerLayout;
import com.tbs.tbs_mj.fragment.ProjectListFragment;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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



    private void getTitleTextList() {
        if(Util.isNetAvailable(mContext)){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("page", "1");
            hashMap.put("pageSize", "1");
            hashMap.put("range_name", "");
            hashMap.put("version", AppInfoUtil.getAppVersionName(mContext));
            OKHttpUtil.post(url, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String s = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                    });
                }
            });
        }
    }

}
