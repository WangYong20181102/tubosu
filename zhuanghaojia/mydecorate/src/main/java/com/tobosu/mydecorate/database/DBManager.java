package com.tobosu.mydecorate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tobosu.mydecorate.entity.HomeFragmentData;
import com.tobosu.mydecorate.entity.SearchHistoryEntity;
import com.tobosu.mydecorate.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dec on 2016/10/17.
 *
 */

public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();
    private Context mContext;
    private DatabaseHelper dBHelper;
    private static DBManager manager = null;


    private DBManager(Context mContext){
        this.mContext = mContext;
        dBHelper = DatabaseHelper.getInstance(mContext);
    }

    public static DBManager getInstance(Context mContext){
        if(manager == null){
            manager = new DBManager(mContext);
        }
        return manager;
    }


    /*---------------------------------  首页顶部类型 ---------------------------------*/
    /**往[hometype]表里插入数据*/
    public boolean insertHomeType(String sql, Object[] bindArgs){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = dBHelper.getWritableDatabase();
            db.execSQL(sql,bindArgs);
            flag = true;
        } catch (SQLiteException e) {
            flag = false;
//            Log.d(TAG, "---插入hometype数据失败---");
        }finally{
            if(db!=null){
                db.close();
                db = null;
            }
        }
        return flag;
    }

    /**根据索标题的数据*/
    public ArrayList<HashMap<String, String>> queryTypeData() {
        ArrayList<HashMap<String, String>> typeData = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            if (db.isOpen()) {
                // 执行查询
                Cursor cursor = null;
                cursor = db.query("Home_Type", null, null, null, null, null, null);

                while (cursor.moveToNext()) {
                    HashMap<String, String> hashData = new HashMap<String, String>();
//                    hashData.put("id", cursor.getString(cursor.getColumnIndex("id")));
                    hashData.put("title", cursor.getString(cursor.getColumnIndex("title")));
                    typeData.add(hashData);
//                    System.out.println("-->>---查询homeType-->> "+cursor.getString(cursor.getColumnIndex("title"))+" 成功 --");
                }
            }
        }catch (Exception e){
//            System.out.println("-->>---查询homeType-->>  失败 --");
        }finally {
            if(db!=null){
                db.close();
                db=null;
            }
            return typeData;
        }
    }

    public boolean qurryTypeData(String string){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            String sql = "select * from Home_Type";
            db = dBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                if(string.equals(id)){
                    flag=true;
//                    Log.d(TAG, "--查询到有重复的历史记录--");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
                db=null;
            }
        }

        return flag;
    }
    /*---------------------------------  首页顶部类型 ---------------------------------*/


    /*---------------------------------  我的关注 ---------------------------------*/
    /**往[我的关注]表里插入数据*/
    public boolean insertConcernWriter(String sql, Object[] bindArgs){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = dBHelper.getWritableDatabase();
            db.execSQL(sql,bindArgs);
            flag = true;
            Log.d(TAG, "---插入关注数据成功---");
        } catch (SQLiteException e) {
            flag = false;
            Log.d(TAG, "---插入关注数据失败---");
        }finally{
            if(db!=null){
                db.close();
                db = null;
            }
        }
        return flag;
    }

    /**
     * 查询表中是否已经有这个记录
     * @return true 有重复记录；  false 无重复记录
     */
    public boolean qurryData(String string){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            String sql = "select * from Concern_Writer_Table";
            db = dBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String writerUserId = cursor.getString(cursor.getColumnIndex("uid"));
                if(string.equals(writerUserId)){
                    flag=true;
                    Log.d(TAG, "--关注 重复writer id-->> " + string);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
                db = null;
            }
        }

        return flag;
    }


    /**
     * 查询表中是否已经有这个记录 且是否相同 不同就修改数据表中数据
     */
    public void checkEqual(String userid, String is_att){
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            if(!"".equals(is_att)){
                values.put("is_att", is_att);
            }
            String[] args = {String.valueOf(userid)};
            db.update("Recommend_Data", values, "uid=?", args);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**根据索关注的数据*/
    public ArrayList<HashMap<String, String>> queryMyConcernWriter() {
        ArrayList<HashMap<String, String>> writerList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            if (db.isOpen()) {
                // 执行查询
                Cursor cursor = db.query("Concern_Writer_Table", null, null, null, null, null, null);

                //uid, nick, sort_description, header_pic_url, count_article, is_att, is_read, time_num， time_uint

                while (cursor.moveToNext()) {
                    HashMap<String, String> hashData = new HashMap<String, String>();
//                    hashData.put("no", cursor.getString(cursor.getColumnIndex("no")));
                    hashData.put("uid", cursor.getString(cursor.getColumnIndex("uid")));
                    hashData.put("nick", cursor.getString(cursor.getColumnIndex("nick")));
                    hashData.put("sort_description", cursor.getString(cursor.getColumnIndex("sort_description")));
                    hashData.put("header_pic_url", cursor.getString(cursor.getColumnIndex("header_pic_url")));
                    hashData.put("count_article", cursor.getString(cursor.getColumnIndex("count_article")));
                    hashData.put("is_att", cursor.getString(cursor.getColumnIndex("is_att")));
                    hashData.put("is_read", cursor.getString(cursor.getColumnIndex("is_read")));
                    hashData.put("time_num", cursor.getString(cursor.getColumnIndex("time_num")));
                    hashData.put("time_uint", cursor.getString(cursor.getColumnIndex("time_uint")));
                    writerList.add(hashData);

//                    System.out.println("-->>---查询我的关注-->> " +
//                            cursor.getString(cursor.getColumnIndex("time_uint")) +
//                            cursor.getString(cursor.getColumnIndex("time_num"))+
//                            cursor.getString(cursor.getColumnIndex("sort_description"))+
//                            cursor.getString(cursor.getColumnIndex("is_read"))+
//                            cursor.getString(cursor.getColumnIndex("is_att"))+
//                            cursor.getString(cursor.getColumnIndex("count_article"))+ "[ " +
//                            cursor.getString(cursor.getColumnIndex("header_pic_url"))+ " ]" +
//                            cursor.getString(cursor.getColumnIndex("nick"))+
//                            cursor.getString(cursor.getColumnIndex("uid"))+" --");

                }
                System.out.println("-->>---查询我的关注-->>  成功 --");
            }
            return writerList;
        }catch (Exception e){
            System.out.println("-->>---查询我的关注-->>  失败 --");
        }finally {
            if(db!=null){
                db.close();
                db=null;
            }
            return writerList;
        }
    }
    /*---------------------------------  我的关注 ---------------------------------*/



    /*---------------------------------  推荐关注 ---------------------------------*/
    /**往[推荐表]表里插入数据*/
    public boolean insertRecommand(String sql, Object[] bindArgs){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = dBHelper.getWritableDatabase();
            db.execSQL(sql,bindArgs);
            flag = true;
            Log.d(TAG, "---插入Recommend数据成功---");
        } catch (SQLiteException e) {
            e.printStackTrace();
            flag = false;
            Log.d(TAG, "---插入Recommend数据失败---");
        }finally{
            if(db!=null){
                db.close();
                db = null;
            }
        }
        return flag;
    }

    /**
     * 查询表中是否已经有这个记录
     * @return true 有重复记录；  false 无重复记录
     */
    public boolean checkRecommandData(String string){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            String sql = "select * from Recommend_Data";
            db = dBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String writerUserId = cursor.getString(cursor.getColumnIndex("uid"));
                if(string.equals(writerUserId)){
                    flag=true;
                    Log.d(TAG, "--推荐 重复--");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
                db = null;
            }
        }

        return flag;
    }

    // uid, nick, header_pic_url, count_article, is_att, is_read, time_num， time_uint
    public ArrayList<HashMap<String, String>> queryRecommandData() {
        ArrayList<HashMap<String, String>> writerList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            if (db.isOpen()) {
                // 执行查询
                Cursor cursor = null;
                cursor = db.query("Recommend_Data", null, null, null, null, null, null);

                //uid, nick, header_pic_url, count_article, is_att, is_read, time_num， time_uint

                while (cursor.moveToNext()) {
                    HashMap<String, String> hashData = new HashMap<String, String>();
//                    hashData.put("no", cursor.getString(cursor.getColumnIndex("no")));
                    hashData.put("uid", cursor.getString(cursor.getColumnIndex("uid")));
                    hashData.put("nick", cursor.getString(cursor.getColumnIndex("nick")));
                    hashData.put("sort_description", cursor.getString(cursor.getColumnIndex("sort_description")));
                    hashData.put("header_pic_url", cursor.getString(cursor.getColumnIndex("header_pic_url")));
                    hashData.put("count_article", cursor.getString(cursor.getColumnIndex("count_article")));
                    hashData.put("is_att", cursor.getString(cursor.getColumnIndex("is_att")));
                    hashData.put("is_read", cursor.getString(cursor.getColumnIndex("is_read")));
                    hashData.put("time_num", cursor.getString(cursor.getColumnIndex("time_num")));
                    hashData.put("time_uint", cursor.getString(cursor.getColumnIndex("time_uint")));
                    writerList.add(hashData);
//                    System.out.println("-->>---查询推荐-->>  成功 --");
                }
            }
        }catch (Exception e){
//            System.out.println("-->>---查询推荐-->>  失败 --");
        }finally {
            if(db!=null){
                db.close();
                db=null;
            }
            return writerList;
        }
    }


    /**
     * 修改为关注
     * @param _is_att 是否关注  若传空字符串则不修改状态
     * @param _is_read 是否已读  若传空字符串则不修改状态
     * @param _uid 被关注的id
     * @param _tableName 要修改的表名
     */
    public void updateConcernData(String _is_att, String _is_read, String _uid, String _tableName){
        try{
            SQLiteDatabase db = dBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            if(!"".equals(_is_att)){
                values.put("is_att", _is_att);
            }else if(!"".equals(_is_read)) {
                values.put("is_read", _is_read);
            }
            String[] args = {String.valueOf(_uid)};
            db.update(_tableName, values, "uid=?", args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public boolean clearTable(String table){
        boolean flag = false;
        SQLiteDatabase db = null;

        try{
            db = dBHelper.getWritableDatabase();
            db.delete(table, null, null);
            flag = true;
            Log.d(TAG, "---已删除数据"+table+"表---");
        }catch(Exception e){
            e.printStackTrace();
            flag = false;
        }finally{
            if(db!=null){
                db.close();
                db=null;
            }
        }


        return flag;
    }


    /*---------------------------------  推荐关注 ---------------------------------*/





    /*---------------------------------  消息中心 ---------------------------------*/
    //    Message_Data(no integer primary key autoincrement, aid text, title text, read_flag)
    public boolean insertMessageTable(String sql, Object[] bindArgs){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = dBHelper.getWritableDatabase();
            db.execSQL(sql,bindArgs);
            flag = true;
            Log.d(TAG, "---插入Message_Data数据成功---");
        } catch (SQLiteException e) {
            flag = false;
            Log.d(TAG, "---插入Message_Data数据失败---");
        }finally{
            if(db!=null){
                db.close();
                db = null;
            }
        }
        return flag;
    }

    /**
     * 查询表中是否已经有这个记录
     * @return true 有重复记录；  false 无重复记录
     */
    public boolean checkMessageData(String string){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            String sql = "select * from Message_Data";
            db = dBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String writerUserId = cursor.getString(cursor.getColumnIndex("aid"));
                if(string.equals(writerUserId)){
                    flag=true;
                    Log.d(TAG, "--消息 重复--");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
                db = null;
            }
        }

        return flag;
    }


    /***
     * 修改数据
     * @param aid
     */
    public void updateMessage(String aid){
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("read_flag", "1");
            db.update("Message_Data", values, "aid=?", new String[]{aid});
            System.out.println("---------消息更改状态成功-------");
        }catch (Exception e){
            System.out.println("---------消息更改状态失败-------");
        }finally {
            if(db!=null){
                db.close();
                db = null;
            }
        }
    }


    public ArrayList<HashMap<String, String>> queryMessageData() {
        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            if (db.isOpen()) {
                // 执行查询
                Cursor cursor = null;
                cursor = db.query("Message_Data", null, null, null, null, null, null);

//                Message_Data(no integer primary key autoincrement, aid text, title text, read_flag)

                while (cursor.moveToNext()) {
                    HashMap<String, String> hashData = new HashMap<String, String>();
                    hashData.put("aid", cursor.getString(cursor.getColumnIndex("aid")));
                    hashData.put("title", cursor.getString(cursor.getColumnIndex("title")));
                    hashData.put("read_flag", cursor.getString(cursor.getColumnIndex("read_flag")));
                    messageList.add(hashData);
                }
            }
        }catch (Exception e){

        }finally {
            if(db!=null){
                db.close();
                db=null;
            }
            return messageList;
        }
    }

    /*---------------------------------  消息中心 ---------------------------------*/


    public boolean deleteTable(String tableName){
        boolean flag = false;
        SQLiteDatabase db = null;

        try{
            db = dBHelper.getWritableDatabase();
            db.delete(tableName, null, null);
            flag = true;
            Log.d(TAG, "---已删除数据"+tableName+"表---");
        }catch(Exception e){
            flag = false;
        }finally{
            if(db!=null){
                db.close();
                db=null;
            }
        }
        return flag;
    }

    /*--------------------------------- 大数据存储哦 ---------------------------------*/
