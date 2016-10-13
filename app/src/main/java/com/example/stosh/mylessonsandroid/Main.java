package com.example.stosh.mylessonsandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

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

    private String message;
    private int counter = 0;
    private NotificationManager NotificationManager;
    private Notification Builder;
    private Unbinder Unbinder;
    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Unbinder = ButterKnife.bind(this);
        createSpinner();
    }

    @OnClick({R.id.button_setMassage, R.id.button_start, R.id.button_stop})
    public void onButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.button_setMassage:
                if (TextUtils.isEmpty(editText.getText().toString())) return;
                message = editText.getText().toString();
                break;
            case R.id.button_start:
                if (message != null && counter != 0) {
                    myTimer = new Timer();
                    final Handler uiHandler = new Handler();
                    myTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    createNotification();
                                }
                            });
                        }
                    }, 0L, 5L * 1000);
                    break;
                } else if (message == null) {
                    Toast toastNoMessage = Toast.makeText(
                            getApplicationContext(),
                            "Ви не ввели повідомлення",
                            Toast.LENGTH_LONG
                    );
                    toastNoMessage.show();
                } else {
                    Toast toastNoCounter = Toast.makeText(
                            getApplicationContext(),
                            "Ви не ввели к-сть повторень",
                            Toast.LENGTH_LONG
                    );
                    toastNoCounter.show();
                }
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
        spinner.setPrompt("Виберіть число повторень");
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                counter = position + 1;
                textView.setText("Лічильник = " + counter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void createNotification() {
        textView.setText("Лічильник = " + counter);
        Builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.start)
                .setContentTitle("Сповіщення")
                .setContentText(message + " " + counter)
                .setAutoCancel(true)
                .build();

        NotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManager.notify(ID_NOTIFICATION, Builder);
        counter--;
        if (counter == -1) {
            NotificationManager.cancel(ID_NOTIFICATION);
            myTimer.cancel();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Unbinder.unbind();
    }
}

