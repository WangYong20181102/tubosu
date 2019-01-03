package com.tbs.tobosutype.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.HistoryRecordAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.base.HistoryRecordBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2019/1/2 13:57.
 */
public class HistoryRecordActivity extends BaseActivity implements HistoryRecordAdapter.OnItenClickListener {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.rv_history_record)
    RecyclerView rvHistoryRecord;
    @BindView(R.id.srl_history_record)
    SwipeRefreshLayout dqSwipe;
    @BindView(R.id.image_bottom_icon)
    ImageView imageBottomIcon;
    @BindView(R.id.rl_all_select)
    RelativeLayout rlAllSelect;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tv_all_select)
    TextView tvAllSelect;
    @BindView(R.id.image_no_data)
    ImageView imageNoData;
    private HistoryRecordAdapter adapter;
    private LinearLayoutManager manager;
    private List<HistoryRecordBean> recordBeanList;
    private boolean editorStatus = false;//编辑状态
    private int index = 0;
    private boolean isSelectAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyrecord);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        recordBeanList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            HistoryRecordBean bean = new HistoryRecordBean();
            bean.setCheck(false);
            bean.setData("2019.1.1" + "-->" + i);
            bean.setValue("100" + "-->" + i);
            bean.setNum("1000" + "-num->" + i);
            recordBeanList.add(bean);
        }
        //下拉刷新
        dqSwipe.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        dqSwipe.setBackgroundColor(Color.WHITE);
        dqSwipe.setSize(SwipeRefreshLayout.DEFAULT);
//        dqSwipe.setOnRefreshListener(onRefreshListener);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistoryRecord.setLayoutManager(manager);

        adapter = new HistoryRecordAdapter(this, recordBeanList);
        adapter.setOnItenClickListener(this);
        rvHistoryRecord.setAdapter(adapter);


//        rvHistoryRecord.setOnTouchListener(onTouchListener);
//        rvHistoryRecord.setOnScrollListener(onScrollListener);

    }

    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            initData();
        }
    };
    //上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (adapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && manager.findLastVisibleItemPosition() + 1 == adapter.getItemCount()) {
                    LoadMore();
                }
            }
        }
    };

    /**
     * 加载更多
     */
    private void LoadMore() {

    }

    //touch
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (dqSwipe.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };


    @OnClick({R.id.rlBack, R.id.tv_edit, R.id.rl_all_select, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
                finish();
                break;
            case R.id.tv_edit:  //编辑
                String text = tvEdit.getText().toString().trim();
                if (text.equals("编辑")) {
                    tvEdit.setText("完成");
                    rlBottom.setVisibility(View.VISIBLE);
                    adapter.isEditState(true);
                    editorStatus = true;
                } else {
                    tvEdit.setText("编辑");
                    rlBottom.setVisibility(View.GONE);
                    adapter.isEditState(false);
                    editorStatus = false;
                    isSelectAll = true;
                    selectAll();
                }
                break;
            case R.id.rl_all_select:    //全选
                selectAll();
                break;
            case R.id.btn_delete:   //删除
                deleteData();
                break;
        }
    }

    /**
     * 删除
     */
    private void deleteData() {
        if (index == 0) {
            return;
        }
        for (int i = adapter.getHistoryRecordList().size(); i > 0; i--) {
            HistoryRecordBean bean = adapter.getHistoryRecordList().get(i - 1);
            if (bean.isCheck()) {
                adapter.getHistoryRecordList().remove(bean);
            }
        }
        index = 0;
        imageBottomIcon.setImageResource(R.drawable.edit_unselected);
        adapter.notifyDataSetChanged();
        if (isSelectAll) {
            imageNoData.setVisibility(View.VISIBLE);
            rvHistoryRecord.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            tvEdit.setText("编辑");
            tvEdit.setClickable(false);
            dqSwipe.setVisibility(View.GONE);
        } else {
            rlBottom.setVisibility(View.VISIBLE);
            imageNoData.setVisibility(View.GONE);
            rvHistoryRecord.setVisibility(View.VISIBLE);
            tvEdit.setClickable(true);
            dqSwipe.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 全选取消全选
     */
    private void selectAll() {
        if (adapter == null) {
            return;
        }
        if (!isSelectAll) {
            for (int i = 0; i < adapter.getHistoryRecordList().size(); i++) {
                adapter.getHistoryRecordList().get(i).setCheck(true);
            }
            index = adapter.getHistoryRecordList().size();
            tvAllSelect.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0; i < adapter.getHistoryRecordList().size(); i++) {
                adapter.getHistoryRecordList().get(i).setCheck(false);
            }
            index = 0;
            tvAllSelect.setText("全选");
            isSelectAll = false;
        }
        if (index == 0) {
            imageBottomIcon.setImageResource(R.drawable.edit_unselected);
        } else {
            imageBottomIcon.setImageResource(R.drawable.edit_selected);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickListener(int position, List<HistoryRecordBean> beanList) {
        if (editorStatus) {
            HistoryRecordBean bean = beanList.get(position);
            boolean isCheck = bean.isCheck();
            if (!isCheck) {
                index++;
                bean.setCheck(true);
                if (index == beanList.size()) {
                    isSelectAll = true;
                    tvAllSelect.setText("取消全选");
                }
            } else {
                index--;
                bean.setCheck(false);
                isSelectAll = false;
                tvAllSelect.setText("全选");
            }
            if (index == 0) {
                imageBottomIcon.setImageResource(R.drawable.edit_unselected);
            } else {
                imageBottomIcon.setImageResource(R.drawable.edit_selected);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
