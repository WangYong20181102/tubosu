package com.tbs.tobosupicture.constants;

import com.tbs.tobosupicture.MyApplication;
import com.tbs.tobosupicture.utils.Utils;

/**
 * Created by Mr.Lin on 2017/5/10 16:21.
 * 存放环境以及接口地址
 */

public class UrlConstans {
    /**
     * 正式环境的URl
     */
//    public static final String ZXKK_URL = "http://www.tobosu.com/";

    /**
     * 测试环境的URl
     */
    public static final String ZXKK_URL = "http://www.dev.tobosu.com/";

    /**
     * 发单所要用到的标识码
     */
    public static final String PIPE_CODE = "http://m.tobosu.com/app/pub?channel=seo&subchannel=zhjandroid&chcode=" + Utils.getChannType(MyApplication.getContexts()) + "&app_type=2";
    /**
     * 发单入口url
     */
    public static final String PUB_ORDER_URL = ZXKK_URL + "tapi/order/pub_order";
    /**
     * 获取使用发单人口数量
     */
    public static final String GET_BILL_COUNT_URL = ZXKK_URL + "zapp/index/get_bill_count";
    /**
     * 上传图片的地址
     */
    public static final String UPLOAD_IMAGE = ZXKK_URL + "cloud/upload/upload_for_ke?";
}
