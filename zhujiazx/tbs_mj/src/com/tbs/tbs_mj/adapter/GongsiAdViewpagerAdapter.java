package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tbs.tbs_mj.activity.DecComActivity;
import com.tbs.tbs_mj.activity.NewWebViewActivity;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class GongsiAdViewpagerAdapter extends PagerAdapter {
    private String TAG = "GongsiAdViewpagerAdapter";
    private ArrayList<ImageView> viewList;
    private ArrayList<String> urlStrings;
    private List<String> clickId;
    private Context context;

    public GongsiAdViewpagerAdapter(Context context, ArrayList<ImageView> viewList, ArrayList<String> urlStrings, List<String> clickId) {
        this.context = context;
        this.viewList = viewList;
        this.urlStrings = urlStrings;
        this.clickId = clickId;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (position < viewList.size()) {
            ((ViewPager) container).removeView(viewList.get(position));
        }

    }

    @Override
    public Object instantiateItem(final View container, final int position) {
        ((ViewPager) container).addView(viewList.get(position));
        viewList.get(position).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (position < urlStrings.size()) {
                    setClickRequest(clickId.get(position));
                    Intent webIntent;
                    if (urlStrings.get(position).contains(Constant.TEN_YEARS_ACTIVITY)) {
                        webIntent = new Intent(context, NewWebViewActivity.class);
                    } else {
                        webIntent = new Intent(context, NewWebViewActivity.class);
                    }
                    webIntent.putExtra("mLoadingUrl", urlStrings.get(position));
                    Util.setErrorLog("装修公司viewpagerAdapter", urlStrings.get(position));
                    context.startActivity(webIntent);
                }
            }
        });
        return viewList.get(position);
    }

    @Override
    public int getCount() {
        return viewList == null ? 0 : viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void setClickRequest(String id) {
        if (Util.isNetAvailable(context)) {
            HashMap<String, Object> para = new HashMap<String, Object>();
            para.put("token", Util.getDateToken());
            para.put("id", id);
            OKHttpUtil.post(Constant.clickUrl, para, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        }
    }
}
