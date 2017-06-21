package com.tobosu.mydecorate.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.FeedBackActivity;
import com.tobosu.mydecorate.activity.LoginActivity;
import com.tobosu.mydecorate.activity.MessageCenterActivity;
import com.tobosu.mydecorate.activity.MyCollectionActivity;
import com.tobosu.mydecorate.activity.SettingActivity;
import com.tobosu.mydecorate.activity.UserInfoActivity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.RoundImageView;
import com.tobosu.mydecorate.view.TipDialog;


/**
 * Created by dec on 2016/9/14.
 * 我 页面
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = MineFragment.class.getSimpleName();

    private RelativeLayout rel_head_picture_layout;

    private RelativeLayout rel_collection;
    private RelativeLayout rel_msgcenter;
    private RelativeLayout rel_setting;
    private RelativeLayout rel_feedback;

    private TextView tv_writername;
    private RoundImageView riv_head_picture;

    private String headPictureUrl = "";

    private String userName = "";

    private String uid = "";

//    private Handler myHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            riv_head_picture.setBackgroundResource(R.mipmap.icon_head_default);
//        }
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("---------minefragment----------");
        View view = inflater.inflate(R.layout.fragment_mine,null);
        initView(view);
        initDatas();
        return view;
    }



    private void initView(View view) {
        riv_head_picture = (RoundImageView) view.findViewById(R.id.riv_head_picture);
        tv_writername = (TextView) view.findViewById(R.id.tv_writername );

        rel_head_picture_layout = (RelativeLayout) view.findViewById(R.id.rel_head_picture_layout);
        rel_head_picture_layout.setOnClickListener(this);
        rel_collection = (RelativeLayout) view.findViewById(R.id.rel_collection);
        rel_collection.setOnClickListener(this);
        rel_msgcenter = (RelativeLayout) view.findViewById(R.id.rel_msgcenter);
        rel_msgcenter.setOnClickListener(this);
        rel_setting = (RelativeLayout) view.findViewById(R.id.rel_setting);
        rel_setting.setOnClickListener(this);
        rel_feedback = (RelativeLayout) view.findViewById(R.id.rel_feedback);
        rel_feedback.setOnClickListener(this);
    }


    public void initDatas() {

        if(Util.isNetAvailable(getActivity())){
            if (Util.isLogin(getActivity())){
                userName = getActivity().getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("user_name","");
                tv_writername.setText(userName);
                headPictureUrl = getActivity().getSharedPreferences("User_Info_SP",Context.MODE_PRIVATE).getString("head_pic_url","");

                Picasso .with(getActivity())
                        .load(headPictureUrl)
                        .fit()
                        .placeholder(R.mipmap.icon_head_default)
                        .error(R.mipmap.icon_head_default)
                        .into(riv_head_picture);
            }else{
//                myHandler.sendEmptyMessage(0);
                riv_head_picture.setBackgroundResource(R.mipmap.icon_head_default);
                System.out.println("-------------你来这里了------------");
                tv_writername.setText("登 录");
            }
        }else {
            //TODO 无网络 不需要占位图
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_head_picture_layout:

                if(Util.isLogin(getActivity())){
                    String userType = getActivity().getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("mark","");
                    if (getActivity().getSharedPreferences("See_Tip_Sp", Context.MODE_PRIVATE).getString("has_seen_tip","").equals("see")==false) {
                        if("2".equals(userType) || "3".equals(userType)){
                            initTipDilog();
                        }else {
                            goUserInfoActivity();
                        }
                    }else {
                        //
                        goUserInfoActivity();
                    }
                }else{
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

                break;
            case R.id.rel_collection:
                if (Util.isLogin(getActivity())) {
                    uid = Util.getUserId(getActivity());
                    Intent collectionInetnt = new Intent(getActivity(), MyCollectionActivity.class);
                    Bundle b = new Bundle();
                    b.putString("uid", uid);
                    collectionInetnt.putExtra("Collection_Bundle",b);
                    startActivity(collectionInetnt);
                }else{
                    //未登陆 跳转登陆页面  登陆成功后，登陆界面消失 直接去收藏页面
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

                break;
            case R.id.rel_msgcenter:
                startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                break;
            case R.id.rel_setting:
               startActivityForResult(new Intent(getActivity(), SettingActivity.class), 0);
                break;
            case R.id.rel_feedback:
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
//                DBManager.getInstance(getActivity()).deleteTable("Concern_Writer_Table");
                break;
        }
    }

    private void goUserInfoActivity(){
        Intent it = new Intent(getActivity(), UserInfoActivity.class);
        Bundle b = new Bundle();
        b.putString("change_user_name",userName);
        it.putExtra("Change_UserName_Bundle",b);
        startActivityForResult(it, Constant.CHANGE_USER_INFO_REQUESTCODE);
    }

    private void initTipDilog(){
        TipDialog.Builder builder = new TipDialog.Builder(getActivity());
        builder.setPositiveButton("知道啦",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().getSharedPreferences("See_Tip_Sp",Context.MODE_PRIVATE).edit().putString("has_seen_tip","see").commit();
                        dialog.cancel();

                        // 登录过的直接去用户页面
                        goUserInfoActivity();

                    }
                });
        builder.create().show();
    }



    @Override
    public void onResume() {
        super.onResume();
        initDatas();
        Intent it = new Intent(Constant.REFRESH_MINEFRAGMENT_DATA_ACITION);
        getActivity().sendBroadcast(it);
    }
}
