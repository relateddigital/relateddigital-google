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

object StoryActionRequest {
    private const val LOG_TAG = "StoryActionRequest"


    fun createStoryActionRequest(
        context: Context,
        visilabsCallback: VisilabsCallback,
        properties: HashMap<String, String>?
    ) {
        if (RelatedDigital.isBlocked(context)) {
            Log.w(LOG_TAG, "Too much server load, ignoring the request!")
            return
        }

        RequestFormer.updateSessionParameters(context, Constants.STORY_ACTION_TYPE_VAL)

        val queryMap = HashMap<String, String>()
        val headerMap = HashMap<String, String>()
        RequestFormer.formStoryActionRequest(
            context = context,
            model = RelatedDigital.getRelatedDigitalModel(context),
            pageName = Constants.PAGE_NAME_REQUEST_VAL,
            properties = properties,
            queryMap = queryMap,
            headerMap = headerMap
        )

        RequestSender.addToQueue(
            Request(
                Domain.IN_APP_STORY_MOBILE, queryMap, headerMap,
                null, visilabsCallback
            ), RelatedDigital.getRelatedDigitalModel(context), context
        )
    }
}