package com.example.temptationmovile.remoto

import com.example.temptationmovile.servicios.BrandService
import com.example.temptationmovile.servicios.CategoryService
import com.example.temptationmovile.servicios.ColorService
import com.example.temptationmovile.servicios.PersonService
import com.example.temptationmovile.servicios.ProviderService

object ApiUtil {
    val API_URL = "http://192.168.18.4:3000/"

    val brandservice: BrandService?
        get() = RetrofitClient.getClient(API_URL)?.create(BrandService::class.java)

    val colorservice: ColorService?
        get() = RetrofitClient.getClient(API_URL)?.create(ColorService::class.java)

    val personService: PersonService?
        get() = RetrofitClient.getClient(API_URL)?.create(PersonService::class.java)

    val categoryService: CategoryService?
        get() = RetrofitClient.getClient(API_URL)?.create(CategoryService::class.java)

    val providerService: ProviderService?
        get() = RetrofitClient.getClient(API_URL)?.create(ProviderService::class.java)

}