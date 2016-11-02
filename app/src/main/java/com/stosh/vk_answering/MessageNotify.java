package com.stosh.vk_answering;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MessageNotify extends Service {

    private final int START_SERVICE = 1;
    private final int STOP_SERVICE = 2;
    private final int ID_NOTIFICATION_SERVICE = 3;
    private final int ID_NOTIFICATION_IN_MESSAGE = 4;
    private final int ID_NOTIFICATION_OUT_MESSAGE = 5;
    NotificationManager notificationManager;

    private String key;
    private String server;
    private String ts;

    private static boolean checkStart = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getIntExtra("Start_Service", 0) == START_SERVICE && checkStart == false) onStartForeground();
        if (intent.getIntExtra("Close", 1) == STOP_SERVICE) stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void onStartForeground() {
        Intent notificationIntent = new Intent(this, MessageNotify.class)
                .putExtra("Close", STOP_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.start)
                .setContentTitle("Сервіс запитів на сервер")
                .setContentText("Слухаю події..")
                .addAction(R.drawable.stop, "Stop", pendingIntent)
                .build();

        startForeground(ID_NOTIFICATION_SERVICE, builder);
        Log.d("Service", "StartForeground");
        checkStart = true;
        getLongPollServer();
    }

    private void getLongPollServer() {
        Log.d("Service", "getLongPollServer");
        VKRequest request = new VKRequest("messages.getLongPollServer", VKParameters.from("use_ssl", "0", "need_pts", "0"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject jsonObject = new JSONObject(response.responseString);
                    JSONObject responseSt = jsonObject.getJSONObject("response");
                    key = responseSt.getString("key");
                    server = responseSt.getString("server");
                    ts = responseSt.getString("ts");
                    Log.d("Service", "key = " + responseSt.getString("key"));
                    Log.d("Service", "server = " + responseSt.getString("server"));
                    Log.d("Service", "ts = " + responseSt.getString("ts"));
                    getLongPoll();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getLongPoll() {
        Log.d("Service", "LongPoll");

        final Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://imv4.vk.com/")
                .build();

        Retro retro = retrofit.create(Retro.class);

        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("act", "a_check");
        paramMap.put("key", key);
        paramMap.put("ts", ts);
        paramMap.put("wait", "25");
        paramMap.put("mode", "2");
        paramMap.put("v", "1");
        Log.d("Service", "1");
        String domain = server.substring(12);
        final Call<ResponseBody> call = retro.longPoll(domain, paramMap);
        Log.d("Service", "2");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.d("Service", "3 " + jsonObject);
                    ts = jsonObject.getString("ts");

                    JSONArray array = jsonObject.getJSONArray("updates");
                    Log.d("Service", " array= " + array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONArray array1 = array.getJSONArray(i);
                        if (array1.getString(0).equals("4")) {
                            String key = array1.getString(2);
                            String textMSG = array1.getString(6);
                            if (key.equals("19") || key.equals("51"))crateNotifyOutMessage(textMSG);
                            else createNotifyInMessage(textMSG);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (checkStart == true) getLongPoll();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Service", "5" + t);
                if (checkStart == true) getLongPoll();
            }
        });
    }

    private void createNotifyInMessage(String txtMsg) {
        Notification builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.start)
                .setContentTitle("Повідомлення")
                .setContentText("Нове повідомлення: " + txtMsg)
                .setAutoCancel(true)
                .build();

        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICATION_IN_MESSAGE, builder);
    }

    private void crateNotifyOutMessage(String txtMsg){
        Notification builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.start)
                .setContentTitle("Повідомлення")
                .setContentText("Відправлено повідомлення: " + txtMsg)
                .setAutoCancel(true)
                .build();

        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICATION_OUT_MESSAGE, builder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "Destroy");
        checkStart = false;
        notificationManager.cancelAll();
    }
}




