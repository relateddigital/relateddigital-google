package com.relateddigital.relateddigital_google.push

interface EuromessageCallback {
    fun success()
    fun fail(errorMessage: String?)
}