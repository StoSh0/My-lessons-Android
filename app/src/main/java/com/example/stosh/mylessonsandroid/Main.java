package com.example.stosh.mylessonsandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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

    private NotificationManager NotificationManager;
    private Notification Builder;
    private Unbinder Unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.button_start, R.id.button_stop})
    public void onButtonClick(Button button) {
        switch (button.getId()) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Unbinder.unbind();
    }
}

