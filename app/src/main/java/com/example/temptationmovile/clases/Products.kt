package com.example.temptationmovile.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Products {
    @SerializedName("idproduc")
    @Expose
    var idproduc: Int = 0;

    @SerializedName("idcat")
    @Expose
    var idcat: Int = 0;

    @SerializedName("idsize")
    @Expose
    var idsize: Int = 0;

    @SerializedName("idstyles")
    @Expose
    var idstyles: Int = 0;

    @SerializedName("idbrand")
    @Expose
    var idbrand: Int = 0;

    @SerializedName("name_p")
    @Expose
    var name_p: String = ""

    @SerializedName("descriptopm")
    @Expose
    var description: String = ""

    @SerializedName("price")
    @Expose
    var price: Int = 0

    @SerializedName("stock")
    @Expose
    var stock: Int = 0

    @SerializedName("stock")
    @Expose
    var image_fornt: Int = 0

    @SerializedName("stock")
    @Expose
    var image_back: Int = 0

    var state: Int = 0;

}