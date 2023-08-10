package com.relateddigital.relateddigital_google.inapp.gamification

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
    private lateinit var mListener: SlotMachineCompleteInterface
    private lateinit var mCopyToClipboardInterface: SlotMachineCopyToClipboardInterface
    private lateinit var mShowCodeInterface: SlotMachineShowCodeInterface
    private val jackpotModel: SlotMachine = Gson().fromJson(this.response, SlotMachine::class.java)

    private var subEmail = ""

    /**
     * This method closes JackpotActivity
     */
    @JavascriptInterface
    fun close() {
        mWebViewDialogFragment.dismiss()
        mListener.onCompleted()
    }

    /**
     * This method copies the coupon code to clipboard
     * and ends the activity
     *
     * @param couponCode - String: coupon code
     */
    @JavascriptInterface
    fun copyToClipboard(couponCode: String?, link: String?) {
        mWebViewDialogFragment.dismiss()
        mCopyToClipboardInterface.copyToClipboard(couponCode, link)
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
            SubsJsonRequest.createSubsJsonRequest(mWebViewDialogFragment.requireContext(), jackpotModel.actiondata!!.type!!,
                jackpotModel.actid.toString(), jackpotModel.actiondata!!.auth!!,
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
            report.impression = jackpotModel.actiondata!!.report!!.impression
            report.click = jackpotModel.actiondata!!.report!!.click
        } catch (e: Exception) {
            Log.e("Jackpot : ", "There is no report to send!")
            e.printStackTrace()
            report = null
        }
        if (report != null) {
            InAppActionClickRequest.createInAppActionClickRequest(mWebViewDialogFragment.requireContext(), report)
        }
    }



    /**
     * This method saves the promotion code shown
     */
    @JavascriptInterface
    fun saveCodeGotten(code: String, email: String?, report: String?) {
        mShowCodeInterface.onCodeShown(code)
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
}