package com.relateddigital.relateddigital_google.model

import java.io.Serializable

enum class PushType(private val nameStr: String) : Serializable {
    Text("Text"), Image("Image"), Carousel("Carousel"), Video("Video");

    fun equalsName(otherName: String): Boolean {
        return nameStr == otherName
    }

    override fun toString(): String {
        return nameStr
    }
}