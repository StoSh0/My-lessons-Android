package com.stosh.vk_answering;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

public class MessageNotify extends Service {

    private final int ID_NOTIFICATION = 1337;

    private Notification builder;
    @Override
    public void onCreate() {
        super.onCreate();
        onStartForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void onStartForeground(){
        Intent notificationIntent = new Intent(this, Main.class);

        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.start)
                .setContentTitle("My Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, builder);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
