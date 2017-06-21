package com.tobosu.mydecorate.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/1 11:11.
 * 我的主页类
 */

public class _MinePage {
    String uid;//用户的Id
    String name;//用户的名称
    String icon;//用户的头像
    String view_count;//浏览量
    String collect_count;//收藏量
    String tup_count;//点赞量
    List<Attention> attentionList = new ArrayList<Attention>();//关注列表
    List<Follow> followList = new ArrayList<>();//推荐关注列表

    public _MinePage(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.uid = jsonObject.getString("uid");
            this.name = jsonObject.getString("name");
            this.icon = jsonObject.getString("icon");
            this.view_count = jsonObject.getString("view_count");
            this.collect_count = jsonObject.getString("collect_count");
            this.tup_count = jsonObject.getString("tup_count");
            JSONArray jsonArray = jsonObject.getJSONArray("attention");
            for (int i = 0; i < jsonArray.length(); i++) {
                this.attentionList.add(new Attention(jsonArray.get(i).toString()));
            }
            JSONArray jsonArray1 = jsonObject.getJSONArray("follow");
            for (int i = 0; i < jsonArray1.length(); i++) {
                this.followList.add(new Follow(jsonArray1.get(i).toString()));
            }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    public String getTup_count() {
        return tup_count;
    }

    public void setTup_count(String tup_count) {
        this.tup_count = tup_count;
    }

    public List<Attention> getAttentionList() {
        return attentionList;
    }

    public void setAttentionList(List<Attention> attentionList) {
        this.attentionList = attentionList;
    }

    public List<Follow> getFollowList() {
        return followList;
    }

    public void setFollowList(List<Follow> followList) {
        this.followList = followList;
    }

    public class Attention {
        String id;
        String aid;//作者Id
        String nick;//作者昵称
        String header_pic_url;
        String article_count;//文章数量

        public Attention(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.id = jsonObject.getString("id");
                this.aid = jsonObject.getString("aid");
                this.nick = jsonObject.getString("nick");
                this.header_pic_url = jsonObject.getString("header_pic_url");
                this.article_count = jsonObject.getString("article_count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getArticle_count() {
            return article_count;
        }

        public void setArticle_count(String article_count) {
            this.article_count = article_count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getHeader_pic_url() {
            return header_pic_url;
        }

        public void setHeader_pic_url(String header_pic_url) {
            this.header_pic_url = header_pic_url;
        }
    }

    public class Follow {
        String uid;//推荐用户的id
        String nick;//推荐用户的昵称
        String icon;//推荐用户的头像

        public Follow(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.uid = jsonObject.getString("uid");
                this.nick = jsonObject.getString("nick");
                this.icon = jsonObject.getString("icon");
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
    }
}
