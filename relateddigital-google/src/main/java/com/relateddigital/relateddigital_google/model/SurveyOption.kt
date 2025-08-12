package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SurveyOption(
    @SerializedName("option_text")
    var optionText: String? = null
) : Serializable