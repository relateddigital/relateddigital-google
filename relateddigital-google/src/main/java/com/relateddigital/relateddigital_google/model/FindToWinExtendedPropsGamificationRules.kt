package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class FindToWinExtendedPropsGamificationRules: Serializable {
    @SerializedName("button_color")
    var buttonColor: String? = null

    @SerializedName("button_text_color")
    var buttonTextColor: String? = null

    @SerializedName("button_text_size")
    var buttonTextSize: String? = null
}