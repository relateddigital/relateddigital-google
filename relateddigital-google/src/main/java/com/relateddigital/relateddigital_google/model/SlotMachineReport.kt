package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SlotMachineReport  : Serializable {
    @SerializedName("impression")
    var impression: String? = null

    @SerializedName("click")
}
