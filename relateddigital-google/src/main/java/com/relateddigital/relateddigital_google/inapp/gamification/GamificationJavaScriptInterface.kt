package com.relateddigital.relateddigital_google.inapp.gamification

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.relateddigital.relateddigital_google.inapp.slotmachine.SlotMachineCompleteInterface
import com.relateddigital.relateddigital_google.inapp.slotmachine.SlotMachineCopyToClipboardInterface
import com.relateddigital.relateddigital_google.inapp.slotmachine.SlotMachineShowCodeInterface
import com.relateddigital.relateddigital_google.inapp.slotmachine.SlotMachineWebDialogFragment
import com.relateddigital.relateddigital_google.model.MailSubReport
import com.relateddigital.relateddigital_google.model.SlotMachine
import com.relateddigital.relateddigital_google.network.requestHandler.InAppActionClickRequest
import com.relateddigital.relateddigital_google.network.requestHandler.SubsJsonRequest

class GamificationJavaScriptInterface internal constructor(webViewDialogFragment: SlotMachineWebDialogFragment,
                                                           @get:JavascriptInterface val response: String)  {
    var mWebViewDialogFragment: SlotMachineWebDialogFragment = webViewDialogFragment
    private val mMainHandler = Handler(Looper.getMainLooper())
    private var mListener: SlotMachineCompleteInterface? = null
    private var mCopyToClipboardInterface: SlotMachineCopyToClipboardInterface? = null
    private var mShowCodeInterface: SlotMachineShowCodeInterface? = null
    private val jackpotModel: SlotMachine = Gson().fromJson(this.response, SlotMachine::class.java)

    private var subEmail = ""

    /**
     * This method closes JackpotActivity
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
            val actionData = jackpotModel.actiondata ?: return
            SubsJsonRequest.createSubsJsonRequest(context, actionData.type.orEmpty(),
                jackpotModel.actid.toString(), actionData.auth.orEmpty(),
                email)
        } else {
            Log.e("Jackpot : ", "Email entered is not valid!")
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
            report.impression = jackpotModel.actiondata?.report?.impression
            report.click = jackpotModel.actiondata?.report?.click
        } catch (e: Exception) {
            Log.e("Jackpot : ", "There is no report to send!")
            e.printStackTrace()
            report = null
        }
        val context = mWebViewDialogFragment.context
        if (report != null && context != null) {
            InAppActionClickRequest.createInAppActionClickRequest(context, report)
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

    fun setJackpotListeners(
        listener: SlotMachineCompleteInterface,
        copyToClipboardInterface: SlotMachineCopyToClipboardInterface,
        showCodeInterface: SlotMachineShowCodeInterface
    ) {
        mListener = listener
        mCopyToClipboardInterface = copyToClipboardInterface
        mShowCodeInterface = showCodeInterface
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