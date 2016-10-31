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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MessageNotify extends Service {

    private final int START_SERVICE = 1;
    private final int STOP_SERVICE = 2;
    private final int ID_NOTIFICATION = 1337;
    private final String URL = "https://api.vk.com/method/";

    private  String ACCESS_TOKEN;
    private Gson gson = new GsonBuilder().create();

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();

    private Retro retro = retrofit.create(Retro.class);

    private Notification builder;
    private NotificationManager notificationManager;

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

        if (intent.getIntExtra("Start_Service", 0) == START_SERVICE) {
            if (builder == null) {
                ACCESS_TOKEN = intent.getStringExtra("ACCESS_TOKEN");
                onStartForeground();
                getLongPollServer();

                Log.d("Service"," "+ ACCESS_TOKEN);
            } else {
                return super.onStartCommand(intent, flags, startId);
            }
        } else if (intent.getIntExtra("Close", 1) == STOP_SERVICE) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "Destroy");

        notificationManager.cancel(ID_NOTIFICATION);
    }


    private void onStartForeground() {

        Intent notificationIntent = new Intent(this, MessageNotify.class);

        notificationIntent.putExtra("Close", STOP_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.start)
                .setContentTitle("Сервіс запитів на сервер")
                .setContentText("Слухаю події..")
                .addAction(R.drawable.stop, "Stop", pendingIntent).build();

        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(ID_NOTIFICATION, builder);
        Log.d("Service", "StartForeground");

    }

    private void getLongPollServer() {
        Log.d("Service", "Start");

        final String METHOD = "messages.getLongPollServer";
        Map<String, String> mapJson = new HashMap<>();
        mapJson.put("use_ssl", "0");
        mapJson.put("need_pts", "0");
        mapJson.put("access_token", ACCESS_TOKEN);
        mapJson.put("v", "5.59");

        final Call<ResponseBody> call = retro.messageGetLongPollServer(METHOD, mapJson);
        Log.d("Service", "call.execute");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    Log.d("Service", "" + response.body().string());
                    Map<String, String> map = gson.fromJson(response.body().string(), Map.class);
                    Log.d("Service","" + map);


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Service", " " + e);
                }
            }
        });
        t.start();
    }
}


