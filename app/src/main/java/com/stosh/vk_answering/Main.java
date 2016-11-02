package com.stosh.vk_answering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main extends AppCompatActivity {

    private final int ID_START_SERVICE = 1;

    private Intent intentServiceNotify;
    private Unbinder Unbinder;
    private String[] scope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.MESSAGES,
            VKScope.DOCS,
    };

    @BindView(R.id.button_login)
    Button button_login;
    @BindView(R.id.button_logout)
    Button button_logout;
    @BindView(R.id.button_start_service)
    Button button_start_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.button_login, R.id.button_logout, R.id.button_start_service})
    public void onButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.button_login:
                VKSdk.login(this, scope);
                break;

            case R.id.button_logout:
                VKSdk.logout();

                button_login.setVisibility(View.VISIBLE);
                button_logout.setVisibility(View.GONE);
                button_start_service.setVisibility(View.GONE);
                if (intentServiceNotify != null) stopService(intentServiceNotify);
                break;

            case R.id.button_start_service:
                intentServiceNotify = new Intent(this, MessageNotify.class);
                intentServiceNotify.putExtra("Start_Service", ID_START_SERVICE);
                startService(intentServiceNotify);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                button_login.setVisibility(View.GONE);
                button_logout.setVisibility(View.VISIBLE);
                button_start_service.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(Main.this, "Помилка авторизації", Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Unbinder.unbind();
    }

}

