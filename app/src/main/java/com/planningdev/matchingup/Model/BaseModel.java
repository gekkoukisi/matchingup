package com.planningdev.matchingup.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import org.json.JSONObject;

/**
 * Created by tokio on 10/20/15.
 */
abstract class BaseModel {

    protected int id;

    //コンストラクタ
    public BaseModel(Bundle bundle){};
    public BaseModel(JSONObject jobj){};
    public BaseModel(Cursor c){};

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


    // DBから取得  全て


    // テーブル名
    static String TABLE_NAME = "";
    // テーブル作成SQL
    static String CREATE_TABLE_SQL = "";
}
