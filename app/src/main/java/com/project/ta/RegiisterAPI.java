package com.project.ta;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegiisterAPI {
    @FormUrlEncoded
    @POST("register")
    Call<RegisterValue> register(
            @Field("name") String name_l,
            @Field("email") String email_l,
            @Field("password") String password_l
    );
}
