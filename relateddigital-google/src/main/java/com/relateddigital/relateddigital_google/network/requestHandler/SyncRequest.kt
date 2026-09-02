package com.relateddigital.relateddigital_google.network.requestHandler

import android.content.Context
import android.os.Build
import android.util.Log
import com.relateddigital.relateddigital_google.RelatedDigital
import com.relateddigital.relateddigital_google.constants.Constants
import com.relateddigital.relateddigital_google.network.RequestSender
import com.relateddigital.relateddigital_google.push.EuromessageCallback
import com.relateddigital.relateddigital_google.util.PushDiagnostics
import com.relateddigital.relateddigital_google.util.RetryCounterManager

object SyncRequest {
    private const val LOG_TAG = "SyncRequest"

    fun createSyncRequest(context: Context, callback: EuromessageCallback? = null) {
        if (Build.VERSION.SDK_INT < Constants.SDK_MIN_API_VERSION) {
            Log.e(LOG_TAG, "RelatedDigital SDK requires min API level 21!")
            callback?.fail("RelatedDigital SDK requires min API level 21!")
            return
        }

        val model = RelatedDigital.getRelatedDigitalModel(context)

        val tokenMissing = model.getToken().isEmpty()
        val appAliasMissing =
            model.getGoogleAppAlias().isEmpty() && model.getHuaweiAppAlias().isEmpty()

        if (tokenMissing || appAliasMissing) {
            val reason = when {
                tokenMissing && appAliasMissing ->
                    "both the push token and the appAlias (appKey) are empty"
                tokenMissing -> "the push token is empty"
                else -> "the appAlias (appKey) is empty"
            }
            val howToFix = when {
                tokenMissing && appAliasMissing ->
                    "Call RelatedDigital.setIsPushNotificationEnabled(context, true, " +
                            "googleAppAlias, token) with a non-empty appAlias and a token " +
                            "obtained from FirebaseMessaging.getInstance().token."
                tokenMissing ->
                    "The FCM token has not been delivered yet. Request it with " +
                            "FirebaseMessaging.getInstance().token and pass the result to " +
                            "RelatedDigital.setIsPushNotificationEnabled(...). This is expected " +
                            "on the very first launch until the token arrives; the SDK syncs " +
                            "automatically from onNewToken once it does."
                else ->
                    "Pass the appAlias (appKey) given to you by Related Digital to " +
                            "RelatedDigital.setIsPushNotificationEnabled(...)."
            }
            PushDiagnostics.logBlocked(context, model, reason, howToFix)
            callback?.fail("Subscription not sent: $reason")
            return
        }

        if (model.isEqual(RelatedDigital.getPreviousModel())) {
            PushDiagnostics.logSkipped(
                context,
                model,
                "identical to the subscription already sent in this session"
            )
            return
        }

        if (model.isDuplicateOfLastAcceptedSubscription(context)) {
            PushDiagnostics.logSkipped(
                context,
                model,
                "identical to the subscription the server accepted at " +
                        "${model.getLastAcceptedSubscriptionDate(context)}; an unchanged " +
                        "subscription is re-sent at most every " +
                        "${Constants.SUBSCRIPTION_DEDUPE_DAYS} days"
            )
            return
        }

        // Guards against a second identical request while this one is in flight. RequestSender
        // clears it again if the request ultimately fails, so the next sync can retry.
        RelatedDigital.updatePreviousModel(context)

        PushDiagnostics.logSending(context, model)
        RequestSender.sendSubscriptionRequest(
            context,
            model,
            RetryCounterManager.counterId,
            callback
        )
    }
}
