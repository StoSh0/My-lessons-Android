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

import com.stosh.vk_answering.VkApi.LongPollServer;

public class ListenerVk extends Service {

    private final int ID_EXIT = 1;
    private final int ID_NOTIFICATION_SERVICE = 3;

    private NotificationManager notificationManager;
    public boolean checkRun = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkRun = true;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        onStartForeground();

        new LongPollServer(this, new NotificationMessage(notificationManager));
    }

    private void onStartForeground() {
        Intent notificationIntent = new Intent(this, ListenerVk.class)
                .putExtra("EXIT", ID_EXIT);
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getIntExtra("EXIT", 0) == ID_EXIT) stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "Destroy");
        notificationManager.cancelAll();
        checkRun = false;
    }
}




