package com.planningdev.matchingup.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.planningdev.matchingup.Model.User;
import com.planningdev.matchingup.Setting.Config;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tokio on 10/20/15.
 */
public class UserDB extends DB{

    // テーブル名
    private static String TABLE_NAME = "users";
    // テーブル作成SQL
    private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
                                  + " ( [_id] int not null primary key,"
                                  +   " [_name] string );";;

    // カーソルからオブジェクト生成
    public static User getUserFromCorsor(Cursor c){
        Bundle bundle = new Bundle();
        bundle.putInt("id", c.getInt(0));
        bundle.putString("name",c.getString(1));
        return new User(bundle);
    }

    /***********************************/
    /*              SELECT             */
    /***********************************/
    // DBから取得  id指定
    public static User selectById(int id, SQLiteDatabase db){
        String sql = "select * from "+TABLE_NAME+" where [_id] = "+id+" ;";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            return getUserFromCorsor(c);
        }else{
            return null;
        }
    }

    // DBから取得 全て取得
    public static List<User> selectAll( SQLiteDatabase db){
        List<User> users = new ArrayList<>();
        String sql = "select * from "+TABLE_NAME+" ;";
        Cursor c = db.rawQuery(sql, null);
        int userLen = c.getCount();
        for(int i=0;i<userLen;i++) {
            User user = getUserFromCorsor(c);
            users.add(user);
        }
        return users;
    }

    /***********************************/
    /*              INSERT             */
    /***********************************/
    public static boolean insert(User user, SQLiteDatabase db){
        String sql = createInsertSQL(user);
        try{
            db.execSQL(sql);
            return true;
        }catch(Exception e){
            Log.d(Config.LOG_TRYCATCH, "user insert error => "+e.getLocalizedMessage());
            return false;
        }
    }

     // SQL生成関数
    public static String createInsertSQL(User user) {
        String sql = "insert into " + TABLE_NAME + " ( "
                + " [_id],"
                + " [_name]"
                + " ) values ("
                + user.id + ", "
                + "'" + user.name + "'"
                + " ) ;";
        return sql;
    }

    /***********************************/
    /*              UPDATE             */
    /***********************************/
    public static boolean update(User user, SQLiteDatabase db){
        String sql = createUpdateSQL(user);
        try{
            db.execSQL(sql);
            return true;
        }catch(Exception e){
            Log.d(Config.LOG_TRYCATCH, "user update error => "+e.getLocalizedMessage());
            return false;
        }
    }

    public static String createUpdateSQL(User user){
        String sql = "update "+TABLE_NAME+" set "
                + " [_name] = " + user.name
                + " where [_id] = "+user.id+" ;";
        return sql;
    }

    /***********************************/
    /*              DELETE             */
    /***********************************/
    public static boolean delete(User user, SQLiteDatabase db){
        String sql = createDeleteSQL(user);
        try{
            db.execSQL(sql);
            return true;
        }catch(Exception e){
            Log.d(Config.LOG_TRYCATCH, "user delete error => "+e.getLocalizedMessage());
            return false;
        }
    }

    public static String createDeleteSQL(User user){
        String sql = "delete "+TABLE_NAME
                   + " where [_id] = "+user.id+" ;";
        return sql;
    }
}
