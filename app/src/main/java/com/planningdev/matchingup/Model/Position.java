package com.planningdev.matchingup.Model;

import android.database.Cursor;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tokio on 10/25/15.
 */
public class Position extends BaseModel{

    public int id;
    public String name;

    public Position(Bundle bundle) {
        super(bundle);
        this.id = bundle.getInt("id");
        this.name = bundle.getString("name");
    }

    public Position(JSONObject jobj) {
        super(jobj);
        try {
            this.id = jobj.getInt("id");
            this.name = jobj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Position(Cursor c) {
        super(c);
    }

}
