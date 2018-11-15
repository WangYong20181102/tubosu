package com.tbs.tobosutype.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.SelectTypeLeftAdapter;
import com.tbs.tobosutype.adapter.SelectTypeRightAdapter;
import com.tbs.tobosutype.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2018/11/13 08:57.
 */
public class SelectTypeActivity extends BaseActivity {
    @BindView(R.id.lv_left)
    ListView lvLest;
    @BindView(R.id.lv_right)
    ListView lvRight;
    private SelectTypeLeftAdapter adapterLeft;
    private SelectTypeRightAdapter adapterRight;
    private List<String> listLeft;
    private List<String> listRight;
    private String inputTittle; //标题
    private String inputContent;    //内容
    private List<String> listImagePath;//图片路径


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);
        ButterKnife.bind(this);
        initData();
        initAdapter();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        inputTittle = bundle.getString("inputTittle");
        inputContent = bundle.getString("inputContent");
        listImagePath = bundle.getStringArrayList("listImagePath");
    }

    //初始化适配器
    private void initAdapter() {
        listLeft = new ArrayList<>();
        listRight = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            listLeft.add("装修流程");
        }
        for (int i = 0; i < 20; i++) {
            listRight.add("二级分页" + i);
        }
        adapterLeft = new SelectTypeLeftAdapter(this, listLeft);
        adapterRight = new SelectTypeRightAdapter(this, listRight);
        lvLest.setAdapter(adapterLeft);
        lvRight.setAdapter(adapterRight);

        lvLest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterLeft.setSelectItem(position);
                adapterRight.setSelectItem(0);
                lvRight.setSelectionAfterHeaderView();
                lvRight.smoothScrollToPosition(0);
            }
        });
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterRight.setSelectItem(position);
            }
        });


    }

    @OnClick({R.id.tv_back, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back: //上一步
                finish();
                break;
            case R.id.tv_send: //发布

                break;
        }
    }

}
