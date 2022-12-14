package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class FavsActionData : Serializable {
    @SerializedName("favorites")
    var favorites: Favorites? = null

    @SerializedName("attributes")
    var attributes: Array<String?>? = null

    override fun toString(): String {
        return "Actiondata [favorites = $favorites, attributes = " + Arrays.toString(
                attributes
        ) + "]"
    }
}