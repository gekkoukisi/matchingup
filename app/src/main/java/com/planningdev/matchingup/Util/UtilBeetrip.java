package com.planningdev.matchingup.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by tokio on 8/3/15.
 */
public class UtilBeetrip {

    public static byte[] BmpTobyte(Bitmap bmp) {
        Log.d("tokio","bitmapをbyteに変換します〜！　今んとこ大きさは"+bmp.getByteCount()+"ほどだす〜！");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        Log.d("tokio","bitmapをbyteに変換しましたで！　大きさは"+bytes.length+"ほどだす！！");
        return bytes;
    }

    public static Byte[] BmpToByte(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return UtilBeetrip.bytesToBytes(bytes);
    }

    /* SQLite内に指定のテーブルがあるかチェック */
    public static boolean SQLiteIsExistTable(String tableName,SQLiteDatabase db){
        String query = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='"+tableName+"';";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String result = c.getString(0);
        return !result.equals("0");
    }

    /* SQLite内の指定テーブル内に指定したデータがあるかチェック */
    public static boolean SQLiteIsExistData(String tableName,String columnName, String value, SQLiteDatabase db){
        String query = "SELECT COUNT(*) FROM "+tableName+" WHERE "+columnName+"="+value+";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String result = c.getString(0);
        return !result.equals("0");
    }

    public static Date unixtimeToDate(int unixtime){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(unixtime);
        Date date = cal.getTime();
        return date;
    }

    public static String unixtimeToString(int unixtime, SimpleDateFormat format){
        Calendar cal = Calendar.getInstance();
        long unixtimeMillis = (long)unixtime;
        unixtimeMillis *= 1000;
        cal.setTimeInMillis(unixtimeMillis);
        Log.d("tokio", cal.get(Calendar.YEAR) + "  <===== year " + unixtimeMillis);
        Date date = cal.getTime();
        String dateString = format.format(date);
        return dateString;
    }

    public static long stringToUnixtime(String dateStr, SimpleDateFormat format){
        Date date;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime();
    }

    public static Bitmap idToBitmap(int id,Context con){
        return BitmapFactory.decodeResource(con.getResources(), id);
    }

    public static Bitmap byteToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.<br>
     */
    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    /* 指定時間後にそよりを実行する関数 */
    public static void setTimeout(final Runnable runnable, final int milisecond, boolean isMain){
        if(isMain) {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable, milisecond);
                }
            });
        }else{
            new Handler().postDelayed(runnable, milisecond);
        }
    }


    /* 定期実行用のインタフェース */
    interface IntervalFunction{
        void function();
    }

    /* 指定時間で定期実行する関数 */
    public static void setInterval(final IntervalFunction intervalFunction, final int milisecond){
        final Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        intervalFunction.function();
                        handler.postDelayed(this, milisecond);
                    }
                }, milisecond
        );

    }

    /* Object <=> Byte[] */
    public static byte[] objectToByte(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(o);
        byte[] bytes = bos.toByteArray();
        out.close();
        bos.close();
        return bytes;
    }
    public static Object byteToObject(byte[] bytes)
            throws OptionalDataException
            , StreamCorruptedException
            , ClassNotFoundException
            , IOException
    {
        return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
    }

    public static long getNowUnixTime(){
        return System.currentTimeMillis() / 1000;
    }

    /************************************************/
    /*現在のスレッドがUIスレッドかどうかをチェックする関数 */
    /************************************************/
    public static boolean isCurrentUI(Context con) {
        return Thread.currentThread().equals(con.getMainLooper().getThread());
    }

    /* 非同期通信を行うためのクラス */
    public static class BackgroundTask<T,M,K> extends AsyncTask<T,M,K>{

        BackgroundTask<T,M,K> self;
        BackgroundCallback<T,M,K> callback;
        public Object obj = null;

        public BackgroundTask(BackgroundCallback callback){
            this.self = this;
            this.callback = callback;
        }

        public BackgroundTask( Object obj, BackgroundCallback callback){
            this.self = this;
            this.obj = obj;
            this.callback = callback;
        }


        @Override
        protected K doInBackground(T... params) {
            Log.d("thread","start  backgroundTask "+Thread.activeCount());
            /* タスクがキャンセルされていたら終わる */
            if(isCancelled()){
                return null;
            }
            return callback.backgroundTask(self, params);
        }

        @Override
        protected void onPostExecute(K response) {

            callback.mainThreadTask(self, obj, response);
            Log.d("thread","background finish  "+ Thread.activeCount());
        }

        public interface BackgroundCallback<T,M,K>{
            K backgroundTask(BackgroundTask self, T... params);
            void mainThreadTask(BackgroundTask self, Object obj, Object... responses);
        }
    }

    public static int booleanToInt(boolean bool){
        if(bool){
            return 1;
        }else{
            return 0;
        }
    }

    public static boolean intToBoolean(int num){
        if(num > 0){
            return true;
        }else{
            return false;
        }
    }

    public static Byte[] bytesToBytes(byte[] bytes){
        int len = bytes.length;
        Byte[] retBytes = new Byte[len];
        for(int i=0;i<len;i++){
            retBytes[i] = bytes[i];
        }
        return retBytes;
    }

    public static byte[] BytesTobytes(Byte[] bytes){
        int len = bytes.length;
        byte[] retBytes = new byte[len];
        for(int i=0;i<len;i++){
            retBytes[i] = bytes[i];
        }
        return retBytes;
    }

    public static void alert(String title, String msg, String leftText, DialogInterface.OnClickListener leftListener, String rightText, DialogInterface.OnClickListener rightListener, Context con) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(con);
                dialog.setTitle(title);
                dialog.setMessage(msg);
                dialog.setNegativeButton(leftText, leftListener);
                if(rightText != null) {
                    dialog.setPositiveButton(rightText, rightListener);
                }
                dialog.show();
    }

    public static Bitmap createBitmap(String path, int width, int height) {

        BitmapFactory.Options option = new BitmapFactory.Options();

        // 情報のみ読み込む
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, option);

        if (option.outWidth < width || option.outHeight < height) {
            // 縦、横のどちらかが指定値より小さい場合は普通にBitmap生成
            return BitmapFactory.decodeFile(path);
        }

        float scaleWidth = ((float) width) / option.outWidth;
        float scaleHeight = ((float) height) / option.outHeight;

        int newSize = 0;
        int oldSize = 0;
        if (scaleWidth > scaleHeight) {
            newSize = width;
            oldSize = option.outWidth;
        } else {
            newSize = height;
            oldSize = option.outHeight;
        }

        // option.inSampleSizeに設定する値を求める
        // option.inSampleSizeは2の乗数のみ設定可能
        int sampleSize = 1;
        int tmpSize = oldSize;
        while (tmpSize > newSize) {
            sampleSize = sampleSize * 2;
            tmpSize = oldSize / sampleSize;
        }
        if (sampleSize != 1) {
            sampleSize = sampleSize / 2;
        }

        option.inJustDecodeBounds = false;
        option.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(path, option);
    }

}