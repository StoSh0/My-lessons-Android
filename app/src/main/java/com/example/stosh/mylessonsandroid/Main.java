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

	private NotificationManager NotificationManager;
	private Notification Builder;
	private DatePickerDialog MyDatePicker;
	private Unbinder Unbinder;
	private int
			MyYear = 2016,
			MyMonth = 10,
			MyDay = 7;

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
				//NPE fix
				if (NotificationManager != null) NotificationManager.cancel(ID_NOTIFICATION);
				break;
		}
	}

	private void showDataPicker() {
		MyDatePicker = new DatePickerDialog(
				this,
				getDateSetListener(),
				MyYear,
				MyMonth,
				MyDay
		);
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
									"Вибрана дата: %y %m %d",
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
