package com.relateddigital.relateddigital_google.network.requestHandler

import android.app.Activity
import android.content.Context
import android.util.Log
import com.relateddigital.relateddigital_google.RelatedDigital

import com.relateddigital.relateddigital_google.model.Domain
import com.relateddigital.relateddigital_google.model.RelatedDigitalModel
import com.relateddigital.relateddigital_google.model.Request
import com.relateddigital.relateddigital_google.network.RequestFormer
import com.relateddigital.relateddigital_google.network.RequestSender
import java.util.HashMap

object InAppActionRequest {
    private const val LOG_TAG = "InAppActionRequest"

    fun createInAppActionRequest(
        context: Context, model: RelatedDigitalModel, pageName: String,
        properties: HashMap<String, String>?, parent: Activity? = null
    ) {
        if (RelatedDigital.isBlocked(context)) {
            Log.w(LOG_TAG, "Too much server load, ignoring the request!")
            return
        }

        val queryMap = HashMap<String, String>()
        val headerMap = HashMap<String, String>()
        RequestFormer.formInAppActionRequest(
            context = context,
            model = model,
            pageName = pageName,
            properties = properties,
            queryMap = queryMap,
            headerMap = headerMap
        )

        RequestSender.addToQueue(
            Request(Domain.IN_APP_ACTION_MOBILE, queryMap, headerMap, parent),
            model,
            context
        )
    }
}