package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.SendDDFragmentAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.fragment.SendDynamicDetailFragment;
import com.tbs.tobosupicture.utils.EventBusUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布动态 是点击图片编辑进入的页面
 */
public class SendDynamicDetailActivity extends BaseActivity {
    @BindView(R.id.new_send_detail_back_btn)
    LinearLayout newSendDetailBackBtn;
    @BindView(R.id.send_dynamic_detail_num)
    TextView sendDynamicDetailNum;
    @BindView(R.id.new_send_dynamic_detail_ok)
    LinearLayout newSendDynamicDetailOk;
    @BindView(R.id.new_send_dynamic_viewpager)
    ViewPager newSendDynamicViewpager;
    @BindView(R.id.send_dynamic_edit)
    EditText sendDynamicEdit;
    private Context mContext;
    private String TAG = "SendDynaDetlActivity";
    private Intent mIntent;
    private ArrayList<String> mImageUriList = new ArrayList<>();


    private int mPosition;//图片的显示位置
    private SendDDFragmentAdapter sendDDFragmentAdapter;
    private HashMap<String, String> mTitleMap = new HashMap<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private int mViewPagerPosition = 0;//viewpager显示的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_dynamic_detail);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        initEditTextShow(mPosition);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    private void initViewEvent() {
        mIntent = getIntent();
        mImageUriList.addAll(mIntent.getStringArrayListExtra("mImageUriList"));
        mPosition = mIntent.getIntExtra("mPosition", 0);
        mTitleMap.putAll((Map<? extends String, ? extends String>) mIntent.getSerializableExtra("mTitleMap"));

        for (int i = 0; i < mImageUriList.size(); i++) {
            fragmentList.add(SendDynamicDetailFragment.newInstance(mImageUriList.get(i)));
        }
        sendDDFragmentAdapter = new SendDDFragmentAdapter(getSupportFragmentManager(), fragmentList);
        newSendDynamicViewpager.setAdapter(sendDDFragmentAdapter);
        newSendDynamicViewpager.setCurrentItem(mPosition);
        newSendDynamicViewpager.addOnPageChangeListener(onPageChangeListener);
        mViewPagerPosition = newSendDynamicViewpager.getCurrentItem();
        sendDynamicDetailNum.setText((mViewPagerPosition + 1) + "/" + mImageUriList.size());
        Log.e(TAG, "调用文本监听事件=======");
        sendDynamicEdit.addTextChangedListener(textWatcher);
    }

    //初始化输入框的标题
    private void initEditTextShow(int mPosition) {
        sendDynamicEdit.setText(mTitleMap.get(mImageUriList.get(mPosition)));
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String title = sendDynamicEdit.getText().toString();
            Log.e(TAG, "正在输入的监听==title====" + title + "count====" + count + "==start==" + start + "==before==" + before);
            if (start == 0 && before == 1) {
                //处理完全清空数据时的存入
                mTitleMap.put(mImageUriList.get(newSendDynamicViewpager.getCurrentItem()), title);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //子标题录入
            String title = sendDynamicEdit.getText().toString();
            if (!TextUtils.isEmpty(title)) {
                mTitleMap.put(mImageUriList.get(mViewPagerPosition), title);
                Log.e(TAG, "子标题录入====位置===" + mViewPagerPosition + "===key===" + mImageUriList.get(mViewPagerPosition) + "====内容===" + mTitleMap.get(mImageUriList.get(mViewPagerPosition)));
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //通知变换全局的位置
            mViewPagerPosition = position;
            //设置滑块显示
            sendDynamicDetailNum.setText((position + 1) + "/" + (mImageUriList.size()));
            Log.e(TAG, "当前滑动的页面显示的图片======" + mImageUriList.get(position) + "==图片所在的position===" + mViewPagerPosition);
            //设置输入框的显示
            sendDynamicEdit.setText("");
            //设置map中存储的值
            sendDynamicEdit.setText(mTitleMap.get(mImageUriList.get(position)));
            Log.e(TAG, "填入Edit的内容=====" + mTitleMap.get(mImageUriList.get(position)));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @OnClick({R.id.new_send_detail_back_btn, R.id.new_send_dynamic_detail_ok})
    public void onViewClickedInSendDynamicActivity(View view) {
        switch (view.getId()) {
            case R.id.new_send_detail_back_btn:
                //取消按钮的处理逻辑 提示用户要保存标题
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("当前单图编辑未保存，确定要退出吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.new_send_dynamic_detail_ok:
                //保存title 并发送给写动态端
                EventBusUtil.sendEvent(new Event(EC.EventCode.SEND_TITLE_MAP, mTitleMap));
                finish();
                break;
        }
    }
}
