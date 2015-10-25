package com.planningdev.matchingup.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tokio on 10/20/15.
 */
public class DB {

    private static MySQLiteOpenHelper helper;
    private static SQLiteDatabase writableDB = null;
    private static SQLiteDatabase readableDB = null;
    private final static String DB_NAME = "matchingup.db";
    private final static int DB_VERSION = 1;

    public static boolean setDB(Context con){
        helper = new MySQLiteOpenHelper(con, DB_NAME, DB_VERSION);
        if(helper != null && con != null) {
            readableDB = helper.getReadableDatabase();
            writableDB = helper.getWritableDatabase();
            return true;
        }
        return false;
    }

    public static void unsetDB(){
        helper.close();
    }

    public static SQLiteDatabase getReadableDB(){
        return readableDB;
    }

    public static SQLiteDatabase getWritableDB(){
        return writableDB;
    }


    public static boolean isExistTable(SQLiteDatabase db){
        String query = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='"+TABLE_NAME+"';";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String result = c.getString(0);
        return !result.equals("0");
    }

    // テーブル名
    static String TABLE_NAME = "";
    // テーブル作成SQL
    static String CREATE_TABLE_SQL = "";

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper{

        public MySQLiteOpenHelper(Context con, String dbName, int dbVersion){
            super(con, dbName, null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public static int boolToInt(boolean bool){
        if(bool){
            return 1;
        }else{
            return 0;
        }
    }

    public static boolean intToBool(int num){
        if(num == 0){
            return false;
        }else{
            return true;
        }
    }
}
