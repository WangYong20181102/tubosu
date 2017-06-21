package com.tobosu.mydecorate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tobosu.mydecorate.util.Util;

/**
 * Created by dec on 2016/10/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private Context context;

    private static final String DB_NAME = "ToBoSu.db";


    private static DatabaseHelper helper = null;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
        this.context = context;
    }

    public static DatabaseHelper getInstance(Context mContext){
        if(helper==null){
            helper = new DatabaseHelper(mContext);
        }
        return helper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        /* 搜索记录表 */
        db.execSQL("create table IF NOT EXISTS Search_History_Data(no integer primary key autoincrement, history_text text, history_date text)");

        Util.setErrorLog("DatabaseHelper", "到底有没有创建这个表？");
        /**
         *
         "aid": "74",
         "uid": "308608",
         "title": "地板泡水如何处理？ 三招帮你解决问题",
         "type_id": "2",
         "tup_count": "0",  点赞数
         "collect_count": "0", 收藏数
         "show_count": "0", 分享数
         "image_url": "http://cdn01.tobosu.net/mt_banner/2016-11-08/5821411d1d78c.jpg", 图片url
         "time_rec": {-
                     "time": 9,
                     "time_unit": "小时前"
                    },
         "time_create": "1478574381",
         "view_count": "23",
         "system_code": "jiazhuang",
         "c_title": "家装",
         "url": "http://bshare.optimix.asia/barCode?site=weixin&url=http://www.tobosu.com/jiazhuang/74html", 可以不用管
         "nick": "广州苹果装饰", 作者名称
         "header_pic_url": " 头像url
         */
        db.execSQL("create table IF NOT EXISTS Main_Data(no integer primary key autoincrement, aid text, uid text, title text, " +
                "type_id text, tup_count text, collect_count text, show_count text, image_url text, time text, time_unit text, " +
                "view_count text, c_title text, url text, nick text, header_pic_url text, child_position text)");

        db.execSQL("create table IF NOT EXISTS Home_Type(no integer primary key autoincrement, id text, title text)");

        db.execSQL("create table IF NOT EXISTS Recommend_Data(no integer primary key autoincrement, uid text, nick text, sort_description text, header_pic_url text," +
                " count_article text, is_att text, is_read text, time_num text, time_uint text)");

        db.execSQL("create table IF NOT EXISTS Concern_Writer_Table(no integer primary key autoincrement, uid text, nick text, sort_description text, header_pic_url text," +
                " count_article text, is_att text, is_read text, time_num text, time_uint text)");

        db.execSQL("create table IF NOT EXISTS Message_Data(no integer primary key autoincrement, aid text, title text, read_flag text)");

//        db.execSQL("create table IF NOT EXISTS AppData(no integer primary key autoincrement, type_position text, title text)");


        /**
         "aid": "54",
         "uid": "212425",
         "title": "15万就能打造125平的北欧宜家三居室",
         "type_id": "2",
         "tup_count": "0",
         "collect_count": "0",
         "show_count": "1",
         "image_url": "http://cdn01.tobosu.net/mt_banner/2016-10-25/580ecc670f41f.jpg",
         "time_rec": {-
         "time": "01",
         "time_unit": "周前"},
         "time_create": "1477364866",
         "view_count": "115",
         "system_code": "jiazhuang",
         "c_title": "家装",
         "url": "http://bshare.optimix.asia/barCode?site=weixin&url=http://www.tobosu.com/jiazhuang/54html",
         "nick": "室内装修设计",
         "header_pic_url":
         */






    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
