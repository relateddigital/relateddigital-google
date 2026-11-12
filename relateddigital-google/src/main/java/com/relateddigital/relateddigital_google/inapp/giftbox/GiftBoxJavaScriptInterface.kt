package com.relateddigital.relateddigital_google.inapp.giftbox

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.relateddigital.relateddigital_google.model.GiftBox
import com.relateddigital.relateddigital_google.model.MailSubReport
import com.relateddigital.relateddigital_google.network.requestHandler.SubsJsonRequest
import com.relateddigital.relateddigital_google.network.RequestHandler
import com.relateddigital.relateddigital_google.network.requestHandler.InAppActionClickRequest

class GiftBoxJavaScriptInterface internal constructor(webViewDialogFragment: GiftBoxWebDialogFragment,
                                                      @get:JavascriptInterface val response: String) {
    var mWebViewDialogFragment: GiftBoxWebDialogFragment = webViewDialogFragment
    private val mMainHandler = Handler(Looper.getMainLooper())
    private var mListener: GiftBoxCompleteInterface? = null
    private var mCopyToClipboardInterface: GiftBoxCopyToClipboardInterface? = null
    private var mShowCodeInterface: GiftBoxShowCodeInterface? = null
    private val giftboxModel: GiftBox = Gson().fromJson(this.response, GiftBox::class.java)

    private var subEmail = ""

    /**
     * This method closes GiftBoxActivity
     */
    @JavascriptInterface
    fun close() {
        runOnMain {
            mWebViewDialogFragment.dismissAllowingStateLoss()
            mListener?.onCompleted()
        }
    }

    /**
     * This method copies the coupon code to clipboard
     * and ends the activity
     *
     * @param couponCode - String: coupon code
     */
    @JavascriptInterface
    fun copyToClipboard(couponCode: String?, link: String?) {
        runOnMain {
            mWebViewDialogFragment.dismissAllowingStateLoss()
            mCopyToClipboardInterface?.copyToClipboard(couponCode, link)
            sendReport()
        }
    }

    /**
     * This method sends a subscription request for the email entered
     *
     * @param email : String - the value entered for email
     */
    @JavascriptInterface
    fun subscribeEmail(email: String?) {
        if (!email.isNullOrEmpty()) {
            subEmail = email
            val context = mWebViewDialogFragment.context ?: return
            val actionData = giftboxModel.actiondata ?: return
            SubsJsonRequest.createSubsJsonRequest(context, actionData.type.orEmpty(),
                giftboxModel.actid.toString(), actionData.auth.orEmpty(),
                email)
        } else {
            Log.e("GiftBox : ", "Email entered is not valid!")
        }
    }

    /**
     * This method sends the report to the server
     */
    @JavascriptInterface
    fun sendReport() {
        var report: MailSubReport?
        try {
            report = MailSubReport()
            report.click = giftboxModel.actiondata?.report?.click
        } catch (e: Exception) {
            Log.e("GiftBox : ", "There is no report to send!")
            e.printStackTrace()
            report = null
        }
        val context = mWebViewDialogFragment.context
        if (report != null && context != null) {
            InAppActionClickRequest.createInAppActionClickRequest(context, report)
        }
    }

    @JavascriptInterface
    fun sendReportImpression() {
        var report: MailSubReport?
        try {
            report = MailSubReport()
            report.impression = giftboxModel.actiondata?.report?.impression
        } catch (e: Exception) {
            Log.e("GiftBox : ", "There is no impression report to send!")
            e.printStackTrace()
            report = null
        }
        val context = mWebViewDialogFragment.context
        if (report != null && context != null) {
            InAppActionClickRequest.createInAppActionImressionRequest(context, report)
        }
    }

    /**
     * This method saves the promotion code shown
     */
    @JavascriptInterface
    fun saveCodeGotten(code: String, email: String?, report: String?) {
        runOnMain {
            mShowCodeInterface?.onCodeShown(code)
        }
    }

    fun setGiftBoxListeners(
        listener: GiftBoxCompleteInterface,
        copyToClipboardInterface: GiftBoxCopyToClipboardInterface,
        showCodeInterface: GiftBoxShowCodeInterface
    ) {
        mListener = listener
        mCopyToClipboardInterface = copyToClipboardInterface
        mShowCodeInterface = showCodeInterface
        sendReportImpression()
    }

    private fun runOnMain(block: () -> Unit) {
        mMainHandler.post {
            if (!mWebViewDialogFragment.isAdded) {
                return@post
            }
            try {
                block()
            } catch (e: Exception) {
                Log.e("JavaScriptInterface", "Could not handle the web view call!", e)
            }
        }
    }
}