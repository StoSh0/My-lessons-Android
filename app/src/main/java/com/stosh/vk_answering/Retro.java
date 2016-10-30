package com.stosh.vk_answering;

import java.util.Map;

import retrofit.Call;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by StoSh on 29-Oct-16.
 */

public interface Retro {
    @FormUrlEncoded
    @POST("method/user.get")
    Call translate (@FieldMap Map<String, String> map);
}
