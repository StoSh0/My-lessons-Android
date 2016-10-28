package com.stosh.vk_answering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main extends AppCompatActivity {

    private Unbinder Unbinder;
    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKSdk.login(this, scope);
        Unbinder = ButterKnife.bind(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Log.d("Auteh_Chec", "Ok");
            }

            @Override
            public void onError(VKError error) {
                Log.d("Auteh_Chec", "Error");
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick({R.id.button_start})
    public void onButtonClick() {

        Intent intentServiceNotify = new Intent(this, MessageNotify.class);
        startService(intentServiceNotify);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Unbinder.unbind();
    }
}

