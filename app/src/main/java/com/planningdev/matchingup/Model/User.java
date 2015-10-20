package com.planningdev.matchingup.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tokio on 10/20/15.
 */
public class User extends BaseModel{

    public int id;
    public String name;

    public User(Bundle bundle) {
        super(bundle);
        this.id = bundle.getInt("id");
        this.name = bundle.getString("name");
    }

    public User(JSONObject jobj) {
        super(jobj);
        try {
            this.id = jobj.getInt("id");
            this.name = jobj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(Cursor c) {
        super(c);

    }
}
