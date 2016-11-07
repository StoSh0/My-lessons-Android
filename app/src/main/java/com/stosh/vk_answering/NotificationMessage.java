package com.stosh.vk_answering;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

/**
 * Created by StoSh on 05-Nov-16.
 */

public class NotificationMessage {

    private final int ID_NOTIFICATION_IN_MESSAGE = 4;
    private final int ID_NOTIFICATION_OUT_MESSAGE = 5;

    private NotificationManager notificationManager;

    public NotificationMessage(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public void createNotifyInMessage(String txtMsg) {
        Notification builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.start)
                .setContentTitle("Повідомлення")
                .setContentText("Нове повідомлення: " + txtMsg)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(ID_NOTIFICATION_IN_MESSAGE, builder);
    }

    public void crateNotifyOutMessage(String txtMsg) {
        Notification builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.start)
                .setContentTitle("Повідомлення")
                .setContentText("Відправлено повідомлення: " + txtMsg)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(ID_NOTIFICATION_OUT_MESSAGE, builder);
    }
}
