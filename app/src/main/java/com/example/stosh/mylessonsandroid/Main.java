package com.example.stosh.mylessonsandroid;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main extends AppCompatActivity {

    public static final int ID_DIALOG_DATE = 1;
    public static final int ID_NOTIFICATION = 20;

    private Unbinder unbinder;

    public NotificationManager mNotificationManager;
    public NotificationCompat.Builder mBuilder;

    public int my_year = 2016, my_month = 10, my_day = 7;

    @BindView(R.id.textView)
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.button_call_time, R.id.button_start, R.id.button_stop})
    public void onButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.button_call_time:
                showDialog(ID_DIALOG_DATE);
                break;
            case R.id.button_start:
                mBuilder = (NotificationCompat.Builder)
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.start)
                                .setContentTitle("Сповіщення")
                                .setContentText(textView.getText())
                                .setAutoCancel(true);

                Intent resultIntent = new Intent(this, Main.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(Main.class);
                stackBuilder.addNextIntent(resultIntent);

                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(resultPendingIntent);

                mNotificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(ID_NOTIFICATION, mBuilder.build());
                break;
            case R.id.button_stop:

                mNotificationManager.cancel(ID_NOTIFICATION);
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        if (id == ID_DIALOG_DATE) {
            DatePickerDialog myDatePicker = new DatePickerDialog(
                    this,
                    myListenerDateDialog,
                    my_year,
                    my_month,
                    my_day
            );
            return myDatePicker;
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog.OnDateSetListener myListenerDateDialog =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    my_year = year;
                    my_month = month;
                    my_day = dayOfMonth;
                    textView.setText("Вибрана дата:" + my_year + "." + my_month + "." + my_day);
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
