package com.tbs.tobosupicture.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.ImgToFriendSeachActivity;
import com.tbs.tobosupicture.activity.NewSendDynamicActivity;
import com.tbs.tobosupicture.activity.SendDynamicActivity;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.utils.FileUtil;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.SelectPersonalPopupWindow;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Mr.Lin on 2017/7/17 14:14.
 */

public class ImgToFriendFragment extends BaseFragment {
    @BindView(R.id.imgtofriend_sousuo)
    RelativeLayout imgtofriendSousuo;
    @BindView(R.id.imgtofriend_fabu)
    RelativeLayout imgtofriendFabu;
    @BindView(R.id.imgtofriend_zuire)
    LinearLayout imgtofriendZuire;
    @BindView(R.id.imgtofriend_zuixin)
    LinearLayout imgtofriendZuixin;
    @BindView(R.id.imgtofriend_youguanyuwo)
    LinearLayout imgtofriendYouguanyuwo;
    @BindView(R.id.imgtofriend_framelayout)
    FrameLayout imgtofriendFramelayout;
    Unbinder unbinder;
    @BindView(R.id.text_zuire)
    TextView textZuire;
    @BindView(R.id.text_zuixin)
    TextView textZuixin;
    @BindView(R.id.text_youguanyuwo)
    TextView textYouguanyuwo;
    @BindView(R.id.img_to_friend_show_pop)
    View imgToFriendShowPop;
    @BindView(R.id.img_zuire)
    ImageView imgZuire;
    @BindView(R.id.img_zuixin)
    ImageView imgZuixin;
    @BindView(R.id.img_wo)
    ImageView imgWo;

    private Context mContext;
    private String TAG = "ImgToFriendFragment";
    private Fragment[] mFragments;
    private int mIndex;//fragment数据下标
    private SelectPersonalPopupWindow menuWindow;


    //TODO 调取相机要用的值
    private static final int REQUESTCODE_PICK = 0;
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private static final int REQUEST_IMAGE = 3;//图片选择器用到的code
    private static final int REQUESTCODE_XIAO_MI_TAKE = 4;//图片选择器用到的code
    //存储的名称
    private static final String IMAGE_FILE_NAME = Utils.getNowTime() + "avatarImage.jpg";
    //默认已选择图片. 只有在选择模式为多选时有效
    private ArrayList<String> imageList = new ArrayList<>();

