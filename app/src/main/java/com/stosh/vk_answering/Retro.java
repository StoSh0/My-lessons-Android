package com.stosh.vk_answering;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by StoSh on 29-Oct-16.
 */

public interface Retro {
    @FormUrlEncoded
    @POST("method")
    Call<ResponseBody> messageGetLongPollServer(@Field("method")String method, @FieldMap Map<String, String> map);
}
