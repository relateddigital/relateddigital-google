package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GiftBoxGameResultElements : Serializable {

    @SerializedName("image"   ) var image   : String? = null
    @SerializedName("title"   ) var title   : String? = null
    @SerializedName("message" ) var message : String? = null
}