    public ImgToFriendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img_tofriend, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initFragment();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initFragment() {
        //最热
        ZuiReFragment zuiReFragment = new ZuiReFragment();
        //最新
        ZuiXinFragment zuiXinFragment = new ZuiXinFragment();
        //有关于我
        AboutMeFragment aboutMeFragment = new AboutMeFragment();

        mFragments = new Fragment[]{zuiReFragment, zuiXinFragment, aboutMeFragment};
        //fragment切换事务
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.imgtofriend_framelayout, zuiReFragment).commit();
        //默认显示最热
        setIndexSelect(0);
        clcikChange(0);
    }

    //切换选择
    private void setIndexSelect(int indexSelect) {
        if (mIndex == indexSelect) {
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.hide(mFragments[mIndex]);
        if (!mFragments[indexSelect].isAdded()) {
            ft.add(R.id.imgtofriend_framelayout, mFragments[indexSelect]).show(mFragments[indexSelect]);
        } else {
            ft.show(mFragments[indexSelect]);
        }
        ft.commitAllowingStateLoss();
        mIndex = indexSelect;
    }

    @OnClick({R.id.imgtofriend_sousuo, R.id.imgtofriend_fabu, R.id.imgtofriend_zuire,
            R.id.imgtofriend_zuixin, R.id.imgtofriend_youguanyuwo})
    public void onViewClickedInImgToFriendFragment(View view) {
        switch (view.getId()) {
            case R.id.imgtofriend_sousuo:
                //跳转到搜索页
                Intent intent = new Intent(mContext, ImgToFriendSeachActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.imgtofriend_fabu:
                //调用相机或者图册进行动态发布
                if (Utils.userIsLogin(mContext)) {
                    CreatDynamic();
                } else {
                    Utils.gotoLogin(mContext);
                }
                Log.e(TAG, "获取手机品牌======" + Build.BRAND);
                break;
            case R.id.imgtofriend_zuire:
                //切换至最热
                MobclickAgent.onEvent(mContext,"click_zui_re");
                setIndexSelect(0);
                clcikChange(0);

                break;
            case R.id.imgtofriend_zuixin:
                //切换至最新
                MobclickAgent.onEvent(mContext,"click_zui_xin");
                setIndexSelect(1);
                clcikChange(1);

                break;
            case R.id.imgtofriend_youguanyuwo:
                //切换至有关于我
                if (Utils.userIsLogin(mContext)) {
                    setIndexSelect(2);
                    clcikChange(2);
                } else {
                    Utils.gotoLogin(mContext);
                }
                break;
        }
    }

    //切换文字的改变
    private void clcikChange(int position) {
        if (position == 0) {
            //最热
            textZuire.setTextColor(Color.parseColor("#202124"));
            textZuixin.setTextColor(Color.parseColor("#8a8f99"));
            textYouguanyuwo.setTextColor(Color.parseColor("#8a8f99"));
            Glide.with(mContext).load(R.mipmap.zuire_after).into(imgZuire);
            Glide.with(mContext).load(R.mipmap.zuixin).into(imgZuixin);
            Glide.with(mContext).load(R.mipmap.wo).into(imgWo);

        } else if (position == 1) {
            textZuire.setTextColor(Color.parseColor("#8a8f99"));
            textZuixin.setTextColor(Color.parseColor("#202124"));
            textYouguanyuwo.setTextColor(Color.parseColor("#8a8f99"));

            Glide.with(mContext).load(R.mipmap.zuire).into(imgZuire);
            Glide.with(mContext).load(R.mipmap.zuixin_after).into(imgZuixin);
            Glide.with(mContext).load(R.mipmap.wo).into(imgWo);
        } else if (position == 2) {
            textZuire.setTextColor(Color.parseColor("#8a8f99"));
            textZuixin.setTextColor(Color.parseColor("#8a8f99"));
            textYouguanyuwo.setTextColor(Color.parseColor("#202124"));

            Glide.with(mContext).load(R.mipmap.zuire).into(imgZuire);
            Glide.with(mContext).load(R.mipmap.zuixin).into(imgZuixin);
            Glide.with(mContext).load(R.mipmap.wo_after).into(imgWo);
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    //接收EventBus消息事件

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.LOGIN_OUT:
                //用户已经登出
                setIndexSelect(0);
                clcikChange(0);
                break;
            case EC.EventCode.SHOW_ABOUT_ME:
                //有消息
                setIndexSelect(2);
                clcikChange(2);
                break;
            case EC.EventCode.GOTO_SEND_DYNAMIC:
                CreatDynamic();
                break;
            case EC.EventCode.GOTO_ZUIXIN:
                //切换到最新页面
                setIndexSelect(1);
                clcikChange(1);
                break;
        }
    }

    //发布动态
    private void CreatDynamic() {
        menuWindow = new SelectPersonalPopupWindow(mContext, popClickLister);
        menuWindow.showAtLocation(imgToFriendShowPop, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //弹窗点击事件
    private View.OnClickListener popClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    //开启相机 调用原生相机
                    if (android.os.Build.BRAND.equals("Xiaomi")) {
                        //小米专用
                        Log.e(TAG, "进入的了小米专用==========");
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takeIntent, REQUESTCODE_XIAO_MI_TAKE);
                    } else {
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                        startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    }

                    break;
                case R.id.pickPhotoBtn:
                    //开启图册 采用图册选择框架
                    MultiImageSelector.create(mContext)
                            .showCamera(false)
                            .count(9)
                            .multi()
                            .origin(imageList)
                            .start(ImgToFriendFragment.this, REQUEST_IMAGE);
                    break;
            }
        }
    };

    /***
     * 裁剪图片
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_TAKE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:
                //拍照处理之后将图片发给下一个页面
                if (resultCode == Activity.RESULT_CANCELED) {
                    //取消操作
                    return;
                }
                if (data != null && data.getExtras().getParcelable("data") != null) {
                    Bitmap photo = data.getExtras().getParcelable("data");
                    String ImgPath = FileUtil.saveFile(mContext, Utils.getNowTime() + "lin_zxkk.jpg", photo);
                    ArrayList<String> imgPathList = new ArrayList<>();
                    imgPathList.add(ImgPath);
//                    Intent intent = new Intent(mContext, SendDynamicActivity.class);
                    Intent intent = new Intent(mContext, NewSendDynamicActivity.class);
                    intent.putStringArrayListExtra("ImageList", imgPathList);
                    startActivity(intent);
                }
                break;
            case REQUESTCODE_XIAO_MI_TAKE:
                //小米专用拍照
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (data != null) {
                    if (data.getExtras() != null) {
                        Bundle bundle = data.getExtras();
                        Bitmap photo = (Bitmap) bundle.get("data");
                        if (photo != null) {
                            String ImgPath = FileUtil.saveFile(mContext, Utils.getNowTime() + "lin_zxkk.jpg", photo);
                            ArrayList<String> imgPathList = new ArrayList<>();
                            imgPathList.add(ImgPath);
//                            Intent intent = new Intent(mContext, SendDynamicActivity.class);
                            Intent intent = new Intent(mContext, NewSendDynamicActivity.class);
                            intent.putStringArrayListExtra("ImageList", imgPathList);
                            startActivity(intent);
                        }
                    }
                }
                break;
            case REQUEST_IMAGE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    ArrayList<String> imgPathList = new ArrayList<>();
                    imgPathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    Intent intent = new Intent(mContext, NewSendDynamicActivity.class);
                    intent.putStringArrayListExtra("ImageList", imgPathList);
                    startActivity(intent);
                }
                break;
        }
    }
}
