package com.planningdev.matchingup.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.planningdev.matchingup.Model.ApiConnect;
import com.planningdev.matchingup.R;
import com.planningdev.matchingup.Setting.Config;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // ユーザー登録済みかどうか

        Map<String, String> params = new HashMap<String, String>();
        params.put("ptype","apis");
        params.put("action", "newslist");
        ApiConnect.requestJSON(Config.API_URL, params, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        Log.d(Config.LOG_CONNECT, "onNext : "+jsonObject.toString());
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Intent intent = new Intent( this, );
                if(!isFinishing()) {
                    startActivity(intent);
                }*/
            }
        }, 3000);
    }
}