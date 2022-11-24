package com.relateddigital.relateddigital_google.push

import com.relateddigital.relateddigital_google.model.Message

interface PushMessageInterface {
    fun success(pushMessages: List<Message>)
    fun fail(errorMessage: String)
}