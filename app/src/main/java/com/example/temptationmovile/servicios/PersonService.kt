package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.ResponseLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PersonService {
    @POST("login")
    fun login(@Body responseLogin: ResponseLogin?): Call<ResponseLogin>?
}