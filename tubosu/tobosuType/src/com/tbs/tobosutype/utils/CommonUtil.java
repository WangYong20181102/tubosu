package com.tbs.tobosutype.utils;

import android.app.Activity;
import android.content.Intent;

import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/12 13:44.
 */
public class CommonUtil {
    /*
     * 选择图片
     * */
    public static Intent uploadPictures(Activity activity, int requestCode, List<String> imagePaths) {
        //加载图片
        PhotoPickerIntent intent = new PhotoPickerIntent(activity);
        intent.setSelectModel(SelectModel.MULTI);//多选
        intent.setShowCarema(false); // 是否显示拍照
        intent.setMaxTotal(3); // 最多选择照片数量，默认为9
        intent.setSelectedPaths((ArrayList<String>) imagePaths); // 已选中的照片地址， 用于回显选中状态
        intent.putExtra("type", "photo");//选择方式；
        activity.startActivityForResult(intent, requestCode);
        return null;
    }
}
