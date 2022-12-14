package com.relateddigital.relateddigital_google.model

import android.content.Context
import android.os.Bundle
import com.relateddigital.relateddigital_google.util.LogUtils
import org.json.JSONArray
import org.json.JSONException
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap

class Message : Serializable {
    var date: String?
    var mediaUrl: String?
        private set
    var altUrl: String?
        private set
    var pushId: String?
        private set
    var campaignId: String?
        private set
    var url: String?
        private set
    var from: String?
        private set
    var message: String?
        private set
    var title: String?
        private set
    var sound: String?
        private set
    var emPushSp: String?
        private set
    var deliver: String?
        private set
    var silent: String?
        private set
    private var pushType: PushType? = null
    var collapseKey: String?
        private set
    private val params: MutableMap<String, String> = HashMap()
    private var elements: ArrayList<Element>? = null
    var loginID: String? = null

    constructor(context: Context, bundle: Map<String, String?>) {
        for (key in bundle.keys) {
            val value: Any? = bundle[key]
            if (value != null) {
                params[key] = value.toString()
            }
        }
        date = bundle["date"]
        mediaUrl = bundle["mediaUrl"]
        pushId = bundle["pushId"]
        url = bundle["url"]
        altUrl = bundle["altUrl"]
        from = bundle["from"]
        message = bundle["message"]
        title = bundle["title"]
        sound = bundle["sound"]
        if(sound == null) {
            sound = ""
        }
        emPushSp = bundle["emPushSp"]
        deliver = bundle["deliver"]
        silent = bundle["silent"]
        campaignId = bundle["cId"]
        pushType = if (bundle["pushType"] != null) {
            PushType.valueOf(bundle["pushType"]!!)
        } else {
            PushType.Text
        }
        collapseKey = bundle["collapse_key"]
        if (bundle["elements"] != null) {
            convertJsonStrToElementsArray(context, bundle["elements"])
        }
    }

    private fun convertJsonStrToElementsArray(context: Context, elementJsonStr: String?) {
        val jsonArr: JSONArray
        try {
            jsonArr = JSONArray(elementJsonStr)
            elements = ArrayList<Element>()
            for (i in 0 until jsonArr.length()) {
                val jsonObj = jsonArr.getJSONObject(i)
                val element = Element()
                element.id = jsonObj.getString("id")
                element.title = jsonObj.getString("title")
                element.content = jsonObj.getString("content")
                element.picture = jsonObj.getString("picture")
                element.url = jsonObj.getString("url")
                elements!!.add(element)
            }
        } catch (e: JSONException) {
            val element = Throwable().stackTrace[0]
            LogUtils.formGraylogModel(
                context,
                "e",
                "Converting JSON string to array list : " + e.message,
                element.className + "/" + element.methodName + "/" + element.lineNumber
            )
            e.printStackTrace()
        }
    }

    constructor(bundle: Bundle) {
        for (key in bundle.keySet()) {
            val value = bundle[key]
            params[key] = value.toString()
        }
        date = bundle.getString("date")
        mediaUrl = bundle.getString("mediaUrl")
        pushId = bundle.getString("pushId")
        url = bundle.getString("url")
        altUrl = bundle.getString("altUrl")
        from = bundle.getString("from")
        message = bundle.getString("message")
        title = bundle.getString("title")
        sound = bundle.getString("sound")
        if(sound == null) {
            sound = ""
        }
        emPushSp = bundle.getString("emPushSp")
        deliver = bundle.getString("deliver")
        silent = bundle.getString("silent")
        campaignId = bundle.getString("cId")
        pushType = if (bundle.getString("pushType") != null) {
            PushType.valueOf(bundle.getString("pushType")!!)
        } else {
            PushType.Text
        }
        collapseKey = bundle.getString("collapse_key")
        elements = bundle.getParcelable("elements")
    }

    fun getPushType(): PushType? {
        return pushType
    }

    fun getParams(): Map<String, String> {
        return params
    }

    fun getElements(): ArrayList<Element>? {
        return elements
    }
}