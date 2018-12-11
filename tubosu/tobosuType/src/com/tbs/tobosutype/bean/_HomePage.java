package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/8/31 14:21.
 * 土拨鼠4.0版本新的首页实体类
 */
public class _HomePage {


    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String course_list_url;
        private String is_see = "";
        private List<BannerBean> banner;
        private List<CasesBean> cases;
        private List<ImpressionBean> impression;
        private List<ArticleTypeBean> article_type;
        private List<List<ArticleBean>> article;
        private List<TopicBean> topic;
        private List<IndexAdvert1Bean> index_advert_1;
        private List<IndexAdvert2Bean> index_advert_2;

        public String getIs_see() {
            return is_see;
        }

        public void setIs_see(String is_see) {
            this.is_see = is_see;
        }

        public String getCourse_list_url() {
            return course_list_url;
        }

        public void setCourse_list_url(String course_list_url) {
            this.course_list_url = course_list_url;
        }

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<CasesBean> getCases() {
            return cases;
        }

        public void setCases(List<CasesBean> cases) {
            this.cases = cases;
        }

        public List<ImpressionBean> getImpression() {
            return impression;
        }

        public void setImpression(List<ImpressionBean> impression) {
            this.impression = impression;
        }

        public List<ArticleTypeBean> getArticle_type() {
            return article_type;
        }

        public void setArticle_type(List<ArticleTypeBean> article_type) {
            this.article_type = article_type;
        }

        public List<List<ArticleBean>> getArticle() {
            return article;
        }

        public void setArticle(List<List<ArticleBean>> article) {
            this.article = article;
        }

        public List<TopicBean> getTopic() {
            return topic;
        }

        public void setTopic(List<TopicBean> topic) {
            this.topic = topic;
        }

        public List<IndexAdvert1Bean> getIndex_advert_1() {
            return index_advert_1;
        }

        public void setIndex_advert_1(List<IndexAdvert1Bean> index_advert_1) {
            this.index_advert_1 = index_advert_1;
        }

        public List<IndexAdvert2Bean> getIndex_advert_2() {
            return index_advert_2;
        }

        public void setIndex_advert_2(List<IndexAdvert2Bean> index_advert_2) {
            this.index_advert_2 = index_advert_2;
        }

        public static class BannerBean {

            private String id;
            private String img_url;
            private String content_url;
            private String bgcolor;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getContent_url() {
                return content_url;
            }

            public void setContent_url(String content_url) {
                this.content_url = content_url;
            }

            public String getBgcolor() {
                return bgcolor;
            }

            public void setBgcolor(String bgcolor) {
                this.bgcolor = bgcolor;
            }
        }

        public static class CasesBean {

            private String id;
            private String cover_url;
            private String city_name;
            private String community_name;
            private String owner_name;
            private String desmethod;
            private String design_id;
            private String description;
            private String icon;
            private String sub_title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getCommunity_name() {
                return community_name;
            }

            public void setCommunity_name(String community_name) {
                this.community_name = community_name;
            }

            public String getOwner_name() {
                return owner_name;
            }

            public void setOwner_name(String owner_name) {
                this.owner_name = owner_name;
            }

            public String getDesmethod() {
                return desmethod;
            }

            public void setDesmethod(String desmethod) {
                this.desmethod = desmethod;
            }

            public String getDesign_id() {
                return design_id;
            }

            public void setDesign_id(String design_id) {
                this.design_id = design_id;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getSub_title() {
                return sub_title;
            }

            public void setSub_title(String sub_title) {
                this.sub_title = sub_title;
            }
        }

        public static class ImpressionBean {

            private String id;
            private String cover_url;
            private int image_width;
            private int image_height;
            private String type;
            private String designer_name;
            private String designer_icon;
            private String title;
            private String sub_title;
            private String collect_count;
            private String is_collect;
            private String share_url;
            private List<String> sub_images;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public int getImage_width() {
                return image_width;
            }

            public void setImage_width(int image_width) {
                this.image_width = image_width;
            }

            public int getImage_height() {
                return image_height;
            }

            public void setImage_height(int image_height) {
                this.image_height = image_height;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDesigner_name() {
                return designer_name;
            }

            public void setDesigner_name(String designer_name) {
                this.designer_name = designer_name;
            }

            public String getDesigner_icon() {
                return designer_icon;
            }

            public void setDesigner_icon(String designer_icon) {
                this.designer_icon = designer_icon;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSub_title() {
                return sub_title;
            }

            public void setSub_title(String sub_title) {
                this.sub_title = sub_title;
            }

            public String getCollect_count() {
                return collect_count;
            }

            public void setCollect_count(String collect_count) {
                this.collect_count = collect_count;
            }

            public String getIs_collect() {
                return is_collect;
            }

            public void setIs_collect(String is_collect) {
                this.is_collect = is_collect;
            }

            public String getShare_url() {
                return share_url;
            }

            public void setShare_url(String share_url) {
                this.share_url = share_url;
            }

            public List<String> getSub_images() {
                return sub_images;
            }

            public void setSub_images(List<String> sub_images) {
                this.sub_images = sub_images;
            }
        }

        public static class ArticleTypeBean {

            private String id;
            private String title;
            private String index;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ArticleBean {

            private String id;
            private String title;
            private String image_url;
            private String add_time;
            private String type;
            private String jump_url;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

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

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getJump_url() {
                return jump_url;
            }

            public void setJump_url(String jump_url) {
                this.jump_url = jump_url;
            }
        }

        public static class TopicBean {

            private String id;
            private String title;
            private String cover_url;
            private String desc;
            private String add_time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }
        }

        public static class IndexAdvert1Bean {

            private String id;
            private String img_url;
            private String jump_url;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getJump_url() {
                return jump_url;
            }

            public void setJump_url(String jump_url) {
                this.jump_url = jump_url;
            }
        }

        public static class IndexAdvert2Bean {

            private String id;
            private String img_url;
            private String jump_url;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getJump_url() {
                return jump_url;
            }

            public void setJump_url(String jump_url) {
                this.jump_url = jump_url;
            }
        }
    }
}
