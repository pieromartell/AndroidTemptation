package com.example.temptationmovile.remoto

import com.example.temptationmovile.servicios.*

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

    val styleService:StyleService?
        get() = RetrofitClient.getClient(API_URL)?.create(StyleService::class.java)

}