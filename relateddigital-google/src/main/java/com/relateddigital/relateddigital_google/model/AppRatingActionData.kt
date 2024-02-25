package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName

class AppRatingActionData {

    @SerializedName("value"  ) var value  : String? = null
    @SerializedName("report" ) var report : AppRatingReport? = null

}