package com.relateddigital.relateddigital_google.network.requestHandler

import android.content.Context
import android.os.Build
import android.util.Log
import com.relateddigital.relateddigital_google.constants.Constants
import com.relateddigital.relateddigital_google.model.RelatedDigitalModel
import com.relateddigital.relateddigital_google.network.RequestSender
import com.relateddigital.relateddigital_google.util.RetryCounterManager

object RegisterEmailRequest {
    private const val LOG_TAG = "RegisterEmailRequest"

    fun createRegisterEmailRequest(context: Context, registerEmailModel: RelatedDigitalModel) {
        if (Build.VERSION.SDK_INT < Constants.SDK_MIN_API_VERSION) {
            Log.e(LOG_TAG, "RelatedDigital SDK requires min API level 21!")
            return
        }

        if (registerEmailModel.getToken().isEmpty() || (registerEmailModel.getGoogleAppAlias().isEmpty() && registerEmailModel.getHuaweiAppAlias().isEmpty()) ) {
            Log.e(LOG_TAG, "token or appKey cannot be null!")
            return
        }

        RequestSender.sendSubscriptionRequest(context, registerEmailModel, RetryCounterManager.counterId, null)
    }
}