//            aid text, uid text, title text, " +
//            "type_id text, tup_count text, collect_count text, show_count text, image_url text, time text, time_unit text, " +
//            "view_count text, c_title text, url text, nick text, header_pic_url text, child_position)

    public boolean insertMainData(String sql, Object[] bindArgs, int position){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = dBHelper.getWritableDatabase();
            db.execSQL(sql,bindArgs);
            flag = true;
            Log.d(TAG, "---插入第"+position+"页主数据成功---");
        } catch (SQLiteException e) {
            flag = false;
            Log.d(TAG, "---插入主数据失败---");
        }finally{
            if(db!=null){
                db.close();
                db = null;
            }
        }
        return flag;
    }


    /**
     * 查询表中是否已经有这个记录
     * @return true 有重复记录；  false 无重复记录
     */
    public boolean checkMainData(String _aid, String _child_position){
        boolean flag = false;
        SQLiteDatabase db = null;

        try {
            String sql = "select * from Main_Data";
            db = dBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String aid_ = cursor.getString(cursor.getColumnIndex("aid"));
                String pos = cursor.getString(cursor.getColumnIndex("child_position"));

                if(_aid.equals(aid_) && _child_position.equals(pos)){
                    flag=true;
                    Log.d(TAG, "--main data 重复--");
                }else{
                    flag = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
                db = null;
            }
        }
        return flag;
    }


    public ArrayList<HomeFragmentData> queryDataByPosition(String position) {
        ArrayList<HomeFragmentData> mainDataList = new ArrayList<HomeFragmentData>();
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            if (db.isOpen()) {
                // 执行查询 SELECT DISTINCT store_name FROM Store_Information ;
                Cursor cursor = db.rawQuery("select * from Main_Data where child_position=?", new String[]{position});
//                Cursor cursor = db.query(true, "Main_Data", new String[]{"child_position"}, null, null, position, null, null, null, null);

//                Cursor cursor = db.query(true, "Main_Data", new String[]{"child_position"}, "child_position=?", new String[]{position}, "child_position", null, null, null);

//                System.out.println("======= 查询Main_Data" +cursor.getCount());

                while (cursor.moveToNext()) {

                    /**
                     /* 使用查询语句：方式二
                     * @ distinct           --是否去除重复行            例：值为：true/false;
                     * @ table              --表名
                     * @ columns            --要查询的列(字段)          例： new String[]{"id","name","age"}
                     * @ selection          --查询条件                 例："id>?"
                     * @ selectionArgs      --查询条件的参数            例：new String[]{"3"}
                     * @ groupBy            --对查询的结果进行分组
                     * @ having             --对分组的结果进行限制
                     * @ orderby            --对查询的结果进行排序；     例："age asc"
                     * @ limit              --分页查询限制 ；           例："2,5"    从第2行开始，到第5行结束；注：行数从0 开始；
                     */

                    HomeFragmentData homeData = new HomeFragmentData();
                    homeData.setAid(cursor.getString(cursor.getColumnIndex("aid")));
                    homeData.setUid(cursor.getString(cursor.getColumnIndex("uid")));
                    homeData.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    homeData.setType_id(cursor.getString(cursor.getColumnIndex("type_id")));
                    homeData.setTup_count(cursor.getString(cursor.getColumnIndex("tup_count")));
                    homeData.setCollect_count(cursor.getString(cursor.getColumnIndex("collect_count")));
                    homeData.setShow_count(cursor.getString(cursor.getColumnIndex("show_count")));
                    homeData.setImage_url(cursor.getString(cursor.getColumnIndex("image_url")));
                    homeData.setTime(cursor.getString(cursor.getColumnIndex("time")));
                    homeData.setTime_unit(cursor.getString(cursor.getColumnIndex("time_unit")));
                    homeData.setView_count(cursor.getString(cursor.getColumnIndex("view_count")));
                    homeData.setC_title(cursor.getString(cursor.getColumnIndex("c_title")));
                    homeData.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                    homeData.setName(cursor.getString(cursor.getColumnIndex("nick")));
                    homeData.setUrl(cursor.getString(cursor.getColumnIndex("header_pic_url")));
                    mainDataList.add(homeData);

                    System.out.println("页面标记"+ position +"=" +cursor.getString(cursor.getColumnIndex("aid"))+"="+
                            cursor.getString(cursor.getColumnIndex("uid"))+"="+
                            cursor.getString(cursor.getColumnIndex("title"))+"="+
                            cursor.getString(cursor.getColumnIndex("type_id"))+"="+
                            cursor.getString(cursor.getColumnIndex("tup_count"))+"="+
                            cursor.getString(cursor.getColumnIndex("collect_count"))+"="+
                            cursor.getString(cursor.getColumnIndex("show_count"))+"="+
                            cursor.getString(cursor.getColumnIndex("image_url"))+"="+
                            cursor.getString(cursor.getColumnIndex("time"))+"="+
                            cursor.getString(cursor.getColumnIndex("time_unit"))+"="+
                            cursor.getString(cursor.getColumnIndex("view_count"))+"="+
                            cursor.getString(cursor.getColumnIndex("c_title"))+"="+
                            cursor.getString(cursor.getColumnIndex("url"))+"="+
                            cursor.getString(cursor.getColumnIndex("nick"))+"="+
                            cursor.getString(cursor.getColumnIndex("header_pic_url"))+"="+
                            "=");
                    System.out.println("============= 查询Main_Data ===============");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("============= 查询Main_Data失败 ===============");
        }finally {
            if(db!=null){
                db.close();
                db=null;
            }
            return mainDataList;
        }
    }


    public boolean deleteDataByPosition(int _child_position){
        boolean flag = false;
        // 做逻辑删除
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            db.delete("Main_Data", "child_position=?", new String[]{String.valueOf(_child_position)});
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
                db = null;
            }
        }
        return flag;
    }

    /*--------------------------------- 大数据存储哦 ---------------------------------*/





    /*---------------------------------  搜索历史 ---------------------------------*/
    /**往[Search_History_Data]表里插入数据*/
    public boolean insertHistory(String sql, Object[] bindArgs){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = dBHelper.getWritableDatabase();
            db.execSQL(sql,bindArgs);
            flag = true;
        } catch (SQLiteException e) {
            flag = false;
//            Log.d(TAG, "---插入搜索历史数据失败---");
        }finally{
            if(db!=null){
                db.close();
                db = null;
            }
        }
        return flag;
    }


    /**查询搜索历史*/
    public ArrayList<SearchHistoryEntity> queryHistory() {
        ArrayList<SearchHistoryEntity> historyList = new ArrayList<SearchHistoryEntity>();
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            if (db.isOpen()) {
                // 执行查询
                Cursor cursor = null;
//                cursor = db.query("Search_History_Data", null, null, null, null, null, null);
                cursor = db.rawQuery("select * from Search_History_Data order by history_date desc limit 10", null);
                while (cursor.moveToNext()) {
                    SearchHistoryEntity entity = new SearchHistoryEntity();
                    entity.setHistoryText(cursor.getString(cursor.getColumnIndex("history_text")));
                    entity.setHistoryTime(cursor.getString(cursor.getColumnIndex("history_date")));
                    historyList.add(entity);
                    Util.setLog("DATA_BASE", cursor.getString(cursor.getColumnIndex("history_text")));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
                db=null;
            }
            return historyList;
        }
    }



    /**查询数据表里面是否已经有该数据*/
    public boolean checkDuplicateSearchHistory(String string){
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            String sql = "select * from Search_History_Data";
            db = dBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String text = cursor.getString(cursor.getColumnIndex("history_text"));
                if(string.equals(text)){
                    flag=true;
                    //更新搜索历史的时间
                    ContentValues values = new ContentValues();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String historyTime = sdf.format(new Date());
                    String[] args = {historyTime};
                    db.update("Search_History_Data", values, "history_date=?", args);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
                db=null;
            }
        }
        return flag;
    }

    public void dropHistoryTable(){
//        try {
//            String sql="drop table user";
//            createOrOpenDatabase();
//            sld.execSQL(sql);
//            closeDatabase();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
        SQLiteDatabase db = null;
        try{
            db = dBHelper.getWritableDatabase();
            db.delete("Search_History_Data", null,null);
        }catch (Exception e){
            e.printStackTrace();
            Util.setErrorLog("DBManager", "删除历史搜索表失败");
        }
    }
}
