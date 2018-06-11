package com.tbs.tbsbusiness.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.tbs.tbsbusiness.bean.ImageCompress;

import java.io.File;

/**
 * Created by Mr.Lin on 2018/6/7 10:47.
 */
public class ImgCompressUtils {
    /**
     * 压缩图片并且获取压缩之后图片的路径
     *
     * @param mContext  上下文
     * @param mFilePath 要压缩的图片路径
     * @return 返回新的图片路径 用于生成新的压缩图片
     */
    public static String CompressAndGetPath(Context mContext, String mFilePath) {
        ImageCompress compress = new ImageCompress();
        ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
        File tempFile = new File(mFilePath);
        options.uri = Uri.fromFile(tempFile);
        options.maxHeight = 800;
        options.maxWidth = 400;
        Bitmap bitmap = compress.compressFromUri(mContext, options);
        String mNewFilePath = FileUtil.saveFile(mContext, tempFile.getName(), bitmap);
        return mNewFilePath;
    }
}
