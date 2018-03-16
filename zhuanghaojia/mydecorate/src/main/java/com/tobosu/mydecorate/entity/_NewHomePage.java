package com.tobosu.mydecorate.entity;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/3/14 17:42.
 */

public class _NewHomePage {

    private List<CarouselBean> carousel;
    private List<ArticleBean> article;
    private List<AuthorBean> author;

    public List<CarouselBean> getCarousel() {
        return carousel;
    }

    public void setCarousel(List<CarouselBean> carousel) {
        this.carousel = carousel;
    }

    public List<ArticleBean> getArticle() {
        return article;
    }

    public void setArticle(List<ArticleBean> article) {
        this.article = article;
    }

    public List<AuthorBean> getAuthor() {
        return author;
    }

    public void setAuthor(List<AuthorBean> author) {
        this.author = author;
    }

    public static class CarouselBean {
        /**
         * id : 62
         * img_url : https://back.tobosu.com/head_file/2017-09-23/59c5fb4b0e89a.png
         * content_url : http://m.tobosu.com/new_t_price?channel=seo&amp;subchannel=android&amp;chcode=xswap
         */

        private String id;
        private String img_url;
        private String content_url;

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
    }

    public static class ArticleBean {
        /**
         * aid : 7005
         * uid : 226786
         * title : 遵义丰立装饰 这应该是最经典的现代风格了 不接受反驳
         * image_url : https://back.tobosu.com/mt_banner/2018-03-10/5aa39bb255298.jpg
         * view_count : 86
         * rank_num : 1
         */

        private String aid;
        private String uid;
        private String title;
        private String image_url;
        private String view_count;
        private String rank_num;

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getRank_num() {
            return rank_num;
        }

        public void setRank_num(String rank_num) {
            this.rank_num = rank_num;
        }
    }

    public static class AuthorBean {
        /**
         * uid : 226786
         * view_count : 62889
         * count : 86
         * article_count : 31
         * nick : 遵义丰立装饰
         * icon : https://back.tobosu.com/mt_company_logo/2017-08-19/5998037d48800.jpg
         * rank_num : 1
         */

        private String uid;
        private int view_count;
        private String count;
        private int article_count;
        private String nick;
        private String icon;
        private String rank_num;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getView_count() {
            return view_count;
        }

        public void setView_count(int view_count) {
            this.view_count = view_count;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public int getArticle_count() {
            return article_count;
        }

        public void setArticle_count(int article_count) {
            this.article_count = article_count;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getRank_num() {
            return rank_num;
        }

        public void setRank_num(String rank_num) {
            this.rank_num = rank_num;
        }
    }
}
