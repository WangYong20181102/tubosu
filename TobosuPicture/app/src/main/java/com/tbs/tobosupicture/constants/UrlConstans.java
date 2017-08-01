package com.tbs.tobosupicture.constants;

import android.os.Environment;

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
     * 图片存储的位置
     */
    public static final String IMG_PATH = Environment.getExternalStorageDirectory() + "/zxkk/";
    ;
    /**
     * 手机号码 用于验证手机号码是否合法
     */
    public static final String PHONE_NUM = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
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
    /**
     * 获取我的图谜
     */
    public static final String GET_MY_FANS_URL = ZXKK_URL + "rapp/MyOwner/my_fans";


    /**
     * 家装页面 获取筛选类型
     */
    public static final String GET_HOUSE_DECORATE_STYLE_URL = ZXKK_URL + "rapp/TemplateMap/get_home_search_type";

    /**
     * 工装页面 获取筛选类型
     */
    public static final String GET_FACTORY_DECORATE_STYLE_SURL = ZXKK_URL + "rapp/TemplateMap/get_tool_search_type";

    /**
     * 家装/工装列表url
     */
    public static final String GET_LIST = ZXKK_URL + "rapp/TemplateMap/index";

    /**
     * 设计师详情url
     */
    public static final String DESIGNER_URL = ZXKK_URL + "rapp/TemplateMap/designer_index";


    public static final String UID = "23109";

    public static final String DISTRICT_LIST_URL =  ZXKK_URL + "rapp/village/village_list";

    /**
     * 关注 / 取消关注 设计师url
     */
    public static final String CONCERN_URL = ZXKK_URL + "/rapp/public/follow_designer";

    /**
     * 搜索案例url
     */
    public static final String SEARCH_CASE_URL = ZXKK_URL + "rapp/case/case_list";

    /**
     * 案例搜索页面url
     */
    public static final String CASE_SEARCH_URL = ZXKK_URL + "/rapp/case/case_search";

    /**
     * 收藏套图的url
     */
    public static final String COLLECT_PIC_URL = ZXKK_URL + "/rapp/TemplateMap/collect";

    /**
     * 获取他人的图谜url
     */
    public static final String GET_HIS_FANS_URL = ZXKK_URL + "rapp/user/my_fans";

    /**
     * 清空搜索记录
     */
    public static final String CLEAR_CASE_URL = ZXKK_URL + "rapp/case/clear_search_history";
    /**
     * 获取他人的图友url
     */
    public static final String GET_FRIENDS_URL = ZXKK_URL + "rapp/user/my_friends";
    /**
     * 获取我关注的设计师url
     */
    public static final String GET_MY_ATTENTION_DESIGNER_URL = ZXKK_URL + "";
    /**
     * 获取手机验证码
     */
    public static final String GET_PHONE_CODE_URL = ZXKK_URL + "rapp/public/sms_code";
    /**
     * 用户用手机号码注册
     */
    public static final String PHONE_NUM_REGISTER_URL = ZXKK_URL + "rapp/public/register";
    /**
     * 用户设置新密码
     */
    public static final String SET_NEW_PASSWORD = ZXKK_URL + "";
    /**
     * 用户获取我的图友列表
     */
    public static final String GET_MY_FRIENDS = ZXKK_URL + "rapp/MyOwner/my_friends";
    /**
     * 用户获取推荐图友列表
     */
    public static final String GET_RECOMMEND_FRIENDS = ZXKK_URL + "rapp/MyOwner/recomment_friends";
    /**
     * 以图会友（最热）
     */
    public static final String IMAGE_TO_FRIEND_ZUIRE = ZXKK_URL + "rapp/Social/popular_list";
    /**
     * 查看详细页（动态详情页）动态详细页
     */
    public static final String DYNAMIC_DETAIL = ZXKK_URL + "rapp/dynamic/dynamic_detail";
    /**
     * 动态评论列  表动态详细页
     */
    public static final String DYNAMIC_COMMETN_LIST = ZXKK_URL + "rapp/dynamic/dynamic_comment_list";
    /**
     * 评论动态公共功能
     */
    public static final String USER_SEND_COMMENT = ZXKK_URL + "rapp/public/comment";
    /**
     * 点赞/取消点赞公共功能
     */
    public static final String USER_PRAISE = ZXKK_URL + "rapp/public/praise";

    /**
     * 图册url
     */
    public static final String PICTURE_LIST_URL = ZXKK_URL + "rapp/TemplateMap/template_atlas";

    /**
     * 查看个人主页查看个人主页
     */
    public static final String HOME_PAGE = ZXKK_URL + "rapp/user/home_page";
    /**
     * 相关动态列表个人主页相关动态列表(更多个人动态)
     */
    public static final String RELATE_DYNAMIC = ZXKK_URL + "rapp/user/relate_dynamic";
    /**
     * 关注/取消关注（加为图友）公共功能
     */
    public static final String USER_FOLLOW = ZXKK_URL + "rapp/public/follow";
    /**
     * 评论的点赞/取消点赞公共功能
     */
    public static final String COMMENT_PRAISE = ZXKK_URL + "rapp/public/comment_praise";
    /**
     * 点赞列表动态详细页
     */
    public static final String DYNAMIC_PRAISE_LIST = ZXKK_URL + "rapp/dynamic/dynamic_praise_list";
    /**
     * 回复评论列表回复评论列表
     */
    public static final String REPLY_DYNAMIC_COMMENT_LIST = ZXKK_URL + "rapp/dynamic/reply_dynamic_comment_list";
    /**
     * 回复评论公共功能
     */
    public static final String REPLY_COMMENT = ZXKK_URL + "rapp/public/reply_comment";
    /**
     * 回复评论公共功能
     */
    public static final String VIEW_DYNAMIC_IMG = ZXKK_URL + "rapp/Social/view_dynamic_img";
    /**
     * 收藏动态查看图集
     */
    public static final String DYNAMIC_COLLECT = ZXKK_URL + "rapp/dynamic/collect";
    /**
     * 登录公共功能
     */
    public static final String USER_LOGIN = ZXKK_URL + "rapp/public/login";
    /**
     * 用户个人信息用户个人信息
     */
    public static final String PERSONAL_INFO = ZXKK_URL + "rapp/MyOwner/personal_info";
    /**
     * 我的主页我的模块
     */
    public static final String MINE_HOME_PAGE = ZXKK_URL + "rapp/MyOwner/home_page";

}
