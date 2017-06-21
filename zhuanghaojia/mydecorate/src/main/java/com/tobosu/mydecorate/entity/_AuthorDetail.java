package com.tobosu.mydecorate.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/15 09:15.
 */

public class _AuthorDetail {
    Author author;
    List<Article> articleList = new ArrayList<>();

    public _AuthorDetail(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String author = jsonObject.getString("author");
            this.author = new Author(author);
            JSONArray jsonArray = jsonObject.getJSONArray("article");
            for (int i = 0; i < jsonArray.length(); i++) {
                this.articleList.add(new Article(jsonArray.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public class Article {
        String aid;//文章id
        String uid;//作者id
        String title;//文章标题
        String image_url;//文章封面地址
        String type_id;//文章类型id
        String view_count;//浏览量
        String type_name;//类型名称
        String time;//时间1
        String time2;//时间2  “月前”“小时前”

        public Article(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.aid = jsonObject.getString("aid");
                this.uid = jsonObject.getString("uid");
                this.title = jsonObject.getString("title");
                this.image_url = jsonObject.getString("image_url");
                this.type_id = jsonObject.getString("type_id");
                this.view_count = jsonObject.getString("view_count");
                this.type_name = jsonObject.getString("type_name");
                this.time = jsonObject.getString("time");
                this.time2 = jsonObject.getString("time2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime2() {
            return time2;
        }

        public void setTime2(String time2) {
            this.time2 = time2;
        }
    }

    public class Author {
        String nick;//作者昵称
        String icon;//作者的头像
        String is_follow;//该用户是否关注该作者
        String article_count;//文章总数

        public Author(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.nick = jsonObject.getString("nick");
                this.icon = jsonObject.getString("icon");
                this.is_follow = jsonObject.getString("is_follow");
                this.article_count = jsonObject.getString("article_count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        public String getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(String is_follow) {
            this.is_follow = is_follow;
        }

        public String getArticle_count() {
            return article_count;
        }

        public void setArticle_count(String article_count) {
            this.article_count = article_count;
        }
    }
}
