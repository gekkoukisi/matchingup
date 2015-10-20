package com.planningdev.matchingup.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tokio on 10/20/15.
 */
public class DB {

    public static void createTableIfNotExist(SQLiteDatabase db){
        if(isExistTable(db)) return;
        db.execSQL(CREATE_TABLE_SQL);
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
}
