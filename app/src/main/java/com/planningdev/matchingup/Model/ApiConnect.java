package com.planningdev.matchingup.Model;

import android.util.Log;

import com.planningdev.matchingup.Setting.Config;
import com.planningdev.matchingup.Util.UtilBeetrip;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by tokio on 10/19/15.
 */
public class ApiConnect {
    public static Observable<JSONObject> requestJSON(final String url, final Map<String,String> params, final Map<String, File> fileParams) {

        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {

                ResponseBody resBody = connect( url, params, fileParams);
                JSONObject jobj = null;
                Log.d(Config.LOG_CONNECT, "response : " + resBody);
                try {
                    jobj = new JSONObject(resBody.string());
                    if(jobj == null){
                        throw new Exception("JSONがnull");
                    }
                } catch (Exception e) {
                    Log.d(Config.LOG_TRYCATCH," レスポンス処理中にエラー  "+e.getLocalizedMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                    return;
                }
                subscriber.onNext(jobj);
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<Byte[]> requestBinary(final String url, final Map<String,String> params, final Map<String,File> fileParams) {

        return Observable.create(new Observable.OnSubscribe<Byte[]>() {
            @Override
            public void call(Subscriber<? super Byte[]> subscriber) {

                ResponseBody resBody = connect( url, params, fileParams);
                try {
                    subscriber.onNext(UtilBeetrip.bytesToBytes(resBody.bytes()));
                } catch (Exception e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                    return;
                }
                subscriber.onCompleted();
            }
        });
    }

    private static ResponseBody connect(final String url, final Map<String,String> params, final Map<String, File> fileParams) {
        RequestBody reqBody;
        if (fileParams != null) {
            MultipartBuilder builder = new MultipartBuilder();
            for (Map.Entry<String, File> entry : fileParams.entrySet()) {
                builder.addFormDataPart(
                        entry.getKey(), "tmp_name",
                        RequestBody.create(MediaType.parse("image/jpeg"), entry.getValue())
                );
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                        RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), entry.getValue())
                );
            }
            builder.type(MultipartBuilder.FORM);
            reqBody = builder.build();
        } else {
            FormEncodingBuilder builder = new FormEncodingBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            reqBody = builder.build();
        }
        Request request = new Request.Builder()
                .url(url)
                //.addHeader("User-Agent");
                .post(reqBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
