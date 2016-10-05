package com.example.stosh.mylessonsandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.buttonOk)
    public void onButtonClick() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            textView.setText(R.string.edit_text_is_null);
            return;
        }
        textView.setText(editText.getText().toString());
    }
}
