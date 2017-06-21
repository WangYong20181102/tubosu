package com.tobosu.mydecorate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.DragAdapter;
import com.tobosu.mydecorate.adapter.DragAdapter1;
import com.tobosu.mydecorate.adapter.OtherAdapter;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.database.ChannelManage;
import com.tobosu.mydecorate.entity.BibleEntity;
import com.tobosu.mydecorate.entity.DecorateTitleEntity.ChannelItem;
import com.tobosu.mydecorate.entity.DecorateTitleEntity;
import com.tobosu.mydecorate.fragment.DecorateArticleListFragment;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.BladeView;
import com.tobosu.mydecorate.view.DragGrid;
import com.tobosu.mydecorate.view.OtherGridView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dec on 2016/9/27.
 */

public class ChannelActivity extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG = ChannelActivity.class.getSimpleName();
    private Context mContext;
    private RelativeLayout rel_change_channel_back;
    private List<String> dataSourceList = new ArrayList<String>();

    private ChannelManage channelManager;
    private DecorateTitleEntity.ChannelItem defaultData, delData;

    /** 用户栏目的GRIDVIEW */
    private DragGrid userGridView;
    /** 其它栏目的GRIDVIEW */
    private OtherGridView otherGridView;
    /** 用户栏目对应的适配器，可以拖动 */
    private DragAdapter userAdapter;
    /** 其它栏目对应的适配器 */
    private OtherAdapter otherAdapter;
    /** 其它栏目列表 */
    private ArrayList<DecorateTitleEntity.ChannelItem> otherChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();
    /** 用户栏目列表 */
    private ArrayList<DecorateTitleEntity.ChannelItem> userChannelList = new ArrayList<DecorateTitleEntity.ChannelItem>();
    /** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
    private boolean isMove = false;

    private RelativeLayout relEditTitle;
    private TextView tvEditTitle;
    private boolean isEdit = false;

    private boolean isLastOne = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去除这个Activity的标题栏
        setContentView(R.layout.activity_channel);
//        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContext = ChannelActivity.this;
        initView();
        initData();
        setClick();
    }

    private void initView() {
        rel_change_channel_back = (RelativeLayout) findViewById(R.id.rel_change_channel_back);
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);

        relEditTitle =(RelativeLayout) findViewById(R.id.rel_edit_title);
        tvEditTitle =(TextView) findViewById(R.id.tv_edit_title_text);

    }




    private ArrayList<DecorateTitleEntity.ChannelItem> itemsList = new ArrayList<DecorateTitleEntity.ChannelItem>();
    private void initData() {
        channelManager = ChannelManage.getManage(MyApplication.getApp().getSQLHelper());

//        Bundle b = getIntent().getBundleExtra("bible_title_list_bundle");
//        if(b!=null){
//            String itemJson = b.getString("titleItemJson");
//            Util.setErrorLog(TAG, "===="+itemJson);
//
//            Gson gson = new Gson();
//            DecorateTitleEntity titleEntity = gson.fromJson(itemJson, DecorateTitleEntity.class);
//            if(titleEntity!=null && titleEntity.getStatus()==200){
//                int size = titleEntity.getData().size();
//                DecorateTitleEntity.ChannelItem item;
//                if(size>0){
//                    for(int i =0; i<size; i++){
//                        item = new DecorateTitleEntity.ChannelItem(titleEntity.getData().get(i).getId(),
//                                titleEntity.getData().get(i).getName(),i+1,1);
//                        itemsList.add(item);
//                    }
//
//                }
//            }
//
//            channelManager = ChannelManage.getManage(MyApplication.getApp().getSQLHelper());
//            channelManager.setDefaultData(itemsList);
//        }


        userChannelList = channelManager.getUserChannel();
        otherChannelList = channelManager.getOtherChannel();
        userAdapter = new DragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);


    }


    private void setClick(){
        rel_change_channel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isEdit){
//                    tvEditTitle.setText("编辑");
//                    userAdapter.hideDel();
//                    return;
//                }
                if(isLastOne){
                    Util.setToast(mContext, "必须选择一个频道");
                    return;
                }
                goMainActivity();
            }
        });

        relEditTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEdit){

                    tvEditTitle.setText("完成");
                    userAdapter.showDel();
                }else{

                    if(userAdapter.getCount()==0){
                        isLastOne = true;
                        Util.setToast(mContext, "必须选择一个频道");
                        return;
                    }else{
                        isLastOne = false;
                    }

                    tvEditTitle.setText("编辑");
                    userAdapter.hideDel();
                    saveChannel();
                    if(userAdapter.isListChanged()){
                        Intent intent = new Intent(mContext, MainActivity.class);
                        setResult(DecorateArticleListFragment.CHANNELRESULT, intent);
                        Util.setLog(TAG, "数据发生改变");
//                        ArrayList<ChannelItem> data = ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).getUserChannel();
//                        for(int i=0;i<data.size();i++){
//                            Util.setErrorLog(TAG, data.get(i).getName() + "--title_id--->>>" + data.get(i).getId());
//                        }
                    }
                }
                isEdit = !isEdit;
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){

//            if(isEdit){
//                tvEditTitle.setText("编辑");
//                userAdapter.hideDel();
//                return true;
//            }

//            saveChannel();
//            if(userAdapter.isListChanged()){
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                setResult(DecorateArticleListFragment.CHANNELRESULT, intent);
            if(isLastOne){
                Util.setToast(mContext, "必须选择一个频道");
                return true;
            }

            goMainActivity();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void goMainActivity(){
        Intent mainIntent = new Intent(mContext, MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("channel", 10);
        mainIntent.putExtra("channel_bundle", b);
        startActivity(mainIntent);
        finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if(isMove){
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                if(isEdit){
                    // 编辑状态
                    //position为 0，1 的不可以进行任何操作
                    if (position != 0 && position != 1) {
                        final ImageView moveImageView = getView(view);
                        if (moveImageView != null) {
                            TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                            final int[] startLocation = new int[2];
                            newTextView.getLocationInWindow(startLocation);
                            final DecorateTitleEntity.ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                            otherAdapter.setVisible(false);
                            //添加到最后一个
                            otherAdapter.addItem(channel);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    try {
                                        int[] endLocation = new int[2];
                                        //获取终点的坐标
                                        otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                        MoveAnim(moveImageView, startLocation , endLocation, channel,userGridView);
                                        userAdapter.setRemove(position);
                                    } catch (Exception localException) {
                                        localException.printStackTrace();
                                    }
                                }
                            }, 50L);
                        }
                    }else{
//                        Util.setToast(mContext, "");
                    }


//                    final ImageView moveImageView = getView(view);
//                    if (moveImageView != null) {
//                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
//                        final int[] startLocation = new int[2];
//                        newTextView.getLocationInWindow(startLocation);
//                        final DecorateTitleEntity.ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
//                        otherAdapter.setVisible(false);
//                        //添加到最后一个
//                        otherAdapter.addItem(channel);
//                        new Handler().postDelayed(new Runnable() {
//                            public void run() {
//                                try {
//                                    int[] endLocation = new int[2];
//                                    //获取终点的坐标
//                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
//                                    MoveAnim(moveImageView, startLocation , endLocation, channel,userGridView);
//                                    userAdapter.setRemove(position);
//                                } catch (Exception localException) {
//                                    localException.printStackTrace();
//                                }
//                            }
//                        }, 50L);
//                    }


                }else {
                    // 点击跳转到相应的频道
//                    Intent channelIntent = new Intent(Constant.SWITCH_CHANNEL_ACTION);
//                    Bundle b = new Bundle();
//                    b.putInt("channel_position", position);
//                    channelIntent.putExtra("switch_channel_bundle",b);
//                    sendBroadcast(channelIntent);

                    Intent channelIntent = new Intent(mContext, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("channel_position", position);
                    channelIntent.putExtra("switch_channel_bundle",b);
                    startActivity(channelIntent);
                    finish();
                }


                break;
            case R.id.otherGridView:
                if(isEdit){
                    final ImageView omoveImageView = getView(view);
                    TextView newTextView = (TextView) view.findViewById(R.id.other_text_item);
                    final int[] startLocation = new int[2];
                    if(newTextView!=null){
                        newTextView.getLocationInWindow(startLocation);
                        final DecorateTitleEntity.ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                        userAdapter.setVisible(false);
                        //添加到最后一个
                        userAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(omoveImageView, startLocation , endLocation, channel,otherGridView);
                                    otherAdapter.setRemove(position);
                                } catch (Exception localException) {
                                    localException.printStackTrace();
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * 点击ITEM移动动画
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final DecorateTitleEntity.ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                }else{
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /** 退出时候保存选择后数据库的设置  */
    private void saveChannel() {
        ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).deleteAllChannel();
        ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).saveUserChannel(userAdapter.getChannnelLst());
        ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).saveOtherChannel(otherAdapter.getChannnelLst());
    }


}
