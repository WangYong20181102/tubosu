package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/27 10:40.
 */

public class _PhotoDetail {

    /**
     * id : 18
     * comment_count : 0
     * uid: 2557
     * user_type: 1
     * image_detail : [{"title":"1/9 小清新的色调，加上柔和的灯光，非常怡人。地面是经典的黑白混搭瓷砖，整体给人的感觉，清新又明快。全部放置吧台椅，占据空间小，而且非常时尚。\n","image_url":"http://pic.tbscache.com/ke_file/2016-05-14/m_57369e5ed5dae.jpg"},{"title":"2/9 奶茶店的消费者，大多都是有追求，爱尝鲜的时尚年轻人，因此在奶茶店面的装修上，要个性且温馨。简约的桌椅，可爱的碎花墙纸，简约又温馨。最有特色的，是圆筒形的吊灯，既有个性的金属感，又尤显温馨。","image_url":"http://pic.tbscache.com/ke_file/2016-05-14/m_57369e71772ca.jpg"},{"title":"3/9 打破传统奶茶店的温馨风格，这种酷酷的奶茶店面也是十分受欢迎的。黑色的墙面，亮色的装饰画十分显眼，凸显活泼氛围。颜色差异较大的桌椅，在黑色墙壁的衬托下，尤为显眼，跳脱沉闷，打造时尚。","image_url":"http://pic.tbscache.com/ke_file/2016-05-14/m_57369e870d0c6.jpg"},{"title":"4/9 奶茶店面占地较宽，显得大气，同时还能放置多种类型的饮品、小吃，更好的满足顾客需求。窗户几乎占据整面墙，空间足够明亮，颜色不跳跃，十分舒适的感觉。\n","image_url":"http://pic.tbscache.com/ke_file/2016-05-14/m_57369e9857f29.jpg"},{"title":"5/9 还是偏暖的色调，温馨又时尚。橙色的特色招牌，能让顾客第一眼就看到优惠的推荐套餐。餐桌上精心放置的鲜花，活跃氛围。奶茶店面设计了大大的logo，让人一眼难忘。\n","image_url":"http://pic.tbscache.com/ke_file/2016-05-14/m_57369ea41ceef.jpg"},{"title":"6/9 奶茶店面以梦幻为主题，梦幻清新的蓝绿色，搭配温馨的暖色，色彩渐变美轮美奂。设计也尤为新颖，打破传统，运用柔和的弧形，空间更显柔美。\n","image_url":"http://pic.tbscache.com/ke_file/2016-05-14/m_57369eb06abc1.jpg"},{"title":"7/9 小型奶茶店占地面积小，可以在各大繁华地段开设。奶茶店面装修简单时尚，常见于商业街，美食街等，方便快捷，十分受欢迎。\n","image_url":"http://pic.tbscache.com/ke_file/2016-05-14/m_57369ec204f47.jpg"},{"title":"8/9 奶茶店面小而精致，以蓝色为主题，打造清新怡人的海洋风格。简单的木质吧台椅，没有繁杂都市的时尚感，多出的简约朴素，让人感到安心。","image_url":"http://pic.tbscache.com/ke_file/2016-05-14/m_57369ed2ca343.jpg"},{"title":"9/9 中式鞋柜与隔断相结合，下面是实体鞋柜，上面是镂空隔断，一虚一实；下面鞋柜线条简洁明朗，上面隔断是冰纹花格，一简一繁。它们相互结合相互对比，达到一种非凡的艺术美感。","image_url":"https://pic.tbscache.com/impress_pic/2016-04-30/p_572462a83ae3f.jpg"}]
     */

    private String id;
    private String uid;
    private String user_type;
    private String comment_count;
    private String is_collect;//用户是否收藏
    private List<ImageDetail> image_detail;

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public List<ImageDetail> getImage_detail() {
        return image_detail;
    }

    public void setImage_detail(List<ImageDetail> image_detail) {
        this.image_detail = image_detail;
    }

    public static class ImageDetail {
        /**
         * title : 1/9 小清新的色调，加上柔和的灯光，非常怡人。地面是经典的黑白混搭瓷砖，整体给人的感觉，清新又明快。全部放置吧台椅，占据空间小，而且非常时尚。
         * <p>
         * image_url : http://pic.tbscache.com/ke_file/2016-05-14/m_57369e5ed5dae.jpg
         */

        private String title;
        private String image_url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }
}
