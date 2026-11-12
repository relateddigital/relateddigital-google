package com.relateddigital.relateddigital_google.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.relateddigital.relateddigital_google.RelatedDigital

class NotificationActionBroadcastReceiver() : BroadcastReceiver() {
    private var callback: NotificationActionListener? = null

    constructor(callback: NotificationActionListener) : this() {
        this.callback = callback
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        var linkUri= ""
        if (intent?.action=="ACTION_CLICK"){
             linkUri = intent.getStringExtra("KEY_ACTION_ITEM") ?: ""
        }

        (callback ?: RelatedDigital.actionButtonCallback)?.onNotificationActionClicked(linkUri)
    }
}
