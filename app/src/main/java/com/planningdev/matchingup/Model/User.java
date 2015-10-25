package com.planningdev.matchingup.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.planningdev.matchingup.DB.UserDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by tokio on 10/20/15.
 */
public class User extends BaseModel{

    public int id;
    public int positionId;
    public String name;
    public String vision;
    public String skill;
    public boolean isRelation;

    public User(Bundle bundle) {
        super(bundle);
        this.id = bundle.getInt("id");
        this.name = bundle.getString("name");
        this.positionId = bundle.getInt("position_id");
        this.vision = bundle.getString("vision");
        this.skill = bundle.getString("skill");
        this.isRelation = bundle.getBoolean("is_relation");
    }

    public User(JSONObject jobj) throws JSONException {
        super(jobj);
        this.id = jobj.getInt("id");
        this.name = jobj.getString("name");
        this.positionId = jobj.getInt("position_id");
        this.vision = jobj.getString("vision");
        this.skill = jobj.getString("skill");
        this.isRelation = jobj.getBoolean("is_relation");
    }

    public User(Cursor c) {
        super(c);
    }

    public static void init(SQLiteDatabase db){
        UserDB.createTableIfNotExist(db);
    }

    public boolean isExist( SQLiteDatabase readableDB) throws Exception {
        return UserDB.isExistData( this.id, readableDB);
    }

    public boolean save( SQLiteDatabase writableDB) throws Exception {
        if(isExist(writableDB)) {
            return UserDB.update(this, writableDB);
        }else{
            return UserDB.insert(this, writableDB);
        }
    }

    public boolean delete( SQLiteDatabase writableDB) throws Exception {
        if(isExist(writableDB)) {
            return UserDB.delete(this, writableDB);
        }
        return true;
    }

    public static User getUserById(int id, SQLiteDatabase readableDB){
        return UserDB.selectById(id, readableDB);
    }

    public static List<User> getUsers( SQLiteDatabase readableDB){
        return UserDB.selectAll(readableDB);
    }

}
