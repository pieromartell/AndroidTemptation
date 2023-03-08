package com.example.temptationmovile.remoto

import com.example.temptationmovile.servicios.BrandService
import com.example.temptationmovile.servicios.PersonService

object ApiUtil {
    val API_URL = "http://192.168.1.36:3000/"

    val brandservice: BrandService?
        get() = RetrofitClient.getClient(API_URL)?.create(BrandService::class.java)

    val personService: PersonService?
        get() = RetrofitClient.getClient(API_URL)?.create(PersonService::class.java)
}