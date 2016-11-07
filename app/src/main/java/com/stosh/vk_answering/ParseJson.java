package com.stosh.vk_answering;

import android.util.Log;

import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by StoSh on 05-Nov-16.
 */

public class ParseJson {

    private String key, server, ts;

    public void ParseServer(VKResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(response.responseString);
            JSONObject responseSt = jsonObject.getJSONObject("response");
            key = responseSt.getString("key");
            server = responseSt.getString("server");
            ts = responseSt.getString("ts");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray parsePoll(Response<ResponseBody> response) {
        try {
            JSONObject jsonObject = new JSONObject(response.body().string());
            Log.d("Service", "3 " + jsonObject);
            ts = jsonObject.getString("ts");
            JSONArray jsonArray = jsonObject.getJSONArray("updates");
            Log.d("Service", " array = " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray array = jsonArray.getJSONArray(i);
                if (array.getString(0).equals("4")) {
                    return array;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTs() {
        return ts;
    }

    public String getKey() {
        return key;
    }

    public String getServer() {
        return server;
    }

}
