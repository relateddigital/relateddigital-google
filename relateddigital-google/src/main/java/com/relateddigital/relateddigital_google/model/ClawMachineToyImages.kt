package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ClawMachineToyImages : Serializable {

    @SerializedName("image"      ) var image      : String? = null
    @SerializedName("staticcode" ) var staticcode : String? = null
}