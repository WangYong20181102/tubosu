package com.tobosu.mydecorate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tobosu.mydecorate.util.Util;

/**
 * Created by dec on 2016/10/17.
 */

public class HistoryHelper extends SQLiteOpenHelper{
    private static final String TAG = HistoryHelper.class.getSimpleName();

    private Context context;

    private static final String DB_NAME = "History.db";


    private static HistoryHelper helper = null;

    private HistoryHelper(Context context) {
        super(context, DB_NAME, null, 2);
        this.context = context;
    }

    public static HistoryHelper getInstance(Context mContext){
        if(helper==null){
            helper = new HistoryHelper(mContext);
        }
        return helper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /* 搜索记录表 */
        db.execSQL("create table IF NOT EXISTS Search_History_Data(no integer primary key autoincrement, history_text text, history_date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
