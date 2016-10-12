package com.example.stosh.mylessonsandroid;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main extends AppCompatActivity {

    public static final int ID_NOTIFICATION = 20;

    @BindView(R.id.textView)
    TextView textView;

    private Calendar calendar;
    private NotificationManager NotificationManager;
    private Notification Builder;
    private DatePickerDialog MyDatePicker;
    private Unbinder Unbinder;
    private int
            MyYear,
            MyMonth,
            MyDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.button_call_time, R.id.button_start, R.id.button_stop})
    public void onButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.button_call_time:
                showDataPicker();
                break;

            case R.id.button_start:
                Builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.start)
                        .setContentTitle("Сповіщення")
                        .setContentText(textView.getText())
                        .setAutoCancel(true)
                        .build();

                NotificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationManager.notify(ID_NOTIFICATION, Builder);
                break;

            case R.id.button_stop:
                if (NotificationManager != null) NotificationManager.cancel(ID_NOTIFICATION);
                break;
        }
    }

    private void showDataPicker() {

        calendar = Calendar.getInstance();
        MyYear = calendar.get(Calendar.YEAR);
        MyMonth = calendar.get(Calendar.MONTH);
        MyDay = calendar.get(Calendar.DAY_OF_MONTH);

        MyDatePicker = new DatePickerDialog(
                this,
                getDateSetListener(),
                MyYear,
                MyMonth,
                MyDay
        );

        MyDatePicker.getDatePicker().setMinDate(calendar.getTimeInMillis() - 86400000);
        MyDatePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis() + 86400000);
        MyDatePicker.show();
    }

    private DatePickerDialog.OnDateSetListener getDateSetListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                MyYear = year;
                MyMonth = month;
                MyDay = dayOfMonth;
                if (textView != null) {
                    textView.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "Вибрана дата: %d %d %d",
                                    MyYear,
                                    MyMonth,
                                    MyDay
                            )
                    );
                }
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Unbinder.unbind();
        if (MyDatePicker != null) {
            MyDatePicker.dismiss();
            MyDatePicker = null;
        }
    }
}
