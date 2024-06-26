package com.relateddigital.relateddigital_google.inapp.bannercarousel

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.gson.Gson
import com.relateddigital.relateddigital_google.network.requestHandler.BannerCarouselActionRequest
import com.relateddigital.relateddigital_google.RelatedDigital
import com.relateddigital.relateddigital_google.constants.Constants
import com.relateddigital.relateddigital_google.inapp.VisilabsCallback
import com.relateddigital.relateddigital_google.inapp.VisilabsResponse
import com.relateddigital.relateddigital_google.model.AppBanner
import com.relateddigital.relateddigital_google.network.RequestHandler


class BannerRecyclerView : RecyclerView {
    private var mContext: Context = context
    var mBannerItemClickListener: BannerItemClickListener? = null
    var mBannerRequestListener: BannerRequestListener? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle)

    fun requestBannerCarouselAction(context: Context,
                                    properties: HashMap<String, String>?,
                                    bannerRequestListener: BannerRequestListener? = null,
                                    bannerItemClickListener: BannerItemClickListener? = null) {

        if (RelatedDigital.isBlocked(context)) {
            Log.e(LOG_TAG, "Too much server load, ignoring the request!")
            bannerRequestListener?.onRequestResult(false)
            return
        }

        if(RelatedDigital.getRelatedDigitalModel(context).getIsInAppNotificationEnabled()) {
            mBannerItemClickListener = bannerItemClickListener
            mBannerRequestListener = bannerRequestListener

            BannerCarouselActionRequest.createBannerCarouselActionRequest(
                mContext,
                getBannerCallback(context),
                properties
            )
        } else {
            Log.e(LOG_TAG, "In-app notification is not enabled." +
                        "Call RelatedDigital.setIsInAppNotificationEnabled() first")
            bannerRequestListener?.onRequestResult(false)
            return
        }
    }

    private fun getBannerCallback(context: Context): VisilabsCallback {
        return object : VisilabsCallback {
            override fun success(response: VisilabsResponse?) {
                mBannerRequestListener?.onRequestResult(true)
                try {
                    val bannerCarouselAdapter = BannerCarouselAdapter(context, mBannerItemClickListener)
                    layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                    bannerCarouselAdapter.setBannerList(this@BannerRecyclerView, Gson().fromJson(response!!.json.toString(), AppBanner::class.java))
                    setHasFixedSize(true)
                    adapter = bannerCarouselAdapter
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(this@BannerRecyclerView)
                    adapter?.notifyDataSetChanged()
                } catch (ex: Exception) {
                    Log.e(LOG_TAG, ex.message, ex)
                    mBannerRequestListener?.onRequestResult(false)
                }
            }

            override fun fail(response: VisilabsResponse?) {
                Log.e(LOG_TAG, response!!.rawResponse)
                mBannerRequestListener?.onRequestResult(false)
            }
        }
    }

    companion object {
        const val LOG_TAG = "BannerRecyclerView"
    }
}