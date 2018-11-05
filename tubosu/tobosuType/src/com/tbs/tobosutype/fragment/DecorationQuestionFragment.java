package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorationQuestionFragmentAdapter;
import com.tbs.tobosutype.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Wang on 2018/11/5 13:46.
 */
public class DecorationQuestionFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.fragment_dq_rv)
    RecyclerView fragmentDqRv;
    private DecorationQuestionFragmentAdapter decorationQuestionFragmentAdapter;
    private List<String> list = new ArrayList<>();
    private Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decorationquestion,null);
        unbinder = ButterKnife.bind(this,view);
        context = getActivity();
        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        list.add("设计");
        list.add("设计1");
        list.add("设计2");
        list.add("设计3");
        list.add("设计4");
        list.add("设计5");
        list.add("设计6");
        list.add("设计7");
        list.add("设计8");
        list.add("设计9");
        list.add("设计10");
        decorationQuestionFragmentAdapter = new DecorationQuestionFragmentAdapter(getActivity(),list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentDqRv.setLayoutManager(layoutManager);
        fragmentDqRv.setAdapter(decorationQuestionFragmentAdapter);
        decorationQuestionFragmentAdapter.notifyDataSetChanged();
    }

    public static DecorationQuestionFragment newInstance(){

        DecorationQuestionFragment decorationQuestionFragment = new DecorationQuestionFragment();
        return decorationQuestionFragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
