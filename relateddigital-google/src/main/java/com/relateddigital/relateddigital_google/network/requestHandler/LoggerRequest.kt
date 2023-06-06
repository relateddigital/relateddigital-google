package com.relateddigital.relateddigital_google.network.requestHandler

import android.content.Context
import android.util.Log
import com.relateddigital.relateddigital_google.RelatedDigital
import com.relateddigital.relateddigital_google.model.Domain
import com.relateddigital.relateddigital_google.model.RelatedDigitalModel
import com.relateddigital.relateddigital_google.model.Request
import com.relateddigital.relateddigital_google.network.RequestFormer
import com.relateddigital.relateddigital_google.network.RequestSender
import java.util.HashMap

object LoggerRequest {
    private const val LOG_TAG = "LoggerRequest"


    fun createLoggerRequest(
        context: Context, model: RelatedDigitalModel, pageName: String,
        properties: HashMap<String, String>?
    ) {
        if (RelatedDigital.isBlocked(context)) {
            Log.w(LOG_TAG, "Too much server load, ignoring the request!")
            return
        }

        val queryMap = HashMap<String, String>()
        val headerMap = HashMap<String, String>()
        RequestFormer.formLoggerRequest(
            context = context,
            model = model,
            pageName = pageName,
            properties = properties,
            queryMap = queryMap,
            headerMap = headerMap
        )

        RequestSender.addToQueue(
            Request(Domain.LOG_LOGGER, queryMap, headerMap, null),
            model,
            context
        )
        RequestSender.addToQueue(
            Request(Domain.LOG_REAL_TIME, queryMap, headerMap, null),
            model,
            context
        )
    }
}