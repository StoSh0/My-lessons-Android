package com.stosh.vk_answering.VkApi;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stosh.vk_answering.ListenerVk;
import com.stosh.vk_answering.NotificationMessage;
import com.stosh.vk_answering.ParseJson;
import com.stosh.vk_answering.Retro;
import com.stosh.vk_answering.TimerMessage;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by StoSh on 05-Nov-16.
 */

public class LongPoll {

    private String TAG = "Service";
    private String key, server;
    private NotificationMessage notificationMessage;
    private ParseJson parseJson;
    private ListenerVk listenerVk;
    private Timer timer;
    private TimerMessage timerMessage;

    public LongPoll(ParseJson parseJson, ListenerVk listenerVk, NotificationMessage notificationMessage) {
        key = parseJson.getKey();
        server = parseJson.getServer();
        this.parseJson = parseJson;
        this.listenerVk = listenerVk;
        this.notificationMessage = notificationMessage;
        getLongPoll();
    }

    public void getLongPoll() {
        String ts = parseJson.getTs();
        Log.d(TAG, "LongPoll");
        final Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://imv4.vk.com/")
                .build();

        Retro retro = retrofit.create(Retro.class);

        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("act", "a_check");
        paramMap.put("key", key);
        paramMap.put("ts", ts);
        paramMap.put("wait", "25");
        paramMap.put("mode", "2");
        paramMap.put("v", "1");

        String domain = server.substring(12);

        final Call<ResponseBody> call = retro.longPoll(domain, paramMap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONArray array = parseJson.parsePoll(response);
                if (array != null) {
                    try {
                        String key = array.getString(2);
                        String id = array.getString(3);
                        String textMSG = array.getString(6);
                        if (key.equals("19") || key.equals("51") || key.equals("3"))
                            notificationMessage.crateNotifyOutMessage(textMSG);
                        if (key.equals("17") || key.equals("49") || key.equals("1")) {
                            notificationMessage.createNotifyInMessage(textMSG);
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            timer = new Timer();
                            timerMessage = new TimerMessage(id);
                            timer.schedule(timerMessage, 10000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (listenerVk.checkRun == true) getLongPoll();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "5" + t);
                if (listenerVk.checkRun == true) getLongPoll();
            }
        });
    }
}
