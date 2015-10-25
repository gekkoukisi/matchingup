package com.planningdev.matchingup.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.planningdev.matchingup.DB.DB;
import com.planningdev.matchingup.DB.PositionDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by tokio on 10/20/15.
 */

public class Position extends BaseModel {
    public int id;
    public String name;

    public Position(Bundle bundle) {
        super(bundle);
        this.id = bundle.getInt("id");
        this.name = bundle.getString("name");
    }

    public Position(JSONObject jobj) throws JSONException {
        super(jobj);
        this.id = jobj.getInt("id");
        this.name = jobj.getString("name");
    }

    public Position(Cursor c) {
        super(c);
    }

    public static void init(SQLiteDatabase db){
        PositionDB.createTableIfNotExist(db);
    }

    public boolean isExist( SQLiteDatabase readableDB) throws Exception {
        return PositionDB.isExistData( this.id, readableDB);
    }

    public boolean save( SQLiteDatabase writableDB) throws Exception {
        if(isExist(writableDB)) {
            return PositionDB.update(this, writableDB);
        }else{
            return PositionDB.insert(this, writableDB);
        }
    }

    public boolean delete( SQLiteDatabase writableDB) throws Exception {
        if(isExist(writableDB)) {
            return PositionDB.delete(this, writableDB);
        }
        return true;
    }

    public static Position getPositionById(int id, SQLiteDatabase readableDB){
        return PositionDB.selectById(id, readableDB);
    }

    public static List<Position> getPositions( SQLiteDatabase readableDB){
        return PositionDB.selectAll(readableDB);
    }

}
