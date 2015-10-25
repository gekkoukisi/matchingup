package com.planningdev.matchingup.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.planningdev.matchingup.Model.Position;
import com.planningdev.matchingup.Setting.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tokio on 10/25/15.
 */
public class PositionDB extends DB{

    // テーブル名
    private static String TABLE_NAME = "positions";
    // テーブル作成SQL
    private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME
                                  + " ( [_id] int not null primary key,"
                                  +   " [_name] string );";

    public static void createTableIfNotExist(SQLiteDatabase db){
        if(isExistTable(db)) return;
        db.execSQL(CREATE_TABLE_SQL);
    }

    // カーソルからオブジェクト生成
    public static Position getPositionFromCorsor(Cursor c){
        Bundle bundle = new Bundle();
        bundle.putInt("id", c.getInt(0));
        bundle.putString("name",c.getString(1));
        return new Position(bundle);
    }

    /***********************************/
    /*              SELECT             */
    /***********************************/
    // DBから取得  id指定
    public static Position selectById(int id, SQLiteDatabase db){
        String sql = "select * from "+TABLE_NAME+" where [_id] = "+id+" ;";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            return getPositionFromCorsor(c);
        }else{
            return null;
        }
    }

    // DBから取得 全て取得
    public static List<Position> selectAll( SQLiteDatabase db){
        List<Position> positions = new ArrayList<>();
        String sql = "select * from "+TABLE_NAME+" ;";
        Cursor c = db.rawQuery(sql, null);
        int positionLen = c.getCount();
        for(int i=0;i<positionLen;i++) {
            Position position = getPositionFromCorsor(c);
            positions.add(position);
        }
        return positions;
    }

    /***********************************/
    /*              INSERT             */
    /***********************************/
    public static boolean insert(Position position, SQLiteDatabase db) throws Exception{
        String sql = createInsertSQL(position);
        db.execSQL(sql);
        return true;
    }

     // SQL生成関数
    public static String createInsertSQL(Position position) {
        String sql = "insert into " + TABLE_NAME + " ( "
                + " [_id],"
                + " [_name]"
                + " ) values ("
                + position.id + ", "
                + "'" + position.name + "'"
                + " ) ;";
        return sql;
    }

    /***********************************/
    /*              UPDATE             */
    /***********************************/
    public static boolean update(Position position, SQLiteDatabase db){
        String sql = createUpdateSQL(position);
        try{
            db.execSQL(sql);
            return true;
        }catch(Exception e){
            Log.d(Config.LOG_TRYCATCH, "position update error => "+e.getLocalizedMessage());
            return false;
        }
    }

    public static String createUpdateSQL(Position position){
        String sql = "update "+TABLE_NAME+" set "
                + " [_name] = " + position.name
                + " where [_id] = "+position.id+" ;";
        return sql;
    }

    /***********************************/
    /*              DELETE             */
    /***********************************/
    public static boolean delete(Position position, SQLiteDatabase db){
        String sql = createDeleteSQL(position);
        try{
            db.execSQL(sql);
            return true;
        }catch(Exception e){
            Log.d(Config.LOG_TRYCATCH, "position delete error => " + e.getLocalizedMessage());
            return false;
        }
    }

    public static String createDeleteSQL(Position position){
        String sql = "delete "+TABLE_NAME
                   + " where [_id] = "+position.id+" ;";
        return sql;
    }

    public static void dropDB(SQLiteDatabase db){
        String sql = "drop table "+TABLE_NAME+";";
        db.execSQL(sql);
    }
}
