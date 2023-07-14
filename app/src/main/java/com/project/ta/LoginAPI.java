package com.project.ta;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginAPI {
    @FormUrlEncoded
    @POST("login")
    Call<LoginValue> login(
            @Field("email") String email_l,
            @Field("password") String password_l
    );
}
