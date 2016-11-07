package com.stosh.vk_answering;

import android.util.Log;

import com.stosh.vk_answering.VkApi.SendMessage;

import java.util.TimerTask;

/**
 * Created by StoSh on 07-Nov-16.
 */

public class TimerMessage extends TimerTask {
    private String id;

    public TimerMessage(String id){
        this.id = id;
    }
    @Override
    public void run() {
        Log.d("Service", "TIMER");
        SendMessage sendMessage = new SendMessage();
        sendMessage.sendMessage(id);
    }
}
