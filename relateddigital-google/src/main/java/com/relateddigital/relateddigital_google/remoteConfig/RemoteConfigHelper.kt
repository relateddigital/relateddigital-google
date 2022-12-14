package com.relateddigital.relateddigital_google.remoteConfig

import android.content.Context
import android.util.Log
import com.relateddigital.relateddigital_google.RelatedDigital
import com.relateddigital.relateddigital_google.api.ApiMethods
import com.relateddigital.relateddigital_google.api.RemoteConfigApiClient
import com.relateddigital.relateddigital_google.constants.Constants
import com.relateddigital.relateddigital_google.util.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*


object RemoteConfigHelper {
    private const val LOG_TAG = "RemoteConfigHelper"
    fun checkRemoteConfigs(context: Context) {
        if (RemoteConfigApiClient.client != null) {
            val remoteConfigApiInterface: ApiMethods = RemoteConfigApiClient.client!!.create(ApiMethods::class.java)
            val headers = HashMap<String, String>()
            headers[Constants.USER_AGENT_REQUEST_KEY] = RelatedDigital.getRelatedDigitalModel(context).getUserAgent()
            val call: Call<List<String>> = remoteConfigApiInterface.getRemoteConfig(headers)
            call.enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    try {
                        Log.i(LOG_TAG, "Successful Request : " + response.raw().request.url.toString())
                        val profileIds = response.body()
                        var isMatch = false
                        if (!profileIds.isNullOrEmpty()) {
                            for (i in profileIds.indices) {
                                if (RelatedDigital.getRelatedDigitalModel(context)
                                        .getProfileId() == profileIds[i]
                                ) {
                                    isMatch = true
                                    setBlockState(context, true)
                                    break
                                }
                            }
                            if (!isMatch) {
                                setBlockState(context, false)
                            }
                        } else {
                            setBlockState(context, false)
                        }
                    } catch (e: Exception) {
                        Log.i(LOG_TAG, "Could not parse the response!")
                        setBlockState(context, false)
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.i(LOG_TAG, "Fail Request : " + call.request().url.toString())
                    setBlockState(context, false)
                }
            })
        }
    }

    fun setBlockState(context: Context?, isBlock: Boolean) {
        if (isBlock) {
            SharedPref.writeString(context!!, Constants.REMOTE_CONFIG_BLOCK_PREF_KEY, "t")
        } else {
            SharedPref.writeString(context!!, Constants.REMOTE_CONFIG_BLOCK_PREF_KEY, "f")
        }
    }
}