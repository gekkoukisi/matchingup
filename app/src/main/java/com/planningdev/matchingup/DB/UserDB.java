package com.planningdev.matchingup.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    protected static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
                                  + " ( [_id] int not null primary key,"
                                  +   " [_name] string,"
                                  +   " [_position_id] int,"
                                  +   " [_vision] string,"
                                  +   " [_skill] string,"
                                  +   " [_is_relation] int );";


    public static void createTableIfNotExist(SQLiteDatabase db){
        if(isExistTable(db)) return;
        db.execSQL(CREATE_TABLE_SQL);
    }
    // カーソルからオブジェクト生成
    public static User getUserFromCorsor(Cursor c){
        Bundle bundle = new Bundle();
        bundle.putInt("id", c.getInt(0));
        bundle.putString("name", c.getString(1));
        bundle.putInt("position_id", c.getInt(2));
        bundle.putString("vision", c.getString(3));
        bundle.putString("skill", c.getString(4));
        bundle.putBoolean("is_relation", DB.intToBool(c.getInt(5)));
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
    public static boolean insert(User user, SQLiteDatabase db) throws Exception{
        String sql = createInsertSQL(user);
        db.execSQL(sql);
        return true;
    }

     // SQL生成関数
    public static String createInsertSQL(User user) {
        String sql = "insert into " + TABLE_NAME + " ( "
                + " [_id],"
                + " [_name],"
                + " [_vision],"
                + " [_position_id],"
                + " [_skill],"
                + " [_is_relation]"
                + " ) values ("
                + user.id + ", "
                + "'" + user.name + "',"
                + "'" + user.vision + "',"
                + user.positionId + ", "
                + "'" + user.skill + "',"
                + DB.boolToInt(user.isRelation) + ""
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
                + " ,[_vision] = " + user.vision
                + " ,[_skill] = " + user.skill
                + " ,[_position_id] = " + user.positionId
                + " ,[_is_relation] = " + DB.boolToInt(user.isRelation)
                + " ,[_skill] = " + user.skill
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

    public static void dropDB(SQLiteDatabase db){
        String sql = "drop table "+TABLE_NAME+";";
        db.execSQL(sql);
    }
}
