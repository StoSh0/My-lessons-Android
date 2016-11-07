package com.stosh.vk_answering.VkApi;

import com.stosh.vk_answering.ListenerVk;
import com.stosh.vk_answering.NotificationMessage;
import com.stosh.vk_answering.ParseJson;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by StoSh on 05-Nov-16.
 */

public class LongPollServer {

    private ParseJson parseJson;
    private ListenerVk listenerVk;
    private NotificationMessage notificationMessage;

    public LongPollServer(ListenerVk listenerVk, NotificationMessage notificationMessage) {
        parseJson = new ParseJson();
        this.listenerVk = listenerVk;
        this.notificationMessage = notificationMessage;
        getLongPollServer();
    }

    private void getLongPollServer(){
        VKRequest request = new VKRequest("messages.getLongPollServer",
                VKParameters.from(
                        "use_ssl",
                        "0",
                        "need_pts",
                        "0"
                )
        );
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                parseJson.ParseServer(response);
                new LongPoll(parseJson, listenerVk, notificationMessage);
            }
        });
    }
}

