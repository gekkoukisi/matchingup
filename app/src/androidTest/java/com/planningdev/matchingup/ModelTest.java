package com.planningdev.matchingup;

import android.content.Context;
import android.test.ActivityTestCase;
import android.test.ActivityUnitTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.planningdev.matchingup.Activity.SplashActivity;
import com.planningdev.matchingup.DB.DB;
import com.planningdev.matchingup.DB.UserDB;
import com.planningdev.matchingup.Model.User;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tokio on 10/26/15.
 */
public class ModelTest extends ActivityUnitTestCase<SplashActivity>{



    public void setUp() throws Exception {
        super.setUp();

        /* DBのセッティング */
        Context con = this.getInstrumentation().getTargetContext().getApplicationContext();
        RenamingDelegatingContext context = new RenamingDelegatingContext( con, "test_");

        boolean result = DB.setDB(context);
        assertEquals(result, true);
    }

    public void tearDown() throws Exception {
        super.tearDown();

        DB.unsetDB();
    }


    public ModelTest() {
        super(SplashActivity.class);
    }

    public void testUser() throws Exception{
        /*  ユーザー保存テスト */
        if(UserDB.isExistTable(DB.getReadableDB())){
            UserDB.dropDB(DB.getWritableDB());
        }
        User.init(DB.getWritableDB());
        // テストデータ
        int id = 1;
        String name = "ときお";
        int positionId = 1;
        String vision = "世界を変える！";
        String skill = "android, ruby";
        boolean isRelation = true;
        String userJSONStr = "{'id':"+id+"," +
                " name:'"+name+"', " +
                "'position_id':"+positionId+", " +
                "'vision':'"+vision+"', " +
                "'skill':'"+skill+"', " +
                "'is_relation':"+isRelation+"}";
        JSONObject userJSON = null;
        try {
             userJSON = new JSONObject(userJSONStr);
        } catch (JSONException e) {
            e.printStackTrace();
            throw  new Exception("testdataの生成失敗  =>  "+e.getLocalizedMessage());
        }
        User user = null;
        try {
            user = new User(userJSON);
        } catch (JSONException e) {
            e.printStackTrace();
            throw  new Exception("userの生成失敗  =>  "+e.getLocalizedMessage());
        }
        assertEquals(user.id, id);
        assertEquals(user.name, name);
        assertEquals(user.positionId, positionId);
        assertEquals(user.vision, vision);
        assertEquals(user.skill, skill);
        try{
            user.save(DB.getWritableDB());
        }catch(Exception e){
            throw  new Exception("userの保存失敗  =>  "+e.getLocalizedMessage());
        }
        User user2 = User.getUserById(id, DB.getReadableDB());
        assertNotNull(user2);
        assertEquals(user2.id, id);
        assertEquals(user2.name, name);
        assertEquals(user2.positionId, positionId);
        assertEquals(user2.vision, vision);
        assertEquals(user2.skill, skill);
    }

    public void testPosition() throws Exception{
        /*  ユーザー保存テスト */
        if(UserDB.isExistTable(DB.getReadableDB())){
            UserDB.dropDB(DB.getWritableDB());
        }
        User.init(DB.getWritableDB());
        // テストデータ
        int id = 1;
        String name = "ときお";
        int positionId = 1;
        String vision = "世界を変える！";
        String skill = "android, ruby";
        boolean isRelation = true;
        String userJSONStr = "{'id':"+id+"," +
                " name:'"+name+"', " +
                "'position_id':"+positionId+", " +
                "'vision':'"+vision+"', " +
                "'skill':'"+skill+"', " +
                "'is_relation':"+isRelation+"}";
        JSONObject userJSON = null;
        try {
             userJSON = new JSONObject(userJSONStr);
        } catch (JSONException e) {
            e.printStackTrace();
            throw  new Exception("testdataの生成失敗  =>  "+e.getLocalizedMessage());
        }
        User user = null;
        try {
            user = new User(userJSON);
        } catch (JSONException e) {
            e.printStackTrace();
            throw  new Exception("userの生成失敗  =>  "+e.getLocalizedMessage());
        }
        assertEquals(user.id, id);
        assertEquals(user.name, name);
        assertEquals(user.positionId, positionId);
        assertEquals(user.vision, vision);
        assertEquals(user.skill, skill);
        try{
            user.save(DB.getWritableDB());
        }catch(Exception e){
            throw  new Exception("userの保存失敗  =>  "+e.getLocalizedMessage());
        }
        User user2 = User.getUserById(id, DB.getReadableDB());
        assertNotNull(user2);
        assertEquals(user2.id, id);
        assertEquals(user2.name, name);
        assertEquals(user2.positionId, positionId);
        assertEquals(user2.vision, vision);
        assertEquals(user2.skill, skill);

        String skill2 = skill+",swift";
        user2.skill = skill2;

        user2.save(DB.getWritableDB());
        User user3 = User.getUserById(id, DB.getReadableDB());
        assertEquals(user3.skill, skill2);

        assertTrue(user3.delete(DB.getWritableDB()));
        User user4 = User.getUserById(id, DB.getReadableDB());
        assertNull(user4);
    }
}
