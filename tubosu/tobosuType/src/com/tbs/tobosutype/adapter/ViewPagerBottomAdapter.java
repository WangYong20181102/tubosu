package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.ToastUtil;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/15 10:48.
 */
class ViewPagerBottomAdapter extends PagerAdapter {

    private Context context;
    private List<String> strImageUrlList;

    public ViewPagerBottomAdapter(Context context, List<String> strImageUrlList) {
        this.context = context;
        this.strImageUrlList = strImageUrlList;
    }

    @Override
    public int getCount() {
        if (strImageUrlList.size() != 0) {
            return Integer.MAX_VALUE;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final int newPosition = position % strImageUrlList.size();
        View view = View.inflate(container.getContext(), R.layout.ask_ad_viewpager, null);
        ImageView imageView = view.findViewById(R.id.image_bottom);
        GlideUtils.glideLoader(context, strImageUrlList.get(newPosition), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(context, "点击" + newPosition);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
