package com.relateddigital.relateddigital_android.network.requestHandler

import android.content.Context
import android.util.Log
import com.relateddigital.relateddigital_google.RelatedDigital
import com.relateddigital.relateddigital_google.constants.Constants
import com.relateddigital.relateddigital_google.inapp.VisilabsCallback
import com.relateddigital.relateddigital_google.model.Domain
import com.relateddigital.relateddigital_google.model.Request
import com.relateddigital.relateddigital_google.network.RequestFormer
import com.relateddigital.relateddigital_google.network.RequestSender
import java.util.HashMap

object NpsWithNumbersRequest {
    private const val LOG_TAG = "NpsWithNumbersRequest"


    fun createNpsWithNumbersRequest(
        context: Context, visilabsCallback: VisilabsCallback,
        properties: HashMap<String, String>?
    ) {
        if (RelatedDigital.isBlocked(context)) {
            Log.w(LOG_TAG, "Too much server load, ignoring the request!")
            return
        }

        val queryMap = HashMap<String, String>()
        val headerMap = HashMap<String, String>()
        RequestFormer.formNpsActionRequest(
            context = context,
            model = RelatedDigital.getRelatedDigitalModel(context),
            pageName = Constants.PAGE_NAME_REQUEST_VAL,
            properties = properties,
            queryMap = queryMap,
            headerMap = headerMap
        )

        RequestSender.addToQueue(
            Request(
                Domain.IN_APP_NOTIFICATION_ACT_JSON,
                queryMap,
                headerMap,null,visilabsCallback

            ), RelatedDigital.getRelatedDigitalModel(context), context
        )
    }

}