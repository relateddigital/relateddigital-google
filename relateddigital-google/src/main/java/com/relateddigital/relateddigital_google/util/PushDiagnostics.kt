package com.relateddigital.relateddigital_google.util

import android.content.Context
import android.os.Build
import android.util.Log
import com.relateddigital.relateddigital_google.constants.Constants
import com.relateddigital.relateddigital_google.model.RelatedDigitalModel

/**
 * Single place that reports why a push subscription was or was not delivered to Euromessage.
 *
 * Every message is prefixed with [LOG_TAG] so an integrator can capture the whole push lifecycle
 * with `adb logcat -s RDPush`, and every blocking condition names the API that has to be called
 * to resolve it.
 */
object PushDiagnostics {
    const val LOG_TAG = "RDPush"

    /**
     * Tokens are credentials, so only enough of one is logged to tell two tokens apart.
     */
    @JvmStatic
    fun maskToken(token: String?): String {
        return when {
            token == null -> "<null>"
            token.isEmpty() -> "<empty>"
            token.length <= 12 -> "<len=${token.length}>"
            else -> "${token.take(8)}...${token.takeLast(4)} <len=${token.length}>"
        }
    }

    @JvmStatic
    fun describeState(context: Context, model: RelatedDigitalModel): String {
        val osPermission = AppUtils.getNotificationPermissionStatus(context)
        val storedPermission = model.getPushPermissionStatus()
        val pushPermit = model.getExtra()[Constants.PUSH_PERMIT_KEY]?.toString() ?: "<not set>"
        val lastSubsDate = SharedPref.readString(context, Constants.LAST_SUBS_DATE_KEY)

        return buildString {
            append("token=").append(maskToken(model.getToken()))
            append(", appKey=").append(nullSafe(model.getGoogleAppAlias()))
            append(", pushPermit=").append(pushPermit)
            append(", osNotificationPermission=").append(osPermission)
            append(", storedPushPermissionStatus=").append(storedPermission)
            if (osPermission != storedPermission) {
                append(" (STALE)")
            }
            append(", isPushNotificationEnabled=").append(model.getIsPushNotificationEnabled())
            append(", apiLevel=").append(Build.VERSION.SDK_INT)
            append(", lastSubscriptionSentAt=")
                .append(if (lastSubsDate.isEmpty()) "<never>" else lastSubsDate)
        }
    }

    /**
     * Logged right before a subscription request leaves the device.
     */
    @JvmStatic
    fun logSending(context: Context, model: RelatedDigitalModel) {
        Log.i(LOG_TAG, "Sending subscription -> ${describeState(context, model)}")
    }

    /**
     * Logged when the request was never attempted. [howToFix] must tell the integrator what to do.
     */
    @JvmStatic
    fun logBlocked(context: Context, model: RelatedDigitalModel, reason: String, howToFix: String) {
        Log.e(
            LOG_TAG,
            "Subscription NOT sent. Reason: $reason | How to fix: $howToFix | " +
                    "State: ${describeState(context, model)}"
        )
    }

    /**
     * Logged when the request was intentionally suppressed because the server already has this
     * exact state. This is not an error, but integrators mistake it for one without an explanation.
     */
    @JvmStatic
    fun logSkipped(context: Context, model: RelatedDigitalModel, reason: String) {
        Log.i(
            LOG_TAG,
            "Subscription skipped (server already has this state). Reason: $reason | " +
                    "State: ${describeState(context, model)}"
        )
    }

    @JvmStatic
    fun logSuccess(context: Context, model: RelatedDigitalModel) {
        Log.i(LOG_TAG, "Subscription accepted by server -> ${describeState(context, model)}")
    }

    @JvmStatic
    fun logFailure(
        context: Context,
        model: RelatedDigitalModel,
        httpCode: Int?,
        errorMessage: String?
    ) {
        Log.e(
            LOG_TAG,
            "Subscription rejected by server. httpCode=${httpCode ?: "<none>"}, " +
                    "error=${errorMessage ?: "<none>"} | State: ${describeState(context, model)}"
        )
    }

    private fun nullSafe(value: String?): String {
        return if (value.isNullOrEmpty()) "<empty>" else value
    }
}
