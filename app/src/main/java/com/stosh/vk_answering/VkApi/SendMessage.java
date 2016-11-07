package com.stosh.vk_answering.VkApi;

import android.util.Log;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by StoSh on 07-Nov-16.
 */

public class SendMessage {

    public void sendMessage(String id) {
        VKRequest request = new VKRequest("messages.send",
                VKParameters.from(
                        "user_id",
                        id,
                        "message",
                        "Test Message")
        );
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("Service", "Message" + response);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("Service", "MessageError" + error);
            }
        });
    }
}
