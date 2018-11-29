package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.AnswerItemDetailsActivity;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/6 16:23.
 */
public class AnswerDetailsViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> integerList;
    private AnswerItemDetailsActivity activity;

    public AnswerDetailsViewPagerAdapter(Context context, List<String> integerList) {
        this.context = context;
        this.integerList = integerList;
        activity = (AnswerItemDetailsActivity) context;
    }

    public void setActivityBases(List<String> integerList) {
        this.integerList = integerList;
    }

    @Override
    public int getCount() {
        return integerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), R.layout.fragment_viewpager, null);
        ImageView imageView = view.findViewById(R.id.image_viewPager_item);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_viewpager_item);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.hindViewPagerImage();
            }
        });
        GlideUtils.glideLoader(context, integerList.get(position), imageView,"");
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
