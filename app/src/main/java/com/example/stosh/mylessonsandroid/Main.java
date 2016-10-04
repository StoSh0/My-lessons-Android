package com.example.stosh.mylessonsandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    EditText editText;
    Button buttonOk;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        buttonOk = (Button) findViewById(R.id.buttonOk);
        textView =  (TextView)findViewById(R.id.textView);
    }

    public void clickButtonOk(View view){
        if (TextUtils.isEmpty(editText.getText().toString())) {
            textView.setText(R.string.edit_text_is_null);
            return;
        }

        textView.setText(editText.getText().toString());
    }
}
