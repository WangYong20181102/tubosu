package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._HomePage;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2018/9/4 15:08.
 */
public class CaseCardViewAdapter extends PagerAdapter
        implements CardAdapter, View.OnClickListener {
    private List<CardView> mViews;
    private List<_HomePage.DataBean.CasesBean> mCasesBeanList;
    private float mBaseElevation;
    private Context mContext;


    public CaseCardViewAdapter(Context context, List<_HomePage.DataBean.CasesBean> casesBeanList) {
        this.mCasesBeanList = casesBeanList;
        this.mContext = context;
        mViews = new ArrayList<>();
        for (int i = 0; i < mCasesBeanList.size(); i++) {
            mViews.add(null);
        }
    }

    private OnItemClickLister onItemClickLister = null;

    public static interface OnItemClickLister {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mCasesBeanList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager_card_item, container, false);
        container.addView(view);
        bind(mCasesBeanList.get(position), view);
        CardView cardView = view.findViewById(R.id.card_view);
        cardView.setTag(position);
        cardView.setOnClickListener(this);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    //数据销毁
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    //绑定数据
    private void bind(_HomePage.DataBean.CasesBean casesBean, View view) {
//
        //底部图片
        ImageView item_view_card_img = view.findViewById(R.id.item_view_card_img);
        //装修类型
        ImageView item_view_card_dec_type_img = view.findViewById(R.id.item_view_card_dec_type_img);
        //大标题描述
        TextView item_view_card_commuit_name_tv = view.findViewById(R.id.item_view_card_commuit_name_tv);
        //小标题描述
        TextView item_view_card_subtitle_tv = view.findViewById(R.id.item_view_card_subtitle_tv);
        //用户头像
        ImageView item_view_card_icon_img = view.findViewById(R.id.item_view_card_icon_img);
        //底部描述
        TextView item_view_card_description_tv = view.findViewById(R.id.item_view_card_description_tv);

        //设置数据
        //底图
        Glide.with(mContext).load(casesBean.getCover_url()).into(item_view_card_img);
        //装修类型
        if (casesBean.getDesmethod().equals("全包")) {
            Glide.with(mContext).load(R.drawable.all).into(item_view_card_dec_type_img);
        } else if (casesBean.getDesmethod().equals("半包")) {
            Glide.with(mContext).load(R.drawable.half).into(item_view_card_dec_type_img);
        }
        //大标题
        item_view_card_commuit_name_tv.setText("" + casesBean.getCommunity_name());
        //小标题
        item_view_card_subtitle_tv.setText("" + casesBean.getSub_title());
        //用户头像
        GlideUtils.glideLoader(mContext, casesBean.getIcon(), R.drawable.iamge_loading,
                R.drawable.iamge_loading, item_view_card_icon_img, 0);
        //底部描述
        if (casesBean.getDescription() != null && casesBean.getDescription().length() >= 32) {
            String desc = casesBean.getDescription().substring(0, 31);
            item_view_card_description_tv.setText("" + desc + "...");
        } else {
            item_view_card_description_tv.setText(casesBean.getDescription());
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null) {
            onItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }
}
