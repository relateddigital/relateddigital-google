package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChooseFavoriteExtendedPropsGameResultElements : Serializable {
    @SerializedName("title_text_color" ) var titleTextColor : String? = null
    @SerializedName("title_text_size"  ) var titleTextSize  : String? = null
    @SerializedName("text_color"       ) var textColor      : String? = null
    @SerializedName("text_size"        ) var textSize       : String? = null
}