package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName

class AppRatingReport {
    @SerializedName("impression" ) var impression : String? = null
    @SerializedName("click"      ) var click      : String? = null

}