package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class GiftBoxGameElements : Serializable {
    @SerializedName("gift_boxes" ) var giftBoxes : ArrayList<GiftBoxGiftBoxes> = arrayListOf()
}