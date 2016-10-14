package com.example.stosh.mylessonsandroid;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main extends AppCompatActivity {

    public static final int ID_NOTIFICATION = 20;

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.spinner)
    Spinner spinner;

    private int counter = 1;
    private NotificationManager NotificationManager;
    private Notification Builder;
    private AlarmManager alarm;
    private Unbinder Unbinder;
    private Intent intent;
    private PendingIntent pIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Unbinder = ButterKnife.bind(this);
        createSpinner();
    }

    @OnClick({R.id.button_start, R.id.button_stop})
    public void onButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.button_start:

                intent = new Intent(this, CreateNotificationAlert.class);

                intent.setAction("action");
                intent.putExtra("extra", "extra");

                pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

                Log.d("Log", "start");
                alarm.set(AlarmManager.RTC, System.currentTimeMillis() + 4000, pIntent);

                break;
            case R.id.button_stop:
                if (NotificationManager != null) NotificationManager.cancel(ID_NOTIFICATION);
                counter = 0;
                textView.setText("Лічильник = " + counter);
                break;
        }
    }


    private void createSpinner() {
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.index,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setPrompt("Виберіть кількість повторень");
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position,
                                       long id) {
                counter = position + 1;
                textView.setText("Лічильник = " + counter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void  CreateNotification() {
        Builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.start)
                .setContentTitle("Сповіщення")
                .setContentText(editText.getText().toString() + " " + counter)
                .setAutoCancel(true)

                .build();


        NotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManager.notify(ID_NOTIFICATION, Builder);

        counter--;
        textView.setText("Лічильник = " + counter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Unbinder.unbind();
    }
}

