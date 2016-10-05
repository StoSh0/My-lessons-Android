package com.example.stosh.mylessonsandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

    }

    @OnClick({R.id.button_call_time_picker, R.id.button_start, R.id.button_stop})
    public void onButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.button_call_time_picker:
                textView.setText("button_call_time_picker");
                break;
            case R.id.button_start:
                textView.setText("button_call_time_start");
                break;
            case R.id.button_stop:
                textView.setText("button_call_time_stop");
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
