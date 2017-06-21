package com.tobosu.mydecorate.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/5/31 14:01.
 */

public class _HomePage {
    private List<Carousel> carouselList = new ArrayList<Carousel>();
    private List<Article> articleList = new ArrayList<Article>();
    private List<Author> authorList = new ArrayList<Author>();

    public _HomePage() {
    }

    public _HomePage(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray carouselArray = jsonObject.getJSONArray("carousel");
            for (int i = 0; i < carouselArray.length(); i++) {
                this.carouselList.add(new Carousel(carouselArray.get(i).toString()));
            }
            JSONArray articleArray = jsonObject.getJSONArray("article");
            for (int i = 0; i < articleArray.length(); i++) {
                this.articleList.add(new Article(articleArray.get(i).toString()));
            }
            JSONArray authorArray = jsonObject.getJSONArray("author");
            for (int i = 0; i < authorArray.length(); i++) {
                this.authorList.add(new Author(authorArray.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Carousel> getCarouselList() {
        return carouselList;
    }

    public void setCarouselList(List<Carousel> carouselList) {
        this.carouselList = carouselList;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public class Carousel {
        public String id;//轮播图的id
        public String img_url;//轮播图的地址
        public String content_url;//轮播图跳转的地址

        public Carousel(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.id = jsonObject.getString("id");
                this.img_url = jsonObject.getString("img_url");
                this.content_url = jsonObject.getString("content_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    public class Article {
        String aid;//文章的id
        String uid;//作者的id
        String title;//文章的标题
        String image_url;//文章的封面图
        String view_count;//文章的人气数

        public Article(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.aid = jsonObject.getString("aid");
                this.uid = jsonObject.getString("uid");
                this.title = jsonObject.getString("title");
                this.image_url = jsonObject.getString("image_url");
                this.view_count = jsonObject.getString("view_count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getAid() {
            return aid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setAid(String aid) {
            this.aid = aid;
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
    }

    public class Author {
        String uid;//作者的id
        String view_count;//作者的浏览数
        String nick;//作者的昵称
        String icon;//作者的头像
        String article_count;//作者发布文章数量

        public Author(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.uid = jsonObject.getString("uid");
                this.view_count = jsonObject.getString("view_count");
                this.nick = jsonObject.getString("nick");
                this.icon = jsonObject.getString("icon");
                this.article_count = jsonObject.getString("article_count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getArticle_count() {
            return article_count;
        }

        public void setArticle_count(String article_count) {
            this.article_count = article_count;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }
    }
}
