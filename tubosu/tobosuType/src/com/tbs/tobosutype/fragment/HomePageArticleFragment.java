package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.HomePageArticleAdapter;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean._HomePage;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2018/9/5 09:27.
 * 首页的文章模块  数据从首页注入
 */
public class HomePageArticleFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.frag_hp_article_recycler)
    RecyclerView fragHpArticleRecycler;
    private Context mContext;
    private String TAG = "HomePageArticleFragment";
    private Gson mGson = new Gson();
    private String mArticleListJson;
    private GridLayoutManager mGridLayoutManager;
    private List<_HomePage.DataBean.ArticleBean> mArticleBeanList;
    private HomePageArticleAdapter mHomePageArticleAdapter;


    public HomePageArticleFragment() {

    }

    public static HomePageArticleFragment newInstance(String articleListJson) {
        HomePageArticleFragment homePageArticleFragment = new HomePageArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("articleListJson", articleListJson);
        homePageArticleFragment.setArguments(bundle);
        return homePageArticleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mArticleListJson = args.getString("articleListJson");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hp_article, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initViewEvent();
        return view;
    }

    //初始化页面
    private void initViewEvent() {
        mGridLayoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        fragHpArticleRecycler.setLayoutManager(mGridLayoutManager);
        mArticleBeanList = new ArrayList<>();
        if (!mArticleBeanList.isEmpty()) {
            mArticleBeanList.clear();
        }
        try {
            JSONArray jsonArray = new JSONArray(mArticleListJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _HomePage.DataBean.ArticleBean articleBean = mGson.fromJson(jsonArray.get(i).toString(), _HomePage.DataBean.ArticleBean.class);
                mArticleBeanList.add(articleBean);
            }
            if (mHomePageArticleAdapter == null) {
                mHomePageArticleAdapter = new HomePageArticleAdapter(mContext, mArticleBeanList);
                fragHpArticleRecycler.setAdapter(mHomePageArticleAdapter);
                mHomePageArticleAdapter.notifyDataSetChanged();
            } else {
                mHomePageArticleAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
