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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main extends AppCompatActivity {

	public static final int ID_NOTIFICATION = 20;

	@BindView(R.id.textView)
	TextView textView;

	private NotificationManager mNotificationManager;
	private Notification mBuilder;
	private DatePickerDialog mMyDatePicker;
	private DatePickerDialog.OnDateSetListener mDateSetListener;
	private Unbinder mUnbinder;
	private int
			mMyYear = 2016,
			mMyMonth = 10,
			mMyDay = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mUnbinder = ButterKnife.bind(this);
	}

	@OnClick({R.id.button_call_time, R.id.button_start, R.id.button_stop})
	public void onButtonClick(Button button) {
		switch (button.getId()) {
			case R.id.button_call_time:
				showDataPicker();
				break;

			case R.id.button_start:
				/*Intent resultIntent = new Intent(this, Main.class);
				PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        this, 0*//* unique pending id *//*,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );*/


				//?
                /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(Main.class);
                stackBuilder.addNextIntent(resultIntent);

                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(resultPendingIntent);*/

				mBuilder = new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.start)
						.setContentTitle("Сповіщення")
						.setContentText(textView.getText())
						.setAutoCancel(true)
//                        .setContentIntent(resultPendingIntent)
						.build();

				mNotificationManager = (NotificationManager)
						getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(ID_NOTIFICATION, mBuilder);
				break;

			case R.id.button_stop:
				//NPE fix
				if (mNotificationManager != null) mNotificationManager.cancel(ID_NOTIFICATION);
				break;
		}
	}

	private void showDataPicker() {
		mMyDatePicker = new DatePickerDialog(
				this,
				getDateSetListener(),
				mMyYear,
				mMyMonth,
				mMyDay
		);
		mMyDatePicker.show();
	}

	private DatePickerDialog.OnDateSetListener getDateSetListener() {
		return new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				mMyYear = year;
				mMyMonth = month;
				mMyDay = dayOfMonth;
//                textView.setText("Вибрана дата:" + mMyYear + "." + mMyMonth + "." + mMyDay);
				if (textView != null) {
					textView.setText(
							String.format(
									Locale.getDefault(),
									"Вибрана дата: %d %d %d",
									mMyYear,
									mMyMonth,
									mMyDay
							)
					);
				}
			}
		};
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mUnbinder.unbind();
		if (mMyDatePicker != null) {
			mMyDatePicker.dismiss();
			mMyDatePicker = null;
		}
	}
}
