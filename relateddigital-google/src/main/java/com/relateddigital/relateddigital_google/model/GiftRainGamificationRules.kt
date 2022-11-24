package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GiftRainGamificationRules: Serializable {
    @SerializedName("background_image")
    var backgroundImage: String? = null

    @SerializedName("button_label")
    var buttonLabel: String? = null
}