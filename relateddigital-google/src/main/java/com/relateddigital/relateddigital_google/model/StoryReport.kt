package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StoryReport : Serializable {
    @SerializedName("impression")
    var impression: String? = null

    @SerializedName("click")
    var click: String? = null
}